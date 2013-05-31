package micdoodle8.mods.galacticraft.moon.wgen.dungeon;

import java.util.Random;
import net.minecraft.world.World;

public class GCRoomEmpty extends GCDungeonRoom
{

    int sizeX;
    int sizeY;
    int sizeZ;

    public GCRoomEmpty(World worldObj, int posX, int posY, int posZ, int entranceDir)
    {
        super(worldObj, posX, posY, posZ, entranceDir);
        if (worldObj != null)
        {
            final Random rand = new Random(worldObj.getSeed() * posX * posY * 57 * posZ);
            this.sizeX = rand.nextInt(4) + 5;
            this.sizeY = rand.nextInt(2) + 4;
            this.sizeZ = rand.nextInt(4) + 5;
        }
    }

    @Override
    public void generate(short[] chunk, byte[] meta, int cx, int cz)
    {
        for (int i = this.posX - 1; i <= this.posX + this.sizeX; i++)
        {
            for (int j = this.posY - 1; j <= this.posY + this.sizeY; j++)
            {
                for (int k = this.posZ - 1; k <= this.posZ + this.sizeZ; k++)
                {
                    if (i == this.posX - 1 || i == this.posX + this.sizeX || j == this.posY - 1 || j == this.posY + this.sizeY || k == this.posZ - 1 || k == this.posZ + this.sizeZ)
                    {
                        this.placeBlock(chunk, meta, i, j, k, cx, cz, GCMapGenDungeon.DUNGEON_WALL_ID, GCMapGenDungeon.DUNGEON_WALL_META);
                    }
                    else
                    {
                        this.placeBlock(chunk, meta, i, j, k, cx, cz, 0, 0);
                    }
                }
            }
        }
    }

    @Override
    public GCDungeonBoundingBox getBoundingBox()
    {
        return new GCDungeonBoundingBox(this.posX, this.posZ, this.posX + this.sizeX, this.posZ + this.sizeZ);
    }

    @Override
    protected GCDungeonRoom makeRoom(World worldObj, int x, int y, int z, int dir)
    {
        return new GCRoomEmpty(worldObj, x, y, z, dir);
    }

    @Override
    protected void handleTileEntities(Random rand)
    {
        // TODO Auto-generated method stub

    }

}
