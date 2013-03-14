package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityControllable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

public class GCCorePacketControllableEntity implements IGalacticraftAdvancedPacket
{
	public static final byte packetID = 12;
	
	public static Packet buildKeyPacket(int key)
	{
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;
		
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        
        try
        {
        	data.write(packetID);
        	data.writeInt(key);
        	
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
	public byte getPacketID()
	{
		return packetID;
	}

	@Override
	public void handlePacket(DataInputStream stream, Object[] extraData, Side side) 
	{
		try
		{
			EntityPlayer player =  (EntityPlayer) extraData[0];
			
			int key = stream.readInt();
			if(player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityControllable)
			{
				((GCCoreEntityControllable)player.ridingEntity).pressKey(key);
			}
			
		}
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
}
