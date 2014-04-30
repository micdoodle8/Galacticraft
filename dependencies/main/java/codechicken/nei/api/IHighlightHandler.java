package codechicken.nei.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public interface IHighlightHandler
{
    public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop);

    public List<String> handleTextData(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop, List<String> currenttip, ItemInfo.Layout layout);
}
