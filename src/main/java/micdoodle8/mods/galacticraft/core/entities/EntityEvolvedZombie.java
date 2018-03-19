package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityEvolvedZombie extends EntityZombie implements IEntityBreathable, ITumblable
{
    private int conversionTime = 0;
    private float tumbling = 0F;
    private float tumbleAngle = 0F;

    public EntityEvolvedZombie(World par1World)
    {
        super(par1World);
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
        this.setSize(0.6F, 1.95F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
        double difficulty = 0;
        switch (this.worldObj.getDifficulty())
        {
        case HARD : difficulty = 2D;
            break;
        case NORMAL : difficulty = 1D;
            break;
        }
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.26D + 0.04D * difficulty);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3D + difficulty);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16D + difficulty * 2D);
    }

    @Override
    protected void applyEntityAI()
    {
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityIronGolem.class, 1.0D, true));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityVillager.class, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, true));
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    public IAttribute getReinforcementsAttribute()
    {
        return EntityZombie.reinforcementChance;
    }

    @Override
    protected void jump()
    {
        this.motionY = 0.48D / WorldUtil.getGravityFactor(this);
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
        switch (this.rand.nextInt(16))
        {
        case 0:
        case 1:
        case 2:
            //Dehydrated carrot
            this.entityDropItem(new ItemStack(GCItems.foodItem, 1, 1), 0.0F);
            break;
        case 3:
        case 4:
            this.dropItem(GCItems.meteoricIronRaw, 1);
            break;
        case 5:
        case 6:
            //Dehydrated potato
            this.entityDropItem(new ItemStack(GCItems.foodItem, 1, 3), 0.0F);
            break;
        case 7:
        case 8:
            //Oxygen tank half empty or less
            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
            break;
        case 9:
            this.dropItem(GCItems.oxMask, 1);
            break;
        case 10:
            this.dropItem(GCItems.oxygenVent, 1);
            break;
        case 11:
        case 12:
            this.dropItem(Items.carrot, 1);
            break;
        case 13:
        case 14:
        case 15:
            if (ConfigManagerCore.challengeMobDropsAndSpawning) this.dropItem(Items.melon_seeds, 1);
            break;
        }
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        Item item = this.getDropItem();

        //Less rotten flesh than vanilla
        int j = this.rand.nextInt(2);

        if (item != null)
        {
            if (p_70628_2_ > 0)
            {
                j += this.rand.nextInt(p_70628_2_ + 1);
            }

            for (int k = 0; k < j; ++k)
            {
                this.dropItem(item, 1);
            }
        }

        //Drop copper ingot as semi-rare drop if player hit and if dropping rotten flesh (50% chance)
        if (p_70628_1_ && (ConfigManagerCore.challengeMobDropsAndSpawning) && j > 0 && this.rand.nextInt(6) == 0)
            this.entityDropItem(new ItemStack(GCItems.basicItem, 1, 3), 0.0F);
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
