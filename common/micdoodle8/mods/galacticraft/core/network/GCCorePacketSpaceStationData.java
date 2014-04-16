package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketSpaceStationData.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketSpaceStationData implements IGalacticraftAdvancedPacket
{
	public static final byte packetID = 17;

	public static Packet buildSpaceStationDataPacket(World var0, int var1, GCCorePlayerMP player)
	{
		final NBTTagCompound var2 = new NBTTagCompound();
		GCCoreSpaceStationData.getStationData(var0, var1, player).writeToNBT(var2);
		final Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;

		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(GCCorePacketSpaceStationData.packetID);
			data.writeInt(var1);

			final byte[] bytes2 = CompressedStreamTools.compress(var2);
			data.writeShort((short) bytes2.length);
			data.write(bytes2);

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
		return GCCorePacketSpaceStationData.packetID;
	}
}
