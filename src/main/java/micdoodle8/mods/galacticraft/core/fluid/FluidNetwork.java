package micdoodle8.mods.galacticraft.core.fluid;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.Pathfinder;
import micdoodle8.mods.galacticraft.api.transmission.grid.PathfinderChecker;
import micdoodle8.mods.galacticraft.api.transmission.tile.IBufferTransmitter;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacket;
import micdoodle8.mods.galacticraft.core.network.PacketFluidNetworkUpdate;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Based heavily on Mekanism FluidNetwork
 *
 * @author aidancbrady
 */
public class FluidNetwork implements IGridNetwork<FluidNetwork, IBufferTransmitter<FluidStack>, TileEntity>
{
    public Map<BlockPos, IFluidHandler> acceptors = Maps.newHashMap();
    public Map<BlockPos, EnumSet<Direction>> acceptorDirections = Maps.newHashMap();
    public final Set<IBufferTransmitter<FluidStack>> pipes = Sets.newHashSet();
    private final Set<IBufferTransmitter<FluidStack>> pipesAdded = Sets.newHashSet();
    private final Set<DelayQueue> updateQueue = Sets.newLinkedHashSet();
    public FluidStack buffer;
    private int capacity;
    private World world;
    private int prevBufferAmount;
    private boolean needsUpdate;
    public float fluidScale;
    public Fluid refFluid;
    public boolean didTransfer;
    public boolean prevTransfer;
    public int transferDelay = 0;
    private int prevTransferAmount;
    private int updateDelay;
    private boolean firstUpdate = true;

    public FluidNetwork()
    {
    }

