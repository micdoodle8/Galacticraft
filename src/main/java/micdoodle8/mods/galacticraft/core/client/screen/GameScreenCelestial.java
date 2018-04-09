package micdoodle8.mods.galacticraft.core.client.screen;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.client.IScreenManager;
import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.render.RenderPlanet;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.nio.DoubleBuffer;

public class GameScreenCelestial implements IGameScreen
{
    private TextureManager renderEngine;

    private float frameA;
    private float frameBx;
    private float frameBy;
    private float centreX;
    private float centreY;
    private float scale;

    private final int lineSegments = 90;
    private final float cos = MathHelper.cos(Constants.twoPI / lineSegments);
    private final float sin = MathHelper.sin(Constants.twoPI / lineSegments);

    private DoubleBuffer planes;


    public GameScreenCelestial()
    {
        if (GCCoreUtil.getEffectiveSide().isClient())
        {
            renderEngine = FMLClientHandler.instance().getClient().renderEngine;
            planes = BufferUtils.createDoubleBuffer(4 * Double.SIZE);
        }
    }

    @Override
    public void setFrameSize(float frameSize)
    {
        this.frameA = frameSize;
    }

    @Override
    public void render(int type, float ticks, float scaleX, float scaleY, IScreenManager scr)
    {
        centreX = scaleX / 2;
        centreY = scaleY / 2;
        frameBx = scaleX - frameA;
        frameBy = scaleY - frameA;
        this.scale = Math.max(scaleX, scaleY) - 0.2F;

        drawBlackBackground(0.0F);

        planeEquation(frameA, frameA, 0, frameA, frameBy, 0, frameA, frameBy, 1);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE0, planes);
        GL11.glEnable(GL11.GL_CLIP_PLANE0);
        planeEquation(frameBx, frameBy, 0, frameBx, frameA, 0, frameBx, frameA, 1);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE1, planes);
        GL11.glEnable(GL11.GL_CLIP_PLANE1);
        planeEquation(frameA, frameBy, 0, frameBx, frameBy, 0, frameBx, frameBy, 1);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE2, planes);
        GL11.glEnable(GL11.GL_CLIP_PLANE2);
        planeEquation(frameBx, frameA, 0, frameA, frameA, 0, frameA, frameA, 1);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE3, planes);
        GL11.glEnable(GL11.GL_CLIP_PLANE3);

        switch (type)
        {
        case 2:
            WorldProvider wp = scr.getWorldProvider();
            CelestialBody body = null;
            if (wp instanceof IGalacticraftWorldProvider)
            {
                body = ((IGalacticraftWorldProvider) wp).getCelestialBody();
            }
            if (body == null)
            {
                body = GalacticraftCore.planetOverworld;
            }
            drawCelestialBodies(body, ticks);
            break;
        case 3:
            drawCelestialBodiesZ(GalacticraftCore.planetOverworld, ticks);
            break;
        case 4:
            drawPlanetsTest(ticks);
            break;
        }

        GL11.glDisable(GL11.GL_CLIP_PLANE3);
        GL11.glDisable(GL11.GL_CLIP_PLANE2);
        GL11.glDisable(GL11.GL_CLIP_PLANE1);
        GL11.glDisable(GL11.GL_CLIP_PLANE0);
    }

    private void drawBlackBackground(float greyLevel)
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tess = Tessellator.getInstance();
        WorldRenderer worldRenderer = tess.getWorldRenderer();
        GL11.glColor4f(greyLevel, greyLevel, greyLevel, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        worldRenderer.pos(frameA, frameBy, 0.005F).endVertex();
        worldRenderer.pos(frameBx, frameBy, 0.005F).endVertex();
        worldRenderer.pos(frameBx, frameA, 0.005F).endVertex();
        worldRenderer.pos(frameA, frameA, 0.005F).endVertex();
        tess.draw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void drawCelestialBodies(CelestialBody body, float ticks)
    {
        Star star = null;
        SolarSystem solarSystem = null;
        if (body instanceof Planet)
        {
            solarSystem = ((Planet) body).getParentSolarSystem();
        }
        else if (body instanceof Moon)
        {
            solarSystem = ((Moon) body).getParentPlanet().getParentSolarSystem();
        }
        else if (body instanceof Satellite)
        {
            solarSystem = ((Satellite) body).getParentPlanet().getParentSolarSystem();
        }

        if (solarSystem == null)
        {
            solarSystem = GalacticraftCore.solarSystemSol;
        }
        star = solarSystem.getMainStar();

        if (star != null && star.getBodyIcon() != null)
        {
            this.drawCelestialBody(star, 0F, 0F, ticks, 6F);
        }

        String mainSolarSystem = solarSystem.getUnlocalizedName();
        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
            if (planet.getParentSolarSystem() != null && planet.getBodyIcon() != null)
            {
                if (planet.getParentSolarSystem().getUnlocalizedName().equalsIgnoreCase(mainSolarSystem))
                {
                    Vector3f pos = this.getCelestialBodyPosition(planet, ticks);
                    this.drawCircle(planet);
                    this.drawCelestialBody(planet, pos.x, pos.y, ticks, (planet.getRelativeDistanceFromCenter().unScaledDistance < 1.5F) ? 2F : 2.8F);
                }
            }
        }
    }

    private void drawCelestialBodiesZ(CelestialBody planet, float ticks)
    {
        this.drawCelestialBody(planet, 0F, 0F, ticks, 11F);

        for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
        {
            if (moon.getParentPlanet() == planet && moon.getBodyIcon() != null)
            {
                Vector3f pos = this.getCelestialBodyPosition(moon, ticks);
                this.drawCircle(moon);
                this.drawCelestialBody(moon, pos.x, pos.y, ticks, 4F);
            }
        }

        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            if (satellite.getParentPlanet() == planet)
            {
                Vector3f pos = this.getCelestialBodyPosition(satellite, ticks);
                this.drawCircle(satellite);
                this.drawCelestialBody(satellite, pos.x, pos.y, ticks, 3F);
            }
        }
    }

    private void drawTexturedRectCBody(float x, float y, float width, float height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(x, y + height, 0F).tex(0D, 1.0).endVertex();
        worldRenderer.pos(x + width, y + height, 0F).tex(1.0, 1.0).endVertex();
        worldRenderer.pos(x + width, y, 0F).tex(1.0, 0D).endVertex();
        worldRenderer.pos(x, y, 0F).tex(0D, 0D).endVertex();
        tessellator.draw();
    }

    private void drawCelestialBody(CelestialBody planet, float xPos, float yPos, float ticks, float relSize)
    {
        if (xPos + centreX > frameBx || xPos + centreX < frameA)
        {
            return;
        }
        if (yPos + centreY > frameBy || yPos + centreY < frameA)
        {
            return;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(xPos + centreX, yPos + centreY, 0F);

        float alpha = 1.0F;

        CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(planet, planet.getBodyIcon(), 12);
        MinecraftForge.EVENT_BUS.post(preEvent);

        GL11.glColor4f(1, 1, 1, alpha);
        if (preEvent.celestialBodyTexture != null)
        {
            this.renderEngine.bindTexture(preEvent.celestialBodyTexture);
        }

        if (!preEvent.isCanceled())
        {
            float size = relSize / 70 * scale;
            this.drawTexturedRectCBody(-size / 2, -size / 2, size, size);
        }

        CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(planet);
        MinecraftForge.EVENT_BUS.post(postEvent);

        GL11.glPopMatrix();
    }

    private void drawCircle(CelestialBody cBody)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(centreX, centreY, 0.002F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        float sd = 0.002514F * scale;
        float x = this.getScale(cBody);
        float y = 0;
        float grey = 0.1F + 0.65F * Math.max(0F, (0.5F - x));
        x = x * scale / sd;

        GL11.glColor4f(grey, grey, grey, 1.0F);
        GL11.glLineWidth(0.002F);

        GL11.glScalef(sd, sd, sd);
        CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(cBody, new Vector3f(0.0F, 0.0F, 0.0F));
        MinecraftForge.EVENT_BUS.post(preEvent);

        if (!preEvent.isCanceled())
        {
            GL11.glBegin(GL11.GL_LINE_LOOP);

            float temp;
            for (int i = 0; i < lineSegments; i++)
            {
                GL11.glVertex2f(x, y);

                temp = x;
                x = cos * x - sin * y;
                y = sin * temp + cos * y;
            }

            GL11.glEnd();
        }

        CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(cBody);
        MinecraftForge.EVENT_BUS.post(postEvent);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private Vector3f getCelestialBodyPosition(CelestialBody cBody, float ticks)
    {
        float timeScale = cBody instanceof Planet ? 200.0F : 2.0F;
        float distanceFromCenter = this.getScale(cBody) * scale;
        return new Vector3f((float) Math.sin(ticks / (timeScale * cBody.getRelativeOrbitTime()) + cBody.getPhaseShift()) * distanceFromCenter, (float) Math.cos(ticks / (timeScale * cBody.getRelativeOrbitTime()) + cBody.getPhaseShift()) * distanceFromCenter, 0);
    }

    private float getScale(CelestialBody celestialBody)
    {
        float distance = celestialBody.getRelativeDistanceFromCenter().unScaledDistance;
        if (distance >= 1.375F)
        {
            if (distance >= 1.5F)
            {
                distance *= 1.15F;
            }
            else
            {
                distance += 0.075F;
            }
        }
        return 1 / 140.0F * distance * (celestialBody instanceof Planet ? 25.0F : 3.5F);
    }

    private void planeEquation(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3)
    {
        double[] result = new double[4];
        result[0] = y1 * (z2 - z3) + y2 * (z3 - z1) + y3 * (z1 - z2);
        result[1] = z1 * (x2 - x3) + z2 * (x3 - x1) + z3 * (x1 - x2);
        result[2] = x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2);
        result[3] = -(x1 * (y2 * z3 - y3 * z2) + x2 * (y3 * z1 - y1 * z3) + x3 * (y1 * z2 - y2 * z1));
        planes.put(result, 0, 4);
        planes.position(0);
    }

    private void drawPlanetsTest(float ticks)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(centreX, centreY, 0F);

        int id = (int) (ticks / 600F) % 5;
        RenderPlanet.renderID(id, scale, ticks);
        GL11.glPopMatrix();
    }

    private void drawTexturedRectUV(float x, float y, float width, float height, float ticks)
    {
        for (int ysect = 0; ysect < 6; ysect++)
        {
//    		drawTexturedRectUVSixth(x, y, width, height, (ticks / 600F) % 1F, ysect / 6F);
            float angle = 7.5F + 15F * ysect;
            drawTexturedRectUVSixth(x, y, width, height, (ticks / (900F - 80F * MathHelper.cos(angle))) % 1F, ysect / 6F);
        }
    }

    private void drawTexturedRectUVSixth(float x, float y, float width, float height, float prog, float y0)
    {
        y0 /= 2;
        prog = 1.0F - prog;
        float y1 = y0 + 1 / 12F;
        float y2 = 1F - y1;
        float y3 = 1F - y0;
        float yaa = y + height * y0;
        float yab = y + height * y1;
        float yba = y + height * y2;
        float ybb = y + height * y3;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        if (prog <= 0.75F)
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(x, yab, 0F).tex(prog, y1).endVertex();
            worldRenderer.pos(x + width, yab, 0F).tex(prog + 0.25F, y1).endVertex();
            worldRenderer.pos(x + width, yaa, 0F).tex(prog + 0.25F, y0).endVertex();
            worldRenderer.pos(x, yaa, 0F).tex(prog, y0).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(x, ybb, 0F).tex(prog, y3).endVertex();
            worldRenderer.pos(x + width, ybb, 0F).tex(prog + 0.25F, y3).endVertex();
            worldRenderer.pos(x + width, yba, 0F).tex(prog + 0.25F, y2).endVertex();
            worldRenderer.pos(x, yba, 0F).tex(prog, y2).endVertex();
            tessellator.draw();
        }
        else
        {
            double xp = x + width * (1F - prog) / 0.25F;
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(x, yab, 0F).tex(prog, y1).endVertex();
            worldRenderer.pos(xp, yab, 0F).tex(1.0F, y1).endVertex();
            worldRenderer.pos(xp, yaa, 0F).tex(1.0F, y0).endVertex();
            worldRenderer.pos(x, yaa, 0F).tex(prog, y0).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(x, ybb, 0F).tex(prog, y3).endVertex();
            worldRenderer.pos(xp, ybb, 0F).tex(1.0F, y3).endVertex();
            worldRenderer.pos(xp, yba, 0F).tex(1.0F, y2).endVertex();
            worldRenderer.pos(x, yba, 0F).tex(prog, y2).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(xp, yab, 0F).tex(0F, y1).endVertex();
            worldRenderer.pos(x + width, yab, 0F).tex(prog - 0.75F, y1).endVertex();
            worldRenderer.pos(x + width, yaa, 0F).tex(prog - 0.75F, y0).endVertex();
            worldRenderer.pos(xp, yaa, 0F).tex(0F, y0).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(xp, ybb, 0F).tex(0F, y3).endVertex();
            worldRenderer.pos(x + width, ybb, 0F).tex(prog - 0.75F, y3).endVertex();
            worldRenderer.pos(x + width, yba, 0F).tex(prog - 0.75F, y2).endVertex();
            worldRenderer.pos(xp, yba, 0F).tex(0F, y2).endVertex();
            tessellator.draw();
        }
    }
}
