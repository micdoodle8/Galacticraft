package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.miccore.IntCache;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.storage.WorldInfo;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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
    private final int biomeMapFactor;
    private final int tickLimit;
    private int imagefactor;

    private BiomeCache biomeCache;
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    public File biomeMapFile;
    private byte[] biomeAndHeightArray = null;
    private int biomeMapSizeX;
    private int biomeMapSizeZ;

    private Random rand;
    private int[] heights;
    private double[] heighttemp;
    private World world;
    private WorldType worldType;
    private WorldInfo worldInfo;
    private ChunkGeneratorSettings settings = null;
    
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
        this.biomeMapFactor = scale;
        this.tickLimit = Math.min(scale, 16);
        if (MapGen.disabled)
        {
            this.mapNeedsCalculating = false;
            return;
        }
        this.biomeMapSizeX = sx;
        this.biomeMapSizeZ = sz;
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
        this.worldInfo = worldIn.getWorldInfo();
        this.worldType = worldInfo.getTerrainType();
        long seed = worldInfo.getSeed();
        this.biomeCache = new BiomeCache(this);
        String options = worldInfo.getGeneratorOptions();
        GenLayer[] agenlayer;
        try {
            if (options != null)
            {
                this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(options).build();
            }
            if (CompatibilityManager.isBOPWorld(this.worldType))
            {
                Object settingsBOP = CompatibilityManager.classBOPws.getConstructor(String.class).newInstance(options);
                Method bopSetup = CompatibilityManager.classBOPwcm.getMethod("setupBOPGenLayers", long.class, settingsBOP.getClass());
                agenlayer = (GenLayer[]) bopSetup.invoke(null, seed, settingsBOP);
            }
            else
            {
                agenlayer = GenLayer.initializeAllBiomeGenerators(seed, worldType, this.settings);
            }
            agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayer);
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

        long seed = worldInfo.getSeed();
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
        this.heights = new int[256];
        this.heighttemp = new double[825];
        this.biomeCount = new int[this.tickLimit * this.tickLimit];
    }

    public static void arrayClear(int[] array, int len)
    {
        int lenB = len < 16 ? len : 16; 
        for (int i = 0; i < lenB; i++)
        {
            array[i] = 0;
        }
        for (int i = 16; i < len; i += i)
        {
          System.arraycopy(array, 0, array, i, i + i > len ? i : len - i);
        }
    }

	/*
	 * Return false while there are further ticks to carry out 
	 * Return true when completed
	 */
    public boolean BiomeMapOneTick()
    {
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
            biomeMapOneChunk(biomeMapCx + biomeMapX, biomeMapCz + biomeMapZ, progX, progressZ, tickLimit);
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
        ArrayList<Integer> cols = new ArrayList<>();
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
                arrayClear(biomeCount, cols.size());

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
        this.generateHeightMap(cx * 4, 0, cz * 4);

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

    static double[] mainNoiseRegion;
    static double[] minLimitRegion;
    static double[] maxLimitRegion;
    static double[] depthRegion;
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
        NoiseGeneratorPerlin ignore1 = new NoiseGeneratorPerlin(this.rand, 4);
        NoiseGeneratorOctaves ignore2 = new NoiseGeneratorOctaves(this.rand, 10);
        noiseGen4 = new NoiseGeneratorOctaves(rand, 16);
        NoiseGeneratorOctaves ignore3 = new NoiseGeneratorOctaves(this.rand, 8);
        net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld ctx =
                new net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld(noiseGen1, noiseGen2, noiseGen3, ignore1, ignore2, noiseGen4, ignore3);
        ctx = net.minecraftforge.event.terraingen.TerrainGen.getModdedNoiseGenerators(this.world, this.rand, ctx);
        noiseGen1 = ctx.getLPerlin1();
        noiseGen2 = ctx.getLPerlin2();
        noiseGen3 = ctx.getPerlin();
        noiseGen4 = ctx.getDepth();
    }

    private void generateHeightMap(int cx, int cy, int cz)
    {
        float f = this.settings.coordinateScale;
        float f1 = this.settings.heightScale;
        depthRegion = noiseGen4.generateNoiseOctaves(depthRegion, cx, cz, 5, 5, (double)this.settings.depthNoiseScaleX, (double)this.settings.depthNoiseScaleZ, (double)this.settings.depthNoiseScaleExponent);
        mainNoiseRegion = noiseGen3.generateNoiseOctaves(mainNoiseRegion, cx, cy, cz, 5, 33, 5, (double)(f / this.settings.mainNoiseScaleX), (double)(f1 / this.settings.mainNoiseScaleY), (double)(f / this.settings.mainNoiseScaleZ));
        minLimitRegion = noiseGen1.generateNoiseOctaves(minLimitRegion, cx, cy, cz, 5, 33, 5, (double)f, (double)f1, (double)f);
        maxLimitRegion = noiseGen2.generateNoiseOctaves(maxLimitRegion, cx, cy, cz, 5, 33, 5, (double)f, (double)f1, (double)f);
        boolean amplified = this.worldType == WorldType.AMPLIFIED;
        double minLimitScale = (double)this.settings.lowerLimitScale;
        double maxLimitScale = (double)this.settings.upperLimitScale;
        double stretchY = (double)this.settings.stretchY * 128.0D / 256.0D;
        double baseSize = (double)this.settings.baseSize;
        int i = 2;  //start at 2 and later skip 19-33 - because these heightMap entries are never referenced in our code in this class
        int j = 0;

        for (int xx = 0; xx < 5; ++xx)
        {
            for (int zz = 0; zz < 5; ++zz)
            {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                float theMinHeight = biomesGridHeights[xx + 22 + zz * 10].getBaseHeight();

                for (int x = -2; x <= 2; ++x)
                {
                    int baseIndex = xx + x + 22;
                    for (int z = -2; z <= 2; ++z)
                    {
                        Biome biomegenbase1 = biomesGridHeights[baseIndex + (zz + z) * 10];
                        float f5 = this.settings.biomeDepthOffSet + biomegenbase1.getBaseHeight() * this.settings.biomeDepthWeight;
                        float f6 = this.settings.biomeScaleOffset + biomegenbase1.getHeightVariation() * this.settings.biomeScaleWeight;

                        if (amplified && f5 > 0.0F)
                        {
                            f5 = 1.0F + f5 + f5;
                            f6 = 1.0F + f6 * 4.0F;
                        }

                        float f7 = parabolicField[x + 12 + z * 5] / (f5 + 2.0F);

                        if (biomegenbase1.getBaseHeight() > theMinHeight)
                        {
                            f7 /= 2.0F;
                        }

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }

                f2 = f2 / f4;
                f3 = f3 / f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;
                double d7 = this.depthRegion[j] / 8000.0D;

                if (d7 < 0.0D)
                {
                    d7 = -d7 * 0.3D;
                }

                d7 = d7 * 3.0D - 2.0D;

                if (d7 < 0.0D)
                {
                    d7 = d7 / 2.0D;

                    if (d7 < -1.0D)
                    {
                        d7 = -1.0D;
                    }

                    d7 = d7 / 1.4D;
                    d7 = d7 / 2.0D;
                }
                else
                {
                    if (d7 > 1.0D)
                    {
                        d7 = 1.0D;
                    }

                    d7 = d7 / 8.0D;
                }

                ++j;
                double d8 = (double)f3;
                double d9 = (double)f2;
                d8 = d8 + d7 * 0.2D;
                d8 = d8 * baseSize / 8.0D;
                double d0 = baseSize + d8 * 4.0D;

                for (int j2 = 2; j2 < 19; ++j2)
                {
                    double d1 = ((double)j2 - d0) * stretchY / d9;
                    if (d1 < 0.0D)
                    {
                        d1 *= 4.0D;
                    }

                    double d2 = this.minLimitRegion[i] / minLimitScale;
                    double d3 = this.maxLimitRegion[i] / maxLimitScale;
                    double d4 = (this.mainNoiseRegion[i] / 10.0D + 1.0D) / 2.0D;
                    double d5 = MathHelper.clampedLerp(d2, d3, d4) - d1;
                    heighttemp[i] = d5;
                    ++i;
                }
                i += 16;  //We skip j2 = 0-1 and 19-32
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
     *      REPLICATES method in WorldChunkManager (with higher performance!)
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
        System.arraycopy(aint, 0, listToReuse, 0, size);

        return listToReuse;
    }
}
