package micdoodle8.mods.galacticraft.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.EnumSet;
import java.util.Random;

import micdoodle8.mods.galacticraft.CommonProxy;
import micdoodle8.mods.galacticraft.GCBlocks;
import micdoodle8.mods.galacticraft.GCChunkProvider;
import micdoodle8.mods.galacticraft.GCConfigManager;
import micdoodle8.mods.galacticraft.GCEntityArrow;
import micdoodle8.mods.galacticraft.GCEntityCreeper;
import micdoodle8.mods.galacticraft.GCEntityCreeperBoss;
import micdoodle8.mods.galacticraft.GCEntityProjectileTNT;
import micdoodle8.mods.galacticraft.GCEntitySkeleton;
import micdoodle8.mods.galacticraft.GCEntitySludgeling;
import micdoodle8.mods.galacticraft.GCEntitySpaceship;
import micdoodle8.mods.galacticraft.GCEntitySpider;
import micdoodle8.mods.galacticraft.GCEntityZombie;
import micdoodle8.mods.galacticraft.GCItemSensorGlasses;
import micdoodle8.mods.galacticraft.GCItems;
import micdoodle8.mods.galacticraft.GCTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.GCUtil;
import micdoodle8.mods.galacticraft.GCWorldProvider;
import micdoodle8.mods.galacticraft.Galacticraft;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityHugeExplodeFX;
import net.minecraft.src.EntityLargeExplodeFX;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.PlayerAPI;
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
public class ClientProxy extends CommonProxy
{
	private static int treasureChestRenderID;
	private static int fluidRenderID;
	private static int torchRenderID;
	private static int breathableAirRenderID;
	private static int oxygenPipeRenderID;
	public static long getFirstBootTime;
	public static long getCurrentTime;
	private Random rand = new Random();
	
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		MinecraftForge.EVENT_BUS.register(new GCSounds());
		getFirstBootTime = System.currentTimeMillis();
				
