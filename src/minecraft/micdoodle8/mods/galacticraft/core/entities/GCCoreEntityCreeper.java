package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreEntityCreeper extends GCCoreEntityMob
{
    /**
     * The amount of time since the creeper was close enough to the player to ignite
     */
    int timeSinceIgnited;

    /**
     * Time when this creeper was last in an active state (Messed up code here, probably causes creeper animation to go
     * weird)
     */
    int lastActiveTime;

    public GCCoreEntityCreeper(World par1World)
    {
        super(par1World);
        this.texture = "/micdoodle8/mods/galacticraft/core/client/entities/creeper.png";
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new GCCoreEntityAICreeperSwell(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 0.25F, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.2F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.moveSpeed = 1F;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
	public boolean isAIEnabled()
    {
        return true;
    }

    @Override
	public int getMaxHealth()
    {
        return 20;
    }

    @Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) - 1));
        this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);

        if (this.dataWatcher.getWatchableObjectByte(17) == 1)
        {
            par1NBTTagCompound.setBoolean("powered", true);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.dataWatcher.updateObject(17, Byte.valueOf((byte)(par1NBTTagCompound.getBoolean("powered") ? 1 : 0)));
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate()
    {
        if (this.isEntityAlive())
        {
            this.lastActiveTime = this.timeSinceIgnited;
            final int var1 = this.getCreeperState();

            if (var1 > 0 && this.timeSinceIgnited == 0)
            {
                this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
            }

            this.timeSinceIgnited += var1;

            if (this.timeSinceIgnited < 0)
            {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= 30)
            {
                this.timeSinceIgnited = 30;

                if (!this.worldObj.isRemote)
                {
                    if (this.getPowered())
                    {
                        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 6.0F, true);
                    }
                    else
                    {
                        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 3.0F, true);
                    }

                    this.setDead();
                }
            }
        }
        
        this.motionY += 0.06F;

        super.onUpdate();
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
	protected String getHurtSound()
    {
        return "mob.creeper";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
	protected String getDeathSound()
    {
        return "mob.creeperdeath";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
	public void onDeath(DamageSource par1DamageSource)
    {
        super.onDeath(par1DamageSource);

        if (par1DamageSource.getEntity() instanceof EntitySkeleton)
        {
            this.dropItem(Item.record13.itemID + this.rand.nextInt(10), 1);
        }
    }

    @Override
	public boolean attackEntityAsMob(Entity par1Entity)
    {
        return true;
    }

    /**
     * Returns true if the creeper is powered by a lightning bolt.
     */
    public boolean getPowered()
    {
        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Connects the the creeper flashes to the creeper's color multiplier
     */
    public float setCreeperFlashTime(float par1)
    {
        return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * par1) / 28.0F;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    @Override
	protected int getDropItemId()
    {
        return Item.gunpowder.itemID;
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getCreeperState()
    {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setCreeperState(int par1)
    {
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)par1));
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    @Override
	public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt)
    {
        super.onStruckByLightning(par1EntityLightningBolt);
        this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
    }
}
