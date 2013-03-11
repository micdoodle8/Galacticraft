package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

/**
 * Base code by Aidancbrady
 * 
 * Implemented into Galacticraft by micdoodle8
 *
 */
public abstract class GCCoreTileEntityContainer extends GCCoreTileEntityBase implements ISidedInventory, IInventory
{
	public String fullName;
	public ItemStack[] stacks = new ItemStack[50];
	
	public GCCoreTileEntityContainer(String name)
	{
		fullName = name;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound nbtTags)
    {
        super.readFromNBT(nbtTags);
        
        NBTTagList tagList = nbtTags.getTagList("Items");
        
        stacks = new ItemStack[this.getSizeInventory()];

        for (int slots = 0; slots < tagList.tagCount(); ++slots)
        {
            NBTTagCompound tagCompound = (NBTTagCompound)tagList.tagAt(slots);
            byte slotID = tagCompound.getByte("Slot");

            if (slotID >= 0 && slotID < stacks.length)
            {
            	stacks[slotID] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }

	@Override
    public void writeToNBT(NBTTagCompound nbtTags)
    {
        super.writeToNBT(nbtTags);
        
        NBTTagList tagList = new NBTTagList();

        for (int slots = 0; slots < stacks.length; ++slots)
        {
            if (stacks[slots] != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte)slots);
                stacks[slots].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }

        nbtTags.setTag("Items", tagList);
    }
	
	@Override
	public int getStartInventorySide(ForgeDirection side) 
	{
        return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		return 0;
	}

	@Override
	public int getSizeInventory() 
	{
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) 
	{
		return stacks[par1];
	}

	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (stacks[par1] != null)
        {
            ItemStack var3;

            if (stacks[par1].stackSize <= par2)
            {
                var3 = stacks[par1];
                stacks[par1] = null;
                return var3;
            }
            else
            {
                var3 = stacks[par1].splitStack(par2);

                if (stacks[par1].stackSize == 0)
                {
                	stacks[par1] = null;
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
        if (stacks[par1] != null)
        {
            ItemStack var2 = stacks[par1];
            stacks[par1] = null;
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
		stacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit())
        {
            par2ItemStack.stackSize = getInventoryStackLimit();
        }
    }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false : entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}
	
	@Override
	public String getInvName()
	{
		return fullName;
	}
	
	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	
	@Override
	public void openChest()
	{
		playersUsing++;
	}

	@Override
	public void closeChest()
	{
		playersUsing--;
	}
}