package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;

public class ScheduledBlockChange
{
	private BlockVec3 changePosition;
	private int changeID;
	private int changeMeta;

	public ScheduledBlockChange(BlockVec3 changePosition, int changeID, int changeMeta)
	{
		this.changePosition = changePosition;
		this.changeID = changeID;
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

	public int getChangeID()
	{
		return this.changeID;
	}

	public void setChangeID(int changeID)
	{
		this.changeID = changeID;
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
