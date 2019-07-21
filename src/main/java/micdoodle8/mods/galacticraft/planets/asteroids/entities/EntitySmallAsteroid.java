package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntitySmallAsteroid extends Entity
{
    private static final DataParameter<Float> SPIN_PITCH = EntityDataManager.createKey(EntitySmallAsteroid.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> SPIN_YAW = EntityDataManager.createKey(EntitySmallAsteroid.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> ASTEROID_TYPE = EntityDataManager.createKey(EntitySmallAsteroid.class, DataSerializers.VARINT);
    public float spinPitch;
    public float spinYaw;
    public int type;
    private boolean firstUpdate = true;

    public EntitySmallAsteroid(World world)
    {
        super(world);
        this.setSize(1.0F, 1.0F);
        this.isImmuneToFire = true;
    }

    @Override
    public void onEntityUpdate()
    {
        if (!this.firstUpdate)
        {
            // Kill non-moving entities
            if (Math.abs(this.posX - this.prevPosX) + Math.abs(this.posZ - this.prevPosZ) <= 0)
            {
                this.setDead();
            }

            // Remove entities far outside the build range, or too old (to stop accumulations)
            else if (this.posY > 288D || this.posY < -32D || this.ticksExisted > 3000)
            {
                this.setDead();
            }
        }

        super.onEntityUpdate();

        if (!this.world.isRemote)
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

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.firstUpdate = false;
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(SPIN_PITCH, 0.0F);
        this.dataManager.register(SPIN_YAW, 0.0F);
        this.dataManager.register(ASTEROID_TYPE, 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        return compound;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
    }

    public float getSpinPitch()
    {
        return this.dataManager.get(SPIN_PITCH);
    }

    public float getSpinYaw()
    {
        return this.dataManager.get(SPIN_YAW);
    }

    public void setSpinPitch(float pitch)
    {
        this.dataManager.set(SPIN_PITCH, pitch);
    }

    public void setSpinYaw(float yaw)
    {
        this.dataManager.set(SPIN_YAW, yaw);
    }

    public int getAsteroidType()
    {
        return this.dataManager.get(ASTEROID_TYPE);
    }

    public void setAsteroidType(int type)
    {
        this.dataManager.set(ASTEROID_TYPE, type);
    }
}
