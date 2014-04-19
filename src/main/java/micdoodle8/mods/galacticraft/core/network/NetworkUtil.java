package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import cpw.mods.fml.common.network.ByteBufUtils;

public class NetworkUtil
{
	public static void encodeData(ByteBuf buffer, Collection<Object> sendData) throws IOException
	{
		for (Object dataValue : sendData)
		{
			if (dataValue instanceof Integer)
			{
				buffer.writeInt((Integer) dataValue);
			}
			else if (dataValue instanceof Float)
			{
				buffer.writeFloat((Float) dataValue);
			}
			else if (dataValue instanceof Double)
			{
				buffer.writeDouble((Double) dataValue);
			}
			else if (dataValue instanceof Byte)
			{
				buffer.writeByte((Byte) dataValue);
			}
			else if (dataValue instanceof Boolean)
			{
				buffer.writeBoolean((Boolean) dataValue);
			}
			else if (dataValue instanceof String)
			{
				ByteBufUtils.writeUTF8String(buffer, (String) dataValue);
			}
			else if (dataValue instanceof Short)
			{
				buffer.writeShort((Short) dataValue);
			}
			else if (dataValue instanceof Long)
			{
				buffer.writeLong((Long) dataValue);
			}
			else if (dataValue instanceof NBTTagCompound)
			{
				NetworkUtil.writeNBTTagCompound((NBTTagCompound) dataValue, buffer);
			}
			else if (dataValue instanceof FluidTank)
			{
				NetworkUtil.writeFluidTank((FluidTank) dataValue, buffer);
			}
			else if (dataValue instanceof Entity)
			{
				buffer.writeInt(((Entity) dataValue).getEntityId());
			}
			else if (dataValue instanceof Vector3)
			{
				buffer.writeDouble(((Vector3) dataValue).x);
				buffer.writeDouble(((Vector3) dataValue).y);
				buffer.writeDouble(((Vector3) dataValue).z);
			}
			else if (dataValue instanceof Collection)
			{
				NetworkUtil.encodeData(buffer, (Collection<Object>) dataValue);
			}
			else if (dataValue instanceof FlagData)
			{
				buffer.writeInt(((FlagData) dataValue).getWidth());
				buffer.writeInt(((FlagData) dataValue).getHeight());
				buffer.writeBoolean(((FlagData) dataValue).getHasFace());
				
				for (int i = 0; i < ((FlagData) dataValue).getWidth(); i++)
				{
					for (int j = 0; j < ((FlagData) dataValue).getHeight(); j++)
					{
						Vector3 vec = ((FlagData) dataValue).getColorAt(i, j);
						buffer.writeByte((byte) (vec.x * 256 - 128));
						buffer.writeByte((byte) (vec.y * 256 - 128));
						buffer.writeByte((byte) (vec.z * 256 - 128));
					}
				}
			}
			else if (dataValue instanceof Integer[])
			{
				Integer[] array = (Integer[]) dataValue;
				buffer.writeInt(array.length);

				for (int i = 0; i < array.length; i++)
				{
					buffer.writeInt(array[i]);
				}
			}
			else if (dataValue instanceof String[])
			{
				String[] array = (String[]) dataValue;
				buffer.writeInt(array.length);

				for (int i = 0; i < array.length; i++)
				{
					ByteBufUtils.writeUTF8String(buffer, array[i]);
				}
			}
			else if (dataValue instanceof Footprint[])
			{
				Footprint[] array = (Footprint[]) dataValue;
				buffer.writeInt(array.length);

				for (int i = 0; i < array.length; i++)
				{
					buffer.writeFloat((float) array[i].position.x);
					buffer.writeFloat((float) array[i].position.y + 1);
					buffer.writeFloat((float) array[i].position.z);
					buffer.writeFloat((float) array[i].rotation);
					buffer.writeShort((short) array[i].age);
				}
			}
			else
			{
				GCLog.info("Could not find data type to encode!: " + dataValue);
			}
		}
	}

