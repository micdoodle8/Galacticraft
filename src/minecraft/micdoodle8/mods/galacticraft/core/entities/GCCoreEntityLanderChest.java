package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;

public class GCCoreEntityLanderChest extends Entity implements IInventory
{
	public ItemStack[] chestContents = new ItemStack[27];
    public int numUsingPlayers;
    private GCCoreEntityLander mainLander;

	public GCCoreEntityLanderChest(World world)
	{
		super(world);
		this.ignoreFrustumCheck = true;
		this.setSize(20.0F, 20.0F);
	}

	public GCCoreEntityLanderChest(GCCoreEntityLander lander)
	{
		this(lander.worldObj);
		this.mainLander = lander;
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
	}

    public int getSizeInventory()
    {
        return this.chestContents.length;
    }

    public ItemStack getStackInSlot(int par1)
    {
        return this.chestContents[par1];
    }

    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack;

            if (this.chestContents[par1].stackSize <= par2)
            {
                itemstack = this.chestContents[par1];
                this.chestContents[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.chestContents[par1].splitStack(par2);

                if (this.chestContents[par1].stackSize == 0)
                {
                    this.chestContents[par1] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.chestContents[par1] != null)
        {
            final ItemStack itemstack = this.chestContents[par1];
            this.chestContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.chestContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    public String getInvName()
    {
        return "container.chest";
    }

    public boolean isInvNameLocalized()
    {
        return false;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64.0D;
    }


    public void openChest()
    {
        if (this.numUsingPlayers < 0)
        {
            this.numUsingPlayers = 0;
        }

        ++this.numUsingPlayers;
    }

    public void closeChest()
    {
        --this.numUsingPlayers;
    }

    public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

	@Override
	public void onInventoryChanged()
	{
	}

    @Override
	public boolean interact(EntityPlayer var1)
    {
    	FMLLog.info("merp");

        if (this.worldObj.isRemote)
        {
            return true;
        }
        else
        {
        	var1.displayGUIChest(this);
            return true;
        }
    }
}
