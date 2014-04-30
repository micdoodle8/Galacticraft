package codechicken.nei;

import codechicken.nei.api.INEIGuiAdapter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;

public class NEICreativeGuiHandler extends INEIGuiAdapter
{
    @Override
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility)
    {
        if(!(gui instanceof GuiContainerCreative))
            return currentVisibility;
        
        if(((GuiContainerCreative)gui).func_147056_g() != CreativeTabs.tabInventory.getTabIndex())
            currentVisibility.showItemSection = currentVisibility.enableDeleteMode = false;

        return currentVisibility;
    }

}
