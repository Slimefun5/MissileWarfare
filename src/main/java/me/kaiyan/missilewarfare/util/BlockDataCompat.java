package me.kaiyan.missilewarfare.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * Version-safe access to the modern {@code org.bukkit.block.data.BlockData}
 * API (1.13+). All references to those types live here as reflective
 * {@code Class.forName} lookups so that classes loaded at plugin enable never
 * carry {@code org.bukkit.block.data.*} in their bytecode, which would fail the
 * JVM verifier on 1.8.8 where those classes are absent.
 *
 * <p>On legacy versions every method degrades gracefully to a no-op or a
 * best-effort legacy equivalent.
 */
public final class BlockDataCompat {

    private static final boolean BLOCK_DATA_AVAILABLE = resolveBlockDataAvailable();

    private BlockDataCompat() {}

    private static boolean resolveBlockDataAvailable() {
        try {
            Class.forName("org.bukkit.block.data.BlockData");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Sets the facing of a directional block via {@code Directional} block data
     * on 1.13+. No-ops on legacy versions where directional dispensers are
     * placed facing the player by the server already.
     */
    public static void setFacing(@Nonnull Block block, @Nonnull BlockFace face) {
        if (!BLOCK_DATA_AVAILABLE) {
            return;
        }
        try {
            Method getBlockData = Block.class.getMethod("getBlockData");
            Object data = getBlockData.invoke(block);

            Class<?> directional = Class.forName("org.bukkit.block.data.Directional");
            if (!directional.isInstance(data)) {
                return;
            }
            directional.getMethod("setFacing", BlockFace.class).invoke(data, face);

            Class<?> blockData = Class.forName("org.bukkit.block.data.BlockData");
            Block.class.getMethod("setBlockData", blockData).invoke(block, data);
        } catch (ReflectiveOperationException e) {
            // Block is not directional or API unavailable; leave default facing.
        }
    }

    /**
     * Drives a {@code RedstoneWire} block to either full or zero power on 1.13+
     * and returns the power level applied (the maximum when {@code powered},
     * otherwise {@code 0}). On legacy versions the wire is left untouched and
     * {@code 0} is returned.
     *
     * @return the power level applied, or {@code 0} when unsupported
     */
    public static int setRedstoneWirePower(@Nonnull Block block, boolean powered) {
        if (!BLOCK_DATA_AVAILABLE) {
            return 0;
        }
        try {
            Method getBlockData = Block.class.getMethod("getBlockData");
            Object wire = getBlockData.invoke(block);

            Class<?> redstoneWire = Class.forName("org.bukkit.block.data.type.RedstoneWire");
            if (!redstoneWire.isInstance(wire)) {
                return 0;
            }
            int power = 0;
            if (powered) {
                power = (int) redstoneWire.getMethod("getMaximumPower").invoke(wire);
            }
            redstoneWire.getMethod("setPower", int.class).invoke(wire, power);

            Class<?> blockData = Class.forName("org.bukkit.block.data.BlockData");
            Block.class.getMethod("setBlockData", blockData).invoke(block, wire);
            return power;
        } catch (ReflectiveOperationException e) {
            return 0;
        }
    }
}
