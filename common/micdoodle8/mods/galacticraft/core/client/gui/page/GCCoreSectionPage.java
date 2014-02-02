package micdoodle8.mods.galacticraft.core.client.gui.page;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * GCCoreSectionPage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSectionPage extends GCCoreBookPage
{
	String title;
	String body;

	@Override
	public void readPageFromXML(Element element)
	{
		NodeList nodes = element.getElementsByTagName("title");
		if (nodes != null)
		{
			this.title = nodes.item(0).getTextContent();
		}

		nodes = element.getElementsByTagName("text");
		if (nodes != null)
		{
			this.body = nodes.item(0).getTextContent();
		}
	}

	@Override
	public void renderContentLayer(int localWidth, int localHeight)
	{
		this.manual.font.drawSplitString("\u00a7n" + this.title, localWidth + 70, localHeight + 4, 178, 0);
		this.manual.font.drawSplitString(this.body, localWidth, localHeight + 16, 190, 0);
	}
}
