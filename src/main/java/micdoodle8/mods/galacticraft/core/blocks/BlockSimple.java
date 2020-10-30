package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.Block;

public class BlockSimple extends Block implements ISortable
{
    public BlockSimple(Properties properties)
    {
        super(properties);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
