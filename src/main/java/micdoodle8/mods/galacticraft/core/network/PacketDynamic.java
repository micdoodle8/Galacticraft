package micdoodle8.mods.galacticraft.core.network;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;

public class PacketDynamic implements IPacket
{
    private int type;
    private int dimID;
    private Object[] data;
    private ArrayList<Object> sendData;

    public PacketDynamic()
    {

    }

    public PacketDynamic(Entity entity)
    {
        assert entity instanceof IPacketReceiver : "Entity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 0;
        this.dimID = entity.worldObj.provider.getDimension();
        this.data = new Object[] { entity.getEntityId() };
        this.sendData = new ArrayList<Object>();
        ((IPacketReceiver) entity).getNetworkedData(this.sendData);
    }

    public PacketDynamic(TileEntity tile)
    {
        assert tile instanceof IPacketReceiver : "TileEntity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 1;
        this.dimID = tile.getWorld().provider.getDimension();
        this.data = new Object[] { tile.getPos() };
        this.sendData = new ArrayList<Object>();
        ((IPacketReceiver) tile).getNetworkedData(this.sendData);
    }

    @Override
    public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
    {
        buffer.writeInt(this.type);
        buffer.writeInt(this.dimID);

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

        try
        {
            NetworkUtil.encodeData(buffer, this.sendData);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
    {
        this.type = buffer.readInt();
        this.dimID = buffer.readInt();

        World world = GalacticraftCore.proxy.getWorldForID(this.dimID);

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            world = MinecraftServer.getServer().worldServerForDimension(this.dimID);
        }

        if (world == null)
        {
            FMLLog.severe("Failed to get world for dimension ID: " + this.dimID);
        }

        switch (this.type)
        {
        case 0:
            this.data = new Object[1];
            this.data[0] = buffer.readInt();

            if (world != null)
            {
                Entity entity = world.getEntityByID((Integer) this.data[0]);

                if (entity instanceof IPacketReceiver)
                {
                    ((IPacketReceiver) entity).decodePacketdata(buffer);
                }
            }

            break;
        case 1:
            this.data = new Object[1];
            this.data[0] = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());

            if (world != null)
            {
                TileEntity tile = world.getTileEntity((BlockPos) this.data[0]);

                if (tile instanceof IPacketReceiver)
                {
                    ((IPacketReceiver) tile).decodePacketdata(buffer);
                }
            }

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
                ((IPacketReceiver) entity).handlePacketData(side, player);
            }

            break;
        case 1:
            TileEntity tile = player.worldObj.getTileEntity((BlockPos) this.data[0]);

            if (tile instanceof IPacketReceiver)
            {
                ((IPacketReceiver) tile).handlePacketData(side, player);
            }

            break;
        }
    }
}
