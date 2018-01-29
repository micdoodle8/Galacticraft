package codechicken.lib.internal.command;

import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.thread.TaskProfiler;
import codechicken.lib.thread.TaskProfiler.ProfilerResult;
import codechicken.lib.util.BlockStateUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by covers1624 on 9/01/2017.
 */
public class DevEnvCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "devStuff";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Does dev stuff.";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            RayTraceResult trace = RayTracer.retrace(player);
            if (trace == null) {
                addMessage(sender, "Null trace.");
                return;
            }
            IBlockState state = player.worldObj.getBlockState(trace.getBlockPos());
            addMessage(sender, state);
            TaskProfiler timer = new TaskProfiler();
            timer.startOnce("task");
            int hash = BlockStateUtils.hashBlockState(state.getBlock().getExtendedState(state, player.worldObj, trace.getBlockPos()));
            ProfilerResult result = timer.endOnce();

            addMessage(sender, "Hash: " + hash);

            addMessage(sender, "Time: " + result.time / 1000);

        } else {
            addMessage(sender, "You are not an EntityPlayer..");
        }

    }

    private static void addMessage(ICommandSender sender, Object object, Object... format) {
        sender.addChatMessage(new TextComponentString(String.format(String.valueOf(object), format)));
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
