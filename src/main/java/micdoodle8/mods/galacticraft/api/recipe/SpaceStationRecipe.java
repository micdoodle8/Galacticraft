package micdoodle8.mods.galacticraft.api.recipe;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SpaceStationRecipe
{
    private final HashMap<Object, Integer> input = new HashMap<Object, Integer>(4, 1.0F);

    /**
     * @param objMap a map of the items required. Each entry should be an object of
     *               ItemStack, Item/Block or String(OreDict) and the amount of
     *               that item required
     */
    public SpaceStationRecipe(HashMap<Object, Integer> objMap)
    {
        for (final Object obj : objMap.keySet())
        {
            final Integer amount = objMap.get(obj);

            if (obj instanceof ItemStack)
            {
                this.input.put(((ItemStack) obj).copy(), amount);
            }
            else if (obj instanceof Item)
            {
                this.input.put(new ItemStack((Item) obj), amount);
            }
            else if (obj instanceof Block)
            {
                this.input.put(new ItemStack((Block) obj), amount);
            }
            else if (obj instanceof String)
            {
                List<ItemStack> stacks = OreDictionary.getOres((String) obj);
                this.input.put(stacks, amount);
            }
            else if (obj instanceof ArrayList)
            {
                this.input.put(obj, amount);
            }
            else
            {
                throw new RuntimeException("INVALID SPACE STATION RECIPE");
            }
        }
    }

    public int getRecipeSize()
    {
        return this.input.size();
    }

    @SuppressWarnings("unchecked")
    public boolean matches(EntityPlayer player, boolean remove)
    {
        final HashMap<Object, Integer> required = new HashMap<Object, Integer>();
        required.putAll(this.input);

        final Iterator<Object> req = this.input.keySet().iterator();

        while (req.hasNext())
        {
            final Object next = req.next();

            final int amountRequired = required.get(next);
            int amountInInv = 0;

            for (int x = 0; x < player.inventory.getSizeInventory(); x++)
            {
                final ItemStack slot = player.inventory.getStackInSlot(x);

                if (slot != null && slot.getCount() > 0)  //Intentional ItemStack null check
                {
                    if (next instanceof ItemStack)
                    {
                        if (SpaceStationRecipe.checkItemEquals((ItemStack) next, slot))
                        {
                            amountInInv += slot.getCount();
                        }
                    }
                    else if (next instanceof List)
                    {
                        for (final ItemStack item : (List<ItemStack>) next)
                        {
                            if (SpaceStationRecipe.checkItemEquals(item, slot))
                            {
                                amountInInv += slot.getCount();
                            }
                        }
                    }
                }
            }

            if (amountInInv >= amountRequired)
            {
                required.remove(next);
            }
        }

        if (required.isEmpty() && remove)
        {
            this.removeItems(player);
        }

        return required.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public void removeItems(EntityPlayer player)
    {
        final HashMap<Object, Integer> required = new HashMap<Object, Integer>(this.input);

        final Iterator<Object> req = required.keySet().iterator();

        while (req.hasNext())
        {
            final Object next = req.next();

            final int amountRequired = required.get(next);
            int amountRemoved = 0;

            InventoryLoop:
            for (int x = 0; x < player.inventory.getSizeInventory(); x++)
            {
                final ItemStack slot = player.inventory.getStackInSlot(x);

                if (slot != null && slot.getCount() > 0)  //Intentional ItemStack null check
                {
                    final int amountRemaining = amountRequired - amountRemoved;

                    if (next instanceof ItemStack)
                    {
                        if (SpaceStationRecipe.checkItemEquals((ItemStack) next, slot))
                        {
                            final int amountToRemove = Math.min(slot.getCount(), amountRemaining);
                            ItemStack newStack = slot.copy();
                            newStack.shrink(amountToRemove);

                            if (newStack.getCount() <= 0)
                            {
                                newStack = ItemStack.EMPTY;
                            }

                            player.inventory.setInventorySlotContents(x, newStack);
                            amountRemoved += amountToRemove;
                            if (amountRemoved == amountRequired) break;
                        }
                    }
                    else if (next instanceof List)
                    {
                        for (final ItemStack item : (List<ItemStack>) next)
                        {
                            if (SpaceStationRecipe.checkItemEquals(item, slot))
                            {
                                final int amountToRemove = Math.min(slot.getCount(), amountRemaining);
                                ItemStack newStack = slot.copy();
                                newStack.shrink(amountToRemove);

                                if (newStack.getCount() <= 0)
                                {
                                    newStack = ItemStack.EMPTY;
                                }

                                player.inventory.setInventorySlotContents(x, newStack);
                                amountRemoved += amountToRemove;
                                if (amountRemoved == amountRequired) break InventoryLoop;
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean checkItemEquals(ItemStack target, ItemStack input)
    {
        return target.getItem() == input.getItem() && (target.getItemDamage() == OreDictionary.WILDCARD_VALUE || target.getItemDamage() == input.getItemDamage());
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should
     * never manipulate the values in this array as it will effect the recipe
     * itself.
     *
     * @return The recipes input vales.
     */
    public HashMap<Object, Integer> getInput()
    {
        return this.input;
    }
}
