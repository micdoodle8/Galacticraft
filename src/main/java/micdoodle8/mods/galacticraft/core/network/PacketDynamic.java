package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * PacketDynamic is used for updating data for regularly updating Entities and TileEntities
 * Two types of PacketDynamic:
 * Type 0: the identifier is an Entity ID
 * Type 1: the identifier is a TileEntity's BlockPos
 */
public class PacketDynamic extends PacketBase
{
    private int type;
    private Object identifier;
    private ArrayList<Object> sendData;
    private ByteBuf payloadData;

    public PacketDynamic()
    {
        super();
    }

    public PacketDynamic(Entity entity)
    {
        super(GCCoreUtil.getDimensionType(entity.world));
        assert entity instanceof IPacketReceiver : "Entity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 0;
        this.identifier = entity.getEntityId();
        this.sendData = new ArrayList<>();
        ((IPacketReceiver) entity).getNetworkedData(this.sendData);
    }

    public PacketDynamic(TileEntity tile)
    {
        super(GCCoreUtil.getDimensionType(tile.getWorld()));
        assert tile instanceof IPacketReceiver : "TileEntity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 1;
        this.identifier = tile.getPos();
        this.sendData = new ArrayList<>();
        ((IPacketReceiver) tile).getNetworkedData(this.sendData);
    }

    public static void encode(final PacketDynamic message, final PacketBuffer buf)
    {
        message.encodeInto(buf);
    }

    public static PacketDynamic decode(PacketBuffer buf)
    {
        PacketDynamic packet = new PacketDynamic();
        packet.decodeInto(buf);
        return packet;
    }

    public static void handle(final PacketDynamic message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
            {
                message.handleClientSide(Minecraft.getInstance().player);
            }
            else
            {
                message.handleServerSide(ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public boolean isEmpty()
    {
        return sendData.isEmpty();
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.type);

        switch (this.type)
        {
        case 0:
            buffer.writeInt((Integer) this.identifier);
            break;
        case 1:
            BlockPos bp = (BlockPos) this.identifier;
            buffer.writeInt(bp.getX());
            buffer.writeInt(bp.getY());
            buffer.writeInt(bp.getZ());
            break;
        }

        ByteBuf payloadData = Unpooled.buffer();

        try
        {
            NetworkUtil.encodeData(payloadData, this.sendData);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        int readableBytes = payloadData.readableBytes();
        buffer.writeInt(readableBytes);
        buffer.writeBytes(payloadData);
    }

    @Override
    public void decodeInto(ByteBuf buffer) throws IndexOutOfBoundsException
    {
        super.decodeInto(buffer);
        this.type = buffer.readInt();
//        World world = GalacticraftCore.proxy.getWorldForID(this.getDimensionID());
//
//        if (world == null)
//        {
//            FMLLog.severe("Failed to get world for dimension ID: " + this.getDimensionID());
//        }
//
        switch (this.type)
        {
        case 0:
            this.identifier = new Integer(buffer.readInt());

            int length = buffer.readInt();
            payloadData = Unpooled.copiedBuffer(buffer.readBytes(length));
//                if (entity instanceof IPacketReceiver && buffer.readableBytes() > 0)
            break;
        case 1:
            this.identifier = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());

            length = buffer.readInt();
            payloadData = Unpooled.copiedBuffer(buffer.readBytes(length));

            break;
        }
    }

    @Override
    public void handleClientSide(PlayerEntity player)
    {
        this.handleData(LogicalSide.CLIENT, player);
    }

    @Override
    public void handleServerSide(PlayerEntity player)
    {
        this.handleData(LogicalSide.SERVER, player);
    }

    private void handleData(LogicalSide LogicalSide, PlayerEntity player)
    {
        switch (this.type)
        {
        case 0:
            Entity entity = player.world.getEntityByID((Integer) this.identifier);

            if (entity instanceof IPacketReceiver)
            {
                if (this.payloadData.readableBytes() > 0)
                {
                    ((IPacketReceiver) entity).decodePacketdata(payloadData);
                }

                //Treat any packet received by a server from a client as an update request specifically to that client
                if (LogicalSide == net.minecraftforge.fml.LogicalSide.SERVER && player instanceof ServerPlayerEntity && entity != null)
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketDynamic(entity), (ServerPlayerEntity) player);
                }
            }
            break;

        case 1:
            BlockPos bp = (BlockPos) this.identifier;
            if (player.world.isBlockLoaded(bp))
            {
                TileEntity tile = player.world.getTileEntity(bp);

                if (tile instanceof IPacketReceiver)
                {
                    if (this.payloadData.readableBytes() > 0)
                    {
                        ((IPacketReceiver) tile).decodePacketdata(payloadData);
                    }

                    //Treat any packet received by a server from a client as an update request specifically to that client
                    if (LogicalSide == net.minecraftforge.fml.LogicalSide.SERVER && player instanceof ServerPlayerEntity && tile != null)
                    {
                        GalacticraftCore.packetPipeline.sendTo(new PacketDynamic(tile), (ServerPlayerEntity) player);
                    }
                }
            }
            break;
        }
    }
}
