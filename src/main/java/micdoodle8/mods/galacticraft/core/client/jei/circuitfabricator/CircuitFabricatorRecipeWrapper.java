package micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class CircuitFabricatorRecipeWrapper extends BlankRecipeWrapper implements IRecipeWrapper
{
    @Nonnull
    private final List<ItemStack> input;
    @Nonnull
    private final ItemStack output;

    public CircuitFabricatorRecipeWrapper(@Nonnull List<ItemStack> input, @Nonnull ItemStack output)
    {
        this.input = input;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, this.input);
        ingredients.setOutput(ItemStack.class, this.output);
    }
}
