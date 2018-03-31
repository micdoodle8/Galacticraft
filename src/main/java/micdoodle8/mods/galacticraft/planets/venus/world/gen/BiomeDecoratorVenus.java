package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.core.world.gen.WorldGenMinableMeta;
import micdoodle8.mods.galacticraft.planets.venus.ConfigManagerVenus;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class BiomeDecoratorVenus extends BiomeDecorator
{
    private WorldGenerator aluminumGen;
    private WorldGenerator copperGen;
    private WorldGenerator galenaGen;
    private WorldGenerator quartzGen;
    private WorldGenerator siliconGen;
    private WorldGenerator tinGen;
    private World worldObj;

    public BiomeDecoratorVenus()
    {
        this.aluminumGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 6, true, VenusBlocks.venusBlock, 1);
        this.copperGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 7, true, VenusBlocks.venusBlock, 1);
        this.galenaGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 8, true, VenusBlocks.venusBlock, 1);
        this.quartzGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 9, true, VenusBlocks.venusBlock, 1);
        this.siliconGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 10, true, VenusBlocks.venusBlock, 1);
        this.tinGen = new WorldGenMinableMeta(VenusBlocks.venusBlock, 6, 11, true, VenusBlocks.venusBlock, 1);
    }

    @Override
    public void decorate(World worldIn, Random random, BiomeGenBase biome, BlockPos blockPos)
    {
        if (this.worldObj != null)
        {
            throw new RuntimeException("Already decorating!!");
        }
        else
        {
            this.worldObj = worldIn;
            this.randomGenerator = random;
            this.field_180294_c = blockPos;
            this.generateVenus();
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

    private void generateVenus()
    {
        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Pre(this.worldObj, this.randomGenerator, field_180294_c));
        if (!ConfigManagerVenus.disableAluminumGen) this.genStandardOre(18, this.aluminumGen, 0, 60);
        if (!ConfigManagerVenus.disableCopperGen) this.genStandardOre(24, this.copperGen, 0, 60);
        if (!ConfigManagerVenus.disableGalenaGen) this.genStandardOre(18, this.galenaGen, 0, 60);
        if (!ConfigManagerVenus.disableQuartzGen) this.genStandardOre(26, this.quartzGen, 0, 60);
        if (!ConfigManagerVenus.disableSiliconGen) this.genStandardOre(4, this.siliconGen, 0, 60);
        if (!ConfigManagerVenus.disableTinGen) this.genStandardOre(22, this.tinGen, 0, 60);
        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Post(this.worldObj, this.randomGenerator, field_180294_c));
    }
}
