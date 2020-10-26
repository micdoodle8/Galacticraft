package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityProjectileTNT;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fluids.FluidStack;

public class EntityEntryPodVenus extends EntityLanderBase implements IScaleableFuelLevel, ICameraZoomEntity, IIgnoreShift
{
    private Integer groundPosY = null;

    public EntityEntryPodVenus(EntityType<? extends EntityEntryPodVenus> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.5F, 3.0F);
    }

    public static EntityEntryPodVenus createEntityEntryPodVenus(ServerPlayerEntity player)
    {
        EntityEntryPodVenus pod = new EntityEntryPodVenus(VenusEntities.ENTRY_POD, player.world);

        GCPlayerStats stats = GCPlayerStats.get(player);
        pod.stacks = NonNullList.withSize(stats.getRocketStacks().size() + 1, ItemStack.EMPTY);
        pod.fuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), stats.getFuelLevel()));

        for (int i = 0; i < stats.getRocketStacks().size(); i++)
        {
            if (!stats.getRocketStacks().get(i).isEmpty())
            {
                pod.stacks.set(i, stats.getRocketStacks().get(i).copy());
            }
            else
            {
                pod.stacks.get(i).setCount(0);
            }
        }

        pod.setPositionAndRotation(player.getPosX(), player.getPosY(), player.getPosZ(), 0, 0);

        player.startRiding(pod, true);
        return pod;
    }

    @Override
    public double getInitialMotionY()
    {
        return -2.5F;
    }

    @Override
    public double getMountedYOffset()
    {
        return this.getHeight() - 2.0D;
    }

    @Override
    public float getRotateOffset()
    {
        //flag no rotate
        return -20F;
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
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
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
//                this.motionY -= 0.002D;
                this.setMotion(getMotion().x, this.getMotion().y - 0.002, this.getMotion().z);

                if (this.getMotion().y < -0.7F)
                {
//                    this.motionY *= 0.994F;
                    this.setMotion(getMotion().x, this.getMotion().y * 0.994F, this.getMotion().z);
                }

                if (this.getPosY() <= 242.0F)
                {
                    if (groundPosY == null)
                    {
                        this.groundPosY = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ())).getY();
                    }

                    if (this.getPosY() - this.groundPosY > 5.0F)
                    {
                        this.setMotion(getMotion().x, this.getMotion().y * 0.995F, this.getMotion().z);
                    }
                    else
                    {
                        this.setMotion(getMotion().x, this.getMotion().y * 0.9995F, this.getMotion().z);
                    }
                }
            }
        }
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
            return new Vector3D(0, 0, 0);
        }

        if (this.ticks >= 40 && this.ticks < 45)
        {
            this.setMotion(this.getMotion().x, this.getInitialMotionY(), this.getMotionVec().z);
        }

        if (!this.shouldMove())
        {
            return new Vector3D(0, 0, 0);
        }

        return new Vector3D(this.getMotion());
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

//    @Override
//    public String getName()
//    {
//        return GCCoreUtil.translate("container.entry_pod.name");
//    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

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
//            GCCoreUtil.openParachestInv((ServerPlayerEntity) player, this); TODO guis
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
    public boolean shouldIgnoreShiftExit()
    {
        return !this.onGround;
    }

    public Integer getGroundPosY()
    {
        return groundPosY;
    }
}
