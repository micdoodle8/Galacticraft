package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import java.util.LinkedList;
import java.util.List;
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
import micdoodle8.mods.galacticraft.core.util.GCLog;

public class BiomeAdaptive extends BiomeGenBaseGC
{
    public static BiomeAdaptive biomeDefault;
    public static List<BiomeAdaptive> biomeList = new LinkedList<>(); 
    private Biome biomeTrue;
    private final int index;
    private boolean loggedConflict;
    
    public BiomeAdaptive(int i, Biome biomeInitial)
    {
        super(new BiomeProperties("Outer Space" + (i == 0 ? "" : " "+ i)).setRainfall(0.0F));
        this.index = i;
        this.biomeTrue = biomeInitial;
        this.decorator = this.createBiomeDecorator();
        if (index == 0)
        {
            biomeDefault = this;
        }
    }

    @Override
    public void registerTypes(Biome b)
    {
        if (this.biomeTrue instanceof BiomeGenBaseGC)
        {   
            ((BiomeGenBaseGC) this.biomeTrue).registerTypes(this);
        }
        else
        {
            BiomeDictionary.addTypes(this, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.SPOOKY);
        }
    }

    public static BiomeGenBaseGC register(int index, BiomeGenBaseGC biome)
    {
        if (index >= biomeList.size())
        {
            BiomeAdaptive newAdaptive = new BiomeAdaptive(index, biome); 
            biomeList.add(newAdaptive);
            return newAdaptive;
        }
        return biomeList.get(index);
    }
    
    public static BiomeGenBaseGC getDefaultBiomeFor(CelestialBody body)
    {
        return body.biomesToAdapt[0];
    }

    public static void setBody(CelestialBody body)
    {
        biomeDefault.setBodyInstance(body);
    }
    
    public boolean isInstance(Class<?> clazz)
    {
        return clazz.isInstance(this.biomeTrue);
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
     * @return true if end of list
     */
    protected boolean setBodyInstance(CelestialBody body)
    {
        this.biomeTrue = body.biomesToAdapt[this.index];
        this.fillerBlock = this.biomeTrue.fillerBlock;
        this.topBlock = this.biomeTrue.topBlock;
        this.decorator = this.biomeTrue.decorator;
        return this.index == body.biomesToAdapt.length - 1;
    }

    public static List<Biome> getBiomesListFor(CelestialBody body)
    {
        List<Biome> result = new LinkedList<>();
        for (BiomeAdaptive b : biomeList)
        {
            if (result.size() >= body.biomesToAdapt.length)
            {
                break;
            }
            
            result.add(b);
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
            this.setBodyInstance(((IGalacticraftWorldProvider)worldIn.provider).getCelestialBody());
        }
        else
        {
            reportBiomeIDconflict();
        }
        biomeTrue.decorate(worldIn, rand, pos);
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        if (worldIn.provider instanceof IGalacticraftWorldProvider)
        {
            this.setBodyInstance(((IGalacticraftWorldProvider)worldIn.provider).getCelestialBody());
        }
        else
        {
            reportBiomeIDconflict();
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
            this.setBodyInstance(((IGalacticraftWorldProvider)worldIn.provider).getCelestialBody());
        }
        else
        {
            reportBiomeIDconflict();
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
            this.setBodyInstance(((IGalacticraftWorldProvider)world.provider).getCelestialBody());
        }
        else
        {
            reportBiomeIDconflict();
        }
        biomeTrue.plantFlower(world, rand, pos);
    }

    private void reportBiomeIDconflict()
    {
        if (this.loggedConflict) return;
        this.loggedConflict = true;
        GCLog.severe("POTENTIAL BIOME ID CONFLICT for id " + Biome.getIdForBiome(this) + " conflicting with Galacticraft");
        GCLog.severe("PLEASE CHECK CONFIGS FOR BOTH MODS: see Galacticraft core.conf setting BiomeIDBase");
        Thread.dumpStack();
    }
}
