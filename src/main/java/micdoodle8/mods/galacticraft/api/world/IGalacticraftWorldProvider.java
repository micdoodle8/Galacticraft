package micdoodle8.mods.galacticraft.api.world;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;

public interface IGalacticraftWorldProvider
{
    /**
     * gets additional gravity to add to players in this dimension. Typical
     * values range from 0.040 to 0.065
     *
     * @return additional gravity for this provider
     */
    public float getGravity();

    /**
     * Determines the rate to spawn meteors in this planet. Lower means MORE
     * meteors.
     * <p/>
     * Typical value would be about 7. Return 0 for no meteors.
     *
     * @return
     */
    public double getMeteorFrequency();

    /**
     * Depending on gravity, different fuel depletion rates will occur in
     * spacecraft (less force required to lift)
     *
     * @return multiplier of fuel usage, relative to the earth. Lower gravity =
     * Lower fuel usage (multiplier less than zero)
     */
    public double getFuelUsageMultiplier();

    /**
     * Whether or not the spaceship tier from {@link IRocketType} can enter this
     * dimension
     *
     * @param tier The tier of the spaceship entering this dimension
     * @return Whether or not the spaceship with given tier can enter this
     * dimension
     */
    public boolean canSpaceshipTierPass(int tier);

    /**
     * Fall damage will be multiplied by this number while on the planet/moon.
     *
     * @return Fall damage multiplier, returning 1 will be equal to earth.
     */
    public float getFallDamageModifier();

    /**
     * Changes volume of sounds on this planet. You should be using higher
     * values for thin atmospheres and high values for dense atmospheres
     *
     * @return Sound reduction divisor. Value of 10 will make sounds ten times
     * more quiet. Value of 0.1 will make sounds 10 times louder. Be
     * careful with the values you choose!
     */
    public float getSoundVolReductionAmount();

    /**
     * Whether or not the atmosphere of this dimension is valid for players to
     * breathe
     *
     * @return True if players can breathe here, False if not.
     */
    public boolean hasBreathableAtmosphere();

    /**
     * Whether or not the atmosphere of this dimension contains the specified gas
     *
     * @return True if the gas is present in the atmosphere, False if not.
     */
    public boolean isGasPresent(IAtmosphericGas gas);

    /**
     * This value will affect player's thermal level, damaging them if it
     * reaches too high or too low.
     *
     * @return Positive integer for hot celestial bodies, negative for cold.
     * Zero for neutral
     */
    public float getThermalLevelModifier();

    /**
     * Amount of wind on this world. Used for flag waving.
     * <p/>
     * Earth has a value of 1.0F, Luna (Moon) has a value of 0.0F.
     *
     * @return Flag movement magnitude. Relative to earth's value of 1.0F
     */
    public float getWindLevel();
    
    /**
     * Factor by which the sun is to be drawn smaller (<1.0) or larger (>1.0) than
     * the sun on the Overworld
     * 
     * @return  factor
     */
    public float getSolarSize();

    /**
     * The celestial body object for this dimension
     *
     * @return The Moon or Planet object for this dimension
     * @see micdoodle8.mods.galacticraft.api.galaxies.Planet
     * @see micdoodle8.mods.galacticraft.api.galaxies.Moon
     */
    public abstract CelestialBody getCelestialBody();
}
