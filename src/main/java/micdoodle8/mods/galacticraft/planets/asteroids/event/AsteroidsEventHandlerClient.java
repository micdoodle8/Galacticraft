package micdoodle8.mods.galacticraft.planets.asteroids.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore.EventSpecialRender;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.client.SkyProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.NetworkRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

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
                    world.provider.setSkyRenderer(new SkyProviderAsteroids((IGalacticraftWorldProvider) world.provider));
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
    public void onRingRender(CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre renderEvent)
    {
        if (renderEvent.celestialBody.equals(AsteroidsModule.planetAsteroids))
        {
        	if (FMLClientHandler.instance().getClient().currentScreen instanceof GuiCelestialSelection)
        		GL11.glColor4f(0.7F, 0.0F, 0.0F, 0.5F);
        	else
        		GL11.glColor4f(0.3F, 0.1F, 0.1F, 1.0F);
            renderEvent.setCanceled(true);
            GL11.glBegin(GL11.GL_LINE_LOOP);

            final float theta = (float) (2 * Math.PI / 90);
            final float cos = (float) Math.cos(theta);
            final float sin = (float) Math.sin(theta);

            float min = 72.0F;
            float max = 78.0F;

            float x = max * renderEvent.celestialBody.getRelativeDistanceFromCenter().unScaledDistance;
            float y = 0;

            float temp;
            for (int i = 0; i < 90; i++)
            {
                GL11.glVertex2f(x, y);

                temp = x;
                x = cos * x - sin * y;
                y = sin * temp + cos * y;
            }

            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);

            x = min * renderEvent.celestialBody.getRelativeDistanceFromCenter().unScaledDistance;
            y = 0;

            for (int i = 0; i < 90; i++)
            {
                GL11.glVertex2f(x, y);

                temp = x;
                x = cos * x - sin * y;
                y = sin * temp + cos * y;
            }

            GL11.glEnd();
            GL11.glColor4f(0.7F, 0.0F, 0.0F, 0.1F);
            GL11.glBegin(GL11.GL_QUADS);

            x = min * renderEvent.celestialBody.getRelativeDistanceFromCenter().unScaledDistance;
            y = 0;
            float x2 = max * renderEvent.celestialBody.getRelativeDistanceFromCenter().unScaledDistance;
            float y2 = 0;

            for (int i = 0; i < 90; i++)
            {
                GL11.glVertex2f(x2, y2);
                GL11.glVertex2f(x, y);

                temp = x;
                x = cos * x - sin * y;
                y = sin * temp + cos * y;
                temp = x2;
                x2 = cos * x2 - sin * y2;
                y2 = sin * temp + cos * y2;

                GL11.glVertex2f(x, y);
                GL11.glVertex2f(x2, y2);
            }

            GL11.glEnd();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onBodyRender(CelestialBodyRenderEvent.Pre renderEvent)
    {
        if (renderEvent.celestialBody.equals(AsteroidsModule.planetAsteroids))
        {
            GL11.glRotatef(Sys.getTime() / 10.0F % 360, 0, 0, 1);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onSpecialRender(EventSpecialRender event)
    {
        NetworkRenderer.renderNetworks(FMLClientHandler.instance().getClient().theWorld, event.partialTicks);
    }
}
