package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCMarsUtil
{
    public static void addCraftingRecipes()
    {
//         CraftingManager.getInstance().addRecipe(new ItemStack(GCMarsItems.reinforcedBucketEmpty, 1), new Object[] {
//         " Y ",
//         "ZXZ",
//         " Z ",
//         'Y', GCMarsItems.ingotDesh,
//         'X', Item.bucketEmpty,
//         'Z', GCMarsItems.ingotDesh
//         });
    }

    public static void addSmeltingRecipes()
    {
        FurnaceRecipes.smelting().addSmelting(GCMarsItems.rawDesh.itemID, new ItemStack(GCMarsItems.ingotDesh, 1), 0.2F);
        FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.marsBlock.blockID, 4, new ItemStack(GCMarsBlocks.marsBlock, 3), 0.0F);
    }
}
