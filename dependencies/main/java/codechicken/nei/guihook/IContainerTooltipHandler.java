package codechicken.nei.guihook;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * If this interface is implemented by a GuiContainer then it will be the first handler delegated to but handleItmeDisplayName will not be called
 */
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
    public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip);

    /**
     * Use this for modifying the multiline display name of an item which may not necessarily be under the mouse. GuiContainerManager.shouldShowTooltip is enforced here.
     * @param gui An instance of the currentscreen. If only general information about the item is wanted (Eg. potion effects) then this may be null.
     * @param itemstack The ItemStack under the mouse
     * @param currenttip A list of strings, representing each line of the current tooltip as modified by other handlers
     * @return The modified list. NOTE: Do not return null
     */
    public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip);

    /**
     * Use this for modifying the tooltips of items that are under the mouse. GuiContainerManager.shouldShowTooltip is enforced here.
     * @param gui An instance of the currentscreen
     * @param itemstack The ItemStack under the mouse
     * @param currenttip A list of strings, representing each line of the current tooltip as modified by other handlers
     * @return The modified list. NOTE: Do not return null
     */
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, List<String> currenttip);
}
