package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import net.minecraft.block.Block;

public class ScheduledBlockChange
{
    private BlockVec3 changePosition;
    private Block change;
    private int changeMeta;

    public ScheduledBlockChange(BlockVec3 changePosition, Block change, int changeMeta)
    {
        this.changePosition = changePosition;
        this.change = change;
        this.changeMeta = changeMeta;
    }

    public BlockVec3 getChangePosition()
    {
        return this.changePosition;
    }

    public void setChangePosition(BlockVec3 changePosition)
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
