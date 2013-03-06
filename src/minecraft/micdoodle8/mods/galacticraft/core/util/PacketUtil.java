package micdoodle8.mods.galacticraft.core.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerBase;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketUtil 
{
	public static Packet250CustomPayload createRealObjPacket(String channel, int packetID, Object[] input)
    {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream data = null;
        
		try
		{
			data = new ObjectOutputStream(bytes);
		}
		catch (final IOException e1)
		{
			e1.printStackTrace();
		}
		
        try
        {
        	if (data != null)
        	{
                data.write(packetID);

                if (input != null)
                {
                    for (final Object obj : input)
                    {
                    	PacketUtil.writeObjectToStream(obj, data);
                    }
                }
        	}
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        final Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channel;
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;

        return packet;
    }
	
	public static Packet250CustomPayload createPacket(String channel, int packetID, Object[] input)
    {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream(bytes);
        try
        {
            data.write(packetID);

            if (input != null)
            {
                for (final Object obj : input)
                {
                	PacketUtil.writeObjectToStream(obj, data);
                }
            }

        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        final Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channel;
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;

        return packet;
    }

    public static Object[] readPacketData(DataInputStream data, Class[] packetDataTypes)
    {
        final List result = new ArrayList<Object>();

        try
        {
            for (final Class curClass : packetDataTypes)
            {
                result.add(PacketUtil.readObjectFromStream(data, curClass));
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result.toArray();
    }

    public static Object[] readRealObjPacketData(ObjectInputStream data, Class[] packetDataTypes)
    {
        final List result = new ArrayList<Object>();

        try
        {
            for (final Class curClass : packetDataTypes)
            {
                result.add(PacketUtil.readObjectFromStream(data, curClass));
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result.toArray();
    }
    
    private static void writeObjectToStream(Object obj, DataOutputStream data) throws IOException
    {
        final Class objClass = obj.getClass();

        if (objClass.equals(Boolean.class))
        {
            data.writeBoolean((Boolean) obj);
        }
        else if (objClass.equals(Byte.class))
        {
            data.writeByte((Byte) obj);
        }
        else if (objClass.equals(Integer.class))
        {
            data.writeInt((Integer) obj);
        }
        else if (objClass.equals(String.class))
        {
            data.writeUTF((String) obj);
        }
        else if (objClass.equals(Double.class))
        {
            data.writeDouble((Double) obj);
        }
        else if (objClass.equals(Float.class))
        {
            data.writeFloat((Float) obj);
        }
        else if (objClass.equals(Long.class))
        {
            data.writeLong((Long) obj);
        }
        else if (objClass.equals(Short.class))
        {
            data.writeShort((Short) obj);
        }
    }

    private static Object readObjectFromStream(DataInputStream data, Class curClass) throws IOException
    {
        if (curClass.equals(Boolean.class))
        {
            return data.readBoolean();
        }
        else if (curClass.equals(Byte.class))
        {
            return data.readByte();
        }
        else if (curClass.equals(Integer.class))
        {
            return data.readInt();
        }
        else if (curClass.equals(String.class))
        {
            return data.readUTF();
        }
        else if (curClass.equals(Double.class))
        {
            return data.readDouble();
        }
        else if (curClass.equals(Float.class))
        {
            return data.readFloat();
        }
        else if (curClass.equals(Long.class))
        {
            return data.readLong();
        }
        else if (curClass.equals(Short.class))
        {
            return data.readShort();
        }

        return null;
    }
    
    private static void writeObjectToStream(Object obj, ObjectOutputStream data) throws IOException
    {
        final Class objClass = obj.getClass();

        if (objClass.equals(GCCorePlayerBase.class))
        {
            data.writeObject(obj);
        }
    }

    private static Object readObjectFromStream(ObjectInputStream data, Class curClass) throws IOException
    {
    	if (curClass.equals(GCCorePlayerBase.class))
        {
        	try
        	{
				return data.readObject();
			}
        	catch (final ClassNotFoundException e)
        	{
				e.printStackTrace();
			}
        }

        return null;
    }

    public static int readPacketID(ObjectInputStream data)
    {
        int result = -1;

        try
        {
            result = data.read();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static int readPacketID(DataInputStream data)
    {
        int result = -1;

        try
        {
            result = data.read();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
