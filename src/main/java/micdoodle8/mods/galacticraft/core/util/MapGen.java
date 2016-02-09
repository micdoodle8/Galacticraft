package micdoodle8.mods.galacticraft.core.util;

import java.io.File;
import java.io.IOException;
//import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class MapGen
{
    public boolean calculatingMap = false;
    private int ix = 0;
    private int iz = 0;
    private int biomeMapx0 = 0;
    private int biomeMapz0 = 0;
	private int biomeMapz00;
	private int biomeMapCx;
	private int biomeMapCz;
	private int biomeMapFactor;
	private WorldChunkManager biomeMapWCM;
    private static GenLayer biomeMapGenLayer;
	public File biomeMapFile;
	private byte[] biomeAndHeightArray = null;
	public EntityPlayerMP biomeMapPlayerBase = null;
	private int biomeMapSizeX;
	private int biomeMapSizeZ;
	private Random rand = new Random();
//	private WeakReference<World> biomeMapWorld;
    private int[] heights = null;
    private double[] heighttemp = null;
    private WorldType field_147435_p = WorldType.DEFAULT;      

    public MapGen(World world, int sx, int sz, int cx, int cz, int scale, File file)
    {
    	if (file.exists())
        {
			try {
				this.sendToClient(FileUtils.readFileToByteArray(file));
			} catch (IOException e) { e.printStackTrace(); }
			return;
        }

    	this.biomeMapFile = file;
    	this.calculatingMap = true;
    	this.biomeMapSizeX = sx;
    	this.biomeMapSizeZ = sz;
    	this.biomeMapFactor = scale;
    	int limitX = biomeMapSizeX * biomeMapFactor / 32;
    	int limitZ = biomeMapSizeZ * biomeMapFactor / 32;
    	this.biomeMapz00 = -limitZ;
    	this.biomeMapx0 = -limitZ;
    	this.biomeMapz0 = this.biomeMapz00;
    	this.ix = 0;
    	this.iz = 0;
    	this.biomeMapWCM = world.getWorldChunkManager();
  //  	this.biomeMapWorld = new WeakReference<World>(world);
    	this.biomeMapCx = cx;
    	this.biomeMapCz = cz;
    	try {
    		Field bil = biomeMapWCM.getClass().getDeclaredField(VersionUtil.getNameDynamic(VersionUtil.KEY_FIELD_BIOMEINDEXLAYER));
    		bil.setAccessible(true);
    		this.biomeMapGenLayer = (GenLayer) bil.get(biomeMapWCM);
    	} catch (Exception e) { }
    	if (this.biomeMapGenLayer == null)
    	{
    		this.calculatingMap = false;
    		GCLog.debug("Failed to get gen layer from World Chunk Manager.");
    		return;
    	}
    	
    	GCLog.debug("Starting map generation centered at " + cx + "," + cz);
        field_147435_p = world.getWorldInfo().getTerrainType();
    	this.initialise(world.getSeed());
    }
    
	public void writeOutputFile(boolean flag)
	{
		try
		{
			if (!this.biomeMapFile.exists() || (this.biomeMapFile.canWrite() && this.biomeMapFile.canRead()))
			{
				FileUtils.writeByteArrayToFile(this.biomeMapFile, this.biomeAndHeightArray);
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		
		if (flag)
		{
			this.sendToClient(this.biomeAndHeightArray);
		}
		
		this.biomeAndHeightArray = null;
	}
	
	private void sendToClient(byte[] toSend)
	{
		try
		{
			GalacticraftCore.packetPipeline.sendToAll(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { toSend } ));
		}
		catch (Exception ex)
		{
			System.err.println("Error sending overworld image to player.");
			ex.printStackTrace();
		}	
	}

	public boolean BiomeMapOneTick()
	{
		if (this.biomeAndHeightArray == null)
		{
	    	this.biomeAndHeightArray = new byte[biomeMapSizeX * biomeMapSizeZ * 2];
	    	this.heights = new int[256];
	    	this.heighttemp = new double[825];
		}
		int multifactor = biomeMapFactor >> 4;
		if (multifactor < 1) multifactor = 1;
		int imagefactor = 16 / biomeMapFactor;
		if (imagefactor < 1) imagefactor = 1;
    	biomeMapOneChunk(biomeMapCx + biomeMapx0, biomeMapCz + biomeMapz0, ix, iz, biomeMapFactor);
    	biomeMapz0 += multifactor;
    	iz += imagefactor;
    	if (iz > biomeMapSizeZ - imagefactor)
    	{
        	iz = 0;
        	if (ix % 25 == 8) GCLog.debug("Finished map column " + ix + " at " + (biomeMapCx + biomeMapx0) + "," + (biomeMapCz + biomeMapz0));
        	ix += imagefactor;
        	biomeMapz0 = biomeMapz00;
        	biomeMapx0 += multifactor;
        	if (biomeMapx0 > -biomeMapz00 * 4) biomeMapx0 += biomeMapz00 * 8; 
        	return ix > biomeMapSizeX - imagefactor;
    	}
    	return false;
	}
	
	private void biomeMapOneChunk(int x0, int z0, int ix, int iz, int factor)
	{
        IntCache.resetIntCache();
		int[] biomesGrid = biomeMapGenLayer.getInts(x0 << 4, z0 << 4, 16, 16);
//		TODO: For some reason getInts() may not work in Minecraft 1.7.2, gives a banded result where part of the array is 0
//		BiomeGenBase[] biomesGrid = biomeMapWCM.getBiomeGenAt(null, x0 << 4, z0 << 4, 16, 16, false);
    	if (biomesGrid == null) return;
    	getHeightMap(x0, z0);
    	int limit = Math.min(factor, 16);
		int halfFactor = limit * limit / 2;
		ArrayList<Integer> cols = new ArrayList<Integer>();
		int[] count = new int[limit * limit];
    	for (int x = 0; x < 16; x += factor)
    	{
    		int izstore = iz;
    		for (int z = 0; z < 16; z += factor)
    		{
    			cols.clear();
    			for (int j = 0; j < count.length; j++)
    				count[j] = 0;
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
        				divisor ++;
        				biome = biomesGrid[xx + x + ((zz + z) << 4)];   //.biomeID;
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
        				//TODO: reduce prevalence of water at height 62-63
        				count[idx]++;
        				if (count[idx] > maxcount)
        				{
        					maxcount = count[idx];
        					maxindex = idx;
        					if (maxcount > halfFactor) break BIOMEDONE;
        				}                				
        			}
    			}
    			int arrayIndex = (ix * biomeMapSizeZ + iz) * 2;
    			this.biomeAndHeightArray[arrayIndex] = (byte) (cols.get(maxindex).intValue());
    			this.biomeAndHeightArray[arrayIndex + 1] = (byte) (avgHeight / divisor);
    			iz++;
    		}
    		iz = izstore;
    		ix ++;
    	}	
	}
	
    public void getHeightMap(int cx, int cz)
    {
    	rand.setSeed((long)cx * 341873128712L + (long)cz * 132897987541L);
        byte seaLevel = 63;
        func_147423_a(this.biomeMapWCM.getBiomesForGeneration(null, cx * 4 - 2, cz * 4 - 2, 10, 10), cx * 4, 0, cz * 4);

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

                for (int yy = 0; yy < 32; ++yy)
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
    private NoiseGeneratorPerlin noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    
    public void initialise(long seed)
    {
        rand = new Random(seed);
	    noiseGen1 = new NoiseGeneratorOctaves(rand, 16);
	    noiseGen2 = new NoiseGeneratorOctaves(rand, 16);
	    noiseGen3 = new NoiseGeneratorOctaves(rand, 8);
	    noiseGen4 = new NoiseGeneratorPerlin(rand, 4);
	    noiseGen5 = new NoiseGeneratorOctaves(rand, 10);
	    noiseGen6 = new NoiseGeneratorOctaves(rand, 16);
    }

    private void func_147423_a(BiomeGenBase[] biomesGrid, int p_147423_1_, int p_147423_2_, int p_147423_3_)
    {
        double d0 = 684.412D;
        double d1 = 684.412D;
        double d2 = 512.0D;
        double d3 = 512.0D;
        noiseField4 = noiseGen6.generateNoiseOctaves(noiseField4, p_147423_1_, p_147423_3_, 5, 5, 200.0D, 200.0D, 0.5D);
        noiseField3 = noiseGen3.generateNoiseOctaves(noiseField3, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        noiseField1 = noiseGen1.generateNoiseOctaves(noiseField1, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        noiseField2 = noiseGen2.generateNoiseOctaves(noiseField2, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        boolean flag1 = false;
        boolean flag = false;
        int l = 0;
        int i1 = 0;
        double d4 = 8.5D;
        boolean amplified = field_147435_p == WorldType.AMPLIFIED;

        for (int xx = 0; xx < 5; ++xx)
        {
            for (int zz = 0; zz < 5; ++zz)
            {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                BiomeGenBase biomegenbase = biomesGrid[xx + 22 + zz * 10];

                for (int x = -2; x <= 2; ++x)
                {
                    int baseIndex = xx + x + 22 + zz * 10;
                	for (int z = -2; z <= 2; ++z)
                    {
                        BiomeGenBase biomegenbase1 = biomesGrid[baseIndex + z * 10];
                        float f3 = biomegenbase1.rootHeight;
                        float f4 = biomegenbase1.heightVariation;

                        if (amplified && f3 > 0.0F)
                        {
                            f3 = 1.0F + f3 + f3;
                            f4 = 1.0F + f4 * 4.0F;
                        }

                        float f5 = MapUtil.parabolicField[x + 12 + z * 5] / (f3 + 2.0F);

                        if (biomegenbase1.rootHeight > biomegenbase.rootHeight)
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
                double d13 = (double)f1;
                double d14 = (double)f;
                d13 += d12 * 0.2D;
                d13 = d13 * 8.5D / 8.0D;
                double d5 = 8.5D + d13 * 4.0D;

                for (int j2 = 0; j2 < 33; ++j2)
                {
                    double d6 = ((double)j2 - d5) * 12.0D * 128.0D / 256.0D / d14;

                    if (d6 < 0.0D)
                    {
                        d6 *= 4.0D;
                    }

                    double d7 = noiseField1[l] / 512.0D;
                    double d8 = noiseField2[l] / 512.0D;
                    double d9 = (noiseField3[l] / 10.0D + 1.0D) / 2.0D;
                    double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;

                    if (j2 > 29)
                    {
                        double d11 = (double)((float)(j2 - 29) / 3.0F);
                        d10 = d10 * (1.0D - d11) + -10.0D * d11;
                    }

                    heighttemp[l] = d10;
                    ++l;
                }
            }
        }
    }
}
