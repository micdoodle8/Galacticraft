package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
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

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(Object.class, this.recipe.getInput());
        ingredients.setOutput(ItemStack.class, this.recipe.getRecipeOutput());
    }
}
