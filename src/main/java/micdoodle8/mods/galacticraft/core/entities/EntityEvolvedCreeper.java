package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.UUID;

public class EntityEvolvedCreeper extends EntityCreeper implements IEntityBreathable
{
    private float sizeXBase = -1.0F;
    private float sizeYBase;
    private static final UUID babySpeedBoostUUID = UUID.fromString("ef67a435-32a4-4efd-b218-e7431438b109");
    private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost evolved creeper", 0.5D, 1);

    public EntityEvolvedCreeper(World par1World)
    {
        super(par1World);
        this.tasks.taskEntries.clear();
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAICreeperSwell(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.setSize(0.7F, 2.2F);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(12, Byte.valueOf((byte) 0));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25.0D);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);

        if (this.isChild())
        {
            nbt.setBoolean("IsBaby", true);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);

        if (nbt.getBoolean("IsBaby"))
        {
            this.setChild(true);
        }
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    public void setChildSize(boolean isChild)
    {
        this.setCreeperScale(isChild ? 0.5F : 1.0F);
    }

    @Override
    protected final void setSize(float sizeX, float sizeY)
    {
        boolean flag = this.sizeXBase > 0.0F && this.sizeYBase > 0.0F;
        this.sizeXBase = sizeX;
        this.sizeYBase = sizeY;

        if (!flag)
        {
            this.setCreeperScale(1.0F);
        }
    }

    protected final void setCreeperScale(float scale)
    {
        super.setSize(this.sizeXBase * scale, this.sizeYBase * scale);
        //FMLLog.info("" + this.sizeYBase + " " + scale);
    }

    @Override
    public boolean isChild()
    {
        return this.getDataWatcher().getWatchableObjectByte(12) == 1;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer p_70693_1_)
    {
        if (this.isChild())
        {
            this.experienceValue = (this.experienceValue * 5) / 2;
        }

        return super.getExperiencePoints(p_70693_1_);
    }

    public void setChild(boolean isChild)
    {
        this.getDataWatcher().updateObject(12, Byte.valueOf((byte) (isChild ? 1 : 0)));

        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            iattributeinstance.removeModifier(babySpeedBoostModifier);

            if (isChild)
            {
                iattributeinstance.applyModifier(babySpeedBoostModifier);
            }
        }

        this.setChildSize(isChild);
    }

    @Override
    protected void jump()
    {
        this.motionY = 0.45D / WorldUtil.getGravityFactor(this);
        if (this.motionY < 0.22D)
        {
            this.motionY = 0.22D;
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
    protected Item getDropItem()
    {
        if (this.isBurning())
        {
            return Items.blaze_rod;
        }
        return Items.redstone;
    }

    @Override
    protected void addRandomDrop()
    {
        switch (this.rand.nextInt(12))
        {
        case 0:
        case 1:
        case 2:
        case 3:
            this.entityDropItem(new ItemStack(Blocks.sand), 0.0F);
            break;
        case 4:
        case 5:
            //Oxygen tank half empty or less
            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
            break;
        case 6:
            this.dropItem(GCItems.oxygenGear, 1);
            break;
        case 7:
        case 8:
            this.entityDropItem(new ItemStack(Blocks.ice), 0.0F);
            break;
        default:
            if (ConfigManagerCore.challengeMobDropsAndSpawning) this.dropItem(Items.reeds, 1);
            break;
        }
    }
}
