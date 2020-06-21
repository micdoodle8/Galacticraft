package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.dimension.chunk.OrbitGenSettings;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;

import java.util.List;

public class ChunkGeneratorOrbit extends ChunkGenerator<OrbitGenSettings>
{
//    private final Random rand;
//    private final World world;

    public ChunkGeneratorOrbit(IWorld worldIn, BiomeProvider dimension, OrbitGenSettings settingsIn)
    {
        super(worldIn, dimension, settingsIn);
//        this.rand = new Random(par2);
//        this.world = par1World;
    }

    @Override
    public void generateSurface(IChunk chunkIn)
    {

    }

    @Override
    public int getGroundHeight()
    {
        return this.world.getSeaLevel() + 1;
    }

    @Override
    public void makeBase(IWorld worldIn, IChunk chunkIn)
    {

    }

    @Override
    public int func_222529_a(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_)
    {
        return 0;
    }

    //    @Override
//    public Chunk generateChunk(int par1, int par2)
//    {
//        ChunkPrimer chunkprimer = new ChunkPrimer();
//        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
//
//        final Chunk var4 = new Chunk(this.world, chunkprimer, par1, par2);
//
//        final byte b = (byte) Biome.getIdForBiome( BiomeAdaptive.biomeDefault );
//        final byte[] biomesArray = var4.getBiomeArray();
//        for (int i = 0; i < biomesArray.length; ++i)
//        {
//            biomesArray[i] = b;
//        }
//
//        var4.generateSkylightMap();
//        return var4;
//    }
//
//    @Override
//    public void populate(int x, int z)
//    {
//        FallingBlock.fallInstantly = true;
//        final int k = x * 16;
//        final int l = z * 16;
//        this.rand.setSeed(this.world.getSeed());
//        final long i1 = this.rand.nextLong() / 2L * 2L + 1L;
//        final long j1 = this.rand.nextLong() / 2L * 2L + 1L;
//        this.rand.setSeed(x * i1 + z * j1 ^ this.world.getSeed());
//        if (k == 0 && l == 0)
//        {
//            BlockPos pos = new BlockPos(k, 64, l);
//            this.world.setBlockState(pos, GCBlocks.spaceStationBase.getDefaultState(), 2);
//
//            final TileEntity var8 = this.world.getTileEntity(pos);
//
//            if (var8 instanceof IMultiBlock)
//            {
//                ((IMultiBlock) var8).onCreate(this.world, pos);
//            }
//
//            new WorldGenSpaceStation().generate(this.world, this.rand, new BlockPos(k - 10, 62, l - 3));
//        }
//        FallingBlock.fallInstantly = false;
//    }

    @Override
    public List<SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos)
    {
        return null;
    }

//    @Override
//    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
//    {
//    }
}
