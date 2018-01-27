package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityEvolvedSkeleton extends EntitySkeleton implements IEntityBreathable, ITumblable
{
    private float tumbling = 0F;
    private float tumbleAngle = 0F;

    public EntityEvolvedSkeleton(World worldIn)
    {
        super(worldIn);
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, true));

        if (worldIn != null && !worldIn.isRemote)
        {
            this.setCombatTask();
        }
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35F);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2)
    {
        EntityArrow entityarrow = new EntityArrow(this.worldObj, this, par1EntityLivingBase, 0.4F, 17 - this.worldObj.getDifficulty().getDifficultyId() * 4);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
        entityarrow.setDamage(par2 * 2.0F + this.rand.nextGaussian() * 0.25D + this.worldObj.getDifficulty().getDifficultyId() * 0.11F);

        if (i > 0)
        {
            entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            entityarrow.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1)
        {
            entityarrow.setFire(100);
        }

        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entityarrow);
    }

    @Override
    protected void jump()
    {
        this.motionY = 0.45D / WorldUtil.getGravityFactor(this);
        if (this.motionY < 0.24D)
        {
            this.motionY = 0.24D;
        }

        if (this.isPotionActive(Potion.jump))
        {
            this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw / Constants.RADIANS_TO_DEGREES;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }

    @Override
    protected void addRandomDrop()
    {
        int r = this.rand.nextInt(12);
        switch (r)
        {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            this.entityDropItem(new ItemStack(GCBlocks.oxygenPipe), 0.0F);
            break;
        case 6:
            //Oxygen tank half empty or less
            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
            break;
        case 7:
        case 8:
            this.dropItem(GCItems.canister, 1);
            break;
        default:
            if (ConfigManagerCore.challengeMobDropsAndSpawning) this.dropItem(Items.pumpkin_seeds, 1);
            break;
        }
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        Item item = this.getDropItem();

        int j = this.rand.nextInt(3);

        if (item != null)
        {
            if (p_70628_2_ > 0)
            {
                j += this.rand.nextInt(p_70628_2_ + 1);
            }

            for (int k = 1; k < j; ++k)
            {
                this.dropItem(item, 1);
            }
        }

        j = this.rand.nextInt(3 + p_70628_2_);
        if (j > 1)
            this.dropItem(Items.bone, 1);

        //Drop lapis as semi-rare drop if player hit and if dropping bones
        if (p_70628_1_ && (ConfigManagerCore.challengeMobDropsAndSpawning) && j > 1 && this.rand.nextInt(12) == 0)
            this.entityDropItem(new ItemStack(Items.dye, 1, 4), 0.0F);
    }

    @Override
    public void setTumbling(float value)
    {
        if (value !=0F)
        {
            if (this.tumbling == 0F)
                this.tumbling = (this.worldObj.rand.nextFloat() + 0.5F) * value;
        }
        else
            this.tumbling = 0F;
    }
    
    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        if (!this.isDead)
        {
            if (this.tumbling != 0F)
            {
                if (this.onGround)
                {
                    this.tumbling = 0F;
                }
            }

            if (!this.worldObj.isRemote)
            {
                this.setSpinPitch(this.tumbling);
            }
            else
            {
                this.tumbling = this.getSpinPitch();
                this.tumbleAngle -= this.tumbling;
                if (this.tumbling == 0F && this.tumbleAngle != 0F)
                {
                    this.tumbleAngle *= 0.8F;
                    if (Math.abs(this.tumbleAngle) < 1F)
                        this.tumbleAngle = 0F;
                }
            }
        }
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, 0.0F);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.tumbling = nbt.getFloat("tumbling");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setFloat("tumbling", this.tumbling);
    }

    public float getSpinPitch()
    {
        return this.dataWatcher.getWatchableObjectFloat(16);
    }

    public void setSpinPitch(float pitch)
    {
        this.dataWatcher.updateObject(16, pitch);
    }

    @Override
    public float getTumbleAngle(float partial)
    {
        float angle = this.tumbleAngle - partial * this.tumbling;
        if (angle > 360F)
        {   
            this.tumbleAngle -= 360F;
            angle -= 360F;
        }
        if (angle < 0F)
        {
            this.tumbleAngle += 360F;
            angle += 360F;
        }
        return angle;
    }

    @Override
    public float getTumbleAxisX()
    {
        double velocity2 = this.motionX * this.motionX + this.motionZ * this.motionZ;
        if (velocity2 == 0D) return 1F;
        return (float) (this.motionZ / MathHelper.sqrt_double(velocity2));
    }

    @Override
    public float getTumbleAxisZ()
    {
        double velocity2 = this.motionX * this.motionX + this.motionZ * this.motionZ;
        if (velocity2 == 0D) return 0F;
        return (float) (this.motionX / MathHelper.sqrt_double(velocity2));
    }
}
