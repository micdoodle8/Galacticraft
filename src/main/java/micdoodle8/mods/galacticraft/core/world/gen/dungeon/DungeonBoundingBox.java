package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

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
