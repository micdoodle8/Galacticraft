package micdoodle8.mods.galacticraft.mars;

import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.ItemStack;

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
		// TODO
	}
	
	public static void addSmeltingRecipes()
	{
		FurnaceRecipes.smelting().addSmelting(GCMarsItems.rawDesh.shiftedIndex, new ItemStack(GCMarsItems.ingotDesh, 1), 0.2F);
		FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID, 2, new ItemStack(GCMarsItems.ingotQuandrium, 1), 1F);
	}
}