		try
		{
			PlayerAPI.register("Galacticraft", GCPlayerBase.class);
		}
		catch(Exception e)
		{
			FMLLog.severe("PLAYER API NOT INSTALLED.");
			FMLLog.severe("Galacticraft will now fail to load.");
			e.printStackTrace();
		}
	}

	@Override
	public void init(FMLInitializationEvent event) 
	{
		TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
		KeyBindingRegistry.registerKeyBinding(new GCKeyHandler());
        NetworkRegistry.instance().registerChannel(new ClientPacketHandler(), "Galacticraft", Side.CLIENT);
        ClientRegistry.bindTileEntitySpecialRenderer(GCTileEntityTreasureChest.class, new GCTileEntityTreasureChestRenderer());
        this.treasureChestRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCRenderBlockTreasureChest(this.treasureChestRenderID));
        this.fluidRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCBlockRendererBacterialSludge(this.fluidRenderID));
        this.torchRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCBlockRendererUnlitTorch(this.torchRenderID));
        this.breathableAirRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCBlockRendererBreathableAir(this.breathableAirRenderID));
        this.oxygenPipeRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCBlockRendererOxygenPipe(this.oxygenPipeRenderID));
        MinecraftForgeClient.registerItemRenderer(GCBlocks.unlitTorch.blockID, new GCItemRendererUnlitTorch());
        MinecraftForgeClient.registerItemRenderer(GCItems.spaceship.shiftedIndex, new GCItemRendererSpaceship());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
	}
	
	@Override
	public void registerRenderInformation() 
	{
        RenderingRegistry.registerEntityRenderingHandler(GCEntityCreeperBoss.class, new GCRenderCreeperBoss(new GCModelCreeperBoss(), 10.0F));
        RenderingRegistry.registerEntityRenderingHandler(GCEntityProjectileTNT.class, new GCRenderProjectileTNT());
        RenderingRegistry.registerEntityRenderingHandler(GCEntitySpaceship.class, new GCRenderSpaceship());
        RenderingRegistry.registerEntityRenderingHandler(GCEntitySpider.class, new GCRenderSpider());
        RenderingRegistry.registerEntityRenderingHandler(GCEntityZombie.class, new RenderLiving(new GCModelZombie(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(GCEntityCreeper.class, new GCRenderCreeper());
        RenderingRegistry.registerEntityRenderingHandler(GCEntitySkeleton.class, new RenderLiving(new GCModelSkeleton(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(GCEntitySludgeling.class, new GCRenderSludgeling());
        RenderingRegistry.addNewArmourRendererPrefix("oxygen");
        RenderingRegistry.addNewArmourRendererPrefix("sensor");
        RenderingRegistry.addNewArmourRendererPrefix("sensorox");
        RenderingRegistry.addNewArmourRendererPrefix("quandrium");
        RenderingRegistry.addNewArmourRendererPrefix("quandriumox");
        RenderingRegistry.addNewArmourRendererPrefix("desh");
        RenderingRegistry.addNewArmourRendererPrefix("deshox");
        RenderingRegistry.addNewArmourRendererPrefix("titanium");
        RenderingRegistry.addNewArmourRendererPrefix("titaniumox");
        RenderingRegistry.registerEntityRenderingHandler(GCEntityArrow.class, new GCRenderArrow());
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/client/blocks/core.png");
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/client/blocks/mars.png");
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/client/items/core.png");
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/client/items/mars.png");
	}
	
	@Override
	public void displayChoosePlanetGui()
	{
		if (FMLClientHandler.instance().getClient().theWorld != null && !(FMLClientHandler.instance().getClient().currentScreen instanceof GCGuiChoosePlanet))
		{
			FMLClientHandler.instance().getClient().displayGuiScreen(new GCGuiChoosePlanet(FMLClientHandler.instance().getClient().thePlayer));
		}
	}

	@Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

	@Override
	public int getGCFluidRenderID()
	{
		return this.fluidRenderID;
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
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
    {
        Minecraft var14 = FMLClientHandler.instance().getClient();

        if (var14 != null && var14.renderViewEntity != null && var14.effectRenderer != null)
        {
            double var15 = var14.renderViewEntity.posX - var2;
            double var17 = var14.renderViewEntity.posY - var4;
            double var19 = var14.renderViewEntity.posZ - var6;
            Object var21 = null;
            double var22 = 64.0D;
            
            if (var1.equals("whitesmoke"))
            {
        		EntityFX fx = new GCEntityLaunchSmokeFX(var14.theWorld, var2, var4, var6, var8, var10, var12, 1.0F, b);
        		if (fx != null)
        		{
                	var14.effectRenderer.addEffect(fx);
        		}
            }
            else if (var1.equals("whitesmokelarge"))
            {
        		EntityFX fx = new GCEntityLaunchSmokeFX(var14.theWorld, var2, var4, var6, var8, var10, var12, 2.5F, b);
        		if (fx != null)
        		{
        			var14.effectRenderer.addEffect(fx);
        		}
        	}
            if (var1.equals("hugeexplosion2"))
            {
                EntityFX fx = new EntityHugeExplodeFX(var14.theWorld, var2, var4, var6, var8, var10, var12);
        		if (fx != null)
        		{
        			var14.effectRenderer.addEffect(fx);
        		}
            }
            else if (var1.equals("largeexplode2"))
            {
                EntityFX fx = new EntityLargeExplodeFX(var14.renderEngine, var14.theWorld, var2, var4, var6, var8, var10, var12);
        		if (fx != null)
        		{
        			var14.effectRenderer.addEffect(fx);
        		}
            }

            if (var15 * var15 + var17 * var17 + var19 * var19 < var22 * var22)
            {
            	if (var1.equals("sludgeDrip"))
            	{
            		var21 = new GCEntityDropParticleFX(var14.theWorld, var2, var4, var6, GCBlocks.bacterialSludge);
            	}
            }
            
            if (var21 != null)
            {
                ((EntityFX)var21).prevPosX = ((EntityFX)var21).posX;
                ((EntityFX)var21).prevPosY = ((EntityFX)var21).posY;
                ((EntityFX)var21).prevPosZ = ((EntityFX)var21).posZ;
                var14.effectRenderer.addEffect((EntityFX)var21);
            }
        }
    }
	
    public class ClientPacketHandler implements IPacketHandler
    {
        @Override
        public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            int packetType = GCUtil.readPacketID(data);
            EntityPlayer player = (EntityPlayer)p;
            
            if (packetType == 0)
            {
                Class[] decodeAs = {Integer.class};
                Object[] packetReadout = GCUtil.readPacketData(data, decodeAs);

                TickHandlerClient.airRemaining = (Integer) packetReadout[0];
            }

            if (packetType == 1)
            {
                Class[] decodeAs = {Integer.class};
                Object[] packetReadout = GCUtil.readPacketData(data, decodeAs);

            	TickHandlerClient.dimension = (Integer) packetReadout[0];
            }
        }
    }
    
    public static boolean handleBacterialMovement(EntityPlayer player)
    {
        return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), GCBlocks.bacterialSludge);
    }
    
    public static boolean handleLavaMovement(EntityPlayer player)
    {
        return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
    }
    
    public static boolean handleWaterMovement(EntityPlayer player)
    {
        return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.water);
    }
    
    public static boolean handleLiquidMovement(EntityPlayer player)
    {
    	return (handleBacterialMovement(player) || handleLavaMovement(player) || handleWaterMovement(player));
    }
    
    public static class TickHandlerClient implements ITickHandler
    {
    	public static int dimension;
    	
    	public static int airRemaining;
    	
    	@Override
    	public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
    		ClientProxy.getCurrentTime = System.currentTimeMillis();
    		
    		Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            WorldClient world = minecraft.theWorld;
            
            EntityClientPlayerMP player = minecraft.thePlayer;
    		
    		if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
    			if (player != null && player.ridingEntity != null && minecraft.gameSettings.keyBindJump.pressed)
    			{
    	    		Object[] toSend = {0};
    	            PacketDispatcher.sendPacketToServer(GCUtil.createPacket("Galacticraft", 3, toSend));
    			}
    			
    			if (world != null && world.provider instanceof GCWorldProvider)
    			{
    				if (world.provider.getSkyProvider() == null)
                    {
    					world.provider.setSkyProvider(new GCSkyProvider());
                    }
    			}
    			
    			if (player != null && dimension == GCConfigManager.dimensionIDMars && !player.capabilities.isFlying && !minecraft.isGamePaused && !handleLiquidMovement(player)) 
    			{
    				player.motionY = player.motionY + 0.062;
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
            
            if (GCChunkProvider.giantCaveLocations != null)
    		
    		if (player != null && player.inventory.armorItemInSlot(3) != null)
    		{
    			helmetSlot = player.inventory.armorItemInSlot(3);
    		}
            
    		if (type.equals(EnumSet.of(TickType.RENDER)))
            {
        		if (helmetSlot != null && helmetSlot.getItem() instanceof GCItemSensorGlasses && minecraft.currentScreen == null)
        		{
        			i++;
        			
        	        float f = MathHelper.sin(((float)i) / 80.0F) * 0.1F + 0.1F;
        			
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
    				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/micdoodle8/mods/galacticraft/client/gui/hud.png"));
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

        		if (dimension == GCConfigManager.dimensionIDMars)
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
    				GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/micdoodle8/mods/galacticraft/client/gui/gui.png"));
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
    	
    	public void requestRespawn(EntityPlayerSP player)
    	{
    		Object[] toSend = {player.username};
            PacketDispatcher.sendPacketToServer(GCUtil.createPacket("Galacticraft", 1, toSend));
    	}
    }
    
    public static class GCKeyHandler extends KeyHandler
    {
    	static KeyBinding tankRefill = new KeyBinding("Tank Refill", Keyboard.KEY_H);

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
                    PacketDispatcher.sendPacketToServer(GCUtil.createPacket("Galacticraft", 0, toSend));
            	    player.openGui(Galacticraft.instance, GCConfigManager.idGuiTankRefill, minecraft.theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
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
