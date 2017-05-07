package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class IngotCompressorShapelessRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final ShapelessOreRecipeGC recipe;
    private final IStackHelper stackHelper;

    public IngotCompressorShapelessRecipeWrapper(IStackHelper stackhelper, @Nonnull ShapelessOreRecipeGC recipe)
    {
        this.recipe = recipe;
        this.stackHelper = stackhelper;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
    	List<List<ItemStack>> inputs =  this.stackHelper.expandRecipeItemStackInputs(recipe.getInput());
    	ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, this.recipe.getRecipeOutput());
    }
}
