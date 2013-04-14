package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class GCCoreEntityControllable extends Entity
{
	public GCCoreEntityControllable(World par1World)
	{
		super(par1World);
	}

	public abstract boolean pressKey(int key);

	public abstract void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround);
}
