package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class EntityAIArrowAttack extends Goal
{
    private final MobEntity entityHost;

    private final IRangedAttackMob rangedAttackEntityHost;
    private LivingEntity attackTarget;

    private int rangedAttackTime;
    private final double entityMoveSpeed;
    private final int field_96561_g;

    private final int maxRangedAttackTime;
    private final float field_96562_i;
    private final float field_82642_h;

    public EntityAIArrowAttack(IRangedAttackMob par1IRangedAttackMob, double par2, int par4, float par5)
    {
        this(par1IRangedAttackMob, par2, par4, par4, par5);
    }

    public EntityAIArrowAttack(IRangedAttackMob par1IRangedAttackMob, double par2, int par4, int par5, float par6)
    {
        this.rangedAttackTime = -1;

        if (!(par1IRangedAttackMob instanceof LivingEntity))
        {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        else
        {
            this.rangedAttackEntityHost = par1IRangedAttackMob;
            this.entityHost = (MobEntity) par1IRangedAttackMob;
            this.entityMoveSpeed = par2;
            this.field_96561_g = par4;
            this.maxRangedAttackTime = par5;
            this.field_96562_i = par6;
            this.field_82642_h = par6 * par6;
            this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        LivingEntity entitylivingbase = this.entityHost.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else
        {
            this.attackTarget = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        this.attackTarget = null;
        this.rangedAttackTime = -1;
    }

    /**
     * Updates the task
     */
    @Override
    public void tick()
    {
        double d0 = this.entityHost.getDistanceSq(this.attackTarget.getPosX(), this.attackTarget.getBoundingBox().minY, this.attackTarget.getPosZ());
        boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);

        this.entityHost.getLookController().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
        float f;

        if (--this.rangedAttackTime == 0)
        {
            if (d0 > this.field_82642_h || !flag)
            {
                return;
            }

            f = MathHelper.sqrt(d0) / this.field_96562_i;
            float f1 = f;

            if (f < 0.1F)
            {
                f1 = 0.1F;
            }

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }

            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, f1);
            this.rangedAttackTime = MathHelper.floor(f * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
        }
        else if (this.rangedAttackTime < 0)
        {
            f = MathHelper.sqrt(d0) / this.field_96562_i;
            this.rangedAttackTime = MathHelper.floor(f * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
        }
    }
}
