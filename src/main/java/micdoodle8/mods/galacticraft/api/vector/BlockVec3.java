package micdoodle8.mods.galacticraft.api.vector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerChunkProvider;

import javax.annotation.Nullable;

/* BlockVec3 is similar to galacticraft.api.vector.Vector3?
 *
 * But for speed it uses integer arithmetic not doubles, for block coordinates
 * This reduces unnecessary type conversion between integers and doubles and back again.
 * (Minecraft block coordinates are always integers, only entity coordinates are doubles.)
 *
 */
public class BlockVec3 implements Cloneable
{
    public int x;
    public int y;
    public int z;
    public int sideDoneBits = 0;
    private static Chunk chunkCached;
    public static DimensionType chunkCacheDim = null;
    private static int chunkCacheX = 1876000; // outside the world edge
    private static int chunkCacheZ = 1876000; // outside the world edge
    private static Chunk chunkCached_Client;
    public static DimensionType chunkCacheDim_Client = null;
    private static int chunkCacheX_Client = 1876000; // outside the world edge
    private static int chunkCacheZ_Client = 1876000; // outside the world edge
    // INVALID_VECTOR is used in cases where a null vector cannot be used
    public static final BlockVec3 INVALID_VECTOR = new BlockVec3(-1, -1, -1);

    public BlockVec3()
    {
        this(0, 0, 0);
    }

