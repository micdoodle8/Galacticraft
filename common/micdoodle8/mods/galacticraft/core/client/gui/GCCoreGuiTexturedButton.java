package micdoodle8.mods.galacticraft.core.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
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
public class GCCoreGuiTexturedButton extends GuiButton
{
	private final ResourceLocation texture;
	private final int bWidth, bHeight;

	public GCCoreGuiTexturedButton(int par1, int par2, int par3, int par4, int par5, ResourceLocation texture, int width, int height)
	{
		super(par1, par2, par3, par4, par5, "");
		this.texture = texture;
		this.bWidth = width;
		this.bHeight = height;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.drawButton)
		{
			final FontRenderer var4 = par1Minecraft.fontRenderer;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			this.getHoverState(this.field_82253_i);
			par1Minecraft.renderEngine.bindTexture(this.texture);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.bWidth, this.bHeight);
			this.mouseDragged(par1Minecraft, par2, par3);
			int var6 = 14737632;

			if (!this.enabled)
			{
				var6 = -6250336;
			}
			else if (this.field_82253_i)
			{
				var6 = 16777120;
			}

			this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var6);
		}
	}
}
