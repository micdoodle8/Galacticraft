package micdoodle8.mods.galacticraft.core.client.jei.refinery;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RefineryRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper
{
    @Nonnull
    private final ItemStack input;
    @Nonnull
    private final ItemStack output;

    public RefineryRecipeWrapper(@Nonnull ItemStack input, @Nonnull ItemStack output)
    {
        this.input = input;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInput(ItemStack.class, this.input);
        ingredients.setOutput(ItemStack.class, this.output);
    }
}