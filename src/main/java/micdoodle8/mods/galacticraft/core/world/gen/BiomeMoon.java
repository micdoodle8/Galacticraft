package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.world.biome.Biome;
import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.world.biome.BiomeDecorator;

public class BiomeMoon extends BiomeGenBaseGC
{
    public static final Biome moonFlat = new BiomeFlatMoon(new BiomeProperties("Moon").setBaseHeight(1.5F).setHeightVariation(0.4F).setRainfall(0.0F));

    BiomeMoon(BiomeProperties properties)
    {
        super(properties);
        this.setRegistryName(Constants.TEXTURE_PREFIX + this.getBiomeName());
    }
    
    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return getModdedBiomeDecorator(new BiomeDecoratorMoon());
    }

    @Override
    public float getSpawningChance()
    {
        return 0.1F;
    }
}
