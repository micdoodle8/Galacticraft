package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.client.ClientProxyMars;
import net.minecraft.src.Block;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.Item;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
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
	public static Item quandriumHelmetBreathable;
	
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
	public static Item deshHelmetBreathable;
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
		reinforcedBucketEmpty = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucket, 0).setIconIndex(0).setItemName("reinfocedBucket");
		reinforcedBucketMilk = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketMilk, 1).setIconIndex(3).setItemName("reinfocedBucketMilk");
		reinforcedBucketWater = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketWater, Block.waterMoving.blockID).setIconIndex(1).setItemName("reinfocedBucketWater");
		reinforcedBucketLava = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketLava, Block.lavaMoving.blockID).setIconIndex(2).setItemName("reinfocedBucketLava");
		reinforcedBucketBacteria = new GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketBacteria, GCMarsBlocks.bacterialSludgeMoving.blockID).setIconIndex(4).setItemName("reinfocedBucketSludge");
		rawDesh = new GCMarsItem(GCMarsConfigManager.idItemRawDesh).setIconIndex(5).setItemName("rawDesh");
		deshPickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolDeshPickaxe, TOOLDESH).setIconIndex(6).setItemName("deshPick");
		deshAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolDeshAxe, TOOLDESH).setIconIndex(7).setItemName("deshAxe");
		deshHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolDeshHoe, TOOLDESH).setIconIndex(8).setItemName("deshHoe");
		deshSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolDeshSpade, TOOLDESH).setIconIndex(9).setItemName("deshSpade");
		deshSword = new GCMarsItemSword(GCMarsConfigManager.idToolDeshSword, TOOLDESH).setIconIndex(10).setItemName("deshSword");
		planetStonePickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolPlanetStonePickaxe, TOOLPLANETSTONE).setIconIndex(11).setItemName("planetStonePick");
		planetStoneAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolPlanetStoneAxe, TOOLPLANETSTONE).setIconIndex(12).setItemName("planetStoneAxe");
		planetStoneHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolPlanetStoneHoe, TOOLPLANETSTONE).setIconIndex(13).setItemName("planetStoneHoe");
		planetStoneSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolPlanetStoneSpade, TOOLPLANETSTONE).setIconIndex(14).setItemName("planetStoneSpade");
		planetStoneSword = new GCMarsItemSword(GCMarsConfigManager.idToolPlanetStoneSword, TOOLPLANETSTONE).setIconIndex(15).setItemName("planetStoneSword");
		quandriumPickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolQuandriumPickaxe, TOOLQUANDRIUM).setIconIndex(16).setItemName("quandriumPick");
		quandriumAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolQuandriumAxe, TOOLQUANDRIUM).setIconIndex(17).setItemName("quandriumAxe");
		quandriumHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolQuandriumHoe, TOOLQUANDRIUM).setIconIndex(18).setItemName("quandriumHoe");
		quandriumSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolQuandriumSpade, TOOLQUANDRIUM).setIconIndex(19).setItemName("quandriumSpade");
		quandriumSword = new GCMarsItemSword(GCMarsConfigManager.idToolQuandriumSword, TOOLQUANDRIUM).setIconIndex(20).setItemName("quandriumSword");
		deshHelmet = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshHelmet, ARMORDESH, 7, 0, false).setIconIndex(21).setItemName("deshHelmet");
		deshChestplate = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshChestplate, ARMORDESH, 7, 1, false).setIconIndex(22).setItemName("deshChestplate");
		deshLeggings = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshLeggings, ARMORDESH, 7, 2, false).setIconIndex(23).setItemName("deshLeggings");
		deshBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshBoots, ARMORDESH, 7, 3, false).setIconIndex(24).setItemName("deshBoots");
		deshHelmetBreathable = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshHelmetBreathable, ARMORDESH, 7, 0, true).setIconIndex(25).setItemName("deshHelmetBreathable");
		quandriumHelmet = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumHelmet, QUANDRIUMARMOR, 8, 0, false).setIconIndex(26).setItemName("quandriumHelmet");
		quandriumChestplate = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumChestplate, QUANDRIUMARMOR, 8, 1, false).setIconIndex(27).setItemName("quandriumChestplate");
		quandriumLeggings = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumLeggings, QUANDRIUMARMOR, 8, 2, false).setIconIndex(28).setItemName("quandriumLeggings");
		quandriumBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumBoots, QUANDRIUMARMOR, 8, 3, false).setIconIndex(29).setItemName("quandriumBoots");
		quandriumHelmetBreathable = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumHelmetBreathable, QUANDRIUMARMOR, 8, 0, true).setIconIndex(30).setItemName("quandriumHelmetBreathable");
		deshStick = new GCMarsItem(GCMarsConfigManager.idItemDeshStick).setIconIndex(31).setItemName("deshStick");
		heavyBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorHeavyBoots, ARMORHEAVY, 10, 3, false).setIconIndex(35).setItemName("heavyBoots");
		ingotQuandrium = new GCMarsItem(GCMarsConfigManager.idItemIngotQuandrium).setIconIndex(32).setItemName("ingotQuandrium");
		ingotDesh = new GCMarsItem(GCMarsConfigManager.idItemIngotDesh).setIconIndex(33).setItemName("ingotDesh");
		jetpack = new GCMarsItemJetpack(GCMarsConfigManager.idArmorJetpack, ARMORJETPACK, 11, 1).setIconIndex(34).setItemName("jetpack");
	}
	
	public static void registerHarvestLevels()
	{
		MinecraftForge.setToolClass(deshPickaxe, "pickaxe", 4);
		MinecraftForge.setToolClass(deshAxe, "axe", 4);
		MinecraftForge.setToolClass(deshSpade, "shovel", 4);
		MinecraftForge.setToolClass(quandriumPickaxe, "pickaxe", 5);
		MinecraftForge.setToolClass(quandriumAxe, "axe", 5);
		MinecraftForge.setToolClass(quandriumSpade, "shovel", 5);
	}

	@SideOnly(Side.CLIENT)
	public static void addNames() 
	{
		addName(reinforcedBucketEmpty);
		addName(reinforcedBucketMilk);
		addName(reinforcedBucketWater);
		addName(reinforcedBucketLava);
		addName(reinforcedBucketBacteria);
		addName(rawDesh);
		addName(deshPickaxe);
		addName(deshAxe);
		addName(deshSpade);
		addName(deshHoe);
		addName(deshSword);
		addName(planetStonePickaxe);
		addName(planetStoneAxe);
		addName(planetStoneSpade);
		addName(planetStoneHoe);
		addName(planetStoneSword);
		addName(quandriumPickaxe);
		addName(quandriumAxe);
		addName(quandriumSpade);
		addName(quandriumHoe);
		addName(quandriumSword);
		addName(deshHelmet);
		addName(deshChestplate);
		addName(deshLeggings);
		addName(deshBoots);
		addName(deshHelmetBreathable);
		addName(quandriumHelmet);
		addName(quandriumChestplate);
		addName(quandriumLeggings);
		addName(quandriumBoots);
		addName(quandriumHelmetBreathable);
		addName(deshStick);
		addName(heavyBoots);
		addName(ingotQuandrium);
		addName(ingotDesh);
		addName(jetpack);
	}
	
	private static void addName(Item item)
	{
        LanguageRegistry.instance().addStringLocalization(item.getItemName() + ".name", ClientProxyMars.lang.get(item.getItemName() + ".name"));
	}
}
