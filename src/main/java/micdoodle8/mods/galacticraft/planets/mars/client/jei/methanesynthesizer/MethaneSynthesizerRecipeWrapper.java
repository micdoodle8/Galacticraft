package micdoodle8.mods.galacticraft.planets.mars.client.jei.methanesynthesizer;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class MethaneSynthesizerRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final ItemStack input;
    @Nonnull
    private final ItemStack output;

    public MethaneSynthesizerRecipeWrapper(@Nonnull ItemStack input, @Nonnull ItemStack output)
    {
        this.input = input;
        this.output = output;
    }

    @Nonnull
    @Override
    public List getInputs()
    {
        return Collections.singletonList(this.input);
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs()
    {
        return Collections.singletonList(this.output);
    }
}