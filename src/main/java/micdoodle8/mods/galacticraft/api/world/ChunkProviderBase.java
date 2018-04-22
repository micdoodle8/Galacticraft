package micdoodle8.mods.galacticraft.api.world;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

public abstract class ChunkProviderBase implements IChunkGenerator
{
    @Override
    @Nullable
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
    }
    
    // This is something connected with Ocean Monuments, and not used in Galacticraft.
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }
    
    /**
     * Override this if you want to implement an advancement using net.minecraft.advancements.critereon.LocationPredicate
     */
    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        return false;
    }
}
