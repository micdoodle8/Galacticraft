package codechicken.lib.gui;

import codechicken.lib.colour.Colour;
import codechicken.lib.math.MathHelper;
import codechicken.lib.texture.TextureUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiDraw {

    public static class GuiHook extends Gui {

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
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    public static TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;

    public static void drawRect(int x, int y, int w, int h, int colour) {
        drawGradientRect(x, y, w, h, colour, colour);
    }

    public static void drawGradientRectDirect(int left, int top, int right, int bottom, int colour1, int colour2) {
        gui.drawGradientRect(left, top, right, bottom, colour1, colour2);
    }

    public static void drawGradientRect(int x, int y, int w, int h, int colour1, int colour2) {
        gui.drawGradientRect(x, y, x + w, y + h, colour1, colour2);
    }

    public static void drawTexturedModalRect(int x, int y, int tx, int ty, int w, int h) {
        gui.drawTexturedModalRect(x, y, tx, ty, w, h);
    }

    public static void drawLine(int x1, int y1, int x2, int y2, float thickness, int colour) {
        GlStateManager.glLineWidth(thickness);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Colour.glColourRGBA(colour);

        VertexBuffer vb = Tessellator.getInstance().getBuffer();
        vb.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        vb.pos(x1, y1, gui.getZLevel()).endVertex();
        vb.pos(x2, y2, gui.getZLevel()).endVertex();
        vb.finishDrawing();

        GlStateManager.resetColor();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void drawString(String text, int x, int y, int colour, boolean shadow) {
        if (shadow) {
            fontRenderer.drawStringWithShadow(text, x, y, colour);
        } else {
            fontRenderer.drawString(text, x, y, colour);
        }
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
        if (s == null || s.equals("")) {
            return 0;
        }
        return fontRenderer.getStringWidth(TextFormatting.getTextWithoutFormattingCodes(s));
    }

    //TODO, getDisplaySize.
    public static Dimension displaySize() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);
        return new Dimension(res.getScaledWidth(), res.getScaledHeight());
    }

    //TODO, getDisplayRes
    public static Dimension displayRes() {
        Minecraft mc = Minecraft.getMinecraft();
        return new Dimension(mc.displayWidth, mc.displayHeight);
    }

    public static Point getMousePosition(int eventX, int eventY) {
        Dimension size = displaySize();
        Dimension res = displayRes();
        return new Point(eventX * size.width / res.width, size.height - eventY * size.height / res.height - 1);
    }

    public static Point getMousePosition() {
        return getMousePosition(Mouse.getX(), Mouse.getY());
    }

    public static void changeTexture(String s) {
        TextureUtils.changeTexture(s);
    }

    public static void changeTexture(ResourceLocation r) {
        TextureUtils.changeTexture(r);
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
    @Deprecated
    public static final String TOOLTIP_HANDLER = "\u00A7x";
    @Deprecated
    private static List<ITooltipLineHandler> tipLineHandlers = new ArrayList<ITooltipLineHandler>();

    @Deprecated//This is dead as there are forge events.
    public interface ITooltipLineHandler {

        Dimension getSize();

        void draw(int x, int y);
    }

    @Deprecated
    public static int getTipLineId(ITooltipLineHandler handler) {
        return -1;
    }

    public static ITooltipLineHandler getTipLine(String line) {
        return null;
    }

    @Deprecated
    public static void drawMultilineTip(int x, int y, List<String> list) {
        drawMultilineTip(null, x, y, list);
    }

    public static void drawMultilineTip(@Nullable ItemStack stack, int x, int y, List<String> lines) {
        //TODO TOOLTIP_LINESPACE support, pr forge to clip the box in the top bound of the screen + TOOLTIP_LINESPACE
        /*if (lines.isEmpty()) {
            return;
        }

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();

        int w = 0;
        int h = -2;
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            ITooltipLineHandler line = getTipLine(s);
            Dimension d = line != null ? line.getSize() : new Dimension(getStringWidth(s), lines.get(i).endsWith(TOOLTIP_LINESPACE) && i + 1 < lines.size() ? 12 : 10);
            w = Math.max(w, d.width);
            h += d.height;
        }

        if (x < 8) {
            x = 8;
        } else if (x > displaySize().width - w - 8) {
            x -= 24 + w;//flip side of cursor
            if (x < 8) {
                x = 8;
            }
        }
        y = (int) MathHelper.clip(y, 8, displaySize().height - 8 - h);

        gui.incZLevel(300);
        drawTooltipBox(x - 4, y - 4, w + 7, h + 7);
        for (String s : lines) {
            ITooltipLineHandler line = getTipLine(s);
            if (line != null) {
                line.draw(x, y);
                y += line.getSize().height;
            } else {
                fontRenderer.drawStringWithShadow(s, x, y, -1);
                y += s.endsWith(TOOLTIP_LINESPACE) ? 12 : 10;
            }
        }

        tipLineHandlers.clear();
        gui.incZLevel(-300);

        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();*/

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int screenWidth = res.getScaledWidth();
        int screenHeight = res.getScaledHeight();

        if (lines.isEmpty()) {
            return;
        }

        RenderTooltipEvent.Pre event = new RenderTooltipEvent.Pre(stack, lines, x, y, screenWidth, screenHeight, -1, fontRenderer);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return;
        }
        x = event.getX();
        y = event.getY();
        screenWidth = event.getScreenWidth();
        screenHeight = event.getScreenHeight();
        int maxTextWidth = event.getMaxWidth();
        FontRenderer font = event.getFontRenderer();

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        if (x < 8) {
            x = 8;
        }
        y = MathHelper.clip(y, 8, displaySize().height - 8);
        int tooltipTextWidth = 0;

        for (String textLine : lines) {
            int textLineWidth = font.getStringWidth(textLine);

            if (textLineWidth > tooltipTextWidth) {
                tooltipTextWidth = textLineWidth;
            }
        }

        boolean needsWrap = false;

        int titleLinesCount = 1;
        int tooltipX = x + 12;
        if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
            tooltipX = x - 16 - tooltipTextWidth;
            if (tooltipX < 4) // if the tooltip doesn't fit on the screen
            {
                if (x > screenWidth / 2) {
                    tooltipTextWidth = x - 12 - 8;
                } else {
                    tooltipTextWidth = screenWidth - 16 - x;
                }
                needsWrap = true;
            }
        }

        if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
            tooltipTextWidth = maxTextWidth;
            needsWrap = true;
        }

        if (needsWrap) {
            int wrappedTooltipWidth = 0;
            List<String> wrappedTextLines = new ArrayList<String>();
            for (int i = 0; i < lines.size(); i++) {
                String textLine = lines.get(i);
                List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                if (i == 0) {
                    titleLinesCount = wrappedLine.size();
                }

                for (String line : wrappedLine) {
                    int lineWidth = font.getStringWidth(line);
                    if (lineWidth > wrappedTooltipWidth) {
                        wrappedTooltipWidth = lineWidth;
                    }
                    wrappedTextLines.add(line);
                }
            }
            tooltipTextWidth = wrappedTooltipWidth;
            lines = wrappedTextLines;

            if (x > screenWidth / 2) {
                tooltipX = x - 16 - tooltipTextWidth;
            } else {
                tooltipX = x;
            }
        }

        int tooltipY = y;
        int tooltipHeight = 8;

        if (lines.size() > 1) {
            tooltipHeight += (lines.size() - 1) * 10;
            if (lines.size() > titleLinesCount) {
                tooltipHeight += 2; // gap between title lines and next lines
            }
        }

        if (tooltipY + tooltipHeight + 6 > screenHeight) {
            tooltipY = screenHeight - tooltipHeight - 6;
        }

        int backgroundColor = 0xF0100010;
        drawGradientRectDirect(tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
        drawGradientRectDirect(tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
        drawGradientRectDirect(tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
        drawGradientRectDirect(tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
        drawGradientRectDirect(tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
        int borderColorStart = 0x505000FF;
        int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
        drawGradientRectDirect(tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
        drawGradientRectDirect(tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
        drawGradientRectDirect(tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
        drawGradientRectDirect(tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

        MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostBackground(stack, lines, tooltipX, tooltipY, font, tooltipTextWidth, tooltipHeight));
        int tooltipTop = tooltipY;

        for (int lineNumber = 0; lineNumber < lines.size(); ++lineNumber) {
            String line = lines.get(lineNumber);
            font.drawStringWithShadow(line, (float) tooltipX, (float) tooltipY, -1);

            if (lineNumber + 1 == titleLinesCount) {
                tooltipY += 2;
            }

            tooltipY += 10;
        }

        MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostText(stack, lines, tooltipX, tooltipTop, font, tooltipTextWidth, tooltipHeight));

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();

    }

    @Deprecated//TODO 1.11, update this to do what is done in the other method(vague, i know what it means tho :P)
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
