package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketLanderUpdate.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketLanderUpdate implements IGalacticraftAdvancedPacket
{
	public static Packet buildKeyPacket(Entity lander)
	{
		if (!(lander instanceof IInventory))
		{
			return null;
		}

		final Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;

		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(EnumPacketClient.UPDATE_LANDER.getIndex());
			data.writeInt(lander.entityId);
			data.writeInt(((IInventory) lander).getSizeInventory());

			for (int i = 0; i < ((IInventory) lander).getSizeInventory(); i++)
			{
				ItemStack stackAt = ((IInventory) lander).getStackInSlot(i);
				Packet.writeItemStack(stackAt, data);
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
	public byte getPacketID()
	{
		return (byte) EnumPacketClient.UPDATE_LANDER.getIndex();
	}

	@Override
	public void handlePacket(DataInputStream stream, Object[] extraData, Side side)
	{
		try
		{
			final EntityPlayer player = (EntityPlayer) extraData[0];

			int entityID = stream.readInt();
			Entity e = player.worldObj.getEntityByID(entityID);
			int length = stream.readInt();

			if (e != null && e instanceof IInventorySettable)
			{
				((IInventorySettable) e).setSizeInventory(length);

				for (int i = 0; i < length; i++)
				{
					ItemStack stack = Packet.readItemStack(stream);
					((IInventory) e).setInventorySlotContents(i, stack);
				}
			}

		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
}
