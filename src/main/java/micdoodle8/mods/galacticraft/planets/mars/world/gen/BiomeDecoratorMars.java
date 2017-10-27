package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.core.world.gen.WorldGenMinableMeta;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;

public class BiomeDecoratorMars extends BiomeDecorator
{
    private WorldGenerator dirtGen;
    private WorldGenerator deshGen;
    private WorldGenerator tinGen;
    private WorldGenerator copperGen;
    private WorldGenerator ironGen;
    private WorldGenerator iceGen;
    private World currentWorld;

    public BiomeDecoratorMars()
    {
        this.copperGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 4, 0, true, MarsBlocks.marsBlock, 9);
        this.tinGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 4, 1, true, MarsBlocks.marsBlock, 9);
        this.deshGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 6, 2, true, MarsBlocks.marsBlock, 9);
        this.ironGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 8, 3, true, MarsBlocks.marsBlock, 9);
        this.dirtGen = new WorldGenMinableMeta(MarsBlocks.marsBlock, 32, 6, true, MarsBlocks.marsBlock, 9);
        this.iceGen = new WorldGenMinableMeta(Blocks.ice, 18, 0, true, MarsBlocks.marsBlock, 6);
    }

    @Override
    public void decorate(World worldIn, Random random, BiomeGenBase biome, BlockPos blockPos)
    {
        if (this.currentWorld != null)
        {
            throw new RuntimeException("Already decorating!!");
        }
        else
        {
            this.currentWorld = worldIn;
            this.field_180294_c = blockPos;
            this.generateMars(random);
            this.currentWorld = null;
        }
    }

    private void genStandardOre(int amountPerChunk, WorldGenerator worldGenerator, int minY, int maxY, Random rand)
    {
        for (int var5 = 0; var5 < amountPerChunk; ++var5)
        {
            BlockPos pos = this.field_180294_c.add(rand.nextInt(16), rand.nextInt(maxY - minY) + minY, rand.nextInt(16));
            worldGenerator.generate(this.currentWorld, rand, pos);
        }
    }

    private void generateMars(Random random)
    {
        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Pre(this.currentWorld, random, this.field_180294_c));
        this.genStandardOre(4, this.iceGen, 60, 120, random);
        this.genStandardOre(20, this.dirtGen, 0, 200, random);

        if (!ConfigManagerMars.disableDeshGen)
        {
            this.genStandardOre(15, this.deshGen, 20, 64, random);
        }
        if (!ConfigManagerMars.disableCopperGen)
        {
            this.genStandardOre(26, this.copperGen, 0, 60, random);
        }
        if (!ConfigManagerMars.disableTinGen)
        {
            this.genStandardOre(23, this.tinGen, 0, 60, random);
        }
        if (!ConfigManagerMars.disableIronGen)
        {
            this.genStandardOre(20, this.ironGen, 0, 64, random);
        }
        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Post(this.currentWorld, random, this.field_180294_c));
    }
}
