package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

/**
 * GCCoreGuiContainer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreGuiContainer extends GuiContainer
{
	public List<GCCoreInfoRegion> infoRegions = new ArrayList<GCCoreInfoRegion>();

	public GCCoreGuiContainer(Container container)
	{
		super(container);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);

		for (int k = 0; k < this.infoRegions.size(); ++k)
		{
			GCCoreInfoRegion guibutton = this.infoRegions.get(k);
			guibutton.drawRegion(par1, par2);
		}
	}

	@Override
	public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3)
	{
		this.infoRegions.clear();
		super.setWorldAndResolution(par1Minecraft, par2, par3);
	}
}
