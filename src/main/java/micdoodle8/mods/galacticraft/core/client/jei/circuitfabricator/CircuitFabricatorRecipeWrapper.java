package micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CircuitFabricatorRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final ItemStack[] input;
    @Nonnull
    private final ItemStack output;

    public CircuitFabricatorRecipeWrapper(@Nonnull ItemStack[] input, @Nonnull ItemStack output)
    {
        this.input = input;
        this.output = output;
    }

    @Nonnull
    @Override
    public List getInputs()
    {
        List<ItemStack> list = new ArrayList<>();
        list.addAll(Arrays.asList(this.input));
        return list;
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs()
    {
        return Collections.singletonList(this.output);
    }
}
