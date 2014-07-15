package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntitySmallAsteroid extends Entity
{
	public float spinPitch;
	public float spinYaw;
	public int type;
    private boolean firstUpdate = true;

	public EntitySmallAsteroid(World world)
	{
		super(world);
	}

	@Override
	public void onEntityUpdate()
	{
        if (!this.firstUpdate && Math.abs(this.posX - this.prevPosX) <= 0 && Math.abs(this.posZ - this.prevPosZ) <= 0)
        {
            // Kill non-moving entities
            this.setDead();
        }

        super.onEntityUpdate();

		if (!this.worldObj.isRemote && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			this.setSpinPitch(this.spinPitch);
			this.setSpinYaw(this.spinYaw);
			this.setAsteroidType(this.type);
			this.rotationPitch += this.spinPitch;
			this.rotationYaw += this.spinYaw;
		}
		else
		{
			this.rotationPitch += this.getSpinPitch();
			this.rotationYaw += this.getSpinYaw();
		}

        double sqrdMotion = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;

        if (sqrdMotion < 0.05)
        {
            // If the motion is too low (for some odd reason), speed it back up slowly.
            this.motionX *= 1.001D;
            this.motionY *= 1.001D;
            this.motionZ *= 1.001D;
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.firstUpdate = false;
	}

	@Override
	protected void entityInit()
	{
		this.dataWatcher.addObject(10, 0.0F);
		this.dataWatcher.addObject(11, 0.0F);
		this.dataWatcher.addObject(12, 0);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		this.spinPitch = nbt.getFloat("spinPitch");
		this.spinYaw = nbt.getFloat("spinYaw");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("spinPitch", this.spinPitch);
		nbt.setFloat("spinYaw", this.spinYaw);
	}

	public float getSpinPitch()
	{
		return this.dataWatcher.getWatchableObjectFloat(10);
	}

	public float getSpinYaw()
	{
		return this.dataWatcher.getWatchableObjectFloat(11);
	}

	public void setSpinPitch(float pitch)
	{
		this.dataWatcher.updateObject(10, pitch);
	}

	public void setSpinYaw(float yaw)
	{
		this.dataWatcher.updateObject(11, yaw);
	}

	public int getAsteroidType()
	{
		return this.dataWatcher.getWatchableObjectInt(12);
	}

	public void setAsteroidType(int type)
	{
		this.dataWatcher.updateObject(12, type);
	}
}
