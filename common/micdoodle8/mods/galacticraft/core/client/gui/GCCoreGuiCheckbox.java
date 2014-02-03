package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * GCCoreGuiCheckbox.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiCheckbox extends GuiButton
{
	protected static final ResourceLocation texture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/gui.png");

	public Boolean isSelected;

	private ICheckBoxCallback parentGui;

	public GCCoreGuiCheckbox(int id, ICheckBoxCallback parentGui, int x, int y, String text)
	{
		super(id, x, y, 13, 13, text);
		this.parentGui = parentGui;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.isSelected == null)
		{
			this.isSelected = this.parentGui.getInitiallySelected(this);
		}

		if (this.drawButton)
		{
			par1Minecraft.getTextureManager().bindTexture(GCCoreGuiCheckbox.texture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			this.drawTexturedModalRect(this.xPosition, this.yPosition, this.isSelected ? 33 : 20, this.field_82253_i ? 24 : 37, this.width, this.height);
			this.mouseDragged(par1Minecraft, par2, par3);
			par1Minecraft.fontRenderer.drawString(this.displayString, this.xPosition + this.width + 3, this.yPosition + (this.height - 6) / 2, 4210752, false);
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height)
		{
			if (this.parentGui.canPlayerEdit(this, par1Minecraft.thePlayer))
			{
				this.isSelected = !this.isSelected;
				this.parentGui.onSelectionChanged(this, this.isSelected);
				return true;
			}
			else
			{
				this.parentGui.onIntruderInteraction();
			}
		}

		return false;
	}

	public static interface ICheckBoxCallback
	{
		public void onSelectionChanged(GCCoreGuiCheckbox checkbox, boolean newSelected);

		public boolean canPlayerEdit(GCCoreGuiCheckbox checkbox, EntityPlayer player);

		public boolean getInitiallySelected(GCCoreGuiCheckbox checkbox);

		public void onIntruderInteraction();
	}
}
