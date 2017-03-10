package codechicken.nei;

import codechicken.nei.api.INEIGuiAdapter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;

public class NEICreativeGuiHandler extends INEIGuiAdapter {
    @Override
    public VisibilityData modifyVisiblity(GuiContainer gui, VisibilityData currentVisibility) {
        if (!(gui instanceof GuiContainerCreative)) {
            return currentVisibility;
        }

        if (((GuiContainerCreative) gui).getSelectedTabIndex() != CreativeTabs.INVENTORY.getTabIndex()) {
            currentVisibility.showItemPanel = currentVisibility.enableDeleteMode = false;
        }

        return currentVisibility;
    }

}
