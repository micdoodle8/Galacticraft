package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * GCCoreEntityControllable.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreEntityControllable extends Entity implements IControllableEntity
{
	public GCCoreEntityControllable(World par1World)
	{
		super(par1World);
	}

	public abstract void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround);
}
