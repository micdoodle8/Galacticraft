package micdoodle8.mods.galacticraft;

import micdoodle8.mods.galacticraft.client.GCSkyProvider;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Chunk;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.Vec3;
import net.minecraft.src.WorldProvider;
import net.minecraftforge.client.SkyProvider;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCWorldProvider extends WorldProvider
{
    private float[] colorsSunriseSunset = new float[4];
    
	public GCWorldProvider()
    {
        this.setDimension(GCConfigManager.dimensionIDMars);
        this.dimensionId = GCConfigManager.dimensionIDMars;
    }
	
	@Override
    protected void generateLightBrightnessTable()
    {
        float var1 = 0.0F;

        for (int var2 = 0; var2 <= 15; ++var2)
        {
            float var3 = 1.0F - (float)var2 / 15.0F;
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
        this.worldChunkMgr = new GCWorldChunkManager(this.worldObj);
    }

	@SideOnly(Side.CLIENT)
	@Override
    public Vec3 getFogColor(float var1, float var2)
    {
        return Vec3.getVec3Pool().getVecFromPool((double)255F / 255F, (double)193F / 255F, (double)37F / 255F);
    }
	
	@Override
    public float calculateCelestialAngle(long par1, float par3)
    {
        int var4 = (int)(par1 % 24000L);
        float var5 = ((float)var4 + par3) / 24000.0F - 0.25F;

        if (var5 < 0.0F)
        {
            ++var5;
        }

        if (var5 > 1.0F)
        {
            --var5;
        }

        float var6 = var5;
        var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5 * 0.8F;
    }
	
	public float calculatePhobosAngle(long par1, float par3)
	{
		return this.calculateCelestialAngle(par1, par3) * 3000;
	}
	
	public float calculateDeimosAngle(long par1, float par3)
	{
		return calculatePhobosAngle(par1, par3) * 0.0000000001F;
	}

	@Override
    public IChunkProvider getChunkProvider()
    {
        return new GCChunkProvider(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
    }
	
	@Override
	public void updateWeather()
	{
        worldObj.getWorldInfo().setRainTime(0);
        worldObj.getWorldInfo().setRaining(false);
        worldObj.getWorldInfo().setThunderTime(0);
        worldObj.getWorldInfo().setThundering(false);
	}

    public boolean isSkyColored()
    {
        return true;
    }
    
    public double getHorizon()
    {
    	return 44.0D;
    }

    public int getAverageGroundLevel()
    {
        return 44;
    }
    
    public boolean isSurfaceWorld()
    {
        return false;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2)
    {
        int var3 = this.worldObj.getFirstUncoveredBlock(var1, var2);
        return var3 == GCBlocks.marsGrass.blockID;
    }
    
    @Override
    public boolean canRespawnHere()
    {
        return false;
    }
    
    @Override
    public String getSaveFolder()
    {
    	return "DIM" + GCConfigManager.dimensionIDMars;
    }

    public String getWelcomeMessage()
    {
        return "Entering Mars";
    }

    public String getDepartMessage()
    {
        return "Leaving Mars";
    }

	@Override
	public String getDimensionName() 
	{
		return "Mars";
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
    
    public boolean canDoLightning(Chunk chunk)
    {
        return false;
    }
    
    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return false;
    }
}
