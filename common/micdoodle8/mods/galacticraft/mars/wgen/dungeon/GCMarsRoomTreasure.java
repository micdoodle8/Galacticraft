package micdoodle8.mods.galacticraft.mars.wgen.dungeon;

import java.util.ArrayList;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.wgen.dungeon.GCCoreDungeonBoundingBox;
import micdoodle8.mods.galacticraft.core.wgen.dungeon.GCCoreDungeonRoom;
import micdoodle8.mods.galacticraft.core.wgen.dungeon.GCCoreMapGenDungeon;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;

public class GCMarsRoomTreasure extends GCCoreDungeonRoom
{

    int sizeX;
    int sizeY;
    int sizeZ;

    private final ArrayList<ChunkCoordinates> chests = new ArrayList<ChunkCoordinates>();

    public GCMarsRoomTreasure(GCCoreMapGenDungeon dungeon, int posX, int posY, int posZ, ForgeDirection entranceDir)
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
    public void generate(short[] chunk, byte[] meta, int cx, int cz)
    {
        for (int i = this.posX - 1; i <= this.posX + this.sizeX; i++)
        {
            for (int k = this.posZ - 1; k <= this.posZ + this.sizeZ; k++)
            {
                for (int j = this.posY - 1; j <= this.posY + this.sizeY; j++)
                {
                    if (i == this.posX - 1 || i == this.posX + this.sizeX || j == this.posY - 1 || j == this.posY + this.sizeY || k == this.posZ - 1 || k == this.posZ + this.sizeZ)
                    {
                        this.placeBlock(chunk, meta, i, j, k, cx, cz, this.dungeonInstance.DUNGEON_WALL_ID, this.dungeonInstance.DUNGEON_WALL_META);
                    }
                    else
                    {
                        if ((i == this.posX || i == this.posX + this.sizeX - 1) && (k == this.posZ || k == this.posZ + this.sizeZ - 1))
                        {
                            this.placeBlock(chunk, meta, i, j, k, cx, cz, Block.glowStone.blockID, 0);
                        }
                        else
                        {
                            this.placeBlock(chunk, meta, i, j, k, cx, cz, 0, 0);
                        }
                    }
                }
            }
        }
        final int hx = (this.posX + this.posX + this.sizeX) / 2;
        final int hz = (this.posZ + this.posZ + this.sizeZ) / 2;
        if (this.placeBlock(chunk, meta, hx, this.posY, hz, cx, cz, GCMarsBlocks.tier2TreasureChest.blockID, 0))
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
        return new GCMarsRoomTreasure(dungeon, x, y, z, dir);
    }

    @Override
    protected void handleTileEntities(Random rand)
    {
        for (final ChunkCoordinates chestCoords : this.chests)
        {
            final TileEntity chest = this.worldObj.getBlockTileEntity(chestCoords.posX, chestCoords.posY, chestCoords.posZ);
            if (chest != null && chest instanceof IInventory)
            {
                final int amountOfGoodies = rand.nextInt(5) + 2;
                for (int i = 0; i < amountOfGoodies; i++)
                {
                    ((IInventory) chest).setInventorySlotContents(rand.nextInt(((IInventory) chest).getSizeInventory()), this.getLoot(rand));
                }
            }
        }
    }

    private ItemStack getLoot(Random rand)
    {
        return null;
    }

}
