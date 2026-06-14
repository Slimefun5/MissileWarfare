package me.kaiyan.missilewarfare.util;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * Resolves {@link XMaterial} constants to the {@link Material} of the running
 * server version. Compiling against a modern API but running on legacy versions
 * (e.g. 1.8.8) means some materials have no direct enum constant; this routes
 * every lookup through XSeries so the correct legacy material is returned.
 */
public final class MaterialCompat {

    private MaterialCompat() {}

    /**
     * Resolves the given {@link XMaterial} for the current server version,
     * falling back to {@link Material#STONE} when no legacy equivalent exists
     * so callers never receive a {@code null} material.
     */
    @Nonnull
    public static Material safe(@Nonnull XMaterial material) {
        Material resolved = material.parseMaterial();
        return resolved != null ? resolved : Material.STONE;
    }
}
