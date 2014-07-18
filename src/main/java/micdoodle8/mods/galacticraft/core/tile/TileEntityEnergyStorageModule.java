package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.item.ItemElectric;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TileEntityEnergyStorageModule extends TileEntityUniversalElectricalSource implements IPacketReceiver, ISidedInventory, IConnector
{
	private ItemStack[] containingItems = new ItemStack[2];

	public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
	public int scaledEnergyLevel;
	public int lastScaledEnergyLevel;

	public TileEntityEnergyStorageModule()
	{
		this.storage.setCapacity(500000);
		this.storage.setMaxExtract(300);  //Tier 1
		//Designed so that Tier 1 Energy Storage can power up to 10 Tier 1 machines
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		this.scaledEnergyLevel = (int) Math.floor(this.getEnergyStoredGC() * 16 / this.getMaxEnergyStoredGC());

		if (this.scaledEnergyLevel != this.lastScaledEnergyLevel)
		{
			this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
		}

		if (!this.worldObj.isRemote)
		{
			this.recharge(this.containingItems[0]);
			this.discharge(this.containingItems[1]);
		}

		if (!this.worldObj.isRemote)
		{
			this.produce();
		}

		this.lastScaledEnergyLevel = this.scaledEnergyLevel;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length)
			{
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3)
		{
			if (this.containingItems[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
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
			ItemStack var2 = this.containingItems[par1];
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
	public String getInventoryName()
	{
		return GCCoreUtil.translate("tile.machine.1.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		return ItemElectric.isElectricItem(itemstack.getItem());
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int slotID)
	{
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		if (itemstack.getItem() instanceof ItemElectric)
		{
			if (slotID == 0)
			{
				return ((ItemElectric) itemstack.getItem()).getTransfer(itemstack) > 0;
			}
			else if (slotID == 1)
			{
				return ((ItemElectric) itemstack.getItem()).getElectricityStored(itemstack) > 0;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
	{
		if (itemstack.getItem() instanceof ItemElectric)
		{
			if (slotID == 0)
			{
				return ((ItemElectric) itemstack.getItem()).getTransfer(itemstack) <= 0;
			}
			else if (slotID == 1)
			{
				return ((ItemElectric) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || this.getEnergyStoredGC() >= this.getMaxEnergyStoredGC();
			}
		}

		return false;

	}

	/*@Override
	public float getRequest(ForgeDirection direction)
	{
		return this.getElectricalInputDirections().contains(direction) ? this.getMaxEnergyStored() - this.getEnergyStored() : 0;
	}
	*/

	@Override
	public EnumSet<ForgeDirection> getElectricalInputDirections()
	{
		return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - BlockMachine.STORAGE_MODULE_METADATA + 2).getOpposite(), ForgeDirection.UNKNOWN);
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalOutputDirections()
	{
		return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - BlockMachine.STORAGE_MODULE_METADATA + 2), ForgeDirection.UNKNOWN);
	}

	@Override
	public ForgeDirection getElectricalOutputDirectionMain()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() - BlockMachine.STORAGE_MODULE_METADATA + 2);
	}

	@Override
	public boolean canConnect(ForgeDirection direction, NetworkType type)
	{
		if (direction == null || direction.equals(ForgeDirection.UNKNOWN) || type != NetworkType.POWER)
		{
			return false;
		}

		int metadata = this.getBlockMetadata() - BlockMachine.STORAGE_MODULE_METADATA;

		return direction == ForgeDirection.getOrientation(metadata + 2) || direction == ForgeDirection.getOrientation((metadata + 2) ^ 1);
	}

	//	@Override
	//	public float getMaxEnergyStored()
	//	{
	//		return 2500F;
	//	}

}
