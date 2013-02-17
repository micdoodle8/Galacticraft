package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonUtil
{
	public static void addCraftingRecipes()
	{
		CraftingManager.getInstance().addRecipe(new ItemStack(GCMoonItems.cheeseBlock, 1), new Object[] {
			"YYY",
			"YXY",
			"YYY",
			'X', Item.bucketMilk,
			'Y', GCMoonItems.cheeseCurd
		});
	}
	
	public static void addSmeltingRecipes()
	{
	}
}
