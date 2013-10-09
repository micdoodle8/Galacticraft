package codechicken.nei.api;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.VisiblityData;

/**
 * Lets you just override those things you want to
 */
public class INEIGuiAdapter implements INEIGuiHandler
{
    @Override
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility)
    {
        return currentVisibility;
    }

    @Override
    public int getItemSpawnSlot(GuiContainer gui, ItemStack item)
    {
        return -1;
    }

    @Override
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui)
    {
        return null;
    }

    @Override
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button)
    {
        return false;
    }    
}
