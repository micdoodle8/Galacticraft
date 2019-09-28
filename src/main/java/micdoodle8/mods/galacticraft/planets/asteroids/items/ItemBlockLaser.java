package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockLaser extends ItemBlockDesc
{
    public ItemBlockLaser(Block block)
    {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> info, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, info, flagIn);

        info.add(TextFormatting.DARK_RED + "BETA Testing Phase");
        info.add(TextFormatting.DARK_RED + "Please Report Bugs");
    }
}
