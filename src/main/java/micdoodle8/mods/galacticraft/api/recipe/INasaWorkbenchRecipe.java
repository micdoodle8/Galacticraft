package micdoodle8.mods.galacticraft.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public interface INasaWorkbenchRecipe
{
    public boolean matches(IInventory inventory);

    public int getRecipeSize();

    public ItemStack getRecipeOutput();

    public HashMap<Integer, ItemStack> getRecipeInput();
}
