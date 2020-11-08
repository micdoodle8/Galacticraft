package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.dimension.chunk.MoonGenSettings;
import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.storage.WorldInfo;

public class MoonBiomeProviderSettings implements IBiomeProviderSettings
{
    private WorldInfo worldInfo;
    private MoonGenSettings generatorSettings;

    public MoonBiomeProviderSettings(WorldInfo worldInfo)
    {
        this.worldInfo = worldInfo;
    }

    public MoonBiomeProviderSettings setGeneratorSettings(MoonGenSettings settings)
    {
        this.generatorSettings = settings;
        return this;
    }

    public WorldInfo getWorldInfo()
    {
        return this.worldInfo;
    }

    public MoonGenSettings getGeneratorSettings()
    {
        return this.generatorSettings;
    }
}