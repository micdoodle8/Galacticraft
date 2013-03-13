package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.BlockSapling;
import net.minecraft.item.ItemMultiTextureTile;

public class GCCoreItemSapling extends ItemMultiTextureTile
{
    public GCCoreItemSapling(int par1)
    {
        super(par1, GCCoreBlocks.sapling, BlockSapling.WOOD_TYPES);
    }
}
