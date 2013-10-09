package codechicken.nei;

import java.util.List;

import net.minecraft.item.ItemStack;
import codechicken.nei.ItemPanel.ItemPanelObject;
import codechicken.nei.forge.GuiContainerManager;

public class ItemPanelStack implements ItemPanelObject
{
    public ItemStack item;
    
    public ItemPanelStack(ItemStack itemstack)
    {
        item = itemstack;
    }
    
    @Override
    public void draw(int x, int y)
    {
        GuiContainerManager.drawItem(x, y, item);
    }

    @Override
    public List<String> handleTooltip(List<String> tooltip)
    {
        return tooltip;
    }
}
