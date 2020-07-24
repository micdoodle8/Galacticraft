package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Optional;

public class EntityWebShot extends Entity implements IProjectile
{
    public int canBePickedUp;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInAir;

    public EntityWebShot(EntityType<? extends EntityWebShot> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(0.5F, 0.5F);
    }

    public static EntityWebShot createEntityWebShot(World worldIn, double x, double y, double z)
    {
        EntityWebShot webShot = new EntityWebShot(VenusEntities.WEB_SHOT.get(), worldIn);
//        this.setSize(0.5F, 0.5F);
        webShot.setPosition(x, y, z);
        return webShot;
    }

    public static EntityWebShot createEntityWebShot(World worldIn, LivingEntity shooter, LivingEntity target, float p_i1755_4_, float p_i1755_5_)
    {
        EntityWebShot webShot = new EntityWebShot(VenusEntities.WEB_SHOT.get(), worldIn);
        webShot.shootingEntity = shooter;

        if (shooter instanceof PlayerEntity)
        {
            webShot.canBePickedUp = 1;
        }

        webShot.setRawPosition(webShot.getPosX(), shooter.getPosY() + (double) shooter.getEyeHeight() - 0.10000000149011612D, webShot.getPosZ());
        double d0 = target.getPosX() - shooter.getPosX();
        double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - webShot.getPosY();
        double d2 = target.getPosZ() - shooter.getPosZ();
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D)
        {
            float f = (float) MathHelper.atan2(d2, d0) * Constants.RADIANS_TO_DEGREES - 90.0F;
            float f1 = (float) MathHelper.atan2(d1, d3) * -Constants.RADIANS_TO_DEGREES;
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            webShot.setLocationAndAngles(shooter.getPosX() + d4, webShot.getPosY(), shooter.getPosZ() + d5, f, f1);
            float f2 = (float) (d3 * 0.20000000298023224D);
            webShot.shoot(d0, d1 + (double) f2, d2, p_i1755_4_, p_i1755_5_);
        }

