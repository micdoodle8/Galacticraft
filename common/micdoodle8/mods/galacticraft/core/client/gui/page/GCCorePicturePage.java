package micdoodle8.mods.galacticraft.core.client.gui.page;

import net.minecraft.util.ResourceLocation;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * GCCorePicturePage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePicturePage extends GCCoreBookPage
{
	String text;
	String location;
	ResourceLocation background;

	@Override
	public void readPageFromXML(Element element)
	{
		NodeList nodes = element.getElementsByTagName("text");
		if (nodes != null)
		{
			this.text = nodes.item(0).getTextContent();
		}

		nodes = element.getElementsByTagName("location");
		if (nodes != null)
		{
			this.location = nodes.item(0).getTextContent();
		}

		this.background = new ResourceLocation(this.location);
	}

	@Override
	public void renderContentLayer(int localWidth, int localHeight)
	{
		this.manual.font.drawSplitString(this.text, localWidth + 8, localHeight, 178, 0);
	}

	@Override
	public void renderBackgroundLayer(int localWidth, int localHeight)
	{
		this.manual.getMC().getTextureManager().bindTexture(this.background);
		// manual.getMC().renderEngine.bindTexture(location);
		this.manual.drawTexturedModalRect(localWidth, localHeight + 12, 0, 0, 170, 144);
	}
}
