package micdoodle8.mods.galacticraft.API;

/**
 * Used to change the solar multiplier of certain world providers
 * 
 * CONFIRMED WORKING
 * 
 * If you have a solar feature in your mod, check whether the world's provider
 * inherits this class and multiply the solar generation by the solar multiplier
 * double
 */
public interface ISolarLevel
{
    public double getSolarEnergyMultiplier();
}
