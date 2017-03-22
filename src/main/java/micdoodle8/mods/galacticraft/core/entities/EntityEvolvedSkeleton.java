package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityEvolvedSkeleton extends EntitySkeleton implements IEntityBreathable, ITumblable
{
    private static final DataParameter<Float> SPIN_PITCH = EntityDataManager.createKey(EntityEvolvedSkeleton.class, DataSerializers.FLOAT);
    private float tumbling = 0F;
    private float tumbleAngle = 0F;

    public EntityEvolvedSkeleton(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35F);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.worldObj, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entitytippedarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        entitytippedarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
        DifficultyInstance difficultyinstance = this.worldObj.getDifficultyForLocation(new BlockPos(this));
        entitytippedarrow.setDamage((double)(distanceFactor * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.getDifficulty().getDifficultyId() * 0.11F));

        if (i > 0)
        {
            entitytippedarrow.setDamage(entitytippedarrow.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            entitytippedarrow.setKnockbackStrength(j);
        }

        boolean flag = this.isBurning() && difficultyinstance.isHard() && this.rand.nextBoolean() || this.getSkeletonType() == SkeletonType.WITHER;
        flag = flag || EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, this) > 0;

        if (flag)
        {
            entitytippedarrow.setFire(100);
        }

        ItemStack itemstack = this.getHeldItem(EnumHand.OFF_HAND);

        if (itemstack != null && itemstack.getItem() == Items.TIPPED_ARROW)
        {
            entitytippedarrow.setPotionEffect(itemstack);
        }
        else if (this.getSkeletonType() == SkeletonType.STRAY)
        {
            entitytippedarrow.addEffect(new PotionEffect(MobEffects.SLOWNESS, 600));
        }

        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entitytippedarrow);
    }

    @Override
    protected void jump()
    {
        this.motionY = 0.45D / WorldUtil.getGravityFactor(this);
        if (this.motionY < 0.24D)
        {
            this.motionY = 0.24D;
        }

        if (this.isPotionActive(MobEffects.JUMP_BOOST))
        {
            this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }

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
            if (ConfigManagerCore.challengeMode || ConfigManagerCore.challengeMobDropsAndSpawning) this.dropItem(Items.PUMPKIN_SEEDS, 1);
            break;
        }
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        Item item = this.getDropItem();

        int j = this.rand.nextInt(3);

        if (item != null)
        {
            if (lootingModifier > 0)
            {
                j += this.rand.nextInt(lootingModifier + 1);
            }

            for (int k = 1; k < j; ++k)
            {
                this.dropItem(item, 1);
            }
        }

        j = this.rand.nextInt(3 + lootingModifier);
        if (j > 1)
            this.dropItem(Items.BONE, 1);

        //Drop lapis as semi-rare drop if player hit and if dropping bones
        if (wasRecentlyHit && (ConfigManagerCore.challengeMode || ConfigManagerCore.challengeMobDropsAndSpawning) && j > 1 && this.rand.nextInt(12) == 0)
            this.entityDropItem(new ItemStack(Items.DYE, 1, 4), 0.0F);

        if (this.recentlyHit > 0 && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.01F)
        {
            this.addRandomDrop();
        }
    }

    @Override
    public void setTumbling(float value)
    {
        this.tumbling = value / 2F;
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
        this.getDataManager().register(SPIN_PITCH, 0.0F);
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
        return this.getDataManager().get(SPIN_PITCH);
    }

    public void setSpinPitch(float pitch)
    {
        this.getDataManager().set(SPIN_PITCH, pitch);
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
