package micdoodle8.mods.galacticraft.core.client;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.API.ISchematicPage;
import micdoodle8.mods.galacticraft.API.ISchematicResultPage;
import micdoodle8.mods.galacticraft.core.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchFlameFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchSmokeFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityOxygenFX;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirCollector;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirCompressor;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirDistributor;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirSealer;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiFuelLoader;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiGalaxyMap;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiRefinery;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiRocketRefill;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererBreathableAir;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererCraftingTable;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererCrudeOil;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererLandingPad;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererMeteor;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererOxygenPipe;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderAlienVillager;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderArrow;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderAstroOrb;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderBlockTreasureChest;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderBuggy;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderCreeper;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderFlag;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderLander;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderMeteor;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderOxygenBubble;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderParaChest;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderPlayer;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSkeleton;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSkeletonBoss;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSpider;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderWorm;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderZombie;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererBuggy;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererFlag;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererKey;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityAdvancedCraftingTableRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityLandingPadRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityRefineryRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSounds;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAstroOrb;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityControllable;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityOxygenBubble;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityWorm;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerSlowClient;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAdvancedCraftingTable;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;
import micdoodle8.mods.galacticraft.moon.client.GCMoonMapPlanet;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class ClientProxyCore extends CommonProxyCore
{
	private static int treasureChestRenderID;
	private static int torchRenderID;
	private static int breathableAirRenderID;
	private static int oxygenPipeRenderID;
	private static int meteorRenderID;
	private static int craftingTableID;
	private static int crudeOilRenderID;
	private static int fullLandingPadRenderID;
	public static long getFirstBootTime;
	public static long getCurrentTime;
	public static long slowTick;
	private final Random rand = new Random();
	public static ClientProxyMoon moon = new ClientProxyMoon();
	public static List<IPlanetSlotRenderer> slotRenderers = new ArrayList<IPlanetSlotRenderer>();
	public static List<int[]> valueableBlocks = new ArrayList<int[]>();

	public static Set<String> playersUsingParachutes = new HashSet<String>();
	public static HashMap<String, String> parachuteTextures = new HashMap<String, String>();
	public static Set<String> playersWithOxygenMask = new HashSet<String>();
	public static Set<String> playersWithOxygenGear = new HashSet<String>();
	public static Set<String> playersWithOxygenTankLeftRed = new HashSet<String>();
	public static Set<String> playersWithOxygenTankLeftOrange = new HashSet<String>();
	public static Set<String> playersWithOxygenTankLeftGreen = new HashSet<String>();
	public static Set<String> playersWithOxygenTankRightRed = new HashSet<String>();
	public static Set<String> playersWithOxygenTankRightOrange = new HashSet<String>();
	public static Set<String> playersWithOxygenTankRightGreen = new HashSet<String>();

	public static double playerPosX;
    public static double playerPosY;
    public static double playerPosZ;
    public static float playerRotationYaw;
    public static float playerRotationPitch;

    public static int clientSpaceStationID = 0;
    
    private GCCoreThreadDownloadSound downloadResourcesThread;
    
    public static ArrayList<SoundPoolEntry> newMusic = new ArrayList<SoundPoolEntry>();

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		ClientProxyCore.moon.preInit(event);

		MinecraftForge.EVENT_BUS.register(new GCCoreSounds());
		ClientProxyCore.getFirstBootTime = System.currentTimeMillis();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		ClientProxyCore.moon.init(event);

        try
        {
            this.downloadResourcesThread = new GCCoreThreadDownloadSound(FMLClientHandler.instance().getClient().getMinecraftDir(), FMLClientHandler.instance().getClient());
            this.downloadResourcesThread.start();
        }
        catch (Exception exception)
        {
            ;
        }

		TickRegistry.registerTickHandler(new GCCoreTickHandlerClient(), Side.CLIENT);
		TickRegistry.registerScheduledTickHandler(new GCCoreTickHandlerSlowClient(), Side.CLIENT);
		KeyBindingRegistry.registerKeyBinding(new GCKeyHandler());
        NetworkRegistry.instance().registerChannel(new GCCorePacketHandlerClient(), GalacticraftCore.CHANNEL, Side.CLIENT);

        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityTreasureChest.class, new GCCoreTileEntityTreasureChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityAdvancedCraftingTable.class, new GCCoreTileEntityAdvancedCraftingTableRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityRefinery.class, new GCCoreTileEntityRefineryRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityLandingPad.class, new GCCoreTileEntityLandingPadRenderer());
        ClientProxyCore.treasureChestRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreRenderBlockTreasureChest(ClientProxyCore.treasureChestRenderID));
        ClientProxyCore.torchRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererUnlitTorch(ClientProxyCore.torchRenderID));
        ClientProxyCore.breathableAirRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererBreathableAir(ClientProxyCore.breathableAirRenderID));
        ClientProxyCore.oxygenPipeRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererOxygenPipe(ClientProxyCore.oxygenPipeRenderID));
        ClientProxyCore.meteorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererMeteor(ClientProxyCore.meteorRenderID));
        ClientProxyCore.craftingTableID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererCraftingTable(ClientProxyCore.craftingTableID));
        ClientProxyCore.crudeOilRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererCrudeOil(ClientProxyCore.crudeOilRenderID));
        ClientProxyCore.fullLandingPadRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererLandingPad(ClientProxyCore.fullLandingPadRenderID));
        final IMapPlanet earth = new GCCoreMapPlanetOverworld();
        final IMapPlanet moon = new GCMoonMapPlanet();
		GalacticraftCore.addAdditionalMapPlanet(earth);
		GalacticraftCore.addAdditionalMapMoon(earth, moon);
		GalacticraftCore.addAdditionalMapPlanet(new GCCoreMapSun());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		ClientProxyCore.moon.postInit(event);

		for (final IGalacticraftSubModClient client : GalacticraftCore.clientSubMods)
		{
			if (client.getPlanetForMap() != null)
			{
				GalacticraftCore.mapPlanets.add(client.getPlanetForMap());
			}

			if (client.getChildMapPlanets() != null && client.getPlanetForMap() != null)
			{
				for (final IMapPlanet planet : client.getChildMapPlanets())
				{
					GalacticraftCore.mapMoons.put(client.getPlanetForMap(), planet);
				}
			}
		}
	}

	@Override
	public void registerRenderInformation()
	{
		ClientProxyCore.moon.registerRenderInformation();

        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityRocketT1.class, new GCCoreRenderSpaceship(new GCCoreModelSpaceship(), "/micdoodle8/mods/galacticraft/core/client/entities/spaceship1.png"));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpider.class, new GCCoreRenderSpider());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityZombie.class, new GCCoreRenderZombie());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityCreeper.class, new GCCoreRenderCreeper());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySkeleton.class, new GCCoreRenderSkeleton());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySkeletonBoss.class, new GCCoreRenderSkeletonBoss());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityMeteor.class, new GCCoreRenderMeteor());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityBuggy.class, new GCCoreRenderBuggy());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityFlag.class, new GCCoreRenderFlag());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityAstroOrb.class, new GCCoreRenderAstroOrb());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityWorm.class, new GCCoreRenderWorm());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityParaChest.class, new GCCoreRenderParaChest());
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new GCCoreRenderPlayer());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityAlienVillager.class, new GCCoreRenderAlienVillager());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityOxygenBubble.class, new GCCoreRenderOxygenBubble());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityLander.class, new GCCoreRenderLander());
        RenderingRegistry.addNewArmourRendererPrefix("oxygen");
        RenderingRegistry.addNewArmourRendererPrefix("sensor");
        RenderingRegistry.addNewArmourRendererPrefix("sensorox");
        RenderingRegistry.addNewArmourRendererPrefix("titanium");
        RenderingRegistry.addNewArmourRendererPrefix("titaniumox");
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityArrow.class, new GCCoreRenderArrow());
		MinecraftForgeClient.registerItemRenderer(GCCoreBlocks.unlitTorch.blockID, new GCCoreItemRendererUnlitTorch());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.spaceship.itemID, new GCCoreItemRendererSpaceship());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.buggy.itemID, new GCCoreItemRendererBuggy());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.flag.itemID, new GCCoreItemRendererFlag());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.key.itemID, new GCCoreItemRendererKey());
	}

	@Override
	public void addSlotRenderer(IPlanetSlotRenderer slotRenderer)
	{
		ClientProxyCore.slotRenderers.add(slotRenderer);
	}

	@Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

	@Override
	public int getGCTreasureChestRenderID()
	{
		return ClientProxyCore.treasureChestRenderID;
	}

	@Override
	public int getGCUnlitTorchRenderID()
	{
		return ClientProxyCore.torchRenderID;
	}

	@Override
	public int getGCBreathableAirRenderID()
	{
		return ClientProxyCore.breathableAirRenderID;
	}

	@Override
	public int getGCOxygenPipeRenderID()
	{
		return ClientProxyCore.oxygenPipeRenderID;
	}

	@Override
	public int getGCMeteorRenderID()
	{
		return ClientProxyCore.meteorRenderID;
	}

	@Override
	public int getGCCraftingTableRenderID()
	{
		return ClientProxyCore.craftingTableID;
	}

	@Override
	public int getGCCrudeOilRenderID()
	{
		return ClientProxyCore.crudeOilRenderID;
	}

	@Override
	public int getGCFullLandingPadRenderID()
	{
		return ClientProxyCore.fullLandingPadRenderID;
	}

