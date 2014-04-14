package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatMessageComponent;

/**
 * GCCoreCommandSpaceStationRemoveOwner.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreCommandSpaceStationRemoveOwner extends CommandBase
{
	@Override
	public String getCommandUsage(ICommandSender var1)
	{
		return "/" + this.getCommandName() + " [player]";
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
		GCCorePlayerMP playerBase = null;

		if (astring.length > 0)
		{
			var3 = astring[0];

			try
			{
				playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), false);

				if (playerBase != null)
				{
					if (playerBase.getSpaceStationDimensionID() <= 0)
					{
						throw new WrongUsageException("Could not find space station for your username, you need to travel there first!", new Object[0]);
					}
					else
					{
						final GCCoreSpaceStationData data = GCCoreSpaceStationData.getStationData(playerBase.worldObj, playerBase.getSpaceStationDimensionID(), playerBase);

						if (data.getAllowedPlayers().contains(var3.toLowerCase()))
						{
							data.getAllowedPlayers().remove(var3.toLowerCase());
							data.markDirty();
						}
						else
						{
							throw new CommandException("Couldn't find player with username \"" + var3 + "\" on your Space Station list!", new Object[0]);
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
			throw new WrongUsageException("Not enough command arguments! Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
		}

		if (playerBase != null)
		{
			playerBase.sendChatToPlayer(ChatMessageComponent.createFromText("Successfully removed " + var3 + " from Space Station list!"));
		}
	}
}
