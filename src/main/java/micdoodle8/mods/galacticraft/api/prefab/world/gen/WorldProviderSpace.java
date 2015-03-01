package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public abstract class WorldProviderSpace extends WorldProvider implements IGalacticraftWorldProvider
{
    /**
     * The fog color in this dimension
     */
    public abstract Vector3 getFogColor();

    /**
     * The sky color in this dimension
     */
    public abstract Vector3 getSkyColor();

    /**
     * Whether or not there will be rain or snow in this dimension
     */
    public abstract boolean canRainOrSnow();

    /**
     * Whether or not to render vanilla sunset (can be overridden with custom sky provider)
     */
    public abstract boolean hasSunset();

    /**
     * The length of day in this dimension
     * <p/>
     * Default: 24000
     */
    public abstract long getDayLength();

    /**
     * Whether or not the player will respawn in this dimension.
     */
    public abstract boolean shouldForceRespawn();

    public abstract Class<? extends IChunkProvider> getChunkProviderClass();

    public abstract Class<? extends WorldChunkManager> getWorldChunkManagerClass();

    @Override
    public void setDimension(int var1)
    {
        this.dimensionId = var1;
        super.setDimension(var1);
    }

    @Override
    public String getDimensionName()
    {
        return this.getCelestialBody().getUnlocalizedName();
    }

    @Override
    public boolean isGasPresent(IAtmosphericGas gas)
    {
        return this.getCelestialBody().atmosphere.contains(gas);
    }

    @Override
    public void updateWeather()
    {
        if (this.canRainOrSnow())
        {
            super.updateWeather();
        }
        else
        {
            this.worldObj.getWorldInfo().setRainTime(0);
            this.worldObj.getWorldInfo().setRaining(false);
            this.worldObj.getWorldInfo().setThunderTime(0);
            this.worldObj.getWorldInfo().setThundering(false);
            this.worldObj.rainingStrength = 0.0F;
            this.worldObj.thunderingStrength = 0.0F;
        }
    }

    @Override
    public String getSaveFolder()
    {
        return "DIM" + this.getCelestialBody().getDimensionID();
    }

    @Override
    public String getWelcomeMessage()
    {
        return "Entering " + this.getCelestialBody().getLocalizedName();
    }

    @Override
    public String getDepartMessage()
    {
        return "Leaving " + this.getCelestialBody().getLocalizedName();
    }

    @Override
    public boolean isSurfaceWorld()
    {
        return true;
    }

    @Override
    public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
    {
        return this.canRainOrSnow();
    }

    @Override
    public boolean canDoLightning(Chunk chunk)
    {
        return this.canRainOrSnow();
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return this.canRainOrSnow();
    }

    @Override
    public float[] calcSunriseSunsetColors(float var1, float var2)
    {
        return this.hasSunset() ? super.calcSunriseSunsetColors(var1, var2) : null;
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3)
    {
        int j = (int) (par1 % this.getDayLength());
        float f1 = (j + par3) / this.getDayLength() - 0.25F;

        if (f1 < 0.0F)
        {
            ++f1;
        }

        if (f1 > 1.0F)
        {
            --f1;
        }

        float f2 = f1;
        f1 = 0.5F - MathHelper.cos(f1 * 3.1415927F) / 2.0F;
        return f2 + (f1 - f2) / 3.0F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vec3 getFogColor(float var1, float var2)
    {
        Vector3 fogColor = this.getFogColor();
        return Vec3.createVectorHelper(fogColor.floatX(), fogColor.floatY(), fogColor.floatZ());
    }

    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
    {
        Vector3 skyColor = this.getSkyColor();
        return Vec3.createVectorHelper(skyColor.floatX(), skyColor.floatY(), skyColor.floatZ());
    }

    @Override
    public boolean isSkyColored()
    {
        return true;
    }

    @Override
    public boolean canRespawnHere()
    {
        return this.shouldForceRespawn();
    }

    @Override
    public boolean hasBreathableAtmosphere()
    {
        return this.isGasPresent(IAtmosphericGas.OXYGEN) && !this.isGasPresent(IAtmosphericGas.CO2);
    }

    @Override
    public IChunkProvider createChunkGenerator()
    {
        try
        {
            Class<? extends IChunkProvider> chunkProviderClass = this.getChunkProviderClass();

            Constructor<?>[] constructors = chunkProviderClass.getConstructors();
            for (int i = 0; i < constructors.length; i++)
            {
                Constructor<?> constr = constructors[i];
                if (Arrays.equals(constr.getParameterTypes(), new Object[] { World.class, long.class, boolean.class }))
                {
                    return (IChunkProvider) constr.newInstance(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
                }
                else if (constr.getParameterTypes().length == 0)
                {
                    return (IChunkProvider) constr.newInstance();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void registerWorldChunkManager()
    {
        if (this.getWorldChunkManagerClass() == null)
        {
            super.registerWorldChunkManager();
        }
        else
        {
            try
            {
                Class<? extends WorldChunkManager> chunkManagerClass = this.getWorldChunkManagerClass();

                Constructor<?>[] constructors = chunkManagerClass.getConstructors();
                for (Constructor<?> constr : constructors)
                {
                    if (Arrays.equals(constr.getParameterTypes(), new Object[] { World.class }))
                    {
                        this.worldChunkMgr = (WorldChunkManager) constr.newInstance(this.worldObj);
                    }
                    else if (constr.getParameterTypes().length == 0)
                    {
                        this.worldChunkMgr = (WorldChunkManager) constr.newInstance();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double y, double z)
    {
        return false;
    }

    @Override
    public float getSolarSize()
    {
        return 1.0F / this.getCelestialBody().getRelativeDistanceFromCenter().unScaledDistance;
    }
}
