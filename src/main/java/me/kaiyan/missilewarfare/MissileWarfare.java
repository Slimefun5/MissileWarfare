package me.kaiyan.missilewarfare;

import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiText;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiTopic;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import me.kaiyan.missilewarfare.items.CustomItems;
import me.kaiyan.missilewarfare.listeners.ExplosionEventListener;
import me.kaiyan.missilewarfare.missiles.MissileConfig;
import me.kaiyan.missilewarfare.missiles.MissileController;
import me.kaiyan.missilewarfare.integrations.TownyLoader;
import me.kaiyan.missilewarfare.integrations.WorldGuardLoader;
import me.kaiyan.missilewarfare.util.PlayerID;
import me.kaiyan.missilewarfare.util.Translations;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Main plugin class for MissileWarfare, a Slimefun addon that adds
 * surface-to-surface, surface-to-air, and anti-elytra missile systems.
 *
 * @author MissileWarfare contributors
 */
public class MissileWarfare extends JavaPlugin implements SlimefunAddon {
    public static MissileWarfare plugin;
    public static List<MissileController> activemissiles;
    public static boolean worldGuardEnabled = false;
    public static boolean townyEnabled = false;
    public static Metrics metrics;
    public static int firedMissiles = 0;
    public static int blocksExploded = 0;

    @Override
    public void onEnable() {
        int pluginId = 31437;
        // Consolidated metrics: only start our own bStats if the server opted out (metrics.disable-addon-metrics = false).
        if (Slimefun.getCfg().contains("metrics.disable-addon-metrics") && !Slimefun.getCfg().getBoolean("metrics.disable-addon-metrics")) {
            metrics = new Metrics(this, pluginId);

            metrics.addCustomChart(new SingleLineChart("missiles_fired", () -> {
                int missiles = firedMissiles;
                firedMissiles = 0;
                return missiles;
            }));
            metrics.addCustomChart(new SingleLineChart("missile_destroy", () -> {
                int blocks = blocksExploded;
                blocksExploded = 0;
                return blocks;
            }));
        }

        // Startup line intentionally omitted: Slimefun core logs every installed addon uniformly.
        activemissiles = new ArrayList<>();
        plugin = this;
        Config cfg = new Config(this);
        Config saveFile;
        if (!new File(this.getDataFolder() + "/saveID.yml").exists()) {
            saveFile = new Config(new File(this.getDataFolder() + "/saveID.yml"));
            saveFile.createFile();
        } else {
            saveFile = new Config(new File(this.getDataFolder() + "/saveID.yml"));
        }
        File lang = new File(getDataFolder() + "/lang");
        if (!lang.exists()) {
            generateLangPacks(lang);
        }
        try {
            Translations.setup(new Config(getDataFolder() + "/lang/" + cfg.getString("translation-pack") + ".yml"));
            PlayerID.loadPlayers(saveFile);
            MissileConfig.setup(cfg);
            CustomItems.setup();
        } catch (Exception e) {
            getLogger().warning(e.toString());
            getLogger().warning("=== !LANG PACK INVALID, REVERTING TO EN LANG PACK! ===");
            getLogger().warning("/brokenLang/ created, the invalid langpack is in there");
            lang.renameTo(new File(getDataFolder() + "/brokenLang/"));
            generateLangPacks(lang);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerID.targets = new ArrayList<>();
            }
        }.runTaskTimer(this, 20, 200);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (activemissiles.isEmpty()) {
                    for (World world : getServer().getWorlds()) {
                        for (Entity entity : world.getEntities()) {
                            if (entity.getCustomName() != null) {
                                if (entity.getCustomName().equals("MissileHolder")) {
                                    entity.remove();
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, cfg.getInt("other.cleanup-wait-time"));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getServer().getPluginManager().getPlugin("WorldGuard") != null && getServer().getPluginManager().getPlugin("WorldEdit") != null) {
                    WorldGuardLoader.load();
                }
                if (getServer().getPluginManager().getPlugin("Towny") != null) {
                    TownyLoader.setup();
                }
            }
        }.runTaskLater(this, 0);

        getServer().getPluginManager().registerEvents(new ExplosionEventListener(), this);

        Slimefun.getItemTranslationService().registerTranslations(this);

        // Register this addon's own in-game wiki page (core does not auto-generate addon wikis).
        registerWiki();
    }

