package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.ShapedRecipesGC;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class IngotCompressorShapedRecipeHandler implements IRecipeHandler<ShapedRecipesGC>
{
    @Nonnull
    @Override
    public Class<ShapedRecipesGC> getRecipeClass()
    {
        return ShapedRecipesGC.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return RecipeCategories.INGOT_COMPRESSOR_ID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ShapedRecipesGC recipe)
    {
        return new IngotCompressorShapedRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull ShapedRecipesGC recipe)
    {
        if (recipe.getRecipeOutput() == null)
        {
            GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has no output!");
            return false;
        }
        int inputCount = 0;
        for (Object input : recipe.recipeItems)
        {
            if (input instanceof ItemStack)
            {
                inputCount++;
            }
            else
            {
                GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has input that is not an ItemStack!");
                return false;
            }
        }
        if (inputCount > 9)
        {
            GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has too many inputs!");
            return false;
        }
        return inputCount > 0;
    }
}
