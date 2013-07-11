package micdoodle8.mods.galacticraft.core.wgen.dungeon;

public class GCCoreDungeonBoundingBox
{

    int minX;
    int minZ;
    int maxX;
    int maxZ;

    public GCCoreDungeonBoundingBox(int minX, int minZ, int maxX, int maxZ)
    {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    public boolean isOverlapping(GCCoreDungeonBoundingBox bb)
    {
        return this.minX < bb.maxX && this.minZ < bb.maxZ && this.maxX > bb.minX && this.maxZ > bb.minZ;
    }

}
