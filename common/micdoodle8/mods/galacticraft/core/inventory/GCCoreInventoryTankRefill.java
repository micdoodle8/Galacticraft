//package micdoodle8.mods.galacticraft.core.inventory;
//
//import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.inventory.IInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//
///**
// * Copyright 2012-2013, micdoodle8
// *
// *  All rights reserved.
// *
// */
//public class GCCoreInventoryTankRefill implements IInventory
//{
//	public ItemStack[] tankSlotContents = new ItemStack[5];
//	private final GCCorePlayerMP player;
//
//	public GCCoreInventoryTankRefill()
//	{
//		this(null);
//	}
//
//	public GCCoreInventoryTankRefill(GCCorePlayerMP player)
//	{
//		this.player = player;
//	}
//
//	@Override
//	public int getInventoryStackLimit()
//	{
//		return 5;
//	}
//
//	@Override
//	public ItemStack getStackInSlot(int par1)
//	{
//		if (par1 < 5)
//			return this.tankSlotContents[par1];
//		else
//			return null;
//	}
//
//	@Override
//	public ItemStack getStackInSlotOnClosing(int par1)
//	{
//		if (this.tankSlotContents[par1] != null)
//		{
//			final ItemStack var2 = this.tankSlotContents[par1];
//			return var2;
//		}
//		else
//		{
//			return null;
//		}
//	}
//
//	public void consumeTankInSlot(int par1)
//	{
//		if (this.tankSlotContents[par1] != null)
//		{
//			this.tankSlotContents[par1] = null;
//		}
//	}
//
//	@Override
//	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
//	{
//		this.tankSlotContents[par1] = par2ItemStack;
//
//		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
//		{
//			par2ItemStack.stackSize = this.getInventoryStackLimit();
//		}
//
//		this.onInventoryChanged();
//	}
//
//	@Override
//	public ItemStack decrStackSize(int par1, int par2)
//	{
//		if (this.tankSlotContents[par1] != null)
//		{
//			ItemStack var3;
//
//			if (this.tankSlotContents[par1].stackSize <= par2)
//			{
//				var3 = this.tankSlotContents[par1];
//				this.tankSlotContents[par1] = null;
//				this.onInventoryChanged();
//				return var3;
//			}
//			else
//			{
//				var3 = this.tankSlotContents[par1].splitStack(par2);
//
//				if (this.tankSlotContents[par1].stackSize == 0)
//				{
//					this.tankSlotContents[par1] = null;
//				}
//
//				this.onInventoryChanged();
//				return var3;
//			}
//		}
//		else
//		{
//			return null;
//		}
//	}
//
//    public void dropAllItems()
//    {
//        for (int var1 = 0; var1 < this.tankSlotContents.length; ++var1)
//        {
//            if (this.tankSlotContents[var1] != null)
//            {
//            	if (this.player != null)
//            		this.player.dropPlayerItemWithRandomChoice(this.tankSlotContents[var1], true);
//                this.tankSlotContents[var1] = null;
//            }
//        }
//    }
//
//    public NBTTagList writeToNBT(NBTTagList par1NBTTagList)
//    {
//        int var2;
//        NBTTagCompound var3;
//
//        for (var2 = 0; var2 < this.tankSlotContents.length; ++var2)
//        {
//            if (this.tankSlotContents[var2] != null)
//            {
//                var3 = new NBTTagCompound();
//                var3.setByte("Slot", (byte)var2);
//                this.tankSlotContents[var2].writeToNBT(var3);
//                par1NBTTagList.appendTag(var3);
//            }
//        }
//
//        return par1NBTTagList;
//    }
//
//    public void readFromNBT(NBTTagList par1NBTTagList)
//    {
//        this.tankSlotContents = new ItemStack[5];
//
//        for (int var2 = 0; var2 < par1NBTTagList.tagCount(); ++var2)
//        {
//            final NBTTagCompound var3 = (NBTTagCompound)par1NBTTagList.tagAt(var2);
//            final int var4 = var3.getByte("Slot") & 255;
//            final ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);
//
//            if (var5 != null)
//            {
//                if (var4 >= 0 && var4 < this.tankSlotContents.length)
//                {
//                    this.tankSlotContents[var4] = var5;
//                }
//            }
//        }
//    }
//
//	@Override
//	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
//	{
//		return true;
//	}
//
//	@Override
//	public void openChest()
//	{
//	}
//
//	@Override
//	public void closeChest()
//	{
//	}
//
//	@Override
//	public int getSizeInventory()
//	{
//		return this.tankSlotContents.length;
//	}
//
//	@Override
//	public String getInvName()
//	{
//		return "Tank Holder";
//	}
//
//	@Override
//	public void onInventoryChanged()
//	{
//	}
//
//	@Override
//	public boolean isInvNameLocalized()
//	{
//		return true;
//	}
//
//	@Override
//	public boolean isStackValidForSlot(int i, ItemStack itemstack)
//	{
//		return false;
//	}
//}
