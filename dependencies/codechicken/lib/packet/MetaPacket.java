package codechicken.lib.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import codechicken.lib.asm.ObfMapping;

import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * A class that facilitates sending of multiple packets one after the other.
 * It first sends an empty 250 packet and then it's payloads.
 */
public class MetaPacket extends Packet250CustomPayload
{
    static
    {
        try
        {
            String fieldName = new ObfMapping("net/minecraft/network/packet/Packet", "packetClassToIdMap", "Ljava/util/Map;").toRuntime().s_name;
            Field field = Packet.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Map<Class<? extends Packet>, Integer> packetClassToIdMap = (Map<Class<? extends Packet>, Integer>) field.get(null);
            packetClassToIdMap.put(MetaPacket.class, 250);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public ArrayList<Packet> packets = new ArrayList<Packet>();
    
    public MetaPacket(Packet... packets)
    {
        super("", null);
        
        for(Packet p : packets)
            this.packets.add(p);
    }
    
    public MetaPacket(Collection<? extends Packet> packets)
    {
        this.packets.addAll(packets);
    }
    
    @Override
    public void readPacketData(DataInput datain)
    {
        throw new IllegalStateException("Meta packets can't be read");
    }

    @Override
    public void writePacketData(DataOutput dataout) throws IOException
    {
        //send a dummy 250
        super.writePacketData(dataout);
        
        for(Packet p : packets)
            Packet.writePacket(p, dataout);
        
        //adjust sent size for Packet.writePacket
        Packet.sentSize-=getPacketSize()-super.getPacketSize();
    }

    @Override
    public void processPacket(NetHandler nethandler)
    {
        for(Packet p : packets)//Memory connection
            p.processPacket(nethandler);
    }

    @Override
    public int getPacketSize()
    {
        int size = 0;
        for(Packet p : packets)
            size+=p.getPacketSize()+1;
        return size;
    }
}
