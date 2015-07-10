package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.*;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class CommandPlanetTeleport extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getName() + " [<player>]";
    }

    @Override
    public String getName()
    {
        return "dimensiontp";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP playerBase = null;

        if (args.length < 2)
        {
            try
            {
                if (args.length == 1)
                {
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(args[0], true);
                }
                else
                {
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
                }

                if (playerBase != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(playerBase);
                    stats.rocketStacks = new ItemStack[2];
                    stats.rocketType = IRocketType.EnumRocketType.DEFAULT.ordinal();
                    stats.rocketItem = GCItems.rocketTier1;
                    stats.fuelLevel = 1000;

                    WorldUtil.toCelestialSelection(playerBase, stats, Integer.MAX_VALUE);

                    VersionUtil.notifyAdmins(sender, this, "commands.dimensionteleport", new Object[] { String.valueOf(EnumColor.GREY + "[" + playerBase.getName()), "]" });
                }
                else
                {
                    throw new Exception("Could not find player with name: " + args[0]);
                }
            }
            catch (final Exception var6)
            {
                throw new CommandException(var6.getMessage(), new Object[0]);
            }
        }
        else
        {
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.dimensiontp.tooMany", this.getCommandUsage(sender)), new Object[0]);
        }
    }
}
