package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
    	String str0 = "";
    	String str1 = "00:00:00";
    	String str2 = "";
    	if (telemeter != null && telemeter.clientData.length >= 3)
    	{
        	if (telemeter.clientClass != null)
        	{
        		Entity e = null;
        		try {
        			e = (Entity) telemeter.clientClass.getConstructor(World.class).newInstance(te.getWorldObj());
        		} catch (Exception ex) { }
        		if (e != null) str0 = e.getCommandSenderName();
        	}
        	
    		if (telemeter.clientData[1] >= 0)
        	{
        		str1 = makeHealthString(telemeter.clientData[1]);
        	}
    		else
    			str1 = "";
    		
        	if (telemeter.clientData[2] >= 0)
        	{
        		str2 = makeSpeedString(telemeter.clientData[2]);
        	}
    	}
    	else
    	{
			World w1 = te.getWorldObj();
    		int time1 = w1 != null ? (int) ((w1.getWorldTime() + 6000L) % 24000L) : 0;
        	str1 = makeTimeString(time1 * 360);
        }
    	int textWidthPixels = 75;
    	int textHeightPixels = 30;  //1 lines

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
        drawText(str0, GCCoreUtil.to32BitColor(255, 240, 216, 255));
        drawText(str1, GCCoreUtil.to32BitColor(255, 240, 216, 255));
        drawText(str2, GCCoreUtil.to32BitColor(255, 240, 216, 255));
        
        //TODO  Deal with text off screen (including where localizations longer than English)
    }
    
    private String makeTimeString(int l)
    { 	
    	int hrs = l / 360000;
    	int mins = l / 6000 - hrs * 60;
    	int secs = l / 100 - hrs * 3600 - mins * 60;
    	String hrsStr = hrs > 9 ? "" + hrs : "0" + hrs;
    	String minsStr = mins > 9 ? "" + mins : "0" + mins;
    	String secsStr = secs > 9 ? "" + secs : "0" + secs;
    	return hrsStr + ":" + minsStr + ":" + secsStr;
    }

    private String makeSpeedString(int speed100)
    { 	
    	int sp1 = speed100 / 100;
    	int sp2 = (speed100 % 100);
    	String spStr1 = "" + sp1;
    	String spStr2 = (sp2 > 9 ? "" : "0") + sp2;
    	return spStr1 + "." + spStr2 + " " + GCCoreUtil.translate("gui.lander.velocityu");
    }

    private String makeHealthString(int hearts2)
    { 	
    	int sp1 = hearts2 / 2;
    	int sp2 = (hearts2 % 2) * 5;
    	String spStr1 = "" + sp1;
    	String spStr2 = "" + sp2;
    	return spStr1 + "." + spStr2 + " hearts";
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
