package micdoodle8.mods.galacticraft.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.HashMap;

public interface INasaWorkbenchRecipe
{
    boolean matches(IInventory inventory);

    int getRecipeSize();

    @Nonnull
    ItemStack getRecipeOutput();

    HashMap<Integer, Ingredient> getRecipeInput();
}
