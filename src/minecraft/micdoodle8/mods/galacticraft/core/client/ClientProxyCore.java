package micdoodle8.mods.galacticraft.core.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import micdoodle8.mods.galacticraft.API.AdvancedAchievement;
import micdoodle8.mods.galacticraft.API.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.API.IDetectableResource;
import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.API.ISpaceship;
import micdoodle8.mods.galacticraft.core.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchFlameFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchSmokeFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityOxygenFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityWeldingSmoke;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiChoosePlanet;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreOverlayOxygenTankIndicator;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreOverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreOverlaySpaceship;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelPlayer;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererBreathableAir;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererCraftingTable;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererCrudeOil;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererMeteor;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererOxygenCollector;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererOxygenDistributor;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererOxygenPipe;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererRefinery;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderArrow;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderAstroOrb;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderBlockTreasureChest;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderBuggy;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderCreeper;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderFlag;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderMeteor;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderParaChest;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderPlayer;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSkeleton;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSpider;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderWorm;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderZombie;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererBuggy;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererFlag;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityAdvancedCraftingTableRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityOxygenCollectorRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityOxygenDistributorRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityRefineryRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSoundUpdaterSpaceship;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSounds;
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
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSensorGlasses;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAdvancedCraftingTable;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;
import micdoodle8.mods.galacticraft.moon.client.GCMoonMapPlanet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.ModelPlayerAPI;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.stats.StatBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
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
	private static int oxygenDistributorRenderID;
	private static int oxygenCollectorRenderID;
	private static int crudeOilRenderID;
	private static int refineryRenderID;
	public static long getFirstBootTime;
	public static long getCurrentTime;
	public static long slowTick;
	private final Random rand = new Random();
	public static ClientProxyMoon moon = new ClientProxyMoon();
	public static int teleportCooldown;
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
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		ClientProxyCore.moon.preInit(event);
		
		ModelPlayerAPI.register("GalacticraftCore", GCCoreModelPlayer.class);
		RenderPlayerAPI.register("GalacticraftCore", GCCoreRenderPlayer.class);
		PlayerAPI.register("GalacticraftCore", GCCorePlayerBaseClient.class);
		
		MinecraftForge.EVENT_BUS.register(new GCCoreSounds());
		ClientProxyCore.getFirstBootTime = System.currentTimeMillis();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		ClientProxyCore.moon.init(event);
		
		TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
		TickRegistry.registerScheduledTickHandler(new TickHandlerClientSlow(), Side.CLIENT);
		KeyBindingRegistry.registerKeyBinding(new GCKeyHandler());
        NetworkRegistry.instance().registerChannel(new ClientPacketHandler(), "Galacticraft", Side.CLIENT);
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityTreasureChest.class, new GCCoreTileEntityTreasureChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityOxygenDistributor.class, new GCCoreTileEntityOxygenDistributorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityOxygenCollector.class, new GCCoreTileEntityOxygenCollectorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityAdvancedCraftingTable.class, new GCCoreTileEntityAdvancedCraftingTableRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityRefinery.class, new GCCoreTileEntityRefineryRenderer());
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
        ClientProxyCore.oxygenDistributorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererOxygenDistributor(ClientProxyCore.oxygenDistributorRenderID));
        ClientProxyCore.oxygenCollectorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererOxygenCollector(ClientProxyCore.oxygenCollectorRenderID));
        ClientProxyCore.crudeOilRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererCrudeOil(ClientProxyCore.crudeOilRenderID));
        ClientProxyCore.refineryRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererRefinery(ClientProxyCore.refineryRenderID));
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
		
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpaceship.class, new GCCoreRenderSpaceship(new GCCoreModelSpaceship(), "/micdoodle8/mods/galacticraft/core/client/entities/spaceship1.png"));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpider.class, new GCCoreRenderSpider());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityZombie.class, new GCCoreRenderZombie());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityCreeper.class, new GCCoreRenderCreeper());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySkeleton.class, new GCCoreRenderSkeleton());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityMeteor.class, new GCCoreRenderMeteor());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityBuggy.class, new GCCoreRenderBuggy());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityFlag.class, new GCCoreRenderFlag());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityAstroOrb.class, new GCCoreRenderAstroOrb());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityWorm.class, new GCCoreRenderWorm());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityParaChest.class, new GCCoreRenderParaChest());
        RenderingRegistry.addNewArmourRendererPrefix("oxygen");
        RenderingRegistry.addNewArmourRendererPrefix("sensor");
        RenderingRegistry.addNewArmourRendererPrefix("sensorox");
        RenderingRegistry.addNewArmourRendererPrefix("titanium");
        RenderingRegistry.addNewArmourRendererPrefix("titaniumox");
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityArrow.class, new GCCoreRenderArrow());
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/core/client/blocks/core.png");
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/core/client/items/core.png");
        MinecraftForgeClient.registerItemRenderer(GCCoreBlocks.unlitTorch.blockID, new GCCoreItemRendererUnlitTorch());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.spaceship.itemID, new GCCoreItemRendererSpaceship());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.buggy.itemID, new GCCoreItemRendererBuggy());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.flag.itemID, new GCCoreItemRendererFlag());
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
	public int getGCOxygenDistributorRenderID()
	{
		return ClientProxyCore.oxygenDistributorRenderID;
	}

	@Override
	public int getGCOxygenCollectorRenderID()
	{
		return ClientProxyCore.oxygenCollectorRenderID;
	}

	@Override
	public int getGCCrudeOilRenderID()
	{
		return ClientProxyCore.crudeOilRenderID;
	}

	public int getGCRefineryRenderID()
	{
		return ClientProxyCore.refineryRenderID;
	}

	@Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
	{
		this.spawnParticle(var1, var2, var4, var6, var8, var10, var12, 0.0D, 0.0D, 0.0D, b);
	}

	@Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var13, double var14, double var15, boolean b)
    {
        final Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            final double var16 = mc.renderViewEntity.posX - var2;
            final double var17 = mc.renderViewEntity.posY - var4;
            final double var19 = mc.renderViewEntity.posZ - var6;
            Object var21 = null;
            final double var22 = 64.0D;
            
            if (var1.equals("whitesmoke"))
            {
        		final EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1.0F, b);
        		if (fx != null)
        		{
                	mc.effectRenderer.addEffect(fx);
        		}
            }
            else if (var1.equals("whitesmokelarge"))
            {
        		final EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F, b);
        		if (fx != null)
        		{
        			mc.effectRenderer.addEffect(fx);
        		}
        	}
            else if (var1.equals("launchflame"))
            {
        		final EntityFX fx = new GCCoreEntityLaunchFlameFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1F);
        		if (fx != null)
        		{
        			mc.effectRenderer.addEffect(fx);
        		}
            }
            else if (var1.equals("distancesmoke") && var16 * var16 + var17 * var17 + var19 * var19 < var22 * var22 * 1.7)
            {
            	final EntityFX fx = new EntitySmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F);
        		if (fx != null)
        		{
        			mc.effectRenderer.addEffect(fx);
        		}
            }

            if (var16 * var16 + var17 * var17 + var19 * var19 < var22 * var22)
            {
            	if (var1.equals("oxygen"))
            	{
                    var21 = new GCCoreEntityOxygenFX(mc.theWorld, var2, var4, var6, var8, var10, var12);
                    ((EntityFX)var21).setRBGColorF((float)var13, (float)var14, (float)var15);
            	}
            }
            
            if (var21 != null)
            {
                ((EntityFX)var21).prevPosX = ((EntityFX)var21).posX;
                ((EntityFX)var21).prevPosY = ((EntityFX)var21).posY;
                ((EntityFX)var21).prevPosZ = ((EntityFX)var21).posZ;
                mc.effectRenderer.addEffect((EntityFX)var21);
            }
        }
    }
    
    public static Map healthMap = new HashMap();
	
    public class ClientPacketHandler implements IPacketHandler
    {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	
		@Override
		public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
		{
            final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            
            final int packetType = PacketUtil.readPacketID(data);
            
            final EntityPlayer player = (EntityPlayer)p;
            
            if (packetType == 0)
            {
                final Class[] decodeAs = {Integer.class, Integer.class, String.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                if (String.valueOf(packetReadout[2]).equals(String.valueOf(FMLClientHandler.instance().getClient().thePlayer.username)))
                {
                    TickHandlerClient.airRemaining = (Integer) packetReadout[0];
                    TickHandlerClient.airRemaining2 = (Integer) packetReadout[1];
                }
            }
            else if (packetType == 1)
            {
                final Class[] decodeAs = {Float.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
            	
                FMLClientHandler.instance().getClient().thePlayer.timeInPortal = (Float) packetReadout[0];
            }
            else if (packetType == 2)
            {
            	final Class[] decodeAs = {String.class, String.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
            	
                if (String.valueOf(packetReadout[0]).equals(FMLClientHandler.instance().getClient().thePlayer.username))
                {
                	final String[] destinations = ((String)packetReadout[1]).split("\\.");
                	
            		if (FMLClientHandler.instance().getClient().theWorld != null && !(FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet))
            		{
            			FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiChoosePlanet(FMLClientHandler.instance().getClient().thePlayer, destinations));
            		}
                }
            }
            else if (packetType == 3)
            {
            	final Class[] decodeAs = {Integer.class, Integer.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                for(int i = 0; i < player.worldObj.getLoadedEntityList().size(); i++)
                {
	                if(player.worldObj.getLoadedEntityList().get(i) instanceof EntityLiving && ((EntityLiving)player.worldObj.getLoadedEntityList().get(i)).entityId == (Integer)packetReadout[1])
	                {
	                	if (ClientProxyCore.healthMap.containsKey(packetReadout[1]))
	                	{
	                		ClientProxyCore.healthMap.remove(packetReadout[1]);
	                		ClientProxyCore.healthMap.put(packetReadout[1], packetReadout[0]);
	                	}
	                	else
	                	{
	                		ClientProxyCore.healthMap.put(packetReadout[1], packetReadout[0]);
	                	}
	                }
                }
            }
            else if (packetType == 4)
            {
            	final Class[] decodeAs = {String.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                ClientProxyCore.playersUsingParachutes.add((String) packetReadout[0]);
            }
            else if (packetType == 5)
            {
            	final Class[] decodeAs = {String.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                ClientProxyCore.playersUsingParachutes.remove(packetReadout[0]);
            }
            else if (packetType == 6)
            {
            	final Class[] decodeAs = {String.class, String.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                ClientProxyCore.parachuteTextures.put((String)packetReadout[0], (String)packetReadout[1]);
            }
            else if (packetType == 7)
            {
            	final Class[] decodeAs = {String.class, String.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                ClientProxyCore.parachuteTextures.remove(packetReadout[0]);
            }
            else if (packetType == 8)
            {
            	final Class[] decodeAs = {String.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 1;

            	player.sendChatToPlayer("SPACE - Launch");
            	player.sendChatToPlayer("A / D  - Turn left-right");
            	player.sendChatToPlayer("W / S  - Turn up-down");
            	player.sendChatToPlayer(Keyboard.getKeyName(GCKeyHandler.openSpaceshipInv.keyCode) + "       - Inventory / Fuel");
            }
            else if (packetType == 9)
            {
            	final Class[] decodeAs = {Integer.class, Integer.class, Integer.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                int x, y, z;
                x = (Integer) packetReadout[0];
                y = (Integer) packetReadout[1];
                z = (Integer) packetReadout[2];

            	for (int i = 0; i < 4; i++)
            	{
                    if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null && this.mc.theWorld != null)
                    {
                		final EntityFX fx = new GCCoreEntityWeldingSmoke(this.mc.theWorld, x - 0.15 + 0.5, y + 1.2, z + 0.15 + 0.5, this.mc.theWorld.rand.nextDouble() / 20 - this.mc.theWorld.rand.nextDouble() / 20, 0.06, this.mc.theWorld.rand.nextDouble() / 20 - this.mc.theWorld.rand.nextDouble() / 20, 1.0F);
                		if (fx != null)
                		{
                        	this.mc.effectRenderer.addEffect(fx);
                		}
                    }
            	}
            }
            else if (packetType == 10)
            {
            	final Class[] decodeAs = {String.class, Integer.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                final int type = (Integer) packetReadout[1];
                
                switch (type)
                {
                case 0:
                	ClientProxyCore.playersWithOxygenMask.add((String) packetReadout[0]);
                	break;
                case 1:
                	ClientProxyCore.playersWithOxygenMask.remove(packetReadout[0]);
                	break;
                case 2:
                	ClientProxyCore.playersWithOxygenGear.add((String) packetReadout[0]);
                	break;
                case 3:
                	ClientProxyCore.playersWithOxygenGear.remove(packetReadout[0]);
                	break;
                case 4:
                	ClientProxyCore.playersWithOxygenTankLeftRed.add((String) packetReadout[0]);
                	break;
                case 5:
                	ClientProxyCore.playersWithOxygenTankLeftRed.remove(packetReadout[0]);
                	break;
                case 6:
                	ClientProxyCore.playersWithOxygenTankLeftOrange.add((String) packetReadout[0]);
                	break;
                case 7:
                	ClientProxyCore.playersWithOxygenTankLeftOrange.remove(packetReadout[0]);
                	break;
                case 8:
                	ClientProxyCore.playersWithOxygenTankLeftGreen.add((String) packetReadout[0]);
                	break;
                case 9:
                	ClientProxyCore.playersWithOxygenTankLeftGreen.remove(packetReadout[0]);
                	break;
                case 10:
                	ClientProxyCore.playersWithOxygenTankRightRed.add((String) packetReadout[0]);
                	break;
                case 11:
                	ClientProxyCore.playersWithOxygenTankRightRed.remove(packetReadout[0]);
                	break;
                case 12:
                	ClientProxyCore.playersWithOxygenTankRightOrange.add((String) packetReadout[0]);
                	break;
                case 13:
                	ClientProxyCore.playersWithOxygenTankRightOrange.remove(packetReadout[0]);
                	break;
                case 14:
                	ClientProxyCore.playersWithOxygenTankRightGreen.add((String) packetReadout[0]);
                	break;
                case 15:
                	ClientProxyCore.playersWithOxygenTankRightGreen.remove(packetReadout[0]);
                	break;
                }
            }
            else if (packetType == 11)
            {
                final Class[] decodeAs = {String.class};
                final Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);
                
                this.mc.thePlayer.playerCloakUrl = (String) packetReadout[0];
            }
		}
    }
	
    @Override
	public void addStat(EntityPlayer player, StatBase stat, int i)
	{
    	if (stat != null)
		{
			if (stat instanceof AdvancedAchievement)
			{
				final AdvancedAchievement achiev = (AdvancedAchievement) stat;
				
				int amountOfCompletedAchievements = 0;
				
				if (achiev.parentAchievements != null)
				{
					for (int j = 0; i < achiev.parentAchievements.length; j++)
					{
						if (FMLClientHandler.instance().getClient().statFileWriter.hasAchievementUnlocked(achiev.parentAchievements[j]))
						{
							amountOfCompletedAchievements++;
						}
					}
					
					if (amountOfCompletedAchievements >= achiev.parentAchievements.length)
					{
	                    if (!FMLClientHandler.instance().getClient().statFileWriter.hasAchievementUnlocked(achiev))
	                    {
							FMLClientHandler.instance().getClient().guiAchievement.queueTakenAchievement(achiev);
	                    }
					}
				}
				else
				{
					player.addStat(stat, i);
				}

				FMLClientHandler.instance().getClient().statFileWriter.readStat(stat, i);
			}
			else
			{
				player.addStat(stat, i);
			}
		}
	}
	
	public static boolean handleWaterMovement(EntityPlayer player)
	{
		return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.water);
	}
	
	private static boolean lastSpacebarDown;
	
	public static class TickHandlerClientSlow implements IScheduledTickHandler
	{
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData)
		{
			final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
			
			if (player != null)
			{
				ClientProxyCore.valueableBlocks.clear();
				
				for (int i = -4; i < 5; i++)
				{
					for (int j = -4; j < 5; j++)
					{
						for (int k = -4; k < 5; k++)
						{
							int x, y, z;
							
							x = MathHelper.floor_double(player.posX + i);
							y = MathHelper.floor_double(player.posY + j);
							z = MathHelper.floor_double(player.posZ + k);
							
							final int id = player.worldObj.getBlockId(x, y, z);
							
							if (id != 0)
							{
								final Block block = Block.blocksList[id];
								
								if (block != null && (block instanceof BlockOre || block instanceof IDetectableResource))
								{
									final int[] blockPos = {x, y, z};
									
									if (!this.alreadyContainsBlock(x, y, z))
									{
										ClientProxyCore.valueableBlocks.add(blockPos);
									}
								}
							}
						}
					}
				}
			}
		}
		
		private boolean alreadyContainsBlock(int x1, int y1, int z1)
		{
			for (final int[] coordArray : ClientProxyCore.valueableBlocks)
			{
				final int x = coordArray[0];
				final int y = coordArray[1];
				final int z = coordArray[2];
				
				if (x1 == x && y1 == y && z1 == z)
				{
					return true;
				}
			}
			
			return false;
		}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData){}

    	@Override
    	public EnumSet<TickType> ticks()
    	{
    		return EnumSet.of(TickType.CLIENT);
    	}

		@Override
		public String getLabel()
		{
			return "GalacticraftSlowClient";
		}

		@Override
		public int nextTickSpacing()
		{
			return 20;
		}
		
	}
    
    public static class TickHandlerClient implements ITickHandler
    {
    	public static int airRemaining;
    	public static int airRemaining2;
        public static boolean checkedVersion = true;
    	
    	@Override
    	public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
    		ClientProxyCore.getCurrentTime = System.currentTimeMillis();
    		
    		final Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            final WorldClient world = minecraft.theWorld;
            
            final EntityClientPlayerMP player = minecraft.thePlayer;
    		
    		if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
    	    	if (world != null && checkedVersion)
    	    	{
    	    		checkVersion();
    	    		checkedVersion = false;
    	    	}
    	    	
    			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof ISpaceship)
    			{
    				this.zoom(15.0F);
    				final Object[] toSend = {player.ridingEntity.rotationPitch};
    	            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 8, toSend));
    				final Object[] toSend2 = {player.ridingEntity.rotationYaw};
    	            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 7, toSend2));
    			}
    			else
    			{
    				this.zoom(4.0F);
    			}
    			
    			if (world != null && world.provider instanceof WorldProviderSurface)
    			{
    				if (world.provider.getSkyRenderer() == null && player.ridingEntity != null && player.ridingEntity.posY >= 200)
                    {
    					world.provider.setSkyRenderer(new GCCoreSkyProviderOverworld());
                    }
    				else if (world.provider.getSkyRenderer() != null && world.provider.getSkyRenderer() instanceof GCCoreSkyProviderOverworld && (player.ridingEntity == null || player.ridingEntity.posY < 200))
    				{
    					world.provider.setSkyRenderer(null);
    				}
    			}
    			
    			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityControllable)
    			{
    				final GCCoreEntityControllable entityControllable = (GCCoreEntityControllable) player.ridingEntity;
    				
    				if (minecraft.gameSettings.keyBindLeft.pressed)
    				{
    					entityControllable.keyPressed(2, player);
    					final Object[] toSend = {2};
    					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 9, toSend));
    				}
    				
    				if (minecraft.gameSettings.keyBindRight.pressed)
    				{
    					entityControllable.keyPressed(3, player);
    					final Object[] toSend = {3};
    					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 9, toSend));
    				}

    				if (minecraft.gameSettings.keyBindForward.pressed)
    				{
    					entityControllable.keyPressed(0, player);
    					final Object[] toSend = {0};
    					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 9, toSend));
    				}
    				
    				if (minecraft.gameSettings.keyBindBack.pressed)
    				{
    					entityControllable.keyPressed(1, player);
    					final Object[] toSend = {1};
    					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 9, toSend));
    				}
    			}
    			
    			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntitySpaceship)
    			{
    				final GCCoreEntitySpaceship ship = (GCCoreEntitySpaceship) player.ridingEntity;
    				
    				if (minecraft.gameSettings.keyBindLeft.pressed)
    				{
        	            ship.turnYaw(-1.0F);
        				final Object[] toSend = {ship.rotationYaw};
        	            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 7, toSend));
    				}
    				
    				if (minecraft.gameSettings.keyBindRight.pressed)
    				{
        	            ship.turnYaw(1.0F);
        				final Object[] toSend = {ship.rotationYaw};
        	            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 7, toSend));
    				}

    				if (minecraft.gameSettings.keyBindForward.pressed)
    				{
    					if (ship.getLaunched() == 1)
    					{
            	            ship.turnPitch(-0.7F);
            				final Object[] toSend = {ship.rotationPitch};
            	            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 8, toSend));
    					}
    				}
    				
    				if (minecraft.gameSettings.keyBindBack.pressed)
    				{
    					if (ship.getLaunched() == 1)
    					{
            	            ship.turnPitch(0.7F);
            				final Object[] toSend = {ship.rotationPitch};
            	            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 8, toSend));
    					}
    				}
    			}

	        	if (world != null)
	        	{
	    	        for (int i = 0; i < world.loadedEntityList.size(); i++)
	    	        {
    	        		final Entity e = (Entity) world.loadedEntityList.get(i);
    	        		
    	        		if (e != null)
    	        		{
    	        			if (e instanceof GCCoreEntitySpaceship)
    	        			{
    	        				final GCCoreEntitySpaceship eship = (GCCoreEntitySpaceship) e;
    	        				
    	        				if (eship.rocketSoundUpdater == null)
    	        				{
    	        					eship.rocketSoundUpdater = new GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager, eship, FMLClientHandler.instance().getClient().thePlayer);
    	        				}
    	        			}
    	        		}
	    	        }
	        	}
    			
    			if (ClientProxyCore.teleportCooldown > 0)
    			{
    				ClientProxyCore.teleportCooldown--;
    			}
    			
    			if (FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet)
    			{
    				player.motionY = 0;
    			}
    			
    			if (world != null && world.provider instanceof IGalacticraftWorldProvider)
    			{
    				world.setRainStrength(0.0F);
    			}

    			if (!minecraft.gameSettings.keyBindJump.pressed)
    			{
    				ClientProxyCore.lastSpacebarDown = false;
    			}
    			
    			if (player != null && player.ridingEntity != null && minecraft.gameSettings.keyBindJump.pressed && !ClientProxyCore.lastSpacebarDown)
    			{
    				final Object[] toSend = {0};
    	            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 3, toSend));
    	            ClientProxyCore.lastSpacebarDown = true;
    			}
    			
            	if (Keyboard.isKeyDown(Keyboard.KEY_W))
            	{
            		if (minecraft.currentScreen == null)
                	{
                    	final EntityPlayerSP player2 = minecraft.thePlayer;
                    	
                        final Object[] toSend = {player.username, 0};
                        PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 5, toSend));
                	}
            	}
            	if (Keyboard.isKeyDown(Keyboard.KEY_A))
            	{
            		if (minecraft.currentScreen == null)
                	{
                    	final EntityPlayerSP player2 = minecraft.thePlayer;
                    	
                        final Object[] toSend = {player.username, 1};
                        PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 5, toSend));
                	}
            	}
            	if (Keyboard.isKeyDown(Keyboard.KEY_D))
            	{
            		if (minecraft.currentScreen == null)
                	{
                    	final EntityPlayerSP player2 = minecraft.thePlayer;
                    	
                        final Object[] toSend = {player.username, 2};
                        PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 5, toSend));
                	}
            	}
            }
        }

        public static int remoteMajVer;
        public static int remoteMinVer;
        public static int remoteBuildVer;
        public static int localMajVer = 0;
        public static int localMinVer = 0;
        public static int localBuildVer = 12;
    	
    	private static void checkVersion()
        {
        	try
        	{
        		URL url = new URL("http://micdoodle8.com/galacticraft/version.html");

        		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        		Pattern pat = Pattern.compile("Version=");
        		Matcher matcher;
        		String str;
        		String str2[] = null;

        		while ((str = in.readLine()) != null)
        		{
    	    		if (str.contains("Version"))
    	    		{
    	        		str = str.replace("Version=", "");
    	        		
			    		str2 = str.split("#");
    		    		
    		    		if (str2 != null && str2.length == 3)
    		    		{
    		    			remoteMajVer = Integer.parseInt(str2[0]);
    		    			remoteMinVer = Integer.parseInt(str2[1]);
    		    			remoteBuildVer = Integer.parseInt(str2[2]);
    		    		}

    		    		if ((remoteMajVer > localMajVer) || (remoteMajVer == localMajVer && remoteMinVer > localMinVer) || (remoteMajVer == localMajVer && remoteMinVer == localMinVer && remoteBuildVer > localBuildVer))
    		    		{
    		    			FMLClientHandler.instance().getClient().thePlayer.addChatMessage("\u00a77New \u00a73Galacticraft \u00a77version available! v" + String.valueOf(remoteMajVer) + "." + String.valueOf(remoteMinVer) + "." + String.valueOf(remoteBuildVer) + " \u00a71http://micdoodle8.com/");
    		    		}
    	    		}
        		}
        	}
        	catch (MalformedURLException e)
        	{
        		e.printStackTrace();
        		FMLClientHandler.instance().getClient().thePlayer.addChatMessage("[Galacticraft] Update Check Failed!");
        		FMLLog.info("Galacticraft Update Check Failure - MalformedURLException");
        	}
        	catch (IOException e)
        	{
        		e.printStackTrace();
        		FMLClientHandler.instance().getClient().thePlayer.addChatMessage("[Galacticraft] Update Check Failed!");
        		FMLLog.info("Galacticraft Update Check Failure - IOException");
        	}
        	catch (NumberFormatException e)
        	{
        		e.printStackTrace();
        		FMLClientHandler.instance().getClient().thePlayer.addChatMessage("[Galacticraft] Update Check Failed!");
        		FMLLog.info("Galacticraft Update Check Failure - NumberFormatException");
        	}
        }

    	public static void zoom(float value)
    	{
			try
			{
		        ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, FMLClientHandler.instance().getClient().entityRenderer, value, 13);
		        ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, FMLClientHandler.instance().getClient().entityRenderer, value, 14);
			}
			catch (Exception ex)
			{
		        ex.printStackTrace();
			}
    	}

    	@Override
    	public void tickEnd(EnumSet<TickType> type, Object... tickData)
    	{
    		final Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            final WorldClient world = minecraft.theWorld;
            
            final EntityPlayerSP player = minecraft.thePlayer;
            
            ItemStack helmetSlot = null;
    		
    		if (player != null && player.inventory.armorItemInSlot(3) != null)
    		{
    			helmetSlot = player.inventory.armorItemInSlot(3);
    		}
            
    		if (type.equals(EnumSet.of(TickType.RENDER)))
            {
        		final float partialTickTime = (Float) tickData[0];
        		
    			if (player != null)
    			{
        			ClientProxyCore.playerPosX = player.prevPosX + (player.posX - player.prevPosX) * partialTickTime;
        			ClientProxyCore.playerPosY = player.prevPosY + (player.posY - player.prevPosY) * partialTickTime;
        			ClientProxyCore.playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTickTime;
        			ClientProxyCore.playerRotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTickTime;
        			ClientProxyCore.playerRotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTickTime;
    			}
    			
    			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntitySpaceship)
        		{
    				float f = (((GCCoreEntitySpaceship)player.ridingEntity).getTimeSinceLaunch() - 250F) / 175F;
    				
    				if (f < 0)
    				{
    					f = 0F;
    				}
    				
    				if (f > 1)
    				{
    					f = 1F;
    				}
    				
					final ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
			        final int i = scaledresolution.getScaledWidth();
			        final int k = scaledresolution.getScaledHeight();
			        minecraft.entityRenderer.setupOverlayRendering();
			        GL11.glEnable(GL11.GL_BLEND);
			        GL11.glDisable(GL11.GL_DEPTH_TEST);
			        GL11.glDepthMask(false);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					GL11.glDepthMask(true);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        		}
    			
        		if (helmetSlot != null && helmetSlot.getItem() instanceof GCCoreItemSensorGlasses && minecraft.currentScreen == null)
        		{
        			GCCoreOverlaySensorGlasses.renderSensorGlassesMain();

			        
        		}
        		
        		if (player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase)
        		{
        			GCCoreOverlaySpaceship.renderSpaceshipOverlay();
        		}

        		if (player != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && OxygenUtil.shouldDisplayTankGui(minecraft.currentScreen))
    			{
    				int var6 = (TickHandlerClient.airRemaining - 90) * -1;
    				
    				if (TickHandlerClient.airRemaining <= 0)
    				{
    					var6 = 90;
    				}
    				
    				int var7 = (TickHandlerClient.airRemaining2 - 90) * -1;
    				
    				if (TickHandlerClient.airRemaining2 <= 0)
    				{
    					var7 = 90;
    				}
    				
    				if (GCCoreConfigManager.oxygenIndicatorLeftSide)
    				{
        				GCCoreOverlayOxygenTankIndicator.renderOxygenTankIndicatorLeft(var6, var7);
    				}
    				else
    				{
        				GCCoreOverlayOxygenTankIndicator.renderOxygenTankIndicatorRight(var6, var7);
    				}
    			}
            }
    	}
    	
        @Override
		public String getLabel()
        {
            return "Galacticraft Client";
        }

    	@Override
    	public EnumSet<TickType> ticks()
    	{
    		return EnumSet.of(TickType.RENDER, TickType.CLIENT);
    	}
    }
    
    public static class GCKeyHandler extends KeyHandler
    {
    	public static KeyBinding tankRefill = new KeyBinding("Galacticraft Player Inventory", Keyboard.KEY_R);
    	public static KeyBinding galaxyMap = new KeyBinding("Galaxy Map", Keyboard.KEY_M);
    	public static KeyBinding openSpaceshipInv = new KeyBinding("Open Spaceship Inventory", Keyboard.KEY_F);
    	public static KeyBinding toggleAdvGoggles = new KeyBinding("Toggle Advanced Sensor Goggles", Keyboard.KEY_K);

        public GCKeyHandler()
        {
            super(new KeyBinding[] {GCKeyHandler.tankRefill, GCKeyHandler.galaxyMap, GCKeyHandler.openSpaceshipInv, GCKeyHandler.toggleAdvGoggles}, new boolean[] {false, false, false, false});
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
        	
        	final GCCorePlayerBaseClient playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(player);
        	
        	final boolean handled = true;
        	
        	if(minecraft.currentScreen != null || tickEnd)
        	{
    			return;
        	}
        	
        	if (kb.keyCode == GCKeyHandler.tankRefill.keyCode)
        	{
        		if (minecraft.currentScreen == null && playerBase != null)
            	{
        			playerBase.setUseTutorialText(false);
                	
                    final Object[] toSend = {player.username};
                    PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 0, toSend));
            	    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiTankRefill, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
            	}
        	}
        	else if (kb.keyCode == GCKeyHandler.galaxyMap.keyCode)
        	{
        		if (minecraft.currentScreen == null)
        		{
        			player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiGalaxyMap, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
        		}
        	}
        	else if (kb.keyCode == GCKeyHandler.openSpaceshipInv.keyCode)
        	{
                final Object[] toSend = {player.username};
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 6, toSend));
        	    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
        	}
        	else if (kb.keyCode == GCKeyHandler.toggleAdvGoggles.keyCode)
        	{
        		if (playerBase != null)
        		{
            		playerBase.toggleGoggles();
        		}
            }
        	
