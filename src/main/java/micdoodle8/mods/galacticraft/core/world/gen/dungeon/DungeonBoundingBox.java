package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

/**
 * GCCoreDungeonBoundingBox.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author fishtaco567
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class DungeonBoundingBox
{
	int minX;
	int minZ;
	int maxX;
	int maxZ;

	public DungeonBoundingBox(int minX, int minZ, int maxX, int maxZ)
	{
		this.minX = minX;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxZ = maxZ;
	}

	public boolean isOverlapping(DungeonBoundingBox bb)
	{
		return this.minX < bb.maxX && this.minZ < bb.maxZ && this.maxX > bb.minX && this.maxZ > bb.minZ;
	}

}
