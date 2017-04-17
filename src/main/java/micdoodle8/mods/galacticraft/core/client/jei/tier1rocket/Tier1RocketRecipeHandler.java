package micdoodle8.mods.galacticraft.core.client.jei.tier1rocket;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCLog;

import javax.annotation.Nonnull;

public class Tier1RocketRecipeHandler implements IRecipeHandler<Tier1RocketRecipeWrapper>
{
    @Nonnull
    @Override
    public Class<Tier1RocketRecipeWrapper> getRecipeClass()
    {
        return Tier1RocketRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return RecipeCategories.ROCKET_T1_ID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull Tier1RocketRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull Tier1RocketRecipeWrapper recipe)
    {
        if (recipe.getInputs().size() < 15 || recipe.getInputs().size() > 17)
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
