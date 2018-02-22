package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.ShapedRecipesGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.item.ItemStack;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;

public class IngotCompressorShapedRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final ShapedRecipesGC recipe;

    public IngotCompressorShapedRecipeWrapper(@Nonnull ShapedRecipesGC recipe)
    {
        this.recipe = recipe;
        for (Object input : this.recipe.recipeItems)
        {
            if (input instanceof ItemStack)
            {
                ItemStack itemStack = (ItemStack) input;
                if (itemStack.stackSize != 1)
                {
                    itemStack.stackSize = 1;
                }
            }
        }
    }

    @Nonnull
    @Override
    public List getInputs()
    {
        return Arrays.asList(recipe.recipeItems);
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs()
    {
        ItemStack stack = recipe.getRecipeOutput().copy();
        if (ConfigManagerCore.quickMode)
        {
            if (stack.getItem().getUnlocalizedName(stack).contains("compressed"))
            {
                stack.stackSize *= 2;
            }
        }
        return Collections.singletonList(stack);
    }
}
