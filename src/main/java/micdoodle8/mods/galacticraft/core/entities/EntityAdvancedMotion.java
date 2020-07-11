package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketEntityUpdate;
import micdoodle8.mods.galacticraft.core.network.PacketEntityUpdate.IEntityFullSync;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityAdvancedMotion extends InventoryEntity implements IControllableEntity, IEntityFullSync
{
    protected long ticks = 0;

    public float currentDamage;
    public int timeSinceHit;
    public int rockDirection;

    public double advancedPositionX;
    public double advancedPositionY;
    public double advancedPositionZ;
    public double advancedYaw;
    public double advancedPitch;
    public int posRotIncrements;

    protected boolean lastOnGround;

    public EntityAdvancedMotion(EntityType<?> type, World world)
    {
        super(type, world);
        this.preventEntitySpawning = true;
        this.ignoreFrustumCheck = true;
//        this.isImmuneToFire = true;
    }

    public EntityAdvancedMotion(EntityType<?> type, World world, double var2, double var4, double var6)
    {
        this(type, world);
        this.setPosition(var2, var4, var6);
    }

    @Override
    protected void registerData()
    {
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public double getMountedYOffset()
    {
        return this.getHeight() - 1.0D;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return this.isAlive();
    }

    @Override
    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            final double offsetx = Math.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D + 114.8) * -0.5D;
            final double offsetz = Math.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D + 114.8) * -0.5D;
            passenger.setPosition(this.getPosX() + offsetx, this.getPosY() + this.getMountedYOffset() + passenger.getYOffset(), this.getPosZ() + offsetz);
        }
    }

    @Override
    public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround)
    {
        if (this.world.isRemote)
        {
            this.advancedPositionX = x;
            this.advancedPositionY = y;
            this.advancedPositionZ = z;
            this.advancedYaw = yaw;
            this.advancedPitch = pitch;
            this.setMotion(motX, motY, motZ);
            this.posRotIncrements = 5;
        }
        else
        {
            this.setPosition(x, y, z);
            this.setRotation(yaw, pitch);
            this.setMotion(motX, motY, motZ);
            if (onGround || this.forceGroundUpdate())
            {
                this.onGround = onGround;
            }
        }
    }

    protected boolean forceGroundUpdate()
    {
        return true;
    }

    @Override
    public void performHurtAnimation()
    {
        this.rockDirection = -this.rockDirection;
        this.timeSinceHit = 10;
        this.currentDamage *= 5;
    }

    @Override
    public boolean attackEntityFrom(DamageSource var1, float var2)
    {
        if (!this.isAlive() || var1.equals(DamageSource.CACTUS) || !this.allowDamageSource(var1))
        {
            return true;
        }
        else
        {
            Entity e = var1.getTrueSource();
            if (this.isInvulnerableTo(var1) || this.getPosY() > 300 || (e instanceof LivingEntity && !(e instanceof PlayerEntity)))
            {
                return false;
            }
            else
            {
                this.rockDirection = -this.rockDirection;
                this.timeSinceHit = 10;
                this.currentDamage = this.currentDamage + var2 * 10;
                this.markVelocityChanged();

                if (e instanceof PlayerEntity && ((PlayerEntity) e).abilities.isCreativeMode)
                {
                    this.currentDamage = 100;
                }

                if (this.currentDamage > 70)
                {
                    if (!this.getPassengers().isEmpty())
                    {
                        this.removePassengers();

                        return false;
                    }

                    if (!this.world.isRemote)
                    {
                        this.dropItems();

                        this.remove();
                    }
                }

                return true;
            }
        }
    }

    public abstract List<ItemStack> getItemsDropped();

    public abstract boolean shouldMove();

    public abstract void spawnParticles();

    public abstract void tickInAir();

    public abstract void tickOnGround();

    public abstract void onGroundHit();

    public abstract Vector3D getMotionVec();

    /**
     * Can be called in the superclass init method
     * before the subclass fields have been initialised!
     * Therefore include null checks!!!
     */
    public abstract ArrayList<Object> getNetworkedData();

    /**
     * @return ticks between packets being sent to client
     */
    public abstract int getPacketTickSpacing();

    /**
     * @return players within this distance will recieve packets from this
     * entity
     */
    public abstract double getPacketSendDistance();

    public abstract void readNetworkedData(ByteBuf buffer);

    public abstract boolean allowDamageSource(DamageSource damageSource);

    public void dropItems()
    {
        if (this.getItemsDropped() == null)
        {
            return;
        }

        for (final ItemStack item : this.getItemsDropped())
        {
            if (item != null && !item.isEmpty())
            {
                this.entityDropItem(item, 0);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        if (!this.getPassengers().isEmpty())
        {
            if (this.getPassengers().contains(Minecraft.getInstance().player))
            {
            }
            else
            {
                this.posRotIncrements = posRotationIncrements + 5;
                this.advancedPositionX = x;
                this.advancedPositionY = y;
                this.advancedPositionZ = z;
                this.advancedYaw = yaw;
                this.advancedPitch = pitch;
            }
        }
    }

    @Override
    public void move(MoverType typeIn, Vec3d pos)
    {
        if (this.shouldMove())
        {
            super.move(typeIn, pos);
        }
    }

    public abstract boolean canSetPositionClient();

    public abstract boolean shouldSendAdvancedMotionPacket();

    @Override
    public void tick()
    {
        this.ticks++;

        super.tick();

        if (this.canSetPositionClient() && this.world.isRemote && (this.getPassengers().isEmpty() || !this.getPassengers().contains(Minecraft.getInstance().player)))
        {
            double x;
            double y;
            double var12;
            double z;
            if (this.posRotIncrements > 0)
            {
                x = this.getPosX() + (this.advancedPositionX - this.getPosX()) / this.posRotIncrements;
                y = this.getPosY() + (this.advancedPositionY - this.getPosY()) / this.posRotIncrements;
                z = this.getPosZ() + (this.advancedPositionZ - this.getPosZ()) / this.posRotIncrements;
                var12 = MathHelper.wrapDegrees(this.advancedYaw - this.rotationYaw);
                this.rotationYaw = (float) (this.rotationYaw + var12 / this.posRotIncrements);
                this.rotationPitch = (float) (this.rotationPitch + (this.advancedPitch - this.rotationPitch) / this.posRotIncrements);
                --this.posRotIncrements;
                this.setPosition(x, y, z);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
//                x = this.getPosX() + this.motionX;
//                y = this.getPosY() + this.motionY;
//                z = this.getPosZ() + this.motionZ;
//                this.setPosition(x, y, z);
            }
        }

        if (this.timeSinceHit > 0)
        {
            this.timeSinceHit--;
        }

        if (this.currentDamage > 0)
        {
            this.currentDamage--;
        }

        if (this.world.isRemote)
        {
            this.spawnParticles();
        }

        if (this.onGround)
        {
            this.tickOnGround();
        }
        else
        {
            this.tickInAir();
        }

        if (this.world.isRemote)
        {
            Vector3D mot = this.getMotionVec();
            this.setMotion(mot.x, mot.y, mot.z);
        }
        //Necessary on both server and client to achieve a correct this.onGround setting
        this.move(MoverType.SELF, this.getMotion());

        if (this.onGround && !this.lastOnGround)
        {
            this.onGroundHit();
        }

        if (shouldSendAdvancedMotionPacket())
        {
            if (this.world.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketEntityUpdate(this));
            }

            if (!this.world.isRemote && this.ticks % 5 == 0)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketEntityUpdate(this), new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 50.0, GCCoreUtil.getDimensionType(this.world)));
            }
        }

        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();
        this.lastOnGround = this.onGround;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        sendData.addAll(this.getNetworkedData());
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.readNetworkedData(buffer);
    }

//    @OnlyIn(Dist.CLIENT)
//    public void spawnParticles(Map<Vector3, Vector3> points)
//    {
//        for (final Entry<Vector3, Vector3> vec : points.entrySet())
//        {
//            final Vector3 posVec = vec.getKey();
//            final Vector3 motionVec = vec.getValue();
//
//            this.addParticle(this.getParticle(this.rand, posVec.x, posVec.y, posVec.z, motionVec.x, motionVec.y, motionVec.z));
//        }
//    }

//    @OnlyIn(Dist.CLIENT)
//    public void addParticle(Particle fx)
//    {
//        final Minecraft mc = Minecraft.getInstance();
//
//        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
//        {
//            if (fx != null)
//            {
//                mc.effectRenderer.addEffect(fx);
//            }
//        }
//    } TODO Particles
}