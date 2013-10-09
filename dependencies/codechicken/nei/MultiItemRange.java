package codechicken.nei;

import java.util.ArrayList;
import java.util.Collection;
import codechicken.core.ReflectionManager;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A container class for multiple Item Ranges.
 */
public class MultiItemRange
{
    public boolean isItemInRange(int itemid, int damage)
    {
        for(ItemRange range : ranges)
        {
            if(range.isItemInRange(itemid, damage))
            {
                return true;
            }
        }
        return false;
    }
    
    public String toString()
    {
        StringBuilder rangestring = new StringBuilder();
        boolean addcomma = false;
        for(ItemRange range : ranges)
        {
            if(addcomma)
            {
                rangestring.append(',');
            }
            else
            {
                addcomma = true;
            }
            rangestring.append(range.toString());
        }
        return rangestring.toString();
    }
    
    /**
     * Constructs a {@link MultiItemRange} from the specified string. 
     * Look for the references in {@link DropDownFile} and the NEISubsetConfig for reference on format.
     * @param rangestring
     */
    public MultiItemRange(String rangestring)
    {
        ranges = new ArrayList<ItemRange>();
        String[] separatedpairs = rangestring.split(",");
        for(String pairstring : separatedpairs)
            ranges.add(new ItemRange(pairstring));
    }
    
    public MultiItemRange()
    {
        ranges = new ArrayList<ItemRange>();
    }
    
    public MultiItemRange add(ItemRange range)
    {
        ranges.add(range);
        return this;
    }
    
    public MultiItemRange add(Collection<?> ranges)
    {                
        for(Object o : ranges)
        {
            try
            {
                ReflectionManager.callMethod(getClass(), this, "add", o);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return this;
    }
    
    public MultiItemRange add(MultiItemRange range)
    {
        return add(range.ranges);
    }
    
    public MultiItemRange add(int itemID)
    {
        return add(new ItemRange(itemID));
    }
    
    public MultiItemRange add(int itemID, int damageStart, int damageEnd)
    {
        return add(new ItemRange(itemID, damageStart, damageEnd));
    }
    
    public MultiItemRange add(int itemIDFirst, int itemIDLast)
    {
        return add(new ItemRange(itemIDFirst, itemIDLast));
    }    

    public MultiItemRange add(Item item, int damageStart, int damageEnd)
    {
        return add(item.itemID, damageStart, damageEnd);
    }
    
    public MultiItemRange add(Block block, int damageStart, int damageEnd)
    {
        return add(block.blockID, damageStart, damageEnd);
    }
    
    public MultiItemRange add(Item item)
    {
        return add(item.itemID);
    }
    
    public MultiItemRange add(Block block)
    {
        return add(block.blockID);
    }
    
    public MultiItemRange add(ItemStack item)
    {
        if(item.getItem().isDamageable())
            return add(item.itemID);
        return add(item.itemID, item.getItemDamage(), item.getItemDamage());
    }
    
    public int getNumSlots()
    {
        int slots = 0;
        for(ItemRange range : ranges)
        {
            slots+=range.encompasseditems.size();
        }
        return slots;
    }
    
    public void slotClicked(int slot, int button, boolean doubleclick)
    {
        int i = 0;
        for(ItemRange range : ranges)
        {
            if(i+range.encompasseditems.size() <= slot)
            {
                i+=range.encompasseditems.size();
                continue;
            }
            
            for(int item = 0; item < range.encompasseditems.size(); item++)
            {
                if(slot == i)
                {
                    range.onClick(item, button, doubleclick);
                    return;
                }
                i++;
            }
        }
    }
    
    public void hideAllItems()
    {
        for(ItemRange range : ranges)
        {
            range.hideAllItems();
        }
    }
    
    public void showAllItems()
    {
        for(ItemRange range : ranges)
        {
            range.showAllItems();
        }
    }
    
    public int getWidth()
    {
        return 18;
    }
    
    public void resetHashes()
    {
        for(ItemRange range : ranges)
        {
            range.resetHashes();
        }
    }
    
    public void updateState(ItemVisibilityHash vis)
    {
        boolean allshown = false;
        boolean allhidden = false;
        
        for(ItemRange range : ranges)
        {
            if(range.encompasseditems.size() == 0)//he goes with the flow
            {
                continue;
            }
            range.updateState(vis);
            int rstate = range.state;
            if(rstate == 1)//prev mixed therefore we mixed
            {
                state = 1;
                return;
            }
            else if(rstate == 0)//inactive
            {
                if(allshown)
                {
                    state = 1;
                    return;
                }
                allhidden = true;
            }
            else
            {
                if(allhidden)
                {
                    state = 1;
                    return;
                }
                allshown = true;
            }
        }
        
        if(allshown)
        {
            state = 2;
        }
        else
        {
            state = 0;
        }
    }
    
    public void addItemIfInRange(int item, int damage, NBTTagCompound compound)
    {
        for(ItemRange range : ranges)
        {
            if(range.addItemIfInRange(item, damage, compound))
                break;
        }
    }
    
    public ArrayList<ItemRange> ranges;
    public byte state;
    
    protected int lastslotclicked = -1;
    protected long lastslotclicktime;
}
