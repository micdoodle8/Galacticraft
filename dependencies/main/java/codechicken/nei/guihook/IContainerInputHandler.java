package codechicken.nei.guihook;

import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Keyboard;

public interface IContainerInputHandler
{    
    /**
     * Only use this for things like input boxes that have to be 'focused' first and will not conflict with others
     * @param gui An instance of the currentscreen
     * @param keyChar The character representing the keyPress
     * @param keyCode The KeyCode as defined in {@link Keyboard}
     * @return true to terminate further processing of this event.
     */
    public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode);
    
    /**
     * This version of keyTyped is passive and will be called on every input handler before keyTyped is processed
     * @param gui An instance of the currentscreen
     * @param keyChar The character representing the keyPress
     * @param keyID The KeyCode as defined in {@link Keyboard}
     */
    public void onKeyTyped(GuiContainer gui, char keyChar, int keyID);
    /**
     * This version of keyTyped is called if the key event has not been handled by the first pass, use this for key bindings that work globally
     * @param gui An instance of the currentscreen
     * @param keyChar The character representing the keyPress
     * @param keyID The KeyCode as defined in {@link Keyboard}
     * @return true to terminate further processing of this event.
     */
    public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID);
    /**
     * Called when the mouse is clicked in the gui
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     * @param button The button index being pressed, {0 = Left Click, 1 = Right Click, 2 = Middle Click}
     * @return true to terminate further processing of this event.
     */
    public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button);
    /**
     * This version of mouseClicked is passive and will be called on every input handler before mouseClicked is processed
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     * @param button The button index being pressed, {0 = Left Click, 1 = Right Click, 2 = Middle Click}
     */
    public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button);
    /**
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     * @param button The button index being released, {0 = Left Click, 1 = Right Click, 2 = Middle Click}
     */
    public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button);
    
    /**
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     * @param scrolled The number of notches scrolled. Positive for up.
     * @return true to terminate further processing of this event.
     */
    public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled);
    
    /**
     * This version of mouseScrolled is passive and will be called on every input handler before mouseScrolled is processed
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     * @param scrolled The number of notches scrolled. Positive for up.
     */
    public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled);

    /**
     * This version of mouseClicked is passive and will be called on every input handler before mouseClicked is processed
     * @param gui An instance of the currentscreen
     * @param mousex The x position of the mouse in pixels from left
     * @param mousey The y position of the mouse in pixels from top
     * @param button The button index being pressed, {0 = Left Click, 1 = Right Click, 2 = Middle Click}
     * @param heldTime The number of milliseconds since the button was first pressed
     */
    public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime);

}
