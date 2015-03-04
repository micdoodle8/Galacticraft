package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

public class InGameScreen
{
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

    private float yPlane = 0.91F;
    private float frameA = 0.098F;
    private float frameBx;
    private float frameBz;
    private float centreX;
    private float centreZ;
    private float scale;
    private float cornerAx = 0F;
    private float cornerAz = 0F;
    private float cornerBx = 1.0F;
    private float cornerBz = 1.0F;
    
    private float tickDrawn = -1F;
    public boolean initialise = true;
    public boolean initialiseLast = false;
    private boolean readyToInitialise = false;
    private int tileCount = 0;
    private int callCount = 0;

    public InGameScreen(float scaleX, float scaleZ)
    {
    	centreX = scaleX / 2;
    	centreZ = scaleZ / 2;
    	frameBx = scaleX - frameA;
    	frameBz = scaleZ - frameA;
    	this.scale = Math.max(scaleX, scaleZ) - 0.2F;

    	if (scaleX < scaleZ)
    	{
    		cornerAx = (1.0F - (scaleX / scaleZ)) / 2;
    		cornerBx = 1.0F - cornerAx;
    	} else
    	if (scaleZ < scaleX)
    	{
    		cornerAz = (1.0F - (scaleZ / scaleX)) / 2;
    		cornerBz = 1.0F - cornerAz;
    	}
    }
    
    public void drawScreen(int type, float ticks)
    {
        //Spend the first tick just initialising the counter 
    	if (initialise)
    	{
    		if (!initialiseLast)
    		{
    			tickDrawn = ticks;
    			readyToInitialise = false;
    			initialiseLast = true;
    			return;
    		}
    		
    		if (!readyToInitialise)
    		{
    			if (ticks == tickDrawn) return;
    		}
    		
    		if (!readyToInitialise)
         	{
        		readyToInitialise = true;
         		tickDrawn = ticks;
         		tileCount = 1;
         		return;
         	}
        	else if (ticks == tickDrawn)
        	{
        		tileCount++;
        		return;
        	}
        	else
        	{
        		//Start normal operations 
        		initialise = false;
    			initialiseLast = false;
        		readyToInitialise = false;
        	}
    	}
        
        if (++callCount < tileCount)
        {
        	//Normal situation, everything OK
        	if (callCount == 1 || tickDrawn == ticks)
        	{
	        	tickDrawn = ticks;
	        	return;
        	}
        	else
        	//The callCount last tick was less than the tileCount, reinitialise
        	{
        		initialise = true;
        		//but draw this tick [probably a tileEntity moved out of the frustum]
        	}
        }
        
        if (callCount == tileCount)
        {
        	callCount = 0;
        	//Again if this is not the tickDrawn then something is wrong, reinitialise
        	if (tileCount > 1 && ticks != tickDrawn)
        	{
        		initialise = true;
        	}
        }
        	
        tickDrawn = ticks;
    	GL11.glPushMatrix();

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        if (type > 0)
        {
	        //Special GL Lighting for screen
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glEnable(GL11.GL_LIGHT0);
	        GL11.glDisable(GL11.GL_LIGHT1);
	        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
	        GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
	        float ambient = 0.2F;
	        float diffuse = 1.0F;
	        float specular = 0.9F;
	        float ambient2 = 0.6F;
	        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, setColorBuffer(0.5F, 0.95F, 0.5F, 0.0F));
	        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, setColorBuffer(diffuse, diffuse, diffuse, 1.0F));
	        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, setColorBuffer(ambient, ambient, ambient, 1.0F));
	        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, setColorBuffer(specular, specular, 1.0F, 1.0F));
	        GL11.glShadeModel(GL11.GL_FLAT);
	        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setColorBuffer(ambient2, ambient2, ambient2, 1.0F));
        }
        
        switch(type)
        {
        case 0:
        	drawBlackBackground(0.09F);
        	break;
        case 1:
	        if (ClientProxyCore.overworldTextureClient != null)
	        {
	            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ClientProxyCore.overworldTextureClient.getGlTextureId());
	        }
	        else
	        {
	            this.renderEngine.bindTexture(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png"));
	            if (!ClientProxyCore.overworldTextureRequestSent)
	            {
	                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, new Object[] {}));
	                ClientProxyCore.overworldTextureRequestSent = true;
	            }
	        }
	        draw2DTexture();
	        break;
        case 2:
        	drawBlackBackground(0.0F);
            drawCelestialBodies(ticks);
        	 break;
        case 3:
            drawBlackBackground(0.0F);
            drawCelestialBodiesZ(GalacticraftCore.planetOverworld, ticks);
        	break;
        }

        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }

    private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_)
    {
        colorBuffer.clear();
        colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        colorBuffer.flip();
        /** Float buffer used to set OpenGL material colors */
        return colorBuffer;
    }
    
    private void draw2DTexture()
    {
        final Tessellator tess = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tess.setColorRGBA(255, 255, 255, 255);
        tess.startDrawingQuads();

        tess.addVertexWithUV(frameA, yPlane, frameBz, cornerAx, cornerBz);
        tess.addVertexWithUV(frameBx, yPlane, frameBz, cornerBx, cornerBz);
        tess.addVertexWithUV(frameBx, yPlane, frameA, cornerBx, cornerAz);
        tess.addVertexWithUV(frameA, yPlane, frameA, cornerAx, cornerAz);
        tess.draw();   	
    }

    private void drawBlackBackground(float greyLevel)
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tess = Tessellator.instance;
        GL11.glColor4f(greyLevel, greyLevel, greyLevel, 1.0F);
        tess.startDrawingQuads();
        
        tess.addVertex(frameA, yPlane - 0.005F, frameBz);
        tess.addVertex(frameBx, yPlane - 0.005F, frameBz);
        tess.addVertex(frameBx, yPlane - 0.005F, frameA);
        tess.addVertex(frameA, yPlane - 0.005F, frameA);
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
        		this.drawCelestialBody(star, 0F, 0F, ticks, 5.5F);
            }
        }

        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
        	if (planet.getBodyIcon() != null)
            {
                Vector3f pos = this.getCelestialBodyPosition(planet, ticks);
        		this.drawCelestialBody(planet, pos.x, pos.y, ticks, 2.8F);
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
    			this.drawCelestialBody(moon, pos.x, pos.y, ticks, 3F);
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

    private void drawTexturedRect(float x, float z, float width, float height)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, yPlane, z + height, 0, 1.0F);
        tessellator.addVertexWithUV(x + width, yPlane, z + height, 1.0F, 1.0F);
        tessellator.addVertexWithUV(x + width, yPlane, z, 1.0F, 0.0F);
        tessellator.addVertexWithUV(x, yPlane, z, 0.0F, 0.0F);
        tessellator.draw();
    }

    private void drawCelestialBody(CelestialBody planet, float xPos, float zPos, float ticks, float relSize)
    {
        if (xPos + centreX > frameBx || xPos + centreX < frameA) return;
        if (zPos + centreZ > frameBz || zPos + centreZ < frameA) return;

        GL11.glPushMatrix();
        GL11.glTranslatef(xPos + centreX, 0, zPos + centreZ);

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
        return 1 / 140.0F * celestialBody.getRelativeDistanceFromCenter().unScaledDistance * (celestialBody instanceof Planet ? 25.0F : 3.0F);
    }
}
