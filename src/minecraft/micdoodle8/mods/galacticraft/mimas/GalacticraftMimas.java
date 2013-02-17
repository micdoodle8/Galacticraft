package micdoodle8.mods.galacticraft.mimas;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mimas.blocks.GCMimasBlocks;
import micdoodle8.mods.galacticraft.mimas.dimension.GCMimasWorldProvider;
import micdoodle8.mods.galacticraft.saturn.GalacticraftSaturn;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GalacticraftMimas implements IGalacticraftSubMod
{
	public static List mimasPlayers = new ArrayList();
	public static List gcMimasPlayers = new ArrayList();

	public void preInit(FMLPreInitializationEvent event)
	{
		GalacticraftCore.registerSubMod(this);

		new GCMimasConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/mimas.conf"));
		
		GCMimasBlocks.initBlocks();
		GCMimasBlocks.registerBlocks();
		GCMimasBlocks.setHarvestLevels();
//
//		GCMarsItems.initItems();
//		GCMarsItems.addNames(); TODO
	}
	
	public void init(FMLInitializationEvent event)
	{
		DimensionManager.registerProviderType(GCMimasConfigManager.dimensionIDMimas, GCMimasWorldProvider.class, false);
		DimensionManager.registerDimension(GCMimasConfigManager.dimensionIDMimas, GCMimasConfigManager.dimensionIDMimas);
//		MinecraftForge.EVENT_BUS.register(new GCMarsEvents());
//		GameRegistry.registerPlayerTracker(new GCMarsPlayerHandler());
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
	}
	
	public void registerTileEntities()
	{
		
	}
	
	public void registerCreatures()
	{
	}
	
	public void registerOtherEntities()
	{
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
//		GCMarsAchievementList.initAchievs();
	}
	
	public void serverInit(FMLServerStartedEvent event)
	{
        NetworkRegistry.instance().registerChannel(new ServerPacketHandler(), "GcMim", Side.SERVER);
	}

    public void registerGalacticraftCreature(Class var0, String var1, int id, int back, int fore)
    {
    	EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
        EntityRegistry.registerModEntity(var0, var1, id, GalacticraftSaturn.moonMimas, 80, 3, true);
		LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", "en_US", var1);
    }
    
    public void registerGalacticraftNonMobEntity(Class var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
    {
        EntityRegistry.registerModEntity(var0, var1, id, this, trackingDistance, updateFreq, sendVel);
    }
	
    public class ServerPacketHandler implements IPacketHandler
    {
        @Override
        public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            final int packetType = GCCoreUtil.readPacketID(data);
            final EntityPlayerMP player = (EntityPlayerMP)p;
            
            if (packetType == 0)
            {
            	
            }
        }
    }

	@Override
	public String getDimensionName()
	{
		return "Mimas";
	}

	@Override
	public boolean reachableDestination()
	{
		return true;
	}

	@Override
	public IGalaxy getParentGalaxy()
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
