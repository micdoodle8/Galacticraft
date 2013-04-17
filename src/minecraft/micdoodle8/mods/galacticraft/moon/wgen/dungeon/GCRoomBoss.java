package micdoodle8.mods.galacticraft.moon.wgen.dungeon;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class GCRoomBoss extends GCDungeonRoom {
	
	int sizeX;
	int sizeY;
	int sizeZ;
	Random rand;
	
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
					else
					{
						placeBlock(chunk, meta, i, j, k, cx, cz, 0, 0);
					}
				}
			}
		}
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
	protected void handleTileEntities(Random rand) {
		// TODO Auto-generated method stub
		
	}
	
}
