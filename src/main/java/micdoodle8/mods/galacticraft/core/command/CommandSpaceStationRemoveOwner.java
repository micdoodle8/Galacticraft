package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandSpaceStationRemoveOwner extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " [player]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }

    @Override
    public String getCommandName()
    {
        return "ssuninvite";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        String var3 = null;
        EntityPlayerMP playerBase = null;

        if (astring.length > 0)
        {
            var3 = astring[0];

            try
            {
                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), false);

                if (playerBase != null)
                {
                    GCPlayerStats stats = GCEntityPlayerMP.getPlayerStats(playerBase);

                    if (stats.spaceStationDimensionID <= 0)
                    {
                        throw new WrongUsageException("Could not find space station for your username, you need to travel there first!", new Object[0]);
                    }
                    else
                    {
                        final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, stats.spaceStationDimensionID, playerBase);

                        if (data.getAllowedPlayers().contains(var3.toLowerCase()))
                        {
                            data.getAllowedPlayers().remove(var3.toLowerCase());
                            data.markDirty();
                        }
                        else
                        {
                            throw new CommandException("Couldn't find player with username \"" + var3 + "\" on your Space Station list!", new Object[0]);
                        }
                    }
                }
            }
            catch (final Exception var6)
            {
                throw new CommandException(var6.getMessage(), new Object[0]);
            }

        }
        else
        {
            throw new WrongUsageException("Not enough command arguments! Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
        }

        if (playerBase != null)
        {
            playerBase.addChatMessage(new ChatComponentText("Successfully removed " + var3 + " from Space Station list!"));
        }
    }


    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, this.getPlayers(par1ICommandSender)) : null;
    }

    protected String[] getPlayers(ICommandSender icommandsender)
    {
        EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), false);

        if (playerBase != null)
        {
            GCPlayerStats stats = GCEntityPlayerMP.getPlayerStats(playerBase);
            int ssdim = stats.spaceStationDimensionID;
            if (ssdim > 0)
            {
                final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, ssdim, playerBase);
                String[] allNames = MinecraftServer.getServer().getAllUsernames();
                ArrayList<String> allowedNames = data.getAllowedPlayers();
                Iterator<String> itName = allowedNames.iterator();
                ArrayList<String> replaceNames = new ArrayList();
                while (itName.hasNext())
                {
                    String name = itName.next();
                    for (int j = 0; j < allNames.length; j++)
                    {
                        if (name.equalsIgnoreCase(allNames[j]))
                        {
                            itName.remove();
                            replaceNames.add(allNames[j]);
                        }
                    }
                }
                allowedNames.addAll(replaceNames);
                String[] rvsize = new String[allowedNames.size()];
                return allowedNames.toArray(rvsize);
            }
        }

        String[] returnvalue = { "" };
        return returnvalue;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 0;
    }
}
