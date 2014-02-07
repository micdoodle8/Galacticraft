package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.Vector3;

public class ScheduledBlockChange
{
	private Vector3 changePosition;
	private int changeID;
	private int changeMeta;
	private int changeFlag;
	
	public ScheduledBlockChange(Vector3 changePosition, int changeID, int changeMeta)
	{
		this(changePosition, changeID, changeMeta, 3);
	}
	
	public ScheduledBlockChange(Vector3 changePosition, int changeID, int changeMeta, int changeFlag)
	{
		this.changePosition = changePosition;
		this.changeID = changeID;
		this.changeMeta = changeMeta;
		this.changeFlag = changeFlag;
	}

	public Vector3 getChangePosition()
	{
		return changePosition;
	}

	public void setChangePosition(Vector3 changePosition)
	{
		this.changePosition = changePosition;
	}

	public int getChangeID()
	{
		return changeID;
	}

	public void setChangeID(int changeID)
	{
		this.changeID = changeID;
	}

	public int getChangeMeta()
	{
		return changeMeta;
	}

	public void setChangeMeta(int changeMeta)
	{
		this.changeMeta = changeMeta;
	}

	public int getChangeFlag()
	{
		return changeFlag;
	}

	public void setChangeFlag(int changeFlag)
	{
		this.changeFlag = changeFlag;
	}
}
