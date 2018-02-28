/*
 * This is a partial copy of the file ("ActuallyAdditionsAPI.java")
 * used here for interoperability purposes only.
 * 
 * View the original and source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.api;

import net.minecraft.item.ItemStack;

public class ActuallyAdditionsAPI{

    /**
     * Adds a Recipe to the Crusher Recipe Registry
     * The second output will be nothing
     *
     * @param input           The input's OreDictionary name
     * @param outputOne       The first output's OreDictionary name
     * @param outputOneAmount The amount of the first output
     */
    public static void addCrusherRecipe(String input, String outputOne, int outputOneAmount){
    }
    
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
     * Adds a Recipe to the Crusher Recipe Registry
     * The second output will be nothing
     *
     * @param input           The input as an ItemStack
     * @param outputOne       The first output's OreDictionary name
     * @param outputOneAmount The amount of the first output
     */
    public static void addCrusherRecipe(ItemStack input, String outputOne, int outputOneAmount){
    }
}
