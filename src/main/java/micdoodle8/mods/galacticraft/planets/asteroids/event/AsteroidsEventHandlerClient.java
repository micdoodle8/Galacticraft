package micdoodle8.mods.galacticraft.planets.asteroids.event;

import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP.ThermalArmorEvent;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP.ThermalArmorEvent.ArmorAddResult;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore.EventSpecialRender;
import micdoodle8.mods.galacticraft.planets.asteroids.client.SkyProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.NetworkRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AsteroidsEventHandlerClient
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		WorldClient world = minecraft.theWorld;

		if (world != null)
		{
			if (world.provider instanceof WorldProviderAsteroids)
			{
				if (world.provider.getSkyRenderer() == null)
				{
					world.provider.setSkyRenderer(new SkyProviderAsteroids());
				}

				if (world.provider.getCloudRenderer() == null)
				{
					world.provider.setCloudRenderer(new CloudRenderer());
				}
			}
		}			
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onSpecialRender(EventSpecialRender event)
	{
		NetworkRenderer.renderNetworks(FMLClientHandler.instance().getClient().theWorld, event.partialTicks);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onThermalArmorEvent(ThermalArmorEvent event)
	{
		if (event.armorStack == null)
		{
			event.setArmorAddResult(ArmorAddResult.REMOVE);
			return;
		}

		if (event.armorStack.getItem() == AsteroidsItems.itemThermalPadding && event.armorStack.getItemDamage() == event.armorIndex)
		{
			event.setArmorAddResult(ArmorAddResult.ADD);
			return;
		}
		
		event.setArmorAddResult(ArmorAddResult.NOTHING);
	}
}
