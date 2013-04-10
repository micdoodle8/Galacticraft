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
    private HashMap<Object, Integer> input = new HashMap<Object, Integer>();
    
    /**
     * @param objMap a map of the items required. Each entry should be an object of ItemStack, Item/Block or String(OreDict) and the amount of that item required
     */
    public SpaceStationRecipe(HashMap<Object, Integer> objMap)
    {
        for (Object obj : objMap.keySet())
        {
        	Integer amount = objMap.get(obj);
        	
            if (obj instanceof ItemStack)
            {
            	this.input.put(((ItemStack)obj).copy(), amount);
            }
            else if (obj instanceof Item)
            {
            	input.put(new ItemStack((Item)obj), amount);
            }
            else if (obj instanceof Block)
            {
        		input.put(new ItemStack((Block)obj), amount);
            }
            else if (obj instanceof String)
            {
        		input.put(OreDictionary.getOres((String)obj), amount);
            }
            else
            {
                throw new RuntimeException("INVALID SPACE STATION RECIPE");
            }
        }
    }

    public int getRecipeSize(){ return input.size(); }
    
    public boolean matches(EntityPlayer player, boolean remove)
    {
    	HashMap<Object, Integer> required = new HashMap<Object, Integer>(input);

        Iterator req = input.keySet().iterator();
        
        while (req.hasNext())
        {
            Object next = req.next();
            
            int amountRequired = 12;
            int amountInInv = 0;

            for (int x = 0; x < player.inventory.getSizeInventory(); x++)
            {
                ItemStack slot = player.inventory.getStackInSlot(x);

                if (slot != null)
                {
                    if (next instanceof ItemStack)
                    {
                        if (checkItemEquals((ItemStack)next, slot))
                        {
                        	amountInInv += slot.stackSize;
                        }
                    }
                    else if (next instanceof ArrayList)
                    {
                        for (ItemStack item : (ArrayList<ItemStack>)next)
                        {
                            if (checkItemEquals(item, slot))
                            {
                            	amountInInv += slot.stackSize;
                            }
                        }
                    }
                }
            }
            
            FMLLog.info("" + amountInInv);

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
    	HashMap<Object, Integer> required = new HashMap<Object, Integer>(input);

        Iterator req = required.keySet().iterator();
        
        while (req.hasNext())
        {
            Object next = req.next();
            
            int amountRequired = required.get(next);
            int amountRemoved = 0;

            for (int x = 0; x < player.inventory.getSizeInventory(); x++)
            {
                ItemStack slot = player.inventory.getStackInSlot(x);

                if (slot != null)
                {
                    int amountRemaining = amountRequired - amountRemoved;
                    
                    if (next instanceof ItemStack)
                    {
                        if (checkItemEquals((ItemStack)next, slot))
                        {
                        	int amountToRemove = Math.min(slot.stackSize, amountRemaining);
                        	ItemStack newStack = slot.copy();
                        	newStack.stackSize -= amountToRemove;
                        	player.inventory.setInventorySlotContents(x, newStack);
                        	amountRemoved += amountToRemove;
                        }
                    }
                    else if (next instanceof ArrayList)
                    {
                        for (ItemStack item : (ArrayList<ItemStack>)next)
                        {
                            if (checkItemEquals(item, slot))
                            {
                            	int amountToRemove = Math.min(slot.stackSize, amountRemaining);
                            	ItemStack newStack = slot.copy();
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

    private boolean checkItemEquals(ItemStack target, ItemStack input)
    {
        return (target.itemID == input.itemID && (target.getItemDamage() == OreDictionary.WILDCARD_VALUE || target.getItemDamage() == input.getItemDamage()));
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
