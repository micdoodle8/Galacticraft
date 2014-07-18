package codechicken.lib.gui;

import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiDraw
{
    public static class GuiHook extends Gui
    {
        public void setZLevel(float f) {
            zLevel = f;
        }

        public float getZLevel() {
            return zLevel;
        }

        public void incZLevel(float f) {
            zLevel += f;
        }

        @Override
        public void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
            super.drawGradientRect(par1, par2, par3, par4, par5, par6);
        }
    }

    public static final GuiHook gui = new GuiHook();
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    public static TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;

    public static void drawRect(int x, int y, int w, int h, int colour) {
        drawGradientRect(x, y, w, h, colour, colour);
    }

    public static void drawGradientRect(int x, int y, int w, int h, int colour1, int colour2) {
        gui.drawGradientRect(x, y, x + w, y + h, colour1, colour2);
    }

    public static void drawTexturedModalRect(int x, int y, int tx, int ty, int w, int h) {
        gui.drawTexturedModalRect(x, y, tx, ty, w, h);
    }

    public static void drawString(String text, int x, int y, int colour, boolean shadow) {
        if (shadow)
            fontRenderer.drawStringWithShadow(text, x, y, colour);
        else
            fontRenderer.drawString(text, x, y, colour);
    }

    public static void drawString(String text, int x, int y, int colour) {
        drawString(text, x, y, colour, true);
    }

    public static void drawStringC(String text, int x, int y, int w, int h, int colour, boolean shadow) {
        drawString(text, x + (w - getStringWidth(text)) / 2, y + (h - 8) / 2, colour, shadow);
    }

    public static void drawStringC(String text, int x, int y, int w, int h, int colour) {
        drawStringC(text, x, y, w, h, colour, true);
    }

    public static void drawStringC(String text, int x, int y, int colour, boolean shadow) {
        drawString(text, x - getStringWidth(text) / 2, y, colour, shadow);
    }

    public static void drawStringC(String text, int x, int y, int colour) {
        drawStringC(text, x, y, colour, true);
    }

    public static void drawStringR(String text, int x, int y, int colour, boolean shadow) {
        drawString(text, x - getStringWidth(text), y, colour, shadow);
    }

    public static void drawStringR(String text, int x, int y, int colour) {
        drawStringR(text, x, y, colour, true);
    }


    public static int getStringWidth(String s) {
        if (s == null || s.equals(""))
            return 0;
        return fontRenderer.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));
    }

    public static Dimension displaySize() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        return new Dimension(res.getScaledWidth(), res.getScaledHeight());
    }

    public static Dimension displayRes() {
        Minecraft mc = Minecraft.getMinecraft();
        return new Dimension(mc.displayWidth, mc.displayHeight);
    }

    public static Point getMousePosition() {
        Dimension size = displaySize();
        Dimension res = displayRes();
        return new Point(Mouse.getX() * size.width / res.width, size.height - Mouse.getY() * size.height / res.height - 1);
    }

    public static void changeTexture(String s) {
        CCRenderState.changeTexture(s);
    }

    public static void changeTexture(ResourceLocation r) {
        CCRenderState.changeTexture(r);
    }

    public static void drawTip(int x, int y, String text) {
        drawMultilineTip(x, y, Arrays.asList(text));
    }

    /**
     * Append a string in the tooltip list with TOOLTIP_LINESPACE to have a small gap between it and the next line
     */
    public static final String TOOLTIP_LINESPACE = "\u00A7h";
    /**
     * Have a string in the tooltip list with TOOLTIP_HANDLER + getTipLineId(handler) for a custom handler
     */
    public static final String TOOLTIP_HANDLER = "\u00A7x";
    private static List<ITooltipLineHandler> tipLineHandlers = new ArrayList<ITooltipLineHandler>();

    public static interface ITooltipLineHandler
    {
        public Dimension getSize();

        public void draw(int x, int y);
    }

    public static int getTipLineId(ITooltipLineHandler handler) {
        tipLineHandlers.add(handler);
        return tipLineHandlers.size()-1;
    }

    public static ITooltipLineHandler getTipLine(String line) {
        if(!line.startsWith(TOOLTIP_HANDLER))
            return null;
        return tipLineHandlers.get(Integer.parseInt(line.substring(2)));
    }

    public static void drawMultilineTip(int x, int y, List<String> list) {
        if (list.isEmpty())
            return;

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        RenderHelper.disableStandardItemLighting();

        int w = 0;
        int h = -2;
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            ITooltipLineHandler line = getTipLine(s);
            Dimension d = line != null ?
                    line.getSize() :
                    new Dimension(getStringWidth(s), list.get(i).endsWith(TOOLTIP_LINESPACE) && i + 1 < list.size() ? 12 : 10);
            w = Math.max(w, d.width);
            h += d.height;
        }

        if (x < 8) x = 8;
        else if (x > displaySize().width - w - 8) x -= 24 + w;//flip side of cursor
        y = (int) MathHelper.clip(y, 8, displaySize().height - 8 - h);

        gui.incZLevel(300);
        drawTooltipBox(x - 4, y - 4, w + 7, h + 7);
        for (String s : list) {
            ITooltipLineHandler line = getTipLine(s);
            if(line != null) {
                line.draw(x, y);
                y += line.getSize().height;
            }
            else {
                fontRenderer.drawStringWithShadow(s, x, y, -1);
                y += s.endsWith(TOOLTIP_LINESPACE) ? 12 : 10;
            }
        }

        tipLineHandlers.clear();
        gui.incZLevel(-300);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
    }

    public static void drawTooltipBox(int x, int y, int w, int h) {
        int bg = 0xf0100010;
        drawGradientRect(x + 1, y, w - 1, 1, bg, bg);
        drawGradientRect(x + 1, y + h, w - 1, 1, bg, bg);
        drawGradientRect(x + 1, y + 1, w - 1, h - 1, bg, bg);//center
        drawGradientRect(x, y + 1, 1, h - 1, bg, bg);
        drawGradientRect(x + w, y + 1, 1, h - 1, bg, bg);
        int grad1 = 0x505000ff;
        int grad2 = 0x5028007F;
        drawGradientRect(x + 1, y + 2, 1, h - 3, grad1, grad2);
        drawGradientRect(x + w - 1, y + 2, 1, h - 3, grad1, grad2);

        drawGradientRect(x + 1, y + 1, w - 1, 1, grad1, grad1);
        drawGradientRect(x + 1, y + h - 1, w - 1, 1, grad2, grad2);
    }
}
