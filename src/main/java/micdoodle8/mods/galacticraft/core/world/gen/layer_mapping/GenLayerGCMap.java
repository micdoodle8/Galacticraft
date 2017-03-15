package micdoodle8.mods.galacticraft.core.world.gen.layer_mapping;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderSettings;

import java.util.concurrent.Callable;

public abstract class GenLayerGCMap extends net.minecraft.world.gen.layer.GenLayer
{
    /** seed from World#getWorldSeed that is used in the LCG prng */
    private long worldGenSeed;
    /** parent GenLayer that was provided via the constructor */
    protected GenLayerGCMap parent;
    /**
     * final part of the LCG prng that uses the chunk X, Z coords along with the other two seeds to generate
     * pseudorandom numbers
     */
    private long chunkSeed;
    /** base seed to the LCG prng provided via the constructor */
    protected long baseSeed;

    public static GenLayerGCMap[] initializeAllBiomeGenerators(long seed, WorldType p_180781_2_, String p_180781_3_)
    {
        GenLayerGCMap genlayer = new GenLayerIsland(1L);
        genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
        GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
        GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        GenLayerAddIsland genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
        genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
        GenLayerRemoveTooMuchOcean genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
        GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
        GenLayerAddIsland genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayerZoom genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
        genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
        GenLayerAddIsland genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
        GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland3);
        GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayerGCMap genlayer4 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
        ChunkProviderSettings chunkprovidersettings = null;
        int i = 4;
        int j = i;

        if (p_180781_2_ == WorldType.CUSTOMIZED && p_180781_3_.length() > 0)
        {
            chunkprovidersettings = ChunkProviderSettings.Factory.jsonToFactory(p_180781_3_).build();
            i = chunkprovidersettings.biomeSize;
            j = chunkprovidersettings.riverSize;
        }

        if (p_180781_2_ == WorldType.LARGE_BIOMES)
        {
            i = 6;
        }

        i = getModdedBiomeSize(p_180781_2_, i);

