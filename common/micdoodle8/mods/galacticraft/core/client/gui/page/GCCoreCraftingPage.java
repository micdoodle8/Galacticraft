package micdoodle8.mods.galacticraft.core.client.gui.page;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.GCCoreManualUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cpw.mods.fml.common.FMLLog;

/**
 * GCCoreCraftingPage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreCraftingPage extends GCCoreBookPage
{
	String text;
	String size;
	ItemStack[] icons;
	private static final ResourceLocation background = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/bookcrafting.png");

	@Override
	public void readPageFromXML(Element element)
	{
		NodeList nodes = element.getElementsByTagName("text");
		if (nodes != null)
		{
			this.text = nodes.item(0).getTextContent();
		}

		nodes = element.getElementsByTagName("name");
		if (nodes != null)
		{
			this.icons = GCCoreManualUtil.getRecipeIcons(nodes.item(0).getTextContent());
		}

		nodes = element.getElementsByTagName("size");
		if (nodes != null)
		{
			this.size = nodes.item(0).getTextContent();
		}
	}

	@Override
	public void renderContentLayer(int localWidth, int localHeight)
	{
		if (this.size.equals("two"))
		{
			this.drawCraftingPage(this.text, this.icons, 2, localWidth, localHeight + 12);
		}
		if (this.size.equals("three"))
		{
			this.drawCraftingPage(this.text, this.icons, 3, localWidth + (this.side != 1 ? 6 : 0), localHeight + 12);
		}
	}

	public void drawCraftingPage(String info, ItemStack[] icons, int recipeSize, int localWidth, int localHeight)
	{
		if (info != null)
		{
			this.manual.font.drawString("\u00a7n" + info, localWidth + 50, localHeight + 4, 0);
		}

		GL11.glScalef(2f, 2f, 2f);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		this.manual.renderitem.zLevel = 100;

		if (recipeSize == 2)
		{
			this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, icons[0], (localWidth + 126) / 2, (localHeight + 68) / 2);
			if (icons[0].stackSize > 1)
			{
				this.manual.renderitem.renderItemOverlayIntoGUI(this.manual.font, this.manual.getMC().renderEngine, icons[0], (localWidth + 126) / 2, (localHeight + 68) / 2, String.valueOf(icons[0].stackSize));
			}
			for (int i = 0; i < icons.length - 1; i++)
			{
				if (icons[i + 1] != null)
				{
					this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, icons[i + 1], (localWidth + 14 + 36 * (i % 2)) / 2, (localHeight + 36 * (i / 2) + 52) / 2);
				}
			}
		}

		if (recipeSize == 3)
		{
			FMLLog.info("" + this.manual);
			FMLLog.info("" + this.manual.renderitem);
			FMLLog.info("" + this.manual.font);
			FMLLog.info("" + this.manual.getMC());
			FMLLog.info("" + this.manual.getMC().renderEngine);
			FMLLog.info("" + icons);
			this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, icons[0], (localWidth + 138) / 2, (localHeight + 70) / 2);
			if (icons[0].stackSize > 1)
			{
				this.manual.renderitem.renderItemOverlayIntoGUI(this.manual.font, this.manual.getMC().renderEngine, icons[0], (localWidth + 126) / 2, (localHeight + 68) / 2, String.valueOf(icons[0].stackSize));
			}
			for (int i = 0; i < icons.length - 1; i++)
			{
				if (icons[i + 1] != null)
				{
					this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, icons[i + 1], (localWidth - 2 + 36 * (i % 3)) / 2, (localHeight + 36 * (i / 3) + 34) / 2);
				}
			}
		}

		this.manual.renderitem.zLevel = 0;
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

	@Override
	public void renderBackgroundLayer(int localwidth, int localheight)
	{
		if (this.size.equals("two"))
		{
			this.drawBackground(2, localwidth, localheight + 12);
		}

		if (this.size.equals("three"))
		{
			this.drawBackground(3, localwidth + (this.side != 1 ? 6 : 0), localheight + 12);
		}
	}

	public void drawBackground(int size, int localWidth, int localHeight)
	{
		this.manual.getMC().getTextureManager().bindTexture(GCCoreCraftingPage.background);
		if (size == 2)
		{
			this.manual.drawTexturedModalRect(localWidth + 8, localHeight + 46, 0, 116, 154, 78);
		}
		if (size == 3)
		{
			this.manual.drawTexturedModalRect(localWidth - 8, localHeight + 28, 0, 0, 183, 114);
		}
	}

}
