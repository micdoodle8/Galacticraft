package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class GameScreenText implements IGameScreen
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
    public GameScreenText(float frameWidth)
    {
    	this.frameA = frameWidth;
    }
    
    public void render(int type, float ticks, float scaleX, float scaleY)
    {
    	drawBlackBackground(0.0F);
    	float scale = Math.min(scaleX, scaleY);
    	GL11.glTranslatef(frameA + 0.025F * scale, frameA + 0.025F * scale, 0.0F);
    	String str = makeString();  	
    	int width = str.length() * 6;
    	float textScale = (scale - frameA * 2) / width; 
        GL11.glScalef(textScale, textScale, 1.0F);

    	switch(type)
        {
    	default:
	        drawText(str, GCCoreUtil.to32BitColor(255, 240, 216, 255));
	        break;
        }
    }
    
    private String makeString()
    {
    	int l = (int) (Minecraft.getSystemTime() % 86400000L) / 10;  	
    	int hrs = l / 360000;
    	int mins = l / 6000 - hrs * 60;
    	int secs = l / 100 - hrs * 3600 - mins * 60;
    	int cs = l % 100;
    	String hrsStr = hrs > 9 ? "" + hrs : "0" + hrs;
    	String minsStr = mins > 9 ? "" + mins : "0" + mins;
    	String secsStr = secs > 9 ? "" + secs : "0" + secs;
    	String csStr = cs > 9 ? "" + cs : "0" + cs;
    	return hrsStr + ":" + minsStr + ":" + secsStr + "." + csStr;
    }

    private void drawText(String str, int colour)
    {
    	Minecraft.getMinecraft().fontRenderer.drawString(str, 0, 0, colour, false);    	
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
