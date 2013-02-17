package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.client.ClientProxyMars;
import net.minecraft.block.Block;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsItems
{
	public static Item reinforcedBucketEmpty;
	public static Item reinforcedBucketMilk;
	public static Item reinforcedBucketWater;
	public static Item reinforcedBucketLava;
	public static Item reinforcedBucketBacteria;
	public static Item quandriumHelmet;
	public static Item quandriumChestplate;
	public static Item quandriumLeggings;
	public static Item quandriumBoots;
//	public static Item quandriumHelmetBreathable;
	
	public static Item rawDesh;
	public static Item deshPickaxe;
	public static Item deshAxe;
	public static Item deshHoe;
	public static Item deshSpade;
	public static Item deshSword;
	public static Item planetStonePickaxe;
	public static Item planetStoneAxe;
	public static Item planetStoneHoe;
	public static Item planetStoneSpade;
	public static Item planetStoneSword;
	public static Item quandriumPickaxe;
	public static Item quandriumAxe;
	public static Item quandriumHoe;
	public static Item quandriumSpade;
	public static Item quandriumSword;
	public static Item deshHelmet;
	public static Item deshChestplate;
	public static Item deshLeggings;
	public static Item deshBoots;
//	public static Item deshHelmetBreathable;
	public static Item deshStick;
	public static Item heavyBoots;
	public static Item ingotQuandrium;
	public static Item ingotDesh;
	public static Item jetpack;
	
	public static EnumArmorMaterial OXYGENMASK = EnumHelper.addArmorMaterial("OXYGENMASK", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial SENSORGLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumArmorMaterial TITANIUMARMOR = EnumHelper.addArmorMaterial("TITANIUM", 42, new int[] {4, 9, 7, 4}, 12);
	public static EnumArmorMaterial QUANDRIUMARMOR = EnumHelper.addArmorMaterial("QUANDRIUM", 42, new int[] {4, 9, 7, 4}, 12);
	public static EnumArmorMaterial ARMORDESH = EnumHelper.addArmorMaterial("DESH", 42, new int[] {4, 9, 7, 4}, 12);
	public static EnumArmorMaterial ARMORHEAVY = EnumHelper.addArmorMaterial("HEAVY", 200, new int[] {1, 1, 1, 1}, 0);
	public static EnumArmorMaterial ARMORJETPACK = EnumHelper.addArmorMaterial("JETPACK", 200, new int[] {0, 0, 0, 0}, 0);
	public static EnumToolMaterial TOOLPLANETSTONE = EnumHelper.addToolMaterial("PLANETSTONE", 0, 0, 0F, 0, 0);
	public static EnumToolMaterial TOOLDESH = EnumHelper.addToolMaterial("DESH", 0, 0, 0F, 0, 0);
	public static EnumToolMaterial TOOLTITANIUM = EnumHelper.addToolMaterial("TITANIUM", 0, 0, 0F, 0, 0);
	public static EnumToolMaterial TOOLQUANDRIUM = EnumHelper.addToolMaterial("QUANDRIUM", 0, 0, 0F, 0, 0);
	
	public static void initItems()
	{
		GCMarsItems.reinforcedBucketEmpty = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucket, 0).setIconIndex(0).setItemName("reinfocedBucket");
		GCMarsItems.reinforcedBucketMilk = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketMilk, 1).setIconIndex(3).setItemName("reinfocedBucketMilk");
		GCMarsItems.reinforcedBucketWater = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketWater, Block.waterMoving.blockID).setIconIndex(1).setItemName("reinfocedBucketWater");
		GCMarsItems.reinforcedBucketLava = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketLava, Block.lavaMoving.blockID).setIconIndex(2).setItemName("reinfocedBucketLava");
		GCMarsItems.reinforcedBucketBacteria = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketBacteria, GCMarsBlocks.bacterialSludgeMoving.blockID).setIconIndex(4).setItemName("reinfocedBucketSludge");
		GCMarsItems.rawDesh = new GCMarsItem(GCMarsConfigManager.idItemRawDesh).setIconIndex(5).setItemName("rawDesh");
		GCMarsItems.deshPickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolDeshPickaxe, GCMarsItems.TOOLDESH).setIconIndex(6).setItemName("deshPick");
		GCMarsItems.deshAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolDeshAxe, GCMarsItems.TOOLDESH).setIconIndex(7).setItemName("deshAxe");
		GCMarsItems.deshHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolDeshHoe, GCMarsItems.TOOLDESH).setIconIndex(8).setItemName("deshHoe");
		GCMarsItems.deshSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolDeshSpade, GCMarsItems.TOOLDESH).setIconIndex(9).setItemName("deshSpade");
		GCMarsItems.deshSword = new GCMarsItemSword(GCMarsConfigManager.idToolDeshSword, GCMarsItems.TOOLDESH).setIconIndex(10).setItemName("deshSword");
		GCMarsItems.planetStonePickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolPlanetStonePickaxe, GCMarsItems.TOOLPLANETSTONE).setIconIndex(11).setItemName("planetStonePick");
		GCMarsItems.planetStoneAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolPlanetStoneAxe, GCMarsItems.TOOLPLANETSTONE).setIconIndex(12).setItemName("planetStoneAxe");
		GCMarsItems.planetStoneHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolPlanetStoneHoe, GCMarsItems.TOOLPLANETSTONE).setIconIndex(13).setItemName("planetStoneHoe");
		GCMarsItems.planetStoneSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolPlanetStoneSpade, GCMarsItems.TOOLPLANETSTONE).setIconIndex(14).setItemName("planetStoneSpade");
		GCMarsItems.planetStoneSword = new GCMarsItemSword(GCMarsConfigManager.idToolPlanetStoneSword, GCMarsItems.TOOLPLANETSTONE).setIconIndex(15).setItemName("planetStoneSword");
		GCMarsItems.quandriumPickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolQuandriumPickaxe, GCMarsItems.TOOLQUANDRIUM).setIconIndex(16).setItemName("quandriumPick");
		GCMarsItems.quandriumAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolQuandriumAxe, GCMarsItems.TOOLQUANDRIUM).setIconIndex(17).setItemName("quandriumAxe");
		GCMarsItems.quandriumHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolQuandriumHoe, GCMarsItems.TOOLQUANDRIUM).setIconIndex(18).setItemName("quandriumHoe");
		GCMarsItems.quandriumSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolQuandriumSpade, GCMarsItems.TOOLQUANDRIUM).setIconIndex(19).setItemName("quandriumSpade");
		GCMarsItems.quandriumSword = new GCMarsItemSword(GCMarsConfigManager.idToolQuandriumSword, GCMarsItems.TOOLQUANDRIUM).setIconIndex(20).setItemName("quandriumSword");
		GCMarsItems.deshHelmet = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshHelmet, GCMarsItems.ARMORDESH, 7, 0, false).setIconIndex(21).setItemName("deshHelmet");
		GCMarsItems.deshChestplate = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshChestplate, GCMarsItems.ARMORDESH, 7, 1, false).setIconIndex(22).setItemName("deshChestplate");
		GCMarsItems.deshLeggings = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshLeggings, GCMarsItems.ARMORDESH, 7, 2, false).setIconIndex(23).setItemName("deshLeggings");
		GCMarsItems.deshBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshBoots, GCMarsItems.ARMORDESH, 7, 3, false).setIconIndex(24).setItemName("deshBoots");
