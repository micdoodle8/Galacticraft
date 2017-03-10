package codechicken.nei.recipe.potion;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by covers1624 on 3/21/2016.
 * Upgrade between different states, Normal > Splash > Lingering.
 */
public class PotionUpgradeRecipe implements IPotionRecipe {

    private ItemStack input;
    private ItemStack ingredient;
    private ItemStack output;

    public PotionUpgradeRecipe(ItemStack input, ItemStack ingredient, Item output) {
        this.input = input;
        this.ingredient = ingredient;
        int stackSize = input.stackSize;
        int meta = input.getItemDamage();
        NBTTagCompound tagCompound = input.getTagCompound();
        this.output = new ItemStack(output, stackSize, meta);
        this.output.setTagCompound(tagCompound);
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
