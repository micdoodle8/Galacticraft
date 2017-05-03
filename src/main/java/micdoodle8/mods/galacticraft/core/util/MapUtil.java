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

import org.apache.commons.io.FileUtils;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.DynamicTextureProper;
import micdoodle8.mods.galacticraft.core.client.gui.screen.DrawGameScreen;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
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

public class MapUtil
{
    public static AtomicBoolean calculatingMap = new AtomicBoolean();
    public static AtomicBoolean resetClientFlag = new AtomicBoolean();
    public static boolean doneOverworldTexture = false;
	public static ArrayList<BlockVec3> biomeColours = new ArrayList<BlockVec3>(40);
    public static final float[] parabolicField = new float[25];
    private static MapGen currentMap = null;
    private static MapGen slowMap = null;
    private static Random rand = new Random();
//    public static int WORLD_BORDER = 14992;
    private static final int SIZE_STD = 176;
    public static final int SIZE_STD2 = SIZE_STD * 2;
    private static LinkedList<MapGen> queuedMaps = new LinkedList();
    public static LinkedList<String> clientRequests = new LinkedList();
 
    
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
	
	public static void reset()
	{
		currentMap = null;
		slowMap = null;
		queuedMaps.clear();
		calculatingMap.set(false);
		doneOverworldTexture = false;
	}
	
    @SideOnly(Side.CLIENT)
	public static void resetClient()
	{
    	resetClientFlag.set(true);
    	//Can be called from a network thread
	}
    
