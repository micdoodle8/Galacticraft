package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.entities.ai.EntityMoveHelperCeiling;
import micdoodle8.mods.galacticraft.planets.venus.entities.ai.PathNavigateCeiling;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class EntityJuicer extends MonsterEntity implements IEntityBreathable
{
    private static final DataParameter<Boolean> IS_FALLING = EntityDataManager.createKey(EntityJuicer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_HANGING = EntityDataManager.createKey(EntityJuicer.class, DataSerializers.BOOLEAN);
    private BlockPos jumpTarget;
    private int timeSinceLastJump = 0;

    public EntityJuicer(EntityType<? extends EntityJuicer> type, World worldIn)
    {
        super(type, worldIn);
        this.moveController = new EntityMoveHelperCeiling(this);
//        this.setSize(0.95F, 0.6F);
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.timeSinceLastJump = this.rand.nextInt(200) + 50;
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(IS_FALLING, false);
        this.dataManager.register(IS_HANGING, false);
    }

    public boolean isHanging()
    {
        return this.dataManager.get(IS_FALLING);
    }

    public void setHanging(boolean hanging)
    {
        this.dataManager.set(IS_FALLING, hanging);
    }

    public boolean isFalling()
    {
        return this.dataManager.get(IS_FALLING);
    }

    public void setFalling(boolean falling)
    {
        this.dataManager.set(IS_FALLING, falling);
    }

    @Override
    public boolean canBreatheUnderwater()
    {
        return true;
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected float getSoundPitch()
    {
        return super.getSoundPitch() + 0.4F;
    }

//    @Override
//    protected Item getDropItem()
//    {
//        return Item.getItemFromBlock(Blocks.AIR);
//    }

    @Override
    public void livingTick()
    {
        if (this.isHanging())
        {
            this.onGround = true;
        }

        super.livingTick();

        if (!this.world.isRemote)
        {
            if (this.jumpTarget == null)
            {
                if (this.timeSinceLastJump <= 0)
                {
                    BlockPos posAbove = new BlockPos(this.getPosX(), this.getPosY() + (this.isHanging() ? 1.0 : -0.5), this.getPosZ());
                    BlockState blockAbove = this.world.getBlockState(posAbove);

                    if (blockAbove.getBlock() == VenusBlocks.dungeonBrick2 || blockAbove.getBlock() == VenusBlocks.dungeonBrick1)
                    {
                        BlockRayTraceResult hit = this.world.rayTraceBlocks(new RayTraceContext(new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ()), new Vec3d(this.getPosX(), this.getPosY() + (this.isHanging() ? -10 : 10), this.getPosZ()), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));

                        if (hit.getType() == RayTraceResult.Type.BLOCK)
                        {
                            BlockRayTraceResult blockResult = hit;
                            BlockState blockBelow = this.world.getBlockState(blockResult.getPos());
                            if (blockBelow.getBlock() == VenusBlocks.dungeonBrick1 || blockBelow.getBlock() == VenusBlocks.dungeonBrick2)
                            {
                                if (this.isHanging())
                                {
                                    this.jumpTarget = blockResult.getPos();
                                    this.setFalling(true);
                                }
                                else
                                {
                                    this.jumpTarget = blockResult.getPos().offset(Direction.DOWN);
                                    this.setFalling(true);
                                }
                            }
                        }
                    }
                }
                else
                {
                    this.timeSinceLastJump--;
                }
            }
        }

        if (this.isHanging())
        {
            this.setMotion(this.getMotion().x, 0.0, this.getMotion().z);
        }
    }

    @Override
    public void move(MoverType typeIn, Vec3d pos)
    {
        super.move(typeIn, pos);
        if (this.isHanging())
        {
            this.onGround = true;
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.world.isRemote)
        {
            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.getPosX() - this.prevPosX;
            double d0 = this.getPosZ() - this.prevPosZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
            if (this.jumpTarget != null)
            {
                double diffX = this.jumpTarget.getX() - this.getPosX() + 0.5;
                double diffY = this.jumpTarget.getY() - this.getPosY() + 0.6;
                double diffZ = this.jumpTarget.getZ() - this.getPosZ() + 0.5;
                double motY = diffY > 0 ? Math.min(diffY / 2.0F, 0.2123F) : Math.max(diffY / 2.0F, -0.2123F);
                double motX = diffX > 0 ? Math.min(diffX / 2.0F, 0.2123F) : Math.max(diffX / 2.0F, -0.2123F);
                double motZ = diffZ > 0 ? Math.min(diffZ / 2.0F, 0.2123F) : Math.max(diffZ / 2.0F, -0.2123F);
                this.setMotion(motX, motY, motZ);
                if (diffY > 0.0F && Math.abs(this.jumpTarget.getY() - (this.getPosY() + this.getMotion().y)) < 0.4F)
                {
                    this.setPosition(this.jumpTarget.getX() + 0.5, this.jumpTarget.getY() + 0.2, this.jumpTarget.getZ() + 0.5);
                    this.jumpTarget = null;
                    this.setFalling(false);
                    this.timeSinceLastJump = this.rand.nextInt(180) + 60;
                    this.setHanging(true);
                }
                else if (diffY < 0.0F && Math.abs(this.jumpTarget.getY() - (this.getPosY() + this.getMotion().y)) < 0.8F)
                {
                    this.setPosition(this.jumpTarget.getX() + 0.5, this.jumpTarget.getY() + 1.0, this.jumpTarget.getZ() + 0.5);
                    this.jumpTarget = null;
                    this.setFalling(false);
                    this.timeSinceLastJump = this.rand.nextInt(180) + 60;
                    this.setHanging(false);
                }
                else
                {
                    this.setHanging(false);
                }
            }
        }
    }

//    @Override
//    protected boolean isValidLightLevel()
//    {
//        return true;
//    }
//
//    @Override
//    public boolean getCanSpawnHere()
//    {
//        if (super.getCanSpawnHere())
//        {
//            PlayerEntity var1 = this.world.getClosestPlayerToEntity(this, 5.0D);
//            return var1 == null;
//        }
//        else
//        {
//            return false;
//        }
//    }

//    @Override
//    public EnumCreatureAttribute getCreatureAttribute()
//    {
//        return EnumCreatureAttribute.ARTHROPOD;
//    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    protected PathNavigator createNavigator(World worldIn)
    {
        return new PathNavigateCeiling(this, worldIn);
    }

//    @Override
//    public void setInWeb()
//    {
//    }


    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);

        nbt.putInt("timeSinceLastJump", this.timeSinceLastJump);
        nbt.putBoolean("jumpTargetNull", this.jumpTarget == null);
        if (this.jumpTarget != null)
        {
            nbt.putInt("jumpTargetX", this.jumpTarget.getX());
            nbt.putInt("jumpTargetY", this.jumpTarget.getY());
            nbt.putInt("jumpTargetZ", this.jumpTarget.getZ());
        }
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        this.timeSinceLastJump = nbt.getInt("timeSinceLastJump");
        if (nbt.getBoolean("jumpTargetNull"))
        {
            this.jumpTarget = null;
        }
        else
        {
            this.jumpTarget = new BlockPos(nbt.getInt("jumpTargetX"), nbt.getInt("jumpTargetY"), nbt.getInt("jumpTargetZ"));
        }

        this.setFalling(this.jumpTarget != null);
    }
}
