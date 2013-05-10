//package micdoodle8.mods.galacticraft.core.dimension;
//
//import micdoodle8.mods.galacticraft.API.IExitHeight;
//import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
//import micdoodle8.mods.galacticraft.API.ISolarLevel;
//import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
//import micdoodle8.mods.galacticraft.core.wgen.GCCoreChunkProviderInnerSpace;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.MathHelper;
//import net.minecraft.util.Vec3;
//import net.minecraft.world.WorldProvider;
//import net.minecraft.world.chunk.Chunk;
//import net.minecraft.world.chunk.IChunkProvider;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//
///**
// * Copyright 2012-2013, micdoodle8
// *
// *  All rights reserved.
// *
// */
//public class GCCoreWorldProviderInnerSpace extends WorldProvider implements ISolarLevel, IExitHeight, IGalacticraftWorldProvider
//{
//    private final float[] colorsSunriseSunset = new float[4];
//
//    @Override
//	public void setDimension(int var1)
//    {
//        this.dimensionId = var1;
//        super.setDimension(var1);
//    }
//
//    @Override
//	public IChunkProvider createChunkGenerator()
//    {
//    	return new GCCoreChunkProviderInnerSpace(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
//    }
//
//	@Override
//    protected void generateLightBrightnessTable()
//    {
//        final float var1 = 0.0F;
//
//        for (int var2 = 0; var2 <= 15; ++var2)
//        {
//            final float var3 = 1.0F - var2 / 15.0F;
//            this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
//        }
//    }
//
//	@Override
//    public float[] calcSunriseSunsetColors(float var1, float var2)
//    {
//		return null;
//    }
//
//	@SideOnly(Side.CLIENT)
//	@Override
//    public Vec3 getFogColor(float var1, float var2)
//    {
//		return this.worldObj.getWorldVec3Pool().getVecFromPool((double)0F / 255F, (double)0F / 255F, (double)0F / 255F);
//    }
//
//	@Override
//    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
//    {
//        return this.worldObj.getWorldVec3Pool().getVecFromPool(0, 0, 0);
//    }
//
//	@Override
//    public float calculateCelestialAngle(long par1, float par3)
//    {
//        return 1F;
//    }
//
//    @Override
//	@SideOnly(Side.CLIENT)
//    public float getStarBrightness(float par1)
//    {
//        final float var2 = this.worldObj.getCelestialAngle(par1);
//        float var3 = 1.0F - (MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.25F);
//
//        if (var3 < 0.0F)
//        {
//            var3 = 0.0F;
//        }
//
//        if (var3 > 1.0F)
//        {
//            var3 = 1.0F;
//        }
//
//        return var3 * var3 * 0.5F + 0.3F;
//    }
//
//	@Override
//	public void updateWeather()
//	{
//        this.worldObj.getWorldInfo().setRainTime(0);
//        this.worldObj.getWorldInfo().setRaining(false);
//        this.worldObj.getWorldInfo().setThunderTime(0);
//        this.worldObj.getWorldInfo().setThundering(false);
//	}
//
//    @Override
//	public boolean isSkyColored()
//    {
//        return false;
//    }
//
//    @Override
//	public double getHorizon()
//    {
//    	return 44.0D;
//    }
//
//    @Override
//	public int getAverageGroundLevel()
//    {
//        return 44;
//    }
//
//    @Override
//	public boolean isSurfaceWorld()
//    {
//        return false;
//    }
//
//    @Override
//    public boolean canCoordinateBeSpawn(int var1, int var2)
//    {
//    	return false;
//    }
//
//    @Override
//    public boolean canRespawnHere()
//    {
//        return GCCoreConfigManager.canRespawnOnSpaceStations;
//    }
//
//    @Override
//	public String getWelcomeMessage()
//    {
//        return "Leaving Atmosphere";
//    }
//
//    @Override
//	public String getDepartMessage()
//    {
//        return "Entering Atmosphere";
//    }
//
//	@Override
//	public String getDimensionName()
//	{
//		return "Space";
//	}
//
//	@Override
//    public boolean canSnowAt(int x, int y, int z)
//    {
//        return false;
//    }
//
//	@Override
//    public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
//    {
//        return false;
//    }
//
//    @Override
//	public boolean canDoLightning(Chunk chunk)
//    {
//        return false;
//    }
//
//    @Override
//	public boolean canDoRainSnowIce(Chunk chunk)
//    {
//        return false;
//    }
//
//    @Override
//	public String getSaveFolder()
//    {
//        return "DIM_SPACE";
//    }
//
//	@Override
//	public double getSolorEnergyMultiplier()
//	{
//		return 1.0;
//	}
//
//	@Override
//	public double getYCoordinateToTeleport()
//	{
//		return Double.MAX_VALUE;
//	}
//
//	@Override
//	public float getGravity() 
//	{
//		return 0.065F;
//	}
//
//	@Override
//	public double getMeteorFrequency() 
//	{
//		return 0;
//	}
//
//	@Override
//	public double getFuelUsageMultiplier()
//	{
//		return 0;
//	}
//}
