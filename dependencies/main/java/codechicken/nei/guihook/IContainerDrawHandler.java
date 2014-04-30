package codechicken.nei.guihook;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public interface IContainerDrawHandler 
{
    /**
     * Initialize your objects and the gui for drawing.
     * @param gui An instance of the currentscreen
     */
    public void onPreDraw(GuiContainer gui);
    /**
     * Draw your objects. Called after all normal gui rendering has been performed.
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     */
    public void renderObjects(GuiContainer gui, int mousex, int mousey);
    /**
     * Use this to draw things that should always be on top, for example objects being held by the mouse.
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     */
    public void postRenderObjects(GuiContainer gui, int mousex, int mousey);
    /**
     * Render something over a slot before the item in the slot.
     * @param gui An instance of the currentscreen
     * @param slot The slot being rendered.
     */
    public void renderSlotUnderlay(GuiContainer gui, Slot slot);
    /**
     * Render something over a slot after the item in the slot.
     * @param gui An instance of the currentscreen
     * @param slot The slot being rendered.
     */
    public void renderSlotOverlay(GuiContainer gui, Slot slot);
}
