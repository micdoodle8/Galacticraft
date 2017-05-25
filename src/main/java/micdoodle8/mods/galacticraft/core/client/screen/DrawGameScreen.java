package micdoodle8.mods.galacticraft.core.client.screen;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.client.IScreenManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.MapUtil;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class DrawGameScreen implements IScreenManager
{
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
    private static int texCount = 1;

    private float tickDrawn = -1F;
    public boolean initialise = true;
    public boolean initialiseLast = false;
    private boolean readyToInitialise = false;
    private int tileCount = 0;
    private int callCount = 0;
    private int tickMapDone = -1;

    private float scaleX;
    private float scaleZ;

    public TileEntity driver;
    public Class telemetryLastClass;
    public String telemetryLastName;
    public Entity telemetryLastEntity;
    public Render telemetryLastRender;
    public static DynamicTexture reusableMap;  //This will be set up in MapUtil.resetClientBody()
    public int[] localMap = null;
    public boolean mapDone = false;
    public boolean mapFirstTick = false;

    public DrawGameScreen(float scaleXparam, float scaleZparam, TileEntity te)
    {
        this.scaleX = scaleXparam;
        this.scaleZ = scaleZparam;
        this.driver = te;
        this.mapFirstTick = true;
    }

    public boolean check(float scaleXparam, float scaleZparam)
    {
        if (this.mapDone)
        {
            return this.scaleX == scaleXparam && this.scaleZ == scaleZparam;
        }

        return false;
    }

    private void makeMap()
    {
        if (this.mapDone || reusableMap == null || GCCoreUtil.getDimensionID(this.driver.getWorld()) != 0)
        {
            return;
        }
        this.localMap = new int[MapUtil.SIZE_STD2 * MapUtil.SIZE_STD2];
        boolean result = MapUtil.getMap(this.localMap, this.driver.getWorld(), this.driver.getPos());
        if (result)
        {
            TextureUtil.uploadTexture(reusableMap.getGlTextureId(), this.localMap, MapUtil.SIZE_STD2, MapUtil.SIZE_STD2);
            mapDone = true;
        }
    }

    public void drawScreen(int type, float ticks, boolean cornerBlock)
    {
        if (type >= GalacticraftRegistry.getMaxScreenTypes())
        {
            System.out.println("Wrong gamescreen type detected - this is a bug." + type);
            return;
        }

        if (cornerBlock)
        {
            if ((this.mapFirstTick || ((int) ticks) % 99 == 0) && !mapDone)
            {
                if (this.tickMapDone != (int) ticks)
                {
                    this.tickMapDone = (int) ticks;
                    this.makeMap();
                    this.mapFirstTick = false;
                }
            }
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

        if (type > 0)
        {
            GL11.glDisable(GL11.GL_LIGHTING);
        }

        GalacticraftRegistry.getGameScreen(type).render(type, ticks, scaleX, scaleZ, this);

        if (type > 0)
        {
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
    }

    @Override
    public WorldProvider getWorldProvider()
    {
        if (this.driver != null)
        {
            return driver.getWorld().provider;
        }

        return null;
    }

    public float getScaleZ()
    {
        return scaleZ;
    }

    public float getScaleX()
    {
        return scaleX;
    }
}
