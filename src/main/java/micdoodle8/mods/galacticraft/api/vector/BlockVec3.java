package micdoodle8.mods.galacticraft.api.vector;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

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
    public static int chunkCacheDim = Integer.MAX_VALUE;
    private static int chunkCacheX = 1876000; // outside the world edge
    private static int chunkCacheZ = 1876000; // outside the world edge
    private static Chunk chunkCached_Client;
    public static int chunkCacheDim_Client = Integer.MAX_VALUE;
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
        this.x = (int) Math.floor(par1.posX);
        this.y = (int) Math.floor(par1.posY);
        this.z = (int) Math.floor(par1.posZ);
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
    public IBlockState getBlockState(World world)
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
                if (BlockVec3.chunkCacheX_Client == chunkx && BlockVec3.chunkCacheZ_Client == chunkz && BlockVec3.chunkCacheDim_Client == world.provider.getDimension() && BlockVec3.chunkCached_Client.isLoaded())
                {
                    return BlockVec3.chunkCached_Client.getBlockState(this.x & 15, this.y, this.z & 15);
                }
                else
                {
                    final Chunk chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
                    BlockVec3.chunkCached_Client = chunk;
                    BlockVec3.chunkCacheDim_Client = world.provider.getDimension();
                    BlockVec3.chunkCacheX_Client = chunkx;
                    BlockVec3.chunkCacheZ_Client = chunkz;
                    return chunk.getBlockState(this.x & 15, this.y, this.z & 15);
                }
            }
            else
            {
                // In a typical inner loop, 80% of the time consecutive calls to
                // this will be within the same chunk
                if (BlockVec3.chunkCacheX == chunkx && BlockVec3.chunkCacheZ == chunkz && BlockVec3.chunkCacheDim == world.provider.getDimension() && BlockVec3.chunkCached.isLoaded())
                {
                    return BlockVec3.chunkCached.getBlockState(this.x & 15, this.y, this.z & 15);
                }
                else
                {
                    final Chunk chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
                    BlockVec3.chunkCached = chunk;
                    BlockVec3.chunkCacheDim = world.provider.getDimension();
                    BlockVec3.chunkCacheX = chunkx;
                    BlockVec3.chunkCacheZ = chunkz;
                    return chunk.getBlockState(this.x & 15, this.y, this.z & 15);
                }
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Oxygen Sealer thread: Exception getting block type in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Requested block coordinates");
            crashreportcategory.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(new BlockPos(this.x, this.y, this.z)));
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
    public IBlockState getBlockState_noChunkLoad(World world)
    {
        if (this.y < 0 || this.y >= 256 || this.x < -30000000 || this.z < -30000000 || this.x >= 30000000 || this.z >= 30000000)
        {
            return null;
        }

        int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        try
        {
            if (world.getChunkProvider().getLoadedChunk(chunkx, chunkz) != null)
            {
                if (world.isRemote)
                {
                    if (BlockVec3.chunkCacheX_Client == chunkx && BlockVec3.chunkCacheZ_Client == chunkz && BlockVec3.chunkCacheDim_Client == world.provider.getDimension() && BlockVec3.chunkCached_Client.isLoaded())
                    {
                        return BlockVec3.chunkCached_Client.getBlockState(this.x & 15, this.y, this.z & 15);
                    }
                    else
                    {
                        final Chunk chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
                        BlockVec3.chunkCached_Client = chunk;
                        BlockVec3.chunkCacheDim_Client = world.provider.getDimension();
                        BlockVec3.chunkCacheX_Client = chunkx;
                        BlockVec3.chunkCacheZ_Client = chunkz;
                        return chunk.getBlockState(this.x & 15, this.y, this.z & 15);
                    }
                }
                else
                {
                    // In a typical inner loop, 80% of the time consecutive calls to
                    // this will be within the same chunk
                    if (BlockVec3.chunkCacheX == chunkx && BlockVec3.chunkCacheZ == chunkz && BlockVec3.chunkCacheDim == world.provider.getDimension() && BlockVec3.chunkCached.isLoaded())
                    {
                        return BlockVec3.chunkCached.getBlockState(this.x & 15, this.y, this.z & 15);
                    }
                    else
                    {
                        final Chunk chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
                        BlockVec3.chunkCached = chunk;
                        BlockVec3.chunkCacheDim = world.provider.getDimension();
                        BlockVec3.chunkCacheX = chunkx;
                        BlockVec3.chunkCacheZ = chunkz;
                        return chunk.getBlockState(this.x & 15, this.y, this.z & 15);
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
            crashreportcategory.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(new BlockPos(this.x, this.y, this.z)));
            throw new ReportedException(crashreport);
        }
    }

    public IBlockState getBlockState(IBlockAccess par1iBlockAccess)
    {
        return par1iBlockAccess.getBlockState(new BlockPos(this.x, this.y, this.z));
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
    public IBlockState getBlockStateSafe_noChunkLoad(World world)
    {
        if (this.y < 0 || this.y >= 256)
        {
            return null;
        }

        int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        try
        {
            if (world.getChunkProvider().getLoadedChunk(chunkx, chunkz) != null)
            {
                if (world.isRemote)
                {
                    if (BlockVec3.chunkCacheX_Client == chunkx && BlockVec3.chunkCacheZ_Client == chunkz && BlockVec3.chunkCacheDim_Client == world.provider.getDimension() && BlockVec3.chunkCached_Client.isLoaded())
                    {
                        return BlockVec3.chunkCached_Client.getBlockState(this.x & 15, this.y, this.z & 15);
                    }
                    else
                    {
                        final Chunk chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
                        BlockVec3.chunkCached_Client = chunk;
                        BlockVec3.chunkCacheDim_Client = world.provider.getDimension();
                        BlockVec3.chunkCacheX_Client = chunkx;
                        BlockVec3.chunkCacheZ_Client = chunkz;
                        return chunk.getBlockState(this.x & 15, this.y, this.z & 15);
                    }
                }
                else
                {
                    // In a typical inner loop, 80% of the time consecutive calls to
                    // this will be within the same chunk
                    if (BlockVec3.chunkCacheX == chunkx && BlockVec3.chunkCacheZ == chunkz && BlockVec3.chunkCacheDim == world.provider.getDimension() && BlockVec3.chunkCached.isLoaded())
                    {
                        return BlockVec3.chunkCached.getBlockState(this.x & 15, this.y, this.z & 15);
                    }
                    else
                    {
                        final Chunk chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
                        BlockVec3.chunkCached = chunk;
                        BlockVec3.chunkCacheDim = world.provider.getDimension();
                        BlockVec3.chunkCacheX = chunkx;
                        BlockVec3.chunkCacheZ = chunkz;
                        return chunk.getBlockState(this.x & 15, this.y, this.z & 15);
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
            crashreportcategory.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(new BlockPos(this.x, this.y, this.z)));
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

    public BlockVec3 modifyPositionFromSide(EnumFacing side, int amount)
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

    public BlockVec3 modifyPositionFromSide(EnumFacing side)
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
    public TileEntity getTileEntity(IBlockAccess world)
    {
        return world.getTileEntity(new BlockPos(this.x, this.y, this.z));
    }

    /**
     * No chunk load: returns null if chunk to side is unloaded
     */
    public TileEntity getTileEntityOnSide(World world, EnumFacing side)
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
        final BlockPos pos = new BlockPos(x, y, z);
        return world.isBlockLoaded(pos, false) ? world.getTileEntity(pos) : null;
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
        return world.isBlockLoaded(pos, false) ? world.getTileEntity(pos) : null;
    }

    /**
     * This will load the chunk to the side.
     */
    public boolean blockOnSideHasSolidFace(World world, int side)
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
            return false;
        }
        final BlockPos pos = new BlockPos(x, y, z);
        return world.getBlockState(pos).getBlock().isSideSolid(world.getBlockState(pos), world, pos, EnumFacing.getFront(side ^ 1));
    }

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
        final BlockPos pos = new BlockPos(x, y, z);
        return world.isBlockLoaded(pos, false) ? world.getBlockState(pos).getBlock() : null;
    }

    public int getBlockMetadata(IBlockAccess world)
    {
        final IBlockState state = world.getBlockState(new BlockPos(x, y, z));
        return state.getBlock().getMetaFromState(state);
    }

    public static BlockVec3 readFromNBT(NBTTagCompound nbtCompound)
    {
        final BlockVec3 tempVector = new BlockVec3();
        tempVector.x = nbtCompound.getInteger("x");
        tempVector.y = nbtCompound.getInteger("y");
        tempVector.z = nbtCompound.getInteger("z");
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

    public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("x", this.x);
        par1NBTTagCompound.setInteger("y", this.y);
        par1NBTTagCompound.setInteger("z", this.z);
        return par1NBTTagCompound;
    }

    public BlockVec3(NBTTagCompound par1NBTTagCompound)
    {
        this.x = par1NBTTagCompound.getInteger("x");
        this.y = par1NBTTagCompound.getInteger("y");
        this.z = par1NBTTagCompound.getInteger("z");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound, String prefix)
    {
        par1NBTTagCompound.setInteger(prefix + "_x", this.x);
        par1NBTTagCompound.setInteger(prefix + "_y", this.y);
        par1NBTTagCompound.setInteger(prefix + "_z", this.z);
        return par1NBTTagCompound;
    }

    public static BlockVec3 readFromNBT(NBTTagCompound par1NBTTagCompound, String prefix)
    {
        Integer readX = par1NBTTagCompound.getInteger(prefix + "_x");
        if (readX == null) return null;
        Integer readY = par1NBTTagCompound.getInteger(prefix + "_y");
        if (readY == null) return null;
        Integer readZ = par1NBTTagCompound.getInteger(prefix + "_z");
        if (readZ == null) return null;
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

    public void setBlock(World worldObj, IBlockState block)
    {
        worldObj.setBlockState(new BlockPos(x, y, z), block, 3);
    }

    public boolean blockExists(World world)
    {
        return world.isBlockLoaded(new BlockPos(this.x, this.y, this.z), false);
    }

    public void setSideDone(int side)
    {
        this.sideDoneBits |= 1 << side;
    }

    public TileEntity getTileEntityForce(World world)
    {
        int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        
        if (world.getChunkProvider().getLoadedChunk(chunkx, chunkz) != null)
            return world.getTileEntity(this.toBlockPos());
        
        Chunk chunk = ((ChunkProviderServer) world.getChunkProvider()).loadChunk(chunkx, chunkz);
        return chunk.getTileEntity(new BlockPos(this.x & 15, this.y, this.z & 15), Chunk.EnumCreateEntityType.IMMEDIATE);
    }

    public Vector3 midPoint()
    {
        return new Vector3(this.x + 0.5, this.y + 0.5, this.z + 0.5);
    }
}
