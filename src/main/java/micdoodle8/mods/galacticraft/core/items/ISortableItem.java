package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;

public interface ISortableItem
{
    EnumSortCategoryItem getCategory(int meta);
}
