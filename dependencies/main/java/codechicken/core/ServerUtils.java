package codechicken.core;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache.ProfileEntry;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServerUtils extends CommonUtils {
    public static MinecraftServer mc() {
        return MinecraftServer.getServer();
    }

    public static EntityPlayerMP getPlayer(String playername) {
        return mc().getConfigurationManager().getPlayerByUsername(playername);
    }

    public static List<EntityPlayerMP> getPlayers() {
        return mc().getConfigurationManager().playerEntityList;
    }

    public static ArrayList<EntityPlayer> getPlayersInDimension(int dimension) {
        ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
        for (EntityPlayer p : getPlayers()) {
            if (p.dimension == dimension) {
                players.add(p);
            }
        }

        return players;
    }

    public static void openSMPContainer(EntityPlayerMP player, Container container, IGuiPacketSender packetSender) {
        player.getNextWindowId();
        player.closeContainer();
        packetSender.sendPacket(player, player.currentWindowId);
        player.openContainer = container;
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.onCraftGuiOpened(player);
    }

    public static GameProfile getGameProfile(String username) {
        EntityPlayer player = getPlayer(username);
        if (player != null) {
            return player.getGameProfile();
        }

        //try and access it in the cache without forcing a save
        username = username.toLowerCase(Locale.ROOT);
        ProfileEntry cachedEntry = (ProfileEntry) mc().getPlayerProfileCache().usernameToProfileEntryMap.get(username);
        if (cachedEntry != null) {
            return cachedEntry.getGameProfile();
        }

        //load it from the cache
        return mc().getPlayerProfileCache().getGameProfileForUsername(username);
    }

    public static boolean isPlayerOP(String username) {
        GameProfile prof = getGameProfile(username);
        return prof != null && mc().getConfigurationManager().canSendCommands(prof);
    }

    public static boolean isPlayerOwner(String username) {
        return mc().isSinglePlayer() && mc().getServerOwner().equalsIgnoreCase(username);
    }

    public static void sendChatToAll(IChatComponent msg) {
        for (EntityPlayer p : getPlayers()) {
            p.addChatComponentMessage(msg);
        }
    }
}
