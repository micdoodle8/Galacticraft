package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntityGrapple extends Entity implements IProjectile
{
    private static final DataParameter<Integer> PULLING_ENTITY_ID = EntityDataManager.createKey(EntityGrapple.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IS_PULLING = EntityDataManager.createKey(EntityGrapple.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> STRING_ITEM_STACK = EntityDataManager.createKey(EntityGrapple.class, DataSerializers.ITEMSTACK);
    private BlockPos hitVec;
    @Nullable
    private BlockState inBlockState;
    private boolean inGround;
    public int canBePickedUp;
    public int arrowShake;
    public PlayerEntity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    public float rotationRoll;
    public float prevRotationRoll;
    public boolean pullingPlayer;

    public EntityGrapple(EntityType<? extends EntityGrapple> type, World worldIn)
    {
        super(type, worldIn);
        this.ignoreFrustumCheck = false;
//        this.yOffset = -1.5F;
//        this.setSize(0.75F, 0.75F);
    }

    public static EntityGrapple createEntityGrapple(World world, PlayerEntity shootingEntity, float par3, ItemStack stringStack)
    {
        EntityGrapple grapple = new EntityGrapple(AsteroidEntities.GRAPPLE.get(), world);
        grapple.shootingEntity = shootingEntity;
//        grapple.setSize(0.75F, 0.75F);

        if (shootingEntity != null)
        {
            grapple.canBePickedUp = 1;
            grapple.setLocationAndAngles(shootingEntity.posX, shootingEntity.posY + shootingEntity.getEyeHeight(), shootingEntity.posZ, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
        }

        float motX = -MathHelper.sin(grapple.rotationYaw / Constants.RADIANS_TO_DEGREES) * MathHelper.cos(grapple.rotationPitch / Constants.RADIANS_TO_DEGREES);
        float motY = -MathHelper.sin(grapple.rotationPitch / Constants.RADIANS_TO_DEGREES);
        float motZ = MathHelper.cos(grapple.rotationYaw / Constants.RADIANS_TO_DEGREES) * MathHelper.cos(grapple.rotationPitch / Constants.RADIANS_TO_DEGREES);
        grapple.setMotion(motX, motY, motZ);
        grapple.posX += grapple.getMotion().x;
        grapple.posY += grapple.getMotion().y;
        grapple.posZ += grapple.getMotion().z;
//        grapple.yOffset = -1.5F;
        grapple.setPosition(grapple.posX, grapple.posY, grapple.posZ);
        grapple.shoot(grapple.getMotion().x, grapple.getMotion().y, grapple.getMotion().z, par3 * 1.5F, 1.0F);
        grapple.updateStringStack(stringStack);
        return grapple;
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
    public double getYOffset()
    {
        return -1.5F;
    }

    @Override
    protected void registerData()
    {
        this.dataManager.register(PULLING_ENTITY_ID, 0);
        this.dataManager.register(IS_PULLING, false);
        this.dataManager.register(STRING_ITEM_STACK, new ItemStack(Items.STRING));
    }

    @Override
    public void shoot(double mX, double mY, double mZ, float mult, float rand)
    {
        float f2 = MathHelper.sqrt(mX * mX + mY * mY + mZ * mZ);
        mX /= f2;
        mY /= f2;
        mZ /= f2;
        mX += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * rand;
        mY += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * rand;
        mZ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * rand;
        mX *= mult;
        mY *= mult;
        mZ *= mult;
        this.setMotion(mX, mY, mZ);
        float f3 = MathHelper.sqrt(mX * mX + mZ * mZ);
        this.prevRotationYaw = this.rotationYaw = (float) Math.atan2(mX, mZ) * Constants.RADIANS_TO_DEGREES;
        this.prevRotationPitch = this.rotationPitch = (float) Math.atan2(mY, f3) * Constants.RADIANS_TO_DEGREES;
        this.ticksInGround = 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        super.setPosition(x, y, z);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
//        this.motionX = x;
//        this.getMotion().y = y;
//        this.getMotion().z = z;
        this.setMotion(x, y, z);

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float) Math.atan2(x, z) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch = (float) Math.atan2(y, f) * Constants.RADIANS_TO_DEGREES;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        this.prevRotationRoll = this.rotationRoll;

        if (!this.world.isRemote)
        {
            this.updateShootingEntity();

            if (this.getPullingEntity())
            {
                PlayerEntity shootingEntity = this.getShootingEntity();
                if (shootingEntity != null)
                {
                    double deltaPosition = this.getDistanceSq(shootingEntity);

                    Vector3D mot = new Vector3D(shootingEntity.getMotion().x, shootingEntity.getMotion().y, shootingEntity.getMotion().z);

                    if (mot.getMagnitudeSquared() < 0.01 && this.pullingPlayer)
                    {
                        if (deltaPosition < 10)
                        {
                            this.onCollideWithPlayer(shootingEntity);
                        }
                        this.updatePullingEntity(false);
                        this.remove();
                    }

                    this.pullingPlayer = true;
                }
            }
        }
        else
        {
            if (this.getPullingEntity())
            {
                PlayerEntity shootingEntity = this.getShootingEntity();
                if (shootingEntity != null)
                {
                    shootingEntity.setVelocity((this.posX - shootingEntity.posX) / 12.0F, (this.posY - shootingEntity.posY) / 12.0F, (this.posZ - shootingEntity.posZ) / 12.0F);
                    if (shootingEntity.world.isRemote && shootingEntity.world.getDimension() instanceof IZeroGDimension)
                    {
                        GCPlayerStatsClient stats = GCPlayerStatsClient.get(shootingEntity);
                        if (stats != null)
                        {
//                            stats.getFreefallHandler().updateFreefall(shootingEntity); TODO Freefall
                        }
                    }
                }
            }
        }

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);
            this.prevRotationYaw = this.rotationYaw = (float) Math.atan2(this.getMotion().x, this.getMotion().z) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch = (float) Math.atan2(this.getMotion().y, f) * Constants.RADIANS_TO_DEGREES;
        }

        if (this.hitVec != null)
        {
            BlockState state = this.world.getBlockState(this.hitVec);

            if (state.getMaterial() != Material.AIR)
            {
                VoxelShape neighbour = state.getShape(this.world, this.hitVec);

                if (neighbour != VoxelShapes.empty() && neighbour.getBoundingBox().contains(new Vec3d(this.posX, this.posY, this.posZ)))
                {
                    this.inGround = true;
                }
            }
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.inGround)
        {
            if (this.hitVec != null)
            {
                BlockState state = this.world.getBlockState(this.hitVec);

                if (state == inBlockState)
                {
                    if (this.shootingEntity != null)
                    {
                        this.shootingEntity.setMotion((this.posX - this.shootingEntity.posX) / 16.0F, (this.posY - this.shootingEntity.posY) / 16.0F, (this.posZ - this.shootingEntity.posZ) / 16.0F);
                        if (this.shootingEntity instanceof ServerPlayerEntity)
                        	GalacticraftCore.handler.preventFlyingKicks((ServerPlayerEntity) this.shootingEntity);
                    }

                    if (!this.world.isRemote && this.ticksInGround < 5)
                    {
                        this.updatePullingEntity(true);
                    }

                    ++this.ticksInGround;

                    if (this.ticksInGround == 1200)
                    {
                        this.remove();
                    }
                }
                else
                {
                    this.inGround = false;
                    this.setMotion(this.getMotion().mul(this.rand.nextFloat() * 0.2F, this.rand.nextFloat() * 0.2F, this.rand.nextFloat() * 0.2F));
                    this.ticksInGround = 0;
                    this.ticksInAir = 0;
                }
            }
        }
        else
        {
            this.rotationRoll += 5;
            ++this.ticksInAir;

            if (!this.world.isRemote)
            {
                this.updatePullingEntity(false);
            }

            if (this.shootingEntity != null && this.getDistanceSq(this.shootingEntity) >= 40 * 40)
            {
                this.remove();
            }

            Vec3d vec31 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3 = new Vec3d(this.posX + this.getMotion().x, this.posY + this.getMotion().y, this.posZ + this.getMotion().z);
            RayTraceResult castResult = this.world.rayTraceBlocks(new RayTraceContext(vec3, vec31, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            vec31 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3 = new Vec3d(this.posX + this.getMotion().x, this.posY + this.getMotion().y, this.posZ + this.getMotion().z);

            if (castResult.getType() != RayTraceResult.Type.MISS)
            {
                vec3 = new Vec3d(castResult.getHitVec().x, castResult.getHitVec().y, castResult.getHitVec().z);
            }

            Entity entity = null;
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.getMotion().x, this.getMotion().y, this.getMotion().z).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int i;
            final double border = 0.3D;

            for (i = 0; i < list.size(); ++i)
            {
                Entity entity1 = list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
                {
                    AxisAlignedBB axisalignedbb1 = entity1.getBoundingBox().grow(border, border, border);
                    Optional<Vec3d> rayResult = axisalignedbb1.rayTrace(vec3, vec31);

                    if (rayResult.isPresent())
                    {
                        double d1 = vec3.distanceTo(rayResult.get());

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

            float motion;

            if (castResult != null)
            {
                if (castResult.getType() == RayTraceResult.Type.ENTITY)
                {
                    BlockRayTraceResult blockResult = (BlockRayTraceResult) castResult;
                    this.hitVec = blockResult.getPos();
                    BlockState state = this.world.getBlockState(blockResult.getPos());
                    this.inBlockState = state;
                    float motionX = (float) (castResult.getHitVec().x - this.posX);
                    float motionY = (float) (castResult.getHitVec().y - this.posY);
                    float motionZ = (float) (castResult.getHitVec().z - this.posZ);
                    this.setMotion(motionX, motionY, motionZ);
                    motion = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                    this.posX -= motionX / motion * 0.05000000074505806D;
                    this.posY -= motionY / motion * 0.05000000074505806D;
                    this.posZ -= motionZ / motion * 0.05000000074505806D;
                    this.inGround = true;

                    if (!this.inBlockState.isAir(this.world, blockResult.getPos()))
                    {
                        this.inBlockState.onEntityCollision(this.world, blockResult.getPos(), this);
                    }
                }
            }

            this.posX += this.getMotion().x;
            this.posY += this.getMotion().y;
            this.posZ += this.getMotion().z;
            motion = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);
            this.rotationYaw = (float) Math.atan2(this.getMotion().x, this.getMotion().z) * Constants.RADIANS_TO_DEGREES;
            this.rotationPitch = (float) Math.atan2(this.getMotion().y, motion) * Constants.RADIANS_TO_DEGREES;

            while (this.rotationPitch - this.prevRotationPitch < -180.0F)
            {
                this.prevRotationPitch -= 360.0F;
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
                float f4 = 0.25F;
                for (int l = 0; l < 4; ++l)
                {
                    this.world.addParticle(ParticleTypes.BUBBLE, this.posX - this.getMotion().x * f4, this.posY - this.getMotion().y * f4, this.posZ - this.getMotion().z * f4, this.getMotion().x, this.getMotion().y, this.getMotion().z);
                }

            }

            if (this.isWet())
            {
                this.extinguish();
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }

        if (!this.world.isRemote && (this.ticksInGround - 1) % 10 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimpleAsteroids(PacketSimpleAsteroids.EnumSimplePacketAsteroids.C_UPDATE_GRAPPLE_POS, GCCoreUtil.getDimensionID(this.world), new Object[] { this.getEntityId(), new Vector3(this) }), new PacketDistributor.TargetPoint(this.posX, this.posY, this.posZ, 150, GCCoreUtil.getDimensionID(this.world)));
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound)
    {
        if (this.hitVec != null)
        {
            compound.putShort("xTile", (short) this.hitVec.getX());
            compound.putShort("yTile", (short) this.hitVec.getY());
            compound.putShort("zTile", (short) this.hitVec.getZ());
        }
        compound.putShort("life", (short) this.ticksInGround);
        if (this.inBlockState != null) {
            compound.put("inBlockState", NBTUtil.writeBlockState(this.inBlockState));
        }
        compound.putByte("shake", (byte) this.arrowShake);
        compound.putByte("inGround", (byte) (this.inGround ? 1 : 0));
        compound.putByte("pickup", (byte) this.canBePickedUp);
        compound.putString("stringStack", Objects.requireNonNull(getStringItemStack().getItem().getRegistryName()).toString());
    }

    @Override
    protected void readAdditional(CompoundNBT compound)
    {
        if (compound.contains("xTile"))
        {
            this.hitVec = new BlockPos(compound.getShort("xTile"), compound.getShort("yTile"), compound.getShort("zTile"));
        }

        this.ticksInGround = compound.getShort("life");
        if (compound.contains("inBlockState", 10)) {
            this.inBlockState = NBTUtil.readBlockState(compound.getCompound("inBlockState"));
        }
        this.arrowShake = compound.getByte("shake") & 255;
        this.inGround = compound.getByte("inGround") == 1;

        if (compound.contains("pickup", 99))
        {
            this.canBePickedUp = compound.getByte("pickup");
        }
        else if (compound.contains("player", 99))
        {
            this.canBePickedUp = compound.getBoolean("player") ? 1 : 0;
        }

        if (compound.contains("stringStack")) {
            this.updateStringStack(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(compound.getString("stringStack"))))));
        } else {
            this.updateStringStack(new ItemStack(Items.STRING));
        }
    }

    @Override
    public void onCollideWithPlayer(PlayerEntity par1EntityPlayer)
    {
        if (!this.world.isRemote && this.inGround && this.arrowShake <= 0)
        {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.abilities.isCreativeMode;

            if (this.canBePickedUp == 1 && getStringItemStack() != ItemStack.EMPTY && !par1EntityPlayer.inventory.addItemStackToInventory(getStringItemStack()))
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
    protected boolean canTriggerWalking()
    {
        return false;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public float getShadowSize()
//    {
//        return 0.0F;
//    }


    @Override
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    private void updateShootingEntity()
    {
        if (this.shootingEntity != null)
        {
            this.dataManager.set(PULLING_ENTITY_ID, this.shootingEntity.getEntityId());
        }
    }

    public PlayerEntity getShootingEntity()
    {
        Entity entity = this.world.getEntityByID(this.dataManager.get(PULLING_ENTITY_ID));

        if (entity instanceof PlayerEntity)
        {
            return (PlayerEntity) entity;
        }

        return null;
    }

    public void updatePullingEntity(boolean pulling)
    {
        this.dataManager.set(IS_PULLING, pulling);
    }

    public void updateStringStack(ItemStack stringStack)
    {
        this.dataManager.set(STRING_ITEM_STACK, stringStack);
    }

    public boolean getPullingEntity()
    {
        return this.dataManager.get(IS_PULLING);
    }

    public ItemStack getStringItemStack() {
        return this.dataManager.get(STRING_ITEM_STACK);
    }
}