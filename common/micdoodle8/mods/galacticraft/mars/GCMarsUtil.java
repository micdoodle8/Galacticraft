package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.block.Block;
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
        // CraftingManager.getInstance().addRecipe(new
        // ItemStack(GCMarsItems.reinforcedBucketEmpty, 1), new Object[] {
        // " Y ",
        // "ZXZ", TODO
        // " Z ",
        // 'Y', GCMarsItems.ingotDesh,
        // 'X', Item.bucketEmpty,
        // 'Z', GCMarsItems.ingotQuandrium
        // });
        CraftingManager.getInstance().addRecipe(new ItemStack(GCMarsItems.heavyBoots, 1), new Object[] { " Z ", "ZXZ", " Y ", 'Z', GCMarsItems.ingotDesh, 'X', GCMarsItems.deshBoots, 'Y', Block.blockIron // TODO
                                                                                                                                                                                                           // :
                                                                                                                                                                                                           // Desh
                                                                                                                                                                                                           // block
        });
        // CraftingManager.getInstance().addRecipe(new
        // ItemStack(GCMarsItems.jetpack, 1), new Object[] {
        // "WYW",
        // "YXY",
        // "ZVZ",
        // 'V', GCCoreItems.airFan,
        // 'W', Item.redstone,
        // 'X', new ItemStack(GCCoreItems.canister, 1, 0),
        // 'Y', GCMarsItems.ingotQuandrium,
        // 'Z', GCCoreItems.airVent
        // });
    }

    public static void addSmeltingRecipes()
    {
        FurnaceRecipes.smelting().addSmelting(GCMarsItems.rawDesh.itemID, new ItemStack(GCMarsItems.ingotDesh, 1), 0.2F);
        // FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID,
        // 1, new ItemStack(GCMarsItems.ingotQuandrium, 1), 0.2F);
        // FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID,
        // 2, new ItemStack(GCCoreItems.ingotAluminum, 1), 0.2F);
        // FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID,
        // 3, new ItemStack(GCCoreItems.ingotCopper, 1), 0.2F);
        // FurnaceRecipes.smelting().addSmelting(GCMarsBlocks.blockOres.blockID,
        // 4, new ItemStack(GCCoreItems.ingotTitanium, 1), 0.2F);
    }
}
