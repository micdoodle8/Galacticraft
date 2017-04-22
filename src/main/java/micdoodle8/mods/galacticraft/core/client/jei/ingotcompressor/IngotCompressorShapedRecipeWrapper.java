package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.ShapedRecipesGC;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class IngotCompressorShapedRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final ShapedRecipesGC recipe;

    public IngotCompressorShapedRecipeWrapper(@Nonnull ShapedRecipesGC recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, Arrays.asList(recipe.recipeItems));
        ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
    }
}
