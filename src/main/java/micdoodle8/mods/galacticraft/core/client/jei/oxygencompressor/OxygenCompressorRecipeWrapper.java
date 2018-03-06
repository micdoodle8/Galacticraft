package micdoodle8.mods.galacticraft.core.client.jei.oxygencompressor;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class OxygenCompressorRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final ItemStack output;

    public OxygenCompressorRecipeWrapper(@Nonnull ItemStack output)
    {
        this.output = output;
    }

    @Nonnull
    @Override
    public List getInputs()
    {
        return Collections.EMPTY_LIST;
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs()
    {
        return Collections.singletonList(this.output);
    }
}