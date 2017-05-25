package micdoodle8.mods.galacticraft.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public interface INasaWorkbenchRecipe
{
    boolean matches(IInventory inventory);

    int getRecipeSize();

    ItemStack getRecipeOutput();

    HashMap<Integer, ItemStack> getRecipeInput();
}
