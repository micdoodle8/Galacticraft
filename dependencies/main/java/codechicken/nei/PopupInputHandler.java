package codechicken.nei;

import codechicken.nei.config.KeyBindings;
import codechicken.nei.guihook.IContainerInputHandler;
import codechicken.nei.network.NEIClientPacketHandler;
import net.minecraft.client.gui.inventory.GuiContainer;

import static codechicken.nei.NEIClientConfig.canPerformAction;

public class PopupInputHandler implements IContainerInputHandler {
    @Override
    public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
        return false;
    }

    @Override
    public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        return false;
    }

    @Override
    public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {
    }

    @Override
    public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        if (KeyBindings.get("nei.options.keys.gui.enchant").isActiveAndMatches(keyID) && canPerformAction("enchant")) {
            NEIClientPacketHandler.sendOpenEnchantmentWindow();
            return true;
        }
        if (KeyBindings.get("nei.options.keys.gui.potion").isActiveAndMatches(keyID) && canPerformAction("potion")) {
            NEIClientPacketHandler.sendOpenPotionWindow();
            return true;
        }
        return false;
    }

    @Override
    public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
    }

    @Override
    public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {
    }

    @Override
    public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        return false;
    }

    @Override
    public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
    }

    @Override
    public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {
    }
}
