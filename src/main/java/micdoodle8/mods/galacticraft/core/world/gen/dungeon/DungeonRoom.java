package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public abstract class DungeonRoom
{
    public final MapGenDungeon dungeonInstance;
    public World worldObj;

    // Min Corner, Lower, Left, Back
    public int posX;
    public int posY;
    public int posZ;

    // East = 0, North = 1, South = 2, West = 3, Up = 4, Down = 5.
    // North is z++, East is x++.
    public EnumFacing entranceDir;

    public DungeonRoom(MapGenDungeon dungeon, int posX, int posY, int posZ, EnumFacing entranceDir)
    {
        this.dungeonInstance = dungeon;
        this.worldObj = dungeon != null ? dungeon.worldObj : null;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.entranceDir = entranceDir;
    }

    public abstract void generate(ChunkPrimer primer, int cx, int cz);

    public abstract DungeonBoundingBox getCollisionBoundingBox();

    protected abstract DungeonRoom makeRoom(MapGenDungeon dungeon, int x, int y, int z, EnumFacing dir);

    protected abstract void handleTileEntities(Random rand);

    public static DungeonRoom makeRoom(MapGenDungeon dungeon, Random rand, int x, int y, int z, EnumFacing dir)
    {
        return dungeon.otherRooms.get(rand.nextInt(dungeon.otherRooms.size())).makeRoom(dungeon, x, y, z, dir);
    }

    public static DungeonRoom makeBossRoom(MapGenDungeon dungeon, Random rand, int x, int y, int z, EnumFacing dir)
    {
        return dungeon.bossRooms.get(rand.nextInt(dungeon.bossRooms.size())).makeRoom(dungeon, x, y, z, dir);
    }

    public static DungeonRoom makeTreasureRoom(MapGenDungeon dungeon, Random rand, int x, int y, int z, EnumFacing dir)
    {
        return dungeon.treasureRooms.get(rand.nextInt(dungeon.treasureRooms.size())).makeRoom(dungeon, x, y, z, dir);
    }

    protected boolean placeBlock(ChunkPrimer primer, int x, int y, int z, int cx, int cz, Block id, int meta)
    {
        if (MapGenDungeon.useArrays)
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
            primer.setBlockState(index, id.getStateFromMeta(meta));
//            blocks[index] = id;
//            metas[index] = (byte) meta;
        }
        else
        {
            this.worldObj.setBlockState(new BlockPos(x, y, z), id.getStateFromMeta(meta), 0);
        }
        return true;
    }

    private int getIndex(int x, int y, int z)
    {
        return (x * 16 + z) * 256 + y;
    }
}
