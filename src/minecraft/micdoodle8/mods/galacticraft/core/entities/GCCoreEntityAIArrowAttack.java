package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreEntityAIArrowAttack extends EntityAIBase
{
    World worldObj;

    /** The entity the AI instance has been applied to */
    EntityLiving entityHost;
    EntityLiving attackTarget;

    /**
     * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
     * maxRangedAttackTime.
     */
    int rangedAttackTime = 0;
    float entityMoveSpeed;
    int field_75318_f = 0;

    /**
     * The ID of this ranged attack AI. This chooses which entity is to be used as a ranged attack.
     */
    int rangedAttackID;

    /**
     * The maximum time the AI has to wait before peforming another ranged attack.
     */
    int maxRangedAttackTime;

    public GCCoreEntityAIArrowAttack(EntityLiving par1EntityLiving, float par2, int par3, int par4)
    {
        this.entityHost = par1EntityLiving;
        this.worldObj = par1EntityLiving.worldObj;
        this.entityMoveSpeed = par2;
        this.rangedAttackID = par3;
        if (par4 == 20 && this.worldObj != null && this.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            this.maxRangedAttackTime = par4;
        }
        else
        {
            this.maxRangedAttackTime = par4 * 6;
        }
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
        final EntityLiving var1 = this.entityHost.getAttackTarget();

        if (var1 == null)
        {
            return false;
        }
        else
        {
            this.attackTarget = var1;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
	public boolean continueExecuting()
    {
        return this.shouldExecute();
    }

    /**
     * Resets the task
     */
    @Override
	public void resetTask()
    {
        this.attackTarget = null;
    }

    /**
     * Updates the task
     */
    @Override
	public void updateTask()
    {
        final double var1 = 100.0D;
        final double var3 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
        final boolean var5 = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if (var5)
        {
            ++this.field_75318_f;
        }
        else
        {
            this.field_75318_f = 0;
        }

//        if (var3 <= var1 && this.field_75318_f >= 20)
        {
//            this.entityHost.getNavigator().clearPathEntity();
        }
//        else
//        {
//            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
//        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
        this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);

        if (this.rangedAttackTime <= 0)
        {
            if (var3 <= var1 && var5)
            {
                this.doRangedAttack();
                this.rangedAttackTime = this.maxRangedAttackTime;
            }
        }
    }

    /**
     * Performs a ranged attack according to the AI's rangedAttackID.
     */
    private void doRangedAttack()
    {
        if (this.rangedAttackID == 1)
        {
        	Entity var1;

        	if (this.worldObj.provider instanceof IGalacticraftWorldProvider)
        	{
        		var1 = new GCCoreEntityArrow(this.worldObj, this.entityHost, this.attackTarget, 0.3F, 12.0F);
        	}
        	else
        	{
                var1 = new EntityArrow(this.worldObj, this.entityHost, this.attackTarget, 1.6F, 12.0F);
        	}

            this.worldObj.playSoundAtEntity(this.entityHost, "random.bow", 1.0F, 1.0F / (this.entityHost.getRNG().nextFloat() * 0.4F + 0.8F));
            this.worldObj.spawnEntityInWorld(var1);
        }
        else if (this.rangedAttackID == 2)
        {
            final EntitySnowball var9 = new EntitySnowball(this.worldObj, this.entityHost);
            final double var2 = this.attackTarget.posX - this.entityHost.posX;
            final double var4 = this.attackTarget.posY + this.attackTarget.getEyeHeight() - 1.100000023841858D - var9.posY;
            final double var6 = this.attackTarget.posZ - this.entityHost.posZ;
            final float var8 = MathHelper.sqrt_double(var2 * var2 + var6 * var6) * 0.2F;
            var9.setThrowableHeading(var2, var4 + var8, var6, 1.6F, 12.0F);
            this.worldObj.playSoundAtEntity(this.entityHost, "random.bow", 1.0F, 1.0F / (this.entityHost.getRNG().nextFloat() * 0.4F + 0.8F));
            this.worldObj.spawnEntityInWorld(var9);
        }
    }
}
