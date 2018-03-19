package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityEvolvedZombie extends EntityZombie implements IEntityBreathable, ITumblable
{
    private static final DataParameter<Float> SPIN_PITCH = EntityDataManager.createKey(EntityEvolvedZombie.class, DataSerializers.FLOAT);
    private int conversionTime = 0;
    private float tumbling = 0F;
    private float tumbleAngle = 0F;

    public EntityEvolvedZombie(World par1World)
    {
        super(par1World);
        this.setSize(0.6F, 1.95F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        double difficulty = 0;
        switch (this.world.getDifficulty())
        {
        case HARD : difficulty = 2D;
            break;
        case NORMAL : difficulty = 1D;
            break;
        }
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D + 0.04D * difficulty);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D + difficulty);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D + difficulty * 2D);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    public IAttribute getReinforcementsAttribute()
    {
        return EntityZombie.SPAWN_REINFORCEMENTS_CHANCE;
    }

    @Override
    protected void jump()
    {
        this.motionY = 0.48D / WorldUtil.getGravityFactor(this);
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
            float f = this.rotationYaw / Constants.RADIANS_TO_DEGREES;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }

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
            this.dropItem(Items.CARROT, 1);
            break;
        case 13:
        case 14:
        case 15:
            if (ConfigManagerCore.challengeMobDropsAndSpawning) this.dropItem(Items.MELON_SEEDS, 1);
            break;
        }
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        // No loot table
        this.dropFewItems(wasRecentlyHit, lootingModifier);
        this.dropEquipment(wasRecentlyHit, lootingModifier);
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        Item item = Items.ROTTEN_FLESH;

        //Less rotten flesh than vanilla
        int j = this.rand.nextInt(2);

        if (item != null)
        {
            if (lootingModifier > 0)
            {
                j += this.rand.nextInt(lootingModifier + 1);
            }

            for (int k = 0; k < j; ++k)
            {
                this.dropItem(item, 1);
            }
        }

        //Drop copper ingot as semi-rare drop if player hit and if dropping rotten flesh (50% chance)
        if (wasRecentlyHit && (ConfigManagerCore.challengeMobDropsAndSpawning) && j > 0 && this.rand.nextInt(6) <= (lootingModifier + 1) / 2)
            this.entityDropItem(new ItemStack(GCItems.basicItem, 1, 3), 0.0F);

        if (wasRecentlyHit && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.02F)
        {
            this.addRandomDrop();
        }
    }

    @Override
    public void setTumbling(float value)
    {
        if (value !=0F)
        {
            if (this.tumbling == 0F)
                this.tumbling = (this.world.rand.nextFloat() + 0.5F) * value;
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

            if (!this.world.isRemote)
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
        return (float) (this.motionZ / MathHelper.sqrt(velocity2));
    }

    @Override
    public float getTumbleAxisZ()
    {
        double velocity2 = this.motionX * this.motionX + this.motionZ * this.motionZ;
        if (velocity2 == 0D) return 0F;
        return (float) (this.motionX / MathHelper.sqrt(velocity2));
    }
}
