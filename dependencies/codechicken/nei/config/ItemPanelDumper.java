package codechicken.nei.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import codechicken.core.inventory.InventoryUtils;
import codechicken.nei.ItemPanel;
import codechicken.nei.ItemPanelStack;
import codechicken.nei.ItemPanel.ItemPanelObject;
import codechicken.nei.forge.GuiContainerManager;

public class ItemPanelDumper extends DataDumper
{
    public ItemPanelDumper(String name)
    {
        super(name);
    }
    
    @Override
    public String[] header()
    {
        return new String[]{"Item ID", "Item meta", "Has NBT", "Display Name"};
    }

    @Override
    public Iterable<String[]> dump(int mode)
    {
        LinkedList<String[]> list = new LinkedList<String[]>();
        for(ItemPanelObject obj : ItemPanel.visibleitems)
            if(obj instanceof ItemPanelStack)
            {
                ItemStack stack = ((ItemPanelStack)obj).item;
                list.add(new String[]{
                        Integer.toString(stack.itemID), 
                        Integer.toString(InventoryUtils.actualDamage(stack)), 
                        stack.stackTagCompound == null ? "false" : "true",
                        GuiContainerManager.itemDisplayNameShort(stack).replaceAll("\247.", "")
                    });
            }
        
        return list;
    }
    
    @Override
    public String renderName()
    {
        return translateN(name);
    }
    
    @Override
    public String getFileName(String suffix)
    {
        return suffix + (getMode() == 0 ? ".csv" : ".nbt");
    }
    
    @Override
    public String dumpMessage(File file)
    {
        return translateN(name+".dumped", "dumps/"+file.getName());
    }
    
    @Override
    public String modeButtonText()
    {
        return translateN(name+".mode."+getMode());
    }
    
    @Override
    public void dumpTo(File file) throws IOException
    {
        if(getMode() == 0)
            super.dumpTo(file);
        else
            dumpNBT(file);
    }
    
    public void dumpNBT(File file) throws IOException
    {
        NBTTagList list = new NBTTagList();
        for(ItemPanelObject obj : ItemPanel.visibleitems)
            if(obj instanceof ItemPanelStack)
                list.appendTag(((ItemPanelStack)obj).item.writeToNBT(new NBTTagCompound()));
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("list", list);
        
        CompressedStreamTools.writeCompressed(tag, new FileOutputStream(file));
    }

    @Override
    public int modeCount()
    {
        return 2;
    }
}
