package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItems
{
	public static GCCoreItemOxygenTank lightOxygenTankFull;
	public static GCCoreItemOxygenTank medOxygenTankFull;
	public static GCCoreItemOxygenTank heavyOxygenTankFull;
	public static Item oxygenMask;
	public static Item spaceship;
	public static Item sensorGlasses;
//	public static Item sensorGlassesWithOxygenMask;
	public static Item sensorLens;
	
	public static Item titaniumPickaxe;
	public static Item titaniumAxe;
	public static Item titaniumHoe;
	public static Item titaniumSpade;
	public static Item titaniumSword;
	public static Item titaniumHelmet;
	public static Item titaniumChestplate;
	public static Item titaniumLeggings;
	public static Item titaniumBoots;
//	public static Item titaniumHelmetBreathable;
	public static Item ingotTitanium;
	public static Item ingotCopper;
	public static Item ingotAluminum;
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
	
	public static EnumArmorMaterial OXYGENMASK = EnumHelper.addArmorMaterial("OXYGENMASK", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial SENSORGLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial TITANIUMARMOR = EnumHelper.addArmorMaterial("TITANIUM", 42, new int[] {4, 9, 7, 4}, 12);
	public static EnumToolMaterial TOOLTITANIUM = EnumHelper.addToolMaterial("TITANIUM", 4, 768, 7.0F, 3, 8);
	
	public static void initItems()
	{
		GCCoreItems.lightOxygenTankFull = (GCCoreItemOxygenTank) new GCCoreItemOxygenTank(GCCoreConfigManager.idItemLightOxygenTank).setMaxDamage(90).setIconIndex(0).setItemName("oxygenTankLightFull");
		GCCoreItems.medOxygenTankFull = (GCCoreItemOxygenTank) new GCCoreItemOxygenTank(GCCoreConfigManager.idItemMedOxygenTank).setMaxDamage(180).setIconIndex(1).setItemName("oxygenTankMedFull");
		GCCoreItems.heavyOxygenTankFull = (GCCoreItemOxygenTank) new GCCoreItemOxygenTank(GCCoreConfigManager.idItemHeavyOxygenTank).setMaxDamage(270).setIconIndex(2).setItemName("oxygenTankHeavyFull");
		GCCoreItems.oxygenMask = new GCCoreItemOxygenMask(GCCoreConfigManager.idArmorOxygenMask).setIconIndex(6).setItemName("oxygenMask");
		GCCoreItems.spaceship = new GCCoreItemSpaceship(GCCoreConfigManager.idItemSpaceship).setItemName("spaceship");
		GCCoreItems.sensorGlasses = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlasses, GCCoreItems.SENSORGLASSES, 6, 0, false).setIconIndex(7).setItemName("sensorGlasses");
//		sensorGlassesWithOxygenMask = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlassesWithOxygenMask, SENSORGLASSES, 7, 0, true).setIconIndex(8).setItemName("sensorGlassesWithOxygenMask");
		GCCoreItems.titaniumPickaxe = new GCCoreItemPickaxe(GCCoreConfigManager.idToolTitaniumPickaxe, GCCoreItems.TOOLTITANIUM).setIconIndex(11).setItemName("titaniumPick");
		GCCoreItems.titaniumAxe = new GCCoreItemAxe(GCCoreConfigManager.idToolTitaniumAxe, GCCoreItems.TOOLTITANIUM).setIconIndex(10).setItemName("titaniumAxe");
		GCCoreItems.titaniumHoe = new GCCoreItemHoe(GCCoreConfigManager.idToolTitaniumHoe, GCCoreItems.TOOLTITANIUM).setIconIndex(9).setItemName("titaniumHoe");
		GCCoreItems.titaniumSpade = new GCCoreItemSpade(GCCoreConfigManager.idToolTitaniumSpade, GCCoreItems.TOOLTITANIUM).setIconIndex(12).setItemName("titaniumSpade");
		GCCoreItems.titaniumSword = new GCCoreItemSword(GCCoreConfigManager.idToolTitaniumSword, GCCoreItems.TOOLTITANIUM).setIconIndex(13).setItemName("titaniumSword");
		GCCoreItems.titaniumHelmet = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumHelmet, GCCoreItems.TITANIUMARMOR, 9, 0, false).setIconIndex(14).setItemName("titaniumHelmet");
		GCCoreItems.titaniumChestplate = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumChestplate, GCCoreItems.TITANIUMARMOR, 9, 1, false).setIconIndex(15).setItemName("titaniumChestplate");
		GCCoreItems.titaniumLeggings = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumLeggings, GCCoreItems.TITANIUMARMOR, 9, 2, false).setIconIndex(16).setItemName("titaniumLeggings");
		GCCoreItems.titaniumBoots = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumBoots, GCCoreItems.TITANIUMARMOR, 9, 3, false).setIconIndex(17).setItemName("titaniumBoots");
