package micdoodle8.mods.galacticraft.planets.mars.client.jei.cargorocket;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCLog;

import javax.annotation.Nonnull;

public class CargoRocketRecipeHandler implements IRecipeHandler<CargoRocketRecipeWrapper>
{
    @Nonnull
    @Override
    public Class<CargoRocketRecipeWrapper> getRecipeClass()
    {
        return CargoRocketRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return RecipeCategories.ROCKET_CARGO_ID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull CargoRocketRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull CargoRocketRecipeWrapper recipe)
    {
        if (recipe.getInputs().size() != 16)
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
