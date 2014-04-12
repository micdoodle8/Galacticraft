package micdoodle8.mods.galacticraft.mars.tile;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPadFull;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricBlock;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import micdoodle8.mods.galacticraft.core.world.IChunkLoader;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockMachine;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

/**
 * GCMarsTileEntityLaunchController.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsTileEntityLaunchController extends GCCoreTileEntityElectricBlock implements IChunkLoader, IElectrical, IInventory, ISidedInventory, ILandingPadAttachable
{
	public static final float WATTS_PER_TICK = 0.05000001f;
	private ItemStack[] containingItems = new ItemStack[1];
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean launchPadRemovalDisabled = true;
	private Ticket chunkLoadTicket;
	private List<ChunkCoordinates> connectedPads = new ArrayList<ChunkCoordinates>();
	@NetworkedField(targetSide = Side.CLIENT)
	public int frequency = -1;
	@NetworkedField(targetSide = Side.CLIENT)
	public int destFrequency = -1;
	@NetworkedField(targetSide = Side.CLIENT)
	public String ownerName = "";
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean frequencyValid;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean destFrequencyValid;
	@NetworkedField(targetSide = Side.CLIENT)
	public int launchDropdownSelection;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean launchSchedulingEnabled;
	public boolean requiresClientUpdate;

	public GCMarsTileEntityLaunchController()
	{
		super(GCMarsTileEntityLaunchController.WATTS_PER_TICK, 50);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.requiresClientUpdate)
			{
				// PacketDispatcher.sendPacketToAllPlayers(this.getPacket());
				// TODO
				this.requiresClientUpdate = false;
			}

			if (this.ticks % 40 == 0)
			{
				this.setFrequency(this.frequency);
				this.setDestinationFrequency(this.destFrequency);
			}

			if (this.ticks % 20 == 0)
			{
				if (this.chunkLoadTicket != null)
				{
					for (int i = 0; i < this.connectedPads.size(); i++)
					{
						ChunkCoordinates coords = this.connectedPads.get(i);
						int blockID = this.worldObj.getBlockId(coords.posX, coords.posY, coords.posZ);

						if (blockID != GCCoreBlocks.landingPadFull.blockID)
						{
							this.connectedPads.remove(i);
							ForgeChunkManager.unforceChunk(this.chunkLoadTicket, new ChunkCoordIntPair(coords.posX >> 4, coords.posZ >> 4));
						}
					}
				}
			}
		}
		else
		{
			if (this.frequency == -1 && this.destFrequency == -1)
			{
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 5, new Object[] { 5, this.xCoord, this.yCoord, this.zCoord, 0 }));
			}
		}
	}

	@Override
	public String getOwnerName()
	{
		return this.ownerName;
	}

	@Override
	public void setOwnerName(String ownerName)
	{
		this.ownerName = ownerName;
	}

	@Override
	public void invalidate()
	{
		super.invalidate();

		if (this.chunkLoadTicket != null)
		{
			ForgeChunkManager.releaseTicket(this.chunkLoadTicket);
		}
	}

	@Override
	public void onTicketLoaded(Ticket ticket, boolean placed)
	{
		if (!this.worldObj.isRemote && GCMarsConfigManager.launchControllerChunkLoad)
		{
			if (ticket == null)
			{
				return;
			}

			if (this.chunkLoadTicket == null)
			{
				this.chunkLoadTicket = ticket;
			}

			NBTTagCompound nbt = this.chunkLoadTicket.getModData();
			nbt.setInteger("ChunkLoaderTileX", this.xCoord);
			nbt.setInteger("ChunkLoaderTileY", this.yCoord);
			nbt.setInteger("ChunkLoaderTileZ", this.zCoord);

			for (int x = -2; x <= 2; x++)
			{
				for (int z = -2; z <= 2; z++)
				{
					int blockID = this.worldObj.getBlockId(this.xCoord + x, this.yCoord, this.zCoord + z);

					if (blockID > 0 && Block.blocksList[blockID] instanceof GCCoreBlockLandingPadFull)
					{
						if (this.xCoord + x >> 4 != this.xCoord >> 4 || this.zCoord + z >> 4 != this.zCoord >> 4)
						{
							this.connectedPads.add(new ChunkCoordinates(this.xCoord + x, this.yCoord, this.zCoord + z));

							if (placed)
							{
								ChunkLoadingCallback.forceChunk(this.chunkLoadTicket, this.worldObj, this.xCoord + x, this.yCoord, this.zCoord + z, this.getOwnerName());
							}
							else
							{
								ChunkLoadingCallback.addToList(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.getOwnerName());
							}
						}
					}
				}
			}

			ChunkLoadingCallback.forceChunk(this.chunkLoadTicket, this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.getOwnerName());
		}
	}

	@Override
	public Ticket getTicket()
	{
		return this.chunkLoadTicket;
	}

	@Override
	public ChunkCoordinates getCoords()
	{
		return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList var2 = nbt.getTagList("Items");
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length)
			{
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		this.ownerName = nbt.getString("OwnerName");
		this.launchDropdownSelection = nbt.getInteger("LaunchSelection");
		this.frequency = nbt.getInteger("ControllerFrequency");
		this.destFrequency = nbt.getInteger("TargetFrequency");
		this.launchPadRemovalDisabled = nbt.getBoolean("LaunchPadRemovalDisabled");
		this.launchSchedulingEnabled = nbt.getBoolean("LaunchPadSchedulingEnabled");
		this.requiresClientUpdate = true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
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

		nbt.setTag("Items", var2);
		nbt.setString("OwnerName", this.ownerName);
		nbt.setInteger("LaunchSelection", this.launchDropdownSelection);
		nbt.setInteger("ControllerFrequency", this.frequency);
		nbt.setInteger("TargetFrequency", this.destFrequency);
		nbt.setBoolean("LaunchPadRemovalDisabled", this.launchPadRemovalDisabled);
		nbt.setBoolean("LaunchPadSchedulingEnabled", this.launchSchedulingEnabled);
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
	public String getInvName()
	{
		return StatCollector.translateToLocal("container.launchcontroller.name");
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
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
	{
		return slotID == 0 ? itemStack.getItem() instanceof IItemElectric : false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack par2ItemStack, int par3)
	{
		return this.isItemValidForSlot(slotID, par2ItemStack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack par2ItemStack, int par3)
	{
		return slotID == 0;
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalOutputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public float getProvide(ForgeDirection direction)
	{
		return 0;
	}

	@Override
	public boolean shouldPullEnergy()
	{
		return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
	}

	@Override
	public boolean shouldUseEnergy()
	{
		return !this.getDisabled(0);
	}

	@Override
	public ForgeDirection getElectricInputDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() - GCMarsBlockMachine.LAUNCH_CONTROLLER_METADATA + 2);
	}

	@Override
	public ItemStack getBatteryInSlot()
	{
		return this.getStackInSlot(0);
	}

	@Override
	public void setDisabled(int index, boolean disabled)
	{
		if (this.disableCooldown == 0)
		{
			switch (index)
			{
			case 0:
				this.disabled = disabled;
				break;
			case 1:
				this.launchSchedulingEnabled = disabled;
				break;
			}

			this.disableCooldown = 20;
		}
	}

	@Override
	public boolean getDisabled(int index)
	{
		switch (index)
		{
		case 0:
			return this.disabled;
		case 1:
			return this.launchSchedulingEnabled;
		}

		return true;
	}

	@Override
	public boolean canAttachToLandingPad(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);

		return tile instanceof GCCoreTileEntityLandingPad;
	}

	public void setFrequency(int frequency)
	{
		this.frequency = frequency;

		if (this.frequency >= 0)
		{
			this.frequencyValid = true;

			worldLoop:
			for (int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().worldServers.length; i++)
			{
				WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[i];

				for (int j = 0; j < world.loadedTileEntityList.size(); j++)
				{
					TileEntity tile2 = (TileEntity) world.loadedTileEntityList.get(j);

					if (this != tile2)
					{
						tile2 = world.getBlockTileEntity(tile2.xCoord, tile2.yCoord, tile2.zCoord);

						if (tile2 instanceof GCMarsTileEntityLaunchController)
						{
							GCMarsTileEntityLaunchController launchController2 = (GCMarsTileEntityLaunchController) tile2;

							if (launchController2.frequency == this.frequency)
							{
								this.frequencyValid = false;
								break worldLoop;
							}
						}
					}
				}
			}
		}
		else
		{
			this.frequencyValid = false;
		}
	}

	public void setDestinationFrequency(int frequency)
	{
		this.destFrequency = frequency;

		if (this.destFrequency >= 0)
		{
			this.destFrequencyValid = false;

			worldLoop:
			for (int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().worldServers.length; i++)
			{
				WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[i];

				for (int j = 0; j < world.loadedTileEntityList.size(); j++)
				{
					TileEntity tile2 = (TileEntity) world.loadedTileEntityList.get(j);

					if (this != tile2)
					{
						tile2 = world.getBlockTileEntity(tile2.xCoord, tile2.yCoord, tile2.zCoord);

						if (tile2 instanceof GCMarsTileEntityLaunchController)
						{
							GCMarsTileEntityLaunchController launchController2 = (GCMarsTileEntityLaunchController) tile2;

							if (launchController2.frequency == this.destFrequency)
							{
								this.destFrequencyValid = true;
								break worldLoop;
							}
						}
					}
				}
			}
		}
		else
		{
			this.destFrequencyValid = false;
		}
	}

	public boolean validFrequency()
	{
		return !this.getDisabled(0) && this.getEnergyStored() > 0 && this.frequencyValid && this.destFrequencyValid;
	}
}
