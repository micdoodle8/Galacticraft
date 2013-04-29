package micdoodle8.mods.galacticraft.moon.wgen.dungeon;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class GCRoomSpawner extends GCDungeonRoom {

	int sizeX;
	int sizeY;
	int sizeZ;
	Random rand;

	private ArrayList<ChunkCoordinates> spawners = new ArrayList<ChunkCoordinates>();	
	
	public GCRoomSpawner(World worldObj, int posX, int posY, int posZ, int entranceDir) {
		super(worldObj, posX, posY, posZ, entranceDir);
		if(worldObj != null)
		{
			rand = new Random(worldObj.getSeed() * posX * posY * 57 * posZ);
			sizeX = rand.nextInt(5) + 6;
			sizeY = rand.nextInt(2) + 4;
			sizeZ = rand.nextInt(5) + 6;
		}
	}

	@Override
	public void generate(short[] chunk, byte[] meta, int cx, int cz) {
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
						if(rand.nextFloat() < 0.05F)
						{
							placeBlock(chunk, meta, i, j, k, cx, cz, Block.web.blockID, 0);
						}
					}
				}
			}
		}
		if(placeBlock(chunk, meta, posX + 1, posY - 2, posZ + 1, cx, cz, Block.mobSpawner.blockID, 0))
		{
			spawners.add(new ChunkCoordinates(posX + 1, posY - 2, posZ + 1));
		}
		if(placeBlock(chunk, meta, posX + sizeX - 1, posY - 2, posZ + sizeZ - 1, cx, cz, Block.mobSpawner.blockID, 0))
		{
			spawners.add(new ChunkCoordinates(posX + sizeX - 1, posY - 2, posZ + sizeZ - 1));
		}
	}

	@Override
	public GCDungeonBoundingBox getBoundingBox() {
		return new GCDungeonBoundingBox(posX, posZ, posX + sizeX, posZ + sizeZ);
	}

	@Override
	protected GCDungeonRoom makeRoom(World worldObj, int x, int y, int z, int dir) {
		return new GCRoomSpawner(worldObj, x, y, z, dir);
	}

	@Override
	protected void handleTileEntities(Random rand) {
		for(ChunkCoordinates spawnerCoords : spawners)
		{
			TileEntityMobSpawner spawner = (TileEntityMobSpawner)worldObj.getBlockTileEntity(spawnerCoords.posX, spawnerCoords.posY, spawnerCoords.posZ);
			if(spawner != null)
			{
				spawner.func_98049_a().setMobID(getMob(rand));
			}
		}
	}
	
	private static String getMob(Random rand)
	{
		switch(rand.nextInt(2))
		{
			case 0:
				return "Evolved Spider";
			case 1:
				return "Evolved Skeleton";
			default:
				return "Evolved Zombie";
		}
	}
}
