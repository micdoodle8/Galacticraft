package micdoodle8.mods.galacticraft.core.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.fx.EntityFXLanderFlame;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityLander extends EntityLanderBase implements IIgnoreShift, ICameraZoomEntity
{
    private double lastMotionY;

    public EntityLander(World world)
    {
        super(world, 0.0F);
        this.setSize(3.0F, 3.0F);
    }

    public EntityLander(EntityPlayerMP player)
    {
        super(player, 0.0F);
    }

    @Override
    public double getMountedYOffset()
    {
        return super.getMountedYOffset();
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        this.lastMotionY = this.motionY;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);

        this.lastMotionY = this.motionY;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
    }

    @Override
    public String getInventoryName()
    {
        return GCCoreUtil.translate("container.lander.name");
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public boolean interactFirst(EntityPlayer var1)
    {
        if (this.worldObj.isRemote)
        {
            if (this.riddenByEntity != null)
            {
                this.riddenByEntity.mountEntity(this);
            }

            return true;
        }

        if (this.riddenByEntity == null && var1 instanceof EntityPlayerMP)
        {
            GCCoreUtil.openParachestInv((EntityPlayerMP) var1, this);
            return true;
        }
        else if (var1 instanceof EntityPlayerMP)
        {
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

        float turnFactor = 2.0F;
        float angle = 45;

        switch (key)
        {
        case 0:
            this.rotationPitch = Math.min(Math.max(this.rotationPitch - 0.5F * turnFactor, -angle), angle);
            return true;
        case 1:
            this.rotationPitch = Math.min(Math.max(this.rotationPitch + 0.5F * turnFactor, -angle), angle);
            return true;
        case 2:
            this.rotationYaw -= 0.5F * turnFactor;
            return true;
        case 3:
            this.rotationYaw += 0.5F * turnFactor;
            return true;
        case 4:
            this.motionY = Math.min(this.motionY + 0.03F, this.posY < 90 ? -0.15 : -1.0);
            return true;
        case 5:
            this.motionY = Math.min(this.motionY - 0.022F, -1.0);
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldSpawnParticles()
    {
        return this.rotationPitch != 0.0000000000001F;
    }

    @Override
    public Map<Vector3, Vector3> getParticleMap()
    {
        final double x1 = 4 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        final double z1 = 4 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        final double y1 = -4 * Math.abs(Math.cos(this.rotationPitch * Math.PI / 180.0D));

        new Vector3(this);

        final Map<Vector3, Vector3> particleMap = new HashMap<Vector3, Vector3>();
        particleMap.put(new Vector3(this).translate(new Vector3(0, 1, 0)), new Vector3(x1, y1, z1));
        particleMap.put(new Vector3(this).translate(new Vector3(0, 1, 0)), new Vector3(x1, y1, z1));
        particleMap.put(new Vector3(this).translate(new Vector3(0, 1, 0)), new Vector3(x1, y1, z1));
        particleMap.put(new Vector3(this).translate(new Vector3(0, 1, 0)), new Vector3(x1, y1, z1));
        return particleMap;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EntityFX getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ)
    {
        return new EntityFXLanderFlame(this.worldObj, x, y, z, motX, motY, motZ, this.riddenByEntity instanceof EntityLivingBase ? (EntityLivingBase)this.riddenByEntity : null);
    }

    @Override
    public void tickInAir()
    {
        super.tickInAir();

        if (this.worldObj.isRemote)
        {
            if (!this.onGround)
            {
                this.motionY -= 0.008D;
            }

            double motY = -1 * Math.sin(this.rotationPitch * Math.PI / 180.0D);
            double motX = Math.cos(this.rotationYaw * Math.PI / 180.0D) * motY;
            double motZ = Math.sin(this.rotationYaw * Math.PI / 180.0D) * motY;
            this.motionX = motX / 2.0F;
            this.motionZ = motZ / 2.0F;
        }
    }

    @Override
    public void tickOnGround()
    {
        this.rotationPitch = 0.0000000000001F;
    }

    @Override
    public void onGroundHit()
    {
        if (!this.worldObj.isRemote)
        {
            if (Math.abs(this.lastMotionY) > 2.0D)
            {
                if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayerMP)
                {
                    this.riddenByEntity.mountEntity(this);
                }

                this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 12, true);

                this.setDead();
            }
        }
    }

    @Override
    public Vector3 getMotionVec()
    {
        if (this.onGround)
        {
            return new Vector3(0, 0, 0);
        }

        if (this.ticks >= 40 && this.ticks < 45)
        {
            this.motionY = this.getInitialMotionY();
        }

        return new Vector3(this.motionX, this.ticks < 40 ? 0 : this.motionY, this.motionZ);
    }

    @Override
    public float getCameraZoom()
    {
        return 15;
    }

    @Override
    public boolean defaultThirdPerson()
    {
        return true;
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return !this.onGround;
    }

    @Override
    public double getInitialMotionY()
    {
        return -2.5D;
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return null;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
}