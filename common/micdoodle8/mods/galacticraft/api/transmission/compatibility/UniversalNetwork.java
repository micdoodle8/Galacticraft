package micdoodle8.mods.galacticraft.api.transmission.compatibility;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerServer;
import net.minecraft.client.Minecraft;
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
 * @author micdoodle8, Calclavia, Aidancbrady, radfast
 * 
 */
public class UniversalNetwork extends ElectricityNetwork
{
	/* Re-written by radfast for better performance
	 * 
	 * Imagine a 30 producer, 80 acceptor network...
	 * 
	 *   Before: it would have called the inner loop in produce() 2400 times each tick.  Not good!
	 * 
	 *   After: the inner loop runs 80 times - part of it is in doTickStartCalc() at/near the tick start, and part of it is in doProduce() at the end of the tick
	 */
	
	public static int tickCount = 0;
	private int tickDone = -1;
	private float totalRequested = 0;
	private float totalEnergy = 0;
	private boolean doneScheduled = false;
	private boolean spamstop = false;
	
	/*
	 * possibleAcceptors is all the acceptors connected to this network
	 * acceptorDirections is the directions of those connections (from the point of view of the acceptor tile)
	 *   Note: each position in those two linked lists matches
	 *         so, an acceptor connected on two sides will be in possibleAcceptors twice 
	 */
	private List<TileEntity> possibleAcceptors = new LinkedList<TileEntity>();
	private List<ForgeDirection> acceptorDirections = new LinkedList<ForgeDirection>();
	
	/*
	 *  availableAcceptors is the acceptors which can receive energy (this tick)
	 *  availableAcceptorDirections is a map of those acceptors and the directions they will receive from (from the point of view of the acceptor tile)
	 *    Note: each acceptor will only be included once in these collections
	 *          (there is no point trying to put power into a machine twice from two different sides)
	 */
	private Set<TileEntity> availableAcceptors = new HashSet<TileEntity>();
	private Map<TileEntity, ForgeDirection> availableAcceptorDirections = new HashMap<TileEntity, ForgeDirection>();
	
	private Map<TileEntity,Float> energyRequests = new HashMap<TileEntity,Float>();
	private List<TileEntity> ignoreAcceptors = new LinkedList<TileEntity>();
	
	@Override
	public ElectricityPack getRequest(TileEntity... ignoreTiles)
	{
		if (UniversalNetwork.tickCount!=this.tickDone)
		{	
			//Start the new tick - initialise everything
			doTickStartCalc(ignoreTiles);
			this.tickDone = UniversalNetwork.tickCount;
		}
		return ElectricityPack.getFromWatts(totalRequested, 120F);
	}
	
	@Override
	public float produce(ElectricityPack electricity, boolean doReceive, TileEntity... ignoreTiles)
	{
		float energyToProduce = electricity.getWatts();

		if (energyToProduce > 0F)
		{
			ElectricityProductionEvent evt = new ElectricityProductionEvent(this, electricity, ignoreTiles);
			MinecraftForge.EVENT_BUS.post(evt);
			
			if (!evt.isCanceled())
			{
				if (UniversalNetwork.tickCount!=this.tickDone)
				{	
					//Start the new tick - initialise everything
					doTickStartCalc(ignoreTiles);
					this.tickDone = UniversalNetwork.tickCount;
				}
		
				if (!this.doneScheduled)
				{
					GCCoreTickHandlerServer.scheduleNetworkTick(this);
					this.doneScheduled = true;
				}
		
				this.ignoreAcceptors.addAll(Arrays.asList(ignoreTiles));
				
				//On a regular mid-tick produce(), just figure out how much is totalEnergy this tick and return the used amount
				//This will return 0 if totalRequested is 0 - for example a network with no acceptors
				float availpre = this.totalEnergy;
				
				//Add the energy for distribution by this grid later this tick
				//Note: totalEnergy cannot exceed totalRequested
				if (doReceive) this.totalEnergy += Math.min(energyToProduce, this.totalRequested - availpre);
				
				if (this.totalRequested >= availpre + energyToProduce) return 0F; //All the electricity will be used
				if (availpre >= this.totalRequested) return energyToProduce; //None of the electricity will be used
				return availpre + energyToProduce - this.totalRequested; //Some of the electricity will be used
			}
		}
		return energyToProduce;
	}

	public void tickEnd()
	{
		this.doneScheduled = false;
		
		//Finish the last tick if there was some to send and something to receive it
		if (this.totalEnergy > 0F && this.totalRequested > 0F)
		{
			float joulesTransmitted = doProduce();
			if (joulesTransmitted < this.totalEnergy)
				//Any spare energy left is retained for the next tick
				this.totalEnergy -= joulesTransmitted;
			else totalEnergy = 0F;
		} else totalEnergy = 0F;	
	}

