package micdoodle8.mods.galacticraft.core.client.gui.screen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.client.IScreenManager;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

	@SideOnly(Side.CLIENT)
	public void render(int type, float ticks, float sizeX, float sizeY, IScreenManager scr)
    {
    	DrawGameScreen screen = (DrawGameScreen) scr;
    	
		frameBx = sizeX - frameA;
    	frameBy = sizeY - frameA;
    	drawBlackBackground(0.0F);
    	yPos = 0;

    	TileEntityTelemetry telemeter = TileEntityTelemetry.getNearest(screen.driver);
    	//Make the text to draw.  To look good it's important the width and height
    	//of the whole text box are correctly set here.
    	String strName = "";
    	String str0 = "No link";
    	String str1 = "";
    	String str2 = "";
    	String str3 = "";
    	String str4 = "";
    	Render renderEntity = null;
    	Entity entity = null;
    	float Xmargin = 0;
    	
    	if (telemeter != null && telemeter.clientData.length >= 3)
    	{
        	if (telemeter.clientClass != null)
        	{
        		if (telemeter.clientClass == screen.telemetryLastClass && (telemeter.clientClass != EntityPlayerMP.class || telemeter.clientName.equals(screen.telemetryLastName)))
        		{
        			//Used cached data from last time if possible
        			entity = screen.telemetryLastEntity;
        			renderEntity = screen.telemetryLastRender;
        			strName = screen.telemetryLastName;
        		}
        		else
        		{	        		
	        		//Create an entity to render, based on class, and get its name
        			entity = null;
	        		
	        		if (telemeter.clientClass == EntityPlayerMP.class)
	        		{
	        			strName = telemeter.clientName;
	        			entity = new EntityOtherPlayerMP(screen.driver.getWorldObj(), telemeter.clientGameProfile);
	        			renderEntity = (Render) RenderManager.instance.entityRenderMap.get(EntityPlayer.class);
	        		}
	        		else
	        		{
		        		try {
		        			entity = (Entity) telemeter.clientClass.getConstructor(World.class).newInstance(screen.driver.getWorldObj());
		        		} catch (Exception ex) { }
		        		if (entity != null) strName = entity.getCommandSenderName();
		        		renderEntity = (Render) RenderManager.instance.entityRenderMap.get(telemeter.clientClass);
	        		}	        		
        		}
        	}
        	
        	if (entity instanceof EntityLivingBase)
        	{
        		//Living entity:
        		//  data0 = time to show red damage
        		//  data1 = health in half-hearts
        		//  data2 = pulse
        		//  data3 = hunger
        		//  data4 = oxygen
        		str0 = telemeter.clientData[0] > 0 ? "ouch!!!!" : "";
	    		if (telemeter.clientData[1] >= 0)
	        	{
	        		str1 = "Health: " + telemeter.clientData[1] + "%";
	        	}
	    		else
	    			str1 = "";
	    		str2 = "" + telemeter.clientData[2] + " bpm";
	    		if (telemeter.clientData[3] > -1)
	        	{
	        		str3 = "Food: "  + telemeter.clientData[3] + "%";
	        	}
	    		if (telemeter.clientData[4] > -1)
	        	{
	        		int oxygen = telemeter.clientData[4];
	        		oxygen = (oxygen % 4096) + (oxygen / 4096);
	        		if (oxygen == 180 || oxygen == 90)
	        			str4 = "Oxygen: OK";
	        		else
	        			str4 = "Oxygen: " + this.makeOxygenString(oxygen)  + GCCoreUtil.translate("gui.seconds");
	        	} 
        	}
        	else if (entity instanceof EntitySpaceshipBase)
        	{
        		//Spaceships:
        		//  data0 = launch countdown
        		//  data1 = height
        		//  data2 = speed
        		//  data3 = fuel remaining
        		//  data4 = pitch angle
        		int countdown = telemeter.clientData[0];
        		str0 = "";
        		str1 = (countdown == 400) ? "On launchpad" : ((countdown > 0) ? "Countdown: " + countdown / 20 : "Launched");
        		str2 = "Height: " + telemeter.clientData[1];
        		str3 = "Speed: " + this.makeSpeedString(telemeter.clientData[2]);
        		str4 = "Fuel: " + telemeter.clientData[3] + "%";
        	}
        	else
        	//Generic - could be boats or minecarts etc - just show the speed
        	//TODO  can add more here, e.g. position data?
        	if (telemeter.clientData[2] >= 0)
        	{
        		str2 = "Speed: " + makeSpeedString(telemeter.clientData[2]);
        	}
    	}
    	else if (GalacticraftCore.isPlanetsLoaded && entity instanceof EntityAstroMiner)
    	{
    		str0 = "";
    		str1 = "";
    		switch (telemeter.clientData[4])
    		{
    			case 0:
    				str1 = "Stuck - out of energy";
    				break;
    			case 1:
    				str1 = "Travelling to mine";
    				break;
    			case 2:
    				str1 = "Mining";
    				break;
    			case 3:
    				str1 = "Returning to base";
    				break;
    			case 4:
    				str1 = "At base - charging";
    				break;
    		}
    		str2 = "dy: " + telemeter.clientData[1];
    		str3 = "dx: " + telemeter.clientData[2];
    		str4 = "Energy: " + telemeter.clientData[3];        		
    	}
    	else
    	{
    		//Default - draw a simple time display just to show the Display Screen is working
			World w1 = screen.driver.getWorldObj();
    		int time1 = w1 != null ? (int) ((w1.getWorldTime() + 6000L) % 24000L) : 0;
    		str2 = makeTimeString(time1 * 360);
        }
    	
    	int textWidthPixels = 155;
    	int textHeightPixels = 60;  //1 lines
    	if (str3.isEmpty()) textHeightPixels -= 10; 
    	if (str4.isEmpty()) textHeightPixels -= 10; 

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
		if (entity != null && renderEntity != null) Xmargin = (sizeX - borders) / 2;
    	float Xoffset = (sizeX - borders - textWidthPixels * scaleText) / 2 + Xmargin;
    	float Yoffset = (sizeY - borders - textHeightPixels * scaleText) / 2 + scaleText;
    	GL11.glTranslatef(border + Xoffset, border + Yoffset, 0.0F);
        GL11.glScalef(scaleText, scaleText, 1.0F);

        //Actually draw the text
        int whiteColour = ColorUtil.to32BitColor(255, 240, 216, 255);
        drawText(strName, whiteColour);
        drawText(str0, whiteColour);
        drawText(str1, whiteColour);
        drawText(str2, whiteColour);
        drawText(str3, whiteColour);
        drawText(str4, whiteColour);

        //If there is an entity to render, draw it on the left of the text
        if (renderEntity != null && entity != null ) 
        {
        	GL11.glTranslatef(-Xmargin / 2 / scaleText, textHeightPixels / 2 + (-Yoffset + (sizeY - borders) / 2) / scaleText, -0.0005F);
        	float scalefactor = 38F / (float) Math.pow(Math.max(entity.height, entity.width), 0.65);
        	GL11.glScalef(scalefactor, scalefactor, 0.0015F);
        	GL11.glRotatef(180F, 0, 0, 1);
        	GL11.glRotatef(180F, 0, 1, 0);
        	if (entity instanceof EntitySpaceshipBase)
        	{
            	GL11.glRotatef(telemeter.clientData[4], -1, 0, 0);
            	GL11.glTranslatef(0, entity.height / 4, 0);
        	}
        	RenderPlayerGC.flagThermalOverride = true;
        	renderEntity.doRender(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
            RenderPlayerGC.flagThermalOverride = false;
        }

        //TODO  Cross-dimensional tracking (i.e. old entity setDead, new entity created)
        //TODO  Deal with text off screen (including where localizations longer than English)

        screen.telemetryLastClass = (telemeter == null) ? null : telemeter.clientClass;
        screen.telemetryLastEntity = entity;
        screen.telemetryLastRender = renderEntity;
        screen.telemetryLastName = strName;
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

    private String makeOxygenString(int oxygen)
    { 	
    	//Server takes 1 air away every 9 ticks (OxygenUtil.getDrainSpacing)
    	int sp1 = oxygen * 9 / 20;
    	int sp2 = ((oxygen * 9) % 20) / 2;
    	String spStr1 = "" + sp1;
    	String spStr2 = "" + sp2;
    	return spStr1 + "." + spStr2;
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
