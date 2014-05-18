package micdoodle8.mods.galacticraft.core.tile;

import java.lang.reflect.Constructor;

import micdoodle8.mods.galacticraft.api.transmission.ElectricityPack;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

/**
 * GCCoreTileEntityUniversalConductor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreTileEntityUniversalConductor extends GCCoreTileEntityConductor
{
	protected boolean isAddedToEnergyNet;
	public Object powerHandler;
	public float buildcraftBuffer = NetworkConfigHandler.BC3_RATIO * 50;
	private float IC2surplusJoules = 0F;

	public GCCoreTileEntityUniversalConductor()
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
				this.powerHandler = new PowerHandler((IPowerReceptor) this, Type.PIPE);
				//this.reconfigureBC();  //This is not wanted when the tileEntity is first created as the tileEntity has no x,y,z coords yet
				((PowerHandler) this.powerHandler).configurePowerPerdition(0, 0);
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
			return ((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() * NetworkConfigHandler.TO_IC2_RATIO;
		}
		
		ElectricityPack toSend = ElectricityPack.getFromWatts(IC2surplusJoules, 120F);
		this.IC2surplusJoules = ((IElectricityNetwork) this.getNetwork()).produce(toSend, true, this);
		if (this.IC2surplusJoules < 0.001F)
		{
			this.IC2surplusJoules = 0F;
			return ((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() * NetworkConfigHandler.TO_IC2_RATIO;
		}
		return 0D;
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
	{
		TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, directionFrom);
		float convertedEnergy = (float) amount * NetworkConfigHandler.IC2_RATIO;
		ElectricityPack pack = ElectricityPack.getFromWatts(convertedEnergy, 120F);
		float surplus = ((IElectricityNetwork) this.getNetwork()).produce(pack, true, this, tile);

		if (surplus >= 0.001F) this.IC2surplusJoules = surplus;
		else this.IC2surplusJoules = 0F;

		return Math.round(this.IC2surplusJoules * NetworkConfigHandler.TO_IC2_RATIO);
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public int getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
	{
		return true;
	}

	/**
	 * BuildCraft functions
	 */
	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		if (this.getNetwork() == null || ((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() <= 0.0F)
		{
			return null;
		}

		return ((PowerHandler) this.powerHandler).getPowerReceiver();
	}

	public void reconfigureBC()
	{
		float requiredEnergy = ((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() * NetworkConfigHandler.TO_BC_RATIO;
		((PowerHandler) this.powerHandler).configure(1, requiredEnergy, 0, requiredEnergy);
	}

	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
	public void doWork(PowerHandler workProvider)
	{
		if (((PowerHandler) this.powerHandler).getEnergyStored() > 0.0F)
		{
			if (this.getNetwork() != null)
			{
				ElectricityPack pack = ElectricityPack.getFromWatts(((PowerHandler) this.powerHandler).getEnergyStored() * NetworkConfigHandler.BC3_RATIO, 120);
				((IElectricityNetwork) this.getNetwork()).produce(pack, true, this);
			}
		}

		((PowerHandler) this.powerHandler).setEnergy(0.0F);
		this.reconfigureBC();
	}

	@RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
	public World getWorld()
	{
		return this.getWorldObj();
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		ElectricityPack totalEnergyPack = ElectricityPack.getFromWatts(maxReceive * NetworkConfigHandler.TE_RATIO, 120);
		float sent = (totalEnergyPack.getWatts() - ((IElectricityNetwork) this.getNetwork()).produce(totalEnergyPack, !simulate, this));
		return MathHelper.floor_float(sent * NetworkConfigHandler.TO_TE_RATIO);
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		return 0;
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public boolean canInterface(ForgeDirection from)
	{
		return this.canConnect(from, NetworkType.POWER);
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public int getEnergyStored(ForgeDirection from)
	{
		return 0;
	}

	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
	public int getMaxEnergyStored(ForgeDirection from)
	{
		if (this.getNetwork() == null)
		{
			return 0;
		}

		return (int) Math.floor(((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() * NetworkConfigHandler.TO_TE_RATIO);
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
	public double transferEnergyToAcceptor(ForgeDirection side, double amount)
	{
		if (!this.canReceiveEnergy(side))
		{
			return 0;
		}

		return amount - ((IElectricityNetwork) this.getNetwork()).produce(ElectricityPack.getFromWatts((float) (amount * NetworkConfigHandler.MEKANISM_RATIO), 120), true, this) * NetworkConfigHandler.TO_MEKANISM_RATIO;
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
	public boolean canReceiveEnergy(ForgeDirection side)
	{
		if (this.getNetwork() == null)
		{
			return false;
		}

		return ((IElectricityNetwork) this.getNetwork()).getRequest(this).getWatts() > 0.0F;
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
		return 1;
	}
}
