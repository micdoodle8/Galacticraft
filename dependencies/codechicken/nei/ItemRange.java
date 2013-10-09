package codechicken.nei;

import java.util.ArrayList;
import java.util.HashSet;

import codechicken.core.inventory.ItemKey;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A helper class for the dropdown file.
 * Stores ranges of ItemID's and damages in a more compact manner.
 * Capable of being loaded from and saved to Strings.
 *
 */
public class ItemRange
{
    public ItemRange(int itemID)
    {
        firstID = itemID;
        firstDamage = -1;
        lastID = itemID;
        lastDamage = -1;
    }
    
    public ItemRange(int itemID, int damageStart, int damageEnd)
    {
        firstID = itemID;
        firstDamage = damageStart;
        lastID = itemID;
        lastDamage = damageEnd;
    }
    
    public ItemRange(int itemIDFirst, int itemIDLast)
    {
        firstID = itemIDFirst;
        firstDamage = -1;
        lastID = itemIDLast;
        lastDamage = -1;
    }
    
    public boolean isItemInRange(int id, int damage)
    {
        if((id >= firstID && id <= lastID) && 
                (firstDamage==-1 || (damage >= firstDamage && damage <= lastDamage)))
        {
            return true;
        }
        return false;
    }
    
    public String toString()
    {
        if(firstID==lastID)
        {
            if(firstDamage==-1)
                return "["+firstID+"]";
            else if(firstDamage == lastDamage)
                return "["+firstID+":"+firstDamage+"]";
            else
                return "["+firstID+":"+firstDamage+"-"+lastDamage+"]";
        }
        return "["+firstID+"-"+lastID+"]";
    }
    
    public ItemRange(String rangestring)
    {
        rangestring = rangestring.replace(" ", "");
        rangestring = rangestring.replace("\t", "");
        rangestring = rangestring.substring(1, rangestring.length()-1);//cut off [ and ]
        String[] damagesplit = rangestring.split(":");
        
        if(damagesplit.length == 2)
        {
            String[] rangesplit = damagesplit[1].split("-");
            
            firstID = Integer.parseInt(damagesplit[0]);
            lastID = firstID;
            
            firstDamage = Integer.parseInt(rangesplit[0]);
            
            if(rangesplit.length == 2)
            {
                lastDamage = Integer.parseInt(rangesplit[1]);
            }
            else
            {
                lastDamage = firstDamage;
            }
        }
        else
        {
            String[] rangesplit = damagesplit[0].split("-");
            firstID = Integer.parseInt(rangesplit[0]);
            
            if(rangesplit.length == 2)
            {
                lastID = Integer.parseInt(rangesplit[1]);
            }
            else
            {
                lastID = firstID;
            }
        }
    }

    public synchronized void updateState(ItemVisibilityHash vis)
    {
        boolean allhidden = false;
        boolean allshown = false;
        for(ItemKey item : encompasseditems)
        {
            if(vis.isItemHidden(item))
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
    
    public synchronized void resetHashes()
    {
        encompassedhash.clear();
        encompasseditems.clear();
    }
    
    public synchronized boolean addItemIfInRange(int item, int damage, NBTTagCompound compound)
    {
        if(isItemInRange(item, damage))
        {
            ItemKey hash = new ItemKey(item, damage, compound);
            if(encompassedhash.add(hash))
            {
                encompasseditems.add(hash);
                return true;
            }
        }
        return false;
    }
    
    public void onClick(int itemno, int button, boolean doubleclick)
    {
        ItemVisibilityHash vis = NEIClientConfig.vishash;
        ItemKey item = encompasseditems.get(itemno);
        if(NEIClientUtils.controlKey())
        {
            NEIClientUtils.cheatItem(item.item, button, 0);
            return;
        }
        
        if(button == 0)//show
        {
            if(doubleclick)
            {
                DropDownFile.dropDownInstance.hideAllItems();
            }
            vis.unhideItem(item);
        }
        else if(button == 1)//hide
        {
            vis.hideItem(item);
        }
        DropDownFile.dropDownInstance.updateState();
        ItemList.updateSearch();
        NEIClientConfig.vishash.save();
    }
    
    public synchronized void hideAllItems()
    {
        ItemVisibilityHash vis = NEIClientConfig.vishash;
        for(ItemKey item : encompasseditems)
            vis.hideItem(item);
    }
    
    public synchronized void showAllItems()
    {
        ItemVisibilityHash vis = NEIClientConfig.vishash;
        for(ItemKey item : encompasseditems)
            vis.unhideItem(item);
    }
    
    public ArrayList<Integer> toIDList()
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i = firstID; i <= lastID; i++)
        {
            list.add(i);
        }
        return list;
    }
    
    public int firstID;
    public int firstDamage = -1;
    public int lastID;
    public int lastDamage = -1;
    public byte state = 0;

    public HashSet<ItemKey> encompassedhash = new HashSet<ItemKey>();
    public ArrayList<ItemKey> encompasseditems = new ArrayList<ItemKey>();
}
