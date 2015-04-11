package mekanism.api;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Pos3D - a way of performing operations on objects in a three dimensional environment.
 * @author aidancbrady
 *
 */
public class Pos3D
{
	public double xPos;
	public double yPos;
	public double zPos;

	public Pos3D()
	{
		this(0, 0, 0);
	}
	
	public Pos3D(Vec3 vec)
	{
		xPos = vec.xCoord;
		yPos = vec.yCoord;
		zPos = vec.zCoord;
	}

	public Pos3D(double x, double y, double z)
	{
		xPos = x;
		yPos = y;
		zPos = z;
	}
	
	public Pos3D(Coord4D coord)
	{
		xPos = coord.xCoord;
		yPos = coord.yCoord;
		zPos = coord.zCoord;
	}

	/**
	 * Creates a Pos3D with an entity's posX, posY, and posZ values.
	 * @param entity - entity to create the Pos3D from
	 */
	public Pos3D(Entity entity)
	{
		this(entity.posX, entity.posY, entity.posZ);
	}

	/**
	 * Creates a Pos3D with a TileEntity's xCoord, yCoord and zCoord values.
	 * @param tileEntity - TileEntity to create the Pos3D from
	 */
	public Pos3D(TileEntity tileEntity)
	{
		this(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
	}

	/**
	 * Creates and returns a Pos3D with values representing the difference between this and the Pos3D in the parameters.
	 * @param pos - Pos3D to subtract
	 * @return difference of the two Pos3Ds
	 */
	public Pos3D diff(Pos3D pos)
	{
		return new Pos3D(xPos-pos.xPos, yPos-pos.yPos, zPos-pos.zPos);
	}

	/**
	 * Creates a new Pos3D from the motion of an entity.
	 * @param entity
	 * @return
	 */
	public static Pos3D fromMotion(Entity entity)
	{
		return new Pos3D(entity.motionX, entity.motionY, entity.motionZ);
	}

	/**
	 * Centres a block-derived Pos3D
	 */
	public Pos3D centre()
	{
		return translate(0.5, 0.5, 0.5);
	}

	/**
	 * Translates this Pos3D by the defined values.
	 * @param x - amount to translate on the x axis
	 * @param y - amount to translate on the y axis
	 * @param z - amount to translate on the z axis
	 * @return the translated Pos3D
	 */
	public Pos3D translate(double x, double y, double z)
	{
		xPos += x;
		yPos += y;
		zPos += z;

		return this;
	}

	/**
	 * Performs the same operation as translate(x, y, z), but with a Pos3D value instead.
	 * @param pos - Pos3D value to translate by
	 * @return translated Pos3D
	 */
	public Pos3D translate(Pos3D pos)
	{
		return translate(pos.xPos, pos.yPos, pos.zPos);
	}

	/**
	 * Performs the same operation as translate(x, y, z), but by a set amount in a ForgeDirection
	 */
	public Pos3D translate(ForgeDirection direction, double amount)
	{
		return translate(direction.offsetX * amount, direction.offsetY * amount, direction.offsetZ * amount);
	}

	/**
	 * Performs the same operation as translate(x, y, z), but by a set amount in a ForgeDirection
	 */
	public Pos3D translateExcludingSide(ForgeDirection direction, double amount)
	{
		if(direction.offsetX == 0) xPos += amount;
		if(direction.offsetY == 0) yPos += amount;
		if(direction.offsetZ == 0) zPos += amount;

		return this;
	}

	/**
	 * Returns the distance between this and the defined Pos3D.
	 * @param pos - the Pos3D to find the distance to
	 * @return the distance between this and the defined Pos3D
	 */
	public double distance(Pos3D pos)
	{
		double subX = xPos - pos.xPos;
		double subY = yPos - pos.yPos;
		double subZ = zPos - pos.zPos;
		return MathHelper.sqrt_double(subX * subX + subY * subY + subZ * subZ);
	}

	/**
	 * Rotates this Pos3D by the defined yaw value.
	 * @param yaw - yaw to rotate by
	 * @return rotated Pos3D
	 */
	public Pos3D rotateYaw(double yaw)
	{
		double yawRadians = Math.toRadians(yaw);

		double x = xPos;
		double z = zPos;

		if(yaw != 0)
		{
			xPos = x * Math.cos(yawRadians) - z * Math.sin(yawRadians);
			zPos = z * Math.cos(yawRadians) + x * Math.sin(yawRadians);
		}

		return this;
	}
	
	public Pos3D rotatePitch(double pitch)
	{
		double pitchRadians = Math.toRadians(pitch);
		
		double y = yPos;
		double z = zPos;
		
		if(pitch != 0)
		{
			yPos = y * Math.cos(pitchRadians) - z * Math.sin(pitchRadians);
			zPos = z * Math.cos(pitchRadians) + y * Math.sin(pitchRadians);
		}
		
		return this;
	}
	
	public Pos3D rotate(double yaw, double pitch)
	{
		double yawRadians = Math.toRadians(yaw);
		double pitchRadians = Math.toRadians(pitch);
		
		double x = xPos;
		double y = yPos;
		double z = zPos;
		
		xPos = x * Math.cos(yawRadians) - z * Math.sin(yawRadians);
		yPos = y * Math.cos(pitchRadians) - z * Math.sin(pitchRadians);
		zPos = (z * Math.cos(yawRadians) + x * Math.sin(yawRadians)) * (z * Math.cos(pitchRadians) + y * Math.sin(pitchRadians));
		
		return this;
	}
	
	public Pos3D multiply(Pos3D pos)
	{
		xPos *= pos.xPos;
		yPos *= pos.yPos;
		zPos *= pos.zPos;
		
		return this;
	}

	/**
	 * Scales this Pos3D by the defined x, y, an z values.
	 * @param x - x value to scale by
	 * @param y - y value to scale by
	 * @param z - z value to scale by
	 * @return scaled Pos3D
	 */
	public Pos3D scale(double x, double y, double z)
	{
		xPos *= x;
		yPos *= y;
		zPos *= z;

		return this;
	}

	/**
	 * Performs the same operation as scale(x, y, z), but with a value representing all three dimensions.
	 * @param scale - value to scale by
	 * @return scaled Pos3D
	 */
	public Pos3D scale(double scale)
	{
		return scale(scale, scale, scale);
	}

	public static AxisAlignedBB getAABB(Pos3D pos1, Pos3D pos2)
	{
		return AxisAlignedBB.getBoundingBox(
				Math.min(pos1.xPos, pos2.xPos),
				Math.min(pos1.yPos, pos2.yPos),
				Math.min(pos1.zPos, pos2.zPos),
				Math.max(pos1.xPos, pos2.xPos),
				Math.max(pos1.yPos, pos2.yPos),
				Math.max(pos1.zPos, pos2.zPos)
		);
	}

	@Override
	public Pos3D clone()
	{
		return new Pos3D(xPos, yPos, zPos);
	}

	@Override
	public String toString()
	{
		return "[Pos3D: " + xPos + ", " + yPos + ", " + zPos + "]";
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Pos3D &&
				((Pos3D)obj).xPos == xPos &&
				((Pos3D)obj).yPos == yPos &&
				((Pos3D)obj).zPos == zPos;
	}

	@Override
	public int hashCode()
	{
		int code = 1;
		code = 31 * code + new Double(xPos).hashCode();
		code = 31 * code + new Double(yPos).hashCode();
		code = 31 * code + new Double(zPos).hashCode();
		return code;
	}
}
