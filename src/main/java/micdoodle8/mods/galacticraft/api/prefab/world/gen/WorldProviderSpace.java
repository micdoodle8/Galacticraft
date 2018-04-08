package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.JavaUtil;
import net.minecraft.command.CommandTime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class WorldProviderSpace extends WorldProvider implements IGalacticraftWorldProvider
{
    private long timeCurrentOffset = 0L;
    public long preTickTime = Long.MIN_VALUE;
    private long saveTCO = 0L;
    static Field tickCounter;
    
    static
    {
        try
        {
            tickCounter = VillageCollection.class.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "tickCounter" : "field_75553_e");
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
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
     *
     * @deprecated Use new shouldDisablePrecipitation method in IGalacticraftWorldProvider interface
     */
    @Deprecated
    public boolean canRainOrSnow()
    {
        return false;
    }

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

    public abstract Class<? extends IChunkGenerator> getChunkProviderClass();

    @Deprecated
    /**
     * If possible you should not override this so that BiomeProviderDefault is used (see Moon, Mars and Asteroids for examples)
     * 
     * But this can be used for planets where you have multiple biomes
     * In that case if using BiomeAdaptive (see Venus for example) this will need a BiomeAdaptive.setBodyMultiBiome() call;
     */
    public Class<? extends BiomeProvider> getBiomeProviderClass()
    {
        return null;
    }

    @Override
    public void setDimension(int var1)
    {
        super.setDimension(var1);
    }

    @Override
    public void updateWeather()
    {
        if (!this.world.isRemote)
        {
            long newTime = world.getWorldInfo().getWorldTime();
            if (this.preTickTime == Long.MIN_VALUE)
            {
                //First tick: get the timeCurrentOffset from saved ticks in villages.dat :)
                int savedTick = 0;
                try {
                    tickCounter.setAccessible(true);
                    savedTick = tickCounter.getInt(this.world.villageCollection);
                    if (savedTick < 0) savedTick = 0;
                } catch (Exception ignore) { }
                this.timeCurrentOffset = savedTick - newTime;
            }
            else
            {
                //Detect jumps in world time (e.g. because of bed use on Overworld) and reverse them for this world
                long diff = (newTime - this.preTickTime);
                if (diff > 1L)
                {
                    this.timeCurrentOffset -= diff - 1L;
                    this.saveTime();
                }
            }
            this.preTickTime = newTime;
            this.saveTCO = 0L;
        }
        
        if (this.shouldDisablePrecipitation())
        {
            this.world.getWorldInfo().setRainTime(0);
            this.world.getWorldInfo().setRaining(false);
            this.world.getWorldInfo().setThunderTime(0);
            this.world.getWorldInfo().setThundering(false);
            this.world.rainingStrength = 0.0F;
            this.world.thunderingStrength = 0.0F;
        }
        else
        {
            this.updateWeatherOverride();
        }
    }

    /*
     * Override this to circumvent vanilla updateWeather()
     */
    protected void updateWeatherOverride()
    {
        super.updateWeather();
    }

    @Override
    public String getSaveFolder()
    {
        return "DIM" + this.getCelestialBody().getDimensionID();
    }

    @Override
    public boolean isGasPresent(EnumAtmosphericGas gas)
    {
        return this.getCelestialBody().atmosphere.isGasPresent(gas);
    }

    @Override
    public boolean hasNoAtmosphere()
    {
        return this.getCelestialBody().atmosphere.hasNoGases();
    }

    @Override
    public boolean hasBreathableAtmosphere()
    {
        return this.getCelestialBody().atmosphere.isBreathable();
    }
    
    @Override
    public boolean shouldDisablePrecipitation()
    {
        return !this.getCelestialBody().atmosphere.hasPrecipitation();
    }

    @Override
    public float getSoundVolReductionAmount()
    {
        float d = this.getCelestialBody().atmosphere.relativeDensity();
        if (d <= 0.0F)
        {
            return 20.0F;
        }
        if (d > 5.0F)
        {
            return 0.2F;
        }
        return 1.0F / d;
    }

    @Override
    public double getMeteorFrequency()
    {
        float d = this.getCelestialBody().atmosphere.relativeDensity();
        if (d <= 0.0F)
        {
            return 5.0D;
        }
        return d * 100D; 
    }

    @Override
    public float getThermalLevelModifier()
    {
        return this.getCelestialBody().atmosphere.thermalLevel();
    }
    
    @Override
    public float getWindLevel()
    {
        return this.getCelestialBody().atmosphere.windLevel();
    }

    @Override
    public boolean shouldCorrodeArmor()
    {
        return this.getCelestialBody().atmosphere.isCorrosive();
    }

    @Override
    public boolean canBlockFreeze(BlockPos pos, boolean byWater)
    {
        //TODO: if block is water and world temperature is low, freeze
        return super.canBlockFreeze(pos, byWater);
    }

    @Override
    public boolean canDoLightning(Chunk chunk)
    {
        return !this.shouldDisablePrecipitation();
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return !this.shouldDisablePrecipitation();
    }

    @Override
    public float getSolarSize()
    {
        return 1.0F / this.getCelestialBody().getRelativeDistanceFromCenter().unScaledDistance;
    }

    @Override
    public float[] calcSunriseSunsetColors(float var1, float var2)
    {
        return this.hasSunset() ? super.calcSunriseSunsetColors(var1, var2) : null;
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3)
    {
        par1 = this.getWorldTime();
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
    public Vec3d getFogColor(float var1, float var2)
    {
        Vector3 fogColor = this.getFogColor();
        return new Vec3d(fogColor.floatX(), fogColor.floatY(), fogColor.floatZ());
    }

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks)
    {
        Vector3 skyColor = this.getSkyColor();
        return new Vec3d(skyColor.floatX(), skyColor.floatY(), skyColor.floatZ());
    }

    @Override
    public boolean isSkyColored()
    {
        return true;
    }

    /**
     * Do not override this.
     * 
     * Returns true on clients (to allow rendering of sky etc, maybe even clouds).
     * Returns false on servers (to disable Nether Portal mob spawning and sleeping in beds).
     */
    @Override
    public boolean isSurfaceWorld()
    {
        return (this.world == null) ? false : this.world.isRemote;
    }

    /**
     * This must normally return false, so that if the dimension is set for 'static' loading
     * it will not keep chunks around the dimension spawn position permanently loaded.
     * It is also needed to be false so that the 'Force Overworld Respawn' setting in core.conf
     * will work correctly - see also WorldProviderS[ace.getRespawnDimension(). 
     * 
     * But: returning 'false' will cause beds to explode in this dimension.
     * If you want beds NOT to explode, you can override this, like in WorldProviderMoon.
     */
	@Override
	public boolean canRespawnHere()
	{
		return false;
	}
	
    /**
     * Do NOT override this in your add-ons.
     * 
     * This controls whether the player will respawn in the space dimension or the Overworld
     * in accordance with the 'Force Overworld Respawn' setting on core.conf.
     */
    @Override
    public int getRespawnDimension(EntityPlayerMP player)
    {
        return this.shouldForceRespawn() ? this.getDimension() : 0;
    }

    /**
     * If true, the the player should respawn in this dimension upon death.
     * 
     * Obeying the 'Force Overworld Respawn' setting from core.conf is an important protection
     * for players are endlessly dying in a space dimension: for example respawning
     * in an airless environment with no oxygen tanks and no oxygen machinery.       
     */
    public boolean shouldForceRespawn()
    {
        return !ConfigManagerCore.forceOverworldRespawn;
    }

    /**
     * If false (the default) then Nether Portals will have no function on this world.
     * Nether Portals can still be constructed, if the player can make fire, they just
     * won't do anything.
     * 
     * @return True if Nether Portals should work like on the Overworld.
     */
    @Override
    public boolean netherPortalsOperational()
    {
        return false;
    }

    @Override
    public float getArrowGravity()
    {
        return 0.005F;
    }
    
    @Override
    public IChunkGenerator createChunkGenerator()
    {
        try
        {
            Class<? extends IChunkGenerator> chunkProviderClass = this.getChunkProviderClass();

            Constructor<?>[] constructors = chunkProviderClass.getConstructors();
            for (int i = 0; i < constructors.length; i++)
            {
                Constructor<?> constr = constructors[i];
                if (Arrays.equals(constr.getParameterTypes(), new Object[] { World.class, long.class, boolean.class }))
                {
                    return (IChunkGenerator) constr.newInstance(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled());
                }
                else if (constr.getParameterTypes().length == 0)
                {
                    return (IChunkGenerator) constr.newInstance();
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
    protected void init()
    {
        this.hasSkyLight = true;

        if (this.getBiomeProviderClass() != null)
        {
            try
            {
                Class<? extends BiomeProvider> biomeProviderClass = this.getBiomeProviderClass();

                Constructor<?>[] constructors = biomeProviderClass.getConstructors();
                for (Constructor<?> constr : constructors)
                {
                    if (Arrays.equals(constr.getParameterTypes(), new Object[] { World.class }))
                    {
                        this.biomeProvider = (BiomeProvider) constr.newInstance(this.world);
                    }
                    else if (constr.getParameterTypes().length == 0)
                    {
                        this.biomeProvider = (BiomeProvider) constr.newInstance();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            this.biomeProvider = new BiomeProviderDefault(this.getCelestialBody());
        }
    }

    @Override
    public BiomeProvider getBiomeProvider()
    {
        if (this.getBiomeProviderClass() == null)
        {
            BiomeAdaptive.setBody(this.getCelestialBody());
        }
        return biomeProvider;
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double y, double z)
    {
        return false;
    }

    //Work around vanilla feature: worlds which are not the Overworld cannot change the time, as the worldInfo is a DerivedWorldInfo
    //Therefore each Galacticraft dimension maintains its own timeCurrentOffset
    @Override
    public void setWorldTime(long time)
    {
        world.getWorldInfo().setWorldTime(time);
        if (!world.isRemote)
        {
            if (JavaUtil.instance.isCalledBy(CommandTime.class))
            {
                this.timeCurrentOffset = this.saveTCO;
                this.saveTime();
                this.preTickTime = time;
            }
            else
            {
                long newTCO = time - world.getWorldInfo().getWorldTime();
                long diff = newTCO - this.timeCurrentOffset;
                if (diff > 1L || diff < -1L)
                {
                    this.timeCurrentOffset = newTCO; 
                    this.saveTime();
                    this.preTickTime = time;
                }
            }
            this.saveTCO = 0;
        }
    }

    @Override
    public long getWorldTime()
    {
        if (JavaUtil.instance.isCalledBy(CommandTime.class))
        {
            this.saveTCO  = this.timeCurrentOffset;
        }
        return world.getWorldInfo().getWorldTime() + this.timeCurrentOffset;
    }
    
    /**
     * Adjust time offset on Galacticraft worlds when the Overworld time jumps and you don't want the time
     * on all the other Galacticraft worlds to jump also - see WorldUtil.setNextMorning() for example
     */
    public void adjustTimeOffset(long diff)
    {
        if (diff != 0L)
        {
            this.timeCurrentOffset -= diff;
            this.preTickTime += diff;
            this.saveTime();
        }
    }
    
    public void adjustTime(long newTime)
    {
        long diff = newTime - this.preTickTime;
        if (diff != 0L)
        {
            this.timeCurrentOffset -= diff;
            this.preTickTime = newTime;
            this.saveTime();
        }
    }
    
    /**
     * Save this world's custom time (from timeCurrentOffset) into this world's villages.dat :)
     */
    private void saveTime()
    {
        try {
            VillageCollection vc = this.world.villageCollection;
            tickCounter.setAccessible(true);
            tickCounter.setInt(vc, (int) (this.getWorldTime()));
            vc.markDirty();
        } catch (Exception ignore) { }
    }
}
