package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiElementSlider extends GuiButton
{
    private Vector3 firstColor;
    private Vector3 lastColor;
    private final boolean isVertical;
    private int sliderPos;

    public GuiElementSlider(int id, int x, int y, int width, int height, boolean vertical, Vector3 firstColor, Vector3 lastColor)
    {
        this(id, x, y, width, height, vertical, firstColor, lastColor, "");
    }

    public GuiElementSlider(int id, int x, int y, int width, int height, boolean vertical, Vector3 firstColor, Vector3 lastColor, String displayString)
    {
        super(id, x, y, width, height, displayString);
        this.isVertical = vertical;
        this.firstColor = firstColor;
        this.lastColor = lastColor;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.visible)
        {
            this.hovered = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

            if (Mouse.isButtonDown(0) && this.hovered)
            {
                if (this.isVertical)
                {
                    this.sliderPos = par3 - this.yPosition;
                }
                else
                {
                    this.sliderPos = par2 - this.xPosition;
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();

            if (this.isVertical)
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.xPosition + this.width, this.yPosition, this.zLevel).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos(this.xPosition, this.yPosition, this.zLevel).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos(this.xPosition, (double) this.yPosition + this.height, this.zLevel).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + this.width, (double) this.yPosition + this.height, this.zLevel).color(0, 0, 0, 1.0F).endVertex();
                tessellator.draw();

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.xPosition + this.width - 1, (double) this.yPosition + 1, this.zLevel).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + 1, (double) this.yPosition + 1, this.zLevel).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + 1, (double) this.yPosition + this.height - 1, this.zLevel).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + this.width - 1, (double) this.yPosition + this.height - 1, this.zLevel).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                tessellator.draw();

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.xPosition + this.width, (double) this.yPosition + this.sliderPos - 1, this.zLevel).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos(this.xPosition, (double) this.yPosition + this.sliderPos - 1, this.zLevel).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos(this.xPosition, (double) this.yPosition + this.sliderPos + 1, this.zLevel).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + this.width, (double) this.yPosition + this.sliderPos + 1, this.zLevel).color(1, 1, 1, 1.0F).endVertex();
                tessellator.draw();
            }
            else
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.xPosition + this.width, this.yPosition, this.zLevel).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos(this.xPosition, this.yPosition, this.zLevel).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos(this.xPosition, (double) this.yPosition + this.height, this.zLevel).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + this.width, (double) this.yPosition + this.height, this.zLevel).color(0, 0, 0, 1.0F).endVertex();
                tessellator.draw();

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.xPosition + this.width - 1, (double) this.yPosition + 1, this.zLevel).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + 1, (double) this.yPosition + 1, this.zLevel).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + 1, (double) this.yPosition + this.height - 1, this.zLevel).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + this.width - 1, (double) this.yPosition + this.height - 1, this.zLevel).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                tessellator.draw();

                GL11.glShadeModel(GL11.GL_FLAT);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glShadeModel(GL11.GL_SMOOTH);

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.xPosition + this.sliderPos + 1, this.yPosition, this.zLevel).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + this.sliderPos - 1, this.yPosition, this.zLevel).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + this.sliderPos - 1, (double) this.yPosition + this.height, this.zLevel).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos((double) this.xPosition + this.sliderPos + 1, (double) this.yPosition + this.height, this.zLevel).color(1, 1, 1, 1.0F).endVertex();
                tessellator.draw();
            }

            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void drawHoveringText()
    {
        if (this.hovered)
        {
            FontRenderer font = FMLClientHandler.instance().getClient().fontRendererObj;
            Minecraft mc = FMLClientHandler.instance().getClient();
            ScaledResolution scaledresolution = ClientUtil.getScaledRes(mc, mc.displayWidth, mc.displayHeight);
            int width = scaledresolution.getScaledWidth();
            int height = scaledresolution.getScaledHeight();
            int x = Mouse.getX() * width / mc.displayWidth;
            int y = height - Mouse.getY() * height / mc.displayHeight - 1;
            drawHoveringText(Collections.singletonList(this.displayString), x, y, width, height, -1, font);
        }
    }
    
    public static void drawHoveringText(List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight,
            int maxTextWidth, FontRenderer font)
    {
        if (!textLines.isEmpty())
        {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int tooltipTextWidth = 0;

            for (String textLine : textLines)
            {
                int textLineWidth = font.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth)
                {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth)
            {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2)
                    {
                        tooltipTextWidth = mouseX - 12 - 8;
                    }
                    else
                    {
                        tooltipTextWidth = screenWidth - 16 - mouseX;
                    }
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth)
            {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap)
            {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = new ArrayList<String>();
                for (int i = 0; i < textLines.size(); i++)
                {
                    String textLine = textLines.get(i);
                    List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);
                    if (i == 0)
                    {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine)
                    {
                        int lineWidth = font.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth)
                        {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (mouseX > screenWidth / 2)
                {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                }
                else
                {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1)
            {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2; // gap between title lines and next lines
                }
            }

            if (tooltipY + tooltipHeight + 6 > screenHeight)
            {
                tooltipY = screenHeight - tooltipHeight - 6;
            }

            final int zLevel = 300;
            final int backgroundColor = 0xF0100010;
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            final int borderColorStart = 0x505000FF;
            final int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            int tooltipTop = tooltipY;
            
            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber)
            {
                String line = textLines.get(lineNumber);
                font.drawStringWithShadow(line, (float)tooltipX, (float)tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount)
                {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
    
    public static void drawGradientRect(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float startAlpha = (float)(startColor >> 24 & 255) / 255.0F;
        float startRed = (float)(startColor >> 16 & 255) / 255.0F;
        float startGreen = (float)(startColor >> 8 & 255) / 255.0F;
        float startBlue = (float)(startColor & 255) / 255.0F;
        float endAlpha = (float)(endColor >> 24 & 255) / 255.0F;
        float endRed = (float)(endColor >> 16 & 255) / 255.0F;
        float endGreen = (float)(endColor >> 8 & 255) / 255.0F;
        float endBlue = (float)(endColor & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(right, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        vertexbuffer.pos(left, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        vertexbuffer.pos(left, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        vertexbuffer.pos(right, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void setSliderPos(float pos)
    {
        this.sliderPos = (int) Math.floor(this.height * pos);
    }

    public int getSliderPos()
    {
        return this.sliderPos;
    }

    public float getNormalizedValue()
    {
        return this.sliderPos / (float) this.height;
    }

    public double getColorValueD()
    {
        return (this.sliderPos * 255.0D) / (this.height - 1);
    }

    public int getButtonHeight()
    {
        return this.height;
    }
}
