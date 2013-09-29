package micdoodle8.mods.galacticraft.api.recipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.item.ItemStack;

public class CompressorRecipes
{
    private static HashMap<List<Integer>, ItemStack> metaCompressorRecipes = new HashMap<List<Integer>, ItemStack>();

    /**
     * Adds an item and the compressed equivalent to the list of compressor recipes
     * 
     * Output stack size does matter, unlike furnaces.
     * 
     * @param input The input ItemStack
     * @param output The output ItemStack
     */
    public static void addCompressorRecipe(ItemStack input, ItemStack output)
    {
        CompressorRecipes.metaCompressorRecipes.put(Arrays.asList(input.itemID, input.getItemDamage()), output);
    }
    
    /**
     * Returns the compressed equivalent of the given item
     * 
     * @param item The input ItemStack
     * @return the output ItemStack
     */
    public static ItemStack getCompressedResult(ItemStack item) 
    {
        if (item == null)
        {
            return null;
        }
        
        return (ItemStack)metaCompressorRecipes.get(Arrays.asList(item.itemID, item.getItemDamage()));
    }
}