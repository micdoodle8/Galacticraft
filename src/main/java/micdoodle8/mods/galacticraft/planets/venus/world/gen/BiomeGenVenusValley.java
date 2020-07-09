//package micdoodle8.mods.galacticraft.planets.venus.world.gen;
//
//import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
//import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockVenusRock;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.material.Material;
//import net.minecraft.world.World;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.chunk.ChunkPrimer;
//import net.minecraftforge.common.BiomeDictionary;
//
//import java.util.Random;
//
//public class BiomeGenVenusValley extends BiomeVenus
//{
//    public BiomeGenVenusValley(BiomeProperties properties)
//    {
//        super(properties);
//    }
//
//    @Override
//    public void registerTypes(Biome b)
//    {
//        BiomeDictionary.addTypes(b, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
//    }
//
//    @Override
//    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
//    {
//        this.topBlock = VenusBlocks.venusBlock.getDefaultState().with(BlockVenusRock.BASIC_TYPE_VENUS, BlockVenusRock.EnumBlockBasicVenus.ROCK_SOFT);
//        this.fillerBlock = VenusBlocks.venusBlock.getDefaultState().with(BlockVenusRock.BASIC_TYPE_VENUS, BlockVenusRock.EnumBlockBasicVenus.ROCK_SOFT);
//        BlockState stoneBlock = VenusBlocks.venusBlock.getDefaultState().with(BlockVenusRock.BASIC_TYPE_VENUS, BlockVenusRock.EnumBlockBasicVenus.ROCK_HARD);
//        BlockState gravelBlock = VenusBlocks.venusBlock.getDefaultState().with(BlockVenusRock.BASIC_TYPE_VENUS, BlockVenusRock.EnumBlockBasicVenus.ROCK_VOLCANIC_DEPOSIT);
//
//        int i = worldIn.getSeaLevel();
//        BlockState topBlock = this.topBlock;
//        BlockState fillerBlock = this.fillerBlock;
//        int j = -1;
//        int k = (int)(p_180622_6_ / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
//        int l = p_180622_4_ & 15;
//        int i1 = p_180622_5_ & 15;
//
//        for (int j1 = 255; j1 >= 0; --j1)
//        {
//            if (j1 <= rand.nextInt(5))
//            {
//                chunkPrimerIn.setBlockState(i1, j1, l, Blocks.BEDROCK.getDefaultState());
//            }
//            else
//            {
//                BlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);
//
//                if (iblockstate2.getMaterial() == Material.AIR)
//                {
//                    j = -1;
//                }
//                else if (iblockstate2.getBlock() == VenusBlocks.venusBlock)
//                {
//                    if (j == -1)
//                    {
//                        if (k <= 0)
//                        {
//                            topBlock = null;
//                            fillerBlock = stoneBlock;
//                        }
//                        else if (j1 >= i - 4 && j1 <= i + 1)
//                        {
//                            topBlock = this.topBlock;
//                            fillerBlock = this.fillerBlock;
//                        }
//
//                        j = k;
//
//                        if (j1 >= i - 1)
//                        {
//                            chunkPrimerIn.setBlockState(i1, j1, l, topBlock);
//                        }
//                        else if (j1 < i - 7 - k)
//                        {
//                            topBlock = null;
//                            fillerBlock = stoneBlock;
//                            chunkPrimerIn.setBlockState(i1, j1, l, gravelBlock);
//                        }
//                        else
//                        {
//                            chunkPrimerIn.setBlockState(i1, j1, l, fillerBlock);
//                        }
//                    }
//                    else if (j > 0)
//                    {
//                        --j;
//                        chunkPrimerIn.setBlockState(i1, j1, l, fillerBlock);
//                    }
//                }
//            }
//        }
//    }
//}
