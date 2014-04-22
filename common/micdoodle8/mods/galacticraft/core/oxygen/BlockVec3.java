package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeDirection;

/* Think this BlockVec3 is confusing with galacticraft.api.vector.Vector3?
 * 
 * For speed, in 95% of cases Galacticraft code could be using integer arithmetic not doubles,
 * for block coordinates, to avoid massive unnecessary type conversion between integers and doubles.
 * (Minecraft block coordinates are always integers, only entity coordinates are doubles.)
 * 
 * Most of Galacticraft could therefore be adapted to use this BlockVec3 instead.
 * To avoid a big diff, the methods here are as similar as possible to those in Vector3.
 *  (Though really, calls like vector3.intX() ought to be replaced by vector3.x, for maximum speed)
 *  
 * Note also when writing NBT data BlockVec3 writes and reads its coordinates as doubles, for 100% file and network compatibility with prior code.
 */
public class BlockVec3 implements Cloneable
{
	public int x;
	public int y;
	public int z;
	public boolean[] sideDone = {false,false,false,false,false,false};
	private static Chunk chunkCached;
	private static int chunkCacheX = 1876000; // outside the world edge
	private static int chunkCacheZ = 1876000; // outside the world edge
	

	public BlockVec3()
	{
		this(0, 0, 0);
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
		this.x = par1.xCoord;
		this.y = par1.yCoord;
		this.z = par1.zCoord;
	}

	/**
	 * Makes a new copy of this Vector. Prevents variable referencing problems.
	 */
	@Override
	public BlockVec3 clone()
	{
		return new BlockVec3(this.x, this.y, this.z);
	}

	/**
	 * Get block ID at the BlockVec3 coordinates.
	 * 
	 * @param world
	 * @return the block ID, or -1 if the y-coordinate is less than 0 or greater than 256 or the x or z is outside the Minecraft worldmap. 
	 * Returns -2 if the coordinates being checked are in an unloaded chunk
	 */
	public int getBlockID(World world)
	{
		if (this.y < 0 || this.y >= 256 || this.x < -30000000 || this.z < -30000000 || this.x >= 30000000 || this.z >= 30000000)
		{
			return -1;
		}

		int chunkx = this.x >> 4;
		int chunkz = this.z >> 4;
		try
		{
			if (world.getChunkProvider().chunkExists(chunkx,chunkz))
			{
				// In a typical inner loop, 80% of the time consecutive calls to
				// this will be within the same chunk
				if (BlockVec3.chunkCacheX == chunkx && BlockVec3.chunkCacheZ == chunkz && BlockVec3.chunkCached.isChunkLoaded)
				{
					return BlockVec3.chunkCached.getBlockID(this.x & 15, this.y, this.z & 15);
				}
				else
				{
					Chunk chunk = null;
					chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
					BlockVec3.chunkCached = chunk;
					BlockVec3.chunkCacheX = chunkx;
					BlockVec3.chunkCacheZ = chunkz;
					return chunk.getBlockID(this.x & 15, this.y, this.z & 15);
				}
			}
			//Chunk doesn't exist - meaning, it is not loaded
			return -2;
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
	 * Get block ID at the BlockVec3 coordinates.
	 * Only call this 'safe' version if x and z coordinates are within the Minecraft world map (-30m to +30m)
	 * @param world
	 * @return the block ID, or -1 if the y-coordinate is less than 0 or greater than 256. 
	 * Returns -2 if the coordinates being checked are in an unloaded chunk
	 */
	public int getBlockIDsafe(World world)
	{
		if (this.y < 0 || this.y >= 256)
		{
			return -1;
		}

		int chunkx = this.x >> 4;
		int chunkz = this.z >> 4;
		try
		{
			if (world.getChunkProvider().chunkExists(chunkx,chunkz))
			{
				// In a typical inner loop, 80% of the time consecutive calls to
				// this will be within the same chunk
				if (BlockVec3.chunkCacheX == chunkx && BlockVec3.chunkCacheZ == chunkz && BlockVec3.chunkCached.isChunkLoaded)
				{
					return BlockVec3.chunkCached.getBlockID(this.x & 15, this.y, this.z & 15);
				}
				else
				{
					Chunk chunk = null;
					chunk = world.getChunkFromChunkCoords(chunkx, chunkz);
					BlockVec3.chunkCached = chunk;
					BlockVec3.chunkCacheX = chunkx;
					BlockVec3.chunkCacheZ = chunkz;
					return chunk.getBlockID(this.x & 15, this.y, this.z & 15);
				}
			}
			//Chunk doesn't exist - meaning, it is not loaded
			return -2;
		}
		catch (Throwable throwable)
		{
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Oxygen Sealer thread: Exception getting block type in world");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Requested block coordinates");
			crashreportcategory.addCrashSection("Location", CrashReportCategory.getLocationInfo(this.x, this.y, this.z));
			throw new ReportedException(crashreport);
		}
	}

	public BlockVec3 add(BlockVec3 par1)
	{
		this.x += par1.x;
		this.y += par1.y;
		this.z += par1.z;
		return this;
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
		this.x = this.x -= par1.x;
		this.y = this.y -= par1.y;
		this.z = this.z -= par1.z;

		return this;
	}

	public BlockVec3 modifyPositionFromSide(ForgeDirection side, int amount)
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
		BlockVec3 vec = new BlockVec3(this.x, this.y, this.z);
		vec.sideDone[side ^ 1]=true;
		switch (side)
		{
		case 0:
			vec.y--;
			break;
		case 1:
			vec.y++;
			break;
		case 2:
			vec.z--;
			break;
		case 3:
			vec.z++;
			break;
		case 4:
			vec.x--;
			break;
		case 5:
			vec.x++;
			break;
		}
		return vec;
	}

