package micdoodle8.mods.galacticraft.api.prefab.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

/**
 * Do not include this prefab class in your released mod download.
 */
@Deprecated
public class BlockMetaPair
{
    private final Block block;
    private final byte metadata;

    public BlockMetaPair(Block block, byte metadata)
    {
        this.block = block;
        this.metadata = metadata;
    }

    public Block getBlock()
    {
        return this.block;
    }

    public byte getMetadata()
    {
        return this.metadata;
    }

    public BlockState getBlockState()
    {
        return this.block.getStateFromMeta(this.metadata);
    }
}
