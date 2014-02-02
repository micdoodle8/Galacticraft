package micdoodle8.mods.galacticraft.core.client.gui.page;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * GCCoreTextPage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTextPage extends GCCoreBookPage
{
	String text;

	@Override
	public void readPageFromXML(Element element)
	{
		NodeList nodes = element.getElementsByTagName("text");
		if (nodes != null)
		{
			this.text = nodes.item(0).getTextContent();
		}
	}

	@Override
	public void renderContentLayer(int localWidth, int localHeight)
	{
		this.manual.font.drawSplitString(this.text, localWidth, localHeight, 178, 0);
	}
}
