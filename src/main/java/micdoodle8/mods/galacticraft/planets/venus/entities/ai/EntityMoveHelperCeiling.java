package micdoodle8.mods.galacticraft.planets.venus.entities.ai;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.MathHelper;

public class EntityMoveHelperCeiling extends EntityMoveHelper
{
    public EntityMoveHelperCeiling(EntityLiving entitylivingIn)
    {
        super(entitylivingIn);
    }

    @Override
    public void onUpdateMoveHelper()
    {
        this.entity.setMoveForward(0.0F);

        if (this.update)
        {
            this.update = false;
            int i = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5D);
            double d0 = this.posX - this.entity.posX;
            double d1 = this.posZ - this.entity.posZ;
            double d2 = this.posY - (double)i;
            double d3 = d0 * d0 + d1 * d1;

            if (d3 >= 2.500000277905201E-7D)
            {
                float f = (float) MathHelper.atan2(d1, d0) * Constants.RADIANS_TO_DEGREES - 90.0F;
                this.entity.rotationYaw = f;
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));

                if (d2 > 0.0D && d0 * d0 + d1 * d1 < 1.0D)
                {
                    this.entity.getJumpHelper().setJumping();
                }
            }
        }
    }
}