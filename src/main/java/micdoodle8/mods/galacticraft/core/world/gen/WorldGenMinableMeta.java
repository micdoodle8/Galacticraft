package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

public class WorldGenMinableMeta extends WorldGenMinable
{
    private final Block minableBlockId;

    private final int numberOfBlocks;

    private final int metadata;

    private boolean usingMetadata = false;

    private final Block fillerID;

    private final int fillerMetadata;

    public WorldGenMinableMeta(Block par1, int par2, int par3, boolean par4, Block id, int meta)
    {
        super(par1.getStateFromMeta(par3), par2, BlockHelper.forBlock(id));
        this.minableBlockId = par1;
        this.numberOfBlocks = par2;
        this.metadata = par3;
        this.usingMetadata = par4;
        this.fillerID = id;
        this.fillerMetadata = meta;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        float f = rand.nextFloat() * (float)Math.PI;
        double d0 = (double)((float)(position.getX() + 8) + (float)Math.sin(f) * (float)this.numberOfBlocks / 8.0F);
        double d1 = (double)((float)(position.getX() + 8) - (float)Math.sin(f) * (float)this.numberOfBlocks / 8.0F);
        double d2 = (double)((float)(position.getZ() + 8) + (float)Math.cos(f) * (float)this.numberOfBlocks / 8.0F);
        double d3 = (double)((float)(position.getZ() + 8) - (float)Math.cos(f) * (float)this.numberOfBlocks / 8.0F);
        double d4 = (double)(position.getY() + rand.nextInt(3) - 2);
        double d5 = (double)(position.getY() + rand.nextInt(3) - 2);

        for (int i = 0; i < this.numberOfBlocks; ++i)
        {
            float f1 = (float)i / (float)this.numberOfBlocks;
            double d6 = d0 + (d1 - d0) * (double)f1;
            double d7 = d4 + (d5 - d4) * (double)f1;
            double d8 = d2 + (d3 - d2) * (double)f1;
            double d9 = rand.nextDouble() * (double)this.numberOfBlocks / 16.0D;
            double d10 = (double)((float)Math.sin((float)Math.PI * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (double)((float)Math.sin((float)Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int j = (int)Math.floor(d6 - d10 / 2.0D);
            int k = (int)Math.floor(d7 - d11 / 2.0D);
            int l = (int)Math.floor(d8 - d10 / 2.0D);
            int i1 = (int)Math.floor(d6 + d10 / 2.0D);
            int j1 = (int)Math.floor(d7 + d11 / 2.0D);
            int k1 = (int)Math.floor(d8 + d10 / 2.0D);

            for (int l1 = j; l1 <= i1; ++l1)
            {
                double d12 = ((double)l1 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D)
                {
                    for (int i2 = k; i2 <= j1; ++i2)
                    {
                        double d13 = ((double)i2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D)
                        {
                            for (int j2 = l; j2 <= k1; ++j2)
                            {
                                double d14 = ((double)j2 + 0.5D - d8) / (d10 / 2.0D);

                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D)
                                {
                                    BlockPos blockpos = new BlockPos(l1, i2, j2);
                                    IBlockState state = worldIn.getBlockState(blockpos);

                                    if (state.getBlock() == this.fillerID && state.getBlock().getMetaFromState(state) == this.fillerMetadata)
                                    {
                                        if (!this.usingMetadata)
                                        {
                                            worldIn.setBlockState(blockpos, this.minableBlockId.getStateFromMeta(0), 2);
                                        }
                                        else
                                        {
                                            worldIn.setBlockState(blockpos, this.minableBlockId.getStateFromMeta(this.metadata), 2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
