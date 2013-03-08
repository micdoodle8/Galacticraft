package micdoodle8.mods.galacticraft.callisto;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.callisto.blocks.GCCallistoBlocks;
import micdoodle8.mods.galacticraft.callisto.dimension.GCCallistoWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.jupiter.GalacticraftJupiter;
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
public class GalacticraftCallisto implements IGalacticraftSubMod
{
	public static List callistoPlayers = new ArrayList();
	public static List gcCallistoPlayers = new ArrayList();

	public void preInit(FMLPreInitializationEvent event)
	{
		GalacticraftCore.registerSubMod(this);

		new GCCallistoConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/callisto.conf"));

		GCCallistoBlocks.initBlocks();
		GCCallistoBlocks.registerBlocks();
		GCCallistoBlocks.setHarvestLevels();
//
//		GCMarsItems.initItems();
//		GCMarsItems.addNames(); TODO
	}

	public void init(FMLInitializationEvent event)
	{
		DimensionManager.registerProviderType(GCCallistoConfigManager.dimensionIDCallisto, GCCallistoWorldProvider.class, false);
		DimensionManager.registerDimension(GCCallistoConfigManager.dimensionIDCallisto, GCCallistoConfigManager.dimensionIDCallisto);
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
        NetworkRegistry.instance().registerChannel(new ServerPacketHandler(), "GcCal", Side.SERVER);
	}

    public void registerGalacticraftCreature(Class var0, String var1, int id, int back, int fore)
    {
    	EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
        EntityRegistry.registerModEntity(var0, var1, id, GalacticraftJupiter.moonCallisto, 80, 3, true);
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
            final int packetType = PacketUtil.readPacketID(data);
            if (packetType == 0)
            {

            }
        }
    }

	@Override
	public String getDimensionName()
	{
		return "Callisto";
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
