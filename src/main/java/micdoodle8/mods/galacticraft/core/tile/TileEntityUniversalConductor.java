package micdoodle8.mods.galacticraft.core.tile;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.common.eventhandler.Event;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import micdoodle8.mods.miccore.Annotations.VersionSpecific;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ic2.api.energy.tile.IEnergySource;

import java.lang.reflect.Constructor;

public abstract class TileEntityUniversalConductor extends TileEntityConductor
{
	protected boolean isAddedToEnergyNet;
	protected Object powerHandlerBC;

	//	public float buildcraftBuffer = NetworkConfigHandler.BC3_RATIO * 50;
	private float IC2surplusJoules = 0F;

	public TileEntityUniversalConductor()
	{
		this.initBC();
	}

	@Override
	public void onNetworkChanged()
	{
	}

	private void initBC()
	{
		if (NetworkConfigHandler.isBuildcraftLoaded())
		{
			if (this instanceof IPowerReceptor)
			{
				this.powerHandlerBC = new PowerHandler((IPowerReceptor) this, buildcraft.api.power.PowerHandler.Type.PIPE);
				((PowerHandler) this.powerHandlerBC).configurePowerPerdition(0, 0);
			}
		}
	}

	@Override
	public TileEntity[] getAdjacentConnections()
	{
		return WorldUtil.getAdjacentPowerConnections(this);
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.isAddedToEnergyNet)
		{
			if (!this.worldObj.isRemote)
			{
				if (NetworkConfigHandler.isIndustrialCraft2Loaded())
				{
					this.initIC();
				}
			}

			this.isAddedToEnergyNet = true;
		}
	}

	@Override
	public void invalidate()
	{
		this.IC2surplusJoules = 0F;
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
	}

	private void unloadTileIC2()
	{
		if (this.isAddedToEnergyNet && this.worldObj != null)
		{
			if (!this.worldObj.isRemote && NetworkConfigHandler.isIndustrialCraft2Loaded())
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
	
	@VersionSpecific(version = "[1.7.2]")
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public double demandedEnergyUnits()
	{
		if (this.getNetwork() == null)
		{
			return 0.0;
		}

		if (this.IC2surplusJoules < 0.001F)
		{
			this.IC2surplusJoules = 0F;
			return this.getNetwork().getRequest(this) * NetworkConfigHandler.TO_IC2_RATIO;
		}

		this.IC2surplusJoules = this.getNetwork().produce(this.IC2surplusJoules, true, 1, this);
		if (this.IC2surplusJoules < 0.001F)
		{
			this.IC2surplusJoules = 0F;
			return this.getNetwork().getRequest(this) * NetworkConfigHandler.TO_IC2_RATIO;
		}
		return 0D;
	}

	@VersionSpecific(version = "[1.7.10]")
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public double getDemandedEnergy()
	{
		if (this.getNetwork() == null)
		{
			return 0.0;
		}

		if (this.IC2surplusJoules < 0.001F)
		{
			this.IC2surplusJoules = 0F;
			return this.getNetwork().getRequest(this) * NetworkConfigHandler.TO_IC2_RATIO;
		}

		this.IC2surplusJoules = this.getNetwork().produce(this.IC2surplusJoules, true, 1, this);
		if (this.IC2surplusJoules < 0.001F)
		{
			this.IC2surplusJoules = 0F;
			return this.getNetwork().getRequest(this) * NetworkConfigHandler.TO_IC2_RATIO;
		}
		return 0D;
	}

	@VersionSpecific(version = "[1.7.2]")
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
	{
		TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, directionFrom);
		int tier = 1;
		if (tile instanceof IEnergySource && ((IEnergySource) tile).getOfferedEnergy() >= 128) tier = 2;
		float convertedEnergy = (float) amount * NetworkConfigHandler.IC2_RATIO;
		float surplus = this.getNetwork().produce(convertedEnergy, true, tier, this, tile);

		if (surplus >= 0.001F) this.IC2surplusJoules = surplus;
		else this.IC2surplusJoules = 0F;

		return Math.round(this.IC2surplusJoules * NetworkConfigHandler.TO_IC2_RATIO);
	}

	@VersionSpecific(version = "[1.7.10]")
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
	{
		TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, directionFrom);
		int tier = ((int) voltage > 120) ? 2 : 1;
		if (tile instanceof IEnergySource && ((IEnergySource) tile).getOfferedEnergy() >= 128) tier = 2;
		float convertedEnergy = (float) amount * NetworkConfigHandler.IC2_RATIO;
		float surplus = this.getNetwork().produce(convertedEnergy, true, tier, this, tile);

		if (surplus >= 0.001F) this.IC2surplusJoules = surplus;
		else this.IC2surplusJoules = 0F;

		return Math.round(this.IC2surplusJoules * NetworkConfigHandler.TO_IC2_RATIO);
	}

	@VersionSpecific(version = "[1.7.10]")
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public int getSinkTier()
	{
		return 3;
	}
	
	@VersionSpecific(version = "[1.7.2]")
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public double getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyAcceptor", modID = "IC2")
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
	{
		//Don't add connection to IC2 grid if it's a Galacticraft tile
		if (emitter instanceof IElectrical || emitter instanceof IConductor)
			return false;

		//Don't make connection with IC2 wires [don't want risk of multiple connections + there is a graphical glitch in IC2]
		try {
			Class<?> conductorIC2 = Class.forName("ic2.api.energy.tile.IEnergyConductor");
			if (conductorIC2.isInstance(emitter)) return false; 
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return true;
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyEmitter", modID = "IC2")
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
	{
		//Don't add connection to IC2 grid if it's a Galacticraft tile
		if (receiver instanceof IElectrical || receiver instanceof IConductor)
			return false;

		//Don't make connection with IC2 wires [don't want risk of multiple connections + there is a graphical glitch in IC2]
		try {
			Class<?> conductorIC2 = Class.forName("ic2.api.energy.tile.IEnergyConductor");
			if (conductorIC2.isInstance(receiver)) return false; 
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * BuildCraft functions
	 */
	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		if (this.getNetwork() == null || this.getNetwork().getRequest(this) <= 0.0F)
		{
			return null;
		}

		return ((PowerHandler) this.powerHandlerBC).getPowerReceiver();
	}

	public void reconfigureBC()
	{
		double requiredEnergy = this.getNetwork().getRequest(this) * NetworkConfigHandler.TO_BC_RATIO;
		((PowerHandler) this.powerHandlerBC).configure(1, requiredEnergy, 0, requiredEnergy);
	}

	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
	public void doWork(PowerHandler workProvider)
	{
		PowerHandler handler = (PowerHandler) this.powerHandlerBC;
		
		if (handler.getEnergyStored() > 0.0F)
		{
			if (this.getNetwork() != null)
			{
				
			}
		}
		
		double energyBC = handler.getEnergyStored();
		if (energyBC > 0D)
		{
			energyBC = this.getNetwork().produce((float) energyBC * NetworkConfigHandler.BC3_RATIO, true, 1, this) * NetworkConfigHandler.TO_BC_RATIO;
			if (energyBC < 0D) energyBC = 0D;
			handler.setEnergy(energyBC);
		}

		this.reconfigureBC();
	}

	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
	public World getWorld()
	{
		return this.getWorldObj();
	}

	//	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	//	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	//	{
	//		ElectricityPack totalEnergyPack = ElectricityPack.getFromWatts(maxReceive * NetworkConfigHandler.TE_RATIO, 120);
	//		float sent = (totalEnergyPack.getWatts() - ((IElectricityNetwork) this.getNetwork()).produce(totalEnergyPack, !simulate, this));
	//		return MathHelper.floor_float(sent * NetworkConfigHandler.TO_TE_RATIO);
	//	}
	//
	//	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	//	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	//	{
	//		return 0;
	//	}
	//
	//	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	//	public boolean canInterface(ForgeDirection from)
	//	{
	//		return this.canConnect(from, NetworkType.POWER);
	//	}
	//
	//	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	//	public int getEnergyStored(ForgeDirection from)
	//	{
	//		return 0;
	//	}
	//
	//	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	//	public int getMaxEnergyStored(ForgeDirection from)
	//	{
	//		if (this.getNetwork() == null)
	//		{
	//			return 0;
	//		}
	//
	//		return (int) Math.floor(((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() * NetworkConfigHandler.TO_TE_RATIO);
	//	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
	public double transferEnergyToAcceptor(ForgeDirection side, double amount)
	{
		if (!this.canReceiveEnergy(side))
		{
			return 0;
		}

		return amount - this.getNetwork().produce((float) amount * NetworkConfigHandler.MEKANISM_RATIO, true, 1, this) * NetworkConfigHandler.TO_MEKANISM_RATIO;
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
	public boolean canReceiveEnergy(ForgeDirection side)
	{
		if (this.getNetwork() == null)
		{
			return false;
		}

		return true;
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
	public double getEnergy()
	{
		return 0;
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
	public void setEnergy(double energy)
	{
		;
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
	public double getMaxEnergy()
	{
		if (this.getNetwork() == null)
		{
			return 0;
		}
		return this.getNetwork().getRequest(this) * NetworkConfigHandler.TO_MEKANISM_RATIO;
	}
}