//        	int key = -1;
//
//        	switch (kb.keyCode)
//        	{
//        	case Keyboard.KEY_W:
//        		key = 2;
//        	case Keyboard.KEY_A:
//        		key = 0;
//        	case Keyboard.KEY_D:
//        		key = 1;
//        	default:
//        		handled = false;
//        	}
//
//        	EntityPlayer player = minecraft.thePlayer;
//    		Entity entityTest  = player.ridingEntity;
//
//    		if (entityTest != null && entityTest instanceof GCCoreEntityBuggy && handled == true)
//    		{
//    			GCCoreEntityBuggy entity = (GCCoreEntityBuggy)entityTest;
//    			if (kb.keyCode == minecraft.gameSettings.keyBindInventory.keyCode)
//    			{
//    				minecraft.gameSettings.keyBindInventory.pressed = false;
//    				minecraft.gameSettings.keyBindInventory.pressTime = 0;
//    			}
//    			entity.onKeyPressed(key);
//    			handled = true;
//
//    			if (handled)
//    			{
//                    Object[] toSend = {player.username, 2};
//                    PacketDispatcher.sendPacketToServer(PacketUtil.createPacket("Galacticraft", 5, toSend));
//    			}
//    		}
//    		else
//    			handled = false;
//
//
//    		if (handled == true)
//    			return;
//
//    		for (KeyBinding keybind : minecraft.gameSettings.keyBindings)
//    		{
//    			if (kb.keyCode == keybind.keyCode && keybind != kb)
//    			{
//    				keybind.pressed = true;
//    				keybind.pressTime = 1;
//    				break;
//    			}
//    		}
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
}
