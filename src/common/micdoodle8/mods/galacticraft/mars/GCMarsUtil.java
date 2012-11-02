package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.GCCoreItems;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
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
//		GameRegistry.addRecipe(
//				new ItemStack(GCMarsItems.reinforcedBucketEmpty, 1), 
//				new Object[] 
//						{" y ", "yxy", " y ", 
//					'y',
//					GCMarsItems.ingotDesh.shiftedIndex, 
//					'x', 
//					Item.bucketEmpty.shiftedIndex});
//		// TODO
	}
	
	public static void addSmeltingRecipes()
	{
		FurnaceRecipes.smelting().addSmelting(GCMarsItems.rawDesh.shiftedIndex, new ItemStack(GCMarsItems.ingotDesh, 1), 0.2F);
//		FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID, 2, new ItemStack(GCMarsItems.ingotQuandrium, 1), 1F);
	}
}
