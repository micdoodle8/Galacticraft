package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandGCEnergyUnits extends CommandBase
{
	private boolean isBCLoaded = NetworkConfigHandler.isBuildcraftLoaded();
	private boolean isIC2Loaded = NetworkConfigHandler.isIndustrialCraft2Loaded();
	
	@Override
	public String getCommandUsage(ICommandSender var1)
	{
		String options = " [gJ";
		if (this.isBCLoaded) options = options + "/MJ";
		if (this.isIC2Loaded) options = options + "/EU";
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
		if (astring.length == 1)
		{
			String param = astring[0].toLowerCase();
			if (param.length() == 2)
			{
				if ("gj".equals(param))
				{
					NetworkConfigHandler.displayEnergyUnitsBC = false;
					NetworkConfigHandler.displayEnergyUnitsIC2 = false;
					return;
				}
				
				if (this.isBCLoaded && "mj".equals(param))
				{
					NetworkConfigHandler.displayEnergyUnitsBC = true;
					NetworkConfigHandler.displayEnergyUnitsIC2 = false;
					return;
				}

				if (this.isIC2Loaded && "eu".equals(param))
				{
					NetworkConfigHandler.displayEnergyUnitsBC = false;
					NetworkConfigHandler.displayEnergyUnitsIC2 = true;
					return;
				}
			}
			
			throw new WrongUsageException("Invalid units in /GCEnergyUnits command. Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
		}

		throw new WrongUsageException("The units need to be specified! Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
	}
}
