package micdoodle8.mods.galacticraft.mars.proxy;

import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderBubble;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderTier1Rocket;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemRendererKey;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.mars.client.SkyProviderMars;
import micdoodle8.mods.galacticraft.mars.client.fx.EntityFXDropPatricle;
import micdoodle8.mods.galacticraft.mars.client.gui.GuiLaunchController;
import micdoodle8.mods.galacticraft.mars.client.gui.GuiSlimeling;
import micdoodle8.mods.galacticraft.mars.client.gui.GuiSlimelingFeed;
import micdoodle8.mods.galacticraft.mars.client.gui.GuiTerraformer;
import micdoodle8.mods.galacticraft.mars.client.model.ModelTier2Rocket;
import micdoodle8.mods.galacticraft.mars.client.render.block.BlockRendererMachine;
import micdoodle8.mods.galacticraft.mars.client.render.block.BlockRendererSlimelingEgg;
import micdoodle8.mods.galacticraft.mars.client.render.block.BlockRendererTintedGlassPane;
import micdoodle8.mods.galacticraft.mars.client.render.block.BlockRendererTreasureChest;
import micdoodle8.mods.galacticraft.mars.client.render.block.BlockRendererVines;
import micdoodle8.mods.galacticraft.mars.client.render.entity.RenderCargoRocket;
import micdoodle8.mods.galacticraft.mars.client.render.entity.RenderCreeperBoss;
import micdoodle8.mods.galacticraft.mars.client.render.entity.RenderLandingBalloons;
import micdoodle8.mods.galacticraft.mars.client.render.entity.RenderProjectileTNT;
import micdoodle8.mods.galacticraft.mars.client.render.entity.RenderSlimeling;
import micdoodle8.mods.galacticraft.mars.client.render.entity.RenderSludgeling;
import micdoodle8.mods.galacticraft.mars.client.render.item.ItemRendererMachine;
import micdoodle8.mods.galacticraft.mars.client.render.item.ItemRendererTier2Rocket;
import micdoodle8.mods.galacticraft.mars.client.render.tile.TileEntityRendererCryoChamber;
import micdoodle8.mods.galacticraft.mars.client.render.tile.TileEntityRendererTreasureChest;
import micdoodle8.mods.galacticraft.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.mars.entities.EntityCreeperBoss;
import micdoodle8.mods.galacticraft.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.mars.entities.EntityProjectileTNT;
import micdoodle8.mods.galacticraft.mars.entities.EntityTier2Rocket;
import micdoodle8.mods.galacticraft.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.mars.entities.EntitySludgeling;
import micdoodle8.mods.galacticraft.mars.entities.EntityTerraformBubble;
import micdoodle8.mods.galacticraft.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.mars.items.MarsItems.EnumArmorIndexMars;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityTerraformer;
import micdoodle8.mods.galacticraft.mars.tile.TileEntityTreasureChestMars;
import micdoodle8.mods.galacticraft.mars.util.ConfigManagerMars;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
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
		RenderingRegistry.registerBlockHandler(new BlockRendererVines(ClientProxyMars.vineRenderID));
		ClientProxyMars.eggRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRendererSlimelingEgg(ClientProxyMars.eggRenderID));
		ClientProxyMars.treasureRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRendererTreasureChest(ClientProxyMars.treasureRenderID));
		ClientProxyMars.machineRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRendererMachine(ClientProxyMars.machineRenderID));
		ClientProxyMars.tintedGlassRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRendererTintedGlassPane(ClientProxyMars.tintedGlassRenderID));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
	}

	@Override
	public void registerRenderInformation()
	{
		IModelCustom chamberModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftMars.ASSET_DOMAIN, "models/chamber.obj"));
		IModelCustom cargoRocketModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftMars.ASSET_DOMAIN, "models/cargoRocket.obj"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestMars.class, new TileEntityRendererTreasureChest());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCryogenicChamber.class, new TileEntityRendererCryoChamber(chamberModel));
		RenderingRegistry.registerEntityRenderingHandler(EntitySludgeling.class, new RenderSludgeling());
		RenderingRegistry.registerEntityRenderingHandler(EntitySlimeling.class, new RenderSlimeling());
		RenderingRegistry.registerEntityRenderingHandler(EntityCreeperBoss.class, new RenderCreeperBoss());
		RenderingRegistry.registerEntityRenderingHandler(EntityTier2Rocket.class, new RenderTier1Rocket(new ModelTier2Rocket(), GalacticraftMars.ASSET_DOMAIN, "rocketT2"));
		RenderingRegistry.registerEntityRenderingHandler(EntityTerraformBubble.class, new RenderBubble(0.25F, 1.0F, 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectileTNT.class, new RenderProjectileTNT());
		RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new RenderLandingBalloons());
		RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new RenderLandingBalloons());
		RenderingRegistry.registerEntityRenderingHandler(EntityCargoRocket.class, new RenderCargoRocket(cargoRocketModel));
		RenderingRegistry.addNewArmourRendererPrefix("desh");
		MinecraftForgeClient.registerItemRenderer(MarsItems.spaceship, new ItemRendererTier2Rocket(cargoRocketModel));
		MinecraftForgeClient.registerItemRenderer(MarsItems.key, new ItemRendererKey(new ResourceLocation(GalacticraftMars.ASSET_DOMAIN, "textures/model/treasure.png")));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MarsBlocks.machine), new ItemRendererMachine(chamberModel));
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
	public int getArmorRenderID(EnumArmorIndexMars type)
	{
		return 0;
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
					var21 = new EntityFXDropPatricle(var14.theWorld, var2, var4, var6);
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
		AxisAlignedBB axisAlignedBB = player.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D);
        int i = MathHelper.floor_double(axisAlignedBB.minX);
        int j = MathHelper.floor_double(axisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(axisAlignedBB.minY);
        int l = MathHelper.floor_double(axisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(axisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(axisAlignedBB.maxZ + 1.0D);

        for (int k1 = i; k1 < j; ++k1)
        {
            for (int l1 = k; l1 < l; ++l1)
            {
                for (int i2 = i1; i2 < j1; ++i2)
                {
                    if (player.worldObj.getBlock(k1, l1, i2) == MarsBlocks.blockSludge)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
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
			FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSlimeling(slimeling));
			break;
		case 1:
			FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSlimelingFeed(slimeling));
			break;
		}
	}

	public static class TickHandlerClient
	{
		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public void onClientTick(ClientTickEvent event)
		{
			Minecraft minecraft = Minecraft.getMinecraft();
			WorldClient world = minecraft.theWorld;

			if (world != null)
			{
				if (world.provider instanceof WorldProviderMars)
				{
					if (world.provider.getSkyRenderer() == null)
					{
						world.provider.setSkyRenderer(new SkyProviderMars());
					}

					if (world.provider.getCloudRenderer() == null)
					{
						world.provider.setCloudRenderer(new CloudRenderer());
					}
				}

				for (int i = 0; i < world.loadedEntityList.size(); i++)
				{
					final Entity e = (Entity) world.loadedEntityList.get(i);

//					if (e != null)
//					{
//						if (e instanceof GCMarsEntityRocketT2)
//						{
//							GCMarsEntityRocketT2 eship = (GCMarsEntityRocketT2) e;
//
//							if (eship.rocketSoundUpdater == null)
//							{
////								eship.rocketSoundUpdater = new GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager, eship, FMLClientHandler.instance().getClient().thePlayer);
//							}
//						}
//						else if (e instanceof GCMarsEntityCargoRocket)
//						{
//							GCMarsEntityCargoRocket eship = (GCMarsEntityCargoRocket) e;
//
//							if (eship.rocketSoundUpdater == null)
//							{
////								eship.rocketSoundUpdater = new GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager, eship, FMLClientHandler.instance().getClient().thePlayer);
//							}
//						}
//					} TODO Fix rocket sound updater
				}
			}			
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);

		if (ID == ConfigManagerMars.idGuiMachine)
		{
			if (tile instanceof TileEntityTerraformer)
			{
				return new GuiTerraformer(player.inventory, (TileEntityTerraformer) tile);
			}
			else if (tile instanceof TileEntityLaunchController)
			{
				return new GuiLaunchController(player.inventory, (TileEntityLaunchController) tile);
			}
		}

		return null;
	}
}
