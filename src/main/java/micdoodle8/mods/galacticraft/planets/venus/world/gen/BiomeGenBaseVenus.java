package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class BiomeGenBaseVenus extends BiomeGenBaseGC
{
    public static final BiomeGenBase venusFlat = new BiomeGenFlatVenus(ConfigManagerCore.biomeIDbase + 4).setBiomeName("Venus Flat");
    public static final BiomeGenBase venusMountain = new BiomeGenVenusMountain(ConfigManagerCore.biomeIDbase + 5).setBiomeName("Venus Mountain");
    public static final BiomeGenBase venusValley = new BiomeGenVenusValley(ConfigManagerCore.biomeIDbase + 6).setBiomeName("Venus Valley");

    BiomeGenBaseVenus(int var1)
    {
        super(var1);
        this.rainfall = 0F;
        this.temperature = 4.0F;
    }

    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return new BiomeDecoratorVenus();
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
    {
    }

    public final void generateBiomeTerrainVenus(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180628_4_, int p_180628_5_, double p_180628_6_)
    {
        int i = worldIn.getSeaLevel();
        IBlockState topBlock = this.topBlock;
        IBlockState fillerBlock = this.fillerBlock;
        IBlockState stoneBlock = VenusBlocks.venusBlock.getDefaultState().withProperty(BlockBasicVenus.BASIC_TYPE_VENUS, BlockBasicVenus.EnumBlockBasicVenus.ROCK_HARD);
        int j = -1;
        int k = (int)(p_180628_6_ / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = p_180628_4_ & 15;
        int i1 = p_180628_5_ & 15;

        for (int j1 = 255; j1 >= 0; --j1)
        {
            if (j1 <= rand.nextInt(5))
            {
                chunkPrimerIn.setBlockState(i1, j1, l, Blocks.bedrock.getDefaultState());
            }
            else
            {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getBlock().getMaterial() == Material.air)
                {
                    j = -1;
                }
                else if (iblockstate2.getBlock() == VenusBlocks.venusBlock)
                {
                    if (j == -1)
                    {
                        if (k <= 0)
                        {
                            topBlock = null;
                            fillerBlock = stoneBlock;
                        }
                        else if (j1 >= i - 4 && j1 <= i + 1)
                        {
                            topBlock = this.topBlock;
                            fillerBlock = this.fillerBlock;
                        }

                        j = k;

                        if (j1 >= i - 1)
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, topBlock);
                        }
                        else if (j1 < i - 7 - k)
                        {
                            topBlock = null;
                            fillerBlock = stoneBlock;
                        }
                        else
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, fillerBlock);
                        }
                    }
                    else if (j > 0)
                    {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, fillerBlock);
                    }
                }
            }
        }
    }
}
