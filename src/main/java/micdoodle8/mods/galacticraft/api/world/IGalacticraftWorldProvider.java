package micdoodle8.mods.galacticraft.api.world;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IGalacticraftWorldProvider
{
    /**
     * gets additional gravity to add to players in this dimension. Typical
     * values range from 0.040 to 0.065
     *
     * @return additional gravity for this provider
     */
    float getGravity();

    
    /**
     * Gets custom arrow gravity, overriding the vanilla gravity for arrows
     * 
     * @return added y-motion per tick for arrows 
     */
    float getArrowGravity();
    
    /**
     * Determines the rate to spawn meteors in this planet. Lower means MORE
     * meteors.
     * <p/>
     * Typical value would be about 7. Return 0 for no meteors.
     *
     * @return
     */
    double getMeteorFrequency();

    /**
     * Depending on gravity, different fuel depletion rates will occur in
     * spacecraft (less force required to lift)
     *
     * @return multiplier of fuel usage, relative to the earth. Lower gravity =
     * Lower fuel usage (multiplier less than zero)
     */
    double getFuelUsageMultiplier();

    /**
     * Whether or not the spaceship tier from {@link IRocketType} can enter this
     * dimension
     *
     * @param tier The tier of the spaceship entering this dimension
     * @return Whether or not the spaceship with given tier can enter this
     * dimension
     */
    boolean canSpaceshipTierPass(int tier);

    /**
     * Fall damage will be multiplied by this number while on the planet/moon.
     *
     * @return Fall damage multiplier, returning 1 will be equal to earth.
     */
    float getFallDamageModifier();

    /**
     * 
     * @return true if this world has no type of atmosphere at all - e.g. the Moon
     */
    boolean hasNoAtmosphere();
    
    /**
     * Changes volume of sounds on this planet. You should be using higher
     * values for thin atmospheres and high values for dense atmospheres
     *
     * @return Sound reduction divisor. Value of 10 will make sounds ten times
     * more quiet. Value of 0.1 will make sounds 10 times louder. Be
     * careful with the values you choose!
     */
    float getSoundVolReductionAmount();

    /**
     * Whether or not the atmosphere of this dimension is valid for players to
     * breathe
     *
     * @return True if players can breathe here, False if not.
     */
    boolean hasBreathableAtmosphere();
   
    /**
     * If false (the default) then Nether Portals will have no function on this world.
     * Nether Portals can still be constructed, if the player can make fire, they just
     * won't do anything.
     * 
     * @return True if Nether Portals should work like on the Overworld.
     */
    boolean netherPortalsOperational();

    /**
     * Whether or not the atmosphere of this dimension contains the specified gas
     *
     * @return True if the gas is present in the atmosphere, False if not.
     */
    boolean isGasPresent(EnumAtmosphericGas gas);

    /**
     * This value will affect player's thermal level, damaging them if it
     * reaches too high or too low.
     *
     * @return Positive integer for hot celestial bodies, negative for cold.
     * Zero for neutral
     */
    float getThermalLevelModifier();

    /**
     * Amount of wind on this world. Used for flag waving.
     * <p/>
     * Earth has a value of 1.0F, Luna (Moon) has a value of 0.0F.
     *
     * @return Flag movement magnitude. Relative to earth's value of 1.0F
     */
    float getWindLevel();
    
    /**
     * Factor by which the sun is to be drawn smaller (<1.0) or larger (>1.0) than
     * the sun on the Overworld
     * 
     * @return  factor
     */
    float getSolarSize();

    /**
     * The celestial body object for this dimension
     *
     * @return The Moon or Planet object for this dimension
     * @see micdoodle8.mods.galacticraft.api.galaxies.Planet
     * @see micdoodle8.mods.galacticraft.api.galaxies.Moon
     */
    abstract CelestialBody getCelestialBody();

    /**
     * Whether rain and snow should be disabled on this planet
     *
     * @return true if precipitation should be disabled. False otherwise.
     */
    boolean shouldDisablePrecipitation();

    /**
     * Whether or not player's armor should be corroded in this world
     *
     * @return true if armor should be corroded, false if not
     */
    boolean shouldCorrodeArmor();
    
    /**
     * The size (in blocks) of the average spacing between dungeons
     * For example, on the Moon it's 704 blocks, meaning one dungeon in each (704 x 704) square in the (x, z) plane
     * <p/>
     * If your world has no dungeons you can safely return 0 here.
     */
    int getDungeonSpacing();
    
    /**
     * The ChestGenHooks identifier of the dungeon chests to generate in this world
     */
    ResourceLocation getDungeonChestType();

    List<Block> getSurfaceBlocks();
}
