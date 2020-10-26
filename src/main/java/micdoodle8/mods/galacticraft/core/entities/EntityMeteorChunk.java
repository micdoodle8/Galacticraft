package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class EntityMeteorChunk extends Entity implements IProjectile
{
    private static final DataParameter<Boolean> IS_HOT = EntityDataManager.createKey(EntityMeteorChunk.class, DataSerializers.BOOLEAN);
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    @Nullable
    private BlockState inBlockState;
    public int canBePickedUp;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private boolean inGround;
    private final float randYawInc;
    private final float randPitchInc;

    private int knockbackStrength;

    public boolean isHot;

    public EntityMeteorChunk(EntityType<EntityMeteorChunk> type, World world)
    {
        super(type, world);
        this.randPitchInc = rand.nextFloat() * 20 - 10;
        this.randYawInc = rand.nextFloat() * 20 - 10;
    }

    public EntityMeteorChunk(World world, LivingEntity par2EntityLivingBase, float speed)
    {
        super(GCEntities.METEOR_CHUNK, world);
        this.randPitchInc = rand.nextFloat() * 20 - 10;
        this.randYawInc = rand.nextFloat() * 20 - 10;
        this.shootingEntity = par2EntityLivingBase;

        if (par2EntityLivingBase instanceof PlayerEntity)
        {
            this.canBePickedUp = 1;
        }

        this.setLocationAndAngles(par2EntityLivingBase.getPosX(), par2EntityLivingBase.getPosY() + par2EntityLivingBase.getEyeHeight(), par2EntityLivingBase.getPosZ(), par2EntityLivingBase.rotationYaw, par2EntityLivingBase.rotationPitch);
        this.setRawPosition(this.getPosX() - MathHelper.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES) * 0.16F,
                this.getPosY() - 0.10000000149011612D,
                this.getPosZ() - MathHelper.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES) * 0.16F);
        this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        float motionX = -MathHelper.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES) * MathHelper.cos(this.rotationPitch / Constants.RADIANS_TO_DEGREES);
        float motionZ = MathHelper.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES) * MathHelper.cos(this.rotationPitch / Constants.RADIANS_TO_DEGREES);
        float motionY = -MathHelper.sin(this.rotationPitch / Constants.RADIANS_TO_DEGREES);
        setMotion(motionX, motionY, motionZ);
        this.shoot(motionX, motionY, motionZ, speed * 1.5F, 1.0F);
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    @Override
    public void shoot(double headingX, double headingY, double headingZ, float speed, float randMod)
    {
        float f2 = MathHelper.sqrt(headingX * headingX + headingY * headingY + headingZ * headingZ);
        headingX /= f2;
        headingY /= f2;
        headingZ /= f2;
        headingX += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
        headingY += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
        headingZ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
        headingX *= speed;
        headingY *= speed;
        headingZ *= speed;
        this.setMotion(headingX, headingY, headingZ);
        float f3 = MathHelper.sqrt(headingX * headingX + headingZ * headingZ);
        this.prevRotationYaw = this.rotationYaw = (float) Math.atan2(headingX, headingZ) * Constants.RADIANS_TO_DEGREES;
        this.prevRotationPitch = this.rotationPitch = (float) Math.atan2(headingY, f3) * Constants.RADIANS_TO_DEGREES;
        this.ticksInGround = 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.setMotion(x, y, z);

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float) Math.atan2(x, z) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch = (float) Math.atan2(y, f) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.ticksExisted > 400)
        {
            if (this.isHot)
            {
                this.isHot = false;
                this.setHot(this.isHot);
            }
        }
        else if (!this.world.isRemote)
        {
            this.setHot(this.isHot);
        }

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);
            this.prevRotationYaw = this.rotationYaw = (float) Math.atan2(this.getMotion().x, this.getMotion().z) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch = (float) Math.atan2(this.getMotion().y, f) * Constants.RADIANS_TO_DEGREES;
        }

        BlockPos pos = new BlockPos(this.xTile, this.yTile, this.zTile);
        BlockState stateIn = this.world.getBlockState(pos);

        if (!stateIn.getBlock().isAir(this.world.getBlockState(pos), this.world, pos))
        {
            VoxelShape voxelshape = stateIn.getCollisionShape(this.world, pos);
            if (!voxelshape.isEmpty())
            {
                for (AxisAlignedBB axisalignedbb : voxelshape.toBoundingBoxList())
                {
                    if (axisalignedbb.offset(pos).contains(new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ())))
                    {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        if (this.inGround)
        {
            BlockState currentInState = this.world.getBlockState(pos);

            if (this.inBlockState == currentInState)
            {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200)
                {
                    this.remove();
                }
            }
            else
            {
                this.inGround = false;
                this.setMotion(this.getMotion().mul(0.2, 0.2, 0.2));
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        else
        {
            ++this.ticksInAir;
            Vec3d vec3 = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
            Vec3d vec31 = new Vec3d(this.getPosX() + this.getMotion().x, this.getPosY() + this.getMotion().y, this.getPosZ() + this.getMotion().z);
            RayTraceResult castResult = this.world.rayTraceBlocks(new RayTraceContext(vec3, vec31, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            vec3 = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
            vec31 = new Vec3d(this.getPosX() + this.getMotion().x, this.getPosY() + this.getMotion().y, this.getPosZ() + this.getMotion().z);

            if (castResult.getType() != RayTraceResult.Type.MISS)
            {
                vec31 = new Vec3d(castResult.getHitVec().x, castResult.getHitVec().y, castResult.getHitVec().z);
            }

            this.rotationPitch += 1F;

            Entity entity = null;
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.getMotion()).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int l;
            float f1;
            final double border = 0.3D;

            for (l = 0; l < list.size(); ++l)
            {
                Entity entity1 = list.get(l);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
                {
                    AxisAlignedBB axisalignedbb1 = entity1.getBoundingBox().grow(border, border, border);
                    Optional<Vec3d> movingobjectposition1 = axisalignedbb1.rayTrace(vec3, vec31);

                    if (movingobjectposition1.isPresent())
                    {
                        double d1 = vec3.distanceTo(movingobjectposition1.get());

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

            if (castResult.getType() == RayTraceResult.Type.ENTITY && ((EntityRayTraceResult) castResult).getEntity() instanceof PlayerEntity)
            {
                PlayerEntity entityplayer = (PlayerEntity) ((EntityRayTraceResult) castResult).getEntity();

                if (entityplayer.abilities.disableDamage || this.shootingEntity instanceof PlayerEntity && !((PlayerEntity) this.shootingEntity).canAttackPlayer(entityplayer))
                {
                    castResult = null;
                }
            }

            float f2;
            float f3;
            double damage = ConfigManagerCore.hardMode.get() ? 3.2D : 1.6D;

            if (castResult != null)
            {
                if (castResult.getType() == RayTraceResult.Type.ENTITY)
                {
                    EntityRayTraceResult entityResult = (EntityRayTraceResult) castResult;
                    f2 = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().y * this.getMotion().y + this.getMotion().z * this.getMotion().z);
                    int i1 = MathHelper.ceil(f2 * damage);

                    DamageSource damagesource = null;

                    if (this.shootingEntity == null)
                    {
                        damagesource = new IndirectEntityDamageSource("meteor_chunk", this, this).setProjectile();
                    }
                    else
                    {
                        damagesource = new IndirectEntityDamageSource("meteor_chunk", this, this.shootingEntity).setProjectile();
                    }

                    if (this.isBurning() && !(entityResult.getEntity() instanceof EndermanEntity))
                    {
                        entityResult.getEntity().setFire(2);
                    }

                    if (entityResult.getEntity().attackEntityFrom(damagesource, i1))
                    {
                        if (entityResult.getEntity() instanceof LivingEntity)
                        {
                            LivingEntity entitylivingbase = (LivingEntity) entityResult.getEntity();

                            if (!this.world.isRemote)
                            {
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            }

                            if (this.knockbackStrength > 0)
                            {
                                f3 = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);

                                if (f3 > 0.0F)
                                {
                                    entitylivingbase.addVelocity(this.getMotion().x * this.knockbackStrength * 0.6000000238418579D / f3, 0.1D, this.getMotion().z * this.knockbackStrength * 0.6000000238418579D / f3);
                                }
                            }

                            if (this.shootingEntity != null)
                            {
                                EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                                EnchantmentHelper.applyArthropodEnchantments((LivingEntity) this.shootingEntity, entitylivingbase);
                            }

                            if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof PlayerEntity && this.shootingEntity instanceof ServerPlayerEntity)
                            {
                                ((ServerPlayerEntity) this.shootingEntity).connection.sendPacket(new SChangeGameStatePacket(6, 0.0F));
                            }
                        }

                        if (!(entityResult.getEntity() instanceof EndermanEntity))
                        {
                            this.remove();
                        }
                    }
                    else
                    {
                        this.setMotion(this.getMotion().mul(-0.10000000149011612, -0.10000000149011612, -0.10000000149011612));
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                }
                else if (castResult.getType() == RayTraceResult.Type.ENTITY)
                {
                    BlockRayTraceResult blockResult = (BlockRayTraceResult) castResult;
                    this.xTile = blockResult.getPos().getX();
                    this.yTile = blockResult.getPos().getY();
                    this.zTile = blockResult.getPos().getZ();
                    BlockState state = this.world.getBlockState(blockResult.getPos());
                    this.inBlockState = state;
                    float motionX = (float) (castResult.getHitVec().x - this.getPosX());
                    float motionY = (float) (castResult.getHitVec().y - this.getPosY());
                    float motionZ = (float) (castResult.getHitVec().z - this.getPosZ());
                    this.setMotion(motionX, motionY, motionZ);
                    f2 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    this.setRawPosition(this.getPosX() - this.getMotion().x / (double) f2 * 0.05000000074505806D, this.getPosY() - this.getMotion().y / (double) f2 * 0.05000000074505806D, this.getPosZ() - this.getMotion().z / (double) f2 * 0.05000000074505806D);
                    this.inGround = true;

                    if (!this.inBlockState.isAir(this.world, blockResult.getPos()))
                    {
                        this.inBlockState.onEntityCollision(this.world, blockResult.getPos(), this);
                    }
                }
            }

            this.move(MoverType.SELF, this.getMotion());
            f2 = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);

            if (!this.onGround)
            {
                this.rotationPitch += this.randPitchInc;
                this.rotationYaw += this.randYawInc;
            }

            float f4 = 0.99F;
            f1 = 0.05F;

            if (this.isInWater())
            {
                for (int j1 = 0; j1 < 4; ++j1)
                {
                    f3 = 0.25F;
                    this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() - this.getMotion().x * f3, this.getPosY() - this.getMotion().y * f3, this.getPosZ() - this.getMotion().z * f3, this.getMotion().x, this.getMotion().y, this.getMotion().z);
                }
                this.isHot = false;
                f4 = 0.8F;
            }

            this.setMotion(this.getMotion().mul(f4, f4, f4));
//            this.motionX *= f4;
//            this.motionY *= f4;
//            this.motionZ *= f4;
            this.setMotion(this.getMotion().add(0.0, -TransformerHooks.getGravityForEntity(this), 0.0));
            this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
            this.doBlockCollisions();
        }
    }

    @Override
    protected void registerData()
    {
        this.dataManager.register(IS_HOT, false);
    }

    public boolean isHot()
    {
        return this.dataManager.get(IS_HOT);
    }

    public void setHot(boolean isHot)
    {
        this.dataManager.set(IS_HOT, isHot);
    }

    @Override
    protected void readAdditional(CompoundNBT nbttagcompound)
    {

    }

    @Override
    protected void writeAdditional(CompoundNBT nbttagcompound)
    {

    }

    @Override
    public void onCollideWithPlayer(PlayerEntity par1EntityPlayer)
    {
        if (!this.world.isRemote && this.inGround)
        {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.abilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(GCItems.meteorChunk, 1)))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.onItemPickup(this, 1);
                this.remove();
            }
        }
    }

    @Override
    public boolean canBeAttackedWithItem()
    {
        return false;
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
}
