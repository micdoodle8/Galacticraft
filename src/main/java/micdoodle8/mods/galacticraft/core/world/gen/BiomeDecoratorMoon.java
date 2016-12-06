package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class BiomeDecoratorMoon extends BiomeDecorator
{
    private World worldObj;
    private Random randomGenerator;

    private WorldGenerator dirtGen;
    private WorldGenerator cheeseGen;
    private WorldGenerator copperGen;
    private WorldGenerator tinGen;

    public BiomeDecoratorMoon()
    {
        this.copperGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 4, 0, true, GCBlocks.blockMoon, 4);
        this.tinGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 4, 1, true, GCBlocks.blockMoon, 4);
        this.cheeseGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 3, 2, true, GCBlocks.blockMoon, 4);
        this.dirtGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 32, 3, true, GCBlocks.blockMoon, 4);
    }

    @Override
    public void decorate(World worldIn, Random random, BiomeGenBase p_180292_3_, BlockPos pos)
    {
        if (this.worldObj != null)
        {
            throw new RuntimeException("Already decorating!!");
        }
        else
        {
            this.worldObj = worldIn;
            this.randomGenerator = random;
            this.field_180294_c = pos;
            this.generateMoon();
            this.worldObj = null;
            this.randomGenerator = null;
        }
    }

    private void genStandardOre(int amountPerChunk, WorldGenerator worldGenerator, int minY, int maxY)
    {
        for (int var5 = 0; var5 < amountPerChunk; ++var5)
        {
            BlockPos blockpos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(maxY - minY) + minY, this.randomGenerator.nextInt(16));
            worldGenerator.generate(this.worldObj, this.randomGenerator, blockpos);
        }
    }

    private void generateMoon()
    {
        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Pre(this.worldObj, this.randomGenerator, field_180294_c));
        this.genStandardOre(20, this.dirtGen, 0, 200);
        if (!ConfigManagerCore.disableCopperMoon)
        {
            this.genStandardOre(26, this.copperGen, 0, 60);
        }
        if (!ConfigManagerCore.disableTinMoon)
        {
            this.genStandardOre(23, this.tinGen, 0, 60);
        }
        if (!ConfigManagerCore.disableCheeseMoon)
        {
            this.genStandardOre(12, this.cheeseGen, 0, 128);
        }
        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Post(this.worldObj, this.randomGenerator, field_180294_c));
    }
}
