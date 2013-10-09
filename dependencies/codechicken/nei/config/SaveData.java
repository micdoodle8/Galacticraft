package codechicken.nei.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.lib.config.ConfigTagParent;

public class SaveData
{
    public ConfigTagParent config;
    public NBTTagCompound nbt;
    private File nbtFile;
    
    public SaveData(ConfigTagParent config, File nbtFile)
    {
        this.config = config;
        this.nbtFile = nbtFile;
        
        loadNBT();
    }

    public void loadNBT()
    {
        try
        {
            if(!nbtFile.exists())
                nbtFile.createNewFile();
            if(nbtFile.length() == 0)
                return;
            
            DataInputStream din = new DataInputStream(new BufferedInputStream(new FileInputStream(nbtFile)));
            nbt = (NBTTagCompound) NBTBase.readNamedTag(din);
            din.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public void saveNBT()
    {
        try
        {
            DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(nbtFile)));
            NBTBase.writeNamedTag(nbt, dout);
            dout.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
