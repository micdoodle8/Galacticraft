package micdoodle8.mods.galacticraft.mars.world.gen.dungeon;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreDungeonBoundingBox;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreDungeonRoom;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreMapGenDungeon;
import net.minecraftforge.common.ForgeDirection;

/**
 * GCMarsRoomEmpty.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsRoomEmpty extends GCCoreDungeonRoom
{
	int sizeX;
	int sizeY;
	int sizeZ;

	public GCMarsRoomEmpty(GCCoreMapGenDungeon dungeon, int posX, int posY, int posZ, ForgeDirection entranceDir)
	{
		super(dungeon, posX, posY, posZ, entranceDir);
		if (this.worldObj != null)
		{
			final Random rand = new Random(this.worldObj.getSeed() * posX * posY * 57 * posZ);
			this.sizeX = rand.nextInt(4) + 5;
			this.sizeY = rand.nextInt(2) + 7;
			this.sizeZ = rand.nextInt(4) + 5;
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
	}

	@Override
	public GCCoreDungeonBoundingBox getBoundingBox()
	{
		return new GCCoreDungeonBoundingBox(this.posX, this.posZ, this.posX + this.sizeX, this.posZ + this.sizeZ);
	}

	@Override
	protected GCCoreDungeonRoom makeRoom(GCCoreMapGenDungeon dungeon, int x, int y, int z, ForgeDirection dir)
	{
		return new GCMarsRoomEmpty(dungeon, x, y, z, dir);
	}

	@Override
	protected void handleTileEntities(Random rand)
	{
		// TODO Auto-generated method stub

	}

}
