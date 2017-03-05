package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.DimensionType;

public class GCDimensions
{
    public static DimensionType MOON = DimensionType.register("Moon", "moon", ConfigManagerCore.idDimensionMoon, WorldProviderMoon.class, false);
    public static DimensionType ORBIT = DimensionType.register("Space Station", "orbit", ConfigManagerCore.idDimensionOverworldOrbit, WorldProviderOrbit.class, false);
}
