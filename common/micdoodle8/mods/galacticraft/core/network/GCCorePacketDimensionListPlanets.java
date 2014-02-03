package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collection;
import java.util.Iterator;

import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketDimensionListPlanets.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketDimensionListPlanets implements IGalacticraftAdvancedPacket
{
	public static byte packetID = 19;

	public static Packet buildDimensionListPacket(Collection<?> col)
	{
		final Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;

		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(GCCorePacketDimensionListPlanets.packetID);
			data.writeInt(col.size());
			final Iterator<?> var3 = col.iterator();

			while (var3.hasNext())
			{
				final Integer var4 = (Integer) var3.next();
				GCLog.info("Registering remote planet dimension: " + var4);
				data.writeInt(var4.intValue());
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
		return GCCorePacketDimensionListPlanets.packetID;
	}
}
