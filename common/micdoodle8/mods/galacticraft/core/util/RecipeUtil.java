package micdoodle8.mods.galacticraft.core.util;

import gregtechmod.api.GregTech_API;
import ic2.api.item.Items;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryBuggyBench;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryRocketBench;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeUtil
{
    public static ItemStack findMatchingBuggy(GCCoreInventoryBuggyBench benchStacks)
    {
        final ItemStack[] slots = new ItemStack[benchStacks.getSizeInventory()];

        for (int i = 0; i < benchStacks.getSizeInventory(); i++)
        {
            slots[i] = benchStacks.getStackInSlot(i + 1);
        }

        if (slots[0] != null && slots[1] != null && slots[2] != null && slots[3] != null && slots[4] != null && slots[5] != null && slots[6] != null && slots[7] != null && slots[8] != null && slots[9] != null && slots[10] != null && slots[11] != null && slots[12] != null && slots[13] != null && slots[14] != null && slots[15] != null)
        {
            if (slots[5].getItem().itemID == GCCoreItems.buggyMaterial.itemID && slots[5].getItemDamage() == 1)
            {
                int platesInPlace = 0;

                for (int i = 0; i < 12; i++)
                {
                    if (i != 5 && slots[i].getItem().itemID == GCCoreItems.heavyPlating.itemID)
                    {
                        platesInPlace++;
                    }
                }

                if (platesInPlace == 11)
                {
                    int wheels = 0;

                    for (int i = 12; i < 16; i++)
                    {
                        if (slots[i].getItem().itemID == GCCoreItems.buggyMaterial.itemID && slots[i].getItemDamage() == 0)
                        {
                            wheels++;
                        }
                    }

                    if (wheels == 4)
                    {
                        int type = 0;

                        for (int i = 16; i < 19; i++)
                        {
                            if (slots[i] != null && slots[i].getItem().itemID == GCCoreItems.buggyMaterial.itemID && slots[i].getItemDamage() == 2)
                            {
                                type++;
                            }
                        }

                        return new ItemStack(GCCoreItems.buggy, 1, type);
                    }
                }
            }
        }

        return null;
    }

    public static ItemStack findMatchingSpaceshipRecipe(GCCoreInventoryRocketBench inventoryRocketBench)
    {
        final ItemStack[] slots = new ItemStack[18];

        for (int i = 0; i < 18; i++)
        {
            slots[i] = inventoryRocketBench.getStackInSlot(i + 1);
        }

        if (slots[0] != null && slots[1] != null && slots[2] != null && slots[3] != null && slots[4] != null && slots[5] != null && slots[6] != null && slots[7] != null && slots[8] != null && slots[9] != null && slots[10] != null && slots[11] != null && slots[12] != null && slots[13] != null)
        {
            if (slots[0].getItem().itemID == GCCoreItems.rocketNoseCone.itemID)
            {
                int platesInPlace = 0;

                for (int i = 1; i < 9; i++)
                {
                    if (slots[i].getItem().itemID == GCCoreItems.heavyPlating.itemID)
                    {
                        platesInPlace++;
                    }
                }

                if (platesInPlace == 8)
                {
                    if (slots[9].getItem().itemID == GCCoreItems.rocketFins.itemID && slots[10].getItem().itemID == GCCoreItems.rocketFins.itemID)
                    {
                        if (slots[12].getItem().itemID == GCCoreItems.rocketFins.itemID && slots[13].getItem().itemID == GCCoreItems.rocketFins.itemID)
                        {
                            if (slots[11].getItem().itemID == GCCoreItems.rocketEngine.itemID)
                            {
                                int type = 0;

                                for (int i = 14; i < 17; i++)
                                {
                                    if (slots[i] != null)
                                    {
                                        final int id = slots[i].itemID;

                                        if (id < Block.blocksList.length)
                                        {
                                            final Block block = Block.blocksList[id];

                                            if (block != null && block instanceof BlockChest)
                                            {
                                                type++;
                                            }
                                        }
                                    }
                                }

                                return new ItemStack(GCCoreItems.spaceship, 1, type);
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public static void addRecipe(ItemStack result, Object[] obj)
    {
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(result, obj));
    }

    public static ItemStack getGregtechBlock(int index, int amount, int metadata)
    {
        ItemStack stack = GregTech_API.getGregTechBlock(index, amount, metadata);

        if (stack != null)
        {
            return stack;
        }

        GCLog.severe("Failed to load Gregtech block for recipe, ensure Gregtech has loaded properly");
        return stack;
    }

    public static ItemStack getGregtechItem(int index, int amount, int metadata)
    {
        ItemStack stack = GregTech_API.getGregTechItem(index, index, metadata);

        if (stack != null)
        {
            return stack;
        }

        GCLog.severe("Failed to load Gregtech item for recipe, ensure Gregtech has loaded properly");
        return stack;
    }

    public static ItemStack getIndustrialCraftItem(String indentifier)
    {
        ItemStack stack = Items.getItem(indentifier);

        if (stack != null)
        {
            return stack;
        }
        else
        {
            try
            {
                // Might as well, since it'll NPE when trying to add the recipe
                // anyway.
                throw new Exception("Failed to load IC2 item for recipe (" + indentifier + "), ensure it has loaded properly and isn't disabled.");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return stack;
    }
}