    private void registerWiki() {
        WikiText wiki = Slimefun.getWikiText();

        // LinkedHashMap preserves item discovery order.
        Map<ItemGroup, List<String>> groupedItems = new LinkedHashMap<>();
        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                if (item.getAddon() != this) {
                    continue;
                }
                ItemGroup group = item.getItemGroup();
                groupedItems.computeIfAbsent(group, key -> new ArrayList<>()).add(item.getId());

                List<String> page = describeItem(item.getId());
                if (page != null) {
                    wiki.set(item.getId(), page);
                }
            } catch (Exception | LinkageError ignored) {
                // Skip items that fail to resolve their group/addon on legacy versions.
            }
        }

        for (Map.Entry<ItemGroup, List<String>> entry : groupedItems.entrySet()) {
            String groupKey = entry.getKey().getKey().getKey();
            String topicId = "addon_missilewarfare_" + groupKey;

            wiki.registerTopic(new WikiTopic(
                topicId,
                topicDisplayName(groupKey),
                topicIcon(groupKey),
                topicTagline(groupKey)
            ));
            wiki.setMechanic(topicId, describeCategory(groupKey));
            wiki.setTopicItems(topicId, entry.getValue());
        }
    }

    @Nonnull
    private String topicDisplayName(@Nonnull String groupKey) {
        switch (groupKey) {
            case "missile_warfare": return "Missile Warfare: Arsenal";
            default: return "Missile Warfare";
        }
    }

    @Nonnull
    private XMaterial topicIcon(@Nonnull String groupKey) {
        switch (groupKey) {
            case "missile_warfare": return XMaterial.FIRE_CHARGE;
            default: return XMaterial.TNT;
        }
    }

    @Nonnull
    private String topicTagline(@Nonnull String groupKey) {
        switch (groupKey) {
            case "missile_warfare": return "&7Build, launch, intercept & defend";
            default: return "&7Missiles, launchers & defences";
        }
    }

    @Nonnull
    private List<String> describeCategory(@Nonnull String groupKey) {
        switch (groupKey) {
            case "missile_warfare":
                return Arrays.asList(
                    "&7Large-scale surface-to-surface, surface-to-air",
                    "&7and anti-elytra missile systems.",
                    "",
                    "&7&lThe supply chain:",
                    "&7 - Refine &bSugar Fuel&7 & &bRocket Fuel&7 for thrust",
                    "&7 - Press &bExplosive Powder&7 into warheads",
                    "&7 - Forge &bUltralite Ingots/Plates&7 for airframes",
                    "&7 - Assemble bodies, fins & warheads into missiles",
                    "",
                    "&7&lFiring a ground missile:",
                    "&71. Place a &bGround Missile Launcher&7 on &aGreen Concrete",
                    "&72. Right-click with a &eStick&7 to set target X/Z",
                    "&73. Right-click with a &eBlaze Rod&7 to set cruise altitude",
                    "&74. Load a missile inside and power it with redstone",
                    "",
                    "&7&lDefending:",
                    "&7 - &bAnti-Missile Launchers&7 auto-intercept incoming fire",
                    "&7 - &bMANPADs&7 let you lock & fire by hand",
                    "&7 - &bAnti-Elytra Launchers&7 down flying raiders",
                    "&7 - &bMines&7 & &bRadar&7 round out your perimeter",
                    "",
                    "&7Click an item below for its recipe & details."
                );
            default:
                return Arrays.asList(
                    "&7Missiles, launchers and defensive systems.",
                    "",
                    "&7Click an item below for its recipe & details."
                );
        }
    }

    @Nullable
    private List<String> describeItem(@Nonnull String itemId) {
        switch (itemId) {
            case "GROUNDLAUNCHER":
                return Arrays.asList(
                    "&7The main surface-to-surface launcher.",
                    "&7Must be placed on top of &aGreen Concrete&7 to arm.",
                    "&7&eStick&7: set target X/Z. &eBlaze Rod&7: set altitude.",
                    "&7Load a missile inside, then power it to fire.",
                    "&7Sneak + &eStick&7 to read the stored target & range."
                );
            case "ANTIMISSILELAUNCHER":
                return Arrays.asList(
                    "&7Automated point-defence launcher.",
                    "&7Place it on &5Obsidian&7 to arm it.",
                    "&7While unpowered it scans for incoming ground",
                    "&7missiles in range and fires an &bAnti-Air Missile",
                    "&7to intercept them. Keep it stocked with ammo."
                );
            case "ANTIELYTRA":
                return Arrays.asList(
                    "&7Automated anti-air launcher tuned for fliers.",
                    "&7Detects players gliding on an &bElytra&7 in range",
                    "&7and launches &bAnti-Elytra Missiles&7 at them.",
                    "&7Stock it with anti-elytra missiles as ammunition."
                );
            case "MISSILERADAR":
                return Arrays.asList(
                    "&7Early-warning radar block.",
                    "&7Emits a redstone signal whenever a missile",
                    "&7enters its detection range, so you can wire up",
                    "&7sirens, doors or automated defences."
                );
            case "MINE":
                return Arrays.asList(
                    "&7A hidden landmine. When placed it disguises",
                    "&7itself as the block it is set against.",
                    "&7Detonates when a player walks over it, dealing",
                    "&7an explosion plus randomised damage.",
                    "&7Cannot disguise as bedrock or ice."
                );
            case "MANPAD":
                return Arrays.asList(
                    "&7Hand-held man-portable air defence.",
                    "&7Sneak + right-click to power up the seeker and",
                    "&7sweep for incoming ground missiles.",
                    "&7Aim at a locked target and release sneak to fire.",
                    "&7Consumed on launch - craft plenty."
                );
            case "PLAYERLIST":
                return Arrays.asList(
                    "&7A logbook of registered players and targets",
                    "&7used by the warfare systems for tracking."
                );
            case "GUIDEBOOK":
                return Arrays.asList(
                    "&7An in-world guide to the Missile Warfare addon.",
                    "&7Right-click to read tips on building and firing."
                );

            case "MISSILE":
                return Arrays.asList(
                    "&7The standard surface-to-surface missile.",
                    "&7Balanced range and payload. Load it into a",
                    "&7Ground Missile Launcher and fire at set coords."
                );
            case "MISSILEHE":
                return Arrays.asList(
                    "&7High-Explosive missile.",
                    "&7Trades some range for a bigger blast on impact."
                );
            case "MISSILELR":
                return Arrays.asList(
                    "&7Long-Range missile.",
                    "&7Reaches distant targets at the cost of payload."
                );
            case "MISSILEAC":
                return Arrays.asList(
                    "&7Accuracy missile.",
                    "&7Tighter guidance for pinpoint strikes."
                );
            case "SMALLMISSILE":
                return Arrays.asList(
                    "&7A cheap short-range missile built from",
                    "&7small bodies and fins. A good first weapon."
                );
            case "SMALLMISSILEHE":
                return Arrays.asList("&7Small High-Explosive missile.", "&7More blast, short range.");
            case "SMALLMISSILELR":
                return Arrays.asList("&7Small Long-Range missile.", "&7Extra fuel extends its reach.");
            case "SMALLMISSILEAC":
                return Arrays.asList("&7Small Accuracy missile.", "&7Improved guidance, short range.");
            case "ANTIAIRMISSILE":
                return Arrays.asList(
                    "&7Interceptor ammunition for the",
                    "&7Anti-Missile Launcher. Steers onto incoming",
                    "&7ground missiles to destroy them mid-flight."
                );
            case "ANTIELYTRAMISSILE":
                return Arrays.asList(
                    "&7Interceptor ammunition for the",
                    "&7Anti-Elytra Launcher. Hunts gliding players."
                );
            case "MISSILEAPONE":
            case "MISSILEAPTWO":
            case "MISSILEAPTHR":
                return Arrays.asList(
                    "&7Armour-Piercing missile.",
                    "&7Each tier penetrates deeper through",
                    "&7reinforced blocks before detonating."
                );
            case "MISSILEGAS":
                return Arrays.asList(
                    "&7Chemical missile carrying chlorine pellets.",
                    "&7Spreads a wither-inflicting gas cloud on impact."
                );
            case "MISSILEEXCAV":
                return Arrays.asList(
                    "&7Excavation missile.",
                    "&7Tuned to dig and clear terrain rather than",
                    "&7maximise damage."
                );
            case "MISSILESTICK":
                return Arrays.asList(
                    "&7Cobweb missile.",
                    "&7Blankets the impact zone in webs to slow",
                    "&7and trap anyone caught in the area."
                );
            case "MISSILEICBM":
                return Arrays.asList(
                    "&7Intercontinental Ballistic Missile.",
                    "&7End-game weapon built on an ICBM body and",
                    "&7heavy warhead with a steel thruster.",
                    "&7Massive range and a devastating payload."
                );
            case "MISSILECLUSTER":
                return Arrays.asList(
                    "&7Cluster missile.",
                    "&7Splits into multiple submunitions to blanket",
                    "&7a wide area with explosions."
                );
            case "MISSILENAPALM":
                return Arrays.asList(
                    "&7Napalm missile.",
                    "&7Sets the impact area ablaze with persistent fire."
                );
            case "MISSILEADV":
                return Arrays.asList(
                    "&7Advanced standard missile.",
                    "&7Built on the Advanced Missile Body for",
                    "&7greater performance than the basic missile."
                );
            case "MISSILEHEADV":
                return Arrays.asList("&7Advanced High-Explosive missile.", "&7Heavy warhead on an advanced body.");
            case "MISSILELRADV":
                return Arrays.asList("&7Advanced Long-Range missile.", "&7Extended reach with guidance circuitry.");
            case "MISSILEACADV":
                return Arrays.asList("&7Advanced Accuracy missile.", "&7Precision strikes on an advanced body.");

            case "SUGARFUEL":
                return Arrays.asList(
                    "&7Basic solid propellant pressed from sugar",
                    "&7and magnesium. The entry-level missile fuel."
                );
            case "ROCKETFUEL":
                return Arrays.asList(
                    "&7Refined high-energy propellant.",
                    "&7Required for longer-range and advanced missiles."
                );
            case "EXPLOSIVEPOWDER":
                return Arrays.asList(
                    "&7Volatile powder used as the base for",
                    "&7warheads and compressed explosives."
                );
            case "COMPRESSEDEXPLOSIVES":
                return Arrays.asList(
                    "&7Explosive powder compacted into a dense",
                    "&7charge for heavier warheads."
                );
            case "ULTRALITE_INGOT":
                return Arrays.asList(
                    "&7A light, strong alloy smelted for missile",
                    "&7airframes and many other components."
                );
            case "ULTRALITE_PLATE":
                return Arrays.asList(
                    "&7Ultralite ingots pressed into structural",
                    "&7plating for bodies, fins and launchers."
                );
            case "SIMPLEFLIGHTCOMPUTER":
                return Arrays.asList(
                    "&7Guidance electronics that steer a missile",
                    "&7toward its target. Used in bodies and radar."
                );
            case "RADAR":
                return Arrays.asList(
                    "&7A detection component used to craft radar",
                    "&7blocks and seeking anti-air missiles."
                );

            case "SMALLWARHEAD":
                return Arrays.asList("&7A compact warhead for small missiles.");
            case "WARHEAD":
                return Arrays.asList("&7A standard warhead for full-size missiles.");
            case "WARHEADAP":
                return Arrays.asList(
                    "&7Reinforced armour-piercing warhead.",
                    "&7Punches through hardened blocks."
                );
            case "HEAVYWARHEAD":
                return Arrays.asList(
                    "&7A large warhead packing several charges",
                    "&7for ICBMs and the heaviest missiles."
                );
            case "SMALLBODY":
                return Arrays.asList("&7The airframe core for small missiles.");
            case "SMALLFIN":
                return Arrays.asList("&7Stabilising fins for small missiles.");
            case "MISSILEBODY":
                return Arrays.asList(
                    "&7The standard missile airframe.",
                    "&7Houses fuel and the flight computer."
                );
            case "MISSILEFINS":
                return Arrays.asList("&7Stabilising fins for full-size missiles.");
            case "ADVANCEDMISSILEBODY":
                return Arrays.asList(
                    "&7An upgraded airframe with extra guidance",
                    "&7for advanced and specialist missiles."
                );
            case "ICBMMISSILEBODY":
                return Arrays.asList(
                    "&7A neptunium-reinforced airframe used only",
                    "&7for the ICBM. Extreme structural strength."
                );

            case "CHLORINE":
                return Arrays.asList(
                    "&7Toxic chlorine washed from soul sand.",
                    "&7Right-click to inflict wither on yourself -",
                    "&7handle with care. Feeds chlorine pellets."
                );
            case "CHLORINEPELLET":
                return Arrays.asList(
                    "&7A stabilised chlorine charge used to build",
                    "&7the chemical Gas Missile."
                );

            default:
                return null;
        }
    }

    /**
     * Returns the singleton plugin instance.
     *
     * @return the MissileWarfare instance
     */
    @Nonnull
    public static MissileWarfare getInstance() {
        return plugin;
    }

    @Override
    public void onDisable() {
        for (MissileController missile : activemissiles) {
            try {
                missile.armourStand.remove();
                missile.update.cancel();
            } catch (NullPointerException e) {
                try {
                    missile.update.cancel();
                } catch (NullPointerException ignored) {
                }
            }
        }
        PlayerID.savePlayers(new Config(new File(this.getDataFolder() + "/saveID.yml")));
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return null;
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    /**
     * Generates default language pack files from bundled resources
     * and moves them into the lang directory.
     *
     * @param lang the target language directory
     */
    public void generateLangPacks(@Nonnull File lang) {
        String[] loadedpacks = this.getConfig().getStringList("saved-packs").toArray(new String[0]);
        for (String pack : loadedpacks) {
            saveResource(pack + ".yml", false);
        }

        lang.mkdir();

        File datafolder = getDataFolder();
        for (File file : datafolder.listFiles()) {
            if (file.getName().startsWith("pack-")) {
                try {
                    Files.move(file.toPath(), new File(lang.getPath(), file.getName()).toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

