package micdoodle8.mods.galacticraft.core.client.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import micdoodle8.mods.galacticraft.core.recipe.ShapedRecipeNBT;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class NBTSensitiveShapedRecipeHandler implements IRecipeHandler<ShapedRecipeNBT>
{
    @Nonnull
    @Override
    public Class<ShapedRecipeNBT> getRecipeClass()
    {
        return ShapedRecipeNBT.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Override
    public String getRecipeCategoryUid(ShapedRecipeNBT recipe)
    {
        return this.getRecipeCategoryUid();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ShapedRecipeNBT recipe)
    {
        return new NBTSensitiveShapedRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull ShapedRecipeNBT recipe)
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
