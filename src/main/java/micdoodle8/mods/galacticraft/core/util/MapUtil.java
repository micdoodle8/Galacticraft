package micdoodle8.mods.galacticraft.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.layer.GenLayer;

import org.apache.commons.io.FileUtils;

public class MapUtil
{
    public static AtomicBoolean calculatingMap = new AtomicBoolean();
    public static boolean doneOverworldTexture = false;
    private static int ix = 0;
    private static int iz = 0;
    private static int biomeMapx0 = 0;
    private static int biomeMapz0 = 0;
	private static int biomeMapz00;
	private static int biomeMapCx;
	private static int biomeMapCz;
	private static int biomeMapFactor;
	private static WorldChunkManager biomeMapWCM;
	private static BufferedImage biomeMapImage;
	private static File biomeMapFile;
	private static EntityPlayerMP biomeMapPlayerBase;
	private static int biomeMapSizeX;
	private static int biomeMapSizeZ;
	private static ArrayList<BlockVec3> biomeColours = new ArrayList<BlockVec3>(40);
	private static Random rand = new Random();
	private static World biomeMapWorld;
    private static final float[] parabolicField = new float[25];
    private static int[] heights = new int[256];
    private static double[] heighttemp = new double[825];
    private static GenLayer biomeMapGenLayer;
    
	static
	{
		setupColours();

        for (int j = -2; j <= 2; ++j)
        {
            for (int k = -2; k <= 2; ++k)
            {
                float f = 10.0F / MathHelper.sqrt_float((float)(j * j + k * k) + 0.2F);
                parabolicField[j + 2 + (k + 2) * 5] = f;
            }
        }
	}
	
	/**
	 * The BufferedImage needs to be already set up as a 400 x 400 image of TYPE_INT_RGB
	 */
    public static void getLocalMap(World world, int chunkXPos, int chunkZPos, BufferedImage image)
	{
        for (int x0 = -12; x0 <= 12; x0++)
        {
            for (int z0 = -12; z0 <= 12; z0++)
            {
                Chunk chunk = world.getChunkFromChunkCoords(chunkXPos + x0, chunkZPos + z0);

                if (chunk != null)
                {
                    for (int z = 0; z < 16; z++)
                    {
                        for (int x = 0; x < 16; x++)
                        {
                            int l4 = chunk.getHeight(x, z) + 1;
                            Block block = Blocks.air;
                            IBlockState i5 = null;

                            if (l4 > 1)
                            {
                                do
                                {
                                    --l4;
                                    block = chunk.getBlock(x, l4, z);
                                    i5 = chunk.getBlockState(new BlockPos(x, l4, z));
                                }
                                while (block.getMapColor(i5) == MapColor.airColor && l4 > 0);
                            }

                            int col = block.getMapColor(i5).colorValue;
                            image.setRGB(x + (x0 + 12) * 16, z + (z0 + 12) * 16, col);
                        }
                    }
                }
            }
        }
	}
    
	public static void makeOverworldTexture()
	{
    	doneOverworldTexture = true;
    	if (doneOverworldTexture) return;
		World world = GalacticraftCore.proxy.getWorldForID(0);
    	if (world == null) return;
    	if (calculatingMap.getAndSet(true)) return;
    	biomeMapWCM = world.getWorldChunkManager();
    	try {
    		Field bil = biomeMapWCM.getClass().getDeclaredField(VersionUtil.getNameDynamic(VersionUtil.KEY_FIELD_BIOMEINDEXLAYER));
    		bil.setAccessible(true);
    		biomeMapGenLayer = (GenLayer) bil.get(biomeMapWCM);
    	} catch (Exception e) { }
    	if (biomeMapGenLayer == null)
    	{
    		calculatingMap.set(false);
        	if (ConfigManagerCore.enableDebug) System.out.println("Failed to get gen layer from World Chunk Manager.");
    		return;
    	}
        File baseFolder = new File(MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation(), "galacticraft/overworldMap");
        if (!baseFolder.exists())
        {
            if (!baseFolder.mkdirs())
            {
            	GCLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
        		calculatingMap.set(false);
        		return;            	
            }
        }
//		try {
//			IResourceManager rm = Minecraft.getMinecraft().getResourceManager();			
//			BufferedImage paletteImage2 = null;
//			InputStream in = rm.getResource(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png")).getInputStream();
//			paletteImage2 = readImage(in);
//			in.close();
//			writeOutputFile(paletteImage2, false);
//			biomeMapFile.renameTo(new File("OWdiffread.jpg"));
//		} catch (Exception e) { e.printStackTrace(); }

    	if (ConfigManagerCore.enableDebug) System.out.println("Starting overworld generation centered at " + 0 + "," + 0);
    	ix = 0;
    	iz = 0;
    	biomeMapCx = 0;
    	biomeMapCz = 0;
    	biomeMapFactor = 256;
    	biomeMapSizeX = 192;
    	biomeMapSizeZ = 48;
    	biomeMapWorld = world;
    	biomeMapImage = new BufferedImage(biomeMapSizeX, biomeMapSizeZ, BufferedImage.TYPE_INT_RGB);
    	int limitX = biomeMapSizeX * biomeMapFactor / 32;
    	int limitZ = biomeMapSizeZ * biomeMapFactor / 32;
    	biomeMapz00 = -limitZ;
    	biomeMapx0 = -limitZ;
    	biomeMapz0 = biomeMapz00;
        File outputFile = new File(baseFolder, "Overworld192x48.jpg");
    	biomeMapFile = outputFile;
    	biomeMapPlayerBase = null;
	}
	
