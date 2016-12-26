package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import micdoodle8.mods.galacticraft.planets.venus.entities.pathfinding.EntityMoveHelperCeiling;
import micdoodle8.mods.galacticraft.planets.venus.entities.pathfinding.PathNavigateCeiling;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityJuicer extends EntityMob implements IEntityBreathable
{
    private BlockPos jumpTarget;
    private int timeSinceLastJump = 0;
    private int posRotationIncrements;
    private double incX;
    private double incY;
    private double incZ;
    private double incYaw;
    private double incPitch;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;

    public EntityJuicer(World world)
    {
        super(world);
        this.moveHelper = new EntityMoveHelperCeiling(this);
        this.setSize(1F, 0.6F);
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D, 40));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.timeSinceLastJump = this.rand.nextInt(50) + 100;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
    }

    public boolean isHanging()
    {
        return this.dataWatcher.getWatchableObjectByte(16) != 0;
    }

    public void setHanging(boolean hanging)
    {
        this.dataWatcher.updateObject(16, (byte)(hanging ? 1 : 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        if (teleport && this.riddenByEntity != null)
        {
            this.prevPosX = this.posX = x;
            this.prevPosY = this.posY = y;
            this.prevPosZ = this.posZ = z;
            this.rotationYaw = yaw;
            this.rotationPitch = pitch;
            this.posRotationIncrements = 0;
            this.setPosition(x, y, z);
            this.motionX = this.velocityX = 0.0D;
            this.motionY = this.velocityY = 0.0D;
            this.motionZ = this.velocityZ = 0.0D;
        }
        else
        {
            double d0 = x - this.posX;
            double d1 = y - this.posY;
            double d2 = z - this.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 <= 1.0D)
            {
                return;
            }

            this.posRotationIncrements = 3;

            this.incX = x;
            this.incY = y;
            this.incZ = z;
            this.incYaw = (double)yaw;
            this.incPitch = (double)pitch;
            this.motionX = this.velocityX;
            this.motionY = this.velocityY;
            this.motionZ = this.velocityZ;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.velocityX = this.motionX = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.motionZ = z;
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
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected String getLivingSound()
    {
        return "mob.spider.say";
    }

    @Override
    protected String getHurtSound()
    {
        return "mob.spider.say";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.spider.death";
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound("mob.spider.step", 0.15F, 1.2F);
    }

    @Override
    protected float getSoundPitch()
    {
        return super.getSoundPitch() + 0.4F;
    }

    @Override
    protected Item getDropItem()
    {
        return Item.getItemFromBlock(Blocks.air);
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.isHanging())
        {
            this.onGround = true;
        }

        super.onLivingUpdate();

        if (!this.worldObj.isRemote)
        {
            if (this.jumpTarget == null && this.moveForward < 0.005F)
            {
                if (this.timeSinceLastJump <= 0)
                {
                    IBlockState blockAbove = this.worldObj.getBlockState(new BlockPos(this.posX, this.posY + (this.isHanging() ? 1.0 : -0.5), this.posZ));

                    if (blockAbove.getBlock() == VenusBlocks.venusBlock && blockAbove.getValue(BlockBasicVenus.BASIC_TYPE_VENUS) == BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_2)
                    {
                        MovingObjectPosition hit = this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY, this.posZ), new Vec3(this.posX, this.posY + (this.isHanging() ? -10 : 10), this.posZ), false, true, false);

                        if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                        {
                            IBlockState blockBelow = this.worldObj.getBlockState(hit.getBlockPos());
                            if (blockBelow.getBlock() == VenusBlocks.venusBlock && blockBelow.getValue(BlockBasicVenus.BASIC_TYPE_VENUS) == BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_2)
                            {
                                if (this.isHanging())
                                {
                                    this.jumpTarget = hit.getBlockPos();
                                }
                                else
                                {
                                    this.jumpTarget = hit.getBlockPos().offset(EnumFacing.DOWN);
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
    public void moveEntity(double x, double y, double z)
    {
        super.moveEntity(x, y, z);
        if (this.isHanging())
        {
            this.onGround = true;
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.worldObj.isRemote)
        {
            if (this.posRotationIncrements > 0)
            {
                double d12 = this.posX + (this.incX - this.posX) / (double)this.posRotationIncrements;
                double d16 = this.posY + (this.incY - this.posY) / (double)this.posRotationIncrements;
                double d19 = this.posZ + (this.incZ - this.posZ) / (double)this.posRotationIncrements;
                double d22 = MathHelper.wrapAngleTo180_double(this.incYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + d22 / (double)this.posRotationIncrements);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.incPitch - (double)this.rotationPitch) / (double)this.posRotationIncrements);
                --this.posRotationIncrements;
                this.setPosition(d12, d16, d19);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                double d13 = this.posX + this.motionX;
                double d17 = this.posY + this.motionY;
                double d20 = this.posZ + this.motionZ;
                this.setPosition(d13, d17, d20);

                if (this.onGround)
                {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

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
                double diff = this.jumpTarget.getY() - this.posY + 0.6;
                this.motionY = diff > 0 ? Math.min(diff / 2.0F, 0.2123F) : Math.max(diff / 2.0F, -0.2123F);
                if (diff > 0.0F && Math.abs(this.jumpTarget.getY() - (this.posY + this.motionY)) < 0.4F)
                {
                    this.setPosition(this.posX, this.jumpTarget.getY() + 0.2, this.posZ);
                    this.jumpTarget = null;
                    this.timeSinceLastJump = this.rand.nextInt(80) + 40;
                    this.setHanging(true);
                }
                else if (diff < 0.0F && Math.abs(this.jumpTarget.getY() - (this.posY + this.motionY)) < 0.8F)
                {
                    this.setPosition(this.posX, this.jumpTarget.getY() + 1.0, this.posZ);
                    this.jumpTarget = null;
                    this.timeSinceLastJump = this.rand.nextInt(80) + 40;
                    this.setHanging(false);
                }
                else
                {
                    this.setHanging(false);
                }
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
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
            EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
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
    protected PathNavigate getNewNavigator(World worldIn)
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
    }
}
