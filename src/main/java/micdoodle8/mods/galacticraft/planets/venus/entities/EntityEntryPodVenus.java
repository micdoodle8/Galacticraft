package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;

public class EntityEntryPodVenus extends EntityLanderBase implements IScaleableFuelLevel, ICameraZoomEntity, IIgnoreShift
{
    private Integer groundPosY = null;

    public EntityEntryPodVenus(World var1)
    {
        super(var1);
        this.setSize(1.5F, 3.0F);
    }

    public EntityEntryPodVenus(EntityPlayerMP player)
    {
        super(player, 0.0F);
        this.setSize(1.5F, 3.0F);
    }

    @Override
    public double getInitialMotionY()
    {
        return -2.5F;
    }

    @Override
    public double getMountedYOffset()
    {
        return this.height - 2.0D;
    }

    @Override
    public float getRotateOffset()
    {
        //flag no rotate
        return -20F;
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
    public Particle getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ)
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

        if (this.world.isRemote)
        {
            if (!this.onGround)
            {
                this.motionY -= 0.002D;

                if (this.motionY < -0.7F)
                {
                    this.motionY *= 0.994F;
                }

                if (this.posY <= 242.0F)
                {
                    if (groundPosY == null)
                    {
                        this.groundPosY = this.world.getTopSolidOrLiquidBlock(new BlockPos(this.posX, this.posY, this.posZ)).getY();
                    }

                    if (this.posY - this.groundPosY > 5.0F)
                    {
                        this.motionY *= 0.995F;
                    }
                    else
                        this.motionY *= 0.9995F;
                }
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

        if (!this.shouldMove())
        {
            return new Vector3(0, 0, 0);
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
    public String getName()
    {
        return GCCoreUtil.translate("container.entry_pod.name");
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox()
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
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        if (this.world.isRemote)
        {
            if (!this.onGround)
            {
                return false;
            }

            if (!this.getPassengers().isEmpty())
            {
                this.removePassengers();
            }

            return true;
        }

        if (this.getPassengers().isEmpty() && player instanceof EntityPlayerMP)
        {
            GCCoreUtil.openParachestInv((EntityPlayerMP) player, this);
            return true;
        }
        else if (player instanceof EntityPlayerMP)
        {
            if (!this.onGround)
            {
                return false;
            }

            this.removePassengers();
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

    public Integer getGroundPosY()
    {
        return groundPosY;
    }
}
