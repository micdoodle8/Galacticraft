package micdoodle8.mods.galacticraft.core.command;

import com.google.common.collect.Sets;
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

import java.util.*;

public class CommandSpaceStationRemoveOwner extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " <player>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
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

                    if (stats.spaceStationDimensionData.isEmpty())
                    {
                        throw new WrongUsageException(GCCoreUtil.translate("commands.ssinvite.notFound"), new Object[0]);
                    }
                    else
                    {
                        for (Map.Entry<Integer, Integer> e : stats.spaceStationDimensionData.entrySet())
                        {
                            final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, e.getValue(), playerBase);

                            String str = null;
                            for (String name : data.getAllowedPlayers())
                            {
                                if (name.equalsIgnoreCase(var3))
                                {
                                    str = name;
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
            if (!stats.spaceStationDimensionData.isEmpty())
            {
                String[] allNames = MinecraftServer.getServer().getAllUsernames();
                //data.getAllowedPlayers may include some in lowercase
                //Convert to correct case at least for those players who are online
                HashSet<String> allowedNames = Sets.newHashSet();

                for (Map.Entry<Integer, Integer> e : stats.spaceStationDimensionData.entrySet())
                {
                    final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, e.getValue(), playerBase);
                    allowedNames.addAll(data.getAllowedPlayers());
                }

                Iterator<String> itName = allowedNames.iterator();
                ArrayList<String> replaceNames = new ArrayList<String>();
                while (itName.hasNext())
                {
                    String name = itName.next();
                    for (String allEntry : allNames)
                    {
                        if (name.equalsIgnoreCase(allEntry))
                        {
                            itName.remove();
                            replaceNames.add(allEntry);
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
