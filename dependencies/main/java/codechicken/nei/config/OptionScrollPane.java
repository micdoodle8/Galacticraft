package codechicken.nei.config;

import codechicken.core.gui.GuiScrollPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public abstract class OptionScrollPane extends GuiScrollPane
{
    public OptionScrollPane() {
        super(0, 0, 0, 0);
        setMargins(24, 4, 20, 4);
    }

    @Override
    public void drawBackground(float frame) {
        Rectangle sbar = scrollbarBounds();
        drawRect(sbar.x, y, sbar.x + sbar.width, y + height, 0xFF000000);
    }

    @Override
    public void drawOverlay(float frame) {
        drawOverlay(y, height, parentScreen.width, zLevel);
    }

    @Override
    public Dimension scrollbarDim() {
        Dimension dim = super.scrollbarDim();
        dim.width = 6;
        return dim;
    }

    @Override
    public int scrollbarGuideAlignment() {
        return 0;
    }

    public void resize() {
        int width = Math.min(parentScreen.width - 80, 320);
        setSize((parentScreen.width - width) / 2, 20, width, parentScreen.height - 50);
    }

    @Override
    public void mouseScrolled(int x, int y, int scroll) {
        scroll(-scroll);
    }

    public static void drawOverlay(int y, int height, int screenwidth, float zLevel) {
        OptionScrollPane.drawOverlayTex(0, 0, screenwidth, y, zLevel);
        OptionScrollPane.drawOverlayTex(0, y + height, screenwidth, screenwidth - y - height, zLevel);
        OptionScrollPane.drawOverlayGrad(0, screenwidth, y, y + 4, zLevel);
        OptionScrollPane.drawOverlayGrad(0, screenwidth, y + height, y + height - 4, zLevel);
    }

    public static void drawOverlayTex(int x, int y, int w, int h, float zLevel) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(Gui.optionsBackground);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x, y, zLevel, 0, 0);
        t.addVertexWithUV(x, y + h, zLevel, 0, h / 16D);
        t.addVertexWithUV(x + w, y + h, zLevel, w / 16D, h / 16D);
        t.addVertexWithUV(x + w, y, zLevel, w / 16D, 0);
        t.draw();
    }

    public static void drawOverlayGrad(int x1, int x2, int y1, int y2, float zLevel) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setColorRGBA_I(0, 255);
        t.addVertex(x2, y1, zLevel);
        t.addVertex(x1, y1, zLevel);
        t.setColorRGBA_I(0, 0);
        t.addVertex(x1, y2, zLevel);
        t.addVertex(x2, y2, zLevel);
        t.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
