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
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.List;
import java.util.Map;

public class CommandSpaceStationAddOwner extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " [ <player> | +all | -all ]";
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
        return "ssinvite";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        String var3 = null;
        EntityPlayerMP playerBase = null;

        if (args.length > 0)
        {
            var3 = args[0];

            try
            {
                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);

                if (playerBase != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(playerBase);

                    if (stats.getSpaceStationDimensionData().isEmpty())
                    {
                        throw new WrongUsageException(GCCoreUtil.translate("commands.ssinvite.not_found"), new Object[0]);
                    }
                    else
                    {
                        for (Map.Entry<Integer, Integer> ownedStations : stats.getSpaceStationDimensionData().entrySet())
                        {
                            final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, ownedStations.getValue(), playerBase);

                            if (var3.equalsIgnoreCase("+all"))
                            {
                                data.setAllowedAll(true);
                                playerBase.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.spacestation.allow_all_true")));
                                return;
                            }
                            if (var3.equalsIgnoreCase("-all"))
                            {
                                data.setAllowedAll(false);
                                playerBase.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.spacestation.allow_all_false", var3)));
                                return;
                            }

                            if (!data.getAllowedPlayers().contains(var3))
                            {
                                data.getAllowedPlayers().add(var3);
                                data.markDirty();
                            }
                        }
                    }

                    final EntityPlayerMP playerToAdd = PlayerUtil.getPlayerBaseServerFromPlayerUsername(var3, true);

                    if (playerToAdd != null)
                    {
                        playerToAdd.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.spacestation.added", PlayerUtil.getName(playerBase))));
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
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssinvite.wrong_usage", this.getCommandUsage(sender)), new Object[0]);
        }

        if (playerBase != null)
        {
            playerBase.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.spacestation.addsuccess", var3)));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getPlayers()) : null;
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 0;
    }
}
