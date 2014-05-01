package micdoodle8.mods.galacticraft.api.world;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import net.minecraft.world.WorldProvider;

public abstract class WorldProviderSpace extends WorldProvider implements IGalacticraftWorldProvider
{
	public abstract CelestialBody getCelestialBody();

	@Override
	public String getDimensionName()
	{
		return this.getCelestialBody().getLocalizedName();
	}

    public void updateWeather()
    {
		this.worldObj.getWorldInfo().setRainTime(0);
		this.worldObj.getWorldInfo().setRaining(false);
		this.worldObj.getWorldInfo().setThunderTime(0);
		this.worldObj.getWorldInfo().setThundering(false);
    	this.worldObj.rainingStrength = 0.0F;
    	this.worldObj.thunderingStrength = 0.0F;
    }
}