    public BlockVec3(BlockPos pos)
    {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockVec3(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockVec3(Entity par1)
    {
        this.x = (int) Math.floor(par1.getPosX());
        this.y = (int) Math.floor(par1.getPosY());
        this.z = (int) Math.floor(par1.getPosZ());
    }

    public BlockVec3(TileEntity par1)
    {
        this.x = par1.getPos().getX();
        this.y = par1.getPos().getY();
        this.z = par1.getPos().getZ();
    }

    /**
     * Makes a new copy of this Vector. Prevents variable referencing problems.
     */
    @Override
    public final BlockVec3 clone()
    {
        return new BlockVec3(this.x, this.y, this.z);
    }

    public BlockPos toBlockPos()
    {
        return new BlockPos(this.x, this.y, this.z);
    }

    /**
     * Get block ID at the BlockVec3 coordinates, with a forced chunk load if
     * the coordinates are unloaded.
     *
     * @param world
     * @return the block ID, or null if the y-coordinate is less than 0 or
     * greater than 256 or the x or z is outside the Minecraft worldmap.
     */
    public BlockState getBlockState(World world)
    {
        if (this.y < 0 || this.y >= 256 || this.x < -30000000 || this.z < -30000000 || this.x >= 30000000 || this.z >= 30000000)
        {
            return null;
        }

        int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        try
        {
            if (world.isRemote)
            {
                if (BlockVec3.chunkCacheX_Client == chunkx && BlockVec3.chunkCacheZ_Client == chunkz && BlockVec3.chunkCacheDim_Client == world.getDimension().getType() && BlockVec3.chunkCached_Client.loaded)
                {
                    return BlockVec3.chunkCached_Client.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                }
                else
                {
                    final Chunk chunk = world.getChunk(chunkx, chunkz);
                    BlockVec3.chunkCached_Client = chunk;
                    BlockVec3.chunkCacheDim_Client = world.getDimension().getType();
                    BlockVec3.chunkCacheX_Client = chunkx;
                    BlockVec3.chunkCacheZ_Client = chunkz;
                    return chunk.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                }
            }
            else
            {
                // In a typical inner loop, 80% of the time consecutive calls to
                // this will be within the same chunk
                if (BlockVec3.chunkCacheX == chunkx && BlockVec3.chunkCacheZ == chunkz && BlockVec3.chunkCacheDim == world.getDimension().getType() && BlockVec3.chunkCached.loaded)
                {
                    return BlockVec3.chunkCached.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                }
                else
                {
                    final Chunk chunk = world.getChunk(chunkx, chunkz);
                    BlockVec3.chunkCached = chunk;
                    BlockVec3.chunkCacheDim = world.getDimension().getType();
                    BlockVec3.chunkCacheX = chunkx;
                    BlockVec3.chunkCacheZ = chunkz;
                    return chunk.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                }
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Oxygen Sealer thread: Exception getting block type in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Requested block coordinates");
            crashreportcategory.addDetail("Location", CrashReportCategory.getCoordinateInfo(new BlockPos(this.x, this.y, this.z)));
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Get block ID at the BlockVec3 coordinates without forcing a chunk load.
     *
     * @param world
     * @return the block ID, or null if the y-coordinate is less than 0 or
     * greater than 256 or the x or z is outside the Minecraft worldmap.
     * Returns Blocks.BEDROCK if the coordinates being checked are in an
     * unloaded chunk
     */
    public BlockState getBlockState_noChunkLoad(World world)
    {
        if (this.y < 0 || this.y >= 256 || this.x < -30000000 || this.z < -30000000 || this.x >= 30000000 || this.z >= 30000000)
        {
            return null;
        }

        int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        try
        {
            if (world.getChunkProvider().isChunkLoaded(new ChunkPos(chunkx, chunkz)))
            {
                if (world.isRemote)
                {
                    if (BlockVec3.chunkCacheX_Client == chunkx && BlockVec3.chunkCacheZ_Client == chunkz && BlockVec3.chunkCacheDim_Client == world.getDimension().getType() && BlockVec3.chunkCached_Client.loaded)
                    {
                        return BlockVec3.chunkCached_Client.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                    }
                    else
                    {
                        final Chunk chunk = world.getChunk(chunkx, chunkz);
                        BlockVec3.chunkCached_Client = chunk;
                        BlockVec3.chunkCacheDim_Client = world.getDimension().getType();
                        BlockVec3.chunkCacheX_Client = chunkx;
                        BlockVec3.chunkCacheZ_Client = chunkz;
                        return chunk.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                    }
                }
                else
                {
                    // In a typical inner loop, 80% of the time consecutive calls to
                    // this will be within the same chunk
                    if (BlockVec3.chunkCacheX == chunkx && BlockVec3.chunkCacheZ == chunkz && BlockVec3.chunkCacheDim == world.getDimension().getType() && BlockVec3.chunkCached.loaded)
                    {
                        return BlockVec3.chunkCached.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                    }
                    else
                    {
                        final Chunk chunk = world.getChunk(chunkx, chunkz);
                        BlockVec3.chunkCached = chunk;
                        BlockVec3.chunkCacheDim = world.getDimension().getType();
                        BlockVec3.chunkCacheX = chunkx;
                        BlockVec3.chunkCacheZ = chunkz;
                        return chunk.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                    }
                }
            }
            //Chunk doesn't exist - meaning, it is not loaded
            return Blocks.BEDROCK.getDefaultState();
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Oxygen Sealer thread: Exception getting block type in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Requested block coordinates");
            crashreportcategory.addDetail("Location", CrashReportCategory.getCoordinateInfo(new BlockPos(this.x, this.y, this.z)));
            throw new ReportedException(crashreport);
        }
    }

    public BlockState getBlockState(IBlockReader par1IBlockReader)
    {
        return par1IBlockReader.getBlockState(new BlockPos(this.x, this.y, this.z));
    }

    /**
     * Get block ID at the BlockVec3 coordinates without forcing a chunk load.
     * Only call this 'safe' version if x and z coordinates are within the
     * Minecraft world map (-30m to +30m)
     *
     * @param world
     * @return the block ID, or null if the y-coordinate is less than 0 or
     * greater than 256. Returns Blocks.BEDROCK if the coordinates being
     * checked are in an unloaded chunk
     */
    @Nullable
    public BlockState getBlockStateSafe_noChunkLoad(World world)
    {
        if (this.y < 0 || this.y >= 256)
        {
            return null;
        }

        int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        try
        {
            if (world.getChunkProvider().isChunkLoaded(new ChunkPos(chunkx, chunkz)))
            {
                if (world.isRemote)
                {
                    if (BlockVec3.chunkCacheX_Client == chunkx && BlockVec3.chunkCacheZ_Client == chunkz && BlockVec3.chunkCacheDim_Client == world.getDimension().getType() && BlockVec3.chunkCached_Client.loaded)
                    {
                        return BlockVec3.chunkCached_Client.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                    }
                    else
                    {
                        final Chunk chunk = world.getChunk(chunkx, chunkz);
                        BlockVec3.chunkCached_Client = chunk;
                        BlockVec3.chunkCacheDim_Client = world.getDimension().getType();
                        BlockVec3.chunkCacheX_Client = chunkx;
                        BlockVec3.chunkCacheZ_Client = chunkz;
                        return chunk.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                    }
                }
                else
                {
                    // In a typical inner loop, 80% of the time consecutive calls to
                    // this will be within the same chunk
                    if (BlockVec3.chunkCacheX == chunkx && BlockVec3.chunkCacheZ == chunkz && BlockVec3.chunkCacheDim == world.getDimension().getType() && BlockVec3.chunkCached.loaded)
                    {
                        return BlockVec3.chunkCached.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                    }
                    else
                    {
                        final Chunk chunk = world.getChunk(chunkx, chunkz);
                        BlockVec3.chunkCached = chunk;
                        BlockVec3.chunkCacheDim = world.getDimension().getType();
                        BlockVec3.chunkCacheX = chunkx;
                        BlockVec3.chunkCacheZ = chunkz;
                        return chunk.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                    }
                }
            }
            //Chunk doesn't exist - meaning, it is not loaded
            return Blocks.BEDROCK.getDefaultState();
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Oxygen Sealer thread: Exception getting block type in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Requested block coordinates");
            crashreportcategory.addDetail("Location", CrashReportCategory.getCoordinateInfo(new BlockPos(this.x, this.y, this.z)));
            throw new ReportedException(crashreport);
        }
    }

    public BlockVec3 translate(BlockVec3 par1)
    {
        this.x += par1.x;
        this.y += par1.y;
        this.z += par1.z;
        return this;
    }

    public BlockVec3 translate(int par1x, int par1y, int par1z)
    {
        this.x += par1x;
        this.y += par1y;
        this.z += par1z;
        return this;
    }

    public static BlockVec3 add(BlockVec3 par1, BlockVec3 a)
    {
        return new BlockVec3(par1.x + a.x, par1.y + a.y, par1.z + a.z);
    }

    public BlockVec3 subtract(BlockVec3 par1)
    {
        this.x -= par1.x;
        this.y -= par1.y;
        this.z -= par1.z;

        return this;
    }

    public BlockVec3 scale(int par1)
    {
        this.x *= par1;
        this.y *= par1;
        this.z *= par1;

        return this;
    }

    public BlockVec3 modifyPositionFromSide(Direction side, int amount)
    {
        switch (side.ordinal())
        {
        case 0:
            this.y -= amount;
            break;
        case 1:
            this.y += amount;
            break;
        case 2:
            this.z -= amount;
            break;
        case 3:
            this.z += amount;
            break;
        case 4:
            this.x -= amount;
            break;
        case 5:
            this.x += amount;
            break;
        }
        return this;
    }

    public BlockVec3 newVecSide(int side)
    {
        final BlockVec3 vec = new BlockVec3(this.x, this.y, this.z);
        vec.sideDoneBits = (1 << (side ^ 1)) + (side << 6);
        switch (side)
        {
        case 0:
            vec.y--;
            return vec;
        case 1:
            vec.y++;
            return vec;
        case 2:
            vec.z--;
            return vec;
        case 3:
            vec.z++;
            return vec;
        case 4:
            vec.x--;
            return vec;
        case 5:
            vec.x++;
            return vec;
        }
        return vec;
    }

    public BlockVec3 newVecSide(Direction direction)
    {
        final BlockVec3 vec = new BlockVec3(this.x, this.y, this.z);
        vec.sideDoneBits = (1 << (direction.getIndex() ^ 1)) + (direction.getIndex() << 6);
        switch (direction)
        {
        case DOWN:
            vec.y--;
            return vec;
        case UP:
            vec.y++;
            return vec;
        case NORTH:
            vec.z--;
            return vec;
        case SOUTH:
            vec.z++;
            return vec;
        case WEST:
            vec.x--;
            return vec;
        case EAST:
            vec.x++;
            return vec;
        }
        return vec;
    }

    public BlockVec3 modifyPositionFromSide(Direction side)
    {
        return this.modifyPositionFromSide(side, 1);
    }

    @Override
    public int hashCode()
    {
        // Upgraded hashCode calculation from the one in VecDirPair to something
        // a bit stronger and faster
        return ((this.y * 379 + this.x) * 373 + this.z) * 7;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof BlockVec3)
        {
            final BlockVec3 vector = (BlockVec3) o;
            return this.x == vector.x && this.y == vector.y && this.z == vector.z;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "[" + this.x + "," + this.y + "," + this.z + "]";
    }

    /**
     * This will load the chunk.
     */
    public TileEntity getTileEntity(IBlockReader world)
    {
        return world.getTileEntity(new BlockPos(this.x, this.y, this.z));
    }

    /**
     * No chunk load: returns null if chunk to side is unloaded
     */
    public TileEntity getTileEntityOnSide(World world, Direction side)
    {
        int x = this.x;
        int y = this.y;
        int z = this.z;
        switch (side.ordinal())
        {
        case 0:
            y--;
            break;
        case 1:
            y++;
            break;
        case 2:
            z--;
            break;
        case 3:
            z++;
            break;
        case 4:
            x--;
            break;
        case 5:
            x++;
            break;
        default:
            return null;
        }
        return world.chunkExists(x >> 4, z >> 4) ? world.getTileEntity(new BlockPos(x, y, z)) : null;
    }

    /**
     * No chunk load: returns null if chunk to side is unloaded
     */
    public TileEntity getTileEntityOnSide(World world, int side)
    {
        int x = this.x;
        int y = this.y;
        int z = this.z;
        switch (side)
        {
        case 0:
            y--;
            break;
        case 1:
            y++;
            break;
        case 2:
            z--;
            break;
        case 3:
            z++;
            break;
        case 4:
            x--;
            break;
        case 5:
            x++;
            break;
        default:
            return null;
        }
        final BlockPos pos = new BlockPos(x, y, z);
        return world.chunkExists(x >> 4, z >> 4) ? world.getTileEntity(new BlockPos(x, y, z)) : null;
    }

//    /**
//     * This will load the chunk to the side.
//     */
//    public boolean blockOnSideHasSolidFace(World world, int side)
//    {
//        int x = this.x;
//        int y = this.y;
//        int z = this.z;
//        switch (side)
//        {
//        case 0:
//            y--;
//            break;
//        case 1:
//            y++;
//            break;
//        case 2:
//            z--;
//            break;
//        case 3:
//            z++;
//            break;
//        case 4:
//            x--;
//            break;
//        case 5:
//            x++;
//            break;
//        default:
//            return false;
//        }
//        final BlockPos pos = new BlockPos(x, y, z);
//        return world.getBlockState(pos).getBlock().isSideSolid(world.getBlockState(pos), world, pos, Direction.byIndex(side ^ 1));
//    }

    /**
     * No chunk load: returns null if chunk is unloaded
     */
    public Block getBlockOnSide(World world, int side)
    {
        int x = this.x;
        int y = this.y;
        int z = this.z;
        switch (side)
        {
        case 0:
            y--;
            break;
        case 1:
            y++;
            break;
        case 2:
            z--;
            break;
        case 3:
            z++;
            break;
        case 4:
            x--;
            break;
        case 5:
            x++;
            break;
        default:
            return null;
        }
        return world.chunkExists(x >> 4, z >> 4) ? world.getBlockState(new BlockPos(x, y, z)).getBlock() : null;
    }

//    public int getBlockMetadata(IBlockReader world)
//    {
//        final BlockState state = world.getBlockState(new BlockPos(x, y, z));
//        return state.getBlock().getMetaFromState(state);
//    }

    public static BlockVec3 read(CompoundNBT nbtCompound)
    {
        final BlockVec3 tempVector = new BlockVec3();
        tempVector.x = nbtCompound.getInt("x");
        tempVector.y = nbtCompound.getInt("y");
        tempVector.z = nbtCompound.getInt("z");
        return tempVector;
    }

    public int distanceTo(BlockVec3 vector)
    {
        int var2 = vector.x - this.x;
        int var4 = vector.y - this.y;
        int var6 = vector.z - this.z;
        return MathHelper.floor(Math.sqrt(var2 * var2 + var4 * var4 + var6 * var6));
    }

    public int distanceSquared(BlockVec3 vector)
    {
        int var2 = vector.x - this.x;
        int var4 = vector.y - this.y;
        int var6 = vector.z - this.z;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    public CompoundNBT write(CompoundNBT par1NBTTagCompound)
    {
        par1NBTTagCompound.putInt("x", this.x);
        par1NBTTagCompound.putInt("y", this.y);
        par1NBTTagCompound.putInt("z", this.z);
        return par1NBTTagCompound;
    }

    public BlockVec3(CompoundNBT par1NBTTagCompound)
    {
        this.x = par1NBTTagCompound.getInt("x");
        this.y = par1NBTTagCompound.getInt("y");
        this.z = par1NBTTagCompound.getInt("z");
    }

    public CompoundNBT write(CompoundNBT par1NBTTagCompound, String prefix)
    {
        par1NBTTagCompound.putInt(prefix + "_x", this.x);
        par1NBTTagCompound.putInt(prefix + "_y", this.y);
        par1NBTTagCompound.putInt(prefix + "_z", this.z);
        return par1NBTTagCompound;
    }

    public static BlockVec3 read(CompoundNBT nbt, String prefix)
    {
        if (!nbt.contains(prefix + "_x") || !nbt.contains(prefix + "_y") || !nbt.contains(prefix + "_z"))
        {
            return null;
        }
        int readX = nbt.getInt(prefix + "_x");
        int readY = nbt.getInt(prefix + "_y");
        int readZ = nbt.getInt(prefix + "_z");
        return new BlockVec3(readX, readY, readZ);
    }

    public double getMagnitude()
    {
        return Math.sqrt(this.getMagnitudeSquared());
    }

    public int getMagnitudeSquared()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public void setBlock(World worldObj, BlockState block)
    {
        worldObj.setBlockState(new BlockPos(x, y, z), block, 3);
    }

    public boolean blockExists(World world)
    {
        return world.chunkExists(this.x >> 4, this.z >> 4);
    }

    public void setSideDone(int side)
    {
        this.sideDoneBits |= 1 << side;
    }

//    public TileEntity getTileEntityForce(World world)
//    {
//        int chunkx = this.x >> 4;
//        int chunkz = this.z >> 4;
//
//        if (world.getChunkProvider().isChunkLoaded(new ChunkPos(chunkx, chunkz)))
//            return world.getTileEntity(this.toBlockPos());
//
//        Chunk chunk = ((ServerChunkProvider) world.getChunkProvider()).loadChunk(chunkx, chunkz);
//        return chunk.getTileEntity(new BlockPos(this.x & 15, this.y, this.z & 15), Chunk.EnumCreateEntityType.IMMEDIATE);
//    }

    public Vector3 midPoint()
    {
        return new Vector3(this.x + 0.5F, this.y + 0.5F, this.z + 0.5F);
    }
}
