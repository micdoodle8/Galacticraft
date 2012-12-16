package micdoodle8.mods.galacticraft.jupiter.client;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.europa.client.ClientProxyEuropa;
import micdoodle8.mods.galacticraft.europa.client.GCEuropaSlotRenderer;
import micdoodle8.mods.galacticraft.io.client.ClientProxyIo;
import micdoodle8.mods.galacticraft.jupiter.CommonProxyJupiter;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class ClientProxyJupiter extends CommonProxyJupiter implements IGalacticraftSubModClient
{
	public static GCCoreLocalization lang;
	
	public static ClientProxyIo moonClientIo = new ClientProxyIo();
	public static ClientProxyEuropa moonClientEuropa = new ClientProxyEuropa();
	
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		moonClientIo.preInit(event);
		moonClientEuropa.preInit(event);
		this.lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/jupiter/client");
	}

	@Override
	public void init(FMLInitializationEvent event) 
	{
		moonClientIo.init(event);
		moonClientEuropa.init(event);
		GalacticraftCore.registerClientSubMod(this);
		GalacticraftCore.addMapPlanet(new GCJupiterMapPlanet());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
		moonClientIo.postInit(event);
		moonClientEuropa.postInit(event);
	}
	
	@Override
	public void registerRenderInformation() 
	{
	}

	@Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
    {
    }
	
    public class ClientPacketHandler implements IPacketHandler
    {
		@Override
		public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
		{
			
		}
    }
    
    public static class TickHandlerClient implements ITickHandler
    {
    	@Override
    	public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
    		
        }

    	@Override
    	public void tickEnd(EnumSet<TickType> type, Object... tickData) 
    	{
    	}
    	
        @Override
		public String getLabel()
        {
            return "Galacticraft Jupiter Client";
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
		return "Jupiter";
	}

	@Override
	public GCCoreLocalization getLanguageFile() 
	{
		return this.lang;
	}

	@Override
	public String getPlanetSpriteDirectory() 
	{
		return "/micdoodle8/mods/galacticraft/jupiter/client/planets/";
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCJupiterSlotRenderer();
	}
}
