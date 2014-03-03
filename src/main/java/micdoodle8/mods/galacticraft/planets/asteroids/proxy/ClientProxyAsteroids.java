package micdoodle8.mods.galacticraft.planets.asteroids.proxy;

import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemRendererGrappleGun;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.client.SkyProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
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
public class ClientProxyAsteroids extends CommonProxyAsteroids
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(new TickHandlerClient());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		IModelCustom grappleGunModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_DOMAIN, "models/grapplegun.obj"));
		IModelCustom grappleModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_DOMAIN, "models/grapple.obj"));
		MinecraftForgeClient.registerItemRenderer(AsteroidsItems.itemGrapple, new ItemRendererGrappleGun(grappleGunModel, grappleModel));
	}

	@Override
	public void spawnParticle(String particleID, double x, double y, double z)
	{
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
				if (world.provider instanceof WorldProviderMars || world.provider instanceof WorldProviderAsteroids)
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
			}			
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}
