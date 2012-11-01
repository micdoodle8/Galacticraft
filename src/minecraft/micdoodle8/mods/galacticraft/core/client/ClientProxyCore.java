package micdoodle8.mods.galacticraft.core.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.EnumSet;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.GCCoreEntityMeteor;
import micdoodle8.mods.galacticraft.core.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.GCCoreEntitySpaceship;
import micdoodle8.mods.galacticraft.core.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.GCCoreItemSensorGlasses;
import micdoodle8.mods.galacticraft.core.GCCoreItems;
import micdoodle8.mods.galacticraft.core.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.GalacticraftWorldProvider;
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
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.RenderLiving;
import net.minecraft.src.ScaledResolution;
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
		
//		try
//		{
//			for (String string : GalacticraftCore.loader.spriteSheets)
//			{
//				FMLLog.info(string);
//				TextureFXManager.instance().addNewTextureOverride("/micdoodle8/mods/galacticraft/core/client/override/1.png.png", string, 0);
//			}
//		} 
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
		
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpaceship.class, new GCCoreRenderSpaceship());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpider.class, new GCCoreRenderSpider());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityZombie.class, new RenderLiving(new GCCoreModelZombie(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityCreeper.class, new GCCoreRenderCreeper());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySkeleton.class, new RenderLiving(new GCCoreModelSkeleton(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityMeteor.class, new GCCoreRenderMeteor());
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
	}
	
	@Override
	public void displayChoosePlanetGui()
	{
		if (FMLClientHandler.instance().getClient().theWorld != null && !(FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet))
		{
			FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiChoosePlanet(FMLClientHandler.instance().getClient().thePlayer));
		}
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
                Class[] decodeAs = {Integer.class};
                Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);

                TickHandlerClient.airRemaining = (Integer) packetReadout[0];
            }
            else if (packetType == 1)
            {
                Class[] decodeAs = {Float.class};
                Object[] packetReadout = GCCoreUtil.readPacketData(data, decodeAs);
            	
                FMLClientHandler.instance().getClient().thePlayer.timeInPortal = (Float) packetReadout[0];
            }
		}
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
    			if (player != null && player.worldObj.provider instanceof GalacticraftWorldProvider && !player.capabilities.isFlying && !minecraft.isGamePaused) 
    			{
    				player.motionY = player.motionY + 0.062;
    			}
    			
    			if (world != null)
    			{
    				world.setRainStrength(0.0F);
    			}
    			
    			if (player != null && player.ridingEntity != null && minecraft.gameSettings.keyBindJump.pressed)
    			{
//    				player.worldObj.playSoundEffect(player.ridingEntity.posX, player.ridingEntity.posY, player.ridingEntity.posZ, "shuttle.sound", 1F, 2.0F);
    	    		Object[] toSend = {0};
    	            PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 3, toSend));
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
    				float f = (((GCCoreEntitySpaceship)player.ridingEntity).getTimeSinceLaunch() - 250F) / 50F;
    				
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

        		if (player != null && player.worldObj.provider instanceof GalacticraftWorldProvider)
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
        	
        	if (kb == this.tankRefill)
        	{
        		if (minecraft.currentScreen == null)
            	{
                	EntityPlayerSP player = minecraft.thePlayer;
                	
                    Object[] toSend = {player.username};
                    PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 0, toSend));
            	    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiTankRefill, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
            	}
        	}
        }

        @Override
        public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) 
        {
        }

        @Override
        public EnumSet<TickType> ticks() 
        {
            return EnumSet.of(TickType.CLIENT);
        }
    }
}
