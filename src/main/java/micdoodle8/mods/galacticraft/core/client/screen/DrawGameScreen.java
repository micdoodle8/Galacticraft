package micdoodle8.mods.galacticraft.core.client.screen;

import com.mojang.blaze3d.platform.GLX;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.client.IScreenManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.MapUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class DrawGameScreen implements IScreenManager
{
    private final TextureManager textureManager = Minecraft.getInstance().textureManager;
    private static final FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
    private static final int texCount = 1;

    private float tickDrawn = -1F;
    public boolean initialise = true;
    public boolean initialiseLast = false;
    private boolean readyToInitialise = false;
    private int tileCount = 0;
    private int callCount = 0;
    private int tickMapDone = -1;

    private final float scaleX;
    private final float scaleZ;

    public TileEntity driver;
    public EntityType<?> telemetryLastType;
    public String telemetryLastName;
    public Entity telemetryLastEntity;
    public EntityRenderer telemetryLastRender;
    public static DynamicTexture reusableMap;  //This will be set up in MapUtil.resetClientBody()
    //    public int[] localMap = null;
    private NativeImage localMap;
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
        if (this.mapDone || reusableMap == null || GCCoreUtil.getDimensionType(this.driver.getWorld()) != DimensionType.OVERWORLD)
        {
            return;
        }
        this.localMap = new NativeImage(MapUtil.SIZE_STD2, MapUtil.SIZE_STD2, false);
//        this.localMap = new int[MapUtil.SIZE_STD2 * MapUtil.SIZE_STD2];
        boolean result = MapUtil.getMap(this.localMap, this.driver.getWorld(), this.driver.getPos());
        if (result)
        {
            this.localMap.uploadTextureSub(0, 0, 0, false);
//            TextureUtil.uploadTexture(reusableMap.getGlTextureId(), this.localMap, MapUtil.SIZE_STD2, MapUtil.SIZE_STD2);
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
////        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
////        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
////        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
//        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
//
//        if (type > 0) TODO Drawing
//        {
//            GL11.glDisable(GL11.GL_LIGHTING);
//        }
//
//        GalacticraftRegistry.getGameScreen(type).render(type, ticks, scaleX, scaleZ, this);
//
//        if (type > 0)
//        {
//            GL11.glEnable(GL11.GL_LIGHTING);
//        }
//
////        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
    }

    @Override
    public Dimension getWorldProvider()
    {
        if (this.driver != null)
        {
            return driver.getWorld().dimension;
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
