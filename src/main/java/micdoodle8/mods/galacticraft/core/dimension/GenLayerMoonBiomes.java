package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonFlat;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonHills;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonSuperFlat;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

public enum GenLayerMoonBiomes implements IAreaTransformer0
{
    INSTANCE;

    private static final Biome[] biomes = new Biome[] { BiomeMoonHills.moonBiomeHills, BiomeMoonFlat.moonBiomeFlat, BiomeMoonSuperFlat.moonBiomeSuperFlat };

    @Override
    public int apply(INoiseRandom noise, int x, int y)
    {
        return Registry.BIOME.getId(biomes[noise.random(biomes.length)]);
    }
}
