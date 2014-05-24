package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;

/**
 * GCCoreCommandKeepDim.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8, radfast
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class CommandKeepDim extends CommandBase
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
		GCEntityPlayerMP playerBase = null;

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

					if (ConfigManagerCore.setLoaded(dimID))
					{
						playerBase.addChatMessage(new ChatComponentText("[GCKeepLoaded] Successfully set dimension " + dimID + " to load staticly"));
					}
					else
					{
<<<<<<< HEAD:src/main/java/micdoodle8/mods/galacticraft/core/command/CommandKeepDim.java
						playerBase.addChatMessage(new ChatComponentText("[GCKeepLoaded] Dimension " + dimID + " is already set as static!"));
=======
						if (GCCoreConfigManager.setUnloaded(dimID))
						{
							playerBase.sendChatToPlayer(ChatMessageComponent.createFromText("[GCKeepLoaded] Successfully set dimension " + dimID + " to not load staticly"));
						}
						else
						{
							playerBase.sendChatToPlayer(ChatMessageComponent.createFromText("[GCKeepLoaded] Failed to set dimension as not static"));
						}
>>>>>>> 58f48f8b7e9a89c745a63e4440ff91be6c07e9bf:common/micdoodle8/mods/galacticraft/core/command/GCCoreCommandKeepDim.java
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
