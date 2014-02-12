package micdoodle8.mods.galacticraft.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.client.gui.GuiHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.network.PacketPipeline;
import micdoodle8.mods.galacticraft.core.proxy.CommonProxy;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = GalacticraftCore.MOD_ID, name = GalacticraftCore.MOD_NAME, dependencies = "required-after:Forge@[7.0,);required-after:FML@[5.0.5,)")
public class GalacticraftCore 
{
	public static final String MOD_ID = "GalacticraftCore";
	public static final String MOD_NAME = "Galacticraft Core";
	public static final String ASSET_DOMAIN = "galacticraftcore";
	public static final String ASSET_PREFIX = GalacticraftCore.ASSET_DOMAIN + ":";
	public static final String CONFIG_FILE = "Galacticraft/core.conf";
	
    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.proxy.ClientProxy", serverSide = "micdoodle8.mods.galacticraft.core.proxy.CommonProxy")
	public static CommonProxy proxy;
    @Instance(GalacticraftCore.MOD_ID)
    public static GalacticraftCore instance;
    public static PacketPipeline packetPipeline;
    
	public static Map<String, GCCorePlayerSP> playersClient = new HashMap<String, GCCorePlayerSP>();
	public static Map<String, GCCorePlayerMP> playersServer = new HashMap<String, GCCorePlayerMP>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
		
		GCCoreConfigManager.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		
		this.packetPipeline = new PacketPipeline();
		this.packetPipeline.initalise();
		
		FMLCommonHandler.instance().bus().register(new GCCoreTickHandlerClient());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
}
