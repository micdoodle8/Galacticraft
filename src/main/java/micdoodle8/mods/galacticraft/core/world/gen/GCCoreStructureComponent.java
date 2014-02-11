package micdoodle8.mods.galacticraft.core.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

/**
 * GCCoreStructureComponent.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCCoreStructureComponent extends StructureComponent
{
	public GCCoreStructureComponent(int var1)
	{
		super(var1);
	}

	public static StructureBoundingBox getComponentToAddBoundingBox(int x, int y, int z, int lengthOffset, int heightOffset, int widthOffset, int length, int height, int width, int coordBaseMode)
	{
		switch (coordBaseMode)
		{
		case 0:
			return new StructureBoundingBox(x + lengthOffset, y + heightOffset, z + widthOffset, x + length + lengthOffset, y + height + heightOffset, z + width + widthOffset);

		case 1:
			return new StructureBoundingBox(x - width + widthOffset, y + heightOffset, z + lengthOffset, x + widthOffset, y + height + heightOffset, z + length + lengthOffset);

		case 2:
			return new StructureBoundingBox(x - length - lengthOffset, y + heightOffset, z - width - widthOffset, x - lengthOffset, y + height + heightOffset, z - widthOffset);

		case 3:
			return new StructureBoundingBox(x + widthOffset, y + heightOffset, z - length, x + width + widthOffset, y + height + heightOffset, z + lengthOffset);

		default:
			return new StructureBoundingBox(x + lengthOffset, y + heightOffset, z + widthOffset, x + length + lengthOffset, y + height + heightOffset, z + width + widthOffset);
		}
	}

	protected void placeSpawnerAtCurrentPosition(World var1, Random var2, int var3, int var4, int var5, String var6, StructureBoundingBox var7)
	{
		final int var8 = this.getXWithOffset(var3, var5);
		final int var9 = this.getYWithOffset(var4);
		final int var10 = this.getZWithOffset(var3, var5);

		if (var7.isVecInside(var8, var9, var10) && var1.getBlockId(var8, var9, var10) != Block.mobSpawner.blockID)
		{
			var1.setBlock(var8, var9, var10, Block.mobSpawner.blockID, 0, 3);
			final TileEntityMobSpawner var11 = (TileEntityMobSpawner) var1.getBlockTileEntity(var8, var9, var10);

			if (var11 != null)
			{
				var11.getSpawnerLogic().setMobID(var6);
			}
		}
	}

	protected int[] offsetTowerCoords(int var1, int var2, int var3, int var4, int var5)
	{
		final int var6 = this.getXWithOffset(var1, var3);
		final int var7 = this.getYWithOffset(var2);
		final int var8 = this.getZWithOffset(var1, var3);
		return var5 == 0 ? new int[] { var6 + 1, var7 - 1, var8 - var4 / 2 } : var5 == 1 ? new int[] { var6 + var4 / 2, var7 - 1, var8 + 1 } : var5 == 2 ? new int[] { var6 - 1, var7 - 1, var8 + var4 / 2 } : var5 == 3 ? new int[] { var6 - var4 / 2, var7 - 1, var8 - 1 } : new int[] { var1, var2, var3 };
	}

	public int[] getOffsetAsIfRotated(int[] var1, int var2)
	{
		final int var3 = this.getCoordBaseMode();
		final int[] var4 = new int[3];
		this.setCoordBaseMode(var2);
		var4[0] = this.getXWithOffset(var1[0], var1[2]);
		var4[1] = this.getYWithOffset(var1[1]);
		var4[2] = this.getZWithOffset(var1[0], var1[2]);
		this.setCoordBaseMode(var3);
		return var4;
	}

	@Override
	protected int getXWithOffset(int var1, int var2)
	{
		switch (this.getCoordBaseMode())
		{
		case 0:
			return this.boundingBox.minX + var1;

		case 1:
			return this.boundingBox.maxX - var2;

		case 2:
			return this.boundingBox.maxX - var1;

		case 3:
			return this.boundingBox.minX + var2;

		default:
			return var1;
		}
	}

	@Override
	protected int getZWithOffset(int var1, int var2)
	{
		switch (this.getCoordBaseMode())
		{
		case 0:
			return this.boundingBox.minZ + var2;

		case 1:
			return this.boundingBox.minZ + var1;

		case 2:
			return this.boundingBox.maxZ - var2;

		case 3:
			return this.boundingBox.maxZ - var1;

		default:
			return var2;
		}
	}

	@Override
	protected int getYWithOffset(int var1)
	{
		return super.getYWithOffset(var1);
	}

	public int getCoordBaseMode()
	{
		return this.coordBaseMode;
	}

	public void setCoordBaseMode(int var1)
	{
		this.coordBaseMode = var1;
	}

	@Override
	protected int getBlockIdAtCurrentPosition(World var1, int var2, int var3, int var4, StructureBoundingBox var5)
	{
		return super.getBlockIdAtCurrentPosition(var1, var2, var3, var4, var5);
	}

	@Override
	protected void placeBlockAtCurrentPosition(World world, int blockID, int meta, int x, int y, int z, StructureBoundingBox bb)
	{
		super.placeBlockAtCurrentPosition(world, blockID, meta, x, y, z, bb);
	}
}
