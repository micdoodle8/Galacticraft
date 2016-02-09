package micdoodle8.mods.galacticraft.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import cpw.mods.fml.client.FMLClientHandler;

public class MapUtil
{
    public static AtomicBoolean calculatingMap = new AtomicBoolean();
    public static boolean doneOverworldTexture = false;
	public static ArrayList<BlockVec3> biomeColours = new ArrayList<BlockVec3>(40);
    public static final float[] parabolicField = new float[25];
    private static MapGen currentMap;
    private static Random rand = new Random();
//    public static int WORLD_BORDER = 14992;
    private static LinkedList<MapGen> queuedMaps = new LinkedList();
    
	static
	{
		//TODO: Deal with mods like ExtraBiomes
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
                            int l4 = chunk.getHeightValue(x, z) + 1;
                            Block block = Blocks.air;
                            int i5 = 0;

                            if (l4 > 1)
                            {
                                do
                                {
                                    --l4;
                                    block = chunk.getBlock(x, l4, z);
                                    i5 = chunk.getBlockMetadata(x, l4, z);
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
    	//doneOverworldTexture = true;
    	if (doneOverworldTexture) return;
		World world = WorldUtil.getProviderForDimensionServer(0).worldObj;
    	if (world == null) return;

    	File baseFolder = new File(MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation(), "galacticraft/overworldMap");
        if (!baseFolder.exists())
        {
            if (!baseFolder.mkdirs())
            {
            	GCLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
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

        File outputFile = new File(baseFolder, "Overworld192x48.bin");
        if (MapUtil.getBiomeMapForCoords(world, 0, 0, 6, 384, 96, outputFile, null))
        	doneOverworldTexture = true;
	}
	
	public static boolean getBiomeMapForCoords(World world, int cx, int cz, int scale, int sizeX, int sizeZ, File outputFile, EntityPlayerMP player)
    {
    	MapGen newGen = new MapGen(world, sizeX, sizeZ, cx, cz, 1 << scale, outputFile);
    	if (newGen.calculatingMap)
    	{
	    	newGen.biomeMapPlayerBase = player;
	    	if (calculatingMap.getAndSet(true))
	    		queuedMaps.add(newGen);
	    	else
	    		currentMap = newGen;
	    	return false;
    	}
    	return true;
   	}

	public static void BiomeMapNextTick()
	{
		if (currentMap == null)
			return;
		
		int j = 48;
		while (j > 0)
		{
			if (currentMap.BiomeMapOneTick())
			{
				//Finished
				currentMap.writeOutputFile(true);
				if (currentMap.biomeMapFile.getName().equals("Overworld192x48.bin"))
					doneOverworldTexture = true;
				currentMap = null;
				if (queuedMaps.size() > 0)
					currentMap = queuedMaps.removeFirst();
				else
					calculatingMap.set(false);
				return;
			}
			j--;
		}
    }
	
	/**
	 * Converts a 48px high image to a 12px high image with a palette chosen only from the colours in the paletteImage
	 * 
	 * @param overworldImage  Output image already created as a blank image, dimensions biomeMapSizeX x biomeMapSizeY
	 * @param paletteImage   Palette image, dimensions must be a square with sides biomeMapSizeZ / 4
	 */
	public static BufferedImage convertTo12pxTexture(BufferedImage overworldImage, BufferedImage paletteImage)
	{
		BufferedImage result = new BufferedImage(overworldImage.getWidth(), overworldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		TreeMap<Integer, Integer> mapColPos = new TreeMap();
		TreeMap<Integer, Integer> mapColPosB = new TreeMap();
		int count = 0;
		for (int x = 0; x < overworldImage.getWidth(); x+=4)
			for (int z = 0; z < overworldImage.getHeight(); z+=4)
			{
				int r = 0;
				int g = 0;
				int b = 0;
				for (int xx = 0; xx < 4; xx++)
					for (int zz = 0; zz < 4; zz++)
					{
						int col = overworldImage.getRGB(xx + x, zz + z);
						r += (col >> 16);
						g += (col >> 8) & 255;
						b += col & 255;
					}
				while (mapColPos.containsKey(g - b)) g++;
				mapColPos.put(g - b, count);
				if (x < overworldImage.getHeight())
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
		int modulus = overworldImage.getHeight() / 4;
		int mod2 = overworldImage.getWidth() / overworldImage.getHeight();
		for (int x = 0; x < overworldImage.getWidth() / 4; x++)
			for (int z = 0; z < modulus; z++)
			{
				if (count % mod2 == 0) newCol = mapColPosB.get(it.next());
				int position = mapColPos.get(itt.next());
				int xx = position / modulus;
				int zz = position % modulus;
				for (int xxx = 0; xxx < 4; xxx++)
					for (int zzz = 0; zzz < 4; zzz++)
					{
						result.setRGB(xx * 4 + xxx, zz * 4 + zzz, newCol);
					}
				count++;
			}
		
		return result;
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

    //This runs on the client
	public static void getOverworldImageFromRaw(byte[] raw) throws IOException
	{
		if (raw.length == 18432 * 4)
		{
			//raw is a 192 x 48 byte array of biome types, followed by a 192 x 48 byte array of heights
			File folder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/temp");
			File fileImg = new File(folder, "overworldLocal.jpg");
			if (fileImg.exists()) fileImg.delete();

			BufferedImage worldImage = new BufferedImage(192, 48, BufferedImage.TYPE_INT_RGB);
			ArrayList<Integer> cols = new ArrayList<Integer>();
			int lastcol = -1;
			int idx = 0;
			for (int x = 0; x < 192; x++)
			{
				for (int z = 0; z < 48; z++)
				{
					int arrayIndex = (x * 96 + z) * 4;
					int biome = ((int) raw[arrayIndex]) & 255;
					int height = ((int) raw[arrayIndex + 1]) & 255;

					if (height < 63 && biome != 10)
						biome = 0;
					if (height < 56 && biome == 0)
						biome = 24;

					worldImage.setRGB(x, z, convertBiomeColour(biome, height));
				}
			}
			
			//Temporary to check everything is working
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

			BufferedImage result = convertTo12pxTexture(worldImage, paletteImage);
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
    	MapUtil.biomeColours.add(new BlockVec3(Material.water.getMaterialMapColor().colorValue, 0, 0));
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
    	MapUtil.biomeColours.add(new BlockVec3(0x2f2fd4, 0, 0));
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
}
