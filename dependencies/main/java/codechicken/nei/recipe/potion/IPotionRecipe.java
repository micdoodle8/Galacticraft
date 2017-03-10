package codechicken.nei.recipe.potion;

import net.minecraft.item.ItemStack;

/**
 * Created by covers1624 on 3/21/2016.
 */
public interface IPotionRecipe {
    ItemStack getRecipeOutput();

    ItemStack getRecipeInput();

    ItemStack getRecipeIngredient();
}
