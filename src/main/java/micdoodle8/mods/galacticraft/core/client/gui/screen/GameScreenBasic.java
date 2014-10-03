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
    private float frameBy;
    private float cornerAx = 0F;
    private float cornerAy = 0F;
    private float cornerBx = 1.0F;
    private float cornerBy = 1.0F;
    
    /**
     * Initialise the basic screen renderer
     * 
     * @param frameWidth  The undrawn frame border, in blocks (typically 0.1F)
     */
    public GameScreenBasic(float frameWidth)
    {
    	this.frameA = frameWidth;
    }
    
    public void render(int type, float ticks, float scaleX, float scaleY)
    {
    	frameBx = scaleX - frameA;
    	frameBy = scaleY - frameA;

    	if (scaleX == scaleY)
     	{
     	    cornerAx = 0F;
     	    cornerAy = 0F;
     	    cornerBx = 1.0F;
     	    cornerBy = 1.0F;
     	}
    	else if (scaleX < scaleY)
    	{
    		cornerAx = (1.0F - (scaleX / scaleY)) / 2;
    	    cornerAy = 0F;
    		cornerBx = 1.0F - cornerAx;
    	    cornerBy = 1.0F;
    	} else
    	if (scaleY < scaleX)
    	{
    	    cornerAx = 0F;
    	    cornerAy = (1.0F - (scaleY / scaleX)) / 2;
    	    cornerBx = 1.0F;
    	    cornerBy = 1.0F - cornerAy;
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

        tess.addVertexWithUV(frameA, frameBy, 0F, cornerAx, cornerBy);
        tess.addVertexWithUV(frameBx, frameBy, 0F, cornerBx, cornerBy);
        tess.addVertexWithUV(frameBx, frameA, 0F, cornerBx, cornerAy);
        tess.addVertexWithUV(frameA, frameA, 0F, cornerAx, cornerAy);
        tess.draw();   	
    }

    private void drawBlackBackground(float greyLevel)
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tess = Tessellator.instance;
        GL11.glColor4f(greyLevel, greyLevel, greyLevel, 1.0F);
        tess.startDrawingQuads();
        
        tess.addVertex(frameA, frameBy, - 0.005F);
        tess.addVertex(frameBx, frameBy, - 0.005F);
        tess.addVertex(frameBx, frameA, - 0.005F);
        tess.addVertex(frameA, frameA, - 0.005F);
        tess.draw();   	

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
