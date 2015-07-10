package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class ScheduledBlockChange
{
    private BlockPos changePosition;
    private Block change;
    private int changeMeta;

    public ScheduledBlockChange(BlockPos changePosition, Block change, int changeMeta)
    {
        this.changePosition = changePosition;
        this.change = change;
        this.changeMeta = changeMeta;
    }

    public BlockPos getChangePosition()
    {
        return this.changePosition;
    }

    public void setChangePosition(BlockPos changePosition)
    {
        this.changePosition = changePosition;
    }

    public Block getChangeID()
    {
        return this.change;
    }

    public void setChangeID(Block change)
    {
        this.change = change;
    }

    public int getChangeMeta()
    {
        return this.changeMeta;
    }

    public void setChangeMeta(int changeMeta)
    {
        this.changeMeta = changeMeta;
    }
}
