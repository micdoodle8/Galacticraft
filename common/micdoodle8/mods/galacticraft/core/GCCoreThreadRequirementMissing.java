package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiMissingCore;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreThreadRequirementMissing.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreThreadRequirementMissing extends Thread
{
	private static Side threadSide;

	public GCCoreThreadRequirementMissing(Side threadSide)
	{
		super("Galacticraft Requirement Check Thread");
		this.setDaemon(true);
		GCCoreThreadRequirementMissing.threadSide = threadSide;
	}

	@Override
	public void run()
	{
		if (!Loader.isModLoaded("Micdoodlecore"))
		{
			if (GCCoreThreadRequirementMissing.threadSide.isServer())
			{
				FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("===================================================================");
				FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("MicdoodleCore not found in mods folder. Galacticraft will not load.");
				FMLCommonHandler.instance().getMinecraftServerInstance().getLogAgent().logSevere("===================================================================");
			}
			else
			{
				GCCoreThreadRequirementMissing.openGuiClient();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private static void openGuiClient()
	{
		FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiMissingCore());
	}
}
