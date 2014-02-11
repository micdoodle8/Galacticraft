package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
import net.minecraft.world.World;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * CommonProxyCore.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class CommonProxyCore
{
	public void preInit(FMLPreInitializationEvent event)
	{
		;
	}

	public void init(FMLInitializationEvent event)
	{
		;
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		;
	}

	public void registerRenderInformation()
	{
		;
	}

	public int getBlockRenderID(int blockID)
	{
		return -1;
	}

	public int getTitaniumArmorRenderIndex()
	{
		return 0;
	}

	public int getSensorArmorRenderIndex()
	{
		return 0;
	}

	public World getClientWorld()
	{
		return null;
	}

	public void addSlotRenderer(ICelestialBodyRenderer slotRenderer)
	{
		;
	}

	public void spawnParticle(String particleID, Vector3 position, Vector3 motion)
	{
		;
	}

	public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Vector3 color)
	{
		;
	}
}
