package micdoodle8.mods.galacticraft.core.network;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.transmission.tile.IBufferTransmitter;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.Collection;

public class PacketFluidNetworkUpdate extends PacketBase
{
    private PacketType type;
    private BlockPos pos;

    private FluidStack stack;
    private Fluid fluidType;
    private boolean didTransfer;

    private boolean newNetwork;
    private Collection<IBufferTransmitter<FluidStack>> transmittersAdded;
    private Collection<BlockPos> transmittersCoords;

    public PacketFluidNetworkUpdate()
    {
    }

    public static PacketFluidNetworkUpdate getFluidUpdate(int dimensionID, BlockPos pos, FluidStack stack, boolean didTransfer)
    {
        return new PacketFluidNetworkUpdate(PacketType.FLUID, dimensionID, pos, stack, didTransfer);
    }

    public static PacketFluidNetworkUpdate getAddTransmitterUpdate(int dimensionID, BlockPos pos, boolean newNetwork, Collection<IBufferTransmitter<FluidStack>> transmittersAdded)
    {
        return new PacketFluidNetworkUpdate(PacketType.ADD_TRANSMITTER, dimensionID, pos, newNetwork, transmittersAdded);
    }

    private PacketFluidNetworkUpdate(PacketType type, int dimensionID, BlockPos pos, FluidStack stack, boolean didTransfer)
    {
        super(dimensionID);
        this.type = type;
        this.pos = pos;
        this.stack = stack;
        this.didTransfer = didTransfer;
    }

    private PacketFluidNetworkUpdate(PacketType type, int dimensionID, BlockPos pos, boolean newNetwork, Collection<IBufferTransmitter<FluidStack>> transmittersAdded)
    {
        super(dimensionID);
        this.type = type;
        this.pos = pos;
        this.newNetwork = newNetwork;
        this.transmittersAdded = transmittersAdded;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.pos.getX());
        buffer.writeInt(this.pos.getY());
        buffer.writeInt(this.pos.getZ());
        buffer.writeInt(this.type.ordinal());

        switch (this.type)
        {
        case ADD_TRANSMITTER:
            buffer.writeBoolean(this.newNetwork);
            buffer.writeInt(this.transmittersAdded.size());

            for (IBufferTransmitter transmitter : this.transmittersAdded)
            {
                TileEntity tile = (TileEntity) transmitter;
                buffer.writeInt(tile.getPos().getX());
                buffer.writeInt(tile.getPos().getY());
                buffer.writeInt(tile.getPos().getZ());
            }
            break;
        case FLUID:
            if (this.stack != null)
            {
                buffer.writeBoolean(true);
                ByteBufUtils.writeUTF8String(buffer, FluidRegistry.getFluidName(this.stack));
                buffer.writeInt(this.stack.amount);
            }
            else
            {
                buffer.writeBoolean(false);
            }

            buffer.writeBoolean(this.didTransfer);
            break;
        }
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        super.decodeInto(buffer);
        this.pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        this.type = PacketType.values()[buffer.readInt()];

        switch (this.type)
        {
        case ADD_TRANSMITTER:
            this.newNetwork = buffer.readBoolean();
            this.transmittersCoords = Sets.newHashSet();
            int transmitterCount = buffer.readInt();

            for (int i = 0; i < transmitterCount; ++i)
            {
                this.transmittersCoords.add(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
            }
            break;
        case FLUID:
            if (buffer.readBoolean())
            {
                this.fluidType = FluidRegistry.getFluid(ByteBufUtils.readUTF8String(buffer));
                if (this.fluidType != null)
                {
                    this.stack = new FluidStack(this.fluidType, buffer.readInt());
                }
            }
            else
            {
                this.fluidType = null;
                this.stack = null;
            }

            this.didTransfer = buffer.readBoolean();
            break;
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        TileEntity tile = player.world.getTileEntity(this.pos);

        if (tile instanceof IBufferTransmitter)
        {
            IBufferTransmitter<FluidStack> transmitter = (IBufferTransmitter<FluidStack>) tile;

            switch (this.type)
            {
            case ADD_TRANSMITTER:
            {
                FluidNetwork network = transmitter.hasNetwork() && !this.newNetwork ? (FluidNetwork) transmitter.getNetwork() : new FluidNetwork();
                network.register();
                transmitter.setNetwork(network);

                for (BlockPos pos : this.transmittersCoords)
                {
                    TileEntity transmitterTile = player.world.getTileEntity(pos);

                    if (transmitterTile instanceof IBufferTransmitter)
                    {
                        ((IBufferTransmitter) transmitterTile).setNetwork(network);
                    }
                }

                network.updateCapacity();
            }
            break;
            case FLUID:
                if (transmitter.getNetwork() != null)
                {
                    FluidNetwork network = (FluidNetwork) transmitter.getNetwork();

                    if (this.fluidType != null)
                    {
                        network.refFluid = this.fluidType;
                    }

                    network.buffer = this.stack;
                    network.didTransfer = this.didTransfer;
                }
                break;
            }
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {

    }

    public enum PacketType
    {
        ADD_TRANSMITTER,
        FLUID,
    }
}
