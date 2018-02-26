package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import micdoodle8.mods.galacticraft.planets.venus.entities.ai.EntityMoveHelperCeiling;
import micdoodle8.mods.galacticraft.planets.venus.entities.ai.PathNavigateCeiling;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityJuicer extends EntityMob implements IEntityBreathable
{
    private static final DataParameter<Boolean> IS_FALLING = EntityDataManager.createKey(EntityJuicer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_HANGING = EntityDataManager.createKey(EntityJuicer.class, DataSerializers.BOOLEAN);
    private BlockPos jumpTarget;
    private int timeSinceLastJump = 0;

    public EntityJuicer(World world)
    {
        super(world);
        this.moveHelper = new EntityMoveHelperCeiling(this);
        this.setSize(0.95F, 0.6F);
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, true));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.timeSinceLastJump = this.rand.nextInt(200) + 50;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
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
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
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
    protected void playStepSound(BlockPos pos, Block blockIn)
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

    @Override
    protected Item getDropItem()
    {
        return Item.getItemFromBlock(Blocks.AIR);
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.isHanging())
        {
            this.onGround = true;
        }

        super.onLivingUpdate();

        if (!this.world.isRemote)
        {
            if (this.jumpTarget == null)
            {
                if (this.timeSinceLastJump <= 0)
                {
                    BlockPos posAbove = new BlockPos(this.posX, this.posY + (this.isHanging() ? 1.0 : -0.5), this.posZ);
                    IBlockState blockAbove = this.world.getBlockState(posAbove);

                    if (blockAbove.getBlock() == VenusBlocks.venusBlock && (blockAbove.getValue(BlockBasicVenus.BASIC_TYPE_VENUS) == BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_2 ||
                            blockAbove.getValue(BlockBasicVenus.BASIC_TYPE_VENUS) == BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_1))
                    {
                        RayTraceResult hit = this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY, this.posZ), new Vec3d(this.posX, this.posY + (this.isHanging() ? -10 : 10), this.posZ), false, true, false);

                        if (hit != null && hit.typeOfHit == RayTraceResult.Type.BLOCK)
                        {
                            IBlockState blockBelow = this.world.getBlockState(hit.getBlockPos());
                            if (blockBelow.getBlock() == VenusBlocks.venusBlock && (blockBelow.getValue(BlockBasicVenus.BASIC_TYPE_VENUS) == BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_2 ||
                                    blockBelow.getValue(BlockBasicVenus.BASIC_TYPE_VENUS) == BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_1))
                            {
                                if (this.isHanging())
                                {
                                    this.jumpTarget = hit.getBlockPos();
                                    this.setFalling(this.jumpTarget != null);
                                }
                                else
                                {
                                    this.jumpTarget = hit.getBlockPos().offset(EnumFacing.DOWN);
                                    this.setFalling(this.jumpTarget != null);
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
            this.motionY = 0.0F;
        }
    }

    @Override
    public void move(MoverType type, double x, double y, double z)
    {
        super.move(type, x, y, z);
        if (this.isHanging())
        {
            this.onGround = true;
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.world.isRemote)
        {
            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
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
                double diffX = this.jumpTarget.getX() - this.posX + 0.5;
                double diffY = this.jumpTarget.getY() - this.posY + 0.6;
                double diffZ = this.jumpTarget.getZ() - this.posZ + 0.5;
                this.motionY = diffY > 0 ? Math.min(diffY / 2.0F, 0.2123F) : Math.max(diffY / 2.0F, -0.2123F);
                this.motionX = diffX > 0 ? Math.min(diffX / 2.0F, 0.2123F) : Math.max(diffX / 2.0F, -0.2123F);
                this.motionZ = diffZ > 0 ? Math.min(diffZ / 2.0F, 0.2123F) : Math.max(diffZ / 2.0F, -0.2123F);
                if (diffY > 0.0F && Math.abs(this.jumpTarget.getY() - (this.posY + this.motionY)) < 0.4F)
                {
                    this.setPosition(this.jumpTarget.getX() + 0.5, this.jumpTarget.getY() + 0.2, this.jumpTarget.getZ() + 0.5);
                    this.jumpTarget = null;
                    this.setFalling(false);
                    this.timeSinceLastJump = this.rand.nextInt(180) + 60;
                    this.setHanging(true);
                }
                else if (diffY < 0.0F && Math.abs(this.jumpTarget.getY() - (this.posY + this.motionY)) < 0.8F)
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

    @Override
    protected boolean isValidLightLevel()
    {
        return true;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        if (super.getCanSpawnHere())
        {
            EntityPlayer var1 = this.world.getClosestPlayerToEntity(this, 5.0D);
            return var1 == null;
        }
        else
        {
            return false;
        }
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    protected PathNavigate createNavigator(World worldIn)
    {
        return new PathNavigateCeiling(this, worldIn);
    }

    @Override
    public void setInWeb()
    {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);

        tagCompound.setInteger("timeSinceLastJump", this.timeSinceLastJump);
        tagCompound.setBoolean("jumpTargetNull", this.jumpTarget == null);
        if (this.jumpTarget != null)
        {
            tagCompound.setInteger("jumpTargetX", this.jumpTarget.getX());
            tagCompound.setInteger("jumpTargetY", this.jumpTarget.getY());
            tagCompound.setInteger("jumpTargetZ", this.jumpTarget.getZ());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        this.timeSinceLastJump = tagCompund.getInteger("timeSinceLastJump");
        if (tagCompund.getBoolean("jumpTargetNull"))
        {
            this.jumpTarget = null;
        }
        else
        {
            this.jumpTarget = new BlockPos(tagCompund.getInteger("jumpTargetX"), tagCompund.getInteger("jumpTargetY"), tagCompund.getInteger("jumpTargetZ"));
        }

        this.setFalling(this.jumpTarget != null);
    }
}