	public static void getBiomeMapForCoords(World world, int cx, int cz, int scale, int size, File outputFile, EntityPlayerMP player)
    {
    	if (calculatingMap.getAndSet(true)) return;
    	if (ConfigManagerCore.enableDebug) System.out.println("Starting map generation centered at " + cx + "," + cz);
    	ix = 0;
    	iz = 0;
    	biomeMapCx = cx;
    	biomeMapCz = cz;
    	biomeMapFactor = 1 << scale;
    	biomeMapWCM = world.getWorldChunkManager();
    	biomeMapWorld = world;
    	try {
    		Field bil = biomeMapWCM.getClass().getDeclaredField(VersionUtil.getNameDynamic(VersionUtil.KEY_FIELD_BIOMEINDEXLAYER));
    		bil.setAccessible(true);
    		biomeMapGenLayer = (GenLayer) bil.get(biomeMapWCM);
    	} catch (Exception e) { }
    	if (biomeMapGenLayer == null)
    	{
    		calculatingMap.set(false);
    		return;
    	}
    	biomeMapImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
    	int limit = size * biomeMapFactor / 32;
    	biomeMapz00 = -limit;
    	biomeMapSizeX = size;
    	biomeMapSizeZ = size;
    	biomeMapx0 = -limit;
    	biomeMapz0 = biomeMapz00;
    	biomeMapFile = outputFile;
    	biomeMapPlayerBase = player;
	}

