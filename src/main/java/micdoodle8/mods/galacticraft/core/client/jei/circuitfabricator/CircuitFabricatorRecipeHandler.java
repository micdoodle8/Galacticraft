package micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCLog;

import javax.annotation.Nonnull;

public class CircuitFabricatorRecipeHandler implements IRecipeHandler<CircuitFabricatorRecipeWrapper>
{
    @Nonnull
    @Override
    public Class<CircuitFabricatorRecipeWrapper> getRecipeClass()
    {
        return CircuitFabricatorRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return RecipeCategories.CIRCUIT_FABRICATOR_ID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull CircuitFabricatorRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull CircuitFabricatorRecipeWrapper recipe)
    {
        if (recipe.getInputs().size() != 5)
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
