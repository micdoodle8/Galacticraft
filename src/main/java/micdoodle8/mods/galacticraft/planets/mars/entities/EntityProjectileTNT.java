package micdoodle8.mods.galacticraft.planets.mars.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityProjectileTNT extends EntityFireball
{
    public EntityProjectileTNT(World par1World)
    {
        super(par1World);
        this.setSize(1.0F, 1.0F);
    }

    public EntityProjectileTNT(World par1World, EntityLivingBase par2EntityLivingBase, double par3, double par5, double par7)
    {
        super(par1World, par2EntityLivingBase, par3, par5, par7);
        this.setSize(1.0F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public EntityProjectileTNT(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        super(par1World, par2, par4, par6, par8, par10, par12);
        this.setSize(0.3125F, 0.3125F);
    }

    @Override
    public boolean isBurning()
    {
        return false;
    }

    @Override
    protected void onImpact(RayTraceResult movingObjectPosition)
    {
        if (!this.world.isRemote)
        {
            if (movingObjectPosition.entityHit != null && !(movingObjectPosition.entityHit instanceof EntityCreeper))
            {
                float difficulty = 0;
                switch (this.world.getDifficulty())
                {
                case HARD : difficulty = 2F;
                    break;
                case NORMAL : difficulty = 1F;
                    break;
                }
                movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F + 3.0F * difficulty);
            }

            this.world.newExplosion((Entity) null, this.posX, this.posY, this.posZ, 1.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
            this.setDead();
        }
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }
}
