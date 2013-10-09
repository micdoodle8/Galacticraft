package codechicken.nei;

import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.forge.IContainerInputHandler;

import static codechicken.nei.NEIClientConfig.*;

public class PopupInputHandler implements IContainerInputHandler
{
    @Override
    public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode)
    {
        return false;
    }

    @Override
    public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button)
    {
        return false;
    }

    @Override
    public void onKeyTyped(GuiContainer gui, char keyChar, int keyID)
    {
    }

    @Override
    public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID)
    {
        if(keyID == getKeyBinding("gui.enchant") && canPerformAction("enchant"))
        {
            NEICPH.sendOpenEnchantmentWindow();
            return true;
        }
        if(keyID == getKeyBinding("gui.potion") && canPerformAction("potion"))
        {
            NEICPH.sendOpenPotionWindow();
            return true;
        }
        return false;
    }

    @Override
    public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button)
    {
    }

    @Override
    public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button)
    {
    }

    @Override
    public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled)
    {
        return false;
    }

    @Override
    public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled)
    {
    }
    
    @Override
    public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime)
    {
    }
}
