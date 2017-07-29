package mekanism.api;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

/**
 * Coord4D - an integer-based way to keep track of and perform operations on blocks in a Minecraft-based environment. This also takes
 * in account the dimension the coordinate is in.
 * @author aidancbrady
 *
 */
public class Coord4D
{
	public int xCoord;
	public int yCoord;
	public int zCoord;

	public int dimensionId;
	
	/**
	 * Creates a Coord4D from an entity's position, rounded down.
	 * @param entity - entity to create the Coord4D from
	 */
	public Coord4D(Entity entity)
	{
		xCoord = (int)entity.posX;
		yCoord = (int)entity.posY;
		zCoord = (int)entity.posZ;
		
		dimensionId = entity.worldObj.provider.getDimension();
	}

	/**
	 * Creates a Coord4D from the defined x, y, z, and dimension values.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 * @param dimension - dimension ID
	 */
	public Coord4D(double x, double y, double z, int dimension)
	{
		xCoord = MathHelper.floor_double(x);
		yCoord = MathHelper.floor_double(y);
		zCoord = MathHelper.floor_double(z);

		dimensionId = dimension;
	}
	
	public Coord4D(BlockPos pos, World world)
	{
		this(pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimension());
	}

	public Coord4D(RayTraceResult mop, World world)
	{
		this(mop.getBlockPos(), world);
	}

	/**
	 * Gets the state of the block representing this Coord4D.
	 * @param world - world this Coord4D is in
	 * @return the state of this Coord4D's block
	 */
	public IBlockState getBlockState(IBlockAccess world)
	{
		return world.getBlockState(getPos());
	}
	
	public int getBlockMeta(IBlockAccess world)
	{
		IBlockState state = getBlockState(world);
		return state == null ? 0 : state.getBlock().getMetaFromState(state);
	}
	
	public BlockPos getPos()
	{
		return new BlockPos(xCoord, yCoord, zCoord);
	}

	/**
	 * Gets the TileEntity of the block representing this Coord4D.
	 * @param world - world this Coord4D is in
	 * @return the TileEntity of this Coord4D's block
	 */
	public TileEntity getTileEntity(IBlockAccess world)
	{
		if(world instanceof World && !exists((World)world))
		{
			return null;
		}

		return world.getTileEntity(getPos());
	}

	/**
	 * Gets the Block value of the block representing this Coord4D.
	 * @param world - world this Coord4D is in
	 * @return the Block value of this Coord4D's block
	 */
	public Block getBlock(IBlockAccess world)
	{
		if(world instanceof World && !exists((World)world))
		{
			return null;
		}
		
		return getBlockState(world).getBlock();
	}

	/**
	 * Writes this Coord4D's data to an NBTTagCompound.
	 * @param nbtTags - tag compound to write to
	 * @return the tag compound with this Coord4D's data
	 */
	public NBTTagCompound write(NBTTagCompound nbtTags)
	{
		nbtTags.setInteger("x", xCoord);
		nbtTags.setInteger("y", yCoord);
		nbtTags.setInteger("z", zCoord);
		nbtTags.setInteger("dimensionId", dimensionId);

		return nbtTags;
	}

	/**
	 * Writes this Coord4D's data to an ArrayList for packet transfer.
	 * @param data - the ArrayList to add the data to
	 */
	public void write(ArrayList data)
	{
		data.add(xCoord);
		data.add(yCoord);
		data.add(zCoord);
		data.add(dimensionId);
	}
	
	/**
	 * Writes this Coord4D's data to a ByteBuf for packet transfer.
	 * @param dataStream - the ByteBuf to add the data to
	 */
	public void write(ByteBuf dataStream)
	{
		dataStream.writeInt(xCoord);
		dataStream.writeInt(yCoord);
		dataStream.writeInt(zCoord);
		dataStream.writeInt(dimensionId);
	}

