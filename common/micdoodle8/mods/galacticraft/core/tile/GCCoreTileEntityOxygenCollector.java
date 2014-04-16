package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreTileEntityOxygenCollector.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityOxygenCollector extends GCCoreTileEntityOxygen implements IInventory, ISidedInventory
{
	public boolean active;
	public static final float WATTS_PER_TICK = 0.2F;
	public static final int OUTPUT_PER_TICK = 100;
	@NetworkedField(targetSide = Side.CLIENT)
	public float lastOxygenCollected;
	private ItemStack[] containingItems = new ItemStack[1];

	public GCCoreTileEntityOxygenCollector()
	{
		super(GCCoreTileEntityOxygenCollector.WATTS_PER_TICK, 50, 1200, 0);
	}

	@Override
	public int getCappedScaledOxygenLevel(int scale)
	{
		return (int) Math.max(Math.min(Math.floor((double) this.storedOxygen / (double) this.maxOxygen * scale), scale), 0);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			this.produceOxygen();
			// if (this.getEnergyStored() > 0)
			// {
			// int gasToSend = Math.min(this.storedOxygen,
			// GCCoreTileEntityOxygenCollector.OUTPUT_PER_TICK);
			// GasStack toSend = new GasStack(GalacticraftCore.gasOxygen,
			// gasToSend);
			// this.storedOxygen -= GasTransmission.emitGasToNetwork(toSend,
			// this, this.getOxygenOutputDirection());
			//
			// Vector3 thisVec = new Vector3(this);
			// TileEntity tileEntity =
			// thisVec.modifyPositionFromSide(this.getOxygenOutputDirection()).getTileEntity(this.worldObj);
			//
			// if (tileEntity instanceof IGasAcceptor)
			// {
			// if (((IGasAcceptor)
			// tileEntity).canReceiveGas(this.getOxygenOutputDirection().getOpposite(),
			// GalacticraftCore.gasOxygen))
			// {
			// double sendingGas = 0;
			//
			// if (this.storedOxygen >=
			// GCCoreTileEntityOxygenCollector.OUTPUT_PER_TICK)
			// {
			// sendingGas = GCCoreTileEntityOxygenCollector.OUTPUT_PER_TICK;
			// }
			// else
			// {
			// sendingGas = this.storedOxygen;
			// }
			//
			// this.storedOxygen -= sendingGas - ((IGasAcceptor)
			// tileEntity).receiveGas(new GasStack(GalacticraftCore.gasOxygen,
			// (int) Math.floor(sendingGas)));
			// }
			// }
			// }

			// The later calculations are more efficient if power is a float, so
			// there are fewer casts
			float power = 0;
			int breatheableAirID = GCCoreBlocks.breatheableAir.blockID;

			if (this.getEnergyStored() > 0)
			{
				if (this.worldObj.provider instanceof IGalacticraftWorldProvider)
				{
					// Pre-test to see if close to the map edges, so code
					// doesn't have to continually test for map edges inside the
					// loop
					if (this.xCoord > -29999995 && this.xCoord < 2999995 && this.zCoord > -29999995 && this.zCoord < 29999995)
					{
						// Test the y coordinates, so code doesn't have to keep
						// testing that either
						int miny = this.yCoord - 5;
						int maxy = this.yCoord + 5;
						if (miny < 0)
						{
							miny = 0;
						}
						if (maxy >= this.worldObj.getHeight())
						{
							maxy = this.worldObj.getHeight() - 1;
						}

						// Loop the x and the z first, so the y loop will be at
						// fixed (x,z) coordinates meaning fixed chunk
						// coordinates
						for (int x = this.xCoord - 5; x <= this.xCoord + 5; x++)
						{
							int chunkx = x >> 4;
							int intrachunkx = x & 15;
							// Preload the first chunk for the z loop - there
							// can be a maximum of 2 chunks in the z loop
							int chunkz = (this.zCoord - 5) >> 4;
							Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkx, chunkz);
							for (int z = this.zCoord - 5; z <= this.zCoord + 5; z++)
							{
								if ((z >> 4) != chunkz)
								{
									// moved across z chunk boundary into a new
									// chunk, so load the new chunk
									chunkz = z >> 4;
									chunk = this.worldObj.getChunkFromChunkCoords(chunkx, chunkz);
								}
								for (int y = miny; y <= maxy; y++)
								{
									// chunk.getBlockID is like world.getBlockId
									// but faster - needs to be given
									// intra-chunk coordinates though
									final int blockID = chunk.getBlockID(intrachunkx, y, z & 15);
									// Test for the two most common blocks (air
									// and breatheable air) without looking up
									// in the blocksList
									if (blockID != 0 && blockID != breatheableAirID)
									{
										final Block block = Block.blocksList[blockID];

										if (block.isLeaves(this.worldObj, x, y, z) || block instanceof IPlantable && ((IPlantable) block).getPlantType(this.worldObj, x, y, z) == EnumPlantType.Crop)
										{
											power += 0.075F;
										}
									}
								}
							}
						}
					}
				}
				else
				{
					power = 9.3F;
				}

				power = (float) Math.floor(power);

				this.lastOxygenCollected = power;

				this.storedOxygen = (int) Math.max(Math.min(this.storedOxygen + power, this.maxOxygen), 0);
			}
			else
			{
				this.lastOxygenCollected = 0;
			}
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
		return StatCollector.translateToLocal("container.oxygencollector.name");
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
		return this.storedOxygen > 0F;
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
		return false;
	}

	@Override
	public boolean shouldUseOxygen()
	{
		return false;
	}

	@Override
	public EnumSet<ForgeDirection> getOxygenInputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public EnumSet<ForgeDirection> getOxygenOutputDirections()
	{
		return EnumSet.of(this.getElectricInputDirection().getOpposite());
	}

	@Override
	public float getOxygenProvide(ForgeDirection direction)
	{
		return this.getOxygenOutputDirections().contains(direction) ? Math.min(GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.getOxygenStored()) : 0.0F;
	}
}
