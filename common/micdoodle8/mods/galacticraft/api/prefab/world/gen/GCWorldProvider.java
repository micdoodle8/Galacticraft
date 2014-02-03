package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import java.lang.reflect.Constructor;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class GCWorldProvider extends WorldProvider implements IGalacticraftWorldProvider
{
	public abstract Class<? extends GCChunkProvider> getChunkProviderClass();

	public abstract Class<? extends GCWorldChunkManager> getWorldChunkManagerClass();

	@Override
	public void setDimension(int var1)
	{
		this.dimensionId = var1;
		super.setDimension(var1);
	}

	@Override
	protected void generateLightBrightnessTable()
	{
		final float var1 = 0.0F;

		for (int var2 = 0; var2 <= 15; ++var2)
		{
			final float var3 = 1.0F - var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}
	}

	@Override
	public float[] calcSunriseSunsetColors(float var1, float var2)
	{
		return null;
	}

	@Override
	public void registerWorldChunkManager()
	{
		try
		{
			Class<? extends GCWorldChunkManager> chunkManagerClass = this.getWorldChunkManagerClass();
			Constructor<? extends GCWorldChunkManager> c = chunkManagerClass.getConstructor(World.class, Long.class, Boolean.class);
			this.worldChunkMgr = c.newInstance(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Vec3 getFogColor(float var1, float var2)
	{
		return this.worldObj.getWorldVec3Pool().getVecFromPool((double) 0F / 255F, (double) 0F / 255F, (double) 0F / 255F);
	}

	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
	{
		return this.worldObj.getWorldVec3Pool().getVecFromPool(0, 0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{
		final float var2 = this.worldObj.getCelestialAngle(par1);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float) Math.PI * 2.0F) * 2.0F + 0.25F);

		if (var3 < 0.0F)
		{
			var3 = 0.0F;
		}

		if (var3 > 1.0F)
		{
			var3 = 1.0F;
		}

		return var3 * var3 * 0.5F + 0.3F;
	}

	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		return super.calculateCelestialAngle(par1, par3);
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		try
		{
			Class<? extends GCChunkProvider> chunkProviderClass = this.getChunkProviderClass();
			Constructor<? extends GCChunkProvider> c = chunkProviderClass.getConstructor(World.class, Long.class, Boolean.class);
			GCChunkProvider o = c.newInstance(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
			return o;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void updateWeather()
	{
		this.worldObj.getWorldInfo().setRainTime(0);
		this.worldObj.getWorldInfo().setRaining(false);
		this.worldObj.getWorldInfo().setThunderTime(0);
		this.worldObj.getWorldInfo().setThundering(false);
	}

	@Override
	public boolean isSkyColored()
	{
		return true;
	}

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
	public boolean canSnowAt(int x, int y, int z)
	{
		return false;
	}

	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
	{
		return false;
	}

	@Override
	public boolean canDoLightning(Chunk chunk)
	{
		return false;
	}

	@Override
	public boolean canDoRainSnowIce(Chunk chunk)
	{
		return false;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return true;
	}

	@Override
	public boolean canCoordinateBeSpawn(int var1, int var2)
	{
		return true;
	}

	@Override
	public boolean canRespawnHere()
	{
		return true;
	}

	@Override
	public String getWelcomeMessage()
	{
		return "Entering " + this.getDimensionName();
	}

	@Override
	public String getDepartMessage()
	{
		return "Leaving " + this.getDimensionName();
	}
}
