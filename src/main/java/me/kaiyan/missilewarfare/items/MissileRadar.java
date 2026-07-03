package me.kaiyan.missilewarfare.items;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.handlers.BlockPlaceHandler;
import me.kaiyan.missilewarfare.MissileWarfare;
import me.kaiyan.missilewarfare.missiles.MissileController;
import me.kaiyan.missilewarfare.util.Translations;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import me.kaiyan.missilewarfare.util.BlockDataCompat;
import me.kaiyan.missilewarfare.util.MaterialCompat;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;

/**
 * A Slimefun block that detects nearby missiles and outputs a redstone
 * signal when one is within range.
 *
 * @author MissileWarfare contributors
 */
public class MissileRadar extends SlimefunItem {

    /**
     * Creates a new missile radar block with tick-based missile detection.
     *
     * @param itemGroup the item group this item belongs to
     * @param recipe    the crafting recipe
     */
    public MissileRadar(@Nonnull ItemGroup itemGroup, @Nonnull ItemStack[] recipe) {
        super(itemGroup, new SlimefunItemStack("MISSILERADAR", MaterialCompat.safe(XMaterial.GRAY_WOOL)), RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                if (!MissileWarfare.activemissiles.isEmpty()) {
                    boolean missilenear = false;
                    for (MissileController missile : MissileWarfare.activemissiles) {
                        if (block.getLocation().distanceSquared(missile.pos.toLocation(missile.world)) < (700 * 700)) {
                            missilenear = true;
                            break;
                        }
                    }
                    if (block.getRelative(BlockFace.UP).getType() == MaterialCompat.safe(XMaterial.REDSTONE_WIRE) && missilenear) {
                        Block wireBlock = block.getRelative(BlockFace.UP);
                        int power = BlockDataCompat.setRedstoneWirePower(wireBlock, true);
                        MissileWarfare.getInstance().getServer().getPluginManager().callEvent(new BlockRedstoneEvent(wireBlock, 0, power));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                BlockDataCompat.setRedstoneWirePower(wireBlock, false);
                                MissileWarfare.getInstance().getServer().getPluginManager().callEvent(new BlockRedstoneEvent(wireBlock, power, 0));
                            }
                        }.runTaskLater(MissileWarfare.getInstance(), 1);
                    }
                }
            }
        });

        BlockPlaceHandler placeHandler = new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent event) {
                event.getPlayer().sendMessage(Translations.get("messages.radar"));
            }
        };
        addItemHandler(placeHandler);
    }
}

