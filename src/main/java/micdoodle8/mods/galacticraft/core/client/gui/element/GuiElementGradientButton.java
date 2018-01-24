package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class GuiElementGradientButton extends GuiButton
{
    public GuiElementGradientButton(int id, int x, int y, int width, int height, String buttonText)
    {
        super(id, x, y, width, height, buttonText);
    }

    @Override
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_, float partial)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = p_146112_1_.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = p_146112_2_ >= this.x && p_146112_3_ >= this.y && p_146112_2_ < this.x + this.width && p_146112_3_ < this.y + this.height;
            int k = this.getHoverState(this.hovered);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int color = ColorUtil.to32BitColor(150, 10, 10, 10);

            if (!this.enabled)
            {
                if (this.hovered)
                {
                    color = ColorUtil.to32BitColor(150, 30, 30, 30);
                }
                else
                {
                    color = ColorUtil.to32BitColor(150, 32, 32, 32);
                }
            }
            else if (this.hovered)
            {
                color = ColorUtil.to32BitColor(150, 30, 30, 30);
            }

            this.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.height, color, color);
            this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
            int l = 14737632;

            if (this.packedFGColour != 0)
            {
                l = this.packedFGColour;
            }
            else if (!this.enabled)
            {
                l = 10526880;
            }
            else if (this.hovered)
            {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, l);
        }
    }
}
