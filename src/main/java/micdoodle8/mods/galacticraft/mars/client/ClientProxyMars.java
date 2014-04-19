package micdoodle8.mods.galacticraft.mars.client;

import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderBubble;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderTier1Rocket;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererKey;
import micdoodle8.mods.galacticraft.mars.CommonProxyMars;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.client.gui.GCMarsGuiLaunchController;
import micdoodle8.mods.galacticraft.mars.client.gui.GCMarsGuiSlimeling;
import micdoodle8.mods.galacticraft.mars.client.gui.GCMarsGuiSlimelingFeed;
import micdoodle8.mods.galacticraft.mars.client.gui.GCMarsGuiTerraformer;
import micdoodle8.mods.galacticraft.mars.client.model.GCMarsModelSpaceshipTier2;
import micdoodle8.mods.galacticraft.mars.client.render.block.GCMarsBlockRendererMachine;
import micdoodle8.mods.galacticraft.mars.client.render.block.GCMarsBlockRendererRock;
import micdoodle8.mods.galacticraft.mars.client.render.block.GCMarsBlockRendererTintedGlassPane;
import micdoodle8.mods.galacticraft.mars.client.render.block.GCMarsBlockRendererTreasureChest;
import micdoodle8.mods.galacticraft.mars.client.render.block.GCMarsBlockRendererVine;
import micdoodle8.mods.galacticraft.mars.client.render.entity.GCMarsRenderCargoRocket;
import micdoodle8.mods.galacticraft.mars.client.render.entity.GCMarsRenderCreeperBoss;
import micdoodle8.mods.galacticraft.mars.client.render.entity.GCMarsRenderLandingBalloons;
import micdoodle8.mods.galacticraft.mars.client.render.entity.GCMarsRenderProjectileTNT;
import micdoodle8.mods.galacticraft.mars.client.render.entity.GCMarsRenderSlimeling;
import micdoodle8.mods.galacticraft.mars.client.render.entity.GCMarsRenderSludgeling;
import micdoodle8.mods.galacticraft.mars.client.render.item.GCMarsItemRendererMachine;
import micdoodle8.mods.galacticraft.mars.client.render.item.GCMarsItemRendererSpaceshipT2;
import micdoodle8.mods.galacticraft.mars.client.render.tile.GCMarsTileEntityCryogenicChamberRenderer;
import micdoodle8.mods.galacticraft.mars.client.render.tile.GCMarsTileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.mars.dimension.GCMarsWorldProvider;
import micdoodle8.mods.galacticraft.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.mars.entities.EntityCreeperBoss;
import micdoodle8.mods.galacticraft.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.mars.entities.EntityProjectileTNT;
import micdoodle8.mods.galacticraft.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.mars.entities.EntitySludgeling;
import micdoodle8.mods.galacticraft.mars.entities.EntityTerraformBubble;
import micdoodle8.mods.galacticraft.mars.entities.EntityTier2Rocket;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityTerraformer;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityTreasureChest;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * ClientProxyMars.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ClientProxyMars extends CommonProxyMars
{
	private static int vineRenderID;
	private static int eggRenderID;
	private static int treasureRenderID;
	private static int machineRenderID;
	private static int tintedGlassRenderID;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(new TickHandlerClient());
		ClientProxyMars.vineRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new GCMarsBlockRendererVine(ClientProxyMars.vineRenderID));
		ClientProxyMars.eggRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new GCMarsBlockRendererRock(ClientProxyMars.eggRenderID));
		ClientProxyMars.treasureRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new GCMarsBlockRendererTreasureChest(ClientProxyMars.treasureRenderID));
		ClientProxyMars.machineRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new GCMarsBlockRendererMachine(ClientProxyMars.machineRenderID));
		ClientProxyMars.tintedGlassRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new GCMarsBlockRendererTintedGlassPane(ClientProxyMars.tintedGlassRenderID));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
	}

	@Override
	public void registerRenderInformation()
	{
		IModelCustom chamberModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "models/chamber.obj"));
		IModelCustom cargoRocketModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "models/cargoRocket.obj"));
		ClientRegistry.bindTileEntitySpecialRenderer(GCMarsTileEntityTreasureChest.class, new GCMarsTileEntityTreasureChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(GCMarsTileEntityCryogenicChamber.class, new GCMarsTileEntityCryogenicChamberRenderer(chamberModel));
		RenderingRegistry.registerEntityRenderingHandler(EntitySludgeling.class, new GCMarsRenderSludgeling());
		RenderingRegistry.registerEntityRenderingHandler(EntitySlimeling.class, new GCMarsRenderSlimeling());
		RenderingRegistry.registerEntityRenderingHandler(EntityCreeperBoss.class, new GCMarsRenderCreeperBoss());
		RenderingRegistry.registerEntityRenderingHandler(EntityTier2Rocket.class, new RenderTier1Rocket(new GCMarsModelSpaceshipTier2(), GalacticraftMars.TEXTURE_DOMAIN, "rocketT2"));
		RenderingRegistry.registerEntityRenderingHandler(EntityTerraformBubble.class, new RenderBubble(0.25F, 1.0F, 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectileTNT.class, new GCMarsRenderProjectileTNT());
		RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new GCMarsRenderLandingBalloons());
		RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new GCMarsRenderLandingBalloons());
		RenderingRegistry.registerEntityRenderingHandler(EntityCargoRocket.class, new GCMarsRenderCargoRocket(cargoRocketModel));
		RenderingRegistry.addNewArmourRendererPrefix("desh");
		MinecraftForgeClient.registerItemRenderer(GCMarsItems.spaceship, new GCMarsItemRendererSpaceshipT2(cargoRocketModel));
		MinecraftForgeClient.registerItemRenderer(GCMarsItems.key, new ItemRendererKey(new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/treasure.png")));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GCMarsBlocks.machine), new GCMarsItemRendererMachine(chamberModel));
	}

	@Override
	public int getVineRenderID()
	{
		return ClientProxyMars.vineRenderID;
	}

	@Override
	public int getEggRenderID()
	{
		return ClientProxyMars.eggRenderID;
	}

	@Override
	public int getTreasureRenderID()
	{
		return ClientProxyMars.treasureRenderID;
	}

	@Override
	public int getMachineRenderID()
	{
		return ClientProxyMars.machineRenderID;
	}

	@Override
	public int getTintedGlassPaneRenderID()
	{
		return ClientProxyMars.tintedGlassRenderID;
	}

	@Override
	public void spawnParticle(String var1, double var2, double var4, double var6)
	{
		final Minecraft var14 = FMLClientHandler.instance().getClient();

		if (var14 != null && var14.renderViewEntity != null && var14.effectRenderer != null)
		{
			final double var15 = var14.renderViewEntity.posX - var2;
			final double var17 = var14.renderViewEntity.posY - var4;
			final double var19 = var14.renderViewEntity.posZ - var6;
			Object var21 = null;
			final double var22 = 64.0D;

			if (var15 * var15 + var17 * var17 + var19 * var19 < var22 * var22)
			{
				if (var1.equals("sludgeDrip"))
				{
					var21 = new EntityDropParticleFX(var14.theWorld, var2, var4, var6, GCMarsBlocks.bacterialSludge);
				}
			}

			if (var21 != null)
			{
				((EntityFX) var21).prevPosX = ((EntityFX) var21).posX;
				((EntityFX) var21).prevPosY = ((EntityFX) var21).posY;
				((EntityFX) var21).prevPosZ = ((EntityFX) var21).posZ;
				var14.effectRenderer.addEffect((EntityFX) var21);
			}
		}
	}

	public static boolean handleBacterialMovement(EntityPlayer player)
	{
		return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), GCMarsBlocks.bacterialSludge);
	}

	public static boolean handleLavaMovement(EntityPlayer player)
	{
		return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
	}

	public static boolean handleWaterMovement(EntityPlayer player)
	{
		return player.worldObj.isMaterialInBB(player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.water);
	}

	public static boolean handleLiquidMovement(EntityPlayer player)
	{
		return ClientProxyMars.handleBacterialMovement(player) || ClientProxyMars.handleLavaMovement(player) || ClientProxyMars.handleWaterMovement(player);
	}

	@Override
	public void opengSlimelingGui(EntitySlimeling slimeling, int gui)
	{
		switch (gui)
		{
		case 0:
			FMLClientHandler.instance().getClient().displayGuiScreen(new GCMarsGuiSlimeling(slimeling));
			break;
		case 1:
			FMLClientHandler.instance().getClient().displayGuiScreen(new GCMarsGuiSlimelingFeed(slimeling));
			break;
		}
	}

	public static class TickHandlerClient
	{
		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public void onClientTick(ClientTickEvent event)
		{
			final Minecraft minecraft = FMLClientHandler.instance().getClient();

			final WorldClient world = minecraft.theWorld;

			if (world != null)
			{
				if (world.provider instanceof GCMarsWorldProvider)
				{
					if (world.provider.getSkyRenderer() == null)
					{
						world.provider.setSkyRenderer(new GCMarsSkyProvider());
					}

					if (world.provider.getCloudRenderer() == null)
					{
						world.provider.setCloudRenderer(new CloudRenderer());
					}
				}

//				for (int i = 0; i < world.loadedEntityList.size(); i++)
//				{
//					final Entity e = (Entity) world.loadedEntityList.get(i);
//
//					if (e != null)
//					{
//						if (e instanceof EntityRocketT2)
//						{
//							EntityRocketT2 eship = (EntityRocketT2) e;
//
//							if (eship.rocketSoundUpdater == null)
//							{
//								eship.rocketSoundUpdater = new GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager, eship, FMLClientHandler.instance().getClient().thePlayer);
//							}
//						}
//						else if (e instanceof EntityCargoRocket)
//						{
//							EntityCargoRocket eship = (EntityCargoRocket) e;
//
//							if (eship.rocketSoundUpdater == null)
//							{
//								eship.rocketSoundUpdater = new GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager, eship, FMLClientHandler.instance().getClient().thePlayer);
//							}
//						}
//					}
//				}
			}
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);

		if (ID == GCMarsConfigManager.idGuiMachine)
		{
			if (tile instanceof GCMarsTileEntityTerraformer)
			{
				return new GCMarsGuiTerraformer(player.inventory, (GCMarsTileEntityTerraformer) tile);
			}
			else if (tile instanceof GCMarsTileEntityLaunchController)
			{
				return new GCMarsGuiLaunchController(player.inventory, (GCMarsTileEntityLaunchController) tile);
			}
		}

		return null;
	}
}
