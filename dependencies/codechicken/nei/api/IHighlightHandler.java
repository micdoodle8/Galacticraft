package codechicken.nei.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public interface IHighlightHandler
{
    public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop);

    public List<String> handleTextData(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop, List<String> currenttip, ItemInfo.Layout layout);
}
