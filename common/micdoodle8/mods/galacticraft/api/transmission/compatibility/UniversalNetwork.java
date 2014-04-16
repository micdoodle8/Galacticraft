package micdoodle8.mods.galacticraft.api.transmission.compatibility;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import micdoodle8.mods.galacticraft.api.transmission.ElectricalEvent.ElectricityProductionEvent;
import micdoodle8.mods.galacticraft.api.transmission.ElectricalEvent.ElectricityRequestEvent;
import micdoodle8.mods.galacticraft.api.transmission.ElectricityPack;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.ElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.core.path.Pathfinder;
import micdoodle8.mods.galacticraft.api.transmission.core.path.PathfinderChecker;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cofh.api.energy.IEnergyHandler;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLLog;

/**
 * A universal network that words with multiple energy systems.
 * 
 * @author micdoodle8, Calclavia, Aidancbrady
 * 
 */
public class UniversalNetwork extends ElectricityNetwork
{
	@Override
	public float produce(ElectricityPack electricity, boolean doReceive, TileEntity... ignoreTiles)
	{
		ElectricityProductionEvent evt = new ElectricityProductionEvent(this, electricity, ignoreTiles);
		MinecraftForge.EVENT_BUS.post(evt);

		float totalEnergy = electricity.getWatts();
		float voltage = electricity.voltage;
		float sent = 0.0F;

		if (!evt.isCanceled())
		{
			if (!this.electricalTiles.isEmpty())
			{
				final float totalEnergyRequest = this.getRequest(ignoreTiles).getWatts();

				if (totalEnergyRequest > 0)
				{
					List<Object> acceptors = Lists.newCopyOnWriteArrayList();
					acceptors.addAll(this.electricalTiles.keySet());

					for (TileEntity tileEntity : ignoreTiles)
					{
						if (acceptors.contains(tileEntity))
						{
							acceptors.remove(tileEntity);
						}
					}

					List<Object> removeList = new ArrayList<Object>();

					for (Object obj : acceptors)
					{
						if (obj instanceof TileEntity)
						{
							TileEntity tileEntity = (TileEntity) obj;

							if (tileEntity == null || tileEntity.isInvalid())
							{
								removeList.add(tileEntity);
							}
						}
					}

					acceptors.removeAll(removeList);

					Collections.shuffle(acceptors);

					int divider = acceptors.size();
					double remaining = totalEnergy % divider;
					double sending = (totalEnergy - remaining) / divider;

					for (Object obj : acceptors)
					{
						if (obj instanceof TileEntity)
						{
							TileEntity tileEntity = (TileEntity) obj;
							double currentSending = sending + remaining;
							ForgeDirection side = this.electricalTiles.get(tileEntity);

							if (side == null)
							{
								continue;
							}

							remaining = 0;

							if (tileEntity instanceof IElectrical)
							{
								IElectrical electricalTile = (IElectrical) tileEntity;
								ElectricityPack electricityToSend = ElectricityPack.getFromWatts((float) currentSending, voltage);
								sent += electricalTile.receiveElectricity(side.getOpposite(), electricityToSend, doReceive);
							}
							else if (NetworkConfigHandler.isThermalExpansionLoaded() && tileEntity instanceof IEnergyHandler)
							{
								IEnergyHandler handler = (IEnergyHandler) tileEntity;
								int used = handler.receiveEnergy(side.getOpposite(), (int) Math.round(currentSending * NetworkConfigHandler.TO_TE_RATIO), !doReceive);
								sent += used * NetworkConfigHandler.TE_RATIO;
							}
							else if (NetworkConfigHandler.isIndustrialCraft2Loaded() && tileEntity instanceof IEnergySink)
							{
								IEnergySink electricalTile = (IEnergySink) tileEntity;
								double toSend = Math.min(currentSending, electricalTile.getMaxSafeInput() * NetworkConfigHandler.IC2_RATIO);
								toSend = Math.min(toSend, electricalTile.demandedEnergyUnits() * NetworkConfigHandler.IC2_RATIO);
								sent += (toSend - (electricalTile.injectEnergyUnits(side.getOpposite(), toSend * NetworkConfigHandler.TO_IC2_RATIO) * NetworkConfigHandler.IC2_RATIO));
							}
							else if (NetworkConfigHandler.isBuildcraftLoaded() && tileEntity instanceof IPowerReceptor)
							{
								IPowerReceptor electricalTile = (IPowerReceptor) tileEntity;
								PowerReceiver receiver = electricalTile.getPowerReceiver(side.getOpposite());

								if (receiver != null)
								{
									float req = receiver.powerRequest();
									double bcToSend = currentSending * NetworkConfigHandler.TO_BC_RATIO;
									float bcSent = receiver.receiveEnergy(Type.PIPE, (float) (Math.min(req, bcToSend)), side.getOpposite());
									sent += bcSent * NetworkConfigHandler.BC3_RATIO;
								}
							}
						}
					}
				}
			}
		}

		return totalEnergy - sent;
	}

