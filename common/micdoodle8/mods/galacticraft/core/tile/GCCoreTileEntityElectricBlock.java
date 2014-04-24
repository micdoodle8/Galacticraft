package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreTileEntityElectric.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreTileEntityElectricBlock extends GCCoreTileEntityUniversalElectrical implements IPacketReceiver, IDisableableMachine
{
	public float ueWattsPerTick;
	private final float ueMaxEnergy;

	@NetworkedField(targetSide = Side.CLIENT)
	public boolean disabled = true;
	@NetworkedField(targetSide = Side.CLIENT)
	public int disableCooldown = 0;

	public abstract boolean shouldPullEnergy();

	public abstract boolean shouldUseEnergy();

	public abstract ForgeDirection getElectricInputDirection();

	public abstract ItemStack getBatteryInSlot();

	public GCCoreTileEntityElectricBlock(float ueWattsPerTick, float maxEnergy)
	{
		this.ueMaxEnergy = maxEnergy;
		this.ueWattsPerTick = ueWattsPerTick;

		/*
		 * if (PowerFramework.currentFramework != null) { this.bcPowerProvider =
		 * new GCCoreLinkedPowerProvider(this);
		 * this.bcPowerProvider.configure(20, 1, 10, 10, 1000); }
		 */
	}

	@Override
	public float getMaxEnergyStored()
	{
		return this.ueMaxEnergy;
	}

	public int getScaledElecticalLevel(int i)
	{
		return (int) Math.floor(this.getEnergyStored() * i / (this.getMaxEnergyStored() - this.ueWattsPerTick));
	}

	@Override
	public float getRequest(ForgeDirection direction)
	{
		if (this.shouldPullEnergy())
		{
			return this.ueWattsPerTick * 2;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public float getProvide(ForgeDirection direction)
	{
		return 0;
	}

	@Override
	public void updateEntity()
	{
		if (this.shouldPullEnergy() && this.getEnergyStored() < this.getMaxEnergyStored() && this.getBatteryInSlot() != null && this.getElectricInputDirection() != null)
		{
			if (!this.worldObj.isRemote)
			{
				this.discharge(this.getBatteryInSlot());
			}
			// this.receiveElectricity(this.getElectricInputDirection(),
			// ElectricityPack.getFromWatts(ElectricItemHelper.dischargeItem(this.getBatteryInSlot(),
			// this.getRequest(ForgeDirection.UNKNOWN)), this.getVoltage()),
			// true);
		}

		if (!this.worldObj.isRemote && this.shouldUseEnergy())
		{
			this.setEnergyStored(this.getEnergyStored() - this.ueWattsPerTick);
		}

		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.disableCooldown > 0)
			{
				this.disableCooldown--;
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setBoolean("isDisabled", this.getDisabled(0));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.setDisabled(0, nbt.getBoolean("isDisabled"));
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

	@RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return false;
	}

	@RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
	public short getFacing()
	{
		return (short) this.worldObj.getBlockMetadata(MathHelper.floor_double(this.xCoord), MathHelper.floor_double(this.yCoord), MathHelper.floor_double(this.zCoord));
	}

	@RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
	public void setFacing(short facing)
	{
		;
	}

	@RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return false;
	}

	@RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = "IC2")
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return Block.blocksList[this.getBlockType().blockID].getPickBlock(null, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalInputDirections()
	{
		if (this.getElectricInputDirection() == null)
		{
			return EnumSet.noneOf(ForgeDirection.class);
		}

		return EnumSet.of(this.getElectricInputDirection());
	}
	
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && entityplayer.getDistanceSq(this.xCoord+0.5D, this.yCoord+0.5D, this.zCoord+0.5D) <= 64.0D;
	}
}
