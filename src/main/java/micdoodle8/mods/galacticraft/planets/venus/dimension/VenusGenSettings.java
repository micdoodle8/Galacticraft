package micdoodle8.mods.galacticraft.planets.venus.dimension;

import net.minecraft.world.gen.GenerationSettings;

public class VenusGenSettings extends GenerationSettings
{
    public int getBiomeSize()
    {
        return 4;
    }

    public int getRiverSize()
    {
        return 4;
    }

    public int getBiomeId()
    {
        return -1;
    }

    @Override
    public int getBedrockFloorHeight()
    {
        return 0;
    }

    public int getHomeTreeDistance()
    {
        return 20;
    }

    public int getHomeTreeSeparation()
    {
        return 4;
    }
}