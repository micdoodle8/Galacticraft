package micdoodle8.mods.galacticraft.core.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ICelestialBody;
import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
import micdoodle8.mods.galacticraft.api.world.IMoon;
import micdoodle8.mods.galacticraft.api.world.IPlanet;
import micdoodle8.mods.galacticraft.core.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEffectHandler;
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
import micdoodle8.mods.galacticraft.core.client.render.block.GCCoreBlockRendererMachine;
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
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
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
import micdoodle8.mods.galacticraft.core.tick.GCCoreKeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAdvancedCraftingTable;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityParachest;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySolar;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.wrappers.BlockMetaList;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.EnumHelperClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;

import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * ClientProxyCore.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ClientProxyCore extends CommonProxyCore
{
	private static int renderIdTreasureChest;
	private static int renderIdTorchUnlit;
	private static int renderIdBreathableAir;
	private static int renderIdOxygenPipe;
	private static int renderIdMeteor;
	private static int renderIdCraftingTable;
	private static int renderIdLandingPad;
	private static int renderIdMachine;

	private static int renderIndexHeavyArmor;
	private static int renderIndexSensorGlasses;

	public static ClientProxyMoon moon = new ClientProxyMoon();
	public static List<ICelestialBodyRenderer> slotRenderers = Lists.newArrayList();
	public static Set<Vector3> valueableBlocks = Sets.newHashSet();
	public static HashSet<BlockMetaList> detectableBlocks = Sets.newHashSet();

	public static Set<PlayerGearData> playerItemData = Sets.newHashSet();

	public static double playerPosX;
	public static double playerPosY;
	public static double playerPosZ;
	public static float playerRotationYaw;
	public static float playerRotationPitch;

	public static String MODEL_DIRECTORY = "/assets/galacticraftcore/models/";

	public static boolean lastSpacebarDown;

	public static int clientSpaceStationID = 0;

	public static ArrayList<SoundPoolEntry> newMusic = new ArrayList<SoundPoolEntry>();

	public static EnumRarity galacticraftItem = EnumHelperClient.addRarity("GCRarity", 9, "Space");

	public static Map<String, String> capeMap = new HashMap<String, String>();

	public static GCCoreInventoryExtended dummyInventory = new GCCoreInventoryExtended();

	public static Document materialsTest;

	private static final ResourceLocation underOilTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/misc/underoil.png");

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		ClientProxyCore.moon.preInit(event);

		MinecraftForge.EVENT_BUS.register(new GCCoreSounds());

		ClientProxyCore.renderIndexSensorGlasses = RenderingRegistry.addNewArmourRendererPrefix("sensor");
		ClientProxyCore.renderIndexHeavyArmor = RenderingRegistry.addNewArmourRendererPrefix("titanium");
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		ClientProxyCore.moon.init(event);
		ClientProxyCore.registerHandlers();
		ClientProxyCore.registerManuals();
		ClientProxyCore.registerTileEntityRenderers();
		ClientProxyCore.registerBlockHandlers();
		ClientProxyCore.setupCapes();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		ClientProxyCore.moon.postInit(event);
		ClientProxyCore.registerInventoryTabs();
		ClientProxyCore.registerMapObjects();
		ClientProxyCore.registerEntityRenderers();
		ClientProxyCore.registerItemRenderers();
	}

	public static void registerEntityRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityRocketT1.class, new GCCoreRenderSpaceship(new GCCoreModelSpaceship(), GalacticraftCore.ASSET_DOMAIN, "rocketT1"));
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySpider.class, new GCCoreRenderSpider());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityZombie.class, new GCCoreRenderZombie());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityCreeper.class, new GCCoreRenderCreeper());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySkeleton.class, new GCCoreRenderSkeleton());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntitySkeletonBoss.class, new GCCoreRenderSkeletonBoss());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityMeteor.class, new GCCoreRenderMeteor());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityBuggy.class, new GCCoreRenderBuggy());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityMeteorChunk.class, new GCCoreRenderMeteorChunk());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityFlag.class, new GCCoreRenderFlag());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityParaChest.class, new GCCoreRenderParaChest());
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new GCCoreRenderPlayer());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityAlienVillager.class, new GCCoreRenderAlienVillager());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityOxygenBubble.class, new GCCoreRenderOxygenBubble(0.25F, 0.25F, 1.0F));
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityLander.class, new GCCoreRenderLander());
		RenderingRegistry.registerEntityRenderingHandler(GCCoreEntityArrow.class, new GCCoreRenderArrow());
	}

	public static void registerItemRenderers()
	{
		MinecraftForgeClient.registerItemRenderer(GCCoreBlocks.unlitTorch.blockID, new GCCoreItemRendererUnlitTorch());
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.rocketTier1.itemID, new GCCoreItemRendererSpaceship(new GCCoreEntityRocketT1(FMLClientHandler.instance().getClient().theWorld), new GCCoreModelSpaceship(), new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/rocketT1.png")));
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.buggy.itemID, new GCCoreItemRendererBuggy());
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.flag.itemID, new GCCoreItemRendererFlag());
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.key.itemID, new GCCoreItemRendererKey(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/treasure.png")));
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.meteorChunk.itemID, new GCCoreItemRendererMeteorChunk());
	}

	public static void registerHandlers()
	{
		TickRegistry.registerTickHandler(new GCCoreTickHandlerClient(), Side.CLIENT);
		NetworkRegistry.instance().registerChannel(new GCCorePacketHandlerClient(), GalacticraftCore.CHANNEL, Side.CLIENT);
		KeyBindingRegistry.registerKeyBinding(new GCCoreKeyHandlerClient());
	}

	public static void registerManuals()
	{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		ClientProxyCore.materialsTest = GCCoreManualUtil.readManual("/assets/galacticraftcore/manuals/gettingstarted.xml", docBuilderFactory);

		ClientProxyCore.registerManualPages();
		ClientProxyCore.registerManualIcons();
	}

	private static void registerManualPages()
	{
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
	}

	private static void registerManualIcons()
	{
		GCCoreManualUtil.registerIcon("heavyplatingT1", new ItemStack(GCCoreItems.heavyPlatingTier1));
		GCCoreManualUtil.registerIcon("oxygenmask", new ItemStack(GCCoreItems.oxMask));
		GCCoreManualUtil.registerIcon("oxygenTankHeavy", new ItemStack(GCCoreItems.oxTankHeavy));
		GCCoreManualUtil.registerIcon("rocketT1", new ItemStack(GCCoreItems.rocketTier1));
	}

	public static void registerTileEntityRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityAluminumWire.class, new GCCoreRenderAluminumWire());
		ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityTreasureChest.class, new GCCoreTileEntityTreasureChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityParachest.class, new GCCoreTileEntityParachestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntityAdvancedCraftingTable.class, new GCCoreTileEntityAdvancedCraftingTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(GCCoreTileEntitySolar.class, new GCCoreTileEntitySolarPanelRenderer());
	}

	public static void registerBlockHandlers()
	{
		ClientProxyCore.renderIdTreasureChest = RenderingRegistry.getNextAvailableRenderId();
		ClientProxyCore.renderIdTorchUnlit = RenderingRegistry.getNextAvailableRenderId();
		ClientProxyCore.renderIdBreathableAir = RenderingRegistry.getNextAvailableRenderId();
		ClientProxyCore.renderIdOxygenPipe = RenderingRegistry.getNextAvailableRenderId();
		ClientProxyCore.renderIdMeteor = RenderingRegistry.getNextAvailableRenderId();
		ClientProxyCore.renderIdCraftingTable = RenderingRegistry.getNextAvailableRenderId();
		ClientProxyCore.renderIdLandingPad = RenderingRegistry.getNextAvailableRenderId();
		ClientProxyCore.renderIdMachine = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererTreasureChest(ClientProxyCore.renderIdTreasureChest));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererUnlitTorch(ClientProxyCore.renderIdTorchUnlit));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererBreathableAir(ClientProxyCore.renderIdBreathableAir));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererOxygenPipe(ClientProxyCore.renderIdOxygenPipe));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererMeteor(ClientProxyCore.renderIdMeteor));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererCraftingTable(ClientProxyCore.renderIdCraftingTable));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererLandingPad(ClientProxyCore.renderIdLandingPad));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererMachine(ClientProxyCore.renderIdMachine));
	}

	public static void setupCapes()
	{
		try
		{
			ClientProxyCore.updateCapeList();
		}
		catch (Exception e)
		{
			FMLLog.severe("Error while setting up Galacticraft donor capes");
			e.printStackTrace();
		}

		if (Loader.isModLoaded("CoFHCore"))
		{
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

	private static void updateCapeList() throws Exception
	{
		int timeout = 10000;
		URL capeListUrl = new URL("https://raw.github.com/micdoodle8/Galacticraft/master/capes.txt");
		URLConnection connection = capeListUrl.openConnection();
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout);
		InputStream stream = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String line;
		while ((line = reader.readLine()) != null)
		{
			if (line.contains(":"))
			{
				int splitLocation = line.indexOf(":");
				String username = line.substring(0, splitLocation);
				String capeUrl = "https://raw.github.com/micdoodle8/Galacticraft/master/capes/" + line.substring(splitLocation + 1) + ".png";
				ClientProxyCore.capeMap.put(username, capeUrl);
			}
		}
	}

	public static void registerInventoryTabs()
	{
		if (!Loader.isModLoaded("TConstruct") || TabRegistry.getTabList().size() < 3)
		{
			TabRegistry.registerTab(new InventoryTabVanilla());
		}

		TabRegistry.registerTab(new GCCoreInventoryTabGalacticraft());
	}

	public static void registerMapObjects()
	{
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
	}

	@Override
	public void registerRenderInformation()
	{
		ClientProxyCore.moon.registerRenderInformation();
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
	public int getBlockRenderID(int blockID)
	{
		if (blockID == GCCoreBlocks.treasureChestTier1.blockID)
		{
			return ClientProxyCore.renderIdTreasureChest;
		}
		else if (blockID == GCCoreBlocks.breatheableAir.blockID)
		{
			return ClientProxyCore.renderIdBreathableAir;
		}
		else if (blockID == GCCoreBlocks.oxygenPipe.blockID)
		{
			return ClientProxyCore.renderIdOxygenPipe;
		}
		else if (blockID == GCCoreBlocks.fallenMeteor.blockID)
		{
			return ClientProxyCore.renderIdMeteor;
		}
		else if (blockID == GCCoreBlocks.nasaWorkbench.blockID)
		{
			return ClientProxyCore.renderIdCraftingTable;
		}
		else if (blockID == GCCoreBlocks.landingPadFull.blockID)
		{
			return ClientProxyCore.renderIdLandingPad;
		}
		else if (blockID == GCCoreBlocks.unlitTorch.blockID || blockID == GCCoreBlocks.unlitTorchLit.blockID || blockID == GCCoreBlocks.glowstoneTorch.blockID)
		{
			return ClientProxyCore.renderIdTorchUnlit;
		}
		else if (blockID == GCCoreBlocks.fuelLoader.blockID || blockID == GCCoreBlocks.cargoLoader.blockID || blockID == GCCoreBlocks.machineBase.blockID || blockID == GCCoreBlocks.machineBase2.blockID || blockID == GCCoreBlocks.oxygenCollector.blockID || blockID == GCCoreBlocks.oxygenCompressor.blockID || blockID == GCCoreBlocks.oxygenDetector.blockID || blockID == GCCoreBlocks.oxygenDistributor.blockID || blockID == GCCoreBlocks.oxygenSealer.blockID || blockID == GCCoreBlocks.refinery.blockID)
		{
			return ClientProxyCore.renderIdMachine;
		}

		return -1;
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public int getTitaniumArmorRenderIndex()
	{
		return ClientProxyCore.renderIndexHeavyArmor;
	}

	@Override
	public int getSensorArmorRenderIndex()
	{
		return ClientProxyCore.renderIndexSensorGlasses;
	}

	@Override
	public void spawnParticle(String particleID, Vector3 position, Vector3 motion)
	{
		this.spawnParticle(particleID, position, motion, new Vector3(0, 0, 0));
	}

	@Override
	public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Vector3 color)
	{
		GCCoreEffectHandler.spawnParticle(particleID, position, motion, color);
	}

	public static void renderLiquidOverlays(float partialTicks)
	{
		Minecraft minecraft = FMLClientHandler.instance().getClient();

		if (ClientProxyCore.isInsideOfFluid(minecraft.thePlayer, GalacticraftCore.fluidOil))
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

	public static boolean isInsideOfFluid(Entity entity, Fluid fluid)
	{
		double d0 = entity.posY + entity.getEyeHeight();
		int i = MathHelper.floor_double(entity.posX);
		int j = MathHelper.floor_float(MathHelper.floor_double(d0));
		int k = MathHelper.floor_double(entity.posZ);
		int l = entity.worldObj.getBlockId(i, j, k);

		Block block = Block.blocksList[l];
		if (block != null && block instanceof IFluidBlock && ((IFluidBlock) block).getFluid() != null && ((IFluidBlock) block).getFluid().getName().equals(fluid.getName()))
		{
			double filled = ((IFluidBlock) block).getFilledPercentage(entity.worldObj, i, j, k);
			if (filled < 0)
			{
				filled *= -1;
				return d0 > j + (1 - filled);
			}
			else
			{
				return d0 < j + filled;
			}
		}
		else
		{
			return false;
		}
	}
}
