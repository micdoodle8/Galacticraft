package codechicken.nei;

import codechicken.nei.api.IHighlightHandler;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

@Deprecated
public class DefaultHighlightHandler implements IHighlightHandler {
    @Override
    public List<String> handleTextData(ItemStack stack, World world, EntityPlayer player, RayTraceResult rayTraceResult, List<String> currenttip, ItemInfo.Layout layout) {
        String name = null;
        try {
            String s = GuiContainerManager.itemDisplayNameShort(stack);
            if (s != null && !s.endsWith("Unnamed")) {
                name = s;
            }

            if (name != null) {
                currenttip.add(name);
            }
        } catch (Exception ignored) {
        }

        IBlockState b = world.getBlockState(rayTraceResult.getBlockPos());
        if (name != null && b.getBlock() == Blocks.REDSTONE_WIRE) {
            String s = "" + b.getValue(BlockRedstoneWire.POWER);
            if (s.length() < 2) {
                s = " " + s;
            }
            currenttip.set(currenttip.size() - 1, name + " " + s);
        }

        return currenttip;
    }

    @Override
    public ItemStack identifyHighlight(World world, EntityPlayer player, RayTraceResult rayTraceResult) {
        return null;
    }
}
