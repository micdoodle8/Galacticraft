package micdoodle8.mods.galacticraft.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAstroOrb;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityPlayer;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityWorm;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerHandler;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBreathableAir;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.wgen.GCCoreWorldGenVanilla;
import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
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
	
	public static List players = new ArrayList();
	public static List gcPlayers = new ArrayList();
	
	public static List<IGalacticraftSubMod> subMods = new ArrayList<IGalacticraftSubMod>();
	@SideOnly(Side.CLIENT)
	public static List<IGalacticraftSubModClient> clientSubMods = new ArrayList<IGalacticraftSubModClient>();

	public static List<IGalaxy> galaxies = new ArrayList<IGalaxy>();
	
	public static List<IMapPlanet> mapPlanets = new ArrayList<IMapPlanet>();
	public static HashMap<String, IMapPlanet> mapMoons = new HashMap<String, IMapPlanet>();
	
	public static final CreativeTabs galacticraftTab = new GCCoreCreativeTab(12, "galacticraft");
	
	public static final IGalaxy galaxyMilkyWay = new GCCoreGalaxyBlockyWay();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		moon.preLoad(event);
		
		GalacticraftCore.registerSubMod(moon);
		
		new GCCoreConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/core.conf"));
		
		lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/core/client");
		
		GCCoreBlocks.initBlocks();
		GCCoreBlocks.registerBlocks();
		GCCoreBlocks.setHarvestLevels();
		GCCoreBlocks.addNames();
		
		GCCoreItems.initItems();
		GCCoreItems.addNames();
		GCCoreItems.registerHarvestLevels();
		
		proxy.preInit(event);
	}
	
	@Init
	public void init(FMLInitializationEvent event)
	{
		for (final IGalacticraftSubMod mod : GalacticraftCore.subMods)
		{
			if (mod.getParentGalaxy() != null && !galaxies.contains(mod.getParentGalaxy()))
			{
				this.galaxies.add(mod.getParentGalaxy());
			}
			else
			{
				FMLLog.severe("Galacticraft " + mod.getDimensionName() + " error! NO GALAXY OBJECT PROVIDED");
			}
		}
		
		moon.load(event);
		
        LanguageRegistry.instance().addStringLocalization("itemGroup.galacticraft", lang.get("itemGroup.galacticraft"));
		
        GameRegistry.registerWorldGenerator(new GCCoreWorldGenVanilla());
        GCCoreUtil.addCraftingRecipes();
		GCCoreUtil.addSmeltingRecipes();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		GameRegistry.registerPlayerTracker(new GCCorePlayerHandler());
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
		MinecraftForge.EVENT_BUS.register(new GCCoreEvents());
		proxy.init(event);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		moon.postLoad(event);
		
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
	
	public static void registerSlotRenderer(IPlanetSlotRenderer renderer)
	{
		proxy.addSlotRenderer(renderer);
	}
	
	public static void registerSubMod(IGalacticraftSubMod mod)
	{
		subMods.add(mod);
	}
	
	public static void registerClientSubMod(IGalacticraftSubModClient mod)
	{
		clientSubMods.add(mod);
	}
	
	public static void addAdditionalMapPlanet(IMapPlanet planet)
	{
		mapPlanets.add(planet);
	}
	
	public static void addAdditionalMapMoon(String planet, IMapPlanet moon)
	{
		mapMoons.put(planet, moon);
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
		this.registerGalacticraftCreature(GCCoreEntitySpider.class, "Evolved Spider", GCCoreConfigManager.idEntityEvolvedSpider, 3419431, 11013646);
		this.registerGalacticraftCreature(GCCoreEntityZombie.class, "Evolved Zombie", GCCoreConfigManager.idEntityEvolvedZombie, 44975, 7969893);
		this.registerGalacticraftCreature(GCCoreEntityCreeper.class, "Evolved Creeper", GCCoreConfigManager.idEntityEvolvedCreeper, 894731, 0);
		this.registerGalacticraftCreature(GCCoreEntitySkeleton.class, "Evolved Skeleton", GCCoreConfigManager.idEntityEvolvedSkeleton, 12698049, 4802889);
		this.registerGalacticraftCreature(GCCoreEntityWorm.class, "Giant Worm", GCCoreConfigManager.idEntityGiantWorm, 12698049, 4802889);
	}
	
	public void registerOtherEntities()
	{
		this.registerGalacticraftNonMobEntity(GCCoreEntitySpaceship.class, "Spaceship", GCCoreConfigManager.idEntitySpaceship, 150, 1, true);
		this.registerGalacticraftNonMobEntity(GCCoreEntityArrow.class, "Gravity Arrow", GCCoreConfigManager.idEntityAntiGravityArrow, 150, 5, true);
		this.registerGalacticraftNonMobEntity(GCCoreEntityMeteor.class, "Meteor", GCCoreConfigManager.idEntityMeteor, 150, 5, true);
		this.registerGalacticraftNonMobEntity(GCCoreEntityBuggy.class, "Buggy", GCCoreConfigManager.idEntityBuggy, 150, 10, false);
		this.registerGalacticraftNonMobEntity(GCCoreEntityFlag.class, "Flag", GCCoreConfigManager.idEntityFlag, 150, 5, true);
		this.registerGalacticraftNonMobEntity(GCCoreEntityAstroOrb.class, "AstroOrb", GCCoreConfigManager.idEntityAstroOrb, 150, 5, true);
		this.registerGalacticraftNonMobEntity(GCCoreEntityParaChest.class, "ParaChest", GCCoreConfigManager.idEntityParaChest, 150, 5, true);
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
        public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            final int packetType = GCCoreUtil.readPacketID(data);
            final EntityPlayerMP player = (EntityPlayerMP)p;
            
            if (packetType == 0)
            {
                final Class[] decodeAs = {String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);

                player.openGui(instance, GCCoreConfigManager.idGuiTankRefill, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            }
            else if (packetType == 1)
            {
                final Class[] decodeAs = {String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte)player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.worldObj.getHeight(), player.theItemInWorldManager.getGameType()));
            }
            else if (packetType == 2)
            {
                final Class[] decodeAs = {String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                for (int j = 0; j < GalacticraftCore.gcPlayers.size(); ++j)
	            {
                	final GCCoreEntityPlayer playerBase = (GCCoreEntityPlayer) GalacticraftCore.gcPlayers.get(j);
	    			
	    			if (player.username == playerBase.getPlayer().username)
	    			{
	    	    		final Integer dim = GCCoreUtil.getProviderForName((String)packetReadout[0]).dimensionId;
	    				playerBase.travelToTheEnd(dim);
	    			}
	            }
            }
            else if (packetType == 3)
            {
                if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;
                	
                	ship.ignite();
                }
            }
            else if (packetType == 4)
            {
                final Class[] decodeAs = {Integer.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
            	if (player != null)
            	{
            		
            		switch ((Integer)packetReadout[0])
            		{
            		case 0:
            			player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRocketCraftingBench, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            			break;
            		case 1:
            			player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiBuggyCraftingBench, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            			break;
            		case 2:
            			break;
            		}
            	}
            }
            else if (packetType == 5)
            {
                final Class[] decodeAs = {Integer.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                for(int i = 0; i < player.worldObj.getLoadedEntityList().size(); i++)
                {
	                if(((Entity)player.worldObj.getLoadedEntityList().get(i)).entityId == (Integer)packetReadout[0])
	                {
	                	if (player.worldObj.getLoadedEntityList().get(i) instanceof EntityLiving)
	                	{
	                        final Object[] toSend = {((EntityLiving)player.worldObj.getLoadedEntityList().get(i)).getHealth(), (Integer)packetReadout[0]};
	                        
	                        player.playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 3, toSend));
	                	}
	                }
                }
            }
            else if (packetType == 6)
            {
                final Class[] decodeAs = {Integer.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                if (player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                    player.displayGUIChest((GCCoreEntitySpaceship)player.ridingEntity);
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
				final World world = (World) tickData[0];
				
				if (world.provider.getDimensionName() == "Overworld" && !world.isRemote)
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
