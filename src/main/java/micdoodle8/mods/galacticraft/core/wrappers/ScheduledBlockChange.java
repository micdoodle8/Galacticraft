package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.block.Block;

public class ScheduledBlockChange
{
	private Vector3 changePosition;
	private Block changeBlock;
	private int changeMeta;
	private int changeFlag;
	
	public ScheduledBlockChange(Vector3 changePosition, Block changeBlock, int changeMeta)
	{
		this(changePosition, changeBlock, changeMeta, 3);
	}
	
	public ScheduledBlockChange(Vector3 changePosition, Block changeBlock, int changeMeta, int changeFlag)
	{
		this.changePosition = changePosition;
		this.changeBlock = changeBlock;
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

	public Block getChangeBlock()
	{
		return changeBlock;
	}

	public void setChangeBlock(Block changeID)
	{
		this.changeBlock = changeID;
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
