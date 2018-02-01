package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import net.minecraft.world.biome.Biome;

public class BiomeMars extends BiomeGenBaseGC
{
    public static final Biome marsFlat = new BiomeGenFlatMars(new BiomeProperties("Mars Flat").setBaseHeight(2.5F).setHeightVariation(0.4F).setRainfall(0.0F).setRainDisabled());

    @SuppressWarnings("unchecked")
    BiomeMars(BiomeProperties properties)
    {
        super(properties, true);
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}
