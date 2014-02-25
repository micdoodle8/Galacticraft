package micdoodle8.mods.galacticraft.mars.tile;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricBlock;
import micdoodle8.mods.galacticraft.mars.entities.EntityTerraformBubble;
import micdoodle8.mods.galacticraft.mars.world.gen.WorldGenTerraformTree;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import cpw.mods.fml.relauncher.Side;

/**
 * GCMarsTileEntityTerraformer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TileEntityTerraformer extends TileEntityElectricBlock implements IInventory, ISidedInventory, IDisableableMachine
{
	private final int tankCapacity = 2000;
	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank waterTank = new FluidTank(this.tankCapacity);
	public boolean active;
	public boolean lastActive;
	public static final float WATTS_PER_TICK = 0.2F;
	private ItemStack[] containingItems = new ItemStack[14];
	@NetworkedField(targetSide = Side.CLIENT)
	public EntityTerraformBubble terraformBubble;
	private ArrayList<Vector3> terraformableBlocksList = new ArrayList<Vector3>();
	private ArrayList<Vector3> grassBlockList = new ArrayList<Vector3>();
	@NetworkedField(targetSide = Side.CLIENT)
	public int terraformableBlocksListSize = 0; // used for server->client ease
	@NetworkedField(targetSide = Side.CLIENT)
	public int grassBlocksListSize = 0; // used for server->client ease
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean treesDisabled;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean grassDisabled;
	@NetworkedField(targetSide = Side.CLIENT)
	public float size;
	public final double MAX_SIZE = 15.0D;
	private int[] useCount = new int[2];

	public TileEntityTerraformer()
	{
		super(TileEntityTerraformer.WATTS_PER_TICK, 50);
	}

	public int getScaledWaterLevel(int i)
	{
		final double fuelLevel = this.waterTank.getFluid() == null ? 0 : this.waterTank.getFluid().amount;

		return (int) (fuelLevel * i / this.tankCapacity);
	}

	@Override
	public void invalidate()
	{
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

		if (this.terraformBubble == null)
		{
			if (!this.worldObj.isRemote)
			{
				this.terraformBubble = new EntityTerraformBubble(this.worldObj, new Vector3(this), this);
				this.terraformBubble.setPosition(this.xCoord, this.yCoord, this.zCoord);
				this.worldObj.spawnEntityInWorld(this.terraformBubble);
			}
		}

		if (!this.worldObj.isRemote)
		{
			if (this.containingItems[0] != null)
			{
				final FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(this.containingItems[0]);

				if (liquid != null && liquid.getFluid().getName().equals(FluidRegistry.WATER.getName()))
				{
					if (this.waterTank.getFluid() == null || this.waterTank.getFluid().amount + liquid.amount <= this.waterTank.getCapacity())
					{
						this.waterTank.fill(liquid, true);

						if (FluidContainerRegistry.isBucket(this.containingItems[0]) && FluidContainerRegistry.isFilledContainer(this.containingItems[0]))
						{
							this.containingItems[0] = new ItemStack(Items.bucket, this.containingItems[0].stackSize);
						}
						else
						{
							this.containingItems[0].stackSize--;

							if (this.containingItems[0].stackSize == 0)
							{
								this.containingItems[0] = null;
							}
						}
					}
				}
			}

			if (this.terraformBubble.getSize() == this.MAX_SIZE && this.getEnergyStored() > 0 && this.getFirstBonemealStack() != null && this.waterTank.getFluid() != null && this.waterTank.getFluid().amount > 0)
			{
				this.active = true;
			}
			else
			{
				this.active = false;
			}
		}

		if (!this.worldObj.isRemote && (this.active != this.lastActive || this.ticks % 20 == 0))
		{
			this.terraformableBlocksList.clear();
			this.grassBlockList.clear();

			if (this.active)
			{
				for (int x = (int) Math.floor(this.xCoord - this.terraformBubble.getSize()); x < Math.ceil(this.xCoord + this.terraformBubble.getSize()); x++)
				{
					for (int y = (int) Math.floor(this.yCoord - this.terraformBubble.getSize()); y < Math.ceil(this.yCoord + this.terraformBubble.getSize()); y++)
					{
						for (int z = (int) Math.floor(this.zCoord - this.terraformBubble.getSize()); z < Math.ceil(this.zCoord + this.terraformBubble.getSize()); z++)
						{
							Block block = this.worldObj.getBlock(x, y, z);
							Block blockIDAbove = this.worldObj.getBlock(x, y + 1, z);

							if (this.getDistanceFromServer(x, y, z) < this.terraformBubble.getSize() * this.terraformBubble.getSize())
							{
								if (block instanceof ITerraformableBlock && ((ITerraformableBlock) block).isTerraformable(this.worldObj, x, y, z))
								{
									if (!this.grassDisabled && this.getFirstSeedStack() != null)
									{
										this.terraformableBlocksList.add(new Vector3(x, y, z));
									}
								}
								else if (block == Blocks.grass && blockIDAbove.isAir(this.worldObj, x, y + 1, z))
								{
									if (!this.treesDisabled && this.getFirstSaplingStack() != null)
									{
										this.grassBlockList.add(new Vector3(x, y, z));
									}
								}
							}
						}
					}
				}
			}
		}

		if (!this.worldObj.isRemote && this.terraformableBlocksList.size() > 0 && this.ticks % 15 == 0)
		{
			ArrayList<Vector3> terraformableBlocks2 = new ArrayList<Vector3>(this.terraformableBlocksList);

			int randomIndex = this.worldObj.rand.nextInt(this.terraformableBlocksList.size());
			Vector3 vec = terraformableBlocks2.get(randomIndex);

			Block id = Blocks.air;

			switch (this.worldObj.rand.nextInt(40))
			{
			case 0:
				if (this.worldObj.isBlockNormalCubeDefault(vec.intX() - 1, vec.intY(), vec.intZ(), false) && this.worldObj.isBlockNormalCubeDefault(vec.intX() + 1, vec.intY(), vec.intZ(), false) && this.worldObj.isBlockNormalCubeDefault(vec.intX(), vec.intY(), vec.intZ() - 1, false) && this.worldObj.isBlockNormalCubeDefault(vec.intX(), vec.intY(), vec.intZ() + 1, false))
				{
					id = Blocks.flowing_water;
				}
				else
				{
					id = Blocks.grass;
				}
				break;
			default:
				id = Blocks.grass;
				break;
			}

			this.worldObj.setBlock(vec.intX(), vec.intY(), vec.intZ(), id);

			if (id == Blocks.grass)
			{
				this.useCount[0]++;
				this.waterTank.drain(1, true);
				this.checkUsage(1);
			}
			else if (id == Blocks.flowing_water)
			{
				this.checkUsage(2);
			}

			this.terraformableBlocksList.remove(randomIndex);
		}

		if (!this.worldObj.isRemote && !this.treesDisabled && this.grassBlockList.size() > 0 && this.ticks % 50 == 0)
		{
			ArrayList<Vector3> grassBlocks2 = new ArrayList<Vector3>(this.grassBlockList);

			int randomIndex = this.worldObj.rand.nextInt(this.grassBlockList.size());
			Vector3 vec = grassBlocks2.get(randomIndex);

			if (new WorldGenTerraformTree(true).generate(this.worldObj, this.worldObj.rand, vec.intX(), vec.intY(), vec.intZ()))
			{
				this.useCount[1]++;
				this.waterTank.drain(50, true);
				this.checkUsage(0);
			}

			this.grassBlockList.remove(randomIndex);
		}

		if (!this.worldObj.isRemote)
		{
			this.terraformableBlocksListSize = this.terraformableBlocksList.size();
			this.grassBlocksListSize = this.grassBlockList.size();
		}

		if (this.getEnergyStored() > 0.0F && (!this.grassDisabled || !this.treesDisabled))
		{
			this.size = (float) Math.min(Math.max(0, this.size + 0.1F), this.MAX_SIZE);
		}
		else
		{
			this.size = (float) Math.min(Math.max(0, this.size - 0.1F), this.MAX_SIZE);
		}

		this.lastActive = this.active;
	}

	private void checkUsage(int type)
	{
		ItemStack stack = null;

		if ((this.useCount[0] + this.useCount[1]) % 4 == 0)
		{
			stack = this.getFirstBonemealStack();

			if (stack != null)
			{
				stack.stackSize--;

				if (stack.stackSize <= 0)
				{
					this.containingItems[this.getSelectiveStack(2, 6)] = null;
				}
			}
		}

		switch (type)
		{
		case 0:
			stack = this.getFirstSaplingStack();

			if (stack != null)
			{
				stack.stackSize--;

				if (stack.stackSize <= 0)
				{
					this.containingItems[this.getSelectiveStack(6, 10)] = null;
				}
			}
			break;
		case 1:
			if (this.useCount[0] % 4 == 0)
			{
				stack = this.getFirstSeedStack();

				if (stack != null)
				{
					stack.stackSize--;

					if (stack.stackSize <= 0)
					{
						this.containingItems[this.getSelectiveStack(10, 14)] = null;
					}
				}
			}
			break;
		case 2:
			this.waterTank.drain(50, true);
			break;
		}
	}

	private int getSelectiveStack(int start, int end)
	{
		for (int i = start; i < end; i++)
		{
			ItemStack stack = this.containingItems[i];

			if (stack != null)
			{
				return i;
			}
		}

		return -1;
	}

	public ItemStack getFirstBonemealStack()
	{
		int index = this.getSelectiveStack(2, 6);

		if (index != -1)
		{
			return this.containingItems[index];
		}

		return null;
	}

	public ItemStack getFirstSaplingStack()
	{
		int index = this.getSelectiveStack(6, 10);

		if (index != -1)
		{
			return this.containingItems[index];
		}

		return null;
	}

	public ItemStack getFirstSeedStack()
	{
		int index = this.getSelectiveStack(10, 14);

		if (index != -1)
		{
			return this.containingItems[index];
		}

		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		final NBTTagList var2 = nbt.getTagList("Items", 10);
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

		this.size = nbt.getFloat("BubbleSize");
		this.useCount = nbt.getIntArray("UseCountArray");

		if (this.useCount.length == 0)
		{
			this.useCount = new int[2];
		}

		if (nbt.hasKey("waterTank"))
		{
			this.waterTank.readFromNBT(nbt.getCompoundTag("waterTank"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

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
		nbt.setFloat("BubbleSize", this.size);
		nbt.setIntArray("UseCountArray", this.useCount);

		if (this.waterTank.getFluid() != null)
		{
			nbt.setTag("waterTank", this.waterTank.writeToNBT(new NBTTagCompound()));
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
	public String getInventoryName()
	{
		return StatCollector.translateToLocal("container.oxygendistributor.name");
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

	// ISidedInventory Implementation:

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[] { 1 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		return this.isItemValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
	{
		return slotID == 1;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		return slotID == 1 ? itemstack.getItem() instanceof IItemElectric : false;
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
	public boolean shouldPullEnergy()
	{
		return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
	}

	@Override
	public boolean shouldUseEnergy()
	{
		return !this.grassDisabled || !this.treesDisabled;
	}

	@Override
	public void addExtraNetworkedData(List<Object> networkedList)
	{
		ItemStack stack1 = this.getFirstBonemealStack();
		ItemStack stack2 = this.getFirstSaplingStack();
		ItemStack stack3 = this.getFirstSeedStack();
		networkedList.add(stack1 == null ? -1 : this.getSelectiveStack(2, 6));
		networkedList.add(stack1 == null ? -1 : Item.getIdFromItem(stack1.getItem()));
		networkedList.add(stack1 == null ? -1 : stack1.stackSize);
		networkedList.add(stack1 == null ? -1 : stack1.getItemDamage());
		networkedList.add(stack2 == null ? -1 : this.getSelectiveStack(6, 4));
		networkedList.add(stack2 == null ? -1 : Item.getIdFromItem(stack2.getItem()));
		networkedList.add(stack2 == null ? -1 : stack2.stackSize);
		networkedList.add(stack2 == null ? -1 : stack2.getItemDamage());
		networkedList.add(stack3 == null ? -1 : this.getSelectiveStack(10, 14));
		networkedList.add(stack3 == null ? -1 : Item.getIdFromItem(stack3.getItem()));
		networkedList.add(stack3 == null ? -1 : stack3.stackSize);
		networkedList.add(stack3 == null ? -1 : stack3.getItemDamage());
	}

	@Override
	public void readExtraNetworkedData(ByteBuf buffer)
	{
		int firstStack = -1;
		Item itemID = null;
		int stackSize = -1;
		int stackMetadata = -1;

		for (int i = 0; i < 3; i++)
		{
			firstStack = buffer.readInt();
			itemID = Item.getItemById(buffer.readInt());
			stackSize = buffer.readInt();
			stackMetadata = buffer.readInt();

			if (firstStack != -1)
			{
				this.containingItems[firstStack] = new ItemStack(itemID, stackSize, stackMetadata);
			}
		}
	}

	@Override
	public double getPacketRange()
	{
		return 320.0D;
	}

	@Override
	public ForgeDirection getElectricInputDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public ItemStack getBatteryInSlot()
	{
		return this.getStackInSlot(1);
	}

	@Override
	public void setDisabled(int index, boolean disabled)
	{
		if (this.disableCooldown <= 0)
		{
			switch (index)
			{
			case 0:
				this.treesDisabled = !this.treesDisabled;
				break;
			case 1:
				this.grassDisabled = !this.grassDisabled;
				break;
			}

			this.disableCooldown = 10;
		}
	}

	@Override
	public boolean getDisabled(int index)
	{
		switch (index)
		{
		case 0:
			return this.treesDisabled;
		case 1:
			return this.grassDisabled;
		}

		return false;
	}
}
