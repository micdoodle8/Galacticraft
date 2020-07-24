package micdoodle8.mods.galacticraft.planets.mars.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntityProjectileTNT extends DamagingProjectileEntity
{
    public EntityProjectileTNT(EntityType<? extends EntityProjectileTNT> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.0F, 1.0F);
    }

    public static EntityProjectileTNT createEntityProjectileTNT(World world, LivingEntity entityShooting, double motX, double motY, double motZ)
    {
        EntityProjectileTNT projectileTNT = new EntityProjectileTNT(MarsEntities.PROJECTILE_TNT.get(), world);
//        this.setSize(1.0F, 1.0F);
        projectileTNT.shootingEntity = entityShooting;
        projectileTNT.setLocationAndAngles(entityShooting.getPosX(), entityShooting.getPosY(), entityShooting.getPosZ(), entityShooting.rotationYaw, entityShooting.rotationPitch);
        projectileTNT.setPosition(projectileTNT.getPosX(), projectileTNT.getPosY(), projectileTNT.getPosZ());
        projectileTNT.setMotion(Vec3d.ZERO);
        motX = motX + projectileTNT.rand.nextGaussian() * 0.4D;
        motY = motY + projectileTNT.rand.nextGaussian() * 0.4D;
        motZ = motZ + projectileTNT.rand.nextGaussian() * 0.4D;
        double d0 = MathHelper.sqrt(motX * motX + motY * motY + motZ * motZ);
        projectileTNT.accelerationX = motX / d0 * 0.1D;
        projectileTNT.accelerationY = motY / d0 * 0.1D;
        projectileTNT.accelerationZ = motZ / d0 * 0.1D;
        return projectileTNT;
    }
//
//    @OnlyIn(Dist.CLIENT)
//    public EntityProjectileTNT(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
//    {
//        super(par1World, par2, par4, par6, par8, par10, par12);
////        this.setSize(0.3125F, 0.3125F);
//    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
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
            if (movingObjectPosition.getType() == RayTraceResult.Type.ENTITY)
            {
                EntityRayTraceResult entityResult = (EntityRayTraceResult) movingObjectPosition;
                if (!(entityResult.getEntity() instanceof CreeperEntity))
                {
                    float difficulty = 0;
                    switch (this.world.getDifficulty())
                    {
                        case HARD:
                            difficulty = 2F;
                            break;
                        case NORMAL:
                            difficulty = 1F;
                            break;
                    }
                    entityResult.getEntity().attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F + 3.0F * difficulty);
                }
            }

            this.world.createExplosion(null, this.getPosX(), this.getPosY(), this.getPosZ(), 1.0F, false, this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
            this.remove();
        }
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }
}
