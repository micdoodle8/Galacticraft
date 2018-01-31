package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;

public class BiomeAdaptive extends BiomeGenBaseGC
{
    public static final BiomeAdaptive biomeDefault = new BiomeAdaptive();
    public static List<BiomeAdaptive> biomeList = new LinkedList<>(); 
    private Map<CelestialBody, BiomeGenBaseGC> map = new HashMap<>();
    private Biome biomeTrue;
    
    static
    {
        biomeList.add(biomeDefault);
    }
    
    public BiomeAdaptive()
    {
        super(new BiomeProperties("Outer Space").setRainfall(0.0F));
    }

    public BiomeAdaptive(int i)
    {
        super(new BiomeProperties("Outer Space " + i).setRainfall(0.0F));
    }

    @Override
    public void registerTypes()
    {
        BiomeDictionary.addTypes(this, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.SPOOKY);
    }

    public static void register(CelestialBody body, BiomeGenBaseGC biome)
    {
        assert(body != null);

        for (BiomeAdaptive b : biomeList)
        {
            if (b.registerAndTest(body, biome))
                return;
        }
        
        BiomeAdaptive biomeNew = new BiomeAdaptive(biomeList.size()); 
        biomeList.add(biomeNew);
        biomeNew.registerAndTest(body, biome);
    }
    
    /**
     * @param body
     * @param biome
     * @return false if this biome is already registered, true if success
     */
    public boolean registerAndTest(CelestialBody body, BiomeGenBaseGC biome)
    {
        if (map.containsKey(body))
            return false;
        map.put(body, biome);
        return true;
    }
    
    public static BiomeGenBaseGC getDefaultBiomeFor(CelestialBody body)
    {
        return biomeDefault.map.get(body);
    }

    public static void setBody(CelestialBody body)
    {
        biomeDefault.setBodyInstance(body);
    }
    
    /**
     * Be sure to call this from your BiomeProvider in:
     * <br>-- getBiome(BlockPos pos, Biome defaultBiome)
     * <br>-- getBiomesForGeneration(Biome[] biomes, int x, int z, int length, int width)
     * <br>-- getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth)
     * <br>-- getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
     * <br>also from your WorldProvider in:
     * <br>-- getBiomeProviderClass()
     * <p>
     * See BiomeProviderVenus for an example.
     */
    public static void setBodyMultiBiome(CelestialBody body)
    {
        for (BiomeAdaptive b : biomeList)
        {
            if (b.setBodyInstance(body))
                break;
        }
    }
    
    /**
     * @param body
     * @return true if none found
     */
    protected boolean setBodyInstance(CelestialBody body)
    {
        biomeTrue = map.get(body);
        return biomeTrue == null;
    }

    public static List<Biome> getBiomesListFor(CelestialBody body)
    {
        List<Biome> result = new LinkedList<>();
        for (BiomeAdaptive b : biomeList)
        {
            if (b.map.get(body) != null)
            {
                result.add(b);
            }
            else
            {
                break;
            }
        }
        return result;
    }
   

    // ===========================================================================
    // ===============Pass through all Biome methods to the true biome============
    // ===========================================================================
       
    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return biomeTrue == null ? null : biomeTrue.createBiomeDecorator();
    }

    @Override
    public boolean isMutation()
    {
        return biomeTrue.isMutation();
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand)
    {
        return biomeTrue.getRandomTreeFeature(rand);
    }

    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random rand)
    {
        return biomeTrue.getRandomWorldGenForGrass(rand);
    }

    @Override
    public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
    {
        return biomeTrue.pickRandomFlower(rand, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature)
    {
        return biomeTrue.getSkyColorByTemp(currentTemperature);
    }

    @Override
    public List<Biome.SpawnListEntry> getSpawnableList(EnumCreatureType creatureType)
    {
        return biomeTrue.getSpawnableList(creatureType);
    }

    @Override
    public boolean getEnableSnow()
    {
        return biomeTrue.getEnableSnow();
    }

    @Override
    public boolean canRain()
    {
        return biomeTrue.canRain();
    }

    @Override
    public boolean isHighHumidity()
    {
        return biomeTrue.isHighHumidity();
    }

    @Override
    public float getSpawningChance()
    {
        return biomeTrue.getSpawningChance();
    }

    @Override
    public float getTemperature(BlockPos pos)
    {
        return biomeTrue.getTemperature(pos);
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        if (worldIn.provider instanceof IGalacticraftWorldProvider)
        {
            biomeTrue = map.get(((IGalacticraftWorldProvider)worldIn.provider).getCelestialBody());
        }
        else
        {
            Thread.dumpStack();
        }
        biomeTrue.decorate(worldIn, rand, pos);
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        if (worldIn.provider instanceof IGalacticraftWorldProvider)
        {
            biomeTrue = map.get(((IGalacticraftWorldProvider)worldIn.provider).getCelestialBody());
        }
        else
        {
            Thread.dumpStack();
        }
        biomeTrue.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos)
    {
        return biomeTrue.getGrassColorAtPos(pos);
    }

    @Override
    public final void generateBiomeTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        if (worldIn.provider instanceof IGalacticraftWorldProvider)
        {
            biomeTrue = map.get(((IGalacticraftWorldProvider)worldIn.provider).getCelestialBody());
        }
        else
        {
            Thread.dumpStack();
        }
        biomeTrue.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos)
    {
        return biomeTrue.getFoliageColorAtPos(pos);
    }

    @Override
    public Class <? extends Biome > getBiomeClass()
    {
        return biomeTrue.getBiomeClass();
    }

    @Override
    public Biome.TempCategory getTempCategory()
    {
        return biomeTrue.getTempCategory();
    }

    @Override
    public boolean ignorePlayerSpawnSuitability()
    {
        return biomeTrue.ignorePlayerSpawnSuitability();
    }

    @Override
    public float getBaseHeight()
    {
        return biomeTrue == null ? 0.0F : biomeTrue.getBaseHeight();
    }

    @Override
    public float getRainfall()
    {
        return biomeTrue.getRainfall();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getBiomeName()
    {
        return biomeTrue.getBiomeName();
    }

    @Override
    public float getHeightVariation()
    {
        return biomeTrue.getHeightVariation();
    }

    @Override
    public float getDefaultTemperature()
    {
        return biomeTrue.getDefaultTemperature();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getWaterColor()
    {
        return biomeTrue.getWaterColor();
    }

    @Override
    public boolean isSnowyBiome()
    {
        return biomeTrue.isSnowyBiome();
    }

    @Override
    public BiomeDecorator getModdedBiomeDecorator(BiomeDecorator original)
    {
        return biomeTrue.getModdedBiomeDecorator(original);
    }

    @Override
    public int getWaterColorMultiplier()
    {
        return biomeTrue.getWaterColorMultiplier();
    }

    @Override
    public int getModdedBiomeGrassColor(int original)
    {
        return biomeTrue.getModdedBiomeGrassColor(original);
    }

    @Override
    public int getModdedBiomeFoliageColor(int original)
    {
        return biomeTrue.getModdedBiomeFoliageColor(original);
    }

    @Override
    public void addDefaultFlowers()
    {
        if (biomeTrue != null)
            biomeTrue.addDefaultFlowers();
    }

    @Override
    public void addFlower(IBlockState state, int weight)
    {
        biomeTrue.addFlower(state, weight);
    }

    @Override
    public void plantFlower(World world, Random rand, BlockPos pos)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            biomeTrue = map.get(((IGalacticraftWorldProvider)world.provider).getCelestialBody());
        }
        else
        {
            Thread.dumpStack();
        }
        biomeTrue.plantFlower(world, rand, pos);
    }
}
