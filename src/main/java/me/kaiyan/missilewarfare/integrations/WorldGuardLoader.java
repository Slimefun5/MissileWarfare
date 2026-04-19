package me.kaiyan.missilewarfare.integrations;

import me.kaiyan.missilewarfare.MissileWarfare;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

// TODO: Re-enable WorldGuard integration when compatible dependency versions are available.
// WorldGuard/WorldEdit deps are commented out due to strict version constraints conflicting with Paper API.
public class WorldGuardLoader {

    public static void load(){
        MissileWarfare.worldGuardEnabled = true;
        MissileWarfare.getInstance().getLogger().info("WorldGuard Enabled!");
    }

    public static void explode(World world, Vector pos, double power, Entity armourStand, Player nearestPlayer){
        world.createExplosion(pos.toLocation(world), (float) power, false, true, armourStand);
    }
}
