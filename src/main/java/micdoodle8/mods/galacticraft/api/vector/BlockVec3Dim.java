package micdoodle8.mods.galacticraft.api.vector;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;

/* BlockVec3 is similar to galacticraft.api.vector.Vector3?
 *
 * But for speed it uses integer arithmetic not doubles, for block coordinates
 * This reduces unnecessary type conversion between integers and doubles and back again.
 * (Minecraft block coordinates are always integers, only entity coordinates are doubles.)
 *
 */
public class BlockVec3Dim implements Cloneable
{
    public int x;
    public int y;
    public int z;
    public DimensionType dim;

    private static Chunk chunkCached;
    public static DimensionType chunkCacheDim = null;
    private static int chunkCacheX = 1876000; // outside the world edge
    private static int chunkCacheZ = 1876000; // outside the world edge
    // INVALID_VECTOR is used in cases where a null vector cannot be used
    public static final BlockVec3Dim INVALID_VECTOR = new BlockVec3Dim(-1, -1, -1, null);

    public BlockVec3Dim()
    {
        this(0, 0, 0, null);
    }

    public BlockVec3Dim(int x, int y, int z, DimensionType d)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = d;
    }

    public BlockVec3Dim(Entity par1)
    {
        this.x = (int) Math.floor(par1.getPosX());
        this.y = (int) Math.floor(par1.getPosY());
        this.z = (int) Math.floor(par1.getPosZ());
        this.dim = par1.dimension;
    }

    public BlockVec3Dim(TileEntity par1)
    {
        this.x = par1.getPos().getX();
        this.y = par1.getPos().getY();
        this.z = par1.getPos().getZ();
        this.dim = par1.getWorld().getDimension().getType();
    }

    public BlockVec3Dim(BlockPos pos, DimensionType dimensionId)
    {
        this(pos.getX(), pos.getY(), pos.getZ(), dimensionId);
    }

    /**
     * Makes a new copy of this Vector. Prevents variable referencing problems.
     */
    @Override
    public final BlockVec3Dim clone()
    {
        return new BlockVec3Dim(this.x, this.y, this.z, this.dim);
    }

    /**
     * Get block ID at the BlockVec3Dim coordinates, with a forced chunk load if
     * the coordinates are unloaded.  Only works server-LogicalSide.
     *
     * @return the block ID, or null if the y-coordinate is less than 0 or
     * greater than 256 or the x or z is outside the Minecraft worldmap.
     */
    public BlockState getBlockState()
    {
        if (this.y < 0 || this.y >= 256 || this.x < -30000000 || this.z < -30000000 || this.x >= 30000000 || this.z >= 30000000)
        {
            return null;
        }

        World world = getWorldForId(this.dim);
        if (world == null)
        {
            return null;
        }

        int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        try
        {
            // In a typical inner loop, 80% of the time consecutive calls to
            // this will be within the same chunk
            if (BlockVec3Dim.chunkCacheX == chunkx && BlockVec3Dim.chunkCacheZ == chunkz && BlockVec3Dim.chunkCacheDim == world.getDimension().getType() && BlockVec3Dim.chunkCached.loaded)
            {
                return BlockVec3Dim.chunkCached.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
            }
            else
            {
                Chunk chunk = world.getChunk(chunkx, chunkz);
                BlockVec3Dim.chunkCached = chunk;
                BlockVec3Dim.chunkCacheDim = world.getDimension().getType();
                BlockVec3Dim.chunkCacheX = chunkx;
                BlockVec3Dim.chunkCacheZ = chunkz;
                return chunk.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
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
     * @return the block ID, or null if the y-coordinate is less than 0 or
     * greater than 256 or the x or z is outside the Minecraft worldmap.
     * Returns Blocks.BEDROCK if the coordinates being checked are in an
     * unloaded chunk
     */
    public BlockState getBlockState_noChunkLoad()
    {
        if (this.y < 0 || this.y >= 256 || this.x < -30000000 || this.z < -30000000 || this.x >= 30000000 || this.z >= 30000000)
        {
            return null;
        }

        World world = getWorldForId(this.dim);
        if (world == null)
        {
            return null;
        }

        int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        try
        {
            if (world.getChunkProvider().isChunkLoaded(new ChunkPos(chunkx, chunkz)))
            {
                // In a typical inner loop, 80% of the time consecutive calls to
                // this will be within the same chunk
                if (BlockVec3Dim.chunkCacheX == chunkx && BlockVec3Dim.chunkCacheZ == chunkz && BlockVec3Dim.chunkCacheDim == world.getDimension().getType() && BlockVec3Dim.chunkCached.loaded)
                {
                    return BlockVec3Dim.chunkCached.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
                }
                else
                {
                    Chunk chunk = null;
                    chunk = world.getChunk(chunkx, chunkz);
                    BlockVec3Dim.chunkCached = chunk;
                    BlockVec3Dim.chunkCacheDim = world.getDimension().getType();
                    BlockVec3Dim.chunkCacheX = chunkx;
                    BlockVec3Dim.chunkCacheZ = chunkz;
                    return chunk.getBlockState(new BlockPos(this.x & 15, this.y, this.z & 15));
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

    public Block getBlock()
    {
        World world = this.getWorldForId(this.dim);
        if (world == null)
        {
            return null;
        }
        return world.getBlockState(new BlockPos(this.x, this.y, this.z)).getBlock();
    }

    public BlockVec3Dim modifyPositionFromSide(Direction side, int amount)
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

    public BlockVec3Dim newVecSide(int LogicalSide)
    {
        BlockVec3Dim vec = new BlockVec3Dim(this.x, this.y, this.z, this.dim);
        switch (LogicalSide)
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

    public BlockVec3Dim modifyPositionFromSide(Direction side)
    {
        return this.modifyPositionFromSide(side, 1);
    }

    @Override
    public int hashCode()
    {
        return (((this.z * 431 + this.x) * 379 + this.y) * 373 + this.dim.getId()) * 7;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof BlockVec3Dim)
        {
            BlockVec3Dim vector = (BlockVec3Dim) o;
            return this.x == vector.x && this.y == vector.y && this.z == vector.z && this.dim == vector.dim;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "BlockVec3 " + this.dim + ":[" + this.x + "," + this.y + "," + this.z + "]";
    }

    /**
     * This will load the chunk - use getTileEntityNoLoad() if just updating things if present.
     */
    public TileEntity getTileEntity()
    {
        World world = this.getWorldForId(this.dim);
        if (world == null)
        {
            return null;
        }
        return world.getTileEntity(new BlockPos(this.x, this.y, this.z));
    }

    public TileEntity getTileEntityNoLoad()
    {
        World world = getWorldForId(this.dim);
        if (world == null)
        {
            return null;
        }
        BlockPos pos = new BlockPos(this.x, this.y, this.z);
        if (world.isBlockLoaded(pos))
        {
            return world.getTileEntity(pos);
        }
        return null;
    }

    public BlockState getBlockMetadata()
    {
        World world = this.getWorldForId(this.dim);
        if (world == null)
        {
            return null;
        }
        return world.getBlockState(new BlockPos(this.x, this.y, this.z));
    }

    public static BlockVec3Dim readFromNBT(CompoundNBT nbt)
    {
        BlockVec3Dim tempVector = new BlockVec3Dim();
        tempVector.x = nbt.getInt("x");
        tempVector.y = nbt.getInt("y");
        tempVector.z = nbt.getInt("z");
        tempVector.dim = DimensionType.byName(new ResourceLocation(nbt.getString("dim_str")));
        return tempVector;
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt)
    {
        nbt.putInt("x", this.x);
        nbt.putInt("y", this.y);
        nbt.putInt("z", this.z);
        nbt.putString("dim_str", this.dim.getRegistryName().toString());
        return nbt;
    }

    public BlockVec3Dim(CompoundNBT nbt)
    {
        this.x = nbt.getInt("x");
        this.y = nbt.getInt("y");
        this.z = nbt.getInt("z");
        this.dim = DimensionType.byName(new ResourceLocation(nbt.getString("dim_str")));
    }

    public double getMagnitude()
    {
        return Math.sqrt(this.getMagnitudeSquared());
    }

    public int getMagnitudeSquared()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public void setBlock(BlockState block)
    {
        World world = this.getWorldForId(this.dim);
        if (world == null)
        {
            return;
        }
        world.setBlockState(new BlockPos(this.x, this.y, this.z), block, 3);
    }

    public boolean blockExists()
    {
        World world = this.getWorldForId(this.dim);
        if (world == null)
        {
            return false;
        }
        return world.isBlockLoaded(new BlockPos(this.x, this.y, this.z));
    }

    /**
     * It is up to the calling method to check that the dimension matches
     *
     * @param vector
     * @return
     */
    public int distanceSquared(BlockVec3 vector)
    {
        int var2 = vector.x - this.x;
        int var4 = vector.y - this.y;
        int var6 = vector.z - this.z;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    public BlockPos toBlockPos()
    {
        return new BlockPos(x, y, z);
    }

    private World getWorldForId(DimensionType dimensionID)
    {
        if (GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER)
        {
            return WorldUtil.getWorldForDimensionServer(dimensionID);
        }
        else
        {
            return getWorldForIdClient(dimensionID);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private World getWorldForIdClient(DimensionType dimensionID)
    {
        World world = Minecraft.getInstance().world;

        if (world != null && world.getDimension().getType() == dimensionID)
        {
            return world;
        }

        return null;
    }
}
