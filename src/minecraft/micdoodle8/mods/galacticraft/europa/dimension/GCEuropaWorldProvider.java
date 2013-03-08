package micdoodle8.mods.galacticraft.europa.dimension;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.europa.GCEuropaConfigManager;
import micdoodle8.mods.galacticraft.europa.wgen.GCEuropaChunkProvider;
import micdoodle8.mods.galacticraft.europa.wgen.GCEuropaWorldChunkManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCEuropaWorldProvider extends WorldProvider implements IGalacticraftWorldProvider
{
    private final float[] colorsSunriseSunset = new float[4];

	public GCEuropaWorldProvider()
    {
        this.setDimension(GCEuropaConfigManager.dimensionIDEuropa);
        this.dimensionId = GCEuropaConfigManager.dimensionIDEuropa;
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
        this.worldChunkMgr = new GCEuropaWorldChunkManager(this.worldObj, 0F);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int par1, int par2)
    {
        return false;
    }

	@SideOnly(Side.CLIENT)
	@Override
    public Vec3 getFogColor(float var1, float var2)
    {
        return this.worldObj.getWorldVec3Pool().getVecFromPool((double)100F / 255F, (double)100F / 255F, (double)100F / 255F);
    }

	@Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
    {
        return this.worldObj.getWorldVec3Pool().getVecFromPool(0, 0, 0);
    }

	@Override
    public float calculateCelestialAngle(long par1, float par3)
    {
        final int var4 = (int)(par1 % 48000L);
        float var5 = (var4 + par3) / 48000.0F - 0.25F;

        if (var5 < 0.0F)
        {
            ++var5;
        }

        if (var5 > 1.0F)
        {
            --var5;
        }

        final float var6 = var5;
        var5 = 1.0F - (float)((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
    }

	public float calculatePhobosAngle(long par1, float par3)
	{
		return this.calculateCelestialAngle(par1, par3) * 3000;
	}

	public float calculateDeimosAngle(long par1, float par3)
	{
		return this.calculatePhobosAngle(par1, par3) * 0.0000000001F;
	}

	@Override
    public IChunkProvider createChunkGenerator()
    {
        return new GCEuropaChunkProvider(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
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
	public boolean isSurfaceWorld()
    {
        return false;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2)
    {
    	return true;
//        int var3 = this.worldObj.getFirstUncoveredBlock(var1, var2);
//        return var3 == GCMarsBlocks.marsGrass.blockID;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        final float var2 = this.worldObj.getCelestialAngle(par1);
        float var3 = 1.0F - (MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.25F);

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
    public boolean canRespawnHere()
    {
        return false;
    }

    @Override
    public String getSaveFolder()
    {
    	return "DIM" + GCEuropaConfigManager.dimensionIDEuropa;
    }

    @Override
	public String getWelcomeMessage()
    {
        return "Entering Europa";
    }

    @Override
	public String getDepartMessage()
    {
        return "Leaving Europa";
    }

	@Override
	public String getDimensionName()
	{
		return "Europa";
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
	public float getGravity()
	{
		return 0.069F;
	}

	@Override
	public float getMeteorFrequency()
	{
		return 0F;
	}
}
