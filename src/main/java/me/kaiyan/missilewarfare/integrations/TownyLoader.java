package me.kaiyan.missilewarfare.integrations;

import me.kaiyan.missilewarfare.MissileWarfare;
import org.bukkit.Location;
import org.bukkit.entity.Player;

// TODO: Re-enable Towny integration when compatible dependency versions are available.
public class TownyLoader {
    public static void setup(){
        MissileWarfare.townyEnabled = true;
    }

    public static boolean exploded(Player nearestPlayer, Location loc){
        return false;
    }
}
