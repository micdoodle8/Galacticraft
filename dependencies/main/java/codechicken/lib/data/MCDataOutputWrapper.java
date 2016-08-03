package codechicken.lib.data;

import codechicken.lib.vec.BlockCoord;
import io.netty.handler.codec.EncoderException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.io.DataOutput;
import java.io.IOException;

public class MCDataOutputWrapper implements MCDataOutput {
    public DataOutput dataout;

    public MCDataOutputWrapper(DataOutput out) {
        dataout = out;
    }

    public MCDataOutputWrapper writeBoolean(boolean b) {
        try {
            dataout.writeBoolean(b);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeByte(int b) {
        try {
            dataout.writeByte(b);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeShort(int s) {
        try {
            dataout.writeShort(s);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeInt(int i) {
        try {
            dataout.writeInt(i);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeFloat(float f) {
        try {
            dataout.writeFloat(f);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeDouble(double d) {
        try {
            dataout.writeDouble(d);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        return this;
    }

    public MCDataOutputWrapper writeLong(long l) {
        try {
            dataout.writeLong(l);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        return this;
    }

    @Override
    public MCDataOutputWrapper writeChar(char c) {
        try {
            dataout.writeChar(c);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        return this;
    }

    @Override
    public MCDataOutput writeVarInt(int i) {
        MCDataIO.writeVarInt(this, i);
        return this;
    }

    @Override
    public MCDataOutput writeVarShort(int s) {
        MCDataIO.writeVarShort(this, s);
        return this;
    }

    public MCDataOutputWrapper writeArray(byte[] barray) {
        try {
            dataout.write(barray);
        } catch (IOException e) {
            throw new EncoderException(e);
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
        MCDataIO.writeString(this, s);
        return this;
    }

    public MCDataOutputWrapper writeItemStack(ItemStack stack) {
        MCDataIO.writeItemStack(this, stack);
        return this;
    }

    public MCDataOutputWrapper writeNBTTagCompound(NBTTagCompound nbt) {
        if (nbt == null) {
            this.writeByte(0);
        } else {
            try {
                CompressedStreamTools.write(nbt, dataout);
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
        return this;
    }

    public MCDataOutputWrapper writeFluidStack(FluidStack fluid) {
        MCDataIO.writeFluidStack(this, fluid);
        return this;
    }
}
