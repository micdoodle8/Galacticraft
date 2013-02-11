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
	public static Item aluminumCanister;
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
	
	public static EnumArmorMaterial OXYGENMASK = EnumHelper.addArmorMaterial("OXYGENMASK", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial SENSORGLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial TITANIUMARMOR = EnumHelper.addArmorMaterial("TITANIUM", 42, new int[] {4, 9, 7, 4}, 12);
	public static EnumToolMaterial TOOLTITANIUM = EnumHelper.addToolMaterial("TITANIUM", 4, 768, 7.0F, 3, 8);
	
	public static void initItems() 
	{
		lightOxygenTankFull = (GCCoreItemOxygenTank) new GCCoreItemOxygenTank(GCCoreConfigManager.idItemLightOxygenTank).setTankTier(EnumOxygenTankTier.light).setMaxDamage(90).setIconIndex(0).setItemName("oxygenTankLightFull");
		medOxygenTankFull = (GCCoreItemOxygenTank) new GCCoreItemOxygenTank(GCCoreConfigManager.idItemMedOxygenTank).setTankTier(EnumOxygenTankTier.medium).setMaxDamage(90).setIconIndex(1).setItemName("oxygenTankMedFull");
		heavyOxygenTankFull = (GCCoreItemOxygenTank) new GCCoreItemOxygenTank(GCCoreConfigManager.idItemHeavyOxygenTank).setTankTier(EnumOxygenTankTier.heavy).setMaxDamage(90).setIconIndex(2).setItemName("oxygenTankHeavyFull");
		oxygenMask = new GCCoreItemOxygenMask(GCCoreConfigManager.idArmorOxygenMask, OXYGENMASK, 5, 0).setIconIndex(6).setItemName("oxygenMask");
		spaceship = new GCCoreItemSpaceship(GCCoreConfigManager.idItemSpaceship).setItemName("spaceship");
		sensorGlasses = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlasses, SENSORGLASSES, 6, 0, false).setIconIndex(7).setItemName("sensorGlasses");
//		sensorGlassesWithOxygenMask = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlassesWithOxygenMask, SENSORGLASSES, 7, 0, true).setIconIndex(8).setItemName("sensorGlassesWithOxygenMask");
		titaniumPickaxe = new GCCoreItemPickaxe(GCCoreConfigManager.idToolTitaniumPickaxe, TOOLTITANIUM).setIconIndex(11).setItemName("titaniumPick");
		titaniumAxe = new GCCoreItemAxe(GCCoreConfigManager.idToolTitaniumAxe, TOOLTITANIUM).setIconIndex(10).setItemName("titaniumAxe");
		titaniumHoe = new GCCoreItemHoe(GCCoreConfigManager.idToolTitaniumHoe, TOOLTITANIUM).setIconIndex(9).setItemName("titaniumHoe");
		titaniumSpade = new GCCoreItemSpade(GCCoreConfigManager.idToolTitaniumSpade, TOOLTITANIUM).setIconIndex(12).setItemName("titaniumSpade");
		titaniumSword = new GCCoreItemSword(GCCoreConfigManager.idToolTitaniumSword, TOOLTITANIUM).setIconIndex(13).setItemName("titaniumSword");
		titaniumHelmet = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumHelmet, TITANIUMARMOR, 9, 0, false).setIconIndex(14).setItemName("titaniumHelmet");
		titaniumChestplate = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumChestplate, TITANIUMARMOR, 9, 1, false).setIconIndex(15).setItemName("titaniumChestplate");
		titaniumLeggings = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumLeggings, TITANIUMARMOR, 9, 2, false).setIconIndex(16).setItemName("titaniumLeggings");
		titaniumBoots = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumBoots, TITANIUMARMOR, 9, 3, false).setIconIndex(17).setItemName("titaniumBoots");
