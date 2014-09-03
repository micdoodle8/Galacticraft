package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.VersionUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map.Entry;

public class CommandPlanetTeleport extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " <player>";
    }

    @Override
    public String getCommandName()
    {
        return "dimensiontp";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        EntityPlayerMP playerBase = null;

        if (astring.length < 2)
        {
            try
            {
                if (astring.length == 1)
                {
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(astring[0], true);
                }
                else
                {
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), true);
                }

                if (playerBase != null)
                {
                    GCPlayerStats stats = GCEntityPlayerMP.getPlayerStats(playerBase);
                    stats.rocketStacks = new ItemStack[2];
                    stats.rocketType = IRocketType.EnumRocketType.DEFAULT.ordinal();
                    stats.rocketItem = GCItems.rocketTier1;
                    stats.fuelLevel = 1000;

                    HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(WorldUtil.getPossibleDimensionsForSpaceshipTier(Integer.MAX_VALUE), playerBase);

                    String temp = "";
                    int count = 0;

                    for (Entry<String, Integer> entry : map.entrySet())
                    {
                        temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
                        count++;
                    }

                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, new Object[] { playerBase.getGameProfile().getName(), temp }), playerBase);
                    stats.spaceshipTier = Integer.MAX_VALUE;
                    stats.usingPlanetSelectionGui = true;
                    playerBase.mountEntity(null);

                    VersionUtil.notifyAdmins(icommandsender, this, "commands.dimensionteleport", new Object[] { String.valueOf(EnumColor.GREY + "[" + playerBase.getCommandSenderName()), "]" });
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
