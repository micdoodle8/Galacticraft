package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.IControllableEntity;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketControllableEntity.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketControllableEntity implements IGalacticraftAdvancedPacket
{
	public static final int packetID = EnumPacketServer.UPDATE_CONTROLLABLE_ENTITY.getIndex();

	public static Packet buildKeyPacket(int key)
	{
		final Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;

		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(GCCorePacketControllableEntity.packetID);
			data.writeInt(key);

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
	public byte getPacketID()
	{
		return (byte) GCCorePacketControllableEntity.packetID;
	}

	@Override
	public void handlePacket(DataInputStream stream, Object[] extraData, Side side)
	{
		try
		{
			final EntityPlayer player = (EntityPlayer) extraData[0];

			int key = stream.readInt();

			if (player.ridingEntity != null && player.ridingEntity instanceof IControllableEntity)
			{
				((IControllableEntity) player.ridingEntity).pressKey(key);
			}

		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
}