	private static void doneBiomeMap()
	{
		if (biomeMapSizeZ == 48)
		{
			BufferedImage worldImage = new BufferedImage(biomeMapSizeX, biomeMapSizeZ, BufferedImage.TYPE_INT_RGB);

			IResourceManager rm = Minecraft.getMinecraft().getResourceManager();			
			BufferedImage paletteImage = null;
			try {
				InputStream in = rm.getResource(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png")).getInputStream();
				paletteImage = ImageIO.read(in);
				in.close();
				paletteImage.getHeight();
			} catch (Exception e) { e.printStackTrace(); calculatingMap.set(false); return;  }

			convertTo12pxTexture(worldImage, paletteImage);
			writeOutputFile(worldImage, false);
			doneOverworldTexture = true;
			calculatingMap.set(false);
			return;
		}
		writeOutputFile(biomeMapImage, true);
		calculatingMap.set(false);
	}
	

	/**
	 * Converts a 48px high image to a 12px high image with a palette chosen only from the colours in the paletteImage
	 * 
	 * @param overworldImage  Output image already created as a blank image, dimensions biomeMapSizeX x biomeMapSizeY
	 * @param paletteImage   Palette image, dimensions must be a square with sides biomeMapSizeZ / 4
	 */
	private static void convertTo12pxTexture(BufferedImage overworldImage, BufferedImage paletteImage)
	{
		TreeMap<Integer, Integer> mapColPos = new TreeMap();
		TreeMap<Integer, Integer> mapColPosB = new TreeMap();
		int count = 0;
		for (int x = 0; x < biomeMapSizeX; x+=4)
			for (int z = 0; z < biomeMapSizeZ; z+=4)
			{
				int r = 0;
				int g = 0;
				int b = 0;
				for (int xx = 0; xx < 4; xx++)
					for (int zz = 0; zz < 4; zz++)
					{
						int col = biomeMapImage.getRGB(xx + x, zz + z);
						r += (col >> 16);
						g += (col >> 8) & 255;
						b += col & 255;
					}
				while (mapColPos.containsKey(g - b)) g++;
				mapColPos.put(g - b, count);
				if (x < biomeMapSizeZ)
				{
					int col = paletteImage.getRGB(x + 1, z + 1);
					r = (col >> 16);
					g = (col >> 8) & 255;
					b = col & 255;
					while (mapColPosB.containsKey(g - b)) g++;
					mapColPosB.put(g - b, col);
				}
				count++;
			}

		count = 0;
		int newCol = 0;
		Iterator<Integer> it = mapColPosB.keySet().iterator();
		Iterator<Integer> itt = mapColPos.keySet().iterator();
		int modulus = biomeMapSizeZ / 4;
		int mod2 = biomeMapSizeX / biomeMapSizeZ;
		for (int x = 0; x < biomeMapSizeX / 4; x++)
			for (int z = 0; z < modulus; z++)
			{
				if (count % mod2 == 0) newCol = mapColPosB.get(it.next());
				int position = mapColPos.get(itt.next());
				int xx = position / modulus;
				int zz = position % modulus;
				for (int xxx = 0; xxx < 4; xxx++)
					for (int zzz = 0; zzz < 4; zzz++)
					{
						overworldImage.setRGB(xx * 4 + xxx, zz * 4 + zzz, newCol);
					}
				count++;
			}
	}

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
	
	private static void writeOutputFile(BufferedImage image, boolean flag)
	{
		try
		{
			if (!biomeMapFile.exists() || (biomeMapFile.canWrite() && biomeMapFile.canRead()))
			{
				ImageOutputStream outputStream = new FileImageOutputStream(biomeMapFile);  
				GalacticraftCore.jpgWriter.setOutput(outputStream);
				GalacticraftCore.jpgWriter.write(null, new IIOImage(image, null, null), GalacticraftCore.writeParam);
				outputStream.close();
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
				byte[] bytes = FileUtils.readFileToByteArray(biomeMapFile);
				GalacticraftCore.packetPipeline.sendToAll(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { bytes } ));//, biomeMapPlayerBase);
			}
			catch (Exception ex)
			{
				System.err.println("Error sending overworld image to player.");
				ex.printStackTrace();
			}
		}
	}

	public static void BiomeMapNextTick()
	{
		int j = 24;
        initialise(biomeMapWorld);
		while (j > 0)
		{
			if (BiomeMapOneTick())
			{
				//Finished
				doneBiomeMap();
				return;
			}
			j--;
		}
    }
	
