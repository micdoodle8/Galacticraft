package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketDimensionListSpaceStations.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketDimensionListSpaceStations implements IGalacticraftAdvancedPacket
{
	public static byte packetID = 16;

	public static Packet buildDimensionListPacket(Collection<?> col)
	{
		final Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;

		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(GCCorePacketDimensionListSpaceStations.packetID);
			data.writeInt(col.size());
			final Iterator<?> var3 = col.iterator();

			while (var3.hasNext())
			{
				final Integer var4 = (Integer) var3.next();
				int dimID = var4.intValue();
				data.writeInt(dimID);
				int id = Arrays.binarySearch(GCCoreConfigManager.staticLoadDimensions, dimID);
				data.writeBoolean(id >= 0);
			}

			packet.data = bytes.toByteArray();
			packet.length = packet.data.length;

			data.close();
			bytes.close();
		}
		catch (final Exception e)
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
		return GCCorePacketDimensionListSpaceStations.packetID;
	}
}
