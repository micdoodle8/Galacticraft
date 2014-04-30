package codechicken.lib.data;

import codechicken.lib.vec.BlockCoord;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.io.DataOutputStream;
import java.io.IOException;

public class MCOutputStreamWrapper implements MCDataOutput
{
    public DataOutputStream dataout;
    
    public MCOutputStreamWrapper(DataOutputStream out)
    {
        dataout = out;
    }
    
    public MCOutputStreamWrapper writeBoolean(boolean b)
    {
        try
        {
            dataout.writeBoolean(b);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeByte(int b)
    {
        try
        {
            dataout.writeByte(b);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeShort(int s)
    {
        try
        {
            dataout.writeShort(s);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeInt(int i)
    {
        try
        {
            dataout.writeInt(i);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeFloat(float f)
    {
        try
        {
            dataout.writeFloat(f);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeDouble(double d)
    {
        try
        {
            dataout.writeDouble(d);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeLong(long l)
    {
        try
        {
            dataout.writeLong(l);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
        
    @Override
    public MCOutputStreamWrapper writeChar(char c)
    {
        try
        {
            dataout.writeChar(c);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public MCDataOutput writeVarInt(int i) {
        while ((i & 0x80) != 0)
        {
            writeByte(i & 0x7F | 0x80);
            i >>>= 7;
        }

        writeByte(i);
        return this;
    }

    @Override
    public MCDataOutput writeVarShort(int s) {
        int low = s & 0x7FFF;
        int high = (s & 0x7F8000) >> 15;
        if (high != 0)
            low |= 0x8000;
        writeShort(low);
        if (high != 0)
            writeByte(high);
        return this;
    }

    public MCOutputStreamWrapper writeByteArray(byte[] barray)
    {
        try
        {
            dataout.write(barray);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeCoord(int x, int y, int z)
    {
        writeInt(x);
        writeInt(y);
        writeInt(z);
        return this;
    }
    
    public MCOutputStreamWrapper writeCoord(BlockCoord coord)
    {
        writeInt(coord.x);
        writeInt(coord.y);
        writeInt(coord.z);
        return this;
    }
    
    public MCOutputStreamWrapper writeString(String s)
    {
        try
        {
            if(s.length() > 65535)
                throw new IOException("String length: "+s.length()+"too long.");
            dataout.writeShort(s.length());
            dataout.writeChars(s);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    public MCOutputStreamWrapper writeItemStack(ItemStack stack)
    {
        writeItemStack(stack, false);
        return this;
    }
    
    public MCOutputStreamWrapper writeItemStack(ItemStack stack, boolean large) {
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
        
    public MCOutputStreamWrapper writeNBTTagCompound(NBTTagCompound compound)
    {
        try
        {            
            if (compound == null)
            {
                writeShort(-1);
            }
            else
            {
                byte[] bytes = CompressedStreamTools.compress(compound);
                writeShort((short)bytes.length);
                writeByteArray(bytes);
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MCOutputStreamWrapper writeFluidStack(FluidStack fluid)
    {
        if (fluid == null)
        {
            writeShort(-1);
        }
        else
        {
            writeShort(fluid.fluidID);
            writeInt(fluid.amount);
            writeNBTTagCompound(fluid.tag);
        }
        return this;
    }
}
