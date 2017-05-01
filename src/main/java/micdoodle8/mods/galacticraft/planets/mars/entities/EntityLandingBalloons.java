package micdoodle8.mods.galacticraft.planets.mars.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.util.MarsUtil;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class EntityLandingBalloons extends EntityLanderBase implements IIgnoreShift, ICameraZoomEntity
{
    private int groundHitCount;
    private float rotationPitchSpeed;
    private float rotationYawSpeed;

    public EntityLandingBalloons(World world)
    {
        super(world);
        this.setSize(2.0F, 2.0F);
        this.rotationPitchSpeed = this.rand.nextFloat();
        this.rotationYawSpeed = this.rand.nextFloat();
    }

    public EntityLandingBalloons(EntityPlayerMP player)
    {
        super(player, 0F);
        this.setSize(2.0F, 2.0F);
    }

    @Override
    public double getMountedYOffset()
    {
        return super.getMountedYOffset() - 0.9;
    }

    @Override
    public float getRotateOffset()
    {
        //Signal no rotate
        return -20.0F;
    }

    @Override
    public void onUpdate()
    {
        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.onGround = false;
        }

        super.onUpdate();

        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.onGround = false;
        }

        if (!this.onGround)
        {
            this.rotationPitch += this.rotationPitchSpeed;
            this.rotationYaw += this.rotationYawSpeed;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.groundHitCount = nbt.getInteger("GroundHitCount");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("GroundHitCount", this.groundHitCount);
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("container.mars_lander.name");
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public boolean interactFirst(EntityPlayer var1)
    {
        if (this.worldObj.isRemote)
        {
            if (!this.onGround)
            {
                return false;
            }

            if (this.riddenByEntity != null)
            {
                this.riddenByEntity.mountEntity(this);
            }

            return true;
        }
        else if (this.riddenByEntity == null && this.groundHitCount >= 14 && var1 instanceof EntityPlayerMP)
        {
            MarsUtil.openParachestInventory((EntityPlayerMP) var1, this);
            return true;
        }
        else if (var1 instanceof EntityPlayerMP)
        {
            if (!this.onGround)
            {
                return false;
            }

            var1.mountEntity(null);
            return true;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean pressKey(int key)
    {
        if (this.onGround)
        {
            return false;
        }

//        switch (key)
//        {
//            case 0: // Accelerate
//            {
//                this.rotationPitchSpeed -= 0.5F * TURN_FACTOR;
//                return true;
//            }
//            case 1: // Deccelerate
//            {
//                this.rotationPitchSpeed += 0.5F * TURN_FACTOR;
//                return true;
//            }
//            case 2: // Left
//                this.rotationYawSpeed -= 0.5F * TURN_FACTOR;
//                return true;
//            case 3: // Right
//                this.rotationYawSpeed += 0.5F * TURN_FACTOR;
//                return true;
//        }

        return false;
    }

    @Override
    public boolean shouldMove()
    {
        if (this.ticks < 40 || !this.hasReceivedPacket)
        {
            return false;
        }

        return ((this.riddenByEntity != null && this.groundHitCount < 14) || !this.onGround);
    }

    @Override
    public boolean shouldSpawnParticles()
    {
        return false;
    }

    @Override
    public Map<Vector3, Vector3> getParticleMap()
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EntityFX getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ)
    {
        return null;
    }

    @Override
    public void tickInAir()
    {
        if (this.worldObj.isRemote)
        {
            if (this.groundHitCount == 0)
            {
                this.motionY = -this.posY / 50.0D;
            }
            else if (this.groundHitCount < 14 || this.shouldMove())
            {
                this.motionY *= 0.95D;
                this.motionY -= 0.08D;
            }
            else
            {
                if (!this.shouldMove())
                {
                    this.motionY = this.motionX = this.motionZ = this.rotationPitchSpeed = this.rotationYawSpeed = 0.0F;
                }
            }
        }
    }

    @Override
    public void tickOnGround()
    {
    }

    @Override
    public void onGroundHit()
    {
    }

    @Override
    public Vector3 getMotionVec()
    {
        if (this.onGround)
        {
            if (this.groundHitCount < 14)
            {
                this.groundHitCount++;
                double mag = (1.0D / this.groundHitCount) * 4.0D;
                double mX = this.rand.nextDouble() - 0.5;
                double mY = 1.0D;
                double mZ = this.rand.nextDouble() - 0.5;
                mX *= mag / 3.0D;
                mY *= mag;
                mZ *= mag / 3.0D;
                return new Vector3(mX, mY, mZ);
            }
        }

        if (this.ticks >= 40 && this.ticks < 45)
        {
            this.motionY = this.getInitialMotionY();
        }

        if (!this.shouldMove())
        {
            return new Vector3(0, 0, 0);
        }

        return new Vector3(this.motionX, this.ticks < 40 ? 0 : this.motionY, this.motionZ);
    }

    @Override
    public ArrayList<Object> getNetworkedData()
    {
        ArrayList<Object> objList = new ArrayList<Object>();
        objList.addAll(super.getNetworkedData());
        if ((this.worldObj.isRemote && this.hasReceivedPacket && this.groundHitCount <= 14) || (!this.worldObj.isRemote && this.groundHitCount == 14))
        {
            objList.add(this.groundHitCount);
        }
        return objList;
    }

    @Override
    public int getPacketTickSpacing()
    {
        return 5;
    }

    @Override
    public double getPacketSendDistance()
    {
        return 50.0D;
    }

    @Override
    public void readNetworkedData(ByteBuf buffer)
    {
        try
        {
            super.readNetworkedData(buffer);

            if (buffer.readableBytes() > 0)
            {
                this.groundHitCount = buffer.readInt();
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean allowDamageSource(DamageSource damageSource)
    {
        return this.groundHitCount > 0 && super.allowDamageSource(damageSource);
    }

    @Override
    public double getInitialMotionY()
    {
        return 0;
    }

    @Override
    public float getCameraZoom()
    {
        return 15.0F;
    }

    @Override
    public boolean defaultThirdPerson()
    {
        return true;
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return this.groundHitCount < 14 || !this.onGround;
    }
}