        GenLayerGCMap lvt_8_1_ = GenLayerZoom.magnify(1000L, genlayer4, 0);
        GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, lvt_8_1_);
        GenLayerGCMap lvt_10_1_ = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayerGCMap genlayerbiomeedge = worldType_getBiomeLayer(seed, genlayer4, p_180781_3_, p_180781_2_);
        GenLayerGCMap genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_10_1_);
        GenLayerGCMap genlayer5 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer5 = GenLayerZoom.magnify(1000L, genlayer5, j);
        GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer5);
        GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);

        for (int k = 0; k < i; ++k)
        {
            genlayerhills = new GenLayerZoom((long)(1000 + k), genlayerhills);

            if (k == 0)
            {
                genlayerhills = new GenLayerAddIsland(3L, genlayerhills);
            }

            if (k == 1 || i == 1)
            {
                genlayerhills = new GenLayerShore(1000L, genlayerhills);
            }
        }

        GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, genlayerhills);
        GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayerGCMap genlayer3 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(seed);
        genlayer3.initWorldGenSeed(seed);
        return new GenLayerGCMap[] {genlayerrivermix, genlayer3, genlayerrivermix};
    }

    private static GenLayerGCMap worldType_getBiomeLayer(long seed, GenLayerGCMap parentLayer, String chunkProviderSettingsJson, WorldType worldType)
    {
    	GenLayerGCMap ret = new GenLayerBiome(200L, parentLayer, worldType, chunkProviderSettingsJson);
    	ret = GenLayerZoom.magnify(1000L, ret, 2);
    	ret = new GenLayerBiomeEdge(1000L, ret);
    	return ret;
	}

	public GenLayerGCMap(long p_i2125_1_)
    {
        super(p_i2125_1_);
        this.baseSeed = p_i2125_1_;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += p_i2125_1_;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += p_i2125_1_;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += p_i2125_1_;
    }

    /**
     * Initialize layer's local worldGenSeed based on its own baseSeed and the world's global seed (passed in as an
     * argument).
     */
    @Override
    public void initWorldGenSeed(long seed)
    {
        this.worldGenSeed = seed;

        if (this.parent != null)
        {
            this.parent.initWorldGenSeed(seed);
        }

        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
    }

    /**
     * Initialize layer's current chunkSeed based on the local worldGenSeed and the (x,z) chunk coordinates.
     */
    @Override
    public void initChunkSeed(long p_75903_1_, long p_75903_3_)
    {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_1_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_3_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_1_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_3_;
    }

    /**
     * returns a LCG pseudo random number from [0, x). Args: int x
     */
    @Override
    protected int nextInt(int p_75902_1_)
    {
        int i = (int)((this.chunkSeed >> 24) % (long)p_75902_1_);

        if (i < 0)
        {
            i += p_75902_1_;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return i;
    }

    /**
     * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
     * amounts, or biomeList[] indices based on the particular GenLayer subclass.
     */
    @Override
    public abstract int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight);

    protected static boolean biomesEqualOrMesaPlateau(int biomeIDA, int biomeIDB)
    {
        if (biomeIDA == biomeIDB)
        {
            return true;
        }
        else if (biomeIDA != Biome.getIdForBiome(Biomes.MESA_ROCK) && biomeIDA != Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK))
        {
            final Biome biome = Biome.getBiome(biomeIDA);
            final Biome biome1 = Biome.getBiome(biomeIDB);

            try
            {
                return (biome != null && biome1 != null) && biome == biome1;
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Comparing biomes");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Biomes being compared");
                crashreportcategory.addCrashSection("Biome A ID", Integer.valueOf(biomeIDA));
                crashreportcategory.addCrashSection("Biome B ID", Integer.valueOf(biomeIDB));
                crashreportcategory.addCrashSection("Biome A", new Object()
                {
                    public String toString()
                    {
                        return String.valueOf((Object)biome);
                    }
                });
                crashreportcategory.addCrashSection("Biome B", new Object()
                {
                    public String toString()
                    {
                        return String.valueOf((Object)biome1);
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        else
        {
            return biomeIDB == Biome.getIdForBiome(Biomes.MESA_ROCK) || biomeIDB == Biome.getIdForBiome(Biomes.MESA_CLEAR_ROCK);
        }
    }

    /**
     * returns true if the biomeId is one of the various ocean biomes.
     */
    protected static boolean isBiomeOceanic(int p_151618_0_)
    {
        return net.minecraftforge.common.BiomeManager.oceanBiomes.contains(Biome.getBiome(p_151618_0_));
    }

    /**
     * selects a random integer from a set of provided integers
     */
    @Override
    protected int selectRandom(int... p_151619_1_)
    {
        return p_151619_1_[this.nextInt(p_151619_1_.length)];
    }

    /**
     * returns the most frequently occurring number of the set, or a random number from those provided
     */
    @Override
    protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_)
    {
        return p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_ ? p_151617_2_ : (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_ ? p_151617_1_ : (p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_3_ && p_151617_1_ == p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_2_ && p_151617_3_ != p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_3_ && p_151617_2_ != p_151617_4_ ? p_151617_1_ : (p_151617_1_ == p_151617_4_ && p_151617_2_ != p_151617_3_ ? p_151617_1_ : (p_151617_2_ == p_151617_3_ && p_151617_1_ != p_151617_4_ ? p_151617_2_ : (p_151617_2_ == p_151617_4_ && p_151617_1_ != p_151617_3_ ? p_151617_2_ : (p_151617_3_ == p_151617_4_ && p_151617_1_ != p_151617_2_ ? p_151617_3_ : this.selectRandom(new int[] {p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_}))))))))));
    }

    /* ======================================== FORGE START =====================================*/
    @Override
    protected long nextLong(long par1)
    {
        long j = (this.chunkSeed >> 24) % par1;

        if (j < 0)
        {
            j += par1;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return j;
    }

    public static int getModdedBiomeSize(WorldType worldType, int original)
    {
        net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize event = new net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize(worldType, original);
        net.minecraftforge.common.MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.getNewSize();
    }
    /* ========================================= FORGE END ======================================*/
}