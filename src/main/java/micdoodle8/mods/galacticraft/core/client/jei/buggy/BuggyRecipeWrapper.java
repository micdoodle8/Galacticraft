package micdoodle8.mods.galacticraft.core.client.jei.buggy;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class BuggyRecipeWrapper implements IRecipeWrapper, ICraftingRecipeWrapper
{
    @Nonnull
    private final INasaWorkbenchRecipe recipe;

    public BuggyRecipeWrapper(@Nonnull INasaWorkbenchRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, Lists.newArrayList(recipe.getRecipeInput().values()));
        ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
    }
}