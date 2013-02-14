package micdoodle8.mods.galacticraft.core.entities;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.GCCoreDamageSource;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPad;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchFlameFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchSmokeFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityOxygenFX;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreEntitySpaceship extends Entity implements IInventory
{
    protected ItemStack[] cargoItems = new ItemStack[36];
	
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
    public int timeUntilLaunch;
    public boolean launched;
    
    public float timeSinceEntityEntry;
    public float timeSinceLaunch;
    
    public float rumble;
    
    protected boolean reversed;
    
    protected boolean failedLaunch;
	
    public IUpdatePlayerListBox rocketSoundUpdater;
    
	private boolean hasDroppedItem;

    public GCCoreEntitySpaceship(World par1World)
    {
        super(par1World);
        this.fuel = 0;
        this.hasDroppedItem = false;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 4F);
        this.yOffset = this.height / 2.0F;
    }

    @Override
	protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
	protected void entityInit()
    {
        this.dataWatcher.addObject(16, new Byte((byte)0));
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
        this.dataWatcher.addObject(21, new Integer(0));
        this.dataWatcher.addObject(22, new Integer(0));
        this.dataWatcher.addObject(23, new Integer(0));
        this.dataWatcher.addObject(24, new Integer(0));
        this.dataWatcher.addObject(25, new Integer(0));
    }

    @Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.boundingBox;
    }

    @Override
	public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    @Override
	public boolean canBePushed()
    {
        return false;
    }

    public GCCoreEntitySpaceship(World par1World, double par2, double par4, double par6, boolean reversed, int type)
    {
        this(par1World);
        this.setPosition(par2, par4 + this.yOffset, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
        this.reversed = reversed;
        
        if (reversed)
        {
        	this.rotationPitch += 0F;
        	this.motionY = -1.0D;
        }
    }

    public GCCoreEntitySpaceship(World par1World, double par2, double par4, double par6, boolean reversed, int type, ItemStack[] inv)
    {
        this(par1World, par2, par4, par6, reversed, type);
        this.cargoItems = inv;
    }

    @Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return true;
    }

    @Override
    public void performHurtAnimation()
    {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(5);
        this.setDamage(this.getDamage() + this.getDamage() * 10);
    }

    @Override
	public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    
    @Override
	public boolean shouldRiderSit()
    {
        return false;
    }

    @Override
    public void setDead()
    {
    	super.setDead();

        if (this.rocketSoundUpdater != null)
        {
            this.rocketSoundUpdater.update();
        }
    }
    
    @Override
	public void onUpdate()
    {
    	super.onUpdate();
        
        int i;
        
        if (this.timeUntilLaunch >= 100)
        {
            i = Math.abs(this.timeUntilLaunch / 100);
        }
        else
        {
        	i = 1;
        }
    	
        if (this.getLaunched() == 1 || this.rand.nextInt(i) == 0)
        {
        	if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        	{
            	this.spawnParticles(this.getLaunched() == 1);
        	}
        }
    	
        if (this.rocketSoundUpdater != null)
        {
            this.rocketSoundUpdater.update();
        }

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

                GCCoreUtil.createNewExplosion(this.worldObj, this, this.posX, this.posY, this.posZ, 6, false);
                
				if (this.worldObj.isRemote && !this.hasDroppedItem && this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && !((EntityPlayer) this.riddenByEntity).capabilities.isCreativeMode)
				{
					final EntityItem var14 = new EntityItem(this.worldObj, MathHelper.floor_double(this.riddenByEntity.posX + 0.5D), MathHelper.floor_double(this.riddenByEntity.posY + 1D), MathHelper.floor_double(this.riddenByEntity.posZ + 0.5D), new ItemStack(GCCoreItems.spaceship));

			        final float var15 = 0.05F;
			        var14.motionX = (float)this.rand.nextGaussian() * var15;
			        var14.motionY = (float)this.rand.nextGaussian() * var15 + 0.2F;
			        var14.motionZ = (float)this.rand.nextGaussian() * var15;
			        this.worldObj.spawnEntityInWorld(var14);
			        this.hasDroppedItem = true;
				}
				else if (this.riddenByEntity == null)
				{
					final EntityItem var14 = new EntityItem(this.worldObj, MathHelper.floor_double(this.posX + 0.5D), MathHelper.floor_double(this.posY + 1D), MathHelper.floor_double(this.posZ + 0.5D), new ItemStack(GCCoreItems.spaceship));

			        final float var15 = 0.05F;
			        var14.motionX = (float)this.rand.nextGaussian() * var15;
			        var14.motionY = (float)this.rand.nextGaussian() * var15 + 0.2F;
			        var14.motionZ = (float)this.rand.nextGaussian() * var15;
			        this.worldObj.spawnEntityInWorld(var14);
				}
                
    			this.setDead();
    		}
    	}
    	
    	if (this.riddenByEntity != null)
    	{
    		if (this.failedLaunch && this.timeSinceLaunch >= 100)
    		{
        		this.riddenByEntity.posX += this.rumble / 20F;
        		this.riddenByEntity.posZ += this.rumble / 20F;
    		}
    		else
    		{
        		this.riddenByEntity.posX += this.rumble / 30F;
        		this.riddenByEntity.posZ += this.rumble / 30F;
    		}
    		
    		EntityPlayer player = (EntityPlayer) this.riddenByEntity;
    	}
    	
    	if (this.getReversed() == 1)
    	{
    		this.rotationPitch = 180F;
    	}
    	
    	if (this.posY > 450D && !this.reversed)
    	{
    		this.teleport();
    	}
    	
    	if (this.getRollingAmplitude() > 0)
        {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
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
        	this.timeUntilLaunch = 20;
        }
        
        if (this.launched)
        {
        	this.timeSinceLaunch++;
        }
        else
        {
        	this.timeSinceLaunch = 0;
        }
        
        if (!this.worldObj.isRemote)
        {
            this.setTimeSinceLaunch((int)this.timeSinceLaunch);
        }
        
        if (this.timeUntilLaunch > 0 && this.ignite == 1)
        {
        	this.timeUntilLaunch --;
        }
        
        this.setTimeUntilLaunch(this.timeUntilLaunch);
        
        if (this.timeUntilLaunch == 0 && this.ignite == 1 || this.getReversed() == 1)
        {
        	this.launched = true;
        	this.setLaunched(1);
        	this.ignite = 0;
        	
        	if (!this.worldObj.isRemote)
        	{
        		int amountRemoved = 0;
        		
        		for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
        		{
            		for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
            		{
                		for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                		{
                			int id = this.worldObj.getBlockId(x, y, z);
                			Block block = Block.blocksList[id];
                			
                			if (block != null && block instanceof GCCoreBlockLandingPad)
                			{
                    			if (amountRemoved < 9);
                    			{
                    				this.worldObj.setBlock(x, y, z, 0);
                    				amountRemoved++;
                    			}
                			}
                		}
            		}
        		}
        		
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        	}
        }
        
        if (this.ignite == 1 || this.launched)
        {
            this.performHurtAnimation();
            
        	this.rumble = (float) this.rand.nextInt(3) - 3;
        }
        
        if (this.launched && !this.reversed && this.getStackInSlot(27) != null && this.getStackInSlot(27).getItem().itemID == GCCoreItems.rocketFuelBucket.itemID)
        {
        	double d = Math.abs(Math.sin(this.timeSinceLaunch));
        	
        	if (d != 0.0)
        	{
        		this.motionY = -d * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);
        	}
        }
        else if ((this.getStackInSlot(27) == null || this.getStackInSlot(27).getItem().itemID != GCCoreItems.rocketFuelBucket.itemID) && this.getLaunched() == 1)
        {
//      		this.rotationPitch = -180;
        	if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
        		this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
        }
        
        this.motionX = -(1.3 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D));
        this.motionZ = -(1.3 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D));
        
        if (this.getReversed() == 0 && (this.rotationPitch > 70F || this.rotationPitch < -70F))
        {
//        	this.failRocket(); TODO
        }
        
        if (this.timeSinceLaunch > 50 && this.onGround)
        {
        	this.failRocket();
        }
        
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        
        if (this.failedLaunch)
      	{
	      	if (this.timeSinceLaunch > 100)
	      	{
	      		this.setFailedLaunch(1);
	      	}
	      	
	      	if (this.timeSinceLaunch > 200)
	      	{
//	      		this.failRocket();
	      	}
      	}
        
