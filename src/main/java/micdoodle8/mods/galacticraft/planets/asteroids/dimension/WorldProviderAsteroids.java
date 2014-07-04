package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.ChunkProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.WorldChunkManagerAsteroids;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderAsteroids extends WorldProviderSpace
{
//	@Override
//	public void registerWorldChunkManager()
//	{
//		this.worldChunkMgr = new WorldChunkManagerAsteroids(this.worldObj, 0F);
//	}

    @Override
    public CelestialBody getCelestialBody()
    {
        return AsteroidsModule.planetAsteroids;
    }

    @Override
    public Vector3 getFogColor()
    {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Vector3 getSkyColor()
    {
        return new Vector3(0, 0, 0);
    }

    @Override
    public boolean canRainOrSnow()
    {
        return false;
    }

    @Override
    public boolean hasSunset()
    {
        return false;
    }

    @Override
    public long getDayLength()
    {
        return 0;
    }

    @Override
    public Class<? extends IChunkProvider> getChunkProviderClass()
    {
        return ChunkProviderAsteroids.class;
    }

    @Override
    public Class<? extends WorldChunkManager> getWorldChunkManagerClass()
    {
        return WorldChunkManagerAsteroids.class;
    }

    @Override
    public boolean shouldForceRespawn()
    {
        return !ConfigManagerCore.forceOverworldRespawn;
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3)
    {
        return 0.25F;
    }

    @Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{
		return 1.0F;
	}

//	@Override
//	public IChunkProvider createChunkGenerator()
//	{
//		return new ChunkProviderAsteroids(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
//	}

	@Override
	public double getHorizon()
	{
		return 44.0D;
	}

	@Override
	public int getAverageGroundLevel()
	{
		return 44;
	}

	@Override
	public boolean canCoordinateBeSpawn(int var1, int var2)
	{
		return true;
	}

    @Override
	public float getGravity()
	{
		return 0.072F;
	}

	@Override
	public int getHeight()
	{
		return 800;
	}

	@Override
	public double getMeteorFrequency()
	{
		return 10.0D;
	}

	@Override
	public double getFuelUsageMultiplier()
	{
		return 0.9D;
	}

	@Override
	public boolean canSpaceshipTierPass(int tier)
	{
		return tier >= 2;
	}

	@Override
	public float getFallDamageModifier()
	{
		return 0.38F;
	}

	@Override
	public float getSoundVolReductionAmount()
	{
		return 10.0F;
	}

	@Override
	public boolean hasBreathableAtmosphere()
	{
		return false;
	}

	@Override
	public float getThermalLevelModifier()
	{
		return -1.5F;
	}
}
