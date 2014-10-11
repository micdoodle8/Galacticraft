package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class GameScreenText implements IGameScreen
{
    private float frameA;
    private float frameBx;
    private float frameBy;
    private int yPos;
    
	public void setFrameSize(float frameSize)
	{
		this.frameA = frameSize;
	}

	public void render(int type, float ticks, float sizeX, float sizeY, TileEntity te)
    {
    	frameBx = sizeX - frameA;
    	frameBy = sizeY - frameA;
    	drawBlackBackground(0.0F);
    	yPos = 0;

    	TileEntityTelemetry telemeter = TileEntityTelemetry.getNearest(te);
    	//Make the text to draw.  To look good it's important the width and height
    	//of the whole text box are correctly set here.
    	String str1 = "00:00:00";
    	if (telemeter != null)
    	{
        	str1 = makeString(telemeter.clientTime1 * 360);
    	}
    	int textWidthPixels = 39;
    	int textHeightPixels = 10;  //1 lines

    	//First pass - approximate border size
    	float borders = frameA * 2 + 0.05F * Math.min(sizeX, sizeY);
    	float scaleXTest = (sizeX - borders) / textWidthPixels;
    	float scaleYTest = (sizeY - borders) / textHeightPixels;
    	float scale = sizeX;
    	if (scaleYTest < scaleXTest)
    		scale = sizeY;
    	//Second pass - the border size may be more accurate now
    	borders = frameA * 2 + 0.05F * scale;
    	scaleXTest = (sizeX - borders) / textWidthPixels;
    	scaleYTest = (sizeY - borders) / textHeightPixels;
    	scale = sizeX;
    	float scaleText = scaleXTest;
    	if (scaleYTest < scaleXTest)
    	{
    		scale = sizeY;
    		scaleText = scaleYTest; 
    	}

    	//Centre the text in the display 
    	float border = frameA + 0.025F * scale;
    	float Xoffset = (sizeX - borders - textWidthPixels * scaleText) / 2;
    	float Yoffset = (sizeY - borders - textHeightPixels * scaleText) / 2 + scaleText;
    	GL11.glTranslatef(border + Xoffset, border + Yoffset, 0.0F);
        GL11.glScalef(scaleText, scaleText, 1.0F);

        //Actually draw the text
        drawText(str1, GCCoreUtil.to32BitColor(255, 240, 216, 255));
    }
    
    private String makeString(int l)
    { 	
    	int hrs = l / 360000;
    	int mins = l / 6000 - hrs * 60;
    	int secs = l / 100 - hrs * 3600 - mins * 60;
    	String hrsStr = hrs > 9 ? "" + hrs : "0" + hrs;
    	String minsStr = mins > 9 ? "" + mins : "0" + mins;
    	String secsStr = secs > 9 ? "" + secs : "0" + secs;
    	return hrsStr + ":" + minsStr + ":" + secsStr;
    }

    private void drawText(String str, int colour)
    {
    	Minecraft.getMinecraft().fontRenderer.drawString(str, 0, yPos, colour, false);
    	yPos += 10;
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
