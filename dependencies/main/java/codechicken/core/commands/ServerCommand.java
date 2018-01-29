package codechicken.core.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public abstract class ServerCommand extends CoreCommand {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        handleCommand(args, (MinecraftServer) sender);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return super.checkPermission(server, sender) && sender instanceof MinecraftServer;
    }

    public abstract void handleCommand(String[] args, MinecraftServer listener);

    public final boolean isOpOnly() {
        return false;
    }
}
