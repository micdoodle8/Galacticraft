package micdoodle8.mods.galacticraft.core.proxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.FootprintRenderer;
import micdoodle8.mods.galacticraft.core.client.fx.EffectHandler;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InventoryTabGalacticraft;
import micdoodle8.mods.galacticraft.core.client.model.ModelRocketTier1;
import micdoodle8.mods.galacticraft.core.client.render.block.BlockRendererBreathableAir;
import micdoodle8.mods.galacticraft.core.client.render.block.BlockRendererLandingPad;
import micdoodle8.mods.galacticraft.core.client.render.block.BlockRendererMachine;
import micdoodle8.mods.galacticraft.core.client.render.block.BlockRendererMeteor;
import micdoodle8.mods.galacticraft.core.client.render.block.BlockRendererNasaWorkbench;
import micdoodle8.mods.galacticraft.core.client.render.block.BlockRendererOxygenPipe;
import micdoodle8.mods.galacticraft.core.client.render.block.BlockRendererTreasureChest;
import micdoodle8.mods.galacticraft.core.client.render.block.BlockRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderBuggy;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderFlag;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderLander;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderMeteor;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderMeteorChunk;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderBubble;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderParaChest;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderTier1Rocket;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedSpider;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderEvolvedZombie;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderAlienVillager;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererBuggy;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererFlag;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererKey;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererMeteorChunk;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererTier1Rocket;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererUnlitTorch;
import micdoodle8.mods.galacticraft.core.client.render.tile.TileEntityAluminumWireRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.TileEntityNasaWorkbenchRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.TileEntityParachestRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.TileEntitySolarPanelRenderer;
import micdoodle8.mods.galacticraft.core.client.render.tile.TileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.core.client.sounds.SoundHandler;
import micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.EntityBubble;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.TileEntityNasaWorkbench;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.wrappers.BlockMetaList;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;

import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;

