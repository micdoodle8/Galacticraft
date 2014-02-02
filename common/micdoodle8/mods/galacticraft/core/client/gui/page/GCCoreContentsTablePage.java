package micdoodle8.mods.galacticraft.core.client.gui.page;

import micdoodle8.mods.galacticraft.core.client.GCCoreManualUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * GCCoreContentsTablePage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreContentsTablePage extends GCCoreBookPage
{
	String text;
	String[] iconText;
	ItemStack[] icons;

	@Override
	public void readPageFromXML(Element element)
	{
		NodeList nodes = element.getElementsByTagName("text");
		if (nodes != null)
		{
			this.text = nodes.item(0).getTextContent();
		}

		nodes = element.getElementsByTagName("link");
		this.iconText = new String[nodes.getLength()];
		this.icons = new ItemStack[nodes.getLength()];
		for (int i = 0; i < nodes.getLength(); i++)
		{
			NodeList children = nodes.item(i).getChildNodes();
			this.iconText[i] = children.item(1).getTextContent();
			this.icons[i] = GCCoreManualUtil.getIcon(children.item(3).getTextContent());
		}
	}

	@Override
	public void renderContentLayer(int localWidth, int localHeight)
	{
		if (this.text != null)
		{
			this.manual.font.drawString("\u00a7n" + this.text, localWidth + 25 + this.manual.font.getStringWidth(this.text) / 2, localHeight + 4, 0);
		}
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		this.manual.renderitem.zLevel = 100;
		for (int i = 0; i < this.icons.length; i++)
		{
			this.manual.renderitem.renderItemAndEffectIntoGUI(this.manual.font, this.manual.getMC().renderEngine, this.icons[i], localWidth + 16, localHeight + 18 * i + 18);
			int yOffset = 18;
			if (this.iconText[i].length() > 40)
			{
				yOffset = 13;
			}
			this.manual.font.drawString(this.iconText[i], localWidth + 38, localHeight + 18 * i + yOffset, 0);
		}
		this.manual.renderitem.zLevel = 0;
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
}
