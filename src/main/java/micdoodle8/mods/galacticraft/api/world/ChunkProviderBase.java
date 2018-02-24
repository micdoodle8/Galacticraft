package micdoodle8.mods.galacticraft.api.world;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;

public abstract class ChunkProviderBase implements IChunkGenerator
{
    @Override
    @Nullable
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_)
    {
        return null;
    }
    
    // This is something connected with Ocean Monuments, and not used in Galacticraft.
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }
}