	/**
	 * Translates this Coord4D by the defined x, y, and z values.
	 * @param x - x value to translate
	 * @param y - y value to translate
	 * @param z - z value to translate
	 * @return translated Coord4D
	 */
	public Coord4D translate(int x, int y, int z)
	{
		return new Coord4D(xCoord+x, yCoord+y, zCoord+z, dimensionId);
	}
	
	/**
	 * Translates this Coord4D by the defined Coord4D's coordinates, regardless of dimension.
	 * @param coord - coordinates to translate by
	 * @return translated Coord4D
	 */
	public Coord4D translate(Coord4D coord)
	{
		return translate(coord.xCoord, coord.yCoord, coord.zCoord);
	}

	/**
	 * Creates and returns a new Coord4D translated to the defined offsets of the side.
	 * @param side - side to translate this Coord4D to
	 * @return translated Coord4D
	 */
	public Coord4D offset(EnumFacing side)
	{
		return offset(side, 1);
	}

	/**
	 * Creates and returns a new Coord4D translated to the defined offsets of the side by the defined amount.
	 * @param side - side to translate this Coord4D to
	 * @param amount - how far to translate this Coord4D
	 * @return translated Coord4D
	 */
	public Coord4D offset(EnumFacing side, int amount)
	{
		if(side == null || amount == 0)
		{
			return this;
		}
		
		return new Coord4D(xCoord+(side.getFrontOffsetX()*amount), yCoord+(side.getFrontOffsetY()*amount), zCoord+(side.getFrontOffsetZ()*amount), dimensionId);
	}
	
	public ItemStack getStack(IBlockAccess world)
	{
		IBlockState state = getBlockState(world);
		
		if(state == null || state == Blocks.AIR)
		{
			return null;
		}
		
		return new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
	}

	/**
	 * Returns a new Coord4D from a defined TileEntity's xCoord, yCoord and zCoord values.
	 * @param tileEntity - TileEntity at the location that will represent this Coord4D
	 * @return the Coord4D object from the TileEntity
	 */
	public static Coord4D get(TileEntity tileEntity)
	{
		return new Coord4D(tileEntity.getPos(), tileEntity.getWorld());
	}

