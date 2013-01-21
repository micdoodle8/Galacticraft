package micdoodle8.mods.galacticraft.mars.entities;

import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
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
public class GCMarsEntityCreeperBoss extends EntityMob
{
    int timeSinceIgnited;

    int lastActiveTime;
    
    Entity targetedEntity = null;
    
    int timeSinceTNTFired;
    
    public int headsRemaining = 3;
    
    public int deathTicks = 0;

    public GCMarsEntityCreeperBoss(World par1World)
    {
        super(par1World);
        this.texture = "/micdoodle8/mods/galacticraft/mars/client/entities/creeper.png";
        this.setSize(8.0F, 12.0F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
//        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 0.25F, false));
        this.tasks.addTask(4, new GCMarsEntityAIProjectileTNTAttack(this, this.moveSpeed, 1, 60));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.isImmuneToFire = true;
    }

    @Override
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public int getMaxHealth()
    {
        return 180;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Integer(this.getMaxHealth()));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
    }

    @Override
    public void onUpdate()
    {
        if (!this.worldObj.isRemote)
        {
            this.dataWatcher.updateObject(16, Integer.valueOf(this.health));
        }
		
		if (this.getBossHealth() >= 2 * (this.getMaxHealth() / 3))
		{
        	this.headsRemaining = 3;
		}
		else if (this.getBossHealth() >= this.getMaxHealth() / 3)
		{
			this.headsRemaining = 2;
		}
		else if (this.getBossHealth() <= this.getMaxHealth() / 3)
		{
			this.headsRemaining = 1;
		}
		else
		{
			this.headsRemaining = 0;
		}
        
    	if (this.timeSinceTNTFired > 0)
    	{
    		this.timeSinceTNTFired -= 1;
    	}
    	
        this.targetedEntity = this.worldObj.getClosestVulnerablePlayerToEntity(this, 50.0D);
        
        if (!this.worldObj.isRemote && this.timeSinceTNTFired == 0)
    	{
            final EntityPlayer var11 = null;

            for (int var12 = 0; var12 < this.worldObj.playerEntities.size(); ++var12)
            {
                final EntityPlayer var13 = (EntityPlayer)this.worldObj.playerEntities.get(var12);

                if (!var13.capabilities.isCreativeMode)
                {
                    final double var14 = var13.getDistanceSq(this.posX, this.posY, this.posZ);

                    if ((5 < 0.0D || var14 < 5 * 5))
                    {
                        GCCoreUtil.createNewExplosion(this.worldObj, this, this.posX, this.posY, this.posZ, 12.0F, true);
                        this.timeSinceTNTFired = 100;
                    }
                }
            }
    	}
        
//        if (this.targetedEntity != null && !this.worldObj.isRemote)
//        {
//        	if (this.timeSinceTNTFired == 0)
//        	{
//                double var11 = this.targetedEntity.posX - this.posX;
//                double var13 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
//                double var15 = this.targetedEntity.posZ - this.posZ;
//            	GCEntityProjectileTNT var17 = new GCEntityProjectileTNT(this.worldObj, this, var11, var13, var15);
//                double var18 = 4.0D;
//                Vec3 var20 = this.getLook(1.0F);
//                var17.posX = this.posX + var20.xCoord * var18;
//                var17.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
//                var17.posZ = this.posZ + var20.zCoord * var18;
//                this.worldObj.spawnEntityInWorld(var17);
//                
//                this.timeSinceTNTFired = 80;
//        	}
//        }
        
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

        super.onUpdate();
    }
    
    @Override
	protected void onDeathUpdate()
    {
        ++this.deathTicks;

        if (this.deathTicks >= 180 && this.deathTicks <= 200)
        {
            final float var1 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            final float var2 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            final float var3 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.worldObj.spawnParticle("hugeexplosion", this.posX + var1, this.posY + 2.0D + var2, this.posZ + var3, 0.0D, 0.0D, 0.0D);
        }

        int var4;
        int var5;

        if (!this.worldObj.isRemote && this.deathTicks > 150 && this.deathTicks % 5 == 0)
        {
            var4 = 100;

            while (var4 > 0)
            {
                var5 = EntityXPOrb.getXPSplit(var4);
                var4 -= var5;
                this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5 / 10));
            }
        }

        this.moveEntity(0.0D, this.deathTicks / 375D, 0.0D);

        if (this.deathTicks == 200 && !this.worldObj.isRemote)
        {
            var4 = 100;

            while (var4 > 0)
            {
                var5 = EntityXPOrb.getXPSplit(var4);
                var4 -= var5;
                this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5 / 10));
            }
            
            this.setDead();
        }
    }

    @Override
    protected String getHurtSound()
    {
        return "mob.creeper";
    }

    @Override
    protected String getDeathSound()
    {
        return "mob.creeperdeath";
    }

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

    public boolean getPowered()
    {
    	return false;
//        return this.dataWatcher.getWatchableObjectByte(17) == 1;
    }

    @SideOnly(Side.CLIENT)
    public float setCreeperFlashTime(float par1)
    {
        return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * par1) / 28.0F;
    }

    @Override
	protected int getDropItemId()
    {
        return Item.gunpowder.itemID;
    }

    public int getCreeperState()
    {
    	return 0;
//        return this.dataWatcher.getWatchableObjectByte(16);
    }

    public void setCreeperState(int par1)
    {
//        this.dataWatcher.updateObject(16, Byte.valueOf((byte)par1));
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt)
    {
    }
    
    public int getBossHealth()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }
}
