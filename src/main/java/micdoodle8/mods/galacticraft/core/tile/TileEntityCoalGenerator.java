package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

public class TileEntityCoalGenerator extends TileBaseUniversalElectricalSource implements IInventory, ISidedInventory, IPacketReceiver, IConnector
{
	//New energy rates:
	//
	//Tier 1 machine typically consumes 600 gJ/s = 30 gJ/t

	//Coal generator on max heat can power up to 4 Tier 1 machines
	//(fewer if one of them is an Electric Furnace)
	//Basic solar gen in full sun can power 1 Tier 1 machine

	//1 lump of coal is equivalent to 38400 gJ
	//because on max heat it produces 120 gJ/t over 320 ticks
	
	//Below the min_generate, all heat is wasted
	//At max generate, 100% efficient conversion coal energy -> electric makes 120 gJ/t
	public static final int MAX_GENERATE_GJ_PER_TICK = 150;
	public static final int MIN_GENERATE_GJ_PER_TICK = 30;

	private static final float BASE_ACCELERATION = 0.3f;

	public float prevGenerateWatts = 0;

	@NetworkedField(targetSide = Side.CLIENT)
	public float heatGJperTick = 0;

	/**
	 * The number of ticks that a fresh copy of the currently-burning item would
	 * keep the furnace burning for
	 */
	@NetworkedField(targetSide = Side.CLIENT)
	public int itemCookTime = 0;
	/**
	 * The ItemStacks that hold the items currently being used in the battery
	 * box
	 */
	private ItemStack[] containingItems = new ItemStack[1];

	public TileEntityCoalGenerator()
	{
		this.storage.setMaxExtract(TileEntityCoalGenerator.MAX_GENERATE_GJ_PER_TICK - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK);
	}

	@Override
	public void updateEntity()
	{
		if (this.heatGJperTick - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK > 0)
			this.receiveEnergyGC(null, (this.heatGJperTick - TileEntityCoalGenerator.MIN_GENERATE_GJ_PER_TICK), false);

		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.itemCookTime > 0)
			{
				this.itemCookTime--;

				if (this.getEnergyStoredGC() < this.getMaxEnergyStoredGC())
				{
					this.heatGJperTick = Math.min(this.heatGJperTick + Math.max(this.heatGJperTick * 0.005F, TileEntityCoalGenerator.BASE_ACCELERATION), TileEntityCoalGenerator.MAX_GENERATE_GJ_PER_TICK);
				}
			}

			if (this.containingItems[0] != null)
			{
				if (this.containingItems[0].getItem() == Items.coal)
				{
					if (this.itemCookTime <= 0)
					{
						this.itemCookTime = 320;
						this.decrStackSize(0, 1);
					}
				}
			}

			this.produce();

			if (this.itemCookTime <= 0)
			{
				this.heatGJperTick = Math.max(this.heatGJperTick - 0.3F, 0);
			}

			this.heatGJperTick = Math.min(Math.max(this.heatGJperTick, 0.0F), this.getMaxEnergyStoredGC());
		}
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
		this.itemCookTime = par1NBTTagCompound.getInteger("itemCookTime");
		this.heatGJperTick = par1NBTTagCompound.getInteger("generateRateInt");
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
		par1NBTTagCompound.setInteger("itemCookTime", this.itemCookTime);
		par1NBTTagCompound.setFloat("generateRate", this.heatGJperTick);
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
		return GCCoreUtil.translate("tile.machine.0.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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
		return itemstack.getItem() == Items.coal;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int j)
	{
		return this.isItemValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int j)
	{
		return slotID == 0;
	}

	@Override
	public float receiveElectricity(ForgeDirection from, float energy, int tier, boolean doReceive)
	{
		return 0;
	}

	/*
	@Override
	public float getRequest(ForgeDirection direction)
	{
		return 0;
	}
	*/
	
	@Override
	public EnumSet<ForgeDirection> getElectricalInputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalOutputDirections()
	{
		return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() + 2));
	}

	@Override
	public ForgeDirection getElectricalOutputDirectionMain()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public boolean canConnect(ForgeDirection direction, NetworkType type)
	{
		if (direction == null || direction.equals(ForgeDirection.UNKNOWN) || type != NetworkType.POWER)
		{
			return false;
		}

		return direction == this.getElectricalOutputDirectionMain();
	}
}
