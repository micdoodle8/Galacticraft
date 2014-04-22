package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTransmission;
import mekanism.api.gas.IGasAcceptor;
import mekanism.api.gas.IGasHandler;
import micdoodle8.mods.galacticraft.api.transmission.NetworkHelper;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.IOxygenNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenReceiver;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenStorage;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.oxygen.BlockVec3;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreTileEntityOxygen.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreTileEntityOxygen extends GCCoreTileEntityElectricBlock implements IOxygenReceiver, IOxygenStorage
{
	public float maxOxygen;
	public float oxygenPerTick;
	@NetworkedField(targetSide = Side.CLIENT)
	public float storedOxygen;
	public float lastStoredOxygen;
	public static int timeSinceOxygenRequest;

	public GCCoreTileEntityOxygen(float wattsPerTick, float maxEnergy, float maxOxygen, float oxygenPerTick)
	{
		super(wattsPerTick, maxEnergy);
		this.maxOxygen = maxOxygen;
		this.oxygenPerTick = oxygenPerTick;
	}

	public int getScaledOxygenLevel(int scale)
	{
		return (int) Math.floor((this.getOxygenStored() * scale) / (this.getMaxOxygenStored() - this.oxygenPerTick));
	}

	public abstract boolean shouldUseOxygen();

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
			if (GCCoreTileEntityOxygen.timeSinceOxygenRequest > 0)
			{
				GCCoreTileEntityOxygen.timeSinceOxygenRequest--;
			}

			if (this.shouldUseOxygen())
			{
				this.storedOxygen = Math.max(this.storedOxygen - this.oxygenPerTick, 0);
			}
		}

		this.lastStoredOxygen = this.storedOxygen;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		if (nbt.hasKey("storedOxygen"))
		{
			this.storedOxygen = nbt.getInteger("storedOxygen");
		}
		else
		{
			this.storedOxygen = nbt.getFloat("storedOxygenF");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("storedOxygenF", this.storedOxygen);
	}

	@Override
	public void setOxygenStored(float oxygen)
	{
		this.storedOxygen = Math.max(Math.min(oxygen, this.getMaxOxygenStored()), 0);
	}

	@Override
	public float getOxygenStored()
	{
		return this.storedOxygen;
	}

	@Override
	public float getMaxOxygenStored()
	{
		return this.maxOxygen;
	}

	public EnumSet<ForgeDirection> getOxygenInputDirections()
	{
		return EnumSet.allOf(ForgeDirection.class);
	}

	public EnumSet<ForgeDirection> getOxygenOutputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public boolean canConnect(ForgeDirection direction, NetworkType type)
	{
		if (direction == null || direction.equals(ForgeDirection.UNKNOWN))
		{
			return false;
		}

		switch (type)
		{
		case OXYGEN:
			return this.getOxygenInputDirections().contains(direction) || this.getOxygenOutputDirections().contains(direction);
		case POWER:
			return super.canConnect(direction, type);
		}

		return false;
	}

	@Override
	public float receiveOxygen(ForgeDirection from, float receive, boolean doReceive)
	{
		if (this.getOxygenInputDirections().contains(from))
		{
			if (!doReceive)
			{
				return this.getOxygenRequest(from);
			}

			return this.receiveOxygen(receive, doReceive);
		}

		return 0;
	}

	public float receiveOxygen(float receive, boolean doReceive)
	{
		if (receive > 0)
		{
			float prevEnergyStored = this.getOxygenStored();
			float newStoredEnergy = Math.min(prevEnergyStored + receive, this.getMaxOxygenStored());

			if (doReceive)
			{
				GCCoreTileEntityOxygen.timeSinceOxygenRequest = 20;
				this.setOxygenStored(newStoredEnergy);
			}

			return Math.max(newStoredEnergy - prevEnergyStored, 0);
		}

		return 0;
	}

	@Override
	public float provideOxygen(ForgeDirection from, float request, boolean doProvide)
	{
		if (this.getOxygenOutputDirections().contains(from))
		{
			if (!doProvide)
			{
				return this.getProvide(from);
			}

			return this.provideOxygen(request, doProvide);
		}

		return 0;
	}

	public float provideOxygen(float request, boolean doProvide)
	{
		if (request > 0)
		{
			float requestedEnergy = Math.min(request, this.storedOxygen);

			if (doProvide)
			{
				this.setOxygenStored(this.storedOxygen - requestedEnergy);
			}

			return requestedEnergy;
		}

		return 0;
	}

	public void produceOxygen()
	{
		if (!this.worldObj.isRemote)
		{
			for (ForgeDirection direction : this.getOxygenOutputDirections())
			{
				if (direction != ForgeDirection.UNKNOWN)
				{
					this.produceOxygen(direction);
				}
			}
		}
	}

	public boolean produceOxygen(ForgeDirection outputDirection)
	{
		float provide = this.getOxygenProvide(outputDirection);

		if (provide > 0)
		{
			TileEntity outputTile = new BlockVec3(this).modifyPositionFromSide(outputDirection).getTileEntity(this.worldObj);
			IOxygenNetwork outputNetwork = NetworkHelper.getOxygenNetworkFromTileEntity(outputTile, outputDirection);

			if (outputNetwork != null)
			{
				float powerRequest = outputNetwork.getRequest(this);

				if (powerRequest > 0)
				{
					float toSend = Math.min(this.getOxygenStored(), provide);
					float rejectedPower = outputNetwork.produce(toSend, this);

					this.provideOxygen(Math.max(toSend - rejectedPower, 0), true);
					return true;
				}
			}
			else if (outputTile instanceof IOxygenReceiver)
			{
				float requestedEnergy = ((IOxygenReceiver) outputTile).getOxygenRequest(outputDirection.getOpposite());

				if (requestedEnergy > 0)
				{
					float toSend = Math.min(this.getOxygenStored(), provide);
					float acceptedOxygen = ((IOxygenReceiver) outputTile).receiveOxygen(outputDirection.getOpposite(), toSend, true);
					this.provideOxygen(acceptedOxygen, true);
					return true;
				}
			}
			else if (NetworkConfigHandler.isMekanismLoaded())
			{
				GasStack toSend = new GasStack((Gas) NetworkConfigHandler.gasOxygen, (int) Math.floor(Math.min(this.getOxygenStored(), provide)));
				int acceptedOxygen = GasTransmission.emitGasToNetwork(toSend, this, outputDirection);
				this.provideOxygen(acceptedOxygen, true);

				if (NetworkConfigHandler.isMekanismV6Loaded())
				{
					if (outputTile instanceof IGasHandler && ((IGasHandler) outputTile).canReceiveGas(outputDirection.getOpposite(), (Gas) NetworkConfigHandler.gasOxygen))
					{
						acceptedOxygen = ((IGasHandler) outputTile).receiveGas(outputDirection.getOpposite(), toSend);
						this.provideOxygen(acceptedOxygen, true);
						return true;
					}
				}
				else if (outputTile instanceof IGasAcceptor)
				{
					if (((IGasAcceptor) outputTile).canReceiveGas(outputDirection.getOpposite(), (Gas) NetworkConfigHandler.gasOxygen))
					{
						acceptedOxygen = toSend.amount - ((IGasAcceptor) outputTile).receiveGas(toSend);
						this.provideOxygen(acceptedOxygen, true);
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public float getOxygenRequest(ForgeDirection direction)
	{
		if (this.shouldPullOxygen())
		{
			return this.oxygenPerTick * 2;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public float getOxygenProvide(ForgeDirection direction)
	{
		return 0;
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
	public int receiveGas(ForgeDirection side, GasStack stack)
	{
		return (int) Math.floor(this.receiveOxygen(stack.amount, true));
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
	public GasStack drawGas(ForgeDirection side, int amount)
	{
		return new GasStack((Gas) NetworkConfigHandler.gasOxygen, (int) Math.floor(this.provideOxygen(amount, true)));
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
	public boolean canDrawGas(ForgeDirection side, Gas type)
	{
		return type.getName().equals("oxygen") && this.getOxygenOutputDirections().contains(side);
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasAcceptor", modID = "Mekanism")
	public int receiveGas(GasStack stack)
	{
		return (int) (stack.amount - Math.floor(this.receiveOxygen(stack.amount, true)));
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasAcceptor", altClasses = { "mekanism.api.gas.IGasHandler" }, modID = "Mekanism")
	public boolean canReceiveGas(ForgeDirection side, Gas type)
	{
		return type.getName().equals("oxygen") && this.getOxygenInputDirections().contains(side);
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = "Mekanism")
	public boolean canTubeConnect(ForgeDirection side)
	{
		return this.canConnect(side, NetworkType.OXYGEN);
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasStorage", modID = "Mekanism")
	public GasStack getGas(Object... data)
	{
		return new GasStack((Gas) NetworkConfigHandler.gasOxygen, (int) Math.floor(this.getOxygenStored()));
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasStorage", modID = "Mekanism")
	public void setGas(GasStack stack, Object... data)
	{
		this.setOxygenStored(stack.amount);
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasStorage", modID = "Mekanism")
	public int getMaxGas(Object... data)
	{
		return (int) Math.floor(this.getMaxOxygenStored());
	}
}
