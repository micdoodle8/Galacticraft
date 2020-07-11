package micdoodle8.mods.galacticraft.core.client.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class GuiElementSlider extends Button
{
    private final Vector3 firstColor;
    private final Vector3 lastColor;
    private final boolean isVertical;
    private int sliderPos;

    public GuiElementSlider(int x, int y, int width, int height, boolean vertical, Vector3 firstColor, Vector3 lastColor)
    {
        this(x, y, width, height, vertical, firstColor, lastColor, "");
    }

    public GuiElementSlider(int x, int y, int width, int height, boolean vertical, Vector3 firstColor, Vector3 lastColor, String message)
    {
        super(x, y, width, height, message, (button) ->
        {
        });
        this.isVertical = vertical;
        this.firstColor = firstColor;
        this.lastColor = lastColor;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partial)
    {
        if (this.visible)
        {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

//            if (Mouse.isButtonDown(0) && this.isHovered())
//            {
//                if (this.isVertical)
//                {
//                    this.sliderPos = mouseY - this.y;
//                }
//                else
//                {
//                    this.sliderPos = mouseX - this.x;
//                }
//            } TODO Slider element

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param,
                    GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder worldRenderer = tessellator.getBuffer();

            if (this.isVertical)
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.x + this.width, this.y, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos(this.x, this.y, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos(this.x, (double) this.y + this.height, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos((double) this.x + this.width, (double) this.y + this.height, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                tessellator.draw();

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.x + this.width - 1, (double) this.y + 1, this.getBlitOffset()).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.x + 1, (double) this.y + 1, this.getBlitOffset()).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.x + 1, (double) this.y + this.height - 1, this.getBlitOffset()).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.x + this.width - 1, (double) this.y + this.height - 1, this.getBlitOffset()).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                tessellator.draw();

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.x + this.width, (double) this.y + this.sliderPos - 1, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos(this.x, (double) this.y + this.sliderPos - 1, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos(this.x, (double) this.y + this.sliderPos + 1, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos((double) this.x + this.width, (double) this.y + this.sliderPos + 1, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                tessellator.draw();
            }
            else
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.x + this.width, this.y, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos(this.x, this.y, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos(this.x, (double) this.y + this.height, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                worldRenderer.pos((double) this.x + this.width, (double) this.y + this.height, this.getBlitOffset()).color(0, 0, 0, 1.0F).endVertex();
                tessellator.draw();

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.x + this.width - 1, (double) this.y + 1, this.getBlitOffset()).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.x + 1, (double) this.y + 1, this.getBlitOffset()).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.x + 1, (double) this.y + this.height - 1, this.getBlitOffset()).color(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F).endVertex();
                worldRenderer.pos((double) this.x + this.width - 1, (double) this.y + this.height - 1, this.getBlitOffset()).color(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F).endVertex();
                tessellator.draw();

                GL11.glShadeModel(GL11.GL_FLAT);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param,
                        GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
                GL11.glShadeModel(GL11.GL_SMOOTH);

                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
                worldRenderer.pos((double) this.x + this.sliderPos + 1, this.y, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos((double) this.x + this.sliderPos - 1, this.y, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos((double) this.x + this.sliderPos - 1, (double) this.y + this.height, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                worldRenderer.pos((double) this.x + this.sliderPos + 1, (double) this.y + this.height, this.getBlitOffset()).color(1, 1, 1, 1.0F).endVertex();
                tessellator.draw();
            }

            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    protected void renderTooltip(ItemStack p_renderTooltip_1_, int p_renderTooltip_2_, int p_renderTooltip_3_)
    {
    }

    public void drawHoveringText()
    {
        if (this.isHovered())
        {
            Minecraft minecraft = Minecraft.getInstance();
            int i = (int) (minecraft.mouseHelper.getMouseX() * (double) minecraft.getMainWindow().getScaledWidth() / (double) minecraft.getMainWindow().getWidth());
            int j = (int) (minecraft.mouseHelper.getMouseY() * (double) minecraft.getMainWindow().getScaledHeight() / (double) minecraft.getMainWindow().getHeight());
            FontRenderer font = Minecraft.getInstance().fontRenderer;
            GuiUtils.drawHoveringText(Collections.singletonList(this.getMessage()), i, j, width, height, -1, font);
        }
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

    public float getColorValueF()
    {
        return (float) getColorValueD();
    }

    public int getButtonHeight()
    {
        return this.height;
    }
}