	@Override
	public ElectricityPack getRequest(TileEntity... ignoreTiles)
	{
		List<ElectricityPack> requests = new ArrayList<ElectricityPack>();

		for (TileEntity tileEntity : new HashSet<TileEntity>(this.electricalTiles.keySet()))
		{
			if (Arrays.asList(ignoreTiles).contains(tileEntity))
			{
				continue;
			}

			if (tileEntity != null && !tileEntity.isInvalid())
			{
				if (tileEntity.worldObj.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) == tileEntity)
				{
					if (tileEntity instanceof IElectrical)
					{
						for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
						{
							Vector3 tileVec = new Vector3(tileEntity);
							TileEntity tile = tileVec.modifyPositionFromSide(direction).getTileEntity(tileEntity.worldObj);

							if (((IElectrical) tileEntity).canConnect(direction, NetworkType.POWER) && this.getTransmitters().contains(tile))
							{
								requests.add(ElectricityPack.getFromWatts(((IElectrical) tileEntity).getRequest(direction), ((IElectrical) tileEntity).getVoltage()));
							}
						}

						continue;
					}

					if (NetworkConfigHandler.isThermalExpansionLoaded() && tileEntity instanceof IEnergyHandler)
					{
						IEnergyHandler receiver = (IEnergyHandler) tileEntity;

						for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
						{
							Vector3 tileVec = new Vector3(tileEntity);
							TileEntity conductor = tileVec.modifyPositionFromSide(direction).getTileEntity(tileEntity.worldObj);

							if (receiver.canInterface(direction) && this.getTransmitters().contains(conductor))
							{
								ElectricityPack pack = ElectricityPack.getFromWatts(receiver.receiveEnergy(direction, Integer.MAX_VALUE, true) * NetworkConfigHandler.TE_RATIO, 1);

								if (pack.getWatts() > 0)
								{
									requests.add(pack);
									break;
								}
							}
						}

						continue;
					}

					if (NetworkConfigHandler.isIndustrialCraft2Loaded() && tileEntity instanceof IEnergySink)
					{
						for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
						{
							Vector3 tileVec = new Vector3(tileEntity);
							TileEntity conductor = tileVec.modifyPositionFromSide(direction).getTileEntity(tileEntity.worldObj);

							if (((IEnergySink) tileEntity).acceptsEnergyFrom(tileVec.modifyPositionFromSide(direction).getTileEntity(tileEntity.worldObj), direction) && this.getTransmitters().contains(conductor))
							{
								ElectricityPack pack = ElectricityPack.getFromWatts((float) (((IEnergySink) tileEntity).demandedEnergyUnits() * NetworkConfigHandler.IC2_RATIO), 1);

								if (pack.getWatts() > 0)
								{
									requests.add(pack);
								}
							}
						}

						continue;
					}

					if (NetworkConfigHandler.isBuildcraftLoaded() && tileEntity instanceof IPowerReceptor)
					{
						for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
						{
							if (((IPowerReceptor) tileEntity).getPowerReceiver(direction) != null)
							{
								ElectricityPack pack = ElectricityPack.getFromWatts(((IPowerReceptor) tileEntity).getPowerReceiver(direction).powerRequest() * NetworkConfigHandler.BC3_RATIO, 1);

								if (pack.getWatts() > 0)
								{
									requests.add(pack);
									break;
								}
							}
						}

						continue;
					}
				}
			}
		}

