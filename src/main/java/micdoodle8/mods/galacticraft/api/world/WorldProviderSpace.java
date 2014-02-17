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
}
