package micdoodle8.mods.galacticraft.core.client.gui.page;

import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiManual;

import org.w3c.dom.Element;

/**
 * GCCoreBookPage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author mDiyo, micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreBookPage
{
	protected GCCoreGuiManual manual;
	protected int side;

	public void init(GCCoreGuiManual manual, int side)
	{
		this.manual = manual;
		this.side = side;
	}

	public abstract void readPageFromXML(Element element);

	public void renderBackgroundLayer(int localwidth, int localheight)
	{
	}

	public abstract void renderContentLayer(int localwidth, int localheight);
}
