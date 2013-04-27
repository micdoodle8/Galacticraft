package micdoodle8.mods.galacticraft.moon.wgen.dungeon;

import java.util.ArrayList;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class GCRoomTreasure extends GCDungeonRoom {

	int sizeX;
	int sizeY;
	int sizeZ;

	private ArrayList<ChunkCoordinates> chests = new ArrayList<ChunkCoordinates>();
	
	public GCRoomTreasure(World worldObj, int posX, int posY, int posZ, int entranceDir) {
		super(worldObj, posX, posY, posZ, entranceDir);
		if(worldObj != null)
		{
			Random rand = new Random(worldObj.getSeed() * posX * posY * 57 * posZ);
			sizeX = rand.nextInt(6) + 7;
			sizeY = rand.nextInt(2) + 5;
			sizeZ = rand.nextInt(6) + 7;
		}
	}

	@Override
	public void generate(int[] chunk, int[] meta, int cx, int cz) {
		for(int i = posX - 1; i <= posX + sizeX; i++)
		{
			for(int k = posZ - 1; k <= posZ + sizeZ; k++)
			{
				for(int j = posY - 1; j <= posY + sizeY; j++)
				{
					if(i == posX - 1 || i == posX + sizeX || j == posY - 1 || j == posY + sizeY || k == posZ - 1 || k == posZ + sizeZ)
					{
						placeBlock(chunk, meta, i, j, k, cx, cz, GCMapGenDungeon.DUNGEON_WALL_ID, GCMapGenDungeon.DUNGEON_WALL_META);
					}
					else
					{
						if((i == posX || i == posX + sizeX - 1) && (k == posZ || k == posZ + sizeZ - 1))
						{
							placeBlock(chunk, meta, i, j, k, cx, cz, Block.glowStone.blockID, 0);
						}
						else
						{
							placeBlock(chunk, meta, i, j, k, cx, cz, 0, 0);
						}
					}
				}
			}
		}
		int hx = (posX + posX + sizeX) / 2;
		int hz = (posZ + posZ + sizeZ) / 2;
		if(placeBlock(chunk, meta, hx, posY, hz, cx, cz, GCCoreBlocks.treasureChest.blockID, 0))
		{
			chests.add(new ChunkCoordinates(hx, posY, hz));
		}
	}

	@Override
	public GCDungeonBoundingBox getBoundingBox() {
		return new GCDungeonBoundingBox(posX, posZ, posX + sizeX, posZ + sizeZ);
	}

	@Override
	protected GCDungeonRoom makeRoom(World worldObj, int x, int y, int z, int dir) {
		return new GCRoomTreasure(worldObj, x, y, z, dir);
	}

	@Override
	protected void handleTileEntities(Random rand) {
		for(ChunkCoordinates chestCoords : chests)
		{
			TileEntity chest = worldObj.getBlockTileEntity(chestCoords.posX, chestCoords.posY, chestCoords.posZ);
			if(chest != null && chest instanceof IInventory)
			{
				int amountOfGoodies = rand.nextInt(5) + 2;
				for(int i = 0; i < amountOfGoodies; i++)
				{
					((IInventory)chest).setInventorySlotContents(rand.nextInt(((IInventory)chest).getSizeInventory()), getLoot(rand));
				}
			}			
		}		
	}

	private ItemStack getLoot(Random rand) 
	{
		return new ItemStack(Item.appleRed, 1, 0);
	}

}
