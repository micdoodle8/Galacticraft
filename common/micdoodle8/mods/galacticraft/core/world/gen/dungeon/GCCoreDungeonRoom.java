package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * GCCoreDungeonRoom.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author fishtaco567
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreDungeonRoom
{
	public final GCCoreMapGenDungeon dungeonInstance;
	public World worldObj;

	// Min Corner, Lower, Left, Back
	public int posX;
	public int posY;
	public int posZ;

	// East = 0, North = 1, South = 2, West = 3, Up = 4, Down = 5.
	// North is z++, East is x++.
	public ForgeDirection entranceDir;

	public GCCoreDungeonRoom(GCCoreMapGenDungeon dungeon, int posX, int posY, int posZ, ForgeDirection entranceDir)
	{
		this.dungeonInstance = dungeon;
		this.worldObj = dungeon != null ? dungeon.worldObj : null;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.entranceDir = entranceDir;
	}

	public abstract void generate(short[] chunk, byte[] meta, int cx, int cz);

	public abstract GCCoreDungeonBoundingBox getBoundingBox();

	protected abstract GCCoreDungeonRoom makeRoom(GCCoreMapGenDungeon dungeon, int x, int y, int z, ForgeDirection dir);

	protected abstract void handleTileEntities(Random rand);

	public static GCCoreDungeonRoom makeRoom(GCCoreMapGenDungeon dungeon, Random rand, int x, int y, int z, ForgeDirection dir)
	{
		return dungeon.otherRooms.get(rand.nextInt(dungeon.otherRooms.size())).makeRoom(dungeon, x, y, z, dir);
	}

	public static GCCoreDungeonRoom makeBossRoom(GCCoreMapGenDungeon dungeon, Random rand, int x, int y, int z, ForgeDirection dir)
	{
		return dungeon.bossRooms.get(rand.nextInt(dungeon.bossRooms.size())).makeRoom(dungeon, x, y, z, dir);
	}

	public static GCCoreDungeonRoom makeTreasureRoom(GCCoreMapGenDungeon dungeon, Random rand, int x, int y, int z, ForgeDirection dir)
	{
		return dungeon.treasureRooms.get(rand.nextInt(dungeon.treasureRooms.size())).makeRoom(dungeon, x, y, z, dir);
	}

	protected boolean placeBlock(short[] blocks, byte[] metas, int x, int y, int z, int cx, int cz, int id, int meta)
	{
		if (GCCoreMapGenDungeon.useArrays)
		{
			cx *= 16;
			cz *= 16;
			x -= cx;
			z -= cz;
			if (x < 0 || x >= 16 || z < 0 || z >= 16)
			{
				return false;
			}
			final int index = this.getIndex(x, y, z);
			blocks[index] = (short) id;
			metas[index] = (byte) meta;
		}
		else
		{
			this.worldObj.setBlock(x, y, z, id, meta, 0);
		}
		return true;
	}

	private int getIndex(int x, int y, int z)
	{
		return y << 8 | z << 4 | x;
	}
}
