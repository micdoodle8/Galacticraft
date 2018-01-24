package micdoodle8.mods.galacticraft.core.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiElementTexturedButton extends GuiButton
{
    private final ResourceLocation texture;
    private final int bWidth, bHeight;

    public GuiElementTexturedButton(int par1, int par2, int par3, int par4, int par5, ResourceLocation texture, int width, int height)
    {
        super(par1, par2, par3, par4, par5, "");
        this.texture = texture;
        this.bWidth = width;
        this.bHeight = height;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3, float partial)
    {
        if (this.visible)
        {
            final FontRenderer var4 = par1Minecraft.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
            this.getHoverState(this.hovered);
            par1Minecraft.renderEngine.bindTexture(this.texture);
            this.drawTexturedModalRect(this.x, this.y, 0, 0, this.bWidth, this.bHeight);
            this.mouseDragged(par1Minecraft, par2, par3);
            int var6 = 14737632;

            if (!this.enabled)
            {
                var6 = -6250336;
            }
            else if (this.hovered)
            {
                var6 = 16777120;
            }

            this.drawCenteredString(var4, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, var6);
        }
    }
}
