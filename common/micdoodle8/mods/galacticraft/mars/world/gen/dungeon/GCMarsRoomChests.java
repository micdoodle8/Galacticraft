package micdoodle8.mods.galacticraft.mars.world.gen.dungeon;

import java.util.ArrayList;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreDungeonBoundingBox;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreDungeonRoom;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreMapGenDungeon;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;

public class GCMarsRoomChests extends GCCoreDungeonRoom
{

    int sizeX;
    int sizeY;
    int sizeZ;

    private final ArrayList<ChunkCoordinates> chests = new ArrayList<ChunkCoordinates>();

    public GCMarsRoomChests(GCCoreMapGenDungeon dungeon, int posX, int posY, int posZ, ForgeDirection entranceDir)
    {
        super(dungeon, posX, posY, posZ, entranceDir);
        if (this.worldObj != null)
        {
            final Random rand = new Random(this.worldObj.getSeed() * posX * posY * 57 * posZ);
            this.sizeX = rand.nextInt(5) + 6;
            this.sizeY = rand.nextInt(2) + 7;
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
                        this.placeBlock(chunk, meta, i, j, k, cx, cz, this.dungeonInstance.DUNGEON_WALL_ID, this.dungeonInstance.DUNGEON_WALL_META);
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
    public GCCoreDungeonBoundingBox getBoundingBox()
    {
        return new GCCoreDungeonBoundingBox(this.posX, this.posZ, this.posX + this.sizeX, this.posZ + this.sizeZ);
    }

    @Override
    protected GCCoreDungeonRoom makeRoom(GCCoreMapGenDungeon dungeon, int x, int y, int z, ForgeDirection dir)
    {
        return new GCMarsRoomChests(dungeon, x, y, z, dir);
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
        if (rand.nextInt(27) < 5)
        {
            int r = rand.nextInt(5);

            switch (r)
            {
            case 0:
                return new ItemStack(Item.appleRed, 1, 0);
            case 1:
                return new ItemStack(Item.ingotIron, rand.nextInt(8) + 2, 0);
            case 2:
                return new ItemStack(Item.arrow, rand.nextInt(14) + 2, 0);
            case 3:
                return new ItemStack(GCMoonItems.meteoricIronRaw, 1 + rand.nextInt(2), 0);
            case 4:
                return new ItemStack(Item.bucketWater, rand.nextInt(2) + 1, 0);
            }
        }

        return null;
    }
}
