package micdoodle8.mods.galacticraft.core.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.AdvancedAchievement;
import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockAdvancedCraftingTable;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchFlameFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchSmokeFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityOxygenFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityWeldingSmoke;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiChoosePlanet;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelPlayer;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererBreathableAir;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererCraftingTable;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererMeteor;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererOxygenPipe;
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
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSoundUpdaterSpaceship;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSounds;
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
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSensorGlasses;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;
import micdoodle8.mods.galacticraft.moon.client.GCMoonMapPlanet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.ITickHandler;
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
	public static long getFirstBootTime;
	public static long getCurrentTime;
	public static long slowTick;
	private final Random rand = new Random();
	public static ClientProxyMoon moon = new ClientProxyMoon();
	public static int teleportCooldown;
	public static List<IPlanetSlotRenderer> slotRenderers = new ArrayList<IPlanetSlotRenderer>();
	public static List<int[]> valueableBlocks = new ArrayList<int[]>();
	
	public static ArrayList<String> playersUsingParachutes = new ArrayList<String>();
	public static HashMap<String, String> parachuteTextures = new HashMap<String, String>();
	
    private static double playerPosX;
    private static double playerPosY;
    private static double playerPosZ;
    private static float playerRotationYaw;
    private static float playerRotationPitch;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		moon.preInit(event);
		
		ModelPlayerAPI.register("GalacticraftCore", GCCoreModelPlayer.class);
		RenderPlayerAPI.register("GalacticraftCore", GCCoreRenderPlayer.class);
		PlayerAPI.register("GalacticraftCore", GCCorePlayerBaseClient.class);
		
		MinecraftForge.EVENT_BUS.register(new GCCoreSounds());
		getFirstBootTime = System.currentTimeMillis();
	}

	@Override
	public void init(FMLInitializationEvent event) 
	{
		moon.init(event);
		
		TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
		TickRegistry.registerScheduledTickHandler(new TickHandlerClientSlow(), Side.CLIENT);
		KeyBindingRegistry.registerKeyBinding(new GCKeyHandler());
        NetworkRegistry.instance().registerChannel(new ClientPacketHandler(), "Galacticraft", Side.CLIENT);
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityTreasureChest.class, new GCCoreTileEntityTreasureChestRenderer());
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
        IMapPlanet earth = new GCCoreMapPlanetOverworld();
        IMapPlanet moon = new GCMoonMapPlanet();
		GalacticraftCore.addAdditionalMapPlanet(earth);
		GalacticraftCore.addAdditionalMapMoon(String.valueOf(earth) + "0", moon);
		GalacticraftCore.addAdditionalMapPlanet(new GCCoreMapSun());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
		moon.postInit(event);
		
		for (final IGalacticraftSubModClient client : GalacticraftCore.clientSubMods)
		{
			if (client.getPlanetForMap() != null)
			{
				GalacticraftCore.mapPlanets.add(client.getPlanetForMap());
			}
			
			if (client.getChildMapPlanets() != null && client.getPlanetForMap() != null)
			{
				for (int i = 0; i < client.getChildMapPlanets().length; i++)
				{
					IMapPlanet planet = client.getChildMapPlanets()[i];
					GalacticraftCore.mapMoons.put(String.valueOf(client.getPlanetForMap()) + GalacticraftCore.mapMoons.size(), planet);
				}
			}
		}
	}
	
	@Override
	public void registerRenderInformation() 
	{
		moon.registerRenderInformation();
		
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpaceship.class, new GCCoreRenderSpaceship());
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
            final int packetType = GCCoreUtil.readPacketID(data);
            final EntityPlayer player = (EntityPlayer)p;
            
            if (packetType == 0)
            {
                final Class[] decodeAs = {Integer.class, Integer.class, String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                if (String.valueOf(packetReadout[2]).equals(String.valueOf(FMLClientHandler.instance().getClient().thePlayer.username)))
                {
                    TickHandlerClient.airRemaining = (Integer) packetReadout[0];
                    TickHandlerClient.airRemaining2 = (Integer) packetReadout[1];
                }
            }
            else if (packetType == 1)
            {
                final Class[] decodeAs = {Float.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
            	
                FMLClientHandler.instance().getClient().thePlayer.timeInPortal = (Float) packetReadout[0];
            }
            else if (packetType == 2)
            {
            	final Class[] decodeAs = {String.class, String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
            	
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
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                for(int i = 0; i < player.worldObj.getLoadedEntityList().size(); i++)
                {
	                if(player.worldObj.getLoadedEntityList().get(i) instanceof EntityLiving && ((EntityLiving)player.worldObj.getLoadedEntityList().get(i)).entityId == (Integer)packetReadout[1])
	                {
	                	if (healthMap.containsKey(packetReadout[1]))
	                	{
	                		healthMap.remove(packetReadout[1]);
	                		healthMap.put(packetReadout[1], packetReadout[0]);
	                	}
	                	else
	                	{
	                		healthMap.put(packetReadout[1], packetReadout[0]);
	                	}
	                }
                }
            }
            else if (packetType == 4)
            {
            	final Class[] decodeAs = {String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                playersUsingParachutes.add((String) packetReadout[0]);
            }
            else if (packetType == 5)
            {
            	final Class[] decodeAs = {String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                playersUsingParachutes.remove((String) packetReadout[0]);
            }
            else if (packetType == 6)
            {
            	final Class[] decodeAs = {String.class, String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                parachuteTextures.put((String)packetReadout[0], (String)packetReadout[1]);
            }
            else if (packetType == 7)
            {
            	final Class[] decodeAs = {String.class, String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                parachuteTextures.remove((String) packetReadout[0]);
            }
            else if (packetType == 8)
            {
            	final Class[] decodeAs = {String.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);

            	player.sendChatToPlayer("SPACE - Launch");
            	player.sendChatToPlayer("A / D  - Turn left-right");
            	player.sendChatToPlayer("W / S  - Turn up-down");
            	player.sendChatToPlayer(Keyboard.getKeyName(GCKeyHandler.openSpaceshipInv.keyCode) + "       - Inventory / Fuel");
            }
            else if (packetType == 9)
            {
            	final Class[] decodeAs = {Integer.class, Integer.class, Integer.class};
                final Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                int x, y, z;
                x = (Integer) packetReadout[0];
                y = (Integer) packetReadout[1];
                z = (Integer) packetReadout[2];

            	for (int i = 0; i < 4; i++)
            	{
                    if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null && mc.theWorld != null)
                    {
                		final EntityFX fx = new GCCoreEntityWeldingSmoke(mc.theWorld, x - 0.15 + 0.5, y + 1.2, z + 0.15 + 0.5, (mc.theWorld.rand.nextDouble() / 20) - (mc.theWorld.rand.nextDouble() / 20), 0.06, (mc.theWorld.rand.nextDouble() / 20) - (mc.theWorld.rand.nextDouble() / 20), 1.0F);
                		if (fx != null)
                		{
                        	mc.effectRenderer.addEffect(fx);
                		}
                    }
            	}
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
			EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
			
			if (player != null)
			{
				for (int i = -5; i < 6; i++)
				{
					for (int j = -5; j < 6; j++)
					{
						for (int k = -5; k < 6; k++)
						{
							int x, y, z;
							
							x = MathHelper.floor_double(player.posX + i);
							y = MathHelper.floor_double(player.posY + j);
							z = MathHelper.floor_double(player.posZ + k);
							
							int id = player.worldObj.getBlockId(x, y, z);
							
							if (id != 0)
							{
								Block block = Block.blocksList[id];
								
								if (block != null && block instanceof BlockOre)
								{
									int[] blockPos = {x, y, z};
									
									if (!alreadyContainsBlock(x, y, z))
									{
										ClientProxyCore.valueableBlocks.add(blockPos);
										FMLLog.info("" + ClientProxyCore.valueableBlocks.size());
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
			for (int[] coordArray : ClientProxyCore.valueableBlocks)
			{
				int x = coordArray[0];
				int y = coordArray[1];
				int z = coordArray[2];
				
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
    	
    	@Override
    	public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
    		ClientProxyCore.getCurrentTime = System.currentTimeMillis();
    		
    		final Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            final WorldClient world = minecraft.theWorld;
            
            final EntityClientPlayerMP player = minecraft.thePlayer;
    		
    		if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
//    	        for (int j = 0; j < GalacticraftCore.gcPlayers.size(); ++j)
//    	        {
//    				final GCCoreEntityPlayer playerBase = (GCCoreEntityPlayer) GalacticraftCore.gcPlayers.get(j);
//    				
//    				if (playerBase != null && player != null && player.username.equals(playerBase.getPlayer().username))
//    				{
//    					if (playerBase.getPlayer() != null && playerBase.getPlayer().getDataWatcher() != null && playerBase.getPlayer().getDataWatcher().getWatchableObjectInt(23) == 1)
//    					{
//    	    				player.motionY = -0.3;
//    	    				player.motionX *= 0.1;
//    	    				player.motionZ *= 0.1;
//    	    				
//    	    				if (player.onGround)
//    	    				{
//    	    					playerBase.getPlayer().getDataWatcher().updateObject(23, Integer.valueOf(0));
//    	    					minecraft.gameSettings.thirdPersonView = 0;
//    	    				}
//    					}
//    				}
//    	        }

	        	if (world != null)
	        	{
	    	        for (int i = 0; i < world.loadedEntityList.size(); i++)
	    	        {
    	        		Entity e = (Entity) world.loadedEntityList.get(i);
    	        		
    	        		if (e != null)
    	        		{
    	        			if (e instanceof GCCoreEntitySpaceship)
    	        			{
    	        				GCCoreEntitySpaceship eship = (GCCoreEntitySpaceship) e;
    	        				
    	        				if (eship.rocketSoundUpdater == null)
    	        				{
    	        					eship.rocketSoundUpdater = new GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager, eship, FMLClientHandler.instance().getClient().thePlayer);
    	        				}
    	        			}
    	        		}
	    	        }
	        	}
    			
    			if (teleportCooldown > 0)
    			{
    				teleportCooldown--;
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
    				lastSpacebarDown = false;
    			}
    			
    			if (player != null && player.ridingEntity != null && minecraft.gameSettings.keyBindJump.pressed && !lastSpacebarDown)
    			{
    				final Object[] toSend = {0};
    	            PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 3, toSend));
    	            lastSpacebarDown = true;
    			}
    			
            	if (Keyboard.isKeyDown(Keyboard.KEY_W))
            	{
            		if (minecraft.currentScreen == null)
                	{
                    	final EntityPlayerSP player2 = minecraft.thePlayer;
                    	
                        final Object[] toSend = {player.username, 0};
                        PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 5, toSend));
                	}
            	}
            	if (Keyboard.isKeyDown(Keyboard.KEY_A))
            	{
            		if (minecraft.currentScreen == null)
                	{
                    	final EntityPlayerSP player2 = minecraft.thePlayer;
                    	
                        final Object[] toSend = {player.username, 1};
                        PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 5, toSend));
                	}
            	}
            	if (Keyboard.isKeyDown(Keyboard.KEY_D))
            	{
            		if (minecraft.currentScreen == null)
                	{
                    	final EntityPlayerSP player2 = minecraft.thePlayer;
                    	
                        final Object[] toSend = {player.username, 2};
                        PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 5, toSend));
                	}
            	}
            }
        }
    	
		int i = 0;

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
        		float partialTickTime = (Float) tickData[0];
        		
    			if (player != null)
    			{
        			ClientProxyCore.playerPosX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTickTime;
        			ClientProxyCore.playerPosY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTickTime;
        			ClientProxyCore.playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTickTime;
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
        			this.i++;
        			
        	        final float f = MathHelper.sin(this.i / 80.0F) * 0.1F + 0.1F;
        			
					final ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
			        final int i = scaledresolution.getScaledWidth();
			        final int k = scaledresolution.getScaledHeight();
			        minecraft.entityRenderer.setupOverlayRendering();
			        GL11.glEnable(GL11.GL_BLEND);
			        GL11.glDisable(GL11.GL_DEPTH_TEST);
			        GL11.glDepthMask(false);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
    				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/hud.png"));
					final Tessellator tessellator = Tessellator.instance;
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(i / 2 - 2 * k - f * 80, k + f * 40, -90D, 0.0D, 1.0D);
					tessellator.addVertexWithUV(i / 2 + 2 * k + f * 80, k + f * 40, -90D, 1.0D, 1.0D);
					tessellator.addVertexWithUV(i / 2 + 2 * k + f * 80, 0.0D - f * 40, -90D, 1.0D, 0.0D);
					tessellator.addVertexWithUV(i / 2 - 2 * k - f * 80, 0.0D - f * 40, -90D, 0.0D, 0.0D);
					tessellator.draw();
					GL11.glDepthMask(true);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			        Iterator var51 = ClientProxyCore.valueableBlocks.iterator();
			        double var52;
			        double var58;
			        double var59;
			        double var20;
			        double var21;
			        float var60;

		            while (var51.hasNext())
		            {
		                int[] coords = (int[]) var51.next();
		                
		                int x = coords[0];
		                int y = coords[1];
		                int z = coords[2];

	                    var52 = ClientProxyCore.playerPosX - (double)x - 0.5D;
	                    var58 = ClientProxyCore.playerPosY - (double)y - 0.5D;
	                    var59 = ClientProxyCore.playerPosZ - (double)z - 0.5D;
	                    var60 = (float)Math.toDegrees(Math.atan2(var52, var59));
	                    var20 = Math.sqrt(var52 * var52 + var58 * var58 + var59 * var59) * 0.5D;
	                    var21 = Math.sqrt(var52 * var52 + var59 * var59) * 0.5D;

	                    ScaledResolution var5 = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
	                    int var6 = var5.getScaledWidth();
	                    int var7 = var5.getScaledHeight();
	                    
	                    boolean var2 = false;

	        	        for (int j = 0; j < GalacticraftCore.gcPlayers.size(); ++j)
	        	        {
	        				final GCCorePlayerBaseClient playerBase = (GCCorePlayerBaseClient) GalacticraftCore.players.get(j);
	        				
	        				if (playerBase != null && player != null && player.username.equals(playerBase.getPlayer().username))
	        				{
	        					var2 = playerBase.getUsingGoggles();
	        				}
	        	        }

                    	minecraft.fontRenderer.drawString("Advanced Mode: " + (var2 ? "ON" : "OFF"), var6 / 2 - 50, 4, 0x03b88f);

	                    try
	                    {
	                        GL11.glPushMatrix();

	                        if (var20 < 4.0D)
	                        {
	                            GL11.glColor4f(0.0F, 255F / 255F, 198F / 255F, (float)Math.min(1.0D, Math.max(0.2D, (var20 - 1.0D) * 0.1D)));
	            				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/indicator.png"));
	                            GL11.glRotatef(-var60 - ClientProxyCore.playerRotationYaw + 180.0F, 0.0F, 0.0F, 1.0F);
	                            GL11.glTranslated(0.0D, var2 ? -var20 * 16 : -var21 * 16, 0.0D);
	                            GL11.glRotatef(-(-var60 - ClientProxyCore.playerRotationYaw + 180.0F), 0.0F, 0.0F, 1.0F);
	                            this.drawCenteringRectangle(var6 / 2, var7 / 2, 1.0D, 8.0D, 8.0D);
	                        }
	                    }
	                    finally
	                    {
	                        GL11.glPopMatrix();
	                    }
		            }
        		}

        		if (player != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && GCCoreUtil.shouldDisplayTankGui(minecraft.currentScreen))
    			{
    				int var6 = (airRemaining - 90) * -1;
    				if (airRemaining <= 0) 
    				{
    					var6 = 90;
    				}
    				
    				int var7 = (airRemaining2 - 90) * -1;
    				if (airRemaining2 <= 0) 
    				{
    					var7 = 90;
    				}
    				
    				final float var9 = 0.00390625F;
    				final float var10 = 0.00390625F;
    				
    				final ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
    		        final int i = scaledresolution.getScaledWidth();
    		        final int k = scaledresolution.getScaledHeight();
    		        minecraft.entityRenderer.setupOverlayRendering();
    		        GL11.glEnable(GL11.GL_BLEND);
    		        GL11.glDisable(GL11.GL_DEPTH_TEST);
    		        GL11.glDepthMask(false);
    				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    				GL11.glDisable(GL11.GL_ALPHA_TEST);
    				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/gui.png"));
    				final Tessellator tessellator = Tessellator.instance;
    				tessellator.startDrawingQuads();
    				tessellator.addVertexWithUV(i - 29, 33.5 + 23.5, -90D, (85) * 0.00390625F, 		47 * 0.00390625F);
    				tessellator.addVertexWithUV(i - 10, 33.5 + 23.5, -90D, (85 + 19) * 0.00390625F, 	47 * 0.00390625F);
    				tessellator.addVertexWithUV(i - 10, 33.5 - 23.5, -90D, (85 + 19) * 0.00390625F, 	0 * 0.00390625F);
    				tessellator.addVertexWithUV(i - 29, 33.5 - 23.5, -90D, (85) * 0.00390625F, 		0 * 0.00390625F);
    				tessellator.draw();
    				tessellator.startDrawingQuads();
    				tessellator.addVertexWithUV(i - 49, 33.5 + 23.5, -90D, (85) * 0.00390625F, 		47 * 0.00390625F);
    				tessellator.addVertexWithUV(i - 30, 33.5 + 23.5, -90D, (85 + 19) * 0.00390625F, 	47 * 0.00390625F);
    				tessellator.addVertexWithUV(i - 30, 33.5 - 23.5, -90D, (85 + 19) * 0.00390625F, 	0 * 0.00390625F);
    				tessellator.addVertexWithUV(i - 49, 33.5 - 23.5, -90D, (85) * 0.00390625F, 		0 * 0.00390625F);
    				tessellator.draw();
    				GL11.glDepthMask(true);
    				GL11.glEnable(GL11.GL_DEPTH_TEST);
    				GL11.glEnable(GL11.GL_ALPHA_TEST);
    				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    				
    				if (var6 > 0 || var6 <= 0) 
    				{
    					final Tessellator tessellator2 = Tessellator.instance;

    					tessellator2.startDrawingQuads();
        				tessellator.addVertexWithUV(i - 48, 34.5 - 23.5 + (var6 / 2), 	0, (105) * 0.00390625F, 		(var6 / 2) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 31, 34.5 - 23.5 + (var6 / 2), 	0, (105 + 17) * 0.00390625F, 	(var6 / 2) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 31, 34.5 - 23.5,	 			0, (105 + 17) * 0.00390625F, 	1 * 0.00390625F);
        				tessellator.addVertexWithUV(i - 48, 34.5 - 23.5, 				0, (105) * 0.00390625F, 		1 * 0.00390625F);
    					tessellator2.draw();

    					tessellator2.startDrawingQuads();
        				tessellator.addVertexWithUV(i - 49, 34.5 - 23.5 + (var6 / 2), 		0, (66) * 0.00390625F, 		(var6 / 2) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 31, 34.5 - 23.5 + (var6 / 2), 		0, (66 + 17) * 0.00390625F, (var6 / 2) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 31, 34.5 - 23.5 + (var6 / 2) - 1,	0, (66 + 17) * 0.00390625F, ((var6 / 2) - 1) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 49, 34.5 - 23.5 + (var6 / 2) - 1, 	0, (66) * 0.00390625F, 		((var6 / 2) - 1) * 0.00390625F);
    					tessellator2.draw();
    				}
    				
    				if (var7 > 0 || var7 <= 0) 
    				{
    					final Tessellator tessellator2 = Tessellator.instance;

    					tessellator2.startDrawingQuads();
        				tessellator.addVertexWithUV(i - 28, 34.5 - 23.5 + (var7 / 2), 	0, (105) * 0.00390625F, 		(var7 / 2) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 11, 34.5 - 23.5 + (var7 / 2), 	0, (105 + 17) * 0.00390625F, 	(var7 / 2) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 11, 34.5 - 23.5,	 			0, (105 + 17) * 0.00390625F, 	1 * 0.00390625F);
        				tessellator.addVertexWithUV(i - 28, 34.5 - 23.5, 				0, (105) * 0.00390625F, 		1 * 0.00390625F);
    					tessellator2.draw();

    					tessellator2.startDrawingQuads();
        				tessellator.addVertexWithUV(i - 29, 34.5 - 23.5 + (var7 / 2), 	0, (66) * 0.00390625F, 		(var7 / 2) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 11, 34.5 - 23.5 + (var7 / 2), 	0, (66 + 17) * 0.00390625F, (var7 / 2) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 11, 34.5 - 23.5 + (var7 / 2) - 1,	 			0, (66 + 17) * 0.00390625F, ((var7 / 2) - 1) * 0.00390625F);
        				tessellator.addVertexWithUV(i - 29, 34.5 - 23.5 + (var7 / 2) - 1, 				0, (66) * 0.00390625F, 		((var7 / 2) - 1) * 0.00390625F);
    					tessellator2.draw();
    				}
    			}
            }
    	}

        private void drawCenteringRectangle(double var1, double var3, double var5, double var7, double var9)
        {
            var7 *= 0.5D;
            var9 *= 0.5D;
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.addVertexWithUV(var1 - var7, var3 + var9, var5, 0.0D, 1.0D);
            t.addVertexWithUV(var1 + var7, var3 + var9, var5, 1.0D, 1.0D);
            t.addVertexWithUV(var1 + var7, var3 - var9, var5, 1.0D, 0.0D);
            t.addVertexWithUV(var1 - var7, var3 - var9, var5, 0.0D, 0.0D);
            t.draw();
        }
    	
        @SuppressWarnings("unused")
		public static void renderName(EntityLiving par1EntityPlayer, double par2, double par4, double par6)
        {
        	// TODO
    		final Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            final WorldClient world = minecraft.theWorld;
            
            final EntityPlayerSP player = minecraft.thePlayer;
            
            ItemStack helmetSlot = null;
    		
    		if (player != null && player.inventory.armorItemInSlot(3) != null)
    		{
    			helmetSlot = player.inventory.armorItemInSlot(3);
    		}
    		
            if (false && helmetSlot != null && helmetSlot.getItem() instanceof GCCoreItemSensorGlasses && minecraft.currentScreen == null && Minecraft.isGuiEnabled() && par1EntityPlayer != RenderManager.instance.livingPlayer && !par1EntityPlayer.getHasActivePotion())
            {
            	final int health = 100 / par1EntityPlayer.getMaxHealth()
    					* par1EntityPlayer.getHealth();
    			final int sizeX = 24;
    			final int sizeY = 24;
    			final int offsetX = 48;
    			final int offsetY = 0;
    			final float var6 = -sizeX / 2.0F;
    			final float var7 = -sizeY / 2.0F;
    			final float var8 = -0.5F;
    			final float var9 = 0.5F;

    			for (int var10 = 0; var10 < sizeX / 24; ++var10) {
    				for (int var11 = 0; var11 < sizeY / 24; ++var11) {
    					final float var12 = var6 + (var10 + 1) * 24;
    					final float var13 = var6 + var10 * 24;
    					final float var14 = var7 + (var11 + 1) * 48;
    					final float var15 = var7 + var11 * 24;
    					hooblah(par2, par4, par6);
    					final float var16 = 72 / 256.0F;
    					final float var17 = 48 / 256.0F;
    					final float var18 = 0 / 256.0F;
    					final float var19 = 48 / 256.0F;
    					final float var20 = 0.75F;
    					final float var21 = 0.8125F;
    					final float var22 = 0.0F;
    					final float var23 = 0.0625F;
    					final float var24 = 0.75F;
    					final float var25 = 0.8125F;
    					final float var26 = 0.001953125F;
    					final float var27 = 0.001953125F;
    					final float var28 = 0.7519531F;
    					final float var29 = 0.7519531F;
    					final float var30 = 0.0F;
    					final float var31 = 0.0625F;
    					final Tessellator var32 = Tessellator.instance;
    					var32.startDrawingQuads();
    					var32.setNormal(0.0F, 0.0F, -1.0F);
    					var32.addVertexWithUV(var12, var15, var8, var17, var18);
    					var32.addVertexWithUV(var13, var15, var8, var16, var18);
    					var32.addVertexWithUV(var13, var14, var8, var16, var19);
    					var32.addVertexWithUV(var12, var14, var8, var17, var19);
    					var32.setNormal(0.0F, 0.0F, 1.0F);
    					var32.addVertexWithUV(var12, var14, var9, var20, var22);
    					var32.addVertexWithUV(var13, var14, var9, var21, var22);
    					var32.addVertexWithUV(var13, var15, var9, var21, var23);
    					var32.addVertexWithUV(var12, var15, var9, var20, var23);
    					var32.setNormal(0.0F, 1.0F, 0.0F);
    					var32.addVertexWithUV(var12, var14, var8, var24, var26);
    					var32.addVertexWithUV(var13, var14, var8, var25, var26);
    					var32.addVertexWithUV(var13, var14, var9, var25, var27);
    					var32.addVertexWithUV(var12, var14, var9, var24, var27);
    					var32.setNormal(0.0F, -1.0F, 0.0F);
    					var32.addVertexWithUV(var12, var15, var9, var24, var26);
    					var32.addVertexWithUV(var13, var15, var9, var25, var26);
    					var32.addVertexWithUV(var13, var15, var8, var25, var27);
    					var32.addVertexWithUV(var12, var15, var8, var24, var27);
    					var32.setNormal(-1.0F, 0.0F, 0.0F);
    					var32.addVertexWithUV(var12, var14, var9, var29, var30);
    					var32.addVertexWithUV(var12, var15, var9, var29, var31);
    					var32.addVertexWithUV(var12, var15, var8, var28, var31);
    					var32.addVertexWithUV(var12, var14, var8, var28, var30);
    					var32.setNormal(1.0F, 0.0F, 0.0F);
    					var32.addVertexWithUV(var13, var14, var8, var29, var30);
    					var32.addVertexWithUV(var13, var15, var8, var29, var31);
    					var32.addVertexWithUV(var13, var15, var9, var28, var31);
    					var32.addVertexWithUV(var13, var14, var9, var28, var30);
    					var32.draw();
    					GL11.glDepthMask(true);
    					GL11.glEnable(GL11.GL_LIGHTING);
    					GL11.glDisable(GL11.GL_BLEND);
    					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    					GL11.glPopMatrix();
    				}
    			}

    			for (int var10 = 0; var10 < sizeX / 13; ++var10) {
    				for (int var11 = 0; var11 < sizeY / 13; ++var11) {
    					final float var12 = var6 + (var10 + 1) * 102;
    					final float var13 = var6 + var10 * 102;
    					final float var14 = var7 + (var11 + 1) * 14;
    					final float var15 = var7 + var11 * 14;
    					hooblah2(par2, par4, par6);
    					final float var16 = 256 / 256.0F;
    					final float var17 = 154 / 256.0F;
    					final float var18 = 14 / 256.0F;
    					final float var19 = 0 / 256.0F;
    					final float var20 = 0.75F;
    					final float var21 = 0.8125F;
    					final float var22 = 0.0F;
    					final float var23 = 0.0625F;
    					final float var24 = 0.75F;
    					final float var25 = 0.8125F;
    					final float var26 = 0.001953125F;
    					final float var27 = 0.001953125F;
    					final float var28 = 0.7519531F;
    					final float var29 = 0.7519531F;
    					final float var30 = 0.0F;
    					final float var31 = 0.0625F;
    					final Tessellator var32 = Tessellator.instance;
    					var32.startDrawingQuads();
    					var32.setNormal(0.0F, 0.0F, -1.0F);
    					var32.addVertexWithUV(var12, var15, var8, var17, var18);
    					var32.addVertexWithUV(var13, var15, var8, var16, var18);
    					var32.addVertexWithUV(var13, var14, var8, var16, var19);
    					var32.addVertexWithUV(var12, var14, var8, var17, var19);
    					var32.setNormal(0.0F, 0.0F, 1.0F);
    					var32.addVertexWithUV(var12, var14, var9, var20, var22);
    					var32.addVertexWithUV(var13, var14, var9, var21, var22);
    					var32.addVertexWithUV(var13, var15, var9, var21, var23);
    					var32.addVertexWithUV(var12, var15, var9, var20, var23);
    					var32.setNormal(0.0F, 1.0F, 0.0F);
    					var32.addVertexWithUV(var12, var14, var8, var24, var26);
    					var32.addVertexWithUV(var13, var14, var8, var25, var26);
    					var32.addVertexWithUV(var13, var14, var9, var25, var27);
    					var32.addVertexWithUV(var12, var14, var9, var24, var27);
    					var32.setNormal(0.0F, -1.0F, 0.0F);
    					var32.addVertexWithUV(var12, var15, var9, var24, var26);
    					var32.addVertexWithUV(var13, var15, var9, var25, var26);
    					var32.addVertexWithUV(var13, var15, var8, var25, var27);
    					var32.addVertexWithUV(var12, var15, var8, var24, var27);
    					var32.setNormal(-1.0F, 0.0F, 0.0F);
    					var32.addVertexWithUV(var12, var14, var9, var29, var30);
    					var32.addVertexWithUV(var12, var15, var9, var29, var31);
    					var32.addVertexWithUV(var12, var15, var8, var28, var31);
    					var32.addVertexWithUV(var12, var14, var8, var28, var30);
    					var32.setNormal(1.0F, 0.0F, 0.0F);
    					var32.addVertexWithUV(var13, var14, var8, var29, var30);
    					var32.addVertexWithUV(var13, var15, var8, var29, var31);
    					var32.addVertexWithUV(var13, var15, var9, var28, var31);
    					var32.addVertexWithUV(var13, var14, var9, var28, var30);
    					var32.draw();
    					GL11.glDepthMask(true);
    					GL11.glEnable(GL11.GL_LIGHTING);
    					GL11.glDisable(GL11.GL_BLEND);
    					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    					GL11.glPopMatrix();
    				}
    			}

    			for (int var10 = 0; var10 < sizeX / 13; ++var10) {
    				for (int var11 = 0; var11 < sizeY / 13; ++var11) {
    					final float var12 = var6 + (var10 + 1) * health + 1;
    					final float var13 = var6 + var10 * health + 1;
    					final float var14 = var7 + (var11 + 1) * 14 - 1;
    					final float var15 = var7 + var11 * 14 - 1;
    					hooblah2(par2, par4, par6);
    					final float var16 = (154 + health) / 256.0F;
    					final float var17 = 154 / 256.0F;
    					final float var18 = 28 / 256.0F;
    					final float var19 = 14 / 256.0F;
    					final float var20 = 0.75F;
    					final float var21 = 0.8125F;
    					final float var22 = 0.0F;
    					final float var23 = 0.0625F;
    					final float var24 = 0.75F;
    					final float var25 = 0.8125F;
    					final float var26 = 0.001953125F;
    					final float var27 = 0.001953125F;
    					final float var28 = 0.7519531F;
    					final float var29 = 0.7519531F;
    					final float var30 = 0.0F;
    					final float var31 = 0.0625F;
    					final Tessellator var32 = Tessellator.instance;
    					var32.startDrawingQuads();
    					var32.setNormal(0.0F, 0.0F, -1.0F);
    					var32.addVertexWithUV(var12, var15, var8, var17, var18);
    					var32.addVertexWithUV(var13, var15, var8, var16, var18);
    					var32.addVertexWithUV(var13, var14, var8, var16, var19);
    					var32.addVertexWithUV(var12, var14, var8, var17, var19);
    					var32.setNormal(0.0F, 0.0F, 1.0F);
    					var32.addVertexWithUV(var12, var14, var9, var20, var22);
    					var32.addVertexWithUV(var13, var14, var9, var21, var22);
    					var32.addVertexWithUV(var13, var15, var9, var21, var23);
    					var32.addVertexWithUV(var12, var15, var9, var20, var23);
    					var32.setNormal(0.0F, 1.0F, 0.0F);
    					var32.addVertexWithUV(var12, var14, var8, var24, var26);
    					var32.addVertexWithUV(var13, var14, var8, var25, var26);
    					var32.addVertexWithUV(var13, var14, var9, var25, var27);
    					var32.addVertexWithUV(var12, var14, var9, var24, var27);
    					var32.setNormal(0.0F, -1.0F, 0.0F);
    					var32.addVertexWithUV(var12, var15, var9, var24, var26);
    					var32.addVertexWithUV(var13, var15, var9, var25, var26);
    					var32.addVertexWithUV(var13, var15, var8, var25, var27);
    					var32.addVertexWithUV(var12, var15, var8, var24, var27);
    					var32.setNormal(-1.0F, 0.0F, 0.0F);
    					var32.addVertexWithUV(var12, var14, var9, var29, var30);
    					var32.addVertexWithUV(var12, var15, var9, var29, var31);
    					var32.addVertexWithUV(var12, var15, var8, var28, var31);
    					var32.addVertexWithUV(var12, var14, var8, var28, var30);
    					var32.setNormal(1.0F, 0.0F, 0.0F);
    					var32.addVertexWithUV(var13, var14, var8, var29, var30);
    					var32.addVertexWithUV(var13, var15, var8, var29, var31);
    					var32.addVertexWithUV(var13, var15, var9, var28, var31);
    					var32.addVertexWithUV(var13, var14, var9, var28, var30);
    					var32.draw();
    					GL11.glDepthMask(true);
    					GL11.glEnable(GL11.GL_LIGHTING);
    					GL11.glDisable(GL11.GL_BLEND);
    					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    					GL11.glPopMatrix();
    				}
    			}
//                float var8 = 1.6F;
//                float var9 = 0.016666668F * var8;
//                double var10 = par1EntityPlayer.getDistanceSqToEntity(RenderManager.instance.livingPlayer);
//                float var12 = par1EntityPlayer.isSneaking() ? 34F : 64F;
//
//                if (var10 < (double)(var12 * var12))
//                {
//                    String var13 = "TESTNO1";
//
//                    if (par1EntityPlayer.isSneaking())
//                    {
//                        FontRenderer var14 = RenderManager.instance.getFontRenderer();
//                        GL11.glPushMatrix();
//                        GL11.glTranslatef((float)par2 + 0.0F, (float)par4 + 2.3F, (float)par6);
//                        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
//                        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
//                        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
//                        GL11.glScalef(-var9, -var9, var9);
//                        GL11.glDisable(GL11.GL_LIGHTING);
//                        GL11.glTranslatef(0.0F, 0.25F / var9, 0.0F);
//                        GL11.glDepthMask(false);
//                        GL11.glEnable(GL11.GL_BLEND);
//                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//                        Tessellator var15 = Tessellator.instance;
//                        GL11.glDisable(GL11.GL_TEXTURE_2D);
//                        var15.startDrawingQuads();
//                        int var16 = var14.getStringWidth(var13) / 2;
//                        var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
//                        var15.addVertex((double)(-var16 - 1), -1.0D, 0.0D);
//                        var15.addVertex((double)(-var16 - 1), 8.0D, 0.0D);
//                        var15.addVertex((double)(var16 + 1), 8.0D, 0.0D);
//                        var15.addVertex((double)(var16 + 1), -1.0D, 0.0D);
//                        var15.draw();
//                        GL11.glEnable(GL11.GL_TEXTURE_2D);
//                        GL11.glDepthMask(true);
//                        var14.drawString(var13, -var14.getStringWidth(var13) / 2, 0, 553648127);
//                        GL11.glEnable(GL11.GL_LIGHTING);
//                        GL11.glDisable(GL11.GL_BLEND);
//                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//                        GL11.glPopMatrix();
//                    }
//                    else if (par1EntityPlayer.isPlayerSleeping())
//                    {
//                        renderLivingLabel(par1EntityPlayer, var13, par2, par4 - 1.5D, par6, 64);
//                    }
//                    else
//                    {
//                        renderLivingLabel(par1EntityPlayer, var13, par2, par4, par6, 64);
//                    }
//                }
            }
        }

    	public static void hooblah(double par2, double par4, double par6) 
    	{
    		final float var8 = 0.5F;
    		final float var9 = 0.016666668F * var8;
    		GL11.glPushMatrix();
    		GL11.glTranslatef((float) par2 + 0.0F, (float) par4 + 2.7F, (float) par6);
    		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    		GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
    		GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
    		GL11.glScalef(-var9, -var9, var9);
    		GL11.glDisable(GL11.GL_LIGHTING);
    		GL11.glTranslatef(0.0F, 0.25F / var9, 0.0F);
    		GL11.glDepthMask(false);
    		GL11.glEnable(GL11.GL_BLEND);
    		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/entities/overhead.png"));
    	}

    	public static void hooblah2(double par2, double par4, double par6) 
    	{
    		final float var8 = 0.5F;
    		final float var9 = 0.016666668F * var8;
    		GL11.glPushMatrix();
    		GL11.glTranslatef((float) par2 + 0.0F, (float) par4 + 2.47F, (float) par6);
    		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    		GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
    		GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
    		GL11.glScalef(-var9, -var9, var9);
    		GL11.glDisable(GL11.GL_LIGHTING);
    		GL11.glTranslatef(-39.0F, 0.25F / var9, 0.0F);
    		GL11.glDepthMask(false);
    		GL11.glEnable(GL11.GL_BLEND);
    		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    		GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/entities/overhead.png"));
    	}
        
        private static void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9)
        {
            final double var10 = par1EntityLiving.getDistanceSqToEntity(RenderManager.instance.livingPlayer);

            if (var10 <= (par9 * par9))
            {
                final FontRenderer var12 = RenderManager.instance.getFontRenderer();
                final float var13 = 1.6F;
                final float var14 = 0.016666668F * var13;
                GL11.glPushMatrix();
                GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + 2.3F, (float)par7);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(-var14, -var14, var14);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                final Tessellator var15 = Tessellator.instance;
                byte var16 = 0;

                if (par2Str.equals("deadmau5"))
                {
                    var16 = -10;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                var15.startDrawingQuads();
                final int var17 = var12.getStringWidth(par2Str) / 2;
                var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                var15.addVertex((-var17 - 1), (-1 + var16), 0.0D);
                var15.addVertex((-var17 - 1), (8 + var16), 0.0D);
                var15.addVertex((var17 + 1), (8 + var16), 0.0D);
                var15.addVertex((var17 + 1), (-1 + var16), 0.0D);
                var15.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, 553648127);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, -1);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    	
        protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3)
        {
            final float var4 = 1.0F;
            final float var5 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * var4;
            final float var6 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * var4;
            final double var7 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * var4;
            final double var9 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * var4 + 1.62D - par2EntityPlayer.yOffset;
            final double var11 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * var4;
            final Vec3 var13 = par1World.getWorldVec3Pool().getVecFromPool(var7, var9, var11);
            final float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
            final float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
            final float var16 = -MathHelper.cos(-var5 * 0.017453292F);
            final float var17 = MathHelper.sin(-var5 * 0.017453292F);
            final float var18 = var15 * var16;
            final float var20 = var14 * var16;
            final double var21 = 100.0D;
            final Vec3 var23 = var13.addVector(var18 * var21, var17 * var21, var20 * var21);
            return par1World.rayTraceBlocks_do_do(var13, var23, par3, !par3);
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
    	static KeyBinding tankRefill = new KeyBinding("Galacticraft Player Inventory", Keyboard.KEY_R);
    	static KeyBinding galaxyMap = new KeyBinding("Galaxy Map", Keyboard.KEY_M);
    	public static KeyBinding openSpaceshipInv = new KeyBinding("Open Spaceship Inventory", Keyboard.KEY_F);
    	public static KeyBinding toggleAdvGoggles = new KeyBinding("Toggle Advanced Sensor Goggles", Keyboard.KEY_K);

        public GCKeyHandler() 
        {
            super(new KeyBinding[] {tankRefill, galaxyMap, openSpaceshipInv, toggleAdvGoggles}, new boolean[] {false, false, false, false});
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
        	
        	final boolean handled = true;
        	
        	if(minecraft.currentScreen != null || tickEnd)
        	{
    			return;
        	}
        	
        	if (kb.keyCode == GCKeyHandler.tankRefill.keyCode)
        	{
        		if (minecraft.currentScreen == null)
            	{
                	final EntityPlayerSP player = minecraft.thePlayer;
                	
                    final Object[] toSend = {player.username};
                    PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 0, toSend));
            	    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiTankRefill, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
            	}
        	}
        	else if (kb.keyCode == GCKeyHandler.galaxyMap.keyCode)
        	{
        		if (minecraft.currentScreen == null)
        		{
                	final EntityPlayerSP player = minecraft.thePlayer;
        			player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiGalaxyMap, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
        		}
        	}
        	else if (kb.keyCode == GCKeyHandler.openSpaceshipInv.keyCode)
        	{
            	final EntityPlayerSP player = minecraft.thePlayer;
            	
                final Object[] toSend = {player.username};
                PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 6, toSend));
        	    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
        	}
        	else if (kb.keyCode == GCKeyHandler.toggleAdvGoggles.keyCode)
        	{
            	final EntityPlayerSP player = minecraft.thePlayer;
            	
    	        for (int j = 0; j < GalacticraftCore.gcPlayers.size(); ++j)
    	        {
    				final GCCorePlayerBaseClient playerBase = (GCCorePlayerBaseClient) GalacticraftCore.players.get(j);
    				
    				if (playerBase != null && player != null && player.username.equals(playerBase.getPlayer().username))
    				{
    					playerBase.toggleGoggles();
    				}
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
//                    PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 5, toSend));
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
