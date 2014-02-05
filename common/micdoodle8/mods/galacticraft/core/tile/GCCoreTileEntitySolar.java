package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;
import java.util.HashSet;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockSolar;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreTileEntitySolar.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntitySolar extends GCCoreTileEntityUniversalElectrical implements IMultiBlock, IPacketReceiver, IDisableableMachine, IInventory, ISidedInventory
{
	public HashSet<TileEntity> connectedTiles = new HashSet<TileEntity>();
	@NetworkedField(targetSide = Side.CLIENT)
	public Vector3 mainBlockPosition;
	@NetworkedField(targetSide = Side.CLIENT)
	public int solarStrength = 0;
	public float targetAngle;
	public float currentAngle;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean disabled = true;
	@NetworkedField(targetSide = Side.CLIENT)
	public int disableCooldown = 0;
	private ItemStack[] containingItems = new ItemStack[1];
	public static final float MAX_GENERATE_WATTS = 15.0F;
	@NetworkedField(targetSide = Side.CLIENT)
	public float generateWatts = 0;
	private float ueMaxEnergy;

	public GCCoreTileEntitySolar()
	{
		this(0);
	}

	public GCCoreTileEntitySolar(float maxEnergy)
	{
		this.ueMaxEnergy = maxEnergy;
	}

	public void setMainBlock(Vector3 mainBlock)
	{
		this.mainBlockPosition = mainBlock;

		if (!this.worldObj.isRemote)
		{
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	@Override
	public void updateEntity()
	{
		this.setEnergyStored(this.getEnergyStored() + this.generateWatts);

		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			this.recharge(this.containingItems[0]);

			if (this.disableCooldown > 0)
			{
				this.disableCooldown--;
			}

			if (!this.getDisabled(0) && this.ticks % 20 == 0)
			{
				this.solarStrength = 0;

				if (this.worldObj.isDaytime() && (this.worldObj.provider instanceof IGalacticraftWorldProvider || !this.worldObj.isRaining() && !this.worldObj.isThundering()))
				{
					double distance = 100.0D;
					double sinA = -Math.sin((this.currentAngle - 77.5D) * Math.PI / 180.0D);
					double cosA = Math.abs(Math.cos((this.currentAngle - 77.5D) * Math.PI / 180.0D));

					for (int x = -1; x <= 1; x++)
					{
						for (int z = -1; z <= 1; z++)
						{
							if (this.getBlockMetadata() < GCCoreBlockSolar.ADVANCED_METADATA)
							{
								if (this.worldObj.canBlockSeeTheSky(this.xCoord + x, this.yCoord + 2, this.zCoord + z))
								{
									boolean valid = true;

									for (int y = this.yCoord + 3; y < 256; y++)
									{
										int blockID = this.worldObj.getBlockId(this.xCoord + x, y, this.zCoord + z);

										if (blockID != 0 && Block.blocksList[blockID].isOpaqueCube())
										{
											valid = false;
											break;
										}
									}

									if (valid)
									{
										this.solarStrength++;
									}
								}
							}
							else
							{
								boolean valid = true;

								for (double d = 0.0D; d < distance; d++)
								{
									Vector3 thisVec = new Vector3(this);
									Vector3 blockAt = thisVec.clone().translate(new Vector3(x, 3, z)).clone().translate(new Vector3(d * sinA, d * cosA, 0));
									int blockID = blockAt.getBlockID(this.worldObj);

									if (blockID != 0 && Block.blocksList[blockID].isOpaqueCube())
									{
										valid = false;
										break;
									}
								}

								if (valid)
								{
									this.solarStrength++;
								}
							}
						}
					}
				}
			}
		}

		float angle = this.worldObj.getCelestialAngle(1.0F) - 0.7845194F < 0 ? 1.0F - 0.7845194F : -0.7845194F;
		float celestialAngle = (this.worldObj.getCelestialAngle(1.0F) + angle) * 360.0F;

		celestialAngle %= 360;

		if (this.getBlockMetadata() >= GCCoreBlockSolar.ADVANCED_METADATA)
		{
			if (celestialAngle > 30 && celestialAngle < 150)
			{
				float difference = this.targetAngle - celestialAngle;

				this.targetAngle -= difference / 20.0F;
			}
			else if (!this.worldObj.isDaytime() || this.worldObj.isRaining() || this.worldObj.isThundering())
			{
				this.targetAngle = 77.5F + 180.0F;
			}
			else if (celestialAngle < 50)
			{
				this.targetAngle = 50;
			}
			else if (celestialAngle > 150)
			{
				this.targetAngle = 150;
			}
		}
		else
		{
			if (!this.worldObj.isDaytime() || this.worldObj.isRaining() || this.worldObj.isThundering())
			{
				this.targetAngle = 77.5F + 180.0F;
			}
			else
			{
				this.targetAngle = 77.5F;
			}
		}

		float difference = this.targetAngle - this.currentAngle;

		this.currentAngle += difference / 20.0F;

		if (!this.worldObj.isRemote)
		{
			if (this.getGenerate() > 0.0F)
			{
				this.generateWatts = Math.min(Math.max(this.getGenerate(), 0), GCCoreTileEntitySolar.MAX_GENERATE_WATTS) / 20.0F;
			}
			else
			{
				this.generateWatts = 0.0F;
			}
		}

		this.produce();
	}

	public float getGenerate()
	{
		if (this.getDisabled(0))
		{
			return 0.0F;
		}

		float angle = this.worldObj.getCelestialAngle(1.0F) - 0.784690560F < 0 ? 1.0F - 0.784690560F : -0.784690560F;
		float celestialAngle = (this.worldObj.getCelestialAngle(1.0F) + angle) * 360.0F;

		celestialAngle %= 360;

		float difference = (180.0F - Math.abs(this.currentAngle % 180 - celestialAngle)) / 180.0F;

		return 1 / 1000.0F * difference * difference * (this.solarStrength * (Math.abs(difference) * 500.0F)) * this.getSolarBoost();
	}

	public float getSolarBoost()
	{
		return (float) (this.worldObj.provider instanceof ISolarLevel ? ((ISolarLevel) this.worldObj.provider).getSolarEnergyMultiplier() : 1.0F);
	}

	public void onBlockRemoval()
	{
		if (this.mainBlockPosition != null)
		{
			TileEntity tileEntity = this.worldObj.getBlockTileEntity(this.mainBlockPosition.intX(), this.mainBlockPosition.intY(), this.mainBlockPosition.intZ());

			if (tileEntity != null && tileEntity instanceof IMultiBlock)
			{
				IMultiBlock mainBlock = (IMultiBlock) tileEntity;

				if (mainBlock != null)
				{
					mainBlock.onDestroy(this);
				}
			}
		}
	}

	@Override
	public boolean onActivated(EntityPlayer entityPlayer)
	{
		return this.getBlockType().onBlockActivated(this.worldObj, this.xCoord, this.yCoord, this.zCoord, entityPlayer, 0, this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void onCreate(Vector3 placedPosition)
	{
		this.mainBlockPosition = placedPosition;

		for (int y = 1; y <= 2; y++)
		{
			for (int x = -1; x < 2; x++)
			{
				for (int z = -1; z < 2; z++)
				{
					final Vector3 vecToAdd = new Vector3(placedPosition.x + (y == 2 ? x : 0), placedPosition.y + y, placedPosition.z + (y == 2 ? z : 0));

					if (!vecToAdd.equals(placedPosition))
					{
						((GCCoreBlockMulti) GCCoreBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 4);
					}
				}
			}
		}
	}

	@Override
	public void onDestroy(TileEntity callingBlock)
	{
		final Vector3 thisBlock = new Vector3(this);

		for (int y = 1; y <= 2; y++)
		{
			for (int x = -1; x < 2; x++)
			{
				for (int z = -1; z < 2; z++)
				{
					if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
					{
						FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + (y == 2 ? x : 0), thisBlock.intY() + y, thisBlock.intZ() + (y == 2 ? z : 0), GCCoreBlocks.solarPanel.blockID & 4095, GCCoreBlocks.solarPanel.blockID >> 12 & 255);
					}
					this.worldObj.destroyBlock(thisBlock.intX() + (y == 2 ? x : 0), thisBlock.intY() + y, thisBlock.intZ() + (y == 2 ? z : 0), false);
				}
			}
		}

		this.worldObj.destroyBlock(thisBlock.intX(), thisBlock.intY(), thisBlock.intZ(), true);
		this.worldObj.setBlock(thisBlock.intX(), thisBlock.intY(), thisBlock.intZ(), 0, 0, 3);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.mainBlockPosition = new Vector3(nbt.getCompoundTag("mainBlockPosition"));
		this.ueMaxEnergy = nbt.getFloat("maxEnergy");

		if (this.ueMaxEnergy > 1000.0F)
		{
			this.ueMaxEnergy /= 1000.0F;
		}

		this.currentAngle = nbt.getFloat("currentAngle");
		this.targetAngle = nbt.getFloat("targetAngle");
		this.setDisabled(0, nbt.getBoolean("disabled"));
		this.disableCooldown = nbt.getInteger("disabledCooldown");

		final NBTTagList var2 = nbt.getTagList("Items");
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
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		if (this.mainBlockPosition != null)
		{
			nbt.setCompoundTag("mainBlockPosition", this.mainBlockPosition.writeToNBT(new NBTTagCompound()));
		}

		nbt.setFloat("maxEnergy", this.getMaxEnergyStored());
		nbt.setFloat("currentAngle", this.currentAngle);
		nbt.setFloat("targetAngle", this.targetAngle);
		nbt.setInteger("disabledCooldown", this.disableCooldown);
		nbt.setBoolean("disabled", this.getDisabled(0));

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
	public float getRequest(ForgeDirection direction)
	{
		return 0;
	}

	@Override
	public float getProvide(ForgeDirection direction)
	{
		return this.getElectricalOutputDirections().contains(direction) ? Math.min(Math.max(this.getEnergyStored(), 0), 1300) : 0;
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalInputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalOutputDirections()
	{
		int metadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);

		if (metadata >= GCCoreBlockSolar.ADVANCED_METADATA)
		{
			metadata -= GCCoreBlockSolar.ADVANCED_METADATA;
		}

		return EnumSet.of(ForgeDirection.getOrientation(metadata + 2).getOpposite(), ForgeDirection.UNKNOWN);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return TileEntity.INFINITE_EXTENT_AABB;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public String getInvName()
	{
		return StatCollector.translateToLocal(this.getBlockMetadata() < GCCoreBlockSolar.ADVANCED_METADATA ? "container.solarbasic.name" : "container.solaradvanced.name");
	}

	@Override
	public void setDisabled(int index, boolean disabled)
	{
		if (this.disableCooldown == 0)
		{
			this.disabled = disabled;
			this.disableCooldown = 20;
		}
	}

	@Override
	public boolean getDisabled(int index)
	{
		return this.disabled;
	}

	public int getScaledElecticalLevel(int i)
	{
		return (int) Math.floor(this.getEnergyStored() * i / this.getMaxEnergyStored());
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
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		return slotID == 0 ? itemstack.getItem() instanceof IItemElectric : false;
	}

	@Override
	public float getMaxEnergyStored()
	{
		return this.ueMaxEnergy;
	}
}
