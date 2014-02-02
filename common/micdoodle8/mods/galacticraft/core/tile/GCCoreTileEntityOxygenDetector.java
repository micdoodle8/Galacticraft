package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockOxygenDetector;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;

/**
 * GCCoreTileEntityOxygenDetector.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityOxygenDetector extends GCCoreTileEntityAdvanced
{
	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.worldObj != null && !this.worldObj.isRemote && this.ticks % 50 == 0)
		{
			this.blockType = this.getBlockType();

			if (this.blockType != null && this.blockType instanceof GCCoreBlockOxygenDetector)
			{
				((GCCoreBlockOxygenDetector) this.blockType).updateOxygenState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, OxygenUtil.isAABBInBreathableAirBlock(this.worldObj, new Vector3(this).translate(new Vector3(-1, -1, -1)), new Vector3(this).translate(new Vector3(2, 2, 2))));
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
