package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.Item;
import net.minecraftforge.common.EnumHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreItems 
{
	public static Item lightOxygenTankFull;
	public static Item medOxygenTankFull;
	public static Item heavyOxygenTankFull;
	public static Item lightOxygenTankEmpty;
	public static Item medOxygenTankEmpty;
	public static Item heavyOxygenTankEmpty;
	public static Item oxygenMask;
	public static Item spaceship;
	public static Item sensorGlasses;
	public static Item sensorGlassesWithOxygenMask;
	
	public static Item titaniumPickaxe;
	public static Item titaniumAxe;
	public static Item titaniumHoe;
	public static Item titaniumSpade;
	public static Item titaniumSword;
	public static Item titaniumHelmet;
	public static Item titaniumChestplate;
	public static Item titaniumLeggings;
	public static Item titaniumBoots;
	public static Item titaniumHelmetBreathable;
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
	
	public static EnumArmorMaterial OXYGENMASK = EnumHelper.addArmorMaterial("OXYGENMASK", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial SENSORGLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial TITANIUMARMOR = EnumHelper.addArmorMaterial("TITANIUM", 42, new int[] {4, 9, 7, 4}, 12);
	public static EnumToolMaterial TOOLTITANIUM = EnumHelper.addToolMaterial("TITANIUM", 0, 0, 0F, 0, 0);
	
	public static void initItems() 
	{
		lightOxygenTankFull = new GCCoreItemOxygenTank(GCCoreConfigManager.idItemLightOxygenTank).setMaxDamage(90).setIconIndex(0).setItemName("oxygenTankLightFull");
		lightOxygenTankEmpty = new GCCoreItem(GCCoreConfigManager.idItemLightOxygenTankEmpty).setIconIndex(3).setItemName("oxygenTankLightEmpty");
		medOxygenTankFull = new GCCoreItemOxygenTank(GCCoreConfigManager.idItemMedOxygenTank).setMaxDamage(90).setIconIndex(1).setItemName("oxygenTankMedFull");
		medOxygenTankEmpty = new GCCoreItem(GCCoreConfigManager.idItemMedOxygenTankEmpty).setIconIndex(4).setItemName("oxygenTankMedEmpty");
		heavyOxygenTankFull = new GCCoreItemOxygenTank(GCCoreConfigManager.idItemHeavyOxygenTank).setMaxDamage(90).setIconIndex(2).setItemName("oxygenTankHeavyFull");
		heavyOxygenTankEmpty = new GCCoreItem(GCCoreConfigManager.idItemHeavyOxygenTankEmpty).setIconIndex(5).setItemName("oxygenTankHeavyEmpty");
		oxygenMask = new GCCoreItemOxygenMask(GCCoreConfigManager.idArmorOxygenMask, OXYGENMASK, 5, 0).setIconIndex(6).setItemName("oxygenMask");
		spaceship = new GCCoreItemSpaceship(GCCoreConfigManager.idItemSpaceship).setIconIndex(8).setItemName("spaceship");
		sensorGlasses = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlasses, SENSORGLASSES, 6, 0, false).setIconIndex(7).setItemName("sensorGlasses");
		sensorGlassesWithOxygenMask = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlassesWithOxygenMask, SENSORGLASSES, 7, 0, true).setIconIndex(8).setItemName("sensorGlassesWithOxygenMask");
		titaniumPickaxe = new GCCoreItemPickaxe(GCCoreConfigManager.idToolTitaniumPickaxe, TOOLTITANIUM).setIconIndex(11).setItemName("titaniumPick");
		titaniumAxe = new GCCoreItemAxe(GCCoreConfigManager.idToolTitaniumAxe, TOOLTITANIUM).setIconIndex(10).setItemName("titaniumAxe");
		titaniumHoe = new GCCoreItemHoe(GCCoreConfigManager.idToolTitaniumHoe, TOOLTITANIUM).setIconIndex(9).setItemName("titaniumHoe");
		titaniumSpade = new GCCoreItemSpade(GCCoreConfigManager.idToolTitaniumSpade, TOOLTITANIUM).setIconIndex(12).setItemName("titaniumSpade");
		titaniumSword = new GCCoreItemSword(GCCoreConfigManager.idToolTitaniumSword, TOOLTITANIUM).setIconIndex(13).setItemName("titaniumSword");
		titaniumHelmet = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumHelmet, TITANIUMARMOR, 9, 0, false).setIconIndex(14).setItemName("titaniumHelmet");
		titaniumChestplate = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumChestplate, TITANIUMARMOR, 9, 1, false).setIconIndex(15).setItemName("titaniumChestplate");
		titaniumLeggings = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumLeggings, TITANIUMARMOR, 9, 2, false).setIconIndex(16).setItemName("titaniumLeggings");
		titaniumBoots = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumBoots, TITANIUMARMOR, 9, 3, false).setIconIndex(17).setItemName("titaniumBoots");
		titaniumHelmetBreathable = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumHelmetBreathable, TITANIUMARMOR, 9, 0, true).setIconIndex(18).setItemName("titaniumHelmetBreathable");
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
	}

	public static void addNames() 
	{
		addName(oxygenMask);
		addName(spaceship);
		addName(lightOxygenTankFull);
		addName(lightOxygenTankEmpty);
		addName(medOxygenTankFull);
		addName(medOxygenTankEmpty);
		addName(heavyOxygenTankFull);
		addName(heavyOxygenTankEmpty);
		addName(sensorGlasses);
		addName(sensorGlassesWithOxygenMask);
		addName(titaniumPickaxe);
		addName(titaniumAxe);
		addName(titaniumSpade);
		addName(titaniumHoe);
		addName(titaniumSword);
		addName(titaniumHelmet);
		addName(titaniumChestplate);
		addName(titaniumLeggings);
		addName(titaniumBoots);
		addName(titaniumHelmetBreathable);
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
	}
	
	private static void addName(Item item)
	{
        LanguageRegistry.instance().addStringLocalization(item.getItemName() + ".name", GalacticraftCore.lang.get(item.getItemName() + ".name"));
	}
}
