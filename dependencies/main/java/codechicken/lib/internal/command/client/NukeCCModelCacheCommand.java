package codechicken.lib.internal.command.client;

import codechicken.lib.model.blockbakery.BlockBakery;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by covers1624 on 30/12/2016.
 */
public class NukeCCModelCacheCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "nukeCCModelCache";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Clears all of CCL's BakedModel cache, requiring models to be re-baked.\nReally only useful for debugging in dev.\nOnly works on blocks / items that use CCL's BakedModel pipe.";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BlockBakery.nukeModelCache();
        sender.addChatMessage(new TextComponentString("Model cache nuked!"));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return new ArrayList<String>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
