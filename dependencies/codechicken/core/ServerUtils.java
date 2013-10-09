package codechicken.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import codechicken.lib.packet.PacketCustom;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.command.CommandHandler;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.ICommand;
import net.minecraft.network.packet.Packet3Chat;

public class ServerUtils extends CommonUtils
{
    public static MinecraftServer mc()
    {
        return MinecraftServer.getServer();
    }

    public static EntityPlayerMP getPlayer(String playername)
    {
        return mc().getConfigurationManager().getPlayerForUsername(playername);
    }
    
    public static ArrayList<EntityPlayer> getAllPlayers()
    {
        return new ArrayList<EntityPlayer>(mc().getConfigurationManager().playerEntityList);
    }

    public static ArrayList<EntityPlayer> getPlayersInDimension(int dimension)
    {
        ArrayList<EntityPlayer> allplayers = getAllPlayers();
        for(Iterator<EntityPlayer> iterator = allplayers.iterator(); iterator.hasNext();)
            if(iterator.next().dimension != dimension)
                iterator.remove();
        
        return allplayers;
    }
    
    public static void sendChatToOps(String message)
    {
        List<String> lines = splitChat(message);
        for(String s : lines)
            PacketCustom.sendToOps(new Packet3Chat(ChatMessageComponent.createFromText(s)));
    }

    public static void sendChatToAll(String message)
    {
        List<String> lines = splitChat(message);
        for(String s : lines)
            PacketCustom.sendToClients(new Packet3Chat(ChatMessageComponent.createFromText(s)));
    }

    public static void sendChatTo(EntityPlayerMP player, String message)
    {
        List<String> lines = splitChat(message);
        for(String s : lines)
            PacketCustom.sendToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(s)), player);
    }
    
    public static void openSMPContainer(EntityPlayerMP player, Container container, IGuiPacketSender packetSender)
    {
        player.incrementWindowID();
        player.closeContainer();
        packetSender.sendPacket(player, player.currentWindowId);        
        player.openContainer = container; 
        player.openContainer.windowId = player.currentWindowId;        
        player.openContainer.addCraftingToCrafters(player);
    }

    public static boolean isPlayerOP(String username)
    {
        return mc().getConfigurationManager().isPlayerOpped(username);
    }
    
    public static boolean isPlayerOwner(String username)
    {
        return mc().isSinglePlayer() && mc().getServerOwner().equalsIgnoreCase(username);
    }
    
    public static void registerCommand(ICommand command)
    {
        ((CommandHandler)mc().getCommandManager()).registerCommand(command);
    }
}
