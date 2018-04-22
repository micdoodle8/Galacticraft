package micdoodle8.mods.galacticraft.api.recipe;

import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ShapelessOreRecipeGC extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipeUpdatable
{
    protected ItemStack output = null;
    protected ArrayList<Object> input = new ArrayList<Object>();

    public ShapelessOreRecipeGC(Block result, Object... recipe){ this(new ItemStack(result), recipe); }

    public ShapelessOreRecipeGC(Item  result, Object... recipe){ this(new ItemStack(result), recipe); }

    public ShapelessOreRecipeGC(ItemStack result, Object... recipe)
    {
        output = result.copy();
        for (Object in : recipe)
        {
            if (in instanceof ItemStack)
            {
                input.add(((ItemStack)in).copy());
            }
            else if (in instanceof Item)
            {
                input.add(new ItemStack((Item)in));
            }
            else if (in instanceof Block)
            {
                input.add(new ItemStack((Block)in));
            }
            else if (in instanceof String)
            {
                input.add(OreDictionary.getOres((String)in));
            }
            else
            {
                StringBuilder ret = new StringBuilder("Invalid compressor shapeless ore recipe: ");
                for (Object tmp :  recipe)
                {
                    ret.append(tmp).append(", ");
                }
                ret.append(output);
                throw new RuntimeException(ret.toString());
            }
        }
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= input.size();
    }
    
    @Override
    public ItemStack getRecipeOutput(){ return output; }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1){ return output.copy(); }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(InventoryCrafting var1, World world)
    {
        List<Object> required = new LinkedList<>(input);

        for (int x = 0; x < var1.getSizeInventory(); x++)
        {
            ItemStack slot = var1.getStackInSlot(x);

            if (!slot.isEmpty())
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        match = OreDictionary.itemMatches((ItemStack)next, slot, false);
                    }
                    else if (next instanceof List)
                    {
                        Iterator<ItemStack> itr = ((List<ItemStack>)next).iterator();
                        while (itr.hasNext() && !match)
                        {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    public boolean matches(List<ItemStack> var1)
    {
        List<Object> required = new LinkedList<>(input);

        for (ItemStack slot : var1)
        {
            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        match = OreDictionary.itemMatches((ItemStack)next, slot, false);
                    }
                    else if (next instanceof List)
                    {
                        Iterator<ItemStack> itr = ((List<ItemStack>)next).iterator();
                        while (itr.hasNext() && !match)
                        {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    public ArrayList<Object> getInput()
    {
        return this.input;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) //getRecipeLeftovers
    {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    @Override
    public void replaceInput(ItemStack inputA, List<ItemStack> inputB)
    {
        for (int i = 0; i < this.input.size(); i++)
        {
            Object test = this.input.get(i);
            if (test instanceof ItemStack && ItemStack.areItemsEqual(inputA, (ItemStack) test) && RecipeUtil.areItemStackTagsEqual(inputA, (ItemStack) test))
            {
                this.input.set(i, inputB);
            }
            else if (test instanceof List<?> && itemListContains((List<?>) test, inputA))
            {
                this.input.set(i, inputB);
            }
        }
    }

    @Override
    public void replaceInput(ItemStack inputB)
    {
        for (int i = 0; i < this.input.size(); i++)
        {
            Object test = this.input.get(i);
            if (test instanceof List<?> && itemListContains((List<?>) test, inputB))
            {
                this.input.set(i, inputB);
            }
        }
    }

    private static boolean itemListContains(List<?> test, ItemStack stack)
    {
        for (Object b : test)
        {
            if (b instanceof ItemStack && ItemStack.areItemsEqual(stack, (ItemStack) b) && RecipeUtil.areItemStackTagsEqual(stack, (ItemStack) b))
                return true;
        }
        return false;
    }
}