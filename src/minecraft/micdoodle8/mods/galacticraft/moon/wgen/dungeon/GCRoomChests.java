package micdoodle8.mods.galacticraft.moon.wgen.dungeon;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class GCRoomChests extends GCDungeonRoom {
	
	int sizeX;
	int sizeY;
	int sizeZ;
	
	private ArrayList<ChunkCoordinates> chests = new ArrayList<ChunkCoordinates>();
	
	public GCRoomChests(World worldObj, int posX, int posY, int posZ, int entranceDir) {
		super(worldObj, posX, posY, posZ, entranceDir);
		if(worldObj != null)
		{
			Random rand = new Random(worldObj.getSeed() * posX * posY * 57 * posZ);
			sizeX = rand.nextInt(5) + 6;
			sizeY = rand.nextInt(2) + 4;
			sizeZ = rand.nextInt(5) + 6;
		}
	}

	@Override
	public void generate(int[] chunk, int[] meta, int cx, int cz) {
		for(int i = posX - 1; i <= posX + sizeX; i++)
		{
			for(int j = posY - 1; j <= posY + sizeY; j++)
			{
				for(int k = posZ - 1; k <= posZ + sizeZ; k++)
				{
					if(i == posX - 1 || i == posX + sizeX || j == posY - 1 || j == posY + sizeY || k == posZ - 1 || k == posZ + sizeZ)
					{
						placeBlock(chunk, meta, i, j, k, cx, cz, GCMapGenDungeon.DUNGEON_WALL_ID, GCMapGenDungeon.DUNGEON_WALL_META);
					}
					else
					{
						placeBlock(chunk, meta, i, j, k, cx, cz, 0, 0);
					}
				}
			}
		}
		int hx = (posX + posX + sizeX) / 2;
		int hz = (posZ + posZ + sizeZ) / 2;
		if(placeBlock(chunk, meta, hx, posY, hz, cx, cz, Block.chest.blockID, 0))
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
		return new GCRoomChests(worldObj, x, y, z, dir);
	}

	@Override
	protected void handleTileEntities(Random rand) {
		for(ChunkCoordinates chestCoords : chests)
		{
			TileEntityChest chest = (TileEntityChest)worldObj.getBlockTileEntity(chestCoords.posX, chestCoords.posY, chestCoords.posZ);
			System.out.println("Chest");
			if(chest != null)
			{
				int amountOfGoodies = rand.nextInt(5) + 2;
				for(int i = 0; i < amountOfGoodies; i++)
				{
					chest.setInventorySlotContents(rand.nextInt(chest.getSizeInventory()), getLoot(rand));
				}
			}			
		}		
	}

	private ItemStack getLoot(Random rand) 
	{
		return new ItemStack(Item.appleRed, 1, 0);
	}
}
