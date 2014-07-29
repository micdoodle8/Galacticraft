package micdoodle8.mods.galacticraft.core.proxy;

import com.google.common.collect.Maps;
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
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.FootprintRenderer;
import micdoodle8.mods.galacticraft.core.client.fx.EffectHandler;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InventoryTabGalacticraft;
import micdoodle8.mods.galacticraft.core.client.model.ModelRocketTier1;
import micdoodle8.mods.galacticraft.core.client.render.block.*;
import micdoodle8.mods.galacticraft.core.client.render.entities.*;
import micdoodle8.mods.galacticraft.core.client.render.item.*;
import micdoodle8.mods.galacticraft.core.client.render.tile.*;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.*;
import micdoodle8.mods.galacticraft.core.wrappers.BlockMetaList;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;
import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.Map.Entry;

public class ClientProxyCore extends CommonProxyCore
{
	private static int renderIdTreasureChest;
	private static int renderIdParachest;
	private static int renderIdTorchUnlit;
	private static int renderIdBreathableAir;
	private static int renderIdOxygenPipe;
	private static int renderIdMeteor;
	private static int renderIdCraftingTable;
	private static int renderIdLandingPad;
	private static int renderIdMachine;

	public static FootprintRenderer footprintRenderer = new FootprintRenderer();

	public static List<String> flagRequestsSent = new ArrayList<String>();

	private static int renderIndexHeavyArmor;
	private static int renderIndexSensorGlasses;

	public static Set<Vector3> valueableBlocks = Sets.newHashSet();
	public static HashSet<BlockMetaList> detectableBlocks = Sets.newHashSet();

	public static Map<String, PlayerGearData> playerItemData = Maps.newHashMap();

	public static double playerPosX;
	public static double playerPosY;
	public static double playerPosZ;
	public static float playerRotationYaw;
	public static float playerRotationPitch;

	public static boolean lastSpacebarDown;

	public static int clientSpaceStationID = 0;

    public static MusicTicker.MusicType MUSIC_TYPE_MARS;

	public static EnumRarity galacticraftItem = EnumHelper.addRarity("GCRarity", EnumChatFormatting.BLUE, "Space");

	public static Map<String, String> capeMap = new HashMap<String, String>();

	public static InventoryExtended dummyInventory = new InventoryExtended();

	public static Document materialsTest;

	private static final ResourceLocation underOilTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/misc/underoil.png");

