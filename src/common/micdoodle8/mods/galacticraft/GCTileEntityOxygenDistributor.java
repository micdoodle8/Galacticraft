package micdoodle8.mods.galacticraft;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.StatList;
import net.minecraft.src.TileEntity;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCTileEntityOxygenDistributor extends TileEntity implements IInventory 
{
    private ItemStack[] distributorStacks;

    public GCTileEntityOxygenDistributor()
    {
    	distributorStacks = new ItemStack[3];
    }
    
    public void onInventoryChanged()
    {
    	if (this.distributorStacks[0] != null)
    	{
        	Item tank = this.distributorStacks[0].getItem();
        	
        	if (tank == GCItems.heavyOxygenTankFull || tank == GCItems.medOxygenTankFull || tank == GCItems.lightOxygenTankFull)
        	{
        		GCBlockOxygenDistributor.updateDistributorState(true, worldObj, xCoord, yCoord, zCoord);
        	}
        	else
        	{
        		GCBlockOxygenDistributor.updateDistributorState(false, worldObj, xCoord, yCoord, zCoord);
        	}
    	}
    	else if (this.distributorStacks[0] == null)
    	{
    		GCBlockOxygenDistributor.updateDistributorState(false, worldObj, xCoord, yCoord, zCoord);
    	}
    }
    
	@Override
	public void updateEntity() 
	{
		super.updateEntity();

		ItemStack tankInSlot = this.getStackInSlot(0);
		
		if (tankInSlot != null)
		{
			int drainSpacing = GCUtil.getDrainSpacing(tankInSlot);
			
			if (drainSpacing > 0 && Galacticraft.instance.tick % (MathHelper.floor_double(drainSpacing / 4)) == 0) 
	    	{
	            if (tankInSlot.getItemDamage() < tankInSlot.getMaxDamage() - 2)
	            {
		    		tankInSlot.damageItem(1, null);
	            }
	            else
	            {
	            	this.distributorStacks[0] = null;
	            }
	    	}
		}
		
		
		if (this.worldObj.getBlockId(xCoord, yCoord, zCoord) == GCBlocks.airDistributorActive.blockID)
		{
			if (this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord + 1, this.yCoord, this.zCoord, GCBlocks.breatheableAir.blockID);
			}

			if (this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord - 1, this.yCoord, this.zCoord, GCBlocks.breatheableAir.blockID);
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord + 1, GCBlocks.breatheableAir.blockID);
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord - 1, GCBlocks.breatheableAir.blockID);
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord + 1, this.zCoord, GCBlocks.breatheableAir.blockID);
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord - 1, this.zCoord, GCBlocks.breatheableAir.blockID);
			}
			
			for (int j = -5; j < 6; j++)
			{
				for (int i = -5; i < 6; i++)
				{
					for (int k = -5; k < 6; k++)
					{
						if (this.worldObj.getBlockId(this.xCoord + i, this.yCoord + j, this.zCoord + k) == GCBlocks.breatheableAir.blockID)
						{
							this.worldObj.scheduleBlockUpdate(this.xCoord + i, this.yCoord + j, this.zCoord + k, GCBlocks.breatheableAir.blockID, GCBlocks.breatheableAir.tickRate());
						}
						else if (this.worldObj.getBlockId(this.xCoord + i, this.yCoord + j, this.zCoord + k) == GCBlocks.unlitTorch.blockID)
						{
							int meta = this.worldObj.getBlockMetadata(this.xCoord + i, this.yCoord + j, this.zCoord + k);
							this.worldObj.setBlockAndMetadataWithNotify(this.xCoord + i, this.yCoord + j, this.zCoord + k, GCBlocks.unlitTorchLit.blockID, meta);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.readFromNBT(par1NBTTagCompound);
		
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.distributorStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.distributorStacks.length)
            {
                this.distributorStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.writeToNBT(par1NBTTagCompound);

        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.distributorStacks.length; ++var3)
        {
            if (this.distributorStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.distributorStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
	}
   
    @Override
    public int getSizeInventory() 
    {
    	return distributorStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) 
    {
    	return distributorStacks[slot];
    }
   
    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) 
    {
    	distributorStacks[slot] = stack;
    	
        if (stack != null && stack.stackSize > getInventoryStackLimit()) 
        {
            stack.stackSize = getInventoryStackLimit();
        }              

        this.onInventoryChanged();
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) 
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) 
        {
            if (stack.stackSize <= amount)
            {
                setInventorySlotContents(slot, null);
            } 
            else 
            {
                stack = stack.splitStack(amount);
                if (stack.stackSize == 0) 
                {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        
        this.onInventoryChanged();
        
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) 
        {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }
   
    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) 
    {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {}

    @Override
    public void closeChest() {}

	@Override
	public String getInvName() 
	{
		return "container.airdistributor";
	}
}