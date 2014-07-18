package codechicken.nei;

import codechicken.nei.ItemPanel.ItemPanelObject;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.item.ItemStack;

import java.util.List;

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
        GuiContainerManager.drawItem(x+1, y+1, item);
    }

    @Override
    public List<String> handleTooltip(List<String> tooltip)
    {
        return tooltip;
    }
}
