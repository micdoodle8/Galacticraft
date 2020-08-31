package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BiomeMoon extends BiomeGC
{
    public static final BiomeMoon moonBiome = new BiomeMoon();
    //    public static final Biome moonFlat = new BiomeFlatMoon(new BiomeProperties("Moon").setBaseHeight(1.5F).setHeightVariation(0.4F).setRainfall(0.0F));
    public static final BlockState TURF = GCBlocks.moonTurf.getDefaultState();
    //    public static final BlockState STONE = GCBlocks.moonStone.getDefaultState();
    public static final BlockState DIRT = GCBlocks.moonDirt.getDefaultState();
    public static final SurfaceBuilderConfig MOON_CONFIG = new SurfaceBuilderConfig(TURF, DIRT, DIRT);

    BiomeMoon()
    {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, MOON_CONFIG).precipitation(Biome.RainType.NONE).category(Category.NONE).depth(1.5F).scale(0.4F).temperature(0.0F).downfall(0.0F).waterColor(10724259).waterFogColor(9868950).parent(null));
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
