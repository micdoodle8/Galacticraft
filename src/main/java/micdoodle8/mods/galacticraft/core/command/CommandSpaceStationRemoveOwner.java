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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.*;

public class CommandSpaceStationRemoveOwner extends CommandBase
{
    @Override
    public String getUsage(ICommandSender var1)
    {
        return "/" + this.getName() + " <player>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "ssuninvite";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String var3 = null;
        EntityPlayerMP playerBase = null;

        if (args.length > 0)
        {
            var3 = args[0];

            try
            {
                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), false);

                if (playerBase != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(playerBase);

                    if (stats.getSpaceStationDimensionData().isEmpty())
                    {
                        throw new WrongUsageException(GCCoreUtil.translate("commands.ssinvite.not_found"), new Object[0]);
                    }
                    else
                    {
                        for (Map.Entry<Integer, Integer> e : stats.getSpaceStationDimensionData().entrySet())
                        {
                            final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.world, e.getValue(), playerBase);

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
                                throw new CommandException(GCCoreUtil.translateWithFormat("commands.ssuninvite.no_player", "\"" + var3 + "\""), new Object[0]);
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
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssinvite.wrong_usage", this.getUsage(sender)), new Object[0]);
        }

        if (playerBase != null)
        {
            playerBase.sendMessage(new TextComponentString(GCCoreUtil.translateWithFormat("gui.spacestation.removesuccess", var3)));
        }
    }


    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getPlayers(server, sender)) : null;
    }

    protected String[] getPlayers(MinecraftServer server, ICommandSender sender)
    {
        EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), false);

        if (playerBase != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(playerBase);
            if (!stats.getSpaceStationDimensionData().isEmpty())
            {
                String[] allNames = server.getOnlinePlayerNames();
                //data.getAllowedPlayers may include some in lowercase
                //Convert to correct case at least for those players who are online
                HashSet<String> allowedNames = Sets.newHashSet();

                for (Map.Entry<Integer, Integer> e : stats.getSpaceStationDimensionData().entrySet())
                {
                    final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.world, e.getValue(), playerBase);
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
