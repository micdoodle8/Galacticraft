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

import mekanism.api.energy.IStrictEnergyAcceptor;
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
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cofh.api.energy.IEnergyHandler;
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
	private float totalRequested = 0F;
	private float totalEnergy = 0F;
	private float totalSent = 0F;
	private boolean doneScheduled = false;
	private boolean spamstop = false;
	
	/*
	 * connectedAcceptors is all the acceptors connected to this network
	 * connectedDirections is the directions of those connections (from the point of view of the acceptor tile)
	 *   Note: each position in those two linked lists matches
	 *         so, an acceptor connected on two sides will be in connectedAcceptors twice 
	 */
	private List<TileEntity> connectedAcceptors = new LinkedList<TileEntity>();
	private List<ForgeDirection> connectedDirections = new LinkedList<ForgeDirection>();
	
	/*
	 *  availableAcceptors is the acceptors which can receive energy (this tick)
	 *  availableconnectedDirections is a map of those acceptors and the directions they will receive from (from the point of view of the acceptor tile)
	 *    Note: each acceptor will only be included once in these collections
	 *          (there is no point trying to put power into a machine twice from two different sides)
	 */
	private Set<TileEntity> availableAcceptors = new HashSet<TileEntity>();
	private Map<TileEntity, ForgeDirection> availableconnectedDirections = new HashMap<TileEntity, ForgeDirection>();
	
	private Map<TileEntity,Float> energyRequests = new HashMap<TileEntity,Float>();
	private List<TileEntity> ignoreAcceptors = new LinkedList<TileEntity>();
	
	@Override
	public ElectricityPack getRequest(TileEntity... ignoreTiles)
	{
		if (UniversalNetwork.tickCount!=this.tickDone)
		{	
			this.tickDone = UniversalNetwork.tickCount;
			//Start the new tick - initialise everything
			doTickStartCalc(ignoreTiles);
			
			if (NetworkConfigHandler.isBuildcraftLoaded())
			{
				try
				{
					Class<?> clazz = Class.forName("micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityUniversalConductor");

					for (IConductor wire : this.getTransmitters())
					{
						if (clazz.isInstance(wire))
						{
							//This will call getRequest() but that's no problem, on the second call it will just return the totalRequested
							clazz.getMethod("reconfigureBC").invoke(wire);
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return ElectricityPack.getFromWatts(this.totalRequested - this.totalEnergy - this.totalSent, 120F);
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
					this.tickDone = UniversalNetwork.tickCount;
					//Start the new tick - initialise everything
					doTickStartCalc(ignoreTiles);
				}
		
				if (!this.doneScheduled && this.totalRequested > 0.0F)
				{
					try
					{
						Class<?> clazz = Class.forName("micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerServer");
						clazz.getMethod("scheduleNetworkTick", getClass()).invoke(null, this);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
					this.doneScheduled = true;
				}
		
				this.ignoreAcceptors.addAll(Arrays.asList(ignoreTiles));
				
				//On a regular mid-tick produce(), just figure out how much is totalEnergy this tick and return the used amount
				//This will return 0 if totalRequested is 0 - for example a network with no acceptors
				float totalEnergyLast = this.totalEnergy;
				
				//Add the energy for distribution by this grid later this tick
				//Note: totalEnergy cannot exceed totalRequested
				if (doReceive) this.totalEnergy += Math.min(energyToProduce, this.totalRequested - totalEnergyLast);
				
				if (this.totalRequested >= totalEnergyLast + energyToProduce) return 0F; //All the electricity will be used
				if (totalEnergyLast >= this.totalRequested) return energyToProduce; //None of the electricity will be used
				return totalEnergyLast + energyToProduce - this.totalRequested; //Some of the electricity will be used
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
			this.totalSent = doProduce();
			if (this.totalSent < this.totalEnergy)
				//Any spare energy left is retained for the next tick
				this.totalEnergy -= this.totalSent;
			else totalEnergy = 0F;
		} else totalEnergy = 0F;	
	}

	private void doTickStartCalc(TileEntity... ignoreTiles)
	{
		this.totalSent = 0F;
		refreshAcceptors();
		 
		if (this.getTransmitters().size() == 0) return;
		
		this.availableAcceptors.clear();
		this.availableconnectedDirections.clear();
		this.energyRequests.clear();
		this.totalRequested = 0.0F;
		this.ignoreAcceptors.clear();
		
		List<TileEntity> ignored = Arrays.asList(ignoreTiles);
		
		boolean isTELoaded = NetworkConfigHandler.isThermalExpansionLoaded();
		boolean isIC2Loaded = NetworkConfigHandler.isIndustrialCraft2Loaded();
		boolean isBCLoaded = NetworkConfigHandler.isBuildcraftLoaded();
		boolean isMekLoaded = NetworkConfigHandler.isMekanismLoaded();

		if(!this.connectedAcceptors.isEmpty())
		{
			float e;
			final Iterator<ForgeDirection> acceptorDirection = this.connectedDirections.iterator();
			for(TileEntity acceptor : this.connectedAcceptors)
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
					else if (isMekLoaded && acceptor instanceof IStrictEnergyAcceptor)
					{
						e = (float) ((((IStrictEnergyAcceptor) acceptor).getMaxEnergy() - ((IStrictEnergyAcceptor) acceptor).getEnergy()) * NetworkConfigHandler.MEKANISM_RATIO);
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
						this.availableconnectedDirections.put(acceptor,sideFrom);
						this.energyRequests.put(acceptor,Float.valueOf(e));
						this.totalRequested += e;
					}
				}
			}
		}
			
		ElectricityPack mergedPack = new ElectricityPack(totalRequested, 1);
		ElectricityRequestEvent evt = new ElectricityRequestEvent(this, mergedPack, ignoreTiles);
		MinecraftForge.EVENT_BUS.post(evt);
		this.totalRequested = mergedPack.getWatts();
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
			boolean isMekLoaded = NetworkConfigHandler.isMekanismLoaded();
			
			ArrayList<TileEntity> acceptors = new ArrayList();
			acceptors.addAll(this.availableAcceptors);
			Collections.shuffle(acceptors);

			int divider = acceptors.size();
			double remaining = this.totalEnergy % divider;
			double sending = (this.totalEnergy - remaining) / divider;
			float sentToAcceptor;

			for (TileEntity tileEntity : acceptors)
			{
				double currentSending = sending + remaining;
				ForgeDirection sideFrom = this.availableconnectedDirections.get(tileEntity);

				remaining = 0D;

				if (tileEntity instanceof IElectrical)
				{
					ElectricityPack electricityToSend = ElectricityPack.getFromWatts((float) currentSending, 120F);
					sentToAcceptor = ((IElectrical) tileEntity).receiveElectricity(sideFrom, electricityToSend, true);
				}
				else if (isMekLoaded && tileEntity instanceof IStrictEnergyAcceptor)
				{
					IStrictEnergyAcceptor receiver = (IStrictEnergyAcceptor) tileEntity;
					double mekToSend = currentSending * NetworkConfigHandler.TO_MEKANISM_RATIO;
					sentToAcceptor = (float) receiver.transferEnergyToAcceptor(sideFrom, mekToSend);
				}
				else if (isTELoaded && tileEntity instanceof IEnergyHandler)
				{
					IEnergyHandler handler = (IEnergyHandler) tileEntity;
					int currentSendinginRF = (currentSending >= Integer.MAX_VALUE / NetworkConfigHandler.TO_TE_RATIO) ? Integer.MAX_VALUE : (int) (currentSending * NetworkConfigHandler.TO_TE_RATIO);
					sentToAcceptor = handler.receiveEnergy(sideFrom, currentSendinginRF, false) * NetworkConfigHandler.TE_RATIO;
				}
				else if (isIC2Loaded && tileEntity instanceof IEnergySink)
				{
					IEnergySink electricalTile = (IEnergySink) tileEntity;
					double toSendIC2 = Math.min(currentSending * NetworkConfigHandler.TO_IC2_RATIO, electricalTile.getMaxSafeInput());
					toSendIC2 = Math.min(toSendIC2, electricalTile.demandedEnergyUnits());
					sentToAcceptor = (float) (toSendIC2 - electricalTile.injectEnergyUnits(sideFrom, toSendIC2)) * NetworkConfigHandler.IC2_RATIO;
				}
				else if (isBCLoaded && tileEntity instanceof IPowerReceptor)
				{
					PowerReceiver receiver = ((IPowerReceptor) tileEntity).getPowerReceiver(sideFrom);

					if (receiver != null)
					{
						float req = receiver.powerRequest();
						float bcToSend = (float) currentSending * NetworkConfigHandler.TO_BC_RATIO;
						sentToAcceptor = receiver.receiveEnergy(Type.PIPE, Math.min(req, bcToSend), sideFrom) * NetworkConfigHandler.BC3_RATIO;
					} else sentToAcceptor = 0F;
				}
				else sentToAcceptor = 0F;
				
				if (sentToAcceptor / currentSending > 1.002D && sentToAcceptor > 1F)
				{	
					if (!this.spamstop)
					{
						FMLLog.info("Energy network: acceptor took too much energy, offered "+currentSending+", took "+sentToAcceptor+". "+tileEntity.toString());
						this.spamstop = true;
					}
					sentToAcceptor = (float) currentSending;
				}
				else
				{
					//Offer the surplus energy to the next acceptor (this is a bit random as it depends on the order)
					remaining = currentSending - sentToAcceptor;
					if (remaining < 0D) remaining = 0D; 
				}
				
				sent += sentToAcceptor;
			}
		}

		if (this.tickCount % 200 == 0) this.spamstop = false;
		
		float returnvalue = sent;
		if (returnvalue > this.totalEnergy) returnvalue = this.totalEnergy;
		if (returnvalue < 0F) returnvalue = 0F;
		return returnvalue;
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

			TileEntity tile = (TileEntity) conductor; 
			World world = tile.getWorldObj();
			//Remove any conductors in unloaded chunks
			if (tile.isInvalid() || world == null || !world.blockExists(tile.xCoord, tile.yCoord, tile.zCoord))
			{
				it.remove();
				continue;
			}
			
			if (conductor != world.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord))
			{
				it.remove();
				continue;
			}

			if (conductor.getNetwork() != this)
			{
				conductor.setNetwork(this);
				conductor.onNetworkChanged();
			}
		}
	}

	private void refreshAcceptors()
	{
		this.connectedAcceptors.clear();
		this.connectedDirections.clear();

		this.refresh();

		try
		{
			boolean isTELoaded = NetworkConfigHandler.isThermalExpansionLoaded();
			boolean isIC2Loaded = NetworkConfigHandler.isIndustrialCraft2Loaded();
			boolean isBCLoaded = NetworkConfigHandler.isBuildcraftLoaded();
			boolean isMekLoaded = NetworkConfigHandler.isMekanismLoaded();

			LinkedList<IConductor> conductors = new LinkedList();
			conductors.addAll(this.getTransmitters());
			//This prevents concurrent modifications if something in the loop causes chunk loading
			//(Chunk loading can change the network if new conductors are found)
			for(IConductor conductor : conductors)
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
								this.connectedAcceptors.add(acceptor);
								this.connectedDirections.add(sideFrom);
							}
						}
						else if (isMekLoaded && acceptor instanceof IStrictEnergyAcceptor)
						{
							if (((IStrictEnergyAcceptor) acceptor).canReceiveEnergy(sideFrom))
							{
								this.connectedAcceptors.add(acceptor);
								this.connectedDirections.add(sideFrom);
							}
						}
						else if (isTELoaded && acceptor instanceof IEnergyHandler)
						{
							if (((IEnergyHandler) acceptor).canInterface(sideFrom))
							{
								this.connectedAcceptors.add(acceptor);
								this.connectedDirections.add(sideFrom);
							}
						}
						else if (isIC2Loaded && acceptor instanceof IEnergyAcceptor)
						{
							if(((IEnergyAcceptor)acceptor).acceptsEnergyFrom((TileEntity) conductor, sideFrom))
							{
								this.connectedAcceptors.add(acceptor);
								this.connectedDirections.add(sideFrom);
							}
						}
						else if (isBCLoaded && acceptor instanceof IPowerReceptor)
						{
							if (((IPowerReceptor) acceptor).getPowerReceiver(sideFrom) != null)
							{
								this.connectedAcceptors.add(acceptor);
								this.connectedDirections.add(sideFrom);
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
			Set<IConductor> thisNetwork = this.getTransmitters();
			Set<IConductor> thatNetwork = network.getTransmitters();
			if (thisNetwork.size() >= thatNetwork.size())
			{
				thisNetwork.addAll(thatNetwork);
				this.refresh();
				if (network instanceof UniversalNetwork) ((UniversalNetwork) network).destroy();
				return this;
			}
			else
			{
				thatNetwork.addAll(thisNetwork);
				network.refresh();
				this.destroy();
				return network;
			}
		}

		return this;
	}

	private void destroy()
	{
		this.getTransmitters().clear();
		this.connectedAcceptors.clear();
		this.availableAcceptors.clear();
		this.totalEnergy = 0F;
		this.totalRequested = 0F;
		try
		{
			Class<?> clazz = Class.forName("micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerServer");
			clazz.getMethod("removeNetworkTick", getClass()).invoke(null, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

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
		return "EnergyNetwork[" + this.hashCode() + "|Wires:" + this.getTransmitters().size() + "|Acceptors:" + this.connectedAcceptors.size() + "]";
	}
}