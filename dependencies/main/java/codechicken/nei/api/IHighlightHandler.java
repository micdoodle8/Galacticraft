package codechicken.nei.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

@Deprecated
public interface IHighlightHandler {
    ItemStack identifyHighlight(World world, EntityPlayer player, RayTraceResult rayTraceResult);

    List<String> handleTextData(ItemStack itemStack, World world, EntityPlayer player, RayTraceResult rayTraceResult, List<String> currentTip, ItemInfo.Layout layout);
}
