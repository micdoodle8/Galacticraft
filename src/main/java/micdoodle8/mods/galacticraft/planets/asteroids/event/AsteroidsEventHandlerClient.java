package micdoodle8.mods.galacticraft.planets.asteroids.event;

import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore.EventSpecialRender;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.NetworkRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class AsteroidsEventHandlerClient
{
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        ClientWorld world = minecraft.world;

        if (world != null)
        {
            if (world.getDimension() instanceof DimensionAsteroids)
            {
//                if (world.getDimension().getSkyRenderer() == null)
//                {
//                    world.getDimension().setSkyRenderer(new SkyProviderAsteroids((IGalacticraftDimension) world.getDimension()));
//                } TODO Sky provider

                if (world.getDimension().getCloudRenderer() == null)
                {
                    world.getDimension().setCloudRenderer(new CloudRenderer());
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRingRender(CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre renderEvent)
    {
        if (renderEvent.celestialBody.equals(AsteroidsModule.planetAsteroids))
        {
            float alpha = 1.0F;
            Screen screen = Minecraft.getInstance().currentScreen;
            if (screen instanceof GuiCelestialSelection)
            {
                alpha = ((GuiCelestialSelection) screen).getAlpha(renderEvent.celestialBody);
                GL11.glColor4f(0.7F, 0.0F, 0.0F, alpha / 2.0F);
            }
            else
            {
                GL11.glColor4f(0.3F, 0.1F, 0.1F, 1.0F);
            }
            renderEvent.setCanceled(true);
            GL11.glBegin(GL11.GL_LINE_LOOP);

            final float theta = Constants.twoPI / 90;
            final float cos = MathHelper.cos(theta);
            final float sin = MathHelper.sin(theta);

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
            GL11.glColor4f(0.7F, 0.0F, 0.0F, alpha / 10.0F);
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onBodyRender(CelestialBodyRenderEvent.Pre renderEvent)
    {
        if (renderEvent.celestialBody.equals(AsteroidsModule.planetAsteroids))
        {
            GL11.glRotatef(ClientUtil.getClientTimeTotal() / 10.0F % 360, 0, 0, 1);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onSpecialRender(EventSpecialRender event)
    {
        NetworkRenderer.renderNetworks(Minecraft.getInstance().world, event.partialTicks);
    }
}
