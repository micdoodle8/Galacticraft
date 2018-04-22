package micdoodle8.mods.galacticraft.core.recipe;

import java.util.HashMap;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ShapedRecipeNBT extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    public int recipeWidth;
    public int recipeHeight;
    public final ItemStack[] recipeItems;
    private final ItemStack recipeOutput;
    private boolean copyIngredientNBT;

    public ShapedRecipeNBT(ItemStack output, Object[] layout)
    {
        this.recipeOutput = output;
        String shape = "";
        int idx = 0;

        int width = 0;
        int height = 0;
        while (layout[idx] instanceof String)
        {
            String s = (String)layout[idx++];
            shape += s;
            width = s.length();
            height++;
        }
        if (width * height != shape.length())
        {
            String ret = "Invalid shaped recipe: ";
            for (Object tmp :  layout)
            {
                ret += tmp + ", ";
            }
            throw new RuntimeException(ret);
        }

        HashMap<Character, ItemStack> itemMap = new HashMap<>();

        for (; idx < layout.length; idx += 2)
        {
            Character chr = (Character)layout[idx];
            Object in = layout[idx + 1];

            if (in instanceof ItemStack)
            {
                itemMap.put(chr, ((ItemStack)in).copy());
            }
            else if (in instanceof Item)
            {
                itemMap.put(chr, new ItemStack((Item)in));
            }
            else if (in instanceof Block)
            {
                itemMap.put(chr, new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE));
            }
            else
            {
                String ret = "Invalid shaped recipe: ";
                for (Object tmp :  layout)
                {
                    ret += tmp + ", ";
                }
                throw new RuntimeException(ret);
            }
        }

        this.recipeWidth = width;
        this.recipeHeight = height;
        this.recipeItems = new ItemStack[width * height];
        int x = 0;
        for (char chr : shape.toCharArray())
        {
            this.recipeItems[x++] = itemMap.get(chr);
        }
    }

    @Override
    @Nullable
    public ItemStack getRecipeOutput()
    {
        return this.recipeOutput;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        for (int i = 0; i <= 3 - this.recipeWidth; ++i)
        {
            for (int j = 0; j <= 3 - this.recipeHeight; ++j)
            {
                if (this.checkMatch(inv, i, j, false))
                {
                    return true;
                }

                if (this.checkMatch(inv, i, j, true))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(InventoryCrafting inv, int x, int z, boolean mirror)
    {
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                int k = i - x;
                int l = j - z;
                ItemStack target = null;

                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight)
                {
                    if (mirror)
                    {
                        target = this.recipeItems[this.recipeWidth - k - 1 + l * this.recipeWidth];
                    }
                    else
                    {
                        target = this.recipeItems[k + l * this.recipeWidth];
                    }
                }

                ItemStack itemstack1 = inv.getStackInRowAndColumn(i, j);

                if (!itemstack1.isEmpty() || target != null)
                {
                    if (itemstack1.isEmpty() || target == null)
                    {
                        return false;
                    }

                    if (target.getItem() != itemstack1.getItem())
                    {
                        return false;
                    }

                    if (target.getMetadata() != 32767 && target.getMetadata() != itemstack1.getMetadata())
                    {
                        return false;
                    }

                    if (!ItemStack.areItemStackTagsEqual(target, itemstack1))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    @Nullable
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        return this.getRecipeOutput().copy();
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width >= this.recipeWidth && height >= this.recipeHeight;
    }
}
