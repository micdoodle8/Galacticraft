package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityDungeonSpawner;

/**
 * IBoss.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public interface IBoss
{
	public void setRoom(Vector3 roomCoords, Vector3 roomSize);

	public void onBossSpawned(GCCoreTileEntityDungeonSpawner spawner);
}
