package micdoodle8.mods.galacticraft.core.client.gui.screen;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class DrawGameScreen
{
    private static List<IGameScreen> gameScreens = new ArrayList<IGameScreen>();

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
    
    public static float FRAMEBORDER = 0.098F;
    public static int maxType = 0;

    public DrawGameScreen(float scaleXparam, float scaleZparam)
    {
    	this.scaleX = scaleXparam;
    	this.scaleZ = scaleZparam;
    	DrawGameScreen.initialise();
    }
    
    private static void initialise()
    {
    	if (DrawGameScreen.gameScreens.isEmpty())
    	{
	        IGameScreen rendererBasic = new GameScreenBasic(FRAMEBORDER);
	        IGameScreen rendererCelest = new GameScreenCelestial(FRAMEBORDER);
	        registerScreen(rendererBasic);  //Type 0 - blank
	        registerScreen(rendererBasic);  //Type 1 - local satellite view
	        registerScreen(rendererCelest);  //Type 2 - solar system
	        registerScreen(rendererCelest);  //Type 3 - local planet
    	}
    }
    
    public static void registerScreen(IGameScreen screen)
    {
    	DrawGameScreen.gameScreens.add(screen);
    	DrawGameScreen.maxType++;
        TileEntityScreen.maxTypes = DrawGameScreen.maxType;
        //Note: add-ons may need to manually increase TileEntityScreen.maxTypes
        //on the server side, as this will only make client-side changes
    }
    
    public void drawScreen(int type, float ticks)
    {
    	if (type >= DrawGameScreen.maxType)
    	{
    		System.out.println("Wrong gamescreen type detected - this is a bug."+type);
    		return;
    	}

		if (type < 2)
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

        DrawGameScreen.gameScreens.get(type).render(type, ticks, scaleX, scaleZ);

        if (type > 0) GL11.glEnable(GL11.GL_LIGHTING);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
    }
}
