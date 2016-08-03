package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;

public interface ISortableBlock
{
    EnumSortCategoryBlock getCategory(int meta);
}
