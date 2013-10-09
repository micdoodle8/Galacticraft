package codechicken.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * Simply an {@link ItemStack} with position.
 * Mainly used in the recipe handlers.
 */
public class PositionedStack
{    
    public int relx;
    public int rely;
    public ItemStack items[];
    //compatibility dummy
    public ItemStack item;
    
    private boolean permutated = false;
    
    public PositionedStack(Object object, int x, int y, boolean genPerms)
    {
        items = NEIServerUtils.extractRecipeItems(object);
        relx = x;
        rely = y;
        
        if(genPerms)
            generatePermutations();
        else
            setPermutationToRender(0);
    }
    
    public PositionedStack(Object object, int x, int y)
    {
        this(object, x, y, true);
    }

    public void generatePermutations()
    {
        if(permutated)
            return;
        
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        for(ItemStack item : items)
        {
            if(item == null || item.getItem() == null)
                continue;
            
            if(item.getItemDamage() == Short.MAX_VALUE)
            {
                List<ItemStack> permutations = NEIClientUtils.getValidItems(item.itemID);
                if(!permutations.isEmpty())
                {
                    for(ItemStack stack : permutations)
                        stacks.add(stack.copy());
                }
                else
                {
                    ItemStack base = new ItemStack(item.itemID, item.stackSize, 0);
                    base.stackTagCompound = item.stackTagCompound;
                    stacks.add(base);
                }
                continue;
            }
            
            stacks.add(item.copy());
        }
        items = stacks.toArray(new ItemStack[0]);
        
        if(items.length == 0)
            items = new ItemStack[]{new ItemStack(Block.fire)};
        
        permutated = true;
        setPermutationToRender(0);
    }

    public void setMaxSize(int i)
    {
        for(ItemStack item : items)
            if(item.stackSize > i)
                item.stackSize = i;
    }
    
    public PositionedStack copy()
    {
        return new PositionedStack(items, relx, rely);
    }
    
    public void setPermutationToRender(int index)
    {
        item = items[index].copy();
        if(item.getItemDamage() == -1)
            item.setItemDamage(0);
    }

    public boolean contains(ItemStack ingredient)
    {
        for(ItemStack item : items)
            if(NEIServerUtils.areStacksSameTypeCrafting(item, ingredient))
                return true;
        
        return false;
    }

    public boolean contains(int ingredID)
    {
        for(ItemStack item : items)
            if(item.itemID == ingredID)
                return true;
        
        return false;
    }
}
