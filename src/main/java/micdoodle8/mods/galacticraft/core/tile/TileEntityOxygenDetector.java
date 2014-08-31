package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenDetector;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityOxygenDetector extends TileEntityAdvanced
{
	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.worldObj != null && !this.worldObj.isRemote && this.ticks % 50 == 0)
		{
			this.blockType = this.getBlockType();

			if (this.blockType != null && this.blockType instanceof BlockOxygenDetector)
			{
				((BlockOxygenDetector) this.blockType).updateOxygenState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, OxygenUtil.isAABBInBreathableAirBlock(this.worldObj, AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1, this.xCoord + 2, this.yCoord + 2, this.zCoord + 2)));
			}
		}
	}

	@Override
	public double getPacketRange()
	{
		return 0;
	}

	@Override
	public int getPacketCooldown()
	{
		return 0;
	}

	@Override
	public boolean isNetworkedTile()
	{
		return false;
	}
}
