package micdoodle8.mods.galacticraft.planets.venus.tile;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.Pathfinder;
import micdoodle8.mods.galacticraft.api.transmission.grid.PathfinderChecker;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A universal network that works with multiple energy systems.
 *
 * @author radfast, micdoodle8, Calclavia, Aidancbrady
 */
public class SolarModuleNetwork implements IGridNetwork<SolarModuleNetwork, ITransmitter, TileEntity>
{
    private final Set<ITransmitter> transmitters = new HashSet<>();

    public SolarModuleNetwork()
    {
    }

    public SolarModuleNetwork(Collection<SolarModuleNetwork> toMerge)
    {
        for (SolarModuleNetwork network : toMerge)
        {
            if (network != null)
            {
                for (ITransmitter transmitter : network.transmitters)
                {
                    transmitter.setNetwork(this);
                    this.transmitters.add(transmitter);
                }
                network.unregister();
            }
        }

        this.register();
    }


    /**
     * Refresh validity of each conductor in the network
     */
    public void refreshWithChecks()
    {
        Iterator<ITransmitter> it = this.transmitters.iterator();
        while (it.hasNext())
        {
            ITransmitter transmitter = it.next();

            if (transmitter == null)
            {
                it.remove();
                continue;
            }

            TileEntity tile = (TileEntity) transmitter;
            World world = tile.getWorld();
            //Remove any transmitters in unloaded chunks
            if (tile.isInvalid() || world == null || !world.isBlockLoaded(tile.getPos()))
            {
                it.remove();
                continue;
            }

            if (transmitter != world.getTileEntity(tile.getPos()))
            {
                it.remove();
                continue;
            }

            if (transmitter.getNetwork() != this)
            {
                transmitter.setNetwork(this);
                transmitter.onNetworkChanged();
            }
        }
    }

    @Override
    public void refresh()
    {
        Iterator<ITransmitter> it = this.transmitters.iterator();
        while (it.hasNext())
        {
            ITransmitter conductor = it.next();

            if (conductor == null)
            {
                it.remove();
                continue;
            }

            TileEntity tile = (TileEntity) conductor;
            World world = tile.getWorld();
            //Remove any transmitters in unloaded chunks
            if (tile.isInvalid() || world == null)
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

    /**
     * Refresh all energy acceptors in the network
     */
    private void refreshAcceptors()
    {
        this.refreshWithChecks();

//        try
//        {
//            LinkedList<ITransmitter> conductorsCopy = new LinkedList<>();
//            conductorsCopy.addAll(this.transmitters);
//            //This prevents concurrent modifications if something in the loop causes chunk loading
//            //(Chunk loading can change the network if new transmitters are found)
//            for (ITransmitter conductor : conductorsCopy)
//            {
//                EnergyUtil.setAdjacentPowerConnections((TileEntity) conductor, this.connectedAcceptors, this.connectedDirections);
//            }
//        }
//        catch (Exception e)
//        {
//            FMLLog.severe("GC Aluminium Wire: Error when testing whether another mod's tileEntity can accept energy.");
//            e.printStackTrace();
//        }
    }

    @Override
    public SolarModuleNetwork merge(SolarModuleNetwork network)
    {
        if (network != null && network != this)
        {
            SolarModuleNetwork newNetwork = new SolarModuleNetwork(Lists.newArrayList(this, network));
            newNetwork.refresh();
            return newNetwork;
        }

        return this;
    }

    private void destroy()
    {
        this.transmitters.clear();
    }

    @Override
    public void split(ITransmitter splitPoint)
    {
        if (splitPoint instanceof TileEntity)
        {
            this.transmitters.remove(splitPoint);

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
                            Pathfinder finder = new PathfinderChecker(((TileEntity) splitPoint).getWorld(), (INetworkConnection) connectedBlockB, NetworkType.SOLAR_MODULE, splitPoint);
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
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).getWorld());

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
                                SolarModuleNetwork newNetwork = new SolarModuleNetwork();

                                for (BlockVec3 node : finder.closedSet)
                                {
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).getWorld());

                                    if (nodeTile instanceof TileEntitySolarArrayModule)
                                    {
                                        if (nodeTile != splitPoint)
                                        {
                                            newNetwork.transmitters.add((TileEntitySolarArrayModule) nodeTile);
                                            this.transmitters.remove(nodeTile);
                                        }
                                    }
                                }

                                newNetwork.refresh();
                                newNetwork.register();
                            }
                        }
                    }
                }
            }

            if (this.transmitters.isEmpty())
            {
                this.unregister();
            }
        }
    }

    public void register()
    {
        GalacticraftPlanets.proxy.registerNetwork(this);
    }

    public void unregister()
    {
        GalacticraftPlanets.proxy.unregisterNetwork(this);
    }

    public void addTransmitter(ITransmitter transmitter)
    {
        this.transmitters.add(transmitter);
        this.refresh();
    }

    public void removeTransmitter(ITransmitter transmitter)
    {
        this.transmitters.remove(transmitter);
    }

    @Override
    public Set<ITransmitter> getTransmitters()
    {
        return transmitters;
    }

    @Override
    public String toString()
    {
        return "SolarModuleNetwork[" + this.hashCode() + "|Wires:" + this.getTransmitters().size() + "]";
    }
}
