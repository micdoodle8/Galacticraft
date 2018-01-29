package codechicken.lib.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * Contains a bunch of stuff to do with rotation.
 * Created by covers1624 on 6/30/2016.
 */
public class RotationUtils {

    /**
     * Gets the rotation for placing a block only on the horizon.
     *
     * @param entity Entity placing block.
     * @return Direction placed.
     */
    public static EnumFacing getPlacedRotationHorizontal(EntityLivingBase entity) {
        int facing = MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;
        return entityRotationToSide(facing).getOpposite();
    }

    /**
     * Gets rotation for placing a block, Will use Up and Down.
     *
     * @param pos    Pos placement is happening.
     * @param entity Entity placing block.
     * @return Direction placed.
     */
    public static EnumFacing getPlacedRotation(BlockPos pos, EntityLivingBase entity) {
        int entityRotation = (int) Math.floor(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        if (Math.abs(entity.posX - pos.getX()) < 2.0D && Math.abs(entity.posZ - pos.getZ()) < 2.0D) {

            double eyeDistance = entity.posY + 1.82D - pos.getY();

            if (eyeDistance > 2.0D) {
                return EnumFacing.DOWN;
            }

            if (eyeDistance < 0.0D) {
                return EnumFacing.UP;
            }
        }

        return entityRotationToSide(entityRotation);
    }

    /**
     * Short hand for getPlacedRotationHorizontal and getPlacedRotation.
     *
     * @param pos         Pos placement is happening.
     * @param entity      Entity placing block.
     * @param onlyHorizon True if should only obey the horizon.
     * @return Direction placed.
     */
    public static EnumFacing getPlacedRotation(BlockPos pos, EntityLivingBase entity, boolean onlyHorizon) {
        if (onlyHorizon) {
            return getPlacedRotationHorizontal(entity);
        }
        return getPlacedRotation(pos, entity);
    }

    /**
     * Rotate this Facing around the Y axis counter-clockwise (NORTH => WEST => SOUTH => EAST => NORTH)
     *
     * @param facing Current facing.
     * @return Next facing.
     */
    public static EnumFacing rotateCounterClockwise(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return EnumFacing.WEST;
            case EAST:
                return EnumFacing.NORTH;
            case SOUTH:
                return EnumFacing.EAST;
            case WEST:
                return EnumFacing.SOUTH;
            default:
                throw new IllegalStateException("Unable to get CCW facing of " + facing);
        }
    }

    /**
     * Rotate this Facing around the Y axis counter-clockwise (NORTH => EAST => SOUTH => WEST => NORTH)
     *
     * @param facing Current facing.
     * @return Next facing.
     */
    public static EnumFacing rotateClockwise(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return EnumFacing.EAST;
            case EAST:
                return EnumFacing.SOUTH;
            case SOUTH:
                return EnumFacing.WEST;
            case WEST:
                return EnumFacing.NORTH;
            default:
                throw new IllegalStateException("Unable to get CW facing of " + facing);
        }
    }

    /**
     * Rotate this Facing around all axises counter-clockwise (NORTH => SOUTH => EAST => WEST => UP => DOWN => NORTH)
     *
     * @param facing Current facing.
     * @return Next facing.
     */
    public static EnumFacing rotateForward(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return EnumFacing.DOWN;
            case DOWN:
                return EnumFacing.UP;
            case UP:
                return EnumFacing.WEST;
            case WEST:
                return EnumFacing.EAST;
            case EAST:
                return EnumFacing.SOUTH;
            case SOUTH:
                return EnumFacing.NORTH;
        }
        return EnumFacing.NORTH;
    }

    /**
     * Rotate this Facing around all axises counter-clockwise (NORTH => DOWN => UP => WEST => EAST => SOUTH => NORTH)
     *
     * @param facing Current facing.
     * @return Next facing.
     */
    public static EnumFacing rotateBackwards(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return EnumFacing.SOUTH;
            case SOUTH:
                return EnumFacing.EAST;
            case EAST:
                return EnumFacing.WEST;
            case WEST:
                return EnumFacing.UP;
            case UP:
                return EnumFacing.DOWN;
            case DOWN:
                return EnumFacing.NORTH;
        }
        return EnumFacing.NORTH;
    }

    /**
     * Turns Entity rotation in to EnumFacing.
     *
     * @param rotation The entity rotation, Generally MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;
     * @return The rotation in EnumFacing.
     */
    public static EnumFacing entityRotationToSide(int rotation) {
        switch (rotation) {
            case 0:
                return EnumFacing.SOUTH;

            case 1:
                return EnumFacing.WEST;

            case 2:
                return EnumFacing.NORTH;

            default:
                return EnumFacing.EAST;
        }
    }

}
