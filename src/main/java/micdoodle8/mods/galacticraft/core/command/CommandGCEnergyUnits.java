package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandGCEnergyUnits extends CommandBase
{
	
	@Override
	public String getCommandUsage(ICommandSender var1)
	{
		String options = " [gJ";
		if (NetworkConfigHandler.isBuildcraftLoaded()) options = options + "/MJ";
		if (NetworkConfigHandler.isIndustrialCraft2Loaded()) options = options + "/EU";
		return "/" + this.getCommandName() + options + "]";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return true;
	}

	@Override
	public String getCommandName()
	{
		return "gcenergyunits";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		GCEntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName(), true);
		if (playerBase == null) return;

		if (astring.length == 1)
		{
			String param = astring[0].toLowerCase();
			if (param.length() == 2)
			{
				int paramvalue = 0;
				if ("gj".equals(param))
					paramvalue = 1;
				else if ("mj".equals(param) && NetworkConfigHandler.isBuildcraftLoaded())
					paramvalue = 2;
				else if ("eu".equals(param) && NetworkConfigHandler.isIndustrialCraft2Loaded())
					paramvalue = 3;
				
				if (paramvalue > 0)
				{
					GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_ENERGYUNITS, new Object[] { paramvalue }), playerBase);
					return;
				}
				
			}
			
			throw new WrongUsageException("Invalid units in /GCEnergyUnits command. Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
		}

		throw new WrongUsageException("The units need to be specified! Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
	}
	

	public static void handleParamClientside(int param)
	{
		if (param == 1)
		{
			NetworkConfigHandler.displayEnergyUnitsBC = false;
			NetworkConfigHandler.displayEnergyUnitsIC2 = false;
			return;
		}
		
		if (param == 2 && NetworkConfigHandler.isBuildcraftLoaded())
		{
			NetworkConfigHandler.displayEnergyUnitsBC = true;
			NetworkConfigHandler.displayEnergyUnitsIC2 = false;
			return;
		}

		if (param == 3 && NetworkConfigHandler.isIndustrialCraft2Loaded())
		{
			NetworkConfigHandler.displayEnergyUnitsBC = false;
			NetworkConfigHandler.displayEnergyUnitsIC2 = true;
			return;
		}	
	}
}
