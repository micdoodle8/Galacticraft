package codechicken.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.command.CommandHandler;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.ICommand;
import net.minecraft.util.IChatComponent;

public class ServerUtils extends CommonUtils
{
    public static MinecraftServer mc() {
        return MinecraftServer.getServer();
    }

    public static EntityPlayerMP getPlayer(String playername) {
        return mc().getConfigurationManager().func_152612_a(playername);
    }

    public static List<EntityPlayerMP> getPlayers() {
        return mc().getConfigurationManager().playerEntityList;
    }

    public static ArrayList<EntityPlayer> getPlayersInDimension(int dimension) {
        ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
        for (EntityPlayer p : getPlayers())
            if(p.dimension == dimension)
                players.add(p);

        return players;
    }

    public static void openSMPContainer(EntityPlayerMP player, Container container, IGuiPacketSender packetSender) {
        player.getNextWindowId();
        player.closeContainer();
        packetSender.sendPacket(player, player.currentWindowId);
        player.openContainer = container;
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.addCraftingToCrafters(player);
    }

    public static boolean isPlayerOP(String username) {
        return mc().getConfigurationManager().func_152596_g(getPlayer(username).getGameProfile());
    }

    public static boolean isPlayerOwner(String username) {
        return mc().isSinglePlayer() && mc().getServerOwner().equalsIgnoreCase(username);
    }

    public static void registerCommand(ICommand command) {
        ((CommandHandler) mc().getCommandManager()).registerCommand(command);
    }

    public static void sendChatToAll(IChatComponent msg) {
        for(EntityPlayer p : getPlayers())
            p.addChatComponentMessage(msg);
    }
}
