package micdoodle8.mods.galacticraft.core.command;

import java.util.HashMap;
import java.util.Map.Entry;
import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumClientPacket;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class GCCoreCommandPlanetTeleport extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " [player]";
    }

    @Override
    public String getCommandName()
    {
        return "dimensiontp";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        GCCorePlayerMP playerBase = null;

        if (astring.length > 0)
        {
            try
            {
                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName());

                if (playerBase != null)
                {
                    HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(WorldUtil.getPossibleDimensionsForSpaceshipTier(Integer.MAX_VALUE), playerBase);

                    String temp = "";
                    int count = 0;

                    for (Entry<String, Integer> entry : map.entrySet())
                    {
                        temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "." : ""));
                        count++;
                    }
                    
                    playerBase.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.UPDATE_DIMENSION_LIST, new Object[] { playerBase.username, temp }));
                    playerBase.setSpaceshipTier(Integer.MAX_VALUE);
                    playerBase.setUsingPlanetGui();
                    playerBase.mountEntity(null);
                    
                    notifyAdmins(icommandsender, "commands.dimensionteleport", new Object[] { String.valueOf(EnumColor.GREY + "[" + playerBase.getEntityName()), "]" });
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
