package micdoodle8.mods.galacticraft.planets.mars.dimension.biome;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BiomeMars extends BiomeGC
{
    public static final BiomeMars marsFlat = new BiomeMars();
    //    public static final Biome moonFlat = new BiomeFlatMoon(new BiomeProperties("Moon").setBaseHeight(1.5F).setHeightVariation(0.4F).setRainfall(0.0F));
    public static final BlockState SURFACE = MarsBlocks.rockSurface.getDefaultState();
    //    public static final BlockState STONE = GCBlocks.moonStone.getDefaultState();
    public static final BlockState MIDDLE = MarsBlocks.rockMiddle.getDefaultState();
    public static final SurfaceBuilderConfig MARS_CONFIG = new SurfaceBuilderConfig(SURFACE, MIDDLE, MIDDLE);

    BiomeMars()
    {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, MARS_CONFIG).precipitation(Biome.RainType.NONE).category(Category.NONE).depth(2.5F).scale(0.4F).temperature(0.0F).downfall(0.0F).waterColor(12208699).waterFogColor(9845808).parent(null));
    }

//    @Override
//    public BiomeDecorator createBiomeDecorator()
//    {
//        return getModdedBiomeDecorator(new BiomeDecoratorMoon());
//    }

    @Override
    public float getSpawningChance()
    {
        return 0.1F;
    }

//    @Override
//    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
//    {
//        this.fillerBlock = MoonChunkGenerator.BLOCK_LOWER;
//        this.topBlock = MoonChunkGenerator.BLOCK_TOP;
//        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
//    }
}

