package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.prefab.core.BlockMetaPair;
import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

public class BiomeGenBaseMars extends BiomeGenBaseGC
{
    public static final BiomeGenBase marsFlat = new BiomeGenFlatMars(ConfigManagerCore.biomeIDbase + 1).setBiomeName("Mars Flat");
    public static final BlockMetaPair BLOCK_TOP = new BlockMetaPair(MarsBlocks.marsBlock, (byte) 5);
    public static final BlockMetaPair BLOCK_FILL = new BlockMetaPair(MarsBlocks.marsBlock, (byte) 6);
    public static final BlockMetaPair BLOCK_LOWER = new BlockMetaPair(MarsBlocks.marsBlock, (byte) 9);

    BiomeGenBaseMars(int var1)
    {
        super(var1);
        this.rainfall = 0F;
    }

    @Override
    public BiomeGenBaseMars setColor(int var1)
    {
        return (BiomeGenBaseMars) super.setColor(var1);
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        this.fillerBlock = BLOCK_LOWER.getBlockState();
        this.topBlock = BLOCK_TOP.getBlockState();
        super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
