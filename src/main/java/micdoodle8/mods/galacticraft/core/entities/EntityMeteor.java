package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class EntityMeteor extends Entity
{
    private static final DataParameter<Integer> SIZE = EntityDataManager.createKey(EntityBuggy.class, DataSerializers.VARINT);
    public EntityLiving shootingEntity;
    public int size;

    public EntityMeteor(World world)
    {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    public EntityMeteor(World world, double x, double y, double z, double motX, double motY, double motZ, int size)
    {
        this(world);
        this.size = size;
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
        this.motionX = motX;
        this.motionY = motY;
        this.motionZ = motZ;
        this.setSize(size);
    }

    @Override
    public void onUpdate()
    {
        this.setRotation(this.rotationYaw + 2F, this.rotationPitch + 2F);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

        if (this.world.isRemote)
        {
            this.spawnParticles();
        }

        Vec3d var15 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult var3 = this.world.rayTraceBlocks(var15, var2, true, true, false);
        var15 = new Vec3d(this.posX, this.posY, this.posZ);
        var2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (var3 != null)
        {
            var2 = new Vec3d(var3.hitVec.x, var3.hitVec.y, var3.hitVec.z);
        }

        Entity var4 = null;
        final List<?> var5 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).expand(2.0D, 2.0D, 2.0D));
        double var6 = 0.0D;
        final Iterator<?> var8 = var5.iterator();

        while (var8.hasNext())
        {
            final Entity var9 = (Entity) var8.next();

            if (var9.canBeCollidedWith() && !var9.isEntityEqual(this.shootingEntity))
            {
                final float var10 = 0.01F;
                final AxisAlignedBB var11 = var9.getEntityBoundingBox().expand(var10, var10, var10);
                final RayTraceResult var12 = var11.calculateIntercept(var15, var2);

                if (var12 != null)
                {
                    final double var13 = var15.distanceTo(var12.hitVec);

                    if (var13 < var6 || var6 == 0.0D)
                    {
                        var4 = var9;
                        var6 = var13;
                    }
                }
            }
        }

        if (var4 != null)
        {
            var3 = new RayTraceResult(var4);
        }

        if (var3 != null)
        {
            this.onImpact(var3);
        }

        if (this.posY <= -20 || this.posY >= 400)
        {
            this.setDead();
        }
    }

    protected void spawnParticles()
    {
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX, this.posY + 1D + Math.random(), this.posZ), new Vector3(0.0D, 0.0D, 0.0D), new Object[] {});
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX + Math.random() / 2, this.posY + 1D + Math.random() / 2, this.posZ), new Vector3(0.0D, 0.0D, 0.0D), new Object[] {});
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX, this.posY + 1D + Math.random(), this.posZ + Math.random()), new Vector3(0.0D, 0.0D, 0.0D), new Object[] {});
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX - Math.random() / 2, this.posY + 1D + Math.random() / 2, this.posZ), new Vector3(0.0D, 0.0D, 0.0D), new Object[] {});
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX, this.posY + 1D + Math.random(), this.posZ - Math.random()), new Vector3(0.0D, 0.0D, 0.0D), new Object[] {});
    }

    protected void onImpact(RayTraceResult movingObjPos)
    {
        if (!this.world.isRemote)
        {
            if (movingObjPos != null)
            {
                BlockPos above = movingObjPos.getBlockPos().up();
                if (this.world.getBlockState(above).getBlock().isAir(world.getBlockState(above), world, above))
                {
                    this.world.setBlockState(above, GCBlocks.fallenMeteor.getDefaultState(), 3);
                }

                if (movingObjPos.entityHit != null)
                {
                    movingObjPos.entityHit.attackEntityFrom(EntityMeteor.causeMeteorDamage(this, this.shootingEntity), ConfigManagerCore.hardMode ? 12F : 6F);
                }
            }

            this.world.newExplosion(this, this.posX, this.posY, this.posZ, this.size / 3 + 2, false, true);
        }

        this.setDead();
    }

    @Override
    public boolean canExplosionDestroyBlock(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_)
    {
        return ConfigManagerCore.meteorBlockDamageEnabled;
    }

    public static DamageSource causeMeteorDamage(EntityMeteor par0EntityMeteor, Entity par1Entity)
    {
        if (par1Entity != null && par1Entity instanceof EntityPlayer)
        {
            I18n.translateToLocalFormatted("death." + "meteor", ((EntityPlayer) par1Entity).getGameProfile().getName() + " was hit by a meteor! That's gotta hurt!");
        }
        return new EntityDamageSourceIndirect("explosion", par0EntityMeteor, par1Entity).setProjectile();
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(SIZE, this.size);
        this.noClip = true;
    }

    public int getSize()
    {
        return this.dataManager.get(SIZE);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setSize(int par1)
    {
        this.dataManager.set(SIZE, Integer.valueOf(par1));
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    }
}
