//package micdoodle8.mods.galacticraft.api.prefab.world.gen;
//
//import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
//import micdoodle8.mods.galacticraft.api.world.BiomeGC;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.EntityClassification;
//import net.minecraft.util.SharedSeedRandom;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.registry.Registry;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.world.IWorld;
//import net.minecraft.world.IWorldReader;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.chunk.IChunk;
//import net.minecraft.world.gen.ChunkGenerator;
//import net.minecraft.world.gen.GenerationSettings;
//import net.minecraft.world.gen.GenerationStage;
//import net.minecraft.world.gen.carver.ConfiguredCarver;
//import net.minecraft.world.gen.carver.ICarverConfig;
//import net.minecraft.world.gen.feature.ConfiguredFeature;
//import net.minecraft.world.gen.feature.IFeatureConfig;
//import net.minecraft.world.gen.feature.structure.Structure;
//import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
//import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.common.BiomeDictionary;
//import net.minecraftforge.registries.IForgeRegistry;
//
//import javax.annotation.Nullable;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//
//public class BiomeAdaptive extends BiomeGC
//{
//    public static BiomeAdaptive biomeDefault;
//    public static List<BiomeAdaptive> biomeList = new LinkedList<>();
//    private Biome biomeTrue;
//    private final int index;
//    private boolean loggedConflict;
//
//    public BiomeAdaptive(int i, Biome biomeInitial)
//    {
////        if (biomeBuilder.surfaceBuilder != null && biomeBuilder.precipitation != null && biomeBuilder.category != null &&
////        biomeBuilder.depth != null && biomeBuilder.scale != null && biomeBuilder.temperature != null &&
////        biomeBuilder.downfall != null && biomeBuilder.waterColor != null && biomeBuilder.waterFogColor != null) {
//
//            super((new Builder().surfaceBuilder(biomeInitial.getSurfaceBuilder())
//                    .precipitation(biomeInitial.getPrecipitation())
//                    .category(biomeInitial.getCategory())
//                    .depth(biomeInitial.getDepth())
//                    .scale(biomeInitial.getScale())
//                    .temperature(biomeInitial.getDefaultTemperature())
//                    .downfall(biomeInitial.getDownfall())
//                    .waterColor(biomeInitial.getWaterColor())
//                    .waterFogColor(biomeInitial.getWaterFogColor())));
////        super(new BiomeProperties().setRainfall(0.0F));
//        this.index = i;
//        this.biomeTrue = biomeInitial;
////        this.decorator = this.createBiomeDecorator();
//        if (index == 0)
//        {
//            biomeDefault = this;
//        }
//    }
//
//    @Override
//    public void registerTypes(Biome b)
//    {
//        if (this.biomeTrue instanceof BiomeGC)
//        {
//            ((BiomeGC) this.biomeTrue).registerTypes(this);
//        }
//        else
//        {
//            BiomeDictionary.addTypes(this, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.SPOOKY);
//        }
//    }
//
//    public static BiomeGC register(IForgeRegistry<Biome> registry, int index, BiomeGC biome)
//    {
//        if (index >= biomeList.size())
//        {
//            BiomeAdaptive newAdaptive = new BiomeAdaptive(index, biome);
////            Registry.register(Registry.BIOME, index, , newAdaptive);
//            registry.register(newAdaptive.setRegistryName("outer_space" + (index == 0 ? "" : "_" + index)));
//            biomeList.add(newAdaptive);
//            return newAdaptive;
//        }
//        return biomeList.get(index);
//    }
//
//    public static BiomeGC getDefaultBiomeFor(CelestialBody body)
//    {
//        return body.biomesToAdapt[0];
//    }
//
//    public static void setBody(CelestialBody body)
//    {
//        biomeDefault.setBodyInstance(body);
//    }
//
//    public boolean isInstance(Class<?> clazz)
//    {
//        return clazz.isInstance(this.biomeTrue);
//    }
//
//    /**
//     * Be sure to call this from your BiomeProvider in:
//     * <br>-- getBiome(BlockPos pos, Biome defaultBiome)
//     * <br>-- getBiomesForGeneration(Biome[] biomes, int x, int z, int length, int width)
//     * <br>-- getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth)
//     * <br>-- getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
//     * <br>also from your WorldProvider in:
//     * <br>-- getBiomeProviderClass()
//     * <p>
//     * See BiomeProviderVenus for an example.
//     */
//    public static void setBodyMultiBiome(CelestialBody body)
//    {
//        for (BiomeAdaptive b : biomeList)
//        {
//            if (b.setBodyInstance(body))
//            {
//                break;
//            }
//        }
//    }
//
//    /**
//     * @param body
//     * @return true if end of list
//     */
//    protected boolean setBodyInstance(CelestialBody body)
//    {
//        this.biomeTrue = body.biomesToAdapt[this.index];
////        this.fillerBlock = this.biomeTrue.fillerBlock;
////        this.topBlock = this.biomeTrue.topBlock;
////        this.decorator = this.biomeTrue.decorator; TODO ?
//        return this.index == body.biomesToAdapt.length - 1;
//    }
//
//    public static List<Biome> getBiomesListFor(CelestialBody body)
//    {
//        List<Biome> result = new LinkedList<>();
//        for (BiomeAdaptive b : biomeList)
//        {
//            if (result.size() >= body.biomesToAdapt.length)
//            {
//                break;
//            }
//
//            result.add(b);
//        }
//        return result;
//    }
//
//
//    // ===========================================================================
//    // ===============Pass through all Biome methods to the true biome============
//    // ===========================================================================
//
////    @Override
////    public BiomeDecorator createBiomeDecorator()
////    {
////        return biomeTrue == null ? null : biomeTrue.createBiomeDecorator();
////    }
////
////    @Override
////    public boolean isMutation()
////    {
////        return biomeTrue.isMutation();
////    }
////
////    @Override
////    public AbstractTreeFeature getRandomTreeFeature(Random rand)
////    {
////        return biomeTrue.getRandomTreeFeature(rand);
////    }
////
////    @Override
////    public Feature getRandomWorldGenForGrass(Random rand)
////    {
////        return biomeTrue.getRandomWorldGenForGrass(rand);
////    }
////
////    @Override
////    public FlowerBlock.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
////    {
////        return biomeTrue.pickRandomFlower(rand, pos);
////    }
////
////    @Override
////    @OnlyIn(Dist.CLIENT)
////    public int getSkyColorByTemp(float currentTemperature)
////    {
////        return biomeTrue.getSkyColorByTemp(currentTemperature);
////    }
////
////    @Override
////    public List<Biome.SpawnListEntry> getSpawnableList(EntityClassification creatureType)
////    {
////        return biomeTrue.getSpawnableList(creatureType);
////    }
////
////    @Override
////    public boolean getEnableSnow()
////    {
////        return biomeTrue.getEnableSnow();
////    }
////
////    @Override
////    public boolean canRain()
////    {
////        return biomeTrue.canRain();
////    }
////
////    @Override
////    public boolean isHighHumidity()
////    {
////        return biomeTrue.isHighHumidity();
////    }
////
////    @Override
////    public float getSpawningChance()
////    {
////        return biomeTrue.getSpawningChance();
////    }
////
////    @Override
////    public float getTemperature(BlockPos pos)
////    {
////        return biomeTrue.getTemperature(pos);
////    }
////
////    @Override
////    public void decorate(World worldIn, Random rand, BlockPos pos)
////    {
////        if (worldIn.dimension instanceof IGalacticraftWorldProvider)
////        {
////            this.setBodyInstance(((IGalacticraftWorldProvider)worldIn.dimension).getCelestialBody());
////        }
////        else
////        {
////            reportBiomeIDconflict();
////        }
////        biomeTrue.decorate(worldIn, rand, pos);
////    }
////
////    @Override
////    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
////    {
////        if (worldIn.dimension instanceof IGalacticraftWorldProvider)
////        {
////            this.setBodyInstance(((IGalacticraftWorldProvider)worldIn.dimension).getCelestialBody());
////        }
////        else
////        {
////            reportBiomeIDconflict();
////        }
////        biomeTrue.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
////    }
////
////    @Override
////    @OnlyIn(Dist.CLIENT)
////    public int getGrassColorAtPos(BlockPos pos)
////    {
////        return biomeTrue.getGrassColorAtPos(pos);
////    }
////
////    @Override
////    public final void generateBiomeTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
////    {
////        if (worldIn.dimension instanceof IGalacticraftWorldProvider)
////        {
////            this.setBodyInstance(((IGalacticraftWorldProvider)worldIn.dimension).getCelestialBody());
////        }
////        else
////        {
////            reportBiomeIDconflict();
////        }
////        biomeTrue.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
////    }
////
////    @Override
////    @OnlyIn(Dist.CLIENT)
////    public int getFoliageColorAtPos(BlockPos pos)
////    {
////        return biomeTrue.getFoliageColorAtPos(pos);
////    }
////
////    @Override
////    public Class <? extends Biome > getBiomeClass()
////    {
////        return biomeTrue.getBiomeClass();
////    }
////
////    @Override
////    public Biome.TempCategory getTempCategory()
////    {
////        return biomeTrue.getTempCategory();
////    }
////
////    @Override
////    public boolean ignorePlayerSpawnSuitability()
////    {
////        return biomeTrue.ignorePlayerSpawnSuitability();
////    }
////
////    @Override
////    public float getDepth()
////    {
////        return biomeTrue == null ? 0.0F : biomeTrue.getDepth();
////    }
////
////    @Override
////    public float getRainfall()
////    {
////        return biomeTrue.getRainfall();
////    }
////
////    @Override
////    @OnlyIn(Dist.CLIENT)
////    public String getBiomeName()
////    {
////        return biomeTrue.getBiomeName();
////    }
////
////    @Override
////    public float getHeightVariation()
////    {
////        return biomeTrue.getHeightVariation();
////    }
////
////    @Override
////    public float getDefaultTemperature()
////    {
////        return biomeTrue.getDefaultTemperature();
////    }
////
////    @Override
////    @OnlyIn(Dist.CLIENT)
////    public int getWaterColor()
////    {
////        return biomeTrue.getWaterColor();
////    }
////
////    @Override
////    public boolean isSnowyBiome()
////    {
////        return biomeTrue.isSnowyBiome();
////    }
////
////    @Override
////    public BiomeDecorator getModdedBiomeDecorator(BiomeDecorator original)
////    {
////        return biomeTrue.getModdedBiomeDecorator(original);
////    }
////
////    @Override
////    public int getWaterColorMultiplier()
////    {
////        return biomeTrue.getWaterColorMultiplier();
////    }
////
////    @Override
////    public int getModdedBiomeGrassColor(int original)
////    {
////        return biomeTrue.getModdedBiomeGrassColor(original);
////    }
////
////    @Override
////    public int getModdedBiomeFoliageColor(int original)
////    {
////        return biomeTrue.getModdedBiomeFoliageColor(original);
////    }
////
////    @Override
////    public void addDefaultFlowers()
////    {
////        if (biomeTrue != null)
////            biomeTrue.addDefaultFlowers();
////    }
////
////    @Override
////    public void addFlower(BlockState state, int weight)
////    {
////        biomeTrue.addFlower(state, weight);
////    }
////
////    @Override
////    public void plantFlower(World world, Random rand, BlockPos pos)
////    {
////        if (world.getDimension() instanceof IGalacticraftWorldProvider)
////        {
////            this.setBodyInstance(((IGalacticraftWorldProvider)world.getDimension()).getCelestialBody());
////        }
////        else
////        {
////            reportBiomeIDconflict();
////        }
////        biomeTrue.plantFlower(world, rand, pos);
////    }
//
//    @Override
//    public boolean isMutation()
//    {
//        return biomeTrue.isMutation();
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public int getSkyColor()
//    {
//        return biomeTrue.getSkyColor();
//    }
//
//    /**
//     * Returns the correspondent list of the EnumCreatureType informed.
//     */
//    @Override
//    public List<Biome.SpawnListEntry> getSpawns(EntityClassification creatureType)
//    {
//        return biomeTrue.getSpawns(creatureType);
//    }
//
//    @Override
//    public Biome.RainType getPrecipitation()
//    {
//        return biomeTrue.getPrecipitation();
//    }
//
//    /**
//     * Checks to see if the rainfall level of the biome is extremely high
//     */
//    @Override
//    public boolean isHighHumidity()
//    {
//        return biomeTrue.isHighHumidity();
//    }
//
//    /**
//     * returns the chance a creature has to spawn.
//     */
//    @Override
//    public float getSpawningChance()
//    {
//        return biomeTrue.getSpawningChance();
//    }
//
//    /**
//     * Gets the current temperature at the given location, based off of the default for this biome, the elevation of the
//     * position, and {@linkplain #TEMPERATURE_NOISE} some random perlin noise.
//     */
//    @Override
//    public float getTemperatureRaw(BlockPos pos)
//    {
//        return biomeTrue.getTemperatureRaw(pos);
//    }
//
//    @Override
//    public boolean doesWaterFreeze(IWorldReader worldIn, BlockPos pos)
//    {
//        return biomeTrue.doesWaterFreeze(worldIn, pos);
//    }
//
//    @Override
//    public boolean doesWaterFreeze(IWorldReader worldIn, BlockPos water, boolean mustBeAtEdge)
//    {
//        return biomeTrue.doesWaterFreeze(worldIn, water, mustBeAtEdge);
//    }
//
//    @Override
//    public boolean doesSnowGenerate(IWorldReader worldIn, BlockPos pos)
//    {
//        return biomeTrue.doesSnowGenerate(worldIn, pos);
//    }
//
//    @Override
//    public void addFeature(GenerationStage.Decoration decorationStage, ConfiguredFeature<?, ?> featureIn)
//    {
//        biomeTrue.addFeature(decorationStage, featureIn);
//    }
//
//    @Override
//    public <C extends IFeatureConfig> void addStructure(ConfiguredFeature<C, ? extends Structure<C>> structureIn)
//    {
//        biomeTrue.addStructure(structureIn);
//    }
//
//    @Override
//    public <C extends ICarverConfig> void addCarver(GenerationStage.Carving stage, ConfiguredCarver<C> carver)
//    {
//        biomeTrue.addCarver(stage, carver);
//    }
//
//    @Override
//    public List<ConfiguredCarver<?>> getCarvers(GenerationStage.Carving stage)
//    {
//        return biomeTrue.getCarvers(stage);
//    }
//
//    @Override
//    public <C extends IFeatureConfig> boolean hasStructure(Structure<C> structureIn)
//    {
//        return biomeTrue.hasStructure(structureIn);
//    }
//
//    @Nullable
//    @Override
//    public <C extends IFeatureConfig> C getStructureConfig(Structure<C> structureIn)
//    {
//        return biomeTrue.getStructureConfig(structureIn);
//    }
//
//    @Override
//    public List<ConfiguredFeature<?, ?>> getFlowers()
//    {
//        return biomeTrue.getFlowers();
//    }
//
//    @Override
//    public List<ConfiguredFeature<?, ?>> getFeatures(GenerationStage.Decoration decorationStage)
//    {
//        return biomeTrue.getFeatures(decorationStage);
//    }
//
//    @Override
//    public void decorate(GenerationStage.Decoration stage, ChunkGenerator<? extends GenerationSettings> chunkGenerator, IWorld worldIn, long seed, SharedSeedRandom random, BlockPos pos)
//    {
//        biomeTrue.decorate(stage, chunkGenerator, worldIn, seed, random, pos);
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public int getGrassColor(double posX, double posZ)
//    {
//        return biomeTrue.getGrassColor(posX, posZ);
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public int getFoliageColor()
//    {
//        return biomeTrue.getFoliageColor();
//    }
//
//    @Override
//    public void buildSurface(Random random, IChunk chunkIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed)
//    {
//        biomeTrue.buildSurface(random, chunkIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed);
//    }
//
//    @Override
//    public Biome.TempCategory getTempCategory()
//    {
//        return biomeTrue.getTempCategory();
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public ITextComponent getDisplayName()
//    {
//        return biomeTrue.getDisplayName();
//    }
//
//    @Override
//    public String getTranslationKey()
//    {
//        return biomeTrue.getTranslationKey();
//    }
//
//    @Override
//    public ConfiguredSurfaceBuilder<?> getSurfaceBuilder()
//    {
//        return biomeTrue.getSurfaceBuilder();
//    }
//
//    @Override
//    public ISurfaceBuilderConfig getSurfaceBuilderConfig()
//    {
//        return biomeTrue.getSurfaceBuilderConfig();
//    }
//
//    @Override
//    @Nullable
//    public String getParent()
//    {
//        return biomeTrue.getParent();
//    }
//
//    @Override
//    public Biome getRiver()
//    {
//        return biomeTrue.getRiver();
//    }
//}
