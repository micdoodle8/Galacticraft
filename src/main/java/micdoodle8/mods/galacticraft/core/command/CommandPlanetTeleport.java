package micdoodle8.mods.galacticraft.core.command;

import java.util.HashMap;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

/**
 * GCCoreCommandPlanetTeleport.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class CommandPlanetTeleport extends CommandBase
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
		GCEntityPlayerMP playerBase = null;

		if (astring.length > 0)
		{
			try
			{
				playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(astring[0], true);

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

					GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, new Object[] { playerBase.getGameProfile().getName(), temp }), playerBase);
					playerBase.setSpaceshipTier(Integer.MAX_VALUE);
					playerBase.setUsingPlanetGui();
					playerBase.mountEntity(null);

					CommandBase.notifyAdmins(icommandsender, "commands.dimensionteleport", new Object[] { String.valueOf(EnumColor.GREY + "[" + playerBase.getCommandSenderName()), "]" });
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
