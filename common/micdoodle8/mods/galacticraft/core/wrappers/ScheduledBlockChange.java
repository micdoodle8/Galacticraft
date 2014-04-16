package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.core.oxygen.BlockVec3;

public class ScheduledBlockChange
{
	private BlockVec3 changePosition;
	private int changeID;
	private int changeMeta;
	private int changeFlag;

	public ScheduledBlockChange(BlockVec3 changePosition, int changeID, int changeMeta)
	{
		this(changePosition, changeID, changeMeta, 3);
	}

	public ScheduledBlockChange(BlockVec3 changePosition, int changeID, int changeMeta, int changeFlag)
	{
		this.changePosition = changePosition;
		this.changeID = changeID;
		this.changeMeta = changeMeta;
		this.changeFlag = changeFlag;
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

	public int getChangeFlag()
	{
		return this.changeFlag;
	}

	public void setChangeFlag(int changeFlag)
	{
		this.changeFlag = changeFlag;
	}
}
