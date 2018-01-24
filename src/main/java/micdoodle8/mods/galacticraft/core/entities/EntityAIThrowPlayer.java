package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIThrowPlayer extends EntityAIBase
{
    EntitySkeletonBoss skeletonBoss;

    EntityPlayer targetPlayer;

    public EntityAIThrowPlayer(EntitySkeletonBoss boss)
    {
        this.skeletonBoss = boss;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute()
    {
        final EntityPlayer player = this.skeletonBoss.world.getClosestPlayerToEntity(this.skeletonBoss, 5.0F);

        if (player == null)
        {
            return false;
        }
        else
        {
            this.targetPlayer = player;
            return true;
        }
    }

    @Override
    public void startExecuting()
    {
        this.skeletonBoss.setAttackTarget(this.targetPlayer);

        // if (this.skeletonBoss.getDistanceToEntity(this.targetPlayer) <= 5.0F)
        {
            double d0 = this.skeletonBoss.posX - this.targetPlayer.posX;
            double d1;

            for (d1 = this.skeletonBoss.posZ - this.targetPlayer.posZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D)
            {
                d0 = (Math.random() - Math.random()) * 0.01D;
            }

            this.targetPlayer.attackedAtYaw = (float) Math.atan2(d1, d0) * Constants.RADIANS_TO_DEGREES - this.targetPlayer.rotationYaw;

            this.targetPlayer.knockBack(this.skeletonBoss, 20, d0, d1);
        }

        super.startExecuting();
    }
}
