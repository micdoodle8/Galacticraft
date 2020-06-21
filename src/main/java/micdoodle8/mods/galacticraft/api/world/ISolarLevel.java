package micdoodle8.mods.galacticraft.api.world;

/**
 * Used to change the solar multiplier of certain world providers
 * <p/>
 * If you have a solar feature in your mod, check whether the world's dimension
 * inherits this class and multiply the solar generation by the solar multiplier
 * double
 * <p/>
 * for example:
 * <p/>
 * if (worldObj.dimension instanceof ISolarLevel) solarStrength *= ((ISolarLevel)
 * worldObj.dimension).getSolarEnergyMultiplier();
 */
public interface ISolarLevel
{
    double getSolarEnergyMultiplier();
}
