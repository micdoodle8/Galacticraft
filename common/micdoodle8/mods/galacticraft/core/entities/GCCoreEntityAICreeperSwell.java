package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreEntityAICreeperSwell extends EntityAIBase
{
    /** The creeper that is swelling. */
    GCCoreEntityCreeper swellingCreeper;

    /**
     * The creeper's attack target. This is used for the changing of the
     * creeper's state.
     */
    EntityLiving creeperAttackTarget;

    public GCCoreEntityAICreeperSwell(GCCoreEntityCreeper par1GCEntityCreeper)
    {
        this.swellingCreeper = par1GCEntityCreeper;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        final EntityLiving var1 = this.swellingCreeper.getAttackTarget();
        return this.swellingCreeper.getCreeperState() > 0 || var1 != null && this.swellingCreeper.getDistanceSqToEntity(var1) < 9.0D;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.swellingCreeper.getNavigator().clearPathEntity();
        this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        this.creeperAttackTarget = null;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        if (this.creeperAttackTarget == null)
        {
            this.swellingCreeper.setCreeperState(-1);
        }
        else if (this.swellingCreeper.getDistanceSqToEntity(this.creeperAttackTarget) > 49.0D)
        {
            this.swellingCreeper.setCreeperState(-1);
        }
        else if (!this.swellingCreeper.getEntitySenses().canSee(this.creeperAttackTarget))
        {
            this.swellingCreeper.setCreeperState(-1);
        }
        else
        {
            this.swellingCreeper.setCreeperState(1);
        }
    }
}
