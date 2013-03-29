package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreItems
{
	public static Item lightOxygenTank;
	public static Item medOxygenTank;
	public static Item heavyOxygenTank;
	public static Item oxygenMask;
	public static Item spaceship;
	public static Item sensorGlasses;
//	public static Item sensorGlassesWithOxygenMask;
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
//	public static Item steelHelmetBreathable;
	public static Item canister;
	public static Item airVent;
	public static Item airFan;
	public static Item oxygenConcentrator;
	public static Item gravityBow;
	public static Item rocketEngine;
	public static Item heavyPlating;
	public static Item rocketNoseCone;
	public static Item rocketFins;
	public static Item buggy;
	public static Item flag;
	public static Item oxygenGear;
	public static Item parachute;
	public static Item canvas;
	public static Item rocketFuelBucket;
	public static Item flagPole;
	public static Item oilCanister;
	public static Item oilExtractor;

	public static EnumArmorMaterial SENSORGLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial steelARMOR = EnumHelper.addArmorMaterial("steel", 30, new int[] {3, 8, 6, 3}, 12);
	public static EnumToolMaterial TOOLsteel = EnumHelper.addToolMaterial("steel", 3, 768, 5.0F, 2, 8);

	public static void initItems()
	{
		GCCoreItems.lightOxygenTank = new GCCoreItemOxygenTank(GCCoreConfigManager.idItemLightOxygenTank).setMaxDamage(90).setUnlocalizedName("oxygenTankLightFull");
		GCCoreItems.medOxygenTank = new GCCoreItemOxygenTank(GCCoreConfigManager.idItemMedOxygenTank).setMaxDamage(180).setUnlocalizedName("oxygenTankMedFull");
		GCCoreItems.heavyOxygenTank = new GCCoreItemOxygenTank(GCCoreConfigManager.idItemHeavyOxygenTank).setMaxDamage(270).setUnlocalizedName("oxygenTankHeavyFull");
		GCCoreItems.oxygenMask = new GCCoreItemOxygenMask(GCCoreConfigManager.idArmorOxygenMask).setUnlocalizedName("oxygenMask");
		GCCoreItems.spaceship = new GCCoreItemSpaceship(GCCoreConfigManager.idItemSpaceship).setUnlocalizedName("spaceship");
		GCCoreItems.sensorGlasses = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlasses, GCCoreItems.SENSORGLASSES, 6, 0).setUnlocalizedName("sensorGlasses");
//		sensorGlassesWithOxygenMask = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlassesWithOxygenMask, SENSORGLASSES, 7, 0, true).setIconIndex(8).setUnlocalizedName("sensorGlassesWithOxygenMask");
		GCCoreItems.steelPickaxe = new GCCoreItemPickaxe(GCCoreConfigManager.idToolSteelPickaxe, GCCoreItems.TOOLsteel).setUnlocalizedName("steel_pickaxe");
		GCCoreItems.steelAxe = new GCCoreItemAxe(GCCoreConfigManager.idToolSteelAxe, GCCoreItems.TOOLsteel).setUnlocalizedName("steel_axe");
		GCCoreItems.steelHoe = new GCCoreItemHoe(GCCoreConfigManager.idToolSteelHoe, GCCoreItems.TOOLsteel).setUnlocalizedName("steel_hoe");
		GCCoreItems.steelSpade = new GCCoreItemSpade(GCCoreConfigManager.idToolSteelSpade, GCCoreItems.TOOLsteel).setUnlocalizedName("steel_shovel");
		GCCoreItems.steelSword = new GCCoreItemSword(GCCoreConfigManager.idToolSteelSword, GCCoreItems.TOOLsteel).setUnlocalizedName("steel_sword");
		GCCoreItems.steelHelmet = new GCCoreItemArmor(GCCoreConfigManager.idArmorSteelHelmet, GCCoreItems.steelARMOR, 9, 0, false).setUnlocalizedName("steel_helmet");
		GCCoreItems.steelChestplate = new GCCoreItemArmor(GCCoreConfigManager.idArmorSteelChestplate, GCCoreItems.steelARMOR, 9, 1, false).setUnlocalizedName("steel_chestplate");
		GCCoreItems.steelLeggings = new GCCoreItemArmor(GCCoreConfigManager.idArmorSteelLeggings, GCCoreItems.steelARMOR, 9, 2, false).setUnlocalizedName("steel_leggings");
		GCCoreItems.steelBoots = new GCCoreItemArmor(GCCoreConfigManager.idArmorSteelBoots, GCCoreItems.steelARMOR, 9, 3, false).setUnlocalizedName("steel_boots");
//		steelHelmetBreathable = new GCCoreItemArmor(GCCoreConfigManager.idArmorsteelHelmetBreathable, steelARMOR, 9, 0, true).setIconIndex(18).setUnlocalizedName("steelHelmetBreathable");
		GCCoreItems.canister = new GCCoreItemCanister(GCCoreConfigManager.idItemTinCanister).setUnlocalizedName("canister");
		GCCoreItems.airVent = new GCCoreItem(GCCoreConfigManager.idItemAirVent, "air_vent" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("airVent");
		GCCoreItems.airFan = new GCCoreItem(GCCoreConfigManager.idItemFan, "air_fan" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("airFan");
		GCCoreItems.oxygenConcentrator = new GCCoreItem(GCCoreConfigManager.idItemOxygenConcentrator, "air_condenser" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("oxygenConcentrator");
		GCCoreItems.gravityBow = new GCCoreItemBow(GCCoreConfigManager.idItemGravityBow).setUnlocalizedName("bow");
		GCCoreItems.heavyPlating = new GCCoreItem(GCCoreConfigManager.idItemHeavyPlate, "heavy_plating" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("heavyPlating");
		GCCoreItems.rocketEngine = new GCCoreItem(GCCoreConfigManager.idItemRocketEngine, "rocket_engine" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("rocketEngine");
		GCCoreItems.rocketFins = new GCCoreItem(GCCoreConfigManager.idItemRocketFins, "rocket_fin" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("rocketFins");
		GCCoreItems.rocketNoseCone = new GCCoreItem(GCCoreConfigManager.idItemRocketNoseCone, "rocket_nose_cone" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("noseCone");
		GCCoreItems.sensorLens = new GCCoreItem(GCCoreConfigManager.idItemSensorLens, "sensor_lens" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("sensorLens");
		GCCoreItems.buggy = new GCCoreItemBuggy(GCCoreConfigManager.idItemBuggy).setUnlocalizedName("buggy");
		GCCoreItems.flag = new GCCoreItemFlag(GCCoreConfigManager.idItemFlag).setUnlocalizedName("flag");
		GCCoreItems.oxygenGear = new GCCoreItemOxygenGear(GCCoreConfigManager.idItemOxygenGear).setUnlocalizedName("oxygenGear");
		GCCoreItems.parachute = new GCCoreItemParachute(GCCoreConfigManager.idItemParachute).setUnlocalizedName("parachute");
		GCCoreItems.canvas = new GCCoreItem(GCCoreConfigManager.idItemCanvas, "canvas" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("canvas");
		GCCoreItems.rocketFuelBucket = new GCCoreItemFuelCanister(GCCoreConfigManager.idItemRocketFuelBucket).setUnlocalizedName("fuelCanisterPartial");
		GCCoreItems.flagPole = new GCCoreItem(GCCoreConfigManager.idItemFlagPole, "flagpole" + GalacticraftCore.TEXTURE_SUFFIX).setUnlocalizedName("flagPole");
		GCCoreItems.oilCanister = new GCCoreItemOilCanister(GCCoreConfigManager.idItemOilCanister).setUnlocalizedName("oilCanisterPartial");
		GCCoreItems.oilExtractor = new GCCoreItemOilExtractor(GCCoreConfigManager.idItemOilExtractor).setUnlocalizedName("oilExtractor");
		
		GalacticraftCore.hiddenItems.add(GCCoreItems.gravityBow.itemID);
	}

	public static void registerHarvestLevels()
	{
		MinecraftForge.setToolClass(GCCoreItems.steelPickaxe, "pickaxe", 4);
		MinecraftForge.setToolClass(GCCoreItems.steelAxe, "axe", 4);
		MinecraftForge.setToolClass(GCCoreItems.steelSpade, "shovel", 4);
	}
}
