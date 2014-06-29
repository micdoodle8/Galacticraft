package micdoodle8.mods.galacticraft.api.galaxies;

import java.util.Locale;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;

import org.apache.commons.lang3.builder.EqualsBuilder;

public abstract class CelestialBody
{
	protected final String bodyName;
	protected String unlocalizedName;

	protected float relativeSize = 1.0F;
	protected float relativeDistanceFromCenter = 1.0F;
	protected float relativeOrbitTime = 1.0F;
	protected float phaseShift = 0.0F;
	protected int dimensionID = 0;
	protected Class<? extends WorldProvider> providerClass;
	protected boolean autoRegisterDimension = false;
	protected boolean isReachable = false;
	protected boolean forceStaticLoad = true;

	protected ResourceLocation celestialBodyIcon;

	protected float ringColorR = 1.0F;
	protected float ringColorG = 1.0F;
	protected float ringColorB = 1.0F;

	public CelestialBody(String bodyName)
	{
		this.bodyName = bodyName.toLowerCase(Locale.ENGLISH);
		this.unlocalizedName = bodyName;
	}

	public abstract int getID();

	public abstract String getUnlocalizedNamePrefix();

	public String getName()
	{
		return this.bodyName;
	}

	public String getUnlocalizedName()
	{
		return this.getUnlocalizedNamePrefix() + "." + this.unlocalizedName;
	}

	public String getLocalizedName()
	{
		String s = this.getUnlocalizedName();
		return s == null ? "" : StatCollector.translateToLocal(s);
	}

	/**
	 * Used for rendering planet's location on the map.
	 * 
	 * Value of 2.0F would result in the planet being rendered twice as large as
	 * earth.
	 * 
	 * @return Size of the planet/moon relative to earth.
	 */
	public float getRelativeSize()
	{
		return this.relativeSize;
	}

	/**
	 * Used for rendering planet's location on the map.
	 * 
	 * Value of 2.0F would result in an ellipse with twice the radius of the
	 * overworld.
	 * 
	 * @return Distance from the center of the map relative to earth.
	 */
	public float getRelativeDistanceFromCenter()
	{
		return this.relativeDistanceFromCenter;
	}

	/**
	 * Used for rendering planet's location on the map.
	 * 
	 * Value of 1440.0F would result in the planet being rendered directly
	 * accross from the original position
	 * 
	 * Value of 2880.0F is a full rotation and therefore would be rendered at
	 * the same spot as the original position
	 * 
	 * @return Phase shift of planet for planet's revolution around the sun.
	 */
	public float getPhaseShift()
	{
		return this.phaseShift;
	}

	/**
	 * Multiplier for length of time relative to earth that this planet takes to
	 * orbit fully.
	 * 
	 * Value of 2.0F would result in the planet rotating twice as slow (and
	 * therefore take twice as long) as the earth takes to revolve around the
	 * sun.
	 * 
	 * @return Multiple value for planet's revolution around the sun.
	 */
	public float getRelativeOrbitTime()
	{
		return this.relativeOrbitTime;
	}

	public CelestialBody setRelativeSize(float relativeSize)
	{
		this.relativeSize = relativeSize;
		return this;
	}

	public CelestialBody setRelativeDistanceFromCenter(float relativeDistanceFromCenter)
	{
		this.relativeDistanceFromCenter = relativeDistanceFromCenter;
		return this;
	}

	public CelestialBody setPhaseShift(float phaseShift)
	{
		this.phaseShift = phaseShift;
		return this;
	}

	public CelestialBody setRelativeOrbitTime(float relativeOrbitTime)
	{
		this.relativeOrbitTime = relativeOrbitTime;
		return this;
	}

	public CelestialBody setDimensionInfo(int dimID, Class<? extends WorldProvider> providerClass)
	{
		return this.setDimensionInfo(dimID, providerClass, true, true);
	}

	public CelestialBody setDimensionInfo(int dimID, Class<? extends WorldProvider> providerClass, boolean autoRegister)
	{
		this.dimensionID = dimID;
		this.providerClass = providerClass;
		this.autoRegisterDimension = autoRegister;
		this.isReachable = true;
		return this;
	}

	public boolean shouldAutoRegister()
	{
		return this.autoRegisterDimension;
	}

	public int getDimensionID()
	{
		return this.dimensionID;
	}

	public Class<? extends WorldProvider> getWorldProvider()
	{
		return this.providerClass;
	}

	public boolean getReachable()
	{
		return this.isReachable;
	}

	public CelestialBody setRingColorRGB(float ringColorR, float ringColorG, float ringColorB)
	{
		this.ringColorR = ringColorR;
		this.ringColorG = ringColorG;
		this.ringColorB = ringColorB;
		return this;
	}

	public float getRingColorR()
	{
		return this.ringColorR;
	}

	public float getRingColorG()
	{
		return this.ringColorG;
	}

	public float getRingColorB()
	{
		return this.ringColorB;
	}

	public ResourceLocation getBodyIcon()
	{
		return this.celestialBodyIcon;
	}

	public CelestialBody setBodyIcon(ResourceLocation planetIcon)
	{
		this.celestialBodyIcon = planetIcon;
		return this;
	}

	public boolean getForceStaticLoad()
	{
		return this.forceStaticLoad;
	}

	public CelestialBody setForceStaticLoad(boolean force)
	{
		this.forceStaticLoad = force;
		return this;
	}

	@Override
	public int hashCode()
	{
		return this.getUnlocalizedName().hashCode();
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof CelestialBody)
		{
			return new EqualsBuilder().append(this.getUnlocalizedName(), ((CelestialBody) other).getUnlocalizedName()).isEquals();
		}

		return false;
	}
}
