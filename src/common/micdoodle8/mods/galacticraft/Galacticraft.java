package micdoodle8.mods.galacticraft;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Packet9Respawn;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
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
@Mod(name="Galacticraft", version="v1", useMetadata = false, modid = "Galacticraft")
@NetworkMod(channels = {"Galacticraft"}, clientSideRequired = true, serverSideRequired = false)
public class Galacticraft 
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.client.ClientProxy", serverSide = "micdoodle8.mods.galacticraft.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance("Galacticraft")
	public static Galacticraft instance;
	
	public static long tick;
	
	public static List serverPlayerBaseList = new ArrayList();
	public static List serverPlayerAPIs = new ArrayList();
	
	public static GCLocalization lang;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		new GCConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/mars.conf"));
		
		lang = new GCLocalization("micdoodle8/mods/galacticraft/client");
		
		GCBlocks.initBlocks();
		GCBlocks.registerBlocks();
		GCBlocks.setHarvestLevels();
		GCBlocks.addNames();
		
		GCItems.initItems();
		GCItems.addNames();
		
		proxy.preInit(event);
		
		try
		{
			ServerPlayerAPI.register("Galacticraft", GCPlayerBaseServer.class);
		}
		catch(Exception e)
		{
			FMLLog.severe("PLAYER API NOT INSTALLED.");
			FMLLog.severe("Galacticraft will now fail to load.");
			e.printStackTrace();
		}
	}
	
	@Init
	public void init(FMLInitializationEvent event)
	{
		DimensionManager.registerProviderType(GCConfigManager.dimensionIDMars, GCWorldProvider.class, true);
		DimensionManager.registerDimension(GCConfigManager.dimensionIDMars, GCConfigManager.dimensionIDMars);
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		GCAchievementList.initAchievs();
		GCUtil.addSmeltingRecipes();
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
		proxy.init(event);
	}
	
	public void registerTileEntities()
	{
        GameRegistry.registerTileEntity(GCTileEntityTreasureChest.class, "Treasure Chest");
        GameRegistry.registerTileEntity(GCTileEntityOxygenDistributor.class, "Air Distributor");
        LanguageRegistry.instance().addStringLocalization("container.airdistributor", "en_US", "Oxygen Distributor");
	}
	
	public void registerCreatures()
	{
		registerGalacticraftCreature(GCEntityCreeperBoss.class, "Creeper Boss", 894731, 0);
		registerGalacticraftCreature(GCEntitySpider.class, "Evolved Spider", 3419431, 11013646);
		registerGalacticraftCreature(GCEntityZombie.class, "Evolved Zombie", 44975, 7969893);
		registerGalacticraftCreature(GCEntityCreeper.class, "Evolved Creeper", 894731, 0);
		registerGalacticraftCreature(GCEntitySkeleton.class, "Evolved Skeleton", 12698049, 4802889);
		registerGalacticraftCreature(GCEntitySludgeling.class, "Sludgeling", 25600, 0);
	}
	
	public void registerOtherEntities()
	{
		registerGalacticraftNonMobEntity(GCEntityProjectileTNT.class, "Projectile TNT", 150, 5, true);
		registerGalacticraftNonMobEntity(GCEntitySpaceship.class, "Spaceship", 150, 5, true);
		registerGalacticraftNonMobEntity(GCEntityArrow.class, "Gravity Arrow", 150, 5, true);
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
        TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
        NetworkRegistry.instance().registerChannel(new ServerPacketHandler(), "Galacticraft", Side.SERVER);
	}

    public void registerGalacticraftCreature(Class var0, String var1, int back, int fore)
    {
    	int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
        EntityRegistry.registerModEntity(var0, var1, id, instance, 80, 3, true);
		LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", "en_US", var1);
    }
    
    public void registerGalacticraftNonMobEntity(Class var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
    {
    	int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(var0, var1, id);
        EntityRegistry.registerModEntity(var0, var1, id, this, trackingDistance, updateFreq, sendVel);
    }
	
	public class CommonTickHandler implements ITickHandler
	{
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData) 
		{
			if (type.equals(EnumSet.of(TickType.WORLD)))
            {
				World world = (World) tickData[0];
				
				if (world.provider instanceof GCWorldProvider)
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
			return "Galacticraft Common";
		}
	}
	
    public class ServerPacketHandler implements IPacketHandler
    {
        @Override
        public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            int packetType = GCUtil.readPacketID(data);
            EntityPlayerMP player = (EntityPlayerMP)p;
            
            if (packetType == 0)
            {
                Class[] decodeAs = {String.class};
                Object[] packetReadout = GCUtil.readPacketData(data, decodeAs);

                player.openGui(instance, GCConfigManager.idGuiTankRefill, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            }
            else if (packetType == 1)
            {
                Class[] decodeAs = {String.class};
                Object[] packetReadout = GCUtil.readPacketData(data, decodeAs);
                
                player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte)player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.worldObj.getHeight(), player.theItemInWorldManager.getGameType()));
            }
            else if (packetType == 2)
            {
                Class[] decodeAs = {Integer.class};
                Object[] packetReadout = GCUtil.readPacketData(data, decodeAs);
                
                for (int j = 0; j < Galacticraft.instance.serverPlayerAPIs.size(); ++j)
	            {
	    			GCPlayerBaseServer playerBase = (GCPlayerBaseServer) Galacticraft.instance.serverPlayerAPIs.get(j);
	    			
	    			if (player.username == playerBase.getPlayer().username)
	    			{
	            		playerBase.timeUntilPortal = 15;
	    				playerBase.setInPortal((Integer)packetReadout[0]);
	    			}
	            }
            }
            else if (packetType == 3)
            {
                if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof GCEntitySpaceship)
                {
                	GCEntitySpaceship ship = (GCEntitySpaceship) player.ridingEntity;
                	
                	ship.ignite();
                }
            }
        }
    }
}
