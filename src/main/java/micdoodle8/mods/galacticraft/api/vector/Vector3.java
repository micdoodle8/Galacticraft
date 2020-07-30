package micdoodle8.mods.galacticraft.api.vector;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * Vector3 Class is used for defining objects in a 3D space.
 *
 * @author Calclavia
 */

public class Vector3 implements Cloneable
{
    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3()
    {
        this(0, 0, 0);
    }

    public Vector3(Vector3 vector)
    {
        this(vector.x, vector.y, vector.z);
    }

    public Vector3(float amount)
    {
        this(amount, amount, amount);
    }

    public Vector3(Entity par1)
    {
        this((float) par1.getPosX(), (float) par1.getPosY(), (float) par1.getPosZ());
    }

    public Vector3(BlockPos pos)
    {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector3(TileEntity par1)
    {
        this(par1.getPos());
    }

    public Vector3(Vec3d par1)
    {
        this((float) par1.x, (float) par1.y, (float) par1.z);
    }

    public Vector3(Vector3f par1)
    {
        this(par1.getX(), par1.getY(), par1.getZ());
    }

    public Vector3(RayTraceResult par1)
    {
        this(par1.getHitVec());
    }

    public Vector3(Direction direction)
    {
        this(direction.getXOffset(), direction.getYOffset(), direction.getZOffset());
    }

    /**
     * Loads a Vector3 from an NBT compound.
     */
    public Vector3(CompoundNBT nbt)
    {
        this(nbt.getFloat("x"), nbt.getFloat("y"), nbt.getFloat("z"));
    }

    public Vector3(BlockVec3 vec)
    {
        this(vec.x, vec.y, vec.z);
    }

    /**
     * Returns the coordinates as integers, ideal for block placement.
     */
    public int intX()
    {
        return (int) Math.floor(this.x);
    }

    public int intY()
    {
        return (int) Math.floor(this.y);
    }

    public int intZ()
    {
        return (int) Math.floor(this.z);
    }

    public float floatX()
    {
        return this.x;
    }

    public float floatY()
    {
        return this.y;
    }

    public float floatZ()
    {
        return this.z;
    }

    /**
     * Makes a new copy of this Vector. Prevents variable referencing problems.
     */
    @Override
    public final Vector3 clone()
    {
        return new Vector3(this);
    }

    /**
     * Easy block access functions.
     *
     * @param world
     * @return
     */
    public Block getBlock(IBlockReader world)
    {
        return world.getBlockState(new BlockPos(this.intX(), this.intY(), this.intZ())).getBlock();
    }

    public BlockState getBlockMetadata(IBlockReader world)
    {
        return world.getBlockState(new BlockPos(this.intX(), this.intY(), this.intZ()));
    }

    public TileEntity getTileEntity(IBlockReader world)
    {
        return world.getTileEntity(new BlockPos(this.intX(), this.intY(), this.intZ()));
    }

    public boolean setBlock(World world, BlockState state, int notify)
    {
        return world.setBlockState(new BlockPos(this.intX(), this.intY(), this.intZ()), state, notify);
    }

    public boolean setBlock(World world, BlockState state)
    {
        return this.setBlock(world, state, 3);
    }

    /**
     * ---------------------- CONVERSION FUNCTIONS ----------------------------
     */
    /**
     * Converts this Vector3 into a Vector2 by dropping the Y axis.
     */
    public Vector2 toVector2()
    {
        return new Vector2(this.x, this.z);
    }

    /**
     * Converts this vector three into a Minecraft Vec3d object
     */
    public Vec3d toVec3()
    {
        return new Vec3d(this.x, this.y, this.z);
    }

    /**
     * Converts Vector3 into a EnumFacing.
     */
    public Direction toEnumFacing()
    {
        for (Direction direction : Direction.values())
        {
            if (this.x == direction.getXOffset() && this.y == direction.getYOffset() && this.z == direction.getZOffset())
            {
                return direction;
            }
        }

        return null;
    }

    public float getMagnitude()
    {
        return (float) Math.sqrt(this.getMagnitudeSquared());
    }

    public float getMagnitudeSquared()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3 normalize()
    {
        float d = this.getMagnitude();

        if (d != 0)
        {
            this.scale(1 / d);
        }

        return this;
    }

    /**
     * Gets the distance between two vectors
     *
     * @return The distance
     */
    public static float distance(Vector3 vec1, Vector3 vec2)
    {
        return vec1.distance(vec2);
    }

    @Deprecated
    public float distanceTo(Vector3 vector3)
    {
        return this.distance(vector3);
    }

    public float distance(Vector3 compare)
    {
        Vector3 difference = this.clone().difference(compare);
        return difference.getMagnitude();
    }

    /**
     * Multiplies the vector by negative one.
     */
    public Vector3 invert()
    {
        this.scale(-1);
        return this;
    }

    public Vector3 translate(Vector3 par1)
    {
        this.x += par1.x;
        this.y += par1.y;
        this.z += par1.z;
        return this;
    }

    public Vector3 translate(float par1)
    {
        this.x += par1;
        this.y += par1;
        this.z += par1;
        return this;
    }

    public static Vector3 translate(Vector3 translate, Vector3 par1)
    {
        translate.x += par1.x;
        translate.y += par1.y;
        translate.z += par1.z;
        return translate;
    }

    public static Vector3 translate(Vector3 translate, float par1)
    {
        translate.x += par1;
        translate.y += par1;
        translate.z += par1;
        return translate;
    }

    @Deprecated
    public Vector3 add(Vector3 amount)
    {
        return this.translate(amount);
    }

    @Deprecated
    public Vector3 add(float amount)
    {
        return this.translate(amount);
    }

    @Deprecated
    public Vector3 subtract(Vector3 amount)
    {
        return this.translate(amount.clone().invert());
    }

    @Deprecated
    public Vector3 subtract(float amount)
    {
        return this.translate(-amount);
    }

    public Vector3 difference(Vector3 amount)
    {
        return this.translate(amount.clone().invert());
    }

    public Vector3 difference(float amount)
    {
        return this.translate(-amount);
    }

    public Vector3 scale(float amount)
    {
        this.x *= amount;
        this.y *= amount;
        this.z *= amount;
        return this;
    }

    public Vector3 scale(Vector3 amount)
    {
        this.x *= amount.x;
        this.y *= amount.y;
        this.z *= amount.z;
        return this;
    }

    public static Vector3 scale(Vector3 vec, float amount)
    {
        return vec.scale(amount);
    }

    public static Vector3 scale(Vector3 vec, Vector3 amount)
    {
        return vec.scale(amount);
    }

    @Deprecated
    public Vector3 multiply(float amount)
    {
        return this.scale(amount);
    }

    @Deprecated
    public Vector3 multiply(Vector3 amount)
    {
        return this.scale(amount);
    }

    /**
     * Static versions of a lot of functions
     */
    public static Vector3 subtract(Vector3 par1, Vector3 par2)
    {
        return new Vector3(par1.x - par2.x, par1.y - par2.y, par1.z - par2.z);
    }

    @Deprecated
    public static Vector3 add(Vector3 par1, Vector3 par2)
    {
        return new Vector3(par1.x + par2.x, par1.y + par2.y, par1.z + par2.z);
    }

    @Deprecated
    public static Vector3 add(Vector3 par1, float par2)
    {
        return new Vector3(par1.x + par2, par1.y + par2, par1.z + par2);
    }

    @Deprecated
    public static Vector3 multiply(Vector3 vec1, Vector3 vec2)
    {
        return new Vector3(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
    }

    @Deprecated
    public static Vector3 multiply(Vector3 vec1, float vec2)
    {
        return new Vector3(vec1.x * vec2, vec1.y * vec2, vec1.z * vec2);
    }

    public Vector3 round()
    {
        return new Vector3(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }

    public Vector3 ceil()
    {
        return new Vector3((float) Math.ceil(this.x), (float) Math.ceil(this.y), (float) Math.ceil(this.z));
    }

    public Vector3 floor()
    {
        return new Vector3((float) Math.floor(this.x), (float) Math.floor(this.y), (float) Math.floor(this.z));
    }

    public Vector3 toRound()
    {
        this.x = Math.round(this.x);
        this.y = Math.round(this.y);
        this.z = Math.round(this.z);
        return this;
    }

    public Vector3 toCeil()
    {
        this.x = (float) Math.ceil(this.x);
        this.y = (float) Math.ceil(this.y);
        this.z = (float) Math.ceil(this.z);
        return this;
    }

    public Vector3 toFloor()
    {
        this.x = (float) Math.floor(this.x);
        this.y = (float) Math.floor(this.y);
        this.z = (float) Math.floor(this.z);
        return this;
    }

    /**
     * Gets all entities inside of this position in block space.
     */
    public List<Entity> getEntitiesWithin(World worldObj, Class<? extends Entity> par1Class)
    {
        return worldObj.getEntitiesWithinAABB(par1Class, new AxisAlignedBB(this.intX(), this.intY(), this.intZ(), this.intX() + 1, this.intY() + 1, this.intZ() + 1));
    }

    /**
     * Gets a position relative to a position's LogicalSide
     *
     * @return The position relative to the original position's LogicalSide
     */
    public Vector3 modifyPositionFromSide(Direction side, float amount)
    {
        return this.translate(new Vector3(side).scale(amount));
    }

    public Vector3 modifyPositionFromSide(Direction side)
    {
        this.modifyPositionFromSide(side, 1);
        return this;
    }

    /**
     * Cross product functions
     *
     * @return The cross product between this vector and another.
     */
    public Vector3 toCrossProduct(Vector3 compare)
    {
        float newX = this.y * compare.z - this.z * compare.y;
        float newY = this.z * compare.x - this.x * compare.z;
        float newZ = this.x * compare.y - this.y * compare.x;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        return this;
    }

    public Vector3 crossProduct(Vector3 compare)
    {
        return this.clone().toCrossProduct(compare);
    }

    public Vector3 xCrossProduct()
    {
        return new Vector3(0.0F, this.z, -this.y);
    }

    public Vector3 zCrossProduct()
    {
        return new Vector3(-this.y, this.x, 0.0F);
    }

    public float dotProduct(Vector3 vec2)
    {
        return this.x * vec2.x + this.y * vec2.y + this.z * vec2.z;
    }

    /**
     * @return The perpendicular vector.
     */
    public Vector3 getPerpendicular()
    {
        if (this.z == 0.0F)
        {
            return this.zCrossProduct();
        }

        return this.xCrossProduct();
    }

    /**
     * @return True if this Vector3 is zero.
     */
    public boolean isZero()
    {
        return this.x == 0 && this.y == 0 && this.z == 0;
    }

    /**
     * Rotate by a this vector around an axis.
     *
     * @return The new Vector3 rotation.
     */
    public Vector3 rotate(float angle, Vector3 axis)
    {
        return Vector3.translateMatrix(Vector3.getRotationMatrix(angle, axis), this.clone());
    }

    public float[] getRotationMatrix(float angle)
    {
        float[] matrix = new float[16];
        Vector3 axis = this.clone().normalize();
        float x = axis.x;
        float y = axis.y;
        float z = axis.z;
        angle /= Constants.RADIANS_TO_DEGREES;
        float cos = MathHelper.cos(angle);
        float ocos = 1.0F - cos;
        float sin = MathHelper.sin(angle);
        matrix[0] = x * x * ocos + cos;
        matrix[1] = y * x * ocos + z * sin;
        matrix[2] = x * z * ocos - y * sin;
        matrix[4] = x * y * ocos - z * sin;
        matrix[5] = y * y * ocos + cos;
        matrix[6] = y * z * ocos + x * sin;
        matrix[8] = x * z * ocos + y * sin;
        matrix[9] = y * z * ocos - x * sin;
        matrix[10] = z * z * ocos + cos;
        matrix[15] = 1.0F;
        return matrix;
    }

    public static Vector3 translateMatrix(float[] matrix, Vector3 translation)
    {
        float x = translation.x * matrix[0] + translation.y * matrix[1] + translation.z * matrix[2] + matrix[3];
        float y = translation.x * matrix[4] + translation.y * matrix[5] + translation.z * matrix[6] + matrix[7];
        float z = translation.x * matrix[8] + translation.y * matrix[9] + translation.z * matrix[10] + matrix[11];
        translation.x = x;
        translation.y = y;
        translation.z = z;
        return translation;
    }

    public static float[] getRotationMatrix(float angle, Vector3 axis)
    {
        return axis.getRotationMatrix(angle);
    }

    /**
     * Rotates this Vector by a yaw, pitch and roll value.
     */
    public void rotate(float yaw, float pitch, float roll)
    {
        float yawRadians = (float) Math.toRadians(yaw);
        float pitchRadians = (float) Math.toRadians(pitch);
        float rollRadians = (float) Math.toRadians(roll);

        float x = this.x;
        float y = this.y;
        float z = this.z;

        float cosYaw = (float) Math.cos(yawRadians);
        float cosPitch = (float) Math.cos(pitchRadians);
        float cosRoll = (float) Math.cos(rollRadians);
        float sinYaw = (float) Math.sin(yawRadians);
        float sinPitch = (float) Math.sin(pitchRadians);
        float sinRoll = (float) Math.sin(rollRadians);

        this.x = x * cosYaw * cosPitch + z * (cosYaw * sinPitch * sinRoll - sinYaw * cosRoll) + y * (cosYaw * sinPitch * cosRoll + sinYaw * sinRoll);
        this.z = x * sinYaw * cosPitch + z * (sinYaw * sinPitch * sinRoll + cosYaw * cosRoll) + y * (sinYaw * sinPitch * cosRoll - cosYaw * sinRoll);
        this.y = -x * sinPitch + z * cosPitch * sinRoll + y * cosPitch * cosRoll;
    }

    /**
     * Rotates a point by a yaw and pitch around the anchor 0,0 by a specific
     * angle.
     */
    public void rotate(float yaw, float pitch)
    {
        this.rotate(yaw, pitch, 0);
    }

    public void rotate(float yaw)
    {
        float yawRadians = (float) Math.toRadians(yaw);

        float x = this.x;
        float z = this.z;

        if (yaw != 0)
        {
            this.x = x * (float) Math.cos(yawRadians) - z * (float) Math.sin(yawRadians);
            this.z = x * (float) Math.sin(yawRadians) + z * (float) Math.cos(yawRadians);
        }
    }

    /**
     * Gets the delta look position based on the rotation yaw and pitch.
     * Minecraft coordinates are messed up. Y and Z are flipped. Yaw is
     * displaced by 90 degrees. Pitch is inversed.
     *
     * @param rotationYaw
     * @param rotationPitch
     */
    public static Vector3 getDeltaPositionFromRotation(float rotationYaw, float rotationPitch)
    {
        return new Vector3((float) Math.cos(Math.toRadians(rotationYaw + 90)), (float) Math.sin(Math.toRadians(-rotationPitch)), (float) Math.sin(Math.toRadians(rotationYaw + 90)));
    }

    /**
     * Gets the angle between this vector and another vector.
     *
     * @return Angle in degrees
     */
    public float getAngle(Vector3 vec2)
    {
        return Vector3.anglePreNorm(this.clone().normalize(), vec2.clone().normalize());
    }

    public static float getAngle(Vector3 vec1, Vector3 vec2)
    {
        return vec1.getAngle(vec2);
    }

    public float anglePreNorm(Vector3 vec2)
    {
        return (float) Math.acos(this.dotProduct(vec2));
    }

    public static float anglePreNorm(Vector3 vec1, Vector3 vec2)
    {
        return (float) Math.acos(vec1.clone().dotProduct(vec2));
    }

    /**
     * Loads a Vector3 from an NBT compound.
     */
    @Deprecated
    public static Vector3 readFromNBT(CompoundNBT nbt)
    {
        return new Vector3(nbt);
    }

    /**
     * Saves this Vector3 to disk
     */
    public CompoundNBT writeToNBT(CompoundNBT nbt)
    {
        nbt.putDouble("x", this.x);
        nbt.putDouble("y", this.y);
        nbt.putDouble("z", this.z);
        return nbt;
    }

    public static Vector3 UP()
    {
        return new Vector3(0, 1, 0);
    }

    public static Vector3 DOWN()
    {
        return new Vector3(0, -1, 0);
    }

    public static Vector3 NORTH()
    {
        return new Vector3(0, 0, -1);
    }

    public static Vector3 SOUTH()
    {
        return new Vector3(0, 0, 1);
    }

    public static Vector3 WEST()
    {
        return new Vector3(-1, 0, 0);
    }

    public static Vector3 EAST()
    {
        return new Vector3(1, 0, 0);
    }

    @Deprecated
    public RayTraceResult rayTraceEntities(World world, float rotationYaw, float rotationPitch, boolean collisionFlag, float reachDistance)
    {
        return this.rayTraceEntities(world, rotationYaw, rotationPitch, reachDistance);
    }

    public RayTraceResult rayTraceEntities(World world, float rotationYaw, float rotationPitch, float reachDistance)
    {
        return this.rayTraceEntities(world, Vector3.getDeltaPositionFromRotation(rotationYaw, rotationPitch).scale(reachDistance));
    }

    /**
     * Does an entity raytrace.
     *
     * @param world  - The world object.
     * @param target - The rotation in terms of Vector3. Convert using
     *               getDeltaPositionFromRotation()
     * @return The target hit.
     */
    public RayTraceResult rayTraceEntities(World world, Vector3 target)
    {
        EntityRayTraceResult pickedEntity = null;
        Vec3d startingPosition = this.toVec3();
        Vec3d look = target.toVec3();
        float reachDistance = this.distance(target);
        Vec3d reachPoint = new Vec3d(startingPosition.x + look.x * reachDistance, startingPosition.y + look.y * reachDistance, startingPosition.z + look.z * reachDistance);

        float checkBorder = 1.1F * reachDistance;
        AxisAlignedBB boxToScan = new AxisAlignedBB(-checkBorder, -checkBorder, -checkBorder, checkBorder, checkBorder, checkBorder).offset(this.x, this.y, this.z);

        List<Entity> entitiesHit = world.getEntitiesWithinAABBExcludingEntity(null, boxToScan);
        float closestEntity = reachDistance;

        if (entitiesHit == null || entitiesHit.isEmpty())
        {
            return null;
        }
        for (Entity entityHit : entitiesHit)
        {
            if (entityHit != null && entityHit.canBeCollidedWith() && entityHit.getCollisionBoundingBox() != null)
            {
                float border = entityHit.getCollisionBorderSize();
                AxisAlignedBB aabb = entityHit.getBoundingBox().grow(border);
                Optional<Vec3d> hitMOP = aabb.rayTrace(startingPosition, reachPoint);

                if (hitMOP.isPresent())
                {
                    if (aabb.contains(startingPosition))
                    {
                        if (0.0D < closestEntity || closestEntity == 0.0D)
                        {
                            pickedEntity = new EntityRayTraceResult(entityHit);
//                            pickedEntity.hitResult = hitMOP.get(); TODO Test
                            closestEntity = 0.0F;
                        }
                    }
                    else
                    {
                        float distance = (float) startingPosition.distanceTo(hitMOP.get());

                        if (distance < closestEntity || closestEntity == 0.0D)
                        {
                            pickedEntity = new EntityRayTraceResult(entityHit);
//                            pickedEntity.hitVec = hitMOP.hitVec;
                            closestEntity = distance;
                        }
                    }
                }
            }
        }
        return pickedEntity;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.x).append(this.y).append(this.z).hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Vector3)
        {
            Vector3 vector3 = (Vector3) o;
            return new EqualsBuilder().append(this.x, vector3.x).append(this.y, vector3.y).append(this.z, vector3.z).isEquals();
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Vector3 [" + this.x + "," + this.y + "," + this.z + "]";
    }

//    public Vector3f toVector3f()
//    {
//        return new Vector3f((float)this.x, (float)this.y, (float)this.z);
//    }
}