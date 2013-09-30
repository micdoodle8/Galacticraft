package micdoodle8.mods.galacticraft.core.items;

import java.util.ArrayList;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreItems
{
    public static Item oxTankLight;
    public static Item oxTankMedium;
    public static Item oxTankHeavy;
    public static Item oxMask;
    public static Item rocketTier1;
    public static Item sensorGlasses;
    public static Item sensorLens;
    public static Item steelPickaxe;
    public static Item steelAxe;
    public static Item steelHoe;
    public static Item steelSpade;
    public static Item steelSword;
    public static Item steelHelmet;
    public static Item steelChestplate;
    public static Item steelLeggings;
    public static Item steelBoots;
    public static Item canister;
    public static Item oxygenVent;
    public static Item oxygenFan;
    public static Item oxygenConcentrator;
    public static Item bowGravity;
    public static Item rocketEngine;
    public static Item heavyPlatingTier1;
    public static Item partNoseCone;
    public static Item partFins;
    public static Item buggy;
    public static Item flag;
    public static Item oxygenGear;
    public static Item parachute;
    public static Item canvas;
    public static Item flagPole;
    public static Item oilCanister;
    public static Item fuelCanister;
    public static Item oilExtractor;
    public static Item schematic;
    public static Item fuel;
    public static Item key;
    public static Item partBuggy;
    public static Item knowledgeBook;
    public static Item basicItem;
    public static Item battery;
    public static Item infiniteBatery;

    public static EnumArmorMaterial ARMOR_SENSOR_GLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", 200, new int[] { 0, 0, 0, 0 }, 0);
    public static EnumArmorMaterial ARMOR_STEEL = EnumHelper.addArmorMaterial("steel", 30, new int[] { 3, 8, 6, 3 }, 12);
    public static EnumToolMaterial TOOL_STEEL = EnumHelper.addToolMaterial("steel", 3, 768, 5.0F, 2, 8);

    public static ArrayList<Integer> hiddenItems = new ArrayList<Integer>();

    public static void initItems()
    {
        GCCoreItems.oxTankLight = new GCCoreItemOxygenTank(GCCoreConfigManager.idItemLightOxygenTank, 1, "oxygenTankLightFull");
        GCCoreItems.oxTankMedium = new GCCoreItemOxygenTank(GCCoreConfigManager.idItemMedOxygenTank, 2, "oxygenTankMedFull");
        GCCoreItems.oxTankHeavy = new GCCoreItemOxygenTank(GCCoreConfigManager.idItemHeavyOxygenTank, 3, "oxygenTankHeavyFull");
        GCCoreItems.oxMask = new GCCoreItemOxygenMask(GCCoreConfigManager.idArmorOxygenMask, "oxygenMask");
        GCCoreItems.rocketTier1 = new GCCoreItemSpaceship(GCCoreConfigManager.idItemSpaceship, "spaceship");
        GCCoreItems.sensorGlasses = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlasses, "sensorGlasses");
        GCCoreItems.steelPickaxe = new GCCoreItemPickaxe(GCCoreConfigManager.idToolSteelPickaxe, "steel_pickaxe");
        GCCoreItems.steelAxe = new GCCoreItemAxe(GCCoreConfigManager.idToolSteelAxe, "steel_axe");
        GCCoreItems.steelHoe = new GCCoreItemHoe(GCCoreConfigManager.idToolSteelHoe, "steel_hoe");
        GCCoreItems.steelSpade = new GCCoreItemSpade(GCCoreConfigManager.idToolSteelSpade, "steel_shovel");
        GCCoreItems.steelSword = new GCCoreItemSword(GCCoreConfigManager.idToolSteelSword, "steel_sword");
        GCCoreItems.steelHelmet = new GCCoreItemArmor(GCCoreConfigManager.idArmorSteelHelmet, 0, "helmet");
        GCCoreItems.steelChestplate = new GCCoreItemArmor(GCCoreConfigManager.idArmorSteelChestplate, 1, "chestplate");
        GCCoreItems.steelLeggings = new GCCoreItemArmor(GCCoreConfigManager.idArmorSteelLeggings, 2, "leggings");
        GCCoreItems.steelBoots = new GCCoreItemArmor(GCCoreConfigManager.idArmorSteelBoots, 3, "boots");
        GCCoreItems.canister = new GCCoreItemCanister(GCCoreConfigManager.idItemTinCanister, "canister");
        GCCoreItems.oxygenVent = new GCCoreItem(GCCoreConfigManager.idItemAirVent, "airVent");
        GCCoreItems.oxygenFan = new GCCoreItem(GCCoreConfigManager.idItemFan, "airFan");
        GCCoreItems.oxygenConcentrator = new GCCoreItem(GCCoreConfigManager.idItemOxygenConcentrator, "oxygenConcentrator");
        GCCoreItems.bowGravity = new GCCoreItemBow(GCCoreConfigManager.idItemGravityBow, "bow");
        GCCoreItems.heavyPlatingTier1 = new GCCoreItem(GCCoreConfigManager.idItemHeavyPlate, "heavyPlating");
        GCCoreItems.rocketEngine = new GCCoreItemRocketEngine(GCCoreConfigManager.idItemRocketEngine, "engine");
        GCCoreItems.partFins = new GCCoreItem(GCCoreConfigManager.idItemRocketFins, "rocketFins");
        GCCoreItems.partNoseCone = new GCCoreItem(GCCoreConfigManager.idItemRocketNoseCone, "noseCone");
        GCCoreItems.sensorLens = new GCCoreItem(GCCoreConfigManager.idItemSensorLens, "sensorLens");
        GCCoreItems.buggy = new GCCoreItemBuggy(GCCoreConfigManager.idItemBuggy, "buggy");
        GCCoreItems.flag = new GCCoreItemFlag(GCCoreConfigManager.idItemFlag, "flag");
        GCCoreItems.oxygenGear = new GCCoreItemOxygenGear(GCCoreConfigManager.idItemOxygenGear, "oxygenGear");
        GCCoreItems.parachute = new GCCoreItemParachute(GCCoreConfigManager.idItemParachute, "parachute");
        GCCoreItems.canvas = new GCCoreItem(GCCoreConfigManager.idItemCanvas, "canvas");
        GCCoreItems.fuelCanister = new GCCoreItemFuelCanister(GCCoreConfigManager.idItemRocketFuelBucket, "fuelCanisterPartial");
        GCCoreItems.flagPole = new GCCoreItem(GCCoreConfigManager.idItemFlagPole, "steelPole");
        GCCoreItems.oilCanister = new GCCoreItemOilCanister(GCCoreConfigManager.idItemOilCanister, "oilCanisterPartial");
        GCCoreItems.oilExtractor = new GCCoreItemOilExtractor(GCCoreConfigManager.idItemOilExtractor, "oilExtractor");
        GCCoreItems.schematic = new GCCoreItemSchematic(GCCoreConfigManager.idItemSchematic, "schematic");
        GCCoreItems.fuel = new GCCoreItemFuel(GCCoreConfigManager.idItemFuel, "fuel");
        GCCoreItems.key = new GCCoreItemKey(GCCoreConfigManager.idItemKey, "key");
        GCCoreItems.partBuggy = new GCCoreItemBuggyMaterial(GCCoreConfigManager.idItemBuggyMaterial, "buggymat");
        GCCoreItems.knowledgeBook = new GCCoreItemKnowledgeBook(GCCoreConfigManager.idItemKnowledgeBook, "knowledgeBook");
        GCCoreItems.basicItem = new GCCoreItemBasic(GCCoreConfigManager.idItemBasic, "basicItem");
        GCCoreItems.battery = new GCCoreItemBattery(GCCoreConfigManager.idItemBattery, "battery");
        GCCoreItems.infiniteBatery = new GCCoreItemInfiniteBattery(GCCoreConfigManager.idItemInfiniteBattery, "infiniteBattery");

        GCCoreItems.hiddenItems.add(GCCoreItems.bowGravity.itemID);
        GCCoreItems.hiddenItems.add(GCCoreItems.knowledgeBook.itemID);

        GCCoreItems.registerHarvestLevels();
        
        GCCoreUtil.registerGalacticraftItem("oxygenTankLightFull", GCCoreItems.oxTankLight);
        GCCoreUtil.registerGalacticraftItem("oxygenTankMediumFull", GCCoreItems.oxTankMedium);
        GCCoreUtil.registerGalacticraftItem("oxygenTankHeavyFull", GCCoreItems.oxTankHeavy);
        GCCoreUtil.registerGalacticraftItem("oxygenTankLightEmpty", GCCoreItems.oxTankLight, 90);
        GCCoreUtil.registerGalacticraftItem("oxygenTankMediumEmpty", GCCoreItems.oxTankMedium, 180);
        GCCoreUtil.registerGalacticraftItem("oxygenTankHeavyEmpty", GCCoreItems.oxTankHeavy, 270);
        GCCoreUtil.registerGalacticraftItem("oxygenMask", GCCoreItems.oxMask);
        GCCoreUtil.registerGalacticraftItem("rocketTier1", GCCoreItems.rocketTier1, 0);
        GCCoreUtil.registerGalacticraftItem("rocketTier1_18cargo", GCCoreItems.rocketTier1, 1);
        GCCoreUtil.registerGalacticraftItem("rocketTier1_36cargo", GCCoreItems.rocketTier1, 2);
        GCCoreUtil.registerGalacticraftItem("rocketTier1_54cargo", GCCoreItems.rocketTier1, 3);
        GCCoreUtil.registerGalacticraftItem("rocketTier1_prefueled", GCCoreItems.rocketTier1, 4);
        GCCoreUtil.registerGalacticraftItem("heavyDutyPickaxe", GCCoreItems.steelPickaxe);
        GCCoreUtil.registerGalacticraftItem("heavyDutyShovel", GCCoreItems.steelSpade);
        GCCoreUtil.registerGalacticraftItem("heavyDutyAxe", GCCoreItems.steelAxe);
        GCCoreUtil.registerGalacticraftItem("heavyDutyHoe", GCCoreItems.steelHoe);
        GCCoreUtil.registerGalacticraftItem("heavyDutySword", GCCoreItems.steelSword);
        GCCoreUtil.registerGalacticraftItem("heavyDutyHelmet", GCCoreItems.steelHelmet);
        GCCoreUtil.registerGalacticraftItem("heavyDutyChestplate", GCCoreItems.steelChestplate);
        GCCoreUtil.registerGalacticraftItem("heavyDutyLeggings", GCCoreItems.steelLeggings);
        GCCoreUtil.registerGalacticraftItem("heavyDutyBoots", GCCoreItems.steelBoots);
        GCCoreUtil.registerGalacticraftItem("tinCanister", GCCoreItems.canister, 0);
        GCCoreUtil.registerGalacticraftItem("copperCanister", GCCoreItems.canister, 1);
        GCCoreUtil.registerGalacticraftItem("oxygenVent", GCCoreItems.oxygenVent);
        GCCoreUtil.registerGalacticraftItem("oxygenFan", GCCoreItems.oxygenFan);
        GCCoreUtil.registerGalacticraftItem("oxygenConcentrator", GCCoreItems.oxygenConcentrator);
        GCCoreUtil.registerGalacticraftItem("heavyPlatingTier1", GCCoreItems.heavyPlatingTier1);
        GCCoreUtil.registerGalacticraftItem("rocketEngineTier1", GCCoreItems.rocketEngine, 0);
        GCCoreUtil.registerGalacticraftItem("rocketBoosterTier1", GCCoreItems.rocketEngine, 1);
        GCCoreUtil.registerGalacticraftItem("rocketFins", GCCoreItems.partFins);
        GCCoreUtil.registerGalacticraftItem("rocketNoseCone", GCCoreItems.partNoseCone);
        GCCoreUtil.registerGalacticraftItem("sensorLens", GCCoreItems.sensorLens);
        GCCoreUtil.registerGalacticraftItem("moonBuggy", GCCoreItems.buggy, 0);
        GCCoreUtil.registerGalacticraftItem("moonBuggy_18cargo", GCCoreItems.buggy, 1);
        GCCoreUtil.registerGalacticraftItem("moonBuggy_36cargo", GCCoreItems.buggy, 2);
        GCCoreUtil.registerGalacticraftItem("moonBuggy_54cargo", GCCoreItems.buggy, 3);
        GCCoreUtil.registerGalacticraftItem("flagAmerican", GCCoreItems.flag, 0);
        GCCoreUtil.registerGalacticraftItem("flagBlack", GCCoreItems.flag, 1);
        GCCoreUtil.registerGalacticraftItem("flagLightBlue", GCCoreItems.flag, 2);
        GCCoreUtil.registerGalacticraftItem("flagLime", GCCoreItems.flag, 3);
        GCCoreUtil.registerGalacticraftItem("flagBrown", GCCoreItems.flag, 4);
        GCCoreUtil.registerGalacticraftItem("flagBlue", GCCoreItems.flag, 5);
        GCCoreUtil.registerGalacticraftItem("flagGray", GCCoreItems.flag, 6);
        GCCoreUtil.registerGalacticraftItem("flagGreen", GCCoreItems.flag, 7);
        GCCoreUtil.registerGalacticraftItem("flagLightGray", GCCoreItems.flag, 8);
        GCCoreUtil.registerGalacticraftItem("flagMagenta", GCCoreItems.flag, 9);
        GCCoreUtil.registerGalacticraftItem("flagOrange", GCCoreItems.flag, 10);
        GCCoreUtil.registerGalacticraftItem("flagPink", GCCoreItems.flag, 11);
        GCCoreUtil.registerGalacticraftItem("flagPurple", GCCoreItems.flag, 12);
        GCCoreUtil.registerGalacticraftItem("flagRed", GCCoreItems.flag, 13);
        GCCoreUtil.registerGalacticraftItem("flagCyan", GCCoreItems.flag, 14);
        GCCoreUtil.registerGalacticraftItem("flagYellow", GCCoreItems.flag, 15);
        GCCoreUtil.registerGalacticraftItem("flagWhite", GCCoreItems.flag, 16);
        GCCoreUtil.registerGalacticraftItem("oxygenGear", GCCoreItems.oxygenGear);
        GCCoreUtil.registerGalacticraftItem("parachuteWhite", GCCoreItems.parachute, 0);
        GCCoreUtil.registerGalacticraftItem("parachuteBlack", GCCoreItems.parachute, 1);
        GCCoreUtil.registerGalacticraftItem("parachuteLightBlue", GCCoreItems.parachute, 2);
        GCCoreUtil.registerGalacticraftItem("parachuteLime", GCCoreItems.parachute, 3);
        GCCoreUtil.registerGalacticraftItem("parachuteBrown", GCCoreItems.parachute, 4);
        GCCoreUtil.registerGalacticraftItem("parachuteBlue", GCCoreItems.parachute, 5);
        GCCoreUtil.registerGalacticraftItem("parachuteGray", GCCoreItems.parachute, 6);
        GCCoreUtil.registerGalacticraftItem("parachuteGreen", GCCoreItems.parachute, 7);
        GCCoreUtil.registerGalacticraftItem("parachuteLightGray", GCCoreItems.parachute, 8);
        GCCoreUtil.registerGalacticraftItem("parachutePink", GCCoreItems.parachute, 9);
        GCCoreUtil.registerGalacticraftItem("parachuteOrange", GCCoreItems.parachute, 10);
        GCCoreUtil.registerGalacticraftItem("parachutePink", GCCoreItems.parachute, 11);
        GCCoreUtil.registerGalacticraftItem("parachutePurple", GCCoreItems.parachute, 12);
        GCCoreUtil.registerGalacticraftItem("parachuteRed", GCCoreItems.parachute, 13);
        GCCoreUtil.registerGalacticraftItem("parachuteCyan", GCCoreItems.parachute, 14);
        GCCoreUtil.registerGalacticraftItem("parachuteYellow", GCCoreItems.parachute, 15);
        GCCoreUtil.registerGalacticraftItem("canvas", GCCoreItems.canvas);
        GCCoreUtil.registerGalacticraftItem("fuelCanisterFull", GCCoreItems.fuelCanister, 1);
        GCCoreUtil.registerGalacticraftItem("oilCanisterFull", GCCoreItems.oilCanister, 1);
        GCCoreUtil.registerGalacticraftItem("liquidCanisterEmpty", GCCoreItems.oilCanister, GCCoreItems.oilCanister.getMaxDamage());
        GCCoreUtil.registerGalacticraftItem("steelPole", GCCoreItems.flagPole);
        GCCoreUtil.registerGalacticraftItem("oilExtractor", GCCoreItems.oilExtractor);
        GCCoreUtil.registerGalacticraftItem("schematicMoonBuggy", GCCoreItems.schematic, 0);
        GCCoreUtil.registerGalacticraftItem("schematicRocketTier2", GCCoreItems.schematic, 1);
        GCCoreUtil.registerGalacticraftItem("tier1Key", GCCoreItems.key);
        GCCoreUtil.registerGalacticraftItem("buggyMaterialWheel", GCCoreItems.partBuggy, 0);
        GCCoreUtil.registerGalacticraftItem("buggyMaterialSeat", GCCoreItems.partBuggy, 1);
        GCCoreUtil.registerGalacticraftItem("buggyMaterialStorage", GCCoreItems.partBuggy, 2);
        GCCoreUtil.registerGalacticraftItem("solarModuleSingle", GCCoreItems.basicItem, 0);
        GCCoreUtil.registerGalacticraftItem("solarModuleFull", GCCoreItems.basicItem, 1);
        GCCoreUtil.registerGalacticraftItem("batteryEmpty", GCCoreItems.battery, 100);
        GCCoreUtil.registerGalacticraftItem("batteryFull", GCCoreItems.battery, 0);
        GCCoreUtil.registerGalacticraftItem("infiniteBattery", GCCoreItems.infiniteBatery);
        
        for (int i = 0; i < GCCoreItemBasic.names.length; i++)
        {
            if (GCCoreItemBasic.names[i].contains("ingot") || GCCoreItemBasic.names[i].contains("compressed"))
            {
                OreDictionary.registerOre(GCCoreItemBasic.names[i], new ItemStack(GCCoreItems.basicItem, 1, i));
            }
        }
    }

    public static void registerHarvestLevels()
    {
        MinecraftForge.setToolClass(GCCoreItems.steelPickaxe, "pickaxe", 4);
        MinecraftForge.setToolClass(GCCoreItems.steelAxe, "axe", 4);
        MinecraftForge.setToolClass(GCCoreItems.steelSpade, "shovel", 4);
    }
}
