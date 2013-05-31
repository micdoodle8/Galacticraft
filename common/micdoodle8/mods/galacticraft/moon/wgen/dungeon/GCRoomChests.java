package micdoodle8.mods.galacticraft.moon.wgen.dungeon;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class GCRoomChests extends GCDungeonRoom
{

    int sizeX;
    int sizeY;
    int sizeZ;

    private final ArrayList<ChunkCoordinates> chests = new ArrayList<ChunkCoordinates>();

    public GCRoomChests(World worldObj, int posX, int posY, int posZ, int entranceDir)
    {
        super(worldObj, posX, posY, posZ, entranceDir);
        if (worldObj != null)
        {
            final Random rand = new Random(worldObj.getSeed() * posX * posY * 57 * posZ);
            this.sizeX = rand.nextInt(5) + 6;
            this.sizeY = rand.nextInt(2) + 4;
            this.sizeZ = rand.nextInt(5) + 6;
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
        final int hx = (this.posX + this.posX + this.sizeX) / 2;
        final int hz = (this.posZ + this.posZ + this.sizeZ) / 2;
        if (this.placeBlock(chunk, meta, hx, this.posY, hz, cx, cz, Block.chest.blockID, 0))
        {
            this.chests.add(new ChunkCoordinates(hx, this.posY, hz));
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
        return new GCRoomChests(worldObj, x, y, z, dir);
    }

    @Override
    protected void handleTileEntities(Random rand)
    {
        for (final ChunkCoordinates chestCoords : this.chests)
        {
            final TileEntityChest chest = (TileEntityChest) this.worldObj.getBlockTileEntity(chestCoords.posX, chestCoords.posY, chestCoords.posZ);
            if (chest != null)
            {
                final int amountOfGoodies = rand.nextInt(5) + 2;
                for (int i = 0; i < amountOfGoodies; i++)
                {
                    chest.setInventorySlotContents(rand.nextInt(chest.getSizeInventory()), this.getLoot(rand));
                }
            }
        }
    }

    private ItemStack getLoot(Random rand)
    {
        return new ItemStack(Item.appleRed, 1, 0);
    }
}
