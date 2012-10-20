package micdoodle8.mods.galacticraft.core;

import java.util.Iterator;
import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityDamageSourceIndirect;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.StatCollector;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class GCCoreEntityMeteor extends Entity
{
    public EntityLiving shootingEntity;
    public int size;
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    
	public GCCoreEntityMeteor(World world)
	{
		super(world);
	}

    public GCCoreEntityMeteor(World world, double x, double y, double z, double motX, double motY, double motZ, int size)
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
        	spawnParticles();
        }

        Vec3 var15 = Vec3.getVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
        Vec3 var2 = Vec3.getVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition var3 = this.worldObj.rayTraceBlocks_do_do(var15, var2, true, true);
        var15 = Vec3.getVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
        var2 = Vec3.getVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (var3 != null)
        {
            var2 = Vec3.getVec3Pool().getVecFromPool(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
        }

        Entity var4 = null;
        List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(2.0D, 2.0D, 2.0D));
        double var6 = 0.0D;
        Iterator var8 = var5.iterator();

        while (var8.hasNext())
        {
            Entity var9 = (Entity)var8.next();

            if (var9.canBeCollidedWith() && (!var9.isEntityEqual(this.shootingEntity)))
            {
                float var10 = 0.01F;
                AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
                MovingObjectPosition var12 = var11.calculateIntercept(var15, var2);

                if (var12 != null)
                {
                    double var13 = var15.distanceTo(var12.hitVec);

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
            this.xTile = var3.blockX;
            this.yTile = var3.blockY;
            this.zTile = var3.blockZ;
            
            if (!(this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile) == GCCoreBlocks.breatheableAir.blockID))
            {
                this.onImpact(var3);
            }
        }
        
        if (this.posY <= -20 || this.posY >= 400)
        {
        	this.setDead();
        }
    }
    
    protected void spawnParticles()
    {
    	this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
    	this.worldObj.spawnParticle("smoke", this.posX + 1D, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
    	this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ + 1D, 0.0D, 0.0D, 0.0D);
    	this.worldObj.spawnParticle("smoke", this.posX - 1D, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
    	this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ - 1D, 0.0D, 0.0D, 0.0D);
    }

    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (!this.worldObj.isRemote)
        {
        	if (par1MovingObjectPosition != null)
        	{
                if (par1MovingObjectPosition.entityHit != null)
                {
                    par1MovingObjectPosition.entityHit.attackEntityFrom(this.causeMeteorDamage(this, this.shootingEntity), 6);
                }
        	}

            this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, this.size / 2, true);
            this.setDead();
        }
    }

    public static DamageSource causeMeteorDamage(GCCoreEntityMeteor par0EntityMeteor, Entity par1Entity)
    {
    	if (par1Entity != null && par1Entity instanceof EntityPlayer)
    	{
            StatCollector.translateToLocalFormatted("death." + "meteor", ((EntityPlayer)par1Entity).username + " was hit by a meteor! That's gotta hurt!");
    	}
        return new EntityDamageSourceIndirect("meteor", par0EntityMeteor, par1Entity).setProjectile();
    }

	@Override
	protected void entityInit() 
	{
        this.dataWatcher.addObject(16, (int) this.size);
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
