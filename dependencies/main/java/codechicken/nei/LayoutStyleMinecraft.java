package codechicken.nei;

import org.lwjgl.opengl.GL11;

import static codechicken.lib.gui.GuiDraw.drawStringC;
import static codechicken.nei.LayoutManager.*;

public class LayoutStyleMinecraft extends LayoutStyleDefault
{
    int stateButtonCount;
    int clickButtonCount;

    @Override
    public String getName() {
        return "minecraft";
    }

    @Override
    public void init() {
        delete.icon = new Image(144, 12, 12, 12);
        gamemode.icons[0] = new Image(132, 12, 12, 12);
        gamemode.icons[1] = new Image(156, 12, 12, 12);
        gamemode.icons[2] = new Image(168, 12, 12, 12);
        rain.icon = new Image(120, 12, 12, 12);
        magnet.icon = new Image(180, 24, 12, 12);
        timeButtons[0].icon = new Image(132, 24, 12, 12);
        timeButtons[1].icon = new Image(120, 24, 12, 12);
        timeButtons[2].icon = new Image(144, 24, 12, 12);
        timeButtons[3].icon = new Image(156, 24, 12, 12);
        heal.icon = new Image(168, 24, 12, 12);
        dropDown.x = 90;
    }

    @Override
    public void reset() {
        stateButtonCount = clickButtonCount = 0;
    }

    @Override
    public void layoutButton(Button button) {
        if ((button.state & 0x4) != 0) {
            button.x = 6 + stateButtonCount * 20;
            button.y = 3;
            stateButtonCount++;
        } else {
            button.x = 6 + (clickButtonCount % 4) * 20;
            button.y = 3 + (1 + clickButtonCount / 4) * 18;
            clickButtonCount++;
        }

        button.height = 17;
        button.width = button.contentWidth() + 6;
    }

    @Override
    public void drawButton(Button b, int mousex, int mousey) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1, 1, 1, 1);

        int tex;
        if ((b.state & 0x3) == 2)
            tex = 0;
        else if ((b.state & 0x4) == 0 && b.contains(mousex, mousey) ||//not a state button and mouseover
                (b.state & 0x3) == 1)//state active
            tex = 2;
        else
            tex = 1;
        LayoutManager.drawButtonBackground(b.x, b.y, b.width, b.height, true, tex);

        Image icon = b.getRenderIcon();
        if (icon == null) {
            int colour = tex == 2 ? 0xffffa0 :
                    tex == 0 ? 0x601010 : 0xe0e0e0;

            drawStringC(b.getRenderLabel(), b.x + b.width / 2, b.y + (b.height - 8) / 2, colour);
        } else {
            GL11.glColor4f(1, 1, 1, 1);

            int iconx = b.x + (b.width - icon.width) / 2;
            int icony = b.y + (b.height - icon.height) / 2;
            LayoutManager.drawIcon(iconx, icony, icon);
        }
    }

    @Override
    public boolean texturedButtons() {
        return true;
    }
}
