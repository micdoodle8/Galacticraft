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
import micdoodle8.mods.galacticraft.API.IInterplanetaryObject;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAstroOrb;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
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
import micdoodle8.mods.galacticraft.core.network.GCCorePacketControllableEntity;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketEntityUpdate;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAdvancedCraftingTable;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLock;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBreathableAir;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPadSingle;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
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
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.prefab.CustomDamageSource;
import universalelectricity.prefab.TranslationHelper;
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
@Mod(name=GalacticraftCore.NAME, version="v1", useMetadata = false, modid=GalacticraftCore.MODID, dependencies = "required-after:Forge@[6.6.0.497,)")
@NetworkMod(channels = {GalacticraftCore.CHANNEL}, clientSideRequired = true, serverSideRequired = false)
public class GalacticraftCore
{
	public static final String NAME = "Galacticraft Core";
	public static final String MODID = "GalacticraftCore";
	public static final String CHANNEL = "GalacticraftCore";

    public static final int LOCALMAJVERSION = 0;
    public static final int LOCALMINVERSION = 1;
    public static final int LOCALBUILDVERSION = 21;
    public static int remoteMajVer;
    public static int remoteMinVer;
    public static int remoteBuildVer;

	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.client.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.CommonProxyCore")
	public static CommonProxyCore proxy;

	@Instance(GalacticraftCore.MODID)
	public static GalacticraftCore instance;

	public static GalacticraftMoon moon = new GalacticraftMoon();

	public static long tick;
	public static long slowTick;

	public static Map<String, GCCorePlayerBaseClient> playersClient = new HashMap<String, GCCorePlayerBaseClient>();
	public static Map<String, GCCorePlayerBase> playersServer = new HashMap<String, GCCorePlayerBase>();

	public static List<IGalacticraftSubMod> subMods = new ArrayList<IGalacticraftSubMod>();
	public static List<IGalacticraftSubModClient> clientSubMods = new ArrayList<IGalacticraftSubModClient>();

	public static List<IGalaxy> galaxies = new ArrayList<IGalaxy>();

	public static List<IMapPlanet> mapPlanets = new ArrayList<IMapPlanet>();
	public static DupKeyHashMap mapMoons = new DupKeyHashMap();

	public static CreativeTabs galacticraftTab;

	public static final IGalaxy galaxyMilkyWay = new GCCoreGalaxyBlockyWay();

