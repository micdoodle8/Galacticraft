package micdoodle8.mods.galacticraft.planets.mars.client.jei.tier2rocket;

import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tier2RocketRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final INasaWorkbenchRecipe recipe;

    public Tier2RocketRecipeWrapper(@Nonnull INasaWorkbenchRecipe recipe)
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