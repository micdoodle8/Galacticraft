package micdoodle8.mods.galacticraft.API;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IGalacticraftSubMod 
{
	public String getDimensionName();
	
	public void load(FMLInitializationEvent event);
	
	public void postLoad(FMLPostInitializationEvent event);
}
