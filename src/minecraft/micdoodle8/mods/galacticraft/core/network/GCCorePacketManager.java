package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class GCCorePacketManager extends PacketManager implements IPacketHandler, IPacketReceiver
{
	public enum GCCorePacketType
	{
		UNSPECIFIED, ENTITY;

		public static GCCorePacketType get(int id)
		{
			if (id >= 0 && id < PacketType.values().length)
			{
				return GCCorePacketType.values()[id];
			}
			return UNSPECIFIED;
		}
	}
	
	public static Packet getPacket(String channelName, Entity sender, Object... sendData)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(GCCorePacketType.ENTITY.ordinal());

			data.writeInt(sender.entityId);
			data = encodeDataStream(data, sendData);

			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = channelName;
			packet.data = bytes.toByteArray();
			packet.length = packet.data.length;

			return packet;
		}
		catch (IOException e)
		{
			System.out.println("Failed to create packet.");
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player)
	{
		try
		{
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);

			int packetTypeID = data.readInt();

			GCCorePacketType packetType = GCCorePacketType.get(packetTypeID);

			if (packetType == GCCorePacketType.ENTITY)
			{
				double id = data.readInt();

				World world = ((EntityPlayer) player).worldObj;

				if (world != null)
				{
					for (Object o : world.loadedEntityList)
					{
						if (o instanceof Entity)
						{
							Entity e = (Entity) o;
							
							if (id == e.entityId && e instanceof IPacketReceiver)
							{
								((IPacketReceiver) e).handlePacketData(network, packetTypeID, packet, ((EntityPlayer) player), data);
							}
						}
					}
				}
			}
			else
			{
				super.onPacketData(network, packet, player);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		
	}
}
