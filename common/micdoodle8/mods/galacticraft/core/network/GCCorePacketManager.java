package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class GCCorePacketManager extends PacketManager implements IPacketHandler, IPacketReceiver
{
    public enum GCCorePacketType
    {
        UNSPECIFIED, TILEENTITY, ENTITY;

        public static GCCorePacketType get(int id)
        {
            if (id >= 0 && id < PacketType.values().length)
            {
                return GCCorePacketType.values()[id];
            }
            return UNSPECIFIED;
        }
    }

    public static Packet getPacket(String channelName, Entity sender, ArrayList<Object> objs)
    {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeInt(2);

            data.writeInt(sender.entityId);
            data = GCCorePacketManager.encodeDataStream(data, objs);

            final Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = channelName;
            packet.data = bytes.toByteArray();
            packet.length = packet.data.length;

            return packet;
        }
        catch (final IOException e)
        {
            System.out.println("Failed to create packet.");
            e.printStackTrace();
        }

        return null;
    }

    public static DataOutputStream encodeDataStream(DataOutputStream data, ArrayList<Object> sendData)
    {
        try
        {
            for (final Object dataValue : sendData)
            {
                if (dataValue instanceof Integer)
                {
                    data.writeInt((Integer) dataValue);
                }
                else if (dataValue instanceof Float)
                {
                    data.writeFloat((Float) dataValue);
                }
                else if (dataValue instanceof Double)
                {
                    data.writeDouble((Double) dataValue);
                }
                else if (dataValue instanceof Byte)
                {
                    data.writeByte((Byte) dataValue);
                }
                else if (dataValue instanceof Boolean)
                {
                    data.writeBoolean((Boolean) dataValue);
                }
                else if (dataValue instanceof String)
                {
                    data.writeUTF((String) dataValue);
                }
                else if (dataValue instanceof Short)
                {
                    data.writeShort((Short) dataValue);
                }
                else if (dataValue instanceof Long)
                {
                    data.writeLong((Long) dataValue);
                }
                else if (dataValue instanceof NBTTagCompound)
                {
                    PacketManager.writeNBTTagCompound((NBTTagCompound) dataValue, data);
                }
            }

            return data;
        }
        catch (final IOException e)
        {
            System.out.println("Packet data encoding failed.");
            e.printStackTrace();
        }

        return data;
    }

    public static Packet getPacket(String channelName, Entity sender, Object... sendData)
    {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);

        try
        {
            data.writeInt(2);

            data.writeInt(sender.entityId);
            data = GCCorePacketManager.encodeDataStream(data, sendData);

            final Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = channelName;
            packet.data = bytes.toByteArray();
            packet.length = packet.data.length;

            return packet;
        }
        catch (final IOException e)
        {
            System.out.println("Failed to create packet.");
            e.printStackTrace();
        }

        return null;
    }

    public static DataOutputStream encodeDataStream(DataOutputStream data, Object... sendData)
    {
        try
        {
            for (Object dataValue : sendData)
            {
                if (dataValue instanceof Integer)
                {
                    data.writeInt((Integer) dataValue);
                }
                else if (dataValue instanceof Float)
                {
                    data.writeFloat((Float) dataValue);
                }
                else if (dataValue instanceof Double)
                {
                    data.writeDouble((Double) dataValue);
                }
                else if (dataValue instanceof Byte)
                {
                    data.writeByte((Byte) dataValue);
                }
                else if (dataValue instanceof Boolean)
                {
                    data.writeBoolean((Boolean) dataValue);
                }
                else if (dataValue instanceof String)
                {
                    data.writeUTF((String) dataValue);
                }
                else if (dataValue instanceof Short)
                {
                    data.writeShort((Short) dataValue);
                }
                else if (dataValue instanceof Long)
                {
                    data.writeLong((Long) dataValue);
                }
                else if (dataValue instanceof NBTTagCompound)
                {
                    PacketManager.writeNBTTagCompound((NBTTagCompound) dataValue, data);
                }
                else if (dataValue instanceof ArrayList)
                {
                    for (Object dataValue2 : (ArrayList) dataValue)
                    {
                        if (dataValue instanceof Integer)
                        {
                            data.writeInt((Integer) dataValue);
                        }
                        else if (dataValue instanceof Float)
                        {
                            data.writeFloat((Float) dataValue);
                        }
                        else if (dataValue instanceof Double)
                        {
                            data.writeDouble((Double) dataValue);
                        }
                        else if (dataValue instanceof Byte)
                        {
                            data.writeByte((Byte) dataValue);
                        }
                        else if (dataValue instanceof Boolean)
                        {
                            data.writeBoolean((Boolean) dataValue);
                        }
                        else if (dataValue instanceof String)
                        {
                            data.writeUTF((String) dataValue);
                        }
                        else if (dataValue instanceof Short)
                        {
                            data.writeShort((Short) dataValue);
                        }
                        else if (dataValue instanceof Long)
                        {
                            data.writeLong((Long) dataValue);
                        }
                        else if (dataValue instanceof NBTTagCompound)
                        {
                            PacketManager.writeNBTTagCompound((NBTTagCompound) dataValue, data);
                        }
                    }
                }
            }

            return data;
        }
        catch (IOException e)
        {
            System.out.println("Packet data encoding failed.");
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player)
    {
        if (packet == null)
        {
            FMLLog.severe("Packet received as null!");
            return;
        }
        
        if (packet.data == null)
        {
            FMLLog.severe("Packet data received as null! ID " + packet.getPacketId());
            return;
        }
        
        try
        {
            final ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);

            final int packetTypeID = data.readInt();

            if (packetTypeID == 2)
            {
                final double id = data.readInt();

                final World world = ((EntityPlayer) player).worldObj;

                if (world != null)
                {
                    for (final Object o : world.loadedEntityList)
                    {
                        if (o instanceof Entity)
                        {
                            final Entity e = (Entity) o;

                            if (id == e.entityId && e instanceof IPacketReceiver)
                            {
                                ((IPacketReceiver) e).handlePacketData(network, packetTypeID, packet, (EntityPlayer) player, data);
                            }
                        }
                    }
                }
            }
            else if (packetTypeID == 1)
            {
                final int x = data.readInt();
                final int y = data.readInt();
                final int z = data.readInt();

                final World world = ((EntityPlayer) player).worldObj;

                if (world != null)
                {
                    final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

                    if (tileEntity != null)
                    {
                        if (tileEntity instanceof IPacketReceiver)
                        {
                            ((IPacketReceiver) tileEntity).handlePacketData(network, packetTypeID, packet, (EntityPlayer) player, data);
                        }
                    }
                }
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {

    }
}
