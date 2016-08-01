package codechicken.nei.config;

import codechicken.core.gui.GuiScrollPane;
import codechicken.lib.render.CCRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public abstract class OptionScrollPane extends GuiScrollPane {
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
        drawOverlayTex(0, 0, screenwidth, y, zLevel);
        drawOverlayTex(0, y + height, screenwidth, screenwidth - y - height, zLevel);
        drawOverlayGrad(0, screenwidth, y, y + 4, zLevel);
        drawOverlayGrad(0, screenwidth, y + height, y + height - 4, zLevel);
    }

    public static void drawOverlayTex(int x, int y, int w, int h, float zLevel) {
        GlStateManager.color(1, 1, 1, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(Gui.optionsBackground);
        //TODO Add CCRenderState.startDrawing(VertexFormat); defaults to quads.
        WorldRenderer worldRenderer = CCRenderState.startDrawing(7, DefaultVertexFormats.POSITION_TEX);

        worldRenderer.pos(x, y, zLevel).tex(0, 0).endVertex();
        worldRenderer.pos(x, y + h, zLevel).tex(0, h / 16D).endVertex();
        worldRenderer.pos(x + w, y + h, zLevel).tex(w / 16D, h / 16D).endVertex();
        worldRenderer.pos(x + w, y, zLevel).tex(w / 16D, 0).endVertex();

        //worldRenderer.addVertexWithUV(x, y, zLevel, 0, 0);
        //worldRenderer.addVertexWithUV(x, y + h, zLevel, 0, h / 16D);
        //worldRenderer.addVertexWithUV(x + w, y + h, zLevel, w / 16D, h / 16D);
        //worldRenderer.addVertexWithUV(x + w, y, zLevel, w / 16D, 0);
        CCRenderState.draw();
    }

    public static void drawOverlayGrad(int x1, int x2, int y1, int y2, float zLevel) {
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        //TODO Add CCRenderState.startDrawing(VertexFormat); defaults to quads.
        WorldRenderer worldRenderer = CCRenderState.startDrawing(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(x2, y1, zLevel).color(0, 0, 0, 255).endVertex();
        worldRenderer.pos(x1, y1, zLevel).color(0, 0, 0, 255).endVertex();

        worldRenderer.pos(x1, y2, zLevel).color(0, 0, 0, 0).endVertex();
        worldRenderer.pos(x2, y2, zLevel).color(0, 0, 0, 0).endVertex();

        //worldRenderer.setColorRGBA_I(0, 255);
        //worldRenderer.addVertex(x2, y1, zLevel);
        //worldRenderer.addVertex(x1, y1, zLevel);
        //worldRenderer.setColorRGBA_I(0, 0);
        //worldRenderer.addVertex(x1, y2, zLevel);
        //worldRenderer.addVertex(x2, y2, zLevel);
        CCRenderState.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
    }
}
