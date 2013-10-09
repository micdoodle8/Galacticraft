package codechicken.nei.config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import codechicken.lib.config.ConfigTagParent;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class ConfigSet
{
    private File nbtFile;
    public NBTTagCompound nbt;
    public ConfigTagParent config;
    
    public ConfigSet(File nbtFile, ConfigTagParent config)
    {
        this.nbtFile = nbtFile;
        this.config = config;
        loadNBT();
    }
    
    public void loadNBT()
    {
        nbt = new NBTTagCompound();
        try
        {
            if(!nbtFile.exists())
                nbtFile.createNewFile();
            if(nbtFile.length() > 0)
            {
                DataInputStream din = new DataInputStream(new FileInputStream(nbtFile));
                nbt = (NBTTagCompound) NBTBase.readNamedTag(din);
                din.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void saveNBT()
    {
        try
        {
            DataOutputStream dout = new DataOutputStream(new FileOutputStream(nbtFile));
            NBTBase.writeNamedTag(nbt, dout);
            dout.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