//        if ((this.getFailedLaunch() == 1 || (this.getStackInSlot(27) == null || this.getStackInSlot(27).getItem().itemID != GCCoreItems.rocketFuelBucket.itemID)) && this.getLaunched() == 1)
//        {
//      		this.rotationYaw += 2;
//        }
        
        this.setRotation(this.rotationYaw, this.rotationPitch);

        if (this.worldObj.isRemote)
        {
            this.setPosition(this.posX, this.posY, this.posZ);
        }
        
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }
    
    public void turnYaw (float f)
    {
		this.rotationYaw += f;
    }
    
    public void turnPitch (float f)
    {
		this.rotationPitch += f;
    }
    
    private void failRocket()
    {
    	if (this.riddenByEntity != null)
    	{
            final double var13 = this.riddenByEntity.getDistance(this.posX, this.posY, this.posZ) / 20;
    		this.riddenByEntity.attackEntityFrom(GCCoreDamageSource.spaceshipExplosion, (int)(4.0D * 20 + 1.0D));
    	}
        
  		this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 20, true);
  		
  		this.setDead();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
    	this.setRotation(par7, par8);
//        this.minecartX = par1;
//        this.minecartY = par3;
//        this.minecartZ = par5;
//        this.minecartYaw = par7;
//        this.minecartPitch = par8;
//        this.motionX = this.velocityX;
//        this.motionY = this.velocityY;
//        this.motionZ = this.velocityZ;
//
//        this.rotationYaw = par7;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5)
    {
        this.velocityX = this.motionX = par1;
        this.velocityY = this.motionY = par3;
        this.velocityZ = this.motionZ = par5;
    }
    
    protected void spawnParticles(boolean launched)
    {
    	final double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
    	final double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
    	final double y1 = 2 * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D) + (this.getReversed() == 1 ? 10D : 0D);
    	
    	final double y = this.prevPosY + (this.posY - this.prevPosY);
    	
    	if (!this.isDead)
    	{
			if (this.riddenByEntity != null)
        	{
            	this.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, 					y - 0.0D + y1, 	this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, 	x1, y1, z1, true);
            	this.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, 					y - 0.0D + y1, 	this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, 	x1, y1, z1, true);
            	this.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, 					y - 0.0D + y1, 	this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, 	x1, y1, z1, true);
            	this.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, 					y - 0.0D + y1,	this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, 	x1, y1, z1, true);
            	this.spawnParticle("launchflame", 	this.posX + x1, 														y - 0.0D + y1, 	this.posZ + z1, 										x1, y1, z1, true);
            	this.spawnParticle("launchflame", 	this.posX + 0.4 + x1, 													y - 0.0D + y1, 	this.posZ + z1, 										x1, y1, z1, true);
            	this.spawnParticle("launchflame", 	this.posX - 0.4 + x1, 													y - 0.0D + y1, 	this.posZ + z1, 										x1, y1, z1, true);
            	this.spawnParticle("launchflame", 	this.posX + x1, 														y - 0.0D + y1, 	this.posZ + 0.4D + z1, 									x1, y1, z1, true);
            	this.spawnParticle("launchflame", 	this.posX + x1, 														y - 0.0D + y1, 	this.posZ - 0.4D + z1, 									x1, y1, z1, true);
        	}
        	else
        	{
            	this.spawnParticle("launchflame", 	this.posX, 											this.posY - 0.8D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
            	this.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10, 	this.posY - 0.8D + y1, 													this.posZ + 0.4 - this.rand.nextDouble() / 10, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
            	this.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10, 	this.posY - 0.8D + y1, 													this.posZ + 0.4 - this.rand.nextDouble() / 10, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
            	this.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10, 	this.posY - 0.8D + y1, 													this.posZ - 0.4 + this.rand.nextDouble() / 10, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
            	this.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10, 	this.posY - 0.8D + y1, 													this.posZ - 0.4 + this.rand.nextDouble() / 10, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                this.spawnParticle("launchflame", 	this.posX + 0.4, 									this.posY - 0.8D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                this.spawnParticle("launchflame", 	this.posX - 0.4, 									this.posY - 0.8D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                this.spawnParticle("launchflame", 	this.posX, 											this.posY - 0.8D + y1, 													this.posZ + 0.4D, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                this.spawnParticle("launchflame", 	this.posX, 											this.posY - 0.8D + y1, 													this.posZ - 0.4D, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
        	}
    	}
    }
    
    protected void spawnParticlesExplosion()
    {
        this.spawnParticle("hugeexplosion2", this.posX, this.posY, this.posZ, 0D, 0D, 0D, false);
    }
    
    @Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("Type", this.getSpaceshipType());
    	par1NBTTagCompound.setBoolean("launched", this.launched);
    	par1NBTTagCompound.setInteger("timeUntilLaunch", this.timeUntilLaunch);
    	par1NBTTagCompound.setInteger("ignite", this.ignite);
    	par1NBTTagCompound.setBoolean("reversed", this.reversed);
    	par1NBTTagCompound.setBoolean("failedlaunch", this.failedLaunch);

        if (getSizeInventory() > 0)
        {
            NBTTagList var2 = new NBTTagList();

            for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
            {
                if (this.cargoItems[var3] != null)
                {
                    NBTTagCompound var4 = new NBTTagCompound();
                    var4.setByte("Slot", (byte)var3);
                    this.cargoItems[var3].writeToNBT(var4);
                    var2.appendTag(var4);
                }
            }

            par1NBTTagCompound.setTag("Items", var2);
        }
    }

    @Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.setSpaceshipType(par1NBTTagCompound.getInteger("Type"));
		this.launched = par1NBTTagCompound.getBoolean("launched");
		if (par1NBTTagCompound.getBoolean("launched"))
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

        if (getSizeInventory() > 0)
        {
            NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
            this.cargoItems = new ItemStack[this.getSizeInventory()];

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
                int var5 = var4.getByte("Slot") & 255;

                if (var5 >= 0 && var5 < this.cargoItems.length)
                {
                    this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }
    }

    @Override
	public boolean interact(EntityPlayer par1EntityPlayer)
    {
    	if (!this.worldObj.isRemote)
    	{
        	par1EntityPlayer.mountEntity(this);
            this.timeSinceEntityEntry = 20;
            this.updateFailChance(par1EntityPlayer);
            
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayerMP)
            {
        	  	final Object[] toSend = {((EntityPlayerMP)this.riddenByEntity).username};
            	((EntityPlayerMP)this.riddenByEntity).playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 8, toSend));
            }
            
        	return true;
    	}
        return false;
    }

    public boolean canBeRidden()
    {
        return false;
    }

    @Override
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

    /**
     * Sets the rolling amplitude the cart rolls while being attacked.
     */
    public void setRollingAmplitude(int par1)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    /**
     * Gets the rolling amplitude the cart rolls while being attacked.
     */
    public int getRollingAmplitude()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Sets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public void setRollingDirection(int par1)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    /**
     * Gets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public int getRollingDirection()
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

    public int getReversed()
    {
        return this.dataWatcher.getWatchableObjectInt(21);
    }
    
    public void setLaunched(int par1)
    {
    	this.dataWatcher.updateObject(22, par1);
    }
    
    public int getLaunched()
    {
    	return this.dataWatcher.getWatchableObjectInt(22);
    }
    
    public void setTimeUntilLaunch(int par1)
    {
    	if (!this.worldObj.isRemote)
    	{
        	this.dataWatcher.updateObject(23, par1);
    	}
    }
    
    public int getTimeUntilLaunch()
    {
    	return this.dataWatcher.getWatchableObjectInt(23);
    }
    
    public void setTimeSinceLaunch(int par1)
    {
    	this.dataWatcher.updateObject(24, par1);
    }
    
    public int getTimeSinceLaunch()
    {
    	return this.dataWatcher.getWatchableObjectInt(24);
    }
    
    public void setSpaceshipType(int par1)
    {
    	this.dataWatcher.updateObject(25, par1);
    }
    
    public int getSpaceshipType()
    {
    	return this.dataWatcher.getWatchableObjectInt(25);
    }
    
    public void ignite()
    {
    	this.ignite = 1;
    }
    
    @Override
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
        		final EntityPlayerMP entityplayermp = (EntityPlayerMP)this.riddenByEntity;
        		
	            for (int j = 0; j < GalacticraftCore.gcPlayers.size(); ++j)
	            {
	    			final GCCorePlayerBase playerBase = (GCCorePlayerBase) GalacticraftCore.gcPlayers.get(j);
	    			
	    			if (entityplayermp.username.equals(playerBase.getPlayer().username))
	    			{
	    				final Integer[] ids = DimensionManager.getIDs();
	    		    	
	    		    	final Set set = GCCoreUtil.getArrayOfPossibleDimensions(ids).entrySet();
	    		    	final Iterator i = set.iterator();
	    		    	
	    		    	String temp = "";
	    		    	
	    		    	for (int k = 0; i.hasNext(); k++)
	    		    	{
	    		    		final Map.Entry entry = (Map.Entry)i.next();
	    		    		temp = k == 0 ? temp.concat(String.valueOf(entry.getKey())) : temp.concat("." + String.valueOf(entry.getKey()));
	    		    	}
	    		    	
	    		    	final Object[] toSend = {entityplayermp.username, temp};
	    		        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(entityplayermp.username).playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 2, toSend));
	    				
	    		        playerBase.rocketStacks = this.cargoItems;
	    		        playerBase.rocketType = this.getSpaceshipType();
	    		        
	    				if (this.riddenByEntity != null)
	    				{
		            		this.riddenByEntity.mountEntity(this);
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
    		this.failedLaunch = false; // TODO
    	}
    	else
    	{
    		this.failedLaunch = false;
    	}
    }

	@Override
	public int getSizeInventory() 
	{
		return 28;
	}

	@Override
	public ItemStack getStackInSlot(int par1)
    {
        return this.cargoItems[par1];
    }

	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.cargoItems[par1] != null)
        {
            ItemStack var3;

            if (this.cargoItems[par1].stackSize <= par2)
            {
                var3 = this.cargoItems[par1];
                this.cargoItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.cargoItems[par1].splitStack(par2);

                if (this.cargoItems[par1].stackSize == 0)
                {
                    this.cargoItems[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

	@Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.cargoItems[par1] != null)
        {
            ItemStack var2 = this.cargoItems[par1];
            this.cargoItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

	@Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.cargoItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

	@Override
    public String getInvName()
    {
        return "container.spaceship";
    }

	@Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

	@Override
    public void onInventoryChanged() {}

	@Override
	public void openChest() { }

	@Override
	public void closeChest() { }
	
	@SideOnly(Side.CLIENT)
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
	{
		this.spawnParticle(var1, var2, var4, var6, var8, var10, var12, 0.0D, 0.0D, 0.0D, b);
	}
	
	@SideOnly(Side.CLIENT)
	public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var13, double var14, double var15, boolean b)
    {
        final Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            final double var16 = mc.renderViewEntity.posX - var2;
            final double var17 = mc.renderViewEntity.posY - var4;
            final double var19 = mc.renderViewEntity.posZ - var6;
            Object var21 = null;
            final double var22 = 64.0D;
            
            if (var1.equals("whitesmoke"))
            {
        		final EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1.0F, b);
        		if (fx != null)
        		{
                	mc.effectRenderer.addEffect(fx);
        		}
            }
            else if (var1.equals("whitesmokelarge"))
            {
        		final EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F, b);
        		if (fx != null)
        		{
        			mc.effectRenderer.addEffect(fx);
        		}
        	}
            else if (var1.equals("launchflame"))
            {
        		final EntityFX fx = new GCCoreEntityLaunchFlameFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1F);
        		if (fx != null)
        		{
        			mc.effectRenderer.addEffect(fx);
        		}
            }
            else if (var1.equals("distancesmoke") && var16 * var16 + var17 * var17 + var19 * var19 < var22 * var22 * 1.7)
            {
            	final EntityFX fx = new EntitySmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F);
        		if (fx != null)
        		{
        			mc.effectRenderer.addEffect(fx);
        		}
            }

            if (var16 * var16 + var17 * var17 + var19 * var19 < var22 * var22)
            {
            	if (var1.equals("oxygen"))
            	{
                    var21 = new GCCoreEntityOxygenFX(mc.theWorld, var2, var4, var6, var8, var10, var12);
                    ((EntityFX)var21).setRBGColorF((float)var13, (float)var14, (float)var15);
            	}
            }
            
            if (var21 != null)
            {
                ((EntityFX)var21).prevPosX = ((EntityFX)var21).posX;
                ((EntityFX)var21).prevPosY = ((EntityFX)var21).posY;
                ((EntityFX)var21).prevPosZ = ((EntityFX)var21).posZ;
                mc.effectRenderer.addEffect((EntityFX)var21);
            }
        }
    }
}