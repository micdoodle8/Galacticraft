package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRail;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIControlledByPlayer;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PathFinder;
import net.minecraft.src.PathPoint;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreEntitySpaceship extends Entity
{
    protected int fuel;
    
    public double pushX;
    public double pushZ;
    
    protected double minecartX;
    protected double minecartY;
    protected double minecartZ;
    protected double minecartYaw;
    protected double minecartPitch;
    @SideOnly(Side.CLIENT)
    protected double velocityX;
    @SideOnly(Side.CLIENT)
    protected double velocityY;
    @SideOnly(Side.CLIENT)
    protected double velocityZ;

    protected float maxSpeedRail;
    protected float maxSpeedGround;
    protected float maxSpeedAirLateral;
    protected float maxSpeedAirVertical;
    protected double dragAir;
    
    protected int ignite;
    protected int timeUntilLaunch;
    protected boolean launched;
    
    protected float timeSinceEntityEntry;
    protected float timeSinceLaunch;
    
    public float rumble;
    
    protected boolean reversed;
    
    protected boolean failedLaunch;

    public GCCoreEntitySpaceship(World par1World)
    {
        super(par1World);
        this.fuel = 0;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 4F);
        this.yOffset = this.height / 2.0F;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, new Byte((byte)0));
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
        this.dataWatcher.addObject(21, new Integer(0));
        this.dataWatcher.addObject(22, new Integer(0));
    }

    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.boundingBox;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    public boolean canBePushed()
    {
        return false;
    }

    public GCCoreEntitySpaceship(World par1World, double par2, double par4, double par6, boolean reversed)
    {
        this(par1World);
        this.setPosition(par2, par4 + (double)this.yOffset, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
        this.reversed = reversed;
        
        if (reversed)
        {
        	this.rotationPitch += 180F;
        	this.motionY = -1.0D;
        }
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
        this.func_70494_i(-this.func_70493_k());
        this.func_70497_h(5);
        this.setDamage(this.getDamage() + this.getDamage() * 10);
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    
    public boolean shouldRiderSit()
    {
        return false;
    }
    
    public void onUpdate()
    {
    	if (this.rumble > 0)
    	{
    		this.rumble--;
    	}
    	
    	if (this.rumble < 0)
    	{
    		this.rumble++;
    	}
    	
    	if (this.timeSinceEntityEntry > 0)
    	{
    		this.timeSinceEntityEntry--;
    	}
    	
    	if (this.reversed)
    	{
    		this.dataWatcher.updateObject(21, Integer.valueOf(1));
    	}
		
    	if (this.reversed)
    	{
    		if (this.worldObj.getBlockMaterial((int)this.posX, (int)this.posY - 5, (int)this.posZ) != Material.air || this.motionY == 0)
    		{
                if (this.riddenByEntity != null)
                {
                    this.riddenByEntity.mountEntity(this);
                }

                GCCoreUtil.createNewExplosion(worldObj, this, this.posX, this.posY, this.posZ, 6, false);
                
				if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && !((EntityPlayer) this.riddenByEntity).capabilities.isCreativeMode)
				{
					EntityItem var14 = new EntityItem(this.worldObj, MathHelper.floor_double(this.riddenByEntity.posX + 0.5D), MathHelper.floor_double(this.riddenByEntity.posY + 1D), MathHelper.floor_double(this.riddenByEntity.posZ + 0.5D), new ItemStack(GCCoreItems.spaceship));

			        float var15 = 0.05F;
			        var14.motionX = (double)((float)this.rand.nextGaussian() * var15);
			        var14.motionY = (double)((float)this.rand.nextGaussian() * var15 + 0.2F);
			        var14.motionZ = (double)((float)this.rand.nextGaussian() * var15);
			        this.worldObj.spawnEntityInWorld(var14);
				}
				else if (this.riddenByEntity == null)
				{
					EntityItem var14 = new EntityItem(this.worldObj, MathHelper.floor_double(this.posX + 0.5D), MathHelper.floor_double(this.posY + 1D), MathHelper.floor_double(this.posZ + 0.5D), new ItemStack(GCCoreItems.spaceship));

			        float var15 = 0.05F;
			        var14.motionX = (double)((float)this.rand.nextGaussian() * var15);
			        var14.motionY = (double)((float)this.rand.nextGaussian() * var15 + 0.2F);
			        var14.motionZ = (double)((float)this.rand.nextGaussian() * var15);
			        this.worldObj.spawnEntityInWorld(var14);
				}
                
    			this.setDead();
    		}
    	}
    	
    	if (this.riddenByEntity != null)
    	{
    		if (this.failedLaunch && this.timeSinceLaunch >= 100)
    		{
        		this.riddenByEntity.posX += (this.rumble) / 20F;
        		this.riddenByEntity.posZ += (this.rumble) / 20F;
    		}
    		else
    		{
        		this.riddenByEntity.posX += (this.rumble) / 30F;
        		this.riddenByEntity.posZ += (this.rumble) / 30F;
    		}
    		
        	if (FMLClientHandler.instance().getClient().gameSettings.keyBindLeft.pressed)
        	{
        		this.rotationYaw -= 0.5F;
        	}
        	
        	if (FMLClientHandler.instance().getClient().gameSettings.keyBindRight.pressed)
        	{
        		this.rotationYaw += 0.5F;
        	}
        	
        	if (FMLClientHandler.instance().getClient().gameSettings.keyBindForward.pressed)
        	{
        		this.rotationYaw += 0.5F;
        	}
    	}
    	
    	if (this.posY > 450D && !this.reversed)
    	{
    		this.teleport();
    	}
    	
    	if (this.func_70496_j() > 0)
        {
            this.func_70497_h(this.func_70496_j() - 1);
        }

        if (this.getDamage() > 0)
        {
            this.setDamage(this.getDamage() - 1);
        }

        if (this.posY < -64.0D || this.posY > 500D && this.dataWatcher.getWatchableObjectInt(21) == 0)
        {
            this.kill();
        }
        
        if (this.ignite == 0)
        {
        	this.timeUntilLaunch = 400;
        }
        
        if (this.launched)
        {
        	this.timeSinceLaunch++;
        }
        else
        {
        	this.timeSinceLaunch = 0;
        }
        
        if (this.timeUntilLaunch > 0 && ignite == 1)
        {
        	this.timeUntilLaunch --;
        }
        
        if (this.timeUntilLaunch == 0 && ignite == 1)
        {
        	this.launched = true;
        	this.setLaunched(1);
        	this.ignite = 0;
        }
        
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        
        int i;
        
        if (this.timeUntilLaunch >= 100)
        {
            i = Math.abs((int)this.timeUntilLaunch / 100);
        }
        else
        {
        	i = 1;
        }
        
        if ((this.ignite == 1 || launched))
        {
            this.performHurtAnimation();
            
        	this.rumble = (float) (rand.nextInt(3)) - 3;
        }

        if ((this.launched || this.rand.nextInt(i) == 0) && this.dataWatcher.getWatchableObjectInt(21) == 0)
        {
        	this.spawnParticles(this.launched);
        }

        if (this.worldObj.isRemote)
        {
        	this.rotationPitch -= Float.valueOf(this.getFailedLaunch()) / 2;
        	
        	this.rotationYaw += Float.valueOf(this.getFailedLaunch()) * 2.0F;
        	
            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else
        {
        	this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            
            if (this.launched && !reversed)
            {
            	if (Math.abs(Math.sin(timeSinceLaunch / 1000)) / 10 != 0.0)
                this.motionY += Math.abs(Math.sin(timeSinceLaunch / 1000)) / 20;
            }
            
            if (this.failedLaunch)
            {
            	if (this.timeSinceLaunch > 100)
            	{
            		this.setFailedLaunch(1);
            		this.motionX += 0.004D;
            		this.motionZ += 0.004D;
            	}
            	
            	if (this.timeSinceLaunch > 200)
            	{
            		this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 20, true);
            		
            		this.spawnParticlesExplosion();
            		
            		this.setDead();
            	}
            }
            
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY);
            int var3 = MathHelper.floor_double(this.posZ);

            if (BlockRail.isRailBlockAt(this.worldObj, var1, var2 - 1, var3))
            {
                --var2;
            }

            double var4 = 0.4D;
            double var6 = 0.0078125D;
            int var8 = this.worldObj.getBlockId(var1, var2, var3);
        }

        this.setRotation(this.rotationYaw, this.rotationPitch);
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.minecartX = par1;
        this.minecartY = par3;
        this.minecartZ = par5;
        this.minecartYaw = (double)par7;
        this.minecartPitch = (double)par8;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5)
    {
        this.velocityX = this.motionX = par1;
        this.velocityY = this.motionY = par3;
        this.velocityZ = this.motionZ = par5;
    }
    
    @SideOnly(Side.CLIENT)
    protected void spawnParticles(boolean launched)
    {
    	if (!this.isDead && (!this.failedLaunch || (this.failedLaunch && this.timeSinceLaunch <= 200)))
    	{
    		if (getLaunched() == 1)
    		{
    			if (this.riddenByEntity != null)
            	{
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4 - (this.rand.nextDouble() / 10), 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 100), this.posZ + 0.4 - (this.rand.nextDouble() / 10), 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4 + (this.rand.nextDouble() / 10), 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 100), this.posZ + 0.4 - (this.rand.nextDouble() / 10), 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4 + (this.rand.nextDouble() / 10), 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 100), this.posZ - 0.4 + (this.rand.nextDouble() / 10), 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4 - (this.rand.nextDouble() / 10), 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 100), this.posZ - 0.4 + (this.rand.nextDouble() / 10), 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX, 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 100), this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                	GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 200), this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4, this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 100), this.posZ, 		(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                    GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX + 0.5, this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 200), this.posZ, 		(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4, this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 100), this.posZ, 		(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                    GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX - 0.5, this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 200), this.posZ,			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX, 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 100), this.posZ + 0.4D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                    GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 200), this.posZ + 0.5D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX, 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 100), this.posZ - 0.4D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                    GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 		this.riddenByEntity.prevPosY - 1.8D - (this.timeSinceLaunch / 200), this.posZ - 0.5D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
            	}
            	else
            	{
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX, 		this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4 - (this.rand.nextDouble() / 10), 		this.posY - 1.8D, this.posZ + 0.4 - (this.rand.nextDouble() / 10), 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4 + (this.rand.nextDouble() / 10), 		this.posY - 1.8D, this.posZ + 0.4 - (this.rand.nextDouble() / 10), 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4 + (this.rand.nextDouble() / 10), 		this.posY - 1.8D, this.posZ - 0.4 + (this.rand.nextDouble() / 10), 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4 - (this.rand.nextDouble() / 10), 		this.posY - 1.8D, this.posZ - 0.4 + (this.rand.nextDouble() / 10), 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                	GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 		this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4, this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                    GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX + 0.5, this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4, this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                    GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX - 0.5, this.posY - 1.8D, this.posZ,			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX, 		this.posY - 1.8D, this.posZ + 0.4D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                    GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 		this.posY - 1.8D, this.posZ + 0.5D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX, 		this.posY - 1.8D, this.posZ - 0.4D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), true);
//                    GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 		this.posY - 1.8D, this.posZ - 0.5D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), launched);
            	}
    		}
    		else if (getLaunched() == 0)
    		{
            	GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX, 		this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
            	GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 		this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
                GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX + 0.5, this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
                GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX + 0.5, this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
                GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX - 0.5, this.posY - 1.8D, this.posZ, 			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
                GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX - 0.5, this.posY - 1.8D, this.posZ,			(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
                GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX, 		this.posY - 1.8D, this.posZ + 0.5D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
                GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 		this.posY - 1.8D, this.posZ + 0.5D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
                GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX, 		this.posY - 1.8D, this.posZ - 0.5D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
                GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 		this.posY - 1.8D, this.posZ - 0.5D, 	(this.rotationPitch / 360), -1D, (this.rotationPitch / 360), false);
    		}
    	}
    }
    
    @SideOnly(Side.CLIENT)
    protected void spawnParticlesExplosion()
    {
        GalacticraftCore.proxy.spawnParticle("hugeexplosion2", this.posX, this.posY, this.posZ, 0D, 0D, 0D, false);
    }
    
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	par1NBTTagCompound.setBoolean("launched", this.launched);
    	par1NBTTagCompound.setInteger("timeUntilLaunch", this.timeUntilLaunch);
    	par1NBTTagCompound.setInteger("ignite", this.ignite);
    	par1NBTTagCompound.setBoolean("reversed", this.reversed);
    	par1NBTTagCompound.setBoolean("failedlaunch", this.failedLaunch);
    }

    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
		this.launched = (Boolean)par1NBTTagCompound.getBoolean("launched");
		if ((Boolean)par1NBTTagCompound.getBoolean("launched"))
		{
			this.setLaunched(1);
		}
		else
		{
			this.setLaunched(0);
		}
		this.timeUntilLaunch = par1NBTTagCompound.getInteger("timeUntilLaunch");
		this.ignite = par1NBTTagCompound.getInteger("ignite");
		this.reversed = par1NBTTagCompound.getBoolean("reversed");
		this.failedLaunch = par1NBTTagCompound.getBoolean("failedlaunch");
    }

    public boolean interact(EntityPlayer par1EntityPlayer)
    {
    	if (!this.worldObj.isRemote)
    	{
        	par1EntityPlayer.mountEntity(this);
            this.timeSinceEntityEntry = 20;
            this.updateFailChance(par1EntityPlayer);
        	return true;
    	}
        return false;
    }

    public boolean canBeRidden()
    {
        return false;
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.isDead ? false : par1EntityPlayer.getDistanceSqToEntity(this) <= 64.0D;
    }
    
    public void setDamage(int par1)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(par1));
    }

    public int getDamage()
    {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    public void func_70497_h(int par1)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    public int func_70496_j()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void func_70494_i(int par1)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    public int func_70493_k()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public void setFailedLaunch(int par1)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(par1));
    }

    public int getFailedLaunch()
    {
        return this.dataWatcher.getWatchableObjectInt(20);
    }
    
    public void setLaunched(int par1)
    {
    	this.dataWatcher.updateObject(22, par1);
    }
    
    public int getLaunched()
    {
    	return this.dataWatcher.getWatchableObjectInt(22);
    }
    
    public void ignite()
    {
    	if (this.ignite == 0)
    	{
        	this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "shuttle.sound", 1F, 1.4F);
    	}
    	this.ignite = 1;
    }
    
    public double getMountedYOffset()
    {
        return -1D;
    }
    
    public void teleport()
    {
    	if (this.riddenByEntity != null)
    	{
    		if (this.riddenByEntity instanceof EntityPlayerMP)
            {
        		EntityPlayerMP entityplayermp = (EntityPlayerMP)this.riddenByEntity;
        		

	            for (int j = 0; j < GalacticraftCore.instance.gcPlayers.size(); ++j)
	            {
	    			GCCoreEntityPlayer playerBase = (GCCoreEntityPlayer) GalacticraftCore.instance.gcPlayers.get(j);
	    			
	    			if (entityplayermp.username == playerBase.getPlayer().username)
	    			{
	    				GalacticraftCore.proxy.displayChoosePlanetGui();
	    				if (this.riddenByEntity != null)
	    				{
		            		riddenByEntity.mountEntity(this);
	    				}
	    			}
	            }
            }
    	}
    }
    
    protected void updateFailChance(EntityPlayer player)
    {
    	if (this.rand.nextInt(100) < GCCoreUtil.getSpaceshipFailChance(player))
    	{
    		this.failedLaunch = true;
    	}
    	else
    	{
    		this.failedLaunch = false;
    	}
    }
}
