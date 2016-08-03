package ic2.api;

import java.util.EnumSet;
import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Represents the 6 possible directions along the axis of a block.
 */
public enum Direction {
	/**
	 * -X
	 */
	XN,
	/**
	 * +X
	 */
	XP,

	/**
	 * -Y
	 */
	YN, //MC-Code starts with 0 here
	/**
	 * +Y
	 */
	YP, // 1...

	/**
	 * -Z
	 */
	ZN,
	/**
	 * +Z
	 */
	ZP;

	private Direction() {
		int side = ordinal() / 2;
		int sign = getSign();

		xOffset = side == 0 ? sign : 0;
		yOffset = side == 1 ? sign : 0;
		zOffset = side == 2 ? sign : 0;
	}

	public static Direction fromSideValue(int side) {
		return directions[(side + 2) % 6];
	}

	public static Direction fromEnumFacing(EnumFacing dir) {

		return fromSideValue(dir.ordinal());
	}

	/**
	 * Get the tile entity next to a tile entity following this direction.
	 *
	 * @param tileEntity tile entity to check
	 * @return Adjacent tile entity or null if none exists
	 */
	public TileEntity applyToTileEntity(TileEntity te) {
		return applyTo(te.getWorld(), te.getPos());
	}

	/**
	 * Get the tile entity next to a position following this direction.
	 *
	 * @param world World to check
	 * @param x X coordinate to check from
	 * @param y Y coordinate to check from
	 * @param z Z coordinate to check from
	 * @return Adjacent tile entity or null if none exists
	 */
	public TileEntity applyTo(World world, BlockPos pos) {

		if (world != null && world.getBlockState(pos).getBlock() != Blocks.air) {
			try {
				return world.getTileEntity(pos);
			} catch (Exception e) {
				throw new RuntimeException("error getting TileEntity at dim "+world.provider.getDimensionId()+" "+pos);
			}
		}

		return null;
	}

	/**
	 * Get the inverse of this direction (XN -> XP, XP -> XN, etc.)
	 *
	 * @return Inverse direction
	 */
	public Direction getInverse() {
		return directions[ordinal() ^ 1];
	}

	/**
	 * Convert this direction to a Minecraft side value.
	 *
	 * @return Minecraft side value
	 */
	public int toSideValue() {
		return (ordinal() + 4) % 6;
	}

	/**
	 * Determine direction sign (N for negative or P for positive).
	 *
	 * @return -1 if the direction is negative, +1 if the direction is positive
	 */
	private int getSign() {
		return (ordinal() % 2) * 2 - 1;
	}

	public EnumFacing toFacing() {
		return EnumFacing.VALUES[toSideValue()];
	}

	public final int xOffset;
	public final int yOffset;
	public final int zOffset;

	public static final Direction[] directions = Direction.values();
	public static final Set<Direction> noDirections = EnumSet.noneOf(Direction.class);
	public static final Set<Direction> allDirections = EnumSet.allOf(Direction.class);
}

