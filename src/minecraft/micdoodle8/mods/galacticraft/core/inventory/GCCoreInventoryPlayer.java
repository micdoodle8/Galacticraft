package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.common.FMLLog;

public class GCCoreInventoryPlayer extends InventoryPlayer
{
	public ItemStack[] tankSlotContents = new ItemStack[5];
	
	public GCCoreInventoryPlayer(EntityPlayer par1EntityPlayer) 
	{
		super(par1EntityPlayer);
	}
	
    public ItemStack tankItemInSlot(int par1)
    {
    	switch (par1)
    	{
    	default:
    		return this.tankSlotContents[par1];
    	}
    }

	@Override
    public int clearInventory(int par1, int par2)
    {
        int k = 0;
        int l;
        ItemStack itemstack;

        for (l = 0; l < this.mainInventory.length; ++l)
        {
            itemstack = this.mainInventory[l];

            if (itemstack != null && (par1 <= -1 || itemstack.itemID == par1) && (par2 <= -1 || itemstack.getItemDamage() == par2))
            {
                k += itemstack.stackSize;
                this.mainInventory[l] = null;
            }
        }

        for (l = 0; l < this.armorInventory.length; ++l)
        {
            itemstack = this.armorInventory[l];

            if (itemstack != null && (par1 <= -1 || itemstack.itemID == par1) && (par2 <= -1 || itemstack.getItemDamage() == par2))
            {
                k += itemstack.stackSize;
                this.armorInventory[l] = null;
            }
        }

        for (l = 0; l < this.tankSlotContents.length; ++l)
        {
            itemstack = this.tankSlotContents[l];

            if (itemstack != null && (par1 <= -1 || itemstack.itemID == par1) && (par2 <= -1 || itemstack.getItemDamage() == par2))
            {
                k += itemstack.stackSize;
                this.tankSlotContents[l] = null;
            }
        }

        return k;
    }

	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (par1 >= this.mainInventory.length)
        {
            if (par1 >= this.armorInventory.length + this.mainInventory.length)
            {
                aitemstack = this.tankSlotContents;
                par1 -= this.mainInventory.length + this.armorInventory.length;
            }
            else
            {
                aitemstack = this.armorInventory;
                par1 -= this.mainInventory.length;
            }
        }

