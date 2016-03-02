package micdoodle8.mods.galacticraft.core.oxygen;

import cpw.mods.fml.common.FMLLog;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IOxygenNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.Pathfinder;
import micdoodle8.mods.galacticraft.api.transmission.grid.PathfinderChecker;
import micdoodle8.mods.galacticraft.api.transmission.tile.*;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

/**
 * An Oxygen Network comprised of ITransmitter which can transmit oxygen
 */
public class OxygenNetwork implements IOxygenNetwork
{
    public Map<TileEntity, ForgeDirection> oxygenTiles;

    private final Set<ITransmitter> pipes = new HashSet<ITransmitter>();

    @Override
    public float produce(float totalOxygen, TileEntity... ignoreTiles)
    {
        float remainingUsableOxygen = totalOxygen;

        if (this.oxygenTiles == null || this.oxygenTiles.isEmpty())
        	this.refreshOxygenTiles();

        if (!this.oxygenTiles.isEmpty())
        {
            final float totalOxygenRequest = this.getRequest(ignoreTiles);

            if (totalOxygenRequest > 0)
            {
                List<TileEntity> ignoreTilesList = Arrays.asList(ignoreTiles);
                for (TileEntity tileEntity : new HashSet<TileEntity>(this.oxygenTiles.keySet()))
                {
                    if (!ignoreTilesList.contains(tileEntity))
                    {
                        if (tileEntity instanceof IOxygenReceiver)
                        {
                            IOxygenReceiver oxygenTile = (IOxygenReceiver) tileEntity;

                            if (oxygenTile.shouldPullOxygen())
                            {
                                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                                {
                                    if (oxygenTile.canConnect(direction, NetworkType.OXYGEN))
                                    {
	                                	TileEntity tile = new BlockVec3(tileEntity).getTileEntityOnSide(tileEntity.getWorldObj(), direction);
	
	                                    if (this.pipes.contains(tile))
	                                    {
	                                        float oxygenToSend = Math.min(remainingUsableOxygen, totalOxygen * (oxygenTile.getOxygenRequest(direction) / totalOxygenRequest));
	
	                                        if (oxygenToSend > 0)
	                                        {
	                                            remainingUsableOxygen -= oxygenTile.receiveOxygen(direction, oxygenToSend, true);
	                                        }
	                                    }
                                    }
                                }
                            }
                        }
                        else if (EnergyConfigHandler.isMekanismLoaded() && tileEntity instanceof IGasHandler)
                        {
                            IGasHandler gasHandler = (IGasHandler) tileEntity;

                            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                            {
                                if (gasHandler.canReceiveGas(direction, (Gas) EnergyConfigHandler.gasOxygen))
                                {
                                	TileEntity tile = new BlockVec3(tileEntity).getTileEntityOnSide(tileEntity.getWorldObj(), direction);

                                	if (this.getTransmitters().contains(tile))
                                	{
                                		int oxygenToSend = (int) Math.floor(totalOxygen / this.oxygenTiles.size());

                                		if (oxygenToSend > 0)
                                		{
                                			try {
                                				remainingUsableOxygen -= gasHandler.receiveGas(direction, (new GasStack((Gas) EnergyConfigHandler.gasOxygen, oxygenToSend)));
                                			} catch (Exception e) { }
                                		}
                                	}
                                }
                            }
                        }
                    }
                }
            }
        }

        return remainingUsableOxygen;
    }

    /**
     * @return How much oxygen this network needs.
     */
    @Override
    public float getRequest(TileEntity... ignoreTiles)
    {
        List<Float> requests = new ArrayList<Float>();
        
        if (this.oxygenTiles == null || this.oxygenTiles.isEmpty())
        	this.refreshOxygenTiles();

        List<TileEntity> ignoreTilesList = Arrays.asList(ignoreTiles);
        for (TileEntity tileEntity : new HashSet<TileEntity>(this.oxygenTiles.keySet()))
        {
            if (ignoreTilesList.contains(tileEntity))
            {
                continue;
            }

            if (tileEntity instanceof IOxygenReceiver && !tileEntity.isInvalid())
            {
                IOxygenReceiver oxygenTile = (IOxygenReceiver) tileEntity;

                if (oxygenTile.shouldPullOxygen())
                {
                    if (tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) == tileEntity)
                    {
                        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                        {
                            if (oxygenTile.canConnect(direction, NetworkType.OXYGEN))
                            {
                            	TileEntity tile = new BlockVec3(tileEntity).getTileEntityOnSide(tileEntity.getWorldObj(), direction);

                            	if (this.pipes.contains(tile))
                            	{
                            		requests.add(((IOxygenReceiver) tileEntity).getOxygenRequest(direction));
                            	}
                            }
                        }
                    }
                }
            }
        }

