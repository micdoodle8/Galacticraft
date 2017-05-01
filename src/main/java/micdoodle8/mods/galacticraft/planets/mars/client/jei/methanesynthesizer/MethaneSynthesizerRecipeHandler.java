package micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynthesizer;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCLog;

import javax.annotation.Nonnull;

public class MethaneSynthesizerRecipeHandler implements IRecipeHandler<MethaneSynthesizerRecipeWrapper>
{
    @Nonnull
    @Override
    public Class<MethaneSynthesizerRecipeWrapper> getRecipeClass()
    {
        return MethaneSynthesizerRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return RecipeCategories.METHANE_SYNTHESIZER_ID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull MethaneSynthesizerRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull MethaneSynthesizerRecipeWrapper recipe)
    {
        if (recipe.getInputs().size() != 1)
        {
            GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has wrong number of inputs!");
        }
        if (recipe.getOutputs().size() != 1)
        {
            GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has wrong number of outputs!");
        }
        return true;
    }
}
