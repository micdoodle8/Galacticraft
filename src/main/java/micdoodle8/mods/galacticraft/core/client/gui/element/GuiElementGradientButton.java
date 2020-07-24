package micdoodle8.mods.galacticraft.core.client.gui.element;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;

public class GuiElementGradientButton extends Button
{
    public GuiElementGradientButton(int x, int y, int width, int height, String buttonText, Button.IPressable onPress)
    {
        super(x, y, width, height, buttonText, onPress);
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partial)
    {
        if (this.visible)
        {
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontrenderer = minecraft.fontRenderer;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
            RenderSystem.blendFunc(770, 771);
            int color = ColorUtil.to32BitColor(150, 10, 10, 10);

            if (!this.active)
            {
                if (this.isHovered())
                {
                    color = ColorUtil.to32BitColor(150, 30, 30, 30);
                }
                else
                {
                    color = ColorUtil.to32BitColor(150, 32, 32, 32);
                }
            }
            else if (this.isHovered())
            {
                color = ColorUtil.to32BitColor(150, 30, 30, 30);
            }

            this.fillGradient(this.x, this.y, this.x + this.width, this.y + this.height, color, color);
            int l = 14737632;

            if (!this.active)
            {
                l = 10526880;
            }
            else if (this.isHovered())
            {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, l);
        }
    }
}
