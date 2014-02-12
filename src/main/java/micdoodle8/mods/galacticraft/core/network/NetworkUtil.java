package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.util.Collection;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import cpw.mods.fml.common.network.ByteBufUtils;

public class NetworkUtil 
{
	public static void encodeData(ByteBuf data, Collection<Object> sendData)
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
				ByteBufUtils.writeUTF8String(data, (String) dataValue);
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
//				GCCorePacketManager.writeNBTTagCompound((NBTTagCompound) dataValue, data); TODO
			}
			else if (dataValue instanceof FluidTank)
			{
//				GCCorePacketManager.writeFluidTank((FluidTank) dataValue, data); TODO
			}
			else if (dataValue instanceof Entity)
			{
				data.writeInt(((Entity) dataValue).getEntityId());
			}
			else if (dataValue instanceof Vector3)
			{
				data.writeDouble(((Vector3) dataValue).x);
				data.writeDouble(((Vector3) dataValue).y);
				data.writeDouble(((Vector3) dataValue).z);
			}
			else if (dataValue instanceof Collection)
			{
				encodeData(data, (Collection<Object>) dataValue);
			}
		}
	}
	
	public static Object getFieldValueFromStream(Field field, ByteBuf buffer, World world)
	{
		Class<?> dataValue = field.getType();

		if (dataValue.equals(int.class))
		{
			return buffer.readInt();
		}
		else if (dataValue.equals(float.class))
		{
			return buffer.readFloat();
		}
		else if (dataValue.equals(double.class))
		{
			return buffer.readDouble();
		}
		else if (dataValue.equals(byte.class))
		{
			return buffer.readByte();
		}
		else if (dataValue.equals(boolean.class))
		{
			return buffer.readBoolean();
		}
		else if (dataValue.equals(String.class))
		{
			return ByteBufUtils.readUTF8String(buffer);
		}
		else if (dataValue.equals(short.class))
		{
			return buffer.readShort();
		}
		else if (dataValue.equals(Long.class))
		{
			return buffer.readLong();
		}
//		else if (dataValue.equals(NBTTagCompound.class))
//		{
//			return GCCorePacketManager.readNBTTagCompound(buffer); TODO
//		} 
//		else if (dataValue.equals(FluidTank.class))
//		{
//			return GCCorePacketManager.readFluidTank(buffer); TODO
//		}
		else if (dataValue.equals(Vector3.class))
		{
			return new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()); 
		}
		else
		{
			Class<?> c = dataValue;

			while (c != null)
			{
				if (c.equals(Entity.class))
				{
					return world.getEntityByID(buffer.readInt());
				}

				c = c.getSuperclass();
			}
		}

		throw new NullPointerException("Field type not found: " + field.getType().getSimpleName());
	}
}
