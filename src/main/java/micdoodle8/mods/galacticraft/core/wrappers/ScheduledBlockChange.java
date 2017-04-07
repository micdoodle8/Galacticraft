package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class ScheduledBlockChange
{
    private BlockPos changePosition;
    private IBlockState change;
    private int changeUpdateFlag;

    public ScheduledBlockChange(BlockVec3 changePosition, IBlockState change)
    {
        this(changePosition.toBlockPos(), change, 3);
    }

    public ScheduledBlockChange(BlockPos pos, IBlockState change, int changeUpdateFlag)
    {
        this.changePosition = pos;
        this.change = change;
        this.changeUpdateFlag = changeUpdateFlag;
    }

    public BlockPos getChangePosition()
    {
        return this.changePosition;
    }

    public void setChangePosition(BlockPos changePosition)
    {
        this.changePosition = changePosition;
    }

    public IBlockState getChangeState()
    {
        return this.change;
    }

    public void setChangeState(IBlockState change)
    {
        this.change = change;
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
