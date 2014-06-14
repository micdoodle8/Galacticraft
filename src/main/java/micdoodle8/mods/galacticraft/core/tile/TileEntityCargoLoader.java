package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity.EnumCargoLoadingState;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity.RemovalResult;
import micdoodle8.mods.galacticraft.api.power.IItemElectric;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;



public class TileEntityCargoLoader extends TileEntityElectricBlock implements IInventory, ISidedInventory, ILandingPadAttachable
{
	private ItemStack[] containingItems = new ItemStack[15];
	public boolean outOfItems;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean targetFull;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean targetNoInventory;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean noTarget;

	public ICargoEntity attachedFuelable;

	public TileEntityCargoLoader()
	{
		this.storage.setMaxExtract(75);
		this.storage.setCapacity(50000);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 100 == 0)
			{
				this.checkForCargoEntity();
			}

			if (this.attachedFuelable != null)
			{
				this.noTarget = false;
				ItemStack stack = this.removeCargo(false).resultStack;

				if (stack != null)
				{
					this.outOfItems = false;

					EnumCargoLoadingState state = this.attachedFuelable.addCargo(stack, false);

					this.targetFull = state == EnumCargoLoadingState.FULL;
					this.targetNoInventory = state == EnumCargoLoadingState.NOINVENTORY;
					this.noTarget = state == EnumCargoLoadingState.NOTARGET;

					if (this.ticks % 15 == 0 && state == EnumCargoLoadingState.SUCCESS && !this.disabled && this.hasEnoughEnergyToRun)
					{
						this.attachedFuelable.addCargo(this.removeCargo(true).resultStack, true);
					}
				}
				else
				{
					this.outOfItems = true;
				}
			}
			else
			{
				this.noTarget = true;
			}
		}
	}

	public void checkForCargoEntity()
	{
		boolean foundFuelable = false;

		for (final ForgeDirection dir : ForgeDirection.values())
		{
			if (dir != ForgeDirection.UNKNOWN)
			{
				final TileEntity pad = new BlockVec3(this).getTileEntityOnSide(this.worldObj, dir);

				if (pad != null && pad instanceof TileEntityMulti)
				{
					final TileEntity mainTile = ((TileEntityMulti) pad).mainBlockPosition.getTileEntity(this.worldObj);

					if (mainTile != null && mainTile instanceof ICargoEntity)
					{
						this.attachedFuelable = (ICargoEntity) mainTile;
						foundFuelable = true;
						break;
					}
				}
				else if (pad != null && pad instanceof ICargoEntity)
				{
					this.attachedFuelable = (ICargoEntity) pad;
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
				this.markDirty();
				return var3;
			}
			else
			{
				var3 = this.containingItems[par1].splitStack(par2);

				if (this.containingItems[par1].stackSize == 0)
				{
					this.containingItems[par1] = null;
					this.markDirty();
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
			this.markDirty();
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
		
		this.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		final NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			final NBTTagCompound var4 = (NBTTagCompound) var2.getCompoundTagAt(var3);
			final byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length)
			{
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
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
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				list.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", list);
	}

	@Override
	public String getInventoryName()
	{
		return GCCoreUtil.translate("container.cargoloader.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	// ISidedInventory Implementation:

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side != this.getBlockMetadata() + 2 ? new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 } : new int[] {};
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		if (side != this.getBlockMetadata() + 2)
		{
			if (slotID == 0)
			{
				return itemstack.getItem() instanceof IItemElectric;
			}
			else
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
	{
		return false;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		if (slotID == 0)
		{
			return itemstack.getItem() instanceof IItemElectric;
		}
		else
		{
			return true;
		}
	}

	@Override
	public boolean shouldUseEnergy()
	{
		return !this.getDisabled(0);
	}

	@Override
	public ForgeDirection getElectricInputDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public ItemStack getBatteryInSlot()
	{
		return this.getStackInSlot(0);
	}

	public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
	{
		int count = 1;

		for (count = 1; count < this.containingItems.length; count++)
		{
			ItemStack stackAt = this.containingItems[count];

			if (stackAt != null && stackAt.getItem() == stack.getItem() && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
				if (stackAt.stackSize + stack.stackSize <= stackAt.getMaxStackSize())
				{
					if (doAdd)
					{
						this.containingItems[count].stackSize += stack.stackSize;
						this.markDirty();
					}
	
					return EnumCargoLoadingState.SUCCESS;
				} else
				{
					//Part of the stack can fill this slot but there will be some left over
					int origSize = stackAt.stackSize;
					int surplus = origSize + stack.stackSize - stackAt.getMaxStackSize();

					if (doAdd)
					{
						this.containingItems[count].stackSize = stackAt.getMaxStackSize();
						this.markDirty();
					}
					
					stack.stackSize = surplus;
					if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
						return EnumCargoLoadingState.SUCCESS;
					
					this.containingItems[count].stackSize = origSize;
					return EnumCargoLoadingState.FULL;			
				}
		}

		for (count = 1; count < this.containingItems.length; count++)
		{
			ItemStack stackAt = this.containingItems[count];

			if (stackAt == null)
			{
				if (doAdd)
				{
					this.containingItems[count] = stack;
					this.markDirty();
				}

				return EnumCargoLoadingState.SUCCESS;
			}
		}

		return EnumCargoLoadingState.FULL;
	}

	public RemovalResult removeCargo(boolean doRemove)
	{
		for (int i = 1; i < this.containingItems.length; i++)
		{
			ItemStack stackAt = this.containingItems[i];

			if (stackAt != null)
			{
				if (doRemove && --this.containingItems[i].stackSize <= 0)
				{
					this.containingItems[i] = null;
				}

				if (doRemove) this.markDirty();
				return new RemovalResult(EnumCargoLoadingState.SUCCESS, new ItemStack(stackAt.getItem(), 1, stackAt.getItemDamage()));
			}
		}

		return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
	}

	@Override
	public boolean canAttachToLandingPad(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}
}
