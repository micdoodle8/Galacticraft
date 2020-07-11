package micdoodle8.mods.galacticraft.api.vector;

import com.sun.javafx.geom.Vec3f;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Optional;

/**
 * Vector3 Class is used for defining objects in a 3D space.
 *
 * @author Calclavia
 */

public class Vector3D implements Cloneable
{
    public double x;
    public double y;
    public double z;

    public Vector3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D()
    {
        this(0, 0, 0);
    }

    public Vector3D(Vector3D vector)
    {
        this(vector.x, vector.y, vector.z);
    }

    public Vector3D(double amount)
    {
        this(amount, amount, amount);
    }

    public Vector3D(Entity par1)
    {
        this(par1.getPosX(), par1.getPosY(), par1.getPosZ());
    }

    public Vector3D(BlockPos pos)
    {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector3D(TileEntity par1)
    {
        this(par1.getPos());
    }

    public Vector3D(Vec3d par1)
    {
        this(par1.x, par1.y, par1.z);
    }

    public Vector3D(Vec3f par1)
    {
        this(par1.x, par1.y, par1.z);
    }

    public Vector3D(RayTraceResult par1)
    {
        this(par1.getHitVec());
    }

    public Vector3D(Direction direction)
    {
        this(direction.getXOffset(), direction.getYOffset(), direction.getZOffset());
    }

    public Vector3D(CompoundNBT nbt)
    {
        this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
    }

    public Vector3D(BlockVec3 vec)
    {
        this(vec.x, vec.y, vec.z);
    }

    public BlockPos toBlockPos()
    {
        return new BlockPos(intX(), intY(), intZ());
    }

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

    public double getMagnitudeSquared()
    {
        return x * x + y * y + z * z;
    }

    public double getMagnitude()
    {
        return Math.sqrt(this.getMagnitudeSquared());
    }

    public Vector3D translate(Vector3D par1)
    {
        this.x += par1.x;
        this.y += par1.y;
        this.z += par1.z;
        return this;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.x).append(this.y).append(this.z).hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Vector3D)
        {
            Vector3D vector3 = (Vector3D) o;
            return new EqualsBuilder().append(this.x, vector3.x).append(this.y, vector3.y).append(this.z, vector3.z).isEquals();
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Vector3D [" + this.x + "," + this.y + "," + this.z + "]";
    }
}