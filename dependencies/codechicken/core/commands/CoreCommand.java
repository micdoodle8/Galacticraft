package codechicken.core.commands;

import java.util.List;

import codechicken.core.ServerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public abstract class CoreCommand implements ICommand
{    
    public class WCommandSender implements ICommandSender
    {
        public ICommandSender wrapped;
        
        public WCommandSender(ICommandSender sender)
        {
            wrapped = sender;
        }
        
        
        @Override
        public String getCommandSenderName()
        {
            return wrapped.getCommandSenderName();
        }

        @Override
        public void sendChatToPlayer(ChatMessageComponent string)
        {
            wrapped.sendChatToPlayer(string);
        }

        public void sendChatToPlayer(String string)
        {
            wrapped.sendChatToPlayer(ChatMessageComponent.createFromText(string));
        }

        @Override
        public boolean canCommandSenderUseCommand(int i, String s)
        {
            return wrapped.canCommandSenderUseCommand(i, s);
        }

        @Override
        public ChunkCoordinates getPlayerCoordinates()
        {
            return wrapped.getPlayerCoordinates();
        }

        @Override
        public World getEntityWorld()
        {
            return wrapped.getEntityWorld();
        }
    }
    
    public abstract boolean OPOnly();
    
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + getCommandName() + " help";
    }
    
    @Override
    public void processCommand(ICommandSender listener, String[] args)
    {
        WCommandSender wsender = new WCommandSender(listener);
        
        if(args.length < minimumParameters() ||
                args.length == 1 && args[0].equals("help"))
        {
            printHelp(wsender);
            return;
        }
        
        String command = getCommandName();
        for(String arg : args)
            command+=" "+arg;
        
        handleCommand(command, wsender.getCommandSenderName(), args, wsender);
    }
    
    public abstract void handleCommand(String command, String playername, String[] args, WCommandSender listener);
    
    public abstract void printHelp(WCommandSender listener);
    
    public final EntityPlayerMP getPlayer(String name)
    {
        return ServerUtils.getPlayer(name);
    }
    
    public WorldServer getWorld(int dimension)
    {
        return DimensionManager.getWorld(dimension);
    }

    public WorldServer getWorld(EntityPlayer player)
    {
        return (WorldServer) player.worldObj;
    }

    public Integer parseInteger(String parse)
    {
        try
        {
            return Integer.parseInt(parse);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }

    @Override
    public int compareTo(Object arg0)
    {
        return getCommandName().compareTo(((ICommand)arg0).getCommandName());
    }

    @Override
    public List<?> getCommandAliases()
    {
        return null;
    }

    @Override
    public List<?> addTabCompletionOptions(ICommandSender var1, String[] var2)
    {
        return null;
    }
    
    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1)
    {
        if(OPOnly())
        {
            if(var1 instanceof EntityPlayer)
                return ServerUtils.isPlayerOP(var1.getCommandSenderName());
            else if(var1 instanceof MinecraftServer)
                return true;
            else
                return false;
        }
        return true;
    }

    
    public abstract int minimumParameters();
}
