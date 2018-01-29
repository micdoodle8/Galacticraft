package codechicken.nei.recipe.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

/**
 * Created by covers1624 on 3/21/2016.
 * Between different types Water > Mundane > blah.
 */
public class PotionTypeRecipe implements IPotionRecipe {

    private ItemStack input;
    private ItemStack ingredient;
    private ItemStack output;

    public PotionTypeRecipe(ItemStack input, ItemStack ingredient, PotionType outputType) {
        this.ingredient = ingredient.copy();
        this.input = input.copy();
        this.output = PotionUtils.addPotionToItemStack(input, outputType);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public ItemStack getRecipeInput() {
        return input;
    }

    @Override
    public ItemStack getRecipeIngredient() {
        return ingredient;
    }
}