	private void doTickStartCalc(TileEntity... ignoreTiles)
	{
		refreshAcceptors();
		 
		if (this.getTransmitters().size() == 0) return;
		
		this.availableAcceptors.clear();
		this.availableAcceptorDirections.clear();
		this.energyRequests.clear();
		this.totalRequested = 0.0F;
		this.ignoreAcceptors.clear();
		
		List<TileEntity> ignored = Arrays.asList(ignoreTiles);
		
		boolean isTELoaded = NetworkConfigHandler.isThermalExpansionLoaded();
		boolean isIC2Loaded = NetworkConfigHandler.isIndustrialCraft2Loaded();
		boolean isBCLoaded = NetworkConfigHandler.isBuildcraftLoaded();

		if(!this.possibleAcceptors.isEmpty())
		{
			float e;
			final Iterator<ForgeDirection> acceptorDirection = this.acceptorDirections.iterator();
			for(TileEntity acceptor : this.possibleAcceptors)
			{
				//This tries all sides of the acceptor which are connected (see refreshAcceptors())
				ForgeDirection sideFrom = acceptorDirection.next();
				
				//But the grid will only put energy into the acceptor from one side - once it's in availableAcceptors
				if(!ignored.contains(acceptor) && !this.availableAcceptors.contains(acceptor))
				{
					e = 0.0F;					

					if (acceptor instanceof IElectrical)
					{
						e = ((IElectrical) acceptor).getRequest(sideFrom);
					}
					else if (isTELoaded && acceptor instanceof IEnergyHandler)
					{
						e = ((IEnergyHandler) acceptor).receiveEnergy(sideFrom, Integer.MAX_VALUE, true) * NetworkConfigHandler.TE_RATIO;
					}
					else if (isIC2Loaded && acceptor instanceof IEnergySink)
					{
						e = (float) Math.min(((IEnergySink) acceptor).demandedEnergyUnits(), ((IEnergySink)acceptor).getMaxSafeInput()) * NetworkConfigHandler.IC2_RATIO;
					}
					else if (isBCLoaded && acceptor instanceof IPowerReceptor)
					{
						e = ((IPowerReceptor) acceptor).getPowerReceiver(sideFrom).powerRequest() * NetworkConfigHandler.BC3_RATIO;
					}
					
					if (e > 0.0F)
					{
						this.availableAcceptors.add(acceptor);
						this.availableAcceptorDirections.put(acceptor,sideFrom);
						this.energyRequests.put(acceptor,Float.valueOf(e));
						this.totalRequested += e;
					}
				}
			}
		}
			
		ElectricityPack mergedPack = new ElectricityPack(totalRequested, 1);
		ElectricityRequestEvent evt = new ElectricityRequestEvent(this, mergedPack, ignoreTiles);
		MinecraftForge.EVENT_BUS.post(evt);
		totalRequested = mergedPack.getWatts();
	}

	private float doProduce()
	{
		float sent = 0.0F;

		if (!this.availableAcceptors.isEmpty())
		{
			//Detect loops, e.g. storage unit output connected to input
			for (TileEntity tileEntity : this.ignoreAcceptors)
			{
				if (this.availableAcceptors.contains(tileEntity))
				{
					this.availableAcceptors.remove(tileEntity);
				}
			}

			//Detect any changes which have happened since the availableAcceptors list was compiled earlier this tick
			List<Object> removeList = new ArrayList<Object>();
			for (TileEntity tileEntity : this.availableAcceptors)
			{
				if (tileEntity == null || tileEntity.isInvalid() || tileEntity != tileEntity.worldObj.getBlockTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord))
				{
					removeList.add(tileEntity);
				}
			}
			this.availableAcceptors.removeAll(removeList);
		}

