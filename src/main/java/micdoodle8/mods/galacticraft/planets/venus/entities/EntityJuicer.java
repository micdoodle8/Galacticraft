package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import micdoodle8.mods.galacticraft.planets.venus.entities.ai.EntityMoveHelperCeiling;
import micdoodle8.mods.galacticraft.planets.venus.entities.ai.PathNavigateCeiling;
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

public class EntityJuicer extends EntityMob implements IEntityBreathable
{
    private BlockPos jumpTarget;
    private int timeSinceLastJump = 0;

    public EntityJuicer(World world)
    {
        super(world);
        this.moveHelper = new EntityMoveHelperCeiling(this);
        this.setSize(0.95F, 0.6F);
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0, true));
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
        this.dataWatcher.addObject(16, (byte)0);
        this.dataWatcher.addObject(17, (byte)0);
    }

    public boolean isHanging()
    {
        return this.dataWatcher.getWatchableObjectByte(16) != 0;
    }

    public void setHanging(boolean hanging)
    {
        this.dataWatcher.updateObject(16, (byte)(hanging ? 1 : 0));
    }

    public boolean isFalling()
    {
        return this.dataWatcher.getWatchableObjectByte(17) != 0;
    }

    public void setFalling(boolean falling)
    {
        this.dataWatcher.updateObject(17, (byte)(falling ? 1 : 0));
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
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
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
            if (this.jumpTarget == null)
            {
                if (this.timeSinceLastJump <= 0)
                {
                    BlockPos posAbove = new BlockPos(this.posX, this.posY + (this.isHanging() ? 1.0 : -0.5), this.posZ);
                    IBlockState blockAbove = this.worldObj.getBlockState(posAbove);

                    if (blockAbove.getBlock() == VenusBlocks.venusBlock && (blockAbove.getValue(BlockBasicVenus.BASIC_TYPE_VENUS) == BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_2 ||
                            blockAbove.getValue(BlockBasicVenus.BASIC_TYPE_VENUS) == BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_1))
                    {
                        MovingObjectPosition hit = this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY, this.posZ), new Vec3(this.posX, this.posY + (this.isHanging() ? -10 : 10), this.posZ), false, true, false);

                        if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                        {
                            IBlockState blockBelow = this.worldObj.getBlockState(hit.getBlockPos());
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

        this.setFalling(this.jumpTarget != null);
    }
}
