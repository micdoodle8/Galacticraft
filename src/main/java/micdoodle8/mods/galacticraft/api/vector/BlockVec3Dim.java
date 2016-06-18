package micdoodle8.mods.galacticraft.api.vector;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

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
    public int dim;
    private static Chunk chunkCached;
    public static int chunkCacheDim = Integer.MAX_VALUE;
    private static int chunkCacheX = 1876000; // outside the world edge
    private static int chunkCacheZ = 1876000; // outside the world edge
    // INVALID_VECTOR is used in cases where a null vector cannot be used
    public static final BlockVec3Dim INVALID_VECTOR = new BlockVec3Dim(-1, -1, -1, -2);
	
    public BlockVec3Dim()
    {
        this(0, 0, 0, 0);
    }

    public BlockVec3Dim(int x, int y, int z, int d)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = d;
    }

    public BlockVec3Dim(Entity par1)
    {
        this.x = (int) Math.floor(par1.posX);
        this.y = (int) Math.floor(par1.posY);
        this.z = (int) Math.floor(par1.posZ);
        this.dim = par1.dimension;
    }

    public BlockVec3Dim(TileEntity par1)
    {
        this.x = par1.xCoord;
        this.y = par1.yCoord;
        this.z = par1.zCoord;
        this.dim = par1.getWorldObj().provider.dimensionId;
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
     * the coordinates are unloaded.  Only works server-side.
     *
     * @return the block ID, or null if the y-coordinate is less than 0 or
     * greater than 256 or the x or z is outside the Minecraft worldmap.
     */
    public Block getBlockID()
    {
        if (this.y < 0 || this.y >= 256 || this.x < -30000000 || this.z < -30000000 || this.x >= 30000000 || this.z >= 30000000)
        {
            return null;
        }

   		World world = getWorldForId(this.dim);
   		if (world == null) return null;
   
   		int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        try
        {
            // In a typical inner loop, 80% of the time consecutive calls to
            // this will be within the same chunk
            if (BlockVec3Dim.chunkCacheX == chunkx && BlockVec3Dim.chunkCacheZ == chunkz && BlockVec3Dim.chunkCacheDim == world.provider.dimensionId && BlockVec3Dim.chunkCached.isChunkLoaded)
            {
                return BlockVec3Dim.chunkCached.getBlock(this.x & 15, this.y, this.z & 15);
            }
            else
            {
                Chunk chunk = null;
                chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
                BlockVec3Dim.chunkCached = chunk;
                BlockVec3Dim.chunkCacheDim = world.provider.dimensionId;
                BlockVec3Dim.chunkCacheX = chunkx;
                BlockVec3Dim.chunkCacheZ = chunkz;
                return chunk.getBlock(this.x & 15, this.y, this.z & 15);
            }
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Oxygen Sealer thread: Exception getting block type in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Requested block coordinates");
            crashreportcategory.addCrashSection("Location", CrashReportCategory.getLocationInfo(this.x, this.y, this.z));
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Get block ID at the BlockVec3 coordinates without forcing a chunk load.
     *
     * @return the block ID, or null if the y-coordinate is less than 0 or
     * greater than 256 or the x or z is outside the Minecraft worldmap.
     * Returns Blocks.bedrock if the coordinates being checked are in an
     * unloaded chunk
     */
    public Block getBlockID_noChunkLoad()
    {
        if (this.y < 0 || this.y >= 256 || this.x < -30000000 || this.z < -30000000 || this.x >= 30000000 || this.z >= 30000000)
        {
            return null;
        }

   		World world = getWorldForId(this.dim);
   		if (world == null) return null;

   		int chunkx = this.x >> 4;
        int chunkz = this.z >> 4;
        try
        {
            if (world.getChunkProvider().chunkExists(chunkx, chunkz))
            {
                // In a typical inner loop, 80% of the time consecutive calls to
                // this will be within the same chunk
                if (BlockVec3Dim.chunkCacheX == chunkx && BlockVec3Dim.chunkCacheZ == chunkz && BlockVec3Dim.chunkCacheDim == world.provider.dimensionId && BlockVec3Dim.chunkCached.isChunkLoaded)
                {
                    return BlockVec3Dim.chunkCached.getBlock(this.x & 15, this.y, this.z & 15);
                }
                else
                {
                    Chunk chunk = null;
                    chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
                    BlockVec3Dim.chunkCached = chunk;
                    BlockVec3Dim.chunkCacheDim = world.provider.dimensionId;
                    BlockVec3Dim.chunkCacheX = chunkx;
                    BlockVec3Dim.chunkCacheZ = chunkz;
                    return chunk.getBlock(this.x & 15, this.y, this.z & 15);
                }
            }
            //Chunk doesn't exist - meaning, it is not loaded
            return Blocks.bedrock;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Oxygen Sealer thread: Exception getting block type in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Requested block coordinates");
            crashreportcategory.addCrashSection("Location", CrashReportCategory.getLocationInfo(this.x, this.y, this.z));
            throw new ReportedException(crashreport);
        }
    }

    public Block getBlock()
    {
   		World world = getWorldForId(this.dim);
   		if (world == null) return null;
        return world.getBlock(this.x, this.y, this.z);
    }
    
    public BlockVec3Dim modifyPositionFromSide(ForgeDirection side, int amount)
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

    public BlockVec3Dim newVecSide(int side)
    {
        BlockVec3Dim vec = new BlockVec3Dim(this.x, this.y, this.z, this.dim);
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

    public BlockVec3Dim modifyPositionFromSide(ForgeDirection side)
    {
        return this.modifyPositionFromSide(side, 1);
    }

    @Override
    public int hashCode()
    {
        return (((this.z * 431 + this.x) * 379 + this.y) * 373 + this.dim) * 7;
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
        return "BlockVec3 "+this.dim+":[" + this.x + "," + this.y + "," + this.z + "]";
    }

    /**
     * This will load the chunk.
     */
    public TileEntity getTileEntity()
    {
   		World world = getWorldForId(this.dim);
   		if (world == null) return null;
        return world.getTileEntity(this.x, this.y, this.z);
    }

    public int getBlockMetadata()
    {
   		World world = getWorldForId(this.dim);
   		if (world == null) return 0;
        return world.getBlockMetadata(this.x, this.y, this.z);
    }

    public static BlockVec3Dim readFromNBT(NBTTagCompound nbtCompound)
    {
        BlockVec3Dim tempVector = new BlockVec3Dim();
        tempVector.x = nbtCompound.getInteger("x");
        tempVector.y = nbtCompound.getInteger("y");
        tempVector.z = nbtCompound.getInteger("z");
        tempVector.dim = nbtCompound.getInteger("dim");
        return tempVector;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("x", this.x);
        par1NBTTagCompound.setInteger("y", this.y);
        par1NBTTagCompound.setInteger("z", this.z);
        par1NBTTagCompound.setInteger("dim", this.dim);
        return par1NBTTagCompound;
    }

    public BlockVec3Dim(NBTTagCompound par1NBTTagCompound)
    {
        this.x = par1NBTTagCompound.getInteger("x");
        this.y = par1NBTTagCompound.getInteger("y");
        this.z = par1NBTTagCompound.getInteger("z");
        this.dim = par1NBTTagCompound.getInteger("dim");
    }

    public double getMagnitude()
    {
        return Math.sqrt(this.getMagnitudeSquared());
    }

    public int getMagnitudeSquared()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public void setBlock(Block block)
    {
   		World world = getWorldForId(this.dim);
   		if (world == null) return;
        world.setBlock(this.x, this.y, this.z, block, 0, 3);
    }

    public boolean blockExists()
    {
   		World world = getWorldForId(this.dim);
   		if (world == null) return false;
        return world.blockExists(this.x, this.y, this.z);
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

    private World getWorldForId(int dimensionID)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
            if (theServer == null) return null;
            return theServer.worldServerForDimension(dimensionID);
        }
        else
        {
            return getWorldForIdClient(dimensionID);
        }
    }

    @SideOnly(Side.CLIENT)
    private World getWorldForIdClient(int dimensionID)
    {
        World world = FMLClientHandler.instance().getClient().theWorld;

        if (world != null && world.provider.dimensionId == dimensionID)
        {
            return world;
        }

        return null;
    }
}
