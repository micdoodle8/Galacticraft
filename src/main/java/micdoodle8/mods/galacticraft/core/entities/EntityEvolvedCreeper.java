package micdoodle8.mods.galacticraft.core.entities;

import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

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
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 0.25F, false));
		this.tasks.addTask(5, new EntityAIWander(this, 0.2F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.setSize(0.6F, 1.8F);
	}

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
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0F);
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

    protected final void setSize(float sizeX, float sizeY)
    {
        boolean flag = this.sizeXBase > 0.0F && this.sizeYBase > 0.0F;
        new Exception().printStackTrace();
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
        FMLLog.info("" + this.sizeYBase + " " + scale);
    }

    public boolean isChild()
    {
        return this.getDataWatcher().getWatchableObjectByte(12) == 1;
    }

    protected int getExperiencePoints(EntityPlayer p_70693_1_)
    {
        if (this.isChild())
        {
            this.experienceValue = (int)((float)this.experienceValue * 2.5F);
        }

        return super.getExperiencePoints(p_70693_1_);
    }

    public void setChild(boolean isChild)
    {
        this.getDataWatcher().updateObject(12, Byte.valueOf((byte)(isChild ? 1 : 0)));

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
}
