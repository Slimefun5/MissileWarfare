package me.kaiyan.missilewarfare.items;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.api.researches.Research;
import io.github.thebusybiscuit.slimefun5.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import me.kaiyan.missilewarfare.MissileWarfare;
import me.kaiyan.missilewarfare.util.MaterialCompat;
import me.kaiyan.missilewarfare.blocks.AntiElytraLauncher;
import me.kaiyan.missilewarfare.blocks.AntiMissileLauncher;
import me.kaiyan.missilewarfare.blocks.GroundMissileLauncher;
import me.kaiyan.missilewarfare.util.Translations;
import org.bukkit.Material;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Registers all custom Slimefun items, recipes, and researches for
 * the MissileWarfare addon.
 *
 * @author MissileWarfare contributors
 */
public class CustomItems {

    /**
     * Creates and registers all MissileWarfare items, recipes, and researches
     * with the Slimefun registry.
     */
    public static void setup() {
        NamespacedKey categoryId = new NamespacedKey("missilewarfare", "missile_warfare");
        ItemStack categoryItem = CustomItemStack.create(MaterialCompat.safe(XMaterial.GUNPOWDER), Translations.get("missilegroupname"));

        ItemGroup group = new ItemGroup(categoryId, categoryItem).setTheme("weapons");

        //<editor-fold desc="CREATE ITEMSTACKS">
        //<editor-fold desc="SUGARFUEL">
        SlimefunItemStack sugarfuelstack = new SlimefunItemStack("SUGARFUEL", MaterialCompat.safe(XMaterial.SUGAR));
        ItemStack[] sugarfuelrecipe = {
                null, SlimefunItems.MAGNESIUM_DUST.item(), null,
                SlimefunItems.MAGNESIUM_DUST.item(), new ItemStack(MaterialCompat.safe(XMaterial.COAL)), SlimefunItems.MAGNESIUM_DUST.item(),
                null, SlimefunItems.MAGNESIUM_DUST.item(), null
        };

        SlimefunItem sugarfuel = new SlimefunItem(group, sugarfuelstack, RecipeType.ENHANCED_CRAFTING_TABLE, sugarfuelrecipe);
        //</editor-fold>
        //<editor-fold desc="GUIDEBOOK">
        SlimefunItemStack guidestack = new SlimefunItemStack("GUIDEBOOK", MaterialCompat.safe(XMaterial.WRITTEN_BOOK));
        ItemStack[] guiderecipe = {
                null, sugarfuelstack.item(), null,
                sugarfuelstack.item(), new ItemStack(MaterialCompat.safe(XMaterial.BOOK)), sugarfuelstack.item(),
                null, sugarfuelstack.item(), null
        };

        GuideBook guide = new GuideBook(group, guidestack, RecipeType.ENHANCED_CRAFTING_TABLE, guiderecipe);
        //</editor-fold>
        //<editor-fold desc="EXPLOSIVEPOWDER">
        SlimefunItemStack explosivepowderstack = new SlimefunItemStack("EXPLOSIVEPOWDER", MaterialCompat.safe(XMaterial.GLOWSTONE_DUST));
        ItemStack[] explosivepowderrecipe = {
                SlimefunItems.MAGNESIUM_DUST.item(), new ItemStack(MaterialCompat.safe(XMaterial.COAL)), SlimefunItems.MAGNESIUM_DUST.item(),
                new ItemStack(MaterialCompat.safe(XMaterial.COAL)), new ItemStack(MaterialCompat.safe(XMaterial.GUNPOWDER)), new ItemStack(MaterialCompat.safe(XMaterial.COAL)),
                SlimefunItems.MAGNESIUM_DUST.item(), new ItemStack(MaterialCompat.safe(XMaterial.COAL)), SlimefunItems.MAGNESIUM_DUST.item(),
        };

        SlimefunItem explosivepowder = new SlimefunItem(group, explosivepowderstack, RecipeType.ENHANCED_CRAFTING_TABLE, explosivepowderrecipe);


        //</editor-fold>
        //<editor-fold desc="COMPRESSEDEXPLOSIVES">
        SlimefunItemStack compressedpowderstack = new SlimefunItemStack("COMPRESSEDEXPLOSIVES", MaterialCompat.safe(XMaterial.YELLOW_CONCRETE));
        ItemStack[] compressedpowderrecipe = {
                null, new ItemStack(MaterialCompat.safe(XMaterial.GUNPOWDER)), null,
                explosivepowderstack.item(), explosivepowderstack.item(), explosivepowderstack.item(),
                null, new ItemStack(MaterialCompat.safe(XMaterial.GUNPOWDER)), null,
        };
        SlimefunItem compressedpowder = new SlimefunItem(group, compressedpowderstack, RecipeType.ENHANCED_CRAFTING_TABLE, compressedpowderrecipe);
        //</editor-fold>
        //<editor-fold desc="ULTRALITE_INGOT">
        SlimefunItemStack ultraliteingotstack = new SlimefunItemStack("ULTRALITE_INGOT", MaterialCompat.safe(XMaterial.BRICK));
        ItemStack[] ultraliteingotrecipe = {
                SlimefunItems.IRON_DUST.item(), SlimefunItems.ALUMINUM_INGOT.item(), SlimefunItems.COPPER_DUST.item(),
                SlimefunItems.ALUMINUM_BRONZE_INGOT.item(), null, null,
                null, null, null
        };

        SlimefunItem ultraliteingot = new SlimefunItem(group, ultraliteingotstack, RecipeType.SMELTERY, ultraliteingotrecipe);
        //</editor-fold>
        //<editor-fold desc="ULTRALITE_PLATE">
        SlimefunItemStack ultraliteplatestack = new SlimefunItemStack("ULTRALITE_PLATE", MaterialCompat.safe(XMaterial.IRON_INGOT));
        ItemStack[] ultraliteplaterecipe = {
                ultraliteingotstack.item(), null, ultraliteingotstack.item(),
                null, new ItemStack(MaterialCompat.safe(XMaterial.COAL)), null,
                ultraliteingotstack.item(), null, ultraliteingotstack.item()
        };

        SlimefunItem ultraliteplate = new SlimefunItem(group, ultraliteplatestack, RecipeType.ENHANCED_CRAFTING_TABLE, ultraliteplaterecipe);
        //</editor-fold>
        //<editor-fold desc="SIMPLE_FLIGHT_COMPUTER">
        SlimefunItemStack simpleflightcomputerstacks = new SlimefunItemStack("SIMPLEFLIGHTCOMPUTER", MaterialCompat.safe(XMaterial.POWERED_RAIL));
        SlimefunItemStack simpleflightcomputerstack = (SlimefunItemStack) simpleflightcomputerstacks.clone();
        simpleflightcomputerstacks.setAmount(8);
        simpleflightcomputerstack.setAmount(1);
        ItemStack[] simpleflightcomputerrecipe = {
                ultraliteingotstack.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), ultraliteingotstack.item(),
                new ItemStack(MaterialCompat.safe(XMaterial.REDSTONE)), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.REDSTONE)),
                ultraliteingotstack.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), ultraliteingotstack.item()
        };

        SlimefunItem simpleflightcomputer = new SlimefunItem(group, simpleflightcomputerstack, RecipeType.ENHANCED_CRAFTING_TABLE, simpleflightcomputerrecipe);
        simpleflightcomputer.setRecipeOutput(simpleflightcomputerstacks.item());
        //</editor-fold>
        //<editor-fold desc="RADAR">
        SlimefunItemStack radarstack = new SlimefunItemStack("RADAR", MaterialCompat.safe(XMaterial.ACTIVATOR_RAIL));
        ItemStack[] radarrecipe = {
                null, ultraliteplatestack.item(), null,
                ultraliteplatestack.item(), simpleflightcomputerstack.item(), ultraliteplatestack.item(),
                new ItemStack(MaterialCompat.safe(XMaterial.REDSTONE)), null, new ItemStack(MaterialCompat.safe(XMaterial.REDSTONE))
        };

        SlimefunItem radar = new SlimefunItem(group, radarstack, RecipeType.ENHANCED_CRAFTING_TABLE, radarrecipe);
        //</editor-fold>
        //<editor-fold desc="ROCKETFUEL">
        SlimefunItemStack rocketfuelstack = new SlimefunItemStack("ROCKETFUEL", MaterialCompat.safe(XMaterial.GUNPOWDER));
        ItemStack[] rocketfuelrecipe = {
                explosivepowderstack.item(), new ItemStack(MaterialCompat.safe(XMaterial.GUNPOWDER)), explosivepowderstack.item(),
                null, SlimefunItems.ALUMINUM_DUST.item(), null,
                explosivepowderstack.item(), new ItemStack(MaterialCompat.safe(XMaterial.GUNPOWDER)), explosivepowderstack.item()
        };
        SlimefunItem rocketfuel = new SlimefunItem(group, rocketfuelstack, RecipeType.ENHANCED_CRAFTING_TABLE, rocketfuelrecipe);
        //</editor-fold>
        //<editor-fold desc="SMALLWARHEAD">
        SlimefunItemStack smallwarheadstack = new SlimefunItemStack("SMALLWARHEAD", MaterialCompat.safe(XMaterial.TNT));
        ItemStack[] smallwarheadrecipe = {
                null, SlimefunItems.ALUMINUM_INGOT.item(), null,
                SlimefunItems.ALUMINUM_INGOT.item(), explosivepowderstack.item(), SlimefunItems.ALUMINUM_INGOT.item(),
                null, null, null
        };

        SlimefunItem smallwarhead = new SlimefunItem(group, smallwarheadstack, RecipeType.ENHANCED_CRAFTING_TABLE, smallwarheadrecipe);
        //</editor-fold>
        //<editor-fold desc="SMALLBODY">
        SlimefunItemStack smallbodystack = new SlimefunItemStack("SMALLBODY", MaterialCompat.safe(XMaterial.IRON_BLOCK));
        ItemStack[] smallbodyrecipe = {
                SlimefunItems.ALUMINUM_INGOT.item(), null, SlimefunItems.ALUMINUM_INGOT.item(),
                ultraliteingotstack.item(), simpleflightcomputerstack.item(), ultraliteingotstack.item(),
                SlimefunItems.ALUMINUM_INGOT.item(), null, SlimefunItems.ALUMINUM_INGOT.item()
        };

        SlimefunItem smallbody = new SlimefunItem(group, smallbodystack, RecipeType.ENHANCED_CRAFTING_TABLE, smallbodyrecipe);
        //</editor-fold>
        //<editor-fold desc="SMALLFIN">
        SlimefunItemStack smallfinstack = new SlimefunItemStack("SMALLFIN", MaterialCompat.safe(XMaterial.IRON_BOOTS));
        ItemStack[] smallfinrecipe = {
                null, null, null,
                null, simpleflightcomputerstack.item(), null,
                SlimefunItems.ALUMINUM_INGOT.item(), null, SlimefunItems.ALUMINUM_INGOT.item()
        };

        SlimefunItem smallfin = new SlimefunItem(group, smallfinstack, RecipeType.ENHANCED_CRAFTING_TABLE, smallfinrecipe);
        //</editor-fold>
        //<editor-fold desc="MINE">
        SlimefunItemStack minestack = new SlimefunItemStack("MINE", MaterialCompat.safe(XMaterial.TNT));
        ItemStack[] minerecipe = {
                SlimefunItems.SILVER_INGOT.item(), new ItemStack(MaterialCompat.safe(XMaterial.STONE_PRESSURE_PLATE)), SlimefunItems.SILVER_INGOT.item(),
                SlimefunItems.SILVER_INGOT.item(), rocketfuelstack.item(), SlimefunItems.SILVER_INGOT.item(),
                SlimefunItems.SILVER_INGOT.item(), explosivepowderstack.item(), SlimefunItems.SILVER_INGOT.item()
        };

        Mine mine = new Mine(group, minestack, RecipeType.ENHANCED_CRAFTING_TABLE, minerecipe);
        //</editor-fold>
        //<editor-fold desc="WARHEAD">
        SlimefunItemStack warheadstack = new SlimefunItemStack("WARHEAD", MaterialCompat.safe(XMaterial.TNT));
        ItemStack[] warheadrecipe = {
                null, ultraliteingotstack.item(), null,
                ultraliteingotstack.item(), compressedpowderstack.item(), ultraliteingotstack.item(),
                null, null, null
        };

        SlimefunItem warhead = new SlimefunItem(group, warheadstack, RecipeType.ENHANCED_CRAFTING_TABLE, warheadrecipe);
        //</editor-fold>
        Translations.setType("smallmissile");
        //<editor-fold desc="SMALLMISSILE">
        SlimefunItemStack smallmissilestack = new SlimefunItemStack("SMALLMISSILE", MaterialCompat.safe(XMaterial.IRON_SWORD));
        ItemStack[] smallmissilerecipe = {
                explosivepowderstack.item(), smallwarheadstack.item(), explosivepowderstack.item(),
                sugarfuelstack.item(), smallbodystack.item(), sugarfuelstack.item(),
                sugarfuelstack.item(), smallfinstack.item(), sugarfuelstack.item()
        };

        MissileItem smallmissile = new MissileItem(group, smallmissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, smallmissilerecipe, 1, Translations.getTypeLore());
        //</editor-fold>
        //<editor-fold desc="SMALLMISSILEHE">
        SlimefunItemStack smallmissilestackHE = new SlimefunItemStack("SMALLMISSILEHE", MaterialCompat.safe(XMaterial.IRON_SWORD));
        ItemStack[] smallmissilerecipeHE = {
                explosivepowderstack.item(), smallwarheadstack.item(), explosivepowderstack.item(),
                explosivepowderstack.item(), smallbodystack.item(), sugarfuelstack.item(),
                sugarfuelstack.item(), smallfinstack.item(), sugarfuelstack.item()
        };

        MissileItem smallmissileHE = new MissileItem(group, smallmissilestackHE, RecipeType.ENHANCED_CRAFTING_TABLE, smallmissilerecipeHE, 2, Translations.getTypeLore());
        //</editor-fold>
        //<editor-fold desc="SMALLMISSILELR">
        SlimefunItemStack smallmissileLRstack = new SlimefunItemStack("SMALLMISSILELR", MaterialCompat.safe(XMaterial.IRON_SWORD));
        ItemStack[] smallmissileLRrecipe = {
                explosivepowderstack.item(), smallwarheadstack.item(), explosivepowderstack.item(),
                sugarfuelstack.item(), smallbodystack.item(), sugarfuelstack.item(),
                rocketfuelstack.item(), smallfinstack.item(), rocketfuelstack.item()
        };
        MissileItem smallmissileLR = new MissileItem(group, smallmissileLRstack, RecipeType.ENHANCED_CRAFTING_TABLE, smallmissileLRrecipe, 3, Translations.getMissileLore("lr"));
        //</editor-fold>
        //<editor-fold desc="SMALLMISSILEAC">
        SlimefunItemStack smallmissileACstack = new SlimefunItemStack("SMALLMISSILEAC", MaterialCompat.safe(XMaterial.IRON_SWORD));
        ItemStack[] smallmissileACrecipe = {
                explosivepowderstack.item(), smallwarheadstack.item(), explosivepowderstack.item(),
                sugarfuelstack.item(), smallbodystack.item(), sugarfuelstack.item(),
                smallfinstack.item(), rocketfuelstack.item(), smallfinstack.item()
        };
        MissileItem smallmissileAC = new MissileItem(group, smallmissileACstack, RecipeType.ENHANCED_CRAFTING_TABLE, smallmissileACrecipe, 4, Translations.getMissileLore("ac"));
        //</editor-fold>
        //<editor-fold desc="GROUNDLAUNCHER">
        SlimefunItemStack groundlauncherstack = new SlimefunItemStack("GROUNDLAUNCHER", MaterialCompat.safe(XMaterial.DISPENSER));
        ItemStack[] groundlauncherrecipe = {
                SlimefunItems.REINFORCED_PLATE.item(), null, SlimefunItems.REINFORCED_PLATE.item(),
                SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.REINFORCED_PLATE.item(),
                SlimefunItems.REINFORCED_PLATE.item(), new ItemStack(MaterialCompat.safe(XMaterial.FLINT_AND_STEEL)), SlimefunItems.REINFORCED_PLATE.item()
        };
        GroundMissileLauncher groundlauncher = new GroundMissileLauncher(group, groundlauncherstack, RecipeType.ENHANCED_CRAFTING_TABLE, groundlauncherrecipe);
        //</editor-fold>
        //<editor-fold desc="MISSILEBODY">
        SlimefunItemStack missilebodystack = new SlimefunItemStack("MISSILEBODY", MaterialCompat.safe(XMaterial.SMOOTH_STONE));
        ItemStack[] missilebodyrecipe = {
                ultraliteplatestack.item(), simpleflightcomputerstack.item(), ultraliteplatestack.item(),
                ultraliteplatestack.item(), rocketfuelstack.item(), ultraliteplatestack.item(),
                ultraliteplatestack.item(), rocketfuelstack.item(), ultraliteplatestack.item()
        };
        SlimefunItemStack missilebodystacks = (SlimefunItemStack) missilebodystack.clone();
        missilebodystacks.setAmount(4);

        SlimefunItem missilebody = new SlimefunItem(group, missilebodystack, RecipeType.ENHANCED_CRAFTING_TABLE, missilebodyrecipe);

        missilebody.setRecipeOutput(missilebodystacks.item());

        //</editor-fold>
        //<editor-fold desc="MISSILEFINS">
        SlimefunItemStack finsstack = new SlimefunItemStack("MISSILEFINS", MaterialCompat.safe(XMaterial.GOLDEN_BOOTS));
        ItemStack[] finsrecipe = {
                null, null, null,
                ultraliteplatestack.item(), null, ultraliteplatestack.item(),
                ultraliteplatestack.item(), null, ultraliteplatestack.item()
        };

        SlimefunItem fins = new SlimefunItem(group, finsstack, RecipeType.ENHANCED_CRAFTING_TABLE, finsrecipe);
        //</editor-fold>
        Translations.setType("anti-missiles");
        //<editor-fold desc="ANTIAIRMISSILE">
        SlimefunItemStack antiAirMissilestack = new SlimefunItemStack("ANTIAIRMISSILE", MaterialCompat.safe(XMaterial.IRON_SWORD));
        ItemStack[] antiAirMissilerecipe = {
                null, radarstack.item(), null,
                explosivepowderstack.item(), missilebodystack.item(), explosivepowderstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem antiAirMissile = new MissileItem(group, antiAirMissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, antiAirMissilerecipe, 5, Translations.getMissileLore("missile"));
        //</editor-fold>
        //<editor-fold desc="ANTIELYTRAMISSILE">
        SlimefunItemStack antielytramissilestack = new SlimefunItemStack("ANTIELYTRAMISSILE", MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        ItemStack[] antielytramissilerecipe = {
                radarstack.item(), smallwarheadstack.item(), radarstack.item(),
                explosivepowderstack.item(), missilebodystack.item(), explosivepowderstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem antielytramissile = new MissileItem(group, antielytramissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, antielytramissilerecipe, 23, Translations.getMissileLore("missile"));
        //</editor-fold>
        //<editor-fold desc="ANTIMISSILELAUNCHER">
        SlimefunItemStack antiairlauncherstack = new SlimefunItemStack("ANTIMISSILELAUNCHER", MaterialCompat.safe(XMaterial.DISPENSER));
        ItemStack[] antiairlauncherrecipe = {
                SlimefunItems.SILVER_INGOT.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), SlimefunItems.SILVER_INGOT.item(),
                SlimefunItems.SILVER_INGOT.item(), null, SlimefunItems.SILVER_INGOT.item(),
                SlimefunItems.LEAD_INGOT.item(), new ItemStack(MaterialCompat.safe(XMaterial.REDSTONE_BLOCK)), SlimefunItems.LEAD_INGOT.item()
        };
        AntiMissileLauncher antiairlauncher = new AntiMissileLauncher(group, antiairlauncherstack, RecipeType.ENHANCED_CRAFTING_TABLE, antiairlauncherrecipe);
        //</editor-fold>
        //<editor-fold desc="MANPAD">
        SlimefunItemStack manpadstack = new SlimefunItemStack("MANPAD", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiNmVlNWJiZTVhZDQyOTY4MGMxYzE1Y2Y0MjBmOTgxMWUxMTRiNzY4NTRmODk5ZjBlZjA4ZmRlMzMyNzk4YyJ9fX0=");
        ItemStack[] manpadrecipe = {
                new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT)), new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT)), new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT)),
                explosivepowderstack.item(), sugarfuelstack.item(), sugarfuelstack.item(),
                new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT)), new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT)), new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT))
        };
        ManPad manpad = new ManPad(group, manpadstack, RecipeType.ENHANCED_CRAFTING_TABLE, manpadrecipe);
        //</editor-fold>
        Translations.setType("normalmissile");
        //<editor-fold desc="MISSILE">
        SlimefunItemStack missilestack = new SlimefunItemStack("MISSILE", MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        ItemStack[] missilerecipe = {
                null, warheadstack.item(), null,
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), smallfinstack.item(), rocketfuelstack.item(),
        };
        MissileItem missile = new MissileItem(group, missilestack, RecipeType.ENHANCED_CRAFTING_TABLE, missilerecipe, 6, Translations.getMissileLore("normal"));
        //</editor-fold>
        //<editor-fold desc="MISSILEHE">
        SlimefunItemStack missileHEstack = new SlimefunItemStack("MISSILEHE", MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        ItemStack[] missileHErecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), smallfinstack.item(), rocketfuelstack.item(),
        };
        MissileItem missileHE = new MissileItem(group, missileHEstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileHErecipe, 7, Translations.getMissileLore("he"));
        //</editor-fold>
        //<editor-fold desc="MISSILELR">
        SlimefunItemStack missileLRstack = new SlimefunItemStack("MISSILELR", MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        ItemStack[] missileLRrecipe = {
                rocketfuelstack.item(), warheadstack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item(),
        };
        MissileItem missileLR = new MissileItem(group, missileLRstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileLRrecipe, 8, Translations.getMissileLore("lr"));
        //</editor-fold>
        //<editor-fold desc="MISSILEAC">
        SlimefunItemStack missileACstack = new SlimefunItemStack("MISSILEAC", MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        ItemStack[] missileACrecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                finsstack.item(), rocketfuelstack.item(), finsstack.item(),
        };
        MissileItem missileAC = new MissileItem(group, missileACstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileACrecipe, 9, Translations.getMissileLore("ac"));
        //</editor-fold>
        //<editor-fold desc="PLAYERLIST">
        SlimefunItemStack playerliststack = new SlimefunItemStack("PLAYERLIST", MaterialCompat.safe(XMaterial.ENCHANTED_BOOK));
        ItemStack[] playerlistrecipe = {
                null, ultraliteingotstack.item(), null,
                ultraliteingotstack.item(), new ItemStack(MaterialCompat.safe(XMaterial.BOOK)), ultraliteingotstack.item(),
                null, ultraliteingotstack.item(), null
        };
        PlayerList playerList = new PlayerList(group, playerliststack, RecipeType.ENHANCED_CRAFTING_TABLE, playerlistrecipe);
        //</editor-fold>
        //<editor-fold desc="ANTIELYTRA">
        SlimefunItemStack antielytrastack = new SlimefunItemStack("ANTIELYTRA", MaterialCompat.safe(XMaterial.DISPENSER));
        ItemStack[] antielytralauncherrecipe = {
                ultraliteplatestack.item(), null, ultraliteplatestack.item(),
                SlimefunItems.REINFORCED_PLATE.item(), null, SlimefunItems.REINFORCED_PLATE.item(),
                SlimefunItems.REINFORCED_PLATE.item(), SlimefunItems.ELECTRIC_MOTOR.item(), SlimefunItems.REINFORCED_PLATE.item()
        };
        AntiElytraLauncher antiElytraLauncher = new AntiElytraLauncher(group, antielytrastack, RecipeType.ENHANCED_CRAFTING_TABLE, antielytralauncherrecipe);

        MissileWarfare main = MissileWarfare.getInstance();
        //</editor-fold>
        //<editor-fold desc="REINFORCEDWARHEAD">
        SlimefunItemStack warheadAPstack = new SlimefunItemStack("WARHEADAP", MaterialCompat.safe(XMaterial.TNT));
        ItemStack[] warheadAPrecipe = {
                null, SlimefunItems.LEAD_INGOT.item(), null,
                ultraliteingotstack.item(), compressedpowderstack.item(), ultraliteingotstack.item(),
                null, null, null
        };

        SlimefunItem warheadAP = new SlimefunItem(group, warheadAPstack, RecipeType.ENHANCED_CRAFTING_TABLE, warheadAPrecipe);
        //</editor-fold>
        Translations.setType("armourpiercing");
        //<editor-fold desc="APMISSILET1">
        SlimefunItemStack missileAPstack = new SlimefunItemStack("MISSILEAPONE", MaterialCompat.safe(XMaterial.DIAMOND_SWORD));
        ItemStack[] missileAPrecipe = {
                compressedpowderstack.item(), smallwarheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                smallfinstack.item(), finsstack.item(), smallfinstack.item(),
        };
        MissileItem missileAP = new MissileItem(group, missileAPstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileAPrecipe, 10, Translations.getMissileLore("tierone"));
        //</editor-fold>
        //<editor-fold desc="APMISSILET2">
        SlimefunItemStack missileAPtstack = new SlimefunItemStack("MISSILEAPTWO", MaterialCompat.safe(XMaterial.DIAMOND_SWORD));
        ItemStack[] missileAPtrecipe = {
                ultraliteingotstack.item(), ultraliteplatestack.item(), ultraliteingotstack.item(),
                ultraliteingotstack.item(), missileAPstack.item(), ultraliteingotstack.item(),
                sugarfuelstack.item(), sugarfuelstack.item(), sugarfuelstack.item()
        };
        MissileItem missileAPt = new MissileItem(group, missileAPtstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileAPtrecipe, 11, Translations.getMissileLore("tiertwo"));
        //</editor-fold>
        //<editor-fold desc="APMISSILET3">
        SlimefunItemStack missileAPtrstack = new SlimefunItemStack("MISSILEAPTHR", MaterialCompat.safe(XMaterial.DIAMOND_SWORD));
        ItemStack[] missileAPtrrecipe = {
                ultraliteingotstack.item(), ultraliteplatestack.item(), ultraliteingotstack.item(),
                ultraliteingotstack.item(), missileAPtstack.item(), ultraliteingotstack.item(),
                sugarfuelstack.item(), sugarfuelstack.item(), sugarfuelstack.item()
        };
        MissileItem missileAPtr = new MissileItem(group, missileAPtrstack, RecipeType.ENHANCED_CRAFTING_TABLE, missileAPtrrecipe, 12, Translations.getMissileLore("tierthree"));
        //</editor-fold>
        //<editor-fold desc="CHLORINE">
        SlimefunItemStack chlorinestack = new SlimefunItemStack("CHLORINE", MaterialCompat.safe(XMaterial.SUGAR));
        ItemStack[] chlorinerecipe = {
                new ItemStack(MaterialCompat.safe(XMaterial.SOUL_SAND)), null, null,
                null, null, null,
                null, null, null
        };
        SlimefunItem chlorine = new SlimefunItem(group, chlorinestack, RecipeType.ORE_WASHER, chlorinerecipe);
        RecipeType.ORE_WASHER.register(chlorinerecipe, chlorinestack.item());
        chlorine.addItemHandler((ItemUseHandler) playerRightClickEvent -> playerRightClickEvent.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 2)));
        //</editor-fold>
        //<editor-fold desc="CHLORINEPELLET">
        SlimefunItemStack chlorinepelletstack = new SlimefunItemStack("CHLORINEPELLET", MaterialCompat.safe(XMaterial.LIME_DYE));
        ItemStack[] chlorinepelletrecipe = {
                chlorinestack.item(), SlimefunItems.SULFATE.item(), chlorinestack.item(),
                SlimefunItems.SULFATE.item(), SlimefunItems.SALT.item(), SlimefunItems.SULFATE.item(),
                chlorinestack.item(), SlimefunItems.SULFATE.item(), chlorinestack.item()
        };
        SlimefunItem chlorinepellet = new SlimefunItem(group, chlorinepelletstack, RecipeType.ENHANCED_CRAFTING_TABLE, chlorinepelletrecipe);
        chlorine.addItemHandler((ItemUseHandler) playerRightClickEvent -> playerRightClickEvent.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10, 2)));
        //</editor-fold>
        //<editor-fold desc="GASMISSILE">
        SlimefunItemStack missilegasstack = new SlimefunItemStack("MISSILEGAS", MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        ItemStack[] missilegasrecipe = {
                chlorinepelletstack.item(), chlorinepelletstack.item(), chlorinepelletstack.item(),
                rocketfuelstack.item(), missilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem missilegas = new MissileItem(group, missilegasstack, RecipeType.ENHANCED_CRAFTING_TABLE, missilegasrecipe, 13, Translations.getSpecialALore("gasmissile"));
        //</editor-fold>
        //<editor-fold desc="EXCAVATIONMISSILE">
        SlimefunItemStack excabmissilestack = new SlimefunItemStack("MISSILEEXCAV", MaterialCompat.safe(XMaterial.WOODEN_SWORD));
        ItemStack[] excabmissilerecipe = {
                explosivepowderstack.item(), warheadstack.item(), explosivepowderstack.item(),
                warheadstack.item(), smallbodystack.item(), warheadstack.item(),
                sugarfuelstack.item(), smallfinstack.item(), sugarfuelstack.item()
        };
        MissileItem excavmissile = new MissileItem(group, excabmissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, excabmissilerecipe, 14, Translations.getSpecialALore("excavmissile"));
        //</editor-fold>
        //<editor-fold desc="COBWEBMISSILE">
        SlimefunItemStack stickymissilestack = new SlimefunItemStack("MISSILESTICK", MaterialCompat.safe(XMaterial.IRON_SWORD));
        ItemStack[] stickymissilerecipe = {
                new ItemStack(MaterialCompat.safe(XMaterial.STRING)), new ItemStack(MaterialCompat.safe(XMaterial.STRING)), new ItemStack(MaterialCompat.safe(XMaterial.STRING)),
                sugarfuelstack.item(), missilebodystack.item(), sugarfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem stickymissile = new MissileItem(group, stickymissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, stickymissilerecipe, 15, Translations.getSpecialALore("stickymissile"));
        //</editor-fold>
        //<editor-fold desc="ADVANCEDMISSILEBODY">
        SlimefunItemStack advancedmissilebodystack = new SlimefunItemStack("ADVANCEDMISSILEBODY", MaterialCompat.safe(XMaterial.GRAY_CONCRETE));
        ItemStack[] advancedmissilebodyrecipe = {
                rocketfuelstack.item(), simpleflightcomputerstack.item(), rocketfuelstack.item(),
                ultraliteplatestack.item(), missilebodystack.item(), ultraliteplatestack.item(),
                rocketfuelstack.item(), simpleflightcomputerstack.item(), rocketfuelstack.item()
        };

        SlimefunItem advancedmissilebody = new SlimefunItem(group, advancedmissilebodystack, RecipeType.ENHANCED_CRAFTING_TABLE, advancedmissilebodyrecipe);

        missilebody.setRecipeOutput(missilebodystacks.item());

        //</editor-fold>
        //<editor-fold desc="LARGEWARHEAD">
        SlimefunItemStack heavywarheadstack = new SlimefunItemStack("HEAVYWARHEAD", MaterialCompat.safe(XMaterial.RED_CONCRETE));
        ItemStack[] heavywarheadrecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                warheadstack.item(), ultraliteplatestack.item(), warheadstack.item(),
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item()
        };

        SlimefunItem heavywarhead = new SlimefunItem(group, heavywarheadstack, RecipeType.ENHANCED_CRAFTING_TABLE, heavywarheadrecipe);
        //</editor-fold>
        //<editor-fold desc="ICBMMISSILEBODY">
        SlimefunItemStack icbmmissilebodystack = new SlimefunItemStack("ICBMMISSILEBODY", MaterialCompat.safe(XMaterial.GREEN_CONCRETE));
        ItemStack[] icbmmissilebodyrecipe = {
                compressedpowderstack.item(), SlimefunItems.NEPTUNIUM.item(), compressedpowderstack.item(),
                SlimefunItems.NEPTUNIUM.item(), advancedmissilebodystack.item(), SlimefunItems.NEPTUNIUM.item(),
                compressedpowderstack.item(), SlimefunItems.NEPTUNIUM.item(), compressedpowderstack.item()
        };

        SlimefunItem icbmmissilebody = new SlimefunItem(group, icbmmissilebodystack, RecipeType.ENHANCED_CRAFTING_TABLE, icbmmissilebodyrecipe);

        missilebody.setRecipeOutput(missilebodystacks.item());

        //</editor-fold>
        //<editor-fold desc="ICBM">
        SlimefunItemStack icbmstack = new SlimefunItemStack("MISSILEICBM", MaterialCompat.safe(XMaterial.DIAMOND_SWORD));
        ItemStack[] icbmrecipe = {
                rocketfuelstack.item(), heavywarheadstack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), icbmmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), SlimefunItems.STEEL_THRUSTER.item(), rocketfuelstack.item()
        };
        MissileItem icbm = new MissileItem(group, icbmstack, RecipeType.ENHANCED_CRAFTING_TABLE, icbmrecipe, 16, Translations.getSpecialALore("icbm"));
        //</editor-fold>
        //<editor-fold desc="CLUSTERMISSILE">
        SlimefunItemStack clusterstack = new SlimefunItemStack("MISSILECLUSTER", MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        ItemStack[] clusterrecipe = {
                warheadstack.item(), heavywarheadstack.item(), warheadstack.item(),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem cluster = new MissileItem(group, clusterstack, RecipeType.ENHANCED_CRAFTING_TABLE, clusterrecipe, 17, Translations.getSpecialALore("clustermissile"));
        //</editor-fold>
        //<editor-fold desc="NAPALMMISSILE">
        SlimefunItemStack napalmmissilestack = new SlimefunItemStack("MISSILENAPALM", MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        ItemStack[] napalmmissilerecipe = {
                new ItemStack(MaterialCompat.safe(XMaterial.FIRE_CHARGE)), heavywarheadstack.item(), new ItemStack(MaterialCompat.safe(XMaterial.FIRE_CHARGE)),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item()
        };
        MissileItem napalmmissile = new MissileItem(group, napalmmissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, napalmmissilerecipe, 18, Translations.getSpecialALore("napalmmissile"));
        //</editor-fold>
        Translations.setType("advanced");
        //<editor-fold desc="ADVANCEDMISSILE">
        SlimefunItemStack advmissilestack = new SlimefunItemStack("MISSILEADV", MaterialCompat.safe(XMaterial.DIAMOND_SWORD));
        ItemStack[] advmissilerecipe = {
                null, warheadstack.item(), null,
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item(),
        };
        MissileItem advmissile = new MissileItem(group, advmissilestack, RecipeType.ENHANCED_CRAFTING_TABLE, advmissilerecipe, 19, Translations.getMissileLore("normal"));
        //</editor-fold>
        //<editor-fold desc="ADVANCEDMISSILEHE">
        SlimefunItemStack advmissileHEstack = new SlimefunItemStack("MISSILEHEADV", MaterialCompat.safe(XMaterial.DIAMOND_SWORD));
        ItemStack[] advmissileHErecipe = {
                compressedpowderstack.item(), heavywarheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                rocketfuelstack.item(), finsstack.item(), rocketfuelstack.item(),
        };
        MissileItem advmissileHE = new MissileItem(group, advmissileHEstack, RecipeType.ENHANCED_CRAFTING_TABLE, advmissileHErecipe, 20, Translations.getMissileLore("he"));
        //</editor-fold>
        //<editor-fold desc="ADVANCEDMISSILELR">
        SlimefunItemStack advmissileLRstack = new SlimefunItemStack("MISSILELRADV", MaterialCompat.safe(XMaterial.DIAMOND_SWORD));
        ItemStack[] advmissileLRrecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                SlimefunItems.BASIC_CIRCUIT_BOARD.item(), finsstack.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(),
        };
        MissileItem advmissileLR = new MissileItem(group, advmissileLRstack, RecipeType.ENHANCED_CRAFTING_TABLE, advmissileLRrecipe, 21, Translations.getMissileLore("lr"));
        //</editor-fold>
        //<editor-fold desc="ADVANCEDMISSILEAC">
        SlimefunItemStack advmissileACstack = new SlimefunItemStack("MISSILEACADV", MaterialCompat.safe(XMaterial.DIAMOND_SWORD));
        ItemStack[] advmissileACrecipe = {
                compressedpowderstack.item(), warheadstack.item(), compressedpowderstack.item(),
                rocketfuelstack.item(), advancedmissilebodystack.item(), rocketfuelstack.item(),
                finsstack.item(), SlimefunItems.BASIC_CIRCUIT_BOARD.item(), finsstack.item(),
        };
        MissileItem advmissileAC = new MissileItem(group, advmissileACstack, RecipeType.ENHANCED_CRAFTING_TABLE, advmissileACrecipe, 22, Translations.getMissileLore("ac"));
        //</editor-fold>
        //</editor-fold>

        //<editor-fold desc="== Declare Guide Categories ==">
        smallwarhead.setGuideType("weapons");
        warhead.setGuideType("weapons");
        warheadAP.setGuideType("weapons");
        heavywarhead.setGuideType("weapons");
        mine.setGuideType("weapons");
        manpad.setGuideType("weapons");
        groundlauncher.setGuideType("machines");
        antiairlauncher.setGuideType("machines");
        antiElytraLauncher.setGuideType("machines");
        sugarfuel.setGuideType("resources");
        rocketfuel.setGuideType("resources");
        explosivepowder.setGuideType("resources");
        compressedpowder.setGuideType("resources");
        ultraliteingot.setGuideType("resources");
        ultraliteplate.setGuideType("resources");
        simpleflightcomputer.setGuideType("resources");
        radar.setGuideType("resources");
        smallbody.setGuideType("resources");
        missilebody.setGuideType("resources");
        advancedmissilebody.setGuideType("resources");
        icbmmissilebody.setGuideType("resources");
        chlorine.setGuideType("resources");
        chlorinepellet.setGuideType("resources");
        playerList.setGuideType("resources");
        //</editor-fold>

        //<editor-fold desc="== Register Items ==">
        guide.register(main);
        playerList.register(main);
        groundlauncher.register(main);
        antiElytraLauncher.register(main);
        antiairlauncher.register(main);
        ItemStack[] missileradarrecipe = new ItemStack[]{
                simpleflightcomputerstack.item(), ultraliteplatestack.item(), simpleflightcomputerstack.item(),
                ultraliteplatestack.item(), radarstack.item(), ultraliteplatestack.item(),
                simpleflightcomputerstack.item(), ultraliteplatestack.item(), simpleflightcomputerstack.item(),
        };
        new MissileRadar(group, missileradarrecipe).setGuideType("machines").register(main);
        mine.register(main);
        manpad.register(main);
        sugarfuel.register(main);
        explosivepowder.register(main);
        compressedpowder.register(main);
        ultraliteingot.register(main);
        ultraliteplate.register(main);
        chlorine.register(main);
        chlorinepellet.register(main);
        simpleflightcomputer.register(main);
        radar.register(main);
        rocketfuel.register(main);
        smallwarhead.register(main);
        warhead.register(main);
        warheadAP.register(main);
        smallbody.register(main);
        smallfin.register(main);
        missilebody.register(main);
        fins.register(main);
        advancedmissilebody.register(main);
        heavywarhead.register(main);
        icbmmissilebody.register(main);
        antiAirMissile.register(main);
        antielytramissile.register(main);
        smallmissile.register(main);
        smallmissileHE.register(main);
        smallmissileLR.register(main);
        smallmissileAC.register(main);
        missile.register(main);
        missileHE.register(main);
        missileLR.register(main);
        missileAC.register(main);
        missileAP.register(main);
        missileAPt.register(main);
        missileAPtr.register(main);
        missilegas.register(main);
        excavmissile.register(main);
        stickymissile.register(main);
        icbm.register(main);
        cluster.register(main);
        napalmmissile.register(main);
        advmissile.register(main);
        advmissileHE.register(main);
        advmissileLR.register(main);
        advmissileAC.register(main);

        //</editor-fold>

        //<editor-fold desc="RESEARCH">
        NamespacedKey basicfuelkey = new NamespacedKey("missilewarfare", "basic_fuel");
        Research basicfuel = new Research(basicfuelkey, 3467341, "Inedible Sugar", 10);
        basicfuel.addItems(sugarfuel);
        basicfuel.register();

        NamespacedKey explosiveskey = new NamespacedKey("missilewarfare", "explosives");
        Research explosives = new Research(explosiveskey, 3447321, "Explosive Diarrhea", 15);
        explosives.addItems(explosivepowder, compressedpowder);
        explosives.register();

        NamespacedKey chlorinekey = new NamespacedKey("missilewarfare", "chlorine");
        Research chlorineres = new Research(chlorinekey, 214141, "Cleaner Pools!", 15);
        chlorineres.addItems(chlorine, chlorinepellet);
        chlorineres.register();

        NamespacedKey groundlauncherskey = new NamespacedKey("missilewarfare", "groundlauncher");
        Research groundlauncherres = new Research(groundlauncherskey, 34117322, "Ground Missile Launcher", 15);
        groundlauncherres.addItems(groundlauncher);
        groundlauncherres.register();

        NamespacedKey antimissilekey = new NamespacedKey("missilewarfare", "antimissilelauncher");
        Research antimissileres = new Research(antimissilekey, 3424321, "Iron Dome.", 15);
        antimissileres.addItems(antiairlauncher, manpad, antiAirMissile);
        antimissileres.register();

        NamespacedKey antielytramissilekey = new NamespacedKey("missilewarfare", "antielytramissilelauncher");
        Research antielytramissileres = new Research(antielytramissilekey, 34213253, "Ender Dome?", 20);
        antielytramissileres.addItems(antiElytraLauncher, antielytramissile);
        antielytramissileres.register();

        NamespacedKey smallgmissilepartskey = new NamespacedKey("missilewarfare", "smallgmissileparts");
        Research smallgmissileparts = new Research(smallgmissilepartskey, 2667313, "Missile with extra steps", 15);
        smallgmissileparts.addItems(smallwarhead, smallbody, smallfin);
        smallgmissileparts.register();

        NamespacedKey gmissilepartskey = new NamespacedKey("missilewarfare", "gmissileparts");
        Research gmissileparts = new Research(gmissilepartskey, 2667313, "Missile with extra steps", 15);
        gmissileparts.addItems(warhead, warheadAP, missilebody, fins);
        gmissileparts.register();

        NamespacedKey smallgmissilekey = new NamespacedKey("missilewarfare", "smallgmissile");
        Research smallgmissile = new Research(smallgmissilekey, 35673323, "5 Shades Of Gray", 20);
        smallgmissile.addItems(smallmissile, smallmissileHE, smallmissileLR, smallmissileLR, smallmissileAC);
        smallgmissile.register();

        NamespacedKey advancedfuelkey = new NamespacedKey("missilewarfare", "advancedfuel");
        Research advancedfuel = new Research(advancedfuelkey, 3461423, "Advanced (and even less edible) Fuels!", 20);
        advancedfuel.addItems(rocketfuel);
        advancedfuel.register();

        NamespacedKey missilepartskey = new NamespacedKey("missilewarfare", "missileparts");
        Research missileparts = new Research(missilepartskey, 4461423, "Missile with even more steps", 25);
        missileparts.addItems(ultraliteingot, ultraliteplate, simpleflightcomputer, radar);
        missileparts.register();

        NamespacedKey gmissilekey = new NamespacedKey("missilewarfare", "gmissile");
        Research gmissile = new Research(gmissilekey, 341243, "The Colors Of The Rainbow!", 20);
        gmissile.addItems(missile, missileHE, missileLR, missileAC);

        NamespacedKey gmissileAPkey = new NamespacedKey("missilewarfare", "gmissileAP");
        Research gmissileAP = new Research(gmissileAPkey, 341246, "The Penetrator Trio", 15);
        gmissileAP.addItems(missileAP, missileAPt, missileAPtr);

        NamespacedKey gmissileGASkey = new NamespacedKey("missilewarfare", "gmissileGAS");
        Research gmissileGAS = new Research(gmissileGASkey, 341226, "One Ball Man", 15);
        gmissileGAS.addItems(missilegas);
        //</editor-fold>
    }
}

