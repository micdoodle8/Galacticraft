package micdoodle8.mods.galacticraft.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IClickableItem
{
    /**
     * Use to replicate ItemStack version of onItemRightClick as in MC1.10
     */
    ItemStack onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer player);
}
