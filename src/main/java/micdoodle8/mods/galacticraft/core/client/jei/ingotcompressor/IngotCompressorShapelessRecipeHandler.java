package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import javax.annotation.Nonnull;
import java.util.List;

public class IngotCompressorShapelessRecipeHandler implements IRecipeHandler<ShapelessOreRecipeGC>
{
    @Nonnull
    @Override
    public Class<ShapelessOreRecipeGC> getRecipeClass()
    {
        return ShapelessOreRecipeGC.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return RecipeCategories.INGOT_COMPRESSOR_ID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ShapelessOreRecipeGC recipe)
    {
        return new IngotCompressorShapelessRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull ShapelessOreRecipeGC recipe)
    {
        if (recipe.getRecipeOutput() == null)
        {
            GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has no outputs!");
            return false;
        }
        int inputCount = 0;
        for (Object input : recipe.getInput())
        {
            if (input instanceof List)
            {
                if (((List) input).size() == 0)
                {
                    GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has an empty list as an input!");
                    return false;
                }
            }
            if (input != null)
            {
                inputCount++;
            }
        }
        if (inputCount > 9)
        {
            GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has too many inputs!");
            return false;
        }
        if (inputCount == 0)
        {
            GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has no inputs!");
            return false;
        }
        return true;
    }
}
