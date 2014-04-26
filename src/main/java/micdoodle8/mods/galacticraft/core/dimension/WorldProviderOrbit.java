package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderOrbit;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreWorldProviderSpaceStation.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8, radfast
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class WorldProviderOrbit extends WorldProvider implements IOrbitDimension, ISolarLevel, IExitHeight
{
	public int spaceStationDimensionID;
	//Used to decide whether to render this as a rotating spacestation - set true for now, for testing purposes
	public boolean doSpinning = true;
	private float angularVelocityRadians = 0.005F;
	private float skyAngularVelocity = (float) (this.angularVelocityRadians * 180 / Math.PI);
	private int counti=0;
	
	@Override
	public void setDimension(int var1)
	{
		this.spaceStationDimensionID = var1;
		this.dimensionId = var1;
		super.setDimension(var1);
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkProviderOrbit(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
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
	public float calculateCelestialAngle(long par1, float par3)
	{
		final int var4 = (int) (par1 % 24000L);
		float var5 = (var4 + par3) / 24000.0F - 0.25F;

		if (var5 < 0.0F)
		{
			++var5;
		}

		if (var5 > 1.0F)
		{
			--var5;
		}

		final float var6 = var5;
		var5 = 1.0F - (float) ((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return var5;
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

	public float calculatePhobosAngle(long par1, float par3)
	{
		return this.calculateCelestialAngle(par1, par3) * 3000;
	}

	public float calculateDeimosAngle(long par1, float par3)
	{
		return this.calculatePhobosAngle(par1, par3) * 0.0000000001F;
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
		return false;
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
		return !ConfigManagerCore.forceOverworldRespawn;
	}

	@Override
	public String getWelcomeMessage()
	{
		return "Entering Earth Orbit";
	}

	@Override
	public String getDepartMessage()
	{
		return "Leaving Earth Orbit";
	}

	@Override
	public String getDimensionName()
	{
		return "Space Station " + this.spaceStationDimensionID;
	}

//	@Override
//	public boolean canSnowAt(int x, int y, int z)
//	{
//		return false;
//	} TODO Fix no snow

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
		return 0.073F;
	}

	@Override
	public double getMeteorFrequency()
	{
		return 0;
	}

	@Override
	public double getFuelUsageMultiplier()
	{
		return 0.5D;
	}

	@Override
	public String getPlanetToOrbit()
	{
		return "Overworld";
	}

	@Override
	public int getYCoordToTeleportToPlanet()
	{
		return 30;
	}

	@Override
	public String getSaveFolder()
	{
		return "DIM_SPACESTATION" + this.spaceStationDimensionID;
	}

	@Override
	public double getSolarEnergyMultiplier()
	{
		return ConfigManagerCore.spaceStationEnergyScalar;
	}

	@Override
	public double getYCoordinateToTeleport()
	{
		return 1200;
	}

	@Override
	public boolean canSpaceshipTierPass(int tier)
	{
		return tier > 0;
	}

	@Override
	public float getFallDamageModifier()
	{
		return 0.4F;
	}

	@Override
	public float getSoundVolReductionAmount()
	{
		return 50.0F;
	}
	
	public void spinUpdate(GCEntityClientPlayerMP p)
	{
		if (p.posY > 71D)
		{
			//TODO maybe need to test to make sure posX and posZ are not large (outside sight range of SS)
			double angle;
			if (p.posX==0D) angle = (p.posZ >0) ? Math.PI/2 : -Math.PI/2;
			else angle = Math.atan(p.posZ/p.posX);
			if (p.posX<0D) angle += Math.PI;
			angle += this.angularVelocityRadians/3D;
			double arc = Math.sqrt(p.posX*p.posX + p.posZ*p.posZ)*this.angularVelocityRadians;
			double offsetX = - arc * Math.sin(angle);
			double offsetZ = arc * Math.cos(angle);
			p.posX += offsetX;
			p.posZ += offsetZ;
			p.boundingBox.offset(offsetX, 0.0D, offsetZ);
			
			p.rotationYaw += this.skyAngularVelocity;
			while (p.rotationYaw > 360F) p.rotationYaw -= 360F;	
		}
	}
	
	public float getSpinRate()
	{
		return this.skyAngularVelocity;
	}
	
	/**
	 * Sets the spin rate for the dimension in radians per tick 
	 * For example, 0.031415 would be 1/200 revolution per tick
	 * So that would be 1 revolution every 10 seconds 
	 */
	public void setSpinRate(float angle)
	{
		this.angularVelocityRadians = angle;
		this.skyAngularVelocity = angle * 180F / 3.1415927F;
		IRenderHandler sky = this.getSkyRenderer();
		if (sky instanceof SkyProviderOrbit) ((SkyProviderOrbit)sky).spinDeltaPerTick = angle;
	}
}
