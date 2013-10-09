package codechicken.nei.forge;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public interface IContainerTooltipHandler
{
    /**
     * Use this to add tooltips for other objects. If the current tip has lines in it after this call, the item tootip will not be handled. GuiContainerManager.shouldShowTooltip is not enforced here. It is recommended that you make this check yourself
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     * @param currenttip A list of strings, representing each line of the current tooltip as modified by other handlers
     * @return The modified list. NOTE: Do not return null
     */
    public List<String> handleTooltipFirst(GuiContainer gui, int mousex, int mousey, List<String> currenttip);
    
    /**
     * Use this for modifying item tooltips. Only called if there is a valid item under the mouse. GuiContainerManager.shouldShowTooltip is enforced here.
     * @param gui An instance of the currentscreen. If only general information about the item is wanted (Eg. potion effects) then this may be null.
     * @param itemStack The ItemStack under the mouse
     * @param currenttip A list of strings, representing each line of the current tooltip as modified by other handlers
     * @return The modified list. NOTE: Do not return null
     */
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, List<String> currenttip);
}
