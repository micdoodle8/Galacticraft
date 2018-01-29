package codechicken.nei;

import codechicken.lib.util.CommonUtils;
import codechicken.lib.config.ConfigFile;
import codechicken.lib.inventory.InventoryUtils;
import codechicken.lib.packet.PacketCustom;
import codechicken.lib.util.ServerUtils;
import codechicken.nei.network.NEIServerPacketHandler;
import codechicken.nei.util.LogHelper;
import codechicken.nei.util.NEIServerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class NEIServerConfig {
    private static MinecraftServer server;

    public static File saveDir;
    public static ConfigFile serverConfig;
    public static Map<Integer, NBTTagCompound> dimTags = new HashMap<Integer, NBTTagCompound>();
    public static HashMap<String, PlayerSave> playerSaves = new HashMap<String, PlayerSave>();
    public static ItemStackMap<Set<String>> bannedItems = new ItemStackMap<Set<String>>();

    public static void load(World world) {
        if (ServerUtils.mc() != server) {
            LogHelper.debug("Loading NEI Server");
            server = ServerUtils.mc();
            saveDir = new File(DimensionManager.getCurrentSaveRootDirectory(), "NEI");

            dimTags.clear();
            loadConfig();
            loadBannedItems();
        }
        loadWorld(world);
    }

    public static File getSaveDir(World world) {
        return new File(CommonUtils.getSaveLocation(world), "NEI");
    }

    private static void loadWorld(World world) {
        try {
            File file = new File(getSaveDir(world), "world.dat");
            NBTTagCompound tag = NEIServerUtils.readNBT(file);
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            dimTags.put(world.provider.getDimension(), tag);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadConfig() {
        File file = new File(saveDir, "server.cfg");
        serverConfig = new ConfigFile(file);

        serverConfig.setNewLineMode(1);
        serverConfig.getTag("permissions").useBraces();
        serverConfig.getTag("permissions").setComment("List of players who can use these features.\nEg. time=CodeChicken, Friend1");

        setDefaultFeature("time");
        setDefaultFeature("rain");
        setDefaultFeature("heal");
        setDefaultFeature("magnet");
        setDefaultFeature("creative");
        setDefaultFeature("creative+");
        setDefaultFeature("adventure");
        setDefaultFeature("enchant");
        setDefaultFeature("potion");
        setDefaultFeature("save-state");
        setDefaultFeature("item");
        setDefaultFeature("delete");
        setDefaultFeature("notify-item", "CONSOLE, OP");
    }

    private static void setDefaultFeature(String featurename, String... names) {
        if (names.length == 0) {
            names = new String[] { "OP" };
        }

        String list = "";
        for (int i = 0; i < names.length; i++) {
            if (i >= 1) {
                list += ", ";
            }
            list += names[i];
        }
        serverConfig.getTag("permissions." + featurename).setDefaultValue(list);
    }

    private static void saveWorld(int dim) {
        try {
            File file = new File(getSaveDir(DimensionManager.getWorld(dim)), "world.dat");
            NEIServerUtils.writeNBT(dimTags.get(dim), file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean canPlayerPerformAction(String playername, String name) {
        return isPlayerInList(playername, getPlayerList("permissions." + NEIActions.base(name)), true);
    }

    public static boolean isPlayerInList(String playername, Set<String> list, boolean allowCards) {
        if (playername.equals("CONSOLE")) {
            return list.contains(playername);
        }

        playername = playername.toLowerCase();

        if (allowCards) {
            if (list.contains("ALL")) {
                return true;
            }
            if ((ServerUtils.isPlayerOP(playername) || ServerUtils.isPlayerOwner(playername)) && list.contains("OP")) {
                return true;
            }
        }

        return list.contains(playername);
    }

    public static boolean isActionDisabled(int dim, String name) {
        return dimTags.get(dim).getBoolean("disabled" + name);
    }

    public static void disableAction(int dim, String name, boolean disable) {
        dimTags.get(dim).setBoolean("disabled" + name, disable);
        NEIServerPacketHandler.sendActionDisabled(dim, name, disable);
        saveWorld(dim);
    }

    public static HashSet<String> getPlayerList(String tag) {
        String[] list = serverConfig.getTag(tag).getValue("").replace(" ", "").split(",");
        return new HashSet<String>(Arrays.asList(list));
    }

    public static void addPlayerToList(String playername, String tag) {
        HashSet<String> list = getPlayerList(tag);

        if (!playername.equals("CONSOLE") && !playername.equals("ALL") && !playername.equals("OP")) {
            playername = playername.toLowerCase();
        }

        list.add(playername);
        savePlayerList(tag, list);
    }

    public static void remPlayerFromList(String playername, String tag) {
        HashSet<String> list = getPlayerList(tag);

        if (!playername.equals("CONSOLE") && !playername.equals("ALL") && !playername.equals("OP")) {
            playername = playername.toLowerCase();
        }

        list.remove(playername);
        savePlayerList(tag, list);
    }

    private static void savePlayerList(String tag, Collection<String> list) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); i++) {
            if (i != 0) {
                sb.append(", ");
            }

            sb.append(iterator.next());
        }

        serverConfig.getTag(tag).setValue(sb.toString());
    }

    private static void loadBannedItems() {
        bannedItems.clear();
        File file = new File(saveDir, "banneditems.cfg");
        if (!file.exists()) {
            bannedItems.put(new ItemStack(Blocks.COMMAND_BLOCK), new HashSet<String>(Arrays.asList("NONE")));
            saveBannedItems();
            return;
        }
        try {
            FileReader r = new FileReader(file);
            int line = 0;
            for (String s : IOUtils.readLines(r)) {
                if (s.charAt(0) == '#' || s.trim().length() == 0) {
                    continue;
                }
                int delim = s.lastIndexOf('=');
                if (delim < 0) {
                    LogHelper.error("line %s: Missing =", line);
                    continue;
                }
                try {
                    NBTTagCompound key = JsonToNBT.getTagFromJson(s.substring(0, delim));
                    Set<String> values = new HashSet<String>();
                    for (String s2 : s.substring(delim + 1).split(",")) {
                        values.add(s2.trim());
                    }
                    bannedItems.put(InventoryUtils.loadPersistant(key), values);
                } catch (Exception e) {
                    LogHelper.error("line %s: %s", line, e.getMessage());
                }
            }
            r.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveBannedItems() {
        File file = new File(saveDir, "banneditems.cfg");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            PrintWriter p = new PrintWriter(file);
            p.println("#Saved in this format for external editing. The format isn't that hard to figure out. If you think you're up to it, modify it here!");

            for (ItemStackMap.Entry<Set<String>> entry : bannedItems.entries()) {
                NBTTagCompound key = InventoryUtils.savePersistant(entry.key, new NBTTagCompound());
                key.removeTag("Count");
                if (key.getByte("Damage") == 0) {
                    key.removeTag("Damage");
                }

                p.print(key.toString());
                p.print("=[");
                int i = 0;
                for (String s : entry.value) {
                    if (i++ != 0) {
                        p.print(", ");
                    }
                    p.print(s);
                }
                p.println("]");
            }
            p.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerSave forPlayer(String username) {
        return playerSaves.get(username);
    }

    public static void loadPlayer(EntityPlayer player) {
        LogHelper.debug("Loading Player: " + player.getGameProfile().getName());
        playerSaves.put(player.getName(), new PlayerSave((EntityPlayerMP) player, new File(saveDir, "players")));
    }

    public static void unloadPlayer(EntityPlayer player) {
        LogHelper.debug("Unloading Player: " + player.getName());
        PlayerSave playerSave = playerSaves.remove(player.getName());
        if (playerSave != null) {
            playerSave.save();
        }
    }

    public static boolean authenticatePacket(EntityPlayerMP sender, PacketCustom packet) {
        switch (packet.getType()) {
        case 1:
            return canPlayerPerformAction(sender.getName(), "item");
        case 4:
            return canPlayerPerformAction(sender.getName(), "delete");
        case 6:
            return canPlayerPerformAction(sender.getName(), "magnet");
        case 7:
            return canPlayerPerformAction(sender.getName(), "time");
        case 8:
            return canPlayerPerformAction(sender.getName(), "heal");
        case 9:
            return canPlayerPerformAction(sender.getName(), "rain");
        case 14:
        case 23:
            return canPlayerPerformAction(sender.getName(), "creative+");
        case 21:
        case 22:
            return canPlayerPerformAction(sender.getName(), "enchant");
        case 24:
            return canPlayerPerformAction(sender.getName(), "potion");
        }
        return true;
    }
}
