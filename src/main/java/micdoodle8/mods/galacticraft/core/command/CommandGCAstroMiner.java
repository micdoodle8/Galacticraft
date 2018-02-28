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
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.List;

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
    public String getCommandName()
    {
        return "gcastrominer";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "show", "set", "reset");
        }
        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, this.getPlayers());
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
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 2)
        {
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.dimensiontp.too_many", this.getCommandUsage(sender)), new Object[0]);
        }
        if (args.length < 1)
        {
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssinvite.wrong_usage", this.getCommandUsage(sender)), new Object[0]);
        }

        int type = 0;
        int newvalue = 0;
        if (args[0].equalsIgnoreCase("show"))
        {
            type = 1;
        }
        else if (args[0].equalsIgnoreCase("reset"))
        {
            type = 2;
        }
        else if (args[0].length() > 3 && args[0].substring(0, 3).equalsIgnoreCase("set"))
        {
            String number = args[0].substring(3);
            try
            {
                newvalue = Integer.parseInt(number);
                if (newvalue > 0)
                {
                    type = 3;
                }
            }
            catch (NumberFormatException ex)
            {
            }
        }

        //Proceed if syntax of show|reset|set<number> was correct
        if (type > 0)
        {
            EntityPlayerMP playerBase = null;
            try
            {
                if (args.length == 2)
                {
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(args[1], true);
                }
                else
                {
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
                }

                if (playerBase != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(playerBase);
                    switch (type)
                    {
                    case 1:
                        sender.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("command.gcastrominer.count", PlayerUtil.getName(playerBase), "" + stats.getAstroMinerCount())));
                        break;
                    case 2:
                        stats.setAstroMinerCount(0);
                        sender.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("command.gcastrominer.count", PlayerUtil.getName(playerBase), "" + 0)));
                        break;
                    case 3:
                        stats.setAstroMinerCount(newvalue);
                        sender.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("command.gcastrominer.count", PlayerUtil.getName(playerBase), "" + newvalue)));
                        break;
                    }
                }
                else
                {
                    throw new Exception("Could not find player with name: " + args[1]);
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
