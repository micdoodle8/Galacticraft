package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.Iterator;
import java.util.List;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreInfoRegion.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreInfoRegion extends Gui
{
	protected int width;
	protected int height;
	public int xPosition;
	public int yPosition;
	public boolean enabled;
	public boolean drawRegion;
	public boolean withinRegion;
	public List<String> tooltipStrings;
	protected static RenderItem itemRenderer = new RenderItem();
	public int parentWidth;
	public int parentHeight;

	public GCCoreInfoRegion(int par2, int par3, int par4, int par5, List<String> tooltipStrings, int parentWidth, int parentHeight)
	{
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.xPosition = par2;
		this.yPosition = par3;
		this.width = par4;
		this.height = par5;
		this.tooltipStrings = tooltipStrings;
		this.parentWidth = parentWidth;
		this.parentHeight = parentHeight;
	}

	protected int getHoverState(boolean par1)
	{
		byte b0 = 1;

		if (!this.enabled)
		{
			b0 = 0;
		}
		else if (par1)
		{
			b0 = 2;
		}

		return b0;
	}

	public void drawRegion(int par2, int par3)
	{
		this.withinRegion = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

		if (this.drawRegion)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int k = this.getHoverState(this.withinRegion);
			Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, GCCoreUtil.convertTo32BitColor(100 * k, 255, 0, 0));
		}

		if (this.tooltipStrings != null && !this.tooltipStrings.isEmpty() && this.withinRegion)
		{
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator<String> iterator = this.tooltipStrings.iterator();

			while (iterator.hasNext())
			{
				String s = iterator.next();
				int l = FMLClientHandler.instance().getClient().fontRenderer.getStringWidth(s);

				if (l > k)
				{
					k = l;
				}
			}

			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;

			if (this.tooltipStrings.size() > 1)
			{
				k1 += (this.tooltipStrings.size() - 1) * 10;
			}

			if (i1 + k > this.parentWidth)
			{
				i1 -= 28 + k;
			}

			if (j1 + k1 + 6 > this.parentHeight)
			{
				j1 = this.parentHeight - k1 - 6;
			}

			this.zLevel = 300.0F;
			GCCoreInfoRegion.itemRenderer.zLevel = 300.0F;
			int l1 = -267386864;
			this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

			for (int k2 = 0; k2 < this.tooltipStrings.size(); ++k2)
			{
				String s1 = this.tooltipStrings.get(k2);
				FMLClientHandler.instance().getClient().fontRenderer.drawStringWithShadow(s1, i1, j1, -1);

				j1 += 10;
			}

			this.zLevel = 0.0F;
			GCCoreInfoRegion.itemRenderer.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
}
