package micdoodle8.mods.galacticraft.core.proxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEffectHandler;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InventoryTabGalacticraft;
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
import micdoodle8.mods.galacticraft.core.items.GCCoreItems.EnumArmorIndex;
import micdoodle8.mods.galacticraft.core.tick.GCCoreKeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvancedCraftingTable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParachest;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;

import org.lwjgl.opengl.GL11;

import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	public static GCCoreInventoryExtended dummyInventory = new GCCoreInventoryExtended();

	private static int renderIndexHeavyArmor;
	private static int renderIndexSensorGlasses;
	
	public static EnumRarity galacticraftItem = EnumRarity.common;//EnumHelperClient.addRarity("GCRarity", 9, "Space");

	private static int renderIdTreasureChest;
	private static int renderIdTorchUnlit;
	private static int renderIdBreathableAir;
	private static int renderIdOxygenPipe;
	private static int renderIdMeteor;
	private static int renderIdCraftingTable;
	private static int renderIdLandingPad;
	private static int renderIdMachine;

	public static Map<String, String> capeMap = new HashMap<String, String>();

	private static final ResourceLocation underOilTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/misc/underoil.png");
	
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);

		MinecraftForge.EVENT_BUS.register(new GCCoreSounds());

		ClientProxy.renderIndexSensorGlasses = RenderingRegistry.addNewArmourRendererPrefix("sensor");
		ClientProxy.renderIndexHeavyArmor = RenderingRegistry.addNewArmourRendererPrefix("titanium");
	}
	
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		FMLCommonHandler.instance().bus().register(new GCCoreKeyHandlerClient());
		FMLCommonHandler.instance().bus().register(new GCCoreTickHandlerClient());
		
		ClientProxy.registerTileEntityRenderers();
		ClientProxy.registerBlockHandlers();
		ClientProxy.setupCapes();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
		ClientProxy.registerInventoryTabs();
		ClientProxy.registerEntityRenderers();
		ClientProxy.registerItemRenderers();
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
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GCCoreBlocks.unlitTorch), new GCCoreItemRendererUnlitTorch());
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.rocketTier1, new GCCoreItemRendererSpaceship(new GCCoreEntityRocketT1(FMLClientHandler.instance().getClient().theWorld), new GCCoreModelSpaceship(), new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/rocketT1.png")));
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.buggy, new GCCoreItemRendererBuggy());
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.flag, new GCCoreItemRendererFlag());
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.key, new GCCoreItemRendererKey(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/treasure.png")));
		MinecraftForgeClient.registerItemRenderer(GCCoreItems.meteorChunk, new GCCoreItemRendererMeteorChunk());
	}

	public static void registerTileEntityRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminumWire.class, new GCCoreRenderAluminumWire());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChest.class, new GCCoreTileEntityTreasureChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityParachest.class, new GCCoreTileEntityParachestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAdvancedCraftingTable.class, new GCCoreTileEntityAdvancedCraftingTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolar.class, new GCCoreTileEntitySolarPanelRenderer());
	}

	public static void registerBlockHandlers()
	{
		ClientProxy.renderIdTreasureChest = RenderingRegistry.getNextAvailableRenderId();
		ClientProxy.renderIdTorchUnlit = RenderingRegistry.getNextAvailableRenderId();
		ClientProxy.renderIdBreathableAir = RenderingRegistry.getNextAvailableRenderId();
		ClientProxy.renderIdOxygenPipe = RenderingRegistry.getNextAvailableRenderId();
		ClientProxy.renderIdMeteor = RenderingRegistry.getNextAvailableRenderId();
		ClientProxy.renderIdCraftingTable = RenderingRegistry.getNextAvailableRenderId();
		ClientProxy.renderIdLandingPad = RenderingRegistry.getNextAvailableRenderId();
		ClientProxy.renderIdMachine = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererTreasureChest(ClientProxy.renderIdTreasureChest));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererUnlitTorch(ClientProxy.renderIdTorchUnlit));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererBreathableAir(ClientProxy.renderIdBreathableAir));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererOxygenPipe(ClientProxy.renderIdOxygenPipe));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererMeteor(ClientProxy.renderIdMeteor));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererCraftingTable(ClientProxy.renderIdCraftingTable));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererLandingPad(ClientProxy.renderIdLandingPad));
		RenderingRegistry.registerBlockHandler(new GCCoreBlockRendererMachine(ClientProxy.renderIdMachine));
	}

	public static void setupCapes()
	{
		try
		{
			ClientProxy.updateCapeList();
		}
		catch (Exception e)
		{
			FMLLog.severe("Error while setting up Galacticraft donor capes");
			e.printStackTrace();
		}

		if (Loader.isModLoaded("CoFHCore"))
		{
			for (Entry<String, String> e : ClientProxy.capeMap.entrySet())
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
				ClientProxy.capeMap.put(username, capeUrl);
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

	@Override
	public int getBlockRenderID(Block blockID)
	{
		if (blockID == GCCoreBlocks.treasureChestTier1)
		{
			return ClientProxy.renderIdTreasureChest;
		}
		else if (blockID == GCCoreBlocks.breatheableAir)
		{
			return ClientProxy.renderIdBreathableAir;
		}
		else if (blockID == GCCoreBlocks.oxygenPipe)
		{
			return ClientProxy.renderIdOxygenPipe;
		}
		else if (blockID == GCCoreBlocks.fallenMeteor)
		{
			return ClientProxy.renderIdMeteor;
		}
		else if (blockID == GCCoreBlocks.nasaWorkbench)
		{
			return ClientProxy.renderIdCraftingTable;
		}
		else if (blockID == GCCoreBlocks.landingPadFull)
		{
			return ClientProxy.renderIdLandingPad;
		}
		else if (blockID == GCCoreBlocks.unlitTorch || blockID == GCCoreBlocks.unlitTorchLit || blockID == GCCoreBlocks.glowstoneTorch)
		{
			return ClientProxy.renderIdTorchUnlit;
		}
		else if (blockID == GCCoreBlocks.fuelLoader || blockID == GCCoreBlocks.cargoLoader || blockID == GCCoreBlocks.machineBase || blockID == GCCoreBlocks.machineBase2 || blockID == GCCoreBlocks.oxygenCollector || blockID == GCCoreBlocks.oxygenCompressor || blockID == GCCoreBlocks.oxygenDetector || blockID == GCCoreBlocks.oxygenDistributor || blockID == GCCoreBlocks.oxygenSealer || blockID == GCCoreBlocks.refinery)
		{
			return ClientProxy.renderIdMachine;
		}

		return -1;
	}
	
	@Override
	public int getArmorRenderID(EnumArmorIndex type)
	{
		switch (type)
		{
		case HEAVY_DUTY:
			return renderIndexHeavyArmor;
		case SENSOR_GLASSES:
			return renderIndexSensorGlasses;
		}
		
		return -1;
	}

	public static void renderLiquidOverlays(float partialTicks)
	{
		Minecraft minecraft = FMLClientHandler.instance().getClient();

		if (ClientProxy.isInsideOfFluid(minecraft.thePlayer, GalacticraftCore.fluidOil))
		{
			minecraft.getTextureManager().bindTexture(ClientProxy.underOilTexture);
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

		if (block instanceof IFluidBlock && ((IFluidBlock) block).getFluid() != null && ((IFluidBlock) block).getFluid().getName().equals(fluid.getName()))
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
