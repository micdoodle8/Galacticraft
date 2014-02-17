package micdoodle8.mods.galacticraft.core.proxy;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.items.GCItems.EnumArmorIndex;
import net.minecraft.block.Block;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{

	}

	public void init(FMLInitializationEvent event)
	{

	}

	public void postInit(FMLPostInitializationEvent event)
	{

	}

	public int getArmorRenderID(EnumArmorIndex type)
	{
		return -1;
	}

	public int getBlockRenderID(Block blockID)
	{
		return -1;
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
