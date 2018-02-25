package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class CommandGCKit extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " [<player>]";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandName()
    {
        return "gckit";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
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
                    ItemHandlerHelper.giveItemToPlayer(playerBase, new ItemStack(GCItems.emergencyKit), 0);
                    CommandBase.notifyOperators(sender, this, "commands.emergencykit", new Object[] { String.valueOf(EnumColor.GREY + "[" + playerBase.getName()), "]" });
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
            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.dimensiontp.too_many", this.getCommandUsage(sender)), new Object[0]);
        }
    }
}
