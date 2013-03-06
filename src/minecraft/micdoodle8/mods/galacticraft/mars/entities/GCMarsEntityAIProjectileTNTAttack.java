package micdoodle8.mods.galacticraft.mars.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsEntityAIProjectileTNTAttack extends EntityAIBase
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

    public GCMarsEntityAIProjectileTNTAttack(EntityLiving par1EntityLiving, float movementSpeed, int rangedAttackID, int waitBeforeRefire)
    {
        this.entityHost = par1EntityLiving;
        this.worldObj = par1EntityLiving.worldObj;
        this.entityMoveSpeed = movementSpeed;
        this.rangedAttackID = rangedAttackID;
        this.maxRangedAttackTime = waitBeforeRefire;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
        final EntityLiving var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this.entityHost, 50.0D);
        
        if (var1 != null && var1.isDead)
        {
        	this.resetTask();
        }

        if (var1 == null || var1.isDead)
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
        return true;
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
        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
        this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);

        if (this.rangedAttackTime <= 0)
        {
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
        	final double var11 = this.attackTarget.posX - this.entityHost.posX;
            final double var13 = this.attackTarget.boundingBox.minY + this.attackTarget.height / 2.0F - (this.entityHost.posY + this.entityHost.height / 2.0F);
            final double var15 = this.attackTarget.posZ - this.entityHost.posZ;
        	final GCMarsEntityProjectileTNT var17 = new GCMarsEntityProjectileTNT(this.entityHost.worldObj, this.entityHost, var11, var13, var15);
            final double var18 = 4.0D;
            final Vec3 var20 = this.entityHost.getLook(1.0F);
            var17.posX = this.entityHost.posX + var20.xCoord * 1;
            var17.posY = this.entityHost.posY + this.entityHost.height / 2.0F + 0.5D;
            var17.posZ = this.entityHost.posZ + var20.zCoord * 1;
            this.worldObj.spawnEntityInWorld(var17);
        }
    }
}
