package codechicken.nei;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import codechicken.core.ServerUtils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerSave
{
    public String username;
    
    private File saveFile;
    private NBTTagCompound nbt;
    
    public ItemStack[] creativeInv;
    private boolean creativeInvDirty;
    
    private boolean isDirty;
    private boolean wasOp;
    
    public PlayerSave(String playername, File saveLocation)
    {
        username = playername;
        wasOp = ServerUtils.isPlayerOP(playername);

        saveFile = new File(saveLocation, username+".dat");
        if(!saveFile.getParentFile().exists())
            saveFile.getParentFile().mkdirs();
        load();
    }

    private void load()
    {
        nbt = new NBTTagCompound();
        try
        {
            if(!saveFile.exists())
                saveFile.createNewFile();
            if(saveFile.length() > 0)
            {
                DataInputStream din = new DataInputStream(new FileInputStream(saveFile));
                nbt = (NBTTagCompound) NBTBase.readNamedTag(din);
                din.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        loadCreativeInv();
    }

    private void loadCreativeInv()
    {
        creativeInv = new ItemStack[54];
        NBTTagList itemList = nbt.getTagList("creativeitems");
        if(itemList != null)
        {
            for(int tagPos = 0; tagPos < itemList.tagCount(); tagPos++)
            {
                NBTTagCompound stacksave = (NBTTagCompound)itemList.tagAt(tagPos);

                creativeInv[stacksave.getByte("Slot") & 0xff] = ItemStack.loadItemStackFromNBT(stacksave);
            }
        }
    }

    public void save()
    {
        if(!isDirty)
            return;

        if(creativeInvDirty)
            saveCreativeInv();

        try
        {
            DataOutputStream dout = new DataOutputStream(new FileOutputStream(saveFile));
            NBTBase.writeNamedTag(nbt, dout);
            dout.close();
            isDirty = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void saveCreativeInv()
    {
        NBTTagList invsave = new NBTTagList();
        for(int i = 0; i < creativeInv.length; i++)
        {
            if(creativeInv[i] != null)
            {
                NBTTagCompound stacksave = new NBTTagCompound();
                stacksave.setByte("Slot", (byte)i);
                creativeInv[i].writeToNBT(stacksave);
                invsave.appendTag(stacksave);
            }
        }
        nbt.setTag("creativeitems", invsave);

        creativeInvDirty = false;
    }

    public void setCreativeDirty()
    {
        creativeInvDirty = isDirty = true;
    }

    public void setDirty()
    {
        isDirty = true;
    }
    
    public void updateOpChange(EntityPlayerMP player)
    {
        boolean isOp = ServerUtils.isPlayerOP(username);
        if(isOp != wasOp)
        {
            NEISPH.sendHasServerSideTo(player);
            wasOp = isOp;
        }
    }
    
    public boolean isActionEnabled(String name)
    {
        return getEnabledActions().getBoolean(name);
    }
    
    private NBTTagCompound getEnabledActions()
    {
        NBTTagCompound tag = nbt.getCompoundTag("enabledActions");
        if(!nbt.hasKey("enabledActions"))
            nbt.setCompoundTag("enabledActions", tag);
        return tag;
    }
    
    public void enableAction(String name, boolean enabled)
    {
        getEnabledActions().setBoolean(name, enabled);
        NEISPH.sendActionEnabled(ServerUtils.getPlayer(username), name, enabled);
        setDirty();
    }
}
