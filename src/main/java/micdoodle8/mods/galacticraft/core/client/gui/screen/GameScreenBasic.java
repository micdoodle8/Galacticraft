package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

public class GameScreenBasic implements IGameScreen
{
    private TextureManager renderEngine;

    private float frameA;
    private float frameBx;
    private float frameBy;
    private float textureAx = 0F;
    private float textureAy = 0F;
    private float textureBx = 1.0F;
    private float textureBy = 1.0F;
    

    public GameScreenBasic()
    {
		//This can be called from either server or client, so don't include
    	//client-side only code on the server.
    	if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			renderEngine = FMLClientHandler.instance().getClient().renderEngine;
		}
    }
    
    public void setFrameSize(float frameSize)
	{
		this.frameA = frameSize;
	}

    public void render(int type, float ticks, float scaleX, float scaleY, TileEntity te)
    {
    	frameBx = scaleX - frameA;
    	frameBy = scaleY - frameA;

    	if (scaleX == scaleY)
     	{
     	    textureAx = 0F;
     	    textureAy = 0F;
     	    textureBx = 1.0F;
     	    textureBy = 1.0F;
     	}
    	else if (scaleX < scaleY)
    	{
    		textureAx = (1.0F - (scaleX / scaleY)) / 2;
    	    textureAy = 0F;
    		textureBx = 1.0F - textureAx;
    	    textureBy = 1.0F;
    	} else
    	if (scaleY < scaleX)
    	{
    	    textureAx = 0F;
    	    textureAy = (1.0F - (scaleY / scaleX)) / 2;
    	    textureBx = 1.0F;
    	    textureBy = 1.0F - textureAy;
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

        tess.addVertexWithUV(frameA, frameBy, 0F, textureAx, textureBy);
        tess.addVertexWithUV(frameBx, frameBy, 0F, textureBx, textureBy);
        tess.addVertexWithUV(frameBx, frameA, 0F, textureBx, textureAy);
        tess.addVertexWithUV(frameA, frameA, 0F, textureAx, textureAy);
        tess.draw();   	
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
}
