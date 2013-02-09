package micdoodle8.mods.galacticraft.core.entities;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.GCCoreDamageSource;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.sounds.GCCoreSoundUpdaterSpaceship;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.material.Material;
import net.minecraft.client.multiplayer.WorldClient;
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
import cpw.mods.fml.common.network.PacketDispatcher;
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
    protected ItemStack[] cargoItems;
	
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
    
    protected final IUpdatePlayerListBox rocketSoundUpdater;
	
	private boolean hasDroppedItem;

    public GCCoreEntitySpaceship(World par1World)
    {
        super(par1World);
        this.cargoItems = new ItemStack[36];
        this.fuel = 0;
        this.hasDroppedItem = false;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 4F);
        this.yOffset = this.height / 2.0F;
        this.rocketSoundUpdater = par1World != null ? par1World instanceof WorldClient ? new GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager, this, FMLClientHandler.instance().getClient().thePlayer) : null : null;
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
	@SideOnly(Side.CLIENT)
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
    		
    		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && ClientProxyCore.GCKeyHandler.openSpaceshipInv.pressed)
    		{
    			if (getSizeInventory() > 0)
    	        {
	            	if (FMLClientHandler.instance().getClient().currentScreen == null)
	            	{
    	                ((EntityPlayer) this.riddenByEntity).displayGUIChest(this);
                        final Object[] toSend = {0};
    	                PacketDispatcher.sendPacketToServer(GCCoreUtil.createPacket("Galacticraft", 6, toSend));
	            	}
    	        }
    		}

        	if (FMLClientHandler.instance().getClient().gameSettings.keyBindRight.pressed)
        	{
        		this.rotationYaw -= 0.9F;
        	}
        	
        	if (FMLClientHandler.instance().getClient().gameSettings.keyBindLeft.pressed)
        	{
        		this.rotationYaw += 0.9F;
        	}
    		
    		if (this.getLaunched() == 1)
    		{
            	if (FMLClientHandler.instance().getClient().gameSettings.keyBindForward.pressed)
            	{
            		this.rotationPitch -= 0.4F;
            	}
            	
            	if (FMLClientHandler.instance().getClient().gameSettings.keyBindBack.pressed)
            	{
            		this.rotationPitch += 0.4F;
            	}
    		}
    		else if (this.getReversed() == 0)
    		{
    			this.rotationPitch = 0F;
    		}
    	}
    	else if (this.getReversed() == 0)
    	{
    		this.rotationPitch = 0F;
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
        }
        
        int i;
        
        if (this.timeUntilLaunch >= 100)
        {
            i = Math.abs(this.timeUntilLaunch / 100);
        }
        else
        {
        	i = 1;
        }
        
        if (this.ignite == 1 || this.launched)
        {
            this.performHurtAnimation();
            
        	this.rumble = (float) this.rand.nextInt(3) - 3;
        }
        

        if (this.getLaunched() == 1 || this.rand.nextInt(i) == 0)
        {
        	this.spawnParticles(this.getLaunched() == 1);
        }
        
        if (this.launched && !this.reversed)
        {
        	if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
        		this.motionY += Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
        }
        
        this.motionX = -(this.motionY * Math.sin(this.rotationPitch * Math.PI / 180.0D) * Math.cos(this.rotationYaw * Math.PI / 180.0D));
        this.motionZ = -(this.motionY * Math.sin(this.rotationPitch * Math.PI / 180.0D) * Math.sin(this.rotationYaw * Math.PI / 180.0D));
        
        if (this.getReversed() == 0 && (this.rotationPitch > 70F || this.rotationPitch < -70F))
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
	      		this.failRocket();
	      	}
      	}
        
        if (this.getFailedLaunch() == 1)
        {
      		this.rotationYaw += (-100 + this.getTimeSinceLaunch()) / 5;
      		this.rotationPitch += (-100 + this.getTimeSinceLaunch()) / 50;
        }

        if (this.worldObj.isRemote)
        {
            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
        }
    }
    
    private void failRocket()
    {
    	if (this.riddenByEntity != null)
    	{
            final double var13 = this.riddenByEntity.getDistance(this.posX, this.posY, this.posZ) / 20;
    		this.riddenByEntity.attackEntityFrom(GCCoreDamageSource.spaceshipExplosion, (int)(4.0D * 20 + 1.0D));
    	}
        
  		this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 20, true);
  		
  		this.spawnParticlesExplosion();
  		
  		this.setDead();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.minecartX = par1;
        this.minecartY = par3;
        this.minecartZ = par5;
        this.minecartYaw = par7;
        this.minecartPitch = par8;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @Override
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
    	final double x1 = 2D * Math.sin(this.rotationPitch * 1.5D * Math.PI / 180.0D) * Math.cos(this.rotationYaw * Math.PI / 180.0D);
    	final double z1 = 2D * Math.sin(this.rotationPitch * 1.5D * Math.PI / 180.0D) * Math.sin(this.rotationYaw * Math.PI / 180.0D);
    	final double y1 = 4D * Math.sin(this.rotationPitch * Math.PI / 180.0D) + (this.getReversed() == 1 ? 10D : 0D);
    	
    	if (!this.worldObj.isRemote && !this.isDead)
    	{
    		if (this.getLaunched() == 1)
    		{
    			if (this.riddenByEntity != null)
            	{
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, 					this.riddenByEntity.prevPosY - 0.0D - this.timeSinceLaunch / 50 + y1, 	this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, 					this.riddenByEntity.prevPosY - 0.0D - this.timeSinceLaunch / 50 + y1, 	this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, 					this.riddenByEntity.prevPosY - 0.0D - this.timeSinceLaunch / 50 + y1, 	this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, 					this.riddenByEntity.prevPosY - 0.0D - this.timeSinceLaunch / 50 + y1,	this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + x1, 														this.riddenByEntity.prevPosY - 0.0D - this.timeSinceLaunch / 50 + y1, 	this.posZ + z1, 										-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4 + x1, 													this.riddenByEntity.prevPosY - 0.0D - this.timeSinceLaunch / 50 + y1, 	this.posZ + z1, 										-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4 + x1, 													this.riddenByEntity.prevPosY - 0.0D - this.timeSinceLaunch / 50 + y1, 	this.posZ + z1, 										-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + x1, 														this.riddenByEntity.prevPosY - 0.0D - this.timeSinceLaunch / 50 + y1, 	this.posZ + 0.4D + z1, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + Math.sin(this.rotationPitch * Math.PI / 180.0D) + x1, 	this.riddenByEntity.prevPosY - 0.0D - this.timeSinceLaunch / 50 + y1, 	this.posZ - 0.4D + z1, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
            	}
            	else
            	{
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX, 											this.posY - 0.8D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10, 	this.posY - 0.8D + y1, 													this.posZ + 0.4 - this.rand.nextDouble() / 10, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10, 	this.posY - 0.8D + y1, 													this.posZ + 0.4 - this.rand.nextDouble() / 10, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10, 	this.posY - 0.8D + y1, 													this.posZ - 0.4 + this.rand.nextDouble() / 10, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                	GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10, 	this.posY - 0.8D + y1, 													this.posZ - 0.4 + this.rand.nextDouble() / 10, 	-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX + 0.4, 									this.posY - 0.8D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX - 0.4, 									this.posY - 0.8D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX, 											this.posY - 0.8D + y1, 													this.posZ + 0.4D, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
                    GalacticraftCore.proxy.spawnParticle("launchflame", 	this.posX, 											this.posY - 0.8D + y1, 													this.posZ - 0.4D, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, true);
            	}
    		}
    		else if (this.getLaunched() == 0)
    		{
            	GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX, 											this.posY - 0.0D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
            	GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 											this.posY - 0.0D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
                GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX + 0.5, 									this.posY - 0.0D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
                GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX + 0.5, 									this.posY - 0.0D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
                GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX - 0.5, 									this.posY - 0.0D + y1, 													this.posZ, 											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
                GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX - 0.5, 									this.posY - 0.0D + y1, 													this.posZ,											-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
                GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX, 											this.posY - 0.0D + y1, 													this.posZ + 0.5D, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
                GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 											this.posY - 0.0D + y1, 													this.posZ + 0.5D, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
                GalacticraftCore.proxy.spawnParticle("whitesmokelarge", 	this.posX, 											this.posY - 0.0D + y1, 													this.posZ - 0.5D, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
                GalacticraftCore.proxy.spawnParticle("whitesmoke", 			this.posX, 											this.posY - 0.0D + y1, 													this.posZ - 0.5D, 									-this.motionX, this.getReversed() == 1 ? 1D : -1D, -this.motionZ, false);
    		}
    	}
    }
    
    @SideOnly(Side.CLIENT)
    protected void spawnParticlesExplosion()
    {
        GalacticraftCore.proxy.spawnParticle("hugeexplosion2", this.posX, this.posY, this.posZ, 0D, 0D, 0D, false);
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
            
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer)
            {
            	((EntityPlayer)this.riddenByEntity).sendChatToPlayer("SPACE - Launch");
            	((EntityPlayer)this.riddenByEntity).sendChatToPlayer("A / D  - Turn left-right");
            	((EntityPlayer)this.riddenByEntity).sendChatToPlayer("W / S  - Turn up-down");
            	((EntityPlayer)this.riddenByEntity).sendChatToPlayer("T       - Inventory / Fuel");
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
	    			final GCCoreEntityPlayer playerBase = (GCCoreEntityPlayer) GalacticraftCore.gcPlayers.get(j);
	    			
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
    		this.failedLaunch = true;
    	}
    	else
    	{
    		this.failedLaunch = false;
    	}
    }

	@Override
	public int getSizeInventory() 
	{
		return this.getSpaceshipType() == 0 ? 0 : 27;
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
}