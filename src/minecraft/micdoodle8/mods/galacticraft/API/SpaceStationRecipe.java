package micdoodle8.mods.galacticraft.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;

public class SpaceStationRecipe
{
	private IOrbitDimension result;
    private final HashMap<Object, Integer> input = new HashMap<Object, Integer>();

    /**
     * @param objMap a map of the items required. Each entry should be an object of ItemStack, Item/Block or String(OreDict) and the amount of that item required
     */
    public SpaceStationRecipe(HashMap<Object, Integer> objMap)
    {
        for (final Object obj : objMap.keySet())
        {
        	final Integer amount = objMap.get(obj);

            if (obj instanceof ItemStack)
            {
            	this.input.put(((ItemStack)obj).copy(), amount);
            }
            else if (obj instanceof Item)
            {
            	this.input.put(new ItemStack((Item)obj), amount);
            }
            else if (obj instanceof Block)
            {
        		this.input.put(new ItemStack((Block)obj), amount);
            }
            else if (obj instanceof String)
            {
            	FMLLog.info("While registering space station recipe, found " + OreDictionary.getOres((String)obj).size() + " type(s) of " + obj);
        		this.input.put(OreDictionary.getOres((String)obj), amount);
            }
            else
            {
                throw new RuntimeException("INVALID SPACE STATION RECIPE");
            }
        }
    }

    public int getRecipeSize(){ return this.input.size(); }

    public boolean matches(EntityPlayer player, boolean remove)
    {
    	final HashMap<Object, Integer> required = new HashMap<Object, Integer>();
    	required.putAll(this.input);

        final Iterator req = this.input.keySet().iterator();

        while (req.hasNext())
        {
            final Object next = req.next();

            final int amountRequired = required.get(next);
            int amountInInv = 0;

            for (int x = 0; x < player.inventory.getSizeInventory(); x++)
            {
                final ItemStack slot = player.inventory.getStackInSlot(x);

                if (slot != null)
                {
                    if (next instanceof ItemStack)
                    {
                        if (this.checkItemEquals((ItemStack)next, slot))
                        {
                        	amountInInv += slot.stackSize;
                        }
                    }
                    else if (next instanceof ArrayList)
                    {
                        for (ItemStack item : (ArrayList<ItemStack>)next)
                        {
                            if (this.checkItemEquals(item, slot))
                            {
                            	amountInInv += slot.stackSize;
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

    public void removeItems(EntityPlayer player)
    {
    	final HashMap<Object, Integer> required = new HashMap<Object, Integer>(this.input);

        final Iterator req = required.keySet().iterator();

        while (req.hasNext())
        {
            final Object next = req.next();

            final int amountRequired = required.get(next);
            int amountRemoved = 0;

            for (int x = 0; x < player.inventory.getSizeInventory(); x++)
            {
                final ItemStack slot = player.inventory.getStackInSlot(x);

                if (slot != null)
                {
                    final int amountRemaining = amountRequired - amountRemoved;

                    if (next instanceof ItemStack)
                    {
                        if (this.checkItemEquals((ItemStack)next, slot))
                        {
                        	final int amountToRemove = Math.min(slot.stackSize, amountRemaining);
                        	final ItemStack newStack = slot.copy();
                        	newStack.stackSize -= amountToRemove;
                        	player.inventory.setInventorySlotContents(x, newStack);
                        	amountRemoved += amountToRemove;
                        }
                    }
                    else if (next instanceof ArrayList)
                    {
                        for (final ItemStack item : (ArrayList<ItemStack>)next)
                        {
                            if (this.checkItemEquals(item, slot))
                            {
                            	final int amountToRemove = Math.min(slot.stackSize, amountRemaining);
                            	final ItemStack newStack = slot.copy();
                            	newStack.stackSize -= amountToRemove;
                            	player.inventory.setInventorySlotContents(x, newStack);
                            	amountRemoved += amountToRemove;
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean checkItemEquals(ItemStack target, ItemStack input)
    {
        return target.itemID == input.itemID && (target.getItemDamage() == OreDictionary.WILDCARD_VALUE || target.getItemDamage() == input.getItemDamage());
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     * @return The recipes input vales.
     */
    public HashMap<Object, Integer> getInput()
    {
        return this.input;
    }
}
