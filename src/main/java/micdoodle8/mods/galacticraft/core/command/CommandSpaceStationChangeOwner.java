package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandSpaceStationChangeOwner extends CommandBase
{
    @Override
    public String getUsage(ICommandSender var1)
    {
        return "/" + this.getName() + " <dim#> <player>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getName()
    {
        return "ssnewowner";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String oldOwner = null;
        String newOwner = "ERROR";
        int stationID = -1;
        EntityPlayerMP playerAdmin = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);

        if (args.length > 1)
        {
            newOwner = args[1];

            try
            {
                stationID = Integer.parseInt(args[0]);
            }
            catch (final Exception var6)
            {
                throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssnewowner.wrong_usage", this.getUsage(sender)), new Object[0]);
            }

            if (stationID < 2)
            {
                throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssnewowner.wrong_usage", this.getUsage(sender)), new Object[0]);
            }

            try
            {
                SpaceStationWorldData stationData = SpaceStationWorldData.getMPSpaceStationData(null, stationID, null);
                if (stationData == null)
                {
                    throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssnewowner.wrong_usage", this.getUsage(sender)), new Object[0]);
                }

                oldOwner = stationData.getOwner();
                stationData.getAllowedPlayers().remove(oldOwner);
                if (stationData.getSpaceStationName().equals("Station: " + oldOwner))
                {
                    stationData.setSpaceStationName("Station: " + newOwner);
                }
                stationData.getAllowedPlayers().add(newOwner);
                stationData.setOwner(newOwner);

                final EntityPlayerMP oldPlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(oldOwner, true);
                final EntityPlayerMP newPlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(newOwner, true);
                if (oldPlayer != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(oldPlayer);
                    SpaceStationWorldData.updateSSOwnership(oldPlayer, oldOwner, stats, stationID, stationData);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionID(oldPlayer.world), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), oldPlayer);
                }
                if (newPlayer != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(newPlayer);
                    SpaceStationWorldData.updateSSOwnership(newPlayer, newOwner.replace(".", ""), stats, stationID, stationData);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionID(oldPlayer.world), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), newPlayer);
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

        if (playerAdmin != null)
        {
            playerAdmin.sendMessage(new TextComponentString(GCCoreUtil.translateWithFormat("gui.spacestation.changesuccess", oldOwner, newOwner)));
        }
        else
        //Console
        {
            System.out.println(GCCoreUtil.translateWithFormat("gui.spacestation.changesuccess", oldOwner, newOwner));
        }
    }
}