    @SideOnly(Side.CLIENT)
    public static void resetClientBody()
    {
		ClientProxyCore.overworldTextureRequestSent = false;
		ClientProxyCore.overworldTexturesValid = false;
		clientRequests.clear();
        File baseFolder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/temp");
        if (baseFolder.exists() && baseFolder.isDirectory())
        {
        	for (File f : baseFolder.listFiles())
        	     if (f.isFile()) f.delete();        	
        }
		GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, new Object[] {}));
		DrawGameScreen.reusableMap = new DynamicTexture(MapUtil.SIZE_STD2, MapUtil.SIZE_STD2);
		MapUtil.biomeColours.clear();
		setupColours();
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
        if (!baseFolder.exists() && !baseFolder.mkdirs())
        {
        	GCLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
    		doneOverworldTexture = true;
        	return;            	
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

        if (MapUtil.getBiomeMapForCoords(world, 0, 0, 7, 192, 48, baseFolder))
        	doneOverworldTexture = true;
        
        //TODO: allow save and resume of partially generated map 
        //MapUtil.getBiomeMapForCoords(world, 0, 0, 4, 1536, 384, baseFolder);
	}
	
	public static void sendOverworldToClient(EntityPlayerMP client)
	{
		if (doneOverworldTexture)
		{
			try
			{
		    	File baseFolder = new File(MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation(), "galacticraft/overworldMap");
		        if (!baseFolder.exists())
		        {
		        	GCLog.severe("Base folder missing: " + baseFolder.getAbsolutePath());
		        	return;            	
		        }
				File file = new File(baseFolder, "Overworld192.bin");
		        if (file.exists())
		        {
					GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { 0, 0, FileUtils.readFileToByteArray(file) } ), client);
		        }
		        file = new File(baseFolder, "Overworld1536.bin");
		        if (file.exists())
		        {
					GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { 0, 0, FileUtils.readFileToByteArray(file) } ), client);
		        }
			}
			catch (Exception ex)
			{
				System.err.println("Error sending overworld image to player.");
				ex.printStackTrace();
			}	
		}
	}

	public static void sendOrCreateMap(World world, int cx, int cz, EntityPlayerMP client)
	{
		try
		{
	    	File baseFolder = new File(MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation(), "galacticraft/overworldMap");
	        if (!baseFolder.exists())
	        {
	        	GCLog.severe("Base folder missing: " + baseFolder.getAbsolutePath());
	        	return;            	
	        }
			File file = getFile(baseFolder, cx, cz);
	        if (!file.exists())
	        {
	        	getBiomeMapForCoords(world, cx, cz, 1, SIZE_STD, SIZE_STD, baseFolder);
	        	return;            	
	        }
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { cx, cz, FileUtils.readFileToByteArray(file) } ), client);
		}
		catch (Exception ex)
		{
			System.err.println("Error sending map image to player.");
			ex.printStackTrace();
		}		
	}
	
	public static boolean buildMaps(World world, int x, int z)
	{
    	File baseFolder = new File(MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation(), "galacticraft/overworldMap");
        if (!baseFolder.exists() && !baseFolder.mkdirs())
        {
        	GCLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
    		return false;            	
        }
        int cx = convertMap(x);
        int cz = convertMap(z);
		getBiomeMapForCoords(world, cx, cz, 1, SIZE_STD, SIZE_STD, baseFolder);
		getBiomeMapForCoords(world, cx + SIZE_STD2, cz, 1, SIZE_STD, SIZE_STD, baseFolder);
		getBiomeMapForCoords(world, cx, cz + SIZE_STD2, 1, SIZE_STD, SIZE_STD, baseFolder);
		getBiomeMapForCoords(world, cx - SIZE_STD2, cz, 1, SIZE_STD, SIZE_STD, baseFolder);
		getBiomeMapForCoords(world, cx, cz - SIZE_STD2, 1, SIZE_STD, SIZE_STD, baseFolder);
		getBiomeMapForCoords(world, cx + SIZE_STD2, cz + SIZE_STD2, 1, SIZE_STD, SIZE_STD, baseFolder);
		getBiomeMapForCoords(world, cx - SIZE_STD2, cz + SIZE_STD2, 1, SIZE_STD, SIZE_STD, baseFolder);
		getBiomeMapForCoords(world, cx - SIZE_STD2, cz - SIZE_STD2, 1, SIZE_STD, SIZE_STD, baseFolder);
		getBiomeMapForCoords(world, cx + SIZE_STD2, cz - SIZE_STD2, 1, SIZE_STD, SIZE_STD, baseFolder);
		return true;
	}
	
	private static int convertMap(int x)
	{
        int cx = x + SIZE_STD;
        if (cx < 0) cx-= SIZE_STD2 - 1;
    	cx /= SIZE_STD2;
        return cx * SIZE_STD2;
	}
	
	public static boolean getBiomeMapForCoords(World world, int cx, int cz, int scale, int sizeX, int sizeZ, File baseFolder)
    {
        File outputFile;
        if (sizeX != sizeZ)
        {
        	outputFile = new File(baseFolder, "Overworld" + sizeX + ".bin");
        	if (sizeX == 1536)
        	{
//        		MapGen newGen = new MapGen(world, sizeX, sizeZ, cx, cz, 1 << scale, outputFile);
//            	if (newGen.calculatingMap)
//            	{
//            		slowMap = newGen;
//            		calculatingMap.set(true);
//            	}
            	return false;
        	}
        }
        else
        	outputFile = getFile(baseFolder, cx, cz);
		
		MapGen newGen = new MapGen(world, sizeX, sizeZ, cx, cz, 1 << scale, outputFile);
    	if (newGen.calculatingMap)
    	{
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
		MapGen map;
		boolean doingSlow = false;
		if (currentMap != null)
			map = currentMap;
		else if (slowMap != null)
		{
			map = slowMap;
			doingSlow = true;
		}
		else
			return;

		//Allow GC background mapping around 9% of the server tick time if server running at full speed
		//(on a slow server, it will be proportionately lower %)
		long end = System.nanoTime() + 4500000L;
		while (System.nanoTime() < end)
		{
			if (map.BiomeMapOneTick())
			{
				//Finished
				map.writeOutputFile(true);
				if (map.biomeMapFile.getName().equals("Overworld192.bin"))
					doneOverworldTexture = true;
				if (doingSlow)
				{
					slowMap = null;
				}
				else
				{
					currentMap = null;
					if (queuedMaps.size() > 0)
						currentMap = queuedMaps.removeFirst();
				}
				if (currentMap == null && slowMap == null)
					calculatingMap.set(false);
				return;
			}
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

    @SideOnly(Side.CLIENT)
	public static void writeImgToFile(BufferedImage img, String name)
	{
		if (GalacticraftCore.enableJPEG)
		{
			File folder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/temp");
	
	        try {
			ImageOutputStream outputStreamA = new FileImageOutputStream(new File(folder, name));  
			GalacticraftCore.jpgWriter.setOutput(outputStreamA);
			GalacticraftCore.jpgWriter.write(null, new IIOImage(img, null, null), GalacticraftCore.writeParam);
			outputStreamA.close();
	        }
	        catch (Exception e) { }
		}
	}

    @SideOnly(Side.CLIENT)
	public static void getOverworldImageFromRaw(File folder, int cx, int cz, byte[] raw) throws IOException
	{
		if (raw.length == 18432 * 64)
		{
            File file0 = new File(folder, "overworldRaw.bin");

            if (!file0.exists() || (file0.canRead() && file0.canWrite()))
            {
                FileUtils.writeByteArrayToFile(file0, raw);
            }
            else
            {
                System.err.println("Cannot read/write to file %minecraftDir%/assets/temp/overworldRaw.bin");
            }

			//raw is a 1536 x 384 byte array of biome types followed by heights
			BufferedImage worldImageLarge = new BufferedImage(384 * 8, 96 * 8, BufferedImage.TYPE_INT_RGB);
			ArrayList<Integer> cols = new ArrayList<Integer>();
			int lastcol = -1;
			int idx = 0;
			for (int x = 0; x < 1536; x++)
			{
				for (int z = 0; z < 384; z++)
				{
					int arrayIndex = (x * 384 + z) * 2;
					int biome = ((int) raw[arrayIndex]) & 255;
					int height = ((int) raw[arrayIndex + 1]) & 255;

					if (height < 63 && biome != 10)
						biome = 0;
					if (height < 56 && biome == 0)
						biome = 24;

					worldImageLarge.setRGB(x * 2, z * 2, convertBiomeColour(biome, height));
					worldImageLarge.setRGB(x * 2, z * 2 + 1, convertBiomeColour(biome, height));
					worldImageLarge.setRGB(x * 2 + 1, z * 2, convertBiomeColour(biome, height));
					worldImageLarge.setRGB(x * 2 + 1, z * 2 + 1, convertBiomeColour(biome, height));
				}
			}

			if (ClientProxyCore.overworldTextureLarge == null)
				ClientProxyCore.overworldTextureLarge = new DynamicTextureProper(768,192);
			ClientProxyCore.overworldTextureLarge.update(worldImageLarge);

			if (GalacticraftCore.enableJPEG)
			{
				ImageOutputStream outputStream = new FileImageOutputStream(new File(folder, "large.jpg"));  
				GalacticraftCore.jpgWriter.setOutput(outputStream);
				GalacticraftCore.jpgWriter.write(null, new IIOImage(worldImageLarge, null, null), GalacticraftCore.writeParam);
				outputStream.close();
			}
		}
		else if (raw.length == 18432)
		{
			//raw is a 192 x 48 byte array of biome types followed by heights
			BufferedImage worldImage = new BufferedImage(192, 48, BufferedImage.TYPE_INT_RGB);
			ArrayList<Integer> cols = new ArrayList<Integer>();
			int lastcol = -1;
			int idx = 0;
			for (int x = 0; x < 192; x++)
			{
				for (int z = 0; z < 48; z++)
				{
					int arrayIndex = (x * 48 + z) * 2;
					int biome = ((int) raw[arrayIndex]) & 255;
					int height = ((int) raw[arrayIndex + 1]) & 255;

					if (height < 63 && biome != 10)
						biome = 0;
					if (height < 56 && biome == 0)
						biome = 24;

					worldImage.setRGB(x, z, convertBiomeColour(biome, height));
				}
			}

			IResourceManager rm = Minecraft.getMinecraft().getResourceManager();			
			BufferedImage paletteImage = null;
			try {
				InputStream in = rm.getResource(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png")).getInputStream();
				paletteImage = ImageIO.read(in);
				in.close();
				paletteImage.getHeight();
			} catch (Exception e) { e.printStackTrace(); return;  }

			BufferedImage result = convertTo12pxTexture(worldImage, paletteImage);

			if (result != null)
			{
				if (ClientProxyCore.overworldTextureWide == null)
					ClientProxyCore.overworldTextureWide = new DynamicTextureProper(192,48);
				if (ClientProxyCore.overworldTextureClient == null)
					ClientProxyCore.overworldTextureClient = new DynamicTextureProper(48,48);
				ClientProxyCore.overworldTextureWide.update(result);
				ClientProxyCore.overworldTextureClient.update(result);
				ClientProxyCore.overworldTexturesValid = true;
			}
		}
		else
		{
            File file0 = getFile(folder, cx, cz);

            if (!file0.exists() || (file0.canRead() && file0.canWrite()))
            {
                FileUtils.writeByteArrayToFile(file0, raw);
            }
		}
	}
	
    @SideOnly(Side.CLIENT)
	public static boolean getMap(int[] image, World world, int xCoord, int zCoord)
	{
        int cx = convertMap(xCoord);
        int cz = convertMap(zCoord);

        File baseFolder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/temp");
        if (!baseFolder.exists() && !baseFolder.mkdirs())
        {
        	GCLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
    		return false;            	
        }

        int dim = world.provider.dimensionId;
        boolean result = true;
        if (makeRGBimage(image, baseFolder, cx - SIZE_STD2, cz - SIZE_STD2, 0, 0, xCoord, zCoord, dim, result)) result = false;
        if (makeRGBimage(image, baseFolder, cx - SIZE_STD2, cz, 0, SIZE_STD, xCoord, zCoord, dim, result)) result = false;
        if (makeRGBimage(image, baseFolder, cx - SIZE_STD2, cz + SIZE_STD2, 0, SIZE_STD2, xCoord, zCoord, dim, result)) result = false;
        if (makeRGBimage(image, baseFolder, cx, cz - SIZE_STD2, SIZE_STD, 0, xCoord, zCoord, dim, result)) result = false;
        if (makeRGBimage(image, baseFolder, cx, cz, SIZE_STD, SIZE_STD, xCoord, zCoord, dim, result)) result = false;
        if (makeRGBimage(image, baseFolder, cx, cz + SIZE_STD2, SIZE_STD, SIZE_STD2, xCoord, zCoord, dim, result)) result = false;
        if (makeRGBimage(image, baseFolder, cx + SIZE_STD2, cz - SIZE_STD2, SIZE_STD2, 0, xCoord, zCoord, dim, result)) result = false;
        if (makeRGBimage(image, baseFolder, cx + SIZE_STD2, cz, SIZE_STD2, SIZE_STD, xCoord, zCoord, dim, result)) result = false;
        if (makeRGBimage(image, baseFolder, cx + SIZE_STD2, cz + SIZE_STD2, SIZE_STD2, SIZE_STD2, xCoord, zCoord, dim, result)) result = false;
        return result;
	}

    private static boolean makeRGBimage(int[] array, File baseFolder, int cx, int cz, int offsetX, int offsetZ, int xCoord, int zCoord, int dim, boolean prevResult)
    {
        File filename = getFile(baseFolder, cx, cz);
        if (!filename.exists())
        {
        	if (clientRequests.contains(filename.getName()))
        		GCLog.debug("Still waiting for file " + filename.getName());
        	else
        	{
        		clientRequests.add(filename.getName());
        		GCLog.debug("Client requested file" + filename.getName());
        		GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_MAP_IMAGE, new Object[] {dim, cx, cz}));
        	}
        	return true;
        }
        
        if (!prevResult) return true;

        int ox = (convertMap(xCoord) - xCoord - SIZE_STD) / 2;
        int oz = (convertMap(zCoord) - zCoord - SIZE_STD) / 2;
        
        byte[] raw = null;
        try {
        	raw = FileUtils.readFileToByteArray(filename);
		} catch (IOException e)
		{
        	GCLog.severe("Problem reading map file: " + baseFolder.getAbsolutePath() + filename.getName());
        	return true;
		}
        if (raw == null || raw.length != SIZE_STD * SIZE_STD * 2)
        {
        	GCLog.debug("map size is " + raw.length);
        	return true;
        }

        int xstart = Math.max(0, -offsetX -ox);
        int zstart = Math.max(0, -offsetZ -oz);
    	for (int x = xstart; x < SIZE_STD; x++)
    	{
    		int imagex = x + offsetX + ox; 
    		if (imagex >= SIZE_STD2) break;
    		for (int z = zstart; z < SIZE_STD; z++)
    		{
        		int imageZ = z + oz + offsetZ;// + SIZE_STD - 1 - z;
        		if (imageZ >= SIZE_STD2) break;

    			int arrayIndex = (x * SIZE_STD + z) * 2;
    			int biome = ((int) raw[arrayIndex]) & 255;
    			int height = ((int) raw[arrayIndex + 1]) & 255;

    			if (height < 63 && biome != 10)
    				biome = 0;
    			if (height < 56 && biome == 0)
    				biome = 24;

    			if (imagex < 0 || imageZ < 0)
    				GCLog.debug("Outside image " + imagex + "," + imageZ + " - " + "x="+x + " z=" + z + " offsetX=" + offsetX + " offsetZ = " + offsetZ + " ox=" + ox + " oz=" + oz);
    			else
    				array[imagex + SIZE_STD2 * imageZ] = convertBiomeColour(biome, height) + 0xff000000;
    		}
    	}
    	return false;
    }

    
	private static File getFile(File folder, int cx, int cz)
	{
		return new File(folder, "overworld" + cx / SIZE_STD2 + "_" + cz / SIZE_STD2 + ".bin");
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
    	if (rv == 0x9c2424 && MapUtil.rand.nextInt(2) == 0)
    		rv = 0xbfa384;
    	if (height < 63) return rv;
    	if (height > 92 && (in == 3 || in == 20 || in == 31 || in == 33 || in == 34))
    	{
    		if (MapUtil.rand.nextInt(8) > 98 - height)
    			rv = Material.snow.getMaterialMapColor().colorValue;
    	}
    	float factor = (height - 68F) / 114F;
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
    	MapUtil.biomeColours.add(new BlockVec3(0x4d654c, Material.rock.getMaterialMapColor().colorValue, 15));
    	//forest = Forest(4, 0) colour(353825) "Forest"
    	MapUtil.biomeColours.add(new BlockVec3(0x3c7521, 0x295416, 45));
    	//taiga = Taiga(5, 0) colour(747097) "Taiga"
    	MapUtil.biomeColours.add(new BlockVec3(0x627e61, 0x172a17, 18));
    	//swampland = Swamp(6) colour(522674) "Swampland"
    	MapUtil.biomeColours.add(new BlockVec3(0x43541b, 0x111309, 25));  	
    	//river = River(7) colour(255) "River"
    	MapUtil.biomeColours.add(new BlockVec3(0x497436, 0, 0));
    	MapUtil.biomeColours.add(new BlockVec3(0, 0, 0));
    	MapUtil.biomeColours.add(new BlockVec3(0, 0, 0));
    	//frozenOcean = Ocean(10) colour(9474208) "FrozenOcean"
    	MapUtil.biomeColours.add(new BlockVec3(Material.ice.getMaterialMapColor().colorValue, 0, 0));
    	//frozenRiver = River(11) colour(10526975) "FrozenRiver"
    	MapUtil.biomeColours.add(new BlockVec3(Material.ice.getMaterialMapColor().colorValue, 0, 0));
    	//icePlains = Snow(12, false) colour(16777215) "Ice Plains"
    	MapUtil.biomeColours.add(new BlockVec3(Material.snow.getMaterialMapColor().colorValue, 0x497436, 3));
    	//iceMountains = Snow(13, false) colour(10526880) "Ice Mountains"
    	MapUtil.biomeColours.add(new BlockVec3(Material.snow.getMaterialMapColor().colorValue, Material.ice.getMaterialMapColor().colorValue, 5));
    	//mushroomIsland = MushroomIsland(14) colour(16711935) "MushroomIsland"
    	MapUtil.biomeColours.add(new BlockVec3(0x63565f, 0x7c1414, 10));
    	//mushroomIslandShore = MushroomIsland(15) colour(10486015) "MushroomIslandShore"
    	MapUtil.biomeColours.add(new BlockVec3(0x6a6066, 0, 0));
    	//beach = Beach(16) colour(16440917) "Beach"
    	MapUtil.biomeColours.add(new BlockVec3(Material.sand.getMaterialMapColor().colorValue, 0, 0));
    	//desertHills = Desert(17) colour(13786898) "DesertHills"
    	MapUtil.biomeColours.add(new BlockVec3(0xd4cd98, 0, 0));
    	//forestHills = Forest(18, 0) colour(2250012) "ForestHills"
    	MapUtil.biomeColours.add(new BlockVec3(0x3c7521, 0x295416, 35));
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
    	MapUtil.biomeColours.add(new BlockVec3(0x9c2424, 0x1e2e18, 98));
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
    
    public static void makeVanillaMap(int dim, int chunkXPos, int chunkZPos, File baseFolder, BufferedImage image)
    {
        for (int x0 = -12; x0 <= 12; x0++)
        {
            for (int z0 = -12; z0 <= 12; z0++)
            {
                Chunk chunk = MinecraftServer.getServer().worldServerForDimension(dim).getChunkFromChunkCoords(chunkXPos + x0, chunkZPos + z0);

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

        try
        {
            File outputFile = new File(baseFolder, dim + "_" + chunkXPos + "_" + chunkZPos + ".jpg");

            if (!outputFile.exists() || (outputFile.canWrite() && outputFile.canRead()))
            {
                ImageIO.write(image, "jpg", outputFile);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
