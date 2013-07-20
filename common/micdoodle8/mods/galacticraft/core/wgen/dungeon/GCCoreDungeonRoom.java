package micdoodle8.mods.galacticraft.core.wgen.dungeon;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.world.World;

public abstract class GCCoreDungeonRoom
{
    public final GCCoreMapGenDungeon dungeonInstance;
    public World worldObj;

    // Min Corner, Lower, Left, Back
    public int posX;
    public int posY;
    public int posZ;

    // East = 0, North = 1, South = 2, West = 3, Up = 4, Down = 5.
    // North is z++, East is x++.
    public int entranceDir;

    public static ArrayList<GCCoreDungeonRoom> rooms = new ArrayList<GCCoreDungeonRoom>();
    public static ArrayList<GCCoreDungeonRoom> bossRooms = new ArrayList<GCCoreDungeonRoom>();
    public static ArrayList<GCCoreDungeonRoom> treasureRooms = new ArrayList<GCCoreDungeonRoom>();

    public GCCoreDungeonRoom(GCCoreMapGenDungeon dungeon, int posX, int posY, int posZ, int entranceDir)
    {
        this.dungeonInstance = dungeon;
        this.worldObj = dungeon != null ? dungeon.worldObj : null;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.entranceDir = entranceDir;
    }

    public abstract void generate(short[] chunk, byte[] meta, int cx, int cz);

    public abstract GCCoreDungeonBoundingBox getBoundingBox();

    protected abstract GCCoreDungeonRoom makeRoom(GCCoreMapGenDungeon dungeon, int x, int y, int z, int dir);

    protected abstract void handleTileEntities(Random rand);

    public static GCCoreDungeonRoom makeRoom(GCCoreMapGenDungeon dungeon, Random rand, int x, int y, int z, int dir)
    {
        return GCCoreDungeonRoom.rooms.get(rand.nextInt(GCCoreDungeonRoom.rooms.size())).makeRoom(dungeon, x, y, z, dir);
    }

    public static GCCoreDungeonRoom makeBossRoom(GCCoreMapGenDungeon dungeon, Random rand, int x, int y, int z, int dir)
    {
        return GCCoreDungeonRoom.bossRooms.get(rand.nextInt(GCCoreDungeonRoom.bossRooms.size())).makeRoom(dungeon, x, y, z, dir);
    }

    public static GCCoreDungeonRoom makeTreasureRoom(GCCoreMapGenDungeon dungeon, Random rand, int x, int y, int z, int dir)
    {
        return GCCoreDungeonRoom.treasureRooms.get(rand.nextInt(GCCoreDungeonRoom.treasureRooms.size())).makeRoom(dungeon, x, y, z, dir);
    }

    protected boolean placeBlock(short[] blocks, byte[] metas, int x, int y, int z, int cx, int cz, int id, int meta)
    {
        if (GCCoreMapGenDungeon.useArrays)
        {
            cx *= 16;
            cz *= 16;
            x -= cx;
            z -= cz;
            if (x < 0 || x >= 16 || z < 0 || z >= 16)
            {
                return false;
            }
            final int index = this.getIndex(x, y, z);
            blocks[index] = (short) id;
            metas[index] = (byte) meta;
        }
        else
        {
            this.worldObj.setBlock(x, y, z, id, meta, 3);
        }
        return true;
    }

    private int getIndex(int x, int y, int z)
    {
        return y << 8 | z << 4 | x;
    }
}
