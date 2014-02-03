package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityControllable;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketEntityUpdate.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketEntityUpdate implements IGalacticraftAdvancedPacket
{
	public static Packet buildUpdatePacket(GCCoreEntityControllable driveable)
	{
		final Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;

		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(EnumPacketClient.UPDATE_CONTROLLABLE_ENTITY.getIndex());
			data.writeInt(driveable.entityId);
			data.writeDouble(driveable.posX);
			data.writeDouble(driveable.posY);
			data.writeDouble(driveable.posZ);
			data.writeFloat(driveable.rotationYaw);
			data.writeFloat(driveable.rotationPitch);
			data.writeDouble(driveable.motionX);
			data.writeDouble(driveable.motionY);
			data.writeDouble(driveable.motionZ);
			data.writeBoolean(driveable.onGround);

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
		try
		{
			final EntityPlayer player = (EntityPlayer) extradata[0];

			final int entityId = stream.readInt();
			GCCoreEntityControllable driveable = null;

			for (final Object obj : player.worldObj.loadedEntityList)
			{
				if (obj instanceof GCCoreEntityControllable && ((Entity) obj).entityId == entityId)
				{
					driveable = (GCCoreEntityControllable) obj;
					break;
				}
			}

			if (driveable != null)
			{
				driveable.setPositionRotationAndMotion(stream.readDouble(), stream.readDouble(), stream.readDouble(), stream.readFloat(), stream.readFloat(), stream.readDouble(), stream.readDouble(), stream.readDouble(), stream.readBoolean());
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public byte getPacketID()
	{
		return (byte) EnumPacketClient.UPDATE_CONTROLLABLE_ENTITY.getIndex();
	}
}
