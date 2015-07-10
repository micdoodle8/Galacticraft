package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;

public class CommandGCHelp extends CommandBase
{

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getName();
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender par1ICommandSender)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "gchelp";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
		if (playerBase == null)
        {
            return;
        }
		playerBase.addChatMessage(IChatComponent.Serializer.jsonToComponent("[{\"text\":\"" + GCCoreUtil.translate("gui.message.help1") + ": \",\"color\":\"white\"}," + "{\"text\":\" " + EnumColor.BRIGHT_GREEN + "wiki."+GalacticraftCore.PREFIX+"com/wiki" + "\"," + "\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":" + "{\"text\":\""+ GCCoreUtil.translate("gui.message.clicklink") +"\",\"color\":\"yellow\"}}," + "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + "http://wiki."+GalacticraftCore.PREFIX+"com/wiki" + "\"}}]"));
    }
}
