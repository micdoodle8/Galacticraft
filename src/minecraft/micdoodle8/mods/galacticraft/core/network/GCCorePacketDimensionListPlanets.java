package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collection;
import java.util.Iterator;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

public class GCCorePacketDimensionListPlanets implements IGalacticraftAdvancedPacket
{
	public static byte packetID = 19;
	
	public static Packet buildDimensionListPacket(Collection col)
	{
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;
		
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        
        try
        {
        	data.writeByte(GCCorePacketDimensionListPlanets.packetID);
        	data.writeInt(col.size());
            Iterator var3 = col.iterator();

            while (var3.hasNext())
            {
                Integer var4 = (Integer)var3.next();
                data.writeInt(var4.intValue());
            }
        	
        	packet.data = bytes.toByteArray();
        	packet.length = packet.data.length;
        	
        	data.close();
        	bytes.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return packet;
	}
	
	@Override
	public void handlePacket(DataInputStream stream, Object[] extradata, Side side)
	{
	}
	
	@Override
	public byte getPacketID()
	{
		return GCCorePacketDimensionListPlanets.packetID;
	}
}
