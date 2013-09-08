package micdoodle8.mods.galacticraft.core.items;

import java.util.ArrayList;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;

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
    public static Item unused_knowledgeBook;
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
        GCCoreItems.unused_knowledgeBook = new GCCoreItemKnowledgeBook(GCCoreConfigManager.idItemKnowledgeBook, "knowledgeBook");
        GCCoreItems.basicItem = new GCCoreItemBasic(GCCoreConfigManager.idItemBasic, "basicItem");
        GCCoreItems.battery = new GCCoreItemBattery(GCCoreConfigManager.idItemBattery, "battery");
        GCCoreItems.infiniteBatery = new GCCoreItemInfiniteBattery(GCCoreConfigManager.idItemInfiniteBattery, "infiniteBattery");

        GCCoreItems.hiddenItems.add(GCCoreItems.bowGravity.itemID);
        GCCoreItems.hiddenItems.add(GCCoreItems.unused_knowledgeBook.itemID);

        GCCoreItems.registerHarvestLevels();
    }

    public static void registerHarvestLevels()
    {
        MinecraftForge.setToolClass(GCCoreItems.steelPickaxe, "pickaxe", 4);
        MinecraftForge.setToolClass(GCCoreItems.steelAxe, "axe", 4);
        MinecraftForge.setToolClass(GCCoreItems.steelSpade, "shovel", 4);
    }
}
