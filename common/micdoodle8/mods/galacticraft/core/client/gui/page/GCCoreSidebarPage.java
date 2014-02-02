package micdoodle8.mods.galacticraft.core.client.gui.page;

import micdoodle8.mods.galacticraft.core.client.GCCoreManualUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * GCCoreSidebarPage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSidebarPage extends GCCoreBookPage
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

		nodes = element.getElementsByTagName("item");
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
		this.manual.font.drawSplitString(this.text, localWidth, localHeight, 178, 0);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		this.manual.renderitem.zLevel = 100;
		int offset = this.text.length() / 4 + 10;
		for (int i = 0; i < this.icons.length; i++)
		{
			if (this.iconText[i].length() > 40)
			{
			}
			this.manual.font.drawSplitString(this.iconText[i], localWidth + 30, localHeight + 18 * i + offset, 140, 0);
		}
		this.manual.renderitem.zLevel = 0;
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
}
