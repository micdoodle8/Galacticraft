package codechicken.lib.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

public interface MCDataInput {

    long readLong();

    int readInt();

    short readShort();

    int readUShort();

    byte readByte();

    short readUByte();

    double readDouble();

    float readFloat();

    boolean readBoolean();

    char readChar();

    int readVarShort();

    int readVarInt();

    long readVarLong();

    byte[] readArray(int length);

    String readString();

    BlockPos readPos();

    NBTTagCompound readNBTTagCompound();

    ItemStack readItemStack();

    FluidStack readFluidStack();
}
