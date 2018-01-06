package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import net.minecraft.block.BlockFalling;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;

import java.util.Random;

public class ChunkProviderOrbit extends ChunkGeneratorOverworld
{
    private final Random rand;

    private final World world;

    public ChunkProviderOrbit(World par1World, long par2, boolean par4)
    {
        super(par1World, par2, par4, "");
        this.rand = new Random(par2);
        this.world = par1World;
    }

    @Override
    public Chunk generateChunk(int par1, int par2)
    {
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);

        final Chunk var4 = new Chunk(this.world, chunkprimer, par1, par2);

        final byte[] biomesArray = var4.getBiomeArray();
        for (int i = 0; i < biomesArray.length; ++i)
        {
            biomesArray[i] = (byte) Biome.getIdForBiome(BiomeOrbit.space);
        }


        var4.generateSkylightMap();
        return var4;
    }

    @Override
    public void populate(int x, int z)
    {
        BlockFalling.fallInstantly = true;
        final int k = x * 16;
        final int l = z * 16;
        this.rand.setSeed(this.world.getSeed());
        final long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        final long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(x * i1 + z * j1 ^ this.world.getSeed());
        if (k == 0 && l == 0)
        {
            BlockPos pos = new BlockPos(k, 64, l);
            this.world.setBlockState(pos, GCBlocks.spaceStationBase.getDefaultState(), 2);

            final TileEntity var8 = this.world.getTileEntity(pos);

            if (var8 instanceof IMultiBlock)
            {
                ((IMultiBlock) var8).onCreate(this.world, pos);
            }

            new WorldGenSpaceStation().generate(this.world, this.rand, new BlockPos(k - 10, 62, l - 3));
        }
        BlockFalling.fallInstantly = false;
    }
}
