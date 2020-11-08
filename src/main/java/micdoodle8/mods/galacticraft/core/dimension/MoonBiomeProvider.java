package micdoodle8.mods.galacticraft.core.dimension;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import micdoodle8.mods.galacticraft.core.dimension.chunk.MoonGenSettings;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonFlat;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonHills;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonSuperFlat;
import net.minecraft.block.BlockState;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.storage.WorldInfo;

import java.util.Set;
import java.util.function.LongFunction;

public class MoonBiomeProvider extends BiomeProvider
{
    private final Layer noiseLayer;
    private final Layer blockLayer;

    protected final Set<BlockState> surfaceBlocks = Sets.newHashSet();

    public MoonBiomeProvider(final MoonBiomeProviderSettings settings)
    {
        super(ImmutableSet.of(
                BiomeMoonFlat.moonBiomeFlat,
                BiomeMoonHills.moonBiomeHills,
                BiomeMoonSuperFlat.moonBiomeSuperFlat
        ));
        final WorldInfo info = settings.getWorldInfo();
        final MoonGenSettings generatorSettings = settings.getGeneratorSettings();
        final Layer[] layers = buildMoonProcedure(info.getSeed(), info.getGenerator(), generatorSettings);
        noiseLayer = layers[0];
        blockLayer = layers[1];
    }

    private static Layer[] buildMoonProcedure(long seed, WorldType type, MoonGenSettings settings)
    {
        final ImmutableList<IAreaFactory<LazyArea>> immutablelist = buildMoonProcedure(type, settings, procedure -> new LazyAreaLayerContext(25, seed, procedure));
        final Layer noiseLayer = new Layer(immutablelist.get(0));
        final Layer blockLayer = new Layer(immutablelist.get(1));
        return new Layer[]{noiseLayer, blockLayer};
    }

    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> buildMoonProcedure(final WorldType type, final MoonGenSettings settings, final LongFunction<C> context)
    {
        IExtendedNoiseRandom<T> r = context.apply(1);
        IAreaFactory<T> mainLayer = GenLayerMoonBiomes.INSTANCE.apply(r);
        IAreaFactory<T> zoomLayer = ZoomLayer.NORMAL.apply(context.apply(1000L), mainLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(1001L), zoomLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(1002L), zoomLayer);
        zoomLayer = ZoomLayer.NORMAL.apply(context.apply(1003L), zoomLayer);

        IAreaFactory<T> blockLayer = GCVoronoiZoomLayer.INSTANCE.apply(context.apply(10), zoomLayer);

        return ImmutableList.of(zoomLayer, blockLayer);
    }

//    @Override
//    public Biome getBiome(int x, int z)
//    {
//        return blockLayer.func_215738_a(x, z);
//    }
//
//    // get noise biome
//    @Override
//    public Biome func_222366_b(int x, int z)
//    {
//        return noiseLayer.func_215738_a(x, z);
//    }
//
//    @Override
//    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag)
//    {
//        return blockLayer.generateBiomes(x, z, width, length);
//    }
//
//    @Override
//    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength)
//    {
//        int x0 = centerX - sideLength >> 2;
//        int z0 = centerZ - sideLength >> 2;
//        int x1 = centerX + sideLength >> 2;
//        int z1 = centerZ + sideLength >> 2;
//        int width = x1 - x0 + 1;
//        int height = z1 - z0 + 1;
//        Set<Biome> lvt_10_1_ = Sets.newHashSet();
//        Collections.addAll(lvt_10_1_, this.noiseLayer.generateBiomes(x0, z0, width, height));
//        return lvt_10_1_;
//    }
//
//    /**
//     * Checks if an area around a block contains only the specified biomes.
//     * To ensure NO other biomes, add a margin of at least four blocks to the radius
//     */
//    @Nullable
//    @Override
//    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> allowedBiomes, Random random)
//    {
//        final int x0 = (x - range) >> 2;
//        final int z0 = (z - range) >> 2;
//        final int x1 = (x + range) >> 2;
//        final int z1 = (z + range) >> 2;
//
//        final int w = x1 - x0 + 1;
//        final int h = z1 - z0 + 1;
//        final Biome[] biomes = noiseLayer.generateBiomes(x0, z0, w, h);
//        BlockPos result = null;
//        int found = 0;
//        for (int i = 0; i < w * h; i++)
//        {
//            final int xx = (x0 + i % w) << 2;
//            final int zz = (z0 + i / w) << 2;
//            if (allowedBiomes.contains(biomes[i]))
//            {
//                if (result == null || random.nextInt(found + 1) == 0)
//                {
//                    result = new BlockPos(xx, 0, zz);
//                }
//                found++;
//            }
//        }
//
//        return result;
//    }

    // really is "can generate structure?"
    @Override
    public boolean hasStructure(Structure<?> structure)
    {
        return this.hasStructureCache.computeIfAbsent(structure, (structure1) ->
        {
            for (Biome biome : biomes)
            {
                if (biome.hasStructure(structure1))
                {
                    return true;
                }
            }

            return false;
        });
    }

    @Override
    public Set<BlockState> getSurfaceBlocks()
    {
        if (surfaceBlocks.isEmpty())
        {
            for (Biome biome : biomes)
            {
                surfaceBlocks.add(biome.getSurfaceBuilderConfig().getTop());
            }
        }
        return surfaceBlocks;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z)
    {
        return noiseLayer.func_215738_a(x, z);
    }
}