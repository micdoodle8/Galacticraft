package micdoodle8.mods.galacticraft.core.tile;

/**
 * GCCoreTileEntityAirLock.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityAirLock extends GCCoreTileEntityAdvanced
{
	@Override
	public void updateEntity()
	{
		super.updateEntity();
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
