package micdoodle8.mods.galacticraft.core.proxy;

import micdoodle8.mods.galacticraft.core.items.GCCoreItems.EnumArmorIndex;
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
}
