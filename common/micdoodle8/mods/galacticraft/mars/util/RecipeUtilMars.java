package micdoodle8.mods.galacticraft.mars.util;

import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsInventoryRocketBenchT2;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.item.ItemStack;

public class RecipeUtilMars
{
    public static ItemStack findMatchingSpaceshipT2Recipe(GCMarsInventoryRocketBenchT2 inventoryRocketBench)
    {
        final ItemStack[] slots = new ItemStack[21];

        for (int i = 0; i < 21; i++)
        {
            slots[i] = inventoryRocketBench.getStackInSlot(i + 1);
        }
        
        boolean valid = true;
        
        for (int i = 0; i < 18; i++)
        {
            if (slots[i] == null)
            {
                valid = false;
                break;
            }
        }

        if (valid && slots[0].getItem().itemID == GCCoreItems.rocketNoseCone.itemID)
        {
            int platesInPlace = 0;

            for (int i = 1; i < 11; i++)
            {
                if (slots[i].getItem().itemID == GCCoreItems.heavyPlating.itemID)
                {
                    platesInPlace++;
                }
            }

            if (platesInPlace == 10)
            {
                if (slots[11].getItem().itemID == GCCoreItems.rocketEngine.itemID && slots[11].getItemDamage() == 1 && slots[15].getItem().itemID == GCCoreItems.rocketEngine.itemID && slots[15].getItemDamage() == 1)
                {
                    if (slots[12].getItem().itemID == GCCoreItems.rocketFins.itemID && slots[13].getItem().itemID == GCCoreItems.rocketFins.itemID)
                    {
                        if (slots[16].getItem().itemID == GCCoreItems.rocketFins.itemID && slots[17].getItem().itemID == GCCoreItems.rocketFins.itemID)
                        {
                            if (slots[14].getItem().itemID == GCCoreItems.rocketEngine.itemID)
                            {
                                int type = 0;

                                for (int i = 18; i < 21; i++)
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

                                return new ItemStack(GCMarsItems.spaceship, 1, type);
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
