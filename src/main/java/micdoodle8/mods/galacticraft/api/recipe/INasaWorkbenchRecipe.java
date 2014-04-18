package micdoodle8.mods.galacticraft.api.recipe;

import java.util.HashMap;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface INasaWorkbenchRecipe
{
	public boolean matches(IInventory inventory);

	public int getRecipeSize();

	public ItemStack getRecipeOutput();

	public HashMap<Integer, ItemStack> getRecipeInput();
}
