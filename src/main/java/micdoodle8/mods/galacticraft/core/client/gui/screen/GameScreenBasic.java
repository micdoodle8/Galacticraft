package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class GameScreenBasic implements IGameScreen
{
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;

    private float frameA;
    private float frameBx;
    private float frameBz;
    private float cornerAx = 0F;
    private float cornerAz = 0F;
    private float cornerBx = 1.0F;
    private float cornerBz = 1.0F;
    
    /**
     * Initialise the basic screen renderer
     * 
     * @param frameWidth  The undrawn frame border, in blocks (typically 0.1F)
     */
    public GameScreenBasic(float frameWidth)
    {
    	this.frameA = frameWidth;
    }
    
    public void render(int type, float ticks, float scaleX, float scaleZ)
    {
    	frameBx = scaleX - frameA;
    	frameBz = scaleZ - frameA;

    	if (scaleX == scaleZ)
     	{
     	    cornerAx = 0F;
     	    cornerAz = 0F;
     	    cornerBx = 1.0F;
     	    cornerBz = 1.0F;
     	}
    	else if (scaleX < scaleZ)
    	{
    		cornerAx = (1.0F - (scaleX / scaleZ)) / 2;
    	    cornerAz = 0F;
    		cornerBx = 1.0F - cornerAx;
    	    cornerBz = 1.0F;
    	} else
    	if (scaleZ < scaleX)
    	{
    	    cornerAx = 0F;
    	    cornerAz = (1.0F - (scaleZ / scaleX)) / 2;
    	    cornerBx = 1.0F;
    	    cornerBz = 1.0F - cornerAz;
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
        }
    }

    private void draw2DTexture()
    {
        final Tessellator tess = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tess.setColorRGBA(255, 255, 255, 255);
        tess.startDrawingQuads();

        tess.addVertexWithUV(frameA, 0F, frameBz, cornerAx, cornerBz);
        tess.addVertexWithUV(frameBx, 0F, frameBz, cornerBx, cornerBz);
        tess.addVertexWithUV(frameBx, 0F, frameA, cornerBx, cornerAz);
        tess.addVertexWithUV(frameA, 0F, frameA, cornerAx, cornerAz);
        tess.draw();   	
    }

    private void drawBlackBackground(float greyLevel)
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tess = Tessellator.instance;
        GL11.glColor4f(greyLevel, greyLevel, greyLevel, 1.0F);
        tess.startDrawingQuads();
        
        tess.addVertex(frameA, - 0.005F, frameBz);
        tess.addVertex(frameBx, - 0.005F, frameBz);
        tess.addVertex(frameBx, - 0.005F, frameA);
        tess.addVertex(frameA, - 0.005F, frameA);
        tess.draw();   	

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
