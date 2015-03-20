package micdoodle8.mods.galacticraft.core.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConnectionPacket
{
	public static final String CHANNEL = "galacticraft";
	public static FMLEventChannel bus;

	public void handle(ByteBuf payload, EntityPlayer player)
	{
		int packetId = payload.readByte();
		List<Object> data = new ArrayList<Object>();
		switch (packetId)
		{
		case 101:
	  	    int length = payload.readInt();
	  	    for (int i = 0; i < length; i++)
	  	    {
	  	    	data.add(payload.readInt());
	  	    }
	        WorldUtil.decodePlanetsListClient(data);
	        break;
		case 102:
	  	    int llength = payload.readInt();
	  	    for (int i = 0; i < llength; i++)
	  	    {
	  	    	data.add(payload.readInt());
	  	    }
	        WorldUtil.decodeSpaceStationListClient(data);
	        break;
		case 103:
	        try
	        {
	        	data = NetworkUtil.decodeData(EnumSimplePacket.C_UPDATE_CONFIGS.getDecodeClasses(), payload);
	        	ConfigManagerCore.saveClientConfigOverrideable();
	        	ConfigManagerCore.setConfigOverride(data);
	        	if (ConfigManagerCore.enableDebug) System.out.println("Server-set configs received OK on client.");
	        }
	        catch (Exception e)
	        {
	            System.err.println("[Galacticraft] Error handling connection packet - maybe the player's Galacticraft version does not match the server version?");
	            e.printStackTrace();
	        }
        	break;
	    default:
		}
        if (payload.readInt() != 3519)
        {
            GCLog.severe("Packet completion problem for connection packet " + packetId + " - maybe the player's Galacticraft version does not match the server version?");
        }
	}

	public static FMLProxyPacket createDimPacket(Integer[] dims)
	{
		ArrayList<Integer> data = new ArrayList();
		for (int i = 0; i < dims.length; i++)
			data.add(dims[i]);
		return createPacket((byte) 101, data);
	}

	public static FMLProxyPacket createSSPacket(Integer[] dims)
	{
		ArrayList<Integer> data = new ArrayList();
		for (int i = 0; i < dims.length; i++)
			data.add(dims[i]);
		return createPacket((byte) 102, data);
	}

	public static FMLProxyPacket createPacket(byte packetId, Collection<Integer> data)
	{
		ByteBuf payload = Unpooled.buffer();

		payload.writeByte(packetId);
		payload.writeInt(data.size());
		for (Integer i : data)
		{
			payload.writeInt(i.intValue());
		}
		payload.writeInt(3519); //signature
		return new FMLProxyPacket(payload, CHANNEL);
	}

	public static FMLProxyPacket createConfigPacket(List<Object> data)
	{
		ByteBuf payload = Unpooled.buffer();
		payload.writeByte(103);
        try
        {
    		NetworkUtil.encodeData(payload, data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
		payload.writeInt(3519); //signature
		return new FMLProxyPacket(payload, CHANNEL);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPacketData(FMLNetworkEvent.ClientCustomPacketEvent event)
	{
		FMLProxyPacket pkt = event.packet;

		onFMLProxyPacketData(event.manager, pkt, Minecraft.getMinecraft().thePlayer);
	}

	@SubscribeEvent
	public void onPacketData(FMLNetworkEvent.ServerCustomPacketEvent event)
	{
		FMLProxyPacket pkt = event.packet;

		onFMLProxyPacketData(event.manager, pkt, ((NetHandlerPlayServer)event.handler).playerEntity);
	}

	public void onFMLProxyPacketData(NetworkManager manager, FMLProxyPacket packet, EntityPlayer player)
	{
		try {
			if ((packet == null) || (packet.payload() == null)) throw new RuntimeException("Empty packet sent to Galacticraft channel");
			ByteBuf data = packet.payload();
			this.handle(data, player);
		} catch (Exception e) {
			GCLog.severe("GC login packet handler: Failed to read packet");
			GCLog.severe(e.toString());
			e.printStackTrace();
		}
	}
}
