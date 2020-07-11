package micdoodle8.mods.galacticraft.planets.venus.dimension;

import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.storage.WorldInfo;

public class VenusBiomeProviderSettings implements IBiomeProviderSettings
{
    private WorldInfo worldInfo;
    private VenusGenSettings generatorSettings;

    public VenusBiomeProviderSettings(WorldInfo worldInfo)
    {
        this.worldInfo = worldInfo;
    }

    public VenusBiomeProviderSettings setGeneratorSettings(VenusGenSettings settings)
    {
        this.generatorSettings = settings;
        return this;
    }

    public WorldInfo getWorldInfo()
    {
        return this.worldInfo;
    }

    public VenusGenSettings getGeneratorSettings()
    {
        return this.generatorSettings;
    }
}