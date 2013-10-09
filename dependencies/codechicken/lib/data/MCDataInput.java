package codechicken.lib.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import codechicken.lib.vec.BlockCoord;

public interface MCDataInput
{
    public long readLong();
    public int readInt();
    public short readShort();
    public int readUShort();
    public byte readByte();
    public int readUByte();
    public double readDouble();
    public float readFloat();
    public boolean readBoolean();
    public char readChar();
    public byte[] readByteArray(int length);
    public String readString();
    public BlockCoord readCoord();
    public NBTTagCompound readNBTTagCompound();
    public ItemStack readItemStack();
    public FluidStack readFluidStack();
}
