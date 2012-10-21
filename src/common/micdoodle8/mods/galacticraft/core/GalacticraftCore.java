package micdoodle8.mods.galacticraft.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import micdoodle8.mods.galacticraft.core.client.GCCoreEvents;
import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Packet9Respawn;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;
import net.minecraft.src.WorldProviderSurface;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@Mod(name="Galacticraft Core", version="v1", useMetadata = false, modid = "GalacticraftCore")
@NetworkMod(channels = {"GalacticraftCore"}, clientSideRequired = true, serverSideRequired = false)
public class GalacticraftCore 
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.client.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.CommonProxyCore")
	public static CommonProxyCore proxy;
	
	@Instance("GalacticraftCore")
	public static GalacticraftCore instance;
	
	public static GCCoreLocalization lang;
	
	public static GalacticraftMoon moon = new GalacticraftMoon();
	
	public static long tick;
	
	public static List serverPlayerBaseList = new ArrayList();
	public static List serverPlayerAPIs = new ArrayList();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		moon.preInit(event);
		
		new GCCoreConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/core.conf"));
		
		lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/core/client");
		
		GCCoreBlocks.initBlocks();
		GCCoreBlocks.registerBlocks();
		GCCoreBlocks.setHarvestLevels();
		GCCoreBlocks.addNames();
		
		GCCoreItems.initItems();
		GCCoreItems.addNames();
		
		try
		{
			ServerPlayerAPI.register("Galacticraft", GCCorePlayerBaseServer.class);
		}
		catch(Exception e)
		{
			FMLLog.severe("PLAYER API NOT INSTALLED.");
			FMLLog.severe("Galacticraft will now fail to load.");
			e.printStackTrace();
		}
		
		proxy.preInit(event);
	}
	
	@Init
	public void init(FMLInitializationEvent event)
	{
		moon.init(event);
		
//		GCCoreUtil.addSmeltingRecipes(); TODO
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
		MinecraftForge.EVENT_BUS.register(new GCCoreEvents());
		proxy.init(event);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
		proxy.registerRenderInformation();
	}
	
	@ServerStarted
	public void serverInit(FMLServerStartedEvent event)
	{
		moon.serverInit(event);
		
        TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
        NetworkRegistry.instance().registerChannel(new ServerPacketHandler(), "Galacticraft", Side.SERVER);
	}
	
	public void registerTileEntities()
	{
        GameRegistry.registerTileEntity(GCCoreTileEntityTreasureChest.class, "Treasure Chest");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenDistributor.class, "Air Distributor");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenCollector.class, "Air Collector");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenPipe.class, "Oxygen Pipe");
        GameRegistry.registerTileEntity(GCCoreTileEntityBreathableAir.class, "Breathable Air");
        LanguageRegistry.instance().addStringLocalization("container.airdistributor", "en_US", "Oxygen Distributor");
	}
	
	public void registerCreatures()
	{
		registerGalacticraftCreature(GCCoreEntitySpider.class, "Evolved Spider", GCCoreConfigManager.idEntityEvolvedSpider, 3419431, 11013646);
		registerGalacticraftCreature(GCCoreEntityZombie.class, "Evolved Zombie", GCCoreConfigManager.idEntityEvolvedZombie, 44975, 7969893);
		registerGalacticraftCreature(GCCoreEntityCreeper.class, "Evolved Creeper", GCCoreConfigManager.idEntityEvolvedCreeper, 894731, 0);
		registerGalacticraftCreature(GCCoreEntitySkeleton.class, "Evolved Skeleton", GCCoreConfigManager.idEntityEvolvedSkeleton, 12698049, 4802889);
	}
	
	public void registerOtherEntities()
	{
		registerGalacticraftNonMobEntity(GCCoreEntitySpaceship.class, "Spaceship", GCCoreConfigManager.idEntitySpaceship, 150, 5, true);
		registerGalacticraftNonMobEntity(GCCoreEntityArrow.class, "Gravity Arrow", GCCoreConfigManager.idEntityAntiGravityArrow, 150, 5, true);
		registerGalacticraftNonMobEntity(GCCoreEntityMeteor.class, "Meteor", GCCoreConfigManager.idEntityMeteor, 150, 5, true);
	}

    public void registerGalacticraftCreature(Class var0, String var1, int id, int back, int fore)
    {
    	EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
        EntityRegistry.registerModEntity(var0, var1, id, instance, 80, 3, true);
		LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", "en_US", var1);
    }
    
    public void registerGalacticraftNonMobEntity(Class var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
    {
        EntityRegistry.registerModEntity(var0, var1, id, this, trackingDistance, updateFreq, sendVel);
    }
    
    public class ServerPacketHandler implements IPacketHandler
    {
        @Override
        public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            int packetType = GCCoreUtil.readPacketID(data);
            EntityPlayerMP player = (EntityPlayerMP)p;
            
            if (packetType == 0)
            {
                Class[] decodeAs = {String.class};
                Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);

                player.openGui(instance, GCCoreConfigManager.idGuiTankRefill, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            }
            else if (packetType == 1)
            {
                Class[] decodeAs = {String.class};
                Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte)player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.worldObj.getHeight(), player.theItemInWorldManager.getGameType()));
            }
            else if (packetType == 2)
            {
                Class[] decodeAs = {Integer.class};
                Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                for (int j = 0; j < GalacticraftCore.instance.serverPlayerAPIs.size(); ++j)
	            {
	    			GCCorePlayerBaseServer playerBase = (GCCorePlayerBaseServer) GalacticraftCore.instance.serverPlayerAPIs.get(j);
	    			
	    			if (player.username == playerBase.getPlayer().username)
	    			{
	            		playerBase.timeUntilPortal = 15;
	    				playerBase.setInPortal((Integer)packetReadout[0]);
	    			}
	            }
            }
            else if (packetType == 3)
            {
                if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                	GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;
                	
                	ship.ignite();
                }
            }
        }
    }
	
	public class CommonTickHandler implements ITickHandler
	{
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData) 
		{
			if (type.equals(EnumSet.of(TickType.WORLD)))
            {
				World world = (World) tickData[0];
				
				if (world.provider instanceof WorldProviderSurface)
				{
					tick++;
				}
            }
		}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

		@Override
		public EnumSet<TickType> ticks() 
		{
			return EnumSet.of(TickType.WORLD);
		}

		@Override
		public String getLabel() 
		{
			return "Galacticraft Core Common";
		}
	}
}
