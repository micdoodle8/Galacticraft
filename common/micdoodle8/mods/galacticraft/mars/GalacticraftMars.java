package micdoodle8.mods.galacticraft.mars;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import micdoodle8.mods.galacticraft.API.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.core.GCCoreCreativeTab;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCoreConnectionHandler;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.dimension.GCMarsWorldProvider;
import micdoodle8.mods.galacticraft.mars.entities.GCCoreEntityRocketT2;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityCreeperBoss;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityProjectileTNT;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySludgeling;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonTeleportType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.prefab.TranslationHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
@Mod(name = GalacticraftMars.NAME, version = GalacticraftMars.LOCALMAJVERSION + "." + GalacticraftMars.LOCALMINVERSION + "." + GalacticraftMars.LOCALBUILDVERSION, useMetadata = true, modid = GalacticraftMars.MODID, dependencies = "required-after:Forge@[7.8.0.685,)")
@NetworkMod(channels = { GalacticraftMars.CHANNEL }, clientSideRequired = true, serverSideRequired = false, connectionHandler = GCCoreConnectionHandler.class, packetHandler = GCCorePacketManager.class)
public class GalacticraftMars implements IGalacticraftSubMod
{
    public static final String NAME = "Galacticraft Mars";
    public static final String MODID = "GalacticraftMars";
    public static final String CHANNEL = "GalacticraftMars";
    public static final String CHANNELENTITIES = "GCMarsEntities";

    public static final String FILE_PATH = "/micdoodle8/mods/galacticraft/mars/";
    public static final String CLIENT_PATH = "client/";
    public static final String LANGUAGE_PATH = GalacticraftMars.FILE_PATH + GalacticraftMars.CLIENT_PATH + "lang/";
    private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "fi_FI" };

    public static final int LOCALMAJVERSION = 0;
    public static final int LOCALMINVERSION = 1;
    public static final int LOCALBUILDVERSION = 0;

    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.mars.client.ClientProxyMars", serverSide = "micdoodle8.mods.galacticraft.mars.CommonProxyMars")
    public static CommonProxyMars proxy;

    @Instance(GalacticraftMars.MODID)
    public static GalacticraftMars instance;

    public static GCCoreCreativeTab galacticraftMarsTab;

    public static String MARS_TEXTURE_PREFIX = "galacticraftmars:";

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        GalacticraftCore.registerSubMod(this);

        new GCMarsConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/mars.conf"));

        GCMarsBlocks.initBlocks();
        GCMarsBlocks.registerBlocks();
        GCMarsBlocks.setHarvestLevels();

        GCMarsItems.initItems();

        GalacticraftMars.proxy.preInit(event);
    }

    @Init
    public void load(FMLInitializationEvent event)
    {
        System.out.println("Galacticraft Mars Loaded: " + TranslationHelper.loadLanguages(GalacticraftMars.LANGUAGE_PATH, GalacticraftMars.LANGUAGES_SUPPORTED) + " Languages.");
        GalacticraftMars.galacticraftMarsTab = new GCCoreCreativeTab(CreativeTabs.getNextID(), GalacticraftMars.MODID, GCMarsItems.spaceship.itemID, 5);
        DimensionManager.registerProviderType(GCMarsConfigManager.dimensionIDMars, GCMarsWorldProvider.class, false);
        MinecraftForge.EVENT_BUS.register(new GCMarsEvents());
        GalacticraftRegistry.registerTeleportType(GCMarsWorldProvider.class, new GCMoonTeleportType());
        GCMarsUtil.addSmeltingRecipes();
        this.registerTileEntities();
        this.registerCreatures();
        this.registerOtherEntities();
        GalacticraftMars.proxy.init(event);
    }

    @ServerStarting
    public void serverStarting(FMLServerStartingEvent event)
    {
        WorldUtil.registerPlanet(GCMarsConfigManager.dimensionIDMars, true);
    }

    public void registerTileEntities()
    {

    }

    public void registerCreatures()
    {
        this.registerGalacticraftCreature(GCMarsEntityCreeperBoss.class, "Creeper Boss", GCMarsConfigManager.idEntityCreeperBoss, 894731, 0);
        this.registerGalacticraftCreature(GCMarsEntitySludgeling.class, "Sludgeling", GCMarsConfigManager.idEntitySludgeling, 25600, 0);
    }

    public void registerOtherEntities()
    {
        this.registerGalacticraftNonMobEntity(GCMarsEntityProjectileTNT.class, "Projectile TNT", GCMarsConfigManager.idEntityProjectileTNT, 150, 5, true);
        this.registerGalacticraftNonMobEntity(GCCoreEntityRocketT2.class, "SpaceshipT2", GCMarsConfigManager.idEntitySpaceshipTier2, 150, 1, true);
    }

    @PostInit
    public void postLoad(FMLPostInitializationEvent event)
    {
        GalacticraftMars.proxy.postInit(event);
        GalacticraftMars.proxy.registerRenderInformation();
        GCMarsUtil.addCraftingRecipes();
    }

    @ServerStarted
    public void serverInit(FMLServerStartedEvent event)
    {
        NetworkRegistry.instance().registerChannel(new ServerPacketHandler(), "GalacticraftMars", Side.SERVER);
    }

    public void registerGalacticraftCreature(Class var0, String var1, int id, int back, int fore)
    {
        EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
        EntityRegistry.registerModEntity(var0, var1, id, GalacticraftMars.instance, 80, 3, true);
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
        return "Mars";
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
