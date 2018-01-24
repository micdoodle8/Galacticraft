package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nullable;

public class ItemBlockShortRangeTelepad extends ItemBlockDesc
{
    public ItemBlockShortRangeTelepad(Block block)
    {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(par1ItemStack, worldIn, tooltip, flagIn);
    }
}