//		titaniumHelmetBreathable = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumHelmetBreathable, TITANIUMARMOR, 9, 0, true).setIconIndex(18).setItemName("titaniumHelmetBreathable");
		GCCoreItems.ingotTitanium = new GCCoreItem(GCCoreConfigManager.idItemIngotTitanium).setIconIndex(19).setItemName("ingotTitanium");
		GCCoreItems.ingotCopper = new GCCoreItem(GCCoreConfigManager.idItemIngotCopper).setIconIndex(20).setItemName("ingotCopper");
		GCCoreItems.ingotAluminum = new GCCoreItem(GCCoreConfigManager.idItemIngotAluminum).setIconIndex(21).setItemName("ingotAluminium");
		GCCoreItems.canister = new GCCoreItemCanister(GCCoreConfigManager.idItemAluminumCanister).setItemName("canister");
		GCCoreItems.airVent = new GCCoreItem(GCCoreConfigManager.idItemAirVent).setIconIndex(23).setItemName("airVent");
		GCCoreItems.airFan = new GCCoreItem(GCCoreConfigManager.idItemFan).setIconIndex(24).setItemName("airFan");
		GCCoreItems.oxygenConcentrator = new GCCoreItem(GCCoreConfigManager.idItemOxygenConcentrator).setIconIndex(25).setItemName("oxygenConcentrator");
		GCCoreItems.gravityBow = new GCCoreItemBow(GCCoreConfigManager.idItemGravityBow).setIconCoord(5, 1).setItemName("bow");
		GCCoreItems.heavyPlating = new GCCoreItem(GCCoreConfigManager.idItemHeavyPlate).setIconIndex(26).setItemName("heavyPlating");
		GCCoreItems.rocketEngine = new GCCoreItem(GCCoreConfigManager.idItemRocketEngine).setIconIndex(27).setItemName("rocketEngine");
		GCCoreItems.rocketFins = new GCCoreItem(GCCoreConfigManager.idItemRocketFins).setIconIndex(28).setItemName("rocketFins");
		GCCoreItems.rocketNoseCone = new GCCoreItem(GCCoreConfigManager.idItemRocketNoseCone).setIconIndex(29).setItemName("noseCone");
		GCCoreItems.sensorLens = new GCCoreItem(GCCoreConfigManager.idItemSensorLens).setIconIndex(31).setItemName("sensorLens");
		GCCoreItems.buggy = new GCCoreItemBuggy(GCCoreConfigManager.idItemBuggy).setIconIndex(200).setItemName("buggy");
		GCCoreItems.flag = new GCCoreItemFlag(GCCoreConfigManager.idItemFlag).setIconIndex(201).setItemName("flag");
		GCCoreItems.oxygenGear = new GCCoreItemOxygenGear(GCCoreConfigManager.idItemOxygenGear).setIconIndex(32).setItemName("oxygenGear");
		GCCoreItems.parachute = new GCCoreItemParachute(GCCoreConfigManager.idItemParachute).setItemName("parachute");
		GCCoreItems.canvas = new GCCoreItem(GCCoreConfigManager.idItemCanvas).setIconIndex(33).setItemName("canvas");
		GCCoreItems.rocketFuelBucket = new GCCoreItemFuelCanister(GCCoreConfigManager.idItemRocketFuelBucket).setIconIndex(64).setItemName("fuelCanisterPartial");
		GCCoreItems.flagPole = new GCCoreItem(GCCoreConfigManager.idItemFlagPole).setIconIndex(51).setItemName("flagPole");
		GCCoreItems.oilCanister = new GCCoreItemOilCanister(GCCoreConfigManager.idItemOilCanister).setIconIndex(57).setItemName("oilCanisterPartial");
		GCCoreItems.oilExtractor = new GCCoreItemOilExtractor(GCCoreConfigManager.idItemOilExtractor).setIconIndex(52).setItemName("oilExtractor");
	}
	
	public static void registerHarvestLevels()
	{
		MinecraftForge.setToolClass(GCCoreItems.titaniumPickaxe, "pickaxe", 4);
		MinecraftForge.setToolClass(GCCoreItems.titaniumAxe, "axe", 4);
		MinecraftForge.setToolClass(GCCoreItems.titaniumSpade, "shovel", 4);
	}
}