//    @Override
//	public void addStat(EntityPlayer player, StatBase stat, int i)
//	{
//    	if (stat != null)
//		{
//			if (stat instanceof AdvancedAchievement)
//			{
//				final AdvancedAchievement achiev = (AdvancedAchievement) stat;
//
//				int amountOfCompletedAchievements = 0;
//
//				if (achiev.parentAchievements != null)
//				{
//					for (int j = 0; i < achiev.parentAchievements.length; j++)
//					{
//						if (FMLClientHandler.instance().getClient().statFileWriter.hasAchievementUnlocked(achiev.parentAchievements[j]))
//						{
//							amountOfCompletedAchievements++;
//						}
//					}
//
//					if (amountOfCompletedAchievements >= achiev.parentAchievements.length)
//					{
//	                    if (!FMLClientHandler.instance().getClient().statFileWriter.hasAchievementUnlocked(achiev))
//	                    {
//							FMLClientHandler.instance().getClient().guiAchievement.queueTakenAchievement(achiev);
//	                    }
//					}
//				}
//				else
//				{
//					player.addStat(stat, i);
//				}
//
//				FMLClientHandler.instance().getClient().statFileWriter.readStat(stat, i);
//			}
//			else
//			{
//				player.addStat(stat, i);
//			}
//		}
//	}

	public static boolean handleWaterMovement(EntityPlayer player)
	{
		return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.water);
	}

	public static boolean lastSpacebarDown;

    public static class GCKeyHandler extends KeyHandler
    {
//    	public static KeyBinding tankRefill = new KeyBinding("[GC] Player Inventory", Keyboard.KEY_E);
    	public static KeyBinding galaxyMap = new KeyBinding("[GC] Galaxy Map", Keyboard.KEY_M);
    	public static KeyBinding openSpaceshipInv = new KeyBinding("[GC] Open Spaceship Inventory", Keyboard.KEY_F);
    	public static KeyBinding toggleAdvGoggles = new KeyBinding("[GC] Toggle Sensor Goggles Mode", Keyboard.KEY_K);
    	public static KeyBinding accelerateKey = new KeyBinding("[GC] Vehicle Forward Key", Keyboard.KEY_W);
    	public static KeyBinding decelerateKey = new KeyBinding("[GC] Vehicle Backward Key", Keyboard.KEY_S);
    	public static KeyBinding leftKey = new KeyBinding("[GC] Vehicle Left Key", Keyboard.KEY_A);
    	public static KeyBinding rightKey = new KeyBinding("[GC] Vehicle Right Key", Keyboard.KEY_D);
    	public static KeyBinding spaceKey = new KeyBinding("[GC] Vehicle Up Key", Keyboard.KEY_SPACE);
    	public static KeyBinding leftShiftKey = new KeyBinding("[GC] Vehicle Down Key", Keyboard.KEY_LSHIFT);

        public GCKeyHandler()
        {
            super(new KeyBinding[] {/*GCKeyHandler.tankRefill, */GCKeyHandler.galaxyMap, GCKeyHandler.openSpaceshipInv, GCKeyHandler.toggleAdvGoggles, GCKeyHandler.accelerateKey, GCKeyHandler.decelerateKey, GCKeyHandler.leftKey, GCKeyHandler.rightKey, GCKeyHandler.spaceKey, GCKeyHandler.leftShiftKey}, new boolean[] {false, false, false, false, true, true, true, true, true, true});
        }

        @Override
        public String getLabel()
        {
            return "Galacticraft Keybinds";
        }

        @Override
        public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
        {
        	final Minecraft minecraft = FMLClientHandler.instance().getClient();

        	final EntityPlayerSP player = minecraft.thePlayer;

        	if (player == null)
        	{
        		return;
        	}

        	final GCCorePlayerSP playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(player);

        	if(minecraft.currentScreen != null || tickEnd)
        	{
    			return;
        	}

        	/*
        	if (kb.keyCode == GCKeyHandler.tankRefill.keyCode)
        	{
        		if (minecraft.currentScreen == null && playerBase != null && !playerBase.capabilities.isCreativeMode)
            	{
        			playerBase.setUseTutorialText(false);

                    final Object[] toSend = {player.username};
                    PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 0, toSend));
            	    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiTankRefill, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
            	}
        	}
        	else */if (kb.keyCode == GCKeyHandler.galaxyMap.keyCode)
        	{
        		if (minecraft.currentScreen == null)
        		{
        			player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiGalaxyMap, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
        		}
        	}
        	else if (kb.keyCode == GCKeyHandler.openSpaceshipInv.keyCode)
        	{
                final Object[] toSend = {player.username};
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 6, toSend));
        	    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
        	}
        	else if (kb.keyCode == GCKeyHandler.toggleAdvGoggles.keyCode)
        	{
        		if (playerBase != null)
        		{
            		playerBase.toggleGoggles();
        		}
            }

    		if(minecraft.currentScreen != null || tickEnd)
    		{
    			return;
    		}

    		int keyNum = -1;
    		boolean handled = true;

    		if(kb == GCKeyHandler.accelerateKey)
    		{
    			keyNum = 0;
    		}
    		else if(kb == GCKeyHandler.decelerateKey)
    		{
    			keyNum = 1;
    		}
    		else if(kb == GCKeyHandler.leftKey)
    		{
    			keyNum = 2;
    		}
    		else if(kb == GCKeyHandler.rightKey)
    		{
    			keyNum = 3;
    		}
    		else if(kb == GCKeyHandler.spaceKey)
    		{
    			keyNum = 4;
    		}
    		else if(kb == GCKeyHandler.leftShiftKey)
    		{
    			keyNum = 5;
    		}
    		else
    		{
    			handled = false;
    		}

    		final Entity entityTest  = player.ridingEntity;

    		if (entityTest != null && entityTest instanceof GCCoreEntityControllable && handled == true)
    		{
    			final GCCoreEntityControllable entity = (GCCoreEntityControllable)entityTest;

    			if (kb.keyCode == minecraft.gameSettings.keyBindInventory.keyCode)
    			{
    				minecraft.gameSettings.keyBindInventory.pressed = false;
    				minecraft.gameSettings.keyBindInventory.pressTime = 0;
    			}

    			handled = entity.pressKey(keyNum);
    		}
    		else
    		{
    			handled = false;
    		}


    		if (handled == true)
    		{
    			return;
    		}

    		for (final KeyBinding key : minecraft.gameSettings.keyBindings)
    		{
    			if (kb.keyCode == key.keyCode && key != kb)
    			{
    				key.pressed = true;
    				key.pressTime = 1;
    			}
    		}
        }

        @Override
        public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
        {
        	if (tickEnd)
    			return;

    		for (final KeyBinding key : FMLClientHandler.instance().getClient().gameSettings.keyBindings)
    		{
    			if (kb.keyCode == key.keyCode && key != kb)
    				key.pressed = false;
    		}
        }

        @Override
        public EnumSet<TickType> ticks()
        {
            return EnumSet.of(TickType.CLIENT);
        }
    }

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		final TileEntity tile = world.getBlockTileEntity(x, y, z);

		/*
		if (ID == GCCoreConfigManager.idGuiTankRefill)
		{
			return new GCCoreGuiTankRefill(player);
		}
		else */if (ID == GCCoreConfigManager.idGuiGalaxyMap)
		{
			return new GCCoreGuiGalaxyMap(player);
		}
		else if (ID == GCCoreConfigManager.idGuiSpaceshipInventory && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityRocketT1)
		{
			return new GCCoreGuiRocketRefill(player.inventory, (GCCoreEntityRocketT1) player.ridingEntity, ((GCCoreEntityRocketT1) player.ridingEntity).getSpaceshipType());
		}
		else if (ID == GCCoreConfigManager.idGuiRefinery)
		{
			return new GCCoreGuiRefinery(player.inventory, (GCCoreTileEntityRefinery)world.getBlockTileEntity(x, y, z));
		}
		else if (ID == GCCoreConfigManager.idGuiAirCompressor)
		{
			if (tile != null && tile instanceof GCCoreTileEntityOxygenCompressor)
			{
				return new GCCoreGuiAirCompressor(player.inventory, (GCCoreTileEntityOxygenCompressor)tile);
			}
			else
			{
				return null;
			}
		}
		else if (ID == GCCoreConfigManager.idGuiAirCollector)
		{
			if (tile != null && tile instanceof GCCoreTileEntityOxygenCollector)
			{
				return new GCCoreGuiAirCollector(player.inventory, (GCCoreTileEntityOxygenCollector)tile);
			}
			else
			{
				return null;
			}
		}
		else if (ID == GCCoreConfigManager.idGuiAirDistributor)
		{
			if (tile != null && tile instanceof GCCoreTileEntityOxygenDistributor)
			{
				return new GCCoreGuiAirDistributor(player.inventory, (GCCoreTileEntityOxygenDistributor)tile);
			}
			else
			{
				return null;
			}
		}
		else if (ID == GCCoreConfigManager.idGuiFuelLoader)
		{
			if (tile != null && tile instanceof GCCoreTileEntityFuelLoader)
			{
				return new GCCoreGuiFuelLoader(player.inventory, (GCCoreTileEntityFuelLoader)tile);
			}
			else
			{
				return null;
			}
		}
		else if (ID == GCCoreConfigManager.idGuiAirSealer)
		{
			if (tile != null && tile instanceof GCCoreTileEntityOxygenSealer)
			{
				return new GCCoreGuiAirSealer(player.inventory, (GCCoreTileEntityOxygenSealer)tile);
			}
			else
			{
				return null;
			}
		}
		else
		{
			final GCCorePlayerSP playerClient = PlayerUtil.getPlayerBaseClientFromPlayer(player);

			if (playerClient != null)
			{
				for (final ISchematicPage page : playerClient.unlockedSchematics)
				{
					if (ID == page.getGuiID())
					{
						final GuiScreen screen = page.getResultScreen(playerClient, x, y, z);

						if (screen instanceof ISchematicResultPage)
						{
							((ISchematicResultPage) screen).setPageIndex(page.getPageID());
						}

						return screen;
					}
				}
			}
		}

		return super.getClientGuiElement(ID, player, world, x, y, z);
	}
}
