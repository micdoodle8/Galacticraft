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
		this.fullName = name;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound nbtTags)
    {
        super.readFromNBT(nbtTags);
        
        NBTTagList tagList = nbtTags.getTagList("Items");
        
        this.stacks = new ItemStack[this.getSizeInventory()];

        for (int slots = 0; slots < tagList.tagCount(); ++slots)
        {
            NBTTagCompound tagCompound = (NBTTagCompound)tagList.tagAt(slots);
            byte slotID = tagCompound.getByte("Slot");

            if (slotID >= 0 && slotID < this.stacks.length)
            {
            	this.stacks[slotID] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }

	@Override
    public void writeToNBT(NBTTagCompound nbtTags)
    {
        super.writeToNBT(nbtTags);
        
        NBTTagList tagList = new NBTTagList();

        for (int slots = 0; slots < this.stacks.length; ++slots)
        {
            if (this.stacks[slots] != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte)slots);
                this.stacks[slots].writeToNBT(tagCompound);
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
		return this.stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) 
	{
		return this.stacks[par1];
	}

	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.stacks[par1] != null)
        {
            ItemStack var3;

            if (this.stacks[par1].stackSize <= par2)
            {
                var3 = this.stacks[par1];
                this.stacks[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.stacks[par1].splitStack(par2);

                if (this.stacks[par1].stackSize == 0)
                {
                	this.stacks[par1] = null;
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
        if (this.stacks[par1] != null)
        {
            ItemStack var2 = this.stacks[par1];
            this.stacks[par1] = null;
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
		this.stacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}
	
	@Override
	public String getInvName()
	{
		return this.fullName;
	}
	
	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	
	@Override
	public void openChest()
	{
		this.playersUsing++;
	}

	@Override
	public void closeChest()
	{
		this.playersUsing--;
	}
}