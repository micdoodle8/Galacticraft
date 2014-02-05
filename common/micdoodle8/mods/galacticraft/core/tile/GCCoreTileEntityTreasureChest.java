package micdoodle8.mods.galacticraft.core.tile;

import java.util.Iterator;
import java.util.List;

import micdoodle8.mods.galacticraft.api.item.IKeyable;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockT1TreasureChest;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreTileEntityTreasureChest.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityTreasureChest extends GCCoreTileEntityAdvanced implements IInventory, IKeyable, IPacketReceiver
{
	private ItemStack[] chestContents = new ItemStack[36];

	/** Determines if the check for adjacent chests has taken place. */
	public boolean adjacentChestChecked = false;

	/** Contains the chest tile located adjacent to this one (if any) */
	public GCCoreTileEntityTreasureChest adjacentChestZNeg;

	/** Contains the chest tile located adjacent to this one (if any) */
	public GCCoreTileEntityTreasureChest adjacentChestXPos;

	/** Contains the chest tile located adjacent to this one (if any) */
	public GCCoreTileEntityTreasureChest adjacentChestXNeg;

	/** Contains the chest tile located adjacent to this one (if any) */
	public GCCoreTileEntityTreasureChest adjacentChestZPos;

	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;

	/** The angle of the lid last tick */
	public float prevLidAngle;

	/** The number of players currently using this chest */
	public int numUsingPlayers;

	/** Server sync counter (once per 20 ticks) */
	private int ticksSinceSync;

	@NetworkedField(targetSide = Side.CLIENT)
	public boolean locked = true;

	public int tier = 1;

	public GCCoreTileEntityTreasureChest()
	{
		this(1);
	}

	public GCCoreTileEntityTreasureChest(int tier)
	{
		this.tier = tier;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return 27;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return this.chestContents[par1];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
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

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
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

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.chestContents[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.locked = nbt.getBoolean("isLocked");
		this.tier = nbt.getInteger("tier");
		final NBTTagList nbttaglist = nbt.getTagList("Items");
		this.chestContents = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			final NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			final int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < this.chestContents.length)
			{
				this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("isLocked", this.locked);
		nbt.setInteger("tier", this.tier);
		final NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.chestContents.length; ++i)
		{
			if (this.chestContents[i] != null)
			{
				final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.chestContents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended. *Isn't this more of a set than a get?*
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	/**
	 * Causes the TileEntity to reset all it's cached values for it's container
	 * block, blockID, metaData and in the case of chests, the adjcacent chest
	 * check
	 */
	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		this.adjacentChestChecked = false;
	}

	private void func_90009_a(GCCoreTileEntityTreasureChest par1TileEntityChest, int par2)
	{
		if (par1TileEntityChest.isInvalid())
		{
			this.adjacentChestChecked = false;
		}
		else if (this.adjacentChestChecked)
		{
			switch (par2)
			{
			case 0:
				if (this.adjacentChestZPos != par1TileEntityChest)
				{
					this.adjacentChestChecked = false;
				}

				break;
			case 1:
				if (this.adjacentChestXNeg != par1TileEntityChest)
				{
					this.adjacentChestChecked = false;
				}

				break;
			case 2:
				if (this.adjacentChestZNeg != par1TileEntityChest)
				{
					this.adjacentChestChecked = false;
				}

				break;
			case 3:
				if (this.adjacentChestXPos != par1TileEntityChest)
				{
					this.adjacentChestChecked = false;
				}
			}
		}
	}

	/**
	 * Performs the check for adjacent chests to determine if this chest is
	 * double or not.
	 */
	public void checkForAdjacentChests()
	{
		if (!this.adjacentChestChecked)
		{
			this.adjacentChestChecked = true;
			this.adjacentChestZNeg = null;
			this.adjacentChestXPos = null;
			this.adjacentChestXNeg = null;
			this.adjacentChestZPos = null;

			if (this.func_94044_a(this.xCoord - 1, this.yCoord, this.zCoord))
			{
				this.adjacentChestXNeg = (GCCoreTileEntityTreasureChest) this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
			}

			if (this.func_94044_a(this.xCoord + 1, this.yCoord, this.zCoord))
			{
				this.adjacentChestXPos = (GCCoreTileEntityTreasureChest) this.worldObj.getBlockTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
			}

			if (this.func_94044_a(this.xCoord, this.yCoord, this.zCoord - 1))
			{
				this.adjacentChestZNeg = (GCCoreTileEntityTreasureChest) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
			}

			if (this.func_94044_a(this.xCoord, this.yCoord, this.zCoord + 1))
			{
				this.adjacentChestZPos = (GCCoreTileEntityTreasureChest) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
			}

			if (this.adjacentChestZNeg != null)
			{
				this.adjacentChestZNeg.func_90009_a(this, 0);
			}

			if (this.adjacentChestZPos != null)
			{
				this.adjacentChestZPos.func_90009_a(this, 2);
			}

			if (this.adjacentChestXPos != null)
			{
				this.adjacentChestXPos.func_90009_a(this, 1);
			}

			if (this.adjacentChestXNeg != null)
			{
				this.adjacentChestXNeg.func_90009_a(this, 3);
			}
		}
	}

	private boolean func_94044_a(int par1, int par2, int par3)
	{
		final Block block = Block.blocksList[this.worldObj.getBlockId(par1, par2, par3)];
		return block != null && block instanceof GCCoreBlockT1TreasureChest;
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses,
	 * e.g. the mob spawner uses this to count ticks and creates a new spawn
	 * inside its implementation.
	 */
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		this.checkForAdjacentChests();
		++this.ticksSinceSync;
		float f;

		if (!this.worldObj.isRemote && this.numUsingPlayers != 0 && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0)
		{
			this.numUsingPlayers = 0;
			f = 5.0F;
			final List<?> list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB(this.xCoord - f, this.yCoord - f, this.zCoord - f, this.xCoord + 1 + f, this.yCoord + 1 + f, this.zCoord + 1 + f));
			final Iterator<?> iterator = list.iterator();

			while (iterator.hasNext())
			{
				final EntityPlayer entityplayer = (EntityPlayer) iterator.next();

				if (entityplayer.openContainer instanceof ContainerChest)
				{
					final IInventory iinventory = ((ContainerChest) entityplayer.openContainer).getLowerChestInventory();

					if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest) iinventory).isPartOfLargeChest(this))
					{
						++this.numUsingPlayers;
					}
				}
			}
		}

		this.prevLidAngle = this.lidAngle;
		f = 0.05F;
		double d0;

		if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
		{
			double d1 = this.xCoord + 0.5D;
			d0 = this.zCoord + 0.5D;

			if (this.adjacentChestZPos != null)
			{
				d0 += 0.5D;
			}

			if (this.adjacentChestXPos != null)
			{
				d1 += 0.5D;
			}

			this.worldObj.playSoundEffect(d1, this.yCoord + 0.5D, d0, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.6F);
		}

		if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F)
		{
			final float f1 = this.lidAngle;

			if (this.numUsingPlayers > 0)
			{
				this.lidAngle += f;
			}
			else
			{
				this.lidAngle -= f;
			}

			if (this.lidAngle > 1.0F)
			{
				this.lidAngle = 1.0F;
			}

			final float f2 = 0.5F;

			if (this.lidAngle < f2 && f1 >= f2 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
			{
				d0 = this.xCoord + 0.5D;
				double d2 = this.zCoord + 0.5D;

				if (this.adjacentChestZPos != null)
				{
					d2 += 0.5D;
				}

				if (this.adjacentChestXPos != null)
				{
					d0 += 0.5D;
				}

				this.worldObj.playSoundEffect(d0, this.yCoord + 0.5D, d2, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.6F);
			}

			if (this.lidAngle < 0.0F)
			{
				this.lidAngle = 0.0F;
			}
		}
	}

	/**
	 * Called when a client event is received with the event number and
	 * argument, see World.sendClientEvent
	 */
	@Override
	public boolean receiveClientEvent(int par1, int par2)
	{
		if (par1 == 1)
		{
			this.numUsingPlayers = par2;
			return true;
		}
		else
		{
			return super.receiveClientEvent(par1, par2);
		}
	}

	@Override
	public void openChest()
	{
		if (this.numUsingPlayers < 0)
		{
			this.numUsingPlayers = 0;
		}

		++this.numUsingPlayers;
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, this.numUsingPlayers);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType().blockID);
	}

	@Override
	public void closeChest()
	{
		if (this.getBlockType() != null && this.getBlockType() instanceof GCCoreBlockT1TreasureChest)
		{
			--this.numUsingPlayers;
			this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, this.numUsingPlayers);
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType().blockID);
		}
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	/**
	 * invalidates a tile entity
	 */
	@Override
	public void invalidate()
	{
		super.invalidate();
		this.updateContainingBlockInfo();
		this.checkForAdjacentChests();
	}

	@Override
	public String getInvName()
	{
		return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? StatCollector.translateToLocal("container.treasurechest.name") : StatCollector.translateToLocal("container.treasurechest.name");
	}

	@Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
	{
		return true;
	}

	@Override
	public int getTierOfKeyRequired()
	{
		return this.tier;
	}

	@Override
	public boolean onValidKeyActivated(EntityPlayer player, ItemStack key, int face)
	{
		if (this.locked)
		{
			this.locked = false;

			if (this.worldObj.isRemote)
			{
				// player.playSound("galacticraft.player.unlockchest", 1.0F,
				// 1.0F);
			}
			else
			{
				if (this.adjacentChestXNeg != null)
				{
					this.adjacentChestXNeg.locked = false;
				}
				if (this.adjacentChestXPos != null)
				{
					this.adjacentChestXPos.locked = false;
				}
				if (this.adjacentChestZNeg != null)
				{
					this.adjacentChestZNeg.locked = false;
				}
				if (this.adjacentChestZPos != null)
				{
					this.adjacentChestZPos.locked = false;
				}

				if (--player.inventory.getCurrentItem().stackSize == 0)
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean onActivatedWithoutKey(EntityPlayer player, int face)
	{
		if (this.locked)
		{
			if (player.worldObj.isRemote)
			{
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.ON_FAILED_CHEST_UNLOCK, new Object[] { this.getTierOfKeyRequired() }));
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean canBreak()
	{
		return false;
	}

	@Override
	public double getPacketRange()
	{
		return 20.0D;
	}

	@Override
	public int getPacketCooldown()
	{
		return 3;
	}

	@Override
	public boolean isNetworkedTile()
	{
		return true;
	}
}
