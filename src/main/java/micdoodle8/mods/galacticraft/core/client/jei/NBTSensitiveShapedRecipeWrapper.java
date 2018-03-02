package micdoodle8.mods.galacticraft.core.client.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import micdoodle8.mods.galacticraft.core.recipe.ShapedRecipeNBT;
import net.minecraft.item.ItemStack;
import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

public class NBTSensitiveShapedRecipeWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper
{
    @Nonnull
    private final ShapedRecipeNBT recipe;

    public NBTSensitiveShapedRecipeWrapper(@Nonnull ShapedRecipeNBT recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        List<ItemStack> recipeItems = Arrays.asList(recipe.recipeItems);
        ItemStack recipeOutput = recipe.getRecipeOutput();
        try {
            ingredients.setInputs(ItemStack.class, recipeItems);
            if (recipeOutput != null)
            {
                ingredients.setOutput(ItemStack.class, recipeOutput);
            }
        } catch (RuntimeException e) { e.printStackTrace(); }
    }

    @Override
    public int getWidth()
    {
        return 3;
    }

    @Override
    public int getHeight()
    {
        return 3;
    }
}
