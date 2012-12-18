package micdoodle8.mods.galacticraft.core.wgen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class GCCoreChunk extends Chunk
{
    public GCCoreChunk(World par1World, int[] idArray, int[] metadataArray, int par3, int par4)
    {
        super(par1World, par3, par4);
        final int var5 = idArray.length / 256;

        for (int var6 = 0; var6 < 16; ++var6)
        {
            for (int var7 = 0; var7 < 16; ++var7)
            {
                for (int var8 = 0; var8 < var5; ++var8)
                {
                    final int idAtCoord = idArray[var6 << 11 | var7 << 7 | var8] & 0xFF;
                    
                    int metaAtCoord = -1;
                    
                    if (metadataArray != null)
                    {
                    	metaAtCoord = metadataArray[var6 << 11 | var7 << 7 | var8] & 0xFF;
                    }

                    if (idAtCoord != 0)
                    {
                        final int var10 = var8 >> 4;

                        if (this.getBlockStorageArray()[var10] == null)
                        {
                            this.getBlockStorageArray()[var10] = new ExtendedBlockStorage(var10 << 4);
                        }

                        this.getBlockStorageArray()[var10].setExtBlockID(var6, var8 & 15, var7, idAtCoord);
                        
                        if (metaAtCoord != -1)
                        {
                            this.getBlockStorageArray()[var10].setExtBlockMetadata(var6, var8 & 15, var7, metaAtCoord);
                        }
                    }
                }
            }
        }
    }
}
