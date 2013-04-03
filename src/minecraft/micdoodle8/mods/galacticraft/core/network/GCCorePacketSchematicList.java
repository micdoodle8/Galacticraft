package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collection;
import java.util.Iterator;

import micdoodle8.mods.galacticraft.API.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

public class GCCorePacketSchematicList implements IGalacticraftAdvancedPacket
{
	public static byte packetID = 21;
	
	public static Packet buildSchematicListPacket(Collection col)
	{
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;
		
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        
        try
        {
        	data.writeByte(GCCorePacketSchematicList.packetID);
        	data.writeInt(col.size());
            Iterator var3 = col.iterator();

            while (var3.hasNext())
            {
            	ISchematicPage var4 = (ISchematicPage)var3.next();
                data.writeInt(var4.getPageID());
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
		return GCCorePacketSchematicList.packetID;
	}
}
