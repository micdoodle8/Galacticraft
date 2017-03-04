package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderOrbit;
import micdoodle8.mods.galacticraft.core.world.gen.WorldChunkManagerOrbit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderOrbit extends WorldProviderZeroGravity implements IOrbitDimension, ISolarLevel, IExitHeight
{
    @Override
    public void setDimension(int var1)
    {
        super.setDimension(var1);
    }

    @Override
    public CelestialBody getCelestialBody()
    {
        return GalacticraftCore.satelliteSpaceStation;
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
    public boolean hasSunset()
    {
        return false;
    }

    @Override
    public long getDayLength()
    {
        return 24000L;
    }

    @Override
    public boolean shouldForceRespawn()
    {
        return !ConfigManagerCore.forceOverworldRespawn;
    }

    @Override
    public Class<? extends IChunkProvider> getChunkProviderClass()
    {
        return ChunkProviderOrbit.class;
    }

    @Override
    public Class<? extends WorldChunkManager> getWorldChunkManagerClass()
    {
        return WorldChunkManagerOrbit.class;
    }

    @Override
    public boolean isDaytime()
    {
        final float a = this.worldObj.getCelestialAngle(0F);
        //TODO: adjust this according to size of planet below
        return a < 0.42F || a > 0.58F;
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
    public void updateWeather()
    {
        super.updateWeather();
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
        return 64;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2)
    {
        return true;
    }

    //Overriding only in case the Galacticraft API is not up-to-date
    //(with up-to-date API this makes zero difference)
    @Override
    public boolean isSurfaceWorld()
    {
        return this.worldObj != null && this.worldObj.isRemote;
    }

    //Overriding only in case the Galacticraft API is not up-to-date
    //(with up-to-date API this makes zero difference)
    @Override
    public boolean canRespawnHere()
    {
        return false;
    }

    //Overriding only in case the Galacticraft API is not up-to-date
    //(with up-to-date API this makes zero difference)
    @Override
    public int getRespawnDimension(EntityPlayerMP player)
    {
        return this.shouldForceRespawn() ? this.dimensionId : 0;
    }

//	@Override
//	public String getWelcomeMessage()
//	{
//		return "Entering Earth Orbit";
//	}
//
//	@Override
//	public String getDepartMessage()
//	{
//		return "Leaving Earth Orbit";
//	}

    @Override
    public String getDimensionName()
    {
        return "Space Station " + this.dimensionId;
    }

    @Override
    public float getGravity()
    {
        return 0.075F;
    }

    @Override
    public boolean hasBreathableAtmosphere()
    {
        return false;
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
        return "DIM_SPACESTATION" + this.dimensionId;
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

    @Override
    public float getThermalLevelModifier()
    {
        return 0;
    }

    @Override
    public float getWindLevel()
    {
        return 0.1F;
    }

    @Override
    public String getInternalNameSuffix()
    {
        return "_orbit";
    }

    @Override
    public boolean shouldDisablePrecipitation()
    {
        return true;
    }

    @Override
    public boolean shouldCorrodeArmor()
    {
        return false;
    }
}
