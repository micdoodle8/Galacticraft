package micdoodle8.mods.galacticraft.api.world;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import net.minecraft.world.WorldProvider;

public abstract class SpaceWorldProvider extends WorldProvider
{
	public abstract CelestialBody getCelestialBody();

    public String getDimensionName()
    {
    	return this.getCelestialBody().getLocalizedName();
    }
}
