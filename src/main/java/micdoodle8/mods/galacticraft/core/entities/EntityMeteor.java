package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.ILaserTrackableFast;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.block.AirBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EntityMeteor extends Entity implements ILaserTrackableFast
{
    private static final DataParameter<Integer> SIZE = EntityDataManager.createKey(EntityMeteor.class, DataSerializers.VARINT);
    public MobEntity shootingEntity;
    public int size;

    public EntityMeteor(EntityType<EntityMeteor> type, World world)
    {
        super(type, world);
//        this.setSize(1.0F, 1.0F);
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    //    public EntityMeteor(World world, double x, double y, double z, double motX, double motY, double motZ, int size)
//    {
//        this(world);
//        this.size = size;
//        this.setSize(1.0F, 1.0F);
//        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
//        this.setPosition(x, y, z);
//        this.motionX = motX;
//        this.motionY = motY;
//        this.motionZ = motZ;
//        this.setSize(size);
//    }

    @Override
    public void tick()
    {
        this.setRotation(this.rotationYaw + 2F, this.rotationPitch + 2F);
        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();
//        this.motionY -= 0.03999999910593033D;
        this.setMotion(this.getMotion().add(0.0, 0.03999999910593033, 0.0));
        this.move(MoverType.SELF, this.getMotion());

        if (this.world.isRemote)
        {
            this.spawnParticles();
        }

        Vec3d currentPosition = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        Vec3d nextPosition = new Vec3d(this.getPosX() + this.getMotion().x, this.getPosY() + this.getMotion().y, this.getPosZ() + this.getMotion().z);
        // currentPosition, nextPosition, true, true, false
        RayTraceResult collisionIntercept = this.world.rayTraceBlocks(new RayTraceContext(currentPosition, nextPosition, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        currentPosition = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        nextPosition = new Vec3d(this.getPosX() + this.getMotion().x, this.getPosY() + this.getMotion().y, this.getPosZ() + this.getMotion().z);

        if (collisionIntercept.getType() != RayTraceResult.Type.MISS)
        {
            nextPosition = new Vec3d(collisionIntercept.getHitVec().x, collisionIntercept.getHitVec().y, collisionIntercept.getHitVec().z);
        }

        Entity collidingEntity = null;
        final List<?> nearbyEntities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.getMotion()).grow(2.0D, 2.0D, 2.0D));
        double distanceToCollidingEntityIntercept = 0.0D;
        final Iterator<?> nearbyEntitiesIterator = nearbyEntities.iterator();
        final double entityBBPadding = 0.01D;

        while (nearbyEntitiesIterator.hasNext())
        {
            final Entity nearbyEntity = (Entity) nearbyEntitiesIterator.next();

            if (nearbyEntity.canBeCollidedWith() && !nearbyEntity.isEntityEqual(this.shootingEntity))
            {
                final AxisAlignedBB nearbyEntityPaddedBB = nearbyEntity.getBoundingBox().grow(entityBBPadding, entityBBPadding, entityBBPadding);
                final Optional<Vec3d> nearbyEntityIntercept = nearbyEntityPaddedBB.rayTrace(currentPosition, nextPosition);

                if (nearbyEntityIntercept.isPresent())
                {
                    final double distanceToNearbyEntityIntercept = currentPosition.distanceTo(nearbyEntityIntercept.get());

                    if (distanceToNearbyEntityIntercept < distanceToCollidingEntityIntercept || distanceToCollidingEntityIntercept == 0.0D)
                    {
                        collidingEntity = nearbyEntity;
                        distanceToCollidingEntityIntercept = distanceToNearbyEntityIntercept;
                    }
                }
            }
        }

        if (collidingEntity != null)
        {
            collisionIntercept = new EntityRayTraceResult(collidingEntity);
        }

        if (collisionIntercept.getType() != RayTraceResult.Type.MISS)
        {
            this.onImpact(collisionIntercept);
        }

        if (this.getPosY() <= -20 || this.getPosY() >= 400)
        {
            this.remove();
        }
    }

    protected void spawnParticles()
    {
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 1D + Math.random(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX() + Math.random() / 2, this.getPosY() + 1D + Math.random() / 2, this.getPosZ(), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 1D + Math.random(), this.getPosZ() + Math.random(), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX() - Math.random() / 2, this.getPosY() + 1D + Math.random() / 2, this.getPosZ(), 0.0D, 0.0D, 0.0D);
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 1D + Math.random(), this.getPosZ() - Math.random(), 0.0D, 0.0D, 0.0D);
    }

    protected void onImpact(RayTraceResult movingObjPos)
    {
        if (!this.world.isRemote)
        {
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), this.size / 3 + 2, false, Explosion.Mode.BREAK);

            if (movingObjPos != null)
            {
                BlockPos pos;
                if (movingObjPos.getType() != RayTraceResult.Type.BLOCK)
                {
                    pos = new BlockPos(movingObjPos.getHitVec());
                }
                else
                {
                    if (movingObjPos.getType() == RayTraceResult.Type.ENTITY)
                    {
                        pos = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ((EntityRayTraceResult) movingObjPos).getEntity().getPosition());
                    }
                    else
                    {
                        pos = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, this.getPosition());
                    }
                }
                BlockPos above = pos.up();
                if (this.world.getBlockState(above).getBlock() instanceof AirBlock)
                {
                    this.world.setBlockState(above, GCBlocks.fallenMeteor.getDefaultState(), 3);
                }

                if (movingObjPos.getType() == RayTraceResult.Type.ENTITY)
                {
                    ((EntityRayTraceResult) movingObjPos).getEntity().attackEntityFrom(EntityMeteor.causeMeteorDamage(this, this.shootingEntity), ConfigManagerCore.INSTANCE.hardMode.get() ? 12F : 6F);
                }
            }
        }

        this.remove();
    }

    public static DamageSource causeMeteorDamage(EntityMeteor par0EntityMeteor, Entity par1Entity)
    {
//        if (par1Entity != null && par1Entity instanceof PlayerEntity)
//        {
//            LanguageMap.getInstance().translateKeyFormatted("death." + "meteor", PlayerUtil.getName(((PlayerEntity) par1Entity)) + " was hit by a meteor! That's gotta hurt!");
//        } TODO Was this ever sent?
        return new IndirectEntityDamageSource("explosion", par0EntityMeteor, par1Entity).setProjectile();
    }

    @Override
    protected void registerData()
    {
        this.dataManager.register(SIZE, this.size);
        this.noClip = true;
    }

    public int getSize()
    {
        return this.dataManager.get(SIZE);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setSize(int par1)
    {
        this.dataManager.set(SIZE, par1);
    }

    @Override
    protected void readAdditional(CompoundNBT compound)
    {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound)
    {

    }
}