//		titaniumHelmetBreathable = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumHelmetBreathable, TITANIUMARMOR, 9, 0, true).setIconIndex(18).setItemName("titaniumHelmetBreathable");
		ingotTitanium = new GCCoreItem(GCCoreConfigManager.idItemIngotTitanium).setIconIndex(19).setItemName("ingotTitanium");
		ingotCopper = new GCCoreItem(GCCoreConfigManager.idItemIngotCopper).setIconIndex(20).setItemName("ingotCopper");
		ingotAluminum = new GCCoreItem(GCCoreConfigManager.idItemIngotAluminum).setIconIndex(21).setItemName("ingotAluminum");
		aluminumCanister = new GCCoreItem(GCCoreConfigManager.idItemAluminumCanister).setIconIndex(22).setItemName("aluminumCanister");
		airVent = new GCCoreItem(GCCoreConfigManager.idItemAirVent).setIconIndex(23).setItemName("airVent");
		airFan = new GCCoreItem(GCCoreConfigManager.idItemFan).setIconIndex(24).setItemName("airFan");
		oxygenConcentrator = new GCCoreItem(GCCoreConfigManager.idItemOxygenConcentrator).setIconIndex(25).setItemName("oxygenConcentrator");
		gravityBow = new GCCoreItemBow(GCCoreConfigManager.idItemGravityBow).setIconCoord(5, 1).setItemName("bow");
		heavyPlating = new GCCoreItem(GCCoreConfigManager.idItemHeavyPlate).setIconIndex(26).setItemName("heavyPlating");
		rocketEngine = new GCCoreItem(GCCoreConfigManager.idItemRocketEngine).setIconIndex(27).setItemName("rocketEngine");
		rocketFins = new GCCoreItem(GCCoreConfigManager.idItemRocketFins).setIconIndex(28).setItemName("rocketFins");
		rocketNoseCone = new GCCoreItem(GCCoreConfigManager.idItemRocketNoseCone).setIconIndex(29).setItemName("noseCone");
		sensorLens = new GCCoreItem(GCCoreConfigManager.idItemSensorLens).setIconIndex(31).setItemName("sensorLens");
		buggy = new GCCoreItemBuggy(GCCoreConfigManager.idItemBuggy).setIconIndex(200).setItemName("buggy");
		flag = new GCCoreItemFlag(GCCoreConfigManager.idItemFlag).setIconIndex(201).setItemName("flag");
		oxygenGear = new GCCoreItemOxygenGear(GCCoreConfigManager.idItemOxygenGear).setIconIndex(32).setItemName("oxygenGear");
		parachute = new GCCoreItemParachute(GCCoreConfigManager.idItemParachute).setItemName("parachute");
		canvas = new GCCoreItem(GCCoreConfigManager.idItemCanvas).setIconIndex(33).setItemName("canvas");
		rocketFuelBucket = new GCCoreItem(GCCoreConfigManager.idItemRocketFuelBucket).setIconIndex(34).setItemName("rocketFuel");
	}
	
	public static void registerHarvestLevels()
	{
		MinecraftForge.setToolClass(titaniumPickaxe, "pickaxe", 4);
		MinecraftForge.setToolClass(titaniumAxe, "axe", 4);
		MinecraftForge.setToolClass(titaniumSpade, "shovel", 4);
	}

	public static void addNames() 
	{
		addName(oxygenMask);
		addName(spaceship);
		addName(lightOxygenTankFull);
		addName(medOxygenTankFull);
		addName(heavyOxygenTankFull);
		addName(sensorGlasses);
//		addName(sensorGlassesWithOxygenMask);
		addName(titaniumPickaxe);
		addName(titaniumAxe);
		addName(titaniumSpade);
		addName(titaniumHoe);
		addName(titaniumSword);
		addName(titaniumHelmet);
		addName(titaniumChestplate);
		addName(titaniumLeggings);
		addName(titaniumBoots);
//		addName(titaniumHelmetBreathable);
		addName(ingotTitanium);
		addName(ingotCopper);
		addName(ingotAluminum);
		addName(aluminumCanister);
		addName(airVent);
		addName(airFan);
		addName(oxygenConcentrator);
		addName(rocketEngine);
		addName(heavyPlating);
		addName(rocketNoseCone);
		addName(rocketFins);
		addName(sensorLens);
		addName(oxygenGear);
		addName(flag);
		addName(canvas);
		addName(rocketFuelBucket);
		
		int i = 0;

		for (i = 0; i < GCCoreItemFlag.names.length; i++)
		{
			String s = GCCoreItemFlag.names[i];
			
	        addNameWithMetadata("item.flag." + s + ".name");
		}

		for (i = 0; i < GCCoreItemParachute.names.length; i++)
		{
			String s = GCCoreItemParachute.names[i];
			
	        addNameWithMetadata("item.parachute." + s + ".name");
		}
	}
	
	private static void addName(Item item)
	{
        LanguageRegistry.instance().addStringLocalization(item.getItemName() + ".name", GalacticraftCore.lang.get(item.getItemName() + ".name"));
	}

	private static void addNameWithMetadata(String string)
	{
        LanguageRegistry.instance().addStringLocalization(string, GalacticraftCore.lang.get(string));
	}
}
