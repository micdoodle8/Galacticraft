package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
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
            float f = this.rotationYaw / Constants.RADIANS_TO_DEGREES;
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
            if (ConfigManagerCore.challengeMobDropsAndSpawning) this.dropItem(Items.PUMPKIN_SEEDS, 1);
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
        Item item = Items.BONE;

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
        if (wasRecentlyHit && (ConfigManagerCore.challengeMobDropsAndSpawning) && j > 1 && this.rand.nextInt(12) <= lootingModifier)
            this.entityDropItem(new ItemStack(Items.DYE, 1, 4), 0.0F);

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
