package codechicken.core.commands;

import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;

public abstract class ServerCommand extends CoreCommand
{
    @Override
    public void processCommand(ICommandSender var1, String[] var2)
    {        
        handleCommand(var2, (MinecraftServer)var1);
    }
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1)
    {
        if(!super.canCommandSenderUseCommand(var1))
            return false;
        return var1 instanceof MinecraftServer;
    }
    
    public abstract void handleCommand(String[] args, MinecraftServer listener);

    public final boolean OPOnly()
    {
        return false;
    }    
}
