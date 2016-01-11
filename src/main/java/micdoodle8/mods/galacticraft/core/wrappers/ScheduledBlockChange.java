package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import net.minecraft.block.Block;

public class ScheduledBlockChange
{
    private BlockVec3 changePosition;
    private Block change;
    private int changeMeta;
    private int changeUpdateFlag;

    public ScheduledBlockChange(BlockVec3 changePosition, Block change, int changeMeta)
    {
        this(changePosition, change, changeMeta, 3);
    }

    public ScheduledBlockChange(BlockVec3 changePosition, Block change, int changeMeta, int changeUpdateFlag)
    {
        this.changePosition = changePosition;
        this.change = change;
        this.changeMeta = changeMeta;
        this.changeUpdateFlag = changeUpdateFlag;
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

    public int getChangeUpdateFlag()
    {
        return changeUpdateFlag;
    }

    public void setChangeUpdateFlag(int changeUpdateFlag)
    {
        this.changeUpdateFlag = changeUpdateFlag;
    }
}
