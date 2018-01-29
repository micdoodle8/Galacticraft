package codechicken.lib.internal.command.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created by covers1624 on 19/12/2016.
 * TODO Items.
 */
public class DumpModelLocationsCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "dumpModelLocations";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Takes the item in your hand and dumps all model locations.";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        ItemStack stack = Minecraft.getMinecraft().thePlayer.getHeldItemMainhand();
        if (stack != null && stack.getItem() instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(stack.getItem());
            BlockStateMapper stateMapper = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getBlockStateMapper();
            for (Entry<IBlockState, ModelResourceLocation> entry : stateMapper.getVariants(block).entrySet()) {
                sender.addChatMessage(new TextComponentString(entry.getKey().toString() + " | " + entry.getValue().toString()));
                FMLLog.info(entry.getKey().toString() + " | " + entry.getValue().toString());
            }
        }
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
