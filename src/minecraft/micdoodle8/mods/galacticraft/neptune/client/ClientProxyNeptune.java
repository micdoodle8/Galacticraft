package micdoodle8.mods.galacticraft.neptune.client;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.neptune.CommonProxyNeptune;
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
public class ClientProxyNeptune extends CommonProxyNeptune implements IGalacticraftSubModClient
{
	public static GCCoreLocalization lang;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		this.lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/neptune/client");
	}

	@Override
	public void init(FMLInitializationEvent event) 
	{
		GalacticraftCore.registerClientSubMod(this);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
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
            return "Galacticraft Neptune Client";
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
		return "Neptune";
	}

	@Override
	public GCCoreLocalization getLanguageFile() 
	{
		return this.lang;
	}

	@Override
	public String getPlanetSpriteDirectory() 
	{
		return "/micdoodle8/mods/galacticraft/neptune/client/planets/";
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCNeptuneSlotRenderer();
	}

	@Override
	public IMapPlanet getPlanetForMap() 
	{
		return new GCNeptuneMapPlanet();
	}
}
