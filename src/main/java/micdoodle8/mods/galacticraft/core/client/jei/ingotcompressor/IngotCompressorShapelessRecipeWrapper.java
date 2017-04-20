package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IngotCompressorShapelessRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final ShapelessOreRecipeGC recipe;

    public IngotCompressorShapelessRecipeWrapper(@Nonnull ShapelessOreRecipeGC recipe)
    {
        this.recipe = recipe;
    }

    @Nonnull
    @Override
    public List getInputs()
    {
        return recipe.getInput();
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs()
    {
        return Collections.singletonList(recipe.getRecipeOutput());
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {

    }
}
