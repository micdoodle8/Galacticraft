package codechicken.nei;

import codechicken.core.ServerUtils;
import codechicken.lib.inventory.InventoryUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.File;

public class PlayerSave
{
    public String username;

    private File saveFile;
    private NBTTagCompound nbt;

    public ItemStack[] creativeInv;
    private boolean creativeInvDirty;

    private boolean isDirty;
    private boolean wasOp;

    public PlayerSave(String playername, File saveLocation) {
        username = playername;
        wasOp = ServerUtils.isPlayerOP(playername);

        saveFile = new File(saveLocation, username + ".dat");
        if (!saveFile.getParentFile().exists())
            saveFile.getParentFile().mkdirs();
        load();
    }

    private void load() {
        nbt = new NBTTagCompound();
        try {
            if (!saveFile.exists())
                saveFile.createNewFile();
            if (saveFile.length() > 0)
                nbt = CompressedStreamTools.read(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadCreativeInv();
    }

    private void loadCreativeInv() {
        creativeInv = new ItemStack[54];
        NBTTagList itemList = nbt.getTagList("creativeitems", 10);
        if (itemList != null)
            InventoryUtils.readItemStacksFromTag(creativeInv, itemList);
    }

    public void save() {
        if (!isDirty)
            return;

        if (creativeInvDirty)
            saveCreativeInv();

        try {
            CompressedStreamTools.write(nbt, saveFile);
            isDirty = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCreativeInv() {
        NBTTagList invsave = InventoryUtils.writeItemStacksToTag(creativeInv);
        nbt.setTag("creativeitems", invsave);

        creativeInvDirty = false;
    }

    public void setCreativeDirty() {
        creativeInvDirty = isDirty = true;
    }

    public void setDirty() {
        isDirty = true;
    }

    public void updateOpChange(EntityPlayerMP player) {
        boolean isOp = ServerUtils.isPlayerOP(username);
        if (isOp != wasOp) {
            NEISPH.sendHasServerSideTo(player);
            wasOp = isOp;
        }
    }

    public boolean isActionEnabled(String name) {
        return getEnabledActions().getBoolean(name);
    }

    private NBTTagCompound getEnabledActions() {
        NBTTagCompound tag = nbt.getCompoundTag("enabledActions");
        if (!nbt.hasKey("enabledActions"))
            nbt.setTag("enabledActions", tag);
        return tag;
    }

    public void enableAction(String name, boolean enabled) {
        getEnabledActions().setBoolean(name, enabled);
        NEISPH.sendActionEnabled(ServerUtils.getPlayer(username), name, enabled);
        setDirty();
    }
}
