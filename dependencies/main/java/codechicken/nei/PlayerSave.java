package codechicken.nei;

import codechicken.lib.inventory.InventoryUtils;
import codechicken.lib.util.ServerUtils;
import codechicken.nei.network.NEIServerPacketHandler;
import codechicken.nei.util.LogHelper;
import codechicken.nei.util.NEIServerUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.File;
import java.util.HashSet;

public class PlayerSave {
    public EntityPlayerMP player;

    private File saveFile;
    private NBTTagCompound nbt;

    public ItemStack[] creativeInv;
    private boolean creativeInvDirty;

    private boolean isDirty;
    private boolean wasOp;

    //runtime things
    public HashSet<EntityItem> magneticItems = new HashSet<EntityItem>();

    public PlayerSave(EntityPlayerMP player, File saveLocation) {
        this.player = player;
        wasOp = ServerUtils.mc().getPlayerList().canSendCommands(player.getGameProfile());

        saveFile = new File(saveLocation, player.getName() + ".dat");
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        load();
    }

    private void load() {
        nbt = new NBTTagCompound();
        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            if (saveFile.length() > 0) {
                nbt = NEIServerUtils.readNBT(saveFile);
            }
        } catch (Exception e) {
            LogHelper.errorError("Error loading player save: " + player, e);
        }

        loadCreativeInv();
    }

    private void loadCreativeInv() {
        creativeInv = new ItemStack[54];
        NBTTagList itemList = nbt.getTagList("creativeitems", 10);
        if (itemList != null) {
            InventoryUtils.readItemStacksFromTag(creativeInv, itemList);
        }
    }

    public void save() {
        if (!isDirty) {
            return;
        }

        if (creativeInvDirty) {
            saveCreativeInv();
        }

        try {
            NEIServerUtils.writeNBT(nbt, saveFile);
            isDirty = false;
        } catch (Exception e) {
            LogHelper.errorError("Error saving player: " + player, e);
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

    public void updateOpChange() {
        boolean isOp = ServerUtils.mc().getPlayerList().canSendCommands(player.getGameProfile());
        if (isOp != wasOp) {
            NEIServerPacketHandler.sendHasServerSideTo(player);
            wasOp = isOp;
        }
    }

    public boolean isActionEnabled(String name) {
        return getEnabledActions().getBoolean(name);
    }

    private NBTTagCompound getEnabledActions() {
        NBTTagCompound tag = nbt.getCompoundTag("enabledActions");
        if (!nbt.hasKey("enabledActions")) {
            nbt.setTag("enabledActions", tag);
        }
        return tag;
    }

    public void enableAction(String name, boolean enabled) {
        getEnabledActions().setBoolean(name, enabled);
        NEIServerPacketHandler.sendActionEnabled(player, name, enabled);
        setDirty();
    }

    public void onWorldReload() {
        NEIServerPacketHandler.sendHasServerSideTo(player);
        magneticItems.clear();
    }
}
