package codechicken.core.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiCCButton extends GuiWidget {
    public String text;
    public String actionCommand;
    private boolean isEnabled = true;
    public boolean visible = true;

    public GuiCCButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    public void setText(String s) {
        text = s;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean b) {
        isEnabled = b;
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (isEnabled && pointInside(x, y) && actionCommand != null) {
            sendAction(actionCommand, button);
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
        }
    }

    @Override
    public void draw(int mousex, int mousey, float frame) {
        if (!visible) {
            return;
        }

        drawButtonTex(mousex, mousey);
        if (text != null) {
            drawText(mousex, mousey);
        }
    }

    public void drawButtonTex(int mousex, int mousey) {
        GlStateManager.color(1, 1, 1, 1);
        renderEngine.bindTexture(guiTex);
        int state = getButtonTex(mousex, mousey);
        drawTexturedModalRect(x, y, 0, 46 + state * 20, width / 2, height / 2);//top left
        drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 46 + state * 20, width / 2, height / 2);//top right
        drawTexturedModalRect(x, y + height / 2, 0, 46 + state * 20 + 20 - height / 2, width / 2, height / 2);//bottom left
        drawTexturedModalRect(x + width / 2, y + height / 2, 200 - width / 2, 46 + state * 20 + 20 - height / 2, width / 2, height / 2);//bottom right
    }

    public int getButtonTex(int mousex, int mousey) {
        return !isEnabled ? 0 : pointInside(mousex, mousey) ? 2 : 1;
    }

    public void drawText(int mousex, int mousey) {
        drawCenteredString(fontRenderer, text, x + width / 2, y + (height - 8) / 2, getTextColour(mousex, mousey));
    }

    public int getTextColour(int mousex, int mousey) {
        return !isEnabled ? 0xFFA0A0A0 : pointInside(mousex, mousey) ? 0xFFFFFFA0 : 0xFFE0E0E0;
    }

    public GuiCCButton setActionCommand(String string) {
        actionCommand = string;
        return this;
    }
}