	public static ArrayList<Object> decodeData(Class<?>[] types, ByteBuf buffer)
	{
		ArrayList<Object> objList = new ArrayList<Object>();

		for (Class clazz : types)
		{
			if (clazz.equals(Integer.class))
			{
				objList.add(buffer.readInt());
			}
			else if (clazz.equals(Float.class))
			{
				objList.add(buffer.readFloat());
			}
			else if (clazz.equals(Double.class))
			{
				objList.add(buffer.readDouble());
			}
			else if (clazz.equals(Byte.class))
			{
				objList.add(buffer.readByte());
			}
			else if (clazz.equals(Boolean.class))
			{
				objList.add(buffer.readBoolean());
			}
			else if (clazz.equals(String.class))
			{
				objList.add(ByteBufUtils.readUTF8String(buffer));
			}
			else if (clazz.equals(Short.class))
			{
				objList.add(buffer.readShort());
			}
			else if (clazz.equals(Long.class))
			{
				objList.add(buffer.readLong());
			}
			else if (clazz.equals(NBTTagCompound.class))
			{
				try
				{
					objList.add(NetworkUtil.readNBTTagCompound(buffer));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else if (clazz.equals(FlagData.class))
			{
				int width = buffer.readInt();
				int height = buffer.readInt();
				boolean hasFace = buffer.readBoolean();
				FlagData flagData = new FlagData(width, height);
				flagData.setHasFace(hasFace);
				
				for (int i = 0; i < width; i++)
				{
					for (int j = 0; j < height; j++)
					{
						flagData.setColorAt(i, j, new Vector3(buffer.readByte() + 128, buffer.readByte() + 128, buffer.readByte() + 128));
					}
				}
				
				objList.add(flagData);
			}
			else if (clazz.equals(Integer[].class))
			{
				int size = buffer.readInt();

				for (int i = 0; i < size; i++)
				{
					objList.add(buffer.readInt());
				}
			}
			else if (clazz.equals(String[].class))
			{
				int size = buffer.readInt();

				for (int i = 0; i < size; i++)
				{
					objList.add(ByteBufUtils.readUTF8String(buffer));
				}
			}
			else if (clazz.equals(Footprint[].class))
			{
				int size = buffer.readInt();

				for (int i = 0; i < size; i++)
				{
					objList.add(new Footprint(new Vector3(buffer.readFloat(), buffer.readFloat(), buffer.readFloat()), buffer.readFloat(), buffer.readShort()));
				}
			}
		}

		return objList;
	}

	public static Object getFieldValueFromStream(Field field, ByteBuf buffer, World world) throws IOException
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
		else if (dataValue.equals(NBTTagCompound.class))
		{
			return NetworkUtil.readNBTTagCompound(buffer);
		}
		else if (dataValue.equals(FluidTank.class))
		{
			return NetworkUtil.readFluidTank(buffer);
		}
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

	public static ItemStack readItemStack(ByteBuf buffer) throws IOException
	{
		ItemStack itemstack = null;
		short itemID = buffer.readShort();

		if (itemID >= 0)
		{
			byte stackSize = buffer.readByte();
			short meta = buffer.readShort();
			itemstack = new ItemStack(Item.getItemById(itemID), stackSize, meta);
			itemstack.stackTagCompound = NetworkUtil.readNBTTagCompound(buffer);
		}

		return itemstack;
	}

	public static void writeItemStack(ItemStack itemStack, ByteBuf buffer) throws IOException
	{
		if (itemStack == null)
		{
			buffer.writeShort(-1);
		}
		else
		{
			buffer.writeShort(Item.getIdFromItem(itemStack.getItem()));
			buffer.writeByte(itemStack.stackSize);
			buffer.writeShort(itemStack.getItemDamage());
			NBTTagCompound nbttagcompound = null;

			if (itemStack.getItem().isDamageable() || itemStack.getItem().getShareTag())
			{
				nbttagcompound = itemStack.stackTagCompound;
			}

			NetworkUtil.writeNBTTagCompound(nbttagcompound, buffer);
		}
	}

	public static NBTTagCompound readNBTTagCompound(ByteBuf buffer) throws IOException
	{
		short dataLength = buffer.readShort();

		if (dataLength < 0)
		{
			return null;
		}
		else
		{
			byte[] compressedNBT = new byte[dataLength];
			buffer.readBytes(compressedNBT);
			return CompressedStreamTools.decompress(compressedNBT);
		}
	}

	public static void writeNBTTagCompound(NBTTagCompound nbt, ByteBuf buffer) throws IOException
	{
		if (nbt == null)
		{
			buffer.writeShort(-1);
		}
		else
		{
			byte[] compressedNBT = CompressedStreamTools.compress(nbt);
			buffer.writeShort((short) compressedNBT.length);
			buffer.writeBytes(compressedNBT);
		}
	}

	public static void writeFluidTank(FluidTank fluidTank, ByteBuf buffer) throws IOException
	{
		if (fluidTank == null)
		{
			buffer.writeInt(0);
			buffer.writeInt(-1);
			buffer.writeInt(0);
		}
		else
		{
			buffer.writeInt(fluidTank.getCapacity());
			buffer.writeInt(fluidTank.getFluid() == null ? -1 : fluidTank.getFluid().fluidID);
			buffer.writeInt(fluidTank.getFluidAmount());
		}
	}

	public static FluidTank readFluidTank(ByteBuf buffer) throws IOException
	{
		int capacity = buffer.readInt();
		int fluidID = buffer.readInt();
		FluidTank fluidTank = new FluidTank(capacity);
		int amount = buffer.readInt();

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
}