import com.google.common.collect.Sets;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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

	public static FootprintRenderer footprintRenderer = new FootprintRenderer();

	private static int renderIndexHeavyArmor;
	private static int renderIndexSensorGlasses;

	public static Set<Vector3> valueableBlocks = Sets.newHashSet();
	public static HashSet<BlockMetaList> detectableBlocks = Sets.newHashSet();

	public static Set<PlayerGearData> playerItemData = Sets.newHashSet();

	public static double playerPosX;
	public static double playerPosY;
	public static double playerPosZ;
	public static float playerRotationYaw;
	public static float playerRotationPitch;

	public static boolean lastSpacebarDown;

	public static int clientSpaceStationID = 0;

	public static ArrayList<SoundPoolEntry> newMusic = new ArrayList<SoundPoolEntry>();

	public static EnumRarity galacticraftItem = EnumRarity.common;//EnumHelperClient.addRarity("GCRarity", 9, "Space");

	public static Map<String, String> capeMap = new HashMap<String, String>();

	public static InventoryExtended dummyInventory = new InventoryExtended();

	public static Document materialsTest;

	private static final ResourceLocation underOilTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/misc/underoil.png");

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new SoundHandler());

		ClientProxyCore.renderIndexSensorGlasses = RenderingRegistry.addNewArmourRendererPrefix("sensor");
		ClientProxyCore.renderIndexHeavyArmor = RenderingRegistry.addNewArmourRendererPrefix("titanium");
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		ClientProxyCore.registerHandlers();
		ClientProxyCore.registerTileEntityRenderers();
		ClientProxyCore.registerBlockHandlers();
		ClientProxyCore.setupCapes();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		ClientProxyCore.registerInventoryTabs();
		ClientProxyCore.registerEntityRenderers();
		ClientProxyCore.registerItemRenderers();
	}

	public static void registerEntityRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityTier1Rocket.class, new RenderTier1Rocket(new ModelRocketTier1(), GalacticraftCore.ASSET_DOMAIN, "rocketT1"));
		RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedSpider.class, new RenderEvolvedSpider());
		RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedZombie.class, new RenderEvolvedZombie());
		RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedCreeper.class, new RenderEvolvedCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntityEvolvedSkeleton.class, new RenderEvolvedSkeleton());
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonBoss.class, new RenderEvolvedSkeletonBoss());
		RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, new RenderMeteor());
		RenderingRegistry.registerEntityRenderingHandler(EntityBuggy.class, new RenderBuggy());
		RenderingRegistry.registerEntityRenderingHandler(EntityMeteorChunk.class, new RenderMeteorChunk());
		RenderingRegistry.registerEntityRenderingHandler(EntityFlag.class, new RenderFlag());
		RenderingRegistry.registerEntityRenderingHandler(EntityParachest.class, new RenderParaChest());
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new RenderPlayerGC());
		RenderingRegistry.registerEntityRenderingHandler(EntityAlienVillager.class, new RenderAlienVillager());
		RenderingRegistry.registerEntityRenderingHandler(EntityBubble.class, new RenderBubble(0.25F, 0.25F, 1.0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityLander.class, new RenderLander());
	}

	public static void registerItemRenderers()
	{
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GCBlocks.unlitTorch), new ItemRendererUnlitTorch());
		MinecraftForgeClient.registerItemRenderer(GCItems.rocketTier1, new ItemRendererTier1Rocket(new EntityTier1Rocket(FMLClientHandler.instance().getClient().theWorld), new ModelRocketTier1(), new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/rocketT1.png")));
		MinecraftForgeClient.registerItemRenderer(GCItems.buggy, new ItemRendererBuggy());
		MinecraftForgeClient.registerItemRenderer(GCItems.flag, new ItemRendererFlag());
		MinecraftForgeClient.registerItemRenderer(GCItems.key, new ItemRendererKey(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/treasure.png")));
		MinecraftForgeClient.registerItemRenderer(GCItems.meteorChunk, new ItemRendererMeteorChunk());
	}

	public static void registerHandlers()
	{
		FMLCommonHandler.instance().bus().register(new TickHandlerClient());
		FMLCommonHandler.instance().bus().register(new KeyHandlerClient());
	}

	public static void registerTileEntityRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminumWire.class, new TileEntityAluminumWireRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChest.class, new TileEntityTreasureChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityParaChest.class, new TileEntityParachestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNasaWorkbench.class, new TileEntityNasaWorkbenchRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolar.class, new TileEntitySolarPanelRenderer());
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
		RenderingRegistry.registerBlockHandler(new BlockRendererTreasureChest(ClientProxyCore.renderIdTreasureChest));
		RenderingRegistry.registerBlockHandler(new BlockRendererUnlitTorch(ClientProxyCore.renderIdTorchUnlit));
		RenderingRegistry.registerBlockHandler(new BlockRendererBreathableAir(ClientProxyCore.renderIdBreathableAir));
		RenderingRegistry.registerBlockHandler(new BlockRendererOxygenPipe(ClientProxyCore.renderIdOxygenPipe));
		RenderingRegistry.registerBlockHandler(new BlockRendererMeteor(ClientProxyCore.renderIdMeteor));
		RenderingRegistry.registerBlockHandler(new BlockRendererNasaWorkbench(ClientProxyCore.renderIdCraftingTable));
		RenderingRegistry.registerBlockHandler(new BlockRendererLandingPad(ClientProxyCore.renderIdLandingPad));
		RenderingRegistry.registerBlockHandler(new BlockRendererMachine(ClientProxyCore.renderIdMachine));
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

		TabRegistry.registerTab(new InventoryTabGalacticraft());
	}

	public static void renderPlanets(float par3)
	{
	}

	@Override
	public int getBlockRender(Block blockID)
	{
		if (blockID == GCBlocks.treasureChestTier1)
		{
			return ClientProxyCore.renderIdTreasureChest;
		}
		else if (blockID == GCBlocks.breatheableAir)
		{
			return ClientProxyCore.renderIdBreathableAir;
		}
		else if (blockID == GCBlocks.oxygenPipe)
		{
			return ClientProxyCore.renderIdOxygenPipe;
		}
		else if (blockID == GCBlocks.fallenMeteor)
		{
			return ClientProxyCore.renderIdMeteor;
		}
		else if (blockID == GCBlocks.nasaWorkbench)
		{
			return ClientProxyCore.renderIdCraftingTable;
		}
		else if (blockID == GCBlocks.landingPadFull)
		{
			return ClientProxyCore.renderIdLandingPad;
		}
		else if (blockID == GCBlocks.unlitTorch || blockID == GCBlocks.unlitTorchLit || blockID == GCBlocks.glowstoneTorch)
		{
			return ClientProxyCore.renderIdTorchUnlit;
		}
		else if (blockID == GCBlocks.fuelLoader || blockID == GCBlocks.cargoLoader || blockID == GCBlocks.machineBase || blockID == GCBlocks.machineBase2 || blockID == GCBlocks.oxygenCollector || blockID == GCBlocks.oxygenCompressor || blockID == GCBlocks.oxygenDetector || blockID == GCBlocks.oxygenDistributor || blockID == GCBlocks.oxygenSealer || blockID == GCBlocks.refinery)
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
		EffectHandler.spawnParticle(particleID, position, motion, color);
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
		Block block = entity.worldObj.getBlock(i, j, k);

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

	public static void renderFootprints(float partialTicks)
	{
		footprintRenderer.renderFootprints(FMLClientHandler.instance().getClient().thePlayer, partialTicks);
	}
}
