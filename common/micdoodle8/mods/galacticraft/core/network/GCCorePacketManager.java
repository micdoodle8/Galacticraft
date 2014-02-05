package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * GCCorePacketManager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePacketManager implements IPacketHandler, IPacketReceiver
{
	public enum GCCorePacketType
	{
		UNSPECIFIED,
		TILEENTITY,
		ENTITY;

		public static GCCorePacketType get(int id)
		{
			if (id >= 0 && id < GCCorePacketType.values().length)
			{
				return GCCorePacketType.values()[id];
			}
			return UNSPECIFIED;
		}
	}

	@SuppressWarnings("resource")
	public static Packet getPacket(String channelName, Entity sender, ArrayList<Object> objs)
	{
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(2);

			data.writeInt(sender.entityId);
			data = GCCorePacketManager.encodeDataStream(data, objs);

			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = channelName;
			packet.data = bytes.toByteArray();
			packet.length = packet.data.length;
			data.close();

			return packet;
		}
		catch (final IOException e)
		{
			System.out.println("Failed to create packet.");
			e.printStackTrace();
		}

		return null;
	}

	public static DataOutputStream encodeDataStream(DataOutputStream data, ArrayList<Object> sendData)
	{
		try
		{
			for (final Object dataValue : sendData)
			{
				if (dataValue instanceof Integer)
				{
					data.writeInt((Integer) dataValue);
				}
				else if (dataValue instanceof Float)
				{
					data.writeFloat((Float) dataValue);
				}
				else if (dataValue instanceof Double)
				{
					data.writeDouble((Double) dataValue);
				}
				else if (dataValue instanceof Byte)
				{
					data.writeByte((Byte) dataValue);
				}
				else if (dataValue instanceof Boolean)
				{
					data.writeBoolean((Boolean) dataValue);
				}
				else if (dataValue instanceof String)
				{
					data.writeUTF((String) dataValue);
				}
				else if (dataValue instanceof Short)
				{
					data.writeShort((Short) dataValue);
				}
				else if (dataValue instanceof Long)
				{
					data.writeLong((Long) dataValue);
				}
				else if (dataValue instanceof NBTTagCompound)
				{
					GCCorePacketManager.writeNBTTagCompound((NBTTagCompound) dataValue, data);
				}
			}

			return data;
		}
		catch (final IOException e)
		{
			System.out.println("Packet data encoding failed.");
			e.printStackTrace();
		}

		return data;
	}

	@SuppressWarnings("resource")
	public static Packet getPacket(String channelName, Entity sender, Object... sendData)
	{
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(2);

			data.writeInt(sender.entityId);
			data = GCCorePacketManager.encodeDataStream(data, sendData);

			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = channelName;
			packet.data = bytes.toByteArray();
			packet.length = packet.data.length;
			data.close();

			return packet;
		}
		catch (final IOException e)
		{
			System.out.println("Failed to create packet.");
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("resource")
	public static Packet getPacket(String channelName, TileEntity sender, Object... sendData)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);

		try
		{
			data.writeInt(GCCorePacketType.TILEENTITY.ordinal());

			data.writeInt(sender.xCoord);
			data.writeInt(sender.yCoord);
			data.writeInt(sender.zCoord);
			data = GCCorePacketManager.encodeDataStream(data, sendData);

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

	public static DataOutputStream encodeDataStream(DataOutputStream data, Object... sendData)
	{
		try
		{
			for (Object dataValue : sendData)
			{
				if (dataValue instanceof Integer)
				{
					data.writeInt((Integer) dataValue);
				}
				else if (dataValue instanceof Float)
				{
					data.writeFloat((Float) dataValue);
				}
				else if (dataValue instanceof Double)
				{
					data.writeDouble((Double) dataValue);
				}
				else if (dataValue instanceof Byte)
				{
					data.writeByte((Byte) dataValue);
				}
				else if (dataValue instanceof Boolean)
				{
					data.writeBoolean((Boolean) dataValue);
				}
				else if (dataValue instanceof String)
				{
					data.writeUTF((String) dataValue);
				}
				else if (dataValue instanceof Short)
				{
					data.writeShort((Short) dataValue);
				}
				else if (dataValue instanceof Long)
				{
					data.writeLong((Long) dataValue);
				}
				else if (dataValue instanceof NBTTagCompound)
				{
					GCCorePacketManager.writeNBTTagCompound((NBTTagCompound) dataValue, data);
				}
				else if (dataValue instanceof FluidTank)
				{
					GCCorePacketManager.writeFluidTank((FluidTank) dataValue, data);
				}
				else if (dataValue instanceof Entity)
				{
					data.writeInt(((Entity) dataValue).entityId);
				}
				else if (dataValue instanceof Vector3)
				{
					data.writeDouble(((Vector3) dataValue).x);
					data.writeDouble(((Vector3) dataValue).y);
					data.writeDouble(((Vector3) dataValue).z);
				}
				else if (dataValue instanceof Collection)
				{
					for (Object subDataValue : (Collection<?>) dataValue)
					{
						if (subDataValue instanceof Integer)
						{
							data.writeInt((Integer) subDataValue);
						}
						else if (subDataValue instanceof Float)
						{
							data.writeFloat((Float) subDataValue);
						}
						else if (subDataValue instanceof Double)
						{
							data.writeDouble((Double) subDataValue);
						}
						else if (subDataValue instanceof Byte)
						{
							data.writeByte((Byte) subDataValue);
						}
						else if (subDataValue instanceof Boolean)
						{
							data.writeBoolean((Boolean) subDataValue);
						}
						else if (subDataValue instanceof String)
						{
							data.writeUTF((String) subDataValue);
						}
						else if (subDataValue instanceof Short)
						{
							data.writeShort((Short) subDataValue);
						}
						else if (subDataValue instanceof Long)
						{
							data.writeLong((Long) subDataValue);
						}
						else if (subDataValue instanceof NBTTagCompound)
						{
							GCCorePacketManager.writeNBTTagCompound((NBTTagCompound) subDataValue, data);
						}
						else if (subDataValue instanceof FluidTank)
						{
							GCCorePacketManager.writeFluidTank((FluidTank) subDataValue, data);
						}
						else if (subDataValue instanceof Entity)
						{
							data.writeInt(((Entity) subDataValue).entityId);
						}
						else if (subDataValue instanceof Vector3)
						{
							data.writeDouble(((Vector3) subDataValue).x);
							data.writeDouble(((Vector3) subDataValue).y);
							data.writeDouble(((Vector3) subDataValue).z);
						}
					}
				}
			}

			return data;
		}
		catch (IOException e)
		{
			System.out.println("Packet data encoding failed.");
			e.printStackTrace();
		}

		return data;
	}

	@Override
	public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player)
	{
		if (packet == null)
		{
			FMLLog.severe("Packet received as null!");
			return;
		}

		if (packet.data == null)
		{
			FMLLog.severe("Packet data received as null! ID " + packet.getPacketId());
			return;
		}

		try
		{
			final ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);

			final int packetTypeID = data.readInt();

			if (packetTypeID == GCCorePacketType.ENTITY.ordinal())
			{
				final double id = data.readInt();

				final World world = ((EntityPlayer) player).worldObj;

				if (world != null)
				{
					for (final Object o : world.loadedEntityList)
					{
						if (o instanceof Entity)
						{
							final Entity e = (Entity) o;

							if (id == e.entityId && e instanceof IPacketReceiver)
							{
								((IPacketReceiver) e).handlePacketData(network, packetTypeID, packet, (EntityPlayer) player, data);
							}
						}
					}
				}
			}
			else if (packetTypeID == GCCorePacketType.TILEENTITY.ordinal())
			{
				final int x = data.readInt();
				final int y = data.readInt();
				final int z = data.readInt();

				final World world = ((EntityPlayer) player).worldObj;

				if (world != null)
				{
					final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

					if (tileEntity != null)
					{
						if (tileEntity instanceof IPacketReceiver)
						{
							((IPacketReceiver) tileEntity).handlePacketData(network, packetTypeID, packet, (EntityPlayer) player, data);
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{

	}

	public static void writeFluidTank(FluidTank fluidTank, DataOutputStream dataStream) throws IOException
	{
		if (fluidTank == null)
		{
			dataStream.writeInt(0);
			dataStream.writeInt(-1);
			dataStream.writeInt(0);
		}
		else
		{
			dataStream.writeInt(fluidTank.getCapacity());
			dataStream.writeInt(fluidTank.getFluid() == null ? -1 : fluidTank.getFluid().fluidID);
			dataStream.writeInt(fluidTank.getFluidAmount());
		}
	}

	public static FluidTank readFluidTank(ByteArrayDataInput dataStream) throws IOException
	{
		int capacity = dataStream.readInt();
		int fluidID = dataStream.readInt();
		FluidTank fluidTank = new FluidTank(capacity);
		int amount = dataStream.readInt();

		if (fluidID == -1)
		{
			fluidTank.setFluid(null);
		}
		else
		{
			Fluid fluid = FluidRegistry.getFluid(fluidID);
			fluidTank.setFluid(new FluidStack(fluid, amount));
		}

		return fluidTank;
	}

	public static void writeNBTTagCompound(NBTTagCompound tag, DataOutputStream dataStream) throws IOException
	{
		if (tag == null)
		{
			dataStream.writeShort(-1);
		}
		else
		{
			byte[] var2 = CompressedStreamTools.compress(tag);
			dataStream.writeShort((short) var2.length);
			dataStream.write(var2);
		}
	}

	public static void writeNBTTagCompound(NBTTagCompound tag, ByteArrayDataOutput dataStream) throws IOException
	{
		if (tag == null)
		{
			dataStream.writeShort(-1);
		}
		else
		{
			byte[] var2 = CompressedStreamTools.compress(tag);
			dataStream.writeShort((short) var2.length);
			dataStream.write(var2);
		}
	}

	public static NBTTagCompound readNBTTagCompound(DataInputStream dataStream) throws IOException
	{
		short var1 = dataStream.readShort();

		if (var1 < 0)
		{
			return null;
		}
		else
		{
			byte[] var2 = new byte[var1];
			dataStream.readFully(var2);
			return CompressedStreamTools.decompress(var2);
		}
	}

	public static NBTTagCompound readNBTTagCompound(ByteArrayDataInput dataStream) throws IOException
	{
		short var1 = dataStream.readShort();

		if (var1 < 0)
		{
			return null;
		}
		else
		{
			byte[] var2 = new byte[var1];
			dataStream.readFully(var2);
			return CompressedStreamTools.decompress(var2);
		}
	}

	public static void sendPacketToClients(Packet packet, World worldObj)
	{
		try
		{
			PacketDispatcher.sendPacketToAllInDimension(packet, worldObj.provider.dimensionId);
		}
		catch (Exception e)
		{
			System.out.println("Sending packet to client failed.");
			e.printStackTrace();
		}
	}

	public static void sendPacketToClients(Packet packet, World worldObj, Vector3 position, double range)
	{
		try
		{
			PacketDispatcher.sendPacketToAllAround(position.x, position.y, position.z, range, worldObj.provider.dimensionId, packet);
		}
		catch (Exception e)
		{
			System.out.println("Sending packet to client failed.");
			e.printStackTrace();
		}
	}

	public static void sendPacketToClients(Packet packet)
	{
		try
		{
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
		catch (Exception e)
		{
			System.out.println("Sending packet to client failed.");
			e.printStackTrace();
		}
	}

	public static Object getFieldValueFromStream(Field field, ByteArrayDataInput stream, World world) throws IOException
	{
		Class<?> dataValue = field.getType();

		if (dataValue.equals(int.class))
		{
			return stream.readInt();
		}
		else if (dataValue.equals(float.class))
		{
			return stream.readFloat();
		}
		else if (dataValue.equals(double.class))
		{
			return stream.readDouble();
		}
		else if (dataValue.equals(byte.class))
		{
			return stream.readByte();
		}
		else if (dataValue.equals(boolean.class))
		{
			return stream.readBoolean();
		}
		else if (dataValue.equals(String.class))
		{
			return stream.readUTF();
		}
		else if (dataValue.equals(short.class))
		{
			return stream.readShort();
		}
		else if (dataValue.equals(Long.class))
		{
			return stream.readLong();
		}
		else if (dataValue.equals(NBTTagCompound.class))
		{
			return GCCorePacketManager.readNBTTagCompound(stream);
		}
		else if (dataValue.equals(FluidTank.class))
		{
			return GCCorePacketManager.readFluidTank(stream);
		}
		else if (dataValue.equals(Vector3.class))
		{
			return new Vector3(stream.readDouble(), stream.readDouble(), stream.readDouble());
		}
		else
		{
			Class<?> c = dataValue;

			while (c != null)
			{
				if (c.equals(Entity.class))
				{
					return world.getEntityByID(stream.readInt());
				}

				c = c.getSuperclass();
			}
		}

		throw new NullPointerException("Field type not found: " + field.getType().getSimpleName());
	}
}
