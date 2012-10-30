package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreTileEntityOxygenDistributor extends TileEntity implements IInventory 
{
    private ItemStack[] distributorStacks;
    
    public double currentPower;

    public GCCoreTileEntityOxygenDistributor()
    {
    	distributorStacks = new ItemStack[3];
    }
    
    @Override
	public void onInventoryChanged()
    {
    	if (this.distributorStacks[0] != null)
    	{
        	Item tank = this.distributorStacks[0].getItem();
        	
        	if (tank == GCCoreItems.heavyOxygenTankFull || tank == GCCoreItems.medOxygenTankFull || tank == GCCoreItems.lightOxygenTankFull)
        	{
        		GCCoreBlockOxygenDistributor.updateDistributorState(true, worldObj, xCoord, yCoord, zCoord);
        	}
        	else
        	{
        		GCCoreBlockOxygenDistributor.updateDistributorState(false, worldObj, xCoord, yCoord, zCoord);
        	}
    	}
    	else if (this.distributorStacks[0] == null || this.currentPower < 1.0D)
    	{
    		GCCoreBlockOxygenDistributor.updateDistributorState(false, worldObj, xCoord, yCoord, zCoord);
    	}
    }
    
	@Override
	public void updateEntity() 
	{
		super.updateEntity();
		
		if (this.currentPower < 1.0D)
		{
    		GCCoreBlockOxygenDistributor.updateDistributorState(false, worldObj, xCoord, yCoord, zCoord);
		}
		
		int[] idSet = new int[6];
		
		idSet[0] = this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord);
		idSet[1] = this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord);
		idSet[2] = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1);
		idSet[3] = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1);
		idSet[4] = this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord);
		idSet[5] = this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord);
		
		TileEntity tile;

		for (int i = 0; i < idSet.length; i++)
		{
			if (idSet[0] == GCCoreBlocks.oxygenPipe.blockID)
			{
				tile = this.worldObj.getBlockTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
				if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
				{
					this.currentPower = ((GCCoreTileEntityOxygenPipe)tile).getOxygenInPipe();
				}
			}
			if (idSet[1] == GCCoreBlocks.oxygenPipe.blockID)
			{
				tile = this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
				if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
				{
					this.currentPower = ((GCCoreTileEntityOxygenPipe)tile).getOxygenInPipe();
				}
			}
			if (idSet[2] == GCCoreBlocks.oxygenPipe.blockID)
			{
				tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
				if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
				{
					this.currentPower = ((GCCoreTileEntityOxygenPipe)tile).getOxygenInPipe();
				}
			}
			if (idSet[3] == GCCoreBlocks.oxygenPipe.blockID)
			{
				tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
				if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
				{
					this.currentPower = ((GCCoreTileEntityOxygenPipe)tile).getOxygenInPipe();
				}
			}
			if (idSet[4] == GCCoreBlocks.oxygenPipe.blockID)
			{
				tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
				if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
				{
					this.currentPower = ((GCCoreTileEntityOxygenPipe)tile).getOxygenInPipe();
				}
			}
			if (idSet[5] == GCCoreBlocks.oxygenPipe.blockID)
			{
				tile = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
				if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
				{
					this.currentPower = ((GCCoreTileEntityOxygenPipe)tile).getOxygenInPipe();
				}
			}
		}
		
		if (this.currentPower > 1.0D)
		{
			GCCoreBlockOxygenDistributor.updateDistributorState(true, worldObj, xCoord, yCoord, zCoord);
		}

		ItemStack tankInSlot = this.getStackInSlot(0);
		
		if (tankInSlot != null)
		{
			int drainSpacing = GCCoreUtil.getDrainSpacing(tankInSlot);
			
			if (drainSpacing > 0 && GalacticraftCore.instance.tick % (MathHelper.floor_double(drainSpacing / 4)) == 0) 
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
		
		
		if (this.worldObj.getBlockId(xCoord, yCoord, zCoord) == GCCoreBlocks.airDistributorActive.blockID)
		{
			TileEntity tile2;
			
			if (this.worldObj.getBlockId(this.xCoord + 1, this.yCoord, this.zCoord) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord + 1, this.yCoord, this.zCoord, GCCoreBlocks.breatheableAir.blockID);
				
				tile2 = this.worldObj.getBlockTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
				
				if (tile2 != null && tile2 instanceof GCCoreTileEntityBreathableAir)
				{
					((GCCoreTileEntityBreathableAir)tile2).setDistributor(this);
				}
			}

			if (this.worldObj.getBlockId(this.xCoord - 1, this.yCoord, this.zCoord) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord - 1, this.yCoord, this.zCoord, GCCoreBlocks.breatheableAir.blockID);
				
				tile2 = this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
				
				if (tile2 != null && tile2 instanceof GCCoreTileEntityBreathableAir)
				{
					((GCCoreTileEntityBreathableAir)tile2).setDistributor(this);
				}
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord + 1) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord + 1, GCCoreBlocks.breatheableAir.blockID);
				
				tile2 = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
				
				if (tile2 != null && tile2 instanceof GCCoreTileEntityBreathableAir)
				{
					((GCCoreTileEntityBreathableAir)tile2).setDistributor(this);
				}
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord - 1) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord - 1, GCCoreBlocks.breatheableAir.blockID);
				
				tile2 = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
				
				if (tile2 != null && tile2 instanceof GCCoreTileEntityBreathableAir)
				{
					((GCCoreTileEntityBreathableAir)tile2).setDistributor(this);
				}
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord + 1, this.zCoord, GCCoreBlocks.breatheableAir.blockID);
				
				tile2 = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
				
				if (tile2 != null && tile2 instanceof GCCoreTileEntityBreathableAir)
				{
					((GCCoreTileEntityBreathableAir)tile2).setDistributor(this);
				}
			}

			if (this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord) == 0)
			{
				this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord - 1, this.zCoord, GCCoreBlocks.breatheableAir.blockID);
				
				tile2 = this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
				
				if (tile2 != null && tile2 instanceof GCCoreTileEntityBreathableAir)
				{
					((GCCoreTileEntityBreathableAir)tile2).setDistributor(this);
				}
			}
			
			int power = Math.min((int) Math.floor(this.currentPower / 3), 8);
			
			for (int j = -power; j <= power; j++)
			{
				for (int i = -power; i <= power; i++)
				{
					for (int k = -power; k <= power; k++)
					{
						if (this.worldObj.getBlockId(this.xCoord + i, this.yCoord + j, this.zCoord + k) == GCCoreBlocks.breatheableAir.blockID)
						{
							this.worldObj.scheduleBlockUpdate(this.xCoord + i, this.yCoord + j, this.zCoord + k, GCCoreBlocks.breatheableAir.blockID, GCCoreBlocks.breatheableAir.tickRate());
						}
						else if (this.worldObj.getBlockId(this.xCoord + i, this.yCoord + j, this.zCoord + k) == GCCoreBlocks.unlitTorch.blockID)
						{
							int meta = this.worldObj.getBlockMetadata(this.xCoord + i, this.yCoord + j, this.zCoord + k);
							this.worldObj.setBlockAndMetadataWithNotify(this.xCoord + i, this.yCoord + j, this.zCoord + k, GCCoreBlocks.unlitTorchLit.blockID, meta);
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