package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class StackSorted
{
    private final Item item;
//    private final int meta;

    public StackSorted(Block block)
    {
        this(Item.getItemFromBlock(block));
    }

    public StackSorted(Item item)
    {
        this.item = item;
//        this.meta = meta;
    }

    public Item getItem()
    {
        return item;
    }

//    public int getMeta()
//    {
//        return meta;
//    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof StackSorted))
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }

        StackSorted other = (StackSorted) obj;
        return new EqualsBuilder()
                .append(item, other.item)
//                .append(meta, other.meta)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append(item)
//                .append(meta)
                .toHashCode();
    }

    @Override
    public String toString()
    {
        return "Item:(" + item + ")";
    }
}
