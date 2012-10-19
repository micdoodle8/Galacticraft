package micdoodle8.mods.galacticraft.moon.client;

import micdoodle8.mods.galacticraft.moon.CommonProxyMoon;
import net.minecraft.src.World;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxyMoon extends CommonProxyMoon
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}

	@Override
	public void registerRenderInformation()
	{
		
	}

	@Override
	public World getClientWorld()
	{
		return null;
	}

	@Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b) {}
}