        return webShot;
    }

    public static EntityWebShot createEntityWebShot(World worldIn, LivingEntity shooter, float velocity)
    {
        EntityWebShot webShot = new EntityWebShot(VenusEntities.WEB_SHOT.get(), worldIn);
        webShot.shootingEntity = shooter;

        if (shooter instanceof PlayerEntity)
        {
            webShot.canBePickedUp = 1;
        }

//        webShot.setSize(0.5F, 0.5F);
        webShot.setLocationAndAngles(shooter.getPosX(), shooter.getPosY() + (double) shooter.getEyeHeight(), shooter.getPosZ(), shooter.rotationYaw, shooter.rotationPitch);
        webShot.setRawPosition(webShot.getPosX() - MathHelper.cos(webShot.rotationYaw / Constants.RADIANS_TO_DEGREES) * 0.16F,
                webShot.getPosY() - 0.10000000149011612D,
                webShot.getPosZ() - MathHelper.sin(webShot.rotationYaw / Constants.RADIANS_TO_DEGREES) * 0.16F);
        webShot.setPosition(webShot.getPosX(), webShot.getPosY(), webShot.getPosZ());
        double motionX = -MathHelper.sin(webShot.rotationYaw / Constants.RADIANS_TO_DEGREES) * MathHelper.cos(webShot.rotationPitch / Constants.RADIANS_TO_DEGREES);
        double motionZ = MathHelper.cos(webShot.rotationYaw / Constants.RADIANS_TO_DEGREES) * MathHelper.cos(webShot.rotationPitch / Constants.RADIANS_TO_DEGREES);
        double motionY = -MathHelper.sin(webShot.rotationPitch / Constants.RADIANS_TO_DEGREES);
        webShot.shoot(motionX, motionY, motionZ, velocity * 1.5F, 1.0F);
        return webShot;
    }

    @Override
    protected void registerData()
    {
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 10.0;
        return distance < d0 * d0;
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x + this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) inaccuracy;
        y = y + this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) inaccuracy;
        z = z + this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) inaccuracy;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        this.setMotion(x, y, z);
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float) MathHelper.atan2(x, z) * Constants.RADIANS_TO_DEGREES;
        this.prevRotationPitch = this.rotationPitch = (float) MathHelper.atan2(y, f1) * Constants.RADIANS_TO_DEGREES;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.setMotion(x, y, z);

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float) MathHelper.atan2(x, z) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch = (float) MathHelper.atan2(y, f) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);
            this.prevRotationYaw = this.rotationYaw = (float) MathHelper.atan2(this.getMotion().x, this.getMotion().z) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch = (float) MathHelper.atan2(this.getMotion().y, f) * Constants.RADIANS_TO_DEGREES;
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.ticksExisted > 1000)
        {
            this.remove();
        }

        ++this.ticksInAir;
        Vec3d vec31 = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        Vec3d vec3 = new Vec3d(this.getPosX() + this.getMotion().x, this.getPosY() + this.getMotion().y, this.getPosZ() + this.getMotion().z);
        RayTraceResult castResult = this.world.rayTraceBlocks(new RayTraceContext(vec3, vec31, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        vec31 = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        vec3 = new Vec3d(this.getPosX() + this.getMotion().x, this.getPosY() + this.getMotion().y, this.getPosZ() + this.getMotion().z);

        if (castResult.getType() != RayTraceResult.Type.MISS)
        {
            vec3 = new Vec3d(castResult.getHitVec().x, castResult.getHitVec().y, castResult.getHitVec().z);
        }

        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.getMotion().x, this.getMotion().y, this.getMotion().z).grow(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;
        final double border = 0.3D;

        for (Entity entity1 : list)
        {
            if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
            {
                AxisAlignedBB axisalignedbb1 = entity1.getBoundingBox().grow(border, border, border);
                Optional<Vec3d> result = axisalignedbb1.rayTrace(vec31, vec3);

                if (result.isPresent())
                {
                    double d1 = vec31.squareDistanceTo(result.get());

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null)
        {
            castResult = new EntityRayTraceResult(entity);
        }

        if (castResult.getType() == RayTraceResult.Type.ENTITY)
        {
            EntityRayTraceResult entityResult = (EntityRayTraceResult) castResult;
            if (entityResult.getEntity() instanceof PlayerEntity)
            {
                PlayerEntity entityplayer = (PlayerEntity) entityResult.getEntity();

                if (entityplayer.abilities.disableDamage || this.shootingEntity instanceof PlayerEntity && !((PlayerEntity) this.shootingEntity).canAttackPlayer(entityplayer))
                {
                    castResult = null;
                }
            }
        }

        if (castResult != null)
        {
            if (castResult.getType() == RayTraceResult.Type.ENTITY)
            {
                EntityRayTraceResult entityResult = (EntityRayTraceResult) castResult;
                if (entityResult.getEntity() != this.shootingEntity && !this.world.isRemote)
                {
                    if (entityResult.getEntity() instanceof LivingEntity)
                    {
                        ((LivingEntity) entityResult.getEntity()).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 180, 2, true, true));
                        this.remove();
                    }
                    else
                    {
//                        this.motionX *= -0.10000000149011612D;
//                        this.motionY *= -0.10000000149011612D;
//                        this.motionZ *= -0.10000000149011612D;
                        this.setMotion(this.getMotion().mul(-0.10000000149011612D, -0.10000000149011612D, -0.10000000149011612D));
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                }
            }
            else
            {
                this.setMotion((float) (castResult.getHitVec().x - this.getPosX()), (float) (castResult.getHitVec().y - this.getPosY()), (float) (castResult.getHitVec().z - this.getPosZ()));
                float f5 = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().y * this.getMotion().y + this.getMotion().z * this.getMotion().z);
                this.setRawPosition(this.getPosX() - this.getMotion().x / (double) f5 * 0.05000000074505806D, this.getPosY() - this.getMotion().y / (double) f5 * 0.05000000074505806D, this.getPosZ() - this.getMotion().z / (double) f5 * 0.05000000074505806D);
                this.arrowShake = 7;
                this.remove();
            }
        }

        this.setRawPosition(this.getPosX() + this.getMotion().x, this.getPosY() + this.getMotion().y, this.getPosZ() + this.getMotion().z);
        float f3 = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);
        this.rotationYaw = (float) MathHelper.atan2(this.getMotion().x, this.getMotion().z) * Constants.RADIANS_TO_DEGREES;

        for (this.rotationPitch = (float) MathHelper.atan2(this.getMotion().y, f3) * Constants.RADIANS_TO_DEGREES; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        if (this.isInWater())
        {
            for (int i1 = 0; i1 < 4; ++i1)
            {
                float f8 = 0.25F;
                this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() - this.getMotion().x * (double) f8, this.getPosY() - this.getMotion().y * (double) f8, this.getPosZ() - this.getMotion().z * (double) f8, this.getMotion().x, this.getMotion().y, this.getMotion().z);
            }
        }

        if (this.isWet())
        {
            this.extinguish();
        }

        this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        this.doBlockCollisions();
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        this.arrowShake = nbt.getByte("shake") & 255;

        if (nbt.contains("pickup", 99))
        {
            this.canBePickedUp = nbt.getByte("pickup");
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt)
    {
        nbt.putByte("shake", (byte) this.arrowShake);
        nbt.putByte("pickup", (byte) this.canBePickedUp);
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public float getEyeHeight(Pose pose)
    {
        return 0.0F;
    }
}