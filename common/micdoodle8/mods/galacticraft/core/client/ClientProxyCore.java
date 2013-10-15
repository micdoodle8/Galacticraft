package micdoodle8.mods.galacticraft.core.client;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.world.ICelestialBody;
import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
import micdoodle8.mods.galacticraft.api.world.IMoon;
import micdoodle8.mods.galacticraft.api.world.IPlanet;
import micdoodle8.mods.galacticraft.core.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchFlameFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchSmokeFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityOxygenFX;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirCollector;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirCompressor;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirDistributor;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirSealer;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiCargoLoader;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiCargoUnloader;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiCircuitFabricator;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiCoalGenerator;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiElectricFurnace;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiExtendedInventory;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiFuelLoader;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiGalaxyMap;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiIngotCompressor;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiManual;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiParachest;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiRefinery;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiRocketRefill;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiSolar;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreInventoryTabGalacticraft;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreBlankPage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreBlockCastPage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreContentsTablePage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreCraftingPage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreFurnacePage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCorePicturePage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreSectionPage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreSidebarPage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreTextPage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreTitlePage;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreToolPage;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererBreathableAir;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererCraftingTable;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererLandingPad;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererMeteor;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererOxygenPipe;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererTreasureChest;
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderAlienVillager;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderArrow;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderBuggy;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderCreeper;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderFlag;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderLander;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderMeteor;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderMeteorChunk;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderOxygenBubble;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderParaChest;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderPlayer;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSkeleton;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSkeletonBoss;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderSpider;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderZombie;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererBuggy;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererFlag;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererKey;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererMeteorChunk;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererSpaceship;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreRenderAluminumWire;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityAdvancedCraftingTableRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityParachestRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntitySolarPanelRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.GCCoreTileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSounds;
import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.entities.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityControllable;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteorChunk;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityOxygenBubble;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryExtended;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAdvancedCraftingTable;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityParachest;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySolar;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.EnumHelperClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;
import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class ClientProxyCore extends CommonProxyCore
{
    private static int treasureChestRenderID;
    private static int torchRenderID;
    private static int breathableAirRenderID;
    private static int oxygenPipeRenderID;
    private static int meteorRenderID;
    private static int craftingTableID;
    private static int fullLandingPadRenderID;
    private static int titaniumArmorRenderIndex;
    private static int sensorArmorRenderIndex;
    public static long getFirstBootTime;
    public static long getCurrentTime;
    public static long slowTick;
    public static ClientProxyMoon moon = new ClientProxyMoon();
    public static List<ICelestialBodyRenderer> slotRenderers = new ArrayList<ICelestialBodyRenderer>();
    public static List<int[]> valueableBlocks = new ArrayList<int[]>();

    public static Set<String> playersUsingParachutes = new HashSet<String>();
    public static HashMap<String, ResourceLocation> parachuteTextures = new HashMap<String, ResourceLocation>();
    public static Set<String> playersWithFrequencyModule = new HashSet<String>();
    public static Set<String> playersWithOxygenMask = new HashSet<String>();
    public static Set<String> playersWithOxygenGear = new HashSet<String>();
    public static Set<String> playersWithOxygenTankLeftRed = new HashSet<String>();
    public static Set<String> playersWithOxygenTankLeftOrange = new HashSet<String>();
    public static Set<String> playersWithOxygenTankLeftGreen = new HashSet<String>();
    public static Set<String> playersWithOxygenTankRightRed = new HashSet<String>();
    public static Set<String> playersWithOxygenTankRightOrange = new HashSet<String>();
    public static Set<String> playersWithOxygenTankRightGreen = new HashSet<String>();

    public static double playerPosX;
    public static double playerPosY;
    public static double playerPosZ;
    public static float playerRotationYaw;
    public static float playerRotationPitch;

    public static int clientSpaceStationID = 0;

    public static ArrayList<SoundPoolEntry> newMusic = new ArrayList<SoundPoolEntry>();

    public static EnumRarity galacticraftItem = EnumHelperClient.addRarity("GCRarity", 9, "Space");

    public static Map<String, String> capeMap = new HashMap<String, String>();

    public static GCCoreInventoryExtended dummyInventory = new GCCoreInventoryExtended();

    public static Document materialsTest;

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        ClientProxyCore.moon.preInit(event);

        MinecraftForge.EVENT_BUS.register(new GCCoreSounds());
        ClientProxyCore.getFirstBootTime = System.currentTimeMillis();

        ClientProxyCore.sensorArmorRenderIndex = RenderingRegistry.addNewArmourRendererPrefix("sensor");
        ClientProxyCore.titaniumArmorRenderIndex = RenderingRegistry.addNewArmourRendererPrefix("titanium");
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        ClientProxyCore.moon.init(event);

        TickRegistry.registerTickHandler(new GCCoreTickHandlerClient(), Side.CLIENT);
        NetworkRegistry.instance().registerChannel(new GCCorePacketHandlerClient(), GalacticraftCore.CHANNEL, Side.CLIENT);

        GCCoreManualUtil.registerManualPage("crafting", GCCoreCraftingPage.class);
        GCCoreManualUtil.registerManualPage("picture", GCCorePicturePage.class);
        GCCoreManualUtil.registerManualPage("text", GCCoreTextPage.class);
        GCCoreManualUtil.registerManualPage("intro", GCCoreTextPage.class);
        GCCoreManualUtil.registerManualPage("sectionpage", GCCoreSectionPage.class);
        GCCoreManualUtil.registerManualPage("intro", GCCoreTitlePage.class);
        GCCoreManualUtil.registerManualPage("contents", GCCoreContentsTablePage.class);
        GCCoreManualUtil.registerManualPage("furnace", GCCoreFurnacePage.class);
        GCCoreManualUtil.registerManualPage("sidebar", GCCoreSidebarPage.class);
        GCCoreManualUtil.registerManualPage("toolpage", GCCoreToolPage.class);
        GCCoreManualUtil.registerManualPage("blockcast", GCCoreBlockCastPage.class);
        GCCoreManualUtil.registerManualPage("blank", GCCoreBlankPage.class);

        GCCoreManualUtil.registerIcon("heavyplatingT1", new ItemStack(GCCoreItems.heavyPlatingTier1));
        GCCoreManualUtil.registerIcon("oxygenmask", new ItemStack(GCCoreItems.oxMask));
        GCCoreManualUtil.registerIcon("oxygenTankHeavy", new ItemStack(GCCoreItems.oxTankHeavy));
        GCCoreManualUtil.registerIcon("rocketT1", new ItemStack(GCCoreItems.rocketTier1));

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        ClientProxyCore.materialsTest = GCCoreManualUtil.readManual("/assets/galacticraftcore/manuals/gettingstarted.xml", docBuilderFactory);

        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityAluminumWire.class, new GCCoreRenderAluminumWire());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityTreasureChest.class, new GCCoreTileEntityTreasureChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityParachest.class, new GCCoreTileEntityParachestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityAdvancedCraftingTable.class, new GCCoreTileEntityAdvancedCraftingTableRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntitySolar.class, new GCCoreTileEntitySolarPanelRenderer());
        ClientProxyCore.treasureChestRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererTreasureChest(ClientProxyCore.treasureChestRenderID));
        ClientProxyCore.torchRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererUnlitTorch(ClientProxyCore.torchRenderID));
        ClientProxyCore.breathableAirRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererBreathableAir(ClientProxyCore.breathableAirRenderID));
        ClientProxyCore.oxygenPipeRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererOxygenPipe(ClientProxyCore.oxygenPipeRenderID));
        ClientProxyCore.meteorRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererMeteor(ClientProxyCore.meteorRenderID));
        ClientProxyCore.craftingTableID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererCraftingTable(ClientProxyCore.craftingTableID));
        ClientProxyCore.fullLandingPadRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererLandingPad(ClientProxyCore.fullLandingPadRenderID));

        String capeString = "https://dl.dropboxusercontent.com/s/zmhn8i0w1v152ei/cape.png?token_hash=AAFuDqDVxs_z9SK3h3DgrOp8W9SFiS-9-VFxapHsbCs4wA&dl=1";
        ClientProxyCore.capeMap.put("JTE", capeString);
        ClientProxyCore.capeMap.put("ajmski", capeString);
        ClientProxyCore.capeMap.put("Azeryuu", capeString);
        ClientProxyCore.capeMap.put("bob10234", capeString);
        ClientProxyCore.capeMap.put("crazybob84", capeString);
        ClientProxyCore.capeMap.put("CrimsonKMR", capeString);
        ClientProxyCore.capeMap.put("_lime", capeString);
        ClientProxyCore.capeMap.put("Hachipatas", capeString);
        ClientProxyCore.capeMap.put("Happypancakes56", capeString);
        ClientProxyCore.capeMap.put("hosker666", capeString);
        ClientProxyCore.capeMap.put("iTyroul", capeString);
        ClientProxyCore.capeMap.put("kingdonflon", capeString);
        ClientProxyCore.capeMap.put("kungfu_dragon", capeString);
        ClientProxyCore.capeMap.put("Lewis_McReu", capeString);
        ClientProxyCore.capeMap.put("mrgreaper", capeString);
        ClientProxyCore.capeMap.put("NukeMaster2009", capeString);
        ClientProxyCore.capeMap.put("odriew", capeString);
        ClientProxyCore.capeMap.put("PureTryOut", capeString);
        ClientProxyCore.capeMap.put("ramenators", capeString);
        ClientProxyCore.capeMap.put("micdoodle8", capeString);
        ClientProxyCore.capeMap.put("randhope21", capeString);
        ClientProxyCore.capeMap.put("smates", capeString);
        ClientProxyCore.capeMap.put("SoaringChris137", capeString);
        ClientProxyCore.capeMap.put("TeisAngel", capeString);
        ClientProxyCore.capeMap.put("TerraGenome", capeString);
        ClientProxyCore.capeMap.put("X_angelz_X", capeString);
        ClientProxyCore.capeMap.put("Yangjo123", capeString);
        ClientProxyCore.capeMap.put("Made_This_Name", capeString);
        ClientProxyCore.capeMap.put("spamenigma", capeString);

        if (Loader.isModLoaded("CoFHCore"))
        {
            // Add to CoFH cape registry as well
            for (Entry<String, String> e : ClientProxyCore.capeMap.entrySet())
            {
                try
                {
                    Object capeRegistry = Class.forName("cofh.api.core.RegistryAccess").getField("capeRegistry").get(null);
                    Class.forName("cofh.api.core.ISimpleRegistry").getMethod("register", String.class, String.class).invoke(capeRegistry, e.getKey(), e.getValue());
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                    break;
                }
            }
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        ClientProxyCore.moon.postInit(event);

        if (!Loader.isModLoaded("TConstruct") || TabRegistry.getTabList().size() < 3)
        {
            TabRegistry.registerTab(new InventoryTabVanilla());
        }

        TabRegistry.registerTab(new GCCoreInventoryTabGalacticraft());

        for (ICelestialBody celestialObject : GalacticraftRegistry.getCelestialBodies())
        {
            if (celestialObject.getMapObject() != null && celestialObject instanceof IPlanet)
            {
                GalacticraftCore.mapPlanets.add((IPlanet) celestialObject);
                GalacticraftCore.mapMoons.put((IPlanet) celestialObject, new ArrayList<IMoon>());
            }
        }

        for (ICelestialBody celestialObject : GalacticraftRegistry.getCelestialBodies())
        {
            if (celestialObject.getMapObject() != null && celestialObject instanceof IMoon)
            {
                ArrayList<IMoon> list = GalacticraftCore.mapMoons.get(((IMoon) celestialObject).getParentPlanet());

                list.add((IMoon) celestialObject);

                GalacticraftCore.mapMoons.put(((IMoon) celestialObject).getParentPlanet(), list);
            }
        }

        KeyBindingRegistry.registerKeyBinding(new GCKeyHandler());
    }

    @Override
    public void registerRenderInformation()
    {
        ClientProxyCore.moon.registerRenderInformation();

        IModelCustom modelBuggy = AdvancedModelLoader.loadModel("/assets/galacticraftcore/models/buggy.obj");
        IModelCustom modelBuggyWheelRight = AdvancedModelLoader.loadModel("/assets/galacticraftcore/models/buggyWheelRight.obj");
        IModelCustom modelBuggyWheelLeft = AdvancedModelLoader.loadModel("/assets/galacticraftcore/models/buggyWheelLeft.obj");
        IModelCustom meteorChunkModel = AdvancedModelLoader.loadModel("/assets/galacticraftcore/models/meteorChunk.obj");

        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityRocketT1.class, new GCCoreRenderSpaceship(new GCCoreModelSpaceship(), GalacticraftCore.TEXTURE_DOMAIN, "rocketT1"));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpider.class, new GCCoreRenderSpider());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityZombie.class, new GCCoreRenderZombie());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityCreeper.class, new GCCoreRenderCreeper());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySkeleton.class, new GCCoreRenderSkeleton());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySkeletonBoss.class, new GCCoreRenderSkeletonBoss());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityMeteor.class, new GCCoreRenderMeteor());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityBuggy.class, new GCCoreRenderBuggy(modelBuggy, modelBuggyWheelRight, modelBuggyWheelLeft));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityMeteorChunk.class, new GCCoreRenderMeteorChunk(meteorChunkModel));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityFlag.class, new GCCoreRenderFlag());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityParaChest.class, new GCCoreRenderParaChest());
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new GCCoreRenderPlayer());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityAlienVillager.class, new GCCoreRenderAlienVillager());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityOxygenBubble.class, new GCCoreRenderOxygenBubble(0.25F, 0.25F, 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityLander.class, new GCCoreRenderLander());
        RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityArrow.class, new GCCoreRenderArrow());
        MinecraftForgeClient.registerItemRenderer(GCCoreBlocks.unlitTorch.blockID, new GCCoreItemRendererUnlitTorch());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.rocketTier1.itemID, new GCCoreItemRendererSpaceship(new GCCoreEntityRocketT1(FMLClientHandler.instance().getClient().theWorld), new GCCoreModelSpaceship(), new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/model/rocketT1.png")));
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.buggy.itemID, new GCCoreItemRendererBuggy(modelBuggy, modelBuggyWheelRight, modelBuggyWheelLeft));
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.flag.itemID, new GCCoreItemRendererFlag());
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.key.itemID, new GCCoreItemRendererKey(new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/model/treasure.png")));
        MinecraftForgeClient.registerItemRenderer(GCCoreItems.meteorChunk.itemID, new GCCoreItemRendererMeteorChunk(meteorChunkModel));
    }

    public static void renderPlanets(float par3)
    {
    }

    @Override
    public void addSlotRenderer(ICelestialBodyRenderer slotRenderer)
    {
        ClientProxyCore.slotRenderers.add(slotRenderer);
    }

    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public int getGCTreasureChestRenderID()
    {
        return ClientProxyCore.treasureChestRenderID;
    }

    @Override
    public int getGCUnlitTorchRenderID()
    {
        return ClientProxyCore.torchRenderID;
    }

    @Override
    public int getGCBreathableAirRenderID()
    {
        return ClientProxyCore.breathableAirRenderID;
    }

    @Override
    public int getGCOxygenPipeRenderID()
    {
        return ClientProxyCore.oxygenPipeRenderID;
    }

    @Override
    public int getGCMeteorRenderID()
    {
        return ClientProxyCore.meteorRenderID;
    }

    @Override
    public int getGCCraftingTableRenderID()
    {
        return ClientProxyCore.craftingTableID;
    }

    @Override
    public int getGCFullLandingPadRenderID()
    {
        return ClientProxyCore.fullLandingPadRenderID;
    }

    @Override
    public int getTitaniumArmorRenderIndex()
    {
        return ClientProxyCore.titaniumArmorRenderIndex;
    }

    @Override
    public int getSensorArmorRenderIndex()
    {
        return ClientProxyCore.sensorArmorRenderIndex;
    }

    @Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
    {
        this.spawnParticle(var1, var2, var4, var6, var8, var10, var12, 0.0D, 0.0D, 0.0D, b);
    }

    @Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var13, double var14, double var15, boolean b)
    {
        final Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            final double var16 = mc.renderViewEntity.posX - var2;
            final double var17 = mc.renderViewEntity.posY - var4;
            final double var19 = mc.renderViewEntity.posZ - var6;
            Object var21 = null;
            final double var22 = 64.0D;

            if (var1.equals("whitesmoke"))
            {
                final EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1.0F, b);
                if (fx != null)
                {
                    mc.effectRenderer.addEffect(fx);
                }
            }
            else if (var1.equals("whitesmokelarge"))
            {
                final EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F, b);
                if (fx != null)
                {
                    mc.effectRenderer.addEffect(fx);
                }
            }
            else if (var1.equals("launchflame"))
            {
                final EntityFX fx = new GCCoreEntityLaunchFlameFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1F, b);
                if (fx != null)
                {
                    mc.effectRenderer.addEffect(fx);
                }
            }
            else if (var1.equals("distancesmoke") && var16 * var16 + var17 * var17 + var19 * var19 < var22 * var22 * 1.7)
            {
                final EntityFX fx = new EntitySmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F);
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
                    ((EntityFX) var21).setRBGColorF((float) var13, (float) var14, (float) var15);
                }
            }

            if (var21 != null)
            {
                ((EntityFX) var21).prevPosX = ((EntityFX) var21).posX;
                ((EntityFX) var21).prevPosY = ((EntityFX) var21).posY;
                ((EntityFX) var21).prevPosZ = ((EntityFX) var21).posZ;
                mc.effectRenderer.addEffect((EntityFX) var21);
            }
        }
    }

    @Override
    public void displayParachestGui(EntityPlayer player, IInventory lander)
    {
        FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiParachest(player.inventory, lander));
        FMLClientHandler.instance().getClient().mouseHelper.ungrabMouseCursor();
    }

    public static boolean handleWaterMovement(EntityPlayer player)
    {
        return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.water);
    }

    public static boolean lastSpacebarDown;

    public static class GCKeyHandler extends KeyHandler
    {
        public static KeyBinding galaxyMap = new KeyBinding(StatCollector.translateToLocal("keybind.map.name"), Keyboard.KEY_M);
        public static KeyBinding openSpaceshipInv = new KeyBinding(StatCollector.translateToLocal("keybind.spaceshipinv.name"), Keyboard.KEY_F);
        public static KeyBinding toggleAdvGoggles = new KeyBinding(StatCollector.translateToLocal("keybind.sensortoggle.name"), Keyboard.KEY_K);
        public static KeyBinding accelerateKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicleforward.name"), Keyboard.KEY_W);
        public static KeyBinding decelerateKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehiclebackward.name"), Keyboard.KEY_S);
        public static KeyBinding leftKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicleleft.name"), Keyboard.KEY_A);
        public static KeyBinding rightKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicleright.name"), Keyboard.KEY_D);
        public static KeyBinding spaceKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicleup.name"), Keyboard.KEY_SPACE);
        public static KeyBinding leftShiftKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicledown.name"), Keyboard.KEY_LSHIFT);

        public GCKeyHandler()
        {
            super(new KeyBinding[] { GCKeyHandler.galaxyMap, GCKeyHandler.openSpaceshipInv, GCKeyHandler.toggleAdvGoggles, GCKeyHandler.accelerateKey, GCKeyHandler.decelerateKey, GCKeyHandler.leftKey, GCKeyHandler.rightKey, GCKeyHandler.spaceKey, GCKeyHandler.leftShiftKey }, new boolean[] { false, false, false, true, true, true, true, true, true });
        }

        @Override
        public String getLabel()
        {
            return "Galacticraft Keybinds";
        }

        @Override
        public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
        {
            final Minecraft minecraft = FMLClientHandler.instance().getClient();

            final EntityPlayerSP player = minecraft.thePlayer;

            if (player == null)
            {
                return;
            }

            final GCCorePlayerSP playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(player);

            if (minecraft.currentScreen != null || tickEnd)
            {
                return;
            }

            if (kb.keyCode == GCKeyHandler.galaxyMap.keyCode)
            {
                if (minecraft.currentScreen == null)
                {
                    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiGalaxyMap, minecraft.theWorld, (int) player.posX, (int) player.posY, (int) player.posZ);
                }
            }
            else if (kb.keyCode == GCKeyHandler.openSpaceshipInv.keyCode)
            {
                final Object[] toSend = { player.username };
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, player.ridingEntity instanceof EntitySpaceshipBase ? 6 : player.ridingEntity instanceof GCCoreEntityBuggy ? 20 : -1, toSend));

                if (player.ridingEntity instanceof EntitySpaceshipBase)
                {
                    player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, minecraft.theWorld, (int) player.posX, (int) player.posY, (int) player.posZ);
                }
            }
            else if (kb.keyCode == GCKeyHandler.toggleAdvGoggles.keyCode)
            {
                if (playerBase != null)
                {
                    playerBase.toggleGoggles();
                }
            }

            if (minecraft.currentScreen != null || tickEnd)
            {
                return;
            }

            int keyNum = -1;
            boolean handled = true;

            if (kb == GCKeyHandler.accelerateKey)
            {
                keyNum = 0;
            }
            else if (kb == GCKeyHandler.decelerateKey)
            {
                keyNum = 1;
            }
            else if (kb == GCKeyHandler.leftKey)
            {
                keyNum = 2;
            }
            else if (kb == GCKeyHandler.rightKey)
            {
                keyNum = 3;
            }
            else if (kb == GCKeyHandler.spaceKey)
            {
                keyNum = 4;
            }
            else if (kb == GCKeyHandler.leftShiftKey)
            {
                keyNum = 5;
            }
            else
            {
                handled = false;
            }

            final Entity entityTest = player.ridingEntity;

            if (entityTest != null && entityTest instanceof GCCoreEntityControllable && handled == true)
            {
                final GCCoreEntityControllable entity = (GCCoreEntityControllable) entityTest;

                if (kb.keyCode == minecraft.gameSettings.keyBindInventory.keyCode)
                {
                    minecraft.gameSettings.keyBindInventory.pressed = false;
                    minecraft.gameSettings.keyBindInventory.pressTime = 0;
                }

                handled = entity.pressKey(keyNum);
            }
            else
            {
                handled = false;
            }

            if (handled == true)
            {
                return;
            }

            for (final KeyBinding key : minecraft.gameSettings.keyBindings)
            {
                if (kb.keyCode == key.keyCode && key != kb)
                {
                    key.pressed = true;
                    key.pressTime = 1;
                }
            }
        }

        @Override
        public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
        {
            if (tickEnd)
            {
                return;
            }

            for (final KeyBinding key : FMLClientHandler.instance().getClient().gameSettings.keyBindings)
            {
                if (kb.keyCode == key.keyCode && key != kb)
                {
                    key.pressed = false;
                }
            }
        }

        @Override
        public EnumSet<TickType> ticks()
        {
            return EnumSet.of(TickType.CLIENT);
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        final TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (ID == GCCoreConfigManager.idGuiGalaxyMap)
        {
            return new GCCoreGuiGalaxyMap(player);
        }
        else if (ID == GCCoreConfigManager.idGuiSpaceshipInventory && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase && player.ridingEntity instanceof IRocketType)
        {
            return new GCCoreGuiRocketRefill(player.inventory, (EntityTieredRocket) player.ridingEntity, ((IRocketType) player.ridingEntity).getType());
        }
        else if (ID == GCCoreConfigManager.idGuiRefinery)
        {
            return new GCCoreGuiRefinery(player.inventory, (GCCoreTileEntityRefinery) world.getBlockTileEntity(x, y, z));
        }
        else if (ID == GCCoreConfigManager.idGuiAirCompressor)
        {
            if (tile != null && tile instanceof GCCoreTileEntityOxygenCompressor)
            {
                return new GCCoreGuiAirCompressor(player.inventory, (GCCoreTileEntityOxygenCompressor) tile);
            }
            else
            {
                return null;
            }
        }
        else if (ID == GCCoreConfigManager.idGuiAirCollector)
        {
            if (tile != null && tile instanceof GCCoreTileEntityOxygenCollector)
            {
                return new GCCoreGuiAirCollector(player.inventory, (GCCoreTileEntityOxygenCollector) tile);
            }
            else
            {
                return null;
            }
        }
        else if (ID == GCCoreConfigManager.idGuiAirDistributor)
        {
            if (tile != null && tile instanceof GCCoreTileEntityOxygenDistributor)
            {
                return new GCCoreGuiAirDistributor(player.inventory, (GCCoreTileEntityOxygenDistributor) tile);
            }
            else
            {
                return null;
            }
        }
        else if (ID == GCCoreConfigManager.idGuiFuelLoader)
        {
            if (tile != null && tile instanceof GCCoreTileEntityFuelLoader)
            {
                return new GCCoreGuiFuelLoader(player.inventory, (GCCoreTileEntityFuelLoader) tile);
            }
            else
            {
                return null;
            }
        }
        else if (ID == GCCoreConfigManager.idGuiAirSealer)
        {
            if (tile != null && tile instanceof GCCoreTileEntityOxygenSealer)
            {
                return new GCCoreGuiAirSealer(player.inventory, (GCCoreTileEntityOxygenSealer) tile);
            }
            else
            {
                return null;
            }
        }
        else if (ID == GCCoreConfigManager.idGuiCargoLoader)
        {
            if (tile != null)
            {
                if (tile instanceof GCCoreTileEntityCargoLoader)
                {
                    return new GCCoreGuiCargoLoader(player.inventory, (GCCoreTileEntityCargoLoader) tile);
                }
                else if (tile instanceof GCCoreTileEntityCargoUnloader)
                {
                    return new GCCoreGuiCargoUnloader(player.inventory, (GCCoreTileEntityCargoUnloader) tile);
                }
            }
            else
            {
                return null;
            }
        }
        else if (ID == GCCoreConfigManager.idGuiParachest)
        {
            if (tile != null && tile instanceof GCCoreTileEntityParachest)
            {
                return new GCCoreGuiParachest(player.inventory, (GCCoreTileEntityParachest) tile);
            }
            else
            {
                return null;
            }
        }
        else if (ID == GCCoreConfigManager.idGuiSolarPanel)
        {
            if (tile != null && tile instanceof GCCoreTileEntitySolar)
            {
                return new GCCoreGuiSolar(player.inventory, (GCCoreTileEntitySolar) tile);
            }
            else
            {
                return null;
            }
        }
        else if (ID == GCCoreConfigManager.idGuiExtendedInventory)
        {
            return new GCCoreGuiExtendedInventory(player, ClientProxyCore.dummyInventory);
        }
        else if (ID == GCCoreConfigManager.idGuiKnowledgeBook)
        {
            return new GCCoreGuiManual(new ItemStack(Block.stone), ClientProxyCore.materialsTest);
        }
        else
        {
            final GCCorePlayerSP playerClient = PlayerUtil.getPlayerBaseClientFromPlayer(player);

            if (playerClient != null)
            {
                for (final ISchematicPage page : playerClient.unlockedSchematics)
                {
                    if (ID == page.getGuiID())
                    {
                        final GuiScreen screen = page.getResultScreen(playerClient, x, y, z);

                        if (screen instanceof ISchematicResultPage)
                        {
                            ((ISchematicResultPage) screen).setPageIndex(page.getPageID());
                        }

                        return screen;
                    }
                }
            }
        }

        if (tile != null)
        {
            if (tile instanceof GCCoreTileEntityEnergyStorageModule)
            {
                return new GCCoreGuiEnergyStorageModule(player.inventory, (GCCoreTileEntityEnergyStorageModule) tile);
            }
            else if (tile instanceof GCCoreTileEntityCoalGenerator)
            {
                return new GCCoreGuiCoalGenerator(player.inventory, (GCCoreTileEntityCoalGenerator) tile);
            }
            else if (tile instanceof GCCoreTileEntityElectricFurnace)
            {
                return new GCCoreGuiElectricFurnace(player.inventory, (GCCoreTileEntityElectricFurnace) tile);
            }
            else if (tile instanceof GCCoreTileEntityIngotCompressor)
            {
                return new GCCoreGuiIngotCompressor(player.inventory, (GCCoreTileEntityIngotCompressor) tile);
            }
            else if (tile instanceof GCCoreTileEntityElectricIngotCompressor)
            {
                return new GCCoreGuiElectricIngotCompressor(player.inventory, (GCCoreTileEntityElectricIngotCompressor) tile);
            }
            else if (tile instanceof GCCoreTileEntityCircuitFabricator)
            {
                return new GCCoreGuiCircuitFabricator(player.inventory, (GCCoreTileEntityCircuitFabricator) tile);
            }
        }

        return null;
    }

    private static final ResourceLocation underOilTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/misc/underoil.png");

    public static void renderLiquidOverlays(float partialTicks)
    {
        Minecraft minecraft = FMLClientHandler.instance().getClient();

        if (minecraft.thePlayer.isInsideOfMaterial(GCCoreBlocks.crudeOil))
        {
            minecraft.getTextureManager().bindTexture(ClientProxyCore.underOilTexture);
        }
        else
        {
            return;
        }

        Tessellator tessellator = Tessellator.instance;
        float f1 = minecraft.thePlayer.getBrightness(partialTicks) / 3.0F;
        GL11.glColor4f(f1, f1, f1, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        float f2 = 4.0F;
        float f3 = -1.0F;
        float f4 = 1.0F;
        float f5 = -1.0F;
        float f6 = 1.0F;
        float f7 = -0.5F;
        float f8 = -minecraft.thePlayer.rotationYaw / 64.0F;
        float f9 = minecraft.thePlayer.rotationPitch / 64.0F;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(f3, f5, f7, f2 + f8, f2 + f9);
        tessellator.addVertexWithUV(f4, f5, f7, 0.0F + f8, f2 + f9);
        tessellator.addVertexWithUV(f4, f6, f7, 0.0F + f8, 0.0F + f9);
        tessellator.addVertexWithUV(f3, f6, f7, f2 + f8, 0.0F + f9);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static boolean addTabsNextTick = false;

    public static void addTabsToInventory(GuiContainer gui)
    {
        boolean tConstructLoaded = Loader.isModLoaded("TConstruct");

        if (!tConstructLoaded)
        {
            if (!ClientProxyCore.addTabsNextTick)
            {
                ClientProxyCore.addTabsNextTick = true;
                return;
            }

            TabRegistry.addTabsToInventory(gui);
        }
    }
}
