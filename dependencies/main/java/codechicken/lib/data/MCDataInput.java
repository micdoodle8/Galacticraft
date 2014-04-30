package codechicken.lib.data;

import codechicken.lib.vec.BlockCoord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public interface MCDataInput
{
    public long readLong();
    public int readInt();
    public short readShort();
    public int readUShort();
    public byte readByte();
    public short readUByte();
    public double readDouble();
    public float readFloat();
    public boolean readBoolean();
    public char readChar();
    public int readVarShort();
    public int readVarInt();
    public byte[] readByteArray(int length);
    public String readString();
    public BlockCoord readCoord();
    public NBTTagCompound readNBTTagCompound();
    public ItemStack readItemStack();
    public FluidStack readFluidStack();
}
