package micdoodle8.mods.galacticraft.core.oxygen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.Pathfinder;
import micdoodle8.mods.galacticraft.api.transmission.grid.PathfinderChecker;
import micdoodle8.mods.galacticraft.api.transmission.tile.*;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class LiquidNetwork implements IGridNetwork<LiquidNetwork, IBufferTransmitter<FluidStack>, TileEntity>
{
    public Map<BlockPos, IFluidHandler> acceptors = Maps.newHashMap();
    public Map<BlockPos, EnumSet<EnumFacing>> acceptorDirections = Maps.newHashMap();
    private final Set<IBufferTransmitter<FluidStack>> pipes = new HashSet<>();
    private FluidStack buffer;
    private int capacity;
    private World worldObj;
    private int prevBufferAmount;
    private boolean needsUpdate;

    public LiquidNetwork()
    {

    }

    public LiquidNetwork(Collection<LiquidNetwork> toMerge)
    {
        for (LiquidNetwork network : toMerge)
        {
            if (network != null)
            {
                if (network.buffer != null)
                {
                    if (this.buffer == null)
                    {
                        this.buffer = network.buffer.copy();
                    }
                    else
                    {
                        if (buffer.getFluid() == network.buffer.getFluid())
                        {
                            buffer.amount += network.buffer.amount;
                        }
                        else if (network.buffer.amount > buffer.amount)
                        {
                            this.buffer = network.buffer.copy();
                        }
                    }

                    network.buffer = null;
                }

                this.pipes.addAll(network.getTransmitters());
                network.unregister();
            }
        }

        this.register();
    }

    public void register()
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            TickHandlerServer.addLiquidNetwork(this);
        }
    }

    public void unregister()
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            TickHandlerServer.removeLiquidNetwork(this);
        }
    }

    public void addTransmitter(IBufferTransmitter<FluidStack> transmitter)
    {
        FluidStack stack = transmitter.getBuffer();

        if (stack == null || stack.getFluid() == null || stack.amount == 0)
        {
            // Nothing to do
            return;
        }

        if (buffer == null || buffer.getFluid() == null || buffer.amount == 0)
        {
            // Set transmitter buffer to network buffer
            buffer = stack.copy();
            stack.amount = 0;
            return;
        }

        if (buffer.isFluidEqual(stack))
        {
            // Add transmitter fluid to network buffer
            buffer.amount += stack.amount;
        }

        stack.amount = 0;
    }

    public void clamp()
    {
        if (buffer != null && buffer.amount > getCapacity())
        {
            buffer.amount = this.capacity;
        }
    }

    public void updateCapacity()
    {
        this.capacity = 0;

        for (IBufferTransmitter<FluidStack> transmitter : getTransmitters())
        {
            this.capacity += transmitter.getCapacity();
        }
    }

    public int getCapacity()
    {
        return capacity;
    }

    public int getRequest()
    {
        return getCapacity() - (buffer != null ? buffer.amount : 0);
    }

    private int emitToAcceptors(FluidStack toSend, boolean doTransfer)
    {
        List<Pair<BlockPos, IFluidHandler>> available = new ArrayList<>();
        available.addAll(this.getAcceptors(toSend));

        Collections.shuffle(available);

        int totalSend = 0;

        if (!available.isEmpty())
        {
            int divider = available.size();
            int remainder = toSend.amount % divider;
            int each = (toSend.amount - remainder) / divider;

            for (Pair<BlockPos, IFluidHandler> pair : available)
            {
                int currentSend = each;
                IFluidHandler acceptor = pair.getRight();
                EnumSet<EnumFacing> sides = acceptorDirections.get(pair.getLeft());

                if (remainder > 0)
                {
                    currentSend++;
                    remainder--;
                }

                for (EnumFacing side : sides)
                {
                    int prev = totalSend;

                    if (acceptor != null)
                    {
                        FluidStack copy = toSend.copy();
                        copy.amount = currentSend;
                        totalSend += acceptor.fill(side, copy, doTransfer);
                    }

                    if (totalSend > prev)
                    {
                        // If fluid was sent to this handler, continue to next
                        break;
                    }
                }
            }
        }

        return totalSend;
    }

    public int emitToBuffer(FluidStack toSend, boolean doTransfer)
    {
        if (toSend == null || (buffer != null && buffer.getFluid() != toSend.getFluid()))
        {
            return 0;
        }

        int toUse = Math.min(getRequest(), toSend.amount);

        if (doTransfer)
        {
            if (buffer == null)
            {
                // Copy
                buffer = toSend.copy();
                buffer.amount = toUse;
            }
            else
            {
                // Add
                buffer.amount += toUse;
            }
        }

        return toUse;
    }

    public void tickEnd()
    {
        this.onUpdate();
    }

    public void onUpdate()
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            int stored = buffer != null ? buffer.amount : 0;

            if (stored != prevBufferAmount)
            {
                this.needsUpdate = true;
            }

            prevBufferAmount = stored;

            if (buffer != null)
            {
                int prevTransferAmount = this.emitToAcceptors(buffer, true);
                if (buffer != null)
                {
                    buffer.amount -= prevTransferAmount;

                    if (buffer.amount <= 0)
                    {
                        this.buffer = null;
                    }
                }
            }
        }
    }

    public Set<Pair<BlockPos, IFluidHandler>> getAcceptors(FluidStack toSend)
    {
        Set<Pair<BlockPos, IFluidHandler>> toReturn = new HashSet<>();

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            return toReturn;
        }

        if (this.acceptors == null || this.acceptors.isEmpty())
        {
            this.refreshAcceptors();
        }

        for (BlockPos coords : acceptors.keySet())
        {
            EnumSet<EnumFacing> sides = acceptorDirections.get(coords);
            TileEntity tile = this.worldObj.getTileEntity(coords);

            if (sides == null || sides.isEmpty() || !(tile instanceof IFluidHandler))
            {
                continue;
            }

            IFluidHandler acceptor = (IFluidHandler) tile;

            for (EnumFacing side : sides)
            {
                if (acceptor.canFill(side, toSend.getFluid()))
                {
                    toReturn.add(Pair.of(coords, acceptor));
                }
            }
        }

        return toReturn;
    }

    @Override
    public void refresh()
    {
        if (this.acceptors != null)
            this.acceptors.clear();

        try
        {
            Iterator<IBufferTransmitter<FluidStack>> it = this.pipes.iterator();

            while (it.hasNext())
            {
                ITransmitter transmitter = it.next();
                TileEntity tileTransmitter = (TileEntity) transmitter;

                if (transmitter == null)
                {
                    it.remove();
                    continue;
                }

                transmitter.onNetworkChanged();

                if (tileTransmitter.isInvalid() || tileTransmitter.getWorld() == null)
                {
                    it.remove();
                    continue;
                }
                else
                {
                    if (this.worldObj == null)
                    {
                        this.worldObj = tileTransmitter.getWorld();
                    }

                    transmitter.setNetwork(this);
                }
            }

            updateCapacity();
            clamp();
        }
        catch (Exception e)
        {
            FMLLog.severe("Failed to refresh liquid pipe network.");
            e.printStackTrace();
        }
    }

    public void refreshAcceptors()
    {
        if (this.acceptors == null)
        {
            this.acceptors = Maps.newHashMap();
        }
        else
        {
            this.acceptors.clear();
        }

        try
        {
            Iterator<IBufferTransmitter<FluidStack>> it = this.pipes.iterator();

            while (it.hasNext())
            {
                IBufferTransmitter<FluidStack> transmitter = it.next();
                TileEntity tile = (TileEntity) transmitter;

                if (transmitter == null || tile.isInvalid() || tile.getWorld() == null)
                {
                    it.remove();
                    continue;
                }

                int i = 0;
                for (TileEntity acceptor : transmitter.getAdjacentConnections())
                {
                    if (!(acceptor instanceof IBufferTransmitter) && acceptor instanceof IFluidHandler)
                    {
                        EnumFacing facing = EnumFacing.getFront(i).getOpposite();
                        BlockPos acceptorPos = tile.getPos().offset(facing.getOpposite());
                        EnumSet<EnumFacing> facingSet = this.acceptorDirections.get(tile.getPos());
                        if (facingSet != null)
                        {
                            facingSet.add(facing);
                        }
                        else
                        {
                            facingSet = EnumSet.of(facing);
                        }
                        this.acceptors.put(acceptorPos, (IFluidHandler) acceptor);
                        this.acceptorDirections.put(acceptorPos, facingSet);
                    }
                    i++;
                }
            }
        }
        catch (Exception e)
        {
            FMLLog.severe("Failed to refresh liquid acceptors");
            e.printStackTrace();
        }
    }

    @Override
    public Set<IBufferTransmitter<FluidStack>> getTransmitters()
    {
        return this.pipes;
    }

    @Override
    public LiquidNetwork merge(LiquidNetwork network)
    {
        if (network != null && network != this)
        {
            LiquidNetwork newNetwork = new LiquidNetwork(Lists.newArrayList(this, network));
            newNetwork.refresh();
            return newNetwork;
        }

        return this;
    }

    @Override
    public void split(IBufferTransmitter<FluidStack> splitPoint)
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
                            Pathfinder finder = new PathfinderChecker(((TileEntity) splitPoint).getWorld(), (INetworkConnection) connectedBlockB, NetworkType.FLUID, splitPoint);
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
                                LiquidNetwork newNetwork = new LiquidNetwork();

                                for (BlockVec3 node : finder.closedSet)
                                {
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).getWorld());

                                    if (nodeTile instanceof IBufferTransmitter)
                                    {
                                        if (nodeTile != splitPoint)
                                        {
                                            newNetwork.getTransmitters().add((IBufferTransmitter) nodeTile);
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

            if (this.pipes.isEmpty())
            {
                this.unregister();
            }
        }
    }

    @Override
    public String toString()
    {
        return "LiquidNetwork[" + this.hashCode() + "|Pipes:" + this.pipes.size() + "|Acceptors:" + (this.acceptors == null ? 0 : this.acceptors.size()) + "]";
    }
}
