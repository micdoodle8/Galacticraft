package micdoodle8.mods.galacticraft.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAstroOrb;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityControllable;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityWorm;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerBase;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLock;
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
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IScheduledTickHandler;
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
	
	public static Map<String, GCCorePlayerBaseClient> playersClient = new HashMap<String, GCCorePlayerBaseClient>();
	public static Map<String, GCCorePlayerBase> playersServer = new HashMap<String, GCCorePlayerBase>();
	
	public static List<IGalacticraftSubMod> subMods = new ArrayList<IGalacticraftSubMod>();
	public static List<IGalacticraftSubModClient> clientSubMods = new ArrayList<IGalacticraftSubModClient>();

	public static List<IGalaxy> galaxies = new ArrayList<IGalaxy>();
	
	public static List<IMapPlanet> mapPlanets = new ArrayList<IMapPlanet>();
	public static DupKeyHashMap mapMoons = new DupKeyHashMap();
	
	public static final CreativeTabs galacticraftTab = new GCCoreCreativeTab(12, "galacticraft");
	
	public static final IGalaxy galaxyMilkyWay = new GCCoreGalaxyBlockyWay();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		GalacticraftCore.moon.preLoad(event);
		
		GalacticraftCore.registerSubMod(GalacticraftCore.moon);
		
		new GCCoreConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/core.conf"));
		
		GalacticraftCore.lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/core/client");
		
		ServerPlayerAPI.register("GalacticraftCore", GCCorePlayerBase.class);
		
		GCCoreBlocks.initBlocks();
		GCCoreBlocks.registerBlocks();
		GCCoreBlocks.setHarvestLevels();
		GCCoreBlocks.addNames();
		
		GCCoreItems.initItems();
		GCCoreItems.addNames();
		GCCoreItems.registerHarvestLevels();
		
		OreDictionary.registerOre("oreCopper", new ItemStack(GCCoreBlocks.blockOres, 1, 0));
		OreDictionary.registerOre("oreAluminium", new ItemStack(GCCoreBlocks.blockOres, 1, 1));
		OreDictionary.registerOre("oreTitanium", new ItemStack(GCCoreBlocks.blockOres, 1, 2));

		OreDictionary.registerOre("ingotCopper", new ItemStack(GCCoreItems.ingotCopper));
		OreDictionary.registerOre("ingotAluminium", new ItemStack(GCCoreItems.ingotAluminum));
		OreDictionary.registerOre("ingotTitanium", new ItemStack(GCCoreItems.ingotTitanium));
		
		GalacticraftCore.proxy.preInit(event);
	}
	
	@Init
	public void init(FMLInitializationEvent event)
	{
		for (final IGalacticraftSubMod mod : GalacticraftCore.subMods)
		{
			if (mod.getParentGalaxy() != null && !GalacticraftCore.galaxies.contains(mod.getParentGalaxy()))
			{
				GalacticraftCore.galaxies.add(mod.getParentGalaxy());
			}
			else
			{
				FMLLog.severe("Galacticraft " + mod.getDimensionName() + " error! NO GALAXY OBJECT PROVIDED");
			}
		}
		
		GalacticraftCore.moon.load(event);
		
        LanguageRegistry.instance().addStringLocalization("itemGroup.galacticraft", GalacticraftCore.lang.get("itemGroup.galacticraft"));
		
        GameRegistry.registerWorldGenerator(new GCCoreWorldGenVanilla());
        GCCoreUtil.addCraftingRecipes();
		GCCoreUtil.addSmeltingRecipes();
		NetworkRegistry.instance().registerGuiHandler(this, GalacticraftCore.proxy);
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
		MinecraftForge.EVENT_BUS.register(new GCCoreEvents());
		GalacticraftCore.proxy.init(event);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		GalacticraftCore.moon.postLoad(event);
		
		GalacticraftCore.proxy.postInit(event);
		GalacticraftCore.proxy.registerRenderInformation();
	}
	
	@ServerStarted
	public void serverInit(FMLServerStartedEvent event)
	{
		GalacticraftCore.moon.serverInit(event);
		
        TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
        TickRegistry.registerScheduledTickHandler(new CommonTickHandlerSlow(), Side.SERVER);
        NetworkRegistry.instance().registerChannel(new ServerPacketHandler(), "Galacticraft", Side.SERVER);
	}
	
	public static void registerSlotRenderer(IPlanetSlotRenderer renderer)
	{
		GalacticraftCore.proxy.addSlotRenderer(renderer);
	}
	
	public static void registerSubMod(IGalacticraftSubMod mod)
	{
		GalacticraftCore.subMods.add(mod);
	}
	
	public static void registerClientSubMod(IGalacticraftSubModClient mod)
	{
		GalacticraftCore.clientSubMods.add(mod);
	}
	
	public static void addAdditionalMapPlanet(IMapPlanet planet)
	{
		GalacticraftCore.mapPlanets.add(planet);
	}
	
	public static void addAdditionalMapMoon(IMapPlanet planet, IMapPlanet moon)
	{
		GalacticraftCore.mapMoons.put(planet, moon);
	}
	
	public void registerTileEntities()
	{
        GameRegistry.registerTileEntity(GCCoreTileEntityTreasureChest.class, "Treasure Chest");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenDistributor.class, "Air Distributor");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenCollector.class, "Air Collector");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenPipe.class, "Oxygen Pipe");
        GameRegistry.registerTileEntity(GCCoreTileEntityBreathableAir.class, "Breathable Air");
        GameRegistry.registerTileEntity(GCCoreTileEntityAirLock.class, "Air Lock Frame");
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
		this.registerGalacticraftNonMobEntity(GCCoreEntityBuggy.class, "Buggy", GCCoreConfigManager.idEntityBuggy, 150, 5, true);
		this.registerGalacticraftNonMobEntity(GCCoreEntityFlag.class, "Flag", GCCoreConfigManager.idEntityFlag, 150, 5, true);
		this.registerGalacticraftNonMobEntity(GCCoreEntityAstroOrb.class, "AstroOrb", GCCoreConfigManager.idEntityAstroOrb, 150, 5, true);
		this.registerGalacticraftNonMobEntity(GCCoreEntityParaChest.class, "ParaChest", GCCoreConfigManager.idEntityParaChest, 150, 5, true);
	}

    public void registerGalacticraftCreature(Class var0, String var1, int id, int back, int fore)
    {
    	EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
        EntityRegistry.registerModEntity(var0, var1, id, GalacticraftCore.instance, 80, 3, true);
		LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", "en_US", var1);
    }
    
    public void registerGalacticraftNonMobEntity(Class var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
    {
        EntityRegistry.registerModEntity(var0, var1, id, this, trackingDistance, updateFreq, sendVel);
    }
    
	public int chatCooldown;
    
    public class ServerPacketHandler implements IPacketHandler
    {
        @Override
        public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            
            final int packetType = GCCoreUtil.readPacketID(data);
            
            final EntityPlayerMP player = (EntityPlayerMP)p;
        	
        	GCCorePlayerBase playerBase = GCCoreUtil.getPlayerBaseServerFromPlayer(player);
            
            if (packetType == 0)
            {
                final Class[] decodeAs = {String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);

                player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiTankRefill, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
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
                
	    		final Integer dim = GCCoreUtil.getProviderForName((String)packetReadout[0]).dimensionId;
	    		GCCoreUtil.getPlayerBaseServerFromPlayer(player).travelToTheEnd(dim);
            }
            else if (packetType == 3)
            {
                if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;
                	
                	final ItemStack stack = ship.getStackInSlot(27);
                	
                	if (stack != null && stack.getItem().itemID == GCCoreItems.rocketFuelBucket.itemID && playerBase != null && playerBase.playerTankInventory != null)
                	{
	    				final ItemStack stack2 = playerBase.playerTankInventory.getStackInSlot(4);
	    				
	    				if (stack2 != null && stack2.getItem() instanceof GCCoreItemParachute || playerBase.launchAttempts > 0)
	    				{
	    					Object[] toSend = {""};
	    					player.playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 12, toSend));
	                    	ship.ignite();
	                    	playerBase.launchAttempts = 0;
	    				}
	                	else if (GalacticraftCore.this.chatCooldown == 0 && playerBase.launchAttempts == 0)
	                	{
	                		player.sendChatToPlayer("I don't have a parachute! If I press launch again, there's no going back!");
	                		GalacticraftCore.this.chatCooldown = 250;
	                		playerBase.launchAttempts = 1;
	                	}
	    			}
                	else if (GalacticraftCore.this.chatCooldown == 0)
                	{
                		player.sendChatToPlayer("I'll probably need some Rocket Fuel before this will fly!");
                		GalacticraftCore.this.chatCooldown = 250;
                	}
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
                
                for(int i = 0; i < player.worldObj.loadedEntityList.size(); i++)
                {
	                if(((Entity)player.worldObj.loadedEntityList.get(i)).entityId == (Integer)packetReadout[0])
	                {
	                	if (player.worldObj.loadedEntityList.get(i) instanceof EntityLiving)
	                	{
	                        final Object[] toSend = {((EntityLiving)player.worldObj.loadedEntityList.get(i)).getHealth(), (Integer)packetReadout[0]};
	                        
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
                	player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
                }
            }
            else if (packetType == 7)
            {
                final Class[] decodeAs = {Float.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                if (player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;
                	
                	if (ship != null)
                	{
                		ship.turnYaw((Float) packetReadout[0]);
                	}
                }
            }
            else if (packetType == 8)
            {
                final Class[] decodeAs = {Float.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                if (player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;
                	
                	if (ship != null)
                	{
                		ship.turnPitch((Float) packetReadout[0]);
                	}
                }
            }
            else if (packetType == 9)
            {
                final Class[] decodeAs = {Integer.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);

                if (player.ridingEntity instanceof GCCoreEntityControllable)
                {
                	final GCCoreEntityControllable controllableEntity = (GCCoreEntityControllable) player.ridingEntity;
                	
                	if (controllableEntity != null)
                	{
                		controllableEntity.keyPressed((Integer) packetReadout[0], player);
                	}
                }
            }
            else if (packetType == 10)
            {
                final Class[] decodeAs = {Integer.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                for (Object object : player.worldObj.loadedEntityList)
                {
                	if (object instanceof EntityLiving)
                	{
                		EntityLiving entity = (EntityLiving) object;
                		
                		if (entity.entityId == (Integer) packetReadout[0])
                		{
                			entity.setFire(3);
                		}
                	}
                }
            }
        }
    }
	
	public class CommonTickHandlerSlow implements IScheduledTickHandler
	{
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData)
		{
			GalacticraftCore.tick++;
		}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

		@Override
		public EnumSet<TickType> ticks()
		{
			return EnumSet.of(TickType.SERVER);
		}

		@Override
		public String getLabel()
		{
			return "Galacticraft Core Common Slow";
		}

		@Override
		public int nextTickSpacing()
		{
			return 1;
		}
		
	}
	
	public class CommonTickHandler implements ITickHandler
	{
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData)
		{
			if (type.equals(EnumSet.of(TickType.SERVER)))
            {
				if (GalacticraftCore.this.chatCooldown > 0)
				{
					GalacticraftCore.this.chatCooldown--;
				}
            }
		}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

		@Override
		public EnumSet<TickType> ticks()
		{
			return EnumSet.of(TickType.SERVER);
		}

		@Override
		public String getLabel()
		{
			return "Galacticraft Core Common";
		}
	}
}
