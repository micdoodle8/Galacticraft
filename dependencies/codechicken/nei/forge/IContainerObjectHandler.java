package codechicken.nei.forge;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

/**
 * Provides more general gui calls as well as functions for more integration between objects
 */
public interface IContainerObjectHandler
{
    /**
     * Called once per tick in the gui.
     * @param gui An instance of the currentscreen
     */
    public void guiTick(GuiContainer gui);
    
    /**
     * Called when the same gui is reshown.
     * @param gui An instance of the currentscreen
     */
    public void refresh(GuiContainer gui);    
    
    /**
     * Called when the gui is shown
     * @param gui An instance of the currentscreen
     */
    public void load(GuiContainer gui);
    
    /**
     * Do not return an item that is handled somewhere else
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     * @return The itemstack from one of your objects that the mouse is hovering over
     */
    public ItemStack getStackUnderMouse(GuiContainer gui, int mousex, int mousey);
    
    /**
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     * @return true if there is an object of yours obscuring the slot that the mouse would otherwise be hovering over. 
     */
    public boolean objectUnderMouse(GuiContainer gui, int mousex, int mousey);

    /**
     * @param gui An instance of the currentscreen
     * @return false if tooltips should not be shown. Eg. if you have a custom object being held
     */
    public boolean shouldShowTooltip(GuiContainer gui);
}
