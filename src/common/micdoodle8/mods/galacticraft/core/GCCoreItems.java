package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.GCCoreItemOxygenMask;
import micdoodle8.mods.galacticraft.core.GCCoreItemOxygenTank;
import micdoodle8.mods.galacticraft.core.GCCoreItemSensorGlasses;
import net.minecraft.src.Block;
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
		oxygenMask = new GCCoreItemOxygenMask(GCCoreConfigManager.idArmorOxygenMask, OXYGENMASK, 5, 0).setIconIndex(7).setItemName("oxygenMask");
		spaceship = new GCCoreItemSpaceship(GCCoreConfigManager.idItemSpaceship).setIconIndex(8).setItemName("spaceship");
		sensorGlasses = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlasses, SENSORGLASSES, 6, 0, false).setIconIndex(9).setItemName("sensorGlasses");
		sensorGlassesWithOxygenMask = new GCCoreItemSensorGlasses(GCCoreConfigManager.idArmorSensorGlassesWithOxygenMask, SENSORGLASSES, 7, 0, true).setIconIndex(10).setItemName("sensorGlassesWithOxygenMask");
		titaniumPickaxe = new GCCoreItemPickaxe(GCCoreConfigManager.idToolTitaniumPickaxe, TOOLTITANIUM).setIconIndex(32).setItemName("titaniumPick");
		titaniumAxe = new GCCoreItemAxe(GCCoreConfigManager.idToolTitaniumAxe, TOOLTITANIUM).setIconIndex(33).setItemName("titaniumAxe");
		titaniumHoe = new GCCoreItemHoe(GCCoreConfigManager.idToolTitaniumHoe, TOOLTITANIUM).setIconIndex(34).setItemName("titaniumHoe");
		titaniumSpade = new GCCoreItemSpade(GCCoreConfigManager.idToolTitaniumSpade, TOOLTITANIUM).setIconIndex(35).setItemName("titaniumSpade");
		titaniumSword = new GCCoreItemSword(GCCoreConfigManager.idToolTitaniumSword, TOOLTITANIUM).setIconIndex(36).setItemName("titaniumSword");
		titaniumHelmet = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumHelmet, TITANIUMARMOR, 9, 0, false).setIconIndex(47).setItemName("titaniumHelmet");
		titaniumChestplate = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumChestplate, TITANIUMARMOR, 9, 1, false).setIconIndex(48).setItemName("titaniumChestplate");
		titaniumLeggings = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumLeggings, TITANIUMARMOR, 9, 2, false).setIconIndex(49).setItemName("titaniumLeggings");
		titaniumBoots = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumBoots, TITANIUMARMOR, 9, 3, false).setIconIndex(50).setItemName("titaniumBoots");
		titaniumHelmetBreathable = new GCCoreItemArmor(GCCoreConfigManager.idArmorTitaniumHelmetBreathable, TITANIUMARMOR, 9, 0, true).setIconIndex(51).setItemName("titaniumHelmetBreathable");
		ingotTitanium = new GCCoreItem(GCCoreConfigManager.idItemIngotTitanium).setIconIndex(56).setItemName("ingotTitanium");
		ingotCopper = new GCCoreItem(GCCoreConfigManager.idItemIngotCopper).setIconIndex(57).setItemName("ingotCopper");
		ingotAluminum = new GCCoreItem(GCCoreConfigManager.idItemIngotAluminum).setIconIndex(58).setItemName("ingotAluminum");
		aluminumCanister = new GCCoreItem(GCCoreConfigManager.idItemAluminumCanister).setIconIndex(60).setItemName("aluminumCanister");
		airVent = new GCCoreItem(GCCoreConfigManager.idItemAirVent).setIconIndex(61).setItemName("airVent");
		airFan = new GCCoreItem(GCCoreConfigManager.idItemFan).setIconIndex(62).setItemName("airFan");
		oxygenConcentrator = new GCCoreItem(GCCoreConfigManager.idItemOxygenConcentrator).setIconIndex(63).setItemName("oxygenConcentrator");
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
	}
	
	private static void addName(Item item)
	{
//        LanguageRegistry.instance().addStringLocalization(item.getItemName() + ".name", GalacticraftCore.lang.get(item.getItemName() + ".name")); TODO
	}
}
