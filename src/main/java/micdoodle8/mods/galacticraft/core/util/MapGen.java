package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.miccore.IntCache;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.layer.GenLayer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MapGen extends BiomeProvider implements Runnable
{
    public static boolean disabled;
    private static final float[] parabolicField = new float[25];

    public boolean mapNeedsCalculating = false;
    public AtomicBoolean finishedCalculating;
    private AtomicBoolean paused;
    private AtomicBoolean aborted;
    
    private AtomicInteger progressX;
    private int progressZ;
    private int biomeMapX;
    private int biomeMapZ;
    private int biomeMap0;
    private int biomeMapCx;
    private int biomeMapCz;
    private int biomeMapFactor;
    private int imagefactor;

    private BiomeCache biomeCache;
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    public File biomeMapFile;
    private byte[] biomeAndHeightArray = null;
    private int biomeMapSizeX;
    private int biomeMapSizeZ;

    private Random rand;
    private int[] heights = null;
    private double[] heighttemp = null;
    private WorldType worldType;
    private World world;
    private ChunkProviderSettings settings = null;
    
    private int[] biomesGrid = null;  //Memory efficient to keep re-using the same one.
    private Biome[] biomesGridHeights = null;
    private int[] biomeCount = null;
    private final int dimID;

    static
    {
        for (int j = -2; j <= 2; ++j)
        {
            for (int k = -2; k <= 2; ++k)
            {
                float f = 10.0F / MathHelper.sqrt((float) (j * j + k * k) + 0.2F);
                parabolicField[j + 2 + (k + 2) * 5] = f;
            }
        }
    }

    public MapGen(World worldIn, int sx, int sz, int cx, int cz, int scale, File file)
    {
        this.dimID = GCCoreUtil.getDimensionID(worldIn);
        if (MapGen.disabled)
        {
            this.mapNeedsCalculating = false;
            return;
        }
        this.biomeMapSizeX = sx;
        this.biomeMapSizeZ = sz;
        this.biomeMapFactor = scale;
        int progress = this.checkProgress(file); 
        if (progress < 0)
        {
            this.mapNeedsCalculating = false;
            return;
        }
        this.mapNeedsCalculating = true;

        this.rand = new Random();
        this.finishedCalculating = new AtomicBoolean();
        this.paused = new AtomicBoolean();
        this.aborted = new AtomicBoolean();

        this.biomeMapCx = cx >> 4;
        this.biomeMapCz = cz >> 4;
        this.biomeMapFile = file;
        this.imagefactor = 16 / biomeMapFactor;
        if (this.imagefactor < 1)
        {
            this.imagefactor = 1;
        }
        int limitX = biomeMapSizeX * biomeMapFactor / 32;
        int limitZ = biomeMapSizeZ * biomeMapFactor / 32;
        this.biomeMap0 = -limitZ;
        this.biomeMapX = -limitX;
        this.biomeMapZ = this.biomeMap0;
        this.progressX = new AtomicInteger();
        this.progressZ = 0;
        this.world = worldIn;
        this.worldType = worldIn.getWorldInfo().getTerrainType();
        if (this.worldType == WorldType.CUSTOMIZED && !worldIn.getWorldInfo().getGeneratorOptions().isEmpty())
        {
            this.settings = ChunkProviderSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
        }

        GCLog.debug("Starting map generation " + file.getName() + " top left " + ((biomeMapCx - limitX) * 16) + "," + ((biomeMapCz - limitZ) * 16));
        if (progress > 0)
        {
            this.resumeProgress(progress);
        }
    }

    /**
     * Returns -1 if the map file saved by the server on disk is already complete.
     * Returns 0 if a new file is needed
     * Returns >0 if an existing file is in progress (in this case, initialises
     * biomeAndHeightArray and smaller arrays to match that existing file. 
     */
    private int checkProgress(File file)
    {
        if (!file.exists()) return 0;
        
        if (file.length() != biomeMapSizeX * biomeMapSizeZ * 2)
        {
            return 0;
        }

        int progress = 0;
        if (file.getName().equals("Overworld" + MapUtil.OVERWORLD_LARGEMAP_WIDTH + ".bin"))
        {
            int len = (int) file.length();
            FileChannel fc;
            try {
                fc = (FileChannel.open(file.toPath()));
                fc.position(len - 8);
                byte[] flagdata = new byte[8];
                ByteBuffer databuff = ByteBuffer.wrap(flagdata);
                fc.read(databuff);
                if (testFlag(flagdata))
                {
                    databuff.order(ByteOrder.BIG_ENDIAN);
                    databuff.position(0);
                    progress = databuff.getInt();
                }
                else
                {
                    //No progress flag data, therefore the file must be complete
                    return -1;  
                }
                fc.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            if (progress > 0 && progress <= biomeMapSizeX - imagefactor)
            {
                try
                {
                    this.biomeAndHeightArray = FileUtils.readFileToByteArray(file);
                    this.initialiseSmallerArrays();
                    return progress;
                } catch (IOException e)
                {
                    e.printStackTrace();
                    this.biomeAndHeightArray = null;
                }
            }
            
            return 0;
        }

        //Smaller files are always complete, if present and name and size match
        return -1;
    }

    @Override
    public void run()
    {
    	//Allow some time for the pause on any other map gen thread to become effective 
    	try {
			Thread.currentThread().sleep(90);
		} catch (InterruptedException e) {}

        long seed = world.getSeed();
        this.biomeCache = new BiomeCache(this);
        GenLayer[] agenlayerOrig = GenLayer.initializeAllBiomeGenerators(seed, worldType, this.settings);
        GenLayer[] agenlayer;
        try {
            agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayerOrig);
        }
        catch (Exception e)
        {
            GCLog.severe("Galacticraft background map image generator not able to run (probably a mod conflict?)");
            GCLog.severe("Please report this at https://github.com/micdoodle8/Galacticraft/issues/2481");
            e.printStackTrace();
            this.mapNeedsCalculating = false;
            MapGen.disabled = true;
            return;
        }
        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
        this.initialise(seed);
    	
    	//Generate this map from start to finish within the thread
    	while (!this.aborted.get())
    	{
    	    if (this.paused.get())
    	    {
    	        try {
    	            //Sleep for a bit, next time around maybe will not be paused?
    	            Thread.currentThread().sleep(1211);
    	        } catch (InterruptedException e) {}
    	    }
    	    else
    	    {
    	        //Do the actual work of the thread
    	        if (this.BiomeMapOneTick())
    	        {
    	            break;  //finished!
    	        }
    	    }
    	}

       	this.finishedCalculating.set(true);
    }

    public void pause()
    {
        this.paused.set(true);
    }

    public void resume()
    {
        this.paused.set(false);
    }

    public void abort()
    {
        this.aborted.set(true);
    }

    private void flagProgress()
    {
        int progX = this.progressX.get(); 
        if (progX > biomeMapSizeX - imagefactor)
            return;
        
        GCLog.debug("Saving partial map image progress " + progX);
        int offset = this.biomeAndHeightArray.length;
        this.biomeAndHeightArray[offset - 1] = (byte) 0xFE;
        this.biomeAndHeightArray[offset - 2] = (byte) 0x06;
        this.biomeAndHeightArray[offset - 3] = (byte) 0x03;
        this.biomeAndHeightArray[offset - 4] = (byte) 0x0E;
        this.biomeAndHeightArray[offset - 5] = (byte) (progX & 0xFF);
        this.biomeAndHeightArray[offset - 6] = (byte) (progX >> 8 & 0xFF);
        this.biomeAndHeightArray[offset - 7] = (byte) (progX >> 16 & 0xFF);
        this.biomeAndHeightArray[offset - 8] = (byte) (progX >> 24 & 0xFF);
    }
    
    public static boolean testFlag(byte[] bb)
    {
        return (bb[7] & 0xFF) == 0xFE && bb[6] == 0x06 && bb[5] == 0x03 && bb[4] == 0x0E;
    }
    
    private void resumeProgress(int progress)
    {
        int multifactor = biomeMapFactor >> 4;
        if (multifactor < 1)
        {
            multifactor = 1;
        }
        int progCount = progress / imagefactor;
        
        progressX.set(progress);
        biomeMapX = multifactor * progCount - (biomeMapSizeX * biomeMapFactor / 32);
        if (biomeMapX > -biomeMap0 * 4)
        {
            biomeMapX += biomeMap0 * 8;
        }
    }
    
    /**
     * This is outside the multithreaded portion of the code
     * This should be called after the finishedCalculating flag is set.
     */
    public void writeOutputFile(boolean sendToClientImmediately)
    {
        if (this.biomeAndHeightArray == null)
            return;
        
        if (!this.aborted.get())  //It should be error-free if it wasn't aborted 
        {
            try
            {
                if (!this.biomeMapFile.exists() || (this.biomeMapFile.canWrite() && this.biomeMapFile.canRead()))
                {
                    this.flagProgress();
                    FileUtils.writeByteArrayToFile(this.biomeMapFile, this.biomeAndHeightArray);
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
    
            if (sendToClientImmediately)
            {
                MapUtil.sendMapPacketToAll(this.biomeMapCx << 4, this.biomeMapCz << 4, this.biomeAndHeightArray);
            }
        }

        this.biomeAndHeightArray = null;
    }

    private void initialiseSmallerArrays()
    {
        int limit = Math.min(biomeMapFactor, 16);
        this.heights = new int[256];
        this.heighttemp = new double[825];
        this.biomeCount = new int[limit * limit];
    }

	/*
	 * Return false while there are further ticks to carry out 
	 * Return true when completed
	 */
    public boolean BiomeMapOneTick()
    {
    	int limit = Math.min(biomeMapFactor, 16);
        if (this.biomeAndHeightArray == null)
        {
            this.biomeAndHeightArray = new byte[biomeMapSizeX * biomeMapSizeZ * 2];
            this.initialiseSmallerArrays();
        }
        int multifactor = biomeMapFactor >> 4;
        if (multifactor < 1)
        {
            multifactor = 1;
        }
        int progX = this.progressX.get();
        try
        {
            biomeMapOneChunk(biomeMapCx + biomeMapX, biomeMapCz + biomeMapZ, progX, progressZ, limit);
        }
        catch (Exception e)
        {
            GCLog.severe("Galacticraft background map image generator hit an error (probably a mod conflict?)");
            GCLog.severe("--> Please report this at https://github.com/micdoodle8/Galacticraft/issues/2544 <--");
            e.printStackTrace();
            MapGen.disabled = true;
            this.aborted.set(true);
            return true;
        }
        biomeMapZ += multifactor;
        progressZ += imagefactor;
        if (progressZ > biomeMapSizeZ - imagefactor)
        {
            progressZ = 0;
//            if (progX % 3 == 0)
//            {
//                System.out.println("Finished map column " + progX + " at " + (biomeMapCx + biomeMapX) + "," + (biomeMapCz + biomeMapZ));
//            }
            this.progressX.set(progX + imagefactor);
            biomeMapZ = biomeMap0;
            biomeMapX += multifactor;
            if (biomeMapX > -biomeMap0 * 4)
            {
                biomeMapX += biomeMap0 * 8;
            }
            return progX > biomeMapSizeX - imagefactor - imagefactor;
        }
        return false;
    }

    private void biomeMapOneChunk(int x0, int z0, int ix, int iz, int limit)
    {
        biomesGrid = this.getBiomeGenAt(biomesGrid, x0 << 4, z0 << 4, 16, 16);
        if (biomesGrid == null)
        {
            return;
        }
        this.getHeightMap(x0, z0);
        int factor = this.biomeMapFactor;
        int halfFactor = limit * limit / 2;
        ArrayList<Integer> cols = new ArrayList<Integer>();
        for (int j = 0; j < biomeCount.length; j++)
        {
            biomeCount[j] = 0;
        }
        for (int x = 0; x < 16; x += factor)
        {
            int izstore = iz;
            for (int z = 0; z < 16; z += factor)
            {
                cols.clear();
                int maxcount = 0;
                int maxindex = -1;
                int biome = -1;
                int lastcol = -1;
                int idx = 0;
                int avgHeight = 0;
                int divisor = 0;
                //TODO: start in centre instead of top left
                BIOMEDONE:
                for (int xx = 0; xx < limit; xx++)
                {
                    int hidx = ((xx + x) << 4) + z;
                    for (int zz = 0; zz < limit; zz++)
                    {
                        int height = heights[hidx + zz];
                        avgHeight += height;
                        divisor++;
                        biome = biomesGrid[xx + x + ((zz + z) << 4)];
                        if (biome != lastcol)
                        {
                            idx = cols.indexOf(biome);
                            if (idx == -1)
                            {
                                idx = cols.size();
                                cols.add(biome);
                            }
                            lastcol = biome;
                        }
                        biomeCount[idx]++;
                        if (biomeCount[idx] > maxcount)
                        {
                            maxcount = biomeCount[idx];
                            maxindex = idx;
                            if (maxcount > halfFactor)
                            {
                                break BIOMEDONE;
                            }
                        }
                    }
                }
                //Clear the array for next time
                for (int j = cols.size() - 1; j >= 0; j--)
                {
                    biomeCount[j] = 0;
                }

                int arrayIndex = (ix * biomeMapSizeZ + iz) * 2;
                this.biomeAndHeightArray[arrayIndex] = (byte) (cols.get(maxindex).intValue());
                this.biomeAndHeightArray[arrayIndex + 1] = (byte) ((avgHeight + (divisor + 1) / 2) / divisor);
                iz++;
            }
            iz = izstore;
            ix++;
        }
    }

    public void getHeightMap(int cx, int cz)
    {
        rand.setSeed((long) cx * 341873128712L + (long) cz * 132897987541L);
        biomesGridHeights = this.getBiomesForGeneration(biomesGridHeights, cx * 4 - 2, cz * 4 - 2, 10, 10);
        this.func_147423_a(cx * 4, 0, cz * 4);

        final double d0 = 0.125D;
        final double d9 = 0.25D;

        for (int xx = 0; xx < 4; ++xx)
        {
            int xa = xx * 5;
            int xb = xa + 5;

            for (int zz = 0; zz < 4; ++zz)
            {
                int aa = (xa + zz) * 33;
                int ab = aa + 33;
                int ba = (xb + zz) * 33;
                int bb = ba + 33;

                for (int yy = 2; yy < 18; ++yy)
                {
                    double d1 = heighttemp[aa + yy];
                    double d2 = heighttemp[ab + yy];
                    double d3 = heighttemp[ba + yy];
                    double d4 = heighttemp[bb + yy];
                    double d5 = (heighttemp[aa + yy + 1] - d1) * d0;
                    double d6 = (heighttemp[ab + yy + 1] - d2) * d0;
                    double d7 = (heighttemp[ba + yy + 1] - d3) * d0;
                    double d8 = (heighttemp[bb + yy + 1] - d4) * d0;

                    for (int y = 0; y < 8; ++y)
                    {
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        int truey = yy * 8 + y;
                        for (int x = 0; x < 4; ++x)
                        {
                            int idx = x + xx * 4 << 4 | zz * 4;
                            double d16 = (d11 - d10) * d9;
                            double d15 = d10 - d16;

                            for (int z = 0; z < 4; ++z)
                            {
                                if ((d15 += d16) > 0.0D)
                                {
                                    heights[idx + z] = truey;
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    static double[] noiseField3;
    static double[] noiseField1;
    static double[] noiseField2;
    static double[] noiseField4;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    public NoiseGeneratorOctaves noiseGen4;

    public void initialise(long seed)
    {
        rand = new Random(seed);
        noiseGen1 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen2 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen3 = new NoiseGeneratorOctaves(rand, 8);
        noiseGen4 = new NoiseGeneratorOctaves(rand, 16);
    }

    private void func_147423_a(int cx, int cy, int cz)
    {
        double d0 = 684.412D;
        double d1 = 684.412D;
        double d2 = 512.0D;
        double d3 = 512.0D;
        noiseField4 = noiseGen4.generateNoiseOctaves(noiseField4, cx, cz, 5, 5, 200.0D, 200.0D, 0.5D);
        noiseField3 = noiseGen3.generateNoiseOctaves(noiseField3, cx, cy, cz, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        noiseField1 = noiseGen1.generateNoiseOctaves(noiseField1, cx, cy, cz, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        noiseField2 = noiseGen2.generateNoiseOctaves(noiseField2, cx, cy, cz, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        boolean flag1 = false;
        boolean flag = false;
        int l = 2;
        int i1 = 0;
        double d4 = 8.5D;
        boolean amplified = this.worldType == WorldType.AMPLIFIED;

        for (int xx = 0; xx < 5; ++xx)
        {
            for (int zz = 0; zz < 5; ++zz)
            {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                float theMinHeight = biomesGridHeights[xx + 22 + zz * 10].getBaseHeight();

                for (int x = -2; x <= 2; ++x)
                {
                    int baseIndex = xx + x + 22 + zz * 10;
                    for (int z = -2; z <= 2; ++z)
                    {
                        Biome biomegenbase1 = biomesGridHeights[baseIndex + z * 10];
                        float f3 = biomegenbase1.getBaseHeight();
                        float f4 = biomegenbase1.getBaseHeight();

                        if (amplified && f3 > 0.0F)
                        {
                            f3 = 1.0F + f3 + f3;
                            f4 = 1.0F + f4 * 4.0F;
                        }

                        float f5 = parabolicField[x + 12 + z * 5] / (f3 + 2.0F);

                        if (biomegenbase1.getBaseHeight() > theMinHeight)
                        {
                            f5 /= 2.0F;
                        }

                        f += f4 * f5;
                        f1 += f3 * f5;
                        f2 += f5;
                    }
                }

                f /= f2;
                f1 /= f2;
                f = f * 0.9F + 0.1F;
                f1 = f1 / 2.0F - 0.125F;
                double d12 = noiseField4[i1] / 8000.0D;

                if (d12 < 0.0D)
                {
                    d12 = -d12 * 0.3D;
                }

                d12 = d12 * 3.0D - 2.0D;

                if (d12 < 0.0D)
                {
                    d12 /= 2.0D;

                    if (d12 < -1.0D)
                    {
                        d12 = -1.0D;
                    }

                    d12 /= 1.4D;
                    d12 /= 2.0D;
                }
                else
                {
                    if (d12 > 1.0D)
                    {
                        d12 = 1.0D;
                    }

                    d12 /= 8.0D;
                }

                ++i1;
                double d13 = (double) f1;
                final double d14 = (double) f / 6.0D;
                d13 += d12 * 0.2D;
                d13 = d13 * 8.5D / 8.0D;
                double d5 = 8.5D + d13 * 4.0D;

                for (int j2 = 2; j2 < 19; ++j2)
                {
                    double d6 = ((double) j2 - d5) / d14;
                    if (d6 < 0.0D)
                    {
                        d6 *= 4.0D;
                    }

                    double d7 = noiseField1[l] / 512.0D;
                    double d8 = noiseField2[l] / 512.0D;
                    double d9 = (noiseField3[l] / 10.0D + 1.0D) / 2.0D;
                    heighttemp[l] = MathHelper.clampedLerp(d7, d8, d9) - d6;
                    ++l;
                }
                l += 16;
            }
        }
    }
    
    /**
     *      REPLICATES method in WorldChunkManager
     * Returns an array of biomes for the location input, used for generating the height map
     */
    @Override
    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height)
    {
        IntCache.resetIntCacheGC();
        int[] aint = this.genBiomes.getInts(x, z, width, height);

        int size = width * height;
        if (biomes == null || biomes.length < size)
        {
            biomes = new Biome[size];
        }
        for (int i = 0; i < size; ++i)
        {
        	int biomeId = aint[i];
        	Biome biomegenbase = Biome.getBiome(biomeId, Biomes.DEFAULT);
//        	else
//        		System.err.println("MapGen: Biome ID is out of bounds: " + biomeId + ", defaulting to 0 (Ocean)");
        	biomes[i] = biomegenbase == null ? Biomes.OCEAN : biomegenbase;
        }

        return biomes;
    }
    
    /**
     *      REPLICATES method in WorldChunkManager
     * Return a list of ints representing mapgen biomes at the specified coordinates. Args: listToReuse, x, y, width, height
     * This is after all genlayers (oceans, islands, hills, rivers, etc)
     */
    public int[] getBiomeGenAt(int[] listToReuse, int x, int z, int width, int height)
    {
        IntCache.resetIntCacheGC();
        int[] aint = this.biomeIndexLayer.getInts(x, z, width, height);

        int size = width * height;
        if (listToReuse == null || listToReuse.length < size)
        {
            listToReuse = new int[size];
        }
        for (int i = 0; i < size; ++i)
        {
        	listToReuse[i] = aint[i];
        }

        return listToReuse;
    }
}
