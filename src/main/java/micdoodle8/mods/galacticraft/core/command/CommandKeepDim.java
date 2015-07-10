package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class CommandKeepDim extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getName() + " <dimension id>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getName()
    {
        return "gckeeploaded";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP playerBase;

        if (args.length > 1)
        {
            throw new WrongUsageException("Too many command arguments! Usage: " + this.getCommandUsage(sender), new Object[0]);
        }
        else
        {
            try
            {
                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);

                if (playerBase != null)
                {
                    int dimID;

                    if (args.length == 0)
                    {
                        dimID = playerBase.dimension;
                    }
                    else
                    {
                    	try {
                    		dimID = CommandBase.parseInt(args[0]);
                    	} catch (Exception e) { throw new WrongUsageException("Needs a dimension number! Usage: " + this.getCommandUsage(sender), new Object[0]); }
                    }

                    if (ConfigManagerCore.setLoaded(dimID))
                    {
                        playerBase.addChatMessage(new ChatComponentText("[GCKeepLoaded] Successfully set dimension " + dimID + " to load staticly"));
                    }
                    else
                    {
                        if (ConfigManagerCore.setUnloaded(dimID))
                        {
                            playerBase.addChatMessage(new ChatComponentText("[GCKeepLoaded] Successfully set dimension " + dimID + " to not load staticly"));
                        }
                        else
                        {
                            playerBase.addChatMessage(new ChatComponentText("[GCKeepLoaded] Failed to set dimension as not static"));
                        }
                    }
                }
            }
            catch (final Exception var6)
            {
                throw new CommandException(var6.getMessage(), new Object[0]);
            }
        }
    }
}
