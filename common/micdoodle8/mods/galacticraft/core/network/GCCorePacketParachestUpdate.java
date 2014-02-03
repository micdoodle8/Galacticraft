package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityParachest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCorePacketParachestUpdate.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketParachestUpdate implements IGalacticraftAdvancedPacket
{
	public static Packet buildKeyPacket(GCCoreTileEntityParachest chest)
	{
		final Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GalacticraftCore.CHANNEL;

		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(EnumPacketClient.UPDATE_PARACHEST.getIndex());
			data.writeInt(chest.xCoord);
			data.writeInt(chest.yCoord);
			data.writeInt(chest.zCoord);
			data.writeInt(chest.getSizeInventory());

			for (int i = 0; i < chest.getSizeInventory(); i++)
			{
				ItemStack stackAt = chest.getStackInSlot(i);
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
		return (byte) EnumPacketClient.UPDATE_PARACHEST.getIndex();
	}

	@Override
	public void handlePacket(DataInputStream stream, Object[] extraData, Side side)
	{
		try
		{
			final EntityPlayer player = (EntityPlayer) extraData[0];

			int xCoord = stream.readInt();
			int yCoord = stream.readInt();
			int zCoord = stream.readInt();
			TileEntity tile = player.worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
			int length = stream.readInt();

			if (tile != null && tile instanceof GCCoreTileEntityParachest)
			{
				GCCoreTileEntityParachest chest = (GCCoreTileEntityParachest) tile;
				chest.chestContents = new ItemStack[length];

				for (int i = 0; i < length; i++)
				{
					ItemStack stack = Packet.readItemStack(stream);
					chest.setInventorySlotContents(i, stack);
				}
			}

		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
}
