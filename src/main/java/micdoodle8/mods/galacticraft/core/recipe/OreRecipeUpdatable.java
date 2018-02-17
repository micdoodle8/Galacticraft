package micdoodle8.mods.galacticraft.core.recipe;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class OreRecipeUpdatable extends ShapedOreRecipe
{
    public OreRecipeUpdatable(Block     result, Object... recipe){ super(new ItemStack(result), recipe); }
    public OreRecipeUpdatable(Item      result, Object... recipe){ super(new ItemStack(result), recipe); }
    public OreRecipeUpdatable(ItemStack result, Object... recipe){ super (result, recipe); }
    
    public void replaceInput(ItemStack inputA, String inputB)
    {
        for (int i = 0; i < this.input.length; i++)
        {
            Object test = this.input[i];
            if (test instanceof ItemStack && ItemStack.areItemsEqual(inputA, (ItemStack) test))
            {
                this.input[i] = OreDictionary.getOres((String)inputB);
            }
        }
    }

    public void replaceInput(String inputA, ItemStack inputB)
    {
        for (int i = 0; i < this.input.length; i++)
        {
            Object test = this.input[i];
            if (test instanceof List<?> && itemListContains((List<?>) test, inputB))
            {
                this.input[i] = inputB;
            }
        }
    }

    public static boolean itemListContains(List<?> test, ItemStack stack)
    {
        for (Object b : test)
        {
            if (b instanceof ItemStack && ItemStack.areItemsEqual(stack, (ItemStack) b))
                return true;
        }
        return false;
    }
}
