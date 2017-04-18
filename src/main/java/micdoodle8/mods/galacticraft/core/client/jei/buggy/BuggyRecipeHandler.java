package micdoodle8.mods.galacticraft.core.client.jei.buggy;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCLog;

import javax.annotation.Nonnull;

public class BuggyRecipeHandler implements IRecipeHandler<BuggyRecipeWrapper>
{
    @Nonnull
    @Override
    public Class<BuggyRecipeWrapper> getRecipeClass()
    {
        return BuggyRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return RecipeCategories.BUGGY_ID;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull BuggyRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull BuggyRecipeWrapper recipe)
    {
        if (recipe.getInputs().size() < 17 || recipe.getInputs().size() > 19)
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
