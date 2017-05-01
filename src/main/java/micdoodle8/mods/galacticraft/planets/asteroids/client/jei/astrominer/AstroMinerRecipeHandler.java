package micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCLog;

import javax.annotation.Nonnull;

public class AstroMinerRecipeHandler implements IRecipeHandler<AstroMinerRecipeWrapper>
{
    @Nonnull
    @Override
    public Class<AstroMinerRecipeWrapper> getRecipeClass()
    {
        return AstroMinerRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return RecipeCategories.ASTRO_MINER_ID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull AstroMinerRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull AstroMinerRecipeWrapper recipe)
    {
        if (recipe.getInputs().size() != 14)
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
