package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.Satellite;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import cpw.mods.fml.client.FMLClientHandler;

public class GameScreenCelestial implements IGameScreen
{
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;

    private float frameA;
    private float frameBx;
    private float frameBy;
    private float centreX;
    private float centreY;
    private float scale;
    
    public GameScreenCelestial(float frame)
    {
    	this.frameA = frame;
    }
    
    public void render(int type, float ticks, float scaleX, float scaleY)
    {
    	centreX = scaleX / 2;
    	centreY = scaleY / 2;
    	frameBx = scaleX - frameA;
    	frameBy = scaleY - frameA;
    	this.scale = Math.max(scaleX, scaleY) - 0.2F;

    	switch(type)
        {
        case 2:
        	drawBlackBackground(0.0F);
            drawCelestialBodies(ticks);
        	 break;
        case 3:
            drawBlackBackground(0.0F);
            drawCelestialBodiesZ(GalacticraftCore.planetOverworld, ticks);
        	break;
        }
    }

    private void drawBlackBackground(float greyLevel)
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tess = Tessellator.instance;
        GL11.glColor4f(greyLevel, greyLevel, greyLevel, 1.0F);
        tess.startDrawingQuads();
        
        tess.addVertex(frameA, frameBy, 0.005F);
        tess.addVertex(frameBx, frameBy, 0.005F);
        tess.addVertex(frameBx, frameA, 0.005F);
        tess.addVertex(frameA, frameA, 0.005F);
        tess.draw();   	

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
    private void drawCelestialBodies(float ticks)
    {
        for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
        {
            Star star = solarSystem.getMainStar();

            if (star != null && star.getBodyIcon() != null)
            {
        		this.drawCelestialBody(star, 0F, 0F, ticks, 6F);
            }
        }

        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
        	if (planet.getBodyIcon() != null)
            {
                Vector3f pos = this.getCelestialBodyPosition(planet, ticks);
        		this.drawCelestialBody(planet, pos.x, pos.y, ticks, (planet.getRelativeDistanceFromCenter().unScaledDistance < 1.5F) ? 2F : 2.8F);
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
    			this.drawCelestialBody(moon, pos.x, pos.y, ticks, 4F);
    		}
    	}

    	for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
    	{
    		if (satellite.getParentPlanet() == planet)
    		{
    	        Vector3f pos = this.getCelestialBodyPosition(satellite, ticks);
    			this.drawCelestialBody(satellite, pos.x, pos.y, ticks, 3F);
    		}
    	}
    }

    private void drawTexturedRect(float x, float y, float width, float height)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0F, 0, 1.0F);
        tessellator.addVertexWithUV(x + width, y + height, 0F, 1.0F, 1.0F);
        tessellator.addVertexWithUV(x + width, y, 0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV(x, y, 0F, 0.0F, 0.0F);
        tessellator.draw();
    }

    private void drawCelestialBody(CelestialBody planet, float xPos, float yPos, float ticks, float relSize)
    {
    	if (xPos + centreX > frameBx || xPos + centreX < frameA) return;
        if (yPos + centreY > frameBy || yPos + centreY < frameA) return;

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
        	this.drawTexturedRect(- size / 2, -size / 2, size, size);
        }

        CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(planet);
        MinecraftForge.EVENT_BUS.post(postEvent);

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
        	if (distance >= 1.5F) distance *= 1.15F;
        	else
        		distance += 0.075F; 
    	return 1 / 140.0F * distance * (celestialBody instanceof Planet ? 25.0F : 3.5F);
    }
}
