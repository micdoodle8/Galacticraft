package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.EntityBubble;
import micdoodle8.mods.galacticraft.core.entities.IBubble;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;

public class TileEntityOxygenDistributor extends TileEntityOxygen implements IInventory, ISidedInventory, IBubbleProvider
{
	public boolean active;
	public boolean lastActive;

	public static final int WATTS_PER_TICK = 1;

	private ItemStack[] containingItems = new ItemStack[1];

	public EntityBubble oxygenBubble;
	@NetworkedField(targetSide = Side.CLIENT)
	public int oxygenBubbleEntityID = -1;

	public TileEntityOxygenDistributor()
	{
		super(6000, 8);
		this.oxygenBubble = null;
		this.storage.setMaxExtract(200);
		this.storage.setCapacity(50000);
	}

	@Override
	public void invalidate()
	{
		if (this.oxygenBubble != null)
		{
			for (int x = (int) Math.floor(this.xCoord - this.oxygenBubble.getSize()); x < Math.ceil(this.xCoord + this.oxygenBubble.getSize()); x++)
			{
				for (int y = (int) Math.floor(this.yCoord - this.oxygenBubble.getSize()); y < Math.ceil(this.yCoord + this.oxygenBubble.getSize()); y++)
				{
					for (int z = (int) Math.floor(this.zCoord - this.oxygenBubble.getSize()); z < Math.ceil(this.zCoord + this.oxygenBubble.getSize()); z++)
					{
						Block block = this.worldObj.getBlock(x, y, z);

						if (block instanceof IOxygenReliantBlock)
						{
							((IOxygenReliantBlock) block).onOxygenRemoved(this.worldObj, x, y, z);
						}
					}
				}
			}
		}

		super.invalidate();
	}

	public double getDistanceFromServer(double par1, double par3, double par5)
	{
		final double d3 = this.xCoord + 0.5D - par1;
		final double d4 = this.yCoord + 0.5D - par3;
		final double d5 = this.zCoord + 0.5D - par5;
		return d3 * d3 + d4 * d4 + d5 * d5;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.oxygenBubble == null || this.ticks < 25)
		{
			if (this.oxygenBubbleEntityID != -1 && (this.oxygenBubble == null || this.oxygenBubbleEntityID != this.oxygenBubble.getEntityId()))
			{
				Entity entity = this.worldObj.getEntityByID(this.oxygenBubbleEntityID);

				if (entity instanceof EntityBubble)
				{
					this.oxygenBubble = (EntityBubble) entity;
				}
			}

			if (this.oxygenBubble == null)
			{
				this.oxygenBubble = new EntityBubble(this.worldObj, new Vector3(this), this);

				if (!this.worldObj.isRemote)
				{
					this.worldObj.spawnEntityInWorld(this.oxygenBubble);
				}
			}
		}

		if (!this.worldObj.isRemote)
		{
			this.oxygenBubbleEntityID = this.oxygenBubble.getEntityId();
		}

		if (!this.worldObj.isRemote)
		{
            this.active = this.oxygenBubble.getSize() >= 1 && this.hasEnoughEnergyToRun;
		}

		if (!this.worldObj.isRemote && (this.active != this.lastActive || this.ticks % 20 == 0))
		{
			if (this.active)
			{
				for (int x = (int) Math.floor(this.xCoord - this.oxygenBubble.getSize() - 4); x < Math.ceil(this.xCoord + this.oxygenBubble.getSize() + 4); x++)
				{
					for (int y = (int) Math.floor(this.yCoord - this.oxygenBubble.getSize() - 4); y < Math.ceil(this.yCoord + this.oxygenBubble.getSize() + 4); y++)
					{
						for (int z = (int) Math.floor(this.zCoord - this.oxygenBubble.getSize() - 4); z < Math.ceil(this.zCoord + this.oxygenBubble.getSize() + 4); z++)
						{
							Block block = this.worldObj.getBlock(x, y, z);

							if (block instanceof IOxygenReliantBlock)
							{
								if (this.getDistanceFromServer(x, y, z) < Math.pow(this.oxygenBubble.getSize() - 0.5D, 2))
								{
									((IOxygenReliantBlock) block).onOxygenAdded(this.worldObj, x, y, z);
								}
								else
								{
									((IOxygenReliantBlock) block).onOxygenRemoved(this.worldObj, x, y, z);
								}
							}
						}
					}
				}
			}
		}

		this.lastActive = this.active;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		final NBTTagList var2 = nbt.getTagList("Items", 10);
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length)
			{
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("BubbleEntityID", this.oxygenBubble == null ? -1 : this.oxygenBubble.getEntityId());

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

		nbt.setTag("Items", list);
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
	public String getInventoryName()
	{
		return GCCoreUtil.translate("container.oxygendistributor.name");
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
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		return slotID == 0 && itemstack.getItem() instanceof IItemElectric;
	}

	@Override
	public void openInventory()
	{

	}

	@Override
	public void closeInventory()
	{

	}

	@Override
	public boolean shouldUseEnergy()
	{
		return TileEntityOxygen.timeSinceOxygenRequest > 0;
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
		return this.hasEnoughEnergyToRun;
	}

	@Override
	public boolean shouldUseOxygen()
	{
		return true;
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

	@Override
	public IBubble getBubble()
	{
		return this.oxygenBubble;
	}

	@Override
	public void setBubbleVisible(boolean shouldRender)
	{
		if (this.oxygenBubble == null)
		{
			return;
		}

		this.oxygenBubble.setShouldRender(shouldRender);
	}
}
