package micdoodle8.mods.galacticraft.core.client.gui.page;

import micdoodle8.mods.galacticraft.core.client.GCCoreManualUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * GCCoreToolPage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreToolPage extends GCCoreBookPage
{
	String title;
	ItemStack[] icons;
	String[] iconText;

	@Override
	public void readPageFromXML(Element element)
	{
		NodeList nodes = element.getElementsByTagName("title");
		if (nodes != null)
		{
			this.title = nodes.item(0).getTextContent();
		}

		nodes = element.getElementsByTagName("item");
		this.iconText = new String[nodes.getLength() + 2];
		this.icons = new ItemStack[nodes.getLength() + 1];

		for (int i = 0; i < nodes.getLength(); i++)
		{
			NodeList children = nodes.item(i).getChildNodes();
			this.iconText[i + 2] = children.item(1).getTextContent();
			this.icons[i + 1] = GCCoreManualUtil.getIcon(children.item(3).getTextContent());
		}

		nodes = element.getElementsByTagName("text");
		if (nodes != null)
		{
			this.iconText[0] = nodes.item(0).getTextContent();
			this.iconText[1] = nodes.item(1).getTextContent();
		}

		nodes = element.getElementsByTagName("icon");
		if (nodes != null)
		{
			this.icons[0] = GCCoreManualUtil.getIcon(nodes.item(0).getTextContent());
		}
	}

	@Override
	public void renderContentLayer(int localWidth, int localHeight)
	{
		this.manual.font.drawString("\u00a7n" + this.title, localWidth + 70, localHeight + 4, 0);
		this.manual.font.drawSplitString(this.iconText[0], localWidth, localHeight + 16, 178, 0);
		int size = this.iconText[0].length() / 48;
		this.manual.font.drawSplitString(this.iconText[1], localWidth, localHeight + 28 + 10 * size, 118, 0);

		this.manual.font.drawString("Crafting Parts: ", localWidth + 124, localHeight + 28 + 10 * size, 0);

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		this.manual.renderitem.zLevel = 100;
		this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[0], localWidth + 50, localHeight + 0);
		for (int i = 1; i < this.icons.length; i++)
		{
			this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[i], localWidth + 120, localHeight + 20 + 10 * size + 18 * i);
			int partOffset = this.iconText[i + 1].length() > 11 ? -3 : 0;
			this.manual.font.drawSplitString(this.iconText[i + 1], localWidth + 140, localHeight + 24 + 10 * size + 18 * i + partOffset, 44, 0);
		}
		this.manual.renderitem.zLevel = 0;
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
}
