package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.EnumGas;
import mekanism.api.IGasAcceptor;
import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreTileEntityOxygenCompressor extends TileEntity implements IInventory, IGasAcceptor, ITubeConnection
{
    public int currentPower;
    
    public boolean active;
    
    /** 
     * Since this is a modulo calculation, the maximum is lower than the minimum speed
     */
    public int MAX_COMPRESSION_SPEED = 20;
    
    /** 
     * Since this is a modulo calculation, the maximum is lower than the minimum speed
     */
    public int MIN_COMPRESSION_SPEED = 400;

	private ItemStack[] compressorStacks = new ItemStack[1];

    public double getDistanceFrom2(double par1, double par3, double par5)
    {
        final double var7 = this.xCoord + 0.5D - par1;
        final double var9 = this.yCoord + 0.5D - par3;
        final double var11 = this.zCoord + 0.5D - par5;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.currentPower < 1)
		{
			this.active = false;
		}
		else
		{
			this.active = true;
		}

		if (this.active)
		{
			if (!this.worldObj.isRemote && GalacticraftCore.tick % ((31 - Math.min(Math.floor(this.currentPower), 30)) * 5) == 0)
			{
				final ItemStack stack = this.getStackInSlot(0);

				if (stack != null && stack.getItemDamage() > 0)
				{
					stack.setItemDamage(stack.getItemDamage() - 1);
				}
				else if (stack != null)
				{
					stack.setItemDamage(0);
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.currentPower = par1NBTTagCompound.getInteger("currentPower");

        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.compressorStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            final byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.compressorStacks.length)
            {
                this.compressorStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("currentPower", this.currentPower);

        final NBTTagList list = new NBTTagList();

        for (int var3 = 0; var3 < this.compressorStacks.length; ++var3)
        {
            if (this.compressorStacks[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.compressorStacks[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", list);
	}

	@Override
    public int getSizeInventory()
    {
        return this.compressorStacks.length;
    }

	@Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.compressorStacks[par1];
    }

	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.compressorStacks[par1] != null)
        {
            ItemStack var3;

            if (this.compressorStacks[par1].stackSize <= par2)
            {
                var3 = this.compressorStacks[par1];
                this.compressorStacks[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.compressorStacks[par1].splitStack(par2);

                if (this.compressorStacks[par1].stackSize == 0)
                {
                    this.compressorStacks[par1] = null;
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
        if (this.compressorStacks[par1] != null)
        {
            final ItemStack var2 = this.compressorStacks[par1];
            this.compressorStacks[par1] = null;
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
        this.compressorStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

	@Override
	public String getInvName()
	{
		return "Oxygen Compressor";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public int transferGasToAcceptor(int amount, EnumGas type)
	{
		if (type == EnumGas.OXYGEN)
		{
			int rejects = 0;
			int neededOxygen = this.getStackInSlot(0).getItemDamage();
			
			if (amount <= neededOxygen)
			{
				currentPower = amount;
			}
			else if (amount > neededOxygen)
			{
				currentPower = neededOxygen;
				rejects = amount - neededOxygen;
			}
			
			return rejects;
		}
		else
		{
			return amount;
		}
	}

	@Override
	public boolean canReceiveGas(ForgeDirection side, EnumGas type)
	{
		return this.getStackInSlot(0) != null && this.getStackInSlot(0).getItemDamage() > 0 && type == EnumGas.OXYGEN;
	}

	@Override
	public boolean canTubeConnect(ForgeDirection side) 
	{
		return (side != ForgeDirection.UP && side != ForgeDirection.DOWN);
	}
}