//		deshHelmetBreathable = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshHelmetBreathable, ARMORDESH, 7, 0, true).setIconIndex(25).setItemName("deshHelmetBreathable");
		GCMarsItems.quandriumHelmet = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumHelmet, GCMarsItems.QUANDRIUMARMOR, 8, 0, false).setIconIndex(26).setItemName("quandriumHelmet");
		GCMarsItems.quandriumChestplate = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumChestplate, GCMarsItems.QUANDRIUMARMOR, 8, 1, false).setIconIndex(27).setItemName("quandriumChestplate");
		GCMarsItems.quandriumLeggings = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumLeggings, GCMarsItems.QUANDRIUMARMOR, 8, 2, false).setIconIndex(28).setItemName("quandriumLeggings");
		GCMarsItems.quandriumBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumBoots, GCMarsItems.QUANDRIUMARMOR, 8, 3, false).setIconIndex(29).setItemName("quandriumBoots");
//		quandriumHelmetBreathable = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumHelmetBreathable, QUANDRIUMARMOR, 8, 0, true).setIconIndex(30).setItemName("quandriumHelmetBreathable");
		GCMarsItems.deshStick = new GCMarsItem(GCMarsConfigManager.idItemDeshStick).setIconIndex(31).setItemName("deshStick");
		GCMarsItems.heavyBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorHeavyBoots, GCMarsItems.ARMORHEAVY, 10, 3, false).setIconIndex(35).setItemName("heavyBoots");
		GCMarsItems.ingotQuandrium = new GCMarsItem(GCMarsConfigManager.idItemIngotQuandrium).setIconIndex(32).setItemName("ingotQuandrium");
		GCMarsItems.ingotDesh = new GCMarsItem(GCMarsConfigManager.idItemIngotDesh).setIconIndex(33).setItemName("ingotDesh");
		GCMarsItems.jetpack = new GCMarsItemJetpack(GCMarsConfigManager.idArmorJetpack, GCMarsItems.ARMORJETPACK, 11, 1).setIconIndex(34).setItemName("jetpack");
	}
	
	public static void registerHarvestLevels()
	{
		MinecraftForge.setToolClass(GCMarsItems.deshPickaxe, "pickaxe", 4);
		MinecraftForge.setToolClass(GCMarsItems.deshAxe, "axe", 4);
		MinecraftForge.setToolClass(GCMarsItems.deshSpade, "shovel", 4);
		MinecraftForge.setToolClass(GCMarsItems.quandriumPickaxe, "pickaxe", 5);
		MinecraftForge.setToolClass(GCMarsItems.quandriumAxe, "axe", 5);
		MinecraftForge.setToolClass(GCMarsItems.quandriumSpade, "shovel", 5);
	}

	@SideOnly(Side.CLIENT)
	public static void addNames()
	{
		GCMarsItems.addName(GCMarsItems.reinforcedBucketEmpty);
		GCMarsItems.addName(GCMarsItems.reinforcedBucketMilk);
		GCMarsItems.addName(GCMarsItems.reinforcedBucketWater);
		GCMarsItems.addName(GCMarsItems.reinforcedBucketLava);
		GCMarsItems.addName(GCMarsItems.reinforcedBucketBacteria);
		GCMarsItems.addName(GCMarsItems.rawDesh);
		GCMarsItems.addName(GCMarsItems.deshPickaxe);
		GCMarsItems.addName(GCMarsItems.deshAxe);
		GCMarsItems.addName(GCMarsItems.deshSpade);
		GCMarsItems.addName(GCMarsItems.deshHoe);
		GCMarsItems.addName(GCMarsItems.deshSword);
		GCMarsItems.addName(GCMarsItems.planetStonePickaxe);
		GCMarsItems.addName(GCMarsItems.planetStoneAxe);
		GCMarsItems.addName(GCMarsItems.planetStoneSpade);
		GCMarsItems.addName(GCMarsItems.planetStoneHoe);
		GCMarsItems.addName(GCMarsItems.planetStoneSword);
		GCMarsItems.addName(GCMarsItems.quandriumPickaxe);
		GCMarsItems.addName(GCMarsItems.quandriumAxe);
		GCMarsItems.addName(GCMarsItems.quandriumSpade);
		GCMarsItems.addName(GCMarsItems.quandriumHoe);
		GCMarsItems.addName(GCMarsItems.quandriumSword);
		GCMarsItems.addName(GCMarsItems.deshHelmet);
		GCMarsItems.addName(GCMarsItems.deshChestplate);
		GCMarsItems.addName(GCMarsItems.deshLeggings);
		GCMarsItems.addName(GCMarsItems.deshBoots);
//		addName(deshHelmetBreathable);
		GCMarsItems.addName(GCMarsItems.quandriumHelmet);
		GCMarsItems.addName(GCMarsItems.quandriumChestplate);
		GCMarsItems.addName(GCMarsItems.quandriumLeggings);
		GCMarsItems.addName(GCMarsItems.quandriumBoots);
//		addName(quandriumHelmetBreathable);
		GCMarsItems.addName(GCMarsItems.deshStick);
		GCMarsItems.addName(GCMarsItems.heavyBoots);
		GCMarsItems.addName(GCMarsItems.ingotQuandrium);
		GCMarsItems.addName(GCMarsItems.ingotDesh);
		GCMarsItems.addName(GCMarsItems.jetpack);
	}
	
	private static void addName(Item item)
	{
        LanguageRegistry.instance().addStringLocalization(item.getItemName() + ".name", ClientProxyMars.lang.get(item.getItemName() + ".name"));
	}
}
