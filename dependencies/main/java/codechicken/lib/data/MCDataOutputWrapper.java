package codechicken.lib.data;

import codechicken.lib.vec.BlockCoord;
import com.google.common.base.Charsets;
import cpw.mods.fml.common.network.ByteBufUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.Validate;

import java.io.DataOutput;
import java.io.IOException;

public class MCDataOutputWrapper implements MCDataOutput
{
    /**
     * Mimics ByteBufUtils
     * Write an integer using variable length encoding.
     *
     * @param to The buffer to write to
     * @param i The integer to write
     */
    public static void writeVarInt(DataOutput to, int i) throws IOException {
        while ((i & 0x80) != 0) {
            to.writeByte(i & 0x7F | 0x80);
            i >>>= 7;
        }

        to.writeByte(i);
    }

    /**
     * Mimics ByteBufUtils
     * Write an extended short using a short and a byte if necessary
     *
     * @param to The buffer to write to
     * @param s The short to write, less than 0x7FFFFF
     */
    public static void writeVarShort(DataOutput to, int s) throws IOException {
        int low = s & 0x7FFF;
        int high = (s & 0x7F8000) >> 15;
        if (high != 0)
            low |= 0x8000;
        to.writeShort(low);
        if (high != 0)
            to.writeByte(high);
    }

    /**
     * Mimics ByteBufUtils
     * Write a String with UTF8 byte encoding to the buffer.
     * It is encoded as <varint length>[<UTF8 char bytes>]
     * @param to the data output to write to
     * @param string The string to write
     */
    public static void writeUTF8String(DataOutput to, String string) throws IOException {
        byte[] utf8Bytes = string.getBytes(Charsets.UTF_8);
        Validate.isTrue(ByteBufUtils.varIntByteCount(utf8Bytes.length) < 3, "The string is too long for this encoding.");
        writeVarInt(to, utf8Bytes.length);
        to.write(utf8Bytes);
    }

    public DataOutput dataout;

    public MCDataOutputWrapper(DataOutput out) {
        dataout = out;
    }

    public MCDataOutputWrapper writeBoolean(boolean b) {
        try {
            dataout.writeBoolean(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeByte(int b) {
        try {
            dataout.writeByte(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeShort(int s) {
        try {
            dataout.writeShort(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeInt(int i) {
        try {
            dataout.writeInt(i);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeFloat(float f) {
        try {
            dataout.writeFloat(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeDouble(double d) {
        try {
            dataout.writeDouble(d);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeLong(long l) {
        try {
            dataout.writeLong(l);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public MCDataOutputWrapper writeChar(char c) {
        try {
            dataout.writeChar(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public MCDataOutput writeVarInt(int i) {
        try {
            writeVarInt(dataout, i);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public MCDataOutput writeVarShort(int s) {
        try {
            writeVarShort(dataout, s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeByteArray(byte[] barray) {
        try {
            dataout.write(barray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeCoord(int x, int y, int z) {
        writeInt(x);
        writeInt(y);
        writeInt(z);
        return this;
    }

    public MCDataOutputWrapper writeCoord(BlockCoord coord) {
        writeInt(coord.x);
        writeInt(coord.y);
        writeInt(coord.z);
        return this;
    }

    public MCDataOutputWrapper writeString(String s) {
        try {
            writeUTF8String(dataout, s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeItemStack(ItemStack stack) {
        writeItemStack(stack, false);
        return this;
    }

    public MCDataOutputWrapper writeItemStack(ItemStack stack, boolean large) {
        if (stack == null) {
            writeShort(-1);
        } else {
            writeShort(Item.getIdFromItem(stack.getItem()));
            if (large)
                writeInt(stack.stackSize);
            else
                writeByte(stack.stackSize);
            writeShort(stack.getItemDamage());
            writeNBTTagCompound(stack.stackTagCompound);
        }
        return this;
    }

    public MCDataOutputWrapper writeNBTTagCompound(NBTTagCompound compound) {
        try {
            if (compound == null) {
                writeShort(-1);
            } else {
                byte[] bytes = CompressedStreamTools.compress(compound);
                writeShort((short) bytes.length);
                writeByteArray(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeFluidStack(FluidStack fluid) {
        if (fluid == null) {
            writeShort(-1);
        } else {
            writeShort(fluid.getFluidID());
            writeVarInt(fluid.amount);
            writeNBTTagCompound(fluid.tag);
        }
        return this;
    }
}
