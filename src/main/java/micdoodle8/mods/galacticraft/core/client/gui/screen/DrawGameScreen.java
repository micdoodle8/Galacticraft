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

        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
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

        DrawGameScreen.gameScreens.get(type).render(type, ticks, scaleX, scaleZ);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
    }

    private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_)
    {
        colorBuffer.clear();
        colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        colorBuffer.flip();
        /** Float buffer used to set OpenGL material colors */
        return colorBuffer;
    }
}
