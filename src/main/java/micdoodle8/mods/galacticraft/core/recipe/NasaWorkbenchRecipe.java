package micdoodle8.mods.galacticraft.core.recipe;

import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map.Entry;

public class NasaWorkbenchRecipe implements INasaWorkbenchRecipe
{
    private ItemStack output;
    private HashMap<Integer, Ingredient> input;

    public NasaWorkbenchRecipe(ItemStack output, HashMap<Integer, Ingredient> input)
    {
        this.output = output;
        this.input = input;

        for (Entry<Integer, Ingredient> entry : this.input.entrySet())
        {
            if (entry.getValue() == null)
            {
                throw new IllegalArgumentException("Recipe contains null ingredient!");
            }
        }
    }

    @Override
    public boolean matches(IInventory inventory)
    {
        for (Entry<Integer, Ingredient> entry : this.input.entrySet())
        {
            ItemStack stackAt = inventory.getStackInSlot(entry.getKey());

            if (!entry.getValue().apply(stackAt))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getRecipeSize()
    {
        return this.input.size();
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return this.output.copy();
    }

    @Override
    public HashMap<Integer, Ingredient> getRecipeInput()
    {
        return this.input;
    }
}