    public FluidNetwork(Collection<FluidNetwork> toMerge)
    {
        for (FluidNetwork network : toMerge)
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
                            buffer.setAmount(buffer.getAmount() + network.buffer.getAmount());
//                            buffer.amount += network.buffer.amount;
                        }
                        else if (network.buffer.getAmount() > buffer.getAmount())
                        {
                            this.buffer = network.buffer.copy();
                        }
                    }

                    network.buffer = null;
                }

                this.adoptNetwork(network);
                network.unregister();
            }
        }

        this.register();
    }

    public void adoptNetwork(FluidNetwork network)
    {
        for (IBufferTransmitter<FluidStack> transmitter : network.pipes)
        {
            transmitter.setNetwork(this);
            this.pipes.add(transmitter);
            this.pipesAdded.add(transmitter);
            this.updateDelay = this.firstUpdate ? 3 : 1;
        }

        this.acceptors.putAll(network.acceptors);

        for (Map.Entry<BlockPos, EnumSet<Direction>> e : network.acceptorDirections.entrySet())
        {
            BlockPos pos = e.getKey();

            if (this.acceptorDirections.containsKey(pos))
            {
                this.acceptorDirections.get(pos).addAll(e.getValue());
            }
            else
            {
                this.acceptorDirections.put(pos, e.getValue());
            }
        }
    }

    public void register()
    {
        GalacticraftCore.proxy.registerNetwork(this);
    }

    public void unregister()
    {
        GalacticraftCore.proxy.unregisterNetwork(this);
    }

    public void addTransmitter(IBufferTransmitter<FluidStack> transmitter)
    {
        this.pipes.add(transmitter);
        this.pipesAdded.add(transmitter);
        this.refresh();
        this.updateDelay = this.firstUpdate ? 20 : 1;
    }

    public void removeTransmitter(IBufferTransmitter<FluidStack> transmitter)
    {
        this.pipes.remove(transmitter);
        this.updateCapacity();
    }

    public void onTransmitterAdded(IBufferTransmitter<FluidStack> transmitter)
    {
        FluidStack stack = transmitter.getBuffer();

        if (stack == null || stack == FluidStack.EMPTY || stack.getAmount() == 0)
        {
            // Nothing to do
            return;
        }

        if (buffer == null || buffer == FluidStack.EMPTY || buffer.getAmount() == 0)
        {
            // Set transmitter buffer to network buffer
            buffer = stack.copy();
            stack.setAmount(0);
            return;
        }

        if (buffer.isFluidEqual(stack))
        {
            // Add transmitter fluid to network buffer
            buffer.setAmount(buffer.getAmount() + stack.getAmount());
//            buffer.amount += stack.amount;
        }

//        stack.amount = 0;
        stack.setAmount(0);
    }

    public void clamp()
    {
        if (buffer != null && buffer.getAmount() > getCapacity())
        {
//            buffer.amount = this.capacity;
            buffer.setAmount(this.capacity);
        }
    }

    public void updateCapacity()
    {
        this.capacity = 0;

        for (IBufferTransmitter<FluidStack> transmitter : this.pipes)
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
        return getCapacity() - (buffer != null ? buffer.getAmount() : 0);
    }

    private int emitToAcceptors(FluidStack toSend, IFluidHandler.FluidAction action)
    {
        List<Pair<BlockPos, Map<Direction, IFluidHandler>>> available = new ArrayList<>();
        available.addAll(this.getAcceptors(toSend));

        Collections.shuffle(available);

        int totalSend = 0;

        if (!available.isEmpty())
        {
            int divider = available.size();
            int remainder = toSend.getAmount() % divider;
            int each = (toSend.getAmount() - remainder) / divider;

            for (Pair<BlockPos, Map<Direction, IFluidHandler>> pair : available)
            {
                int currentSend = each;
                Map<Direction, IFluidHandler> acceptors = pair.getRight();
                EnumSet<Direction> sides = acceptorDirections.get(pair.getLeft());

                if (remainder > 0)
                {
                    currentSend++;
                    remainder--;
                }

                for (Direction side : sides)
                {
                    int prev = totalSend;
                    IFluidHandler acceptor = acceptors.get(side);

                    if (acceptor != null)
                    {
                        FluidStack copy = toSend.copy();
//                        copy.amount = currentSend;
                        copy.setAmount(currentSend);
                        totalSend += acceptor.fill(copy, action);
                    }

                    if (totalSend > prev)
                    {
                        // If fluid was sent to this handler, continue to next
                        break;
                    }
                }
            }
        }

        if (action.execute() && totalSend > 0 && GCCoreUtil.getEffectiveSide().isServer())
        {
            this.didTransfer = true;
            this.transferDelay = 2;
        }

        return totalSend;
    }

    public int emitToBuffer(FluidStack toSend, IFluidHandler.FluidAction action)
    {
        if (toSend == null || (buffer != null && buffer.getFluid() != toSend.getFluid()))
        {
            return 0;
        }

        int toUse = Math.min(getRequest(), toSend.getAmount());

        if (action.execute())
        {
            if (buffer == null)
            {
                // Copy
                buffer = toSend.copy();
                buffer.setAmount(toUse);
            }
            else
            {
                // Add
                buffer.setAmount(buffer.getAmount() + toUse);
            }
        }

        return toUse;
    }

    public void tickEnd()
    {
        this.onUpdate();
    }

    public void addUpdate(ServerPlayerEntity player)
    {
        this.updateQueue.add(new DelayQueue(player));
    }

    private IPacket getAddTransmitterUpdate()
    {
        BlockPos pos = ((TileEntity) this.pipes.iterator().next()).getPos();
        return PacketFluidNetworkUpdate.getAddTransmitterUpdate(GCCoreUtil.getDimensionType(this.world), pos, this.firstUpdate, this.pipesAdded);
    }

    public void onUpdate()
    {
        if (GCCoreUtil.getEffectiveSide().isServer())
        {
            Iterator<DelayQueue> iterator = this.updateQueue.iterator();

            try
            {
                while (iterator.hasNext())
                {
                    DelayQueue queue = iterator.next();

                    if (queue.delay > 0)
                    {
                        queue.delay--;
                    }
                    else
                    {
                        this.pipesAdded.addAll(this.pipes);
                        GalacticraftCore.packetPipeline.sendTo(this.getAddTransmitterUpdate(), queue.player);
                        this.pipesAdded.clear();
                        iterator.remove();
                    }
                }
            }
            catch (Exception e)
            {

            }

            if (this.updateDelay > 0)
            {
                this.updateDelay--;

                if (this.updateDelay == 0)
                {
                    BlockPos pos = ((TileEntity) this.pipes.iterator().next()).getPos();
                    GalacticraftCore.packetPipeline.sendToAllAround(this.getAddTransmitterUpdate(), new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 30.0, GCCoreUtil.getDimensionType(this.world)));
                    this.firstUpdate = false;
                    this.pipesAdded.clear();
                    this.needsUpdate = true;
                }
            }

            this.prevTransferAmount = 0;

            if (this.transferDelay == 0)
            {
                this.didTransfer = false;
            }
            else
            {
                this.transferDelay--;
            }

            int stored = buffer != null ? buffer.getAmount() : 0;

            if (stored != this.prevBufferAmount)
            {
                this.needsUpdate = true;
            }

            this.prevBufferAmount = stored;

            if (this.didTransfer != this.prevTransfer || this.needsUpdate)
            {
                BlockPos pos = ((TileEntity) this.pipes.iterator().next()).getPos();
                GalacticraftCore.packetPipeline.sendToAllAround(PacketFluidNetworkUpdate.getFluidUpdate(GCCoreUtil.getDimensionType(this.world), pos, this.buffer, this.didTransfer), new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 20.0, GCCoreUtil.getDimensionType(this.world)));
                this.needsUpdate = false;
            }

            this.prevTransfer = this.didTransfer;

            if (buffer != null)
            {
                this.prevTransferAmount = this.emitToAcceptors(buffer, IFluidHandler.FluidAction.EXECUTE);
                if (buffer != null)
                {
//                    buffer.amount -= this.prevTransferAmount;
                    buffer.setAmount(buffer.getAmount() - this.prevTransferAmount);

                    if (buffer.getAmount() <= 0)
                    {
                        this.buffer = null;
                    }
                }
            }
        }
    }

    public void clientTick()
    {
        this.fluidScale = Math.max(this.fluidScale, this.getScale());

        if (this.didTransfer && this.fluidScale < 1.0F)
        {
            this.fluidScale = Math.max(this.getScale(), Math.min(1, fluidScale + 0.02F));
        }
        else if (!this.didTransfer && fluidScale > 0.0F)
        {
            this.fluidScale = this.getScale();

            if (this.fluidScale == 0.0F)
            {
                this.buffer = null;
            }
        }
    }

    public float getScale()
    {
        if (this.buffer == null || this.getCapacity() == 0)
        {
            return 0.0F;
        }

        return Math.min(1.0F, this.buffer.getAmount() / (float) this.getCapacity());
    }

    public List<Pair<BlockPos, Map<Direction, IFluidHandler>>> getAcceptors(FluidStack toSend)
    {
        List<Pair<BlockPos, Map<Direction, IFluidHandler>>> toReturn = new LinkedList<>();

        if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
        {
            return toReturn;
        }

        if (this.acceptors == null || this.acceptors.isEmpty())
        {
            this.refreshAcceptors();
        }

        List<BlockPos> acceptorsCopy = new ArrayList<>();
        acceptorsCopy.addAll(acceptors.keySet());

        for (BlockPos coords : acceptorsCopy)
        {
            EnumSet<Direction> sides = acceptorDirections.get(coords);
            if (sides == null || sides.isEmpty())
            {
                continue;
            }

            TileEntity tile = this.world.getTileEntity(coords);

            if (tile == null)
            {
                continue;
            }

            Map<Direction, IFluidHandler> handlers = Maps.newHashMap();

            LazyOptional<IFluidHandler> handler;
            Fluid fluidToSend = toSend.getFluid();
            for (Direction side : sides)
            {
                handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
                if (handler.isPresent() && handler.orElse(null).fill(new FluidStack(fluidToSend, 1), IFluidHandler.FluidAction.SIMULATE) > 0)
                {
                    handlers.put(side, handler.orElse(null));
                }
            }

            toReturn.add(Pair.of(coords, handlers));
        }

        return toReturn;
    }

    @Override
    public void refresh()
    {
        if (this.acceptors != null)
        {
            this.acceptors.clear();
        }

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

                if (tileTransmitter.isRemoved() || tileTransmitter.getWorld() == null)
                {
                    it.remove();
                    continue;
                }
                else
                {
                    if (this.world == null)
                    {
                        this.world = tileTransmitter.getWorld();
                    }

                    transmitter.setNetwork(this);
                }
            }

            updateCapacity();
            clamp();
        }
        catch (Exception e)
        {
            GCLog.severe("Failed to refresh liquid pipe network.");
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

                if (transmitter == null || tile.isRemoved() || tile.getWorld() == null)
                {
                    it.remove();
                    continue;
                }

                if (!transmitter.canTransmit())
                {
                    continue;
                }

                int i = 0;
                for (TileEntity acceptor : transmitter.getAdjacentConnections())
                {
                    if (!(acceptor instanceof IBufferTransmitter) && acceptor != null)
                    {
                        Direction facing = Direction.byIndex(i).getOpposite();
                        LazyOptional<IFluidHandler> handler = acceptor.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
                        if (handler.isPresent())
                        {
                            BlockPos acceptorPos = tile.getPos().offset(facing.getOpposite());
                            EnumSet<Direction> facingSet = this.acceptorDirections.get(acceptorPos);
                            if (facingSet != null)
                            {
                                facingSet.add(facing);
                            }
                            else
                            {
                                facingSet = EnumSet.of(facing);
                            }
                            this.acceptors.put(acceptorPos, handler.orElse(null));
                            this.acceptorDirections.put(acceptorPos, facingSet);
                        }
                    }
                    i++;
                }
            }
        }
        catch (Exception e)
        {
            GCLog.severe("Failed to refresh liquid acceptors");
            e.printStackTrace();
        }
    }

    @Override
    public ImmutableSet<IBufferTransmitter<FluidStack>> getTransmitters()
    {
        return ImmutableSet.copyOf(this.pipes);
    }

    @Override
    public FluidNetwork merge(FluidNetwork network)
    {
        if (network != null && network != this)
        {
            FluidNetwork newNetwork = new FluidNetwork(Lists.newArrayList(this, network));
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
                                FluidNetwork newNetwork = new FluidNetwork();

                                for (BlockVec3 node : finder.closedSet)
                                {
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).getWorld());

                                    if (nodeTile instanceof IBufferTransmitter)
                                    {
                                        if (nodeTile != splitPoint)
                                        {
                                            newNetwork.pipes.add((IBufferTransmitter<FluidStack>) nodeTile);
                                            newNetwork.pipesAdded.add((IBufferTransmitter<FluidStack>) nodeTile);
                                            newNetwork.onTransmitterAdded((IBufferTransmitter<FluidStack>) nodeTile);
                                            this.pipes.remove(nodeTile);
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
            else
            {
                this.updateCapacity();
            }
        }
    }

    @Override
    public String toString()
    {
        return "FluidNetwork[" + this.hashCode() + "|Pipes:" + this.pipes.size() + "|Acceptors:" + (this.acceptors == null ? 0 : this.acceptors.size()) + "]";
    }

    public static class DelayQueue
    {
        public ServerPlayerEntity player;
        public int delay;

        public DelayQueue(ServerPlayerEntity player)
        {
            this.player = player;
            this.delay = 5;
        }

        @Override
        public int hashCode()
        {
            return this.player.hashCode();
        }

        @Override
        public boolean equals(Object obj)
        {
            return obj instanceof DelayQueue && ((DelayQueue) obj).player.equals(this.player);
        }
    }
}
