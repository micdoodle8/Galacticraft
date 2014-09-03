package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandJoinSpaceRace extends CommandBase
{
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName();
    }

    @Override
    public String getCommandName()
    {
        return "joinrace";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), true);

        if (astring.length == 0)
        {
            try
            {
                if (playerBase != null)
                {
                    GCPlayerStats stats = GCEntityPlayerMP.getPlayerStats(playerBase);

                    if (stats.spaceRaceInviteTeamID > 0)
                    {
                        SpaceRaceManager.sendSpaceRaceData(playerBase, SpaceRaceManager.getSpaceRaceFromID(stats.spaceRaceInviteTeamID));
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_JOIN_RACE_GUI, new Object[] { stats.spaceRaceInviteTeamID }), playerBase);
                    }
                    else
                    {
                        throw new Exception("You haven't been invited to a space race team!");
                    }
                }
                else
                {
                    throw new Exception("Could not find player with name: " + astring[0]);
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
    }
}
