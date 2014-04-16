package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatMessageComponent;

/**
 * GCCoreCommandKeepDim.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8, radfast
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreCommandKeepDim extends CommandBase
{
	@Override
	public String getCommandUsage(ICommandSender var1)
	{
		return "/" + this.getCommandName();
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public String getCommandName()
	{
		return "gckeeploaded";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		GCCorePlayerMP playerBase = null;

		if (astring.length > 1)
		{
			throw new WrongUsageException("Not enough command arguments! Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
		}
		else
		{
			try
			{
				playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), true);

				if (playerBase != null)
				{
					int dimID;

					if (astring.length == 0)
					{
						dimID = playerBase.dimension;
					}
					else
					{
						dimID = CommandBase.parseInt(icommandsender, astring[0]);
					}

					if (GCCoreConfigManager.setLoaded(dimID))
					{
						playerBase.sendChatToPlayer(ChatMessageComponent.createFromText("[GCKeepLoaded] Successfully set dimension " + dimID + " to load staticly"));
					}
					else
					{
						playerBase.sendChatToPlayer(ChatMessageComponent.createFromText("[GCKeepLoaded] Dimension " + dimID + " is already set as static!"));
					}
				}
			}
			catch (final Exception var6)
			{
				throw new CommandException(var6.getMessage(), new Object[0]);
			}
		}
	}
}
