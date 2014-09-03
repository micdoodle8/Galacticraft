package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;

public class EntityEntryPod extends EntityLanderBase implements IScaleableFuelLevel, ICameraZoomEntity, IIgnoreShift
{
    public EntityEntryPod(World var1)
    {
        super(var1, 0.0F);
        this.setSize(1.5F, 3.0F);
    }

    public EntityEntryPod(EntityPlayerMP player)
    {
        super(player, 0.0F);
        this.setSize(1.5F, 3.0F);
    }

    @Override
    public double getInitialMotionY()
    {
        return -0.5F;
    }

    @Override
    public double getMountedYOffset()
    {
        return this.height - 2.0D;
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

    @Override
    public EntityFX getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ)
    {
        return null;
    }

    @Override
    public void tickOnGround()
    {

    }

    @Override
    public void tickInAir()
    {
        super.tickInAir();

        if (this.worldObj.isRemote)
        {
            if (!this.onGround)
            {
                this.motionY -= 0.002D;
            }
        }
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
            return new Vector3(0, 0, 0);
        }

        if (this.ticks >= 40 && this.ticks < 45)
        {
            this.motionY = this.getInitialMotionY();
        }

        return new Vector3(this.motionX, this.motionY, this.motionZ);
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
    public boolean pressKey(int key)
    {
        return false;
    }

    @Override
    public String getInventoryName()
    {
        return GCCoreUtil.translate("container.entryPod.name");
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
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
    public boolean shouldIgnoreShiftExit()
    {
        return !this.onGround;
    }
}
