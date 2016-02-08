package micdoodle8.mods.galacticraft.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.layer.GenLayer;

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.client.FMLClientHandler;

public class MapGen
{
    public boolean calculatingMap;
	public int gentype = 0;  //0 Overworld texture   1 Biome Array    2 Local map
    private int ix = 0;
    private int iz = 0;
    private int biomeMapx0 = 0;
    private int biomeMapz0 = 0;
	private int biomeMapz00;
	public int biomeMapCx;
	public int biomeMapCz;
	public int biomeMapFactor;
	private WorldChunkManager biomeMapWCM;
    private static GenLayer biomeMapGenLayer;
	public File biomeMapFile;
	public byte[] biomeArray;
	public byte[] heightArray;
	public EntityPlayerMP biomeMapPlayerBase = null;
	public int biomeMapSizeX;
	public int biomeMapSizeZ;
	public Random rand = new Random();
	public World biomeMapWorld;
    public int[] heights = new int[256];
    public double[] heighttemp = new double[825];
      

    public MapGen(World world, int newtype, int sx, int sz, int scale)
    {
		this.calculatingMap = true;
    	this.gentype = newtype;
    	this.biomeMapSizeX = sx;
    	this.biomeMapSizeZ = sz;
    	this.biomeArray = new byte[sx * sz];
    	this.heightArray = new byte[sx * sz];
    	this.biomeMapFactor = scale;
    	int limitX = biomeMapSizeX * biomeMapFactor / 32;
    	int limitZ = biomeMapSizeZ * biomeMapFactor / 32;
    	this.biomeMapz00 = -limitZ;
    	this.biomeMapx0 = -limitZ;
    	this.biomeMapz0 = this.biomeMapz00;
    	this.ix = 0;
    	this.iz = 0;
    	this.biomeMapWCM = world.getWorldChunkManager();
    	this.biomeMapWorld = world;
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
    	
    	this.initialise();
    }
    
	/**
	 * Converts a 48px high image to a 12px high image with a palette chosen only from the colours in the paletteImage
	 * 
	 * @param overworldImage  Output image already created as a blank image, dimensions biomeMapSizeX x biomeMapSizeY
	 * @param paletteImage   Palette image, dimensions must be a square with sides biomeMapSizeZ / 4
	 */
	//Unused
	public static BufferedImage readImage(Object source) throws IOException
	{
		ImageInputStream stream = ImageIO.createImageInputStream(source);
		ImageReader reader = ImageIO.getImageReaders(stream).next();
		reader.setInput(stream);
		ImageReadParam param =reader.getDefaultReadParam();

		ImageTypeSpecifier typeToUse = null;
		for (Iterator i = reader.getImageTypes(0);i.hasNext(); )
		{
			ImageTypeSpecifier type = (ImageTypeSpecifier) i.next();
			if (type.getColorModel().getColorSpace().isCS_sRGB())
				typeToUse = type;
		}
		if (typeToUse!=null) param.setDestinationType(typeToUse);

		BufferedImage b = reader.read(0, param);
		reader.dispose();
		stream.close();
		return b;
	}

