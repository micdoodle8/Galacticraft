package codechicken.lib.data;

import codechicken.lib.vec.BlockCoord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public interface MCDataOutput
{
    public MCDataOutput writeLong(long l);
    public MCDataOutput writeInt(int i);
    public MCDataOutput writeShort(int s);
    public MCDataOutput writeByte(int b);
    public MCDataOutput writeDouble(double d);
    public MCDataOutput writeFloat(float f);
    public MCDataOutput writeBoolean(boolean b);
    public MCDataOutput writeChar(char c);
    public MCDataOutput writeVarInt(int i);
    public MCDataOutput writeVarShort(int s);
    public MCDataOutput writeByteArray(byte[] array);
    public MCDataOutput writeString(String s);
    public MCDataOutput writeCoord(int x, int y, int z);
    public MCDataOutput writeCoord(BlockCoord coord);
    public MCDataOutput writeNBTTagCompound(NBTTagCompound tag);
    public MCDataOutput writeItemStack(ItemStack stack);
    public MCDataOutput writeFluidStack(FluidStack liquid);
}
