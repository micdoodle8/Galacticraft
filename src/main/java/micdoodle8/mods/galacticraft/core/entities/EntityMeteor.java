package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class EntityMeteor extends Entity
{
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
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.worldObj.isRemote)
        {
            this.spawnParticles();
        }

        Vec3 var15 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition var3 = this.worldObj.func_147447_a(var15, var2, true, true, false);
        var15 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (var3 != null)
        {
            var2 = Vec3.createVectorHelper(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
        }

        Entity var4 = null;
        final List<?> var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(2.0D, 2.0D, 2.0D));
        double var6 = 0.0D;
        final Iterator<?> var8 = var5.iterator();

        while (var8.hasNext())
        {
            final Entity var9 = (Entity) var8.next();

            if (var9.canBeCollidedWith() && !var9.isEntityEqual(this.shootingEntity))
            {
                final float var10 = 0.01F;
                final AxisAlignedBB var11 = var9.boundingBox.expand(var10, var10, var10);
                final MovingObjectPosition var12 = var11.calculateIntercept(var15, var2);

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
            var3 = new MovingObjectPosition(var4);
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
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX, this.posY + 1D + Math.random(), this.posZ), new Vector3(0.0D, 0.0D, 0.0D), new Object[] { });
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX + Math.random() / 2, this.posY + 1D + Math.random() / 2, this.posZ), new Vector3(0.0D, 0.0D, 0.0D), new Object[] { });
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX, this.posY + 1D + Math.random(), this.posZ + Math.random()), new Vector3(0.0D, 0.0D, 0.0D), new Object[] { });
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX - Math.random() / 2, this.posY + 1D + Math.random() / 2, this.posZ), new Vector3(0.0D, 0.0D, 0.0D), new Object[] { });
        GalacticraftCore.proxy.spawnParticle("distanceSmoke", new Vector3(this.posX, this.posY + 1D + Math.random(), this.posZ - Math.random()), new Vector3(0.0D, 0.0D, 0.0D), new Object[] { });
    }

    protected void onImpact(MovingObjectPosition movingObjPos)
    {
        if (!this.worldObj.isRemote)
        {
            if (movingObjPos != null)
            {
            	Block b = this.worldObj.getBlock(movingObjPos.blockX, movingObjPos.blockY + 1, movingObjPos.blockZ); 
                if (b != null && b.isAir(worldObj, movingObjPos.blockX, movingObjPos.blockY + 1, movingObjPos.blockZ))
                {
                    this.worldObj.setBlock(movingObjPos.blockX, movingObjPos.blockY + 1, movingObjPos.blockZ, GCBlocks.fallenMeteor, 0, 3);
                }

                if (movingObjPos.entityHit != null)
                {
                    movingObjPos.entityHit.attackEntityFrom(EntityMeteor.causeMeteorDamage(this, this.shootingEntity), ConfigManagerCore.hardMode ? 12F : 6F);
                }
            }

            this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, this.size / 3 + 2, false, true);
        }

        this.setDead();
    }

	@Override
    public boolean func_145774_a(Explosion p_145774_1_, World p_145774_2_, int p_145774_3_, int p_145774_4_, int p_145774_5_, Block p_145774_6_, float p_145774_7_)
    {
        return ConfigManagerCore.meteorBlockDamageEnabled;
    }
	
    public static DamageSource causeMeteorDamage(EntityMeteor par0EntityMeteor, Entity par1Entity)
    {
        if (par1Entity != null && par1Entity instanceof EntityPlayer)
        {
            StatCollector.translateToLocalFormatted("death." + "meteor", ((EntityPlayer) par1Entity).getGameProfile().getName() + " was hit by a meteor! That's gotta hurt!");
        }
        return new EntityDamageSourceIndirect("explosion", par0EntityMeteor, par1Entity).setProjectile();
    }

    @Override
    protected void entityInit()
    {
        this.dataWatcher.addObject(16, this.size);
        this.noClip = true;
    }

    public int getSize()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setSize(int par1)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(par1));
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
