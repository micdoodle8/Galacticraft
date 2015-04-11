package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenDungeon
{
    public ArrayList<DungeonRoom> bossRooms = new ArrayList<DungeonRoom>();
    public ArrayList<DungeonRoom> treasureRooms = new ArrayList<DungeonRoom>();
    public ArrayList<DungeonRoom> otherRooms = new ArrayList<DungeonRoom>();

    public final Block DUNGEON_WALL_ID;
    public final int DUNGEON_WALL_META;
    public final int RANGE;
    public final int HALLWAY_LENGTH;
    public final int HALLWAY_HEIGHT;

    public static boolean useArrays = false;

    public World worldObj;

    private final ArrayList<DungeonRoom> rooms = new ArrayList<DungeonRoom>();

    public MapGenDungeon(Block wallID, int wallMeta, int range, int hallwayLength, int hallwayHeight)
    {
        this.DUNGEON_WALL_ID = wallID;
        this.DUNGEON_WALL_META = wallMeta;
        this.RANGE = range;
        this.HALLWAY_LENGTH = hallwayLength;
        this.HALLWAY_HEIGHT = hallwayHeight;
    }

    public void generateUsingArrays(World world, long seed, int x, int y, int z, int chunkX, int chunkZ, Block[] blocks, byte[] metas)
    {
        final ChunkCoordinates dungeonCoords = this.getDungeonNear(seed, chunkX, chunkZ);
        if (dungeonCoords != null)
        {
            this.generate(world, new Random(seed * dungeonCoords.posX * dungeonCoords.posZ * 24789), dungeonCoords.posX, y, dungeonCoords.posZ, chunkX, chunkZ, blocks, metas, true);
        }
    }

    public void generateUsingSetBlock(World world, long seed, int x, int y, int z)
    {
        final ChunkCoordinates dungeonCoords = this.getDungeonNear(seed, x / 16, y / 16);
        if (dungeonCoords != null)
        {
            this.generate(world, new Random(seed * dungeonCoords.posX * dungeonCoords.posZ * 24789), x, y, z, x, z, null, null, false);
        }
    }

    public void generate(World world, Random rand, int x, int y, int z, int chunkX, int chunkZ, Block[] blocks, byte[] metas, boolean useArrays)
    {
        MapGenDungeon.useArrays = useArrays;
        this.worldObj = world;

        final List<DungeonBoundingBox> boundingBoxes = new ArrayList<DungeonBoundingBox>();

        final int length = rand.nextInt(4) + 5;

        DungeonRoom currentRoom = DungeonRoom.makeRoom(this, rand, x, y, z, ForgeDirection.DOWN);
        currentRoom.generate(blocks, metas, chunkX, chunkZ);
        this.rooms.add(currentRoom);
        final DungeonBoundingBox cbb = currentRoom.getBoundingBox();
        boundingBoxes.add(cbb);
        this.generateEntranceCrater(blocks, metas, x + (cbb.maxX - cbb.minX) / 2, y, z + (cbb.maxZ - cbb.minZ) / 2, chunkX, chunkZ);

        for (int i = 0; i <= length; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                int offsetX = 0;
                int offsetZ = 0;
                final ForgeDirection dir = this.randDir(rand);
                ForgeDirection entranceDir = dir;
                switch (dir)
                // East = 0, North = 1, South = 2, West = 3
                {
                case EAST: // East z++
                    offsetZ = this.HALLWAY_LENGTH + rand.nextInt(15);
                    if (rand.nextBoolean())
                    {
                        if (rand.nextBoolean())
                        {
                            entranceDir = ForgeDirection.NORTH;
                            offsetX = this.HALLWAY_LENGTH + rand.nextInt(15);
                        }
                        else
                        {
                            entranceDir = ForgeDirection.SOUTH;
                            offsetX = -this.HALLWAY_LENGTH - rand.nextInt(15);
                        }
                    }
                    break;
                case NORTH: // North x++
                    offsetX = this.HALLWAY_LENGTH + rand.nextInt(15);
                    if (rand.nextBoolean())
                    {
                        if (rand.nextBoolean())
                        {
                            entranceDir = ForgeDirection.EAST;
                            offsetZ = this.HALLWAY_LENGTH + rand.nextInt(15);
                        }
                        else
                        {
                            entranceDir = ForgeDirection.WEST;
                            offsetZ = -this.HALLWAY_LENGTH - rand.nextInt(15);
                        }
                    }
                    break;
                case SOUTH: // South x--
                    offsetX = -this.HALLWAY_LENGTH - rand.nextInt(15);
                    if (rand.nextBoolean())
                    {
                        if (rand.nextBoolean())
                        {
                            entranceDir = ForgeDirection.EAST;
                            offsetZ = this.HALLWAY_LENGTH + rand.nextInt(15);
                        }
                        else
                        {
                            entranceDir = ForgeDirection.WEST;
                            offsetZ = -this.HALLWAY_LENGTH - rand.nextInt(15);
                        }
                    }
                    break;
                case WEST: // West z--
                    offsetZ = -this.HALLWAY_LENGTH - rand.nextInt(15);
                    if (rand.nextBoolean())
                    {
                        if (rand.nextBoolean())
                        {
                            entranceDir = ForgeDirection.NORTH;
                            offsetX = this.HALLWAY_LENGTH + rand.nextInt(15);
                        }
                        else
                        {
                            entranceDir = ForgeDirection.SOUTH;
                            offsetX = -this.HALLWAY_LENGTH - rand.nextInt(15);
                        }
                    }
                    break;
                default:
                    break;
                }

                DungeonRoom possibleRoom = DungeonRoom.makeRoom(this, rand, currentRoom.posX + offsetX, y, currentRoom.posZ + offsetZ, entranceDir.getOpposite());// this.getOppositeDir(entranceDir));
                if (i == length - 1)
                {
                    possibleRoom = DungeonRoom.makeBossRoom(this, rand, currentRoom.posX + offsetX, y, currentRoom.posZ + offsetZ, entranceDir.getOpposite()); // this.getOppositeDir(entranceDir));
                }
                if (i == length)
                {
                    possibleRoom = DungeonRoom.makeTreasureRoom(this, rand, currentRoom.posX + offsetX, y, currentRoom.posZ + offsetZ, entranceDir.getOpposite()); // this.getOppositeDir(entranceDir));
                }
                final DungeonBoundingBox possibleRoomBb = possibleRoom.getBoundingBox();
                final DungeonBoundingBox currentRoomBb = currentRoom.getBoundingBox();
                if (!this.isIntersecting(possibleRoomBb, boundingBoxes))
                {
                    final int curCenterX = (currentRoomBb.minX + currentRoomBb.maxX) / 2;
                    final int curCenterZ = (currentRoomBb.minZ + currentRoomBb.maxZ) / 2;
                    final int possibleCenterX = (possibleRoomBb.minX + possibleRoomBb.maxX) / 2;
                    final int possibleCenterZ = (possibleRoomBb.minZ + possibleRoomBb.maxZ) / 2;
                    final int corridorX = this.clamp((curCenterX + possibleCenterX) / 2, Math.max(currentRoomBb.minX + 1, possibleRoomBb.minX + 1), Math.min(currentRoomBb.maxX - 1, possibleRoomBb.maxX - 1));
                    final int corridorZ = this.clamp((curCenterZ + possibleCenterZ) / 2, Math.max(currentRoomBb.minZ + 1, possibleRoomBb.minZ + 1), Math.min(currentRoomBb.maxZ - 1, possibleRoomBb.maxZ - 1));
                    if (offsetX == 0 || offsetZ == 0) // Only 1 hallway
                    {
                        DungeonBoundingBox corridor1 = null;
                        switch (dir)
                        // East = 0, North = 1, South = 2, West = 3
                        {
                        case EAST: // East z++
                            corridor1 = new DungeonBoundingBox(corridorX - 1, currentRoomBb.maxZ, corridorX, possibleRoomBb.minZ - 1);
                            break;
                        case NORTH: // North x++
                            corridor1 = new DungeonBoundingBox(currentRoomBb.maxX, corridorZ - 1, possibleRoomBb.minX - 1, corridorZ);
                            break;
                        case SOUTH: // South x--
                            corridor1 = new DungeonBoundingBox(possibleRoomBb.maxX, corridorZ - 1, currentRoomBb.minX - 1, corridorZ);
                            break;
                        case WEST: // West z--
                            corridor1 = new DungeonBoundingBox(corridorX - 1, possibleRoomBb.maxZ, corridorX, currentRoomBb.minZ - 1);
                            break;
                        default:
                            break;
                        }
                        if (corridor1 != null && !this.isIntersecting(corridor1, boundingBoxes) && !corridor1.isOverlapping(possibleRoomBb))
                        {
                            boundingBoxes.add(possibleRoomBb);
                            boundingBoxes.add(corridor1);
                            currentRoom = possibleRoom;
                            currentRoom.generate(blocks, metas, chunkX, chunkZ);
                            this.rooms.add(currentRoom);
                            this.genCorridor(corridor1, rand, possibleRoom.posY, chunkX, chunkZ, dir, blocks, metas, false);
                            break;
                        }
                    }
                    else
                    // Two Hallways
                    {
                        DungeonBoundingBox corridor1 = null;
                        DungeonBoundingBox corridor2 = null;
                        ForgeDirection dir2 = ForgeDirection.EAST;
                        int extraLength = 0;
                        if (rand.nextInt(6) == 0)
                        {
                            extraLength = rand.nextInt(7);
                        }
                        switch (dir)
                        // East = 0, North = 1, South = 2, West = 3
                        {
                        case EAST: // East z++
                            corridor1 = new DungeonBoundingBox(curCenterX - 1, currentRoomBb.maxZ, curCenterX + 1, possibleCenterZ - 1);
                            if (offsetX > 0) // x++
                            {
                                corridor2 = new DungeonBoundingBox(corridor1.minX - extraLength, corridor1.maxZ + 1, possibleRoomBb.minX, corridor1.maxZ + 3);
                                dir2 = ForgeDirection.NORTH;
                            }
                            else
                            // x--
                            {
                                corridor2 = new DungeonBoundingBox(possibleRoomBb.maxX, corridor1.maxZ + 1, corridor1.maxX + extraLength, corridor1.maxZ + 3);
                                dir2 = ForgeDirection.SOUTH;
                            }
                            break;
                        case NORTH: // North x++
                            corridor1 = new DungeonBoundingBox(currentRoomBb.maxX, curCenterZ - 1, possibleCenterX - 1, curCenterZ + 1);
                            if (offsetZ > 0) // z++
                            {
                                corridor2 = new DungeonBoundingBox(corridor1.maxX + 1, corridor1.minZ - extraLength, corridor1.maxX + 4, possibleRoomBb.minZ);
                                dir2 = ForgeDirection.EAST;
                            }
                            else
                            // z--
                            {
                                corridor2 = new DungeonBoundingBox(corridor1.maxX + 1, possibleRoomBb.maxZ, corridor1.maxX + 4, corridor1.maxZ + extraLength);
                                dir2 = ForgeDirection.WEST;
                            }
                            break;
                        case SOUTH: // South x--
                            corridor1 = new DungeonBoundingBox(possibleCenterX + 1, curCenterZ - 1, currentRoomBb.minX - 1, curCenterZ + 1);
                            if (offsetZ > 0) // z++
                            {
                                corridor2 = new DungeonBoundingBox(corridor1.minX - 3, corridor1.minZ - extraLength, corridor1.minX - 1, possibleRoomBb.minZ);
                                dir2 = ForgeDirection.EAST;
                            }
                            else
                            // z--
                            {
                                corridor2 = new DungeonBoundingBox(corridor1.minX - 3, possibleRoomBb.maxZ, corridor1.minX - 1, corridor1.maxZ + extraLength);
                                dir2 = ForgeDirection.WEST;
                            }
                            break;
                        case WEST: // West z--
                            corridor1 = new DungeonBoundingBox(curCenterX - 1, possibleCenterZ + 1, curCenterX + 1, currentRoomBb.minZ - 1);
                            if (offsetX > 0) // x++
                            {
                                corridor2 = new DungeonBoundingBox(corridor1.minX - extraLength, corridor1.minZ - 3, possibleRoomBb.minX, corridor1.minZ - 1);
                                dir2 = ForgeDirection.NORTH;
                            }
                            else
                            // x--
                            {
                                corridor2 = new DungeonBoundingBox(possibleRoomBb.maxX, corridor1.minZ - 3, corridor1.maxX + extraLength, corridor1.minZ - 1);
                                dir2 = ForgeDirection.SOUTH;
                            }
                            break;
                        default:
                            break;
                        }
                        if (corridor1 != null && corridor2 != null && !this.isIntersecting(corridor1, boundingBoxes) && !this.isIntersecting(corridor2, boundingBoxes) && !corridor1.isOverlapping(possibleRoomBb) && !corridor2.isOverlapping(possibleRoomBb))
                        {
                            boundingBoxes.add(possibleRoomBb);
                            boundingBoxes.add(corridor1);
                            boundingBoxes.add(corridor2);
                            currentRoom = possibleRoom;
                            currentRoom.generate(blocks, metas, chunkX, chunkZ);
                            this.rooms.add(currentRoom);
                            this.genCorridor(corridor2, rand, possibleRoom.posY, chunkX, chunkZ, dir2, blocks, metas, true);
                            this.genCorridor(corridor1, rand, possibleRoom.posY, chunkX, chunkZ, dir, blocks, metas, false);
                            break;
                        }
                        else
                        {
                        }
                    }
                }
                else
                {
                }
            }
        }
    }

    private void genCorridor(DungeonBoundingBox corridor, Random rand, int y, int cx, int cz, ForgeDirection dir, Block[] blocks, byte[] metas, boolean doubleCorridor)
    {
        for (int i = corridor.minX - 1; i <= corridor.maxX + 1; i++)
        {
            for (int k = corridor.minZ - 1; k <= corridor.maxZ + 1; k++)
            {
                loopj:
                for (int j = y - 1; j <= y + this.HALLWAY_HEIGHT; j++)
                {
                    boolean flag = false;
                    int flag2 = -1;

                    switch (dir)
                    {
                    case EAST:
                        if (k == corridor.minZ - 1 && !doubleCorridor || k == corridor.maxZ + 1)
                        {
                            break loopj;
                        }
                        if (doubleCorridor && k == corridor.minZ - 1)
                        {
                            flag = true;
                        }
                        if (i == corridor.minX - 1 || i == corridor.maxX + 1 || j == y - 1 || j == y + this.HALLWAY_HEIGHT)
                        {
                            flag = true;
                        }
                        if ((i == corridor.minX || i == corridor.maxX) && k % 4 == 0 && j == y + 2)
                        {
                            flag2 = i == corridor.minX ? 2 : 1;
                        }
                        break;
                    case WEST:
                        if (k == corridor.minZ - 1 || k == corridor.maxZ + 1 && !doubleCorridor)
                        {
                            break loopj;
                        }
                        if (doubleCorridor && k == corridor.maxX + 1)
                        {
                            flag = true;
                        }
                        if (i == corridor.minX - 1 || i == corridor.maxX + 1 || j == y - 1 || j == y + this.HALLWAY_HEIGHT)
                        {
                            flag = true;
                        }
                        if ((i == corridor.minX || i == corridor.maxX) && k % 4 == 0 && j == y + 2)
                        {
                            flag2 = i == corridor.minX ? 2 : 1;
                        }
                        break;
                    case NORTH:
                        if (i == corridor.minX - 1 && !doubleCorridor || i == corridor.maxX + 1)
                        {
                            break loopj;
                        }
                        if (i == corridor.minX - 1)
                        {
                            flag = true;
                        }
                        if (k == corridor.minZ - 1 || k == corridor.maxZ + 1 || j == y - 1 || j == y + this.HALLWAY_HEIGHT)
                        {
                            flag = true;
                        }
                        if ((k == corridor.minZ || k == corridor.maxZ) && i % 4 == 0 && j == y + 2)
                        {
                            flag2 = k == corridor.minZ ? 4 : 3;
                        }
                        break;
                    case SOUTH:
                        if (i == corridor.minX - 1 || i == corridor.maxX + 1 && !doubleCorridor)
                        {
                            break loopj;
                        }
                        if (i == corridor.maxX + 1)
                        {
                            flag = true;
                        }
                        if (k == corridor.minZ - 1 || k == corridor.maxZ + 1 || j == y - 1 || j == y + this.HALLWAY_HEIGHT)
                        {
                            flag = true;
                        }
                        if ((k == corridor.minZ || k == corridor.maxZ) && i % 4 == 0 && j == y + 2)
                        {
                            flag2 = k == corridor.minZ ? 4 : 3;
                        }
                        break;
                    default:
                        break;
                    }

                    if (!flag)
                    {
                        if (flag2 != -1)
                        {
                        	if (OxygenUtil.noAtmosphericCombustion(this.worldObj.provider))
                        	{
                                this.placeBlock(blocks, metas, i, j, k, cx, cz, GCBlocks.unlitTorch, 0);
                                this.worldObj.scheduleBlockUpdateWithPriority(i, j, k, GCBlocks.unlitTorch, 40, 0);
                        	}
                        	else
                        	{
                                this.placeBlock(blocks, metas, i, j, k, cx, cz, Blocks.torch, 0);
                                this.worldObj.scheduleBlockUpdateWithPriority(i, j, k, Blocks.torch, 40, 0);
                        	}
                        }
                        else
                        {
                            this.placeBlock(blocks, metas, i, j, k, cx, cz, Blocks.air, 0);
                        }
                    }
                    else
                    {
                        this.placeBlock(blocks, metas, i, j, k, cx, cz, this.DUNGEON_WALL_ID, this.DUNGEON_WALL_META);
                    }
                }
            }
        }
    }

    public void handleTileEntities(Random rand)
    {
        final ArrayList<DungeonRoom> rooms = new ArrayList<DungeonRoom>();
        rooms.addAll(this.rooms);
        this.rooms.clear();
        for (final DungeonRoom room : rooms)
        {
            room.handleTileEntities(rand);
        }
    }

    protected boolean canGenDungeonAtCoords(long worldSeed, int i, int j)
    {
        final byte numChunks = 44;
        final byte offsetChunks = 0;
        final int oldi = i;
        final int oldj = j;

        if (i < 0)
        {
            i -= numChunks - 1;
        }

        if (j < 0)
        {
            j -= numChunks - 1;
        }

        int randX = i / numChunks;
        int randZ = j / numChunks;
        final long dungeonSeed = randX * 341873128712L + randZ * 132897987541L + worldSeed + 4291726;
        final Random rand = new Random(dungeonSeed);
        randX *= numChunks;
        randZ *= numChunks;
        randX += rand.nextInt(numChunks - offsetChunks);
        randZ += rand.nextInt(numChunks - offsetChunks);

        return oldi == randX && oldj == randZ;

    }

    public void generateEntranceCrater(Block[] blocks, byte[] meta, int x, int y, int z, int cx, int cz)
    {
        final int range = 18;
        int maxLevel = 0;

        for (int i = -range; i <= range; i++)
        {
            for (int k = -range; k <= range; k++)
            {

                int j = 200;

                while (j > 0)
                {
                    j--;

                    Block block = this.getBlock(blocks, x + i, j, z + k, cx + i / 16, cz + k / 16);

                    if (Blocks.air != block && block != null)
                    {
                        break;
                    }
                }

                maxLevel = Math.max(maxLevel, j);
            }
        }

        for (int i = x - range; i < x + range; i++)
        {
            for (int k = z - range; k < z + range; k++)
            {
                final double xDev = (i - x) / 10D;
                final double zDev = (k - z) / 10D;
                final double distance = xDev * xDev + zDev * zDev;
                final int depth = (int) Math.abs(1 / (distance + .00001D));
                int helper = 0;
                for (int j = maxLevel + 3; j > 0; j--)
                {
                    if ((Blocks.air != this.getBlock(blocks, i, j - 1, k, cx, cz) || this.getBlock(blocks, i, j, k, cx, cz) == this.DUNGEON_WALL_ID) && helper <= depth)
                    {
                        this.placeBlock(blocks, meta, i, j, k, cx, cz, Blocks.air, 0);
                        helper++;
                    }
                    if (helper > depth || j <= y + 1)
                    {
                        break;
                    }
                }
            }
        }
    }

    public ChunkCoordinates getDungeonNear(long worldSeed, int i, int j)
    {
        final int range = 16;
        for (int x = i - range; x <= i + range; x++)
        {
            for (int z = j - range; z <= j + range; z++)
            {
                if (this.canGenDungeonAtCoords(worldSeed, x, z))
                {
                    return new ChunkCoordinates(x * 16 + 8, 0, z * 16 + 8);
                }
            }
        }

        return null;
    }

    private void placeBlock(Block[] blocks, byte[] metas, int x, int y, int z, int cx, int cz, Block id, int meta)
    {
        if (MapGenDungeon.useArrays)
        {
            cx *= 16;
            cz *= 16;
            x -= cx;
            z -= cz;
            if (x < 0 || x >= 16 || z < 0 || z >= 16)
            {
                return;
            }
            final int index = this.getIndex(x, y, z);
            blocks[index] = id;
            metas[index] = (byte) meta;
        }
        else
        {
            this.worldObj.setBlock(x, y, z, id, meta, 0);
        }
    }

    private Block getBlock(Block[] blocks, int x, int y, int z, int cx, int cz)
    {
        if (MapGenDungeon.useArrays)
        {
            cx *= 16;
            cz *= 16;
            x -= cx;
            z -= cz;
            if (x < 0 || x >= 16 || z < 0 || z >= 16)
            {
                return Blocks.air;
            }
            return blocks[this.getIndex(x, y, z)];
        }
        else
        {
            return this.worldObj.getBlock(x, y, z);
        }
    }

    private int getIndex(int x, int y, int z)
    {
        return (x * 16 + z) * 256 + y;
    }

    private ForgeDirection randDir(Random rand)
    {
        return ForgeDirection.values()[rand.nextInt(ForgeDirection.VALID_DIRECTIONS.length)];
    }

    private boolean isIntersecting(DungeonBoundingBox bb, List<DungeonBoundingBox> dungeonBbs)
    {
        for (final DungeonBoundingBox bb2 : dungeonBbs)
        {
            if (bb.isOverlapping(bb2))
            {
                return true;
            }
        }
        return false;
    }

    private int clamp(int x, int min, int max)
    {
        if (x < min)
        {
            return min;
        }
        else if (x > max)
        {
            return max;
        }
        else
        {
            return x;
        }
    }

}
