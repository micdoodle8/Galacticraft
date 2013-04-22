package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.API.IFuelTank;
import micdoodle8.mods.galacticraft.API.IFuelable;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemFuelCanister;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.TileEntityMulti;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

public class GCCoreTileEntityFuelLoader extends GCCoreTileEntityElectric implements IInventory, ISidedInventory, ITankContainer
{
	private final int tankCapacity = 12000;
	public LiquidTank fuelTank = new LiquidTank(this.tankCapacity);

	private ItemStack[] containingItems = new ItemStack[2];
	public static final double WATTS_PER_TICK = 300;
	
	public IFuelable attachedFuelable;

	public GCCoreTileEntityFuelLoader() 
	{
		super(300, 130, 1);
	}
	
	public int getScaledFuelLevel(int i)
	{
		final double fuelLevel = this.fuelTank.getLiquid() == null ? 0 : this.fuelTank.getLiquid().amount;

		return (int) (fuelLevel * i / this.tankCapacity);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.containingItems[1] != null)
			{
				final LiquidStack liquid = LiquidContainerRegistry.getLiquidForFilledItem(this.containingItems[1]);

				if (liquid != null && LiquidDictionary.findLiquidName(liquid).equals("Fuel"))
				{
					if (this.fuelTank.getLiquid() == null || this.fuelTank.getLiquid().amount + liquid.amount <= this.fuelTank.getCapacity())
					{
						this.fuelTank.fill(liquid, true);

						if(this.containingItems[1].getItem() instanceof GCCoreItemFuelCanister)
						{
							this.containingItems[1] = new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage());
						}
						else if (LiquidContainerRegistry.isBucket(this.containingItems[1]) && LiquidContainerRegistry.isFilledContainer(this.containingItems[1]))
						{
							final int amount = this.containingItems[1].stackSize;
							this.containingItems[1] = new ItemStack(Item.bucketEmpty, amount);
						}
						else
						{
							this.containingItems[1].stackSize--;

							if(this.containingItems[1].stackSize == 0)
							{
								this.containingItems[1] = null;
							}
						}
					}
				}
			}

			if (this.ticks % 100 == 0)
			{
				boolean foundFuelable = false;

				for (final ForgeDirection dir : ForgeDirection.values())
				{
					if (dir != ForgeDirection.UNKNOWN)
					{
						Vector3 vecAt = new Vector3(this);
						vecAt = vecAt.modifyPositionFromSide(dir);

						final TileEntity pad = vecAt.getTileEntity(this.worldObj);

						if (pad != null && pad instanceof TileEntityMulti)
						{
							final TileEntity mainTile = ((TileEntityMulti) pad).mainBlockPosition.getTileEntity(this.worldObj);

							if (mainTile != null && mainTile instanceof IFuelable)
							{
								this.attachedFuelable = (IFuelable) mainTile;
								foundFuelable = true;
								break;
							}
						}
						else if (pad != null && pad instanceof IFuelable)
						{
							this.attachedFuelable = (IFuelable) pad;
							foundFuelable = true;
							break;
						}
					}
				}

				if (!foundFuelable)
				{
					this.attachedFuelable = null;
				}
			}
			
			final LiquidStack liquid = LiquidDictionary.getLiquid("Fuel", 1);

			if (this.attachedFuelable != null && (this.ic2Energy > 0 || this.wattsReceived > 0) && !this.disabled)
			{
				if (liquid != null)
				{
					this.fuelTank.drain(this.attachedFuelable.addFuel(liquid, 1, true), true);
				}
			}
		}
	}

	@Override
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }

	@Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }

	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var3;

            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);

                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
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
        if (this.containingItems[par1] != null)
        {
            final ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
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
        this.containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            final byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

		if (par1NBTTagCompound.hasKey("fuelTank"))
		{
			this.fuelTank.readFromNBT(par1NBTTagCompound.getCompoundTag("fuelTank"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);

        final NBTTagList list = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", list);

		if (this.fuelTank.getLiquid() != null)
		{
			par1NBTTagCompound.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public String getInvName()
	{
		return "Fuel Loader";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	// ISidedInventory Implementation:

	@Override
	public int[] getSizeInventorySide(int side)
	{
		return side == 1 || side == 0 ? new int[] {0} : new int[] {1};
	}

	@Override
	public boolean func_102007_a(int slotID, ItemStack itemstack, int side)
	{
		return this.isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean func_102008_b(int slotID, ItemStack itemstack, int side)
	{
		return slotID == 1;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemstack)
	{
		return slotID == 1 ? itemstack.getItem() instanceof IFuelTank && itemstack.getMaxDamage() - itemstack.getItemDamage() != 0 && itemstack.getItemDamage() < itemstack.getMaxDamage()
				: slotID == 0 ? itemstack.getItem() instanceof IItemElectric : false;
	}

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill)
	{
		return this.fill(0, resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill)
	{
		int used = 0;
		final String liquidName = LiquidDictionary.findLiquidName(resource);

		if (tankIndex == 0 && liquidName != null && liquidName.equals("Fuel"))
		{
			used = this.fuelTank.fill(resource, doFill);
		}

		return used;
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return null;
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain)
	{
		return null;
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction)
	{
		return new ILiquidTank[] {this.fuelTank};
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type)
	{
		if (direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2))
		{
			return this.fuelTank;
		}

		return null;
	}

	@Override
	public boolean shouldPullEnergy() 
	{
		return this.fuelTank.getLiquid() != null && this.fuelTank.getLiquid().amount > 0 && !this.disabled;
	}

	@Override
	public void readPacket(ByteArrayDataInput data) 
	{
		if (this.worldObj.isRemote)
		{
			this.wattsReceived = data.readDouble();
			this.ic2Energy = data.readDouble();
			this.fuelTank.setLiquid(new LiquidStack(GCCoreItems.fuel.itemID, data.readInt(), 0));
			this.disabled = data.readBoolean();
			this.disableCooldown = data.readInt();
		}
	}

	@Override
	public Packet getPacket() 
	{
		return PacketManager.getPacket(BasicComponents.CHANNEL, this, this.wattsReceived, this.ic2Energy, this.fuelTank.getLiquid() == null ? 0 : this.fuelTank.getLiquid().amount, this.disabled, this.disableCooldown);
	}

	@Override
	public ForgeDirection getInputDirection() 
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public ItemStack getBatteryInSlot()
	{
		return this.getStackInSlot(0);
	}
}