        if (aitemstack[par1] != null)
        {
            ItemStack itemstack;

            if (aitemstack[par1].stackSize <= par2)
            {
                itemstack = aitemstack[par1];
                aitemstack[par1] = null;
                return itemstack;
            }
            else
            {
                itemstack = aitemstack[par1].splitStack(par2);

                if (aitemstack[par1].stackSize == 0)
                {
                    aitemstack[par1] = null;
                }

                return itemstack;
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
        ItemStack[] aitemstack = this.mainInventory;

        if (par1 >= this.mainInventory.length)
        {
            if (par1 >= this.armorInventory.length + this.mainInventory.length)
            {
                aitemstack = this.tankSlotContents;
                par1 -= this.mainInventory.length + this.armorInventory.length;
            }
            else
            {
                aitemstack = this.armorInventory;
                par1 -= this.mainInventory.length;
            }
        }

        if (aitemstack[par1] != null)
        {
            ItemStack itemstack = aitemstack[par1];
            aitemstack[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

	@Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (par1 >= aitemstack.length)
        {
            if (par1 >= this.armorInventory.length + this.mainInventory.length)
            {
                aitemstack = this.tankSlotContents;
                par1 -= this.mainInventory.length + this.armorInventory.length;
            }
            else
            {
                aitemstack = this.armorInventory;
                par1 -= this.mainInventory.length;
            }
        }

        aitemstack[par1] = par2ItemStack;
    }

	@Override
    public NBTTagList writeToNBT(NBTTagList par1NBTTagList)
    {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.mainInventory[i].writeToNBT(nbttagcompound);
                par1NBTTagList.appendTag(nbttagcompound);
            }
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)(i + 100));
                this.armorInventory[i].writeToNBT(nbttagcompound);
                par1NBTTagList.appendTag(nbttagcompound);
            }
        }

        for (i = 0; i < this.tankSlotContents.length; ++i)
        {
            if (this.tankSlotContents[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)(i + 200));
                this.tankSlotContents[i].writeToNBT(nbttagcompound);
                par1NBTTagList.appendTag(nbttagcompound);
            }
        }

        return par1NBTTagList;
    }

	@Override
    public void readFromNBT(NBTTagList par1NBTTagList)
    {
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];
    	this.tankSlotContents = new ItemStack[5];

        for (int i = 0; i < par1NBTTagList.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)par1NBTTagList.tagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack != null)
            {
                if (j >= 0 && j < this.mainInventory.length)
                {
                    this.mainInventory[j] = itemstack;
                }

                if (j >= 100 && j < this.armorInventory.length + 100)
                {
                    this.armorInventory[j - 100] = itemstack;
                }

                if (j >= 200 && j < this.tankSlotContents.length + 200)
                {
                    this.tankSlotContents[j - 200] = itemstack;
                }
            }
        }
    }
	
	public void readFromNBTOld(NBTTagList par1NBTTagList)
	{
		this.tankSlotContents = new ItemStack[5];

		for (int var2 = 0; var2 < par1NBTTagList.tagCount(); ++var2)
		{
			final NBTTagCompound var3 = (NBTTagCompound)par1NBTTagList.tagAt(var2);
			final int var4 = var3.getByte("Slot") & 255;
			final ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);

			if (var5 != null)
			{
				if (var4 >= 0 && var4 < this.tankSlotContents.length)
				{
					this.tankSlotContents[var4] = var5;
				}
			}
		}
	}

	@Override
    public int getSizeInventory()
    {
        return this.mainInventory.length + this.armorInventory.length + this.tankSlotContents.length;
    }

	@Override
    public ItemStack getStackInSlot(int par1)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (par1 >= aitemstack.length)
        {
            if (par1 >= this.armorInventory.length + this.mainInventory.length)
            {
                aitemstack = this.tankSlotContents;
                par1 -= this.mainInventory.length + this.armorInventory.length;
            }
            else
            {
                aitemstack = this.armorInventory;
                par1 -= this.mainInventory.length;
            }
        }

        return aitemstack[par1];
    }

	@Override
    public void dropAllItems()
    {
        int i;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null)
            {
                this.player.dropPlayerItemWithRandomChoice(this.mainInventory[i], true);
                this.mainInventory[i] = null;
            }
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null)
            {
                this.player.dropPlayerItemWithRandomChoice(this.armorInventory[i], true);
                this.armorInventory[i] = null;
            }
        }

        for (i = 0; i < this.tankSlotContents.length; ++i)
        {
            if (this.tankSlotContents[i] != null)
            {
                this.player.dropPlayerItemWithRandomChoice(this.tankSlotContents[i], true);
                this.tankSlotContents[i] = null;
            }
        }
    }

	@Override
    public boolean hasItemStack(ItemStack par1ItemStack)
    {
        int i;

        for (i = 0; i < this.tankSlotContents.length; ++i)
        {
            if (this.tankSlotContents[i] != null && this.tankSlotContents[i].isItemEqual(par1ItemStack))
            {
                return true;
            }
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null && this.armorInventory[i].isItemEqual(par1ItemStack))
            {
                return true;
            }
        }

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null && this.mainInventory[i].isItemEqual(par1ItemStack))
            {
                return true;
            }
        }

        return false;
    }

	@Override
    public void copyInventory(InventoryPlayer par1InventoryPlayer)
    {
        int i;

        for (i = 0; i < this.tankSlotContents.length; ++i)
        {
            this.tankSlotContents[i] = ItemStack.copyItemStack(((GCCoreInventoryPlayer) par1InventoryPlayer).tankSlotContents[i]);
        }

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            this.mainInventory[i] = ItemStack.copyItemStack(par1InventoryPlayer.mainInventory[i]);
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            this.armorInventory[i] = ItemStack.copyItemStack(par1InventoryPlayer.armorInventory[i]);
        }

        this.currentItem = par1InventoryPlayer.currentItem;
    }
}
