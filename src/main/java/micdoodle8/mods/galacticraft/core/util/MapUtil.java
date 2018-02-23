package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.DynamicTextureProper;
import micdoodle8.mods.galacticraft.core.client.screen.DrawGameScreen;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.io.FileUtils;

import javax.imageio.*;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class MapUtil
{
	//Mapgen management
    public static AtomicBoolean calculatingMap = new AtomicBoolean();
    public static AtomicBoolean resetClientFlag = new AtomicBoolean();
    private static MapGen currentMap = null;
    private static MapGen slowMap = null;
    private static Thread threadCurrentMap = null;
    private static Thread threadSlowMap = null;
    public static boolean doneOverworldTexture = false;
    private static LinkedList<MapGen> queuedMaps = new LinkedList<>();
    public static LinkedList<String> clientRequests = new LinkedList<>();

    public static ArrayList<BlockVec3> biomeColours = new ArrayList<BlockVec3>(40);
    private static Random rand = new Random();
	private static byte[] overworldImageBytesPart; //Used client side only
	private static byte[] overworldImageCompressed = null;
    
    //Map size definitions
    private static final int SIZE_STD = 176;
    public static final int SIZE_STD2 = SIZE_STD * 2;
    public static final int OVERWORLD_LARGEMAP_WIDTH = 1536;   //Do not make a large map whose raw binary exceeds 2MB otherwise sendMapPacket() will not send it.  This raw binary is 576kB
    private static final int OVERWORLD_LARGEMAP_HEIGHT = 960;
    private static final int OVERWORLD_MAP_SCALE = 4;  //Recommended is 4.  This gives a large overworld map of size (1536 x 16) by (960 x 16): that extends 12000 blocks from spawn in both EW directions and 7600 blocks from spawn north and south
    private static final int OVERWORLD_TEXTURE_WIDTH = 192;   //Do not change - planet texture needs to be this size
    private static final int OVERWORLD_TEXTURE_HEIGHT = 48;   //Do not change - planet texture needs to be this size
    private static final int OVERWORLD_TEXTURE_SCALE = 7;
    
    private static final int LARGEMAP_MARKER = 30000001;   //This is a marker to flag world map packets, it must be an impossible cx coordinate

    //Color related constants
	private static final int OCEAN_HEIGHT = 63;
	private static final int DEEP_OCEAN = 56;
    

    static
    {
        //TODO: Deal with mods like ExtraBiomes
        setupColours();
    }

    public static void reset()
    {
        if (currentMap != null) currentMap.abort();
        currentMap = null;
        saveMapProgress();
        threadCurrentMap = null;
        threadSlowMap = null;
        queuedMaps.clear();
        calculatingMap.set(false);
        doneOverworldTexture = false;
        overworldImageCompressed = null;
    }

    @SideOnly(Side.CLIENT)
    public static void resetClient()
    {
        resetClientFlag.set(true);
        //Threadsafe
    }

    @SideOnly(Side.CLIENT)
    public static void resetClientBody()
    {
        ClientProxyCore.overworldTexturesValid = false;
        clientRequests.clear();
        overworldImageBytesPart = null;
        File baseFolder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/galacticraftMaps");
        if (baseFolder.exists() && baseFolder.isDirectory())
        {
            for (File f : baseFolder.listFiles())
            {
                if (f.isFile())
                {
                    f.delete();
                }
            }
        }
        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, GCCoreUtil.getDimensionID(FMLClientHandler.instance().getClient().theWorld), new Object[] {}));
        ClientProxyCore.overworldTextureRequestSent = true;
		DrawGameScreen.reusableMap = new DynamicTexture(MapUtil.SIZE_STD2, MapUtil.SIZE_STD2);
		MapUtil.biomeColours.clear();
		setupColours();
    }

    /**
     * The BufferedImage needs to be already set up as a sized image of TYPE_INT_RGB
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
                            int l4 = chunk.getHeight(new BlockPos(x, 0, z)) + 1;
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
        if (doneOverworldTexture)
        {
            return;
        }
        World overworld = WorldUtil.getProviderForDimensionServer(ConfigManagerCore.idDimensionOverworld).worldObj;
        if (overworld == null)
        {
            return;
        }
        
        if (overworld.getWorldType() == WorldType.FLAT  || !(overworld.provider instanceof WorldProviderSurface))
        {
            doneOverworldTexture = true;
            return;
        }

        File baseFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), "galacticraft/overworldMap");
        if (!baseFolder.exists() && !baseFolder.mkdirs())
        {
            GCLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
            doneOverworldTexture = true;
            return;
        }

        if (MapUtil.getBiomeMapForCoords(overworld, 0, 0, OVERWORLD_TEXTURE_SCALE, OVERWORLD_TEXTURE_WIDTH, OVERWORLD_TEXTURE_HEIGHT, baseFolder))
        {
            doneOverworldTexture = true;
        }

        //This will make the 'slow map', a map covering a large part of the world around spawn
        //(On a typical modern PC, this should take 20-30 minutes to generate in its own thread)
        MapUtil.getBiomeMapForCoords(overworld, 0, 0, OVERWORLD_MAP_SCALE, OVERWORLD_LARGEMAP_WIDTH, OVERWORLD_LARGEMAP_HEIGHT, baseFolder);
    }

    public static void sendOverworldToClient(EntityPlayerMP client)
    {
        if (doneOverworldTexture)
        {
            try
            {
                File baseFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), "galacticraft/overworldMap");
                if (!baseFolder.exists())
                {
                    GCLog.severe("Base folder missing: " + baseFolder.getAbsolutePath());
                    return;
                }
                File file = new File(baseFolder, "Overworld" + OVERWORLD_TEXTURE_WIDTH + ".bin");
                if (file.exists())
                {
                    sendMapPacket(0, 0, client, FileUtils.readFileToByteArray(file));
                }
                file = new File(baseFolder, "Overworld" + OVERWORLD_LARGEMAP_WIDTH + ".bin");
                if (file.exists())
                {
                    sendMapPacket(LARGEMAP_MARKER, 0, client, FileUtils.readFileToByteArray(file));
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
        
        if (world.getWorldType() == WorldType.FLAT  || !(world.provider instanceof WorldProviderSurface))
        {
            doneOverworldTexture = true;
            return;
        }
        
        try
        {
            File baseFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), "galacticraft/overworldMap");
            if (!baseFolder.exists())
            {
                GCLog.severe("Base folder missing: " + baseFolder.getAbsolutePath());
                return;
            }
            File file = makeFileName(baseFolder, cx, cz);
            if (!file.exists())
            {
                getBiomeMapForCoords(world, cx, cz, 1, SIZE_STD, SIZE_STD, baseFolder);
                return;
            }
            sendMapPacket(cx, cz, client, FileUtils.readFileToByteArray(file));
        }
        catch (Exception ex)
        {
            System.err.println("Error sending map image to player.");
            ex.printStackTrace();
        }
    }
    
    public static void sendMapPacket(int cx, int cz, EntityPlayerMP client, byte[] largeMap) throws IOException
    {
        byte[] compressed;
        if (cx == LARGEMAP_MARKER)
        {
            if (overworldImageCompressed == null)
            {
                overworldImageCompressed = zipCompress(largeMap);
            }
            compressed = overworldImageCompressed;
        }
        else
        {
            compressed = zipCompress(largeMap);
        }
        sendMapPacketCompressed(cx, cz, client, compressed);
    }
    
    public static void sendMapPacketToAll(int cx, int cz, byte[] largeMap)
    {
        byte[] compressed;
        if (cx == LARGEMAP_MARKER)
        {
            if (overworldImageCompressed == null)
            {
                overworldImageCompressed = zipCompress(largeMap);
            }
            compressed = overworldImageCompressed;
        }
        else
        {
            compressed = zipCompress(largeMap);
        }
        sendMapPacketAllCompressed(cx, cz, compressed);
    }
    
    private static void sendMapPacketCompressed(int cx, int cz, EntityPlayerMP client, byte[] map) throws IOException
    {
        if (cx == LARGEMAP_MARKER && map.length < 2080000)
        {
            int halfSize = map.length / 2;
            byte[] largeMapPartA = Arrays.copyOf(map, halfSize);
            byte[] largeMapPartB = Arrays.copyOfRange(map, halfSize, map.length);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, GCCoreUtil.getDimensionID(client.worldObj), new Object[] { cx, map.length, largeMapPartA }), client);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, GCCoreUtil.getDimensionID(client.worldObj), new Object[] { cx + 1, map.length, largeMapPartB }), client);
        }
        else if (map.length < 1040000)  //That's about the limit on a Forge packet length
    	{
    		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, GCCoreUtil.getDimensionID(client.worldObj), new Object[] { cx, cz, map }), client);
    	}
    }

    private static void sendMapPacketAllCompressed(int cx, int cz, byte[] map)
    {
        if (cx == LARGEMAP_MARKER && map.length < 2080000)
        {
            int halfSize = map.length / 2;
            byte[] largeMapPartA = Arrays.copyOf(map, halfSize);
            byte[] largeMapPartB = Arrays.copyOfRange(map, halfSize, map.length);
            GCCoreUtil.sendToAllDimensions(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { cx, map.length, largeMapPartA });
            GCCoreUtil.sendToAllDimensions(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { cx + 1, map.length, largeMapPartB });
        }
        else if (map.length < 1040000)  //That's about the limit on a Forge packet length
        {
            GCCoreUtil.sendToAllDimensions(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { cx, cz, map });
        }
    }

    /**
     *  On a server, build any needed patchwork map files around co-ordinates (x, z)
     *  The needed files may already have been generated by previous calls on the same server
     *  Files are stored in the world save folder, subfolder galacticraft/overworldMap
     */
    public static boolean buildMaps(World world, int x, int z)
    {
        if (world.getWorldType() == WorldType.FLAT || !(world.provider instanceof WorldProviderSurface))
        {
            return false;
        }

        File baseFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), "galacticraft/overworldMap");
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
        if (cx < 0)
        {
            cx -= SIZE_STD2 - 1;
        }
        cx /= SIZE_STD2;
        return cx * SIZE_STD2;
    }

    public static boolean getBiomeMapForCoords(World world, int cx, int cz, int scale, int sizeX, int sizeZ, File baseFolder)
    {
        File outputFile;
        if (sizeX != sizeZ)
        {
            outputFile = new File(baseFolder, "Overworld" + sizeX + ".bin");
            if (sizeX == OVERWORLD_LARGEMAP_WIDTH)
            {
                MapGen newGen = new MapGen(world, sizeX, sizeZ, cx, cz, 1 << scale, outputFile);
                if (newGen.mapNeedsCalculating)
                {
                    slowMap = newGen;
                    calculatingMap.set(true);
                }
                return false;
            }
        }
        else
        {
            outputFile = makeFileName(baseFolder, cx, cz);
        }

        MapGen newGen = new MapGen(world, sizeX, sizeZ, cx, cz, 1 << scale, outputFile);
        if (newGen.mapNeedsCalculating)
        {
            if (calculatingMap.getAndSet(true))
            {
                queuedMaps.add(newGen);
            }
            else
            {
                currentMap = newGen;
            }
            return false;
        }
        return true;
    }
   
    public static void saveMapProgress()
    {
        if (slowMap != null)
        {
            slowMap.abort();
            try
            {
                Thread.currentThread().sleep(90);
            } catch (InterruptedException e)
            { }
            slowMap.writeOutputFile(false);
            slowMap = null;
        }
    }

    /**
     * Poll any map threads to see if they need starting or if they're finished
     * 
     *    Multi-threaded version - runs each MapGen in its own thread, polls MapGen.finishedCalculatingMap to know when finished
     */
    public static void BiomeMapNextTick_MultiThreaded()
    {
        if (currentMap != null)
        {
            if (threadCurrentMap == null)
            {
            	//Create the current map thread, pausing any slow map thread 
	        	if (slowMap != null)
	        	{
	        		slowMap.pause();
	        	}
	        	//TODO = should it use a re-usable thread pool?
            	threadCurrentMap = new Thread(currentMap);
                threadCurrentMap.setName("Background world mapping");
            	threadCurrentMap.setPriority(Thread.NORM_PRIORITY - 1);
	            threadCurrentMap.start();
            }
            else if (currentMap.finishedCalculating.get())
            {
            	//Finished the current map
            	threadCurrentMap = null;
            	currentMap.writeOutputFile(true);

      			if (queuedMaps.size() > 0)
      			{
      				currentMap = queuedMaps.removeFirst();
      			}
      			else
      			{
      				currentMap = null;

      				if (slowMap == null)
      					calculatingMap.set(false);
      				else
      				{
      					if (slowMap != null)
      					{
      						slowMap.resume();
      					}
      				}
      			}
            }
            
            return;
        }

        if (!queuedMaps.isEmpty())
        {
            if (slowMap != null)
            {
                slowMap.pause();
            }

            currentMap = queuedMaps.removeFirst();
            return;
        }

        if (slowMap != null)
        {
            if (threadSlowMap == null)
            {
            	//Create the slow map thread 
            	threadSlowMap = new Thread(slowMap);
            	threadSlowMap.setName("Background world mapping");
            	threadSlowMap.setPriority(Thread.NORM_PRIORITY - 1);
	            threadSlowMap.start();
            }
            else if (slowMap.finishedCalculating.get())
            {
            	//Finished the current map
            	threadSlowMap = null;
            	slowMap.writeOutputFile(true);
      			slowMap = null;
				calculatingMap.set(false);
            }
            
            return;
        }
    }
    
    //Single Threaded Version of the same code
    //(Currently unused)
    public static void BiomeMapNextTick_SingleThreaded()
    {
        MapGen map;
        boolean doingSlow = false;
        if (currentMap != null)
        {
            map = currentMap;
        }
        else if (slowMap != null)
        {
            map = slowMap;
            doingSlow = true;
        }
        else
        {
            return;
        }

        //If single threade, allow GC background mapping around 9% of the server tick time if server running at full speed
        //(on a slow server, it will be proportionately lower %)
        long end = System.nanoTime() + 4500000L;
        while (System.nanoTime() < end)
        {
            if (map.BiomeMapOneTick())
            {
                //Finished
                map.writeOutputFile(true);
                if (doingSlow)
                {
                    slowMap = null;
                }
                else
                {
                    currentMap = null;
                    if (queuedMaps.size() > 0)
                    {
                        currentMap = queuedMaps.removeFirst();
                    }
                }
                if (currentMap == null && slowMap == null)
                {
                    calculatingMap.set(false);
                }
                return;
            }
        }
    }


    public static boolean backgroundMapping(Thread currentThread)
    {
        return currentThread == threadSlowMap || currentThread == threadCurrentMap;
    }

    
    /**
     * Converts a 48px high image to a 12px high image with a palette chosen only from the colours in the paletteImage
     *
     * @param overworldImage Output image already created as a blank image, dimensions biomeMapSizeX x biomeMapSizeY
     * @param paletteImage   Palette image, dimensions must be a square with sides biomeMapSizeZ / 4
     */
    public static BufferedImage convertTo12pxTexture(BufferedImage overworldImage, BufferedImage paletteImage)
    {
        BufferedImage result = new BufferedImage(overworldImage.getWidth(), overworldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        TreeMap<Integer, Integer> mapColPos = new TreeMap<>();
        TreeMap<Integer, Integer> mapColPosB = new TreeMap<>();
        int count = 0;
        for (int x = 0; x < overworldImage.getWidth(); x += 4)
        {
            for (int z = 0; z < overworldImage.getHeight(); z += 4)
            {
                int r = 0;
                int g = 0;
                int b = 0;
                for (int xx = 0; xx < 4; xx++)
                {
                    for (int zz = 0; zz < 4; zz++)
                    {
                        int col = overworldImage.getRGB(xx + x, zz + z);
                        r += (col >> 16);
                        g += (col >> 8) & 255;
                        b += col & 255;
                    }
                }
                while (mapColPos.containsKey(g - b))
                {
                    g++;
                }
                mapColPos.put(g - b, count);
                if (x < overworldImage.getHeight())
                {
                    int col = paletteImage.getRGB(x + 1, z + 1);
                    r = (col >> 16);
                    g = (col >> 8) & 255;
                    b = col & 255;
                    while (mapColPosB.containsKey(g - b))
                    {
                        g++;
                    }
                    mapColPosB.put(g - b, col);
                }
                count++;
            }
        }

        count = 0;
        int newCol = 0;
        Iterator<Integer> it = mapColPosB.keySet().iterator();
        Iterator<Integer> itt = mapColPos.keySet().iterator();
        int modulus = overworldImage.getHeight() / 4;
        int mod2 = overworldImage.getWidth() / overworldImage.getHeight();
        for (int x = 0; x < overworldImage.getWidth() / 4; x++)
        {
            for (int z = 0; z < modulus; z++)
            {
                if (count % mod2 == 0)
                {
                    newCol = mapColPosB.get(it.next());
                }
                int position = mapColPos.get(itt.next());
                int xx = position / modulus;
                int zz = position % modulus;
                for (int xxx = 0; xxx < 4; xxx++)
                {
                    for (int zzz = 0; zzz < 4; zzz++)
                    {
                        result.setRGB(xx * 4 + xxx, zz * 4 + zzz, newCol);
                    }
                }
                count++;
            }
        }

        return result;
    }

    //Unused
    public static BufferedImage readImage(Object source) throws IOException
    {
        ImageInputStream stream = ImageIO.createImageInputStream(source);
        ImageReader reader = ImageIO.getImageReaders(stream).next();
        reader.setInput(stream);
        ImageReadParam param = reader.getDefaultReadParam();

        ImageTypeSpecifier typeToUse = null;
        for (Iterator i = reader.getImageTypes(0); i.hasNext(); )
        {
            ImageTypeSpecifier type = (ImageTypeSpecifier) i.next();
            if (type.getColorModel().getColorSpace().isCS_sRGB())
            {
                typeToUse = type;
            }
        }
        if (typeToUse != null)
        {
            param.setDestinationType(typeToUse);
        }

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
            File folder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/galacticraftMaps");

            try
            {
                ImageOutputStream outputStreamA = new FileImageOutputStream(new File(folder, name));
                GalacticraftCore.jpgWriter.setOutput(outputStreamA);
                GalacticraftCore.jpgWriter.write(null, new IIOImage(img, null, null), GalacticraftCore.writeParam);
                outputStreamA.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    public static byte[] zipCompress(byte[] data)
    {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_SPEED);
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream compressed = new ByteArrayOutputStream(data.length * 2 / 3);
        byte[] miniBuffer = new byte[4096];
        while (!deflater.finished())
        {
            int count = deflater.deflate(miniBuffer);
            compressed.write(miniBuffer, 0, count);
        }
        return compressed.toByteArray();
    }
    
    private static byte[] zipDeCompress(byte[] data) throws DataFormatException
    {
        Inflater inflater = new Inflater();
        inflater.setInput(data); 
        ByteArrayOutputStream deCompressed = new ByteArrayOutputStream(data.length * 2);  
        byte[] miniBuffer = new byte[4096];
        while (!inflater.finished())
        {  
            int count = inflater.inflate(miniBuffer);
            deCompressed.write(miniBuffer, 0, count);  
        }  
        return deCompressed.toByteArray();
    }
    
    @SideOnly(Side.CLIENT)
    public static void receiveOverworldImageCompressed(int cx, int cz, byte[] raw) throws IOException
    {
        if (cx == LARGEMAP_MARKER)
        {
            //Received large map part A
            if (overworldImageBytesPart == null)
            {
                overworldImageBytesPart = raw;
                return;
            }
            else
            {
                byte[] overWorldImageComplete = Arrays.copyOf(raw, cz);
                int offsetPartB = cz / 2;
                for (int i = offsetPartB; i < cz; i++)
                    overWorldImageComplete[i] = overworldImageBytesPart[i - offsetPartB];
                overworldImageBytesPart = null;
                raw = overWorldImageComplete;
            }
        }
        else if (cx == LARGEMAP_MARKER + 1)
        {
            //Received large map part B
            if (overworldImageBytesPart == null)
            {
                overworldImageBytesPart = raw;
                return;
            }
            else
            {
                byte[] overWorldImageComplete = Arrays.copyOf(overworldImageBytesPart, cz);
                int offsetPartB = cz / 2;
                for (int i = offsetPartB; i < cz; i++)
                    overWorldImageComplete[i] = raw[i - offsetPartB];
                overworldImageBytesPart = null;
                raw = overWorldImageComplete;
            }
        }
        
        try
        {
            getOverworldImageFromRaw(cx, cz, zipDeCompress(raw));
        } catch (DataFormatException e)
        {  
            GCLog.debug(e.toString());
            GCLog.debug("Client received a corrupted map image data packet from server " + cx + "_" + cz);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void getOverworldImageFromRaw(int cx, int cz, byte[] raw) throws IOException
    {
    	File folder = MapUtil.getClientMapsFolder();

    	if (raw.length == OVERWORLD_LARGEMAP_WIDTH * OVERWORLD_LARGEMAP_HEIGHT * 2)
        {
    	    if (folder != null)
    	    {            
    	        File file0 = new File(folder, "overworldRaw.bin");

    	        if (!file0.exists() || (file0.canRead() && file0.canWrite()))
    	        {
    	            FileUtils.writeByteArrayToFile(file0, raw);
    	        }
    	        else
    	        {
    	            System.err.println("Cannot write to file %minecraft%/assets/galacticraftMaps/overworldRaw.bin");
    	        }
    	    }
    	    else
    	    {
                System.err.println("No folder for file %minecraft%/assets/galacticraftMaps/overworldRaw.bin");
    	    }

            //raw is a WIDTH_WORLD x HEIGHT_WORLD array of 2 byte entries: biome type followed by height
            //Here we will make a texture from that, but twice as large: 4 pixels for each data point, it just looks better that way when the texture is used
            BufferedImage worldImageLarge = new BufferedImage(OVERWORLD_LARGEMAP_WIDTH * 2, OVERWORLD_LARGEMAP_HEIGHT * 2, BufferedImage.TYPE_INT_RGB);
            ArrayList<Integer> cols = new ArrayList<Integer>();
            int lastcol = -1;
            int idx = 0;
            for (int x = 0; x < OVERWORLD_LARGEMAP_WIDTH; x++)
            {
                for (int z = 0; z < OVERWORLD_LARGEMAP_HEIGHT; z++)
                {
                    int arrayIndex = (x * OVERWORLD_LARGEMAP_HEIGHT + z) * 2;
                    int biome = ((int) raw[arrayIndex]) & 255;
                    int height = ((int) raw[arrayIndex + 1]) & 255;

                    if (height < OCEAN_HEIGHT && biome != 2 && biome != 10)
                    {
                    	//Includes ponds, lakes and rivers in other biomes
                        biome = 0;
                    }
                    if (height < DEEP_OCEAN && biome == 0)
                    {
                        biome = 24;
                    }

                    worldImageLarge.setRGB(x * 2, z * 2, convertBiomeColour(biome, height));
                    worldImageLarge.setRGB(x * 2, z * 2 + 1, convertBiomeColour(biome, height));
                    worldImageLarge.setRGB(x * 2 + 1, z * 2, convertBiomeColour(biome, height));
                    worldImageLarge.setRGB(x * 2 + 1, z * 2 + 1, convertBiomeColour(biome, height));
                }
            }

//overworldTextureLarge is currently unused in beta
//            if (ClientProxyCore.overworldTextureLarge == null)
//            {
//                ClientProxyCore.overworldTextureLarge = new DynamicTextureProper(WIDTH_WORLD * 2, HEIGHT_WORLD * 2);
//            }
//            ClientProxyCore.overworldTextureLarge.update(worldImageLarge);

            //Write it to a .jpg file on client for beta preview 
            if (GalacticraftCore.enableJPEG && folder != null)
            {
                ImageOutputStream outputStream = new FileImageOutputStream(new File(folder, "large.jpg"));
                GalacticraftCore.jpgWriter.setOutput(outputStream);
                GalacticraftCore.jpgWriter.write(null, new IIOImage(worldImageLarge, null, null), GalacticraftCore.writeParam);
                outputStream.close();
            }
        }
    	//This is the dimensions of the Overworld texture map
        else if (raw.length == OVERWORLD_TEXTURE_WIDTH * OVERWORLD_TEXTURE_HEIGHT * 2)
        {
            //raw is a WIDTH_STD x HEIGHT_STD array of 2 byte entries: biome type followed by height
            BufferedImage worldImage = new BufferedImage(OVERWORLD_TEXTURE_WIDTH, OVERWORLD_TEXTURE_HEIGHT, BufferedImage.TYPE_INT_RGB);
            ArrayList<Integer> cols = new ArrayList<Integer>();
            int lastcol = -1;
            int idx = 0;
            for (int x = 0; x < OVERWORLD_TEXTURE_WIDTH; x++)
            {
                for (int z = 0; z < OVERWORLD_TEXTURE_HEIGHT; z++)
                {
                    int arrayIndex = (x * OVERWORLD_TEXTURE_HEIGHT + z) * 2;
                    int biome = ((int) raw[arrayIndex]) & 255;
                    int height = ((int) raw[arrayIndex + 1]) & 255;

                    if (height < OCEAN_HEIGHT && biome != 2 && biome != 10)
                    {
                    	//Includes ponds, lakes and rivers in other biomes
                        biome = 0;
                    }
                    if (height < DEEP_OCEAN && biome == 0)
                    {
                        biome = 24;
                    }

                    worldImage.setRGB(x, z, convertBiomeColour(biome, height));
                }
            }

            IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
            BufferedImage paletteImage = null;
            try
            {
                InputStream in = rm.getResource(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png")).getInputStream();
                paletteImage = ImageIO.read(in);
                in.close();
                paletteImage.getHeight();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }

            BufferedImage result = convertTo12pxTexture(worldImage, paletteImage);

            if (result != null)
            {
                if (ClientProxyCore.overworldTextureWide == null)
                {
                    ClientProxyCore.overworldTextureWide = new DynamicTextureProper(OVERWORLD_TEXTURE_WIDTH, OVERWORLD_TEXTURE_HEIGHT);
                }
                if (ClientProxyCore.overworldTextureClient == null)
                {
                    ClientProxyCore.overworldTextureClient = new DynamicTextureProper(OVERWORLD_TEXTURE_HEIGHT, OVERWORLD_TEXTURE_HEIGHT);
                }
                ClientProxyCore.overworldTextureWide.update(result);
                ClientProxyCore.overworldTextureClient.update(result);
                ClientProxyCore.overworldTexturesValid = true;
            }
        }
        else if (folder != null)
        {
            File file0 = makeFileName(folder, cx, cz);

            if (!file0.exists() || (file0.canRead() && file0.canWrite()))
            {
                FileUtils.writeByteArrayToFile(file0, raw);
            }
        }
        else
        {
            System.err.println("No folder %minecraft%/assets/galacticraftMaps for local map file.");
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean getMap(int[] image, World world, BlockPos pos)
    {
        int xCoord = pos.getX();
        int zCoord = pos.getZ();
        int cx = convertMap(xCoord);
        int cz = convertMap(zCoord);

        File baseFolder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/galacticraftMaps");
        if (!baseFolder.exists() && !baseFolder.mkdirs())
        {
            GCLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
            return false;
        }

        int dim = GCCoreUtil.getDimensionID(world);
        boolean result = true;
        if (makeRGBimage(image, baseFolder, cx - SIZE_STD2, cz - SIZE_STD2, 0, 0, xCoord, zCoord, dim, result))
        {
            result = false;
        }
        if (makeRGBimage(image, baseFolder, cx - SIZE_STD2, cz, 0, SIZE_STD, xCoord, zCoord, dim, result))
        {
            result = false;
        }
        if (makeRGBimage(image, baseFolder, cx - SIZE_STD2, cz + SIZE_STD2, 0, SIZE_STD2, xCoord, zCoord, dim, result))
        {
            result = false;
        }
        if (makeRGBimage(image, baseFolder, cx, cz - SIZE_STD2, SIZE_STD, 0, xCoord, zCoord, dim, result))
        {
            result = false;
        }
        if (makeRGBimage(image, baseFolder, cx, cz, SIZE_STD, SIZE_STD, xCoord, zCoord, dim, result))
        {
            result = false;
        }
        if (makeRGBimage(image, baseFolder, cx, cz + SIZE_STD2, SIZE_STD, SIZE_STD2, xCoord, zCoord, dim, result))
        {
            result = false;
        }
        if (makeRGBimage(image, baseFolder, cx + SIZE_STD2, cz - SIZE_STD2, SIZE_STD2, 0, xCoord, zCoord, dim, result))
        {
            result = false;
        }
        if (makeRGBimage(image, baseFolder, cx + SIZE_STD2, cz, SIZE_STD2, SIZE_STD, xCoord, zCoord, dim, result))
        {
            result = false;
        }
        if (makeRGBimage(image, baseFolder, cx + SIZE_STD2, cz + SIZE_STD2, SIZE_STD2, SIZE_STD2, xCoord, zCoord, dim, result))
        {
            result = false;
        }
        return result;
    }

    @SideOnly(Side.CLIENT)
    private static boolean makeRGBimage(int[] array, File baseFolder, int cx, int cz, int offsetX, int offsetZ, int xCoord, int zCoord, int dim, boolean prevResult)
    {
        File filename = makeFileName(baseFolder, cx, cz);
        if (!filename.exists())
        {
            if (clientRequests.contains(filename.getName()))
            {
                //GCLog.debug("Info: Server not yet ready to send map file " + baseFolder.getName() + "/" + filename.getName());
            }
            else
            {
                clientRequests.add(filename.getName());
                //GCLog.debug("Info: Client requested map file" + filename.getName());
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_MAP_IMAGE, GCCoreUtil.getDimensionID(FMLClientHandler.instance().getClient().theWorld), new Object[] { dim, cx, cz }));
            }
            return true;
        }

        if (!prevResult)
        {
            return true;
        }

        int ox = (convertMap(xCoord) - xCoord - SIZE_STD) / 2;
        int oz = (convertMap(zCoord) - zCoord - SIZE_STD) / 2;

        byte[] raw = null;
        try
        {
            raw = FileUtils.readFileToByteArray(filename);
        }
        catch (IOException e)
        {
            GCLog.severe("Problem reading map file: " + baseFolder.getAbsolutePath() + filename.getName());
            return true;
        }
        if (raw == null || raw.length != SIZE_STD * SIZE_STD * 2)
        {
            GCLog.debug("Warning: unexpected map size is " + raw.length + " for file " + filename.toString());
            return true;
        }

        int xstart = Math.max(0, -offsetX - ox);
        int zstart = Math.max(0, -offsetZ - oz);
        for (int x = xstart; x < SIZE_STD; x++)
        {
            int imagex = x + offsetX + ox;
            if (imagex >= SIZE_STD2)
            {
                break;
            }
            for (int z = zstart; z < SIZE_STD; z++)
            {
                int imageZ = z + oz + offsetZ;// + SIZE_STD - 1 - z;
                if (imageZ >= SIZE_STD2)
                {
                    break;
                }

                int arrayIndex = (x * SIZE_STD + z) * 2;
                int biome = ((int) raw[arrayIndex]) & 255;
                int height = ((int) raw[arrayIndex + 1]) & 255;
              
                if (height < OCEAN_HEIGHT && biome != 2 && biome != 10)
                {
                	//Includes ponds, lakes and rivers in other biomes
                    biome = 0;
                }
                if (height < DEEP_OCEAN && biome == 0)
                {
                    biome = 24;
                }

                if (imagex < 0 || imageZ < 0)
                {
                    GCLog.debug("Outside image " + imagex + "," + imageZ + " - " + "x=" + x + " z=" + z + " offsetX=" + offsetX + " offsetZ = " + offsetZ + " ox=" + ox + " oz=" + oz);
                }
                else
                {
                    array[imagex + SIZE_STD2 * imageZ] = convertBiomeColour(biome, height) + 0xff000000;
                }
            }
        }
        return false;
    }


    private static File makeFileName(File folder, int cx, int cz)
    {
        return new File(folder, "overworld" + cx / SIZE_STD2 + "_" + cz / SIZE_STD2 + ".bin");
    }

    private static int getBiomeBaseColour(int biomeId)
    {
        BiomeGenBase[] biomeList = BiomeGenBase.getBiomeGenArray();
    	BiomeGenBase biomegenbase = null;
    	if (biomeId >= 0 && biomeId <= biomeList.length)
    	{
    		biomegenbase = biomeList[biomeId];
    	}
    	return biomegenbase == null ? BiomeGenBase.ocean.color : biomegenbase.color;
    }
    
    public static int convertBiomeColour(int in, int height)
    {
        int rv;
        int s = MapUtil.biomeColours.size();
        if (in >= 128 && in < 128 + s)
        {
            in -= 128;
        }
        if (in >= s)
        {
            rv = getBiomeBaseColour(in);
        }
        else
        {
            BlockVec3 bv = MapUtil.biomeColours.get(in);
            if (bv == null)
            {
                rv = getBiomeBaseColour(in);
            }
            else
            {
                if (bv.z > 0 && MapUtil.rand.nextInt(100) < bv.z)
                {
                    rv = bv.y;
                }
                else
                {
                    rv = bv.x;
                }
            }
        }
        if (rv == 0x9c2424 && MapUtil.rand.nextInt(2) == 0)
        {
            rv = 0xbfa384;
        }
        if (height < OCEAN_HEIGHT)
        {
            return rv;
        }
        if (height > 92 && (in == 3 || in == 20 || in == 31 || in == 33 || in == 34))
        {
            if (MapUtil.rand.nextInt(8) > 98 - height)
            {
                rv = Material.snow.getMaterialMapColor().colorValue;
            }
        }
        float factor = (height - 68F) / 94F;
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
                            IBlockState i5 = block.getDefaultState();

                            if (l4 > 1)
                            {
                                do
                                {
                                    --l4;
                                    BlockPos pos = new BlockPos(x, l4, z);
                                    block = chunk.getBlock(pos);
                                    i5 = chunk.getBlockState(pos);
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

    @SideOnly(Side.CLIENT)
    public static File getClientMapsFolder()
    {
        File folder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/galacticraftMaps");
        try
        {
            if (folder.exists() || folder.mkdirs())
            {
                return folder;
            }
            else
            {
                System.err.println("Cannot create directory %minecraft%/assets/galacticraftMaps! : " + folder.toString());
            }
        }
        catch (Exception e)
        {
            System.err.println(folder.toString());
            e.printStackTrace();
        }
        return null;
    }
}
