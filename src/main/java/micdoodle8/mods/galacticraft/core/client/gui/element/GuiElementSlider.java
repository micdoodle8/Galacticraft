package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.gui.screen.SmallFontRenderer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiElementSlider extends GuiButton
{
    private SmallFontRenderer customFontRenderer;
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
        this.customFontRenderer = new SmallFontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.visible)
        {
            this.field_146123_n = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

            if (Mouse.isButtonDown(0) && this.field_146123_n)
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
            Tessellator tessellator = Tessellator.instance;

            if (this.isVertical)
            {
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F(0, 0, 0, 1.0F);
                tessellator.addVertex((double) this.xPosition + this.width, this.yPosition, this.zLevel);
                tessellator.addVertex(this.xPosition, this.yPosition, this.zLevel);
                tessellator.addVertex(this.xPosition, (double) this.yPosition + this.height, this.zLevel);
                tessellator.addVertex((double) this.xPosition + this.width, (double) this.yPosition + this.height, this.zLevel);
                tessellator.draw();

                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F);
                tessellator.addVertex((double) this.xPosition + this.width - 1, (double) this.yPosition + 1, this.zLevel);
                tessellator.addVertex((double) this.xPosition + 1, (double) this.yPosition + 1, this.zLevel);
                tessellator.setColorRGBA_F(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F);
                tessellator.addVertex((double) this.xPosition + 1, (double) this.yPosition + this.height - 1, this.zLevel);
                tessellator.addVertex((double) this.xPosition + this.width - 1, (double) this.yPosition + this.height - 1, this.zLevel);
                tessellator.draw();

                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F(1, 1, 1, 1.0F);
                tessellator.addVertex((double) this.xPosition + this.width, (double) this.yPosition + this.sliderPos - 1, this.zLevel);
                tessellator.addVertex(this.xPosition, (double) this.yPosition + this.sliderPos - 1, this.zLevel);
                tessellator.addVertex(this.xPosition, (double) this.yPosition + this.sliderPos + 1, this.zLevel);
                tessellator.addVertex((double) this.xPosition + this.width, (double) this.yPosition + this.sliderPos + 1, this.zLevel);
                tessellator.draw();
            }
            else
            {
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F(0, 0, 0, 1.0F);
                tessellator.addVertex((double) this.xPosition + this.width, this.yPosition, this.zLevel);
                tessellator.addVertex(this.xPosition, this.yPosition, this.zLevel);
                tessellator.addVertex(this.xPosition, (double) this.yPosition + this.height, this.zLevel);
                tessellator.addVertex((double) this.xPosition + this.width, (double) this.yPosition + this.height, this.zLevel);
                tessellator.draw();

                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F);
                tessellator.addVertex((double) this.xPosition + this.width - 1, (double) this.yPosition + 1, this.zLevel);
                tessellator.setColorRGBA_F(this.firstColor.floatX(), this.firstColor.floatY(), this.firstColor.floatZ(), 1.0F);
                tessellator.addVertex((double) this.xPosition + 1, (double) this.yPosition + 1, this.zLevel);
                tessellator.addVertex((double) this.xPosition + 1, (double) this.yPosition + this.height - 1, this.zLevel);
                tessellator.setColorRGBA_F(this.lastColor.floatX(), this.lastColor.floatY(), this.lastColor.floatZ(), 1.0F);
                tessellator.addVertex((double) this.xPosition + this.width - 1, (double) this.yPosition + this.height - 1, this.zLevel);
                tessellator.draw();

                GL11.glShadeModel(GL11.GL_FLAT);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                if (this.displayString != null && this.displayString.length() > 0)
                {
                    GL11.glPushMatrix();
                    GL11.glTranslatef(this.xPosition + this.width / 2, this.yPosition + this.height / 2, 0.0F);
                    GL11.glScalef(0.5F, 0.5F, 1.0F);
                    GL11.glTranslatef(-1 * (this.xPosition + this.width / 2), -1 * (this.yPosition + this.height / 2), 0.0F);
                    this.customFontRenderer.drawString(this.displayString, this.xPosition + this.width / 2 - this.customFontRenderer.getStringWidth(this.displayString) / 2, this.yPosition + this.height / 2 - 3, GCCoreUtil.to32BitColor(255, 240, 240, 240));
                    GL11.glPopMatrix();
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glShadeModel(GL11.GL_SMOOTH);

                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F(1, 1, 1, 1.0F);
                tessellator.addVertex((double) this.xPosition + this.sliderPos + 1, this.yPosition, this.zLevel);
                tessellator.addVertex((double) this.xPosition + this.sliderPos - 1, this.yPosition, this.zLevel);
                tessellator.addVertex((double) this.xPosition + this.sliderPos - 1, (double) this.yPosition + this.height, this.zLevel);
                tessellator.addVertex((double) this.xPosition + this.sliderPos + 1, (double) this.yPosition + this.height, this.zLevel);
                tessellator.draw();
            }

            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
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
        return (this.sliderPos * 255.0D) / (double) (this.height - 1);
    }

    public int getButtonHeight()
    {
        return this.height;
    }
}
