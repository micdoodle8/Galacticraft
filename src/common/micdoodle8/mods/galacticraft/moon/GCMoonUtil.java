package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonUtil 
{
	public static void addCraftingRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(GCMoonItems.cheeseBlock, 1), new Object[] {
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
