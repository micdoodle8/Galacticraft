package micdoodle8.mods.galacticraft.api.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public interface IRecipeUpdatable extends IRecipe
{
    /**
     * Replace all inputs which match ItemStack inputA
     * with a List<ItemStack> (probably representing OreDict output).
     * 
     * @param inputA  the current simple ItemStack ingredient
     * @param inputB  the List<ItemStack> to replace it with
     */
    public void replaceInput(ItemStack ingredient, List<ItemStack> replacement);

    /**
     * Replace all inputs which are lists containing ItemStack ingredient
     * with simple ItemStack of ingredient.
     * 
     * @param ingredient
     */
    public void replaceInput(ItemStack ingredient);
}
