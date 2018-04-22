package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class OverworldGenerator implements IWorldGenerator
{
    private final int amountPerChunk;
    private final int maxGenerateLevel;
    private final int minGenerateLevel;
    private final int amountPerVein;
    private final Block oreBlock;
    private final int metadata;

    public OverworldGenerator(Block oreBlock, int metadata, int amountPerChunk, int minGenLevel, int maxGenLevel, int amountPerVein)
    {
        this.oreBlock = oreBlock;
        this.metadata = metadata;
        this.amountPerChunk = amountPerChunk;
        this.minGenerateLevel = minGenLevel;
        this.maxGenerateLevel = maxGenLevel;
        this.amountPerVein = amountPerVein;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (!(world.provider instanceof IGalacticraftWorldProvider))
        {
            for (int i = 0; i < this.amountPerChunk; i++)
            {
                int x = chunkX * 16 + random.nextInt(16);
                int z = chunkZ * 16 + random.nextInt(16);
                int y = random.nextInt(Math.max(this.maxGenerateLevel - this.minGenerateLevel, 0)) + this.minGenerateLevel;
                this.generateOre(world, random, x, y, z);
            }
        }
    }

    private boolean generateOre(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        float var6 = par2Random.nextFloat() * (float) Math.PI;
        double var7 = par3 + 8 + MathHelper.sin(var6) * this.amountPerVein / 8.0F;
        double var9 = par3 + 8 - MathHelper.sin(var6) * this.amountPerVein / 8.0F;
        double var11 = par5 + 8 + MathHelper.cos(var6) * this.amountPerVein / 8.0F;
        double var13 = par5 + 8 - MathHelper.cos(var6) * this.amountPerVein / 8.0F;
        double var15 = par4 + par2Random.nextInt(3) - 2;
        double var17 = par4 + par2Random.nextInt(3) - 2;

        for (int var19 = 0; var19 <= this.amountPerVein; ++var19)
        {
            double var20 = var7 + (var9 - var7) * var19 / this.amountPerVein;
            double var22 = var15 + (var17 - var15) * var19 / this.amountPerVein;
            double var24 = var11 + (var13 - var11) * var19 / this.amountPerVein;
            double var26 = par2Random.nextDouble() * this.amountPerVein / 16.0D;
            double var28 = (MathHelper.sin(var19 * (float) Math.PI / this.amountPerVein) + 1.0F) * var26 + 1.0D;
            double var30 = (MathHelper.sin(var19 * (float) Math.PI / this.amountPerVein) + 1.0F) * var26 + 1.0D;
            int var32 = MathHelper.floor(var20 - var28 / 2.0D);
            int var33 = MathHelper.floor(var22 - var30 / 2.0D);
            int var34 = MathHelper.floor(var24 - var28 / 2.0D);
            int var35 = MathHelper.floor(var20 + var28 / 2.0D);
            int var36 = MathHelper.floor(var22 + var30 / 2.0D);
            int var37 = MathHelper.floor(var24 + var28 / 2.0D);

            for (int var38 = var32; var38 <= var35; ++var38)
            {
                double var39 = (var38 + 0.5D - var20) / (var28 / 2.0D);

                if (var39 * var39 < 1.0D)
                {
                    for (int var41 = var33; var41 <= var36; ++var41)
                    {
                        double var42 = (var41 + 0.5D - var22) / (var30 / 2.0D);

                        if (var39 * var39 + var42 * var42 < 1.0D)
                        {
                            for (int var44 = var34; var44 <= var37; ++var44)
                            {
                                double var45 = (var44 + 0.5D - var24) / (var28 / 2.0D);

                                BlockPos pos = new BlockPos(var38, var41, var44);
                                IBlockState state = par1World.getBlockState(pos);
                                if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0D && state.getBlock().isReplaceableOreGen(state, par1World, pos, BlockMatcher.forBlock(Blocks.STONE)))
                                {
                                    par1World.setBlockState(new BlockPos(var38, var41, var44), this.oreBlock.getStateFromMeta(this.metadata), 2);
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
