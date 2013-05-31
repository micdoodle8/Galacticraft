package micdoodle8.mods.galacticraft.moon.wgen.dungeon;

public class GCDungeonBoundingBox
{

    int minX;
    int minZ;
    int maxX;
    int maxZ;

    public GCDungeonBoundingBox(int minX, int minZ, int maxX, int maxZ)
    {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    public boolean isOverlapping(GCDungeonBoundingBox bb)
    {
        return this.minX < bb.maxX && this.minZ < bb.maxZ && this.maxX > bb.minX && this.maxZ > bb.minZ;
    }

}
