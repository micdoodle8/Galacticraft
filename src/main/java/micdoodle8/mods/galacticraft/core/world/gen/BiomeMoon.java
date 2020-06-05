package micdoodle8.mods.galacticraft.core.world.gen;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.dimension.chunk.MoonChunkGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeMoon extends BiomeGenBaseGC
{
    public static final Biome moonFlat = new BiomeFlatMoon(new BiomeProperties("Moon").setBaseHeight(1.5F).setHeightVariation(0.4F).setRainfall(0.0F));

    BiomeMoon(BiomeProperties properties)
    {
        super(properties, true);
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

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        this.fillerBlock = MoonChunkGenerator.BLOCK_LOWER;
        this.topBlock = MoonChunkGenerator.BLOCK_TOP;
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