		if (!this.availableAcceptors.isEmpty())
		{
			boolean isTELoaded = NetworkConfigHandler.isThermalExpansionLoaded();
			boolean isIC2Loaded = NetworkConfigHandler.isIndustrialCraft2Loaded();
			boolean isBCLoaded = NetworkConfigHandler.isBuildcraftLoaded();
			
			ArrayList<TileEntity> acceptors = new ArrayList();
			acceptors.addAll(this.availableAcceptors);
			Collections.shuffle(acceptors);

			int divider = acceptors.size();
			double remaining = this.totalEnergy % divider;
			double sending = (this.totalEnergy - remaining) / divider;

			for (TileEntity tileEntity : acceptors)
			{
				System.out.println("Tick end total:"+this.totalEnergy+" sent:"+sent+" Trying acceptor at "+tileEntity.xCoord+","+tileEntity.yCoord+","+tileEntity.zCoord);
				
				double currentSending = sending + remaining;
				ForgeDirection sideFrom = this.availableAcceptorDirections.get(tileEntity);

				remaining = 0;

				if (tileEntity instanceof IElectrical)
				{
					IElectrical electricalTile = (IElectrical) tileEntity;
					ElectricityPack electricityToSend = ElectricityPack.getFromWatts((float) currentSending, 120F);
					sent += electricalTile.receiveElectricity(sideFrom, electricityToSend, true);
				}
				else if (isTELoaded && tileEntity instanceof IEnergyHandler)
				{
					IEnergyHandler handler = (IEnergyHandler) tileEntity;
					int currentSendinginRF = (currentSending >= Integer.MAX_VALUE / NetworkConfigHandler.TO_TE_RATIO) ? Integer.MAX_VALUE : (int) (currentSending * NetworkConfigHandler.TO_TE_RATIO);
					sent += handler.receiveEnergy(sideFrom, currentSendinginRF, false) * NetworkConfigHandler.TE_RATIO;
				}
				else if (isIC2Loaded && tileEntity instanceof IEnergySink)
				{
					IEnergySink electricalTile = (IEnergySink) tileEntity;
					double toSend = Math.min(currentSending, electricalTile.getMaxSafeInput() * NetworkConfigHandler.IC2_RATIO);
					toSend = Math.min(toSend, electricalTile.demandedEnergyUnits() * NetworkConfigHandler.IC2_RATIO);
					sent += (toSend - (electricalTile.injectEnergyUnits(sideFrom, toSend * NetworkConfigHandler.TO_IC2_RATIO) * NetworkConfigHandler.IC2_RATIO));
				}
				else if (isBCLoaded && tileEntity instanceof IPowerReceptor)
				{
					IPowerReceptor electricalTile = (IPowerReceptor) tileEntity;
					PowerReceiver receiver = electricalTile.getPowerReceiver(sideFrom);

					if (receiver != null)
					{
						float req = receiver.powerRequest();
						double bcToSend = currentSending * NetworkConfigHandler.TO_BC_RATIO;
						float bcSent = receiver.receiveEnergy(Type.PIPE, (float) (Math.min(req, bcToSend)), sideFrom);
						sent += bcSent * NetworkConfigHandler.BC3_RATIO;
					}
				}
			}
		}

		float returnvalue = sent;
		if (returnvalue > this.totalEnergy) returnvalue = this.totalEnergy;
		if (returnvalue < 0F) returnvalue = 0F;
		return sent;
	}

	@Override
	public void refresh()
	{
		Iterator<IConductor> it = this.getTransmitters().iterator();
		while(it.hasNext())
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
		}
	}

	public void refreshAcceptors()
	{
		this.possibleAcceptors.clear();
		this.acceptorDirections.clear();

		this.refresh();
		
		try
		{
			for(IConductor conductor : this.getTransmitters())
			{	
				TileEntity[] adjacentConnections = conductor.getAdjacentConnections(); 
				for (int i = 0; i < adjacentConnections.length; i++)
				{
					TileEntity acceptor = adjacentConnections[i];

					if (acceptor != null && !(acceptor instanceof IConductor))
					{
						// The direction 'sideFrom' is from the perspective of the acceptor, that's more useful than the conductor's perspective
						ForgeDirection sideFrom = ForgeDirection.getOrientation(i).getOpposite();
						
						if (acceptor instanceof IElectrical)
						{
							if (((IElectrical) acceptor).canConnect(sideFrom, NetworkType.POWER))
							{
								possibleAcceptors.add(acceptor);
								acceptorDirections.add(sideFrom);
							}
						}
						else if (NetworkConfigHandler.isThermalExpansionLoaded() && acceptor instanceof IEnergyHandler)
						{
							if (((IEnergyHandler) acceptor).canInterface(sideFrom))
							{
								possibleAcceptors.add(acceptor);
								acceptorDirections.add(sideFrom);
							}
						}
						else if (NetworkConfigHandler.isIndustrialCraft2Loaded() && acceptor instanceof IEnergyAcceptor)
						{
							if(((IEnergyAcceptor)acceptor).acceptsEnergyFrom((TileEntity) conductor, sideFrom))
							{
								possibleAcceptors.add(acceptor);
								acceptorDirections.add(sideFrom);
							}
						}
						else if (NetworkConfigHandler.isBuildcraftLoaded() && acceptor instanceof IPowerReceptor)
						{
							if (((IPowerReceptor) acceptor).getPowerReceiver(sideFrom) != null)
							{
								possibleAcceptors.add(acceptor);
								acceptorDirections.add(sideFrom);
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			FMLLog.severe("Energy Network: Error when trying to refresh list of power acceptors.");
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
		return "EnergyNetwork[" + this.hashCode() + "|Wires:" + this.getTransmitters().size() + "|Acceptors:" + this.possibleAcceptors.size() + "]";
	}
}