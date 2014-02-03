package micdoodle8.mods.galacticraft.moon.world.gen.dungeon;

import java.util.ArrayList;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreDungeonBoundingBox;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreDungeonRoom;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreMapGenDungeon;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;

/**
 * GCMoonRoomSpawner.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author fishtaco567
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMoonRoomSpawner extends GCCoreDungeonRoom
{
	int sizeX;
	int sizeY;
	int sizeZ;
	Random rand;

	private final ArrayList<ChunkCoordinates> spawners = new ArrayList<ChunkCoordinates>();

	public GCMoonRoomSpawner(GCCoreMapGenDungeon dungeon, int posX, int posY, int posZ, ForgeDirection entranceDir)
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
						if (this.rand.nextFloat() < 0.05F)
						{
							this.placeBlock(chunk, meta, i, j, k, cx, cz, Block.web.blockID, 0);
						}
					}
				}
			}
		}
		if (this.placeBlock(chunk, meta, this.posX + 1, this.posY - 1, this.posZ + 1, cx, cz, Block.mobSpawner.blockID, 0))
		{
			this.spawners.add(new ChunkCoordinates(this.posX + 1, this.posY - 1, this.posZ + 1));
		}
		if (this.placeBlock(chunk, meta, this.posX + this.sizeX - 1, this.posY - 1, this.posZ + this.sizeZ - 1, cx, cz, Block.mobSpawner.blockID, 0))
		{
			this.spawners.add(new ChunkCoordinates(this.posX + this.sizeX - 1, this.posY - 1, this.posZ + this.sizeZ - 1));
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
		return new GCMoonRoomSpawner(dungeon, x, y, z, dir);
	}

	@Override
	protected void handleTileEntities(Random rand)
	{
		for (final ChunkCoordinates spawnerCoords : this.spawners)
		{
			if (this.worldObj.getBlockId(spawnerCoords.posX, spawnerCoords.posY, spawnerCoords.posZ) == Block.mobSpawner.blockID)
			{
				final TileEntityMobSpawner spawner = (TileEntityMobSpawner) this.worldObj.getBlockTileEntity(spawnerCoords.posX, spawnerCoords.posY, spawnerCoords.posZ);
				if (spawner != null)
				{
					spawner.getSpawnerLogic().setMobID(GCMoonRoomSpawner.getMob(rand));
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
