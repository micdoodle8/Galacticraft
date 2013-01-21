package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsUtil 
{
	public static void addCraftingRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(GCMarsItems.reinforcedBucketEmpty, 1), new Object[] {
			" Y ",
			"ZXZ",
			" Z ",
			'Y', GCMarsItems.ingotDesh,
			'X', Item.bucketEmpty,
			'Z', GCMarsItems.ingotQuandrium
		});
		GameRegistry.addRecipe(new ItemStack(GCMarsItems.heavyBoots, 1), new Object[] {
			" Z ",
			"ZXZ",
			" Y ",
			'Z', GCMarsItems.ingotDesh,
			'X', GCMarsItems.deshBoots,
			'Y', Block.blockSteel // TODO : Desh block
		});
		GameRegistry.addRecipe(new ItemStack(GCMarsItems.jetpack, 1), new Object[] {
			"WYW",
			"YXY",
			"ZVZ",
			'V', GCCoreItems.airFan,
			'W', Item.redstone,
			'X', GCCoreItems.aluminumCanister,
			'Y', GCMarsItems.ingotQuandrium,
			'Z', GCCoreItems.airVent
		});
	}
	
	public static void addSmeltingRecipes()
	{
		FurnaceRecipes.smelting().addSmelting(GCMarsItems.rawDesh.itemID, new ItemStack(GCMarsItems.ingotDesh, 1), 0.2F);
		FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID, 1, new ItemStack(GCMarsItems.ingotQuandrium, 1), 0.2F);
		FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID, 2, new ItemStack(GCCoreItems.ingotAluminum, 1), 0.2F);
		FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID, 3, new ItemStack(GCCoreItems.ingotCopper, 1), 0.2F);
		FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID, 4, new ItemStack(GCCoreItems.ingotTitanium, 1), 0.2F);
	}
}
