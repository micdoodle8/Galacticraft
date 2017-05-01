package micdoodle8.mods.galacticraft.planets.mars.client.jei.tier2rocket;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCLog;

import javax.annotation.Nonnull;

public class Tier2RocketRecipeHandler implements IRecipeHandler<Tier2RocketRecipeWrapper>
{
    @Nonnull
    @Override
    public Class<Tier2RocketRecipeWrapper> getRecipeClass()
    {
        return Tier2RocketRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return RecipeCategories.ROCKET_T2_ID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull Tier2RocketRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull Tier2RocketRecipeWrapper recipe)
    {
        if (recipe.getInputs().size() != 21)
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
