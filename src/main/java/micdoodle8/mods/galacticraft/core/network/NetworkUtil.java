package micdoodle8.mods.galacticraft.core.network;

import com.google.common.math.DoubleMath;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorage;
import micdoodle8.mods.galacticraft.core.tile.FluidTankGC;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

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
            else if (dataValue instanceof EnergyStorage)
            {
                EnergyStorage storage = (EnergyStorage) dataValue;
                buffer.writeFloat(storage.getCapacityGC());
                buffer.writeFloat(storage.getMaxReceive());
                buffer.writeFloat(storage.getMaxExtract());
                buffer.writeFloat(storage.getEnergyStoredGC());
            }
            else if (dataValue instanceof NBTTagCompound)
            {
                NetworkUtil.writeNBTTagCompound((NBTTagCompound) dataValue, buffer);
            }
            else if (dataValue instanceof FluidTankGC)
            {
                FluidTankGC tankGC = (FluidTankGC) dataValue;
                BlockPos pos = tankGC.getTilePosition();
                buffer.writeInt(pos.getX());
                buffer.writeInt(pos.getY());
                buffer.writeInt(pos.getZ());
                NetworkUtil.writeFluidTank((FluidTank) dataValue, buffer);
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
            else if (dataValue instanceof BlockVec3)
            {
                buffer.writeInt(((BlockVec3) dataValue).x);
                buffer.writeInt(((BlockVec3) dataValue).y);
                buffer.writeInt(((BlockVec3) dataValue).z);
            }
            else if (dataValue instanceof byte[])
            {
                int size = ((byte[]) dataValue).length;
                buffer.writeInt(size);
                int pos = buffer.writerIndex();
                buffer.capacity(pos + size);
                buffer.setBytes(pos, (byte[]) dataValue);
                buffer.writerIndex(pos + size);
            }
            else if (dataValue instanceof UUID)
            {
                buffer.writeLong(((UUID) dataValue).getLeastSignificantBits());
                buffer.writeLong(((UUID) dataValue).getMostSignificantBits());
            }
            else if (dataValue instanceof Collection)
            {
                NetworkUtil.encodeData(buffer, (Collection<Object>) dataValue);
            }
            else if (dataValue instanceof FlagData)
            {
                buffer.writeInt(((FlagData) dataValue).getWidth());
                buffer.writeInt(((FlagData) dataValue).getHeight());

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
                    buffer.writeInt(array[i].dimension);
                    buffer.writeFloat((float) array[i].position.x);
                    buffer.writeFloat((float) array[i].position.y + 1);
                    buffer.writeFloat((float) array[i].position.z);
                    buffer.writeFloat(array[i].rotation);
                    buffer.writeShort(array[i].age);
                    ByteBufUtils.writeUTF8String(buffer, array[i].owner);
                }
            }
            else if (dataValue instanceof EnumFacing)
            {
                buffer.writeInt(((EnumFacing) dataValue).getIndex());
            }
            else if (dataValue instanceof BlockPos)
            {
                BlockPos pos = (BlockPos) dataValue;
                buffer.writeInt(pos.getX());
                buffer.writeInt(pos.getY());
                buffer.writeInt(pos.getZ());
            }
            else if (dataValue instanceof EnumDyeColor)
            {
                buffer.writeInt(((EnumDyeColor) dataValue).getDyeDamage());
            }
            else
            {
                if (dataValue == null)
                {
                    GCLog.severe("Cannot construct PacketSimple with null data, this is a bug.");
                }
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
            else if (clazz.equals(byte[].class))
            {
                int size = buffer.readInt();
                byte[] bytes = new byte[size];
                buffer.readBytes(bytes, 0, size);
                objList.add(bytes);
            }
            else if (clazz.equals(EnergyStorage.class))
            {
                EnergyStorage storage = new EnergyStorage(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
                storage.setEnergyStored(buffer.readFloat());
                objList.add(storage);
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
            else if (clazz.equals(BlockVec3.class))
            {
                objList.add(new BlockVec3(buffer.readInt(), buffer.readInt(), buffer.readInt()));
            }
            else if (clazz.equals(UUID.class))
            {
                objList.add(new UUID(buffer.readLong(), buffer.readLong()));
            }
            else if (clazz.equals(Vector3.class))
            {
                objList.add(new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()));
            }
            else if (clazz.equals(FlagData.class))
            {
                int width = buffer.readInt();
                int height = buffer.readInt();
                FlagData flagData = new FlagData(width, height);

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
                    objList.add(new Footprint(buffer.readInt(), new Vector3(buffer.readFloat(), buffer.readFloat(), buffer.readFloat()), buffer.readFloat(), buffer.readShort(), ByteBufUtils.readUTF8String(buffer), -1));
                }
            }
            else if (clazz.equals(EnumFacing.class))
            {
                objList.add(EnumFacing.getFront(buffer.readInt()));
            }
            else if (clazz.equals(BlockPos.class))
            {
                objList.add(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
            }
            else if (clazz.equals(EnumDyeColor.class))
            {
                objList.add(EnumDyeColor.byDyeDamage(buffer.readInt()));
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
        else if (dataValue.equals(FluidTankGC.class))
        {
            return NetworkUtil.readFluidTankGC(buffer, world);
        }
        else if (dataValue.equals(FluidTank.class))
        {
            return NetworkUtil.readFluidTank(buffer);
        }
        else if (dataValue.equals(Vector3.class))
        {
            return new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }
        else if (dataValue.equals(BlockVec3.class))
        {
            return new BlockVec3(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
        else if (dataValue.equals(UUID.class))
        {
            return new UUID(buffer.readLong(), buffer.readLong());
        }
        else if (dataValue.equals(byte[].class))
        {
            byte[] bytes = new byte[buffer.readInt()];
            for (int i = 0; i < bytes.length; i++)
            {
                bytes[i] = buffer.readByte();
            }
            return bytes;
        }
        else if (dataValue.equals(EnergyStorage.class))
        {
            float capacity = buffer.readFloat();
            float maxReceive = buffer.readFloat();
            float maxExtract = buffer.readFloat();
            EnergyStorage storage = new EnergyStorage(capacity, maxReceive, maxExtract);
            storage.setEnergyStored(buffer.readFloat());
            return storage;
        }
        else if (dataValue.equals(EnumFacing.class))
        {
            return EnumFacing.getFront(buffer.readInt());
        }
        else if (dataValue.equals(BlockPos.class))
        {
            return new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
        else if (dataValue.equals(EnumDyeColor.class))
        {
            return EnumDyeColor.byDyeDamage(buffer.readInt());
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
            if (buffer.readBoolean())
            {
                itemstack.setTagCompound(readNBTTagCompound(buffer));
            }
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
                nbttagcompound = itemStack.getTagCompound();
            }

            buffer.writeBoolean(nbttagcompound != null);
            if (nbttagcompound != null)
            {
                NetworkUtil.writeNBTTagCompound(nbttagcompound, buffer);
            }
        }
    }

    public static NBTTagCompound readNBTTagCompound(ByteBuf buffer) throws IOException
    {
        try
        {
            int length = buffer.readInt();
            byte[] compressed = new byte[length];
            buffer.readBytes(compressed);
            ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
            return CompressedStreamTools.readCompressed(bais);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeNBTTagCompound(NBTTagCompound nbt, ByteBuf buffer) throws IOException
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CompressedStreamTools.writeCompressed(nbt, baos);
            byte[] compressed = baos.toByteArray();
            buffer.writeInt(compressed.length);
            buffer.writeBytes(compressed);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void writeFluidTank(FluidTank fluidTank, ByteBuf buffer) throws IOException
    {
        if (fluidTank == null)
        {
            buffer.writeInt(0);
            ByteBufUtils.writeUTF8String(buffer, "");
            buffer.writeInt(0);
        }
        else
        {
            buffer.writeInt(fluidTank.getCapacity());
            ByteBufUtils.writeUTF8String(buffer, fluidTank.getFluid() == null ? "" : fluidTank.getFluid().getFluid().getName());
            buffer.writeInt(fluidTank.getFluidAmount());
        }
    }

    public static FluidTankGC readFluidTankGC(ByteBuf buffer, World world) throws IOException
    {
        BlockPos pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        TileEntity tile = world.getTileEntity(pos);
        int capacity = buffer.readInt();
        String fluidName = ByteBufUtils.readUTF8String(buffer);
        FluidTankGC fluidTank = new FluidTankGC(capacity, tile);
        int amount = buffer.readInt();

        if (fluidName.equals(""))
        {
            fluidTank.setFluid(null);
        }
        else
        {
            Fluid fluid = FluidRegistry.getFluid(fluidName);
            fluidTank.setFluid(new FluidStack(fluid, amount));
        }

        return fluidTank;
    }

    public static FluidTank readFluidTank(ByteBuf buffer) throws IOException
    {
        int capacity = buffer.readInt();
        String fluidName = ByteBufUtils.readUTF8String(buffer);
        FluidTank fluidTank = new FluidTank(capacity);
        int amount = buffer.readInt();

        if (fluidName.equals(""))
        {
            fluidTank.setFluid(null);
        }
        else
        {
            Fluid fluid = FluidRegistry.getFluid(fluidName);
            fluidTank.setFluid(new FluidStack(fluid, amount));
        }

        return fluidTank;
    }

    public static boolean fuzzyEquals(Object a, Object b)
    {
        if ((a == null) != (b == null))
        {
            return false;
        }
        else if (a == null)
        {
            return true;
        }
        else if (a instanceof Float && b instanceof Float)
        {
            float af = (Float) a; 
            float bf = (Float) b; 
            return af == bf || Math.abs(af - bf) < 0.01F;
        }
        else if (a instanceof Double && b instanceof Double)
        {
            return DoubleMath.fuzzyEquals((Double) a, (Double) b, 0.01);
        }
        else if (a instanceof Entity && b instanceof Entity)
        {
            Entity a2 = (Entity) a;
            Entity b2 = (Entity) b;
            return fuzzyEquals(a2.getEntityId(), b2.getEntityId());
        }
        else if (a instanceof Vector3 && b instanceof Vector3)
        {
            Vector3 a2 = (Vector3) a;
            Vector3 b2 = (Vector3) b;
            return fuzzyEquals(a2.x, b2.x) &&
                    fuzzyEquals(a2.y, b2.y) &&
                    fuzzyEquals(a2.z, b2.z);
        }
        else if (a instanceof EnergyStorage && b instanceof EnergyStorage)
        {
            EnergyStorage a2 = (EnergyStorage) a;
            EnergyStorage b2 = (EnergyStorage) b;
            return fuzzyEquals(a2.getEnergyStoredGC(), b2.getEnergyStoredGC()) &&
                    fuzzyEquals(a2.getCapacityGC(), b2.getCapacityGC()) &&
                    fuzzyEquals(a2.getMaxReceive(), b2.getMaxReceive()) &&
                    fuzzyEquals(a2.getMaxExtract(), b2.getMaxExtract());
        }
        else if (a instanceof FluidTank && b instanceof FluidTank)
        {
            FluidTank a2 = (FluidTank) a;
            FluidTank b2 = (FluidTank) b;
            FluidStack fluidA = a2.getFluid();
            FluidStack fluidB = b2.getFluid();
            return fuzzyEquals(a2.getCapacity(), b2.getCapacity()) &&
                    fuzzyEquals(fluidA != null ? fluidA.getFluid().getName() : "", fluidB != null ? fluidB.getFluid().getName() : "") &&
                    fuzzyEquals(a2.getFluidAmount(), b2.getFluidAmount());
        }
        else
        {
            return a.equals(b);
        }
    }

    public static Object cloneNetworkedObject(Object a)
    {
        // We only need to clone mutable objects
        if (a instanceof EnergyStorage)
        {
            EnergyStorage prevStorage = (EnergyStorage) a;
            EnergyStorage storage = new EnergyStorage(prevStorage.getCapacityGC(), prevStorage.getMaxReceive(), prevStorage.getMaxExtract());
            storage.setEnergyStored(prevStorage.getEnergyStoredGC());
            return storage;
        }
        else if (a instanceof FluidTankGC)
        {
            FluidTankGC prevTank = (FluidTankGC) a;
            FluidTankGC tank = new FluidTankGC(prevTank.getFluid(), prevTank.getCapacity(), prevTank.getTile());
            return tank;
        }
        else if (a instanceof FluidTank)
        {
            FluidTank prevTank = (FluidTank)a;
            FluidStack prevFluid = prevTank.getFluid();
            prevFluid = prevFluid == null ? null : prevFluid.copy();
            FluidTank tank = new FluidTank(prevFluid, prevTank.getCapacity());
            return tank;
        }
        else
        {
            return a;
        }
    }
}
