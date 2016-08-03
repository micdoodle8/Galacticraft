package codechicken.core.commands;

import codechicken.core.ServerUtils;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

public abstract class CoreCommand implements ICommand {
    public static void chatT(ICommandSender sender, String s, Object... params) {
        sender.addChatMessage(new ChatComponentTranslation(s, params));
    }

    public static void chatOpsT(String s, Object... params) {
        for (EntityPlayerMP player : ServerUtils.getPlayers()) {
            if (MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile())) {
                player.addChatMessage(new ChatComponentTranslation(s, params));
            }
        }
    }

    public abstract boolean OPOnly();

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/" + getCommandName() + " help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < minimumParameters() || args.length == 1 && args[0].equals("help")) {
            printHelp(sender);
            return;
        }

        String command = getCommandName();
        for (String arg : args) {
            command += " " + arg;
        }

        handleCommand(command, sender.getName(), args, sender);
    }

    public abstract void handleCommand(String command, String playername, String[] args, ICommandSender listener);

    public abstract void printHelp(ICommandSender sender);

    public final EntityPlayerMP getPlayer(String name) {
        return ServerUtils.getPlayer(name);
    }

    public WorldServer getWorld(int dimension) {
        return DimensionManager.getWorld(dimension);
    }

    public WorldServer getWorld(EntityPlayer player) {
        return (WorldServer) player.worldObj;
    }

    @Override
    public int compareTo(ICommand o) {
        return getCommandName().compareTo(o.getCommandName());
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>();
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1) {
        if (OPOnly()) {
            if (var1 instanceof EntityPlayer) {
                return MinecraftServer.getServer().getConfigurationManager().canSendCommands(((EntityPlayer) var1).getGameProfile());
            }

            return var1 instanceof MinecraftServer;
        }
        return true;
    }

    public abstract int minimumParameters();
}
