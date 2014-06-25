package micdoodle8.mods.galacticraft.api.galaxies;

import java.util.Locale;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.util.StatCollector;

public class SolarSystem
{
	protected final String systemName;
	protected String unlocalizedName;
	protected Vector3 mapPosition = null;

	public SolarSystem(String solarSystem)
	{
		this.systemName = solarSystem.toLowerCase(Locale.ENGLISH);
		this.unlocalizedName = solarSystem;
	}

	public String getName()
	{
		return this.systemName;
	}

	public final int getID()
	{
		return GalaxyRegistry.getSolarSystemID(this.systemName);
	}

	public String getLocalizedName()
	{
		String s = this.getUnlocalizedName();
		return s == null ? "" : StatCollector.translateToLocal(s);
	}

	public String getUnlocalizedName()
	{
		return "solarsystem." + this.unlocalizedName;
	}

	public Vector3 getMapPosition()
	{
		return this.mapPosition;
	}

	public SolarSystem setMapPosition(Vector3 mapPosition)
	{
		this.mapPosition = mapPosition;
		return this;
	}
}
