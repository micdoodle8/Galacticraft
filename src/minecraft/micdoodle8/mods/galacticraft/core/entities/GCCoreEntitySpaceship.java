package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import micdoodle8.mods.galacticraft.API.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchFlameFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchSmokeFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityOxygenFX;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreEntitySpaceship extends EntitySpaceshipBase implements IInventory
{
    protected ItemStack[] cargoItems = new ItemStack[36];
	
    public IUpdatePlayerListBox rocketSoundUpdater;
    
    private int type;

    public GCCoreEntitySpaceship(World par1World)
    {
    	super(par1World);
    }

    public GCCoreEntitySpaceship(World par1World, double par2, double par4, double par6, int type)
    {
        super(par1World);
        this.setPosition(par2, par4 + this.yOffset, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
    }

    public GCCoreEntitySpaceship(World par1World, double par2, double par4, double par6, boolean reversed, int type, ItemStack[] inv)
    {
        this(par1World, par2, par4, par6, type);
        this.cargoItems = inv;
    }

    @Override
	protected void entityInit()
    {
    	super.entityInit();
        this.dataWatcher.addObject(25, new Integer(0));
        this.setSpaceshipType(type);
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
    	
        if ((this.getLaunched() == 1 || this.rand.nextInt(i) == 0) && !GCCoreConfigManager.disableSpaceshipParticles)
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
        
        if (this.launched && this.getStackInSlot(27) != null && this.getStackInSlot(27).getItem().itemID == GCCoreItems.rocketFuelBucket.itemID)
        {
        	double d = this.timeSinceLaunch / 250;
        	
        	d = Math.min(d, 1);
        	
        	if (d != 0.0)
        	{
        		this.motionY = -d * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);
        	}
        	
        	if (this.timeSinceLaunch % 40 == 0)
        	{
        		this.getStackInSlot(27).setItemDamage(this.getStackInSlot(27).getItemDamage() + 1);
        	}
        }
        else if ((this.getStackInSlot(27) == null || this.getStackInSlot(27).getItem().itemID != GCCoreItems.rocketFuelBucket.itemID || this.getStackInSlot(27).getItemDamage() == this.getStackInSlot(27).getMaxDamage() - 1) && this.getLaunched() == 1)
        {
        	if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
        	{
        		this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
        	}
        }
    }
    
    public void onLaunch() 
    {
    	
    }
    
    public void onTeleport(EntityPlayerMP player) 
    {
        GCCoreUtil.getPlayerBaseServerFromPlayer(player).rocketStacks = this.cargoItems;
        GCCoreUtil.getPlayerBaseServerFromPlayer(player).rocketType = this.getSpaceshipType();
        GCCoreUtil.getPlayerBaseServerFromPlayer(player).fuelDamage = (this.getStackInSlot(27).itemID == GCCoreItems.rocketFuelBucket.itemID ? this.getStackInSlot(27).getItemDamage() : 0);
    }
    
    protected void spawnParticles(boolean launched)
    {
    	final double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
    	final double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
    	double y1 = 2 * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D) + (this.getReversed() == 1 ? 10D : 0D);
    	
    	if (this.getLaunched() == 0)
    	{
    		y1 -= 2D;
    	}
    	
    	final double y = this.prevPosY + (this.posY - this.prevPosY);
    	
    	if (!this.isDead)
    	{
        	this.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, 					y - 0.0D + y1, 	this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, 	x1, y1, z1, this.getLaunched() == 1);
        	this.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, 					y - 0.0D + y1, 	this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, 	x1, y1, z1, this.getLaunched() == 1);
        	this.spawnParticle("launchflame", 	this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, 					y - 0.0D + y1, 	this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, 	x1, y1, z1, this.getLaunched() == 1);
        	this.spawnParticle("launchflame", 	this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, 					y - 0.0D + y1,	this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, 	x1, y1, z1, this.getLaunched() == 1);
        	this.spawnParticle("launchflame", 	this.posX + x1, 														y - 0.0D + y1, 	this.posZ + z1, 										x1, y1, z1, this.getLaunched() == 1);
        	this.spawnParticle("launchflame", 	this.posX + 0.4 + x1, 													y - 0.0D + y1, 	this.posZ + z1, 										x1, y1, z1, this.getLaunched() == 1);
        	this.spawnParticle("launchflame", 	this.posX - 0.4 + x1, 													y - 0.0D + y1, 	this.posZ + z1, 										x1, y1, z1, this.getLaunched() == 1);
        	this.spawnParticle("launchflame", 	this.posX + x1, 														y - 0.0D + y1, 	this.posZ + 0.4D + z1, 									x1, y1, z1, this.getLaunched() == 1);
        	this.spawnParticle("launchflame", 	this.posX + x1, 														y - 0.0D + y1, 	this.posZ - 0.4D + z1, 									x1, y1, z1, this.getLaunched() == 1);
        }
    }
    
    @Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.isDead ? false : par1EntityPlayer.getDistanceSqToEntity(this) <= 64.0D;
    }
    
	@Override
	public int getSizeInventory()
	{
		return 28;
	}
    
    @Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeEntityToNBT(par1NBTTagCompound);
    	
        par1NBTTagCompound.setInteger("Type", this.getSpaceshipType());

        if (this.getSizeInventory() > 0)
        {
            final NBTTagList var2 = new NBTTagList();

            for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
            {
                if (this.cargoItems[var3] != null)
                {
                    final NBTTagCompound var4 = new NBTTagCompound();
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
    	super.readEntityFromNBT(par1NBTTagCompound);
    	
        this.setSpaceshipType(par1NBTTagCompound.getInteger("Type"));

        if (this.getSizeInventory() > 0)
        {
            final NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
            this.cargoItems = new ItemStack[this.getSizeInventory()];

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                final NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
                final int var5 = var4.getByte("Slot") & 255;

                if (var5 >= 0 && var5 < this.cargoItems.length)
                {
                    this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }
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
            final ItemStack var2 = this.cargoItems[par1];
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
    
    public void setSpaceshipType(int par1)
    {
    	this.dataWatcher.updateObject(25, par1);
    }
    
    public int getSpaceshipType()
    {
    	return this.dataWatcher.getWatchableObjectInt(25);
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

	@Override
	public Entity[] getSpaceshipParts() 
	{
		return null;
	}

	@Override
	public HashSet<Integer> getPossiblePlanets() 
	{
		HashSet<Integer> dimensions = new HashSet<Integer>();
		dimensions.add(0);
		dimensions.add(GCMoonConfigManager.dimensionIDMoon);
		return dimensions;
	}

	@Override
	public int getYCoordToTeleport() 
	{
		return 420;
	}

	@Override
	public int getPreLaunchWait() 
	{
		return 400;
	}

	@Override
	public List<ItemStack> getItemsDropped() 
	{
        List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(GCCoreItems.spaceship, 1, this.getSpaceshipType()));
        
        for (int i = 0; i < this.cargoItems.length; i++)
        {
        	ItemStack item = this.cargoItems[i];
        	
        	if (item != null)
        	{
        		items.add(item);
        	}
        }

        return items;
	}
}