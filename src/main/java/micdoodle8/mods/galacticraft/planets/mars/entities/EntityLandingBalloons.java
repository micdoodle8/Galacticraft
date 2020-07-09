package micdoodle8.mods.galacticraft.planets.mars.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.planets.mars.util.MarsUtil;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class EntityLandingBalloons extends EntityLanderBase implements IIgnoreShift, ICameraZoomEntity
{
    private int groundHitCount;
    private float rotationPitchSpeed;
    private float rotationYawSpeed;

    public EntityLandingBalloons(EntityType<? extends EntityLandingBalloons> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(2.0F, 2.0F);
        this.rotationPitchSpeed = this.rand.nextFloat();
        this.rotationYawSpeed = this.rand.nextFloat();
    }

    public static EntityLandingBalloons createEntityLandingBalloons(ServerPlayerEntity player)
    {
        EntityLandingBalloons balloons = new EntityLandingBalloons(MarsEntities.LANDING_BALLOONS.get(), player.world);

        GCPlayerStats stats = GCPlayerStats.get(player);
        balloons.stacks = NonNullList.withSize(stats.getRocketStacks().size() + 1, ItemStack.EMPTY);
        balloons.fuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), stats.getFuelLevel()));

        for (int i = 0; i < stats.getRocketStacks().size(); i++)
        {
            if (!stats.getRocketStacks().get(i).isEmpty())
            {
                balloons.stacks.set(i, stats.getRocketStacks().get(i).copy());
            }
            else
            {
                balloons.stacks.get(i).setCount(0);
            }
        }

        balloons.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);

        player.startRiding(balloons, true);
//        this.setSize(2.0F, 2.0F);
        return balloons;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
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
    public void tick()
    {
        if (!this.getPassengers().isEmpty())
        {
            for (Entity entity : this.getPassengers())
            {
                entity.onGround = false;
            }
        }

        super.tick();

        if (!this.getPassengers().isEmpty())
        {
            for (Entity entity : this.getPassengers())
            {
                entity.onGround = false;
            }
        }

        if (!this.onGround)
        {
            this.rotationPitch += this.rotationPitchSpeed;
            this.rotationYaw += this.rotationYawSpeed;
        }
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);
        this.groundHitCount = nbt.getInt("GroundHitCount");
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);
        nbt.putInt("GroundHitCount", this.groundHitCount);
    }

//    @Override
//    public String getName()
//    {
//        return GCCoreUtil.translate("container.mars_lander.name");
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

            this.removePassengers();

            return true;
        }
        else if (this.getPassengers().isEmpty() && this.groundHitCount >= 14 && player instanceof ServerPlayerEntity)
        {
//            MarsUtil.openParachestInventory((ServerPlayerEntity) player, this);  TODO guis
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

        return ((!this.getPassengers().isEmpty() && this.groundHitCount < 14) || !this.onGround);
    }

//    @Override
//    public boolean shouldSpawnParticles()
//    {
//        return false;
//    }
//
//    @Override
//    public Map<Vector3, Vector3> getParticleMap()
//    {
//        return null;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public Particle getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ)
//    {
//        return null;
//    }

    @Override
    public void spawnParticles()
    {

    }

    @Override
    public void tickInAir()
    {
        if (this.world.isRemote)
        {
            if (this.groundHitCount == 0)
            {
                this.setMotion(this.getMotion().x, -this.posY / 50.0D, this.getMotion().z);
            }
            else if (this.groundHitCount < 14 || this.shouldMove())
            {
                this.setMotion(this.getMotion().x, this.getMotion().y * 0.95 - 0.08, this.getMotion().z);
            }
            else
            {
                if (!this.shouldMove())
                {
                    this.setMotion(0.0, 0.0, 0.0);
                    this.rotationPitchSpeed = 0.0F;
                    this.rotationYawSpeed = 0.0F;
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
    public Vector3D getMotionVec()
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
                return new Vector3D(mX, mY, mZ);
            }
        }

        if (this.ticks >= 40 && this.ticks < 45)
        {
//            this.motionY = this.getInitialMotionY();
            this.setMotion(this.getMotion().x, this.getInitialMotionY(), this.getMotion().z);
        }

        if (!this.shouldMove())
        {
            return new Vector3D(0, 0, 0);
        }

        return new Vector3D(this.getMotion().x, this.ticks < 40 ? 0 : this.getMotion().y, this.getMotion().z);
    }

    @Override
    public ArrayList<Object> getNetworkedData()
    {
        ArrayList<Object> objList = new ArrayList<Object>();
        objList.addAll(super.getNetworkedData());
        if ((this.world.isRemote && this.hasReceivedPacket && this.groundHitCount <= 14) || (!this.world.isRemote && this.groundHitCount == 14))
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