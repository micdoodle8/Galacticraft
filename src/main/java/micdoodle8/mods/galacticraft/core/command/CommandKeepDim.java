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
        return "/" + this.getCommandName();
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandName()
    {
        return "gckeeploaded";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        EntityPlayerMP playerBase;

        if (astring.length > 1)
        {
            throw new WrongUsageException("Not enough command arguments! Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
        }
        else
        {
            try
            {
                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), true);

                if (playerBase != null)
                {
                    int dimID;

                    if (astring.length == 0)
                    {
                        dimID = playerBase.dimension;
                    }
                    else
                    {
                        dimID = CommandBase.parseInt(icommandsender, astring[0]);
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
