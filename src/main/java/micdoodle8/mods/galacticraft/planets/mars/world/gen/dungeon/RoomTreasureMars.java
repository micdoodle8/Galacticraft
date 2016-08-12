package micdoodle8.mods.galacticraft.planets.mars.world.gen.dungeon;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonBoundingBox;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonRoom;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.HashSet;
import java.util.Random;

public class RoomTreasureMars extends DungeonRoom
{
    int sizeX;
    int sizeY;
    int sizeZ;

    private final HashSet<BlockPos> chests = new HashSet<BlockPos>();

    public RoomTreasureMars(MapGenDungeon dungeon, int posX, int posY, int posZ, EnumFacing entranceDir)
    {
        super(dungeon, posX, posY, posZ, entranceDir);
        if (this.worldObj != null)
        {
            final Random rand = new Random(this.worldObj.getSeed() * posX * posY * 57 * posZ);
            this.sizeX = rand.nextInt(6) + 7;
            this.sizeY = rand.nextInt(2) + 8;
            this.sizeZ = rand.nextInt(6) + 7;
        }
    }

    @Override
    public void generate(ChunkPrimer primer, int cx, int cz)
    {
        for (int i = this.posX - 1; i <= this.posX + this.sizeX; i++)
        {
            for (int k = this.posZ - 1; k <= this.posZ + this.sizeZ; k++)
            {
                for (int j = this.posY - 1; j <= this.posY + this.sizeY; j++)
                {
                    if (i == this.posX - 1 || i == this.posX + this.sizeX || j == this.posY - 1 || j == this.posY + this.sizeY || k == this.posZ - 1 || k == this.posZ + this.sizeZ)
                    {
                        this.placeBlock(primer, i, j, k, cx, cz, this.dungeonInstance.DUNGEON_WALL_ID, this.dungeonInstance.DUNGEON_WALL_META);
                    }
                    else
                    {
                        if ((i == this.posX || i == this.posX + this.sizeX - 1) && (k == this.posZ || k == this.posZ + this.sizeZ - 1))
                        {
                            this.placeBlock(primer, i, j, k, cx, cz, Blocks.glowstone, 0);
                        }
                        else
                        {
                            this.placeBlock(primer, i, j, k, cx, cz, Blocks.air, 0);
                        }
                    }
                }
            }
        }
        final int hx = (this.posX + this.posX + this.sizeX) / 2;
        final int hz = (this.posZ + this.posZ + this.sizeZ) / 2;
        if (this.placeBlock(primer, hx, this.posY, hz, cx, cz, MarsBlocks.tier2TreasureChest, 0))
        {
            this.chests.add(new BlockPos(hx, this.posY, hz));
        }
    }

    @Override
    public DungeonBoundingBox getCollisionBoundingBox()
    {
        return new DungeonBoundingBox(this.posX, this.posZ, this.posX + this.sizeX, this.posZ + this.sizeZ);
    }

    @Override
    protected DungeonRoom makeRoom(MapGenDungeon dungeon, int x, int y, int z, EnumFacing dir)
    {
        return new RoomTreasureMars(dungeon, x, y, z, dir);
    }

    @Override
    protected void handleTileEntities(Random rand)
    {
        if (!this.chests.isEmpty())
        {
            HashSet<BlockPos> removeList = new HashSet<BlockPos>();

            for (BlockPos coords : this.chests)
            {
                this.worldObj.setBlockState(coords, MarsBlocks.tier2TreasureChest.getDefaultState(), 3);
                this.worldObj.setTileEntity(coords, new TileEntityTreasureChestMars());
                removeList.add(coords);
            }

            this.chests.removeAll(removeList);
        }
    }
}
