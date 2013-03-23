package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

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
            	playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(icommandsender.getCommandSenderName());
            	
                if (playerBase != null)
                {
                	if (playerBase.spaceStationDimensionID <= 0)
                	{
                        throw new WrongUsageException("Could not find space station for your username, you need to travel there first!", new Object[0]);
                	}
                	else
                	{
                		GCCoreSpaceStationData data = GCCoreSpaceStationData.getStationData(playerBase.worldObj, playerBase.spaceStationDimensionID, playerBase);
                    	
                		if (data.getAllowedPlayers().contains(var3.toLowerCase()))
                		{
                        	data.getAllowedPlayers().remove(var3.toLowerCase());
                        	data.markDirty();
                		}
                	}
                }
            }
            catch (Exception var6)
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
        	playerBase.sendChatToPlayer("Successfully removed " + var3 + " from Space Station list!");
        }
	}
}
