package micdoodle8.mods.galacticraft.planets.venus.tile;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.*;

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
            this.getTransmitters().remove(splitPoint);
            splitPoint.setNetwork(null);

            //If the size of the residual network is 1, it should simply be preserved
            if (this.getTransmitters().size() > 1)
            {
                World world = ((TileEntity) splitPoint).getWorld();

                if (this.getTransmitters().size() > 0)
                {
                    ITransmitter[] nextToSplit = new ITransmitter[6];
                    boolean[] toDo = { true, true, true, true, true, true };
                    TileEntity tileEntity;

                    for (int j = 0; j < 6; j++)
                    {
                        switch (j)
                        {
                            case 0:
                                tileEntity = world.getTileEntity(((TileEntity) splitPoint).getPos().down());
                                break;
                            case 1:
                                tileEntity = world.getTileEntity(((TileEntity) splitPoint).getPos().up());
                                break;
                            case 2:
                                tileEntity = world.getTileEntity(((TileEntity) splitPoint).getPos().north());
                                break;
                            case 3:
                                tileEntity = world.getTileEntity(((TileEntity) splitPoint).getPos().south());
                                break;
                            case 4:
                                tileEntity = world.getTileEntity(((TileEntity) splitPoint).getPos().west());
                                break;
                            case 5:
                                tileEntity = world.getTileEntity(((TileEntity) splitPoint).getPos().east());
                                break;
                            default:
                                //Not reachable, only to prevent uninitiated compile errors
                                tileEntity = null;
                                break;
                        }

                        if (tileEntity instanceof ITransmitter)
                        {
                            nextToSplit[j] = (ITransmitter) tileEntity;
                        }
                        else
                        {
                            toDo[j] = false;
                        }
                    }

                    for (int i1 = 0; i1 < 6; i1++)
                    {
                        if (toDo[i1])
                        {
                            ITransmitter connectedBlockA = nextToSplit[i1];
                            NetworkFinderSolar finder = new NetworkFinderSolar(world, new BlockVec3((TileEntity) connectedBlockA), new BlockVec3((TileEntity) splitPoint));
                            List<ITransmitter> partNetwork = finder.exploreNetwork();

                            //Mark any others still to do in the nextToSplit array which are connected to this, as dealt with
                            for (int i2 = i1 + 1; i2 < 6; i2++)
                            {
                                ITransmitter connectedBlockB = nextToSplit[i2];

                                if (toDo[i2])
                                {
                                    if (partNetwork.contains(connectedBlockB))
                                    {
                                        toDo[i2] = false;
                                    }
                                }
                            }

                            //Now make the new network from partNetwork
                            SolarModuleNetwork newNetwork = new SolarModuleNetwork();
                            newNetwork.getTransmitters().addAll(partNetwork);
                            newNetwork.refreshWithChecks();
                            newNetwork.register();
                        }
                    }

                    this.destroy();
                }
            }
            //Splitting a 1-block network leaves nothing
            else if (this.getTransmitters().size() == 0)
            {
                this.destroy();
            }
        }

        if (this.transmitters.isEmpty())
        {
            this.unregister();
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
