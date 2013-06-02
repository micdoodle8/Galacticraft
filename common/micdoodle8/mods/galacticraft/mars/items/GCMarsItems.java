package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
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
    public static Item deshStick;
    public static Item heavyBoots;
    public static Item ingotQuandrium;
    public static Item ingotDesh;
    public static Item jetpack;
    public static Item spaceship;

    public static EnumArmorMaterial OXYGENMASK = EnumHelper.addArmorMaterial("OXYGENMASK", 200, new int[] { 0, 0, 0, 0 }, 0);
    public static EnumArmorMaterial SENSORGLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", 200, new int[] { 0, 0, 0, 0 }, 0);
    public static EnumArmorMaterial TITANIUMARMOR = EnumHelper.addArmorMaterial("TITANIUM", 42, new int[] { 4, 9, 7, 4 }, 12);
    public static EnumArmorMaterial QUANDRIUMARMOR = EnumHelper.addArmorMaterial("QUANDRIUM", 42, new int[] { 4, 9, 7, 4 }, 12);
    public static EnumArmorMaterial ARMORDESH = EnumHelper.addArmorMaterial("DESH", 42, new int[] { 4, 9, 7, 4 }, 12);
    public static EnumArmorMaterial ARMORHEAVY = EnumHelper.addArmorMaterial("HEAVY", 200, new int[] { 1, 1, 1, 1 }, 0);
    public static EnumArmorMaterial ARMORJETPACK = EnumHelper.addArmorMaterial("JETPACK", 200, new int[] { 0, 0, 0, 0 }, 0);
    public static EnumToolMaterial TOOLPLANETSTONE = EnumHelper.addToolMaterial("PLANETSTONE", 0, 0, 0F, 0, 0);
    public static EnumToolMaterial TOOLDESH = EnumHelper.addToolMaterial("DESH", 0, 0, 0F, 0, 0);
    public static EnumToolMaterial TOOLTITANIUM = EnumHelper.addToolMaterial("TITANIUM", 0, 0, 0F, 0, 0);
    public static EnumToolMaterial TOOLQUANDRIUM = EnumHelper.addToolMaterial("QUANDRIUM", 0, 0, 0F, 0, 0);

    public static void initItems()
    {
        // GCMarsItems.reinforcedBucketEmpty = new
        // GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucket,
        // 0) .setUnlocalizedName("reinfocedBucket");
        // GCMarsItems.reinforcedBucketMilk = new
        // GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketMilk,
        // 1) .setUnlocalizedName("reinfocedBucketMilk");
        // GCMarsItems.reinforcedBucketWater = new
        // GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketWater,
        // Block.waterMoving.blockID)
        // .setUnlocalizedName("reinfocedBucketWater");
        // GCMarsItems.reinforcedBucketLava = new
        // GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketLava,
        // Block.lavaMoving.blockID) .setUnlocalizedName("reinfocedBucketLava");
        // GCMarsItems.reinforcedBucketBacteria = new
        // GCMarsItemReinforcedBucket(GCMarsConfigManager.idItemReinforcedBucketBacteria,
        // GCMarsBlocks.bacterialSludgeMoving.blockID).setUnlocalizedName("reinfocedBucketSludge");
        GCMarsItems.rawDesh = new GCMarsItem(GCMarsConfigManager.idItemRawDesh).setUnlocalizedName("rawDesh");
        GCMarsItems.deshPickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolDeshPickaxe, GCMarsItems.TOOLDESH).setUnlocalizedName("deshPick");
        GCMarsItems.deshAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolDeshAxe, GCMarsItems.TOOLDESH).setUnlocalizedName("deshAxe");
        GCMarsItems.deshHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolDeshHoe, GCMarsItems.TOOLDESH).setUnlocalizedName("deshHoe");
        GCMarsItems.deshSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolDeshSpade, GCMarsItems.TOOLDESH).setUnlocalizedName("deshSpade");
        GCMarsItems.deshSword = new GCMarsItemSword(GCMarsConfigManager.idToolDeshSword, GCMarsItems.TOOLDESH).setUnlocalizedName("deshSword");
        GCMarsItems.planetStonePickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolPlanetStonePickaxe, GCMarsItems.TOOLPLANETSTONE).setUnlocalizedName("planetStonePick");
        GCMarsItems.planetStoneAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolPlanetStoneAxe, GCMarsItems.TOOLPLANETSTONE).setUnlocalizedName("planetStoneAxe");
        GCMarsItems.planetStoneHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolPlanetStoneHoe, GCMarsItems.TOOLPLANETSTONE).setUnlocalizedName("planetStoneHoe");
        GCMarsItems.planetStoneSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolPlanetStoneSpade, GCMarsItems.TOOLPLANETSTONE).setUnlocalizedName("planetStoneSpade");
        GCMarsItems.planetStoneSword = new GCMarsItemSword(GCMarsConfigManager.idToolPlanetStoneSword, GCMarsItems.TOOLPLANETSTONE).setUnlocalizedName("planetStoneSword");
        GCMarsItems.quandriumPickaxe = new GCMarsItemPickaxe(GCMarsConfigManager.idToolQuandriumPickaxe, GCMarsItems.TOOLQUANDRIUM).setUnlocalizedName("quandriumPick");
        GCMarsItems.quandriumAxe = new GCMarsItemAxe(GCMarsConfigManager.idToolQuandriumAxe, GCMarsItems.TOOLQUANDRIUM).setUnlocalizedName("quandriumAxe");
        GCMarsItems.quandriumHoe = new GCMarsItemHoe(GCMarsConfigManager.idToolQuandriumHoe, GCMarsItems.TOOLQUANDRIUM).setUnlocalizedName("quandriumHoe");
        GCMarsItems.quandriumSpade = new GCMarsItemSpade(GCMarsConfigManager.idToolQuandriumSpade, GCMarsItems.TOOLQUANDRIUM).setUnlocalizedName("quandriumSpade");
        GCMarsItems.quandriumSword = new GCMarsItemSword(GCMarsConfigManager.idToolQuandriumSword, GCMarsItems.TOOLQUANDRIUM).setUnlocalizedName("quandriumSword");
        GCMarsItems.deshHelmet = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshHelmet, GCMarsItems.ARMORDESH, 7, 0, false).setUnlocalizedName("deshHelmet");
        GCMarsItems.deshChestplate = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshChestplate, GCMarsItems.ARMORDESH, 7, 1, false).setUnlocalizedName("deshChestplate");
        GCMarsItems.deshLeggings = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshLeggings, GCMarsItems.ARMORDESH, 7, 2, false).setUnlocalizedName("deshLeggings");
        GCMarsItems.deshBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorDeshBoots, GCMarsItems.ARMORDESH, 7, 3, false).setUnlocalizedName("deshBoots");
        GCMarsItems.quandriumHelmet = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumHelmet, GCMarsItems.QUANDRIUMARMOR, 8, 0, false).setUnlocalizedName("quandriumHelmet");
        GCMarsItems.quandriumChestplate = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumChestplate, GCMarsItems.QUANDRIUMARMOR, 8, 1, false).setUnlocalizedName("quandriumChestplate");
        GCMarsItems.quandriumLeggings = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumLeggings, GCMarsItems.QUANDRIUMARMOR, 8, 2, false).setUnlocalizedName("quandriumLeggings");
        GCMarsItems.quandriumBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorQuandriumBoots, GCMarsItems.QUANDRIUMARMOR, 8, 3, false).setUnlocalizedName("quandriumBoots");
        GCMarsItems.deshStick = new GCMarsItem(GCMarsConfigManager.idItemDeshStick).setUnlocalizedName("deshStick");
        GCMarsItems.heavyBoots = new GCMarsItemArmor(GCMarsConfigManager.idArmorHeavyBoots, GCMarsItems.ARMORHEAVY, 10, 3, false).setUnlocalizedName("heavyBoots");
        GCMarsItems.ingotQuandrium = new GCMarsItem(GCMarsConfigManager.idItemIngotQuandrium).setUnlocalizedName("ingotQuandrium");
        GCMarsItems.ingotDesh = new GCMarsItem(GCMarsConfigManager.idItemIngotDesh).setUnlocalizedName("ingotDesh");
        GCMarsItems.jetpack = new GCMarsItemJetpack(GCMarsConfigManager.idArmorJetpack, GCMarsItems.ARMORJETPACK, 11, 1).setUnlocalizedName("jetpack");
        GCMarsItems.spaceship = new GCMarsItemSpaceshipTier2(GCMarsConfigManager.idItemSpaceshipTier2).setUnlocalizedName("spaceshipTier2");
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
}
