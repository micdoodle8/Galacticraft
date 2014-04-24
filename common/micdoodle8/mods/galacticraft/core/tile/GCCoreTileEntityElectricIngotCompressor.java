package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMachine2;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreTileEntityElectricIngotCompressor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityElectricIngotCompressor extends GCCoreTileEntityElectricBlock implements IInventory, ISidedInventory, IPacketReceiver
{
	public static final int PROCESS_TIME_REQUIRED = 200;
	public static final float WATTS_PER_TICK_PER_STACK = 0.25F;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack producingStack = null;
	private long ticks;

	private ItemStack[] containingItems = new ItemStack[3];
	public PersistantInventoryCrafting compressingCraftMatrix = new PersistantInventoryCrafting(3, 3);

	public GCCoreTileEntityElectricIngotCompressor()
	{
		super(GCCoreTileEntityElectricIngotCompressor.WATTS_PER_TICK_PER_STACK, 50);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			boolean updateInv = false;

			if (this.getEnergyStored() > 0.0F)
			{
				if (this.canCompress())
				{
					++this.processTicks;

					if (this.processTicks == GCCoreTileEntityElectricIngotCompressor.PROCESS_TIME_REQUIRED)
					{
						this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "random.anvil_land", 0.2F, 0.5F);
						this.processTicks = 0;
						this.compressItems();
						updateInv = true;
					}
				}
				else
				{
					this.processTicks = 0;
				}
			}
			else
			{
				this.processTicks = 0;
			}

			if (updateInv)
			{
				this.onInventoryChanged();
			}
		}

		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 0;
		}

		this.ticks++;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	private boolean canCompress()
	{
		ItemStack itemstack = this.producingStack;
		if (itemstack == null)
		{
			return false;
		}
		if (this.containingItems[1] == null && this.containingItems[2] == null)
		{
			return true;
		}
		if (this.containingItems[1] != null && !this.containingItems[1].isItemEqual(itemstack) || this.containingItems[2] != null && !this.containingItems[2].isItemEqual(itemstack))
		{
			return false;
		}
		int result = this.containingItems[1] == null ? 0 : this.containingItems[1].stackSize + itemstack.stackSize;
		int result2 = this.containingItems[2] == null ? 0 : this.containingItems[2].stackSize + itemstack.stackSize;
		return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize() && result2 <= this.getInventoryStackLimit() && result2 <= itemstack.getMaxStackSize();
	}

	public void updateInput()
	{
		this.producingStack = CompressorRecipes.findMatchingRecipe(this.compressingCraftMatrix, this.worldObj);
	}

	public void compressItems()
	{
		int stackSize1 = this.containingItems[1] == null ? 0 : this.containingItems[1].stackSize;
		int stackSize2 = this.containingItems[2] == null ? 0 : this.containingItems[2].stackSize;

		if (stackSize1 <= stackSize2)
		{
			this.compressIntoSlot(1);
			this.compressIntoSlot(2);
		}
		else
		{
			this.compressIntoSlot(2);
			this.compressIntoSlot(1);
		}
	}

	private void compressIntoSlot(int slot)
	{
		if (this.canCompress())
		{
			ItemStack resultItemStack = this.producingStack;

			if (this.containingItems[slot] == null)
			{
				this.containingItems[slot] = resultItemStack.copy();
			}
			else if (this.containingItems[slot].isItemEqual(resultItemStack))
			{
				if (this.containingItems[slot].stackSize + resultItemStack.stackSize > 64)
				{
					for (int i = 0; i < this.containingItems[slot].stackSize + resultItemStack.stackSize - 64; i++)
					{
						float var = 0.7F;
						double dx = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dy = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dz = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + dx, this.yCoord + dy, this.zCoord + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));

						entityitem.delayBeforeCanPickup = 10;

						this.worldObj.spawnEntityInWorld(entityitem);
					}
				}

				this.containingItems[slot].stackSize += resultItemStack.stackSize;
			}

			for (int i = 0; i < this.compressingCraftMatrix.getSizeInventory(); i++)
			{
				this.compressingCraftMatrix.decrStackSize(i, 1);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.processTicks = par1NBTTagCompound.getInteger("smeltingTicks");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.containingItems = new ItemStack[this.getSizeInventory() - this.compressingCraftMatrix.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length)
			{
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
			else if (var5 < this.containingItems.length + this.compressingCraftMatrix.getSizeInventory())
			{
				this.compressingCraftMatrix.setInventorySlotContents(var5 - this.containingItems.length, ItemStack.loadItemStackFromNBT(var4));
			}
		}

		this.updateInput();
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("smeltingTicks", this.processTicks);
		NBTTagList var2 = new NBTTagList();
		int var3;

		for (var3 = 0; var3 < this.containingItems.length; ++var3)
		{
			if (this.containingItems[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		for (var3 = 0; var3 < this.compressingCraftMatrix.getSizeInventory(); ++var3)
		{
			if (this.compressingCraftMatrix.getStackInSlot(var3) != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) (var3 + this.containingItems.length));
				this.compressingCraftMatrix.getStackInSlot(var3).writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

	@Override
	public int getSizeInventory()
	{
		return this.containingItems.length + this.compressingCraftMatrix.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int par1)
	{
		if (par1 >= this.containingItems.length)
		{
			return this.compressingCraftMatrix.getStackInSlot(par1 - this.containingItems.length);
		}

		return this.containingItems[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (par1 >= this.containingItems.length)
		{
			return this.compressingCraftMatrix.decrStackSize(par1 - this.containingItems.length, par2);
		}

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
		if (par1 >= this.containingItems.length)
		{
			return this.compressingCraftMatrix.getStackInSlotOnClosing(par1 - this.containingItems.length);
		}

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
		if (par1 >= this.containingItems.length)
		{
			this.compressingCraftMatrix.setInventorySlotContents(par1 - this.containingItems.length, par2ItemStack);
		}
		else
		{
			this.containingItems[par1] = par2ItemStack;

			if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
			{
				par2ItemStack.stackSize = this.getInventoryStackLimit();
			}
		}
	}

	@Override
	public String getInvName()
	{
		return StatCollector.translateToLocal("tile.machine2.4.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
	{
		if (slotID == 0)
		{
			return itemStack.getItem() instanceof IItemElectric;
		}
		else if (slotID >= 3)
		{
			return GCCoreTileEntityIngotCompressor.isItemCompressorInput(itemStack);
		}

		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 0 ? new int[] { 1, 2 } : side == 1 ? new int[] { 0, 3, 4, 5, 6, 7, 8, 9, 10, 11 } : new int[] { 0, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack par2ItemStack, int par3)
	{
		return this.isItemValidForSlot(slotID, par2ItemStack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack par2ItemStack, int par3)
	{
		return slotID == 1 || slotID == 2;
	}

	@Override
	public boolean shouldPullEnergy()
	{
		return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
	}

	@Override
	public boolean shouldUseEnergy()
	{
		return this.processTicks > 0;
	}

	@Override
	public ForgeDirection getElectricInputDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() - GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA + 2);
	}

	@Override
	public ItemStack getBatteryInSlot()
	{
		return this.getStackInSlot(0);
	}
}
