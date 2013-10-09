package codechicken.nei;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import codechicken.nei.api.INEIGuiAdapter;

public class NEICreativeGuiHandler extends INEIGuiAdapter
{
    @Override
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility)
    {
        if(!(gui instanceof GuiContainerCreative))
            return currentVisibility;
        
        if(((GuiContainerCreative)gui).getCurrentTabIndex() != CreativeTabs.tabInventory.getTabIndex())
        {
            currentVisibility.showItemSection = currentVisibility.enableDeleteMode = false;
        }
        return currentVisibility;
    }

}
