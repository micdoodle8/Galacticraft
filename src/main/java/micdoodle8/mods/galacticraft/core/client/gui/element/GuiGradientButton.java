package micdoodle8.mods.galacticraft.core.client.gui.element;

import org.lwjgl.opengl.GL11;

import micdoodle8.mods.galacticraft.core.util.CoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;

public class GuiGradientButton extends GuiButton
{
	public GuiGradientButton(int id, int x, int y, int width, int height, String buttonText)
	{
		super(id, x, y, width, height, buttonText);
	}

    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = p_146112_1_.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.field_146123_n ? CoreUtil.to32BitColor(150, 30, 30, 30) : CoreUtil.to32BitColor(150, 10, 10, 10), this.field_146123_n ? CoreUtil.to32BitColor(250, 30, 30, 30) : CoreUtil.to32BitColor(250, 10, 10, 10));
            this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
            int l = 14737632;

            if (packedFGColour != 0)
            {
                l = packedFGColour;
            }
            else if (!this.enabled)
            {
                l = 10526880;
            }
            else if (this.field_146123_n)
            {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
        }
    }
}