	private static float numbers[] = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 };
	private static FloatBuffer scaleup = BufferUtils.createFloatBuffer(16 * Float.SIZE);
	public static float globalRadius = Float.MAX_VALUE;
	public static double offsetY = 0D;
	public static float terrainHeight = Float.MAX_VALUE;
	private static boolean smallMoonActive = false;

	//private static int playerList;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		ClientProxyCore.scaleup.put(ClientProxyCore.numbers, 0, 16);

		ClientProxyCore.renderIndexSensorGlasses = RenderingRegistry.addNewArmourRendererPrefix("sensor");
		ClientProxyCore.renderIndexHeavyArmor = RenderingRegistry.addNewArmourRendererPrefix("titanium");
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
        Class[][] commonTypes =
        {
                {MusicTicker.MusicType.class, ResourceLocation.class, int.class, int.class},
        };
        MUSIC_TYPE_MARS = EnumHelper.addEnum(commonTypes, MusicTicker.MusicType.class, "MARS_JC", new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "galacticraft.musicSpace"), 12000, 24000);
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

		//ClientProxyCore.playerList = GLAllocation.generateDisplayLists(1);
	}

	public static void registerEntityRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityTier1Rocket.class, new RenderTier1Rocket(new ModelRocketTier1(), GalacticraftCore.ASSET_PREFIX, "rocketT1"));
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
		MinecraftForgeClient.registerItemRenderer(GCItems.rocketTier1, new ItemRendererTier1Rocket(new EntityTier1Rocket(FMLClientHandler.instance().getClient().theWorld), new ModelRocketTier1(), new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/rocketT1.png")));
		MinecraftForgeClient.registerItemRenderer(GCItems.buggy, new ItemRendererBuggy());
		MinecraftForgeClient.registerItemRenderer(GCItems.flag, new ItemRendererFlag());
		MinecraftForgeClient.registerItemRenderer(GCItems.key, new ItemRendererKey(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/treasure.png")));
		MinecraftForgeClient.registerItemRenderer(GCItems.meteorChunk, new ItemRendererMeteorChunk());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GCBlocks.spinThruster), new ItemRendererThruster());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GCBlocks.brightLamp), new ItemRendererArclamp());
	}

	public static void registerHandlers()
	{
        TickHandlerClient tickHandlerClient = new TickHandlerClient();
		FMLCommonHandler.instance().bus().register(tickHandlerClient);
        MinecraftForge.EVENT_BUS.register(tickHandlerClient);
		FMLCommonHandler.instance().bus().register(new KeyHandlerClient());
		ClientRegistry.registerKeyBinding(KeyHandlerClient.galaxyMap);
		ClientRegistry.registerKeyBinding(KeyHandlerClient.openFuelGui);
		ClientRegistry.registerKeyBinding(KeyHandlerClient.toggleAdvGoggles);
		MinecraftForge.EVENT_BUS.register(new ClientProxyCore());
	}

	public static void registerTileEntityRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAluminumWire.class, new TileEntityAluminumWireRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChest.class, new TileEntityTreasureChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityParaChest.class, new TileEntityParachestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNasaWorkbench.class, new TileEntityNasaWorkbenchRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolar.class, new TileEntitySolarPanelRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThruster.class, new TileEntityThrusterRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArclamp.class, new TileEntityArclampRenderer());
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
		ClientProxyCore.renderIdParachest = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRendererTreasureChest(ClientProxyCore.renderIdTreasureChest));
		RenderingRegistry.registerBlockHandler(new BlockRendererParachest(ClientProxyCore.renderIdParachest));
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
		if (blockID == GCBlocks.breatheableAir || blockID == GCBlocks.brightBreatheableAir)
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
		else if (blockID == GCBlocks.fuelLoader || blockID == GCBlocks.cargoLoader || blockID == GCBlocks.machineBase || blockID == GCBlocks.machineBase2 || blockID == GCBlocks.machineTiered || blockID == GCBlocks.oxygenCollector || blockID == GCBlocks.oxygenCompressor || blockID == GCBlocks.oxygenDetector || blockID == GCBlocks.oxygenDistributor || blockID == GCBlocks.oxygenSealer || blockID == GCBlocks.refinery)
		{
			return ClientProxyCore.renderIdMachine;
		}
		else if (blockID == GCBlocks.treasureChestTier1)
		{
			return ClientProxyCore.renderIdTreasureChest;
		}
		else if (blockID == GCBlocks.parachest)
		{
			return ClientProxyCore.renderIdParachest;
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
		ClientProxyCore.footprintRenderer.renderFootprints(FMLClientHandler.instance().getClient().thePlayer, partialTicks);
		MinecraftForge.EVENT_BUS.post(new EventSpecialRender(partialTicks));
	}

	public static class EventSpecialRender extends Event
	{
		public final float partialTicks;

		public EventSpecialRender(float partialTicks)
		{
			this.partialTicks = partialTicks;
		}
	}

	@Override
	public World getWorldForID(int dimensionID)
	{
		World world = FMLClientHandler.instance().getClient().theWorld;

		if (world != null && world.provider.dimensionId == dimensionID)
		{
			return world;
		}

		return null;
	}

	@SubscribeEvent
	public void onRenderPlayerPre(RenderPlayerEvent.Pre event)
	{
		GL11.glPushMatrix();

		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final EntityPlayer player = event.entityPlayer;
		final WorldProvider provider = minecraft.theWorld.provider;

		if (player.ridingEntity instanceof EntityAutoRocket)
		{
			EntityAutoRocket entity = (EntityAutoRocket) player.ridingEntity;
			GL11.glTranslatef(0, - (float) entity.getMountedYOffset() - 1F, 0);
			float anglePitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * event.partialRenderTick;
			float angleYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * event.partialRenderTick;
			GL11.glRotatef(-angleYaw, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(anglePitch, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(0, (float) entity.getMountedYOffset() + 1F, 0);
		}

		//Gravity - freefall - jetpack changes in player model orientation can go here
	}

	@SubscribeEvent
	public void onRenderPlayerPost(RenderPlayerEvent.Post event)
	{
		GL11.glPopMatrix();
	}

	@SubscribeEvent
	public void onRenderPlayerEquipped(RenderPlayerEvent.Specials.Pre event)
	{
		final Entity ridden = event.entityPlayer.ridingEntity;
		if (ridden instanceof EntityAutoRocket || ridden instanceof EntityLander)
		{
			event.setCanceled(true);
		}		
	}

	
	public static void adjustRenderPos(Entity entity, double offsetX, double offsetY, double offsetZ)
	{
		GL11.glPushMatrix();
		//Skip mobs in mobspawners
		//Note: can also look for (entity.posY!=0.0D || entity.posX!=0.0D || entity.posZ!=0.0) which filters hand-held entities and the player in an inventory GUI
		if (ClientProxyCore.smallMoonActive && (offsetX != 0.0D || offsetY != 0.0D || offsetZ != 0.0D))
		{
			final Minecraft minecraft = FMLClientHandler.instance().getClient();
			final EntityPlayerSP player = minecraft.thePlayer;
			if (player.posY > ClientProxyCore.terrainHeight + 8F && player.ridingEntity != entity && player != entity)
			{
				double globalArc = ClientProxyCore.globalRadius / 57.2957795D;

				int pX = MathHelper.floor_double(player.posX / 16D) << 4;
				int pZ = MathHelper.floor_double(player.posZ / 16D) << 4;

				int eX = MathHelper.floor_double(entity.posX / 16D) << 4;
				int eY = MathHelper.floor_double(entity.posY / 16D) << 4;
				int eZ = MathHelper.floor_double(entity.posZ / 16D) << 4;

				float dX = eX - pX;
				float dZ = eZ - pZ;

				float floatPX = (float) player.posX;
				float floatPZ = (float) player.posZ;

				if (dX > 0)
				{
					dX -= 16F;
					if (dX > 0)
					{
						dX -= floatPX - pX;
					}
				}
				else if (dX < 0)
				{
					dX += 16F;
					if (dX < 0)
					{
						dX += 16F - floatPX + pX;
					}
				}

				if (dZ > 0)
				{
					dZ -= 16F;
					if (dZ > 0)
					{
						dZ -= floatPZ - pZ;
					}
				}
				else if (dZ < 0)
				{
					dZ += 16F;
					if (dZ < 0)
					{
						dZ += 16F - floatPZ + pZ;
					}
				}

				float theta = (float) MathHelper.wrapAngleTo180_double(dX / globalArc);
				float phi = (float) MathHelper.wrapAngleTo180_double(dZ / globalArc);
				if (theta < 0)
				{
					theta += 360F;
				}
				if (phi < 0)
				{
					phi += 360F;
				}
				float ytranslate = ClientProxyCore.globalRadius + (float) (player.posY - entity.posY) + eY - ClientProxyCore.terrainHeight;
				GL11.glTranslatef(-dX + eX - floatPX + 8F, -ytranslate, -dZ + eZ - floatPZ + 8F);
				if (theta > 0)
				{
					GL11.glRotatef(theta, 0, 0, -1);
				}
				if (phi > 0)
				{
					GL11.glRotatef(phi, 1, 0, 0);
				}
				GL11.glTranslatef(floatPX - eX - 8F, ytranslate, floatPZ - eZ - 8F);
			}
		}
	}

	public static void adjustTileRenderPos(TileEntity tile, double offsetX, double offsetY, double offsetZ)
	{
		GL11.glPushMatrix();
		//Skip tiles in inventory or in player's hand etc
		if (ClientProxyCore.smallMoonActive && (offsetX != 0.0D || offsetY != 0.0D || offsetZ != 0.0D))
		{
			final Minecraft minecraft = FMLClientHandler.instance().getClient();
			final EntityPlayerSP player = minecraft.thePlayer;
			final WorldProvider provider = minecraft.theWorld.provider;
			if (provider instanceof WorldProviderMoon)
			{
				if (player.posY > ClientProxyCore.terrainHeight + 8F)
				{
					double globalArc = ClientProxyCore.globalRadius / 57.2957795D;

					int pX = MathHelper.floor_double(player.posX / 16D) << 4;
					int pZ = MathHelper.floor_double(player.posZ / 16D) << 4;

					int eX = tile.xCoord / 16 << 4;
					int eY = tile.yCoord / 16 << 4;
					int eZ = tile.zCoord / 16 << 4;

					float dX = eX - pX;
					float dZ = eZ - pZ;

					float floatPX = (float) player.posX;
					float floatPZ = (float) player.posZ;

					if (dX > 0)
					{
						dX -= 16F;
						if (dX > 0)
						{
							dX -= floatPX - pX;
						}
					}
					else if (dX < 0)
					{
						dX += 16F;
						if (dX < 0)
						{
							dX += 16F - floatPX + pX;
						}
					}

					if (dZ > 0)
					{
						dZ -= 16F;
						if (dZ > 0)
						{
							dZ -= floatPZ - pZ;
						}
					}
					else if (dZ < 0)
					{
						dZ += 16F;
						if (dZ < 0)
						{
							dZ += 16F - floatPZ + pZ;
						}
					}

					float theta = (float) MathHelper.wrapAngleTo180_double(dX / globalArc);
					float phi = (float) MathHelper.wrapAngleTo180_double(dZ / globalArc);
					if (theta < 0)
					{
						theta += 360F;
					}
					if (phi < 0)
					{
						phi += 360F;
					}
					float ytranslate = ClientProxyCore.globalRadius + (float) player.posY - tile.yCoord + eY - ClientProxyCore.terrainHeight;
					GL11.glTranslatef(-dX - floatPX + eX + 8F, -ytranslate, -dZ - floatPZ + eZ + 8F);
					if (theta > 0)
					{
						GL11.glRotatef(theta, 0, 0, -1);
					}
					if (phi > 0)
					{
						GL11.glRotatef(phi, 1, 0, 0);
					}
					GL11.glTranslatef(floatPX - eX - 8F, ytranslate, floatPZ - eZ - 8F);
				}
			}
		}
	}

	public static void orientCamera(float partialTicks)
	{
		((GCEntityClientPlayerMP) FMLClientHandler.instance().getClient().thePlayer).reOrientCamera(partialTicks);
	}

	public static void adjustRenderCamera()
	{
		GL11.glPushMatrix();
	}

	public static void setPositionList(WorldRenderer rend, int glRenderList)
	{
		GL11.glNewList(glRenderList + 3, GL11.GL_COMPILE);

		EntityLivingBase entitylivingbase = FMLClientHandler.instance().getClient().renderViewEntity;

		if (entitylivingbase != null)
		{
			if (rend.worldObj.provider instanceof WorldProviderMoon)
			{
				//See what a small moon looks like, for demo purposes
				//Note: terrainHeight must never be less than globalRadius
				ClientProxyCore.globalRadius = 300F;
				ClientProxyCore.terrainHeight = 64F;

				if (entitylivingbase.posY > ClientProxyCore.terrainHeight + 8F)
				{
					ClientProxyCore.smallMoonActive = true;
					double globalArc = ClientProxyCore.globalRadius / 57.2957795D;
					float globeRadius = ClientProxyCore.globalRadius - ClientProxyCore.terrainHeight;

					int pX = MathHelper.floor_double(entitylivingbase.posX / 16D) << 4;
					int pZ = MathHelper.floor_double(entitylivingbase.posZ / 16D) << 4;

					float dX = rend.posX - pX;
					float dZ = rend.posZ - pZ;
					float scalerX = 0;
					float scalerZ = 0;

					if (dX > 0)
					{
						dX -= 16F;
						if (dX > 0)
						{
							dX -= entitylivingbase.posX - pX;
							if (dX < 16F)
							{
								scalerX = 16F - ((float) entitylivingbase.posX - pX);
							}
							else
							{
								scalerX = 16F;
							}
						}
					}
					else if (dX < 0)
					{
						dX += 16F;
						if (dX < 0)
						{
							dX += 16F - (entitylivingbase.posX - pX);
							if (dX > -16F)
							{
								scalerX = (float) entitylivingbase.posX - pX;
							}
							else
							{
								scalerX = 16F;
							}
						}
					}

					if (dZ > 0)
					{
						dZ -= 16F;
						if (dZ > 0)
						{
							dZ -= entitylivingbase.posZ - pZ;
							if (dZ < 16F)
							{
								scalerZ = 16F - ((float) entitylivingbase.posZ - pZ);
							}
							else
							{
								scalerZ = 16F;
							}
						}
					}
					else if (dZ < 0)
					{
						dZ += 16F;
						if (dZ < 0)
						{
							dZ += 16F - (entitylivingbase.posZ - pZ);
							if (dZ > -16F)
							{
								scalerZ = (float) entitylivingbase.posZ - pZ;
							}
							else
							{
								scalerZ = 16F;
							}
						}
					}

					float origClipX = rend.posXClip;
					float origClipY = rend.posYClip;
					float origClipZ = rend.posZClip;

					float theta = (float) MathHelper.wrapAngleTo180_double(dX / globalArc);
					float phi = (float) MathHelper.wrapAngleTo180_double(dZ / globalArc);
					if (theta < 0)
					{
						theta += 360F;
					}
					if (phi < 0)
					{
						phi += 360F;
					}
					GL11.glTranslatef(origClipX - dX + 8F, -globeRadius + 8F, origClipZ - dZ + 8F);
					if (theta > 0)
					{
						GL11.glRotatef(theta, 0, 0, -1F);
					}
					if (phi > 0)
					{
						GL11.glRotatef(phi, 1F, 0, 0);
					}
					GL11.glTranslatef(-8F, origClipY + globeRadius - 8F, -8F);
					if (dX != 0 || dZ != 0)
					{
						float scalex = (ClientProxyCore.globalRadius * 2F + scalerX) / ClientProxyCore.globalRadius / 2F;
						float scalez = (ClientProxyCore.globalRadius * 2F + scalerZ) / ClientProxyCore.globalRadius / 2F;
						ClientProxyCore.scaleup.rewind();
						ClientProxyCore.scaleup.put(scalex);
						ClientProxyCore.scaleup.position(10);
						ClientProxyCore.scaleup.put(scalez);
						ClientProxyCore.scaleup.rewind();
						GL11.glMultMatrix(ClientProxyCore.scaleup);
						GL11.glTranslatef(-8F * (scalex - 1F), 0, -8F * (scalez - 1F));
					}
					GL11.glTranslatef(-origClipX, -origClipY, -origClipZ);
					ClientProxyCore.offsetY = rend.posY - ClientProxyCore.terrainHeight;
				}
				else
				{
					ClientProxyCore.smallMoonActive = false;
					ClientProxyCore.offsetY = 0;
				}
			}
			else
			{
				ClientProxyCore.terrainHeight = Float.MAX_VALUE;
				ClientProxyCore.globalRadius = Float.MAX_VALUE;
				ClientProxyCore.smallMoonActive = false;
				ClientProxyCore.offsetY = 0;
			}
		}
		GL11.glEndList();
	}

	@Override
	public EntityPlayer getPlayerFromNetHandler(INetHandler handler)
	{
		if (handler instanceof NetHandlerPlayServer)
		{
			return ((NetHandlerPlayServer) handler).playerEntity;
		}
		else
		{
			return FMLClientHandler.instance().getClientPlayerEntity();
		}
	}

	//For testing purposes only
	public void addVertex(double x, double y, double z)
	{
		double var7 = 1 + (y + ClientProxyCore.offsetY) / ClientProxyCore.globalRadius;
		x += (x % 16 - 8) * var7 + 8;
		z += (z % 16 - 8) * var7 + 8;
	}
}
