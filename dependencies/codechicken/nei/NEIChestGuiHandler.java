package codechicken.nei;

import java.util.Arrays;
import java.util.List;

import net.minecraft.inventory.ContainerChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.api.INEIGuiAdapter;
import codechicken.nei.api.TaggedInventoryArea;

public class NEIChestGuiHandler extends INEIGuiAdapter
{
    public int chestSize(GuiContainer gui)
    {
        return ((ContainerChest)gui.inventorySlots).getLowerChestInventory().getSizeInventory();
    }
    
    @Override
    public int getItemSpawnSlot(GuiContainer gui, ItemStack item)
    {
        if(!(gui instanceof GuiChest))
            return -1;
        
        return NEIServerUtils.getSlotForStack(gui.inventorySlots, 0, chestSize(gui), item);
    }

    @Override
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui)
    {
        if(!(gui instanceof GuiChest))
            return null;
        
        return Arrays.asList(new TaggedInventoryArea("Chest", 0, chestSize(gui), gui.inventorySlots));
    }
}
