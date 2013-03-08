package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import micdoodle8.mods.galacticraft.API.TileEntityOxygenAcceptor;
import micdoodle8.mods.galacticraft.API.TileEntityOxygenSource;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreTileEntityOxygenCompressor extends TileEntityOxygenAcceptor implements IInventory
{
    protected double currentPower;
    protected double lastPower;
    protected boolean active;
	private int indexFromCollector;

	private ItemStack[] compressorStacks = new ItemStack[1];

	private Set<TileEntityOxygenSource> sources = new HashSet<TileEntityOxygenSource>();

	@Override
	public void addSource(TileEntityOxygenSource source)
	{
		this.sources.add(source);
	}

	@Override
	public void removeSource(TileEntityOxygenSource source)
	{
		this.sources.remove(source);
	}

	@Override
	public void setSourceCollectors(Set<TileEntityOxygenSource> sources)
	{
		this.sources = sources;
	}

	@Override
	public Set<TileEntityOxygenSource> getSourceCollectors()
	{
		return this.sources;
	}

	@Override
	public void setIndexFromSource(int index)
	{
		this.indexFromCollector = index;
	}

	@Override
	public int getIndexFromSource()
	{
		return this.indexFromCollector;
	}

    @Override
	public boolean getActive()
	{
		return this.active;
	}

    @Override
	public void setActive(boolean active)
	{
		this.active = active;
	}

	@Override
	public double getPower()
	{
		return this.currentPower;
	}

	@Override
	public void setPower(double power)
	{
		this.currentPower = this.lastPower = power;
	}

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

		this.updateSourceList();

		for (int i = 0; i < ForgeDirection.values().length; i++)
    	{
			this.updateOxygenFromAdjacentPipe(ForgeDirection.getOrientation(i).offsetX, ForgeDirection.getOrientation(i).offsetY, ForgeDirection.getOrientation(i).offsetZ);
    	}

		if (!this.worldObj.isRemote && GalacticraftCore.tick % ((31 - Math.min(Math.floor(this.getPower()), 30)) * 5) == 0)
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

		if (this.currentPower < 1.0D)
		{
			this.active = false;
		}
		else
		{
			this.active = true;
		}

		this.lastPower = this.currentPower;
	}

	public void updateSourceList()
	{
		final ArrayList<TileEntityOxygenSource> sources = new ArrayList<TileEntityOxygenSource>();

		for (final TileEntityOxygenSource source : this.sources)
		{
			sources.add(source);
		}

		final ListIterator li = sources.listIterator();

		while (li.hasNext())
		{
			final TileEntityOxygenSource source = (TileEntityOxygenSource) li.next();

			if (!(this.worldObj.getBlockTileEntity(source.xCoord, source.yCoord, source.zCoord) instanceof TileEntityOxygenSource))
			{
				this.sources.remove(source);
			}
		}
	}

	public void updateOxygenFromAdjacentPipe(int xOffset, int yOffset, int zOffset)
	{
		final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset);

		if (tile != null && tile instanceof GCCoreTileEntityOxygenPipe)
		{
			final GCCoreTileEntityOxygenPipe pipe = (GCCoreTileEntityOxygenPipe) tile;

			this.currentPower = pipe.getOxygenInPipe();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.setActive(par1NBTTagCompound.getBoolean("active"));

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
		par1NBTTagCompound.setBoolean("active", this.getActive());

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
}