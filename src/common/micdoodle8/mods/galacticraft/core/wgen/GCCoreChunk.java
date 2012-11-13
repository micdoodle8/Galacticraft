package micdoodle8.mods.galacticraft.core.wgen;

import net.minecraft.src.Chunk;
import net.minecraft.src.ExtendedBlockStorage;
import net.minecraft.src.World;

public class GCCoreChunk extends Chunk
{
    public GCCoreChunk(World par1World, int[] par2ArrayOfByte, int par3, int par4)
    {
        super(par1World, par3, par4);
        int var5 = par2ArrayOfByte.length / 256;

        for (int var6 = 0; var6 < 16; ++var6)
        {
            for (int var7 = 0; var7 < 16; ++var7)
            {
                for (int var8 = 0; var8 < var5; ++var8)
                {
                    int var9 = par2ArrayOfByte[var6 << 11 | var7 << 7 | var8] & 0xFF;

                    if (var9 != 0)
                    {
                        int var10 = var8 >> 4;

                        if (this.getBlockStorageArray()[var10] == null)
                        {
                            this.getBlockStorageArray()[var10] = new ExtendedBlockStorage(var10 << 4);
                        }

                        this.getBlockStorageArray()[var10].setExtBlockID(var6, var8 & 15, var7, var9);
                    }
                }
            }
        }
    }
}