	public BlockVec3 modifyPositionFromSide(ForgeDirection side)
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
			BlockVec3 vector = (BlockVec3) o;
			return this.x == vector.x && this.y == vector.y && this.z == vector.z;
		}

		return false;
	}

	@Override
	public String toString()
	{
		return "BlockVec3 [" + this.x + "," + this.y + "," + this.z + "]";
	}

	public TileEntity getTileEntity(IBlockAccess world)
	{
		return world.getBlockTileEntity(this.x, this.y, this.z);
	}

	public int getBlockMetadata(IBlockAccess world)
	{
		return world.getBlockMetadata(this.x, this.y, this.z);
	}

	public static BlockVec3 readFromNBT(NBTTagCompound nbtCompound)
	{
		BlockVec3 tempVector = new BlockVec3();
		tempVector.x = (int) Math.floor(nbtCompound.getDouble("x"));
		tempVector.y = (int) Math.floor(nbtCompound.getDouble("y"));
		tempVector.z = (int) Math.floor(nbtCompound.getDouble("z"));
		return tempVector;
	}

	public int distanceTo(BlockVec3 vector)
	{
		int var2 = vector.x - this.x;
		int var4 = vector.y - this.y;
		int var6 = vector.z - this.z;
		return (int) Math.floor(Math.sqrt(var2 * var2 + var4 * var4 + var6 * var6));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setDouble("x", this.x);
		par1NBTTagCompound.setDouble("y", this.y);
		par1NBTTagCompound.setDouble("z", this.z);
		return par1NBTTagCompound;
	}

	public double getMagnitude()
	{
		return Math.sqrt(this.getMagnitudeSquared());
	}

	public int getMagnitudeSquared()
	{
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public void setBlock(World worldObj, int blockID)
	{
		worldObj.setBlock(this.x, this.y, this.z, blockID, 0, 3);
	}

	public int intX()
	{
		return this.x;
	}

	public int intY()
	{
		return this.x;
	}

	public int intZ()
	{
		return this.x;
	}
	
	public void setSideDone(int side)
	{
		sideDone[side]=true;
	}
}
