package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.block.Block;

import java.util.Arrays;
import java.util.List;

public class BlockMetaList
{
    private Block block;
    private List<Integer> metaList;

    public BlockMetaList(Block blockID, Integer... metadata)
    {
        this(blockID, Arrays.asList(metadata));
    }

    public BlockMetaList(Block blockID, List<Integer> metadata)
    {
        this.block = blockID;
        this.metaList = metadata;
    }

    public Block getBlock()
    {
        return this.block;
    }

    public List<Integer> getMetaList()
    {
        return this.metaList;
    }

    public void addMetadata(int meta)
    {
        this.metaList.add(meta);
    }

    public void removeMetadata(int meta)
    {
        this.metaList.remove(meta);
    }

    @Override
    public int hashCode()
    {
        return this.block.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof BlockMetaList)
        {
            return (BlockMetaList) obj == this;
        }

        return false;
    }
}
