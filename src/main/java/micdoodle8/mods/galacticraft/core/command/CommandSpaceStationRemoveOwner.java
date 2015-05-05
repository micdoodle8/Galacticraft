package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandSpaceStationRemoveOwner extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " <player>";
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
                    GCPlayerStats stats = GCPlayerStats.get(playerBase);

                    if (stats.spaceStationDimensionID <= 0)
                    {
                        throw new WrongUsageException(GCCoreUtil.translate("commands.ssinvite.notFound"), new Object[0]);
                    }
                    else
                    {
                        final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, stats.spaceStationDimensionID, playerBase);

                        String str = null;
                        for (String player : data.getAllowedPlayers())
                        {
                            if (player.equalsIgnoreCase(var3))
                            {
                                str = player;
                                break;
                            }
                        }

                        if (str != null)
                        {
                            data.getAllowedPlayers().remove(str);
                            data.markDirty();
                        }
                        else
                        {
                            throw new CommandException(GCCoreUtil.translateWithFormat("commands.ssuninvite.noPlayer", "\"" + var3 + "\""), new Object[0]);
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
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssinvite.wrongUsage", this.getCommandUsage(icommandsender)), new Object[0]);
        }

        if (playerBase != null)
        {
            playerBase.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.spacestation.removesuccess", var3)));
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
            GCPlayerStats stats = GCPlayerStats.get(playerBase);
            int ssdim = stats.spaceStationDimensionID;
            if (ssdim > 0)
            {
                final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, ssdim, playerBase);
                String[] allNames = MinecraftServer.getServer().getAllUsernames();
                //data.getAllowedPlayers may include some in lowercase
                //Convert to correct case at least for those players who are online
                ArrayList<String> allowedNames = new ArrayList(data.getAllowedPlayers());
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
                //This does the conversion to correct case
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
