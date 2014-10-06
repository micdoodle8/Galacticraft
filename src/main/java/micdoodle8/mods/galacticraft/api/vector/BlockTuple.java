package micdoodle8.mods.galacticraft.api.vector;

import net.minecraft.block.Block;

public class BlockTuple
{
    public Block block;
    public int meta;

    public BlockTuple(Block b, int m)
    {
        this.block = b;
        this.meta = m;
    }
}
