package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.oxygen.BlockVec3;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.oxygen.ThreadFindSeal;
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
 * GCCoreTileEntityOxygenSealer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityOxygenSealer extends GCCoreTileEntityOxygen implements IInventory, ISidedInventory
{
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean sealed;
	public boolean lastSealed = false;

	public static final float WATTS_PER_TICK = 0.2F;
	public boolean lastDisabled = false;

	@NetworkedField(targetSide = Side.CLIENT)
	public boolean active;
	private ItemStack[] containingItems = new ItemStack[1];
	public ThreadFindSeal threadSeal;
	@NetworkedField(targetSide = Side.CLIENT)
	public int stopSealThreadCooldown;
	@NetworkedField(targetSide = Side.CLIENT)
	public int threadCooldownTotal;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean calculatingSealed;
	public static int countEntities = 0;
	private static int countTemp = 0;
	private static long ticksSave = 0L;
	private static boolean sealerCheckedThisTick = false;

	public GCCoreTileEntityOxygenSealer()
	{
		super(GCCoreTileEntityOxygenSealer.WATTS_PER_TICK, 50, 10000, 16);
	}

	public int getScaledThreadCooldown(int i)
	{
		if (this.active)
		{
			return Math.min(i, (int) Math.floor(this.stopSealThreadCooldown * i / (double) this.threadCooldownTotal));
		}
		return 0;
	}

	public int getFindSealChecks()
	{
		if (!this.active || this.storedOxygen < this.oxygenPerTick || this.getEnergyStored() <= 0.0F)
		{
			return 0;
		}
		int blockAbove = this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord);
		if (blockAbove != 0 && blockAbove != GCCoreBlocks.breatheableAir.blockID && !OxygenPressureProtocol.canBlockPassAir(this.worldObj, blockAbove, new BlockVec3(this.xCoord, this.yCoord + 1, this.zCoord), 1))
		{
			// The vent is blocked
			return 0;
		}

		return 1250;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			// Some code to count the number of Oxygen Sealers being updated,
			// tick by tick - needed for queueing
			if (this.ticks == GCCoreTileEntityOxygenSealer.ticksSave)
			{
				GCCoreTileEntityOxygenSealer.countTemp++;
			}
			else
			{
				GCCoreTileEntityOxygenSealer.ticksSave = this.ticks;
				GCCoreTileEntityOxygenSealer.countEntities = GCCoreTileEntityOxygenSealer.countTemp;
				GCCoreTileEntityOxygenSealer.countTemp = 1;
				GCCoreTileEntityOxygenSealer.sealerCheckedThisTick = false;
			}

			if (this.storedOxygen >= 1 && this.energyStored > 0 && !this.disabled)
			{
				this.active = true;
			}
			else
			{
				this.active = false;
			}

			if (this.stopSealThreadCooldown > 0)
			{
				this.stopSealThreadCooldown--;
			}
			else if (GCCoreTileEntityOxygenSealer.sealerCheckedThisTick == false)
			{
				// This puts any Sealer which is updated to the back of the queue for updates
				this.threadCooldownTotal = this.stopSealThreadCooldown = 75 + GCCoreTileEntityOxygenSealer.countEntities;
				GCCoreTileEntityOxygenSealer.sealerCheckedThisTick = true;
				OxygenPressureProtocol.updateSealerStatus(this);
			}

			if (this.threadSeal != null)
			{
				this.sealed = this.active && this.threadSeal.sealedFinal.get();
				this.calculatingSealed = this.active && this.threadSeal.looping.get();
			}

			this.lastDisabled = this.disabled;
			this.lastSealed = this.sealed;
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
			final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
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
	public String getInvName()
	{
		return StatCollector.translateToLocal("container.oxygensealer.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	// ISidedInventory Implementation:

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		return this.isItemValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
	{
		return slotID == 0;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		return slotID == 0 ? itemstack.getItem() instanceof IItemElectric : false;
	}

	@Override
	public boolean shouldPullEnergy()
	{
		return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
	}

	@Override
	public boolean shouldUseEnergy()
	{
		return GCCoreTileEntityOxygen.timeSinceOxygenRequest > 0 && !this.getDisabled(0);
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

	@Override
	public boolean shouldPullOxygen()
	{
		return this.getEnergyStored() > 0;
	}

	@Override
	public boolean shouldUseOxygen()
	{
		return this.active && this.sealed;
	}

	@Override
	public EnumSet<ForgeDirection> getOxygenInputDirections()
	{
		return EnumSet.of(this.getElectricInputDirection().getOpposite());
	}

	@Override
	public EnumSet<ForgeDirection> getOxygenOutputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}
}