		ElectricityPack mergedPack = ElectricityPack.merge(requests);
		ElectricityRequestEvent evt = new ElectricityRequestEvent(this, mergedPack, ignoreTiles);
		MinecraftForge.EVENT_BUS.post(evt);
		return mergedPack;
	}

	@Override
	public void refresh()
	{
		this.electricalTiles.clear();

		try
		{
			Iterator<IConductor> it = this.getTransmitters().iterator();

			while (it.hasNext())
			{
				IConductor conductor = it.next();

				if (conductor == null)
				{
					it.remove();
					continue;
				}

				conductor.onNetworkChanged();

				if (((TileEntity) conductor).isInvalid() || ((TileEntity) conductor).getWorldObj() == null)
				{
					it.remove();
					continue;
				}
				else if (((TileEntity) conductor).getWorldObj().getBlockTileEntity(((TileEntity) conductor).xCoord, ((TileEntity) conductor).yCoord, ((TileEntity) conductor).zCoord) != conductor)
				{
					it.remove();
					continue;
				}
				else
				{
					conductor.setNetwork(this);
				}

				for (int i = 0; i < conductor.getAdjacentConnections().length; i++)
				{
					TileEntity acceptor = conductor.getAdjacentConnections()[i];
					// The direction is from the perspective of the conductor.
					ForgeDirection direction = ForgeDirection.getOrientation(i);

					if (!(acceptor instanceof IConductor))
					{
						if (acceptor instanceof IElectrical || (NetworkConfigHandler.isThermalExpansionLoaded() && acceptor instanceof IEnergyHandler) || (NetworkConfigHandler.isIndustrialCraft2Loaded() && acceptor instanceof IEnergyAcceptor) || (NetworkConfigHandler.isBuildcraftLoaded() && acceptor instanceof IPowerReceptor))
						{
							boolean canConnect = false;

							if (acceptor instanceof IElectrical && ((IElectrical) acceptor).canConnect(direction.getOpposite(), NetworkType.POWER))
							{
								canConnect = true;
							}
							else if (NetworkConfigHandler.isThermalExpansionLoaded() && acceptor instanceof IEnergyHandler && ((IEnergyHandler) acceptor).canInterface(direction))
							{
								canConnect = true;
							}
							else if (NetworkConfigHandler.isIndustrialCraft2Loaded() && acceptor instanceof IEnergyAcceptor && ((IEnergyAcceptor) acceptor).acceptsEnergyFrom((TileEntity) conductor, direction))
							{
								canConnect = true;
							}
							else if (NetworkConfigHandler.isBuildcraftLoaded() && acceptor instanceof IPowerReceptor && ((IPowerReceptor) acceptor).getPowerReceiver(direction) != null)
							{
								canConnect = true;
							}

							if (canConnect)
							{
								this.electricalTiles.put(acceptor, direction);
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			FMLLog.severe("Universal Electricity: Failed to refresh conductor.");
			e.printStackTrace();
		}
	}

	@Override
	public IElectricityNetwork merge(IElectricityNetwork network)
	{
		if (network != null && network != this)
		{
			UniversalNetwork newNetwork = new UniversalNetwork();
			newNetwork.getTransmitters().addAll(this.getTransmitters());
			newNetwork.getTransmitters().addAll(network.getTransmitters());
			newNetwork.refresh();
			return newNetwork;
		}

		return null;
	}

	@Override
	public void split(IConductor splitPoint)
	{
		if (splitPoint instanceof TileEntity)
		{
			this.getTransmitters().remove(splitPoint);

			/**
			 * Loop through the connected blocks and attempt to see if there are
			 * connections between the two points elsewhere.
			 */
			TileEntity[] connectedBlocks = splitPoint.getAdjacentConnections();

			for (TileEntity connectedBlockA : connectedBlocks)
			{
				if (connectedBlockA instanceof INetworkConnection)
				{
					for (final TileEntity connectedBlockB : connectedBlocks)
					{
						if (connectedBlockA != connectedBlockB && connectedBlockB instanceof INetworkConnection)
						{
							Pathfinder finder = new PathfinderChecker(((TileEntity) splitPoint).worldObj, (INetworkConnection) connectedBlockB, NetworkType.POWER, splitPoint);
							finder.init(new Vector3(connectedBlockA));

							if (finder.results.size() > 0)
							{
								/**
								 * The connections A and B are still intact
								 * elsewhere. Set all references of wire
								 * connection into one network.
								 */

								for (Vector3 node : finder.closedSet)
								{
									TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).worldObj);

									if (nodeTile instanceof INetworkProvider)
									{
										if (nodeTile != splitPoint)
										{
											((INetworkProvider) nodeTile).setNetwork(this);
										}
									}
								}
							}
							else
							{
								/**
								 * The connections A and B are not connected
								 * anymore. Give both of them a new network.
								 */
								IElectricityNetwork newNetwork = new UniversalNetwork();

								for (Vector3 node : finder.closedSet)
								{
									TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).worldObj);

									if (nodeTile instanceof INetworkProvider)
									{
										if (nodeTile != splitPoint)
										{
											newNetwork.getTransmitters().add((IConductor) nodeTile);
										}
									}
								}

								newNetwork.refresh();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String toString()
	{
		return "UniversalNetwork[" + this.hashCode() + "|Wires:" + this.getTransmitters().size() + "|Acceptors:" + this.electricalTiles.size() + "]";
	}
}