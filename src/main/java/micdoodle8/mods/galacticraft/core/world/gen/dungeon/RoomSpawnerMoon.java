package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Random;

public class RoomSpawnerMoon extends DungeonRoom
{
    int sizeX;
    int sizeY;
    int sizeZ;
    Random rand;

    private final ArrayList<ChunkCoordinates> spawners = new ArrayList<ChunkCoordinates>();

    public RoomSpawnerMoon(MapGenDungeon dungeon, int posX, int posY, int posZ, ForgeDirection entranceDir)
    {
        super(dungeon, posX, posY, posZ, entranceDir);
        if (this.worldObj != null)
        {
            this.rand = new Random(this.worldObj.getSeed() * posX * posY * 57 * posZ);
            this.sizeX = this.rand.nextInt(5) + 6;
            this.sizeY = this.rand.nextInt(2) + 4;
            this.sizeZ = this.rand.nextInt(5) + 6;
        }
    }

    @Override
    public void generate(Block[] chunk, byte[] meta, int cx, int cz)
    {
        for (int i = this.posX - 1; i <= this.posX + this.sizeX; i++)
        {
            for (int j = this.posY - 1; j <= this.posY + this.sizeY; j++)
            {
                for (int k = this.posZ - 1; k <= this.posZ + this.sizeZ; k++)
                {
                    if (i == this.posX - 1 || i == this.posX + this.sizeX || j == this.posY - 1 || j == this.posY + this.sizeY || k == this.posZ - 1 || k == this.posZ + this.sizeZ)
                    {
                        this.placeBlock(chunk, meta, i, j, k, cx, cz, this.dungeonInstance.DUNGEON_WALL_ID, this.dungeonInstance.DUNGEON_WALL_META);
                    }
                    else
                    {
                        this.placeBlock(chunk, meta, i, j, k, cx, cz, Blocks.air, 0);
                        if (this.rand.nextFloat() < 0.05F)
                        {
                            this.placeBlock(chunk, meta, i, j, k, cx, cz, Blocks.web, 0);
                        }
                    }
                }
            }
        }
        if (this.placeBlock(chunk, meta, this.posX + 1, this.posY - 1, this.posZ + 1, cx, cz, Blocks.mob_spawner, 0))
        {
            this.spawners.add(new ChunkCoordinates(this.posX + 1, this.posY - 1, this.posZ + 1));
        }
        if (this.placeBlock(chunk, meta, this.posX + this.sizeX - 1, this.posY - 1, this.posZ + this.sizeZ - 1, cx, cz, Blocks.mob_spawner, 0))
        {
            this.spawners.add(new ChunkCoordinates(this.posX + this.sizeX - 1, this.posY - 1, this.posZ + this.sizeZ - 1));
        }
    }

    @Override
    public DungeonBoundingBox getBoundingBox()
    {
        return new DungeonBoundingBox(this.posX, this.posZ, this.posX + this.sizeX, this.posZ + this.sizeZ);
    }

    @Override
    protected DungeonRoom makeRoom(MapGenDungeon dungeon, int x, int y, int z, ForgeDirection dir)
    {
        return new RoomSpawnerMoon(dungeon, x, y, z, dir);
    }

    @Override
    protected void handleTileEntities(Random rand)
    {
        for (final ChunkCoordinates spawnerCoords : this.spawners)
        {
            if (this.worldObj.getBlock(spawnerCoords.posX, spawnerCoords.posY, spawnerCoords.posZ) == Blocks.mob_spawner)
            {
                final TileEntityMobSpawner spawner = (TileEntityMobSpawner) this.worldObj.getTileEntity(spawnerCoords.posX, spawnerCoords.posY, spawnerCoords.posZ);
                if (spawner != null)
                {
                    spawner.func_145881_a().setEntityName(RoomSpawnerMoon.getMob(rand));
                }
            }
        }
    }

    private static String getMob(Random rand)
    {
        switch (rand.nextInt(4))
        {
        case 0:
            return "EvolvedSpider";
        case 1:
            return "EvolvedZombie";
        case 2:
            return "EvolvedCreeper";
        case 3:
            return "EvolvedSkeleton";
        default:
            return "EvolvedZombie";
        }
    }
}
