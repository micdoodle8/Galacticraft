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
import net.minecraft.util.ChatComponentText;

public class CommandSpaceStationChangeOwner extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " <dim#> <player>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandName()
    {
        return "ssnewowner";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        String oldOwner = null;
        String newOwner = "ERROR";
        int stationID = -1;
        EntityPlayerMP playerAdmin = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), true);

        if (astring.length > 1)
        {
            newOwner = astring[1];

            try
            {
            	stationID = Integer.parseInt(astring[0]);
            }
            catch (final Exception var6)
            {
            	throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssnewowner.wrongUsage", this.getCommandUsage(icommandsender)), new Object[0]);
            }
            
            if (stationID < 2)
            	throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssnewowner.wrongUsage", this.getCommandUsage(icommandsender)), new Object[0]);
            
            try
            {          	
                SpaceStationWorldData stationData = SpaceStationWorldData.getMPSpaceStationData(null, stationID, null);
                if (stationData == null)
            	{
            		throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssnewowner.wrongUsage", this.getCommandUsage(icommandsender)), new Object[0]);
            	}
                
                oldOwner = stationData.getOwner();
                stationData.getAllowedPlayers().remove(oldOwner);
                if (stationData.getSpaceStationName().equals("Station: " + oldOwner))
                	stationData.setSpaceStationName("Station: " + newOwner);
                stationData.getAllowedPlayers().add(newOwner);
                stationData.setOwner(newOwner);
                
                final EntityPlayerMP oldPlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(oldOwner, true);
                final EntityPlayerMP newPlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(newOwner, true);
                if (oldPlayer != null)
                {
                	final GCPlayerStats stats = GCPlayerStats.get(oldPlayer);
                	SpaceStationWorldData.updateSSOwnership(oldPlayer, oldOwner, stats, stationID, stationData);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, new Object[] { WorldUtil.spaceStationDataToString(stats.spaceStationDimensionData) }), oldPlayer);
                }
                if (newPlayer != null)
                {
                	final GCPlayerStats stats = GCPlayerStats.get(newPlayer);
                	SpaceStationWorldData.updateSSOwnership(newPlayer, newOwner.replace(".", ""), stats, stationID, stationData);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, new Object[] { WorldUtil.spaceStationDataToString(stats.spaceStationDimensionData) }), newPlayer);
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

        if (playerAdmin != null)
        {
            playerAdmin.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.spacestation.changesuccess", oldOwner, newOwner)));
        }
        else
        	//Console
        	System.out.println(GCCoreUtil.translateWithFormat("gui.spacestation.changesuccess", oldOwner, newOwner));
    }

    protected String[] getPlayers()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }
}
