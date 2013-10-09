package codechicken.lib.data;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import codechicken.lib.vec.BlockCoord;

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
    
    public MCOutputStreamWrapper writeItemStack(ItemStack stack, boolean large)
    {
        if (stack == null)
        {
            writeShort(-1);
        }
        else
        {
            writeShort(stack.itemID);
            if(large)
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
