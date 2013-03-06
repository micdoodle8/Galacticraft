package micdoodle8.mods.galacticraft.saturn.client;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.enceladus.client.ClientProxyEnceladus;
import micdoodle8.mods.galacticraft.enceladus.client.GCEnceladusMapPlanet;
import micdoodle8.mods.galacticraft.mimas.client.ClientProxyMimas;
import micdoodle8.mods.galacticraft.mimas.client.GCMimasMapPlanet;
import micdoodle8.mods.galacticraft.saturn.CommonProxySaturn;
import micdoodle8.mods.galacticraft.titan.client.ClientProxyTitan;
import micdoodle8.mods.galacticraft.titan.client.GCTitanMapPlanet;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class ClientProxySaturn extends CommonProxySaturn implements IGalacticraftSubModClient
{
	public static GCCoreLocalization lang;
	
	public static ClientProxyTitan moonClientTitan = new ClientProxyTitan();
	public static ClientProxyEnceladus moonClientEnceladus = new ClientProxyEnceladus();
	public static ClientProxyMimas moonClientMimas = new ClientProxyMimas();
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		ClientProxySaturn.moonClientTitan.preInit(event);
		ClientProxySaturn.moonClientEnceladus.preInit(event);
		ClientProxySaturn.moonClientMimas.preInit(event);
		ClientProxySaturn.lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/saturn/client");
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		ClientProxySaturn.moonClientTitan.init(event);
		ClientProxySaturn.moonClientEnceladus.init(event);
		ClientProxySaturn.moonClientMimas.init(event);
		GalacticraftCore.registerClientSubMod(this);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		ClientProxySaturn.moonClientTitan.postInit(event);
		ClientProxySaturn.moonClientEnceladus.postInit(event);
		ClientProxySaturn.moonClientMimas.postInit(event);
	}
	
	@Override
	public void registerRenderInformation()
	{
		ClientProxySaturn.moonClientTitan.registerRenderInformation();
		ClientProxySaturn.moonClientEnceladus.registerRenderInformation();
		ClientProxySaturn.moonClientMimas.registerRenderInformation();
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
            return "Galacticraft Saturn Client";
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
		return "Saturn";
	}

	@Override
	public String getPlanetSpriteDirectory()
	{
		return "/micdoodle8/mods/galacticraft/saturn/client/planets/";
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer()
	{
		return new GCSaturnSlotRenderer();
	}
	
	private final IMapPlanet saturn = new GCSaturnMapPlanet();

	@Override
	public IMapPlanet getPlanetForMap()
	{
		return this.saturn;
	}

	@Override
	public IMapPlanet[] getChildMapPlanets()
	{
		final IMapPlanet[] moonMapPlanet = {new GCTitanMapPlanet(), new GCEnceladusMapPlanet(), new GCMimasMapPlanet()};
		
		return moonMapPlanet;
	}

	@Override
	public String getPathToMusicFile()
	{
		return null;
	}
}
