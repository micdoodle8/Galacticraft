package micdoodle8.mods.galacticraft.core.tile;

import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;

import java.lang.reflect.Constructor;
import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityPack;
import micdoodle8.mods.galacticraft.api.transmission.NetworkHelper;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.core.item.ElectricItemHelper;
import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectricalStorage;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreTileEntityUniversalElectrical.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreTileEntityUniversalElectrical extends GCCoreTileEntityAdvanced implements IElectrical, IElectricalStorage
{
	protected boolean isAddedToEnergyNet;
	public Object bcPowerHandler;
	public float maxInputEnergy = 100;
	@NetworkedField(targetSide = Side.CLIENT)
	public float energyStored = 0;

	@Override
	public double getPacketRange()
	{
		return 12.0D;
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

	public EnumSet<ForgeDirection> getElectricalInputDirections()
	{
		return EnumSet.allOf(ForgeDirection.class);
	}

	public EnumSet<ForgeDirection> getElectricalOutputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive)
	{
		if (this.getElectricalInputDirections().contains(from))
		{
			if (!doReceive)
			{
				return this.getRequest(from);
			}

			return this.receiveElectricity(receive, doReceive);
		}

		return 0;
	}

	@Override
	public ElectricityPack provideElectricity(ForgeDirection from, ElectricityPack request, boolean doProvide)
	{
		if (this.getElectricalOutputDirections().contains(from))
		{
			if (!doProvide)
			{
				return ElectricityPack.getFromWatts(this.getProvide(from), this.getVoltage());
			}

			return this.provideElectricity(request, doProvide);
		}

		return new ElectricityPack();
	}

	/**
	 * A non-side specific version of receiveElectricity for you to optionally
	 * use it internally.
	 */
	public float receiveElectricity(ElectricityPack receive, boolean doReceive)
	{
		if (receive != null)
		{
			float prevEnergyStored = this.getEnergyStored();
			float newStoredEnergy = Math.min(this.getEnergyStored() + receive.getWatts(), this.getMaxEnergyStored());

			if (doReceive)
			{
				this.setEnergyStored(newStoredEnergy);
			}

			return Math.max(newStoredEnergy - prevEnergyStored, 0);
		}

		return 0;
	}

	public float receiveElectricity(float energy, boolean doReceive)
	{
		return this.receiveElectricity(ElectricityPack.getFromWatts(energy, this.getVoltage()), doReceive);
	}

	/**
	 * A non-side specific version of provideElectricity for you to optionally
	 * use it internally.
	 */
	public ElectricityPack provideElectricity(ElectricityPack request, boolean doProvide)
	{
		if (request != null)
		{
			float requestedEnergy = Math.min(request.getWatts(), this.energyStored);

			if (doProvide)
			{
				this.setEnergyStored(this.energyStored - requestedEnergy);
			}

			return ElectricityPack.getFromWatts(requestedEnergy, this.getVoltage());
		}

		return new ElectricityPack();
	}

	public ElectricityPack provideElectricity(float energy, boolean doProvide)
	{
		return this.provideElectricity(ElectricityPack.getFromWatts(energy, this.getVoltage()), doProvide);
	}

	@Override
	public void setEnergyStored(float energy)
	{
		this.energyStored = Math.max(Math.min(energy, this.getMaxEnergyStored()), 0);
	}

	@Override
	public float getEnergyStored()
	{
		return this.energyStored;
	}

	@Override
	public boolean canConnect(ForgeDirection direction, NetworkType type)
	{
		if (direction == null || direction.equals(ForgeDirection.UNKNOWN) || type != NetworkType.POWER)
		{
			return false;
		}

		return this.getElectricalInputDirections().contains(direction) || this.getElectricalOutputDirections().contains(direction);
	}

	@Override
	public float getVoltage()
	{
		return 0.120F;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.energyStored = nbt.getFloat("energyStored");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("energyStored", this.energyStored);
	}

	/**
	 * Recharges electric item.
	 */
	public void recharge(ItemStack itemStack)
	{
		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof IItemElectric)
			{
				this.setEnergyStored(this.getEnergyStored() - ElectricItemHelper.chargeItem(itemStack, this.getProvide(ForgeDirection.UNKNOWN)));
			}
			else if (NetworkConfigHandler.isIndustrialCraft2Loaded() && itemStack.getItem() instanceof ISpecialElectricItem)
			{
				ISpecialElectricItem electricItem = (ISpecialElectricItem) itemStack.getItem();
				IElectricItemManager manager = electricItem.getManager(itemStack);
				float energy = Math.max(this.getProvide(ForgeDirection.UNKNOWN) * NetworkConfigHandler.IC2_RATIO, 0);
				energy = manager.charge(itemStack, (int) (energy * NetworkConfigHandler.TO_IC2_RATIO), 0, false, false) * NetworkConfigHandler.IC2_RATIO;
				this.provideElectricity(energy, true);
			}
			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
			{
				int accepted = ((IEnergyContainerItem) itemStack.getItem()).receiveEnergy(itemStack, (int) Math.floor(this.getProvide(ForgeDirection.UNKNOWN) * NetworkConfigHandler.TO_TE_RATIO), false);
				this.provideElectricity(accepted * NetworkConfigHandler.TE_RATIO, true);
			}
		}
	}

	public void produce()
	{
		if (!this.worldObj.isRemote)
		{
			for (ForgeDirection outputDirection : this.getElectricalOutputDirections())
			{
				if (outputDirection != ForgeDirection.UNKNOWN)
				{
					if (!this.produceUE(outputDirection))
					{
						this.produceBuildCraft(outputDirection);
					}
				}
			}
		}
	}

	public boolean produceUE(ForgeDirection outputDirection)
	{
		if (!this.worldObj.isRemote && outputDirection != null && outputDirection != ForgeDirection.UNKNOWN)
		{
			float provide = this.getProvide(outputDirection);

			if (provide > 0)
			{
				Vector3 thisVec = new Vector3(this);
				TileEntity outputTile = thisVec.modifyPositionFromSide(outputDirection).getTileEntity(this.worldObj);
				IElectricityNetwork outputNetwork = NetworkHelper.getElectricalNetworkFromTileEntity(outputTile, outputDirection);

				if (outputNetwork != null)
				{
					ElectricityPack powerRequest = outputNetwork.getRequest(this);

					if (powerRequest.getWatts() > 0)
					{
						ElectricityPack sendPack = ElectricityPack.min(ElectricityPack.getFromWatts(this.getEnergyStored(), this.getVoltage()), ElectricityPack.getFromWatts(provide, this.getVoltage()));
						float rejectedPower = outputNetwork.produce(sendPack, true, this);
						this.provideElectricity(Math.max(sendPack.getWatts() - rejectedPower, 0), true);
						return true;
					}
				}
				else if (outputTile instanceof IElectrical)
				{
					float requestedEnergy = ((IElectrical) outputTile).getRequest(outputDirection.getOpposite());

					if (requestedEnergy > 0)
					{
						ElectricityPack sendPack = ElectricityPack.min(ElectricityPack.getFromWatts(this.getEnergyStored(), this.getVoltage()), ElectricityPack.getFromWatts(provide, this.getVoltage()));
						float acceptedEnergy = ((IElectrical) outputTile).receiveElectricity(outputDirection.getOpposite(), sendPack, true);
						this.provideElectricity(acceptedEnergy, true);
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Discharges electric item.
	 */
	public void discharge(ItemStack itemStack)
	{
		if (itemStack != null)
		{
			if (itemStack.getItem() instanceof IItemElectric)
			{
				this.setEnergyStored(this.getEnergyStored() + ElectricItemHelper.dischargeItem(itemStack, this.getRequest(ForgeDirection.UNKNOWN)));
			}
			else if (NetworkConfigHandler.isIndustrialCraft2Loaded() && itemStack.getItem() instanceof ISpecialElectricItem)
			{
				ISpecialElectricItem electricItem = (ISpecialElectricItem) itemStack.getItem();

				if (electricItem.canProvideEnergy(itemStack))
				{
					IElectricItemManager manager = electricItem.getManager(itemStack);
					float energy = Math.max(this.getRequest(ForgeDirection.UNKNOWN) * NetworkConfigHandler.IC2_RATIO, 0);
					energy = manager.discharge(itemStack, (int) (energy * NetworkConfigHandler.TO_IC2_RATIO), 0, false, false);
					this.receiveElectricity(energy, true);
				}
			}
			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
			{
				float given = ((IEnergyContainerItem) itemStack.getItem()).extractEnergy(itemStack, (int) Math.floor(this.getRequest(ForgeDirection.UNKNOWN) * NetworkConfigHandler.TO_TE_RATIO), false);
				this.receiveElectricity(given * NetworkConfigHandler.TE_RATIO, true);
			}
		}
	}

	@Override
	public void initiate()
	{
		super.initiate();
		this.initBuildCraft();
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		// Register to the IC2 Network
		if (!this.worldObj.isRemote)
		{
			if (!this.isAddedToEnergyNet)
			{
				this.initIC();
			}

			if (this.bcPowerHandler == null)
			{
				this.initBuildCraft();
			}

			if (NetworkConfigHandler.isBuildcraftLoaded())
			{
				PowerHandler handler = (PowerHandler) this.bcPowerHandler;

				if (handler.getEnergyStored() > 0)
				{
					/**
					 * Cheat BuildCraft powerHandler and always empty energy
					 * inside of it.
					 */
					this.receiveElectricity(handler.getEnergyStored() * NetworkConfigHandler.BC3_RATIO, true);
					handler.setEnergy(0);
				}
			}
		}
	}

	public boolean produceBuildCraft(ForgeDirection outputDirection)
	{
		if (!this.worldObj.isRemote && outputDirection != null && outputDirection != ForgeDirection.UNKNOWN)
		{
			float provide = this.getProvide(outputDirection);

			if (this.getEnergyStored() >= provide && provide > 0)
			{
				if (NetworkConfigHandler.isBuildcraftLoaded())
				{
					TileEntity tileEntity = new Vector3(this).modifyPositionFromSide(outputDirection).getTileEntity(this.worldObj);

					if (tileEntity instanceof IPowerReceptor)
					{
						PowerReceiver receiver = ((IPowerReceptor) tileEntity).getPowerReceiver(outputDirection.getOpposite());

						if (receiver != null)
						{
							if (receiver.powerRequest() > 0)
							{
								float bc3Provide = provide * NetworkConfigHandler.TO_BC_RATIO;
								float energyUsed = Math.min(receiver.receiveEnergy(Type.MACHINE, bc3Provide, outputDirection.getOpposite()), bc3Provide);
								this.provideElectricity(energyUsed * NetworkConfigHandler.TO_BC_RATIO, true);
							}
						}

						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * IC2 Methods
	 */
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyAcceptor", modID = "IC2")
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
	{
		return this.getElectricalInputDirections().contains(direction);
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
	public double getOfferedEnergy()
	{
		return this.getProvide(ForgeDirection.UNKNOWN) * NetworkConfigHandler.TO_IC2_RATIO;
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
	public void drawEnergy(double amount)
	{
		this.provideElectricity((float) amount * NetworkConfigHandler.IC2_RATIO, true);
	}

	@Override
	public void invalidate()
	{
		this.unloadTileIC2();
		super.invalidate();
	}

	@Override
	public void onChunkUnload()
	{
		this.unloadTileIC2();
		super.onChunkUnload();
	}

	protected void initIC()
	{
		if (NetworkConfigHandler.isIndustrialCraft2Loaded() && !this.worldObj.isRemote)
		{
			try
			{
				Class<?> tileLoadEvent = Class.forName("ic2.api.energy.event.EnergyTileLoadEvent");
				Class<?> energyTile = Class.forName("ic2.api.energy.tile.IEnergyTile");
				Constructor<?> constr = tileLoadEvent.getConstructor(energyTile);
				Object o = constr.newInstance(this);

				if (o != null && o instanceof Event)
				{
					MinecraftForge.EVENT_BUS.post((Event) o);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.isAddedToEnergyNet = true;
	}

	private void unloadTileIC2()
	{
		if (this.isAddedToEnergyNet && this.worldObj != null)
		{
			if (NetworkConfigHandler.isIndustrialCraft2Loaded() && !this.worldObj.isRemote)
			{
				try
				{
					Class<?> tileLoadEvent = Class.forName("ic2.api.energy.event.EnergyTileUnloadEvent");
					Class<?> energyTile = Class.forName("ic2.api.energy.tile.IEnergyTile");
					Constructor<?> constr = tileLoadEvent.getConstructor(energyTile);
					Object o = constr.newInstance(this);

					if (o != null && o instanceof Event)
					{
						MinecraftForge.EVENT_BUS.post((Event) o);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			this.isAddedToEnergyNet = false;
		}
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public double demandedEnergyUnits()
	{
		return Math.ceil(this.getRequest(ForgeDirection.UNKNOWN) * NetworkConfigHandler.TO_IC2_RATIO);
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public double injectEnergyUnits(ForgeDirection direction, double amount)
	{
		if (this.getElectricalInputDirections().contains(direction))
		{
			float convertedEnergy = (float) (amount * NetworkConfigHandler.IC2_RATIO);
			ElectricityPack toSend = ElectricityPack.getFromWatts(convertedEnergy, this.getVoltage());
			float receive = this.receiveElectricity(direction, toSend, true);

			// Return the difference, since injectEnergy returns left over
			// energy, and
			// receiveElectricity returns energy used.
			return Math.round(amount - receive * NetworkConfigHandler.TO_IC2_RATIO);
		}

		return amount;
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyEmitter", modID = "IC2")
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
	{
		return receiver instanceof IEnergyTile && this.getElectricalOutputDirections().contains(direction);
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public int getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}

	/**
	 * BuildCraft power support
	 */
	public void initBuildCraft()
	{
		if (!NetworkConfigHandler.isBuildcraftLoaded())
		{
			return;
		}

		if (this.bcPowerHandler == null)
		{
			this.bcPowerHandler = new PowerHandler((IPowerReceptor) this, Type.MACHINE);
		}

		((PowerHandler) this.bcPowerHandler).configure(0, this.maxInputEnergy, 0, (int) Math.ceil(this.getMaxEnergyStored() * NetworkConfigHandler.BC3_RATIO));
	}

	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		this.initBuildCraft();
		return ((PowerHandler) this.bcPowerHandler).getPowerReceiver();
	}

	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
	public void doWork(PowerHandler workProvider)
	{

	}

	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
	public World getWorld()
	{
		return this.getWorldObj();
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		if (!this.getElectricalInputDirections().contains(from))
		{
			return 0;
		}

		return (int) Math.floor(this.receiveElectricity(maxReceive * NetworkConfigHandler.TE_RATIO, !simulate));
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		if (!this.getElectricalOutputDirections().contains(from))
		{
			return 0;
		}

		return (int) Math.floor(this.provideElectricity(maxExtract * NetworkConfigHandler.TE_RATIO, !simulate).getWatts() * NetworkConfigHandler.TO_TE_RATIO);
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public boolean canInterface(ForgeDirection from)
	{
		return this.getElectricalInputDirections().contains(from) || this.getElectricalOutputDirections().contains(from);
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public int getEnergyStored(ForgeDirection from)
	{
		return (int) Math.floor(this.getEnergyStored() * NetworkConfigHandler.TO_TE_RATIO);
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public int getMaxEnergyStored(ForgeDirection from)
	{
		return (int) Math.floor(this.getMaxEnergyStored() * NetworkConfigHandler.TO_TE_RATIO);
	}
}
