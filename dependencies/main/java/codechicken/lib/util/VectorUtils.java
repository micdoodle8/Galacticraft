package codechicken.lib.util;

import codechicken.lib.vec.Vector3;
import net.minecraft.util.EnumFacing;

/**
 * Created by covers1624 on 4/10/2016.
 */
public class VectorUtils {

    /**
     * Calculates a normal for the given vertices.
     *
     * @param vertices The vertices to calculate a normal for, Expected to be a length of 3.
     * @return The normal.
     */
    public static Vector3 calculateNormal(Vector3... vertices) {
        Vector3 diff1 = vertices[1].copy().subtract(vertices[0]);
        Vector3 diff2 = vertices[2].copy().subtract(vertices[0]);
        return diff1.crossProduct(diff2).normalize().copy();
    }

    /**
     * Calculates the int direction a normal is facing.
     *
     * @param normal The normal to calculate from.
     * @return The direction the normal is facing.
     */
    public static int findSide(Vector3 normal) {
        if (normal.y <= -0.99) {
            return 0;
        }
        if (normal.y >= 0.99) {
            return 1;
        }
        if (normal.z <= -0.99) {
            return 2;
        }
        if (normal.z >= 0.99) {
            return 3;
        }
        if (normal.x <= -0.99) {
            return 4;
        }
        if (normal.x >= 0.99) {
            return 5;
        }
        return -1;
    }

    /**
     * Calculates the EnumFacing for a given normal.
     *
     * @param normal The normal to calculate from.
     * @return The direction the normal is facing.
     */
    public static EnumFacing calcNormalSide(Vector3 normal) {
        if (normal.y <= -0.99) {
            return EnumFacing.DOWN;
        }
        if (normal.y >= 0.99) {
            return EnumFacing.UP;
        }
        if (normal.z <= -0.99) {
            return EnumFacing.NORTH;
        }
        if (normal.z >= 0.99) {
            return EnumFacing.SOUTH;
        }
        if (normal.x <= -0.99) {
            return EnumFacing.WEST;
        }
        if (normal.x >= 0.99) {
            return EnumFacing.EAST;
        }
        return null;
    }
}
