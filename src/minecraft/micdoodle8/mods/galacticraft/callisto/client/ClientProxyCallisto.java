package micdoodle8.mods.galacticraft.callisto.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.EnumSet;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.callisto.CommonProxyCallisto;
import micdoodle8.mods.galacticraft.callisto.blocks.GCCallistoBlocks;
import micdoodle8.mods.galacticraft.callisto.dimension.GCCallistoWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class ClientProxyCallisto extends CommonProxyCallisto implements IGalacticraftSubModClient
{
	public static long getFirstBootTime;
	public static long getCurrentTime;
	private final Random rand = new Random();
	
	public static GCCoreLocalization lang;
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		ClientProxyCallisto.lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/callisto/client");
		MinecraftForge.EVENT_BUS.register(new GCCallistoSounds());
		ClientProxyCallisto.getFirstBootTime = System.currentTimeMillis();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		GalacticraftCore.registerClientSubMod(this);
		TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
        NetworkRegistry.instance().registerChannel(new ClientPacketHandler(), "GcCal", Side.CLIENT);
        GalacticraftCore.proxy.addSlotRenderer(new GCCallistoSlotRenderer());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		GCCallistoBlocks.addNames();
//		GCTitanItems.addNames(); TODO
	}
	
	@Override
	public void registerRenderInformation()
	{
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/callisto/client/blocks/callisto.png");
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/callisto/client/items/callisto.png");
	}

	@SuppressWarnings("unused")
	@Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
    {
        final Minecraft var14 = FMLClientHandler.instance().getClient();

        if (var14 != null && var14.renderViewEntity != null && var14.effectRenderer != null)
        {
            final double var15 = var14.renderViewEntity.posX - var2;
            final double var17 = var14.renderViewEntity.posY - var4;
            final double var19 = var14.renderViewEntity.posZ - var6;
            final Object var21 = null;
            final double var22 = 64.0D;

            if (var15 * var15 + var17 * var17 + var19 * var19 < var22 * var22)
            {
            	if (var1.equals("nothing"))
            	{
            		// spawn nothing
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
        public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            final int packetType = PacketUtil.readPacketID(data);
            final EntityPlayer player = (EntityPlayer)p;
            
            if (packetType == 0)
            {
            	
            }
        }
    }
    
    public static class TickHandlerClient implements ITickHandler
    {
    	@Override
    	public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
    		ClientProxyCallisto.getCurrentTime = System.currentTimeMillis();
    		
    		final Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            final WorldClient world = minecraft.theWorld;
            
            final EntityClientPlayerMP player = minecraft.thePlayer;
    		
    		if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
    			if (world != null && world.provider instanceof GCCallistoWorldProvider)
    			{
    				if (world.provider.getSkyRenderer() == null)
                    {
    					world.provider.setSkyRenderer(new GCCallistoSkyProvider());
                    }
    			}
            }
        }

    	@Override
    	public void tickEnd(EnumSet<TickType> type, Object... tickData)
    	{
    	}
    	
        @Override
		public String getLabel()
        {
            return "Galacticraft Callisto Client";
        }

    	@Override
    	public EnumSet<TickType> ticks()
    	{
    		return EnumSet.of(TickType.CLIENT);
    	}
    }

	@Override
	public String getDimensionName()
	{
		return "Callisto";
	}

	@Override
	public GCCoreLocalization getLanguageFile()
	{
		return ClientProxyCallisto.lang;
	}

	@Override
	public String getPlanetSpriteDirectory()
	{
		return "/micdoodle8/mods/galacticraft/callisto/client/planets/";
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer()
	{
		return new GCCallistoSlotRenderer();
	}

	@Override
	public IMapPlanet getPlanetForMap()
	{
		return null;
	}

	@Override
	public IMapPlanet[] getChildMapPlanets()
	{
		return null;
	}

	@Override
	public String getPathToMusicFile()
	{
		return null;
	}
}
