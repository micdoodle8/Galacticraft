package micdoodle8.mods.galacticraft.core.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.AdvancedAchievement;
import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchFlameFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchSmokeFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityOxygenFX;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiChoosePlanet;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSkeleton;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelZombie;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererBreathableAir;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererMeteor;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererOxygenPipe;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderArrow;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderBlockTreasureChest;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderBuggy;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderCreeper;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderFlag;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderMeteor;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSpider;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererBuggy;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererFlag;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSounds;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSensorGlasses;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntitySmokeFX;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.RenderLiving;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.StatBase;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
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
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;

/**
 * Copyright 2012, micdoodle8
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
	public static long getFirstBootTime;
	public static long getCurrentTime;
	private Random rand = new Random();
	public ClientProxyMoon moon = new ClientProxyMoon();
	public static int teleportCooldown;
	public static List<IPlanetSlotRenderer> slotRenderers = new ArrayList<IPlanetSlotRenderer>();
	
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		moon.preInit(event);
		
		MinecraftForge.EVENT_BUS.register(new GCCoreSounds());
		getFirstBootTime = System.currentTimeMillis();
	}

	@Override
	public void init(FMLInitializationEvent event) 
	{
		moon.init(event);
		
		TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
		KeyBindingRegistry.registerKeyBinding(new GCKeyHandler());
        NetworkRegistry.instance().registerChannel(new ClientPacketHandler(), "Galacticraft", Side.CLIENT);
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityTreasureChest.class, new GCCoreTileEntityTreasureChestRenderer());
        this.treasureChestRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreRenderBlockTreasureChest(this.treasureChestRenderID));
        this.torchRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererUnlitTorch(this.torchRenderID));
        this.breathableAirRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererBreathableAir(this.breathableAirRenderID));
        this.oxygenPipeRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererOxygenPipe(this.oxygenPipeRenderID));
        this.meteorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererMeteor(this.meteorRenderID));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
		moon.postInit(event);
	}
	
	@Override
	public void registerRenderInformation() 
	{
		moon.registerRenderInformation();
		
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpaceship.class, new GCCoreRenderSpaceship());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpider.class, new GCCoreRenderSpider());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityZombie.class, new RenderLiving(new GCCoreModelZombie(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityCreeper.class, new GCCoreRenderCreeper());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySkeleton.class, new RenderLiving(new GCCoreModelSkeleton(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityMeteor.class, new GCCoreRenderMeteor());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityBuggy.class, new GCCoreRenderBuggy());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityFlag.class, new GCCoreRenderFlag());
        RenderingRegistry.addNewArmourRendererPrefix("oxygen");
        RenderingRegistry.addNewArmourRendererPrefix("sensor");
        RenderingRegistry.addNewArmourRendererPrefix("sensorox");
        RenderingRegistry.addNewArmourRendererPrefix("titanium");
        RenderingRegistry.addNewArmourRendererPrefix("titaniumox");
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityArrow.class, new GCCoreRenderArrow());
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/core/client/blocks/core.png");
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/core/client/items/core.png");
        MinecraftForgeClient.registerItemRenderer(GCCoreBlocks.unlitTorch.blockID, new GCCoreItemRendererUnlitTorch());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.spaceship.shiftedIndex, new GCCoreItemRendererSpaceship());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.buggy.shiftedIndex, new GCCoreItemRendererBuggy());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.flag.shiftedIndex, new GCCoreItemRendererFlag());
	}

	@Override
	public void addSlotRenderer(IPlanetSlotRenderer slotRenderer)
	{
		this.slotRenderers.add(slotRenderer);
	}

	@Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }
	
	@Override
	public int getGCTreasureChestRenderID()
	{
		return this.treasureChestRenderID;
	}
	
	@Override
	public int getGCUnlitTorchRenderID()
	{
		return this.torchRenderID;
	}
	
	@Override
	public int getGCBreathableAirRenderID()
	{
		return this.breathableAirRenderID;
	}
	
	@Override
	public int getGCOxygenPipeRenderID()
	{
		return this.oxygenPipeRenderID;
	}
	
	@Override
	public int getGCMeteorRenderID()
	{
		return this.meteorRenderID;
	}

	@Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
	{
		this.spawnParticle(var1, var2, var4, var6, var8, var10, var12, 0.0D, 0.0D, 0.0D, b);
	}

	@Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var13, double var14, double var15, boolean b)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            double var16 = mc.renderViewEntity.posX - var2;
            double var17 = mc.renderViewEntity.posY - var4;
            double var19 = mc.renderViewEntity.posZ - var6;
            Object var21 = null;
            double var22 = 64.0D;
            
            if (var1.equals("whitesmoke"))
            {
        		EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1.0F, b);
        		if (fx != null)
        		{
                	mc.effectRenderer.addEffect(fx);
        		}
            }
            else if (var1.equals("whitesmokelarge"))
            {
        		EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F, b);
        		if (fx != null)
        		{
        			mc.effectRenderer.addEffect(fx);
        		}
        	}
            else if (var1.equals("launchflame"))
            {
        		EntityFX fx = new GCCoreEntityLaunchFlameFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1F);
        		if (fx != null)
        		{
        			mc.effectRenderer.addEffect(fx);
        		}
            }
            else if (var1.equals("distancesmoke") && var16 * var16 + var17 * var17 + var19 * var19 < var22 * var22 * 1.7)
            {
            	EntityFX fx = new EntitySmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F);
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
	
    public class ClientPacketHandler implements IPacketHandler
    {
		@Override
		public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p) 
		{
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            int packetType = GCCoreUtil.readPacketID(data);
            EntityPlayer player = (EntityPlayer)p;
            
            if (packetType == 0)
            {
                Class[] decodeAs = {Integer.class, String.class};
                Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
                
                if (String.valueOf(packetReadout[1]).equals(String.valueOf(FMLClientHandler.instance().getClient().thePlayer.username)))
                {
                    TickHandlerClient.airRemaining = (Integer) packetReadout[0];
                }
            }
            else if (packetType == 1)
            {
                Class[] decodeAs = {Float.class};
                Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
            	
                FMLClientHandler.instance().getClient().thePlayer.timeInPortal = (Float) packetReadout[0];
            }
            else if (packetType == 2)
            {
            	Class[] decodeAs = {String.class, String.class};
                Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
            	
                if (String.valueOf(packetReadout[0]).equals(FMLClientHandler.instance().getClient().thePlayer.username))
                {
                	FMLLog.info((String)packetReadout[1]);
                	
                	String[] destinations = ((String)packetReadout[1]).split("\\.");
                	
            		if (FMLClientHandler.instance().getClient().theWorld != null && !(FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet))
            		{
            			FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiChoosePlanet(FMLClientHandler.instance().getClient().thePlayer, destinations));
            			FMLClientHandler.instance().getClient().setIngameNotInFocus();
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
				AdvancedAchievement achiev = (AdvancedAchievement) stat;
				
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
    
    public static class TickHandlerClient implements ITickHandler
    {
    	public static int airRemaining;
    	
    	@Override
    	public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
    		ClientProxyCore.getCurrentTime = System.currentTimeMillis();
    		
    		Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            WorldClient world = minecraft.theWorld;
            
            EntityClientPlayerMP player = minecraft.thePlayer;
    		
    		if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
    			if (player != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && !player.capabilities.isFlying && !minecraft.isGamePaused && !player.handleWaterMovement()) 
    			{
    				IGalacticraftWorldProvider wp = (IGalacticraftWorldProvider) player.worldObj.provider;
    				player.motionY = player.motionY + wp.getGravity();
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
    			
    			if (player != null && player.ridingEntity != null && minecraft.gameSettings.keyBindJump.pressed)
    			{
    				Object[] toSend = {0};
    	            PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 3, toSend));
    			}
    			
            	if (Keyboard.isKeyDown(Keyboard.KEY_W))
            	{
            		if (minecraft.currentScreen == null)
                	{
                    	EntityPlayerSP player2 = minecraft.thePlayer;
                    	
                        Object[] toSend = {player.username, 0};
                        PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 5, toSend));
                	}
            	}
            	if (Keyboard.isKeyDown(Keyboard.KEY_A))
            	{
            		if (minecraft.currentScreen == null)
                	{
                    	EntityPlayerSP player2 = minecraft.thePlayer;
                    	
                        Object[] toSend = {player.username, 1};
                        PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 5, toSend));
                	}
            	}
            	if (Keyboard.isKeyDown(Keyboard.KEY_D))
            	{
            		if (minecraft.currentScreen == null)
                	{
                    	EntityPlayerSP player2 = minecraft.thePlayer;
                    	
                        Object[] toSend = {player.username, 2};
                        PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 5, toSend));
                	}
            	}
            }
        }
    	
		int i = 0;

    	@Override
    	public void tickEnd(EnumSet<TickType> type, Object... tickData) 
    	{
    		Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            WorldClient world = minecraft.theWorld;
            
            EntityPlayerSP player = minecraft.thePlayer;
            
            ItemStack helmetSlot = null;
    		
    		if (player != null && player.inventory.armorItemInSlot(3) != null)
    		{
    			helmetSlot = player.inventory.armorItemInSlot(3);
    		}
            
    		if (type.equals(EnumSet.of(TickType.RENDER)))
            {
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
    				
					ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
			        int i = scaledresolution.getScaledWidth();
			        int k = scaledresolution.getScaledHeight();
			        minecraft.entityRenderer.setupOverlayRendering();
			        GL11.glEnable(GL11.GL_BLEND);
			        GL11.glDisable(GL11.GL_DEPTH_TEST);
			        GL11.glDepthMask(false);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
    				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/backgrounds/black.png"));
					Tessellator tessellator = Tessellator.instance;
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(i / 2 - 2 * k * 80, k * 40, -90D, 0.0D, 1.0D);
					tessellator.addVertexWithUV(i / 2 + 2 * k * 80, k * 40, -90D, 1.0D, 1.0D);
					tessellator.addVertexWithUV(i / 2 + 2 * k * 80, 0.0D * 40, -90D, 1.0D, 0.0D);
					tessellator.addVertexWithUV(i / 2 - 2 * k * 80, 0.0D * 40, -90D, 0.0D, 0.0D);
					tessellator.draw();
					GL11.glDepthMask(true);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        		}
    			
        		if (helmetSlot != null && helmetSlot.getItem() instanceof GCCoreItemSensorGlasses && minecraft.currentScreen == null)
        		{
        			i++;
        			
        	        float f = MathHelper.sin((i) / 80.0F) * 0.1F + 0.1F;
        			
					ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
			        int i = scaledresolution.getScaledWidth();
			        int k = scaledresolution.getScaledHeight();
			        minecraft.entityRenderer.setupOverlayRendering();
			        GL11.glEnable(GL11.GL_BLEND);
			        GL11.glDisable(GL11.GL_DEPTH_TEST);
			        GL11.glDepthMask(false);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
    				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/hud.png"));
					Tessellator tessellator = Tessellator.instance;
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
        		}

        		if (player != null && player.worldObj.provider instanceof IGalacticraftWorldProvider)
    			{
    				short var8 = 90;
    				int var6 = (airRemaining - 90) * -1;
    				if (airRemaining <= 0) 
    				{
    					var6 = (90);
    				}
    				
    				float var9 = 0.00390625F;
    				float var10 = 0.00390625F;
    				
    				ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
    		        int i = scaledresolution.getScaledWidth();
    		        int k = scaledresolution.getScaledHeight();
    		        minecraft.entityRenderer.setupOverlayRendering();
    		        GL11.glEnable(GL11.GL_BLEND);
    		        GL11.glDisable(GL11.GL_DEPTH_TEST);
    		        GL11.glDepthMask(false);
    				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    				GL11.glDisable(GL11.GL_ALPHA_TEST);
    				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/gui/gui.png"));
    				Tessellator tessellator = Tessellator.instance;
    				tessellator.startDrawingQuads();
    				tessellator.addVertexWithUV(i - 20, k / 2 + 45, -90D, 0 * 0.00390625F, 90 * 0.00390625F);
    				tessellator.addVertexWithUV(i - 10, k / 2 + 45, -90D, 10 * 0.00390625F, 90 * 0.00390625F);
    				tessellator.addVertexWithUV(i - 10, k / 2 - 45, -90D, 10 * 0.00390625F, 0 * 0.00390625F);
    				tessellator.addVertexWithUV(i - 20, k / 2 - 45, -90D, 0 * 0.00390625F, 0 * 0.00390625F);
    				tessellator.draw();
    				GL11.glDepthMask(true);
    				GL11.glEnable(GL11.GL_DEPTH_TEST);
    				GL11.glEnable(GL11.GL_ALPHA_TEST);
    				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    				
    				if (var6 > 0 || var6 <= 0) 
    				{
    					Tessellator tessellator2 = Tessellator.instance;
    					tessellator2.startDrawingQuads();
    					tessellator2.addVertexWithUV(i - 20, k / 2 - 45 + var6, 0, 10 * 0.00390625F, var6 * 0.00390625F);
    					tessellator2.addVertexWithUV(i - 10, k / 2 - 45 + var6, 0, 20 * 0.00390625F, var6 * 0.00390625F);
    					tessellator2.addVertexWithUV(i - 10, k / 2 - 45, 0, 20 * 0.00390625F, 0 * 0.00390625F);
    					tessellator2.addVertexWithUV(i - 20, k / 2 - 45, 0, 10 * 0.00390625F, 0 * 0.00390625F);
    					tessellator2.draw();
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
    	static KeyBinding tankRefill = new KeyBinding("Tank Refill", Keyboard.KEY_R);

        public GCKeyHandler() 
        {
            super(new KeyBinding[] {tankRefill}, new boolean[] {false});
        }

        @Override
        public String getLabel() 
        {
            return "Galacticraft Keybinds";
        }

        @Override
        public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) 
        {
        	Minecraft minecraft = FMLClientHandler.instance().getClient();
        	
        	boolean handled = true;
        	
        	if(minecraft.currentScreen != null || tickEnd)
        	{
    			return;
        	}
        	
        	if (kb.keyCode == this.tankRefill.keyCode)
        	{
        		if (minecraft.currentScreen == null)
            	{
                	EntityPlayerSP player = minecraft.thePlayer;
                	
                    Object[] toSend = {player.username};
                    PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 0, toSend));
            	    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiTankRefill, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
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

    		for (KeyBinding key : FMLClientHandler.instance().getClient().gameSettings.keyBindings)
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
