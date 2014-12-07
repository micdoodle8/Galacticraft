package micdoodle8.mods.galacticraft.planets.mars.tile;

import cpw.mods.fml.common.FMLLog;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IHydrogenNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.Pathfinder;
import micdoodle8.mods.galacticraft.api.transmission.grid.PathfinderChecker;
import micdoodle8.mods.galacticraft.api.transmission.tile.*;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.BlockTransmitter;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class HydrogenNetwork implements IHydrogenNetwork
{
    public Map<TileEntity, ForgeDirection> hydrogenTiles = new HashMap<TileEntity, ForgeDirection>();

    private final Set<ITransmitter> pipes = new HashSet<ITransmitter>();

    @Override
    public float produce(float totalHydrogen, TileEntity... ignoreTiles)
    {
        float remainingUsableHydrogen = totalHydrogen;

        if (this.hydrogenTiles.isEmpty())
        	this.refreshHydrogenTiles();

        if (!this.hydrogenTiles.isEmpty())
        {
            final float totalHydrogenRequest = this.getRequest(ignoreTiles);

            if (totalHydrogenRequest > 0)
            {
                List<TileEntity> ignoreTilesList = Arrays.asList(ignoreTiles);
                for (TileEntity tileEntity : new HashSet<TileEntity>(this.hydrogenTiles.keySet()))
                {
                    if (!ignoreTilesList.contains(tileEntity))
                    {
                        if (tileEntity instanceof TileEntityMethaneSynthesizer)
                        {
                        	TileEntityMethaneSynthesizer hydrogenTile = (TileEntityMethaneSynthesizer) tileEntity;

                            if (hydrogenTile.shouldPullHydrogen())
                            {
                                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                                {
                                    TileEntity tile = new BlockVec3(tileEntity).modifyPositionFromSide(direction, 1).getTileEntity(tileEntity.getWorldObj());

                                    if (hydrogenTile.canConnect(direction, NetworkType.HYDROGEN) && this.pipes.contains(tile))
                                    {
                                        float hydrogenToSend = Math.max(totalHydrogen, totalHydrogen * (hydrogenTile.getHydrogenRequest(direction) / totalHydrogenRequest));

                                        if (hydrogenToSend > 0)
                                        {
                                            remainingUsableHydrogen -= hydrogenTile.receiveHydrogen(direction, hydrogenToSend, true);
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
                                TileEntity tile = new BlockVec3(tileEntity).getTileEntityOnSide(tileEntity.getWorldObj(), direction);

                                if (gasHandler.canReceiveGas(direction, (Gas) EnergyConfigHandler.gasHydrogen) && this.getTransmitters().contains(tile))
                                {
                                    int hydrogenToSend = (int) Math.floor(totalHydrogen / this.hydrogenTiles.size());

                                    if (hydrogenToSend > 0)
                                    {
                                        remainingUsableHydrogen -= gasHandler.receiveGas(direction, (new GasStack((Gas) EnergyConfigHandler.gasHydrogen, hydrogenToSend)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return remainingUsableHydrogen;
    }

    /**
     * @return How much hydrogen this network needs.
     */
    @Override
    public float getRequest(TileEntity... ignoreTiles)
    {
        List<Float> requests = new ArrayList<Float>();
        
        if (this.hydrogenTiles.isEmpty())
        	this.refreshHydrogenTiles();

        List<TileEntity> ignoreTilesList = Arrays.asList(ignoreTiles);
        for (TileEntity tileEntity : new HashSet<TileEntity>(this.hydrogenTiles.keySet()))
        {
            if (ignoreTilesList.contains(tileEntity))
            {
                continue;
            }

            if (tileEntity instanceof TileEntityMethaneSynthesizer && ((TileEntityMethaneSynthesizer) tileEntity).shouldPullHydrogen())
            {
                if (!tileEntity.isInvalid())
                {
                    if (tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) == tileEntity)
                    {
                        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                        {
                            BlockVec3 tileVec = new BlockVec3(tileEntity);
                            TileEntity tile = tileVec.modifyPositionFromSide(direction, 1).getTileEntity(tileEntity.getWorldObj());

                            if (((TileEntityMethaneSynthesizer) tileEntity).canConnect(direction, NetworkType.HYDROGEN) && this.pipes.contains(tile))
                            {
                                requests.add(((TileEntityMethaneSynthesizer) tileEntity).getHydrogenRequest(direction));
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
    	this.hydrogenTiles.clear();

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
            FMLLog.severe("Failed to refresh hydrogen pipe network.");
            e.printStackTrace();
        }
    }

    public void refreshHydrogenTiles()
    {
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

                if (!(((TileEntity) transmitter).getWorldObj().getBlock(((TileEntity) transmitter).xCoord, ((TileEntity) transmitter).yCoord, ((TileEntity) transmitter).zCoord) instanceof BlockTransmitter))
                {
                    it.remove();
                    continue;
                }
   			
    			for (int i = 0; i < transmitter.getAdjacentConnections().length; i++)
    			{
    				TileEntity acceptor = transmitter.getAdjacentConnections()[i];

    				if (!(acceptor instanceof ITransmitter) && acceptor instanceof IConnector)
    				{
    					this.hydrogenTiles.put(acceptor, ForgeDirection.getOrientation(i));
    				}
    			}
    		}
    	}
        catch (Exception e)
        {
            FMLLog.severe("Failed to refresh hydrogen pipe network.");
            e.printStackTrace();
        }
    }

    @Override
    public Set<ITransmitter> getTransmitters()
    {
        return this.pipes;
    }

    @Override
    public IHydrogenNetwork merge(IHydrogenNetwork network)
    {
        if (network != null && network != this)
        {
            HydrogenNetwork newNetwork = new HydrogenNetwork();
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
                            Pathfinder finder = new PathfinderChecker(((TileEntity) splitPoint).getWorldObj(), (INetworkConnection) connectedBlockB, NetworkType.HYDROGEN, splitPoint);
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
                                IHydrogenNetwork newNetwork = new HydrogenNetwork();

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
        return "HydrogenNetwork[" + this.hashCode() + "|Pipes:" + this.pipes.size() + "|Acceptors:" + this.hydrogenTiles.size() + "]";
    }
}