	public static boolean BiomeMapOneTick()
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
        	if (ConfigManagerCore.enableDebug) System.out.println("Finished map column " + ix);
        	ix += imagefactor;
        	biomeMapz0 = biomeMapz00;
        	biomeMapx0 += multifactor;
        	if (biomeMapx0 > -biomeMapz00 * 4) biomeMapx0 += biomeMapz00 * 8; 
        	return ix > biomeMapSizeX - imagefactor;
    	}
    	return false;
	}
	
	private static void biomeMapOneChunk(int x0, int z0, int ix, int iz, int factor)
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
    			biomeMapImage.setRGB(ix, iz, convertBiomeColour(cols.get(maxindex), avgHeight / divisor));
    			iz++;
    		}
    		iz = izstore;
    		ix ++;
    	}	
	}
	
    public static int convertBiomeColour(int in, int height)
    {
    	int rv;
    	int s = MapUtil.biomeColours.size();
    	if (in >= 128 && in < 128 + s) in-= 128;
    	if (in >= s)
    		rv = BiomeGenBase.getBiome(in).color;
    	else
    	{
	    	BlockVec3 bv = MapUtil.biomeColours.get(in);
	    	if (bv == null)
	    		rv = BiomeGenBase.getBiome(in).color;
	    	else
	    	{
		    	if (bv.z > 0 && MapUtil.rand.nextInt(100) < bv.z)
		    		rv = bv.y;
		    	else
		    		rv = bv.x;
	    	}
    	}
    	if (height < 63) return rv;
    	float factor = (height - 68F) / 194F;
    	return ColorUtil.lighten(rv, factor);
    }
    
    private static void setupColours()
    {
    	//ocean = Ocean(0) colour(112) "Ocean"
    	MapUtil.biomeColours.add(new BlockVec3(0x497436, 0, 0));
    	//plains = Plains(1) colour(9286496) "Plains"
    	MapUtil.biomeColours.add(new BlockVec3(0x497436, 0, 0));
    	//desert = Desert(2) colour(16421912) "Desert"
    	MapUtil.biomeColours.add(new BlockVec3(0xd4cd98, Material.cactus.getMaterialMapColor().colorValue, 3));
    	//extremeHills = Hills(3, false) colour(6316128) "Extreme Hills"
    	MapUtil.biomeColours.add(new BlockVec3(0x4d654c, Material.rock.getMaterialMapColor().colorValue, 5));
    	//forest = Forest(4, 0) colour(353825) "Forest"
    	MapUtil.biomeColours.add(new BlockVec3(0x3c7521, 0x497436, 65));
    	//taiga = Taiga(5, 0) colour(747097) "Taiga"
    	MapUtil.biomeColours.add(new BlockVec3(0x627e61, 0x172a17, 18));
    	//swampland = Swamp(6) colour(522674) "Swampland"
    	MapUtil.biomeColours.add(new BlockVec3(0x33341b, 0x111309, 25));  	
    	//river = River(7) colour(255) "River"
    	MapUtil.biomeColours.add(new BlockVec3(0x497436, 0, 0));
    	MapUtil.biomeColours.add(new BlockVec3(0, 0, 0));
    	MapUtil.biomeColours.add(new BlockVec3(0, 0, 0));
    	//frozenOcean = Ocean(10) colour(9474208) "FrozenOcean"
    	MapUtil.biomeColours.add(new BlockVec3(Material.ice.getMaterialMapColor().colorValue, 0, 0));
    	//frozenRiver = River(11) colour(10526975) "FrozenRiver"
    	MapUtil.biomeColours.add(new BlockVec3(Material.ice.getMaterialMapColor().colorValue, 0, 0));
    	//icePlains = Snow(12, false) colour(16777215) "Ice Plains"
    	MapUtil.biomeColours.add(new BlockVec3(Material.snow.getMaterialMapColor().colorValue, Material.ice.getMaterialMapColor().colorValue, 5));
    	//iceMountains = Snow(13, false) colour(10526880) "Ice Mountains"
    	MapUtil.biomeColours.add(new BlockVec3(Material.snow.getMaterialMapColor().colorValue, Material.ground.getMaterialMapColor().colorValue, 10));
    	//mushroomIsland = MushroomIsland(14) colour(16711935) "MushroomIsland"
    	MapUtil.biomeColours.add(new BlockVec3(0x63565f, 0x7c1414, 10));
    	//mushroomIslandShore = MushroomIsland(15) colour(10486015) "MushroomIslandShore"
    	MapUtil.biomeColours.add(new BlockVec3(0x6a6066, 0, 0));
    	//beach = Beach(16) colour(16440917) "Beach"
    	MapUtil.biomeColours.add(new BlockVec3(Material.sand.getMaterialMapColor().colorValue, 0, 0));
    	//desertHills = Desert(17) colour(13786898) "DesertHills"
    	MapUtil.biomeColours.add(new BlockVec3(0xd4cd98, 0, 0));
    	//forestHills = Forest(18, 0) colour(2250012) "ForestHills"
    	MapUtil.biomeColours.add(new BlockVec3(0x3c7521, 0x497436, 40));
    	//taigaHills = Taiga(19, 0) colour(1456435) "TaigaHills"
    	MapUtil.biomeColours.add(new BlockVec3(0x627e61, 0x172a17, 14));
    	//extremeHillsEdge = Hills(20, true) colour(7501978) "Extreme Hills Edge"
    	MapUtil.biomeColours.add(new BlockVec3(0x4d654c, 0x497436, 50));
    	//jungle = Jungle(21, false) colour(5470985) "Jungle"
    	MapUtil.biomeColours.add(new BlockVec3(0x176c03, 0x0f4502, 25));
    	//jungleHills = Jungle(22, false) colour(2900485) "JungleHills"
    	MapUtil.biomeColours.add(new BlockVec3(0x176c03, 0x0f4502, 25));
    	//jungleEdge = Jungle(23, true) colour(6458135) "JungleEdge"
    	MapUtil.biomeColours.add(new BlockVec3(0x176c03, 0x0f4502, 25));
    	//deepOcean = Ocean(24) colour(48) "Deep Ocean"
    	MapUtil.biomeColours.add(new BlockVec3(Material.water.getMaterialMapColor().colorValue, 0, 0));
    	//stoneBeach = StoneBeach(25) colour(10658436) "Stone Beach"
    	MapUtil.biomeColours.add(new BlockVec3(Material.rock.getMaterialMapColor().colorValue, 0, 0));
    	//coldBeach = Beach(26) colour(16445632) "Cold Beach"
    	MapUtil.biomeColours.add(new BlockVec3(Material.sand.getMaterialMapColor().colorValue, Material.snow.getMaterialMapColor().colorValue, 75));
    	//birchForest = Forest(27, 2)) colour(3175492) "Birch Forest"
    	MapUtil.biomeColours.add(new BlockVec3(0x516b36, 0x497436, 65));
    	//birchForestHills = Forest(28, 2)) colour(2055986) "Birch Forest Hills"
    	MapUtil.biomeColours.add(new BlockVec3(0x516b36, 0x497436, 55));
    	//roofedForest = Forest(29, 3) colour(4215066) "Roofed Forest"
    	MapUtil.biomeColours.add(new BlockVec3(0x3e7823, 0x0e1e08, 70));
    	//coldTaiga = Taiga(30, 0) colour(3233098) "Cold Taiga"
    	MapUtil.biomeColours.add(new BlockVec3(Material.snow.getMaterialMapColor().colorValue, 0x172a17, 12));
    	//coldTaigaHills = Taiga(31, 0) colour(2375478) "Cold Taiga Hills"
    	MapUtil.biomeColours.add(new BlockVec3(Material.snow.getMaterialMapColor().colorValue, 0x172a17, 12));
    	//megaTaiga = Taiga(32, 1) colour(5858897) "Mega Taiga"
    	MapUtil.biomeColours.add(new BlockVec3(0x172a17, 0x6e4e35, 12));
    	//megaTaigaHills = Taiga(33, 1) colour(4542270) "Mega Taiga Hills"
    	MapUtil.biomeColours.add(new BlockVec3(0x172a17, 0x6e4e35, 12));
    	//extremeHillsPlus = Hills(34, true) colour(5271632) "Extreme Hills+"
    	MapUtil.biomeColours.add(new BlockVec3(0x7b7978, 0x497436, 10));
    	//savanna = Savanna(35) colour(12431967) "Savanna"
    	MapUtil.biomeColours.add(new BlockVec3(0x565529, 0x262304, 20));   	
    	//savannaPlateau = Savanna(36) colour(10984804) "Savanna Plateau"
    	MapUtil.biomeColours.add(new BlockVec3(0x565529, 0x262304, 14));   	
    	//mesa = Mesa(37, false, false) colour(14238997) "Mesa"
    	MapUtil.biomeColours.add(new BlockVec3(0xa0521f, 0x712f23, 14));
    	//mesaPlateau_F = Mesa(38, false, true) colour(11573093) "Mesa Plateau F"
    	MapUtil.biomeColours.add(new BlockVec3(0xa0521f, 0x712f23, 17));
    	//mesaPlateau = Mesa(39, false, false) colour(13274213) "Mesa Plateau"
    	MapUtil.biomeColours.add(new BlockVec3(0xa0521f, 0x712f23, 20));
    }
 
    public static void getHeightMap(int cx, int cz)
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
    private static NoiseGeneratorOctaves field_147431_j;
    private static NoiseGeneratorOctaves field_147432_k;
    private static NoiseGeneratorOctaves field_147429_l;
    private static NoiseGeneratorPerlin field_147430_m;
    /** A NoiseGeneratorOctaves used in generating terrain */
    public static NoiseGeneratorOctaves noiseGen5;
    /** A NoiseGeneratorOctaves used in generating terrain */
    public static NoiseGeneratorOctaves noiseGen6;
    private static WorldType field_147435_p = WorldType.DEFAULT;
    
    private static void initialise(World world)
    {
        rand = new Random(world.getSeed());
	    field_147431_j = new NoiseGeneratorOctaves(rand, 16);
	    field_147432_k = new NoiseGeneratorOctaves(rand, 16);
	    field_147429_l = new NoiseGeneratorOctaves(rand, 8);
	    field_147430_m = new NoiseGeneratorPerlin(rand, 4);
	    noiseGen5 = new NoiseGeneratorOctaves(rand, 10);
	    noiseGen6 = new NoiseGeneratorOctaves(rand, 16);
    }

    private static void func_147423_a(BiomeGenBase[] biomesGrid, int p_147423_1_, int p_147423_2_, int p_147423_3_)
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
                        float f3 = biomegenbase1.minHeight;
                        float f4 = biomegenbase1.maxHeight;

                        if (field_147435_p == WorldType.AMPLIFIED && f3 > 0.0F)
                        {
                            f3 = 1.0F + f3 * 2.0F;
                            f4 = 1.0F + f4 * 4.0F;
                        }

                        float f5 = parabolicField[x + 2 + (z + 2) * 5] / (f3 + 2.0F);

                        if (biomegenbase1.minHeight > biomegenbase.minHeight)
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
