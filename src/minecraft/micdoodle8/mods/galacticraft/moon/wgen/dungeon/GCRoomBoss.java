package micdoodle8.mods.galacticraft.moon.wgen.dungeon;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class GCRoomBoss extends GCDungeonRoom {
	
	int sizeX;
	int sizeY;
	int sizeZ;
	Random rand;
	ChunkCoordinates spawnerCoords;
	
	public GCRoomBoss(World worldObj, int posX, int posY, int posZ, int entranceDir) {
		super(worldObj, posX, posY, posZ, entranceDir);
		if(worldObj != null)
		{
			rand = new Random(worldObj.getSeed() * posX * posY * 57 * posZ);
			sizeX = rand.nextInt(6) + 14;
			sizeY = rand.nextInt(2) + 8;
			sizeZ = rand.nextInt(6) + 14;
		}
	}

	@Override
	public void generate(int[] chunk, int[] meta, int cx, int cz) {
		for(int i = posX - 1; i <= posX + sizeX; i++)
		{
			for(int k = posZ - 1; k <= posZ + sizeZ; k++)
			{ 
				boolean flag = rand.nextFloat() < 0.50F;
				for(int j = posY - 1; j <= posY + sizeY; j++)
				{
					if(i == posX - 1 || i == posX + sizeX || j == posY - 1 || j == posY + sizeY || k == posZ - 1 || k == posZ + sizeZ)
					{
						placeBlock(chunk, meta, i, j, k, cx, cz, GCMapGenDungeon.DUNGEON_WALL_ID, GCMapGenDungeon.DUNGEON_WALL_META);
					}
					else if ((i == posX && k == posZ) || (i == posX + sizeX - 1 && k == posZ) || (i == posX && k == posZ + sizeZ - 1) || (i == posX + sizeX - 1 && k == posZ + sizeZ - 1))
					{
						placeBlock(chunk, meta, i, j, k, cx, cz, Block.lavaMoving.blockID, 0);
					}
					else if (((j % 3 == 0 && j >= posY + 2) && ((i == posX || i == posX + sizeX - 1 || k == posZ || k == posZ + sizeZ - 1)))
							|| ((i == posX + 1 && k == posZ) || (i == posX && k == posZ + 1) || (i == posX + sizeX - 2 && k == posZ) || (i == posX + sizeX - 1 && k == posZ + 1) || (i == posX + 1 && k == posZ + sizeZ - 1) || (i == posX && k == posZ + sizeZ - 2) || (i == posX + sizeX - 2 && k == posZ + sizeZ - 1) || (i == posX + sizeX - 1 && k == posZ + sizeZ - 2)))
					{
						placeBlock(chunk, meta, i, j, k, cx, cz, Block.fenceIron.blockID, 0);
					}
					else if (((i == posX + 1 && k == posZ + 1) || (i == posX + sizeX - 2 && k == posZ + 1) || (i == posX + 1 && k == posZ + sizeZ - 2) || (i == posX + sizeX - 2 && k == posZ + sizeZ - 2)) && j % 3 == 0)
					{
						placeBlock(chunk, meta, i, j, k, cx, cz, Block.fenceIron.blockID, 0);
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
		spawnerCoords = new ChunkCoordinates(hx, posY + 2, hz);
	}

	@Override
	public GCDungeonBoundingBox getBoundingBox() {
		return new GCDungeonBoundingBox(posX, posZ, posX + sizeX, posZ + sizeZ);
	}

	@Override
	protected GCDungeonRoom makeRoom(World worldObj, int x, int y, int z, int dir) {
		return new GCRoomBoss(worldObj, x, y, z, dir);
	}

	@Override
	protected void handleTileEntities(Random rand)
	{
		if (this.spawnerCoords == null)
		{
			return;
		}
		
		this.worldObj.setBlock(spawnerCoords.posX, spawnerCoords.posY, spawnerCoords.posZ, GCMoonBlocks.blockMoon.blockID, 15, 3);
		
		TileEntity tile = this.worldObj.getBlockTileEntity(spawnerCoords.posX, spawnerCoords.posY, spawnerCoords.posZ);
		
		if (tile == null || !(tile instanceof GCCoreTileEntityDungeonSpawner))
		{
			this.worldObj.setBlockTileEntity(spawnerCoords.posX, spawnerCoords.posY, spawnerCoords.posZ, new GCCoreTileEntityDungeonSpawner());
		}
	}
	
}