	public static final String FILE_PATH = "/micdoodle8/mods/galacticraft/core/";
	public static final String CLIENT_PATH = "client/";
	public static final String LANGUAGE_PATH = FILE_PATH + CLIENT_PATH + "lang/";
	public static final String BLOCK_TEXTURE_FILE = FILE_PATH + CLIENT_PATH + "blocks/core.png";
	public static final String ITEM_TEXTURE_FILE = FILE_PATH + CLIENT_PATH + "items/core.png";
	public static final String CONFIG_FILE = "Galacticraft/core.conf";
	private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "zh_CN", "fr_CA", "fr_FR" };

	public static final CustomDamageSource spaceshipCrash = (CustomDamageSource) new CustomDamageSource("spaceshipCrash").setDeathMessage("%1$s was in a spaceship crash!").setDamageBypassesArmor();
	public static final CustomDamageSource oxygenSuffocation = (CustomDamageSource) new CustomDamageSource("oxygenSuffocation").setDeathMessage("%1$s ran out of oxygen!").setDamageBypassesArmor();
	
	public static double BuildcraftEnergyScalar = 0.2;

	public static ArrayList<Integer> hiddenItems = new ArrayList<Integer>();

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		GalacticraftCore.moon.preLoad(event);
		
		try
		{
			ServerPlayerAPI.register(GalacticraftCore.MODID, GCCorePlayerBase.class);
		}
		catch (Exception e)
		{
			FMLLog.severe("PLAYER API NOT INSTALLED!");
			e.printStackTrace();
		}
		
		GalacticraftCore.registerSubMod(GalacticraftCore.moon);

		new GCCoreConfigManager(new File(event.getModConfigurationDirectory(), CONFIG_FILE));

		GCCoreBlocks.initBlocks();
		GCCoreBlocks.registerBlocks();
		GCCoreBlocks.setHarvestLevels();

		GCCoreItems.initItems();
		GCCoreItems.registerHarvestLevels();

		GalacticraftCore.proxy.preInit(event);
	}

	@Init
	public void init(FMLInitializationEvent event)
	{
		this.galacticraftTab = new GCCoreCreativeTab(CreativeTabs.getNextID(), GalacticraftCore.CHANNEL, GCCoreItems.spaceship.itemID, 0);
		
		GalacticraftCore.proxy.init(event);
		
		for (final IGalacticraftSubMod mod : GalacticraftCore.subMods)
		{
			if (mod.getParentGalaxy() != null && !GalacticraftCore.galaxies.contains(mod.getParentGalaxy()))
			{
				GalacticraftCore.galaxies.add(mod.getParentGalaxy());
			}
		}

		System.out.println("Galacticraft Loaded: " + TranslationHelper.loadLanguages(LANGUAGE_PATH, LANGUAGES_SUPPORTED) + " Languages.");

		GalacticraftCore.moon.load(event);

        RecipeUtil.addCraftingRecipes();
        RecipeUtil.addSmeltingRecipes();
		NetworkRegistry.instance().registerGuiHandler(this, GalacticraftCore.proxy);
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
		MinecraftForge.EVENT_BUS.register(new GCCoreEvents());
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

		GCCoreUtil.checkVersion(Side.SERVER);
        TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
        TickRegistry.registerScheduledTickHandler(new CommonTickHandlerSlow(), Side.SERVER);
        NetworkRegistry.instance().registerChannel(new ServerPacketHandler(), GalacticraftCore.CHANNEL, Side.SERVER);
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
        GameRegistry.registerTileEntity(GCCoreTileEntityRefinery.class, "Refinery");
        GameRegistry.registerTileEntity(GCCoreTileEntityAdvancedCraftingTable.class, "NASA Workbench");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenCompressor.class, "Air Compressor");
        GameRegistry.registerTileEntity(GCCoreTileEntityFuelLoader.class, "Fuel Loader");
        GameRegistry.registerTileEntity(GCCoreTileEntityLandingPadSingle.class, "Landing Pad");
        GameRegistry.registerTileEntity(GCCoreTileEntityLandingPad.class, "Landing Pad Full");
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

            final int packetType = PacketUtil.readPacketID(data);

            final EntityPlayerMP player = (EntityPlayerMP)p;

        	final GCCorePlayerBase playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player);

            if (packetType == 0)
            {
                final Class[] decodeAs = {String.class};
                PacketUtil.readPacketData(data, decodeAs);

                player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiTankRefill, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            }
            else if (packetType == 1)
            {
                final Class[] decodeAs = {String.class};
                PacketUtil.readPacketData(data, decodeAs);

                player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte)player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.worldObj.getHeight(), player.theItemInWorldManager.getGameType()));
            }
            else if (packetType == 2)
            {
                final Class[] decodeAs = {String.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

                if (playerBase != null && playerBase.teleportCooldown <= 0)
                {
    	    		final Integer dim = WorldUtil.getProviderForName((String)packetReadout[0]).dimensionId;
    	    		playerBase.travelToTheEnd(dim);
    	    		playerBase.teleportCooldown = 300;
    	    		final Object[] toSend = {player.username};
    	    		player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 12, toSend));
                }
            }
            else if (packetType == 3)
            {
                if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;

                	final ItemStack stack = ship.getStackInSlot(0);

                	if (ship.hasFuelTank())
                	{
                		ItemStack stack2 = null;

                		if (playerBase != null && playerBase.playerTankInventory != null)
                		{
                			stack2 = playerBase.playerTankInventory.getStackInSlot(4);
                		}

	    				if (stack2 != null && stack2.getItem() instanceof GCCoreItemParachute || playerBase != null && playerBase.launchAttempts > 0)
	    				{
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
                		FMLLog.warning("Player (" + player.username + ") doesn't have rocket fuel to launch spaceship. If player DOES, please report the following line as a bug");
                		FMLLog.warning("STACKNULL: " + (stack == null) + " ISSTACKFUEL: " + (stack == null ? "false" : stack.getItem().itemID == GCCoreItems.rocketFuelBucket.itemID) + " PLAYERBASENULL: " + (playerBase == null) + " PLAYERBASETANKINVENTORYNULL " + (playerBase == null ? "true" : playerBase.playerTankInventory == null));
                		GalacticraftCore.this.chatCooldown = 250;
                	}
                }
            }
            else if (packetType == 4)
            {
                final Class[] decodeAs = {Integer.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

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
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

                for(int i = 0; i < player.worldObj.loadedEntityList.size(); i++)
                {
	                if(((Entity)player.worldObj.loadedEntityList.get(i)).entityId == (Integer)packetReadout[0])
	                {
	                	if (player.worldObj.loadedEntityList.get(i) instanceof EntityLiving)
	                	{
	                        final Object[] toSend = {((EntityLiving)player.worldObj.loadedEntityList.get(i)).getHealth(), (Integer)packetReadout[0]};

	                        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 3, toSend));
	                	}
	                }
                }
            }
            else if (packetType == 6)
            {
                final Class[] decodeAs = {Integer.class};
                PacketUtil.readPacketData(data, decodeAs);

                if (player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                	player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
                }
            }
            else if (packetType == 7)
            {
                final Class[] decodeAs = {Float.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

                if (player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;

                	if (ship != null)
                	{
                		ship.rotationYaw = (Float) packetReadout[0];
                	}
                }
            }
            else if (packetType == 8)
            {
                final Class[] decodeAs = {Float.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

                if (player.ridingEntity instanceof GCCoreEntitySpaceship)
                {
                	final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;

                	if (ship != null)
                	{
                		ship.rotationPitch = (Float) packetReadout[0];
                	}
                }
            }
//            else if (packetType == 9)
//            {
//                final Class[] decodeAs = {Integer.class};
//                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
//
//                if (player.ridingEntity instanceof GCCoreEntityControllable)
//                {
//                	final GCCoreEntityControllable controllableEntity = (GCCoreEntityControllable) player.ridingEntity;
//
//                	if (controllableEntity != null)
//                	{
//                		controllableEntity.keyPressed((Integer) packetReadout[0], player);
//                	}
//                }
//            }
            else if (packetType == 10)
            {
                final Class[] decodeAs = {Integer.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

                for (final Object object : player.worldObj.loadedEntityList)
                {
                	if (object instanceof EntityLiving)
                	{
                		final EntityLiving entity = (EntityLiving) object;

                		if (entity.entityId == (Integer) packetReadout[0])
                		{
                			entity.setFire(3);
                		}
                	}
                }
            }
            else if (packetType == 11)
            {
                final Class[] decodeAs = {Integer.class, Integer.class, Integer.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

                player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRefinery, player.worldObj, (Integer)packetReadout[0], (Integer)packetReadout[1], (Integer)packetReadout[2]);
            }
            else if (packetType == 12)
            {
                try
                {
    	    		new GCCorePacketControllableEntity().handlePacket(data, new Object[] {player}, Side.SERVER);
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                }
            }
            else if (packetType == 13)
            {
            }
            else if (packetType == 14)
            {
                try
                {
    	    		new GCCorePacketEntityUpdate().handlePacket(data, new Object[] {player}, Side.SERVER);
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                }
            }
        }
    }

	public class CommonTickHandlerSlow implements IScheduledTickHandler
	{
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData)
		{
			GalacticraftCore.slowTick++;
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
				GalacticraftCore.tick++;

				if (GalacticraftCore.this.chatCooldown > 0)
				{
					GalacticraftCore.this.chatCooldown--;
				}
            }
			else if (type.equals(EnumSet.of(TickType.WORLD)))
			{
				WorldServer world = (WorldServer) tickData[0];
				for (Object o : world.getLoadedEntityList())
				{
					if (o instanceof Entity && o instanceof IInterplanetaryObject)
					{
						Entity e = (Entity) o;
						IInterplanetaryObject iiobject = (IInterplanetaryObject) e;
						
						if (e.posY >= iiobject.getYCoordToTeleportFrom() && e.dimension != iiobject.getDimensionForTeleport())
						{
							WorldUtil.travelToDimension(e, world, iiobject.getDimensionForTeleport());
						}
					}
				}
			}
		}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

		@Override
		public EnumSet<TickType> ticks()
		{
			return EnumSet.of(TickType.SERVER, TickType.WORLD);
		}

		@Override
		public String getLabel()
		{
			return "Galacticraft Core Common";
		}
	}
}
