package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityControllable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

public class GCCorePacketEntityUpdate implements IGalacticraftAdvancedPacket
{
	public static final byte packetID = 14;
	
	public static Packet buildUpdatePacket(GCCoreEntityControllable driveable)
	{
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;
		
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        
        try
        {
        	data.write(packetID);
        	data.writeInt(driveable.entityId);
        	data.writeDouble(driveable.posX);
        	data.writeDouble(driveable.posY);
        	data.writeDouble(driveable.posZ);
        	data.writeFloat(driveable.rotationYaw);
        	data.writeFloat(driveable.rotationPitch);
        	data.writeDouble(driveable.motionX);
        	data.writeDouble(driveable.motionY);
        	data.writeDouble(driveable.motionZ);
        	
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
		try
		{
			EntityPlayer player =  (EntityPlayer)extradata[0];
			
			int entityId = stream.readInt();
			GCCoreEntityControllable driveable = null;
			
			for(Object obj : player.worldObj.loadedEntityList)
			{
				if(obj instanceof GCCoreEntityControllable && ((Entity)obj).entityId == entityId)
				{
					driveable = (GCCoreEntityControllable)obj;
					break;
				}
			}
			
			if(driveable != null)
			{
				driveable.setPositionRotationAndMotion(stream.readDouble(), stream.readDouble(), stream.readDouble(), stream.readFloat(), stream.readFloat(), stream.readDouble(), stream.readDouble(), stream.readDouble());
			}
		}
        catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	@Override
	public byte getPacketID()
	{
		return packetID;
	}
}
