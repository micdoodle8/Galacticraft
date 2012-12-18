package micdoodle8.mods.galacticraft.europa.wgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GCEuropaWorldGenLakes extends WorldGenerator
{
    private final int blockIndex;

    public GCEuropaWorldGenLakes(int par1)
    {
        this.blockIndex = par1;
    }

    @Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        par3 -= 8;

        if (par4 <= 4)
        {
            return false;
        }
        else
        {
            par4 -= 4;
            final boolean[] var6 = new boolean[2048];
            final int var7 = par2Random.nextInt(4) + 4;
            int var8;

            for (var8 = 0; var8 < var7; ++var8)
            {
                final double var9 = par2Random.nextDouble() * 6.0D + 3.0D;
                final double var11 = par2Random.nextDouble() * 4.0D + 2.0D;
                final double var13 = par2Random.nextDouble() * 6.0D + 3.0D;
                final double var15 = par2Random.nextDouble() * (16.0D - var9 - 2.0D) + 1.0D + var9 / 2.0D;
                final double var17 = par2Random.nextDouble() * (8.0D - var11 - 4.0D) + 2.0D + var11 / 2.0D;
                final double var19 = par2Random.nextDouble() * (16.0D - var13 - 2.0D) + 1.0D + var13 / 2.0D;

                for (int var21 = 1; var21 < 15; ++var21)
                {
                    for (int var22 = 1; var22 < 15; ++var22)
                    {
                        for (int var23 = 1; var23 < 7; ++var23)
                        {
                            final double var24 = (var21 - var15) / (var9 / 2.0D);
                            final double var26 = (var23 - var17) / (var11 / 2.0D);
                            final double var28 = (var22 - var19) / (var13 / 2.0D);
                            final double var30 = var24 * var24 + var26 * var26 + var28 * var28;

                            if (var30 < 1.0D)
                            {
                                var6[(var21 * 16 + var22) * 8 + var23] = true;
                            }
                        }
                    }
                }
            }

            int var10;
            int var32;

            for (var8 = 0; var8 < 16; ++var8)
            {
                for (var32 = 0; var32 < 16; ++var32)
                {
                    for (var10 = 0; var10 < 8; ++var10)
                    {
                        if (var6[(var8 * 16 + var32) * 8 + var10])
                        {
                        	if (par1World.getBlockId(par3 + var8, par4 + var10, par5 + var32) == Block.waterStill.blockID)
                        	{
                                par1World.setBlock(par3 + var8, par4 + var10, par5 + var32, this.blockIndex);
                        	}
                        }
                    }
                }
            }

            return true;
        }
    }
}