        float total = 0.0F;

        for (Float f : requests)
        {
            total += f;
        }

        return total;
    }

    /**
     * This function is called to refresh all conductors in this network
     */
    @Override
    public void refresh()
    {
    	if (this.oxygenTiles != null) this.oxygenTiles.clear();

        try
        {
            Iterator<ITransmitter> it = this.pipes.iterator();

            while (it.hasNext())
            {
                ITransmitter transmitter = it.next();

                if (transmitter == null)
                {
                    it.remove();
                    continue;
                }

                transmitter.onNetworkChanged();

                if (((TileEntity) transmitter).isInvalid() || ((TileEntity) transmitter).getWorldObj() == null)
                {
                    it.remove();
                    continue;
                }
                else
                {
                    transmitter.setNetwork(this);
                }
            }
        }
        catch (Exception e)
        {
            FMLLog.severe("Failed to refresh oxygen pipe network.");
            e.printStackTrace();
        }
    }

    public void refreshOxygenTiles()
    {
    	if (this.oxygenTiles == null)
    		this.oxygenTiles = new HashMap<TileEntity, ForgeDirection>();
    	else
    		this.oxygenTiles.clear();
    	
    	try
    	{
    		Iterator<ITransmitter> it = this.pipes.iterator();

    		while (it.hasNext())
    		{
    			ITransmitter transmitter = it.next();

                if (transmitter == null || ((TileEntity) transmitter).isInvalid() || ((TileEntity) transmitter).getWorldObj() == null)
                {
                    it.remove();
                    continue;
                }

                //This causes problems with Sealed Oxygen Pipes (and maybe also unwanted chunk loading)
/*                if (!(((TileEntity) transmitter).getWorldObj().getBlock(((TileEntity) transmitter).xCoord, ((TileEntity) transmitter).yCoord, ((TileEntity) transmitter).zCoord) instanceof BlockTransmitter))
                {
                    it.remove();
                    continue;
                }
*/ 			
    			int i = 0;
                for (TileEntity acceptor : transmitter.getAdjacentConnections())
    			{
    				if (!(acceptor instanceof ITransmitter) && acceptor instanceof IConnector)
    				{
    					this.oxygenTiles.put(acceptor, ForgeDirection.getOrientation(i));
    				}
    				i++;
    			}
    		}
    	}
        catch (Exception e)
        {
            FMLLog.severe("Failed to refresh oxygen pipe network.");
            e.printStackTrace();
        }
    }

    @Override
    public Set<ITransmitter> getTransmitters()
    {
        return this.pipes;
    }

    @Override
    public IOxygenNetwork merge(IOxygenNetwork network)
    {
        if (network != null && network != this)
        {
            OxygenNetwork newNetwork = new OxygenNetwork();
            newNetwork.pipes.addAll(this.pipes);
            newNetwork.pipes.addAll(network.getTransmitters());
            newNetwork.refresh();
            return newNetwork;
        }

        return this;
    }

    @Override
    public void split(ITransmitter splitPoint)
    {
        if (splitPoint instanceof TileEntity)
        {
            this.pipes.remove(splitPoint);

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
                            Pathfinder finder = new PathfinderChecker(((TileEntity) splitPoint).getWorldObj(), (INetworkConnection) connectedBlockB, NetworkType.OXYGEN, splitPoint);
                            finder.init(new BlockVec3(connectedBlockA));

                            if (finder.results.size() > 0)
                            {
                                /**
                                 * The connections A and B are still intact
                                 * elsewhere. Set all references of wire
                                 * connection into one network.
                                 */

                                for (BlockVec3 node : finder.closedSet)
                                {
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).getWorldObj());

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
                                IOxygenNetwork newNetwork = new OxygenNetwork();

                                for (BlockVec3 node : finder.closedSet)
                                {
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).getWorldObj());

                                    if (nodeTile instanceof INetworkProvider)
                                    {
                                        if (nodeTile != splitPoint)
                                        {
                                            newNetwork.getTransmitters().add((ITransmitter) nodeTile);
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
        return "OxygenNetwork[" + this.hashCode() + "|Pipes:" + this.pipes.size() + "|Acceptors:" + (this.oxygenTiles == null ? 0 : this.oxygenTiles.size()) + "]";
    }
}
