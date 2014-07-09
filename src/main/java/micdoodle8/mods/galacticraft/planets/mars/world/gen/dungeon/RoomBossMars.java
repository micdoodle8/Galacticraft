package micdoodle8.mods.galacticraft.planets.mars.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonBoundingBox;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonRoom;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityDungeonSpawnerMars;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class RoomBossMars extends DungeonRoom
{
	public int sizeX;
	public int sizeY;
	public int sizeZ;
	Random rand;
	ChunkCoordinates spawnerCoords;

	public RoomBossMars(MapGenDungeon dungeon, int posX, int posY, int posZ, ForgeDirection entranceDir)
	{
		super(dungeon, posX, posY, posZ, entranceDir);
		if (this.worldObj != null)
		{
			this.rand = new Random(this.worldObj.getSeed() * posX * posY * 57 * posZ);
			this.sizeX = 24;
			this.sizeY = 11;
			this.sizeZ = 24;
		}
	}

	@Override
	public void generate(Block[] chunk, byte[] meta, int cx, int cz)
	{
		for (int i = this.posX - 1; i <= this.posX + this.sizeX; i++)
		{
			for (int k = this.posZ - 1; k <= this.posZ + this.sizeZ; k++)
			{
				for (int j = this.posY - 1; j <= this.posY + this.sizeY; j++)
				{
					if (i == this.posX - 1 || i == this.posX + this.sizeX || j == this.posY - 1 || j == this.posY + this.sizeY || k == this.posZ - 1 || k == this.posZ + this.sizeZ)
					{
						if (j == this.posY - 1 && (i <= this.posX + 1 || i >= this.posX + this.sizeX - 2 || k == this.posZ + 1 || k == this.posZ + this.sizeZ - 2) && this.rand.nextInt(4) == 0)
						{
							this.placeBlock(chunk, meta, i, j, k, cx, cz, Blocks.glowstone, 0);
						}
						else
						{
							this.placeBlock(chunk, meta, i, j, k, cx, cz, this.dungeonInstance.DUNGEON_WALL_ID, this.dungeonInstance.DUNGEON_WALL_META);
						}
					}
					else if (j == this.posY && (i <= this.posX + 1 || i >= this.posX + this.sizeX - 2 || k == this.posZ + 1 || k == this.posZ + this.sizeZ - 2) && this.rand.nextInt(6) == 0)
					{
						this.placeBlock(chunk, meta, i, j, k, cx, cz, MarsBlocks.creeperEgg, 0);
					}
					else
					{
						this.placeBlock(chunk, meta, i, j, k, cx, cz, Blocks.air, 0);
					}
				}
			}
		}

		final int hx = (this.posX + this.posX + this.sizeX) / 2;
		final int hz = (this.posZ + this.posZ + this.sizeZ) / 2;
		this.spawnerCoords = new ChunkCoordinates(hx, this.posY + 2, hz);
	}

	@Override
	public DungeonBoundingBox getBoundingBox()
	{
		return new DungeonBoundingBox(this.posX, this.posZ, this.posX + this.sizeX, this.posZ + this.sizeZ);
	}

	@Override
	protected DungeonRoom makeRoom(MapGenDungeon dungeon, int x, int y, int z, ForgeDirection dir)
	{
		return new RoomBossMars(dungeon, x, y, z, dir);
	}

	@Override
	protected void handleTileEntities(Random rand)
	{
		if (this.spawnerCoords == null)
		{
			return;
		}

		this.worldObj.setBlock(this.spawnerCoords.posX, this.spawnerCoords.posY, this.spawnerCoords.posZ, MarsBlocks.marsBlock, 10, 3);

		final TileEntity tile = this.worldObj.getTileEntity(this.spawnerCoords.posX, this.spawnerCoords.posY, this.spawnerCoords.posZ);

		if (tile == null || !(tile instanceof TileEntityDungeonSpawnerMars))
		{
			TileEntityDungeonSpawner spawner = new TileEntityDungeonSpawnerMars();
			spawner.setRoom(new Vector3(this.posX, this.posY, this.posZ), new Vector3(this.sizeX, this.sizeY, this.sizeZ));
			this.worldObj.setTileEntity(this.spawnerCoords.posX, this.spawnerCoords.posY, this.spawnerCoords.posZ, spawner);
		}
		else if (tile instanceof TileEntityDungeonSpawner)
		{
			((TileEntityDungeonSpawner) tile).setRoom(new Vector3(this.posX, this.posY, this.posZ), new Vector3(this.sizeX, this.sizeY, this.sizeZ));
		}
	}

}
