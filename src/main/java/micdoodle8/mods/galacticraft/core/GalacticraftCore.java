package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.network.PacketPipeline;
import micdoodle8.mods.galacticraft.core.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GalacticraftCore.MOD_ID, name = GalacticraftCore.MOD_NAME, dependencies = "required-after:Forge@[7.0,);required-after:FML@[5.0.5,)")
public class GalacticraftCore 
{
	public static final String MOD_ID = "GalacticraftCore";
	public static final String MOD_NAME = "Galacticraft Core";
	public static String ASSET_DOMAIN = "galacticraftcore";
	public static String ASSET_PREFIX = GalacticraftCore.ASSET_DOMAIN + ":";
	
    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.proxy.ClientProxy", serverSide = "micdoodle8.mods.galacticraft.core.proxy.CommonProxy")
	public static CommonProxy proxy;
    @Instance(GalacticraftCore.MOD_ID)
    public static GalacticraftCore instance;
    public static PacketPipeline packetPipeline;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		
		this.packetPipeline = new PacketPipeline();
		
//		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
}
