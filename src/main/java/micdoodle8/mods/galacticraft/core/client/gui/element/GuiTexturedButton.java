package micdoodle8.mods.galacticraft.core.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * GCCoreGuiTexturedButton.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GuiTexturedButton extends GuiButton
{
	private final ResourceLocation texture;
	private final int texWidth, texHeight;

	public GuiTexturedButton(int id, int x, int y, int width, int height, ResourceLocation texture)
	{
		this(id, x, y, width, height, texture, width, height);
	}

	public GuiTexturedButton(int id, int x, int y, int width, int height, ResourceLocation texture, int texWidth, int texHeight)
	{
		super(id, x, y, width, height, "");
		this.texture = texture;
		this.texWidth = texWidth;
		this.texHeight = texHeight;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.visible)
		{
			final FontRenderer var4 = par1Minecraft.fontRenderer;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_146123_n = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			this.getHoverState(this.field_146123_n);
			par1Minecraft.renderEngine.bindTexture(this.texture);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);
			this.mouseDragged(par1Minecraft, par2, par3);
			int var6 = 14737632;

			if (!this.enabled)
			{
				var6 = -6250336;
			}
			else if (this.field_146123_n)
			{
				var6 = 16777120;
			}

			this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var6);
		}
	}
	
	@Override
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + this.texHeight) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + this.texWidth) * f), (double)((float)(par4 + this.texHeight) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + this.texWidth) * f), (double)((float)(par4 + 0) * f1));
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
        tessellator.draw();
    }
}
