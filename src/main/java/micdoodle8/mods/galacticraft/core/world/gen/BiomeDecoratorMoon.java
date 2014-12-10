package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class BiomeDecoratorMoon
{
    private World worldObj;
    private Random randomGenerator;

    private int chunkX;
    private int chunkZ;

    private WorldGenerator dirtGen;
    private WorldGenerator cheeseGen;
    private WorldGenerator copperGen;
    private WorldGenerator tinGen;

    public BiomeDecoratorMoon(BiomeGenBase par1BiomeGenBase)
    {
        this.copperGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 4, 0, true, GCBlocks.blockMoon, 4);
        this.tinGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 4, 1, true, GCBlocks.blockMoon, 4);
        this.cheeseGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 3, 2, true, GCBlocks.blockMoon, 4);
        this.dirtGen = new WorldGenMinableMeta(GCBlocks.blockMoon, 32, 3, true, GCBlocks.blockMoon, 4);
    }

    public void decorate(World worldObj, Random rand, int chunkX, int chunkZ)
    {
        if (this.worldObj != null)
        {
            throw new RuntimeException("Already decorating!!");
        }
        else
        {
            this.worldObj = worldObj;
            this.randomGenerator = rand;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.generateMoon();
            this.worldObj = null;
            this.randomGenerator = null;
        }
    }

    void genStandardOre1(int amountPerChunk, WorldGenerator worldGenerator, int minY, int maxY)
    {
        for (int var5 = 0; var5 < amountPerChunk; ++var5)
        {
            final int var6 = this.chunkX + this.randomGenerator.nextInt(16);
            final int var7 = this.randomGenerator.nextInt(maxY - minY) + minY;
            final int var8 = this.chunkZ + this.randomGenerator.nextInt(16);
            worldGenerator.generate(this.worldObj, this.randomGenerator, var6, var7, var8);
        }
    }

    void generateMoon()
    {
        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Pre(this.worldObj, this.randomGenerator, this.chunkX, this.chunkZ));
        this.genStandardOre1(20, this.dirtGen, 0, 200);
        if (!ConfigManagerCore.disableCopperMoon)
        {
            this.genStandardOre1(26, this.copperGen, 0, 60);
        }
        if (!ConfigManagerCore.disableTinMoon)
        {
            this.genStandardOre1(23, this.tinGen, 0, 60);
        }
        if (!ConfigManagerCore.disableCheeseMoon)
        {
            this.genStandardOre1(12, this.cheeseGen, 0, 128);
        }
        MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Post(this.worldObj, this.randomGenerator, this.chunkX, this.chunkZ));
    }
}
