package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collection;
import java.util.Iterator;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketSchematicList.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketSchematicList implements IGalacticraftAdvancedPacket
{
	public static byte packetID = 21;

	public static Packet buildSchematicListPacket(Collection<ISchematicPage> col)
	{
		final Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;

		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(GCCorePacketSchematicList.packetID);
			data.writeInt(col.size());
			final Iterator<ISchematicPage> var3 = col.iterator();

			while (var3.hasNext())
			{
				final ISchematicPage var4 = var3.next();
				data.writeInt(var4 != null ? var4.getPageID() : -2);
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
		return GCCorePacketSchematicList.packetID;
	}
}
