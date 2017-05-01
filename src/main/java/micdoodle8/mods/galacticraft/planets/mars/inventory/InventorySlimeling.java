package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventorySlimeling implements IInventoryDefaults
{
    private ItemStack[] stackList = new ItemStack[27 + 3];
    private EntitySlimeling slimeling;
    public Container currentContainer;

    public InventorySlimeling(EntitySlimeling slimeling)
    {
        this.slimeling = slimeling;
    }

    @Override
    public int getSizeInventory()
    {
        return this.stackList.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return par1 >= this.getSizeInventory() ? null : this.stackList[par1];
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("container.slimeling_inventory.name");
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (this.stackList[par1] != null)
        {
            final ItemStack var2 = this.stackList[par1];
            this.stackList[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    private void removeInventoryBagContents()
    {
        if (this.currentContainer instanceof ContainerSlimeling)
        {
        	ContainerSlimeling.removeSlots((ContainerSlimeling) this.currentContainer);
        }

        for (int i = 2; i < this.stackList.length; i++)
        {
            if (this.stackList[i] != null)
            {
                if (!this.slimeling.worldObj.isRemote)
                {
                    this.slimeling.entityDropItem(this.stackList[i], 0.5F);
                }

                this.stackList[i] = null;
            }
        }
    }
    
    
    
    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
    	if (this.stackList[par1] != null)
        {
            ItemStack var3;

            //It's a removal of the Slimeling Inventory Bag
            if (par1 == 1 && this.stackList[par1].stackSize <= par2)
            {
            	this.removeInventoryBagContents();
                var3 = this.stackList[par1];
                this.stackList[par1] = null;
                return var3;
            }
            else
            //Normal case of decrStackSize for a slot
            {
                var3 = this.stackList[par1].splitStack(par2);

                if (this.stackList[par1].stackSize == 0)
                {
                	//Not sure if this is necessary again, given the above?
                	if (par1 == 1)
                    {
                		this.removeInventoryBagContents();
                    }

                    this.stackList[par1] = null;
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
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        if (par1 == 1 && (par2ItemStack == null && this.stackList[par1] != null || !ItemStack.areItemStacksEqual(par2ItemStack, this.stackList[par1])))
        {
            ContainerSlimeling.addAdditionalSlots((ContainerSlimeling) this.currentContainer, this.slimeling, par2ItemStack);
        }

        this.stackList[par1] = par2ItemStack;

    }

    public void readFromNBT(NBTTagList tagList)
    {
        if (tagList == null || tagList.tagCount() <= 0)
        {
            return;
        }

        this.stackList = new ItemStack[this.stackList.length];

        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            final NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            final int j = nbttagcompound.getByte("Slot") & 255;
            final ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack != null)
            {
                this.stackList[j] = itemstack;
            }
        }
    }

    public NBTTagList writeToNBT(NBTTagList tagList)
    {
        NBTTagCompound nbttagcompound;

        for (int i = 0; i < this.stackList.length; ++i)
        {
            if (this.stackList[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                this.stackList[i].writeToNBT(nbttagcompound);
                tagList.appendTag(nbttagcompound);
            }
        }

        return tagList;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return !this.slimeling.isDead && par1EntityPlayer.getDistanceSqToEntity(this.slimeling) <= 64.0D;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }
}
