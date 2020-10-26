//package micdoodle8.mods.galacticraft.core.world.gen;
//
//import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.blocks.BlockBasicMoon;
//import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.pattern.BlockMatcher;
//import net.minecraft.block.Blocks;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.biome.BiomeDecorator;
//import net.minecraft.world.gen.feature.Feature;
//import net.minecraftforge.common.MinecraftForge;
//
//import java.util.Random;
//
//public class BiomeDecoratorMoon extends BiomeDecorator
//{
//    private World world;
//    private Random randomGenerator;
//
//    private Feature dirtGen;
//    private Feature cheeseGen;
//    private Feature copperGen;
//    private Feature tinGen;
//
//    public BiomeDecoratorMoon()
//    {
//        this.copperGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 4, 0, true, GCBlocks.blockMoon, 4);
//        this.tinGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 4, 1, true, GCBlocks.blockMoon, 4);
//        this.cheeseGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 3, 2, true, GCBlocks.blockMoon, 4);
//        this.dirtGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 32, 3, true, GCBlocks.blockMoon, 4);
//    }
//
//    @Override
//    public void decorate(World worldIn, Random random, Biome p_180292_3_, BlockPos pos)
//    {
//        if (this.world != null)
//        {
//            throw new RuntimeException("Already decorating!!");
//        }
//        else
//        {
//            this.world = worldIn;
//            this.randomGenerator = random;
//            this.chunkPos = pos;
//            this.generateMoon();
//            this.world = null;
//            this.randomGenerator = null;
//        }
//    }
//
//    private void genStandardOre(int amountPerChunk, Feature worldGenerator, int minY, int maxY)
//    {
//        for (int var5 = 0; var5 < amountPerChunk; ++var5)
//        {
//            BlockPos blockpos = this.chunkPos.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(maxY - minY) + minY, this.randomGenerator.nextInt(16));
//            worldGenerator.generate(this.world, this.randomGenerator, blockpos);
//        }
//    }
//
//    private void generateMoon()
//    {
//        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Pre(this.world, this.randomGenerator, chunkPos));
//        this.genStandardOre(20, this.dirtGen, 0, 200);
//        if (!ConfigManagerCore.disableCopperMoon.get())
//        {
//            this.genStandardOre(26, this.copperGen, 0, 60);
//        }
//        if (!ConfigManagerCore.disableTinMoon.get())
//        {
//            this.genStandardOre(23, this.tinGen, 0, 60);
//        }
//        if (!ConfigManagerCore.disableCheeseMoon.get())
//        {
//            this.genStandardOre(14, this.cheeseGen, 0, 85);
//        }
//        if (!ConfigManagerCore.disableSapphireMoon.get())
//        {
//            int count = 3 + this.randomGenerator.nextInt(6);
//            BlockState sapphire = GCBlocks.blockMoon.getDefaultState().with(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.ORE_SAPPHIRE);
//            for (int i = 0; i < count; i++)
//            {
//                BlockPos blockpos = this.chunkPos.add(this.randomGenerator.nextInt(16) + 8, this.randomGenerator.nextInt(28) + 4, this.randomGenerator.nextInt(16) + 8);
//
//                BlockState toReplace = this.world.getBlockState(blockpos);
//
//                if (toReplace.getBlock() == GCBlocks.blockMoon && toReplace.getBlock().isReplaceableOreGen(toReplace, this.world, blockpos, BlockMatcher.forBlock(Blocks.STONE)))
//                {
//                    this.world.setBlockState(blockpos, sapphire, 2);
//                }
//            }
//        }
//        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Post(this.world, this.randomGenerator, chunkPos));
//    }
//}
