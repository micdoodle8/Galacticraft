package codechicken.lib.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * Created by covers1624 on 5/17/2016.
 */
@Deprecated//Brandon this isn't needed anymore, use null and special case it instead of UNKNOWN
public enum CCDirection implements IStringSerializable {

    /**
     * -Y
     */
    DOWN(0, -1, 0),

    /**
     * +Y
     */
    UP(0, 1, 0),

    /**
     * -Z
     */
    NORTH(0, 0, -1),

    /**
     * +Z
     */
    SOUTH(0, 0, 1),

    /**
     * -X
     */
    WEST(-1, 0, 0),

    /**
     * +X
     */
    EAST(1, 0, 0),

    /**
     * Used only by getOrientation, for invalid inputs
     */
    UNKNOWN(0, 0, 0);

    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;
    public final int flag;
    public static final CCDirection[] VALID_DIRECTIONS = { DOWN, UP, NORTH, SOUTH, WEST, EAST };
    public static final int[] OPPOSITES = { 1, 0, 3, 2, 5, 4, 6 };
    // Left hand rule rotation matrix for all possible axes of rotation
    public static final int[][] ROTATION_MATRIX = {//
            { 0, 1, 4, 5, 3, 2, 6 },//
            { 0, 1, 5, 4, 2, 3, 6 },//
            { 5, 4, 2, 3, 0, 1, 6 },//
            { 4, 5, 2, 3, 1, 0, 6 },//
            { 2, 3, 1, 0, 4, 5, 6 },//
            { 3, 2, 0, 1, 4, 5, 6 },//
            { 0, 1, 2, 3, 4, 5, 6 },//
    };

    CCDirection(int x, int y, int z) {
        offsetX = x;
        offsetY = y;
        offsetZ = z;
        flag = 1 << ordinal();
    }

    public static CCDirection getOrientation(int id) {
        if (id >= 0 && id < VALID_DIRECTIONS.length) {
            return VALID_DIRECTIONS[id];
        }
        return UNKNOWN;
    }

    public CCDirection getOpposite() {
        return getOrientation(OPPOSITES[ordinal()]);
    }

    public CCDirection getRotation(CCDirection axis) {
        return getOrientation(ROTATION_MATRIX[axis.ordinal()][ordinal()]);
    }

    public EnumFacing getFacing(CCDirection direction) {
        if (direction != UNKNOWN) {
            return EnumFacing.getFront(direction.ordinal());
        } else {
            return null;
        }
    }

    public CCDirection getDirection(EnumFacing facing) {
        return facing == null ? UNKNOWN : VALID_DIRECTIONS[facing.getIndex()];
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }

}
