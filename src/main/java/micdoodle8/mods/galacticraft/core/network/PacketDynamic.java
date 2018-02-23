package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;
import java.util.ArrayList;

public class PacketDynamic extends PacketBase
{
    private int type;
    private Object[] data;
    private ArrayList<Object> sendData;
    private ByteBuf payloadData;

    public PacketDynamic()
    {
        super();
    }

    public PacketDynamic(Entity entity)
    {
        super(GCCoreUtil.getDimensionID(entity.worldObj));
        assert entity instanceof IPacketReceiver : "Entity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 0;
        this.data = new Object[] { entity.getEntityId() };
        this.sendData = new ArrayList<Object>();
        ((IPacketReceiver) entity).getNetworkedData(this.sendData);
    }

    public PacketDynamic(TileEntity tile)
    {
        super(GCCoreUtil.getDimensionID(tile.getWorld()));
        assert tile instanceof IPacketReceiver : "TileEntity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 1;
        this.data = new Object[] { tile.getPos() };
        this.sendData = new ArrayList<Object>();
        ((IPacketReceiver) tile).getNetworkedData(this.sendData);
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
            buffer.writeInt((Integer) this.data[0]);
            break;
        case 1:
            buffer.writeInt(((BlockPos) this.data[0]).getX());
            buffer.writeInt(((BlockPos) this.data[0]).getY());
            buffer.writeInt(((BlockPos) this.data[0]).getZ());
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
    public void decodeInto(ByteBuf buffer)
    {
        super.decodeInto(buffer);
        this.type = buffer.readInt();

        World world = GalacticraftCore.proxy.getWorldForID(this.getDimensionID());

        if (world == null)
        {
            FMLLog.severe("Failed to get world for dimension ID: " + this.getDimensionID());
        }

        switch (this.type)
        {
        case 0:
            this.data = new Object[1];
            this.data[0] = buffer.readInt();

            int length = buffer.readInt();
            payloadData = Unpooled.copiedBuffer(buffer.readBytes(length));
//                if (entity instanceof IPacketReceiver && buffer.readableBytes() > 0)
            break;
        case 1:
            this.data = new Object[1];
            this.data[0] = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());

            length = buffer.readInt();
            payloadData = Unpooled.copiedBuffer(buffer.readBytes(length));

            break;
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        this.handleData(Side.CLIENT, player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        this.handleData(Side.SERVER, player);
    }

    private void handleData(Side side, EntityPlayer player)
    {
        switch (this.type)
        {
        case 0:
            Entity entity = player.worldObj.getEntityByID((Integer) this.data[0]);

            if (entity instanceof IPacketReceiver)
            {
                if (this.payloadData.readableBytes() > 0)
                {
                    ((IPacketReceiver) entity).decodePacketdata(payloadData);
                }

                //Treat any packet received by a server from a client as an update request specifically to that client
                if (side == Side.SERVER && player instanceof EntityPlayerMP && entity != null)
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketDynamic(entity), (EntityPlayerMP) player);
                }
            }
            break;

        case 1:
            if (player.worldObj.isBlockLoaded((BlockPos) this.data[0], false))
            {
                TileEntity tile = player.worldObj.getTileEntity((BlockPos) this.data[0]);

                if (tile instanceof IPacketReceiver)
                {
                    if (this.payloadData.readableBytes() > 0)
                    {
                        ((IPacketReceiver) tile).decodePacketdata(payloadData);
                    }

                    //Treat any packet received by a server from a client as an update request specifically to that client
                    if (side == Side.SERVER && player instanceof EntityPlayerMP && tile != null)
                    {
                        GalacticraftCore.packetPipeline.sendTo(new PacketDynamic(tile), (EntityPlayerMP) player);
                    }
                }
            }
            break;
        }
    }
}
