package micdoodle8.mods.galacticraft.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.IIOImage;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;

import org.apache.commons.io.FileUtils;

public class MapUtil
{
    public static AtomicBoolean calculatingMap = new AtomicBoolean();
    private static int ix = 0;
    private static int iz = 0;
    private static int biomeMapx0 = 0;
    private static int biomeMapz0 = 0;
	private static int biomeMapCx;
	private static int biomeMapCz;
	private static int biomeMapFactor;
	private static WorldChunkManager biomeMapWCM;
	private static BufferedImage biomeMapImage;
	private static File biomeMapFile;
	private static EntityPlayerMP biomeMapPlayerBase;
	private static int biomeMapRange;
	private static int biomeMapSize;
    
	public static void getBiomeMapForCoords(World world, int cx, int cz, int scale, int size, File outputFile, EntityPlayerMP player)
    {
    	if (calculatingMap.get()) return;
    	System.out.println("Starting map generation centered at " + cx + "," + cz);
    	ix = 0;
    	iz = 0;
    	biomeMapCx = cx;
    	biomeMapCz = cz;
    	biomeMapFactor = 1 << scale;
    	biomeMapWCM = world.getWorldChunkManager();
    	biomeMapImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
    	int ffactor = Math.max(biomeMapFactor, 16);
    	int limit = size * biomeMapFactor / ffactor / 2;
    	biomeMapRange = limit;
    	biomeMapSize = size;
    	biomeMapx0 = -limit;
    	biomeMapz0 = -limit;
    	biomeMapFile = outputFile;
    	biomeMapPlayerBase = player;
		calculatingMap.set(true);
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
    	if (iz > biomeMapSize - imagefactor)
    	{
        	iz = 0;
        	System.out.println("Finished map column " + ix);
        	ix += imagefactor;
        	biomeMapz0 = -biomeMapRange;
        	biomeMapx0 += multifactor;
        	return ix > biomeMapSize - imagefactor;
    	}
    	return false;
	}
	
	public static void BiomeMapNextTick()
	{
		int j = 24;
		while (j > 0)
		{
			if (BiomeMapOneTick())
			{
				//Finished
				calculatingMap.set(false);
				try
				{
					if (!biomeMapFile.exists() || (biomeMapFile.canWrite() && biomeMapFile.canRead()))
					{
						ImageOutputStream outputStream = new FileImageOutputStream(biomeMapFile);  
						GalacticraftCore.jpgWriter.setOutput(outputStream);
						GalacticraftCore.jpgWriter.write(null, new IIOImage(biomeMapImage, null, null), GalacticraftCore.writeParam);
						outputStream.close();
					}
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
				try
				{
					byte[] bytes = FileUtils.readFileToByteArray(biomeMapFile);
					GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { bytes } ), biomeMapPlayerBase);
				}
				catch (Exception ex)
				{
					System.err.println("Error sending overworld image to player.");
					ex.printStackTrace();
				}
				return;
			}
			j--;
		}
    }

	private static void biomeMapOneChunk(int x0, int z0, int ix, int iz, int factor)
	{
		BiomeGenBase[] biomesGrid = biomeMapWCM.loadBlockGeneratorData(null, x0 * 16, z0 * 16, 16, 16);
    	if (biomesGrid == null) return;
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
    			int colour = -1;
    			int lastcol = -1;
    			int idx = 0;
    			//TODO: start in centre instead of top left
    			BIOMEDONE:
    			for (int xx = 0; xx < limit; xx++)
    			{
        			for (int zz = 0; zz < limit; zz++)
        			{
        				colour = biomesGrid[xx + x + ((zz + z) << 4)].color;
        				if (colour != lastcol)
        				{
            				idx = cols.indexOf(colour);
            				if (idx == -1)
            				{
            					idx = cols.size();
            					cols.add(colour);
            				}
            				lastcol = colour;
        				}
        				count[idx]++;
        				if (count[idx] > maxcount)
        				{
        					maxcount = count[idx];
        					maxindex = idx;
        					if (maxcount > halfFactor) break BIOMEDONE;
        				}                				
        			}
    			}          			
    			biomeMapImage.setRGB(ix, iz, convertBiomeColour(cols.get(maxindex)));
    			iz++;
    		}
    		iz = izstore;
    		ix ++;
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
    
    public static int convertBiomeColour(int in)
    {
    	switch (in)
    	{
    		//Plains: grass
    		case 9286496: return Material.grass.getMaterialMapColor().colorValue;
    		//Swampland: dark green
    		case 522674: return 4215066;
    		//Roofed forest: forest color
    		case 4215066: return 353825;
    		//Forest: leaf color
    		case 353825: return Material.leaves.getMaterialMapColor().colorValue;
    		//Desert: sand color
    		case 16421912: return Material.sand.getMaterialMapColor().colorValue;
    		//Desert Hills: lighter color
    		case 13786898: return 16421912; 
    		//Ocean: water color
    		case 112: return Material.water.getMaterialMapColor().colorValue;
    		//DeepOcean: lighter color
    		case 48: return 112;
    		
    	}
    	return in;
    }
}
