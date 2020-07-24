package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.client.fx.EntityParticleData;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;

public class EntityLander extends EntityLanderBase implements IIgnoreShift, ICameraZoomEntity
{
    private double lastMotionY;

    public EntityLander(EntityType<EntityLander> type, World world)
    {
        super(type, world);
    }

    public EntityLander(ServerPlayerEntity player)
    {
        super(GCEntities.LANDER.get(), player);
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    @Override
    protected void registerData()
    {

    }

    @Override
    public double getMountedYOffset()
    {
        return 2.25;
    }

    @Override
    public float getRotateOffset()
    {
        return +0.0F;
    }

    @Override
    public void tick()
    {
        super.tick();

        this.lastMotionY = this.getMotion().y;
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        this.lastMotionY = this.getMotion().y;
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);
    }

//    @Override
//    public String getName()
//    {
//        return GCCoreUtil.translate("container.lander.name");
//    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean processInitialInteract(PlayerEntity player, Hand hand)
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

        if (this.getPassengers().isEmpty() && player instanceof ServerPlayerEntity)
        {
//            GCCoreUtil.openParachestInv((ServerPlayerEntity) player, this);
            return true;
        }
        else if (player instanceof ServerPlayerEntity)
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
                this.setMotion(getMotion().x, Math.min(this.getMotion().y + 0.03F, this.getPosY() < 90 ? -0.15 : -1.0), getMotion().z);
                return true;
            case 5:
                this.setMotion(getMotion().x, Math.min(this.getMotion().y - 0.022F, -1.0), getMotion().z);
                return true;
        }

        return false;
    }

    @Override
    public void spawnParticles()
    {
        if (this.ticks > 40 && this.rotationPitch != 0.0000001F)
        {
            double sinPitch = Math.sin(this.rotationPitch / Constants.RADIANS_TO_DEGREES_D);
            final double x1 = 4 * Math.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            final double z1 = 4 * Math.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            final double y1 = -4 * Math.abs(Math.cos(this.rotationPitch / Constants.RADIANS_TO_DEGREES_D));

            final Map<Vector3, Vector3> particleMap = new HashMap<>();
            particleMap.put(new Vector3(), new Vector3((float) x1, (float) (y1 + this.getMotion().y / 2), (float) z1));
            LivingEntity passenger = this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof LivingEntity) ? null : (LivingEntity) this.getPassengers().get(0);
            this.world.addParticle(new EntityParticleData(GCParticles.LANDER_FLAME, passenger != null ? passenger.getUniqueID() : getUniqueID()),
                    this.getPosX(), this.getPosY() + 1D + this.getMotion().y / 2, this.getPosZ(),
                    x1, y1 + this.getMotion().y / 2, z1);
        }
    }

    @Override
    public void tickInAir()
    {
        super.tickInAir();

        if (this.world.isRemote)
        {
            if (!this.onGround)
            {
                this.setMotion(getMotion().add(0.0, -0.008D, 0.0));
            }

            double motY = -1 * Math.sin(this.rotationPitch / Constants.RADIANS_TO_DEGREES_D);
            double motX = Math.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * motY;
            double motZ = Math.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * motY;
            this.setMotion(motX / 2.0, getMotion().y, motZ / 2.0);
        }
    }

    @Override
    public void tickOnGround()
    {
        //Signal switch off flames
        this.rotationPitch = 0.0000001F;
    }

    @Override
    public void onGroundHit()
    {
        if (!this.world.isRemote)
        {
            if (Math.abs(this.lastMotionY) > 2.0D)
            {
                for (Entity entity : this.getPassengers())
                {
                    entity.stopRiding();
                    if (entity instanceof ServerPlayerEntity)
                    {
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, GCCoreUtil.getDimensionType(this.world), new Object[]{}), (ServerPlayerEntity) entity);
                    }
                    entity.setMotion(0.0, 0.0, 0.0);
                    entity.setPosition(entity.getPosX(), this.getPosY() + this.getMountedYOffset(), entity.getPosZ());
                    if (this.world instanceof ServerWorld)
                    {
                        ((ServerWorld) this.world).chunkCheck(entity);
                    }
                }
                this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 12, Explosion.Mode.BREAK);

                this.remove();
            }
        }
    }

    @Override
    public Vec3d getMotionVec()
    {
        if (this.onGround)
        {
            return new Vec3d(0, 0, 0);
        }

        if (this.ticks >= 40 && this.ticks < 45)
        {
            this.setMotion(this.getMotion().x, this.getInitialMotionY(), this.getMotion().z);
        }

        return new Vec3d((float) this.getMotion().x, (float) (this.ticks < 40 ? 0 : this.getMotion().y), (float) this.getMotion().z);
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
        return this.isAlive();
    }
}