package micdoodle8.mods.galacticraft.planets.venus.dimension.biome;

import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

public enum GenLayerVenusBiomes implements IAreaTransformer0
{
    INSTANCE;

    private static final Biome[] biomes = new Biome[] { BiomeVenus.venusFlat, BiomeVenus.venusValley, BiomeVenus.venusMountain };

    @Override
    public int apply(INoiseRandom noise, int x, int y)
    {
        return Registry.BIOME.getId(biomes[noise.random(biomes.length)]);
    }
}
