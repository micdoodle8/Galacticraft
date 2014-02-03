package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreSmallFontRenderer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCCoreGuiDropdown.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiDropdown extends GuiButton
{
	protected static final ResourceLocation texture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/gui.png");

	public boolean dropdownClicked;
	public String[] optionStrings;
	public int selectedOption = -1;
	public GCCoreSmallFontRenderer font;
	private IDropboxCallback parentClass;

	public GCCoreGuiDropdown(int id, IDropboxCallback parentClass, int x, int y, String... text)
	{
		super(id, x, y, 13, 13, "");
		Minecraft mc = FMLClientHandler.instance().getClient();
		this.parentClass = parentClass;
		this.font = new GCCoreSmallFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		this.optionStrings = text;

		int largestString = Integer.MIN_VALUE;

		for (String element : text)
		{
			largestString = Math.max(largestString, this.font.getStringWidth(element));
		}

		this.width = largestString + 8;
	}

	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
	{
		if (this.selectedOption == -1)
		{
			this.selectedOption = this.parentClass.getInitialSelection(this);
		}

		if (this.drawButton)
		{
			GL11.glPushMatrix();

			GL11.glTranslatef(0, 0, 200);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height), GCCoreUtil.convertTo32BitColor(255, 200, 200, 200));
			Gui.drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height) - 1, GCCoreUtil.convertTo32BitColor(255, 0, 0, 0));

			if (this.dropdownClicked && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height * this.optionStrings.length)
			{
				int hoverPos = (par3 - this.yPosition) / this.height;
				Gui.drawRect(this.xPosition + 1, this.yPosition + this.height * hoverPos + 1, this.xPosition + this.width - 1, this.yPosition + this.height * (hoverPos + 1) - 1, GCCoreUtil.convertTo32BitColor(255, 100, 100, 100));
			}

			this.mouseDragged(par1Minecraft, par2, par3);

			if (this.dropdownClicked)
			{
				for (int i = 0; i < this.optionStrings.length; i++)
				{
					this.font.drawStringWithShadow(this.optionStrings[i], this.xPosition + this.width / 2 - this.font.getStringWidth(this.optionStrings[i]) / 2, this.yPosition + (this.height - 8) / 2 + this.height * i, GCCoreUtil.convertTo32BitColor(255, 255, 255, 255));
				}
			}
			else
			{
				this.font.drawStringWithShadow(this.optionStrings[this.selectedOption], this.xPosition + this.width / 2 - this.font.getStringWidth(this.optionStrings[this.selectedOption]) / 2, this.yPosition + (this.height - 8) / 2, GCCoreUtil.convertTo32BitColor(255, 255, 255, 255));
			}

			GL11.glPopMatrix();
		}
	}

	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
	{
		if (!this.dropdownClicked)
		{
			if (this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height)
			{
				if (this.parentClass.canBeClickedBy(this, par1Minecraft.thePlayer))
				{
					this.dropdownClicked = true;
					return true;
				}
				else
				{
					this.parentClass.onIntruderInteraction();
				}
			}
		}
		else
		{
			if (this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height * this.optionStrings.length)
			{
				if (this.parentClass.canBeClickedBy(this, par1Minecraft.thePlayer))
				{
					int optionClicked = (par3 - this.yPosition) / this.height;
					this.selectedOption = optionClicked % this.optionStrings.length;
					this.dropdownClicked = false;
					this.parentClass.onSelectionChanged(this, this.selectedOption);
					return true;
				}
				else
				{
					this.parentClass.onIntruderInteraction();
				}
			}
			else
			{
				this.dropdownClicked = false;
			}
		}

		return false;
	}

	public static interface IDropboxCallback
	{
		public boolean canBeClickedBy(GCCoreGuiDropdown dropdown, EntityPlayer player);

		public void onSelectionChanged(GCCoreGuiDropdown dropdown, int selection);

		public int getInitialSelection(GCCoreGuiDropdown dropdown);

		public void onIntruderInteraction();
	}
}
