package micdoodle8.mods.galacticraft.core.client.gui.screen;

import java.nio.FloatBuffer;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class DrawGameScreen
{
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

    private float tickDrawn = -1F;
    public boolean initialise = true;
    public boolean initialiseLast = false;
    private boolean readyToInitialise = false;
    private int tileCount = 0;
    private int callCount = 0;
    
    private float scaleX;
    private float scaleZ;
    private TileEntity driver;

    public DrawGameScreen(float scaleXparam, float scaleZparam, TileEntity te)
    {
    	this.scaleX = scaleXparam;
    	this.scaleZ = scaleZparam;
    	this.driver = te;
    }
    
    public void drawScreen(int type, float ticks, boolean cornerBlock)
    {
    	if (type >= TileEntityScreen.maxTypes)
    	{
    		System.out.println("Wrong gamescreen type detected - this is a bug."+type);
    		return;
    	}

		if (type < 2 || cornerBlock)
		{
			this.doDraw(type, ticks);
			this.initialise = true;
			this.initialiseLast = false;
			return;
		}
		
    	//Performance code: if type > 1 then we only want
    	//to draw the screen once per tick, for multi-screens
    	
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
    			if (ticks == tickDrawn)
    			{
    				return;
    			}
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
        
        this.doDraw(type, ticks);
    }
    
    private void doDraw(int type, float ticks)
    {
        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        if (type > 0) GL11.glDisable(GL11.GL_LIGHTING);

        GalacticraftRegistry.getGameScreen(type).render(type, ticks, scaleX, scaleZ, this.driver);

        if (type > 0) GL11.glEnable(GL11.GL_LIGHTING);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
    }
}