	/**
	 * Returns a new Coord4D from a tag compound.
	 * @param tag - tag compound to read from
	 * @return the Coord4D from the tag compound
	 */
    public static Coord4D read(NBTTagCompound tag)
    {
        return new Coord4D(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"), tag.getInteger("id"));
    }

	/**
	 * Returns a new Coord4D from a ByteBuf.
	 * @param dataStream - data input to read from
	 * @return the Coord4D from the data input
	 */
	public static Coord4D read(ByteBuf dataStream)
	{
		return new Coord4D(dataStream.readInt(), dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
	}

	/**
	 * Creates and returns a new Coord4D with values representing the difference between the defined Coord4D
	 * @param other - the Coord4D to subtract from this
	 * @return a Coord4D representing the distance between the defined Coord4D
	 */
	public Coord4D difference(Coord4D other)
	{
		return new Coord4D(xCoord-other.xCoord, yCoord-other.yCoord, zCoord-other.zCoord, dimensionId);
	}

	/**
	 * A method used to find the EnumFacing represented by the distance of the defined Coord4D. Most likely won't have many
	 * applicable uses.
	 * @param other - Coord4D to find the side difference of
	 * @return EnumFacing representing the side the defined relative Coord4D is on to this
	 */
	public EnumFacing sideDifference(Coord4D other)
	{
		Coord4D diff = difference(other);

		for(EnumFacing side : EnumFacing.VALUES)
		{
			if(side.getFrontOffsetX() == diff.xCoord && side.getFrontOffsetY() == diff.yCoord && side.getFrontOffsetZ() == diff.zCoord)
			{
				return side;
			}
		}

		return null;
	}

	/**
	 * Gets the distance to a defined Coord4D.
	 * @param obj - the Coord4D to find the distance to
	 * @return the distance to the defined Coord4D
	 */
	public int distanceTo(Coord4D obj)
	{
		int subX = xCoord - obj.xCoord;
		int subY = yCoord - obj.yCoord;
		int subZ = zCoord - obj.zCoord;
		return (int)MathHelper.sqrt_double(subX * subX + subY * subY + subZ * subZ);
	}

	/**
	 * Whether or not the defined side of this Coord4D is visible.
	 * @param side - side to check
	 * @param world - world this Coord4D is in
	 * @return
	 */
	public boolean sideVisible(EnumFacing side, IBlockAccess world)
	{
		return world.isAirBlock(step(side).getPos());
	}
	
	/**
	 * Gets a TargetPoint with the defined range from this Coord4D with the appropriate coordinates and dimension ID.
	 * @param range - the range the packet can be sent in of this Coord4D
	 * @return TargetPoint relative to this Coord4D
	 */
	public TargetPoint getTargetPoint(double range)
	{
		return new TargetPoint(dimensionId, xCoord, yCoord, zCoord, range);
	}

	/**
	 * Steps this Coord4D in the defined side's offset without creating a new value.
	 * @param side - side to step towards
	 * @return this Coord4D
	 */
	public Coord4D step(EnumFacing side)
	{
		return translate(side.getFrontOffsetX(), side.getFrontOffsetY(), side.getFrontOffsetZ());
	}

	/**
	 * Whether or not the chunk this Coord4D is in exists and is loaded.
	 * @param world - world this Coord4D is in
	 * @return the chunk of this Coord4D
	 */
	public boolean exists(World world)
	{
		return world.getChunkProvider() == null || world.getChunkProvider().getLoadedChunk(xCoord >> 4, zCoord >> 4) != null;
	}

	/**
	 * Gets the chunk this Coord4D is in.
	 * @param world - world this Coord4D is in
	 * @return the chunk of this Coord4D
	 */
	public Chunk getChunk(World world)
	{
		return world.getChunkFromBlockCoords(getPos());
	}
	
	/**
	 * Gets the Chunk3D object with chunk coordinates correlating to this Coord4D's location
	 * @return Chunk3D with correlating chunk coordinates.
	 */
	public Chunk3D getChunk3D()
	{
		return new Chunk3D(this);
	}

	/**
	 * Whether or not the block this Coord4D represents is an air block.
	 * @param world - world this Coord4D is in
	 * @return if this Coord4D is an air block
	 */
	public boolean isAirBlock(IBlockAccess world)
	{
		return world.isAirBlock(getPos());
	}
	
	/**
	 * Whether or not this block this Coord4D represents is replaceable.
	 * @param world - world this Coord4D is in
	 * @return if this Coord4D is replaceable
	 */
	public boolean isReplaceable(World world)
	{
		return getBlock(world).isReplaceable(world, getPos());
	}
	
	/**
	 * Gets a bounding box that contains the area this Coord4D would take up in a world.
	 * @return this Coord4D's bounding box
	 */
	public AxisAlignedBB getBoundingBox()
	{
		return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1);
	}

	@Override
	public Coord4D clone()
	{
		return new Coord4D(xCoord, yCoord, zCoord, dimensionId);
	}

	@Override
	public String toString()
	{
		return "[Coord4D: " + xCoord + ", " + yCoord + ", " + zCoord + ", dim=" + dimensionId + "]";
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Coord4D &&
				((Coord4D)obj).xCoord == xCoord &&
				((Coord4D)obj).yCoord == yCoord &&
				((Coord4D)obj).zCoord == zCoord &&
				((Coord4D)obj).dimensionId == dimensionId;
	}

	@Override
	public int hashCode()
	{
		int code = 1;
		code = 31 * code + xCoord;
		code = 31 * code + yCoord;
		code = 31 * code + zCoord;
		code = 31 * code + dimensionId;
		return code;
	}
}