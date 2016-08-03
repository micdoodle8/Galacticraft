package codechicken.core.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public abstract class ServerCommand extends CoreCommand {
    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        handleCommand(var2, (MinecraftServer) var1);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1) {
        return super.canCommandSenderUseCommand(var1) && var1 instanceof MinecraftServer;
    }

    public abstract void handleCommand(String[] args, MinecraftServer listener);

    public final boolean OPOnly() {
        return false;
    }
}
