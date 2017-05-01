package micdoodle8.mods.galacticraft.core.client.jei.tier1rocket;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;

public class Tier1RocketRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final INasaWorkbenchRecipe recipe;

    public Tier1RocketRecipeWrapper(@Nonnull INasaWorkbenchRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Nonnull
    @Override
    public List getInputs()
    {
        List<ItemStack> list = new ArrayList<>();
        list.addAll(recipe.getRecipeInput().values());
        return list;
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs()
    {
        return Collections.singletonList(recipe.getRecipeOutput());
    }
}