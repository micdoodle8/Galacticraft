/*
 * This is a partial copy of the file ("ActuallyAdditionsAPI.java")
 * used here for interoperability purposes only.
 * 
 * View the original and source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.api;

import java.util.List;

import net.minecraft.item.ItemStack;

public class ActuallyAdditionsAPI{

    /**
     * Adds a Recipe to the Crusher Recipe Registry
     * The second output will be nothing
     *
     * @param input           The input as an ItemStack
     * @param outputOne       The first output as an ItemStack
     * @param outputTwo       The second output as an ItemStack
     * @param outputTwoChance The chance of the second output (0 won't occur at all, 100 will all the time)
     */
    public static void addCrusherRecipe(ItemStack input, ItemStack outputOne, ItemStack outputTwo, int outputTwoChance)
    {
    }

    /**
     * Adds multiple Recipes to the Crusher Recipe Registry
     * Use this if you want to add OreDictionary recipes easier
     *
     * @param inputs           The inputs as an ItemStack List, stacksizes are ignored
     * @param outputOnes       The first outputs as an ItemStack List, stacksizes are ignored
     * @param outputOneAmounts The amount of the first output, will be equal for all entries in the list
     * @param outputTwos       The second outputs as an ItemStack List (can be null or empty if there should be none)
     * @param outputTwoAmounts The amount of the second output, will be equal for all entries in the list
     * @param outputTwoChance  The chance of the second output (0 won't occur at all, 100 will all the time)
     */
    public static boolean addCrusherRecipes(List<ItemStack> inputs, List<ItemStack> outputOnes, int outputOneAmounts, List<ItemStack> outputTwos, int outputTwoAmounts, int outputTwoChance)
    {
        return true;
    }
}