    //This runs on the client
	public static void getOverworldImageFromRaw(byte[] raw) throws IOException
	{
		if (raw.length == 18432)
		{
			//raw is a 192 x 48 byte array of biome types, followed by a 192 x 48 byte array of heights
			File folder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/temp");
			File fileImg = new File(folder, "overworldLocal.png");
			if (fileImg.exists()) fileImg.delete();

			BufferedImage worldImage = new BufferedImage(192, 48, BufferedImage.TYPE_INT_RGB);
			ArrayList<Integer> cols = new ArrayList<Integer>();
			int lastcol = -1;
			int idx = 0;
			for (int x = 0; x < 192; x++)
			{
				for (int z = 0; z < 48; z++)
				{
					int biome = ((int) raw[x * 48 + z]) & 255;
					int height = ((int) raw[x * 48 + z + 9216]) & 255;

					if (height < 63)
						biome = 24;

					worldImage.setRGB(x, z, MapUtil.convertBiomeColour(biome, height));
				}
			}
			
			ImageOutputStream outputStreamA = new FileImageOutputStream(new File(folder, "overworldLarge.jpg"));  
			GalacticraftCore.jpgWriter.setOutput(outputStreamA);
			GalacticraftCore.jpgWriter.write(null, new IIOImage(worldImage, null, null), GalacticraftCore.writeParam);
			outputStreamA.close();

			IResourceManager rm = Minecraft.getMinecraft().getResourceManager();			
			BufferedImage paletteImage = null;
			try {
				InputStream in = rm.getResource(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png")).getInputStream();
				paletteImage = ImageIO.read(in);
				in.close();
				paletteImage.getHeight();
			} catch (Exception e) { e.printStackTrace(); return;  }

			BufferedImage result = MapUtil.convertTo12pxTexture(worldImage, paletteImage);
			ImageOutputStream outputStream = new FileImageOutputStream(fileImg);  
			GalacticraftCore.jpgWriter.setOutput(outputStream);
			GalacticraftCore.jpgWriter.write(null, new IIOImage(result, null, null), GalacticraftCore.writeParam);
			outputStream.close();

			//		BufferedImage img = ImageIO.read(fileImg);
			if (result != null)
			{
				ClientProxyCore.overworldTextureLocal = new DynamicTexture(result);
			}
		}
		else if (raw.length == 8192)
		{

		}
	}

	private void writeOutputFile(boolean flag)
	{
		byte[] toSend = new byte[biomeArray.length + heightArray.length];
		System.arraycopy(biomeArray, 0, toSend, 0, biomeArray.length);
		System.arraycopy(heightArray, 0, toSend, biomeArray.length, heightArray.length);
		try
		{
			if (!biomeMapFile.exists() || (biomeMapFile.canWrite() && biomeMapFile.canRead()))
			{
				FileUtils.writeByteArrayToFile(biomeMapFile, toSend);
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		if (flag)
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
	}

	public boolean BiomeMapOneTick()
	{
		int multifactor = biomeMapFactor / 16;
		if (multifactor < 1) multifactor = 1;
		int imagefactor = 16 / biomeMapFactor;
		if (imagefactor < 1) imagefactor = 1;
    	biomeMapOneChunk(biomeMapCx + biomeMapx0, biomeMapCz + biomeMapz0, ix, iz, biomeMapFactor);
    	biomeMapz0 += multifactor;
    	iz += imagefactor;
    	if (iz > biomeMapSizeZ - imagefactor)
    	{
        	iz = 0;
        	GCLog.debug("Finished map column " + ix + " at " + (biomeMapCx + biomeMapx0) + "," + (biomeMapCz + biomeMapz0));
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
//        IntCache.resetIntCache();
//		int[] biomesGrid = biomeMapGenLayer.getInts(x0 << 4, z0 << 4, 16, 16);
//		//For some reason getInts() doesn't work, gives a banded result where part of the array is 0
		BiomeGenBase[] biomesGrid = biomeMapWCM.getBiomeGenAt(null, x0 << 4, z0 << 4, 16, 16, false);
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
        				if (height < 63)
        					biome = 24;
        				else
        					biome = biomesGrid[xx + x + ((zz + z) << 4)].biomeID;
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
    			biomeArray[ix * biomeMapSizeZ + iz] = (byte) (cols.get(maxindex).intValue());
    			heightArray[ix * biomeMapSizeZ + iz] = (byte) (avgHeight / divisor);
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
        BiomeGenBase[] biomesGrid = biomeMapWorld.getWorldChunkManager().getBiomesForGeneration(null, cx * 4 - 2, cz * 4 - 2, 10, 10);
        func_147423_a(biomesGrid, cx * 4, 0, cz * 4);

        for (int xx = 0; xx < 4; ++xx)
        {
            int xa = xx * 5;
            int xb = (xx + 1) * 5;

            for (int zz = 0; zz < 4; ++zz)
            {
                int aa = (xa + zz) * 33;
                int ab = (xa + zz + 1) * 33;
                int ba = (xb + zz) * 33;
                int bb = (xb + zz + 1) * 33;

                for (int yy = 0; yy < 32; ++yy)
                {
                    double d0 = 0.125D;
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
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        int truey = yy * 8 + y;
                        for (int x = 0; x < 4; ++x)
                        {
                            int idx = x + xx * 4 << 4 | zz * 4;
                            double d16 = (d11 - d10) * 0.25D;
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

    static double[] field_147427_d;
    static double[] field_147428_e;
    static double[] field_147425_f;
    static double[] field_147426_g;
    private NoiseGeneratorOctaves field_147431_j;
    private NoiseGeneratorOctaves field_147432_k;
    private NoiseGeneratorOctaves field_147429_l;
    private NoiseGeneratorPerlin field_147430_m;
    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctaves noiseGen5;
    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctaves noiseGen6;
    private WorldType field_147435_p = WorldType.DEFAULT;
    
    public void initialise()
    {
        rand = new Random(this.biomeMapWorld.getSeed());
	    field_147431_j = new NoiseGeneratorOctaves(rand, 16);
	    field_147432_k = new NoiseGeneratorOctaves(rand, 16);
	    field_147429_l = new NoiseGeneratorOctaves(rand, 8);
	    field_147430_m = new NoiseGeneratorPerlin(rand, 4);
	    noiseGen5 = new NoiseGeneratorOctaves(rand, 10);
	    noiseGen6 = new NoiseGeneratorOctaves(rand, 16);
    }

    private void func_147423_a(BiomeGenBase[] biomesGrid, int p_147423_1_, int p_147423_2_, int p_147423_3_)
    {
        double d0 = 684.412D;
        double d1 = 684.412D;
        double d2 = 512.0D;
        double d3 = 512.0D;
        field_147426_g = noiseGen6.generateNoiseOctaves(field_147426_g, p_147423_1_, p_147423_3_, 5, 5, 200.0D, 200.0D, 0.5D);
        field_147427_d = field_147429_l.generateNoiseOctaves(field_147427_d, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        field_147428_e = field_147431_j.generateNoiseOctaves(field_147428_e, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        field_147425_f = field_147432_k.generateNoiseOctaves(field_147425_f, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        boolean flag1 = false;
        boolean flag = false;
        int l = 0;
        int i1 = 0;
        double d4 = 8.5D;

        for (int xx = 0; xx < 5; ++xx)
        {
            for (int zz = 0; zz < 5; ++zz)
            {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                byte b0 = 2;
                BiomeGenBase biomegenbase = biomesGrid[xx + 2 + (zz + 2) * 10];

                for (int x = -b0; x <= b0; ++x)
                {
                    for (int z = -b0; z <= b0; ++z)
                    {
                        BiomeGenBase biomegenbase1 = biomesGrid[xx + x + 2 + (zz + z + 2) * 10];
                        float f3 = biomegenbase1.rootHeight;
                        float f4 = biomegenbase1.heightVariation;

                        if (field_147435_p == WorldType.AMPLIFIED && f3 > 0.0F)
                        {
                            f3 = 1.0F + f3 * 2.0F;
                            f4 = 1.0F + f4 * 4.0F;
                        }

                        float f5 = MapUtil.parabolicField[x + 2 + (z + 2) * 5] / (f3 + 2.0F);

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
                f1 = (f1 * 4.0F - 1.0F) / 8.0F;
                double d12 = field_147426_g[i1] / 8000.0D;

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

                    double d7 = field_147428_e[l] / 512.0D;
                    double d8 = field_147425_f[l] / 512.0D;
                    double d9 = (field_147427_d[l] / 10.0D + 1.0D) / 2.0D;
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
