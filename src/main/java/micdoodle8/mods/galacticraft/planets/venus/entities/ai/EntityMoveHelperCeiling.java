package micdoodle8.mods.galacticraft.planets.venus.entities.ai;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;

public class EntityMoveHelperCeiling extends MovementController
{
    public EntityMoveHelperCeiling(MobEntity entitylivingIn)
    {
        super(entitylivingIn);
    }

    @Override
    public void tick()
    {
        this.mob.setMoveForward(0.0F);

        if (this.isUpdating())
        {
            this.action = MovementController.Action.WAIT;
            int i = MathHelper.floor(this.mob.getBoundingBox().minY + 0.5D);
            double d0 = this.posX - this.mob.getPosX();
            double d1 = this.posZ - this.mob.getPosZ();
            double d2 = this.posY - (double) i;
            double d3 = d0 * d0 + d1 * d1;

            if (d3 >= 2.500000277905201E-7D)
            {
                float f = (float) MathHelper.atan2(d1, d0) * Constants.RADIANS_TO_DEGREES - 90.0F;
                this.mob.rotationYaw = f;
                this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));

                if (d2 > 0.0D && d0 * d0 + d1 * d1 < 1.0D)
                {
                    this.mob.getJumpController().setJumping();
                }
            }
        }
    }
}