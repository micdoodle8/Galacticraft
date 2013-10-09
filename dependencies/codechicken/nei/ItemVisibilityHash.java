package codechicken.nei;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import codechicken.core.inventory.ItemKey;

import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemVisibilityHash
{
    public static class IDInfo
    {
        public TreeSet<Integer> damages = new TreeSet<Integer>();
        public ArrayList<NBTTagCompound> compounds = new ArrayList<NBTTagCompound>();
    }
    
    public ItemVisibilityHash()
    {        
        try
        {
            loadFromCompound(getCurrentSaveCompound());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public NBTTagCompound getCurrentSaveCompound()
    {
        NBTTagCompound hashSave = NEIClientConfig.global.nbt.getCompoundTag("vis");
        NEIClientConfig.global.nbt.setTag("vis", hashSave);
        
        NBTTagCompound currentSave = hashSave.getCompoundTag("current");
        hashSave.setCompoundTag("current", currentSave);
        return currentSave;
    }

    public void hideItem(int item, int damage)
    {
        IDInfo info = hiddenitems.get(item);
        if(info == null)
        {
            info = new IDInfo();
            hiddenitems.put(item, info);
        }
        info.damages.add(damage);
    }
    
    public void hideItem(int item, NBTTagCompound stackTagCompound)
    {
        IDInfo info = hiddenitems.get(item);
        if(info == null)
        {
            info = new IDInfo();
            hiddenitems.put(item, info);
        }
        if(!info.compounds.contains(stackTagCompound))
        {
            info.compounds.add(stackTagCompound);
        }
    }
    
    public void hideItem(ItemKey item)
    {
        if(item.item.hasTagCompound())
            hideItem(item.item.itemID, item.item.getTagCompound());
        else
            hideItem(item.item.itemID, item.item.getItemDamage());
    }
    
    public void unhideItem(int item, int damage)
    {
        IDInfo info = hiddenitems.get(item);
        if(info == null)
            return;
        
        if(damage == -1)
            hiddenitems.remove(item);
        else
            info.damages.remove(damage);
    }
    
    public void unhideItem(int item, NBTTagCompound stackTagCompound)
    {
        IDInfo info = hiddenitems.get(item);
        if(info == null)
        {
            return;
        }
        info.compounds.remove(stackTagCompound);
    }
    
    public void unhideItem(ItemKey item)
    {
        if(item.item.hasTagCompound())
            unhideItem(item.item.itemID, item.item.getTagCompound());
        else
            unhideItem(item.item.itemID, item.item.getItemDamage());
    }
    
    public boolean isItemHidden(int itemID, int damage)
    {
        IDInfo info = hiddenitems.get(itemID);
        if(info == null)
            return false;
        
        return info.damages.contains(damage) || info.damages.contains(-1);
    }

    public boolean isItemHidden(int itemID, NBTTagCompound stackTagCompound)
    {
        IDInfo info = hiddenitems.get(itemID);
        if(info == null)
            return false;

        return info.compounds.contains(stackTagCompound);
    }
    
    public boolean isItemHidden(ItemKey item)
    {
        IDInfo info = hiddenitems.get(item.item.itemID);
        if(info == null)
            return false;
        
        if(info.damages.contains(item.item.getItemDamage()) || info.damages.contains(-1))
            return true;
        else if(item.item.hasTagCompound())
            return info.compounds.contains(item.item.getTagCompound());
        
        return false;
    }
    
    private void loadFromCompound(NBTTagCompound readTag)
    {
        hiddenitems = new TreeMap<Integer, IDInfo>();
        for(Object obj : readTag.getTags())
        {
            if(obj instanceof NBTTagList)
            {
                NBTTagList compoundlist = (NBTTagList)obj;
                
                int itemID = Integer.parseInt(compoundlist.getName().substring(1));
                IDInfo info = hiddenitems.get(itemID);
                if(info == null)
                {
                    info = new IDInfo();
                    hiddenitems.put(itemID, info);
                }
                
                for(int i = 0; i < compoundlist.tagCount(); i++)
                {
                    NBTTagCompound itemNBT = (NBTTagCompound) compoundlist.tagAt(i);
                    itemNBT.setName("tag");
                    info.compounds.add(itemNBT);
                }
            }
            else if(obj instanceof NBTTagByteArray)
            {
                NBTTagByteArray damagearray = (NBTTagByteArray)obj;
                
                int itemID = Integer.parseInt(damagearray.getName().substring(1));
                IDInfo info = hiddenitems.get(itemID);
                if(info == null)
                {
                    info = new IDInfo();
                    hiddenitems.put(itemID, info);
                }
                
                for(int i = 0; i < damagearray.byteArray.length / 2; i++)
                    info.damages.add((damagearray.byteArray[i*2]<<8)+damagearray.byteArray[i*2+1]);
            }
        }
    }

    public void save()
    {
        NBTTagCompound hashSave = NEIClientConfig.global.nbt.getCompoundTag("vis");
        NEIClientConfig.global.nbt.setTag("vis", hashSave);  
        
        hashSave.setCompoundTag("current", constructSaveCompound());
        NEIClientConfig.global.saveNBT();
    }    
    
    private NBTTagCompound constructSaveCompound()
    {
        NBTTagCompound savecompound = new NBTTagCompound();
        for(Entry<Integer, IDInfo> itemEntry : hiddenitems.entrySet())
        {
            int id = itemEntry.getKey();
            IDInfo info = itemEntry.getValue();
            
            if(info.compounds.size() > 0)
            {
                NBTTagList compoundlist = new NBTTagList();
                for(NBTTagCompound compound : info.compounds)
                {
                    compoundlist.appendTag(compound);
                }
                savecompound.setTag("c"+id, compoundlist);
            }
            
            if(info.damages.size() > 0)
            {
                byte[] damagearray = new byte[info.damages.size()*2];
                int i = 0;
                for(int damage : info.damages)
                {
                    damagearray[i*2]=(byte) (damage>>8);
                    damagearray[i*2+1]=(byte)damage;
                    i++;                    
                }
                savecompound.setByteArray("d"+id, damagearray);
            }
        }
        return savecompound;
    }

    public static void loadStates()
    {
        NBTTagCompound hashSave = NEIClientConfig.global.nbt.getCompoundTag("vis");
        NEIClientConfig.global.nbt.setTag("vis", hashSave);  
                
        for(int i = 0; i < 7; i++)
        {
            NBTTagCompound statesave = hashSave.getCompoundTag("save"+i);
            if(statesave.getTags().size() > 0)
            {
                statesSaved[i] = true;
            }
        }
    }
    
    public void loadState(int i)
    {
        NBTTagCompound hashSave = NEIClientConfig.global.nbt.getCompoundTag("vis");
        NEIClientConfig.global.nbt.setTag("vis", hashSave);  
        
        loadFromCompound(hashSave.getCompoundTag("save"+i));
        
        DropDownFile.dropDownInstance.updateState();
        ItemList.updateSearch();
        NEIClientConfig.vishash.save();
    }
    
    public void saveState(int i)
    {
        NBTTagCompound hashSave = NEIClientConfig.global.nbt.getCompoundTag("vis");
        NEIClientConfig.global.nbt.setTag("vis", hashSave);  
        
        NBTTagCompound saveCompound = getCurrentSaveCompound();
        saveCompound.setBoolean("saved", true);
        hashSave.setCompoundTag("save"+i, saveCompound);
        statesSaved[i] = true;
        NEIClientConfig.global.saveNBT();
    }
    
    public void clearState(int i)
    {
        NBTTagCompound hashSave = NEIClientConfig.global.nbt.getCompoundTag("vis");
        NEIClientConfig.global.nbt.setTag("vis", hashSave);  

        hashSave.setCompoundTag("save"+i, new NBTTagCompound());
        NEIClientConfig.global.saveNBT();
        statesSaved[i] = false;
    }
    
    public static boolean isStateSaved(int i)
    {
        return statesSaved[i];
    }    
    
    private static boolean statesSaved[] = new boolean[7];
    
    public TreeMap<Integer, IDInfo> hiddenitems;
}
