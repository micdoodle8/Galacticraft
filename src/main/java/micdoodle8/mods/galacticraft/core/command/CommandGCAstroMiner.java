package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import java.util.*;

public class CommandGCAstroMiner extends CommandBase
{

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " [show|reset|set<number>] <playername>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }

    @Override
    public String getCommandName()
    {
        return "gcastrominer";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length == 1)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, "show", "set", "reset");
        }
        if (par2ArrayOfStr.length == 2)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, this.getPlayers());
        }
        return null;
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 1;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
    	if (astring.length > 2)
    	{
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.dimensiontp.tooMany", this.getCommandUsage(icommandsender)), new Object[0]);
    	}
    	if (astring.length < 1)
    	{
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssinvite.wrongUsage", this.getCommandUsage(icommandsender)), new Object[0]);
    	}
    	
    	int type = 0;
    	int newvalue = 0;
    	if (astring[0].equalsIgnoreCase("show")) type = 1;
    	else if (astring[0].equalsIgnoreCase("reset")) type = 2;
    	else if (astring[0].length() > 3 && astring[0].substring(0,3).equalsIgnoreCase("set"))
    	{
    		String number = astring[0].substring(3);
            try
            {
                newvalue = Integer.parseInt(number);
        		if (newvalue > 0)
        			type = 3;
            }
            catch (NumberFormatException ex) { }
    	}
    	
    	//Proceed if syntax of show|reset|set<number> was correct
    	if (type > 0)
    	{
    		EntityPlayerMP playerBase = null;
            try
            {
                if (astring.length == 2)
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(astring[1], true);
                else
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), true);

                if (playerBase != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(playerBase);
                    switch (type)
                    {
                    case 1: 
                    	icommandsender.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("command.gcastrominer.count", playerBase.getGameProfile().getName(), "" + stats.astroMinerCount)));
                    	break;
                    case 2:
                    	stats.astroMinerCount = 0;
                    	icommandsender.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("command.gcastrominer.count", playerBase.getGameProfile().getName(), "" + 0)));
                    	break;
                    case 3:
                    	stats.astroMinerCount = newvalue;
                    	icommandsender.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("command.gcastrominer.count", playerBase.getGameProfile().getName(), "" + newvalue)));
                    	break;
                    }
                }
                else
                {
                    throw new Exception("Could not find player with name: " + astring[1]);
                }
            }
            catch (final Exception e)
            {
                throw new CommandException(e.getMessage(), new Object[0]);
            }
            return;
        }
    }
}
