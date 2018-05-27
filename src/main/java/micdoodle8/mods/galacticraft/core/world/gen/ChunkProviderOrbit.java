package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.ChunkProviderBase;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;
import java.util.Random;

public class ChunkProviderOrbit extends ChunkProviderBase
{
    private final Random rand;

    private final World worldObj;

    public ChunkProviderOrbit(World par1World, long par2, boolean par4)
    {
        this.rand = new Random(par2);
        this.worldObj = par1World;
    }

    @Override
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    @Override
    public int getLoadedChunkCount()
    {
        return 0;
    }

    @Override
    public boolean saveChunks(boolean var1, IProgressUpdate var2)
    {
        return true;
    }

    @Override
    public boolean canSave()
    {
        return true;
    }

    @Override
    public Chunk provideChunk(int par1, int par2)
    {
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);

        final Chunk var4 = new Chunk(this.worldObj, chunkprimer, par1, par2);

        final byte[] biomesArray = var4.getBiomeArray();
        byte id = (byte) BiomeGenBaseOrbit.space.biomeID;
        for (int i = 0; i < biomesArray.length; ++i)
        {
            biomesArray[i] = id;
        }

        var4.generateSkylightMap();
        return var4;
    }

    @Override
    public boolean chunkExists(int par1, int par2)
    {
        return true;
    }

    @Override
    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
    {
        BlockFalling.fallInstantly = true;
        final int k = par2 * 16;
        final int l = par3 * 16;
        this.rand.setSeed(this.worldObj.getSeed());
        final long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        final long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(par2 * i1 + par3 * j1 ^ this.worldObj.getSeed());
        if (k == 0 && l == 0)
        {
            BlockPos pos = new BlockPos(k, 64, l);
            this.worldObj.setBlockState(pos, GCBlocks.spaceStationBase.getDefaultState(), 2);

            final TileEntity var8 = this.worldObj.getTileEntity(pos);

            if (var8 instanceof IMultiBlock)
            {
                ((IMultiBlock) var8).onCreate(this.worldObj, pos);
            }

            new WorldGenSpaceStation().generate(this.worldObj, this.rand, new BlockPos(k - 10, 62, l - 3));
        }
        BlockFalling.fallInstantly = false;
    }

    @Override
    public String makeString()
    {
        return "OrbitLevelSource";
    }

    @Override
    public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return null;
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
    {
    }
}
