package micdoodle8.mods.galacticraft.core.tile;

import java.lang.reflect.Constructor;

import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * TileEntityUniversalConductor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class TileEntityUniversalConductor extends TileEntityConductor
{
	protected boolean isAddedToEnergyNet;
	public Object powerHandler;
	public float buildcraftBuffer = NetworkConfigHandler.BC3_RATIO * 50;

//	public TileEntityUniversalConductor()
//	{
//		this.initBC();
//	}

	@Override
	public void onNetworkChanged()
	{
//		if (NetworkConfigHandler.isBuildcraftLoaded())
//		{
//			if (this instanceof IPowerReceptor)
//			{
//				this.reconfigureBC();
//			}
//		}
	}
//
//	private void initBC()
//	{
//		if (NetworkConfigHandler.isBuildcraftLoaded())
//		{
//			if (this instanceof IPowerReceptor)
//			{
//				this.powerHandler = new PowerHandler((IPowerReceptor) this, Type.PIPE);
//				this.reconfigureBC();
//				((PowerHandler) this.powerHandler).configurePowerPerdition(0, 0);
//			}
//		}
//	} TODO Re-implement later

	@Override
	public TileEntity[] getAdjacentConnections()
	{
		if (this.adjacentConnections == null)
		{
			this.adjacentConnections = WorldUtil.getAdjacentPowerConnections(this);
		}

		return this.adjacentConnections;
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void updateEntity()
	{
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

//	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//	public double demandedEnergyUnits()
//	{
//		if (this.getNetwork() == null)
//		{
//			return 0.0;
//		}
//
//		return ((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() * NetworkConfigHandler.TO_IC2_RATIO;
//	}
//
//	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//	public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
//	{
//		TileEntity tile = new Vector3(this).modifyPositionFromSide(directionFrom).getTileEntity(this.worldObj);
//		ElectricityPack pack = ElectricityPack.getFromWatts((float) (amount * NetworkConfigHandler.IC2_RATIO), 120);
//		return ((IElectricityNetwork) this.getNetwork()).produce(pack, true, this, tile) * NetworkConfigHandler.TO_IC2_RATIO;
//	}
//
//	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//	public int getMaxSafeInput()
//	{
//		return Integer.MAX_VALUE;
//	}
//	
//	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
//	{
//		return true;
//	}
//
//	/**
//	 * BuildCraft functions
//	 */
//	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
//	public PowerReceiver getPowerReceiver(ForgeDirection side)
//	{
//		if (this.getNetwork() == null || ((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() <= 0.0F)
//		{
//			return null;
//		}
//		
//		return ((PowerHandler) this.powerHandler).getPowerReceiver();
//	}
//	
//	private void reconfigureBC()
//	{
//		float requiredEnergy = ((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() * NetworkConfigHandler.TO_BC_RATIO;
//		((PowerHandler) this.powerHandler).configure(1, requiredEnergy, 0, requiredEnergy);
//	}
//
//	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
//	public void doWork(PowerHandler workProvider)
//	{
//		if (((PowerHandler) this.powerHandler).getEnergyStored() > 0.0F)
//		{
//			if (this.getNetwork() != null)
//			{
//				ElectricityPack pack = ElectricityPack.getFromWatts(((PowerHandler) this.powerHandler).getEnergyStored() * NetworkConfigHandler.BC3_RATIO, 120);
//				((IElectricityNetwork) this.getNetwork()).produce(pack, true, this);
//			}
//		}
//		
//		((PowerHandler) this.powerHandler).setEnergy(0.0F);
//		this.reconfigureBC();
//	}
//
//	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
//	public World getWorld()
//	{
//		return this.getWorldObj();
//	}
//
//	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
//	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
//	{
//		return maxReceive - (int) Math.floor(((IElectricityNetwork) this.getNetwork()).produce(ElectricityPack.getFromWatts(maxReceive * NetworkConfigHandler.TE_RATIO, 120), !simulate, this) * NetworkConfigHandler.TO_TE_RATIO);
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
//
//	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//	public double transferEnergyToAcceptor(ForgeDirection side, double amount)
//	{
//		if (!this.canReceiveEnergy(side))
//		{
//			return 0;
//		}
//		
//		return amount - ((IElectricityNetwork) this.getNetwork()).produce(ElectricityPack.getFromWatts((float) (amount * NetworkConfigHandler.MEKANISM_RATIO), 120), true, this) * NetworkConfigHandler.TO_MEKANISM_RATIO;
//	}
//
//	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//	public boolean canReceiveEnergy(ForgeDirection side)
//	{
//		if (this.getNetwork() == null)
//		{
//			return false;
//		}
//		
//		return ((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() > 0.0F;
//	}
//
//	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//	public double getEnergy()
//	{
//		return 0;
//	}
//
//	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//	public void setEnergy(double energy)
//	{
//		;
//	}
//
//	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//	public double getMaxEnergy()
//	{
//		return 1;
//	} TODO Re-implement when APIs are ready
}
