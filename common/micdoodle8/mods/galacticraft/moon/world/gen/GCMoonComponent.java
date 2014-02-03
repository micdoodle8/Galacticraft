package micdoodle8.mods.galacticraft.moon.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

/**
 * GCMoonComponent.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class GCMoonComponent extends StructureComponent
{
	public GCMoonComponent()
	{
	}

	public GCMoonComponent(int var1)
	{
		super(var1);
	}

	public static StructureBoundingBox getComponentToAddBoundingBox(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9)
	{
		switch (var9)
		{
		case 0:
			return new StructureBoundingBox(var0 + var3, var1 + var4, var2 + var5, var0 + var6 + var3, var1 + var7 + var4, var2 + var8 + var5);

		case 1:
			return new StructureBoundingBox(var0 - var8 + var5, var1 + var4, var2 + var3, var0 + var5, var1 + var7 + var4, var2 + var6 + var3);

		case 2:
			return new StructureBoundingBox(var0 - var6 - var3, var1 + var4, var2 - var8 - var5, var0 - var3, var1 + var7 + var4, var2 - var5);

		case 3:
			return new StructureBoundingBox(var0 + var5, var1 + var4, var2 - var6, var0 + var8 + var5, var1 + var7 + var4, var2 + var3);

		default:
			return new StructureBoundingBox(var0 + var3, var1 + var4, var2 + var5, var0 + var6 + var3, var1 + var7 + var4, var2 + var8 + var5);
		}
	}

	protected TileEntityMobSpawner placeSpawnerAtCurrentPosition(World var1, Random var2, int var3, int var4, int var5, String var6, StructureBoundingBox var7)
	{
		TileEntityMobSpawner var8 = null;
		final int var9 = this.getXWithOffset(var3, var5);
		final int var10 = this.getYWithOffset(var4);
		final int var11 = this.getZWithOffset(var3, var5);

		if (var7.isVecInside(var9, var10, var11) && var1.getBlockId(var9, var10, var11) != Block.mobSpawner.blockID)
		{
			var1.setBlock(var9, var10, var11, Block.mobSpawner.blockID, 0, 3);
			var8 = (TileEntityMobSpawner) var1.getBlockTileEntity(var9, var10, var11);

			if (var8 != null)
			{
				var8.getSpawnerLogic().setMobID(var6);
			}
		}

		return var8;
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
	protected int getYWithOffset(int var1)
	{
		return super.getYWithOffset(var1);
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

	protected int getXWithOffsetAsIfRotated(int var1, int var2, int var3)
	{
		if (this.coordBaseMode < 0)
		{
			return var1;
		}
		else
		{
			switch ((this.coordBaseMode + var3) % 4)
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
	}

	protected int getZWithOffsetAsIfRotated(int var1, int var2, int var3)
	{
		if (this.coordBaseMode < 0)
		{
			return var1;
		}
		else
		{
			switch ((this.coordBaseMode + var3) % 4)
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

	/**
	 * current Position depends on currently set Coordinates mode, is computed
	 * here
	 */
	@Override
	protected void placeBlockAtCurrentPosition(World var1, int var2, int var3, int var4, int var5, int var6, StructureBoundingBox var7)
	{
		super.placeBlockAtCurrentPosition(var1, var2, var3, var4, var5, var6, var7);
	}

	protected void placeBlockRotated(World var1, int var2, int var3, int var4, int var5, int var6, int var7, StructureBoundingBox var8)
	{
		final int var9 = this.getXWithOffsetAsIfRotated(var4, var6, var7);
		final int var10 = this.getYWithOffset(var5);
		final int var11 = this.getZWithOffsetAsIfRotated(var4, var6, var7);

		if (var8.isVecInside(var9, var10, var11))
		{
			var1.setBlock(var9, var10, var11, var2, var3, 3);
		}
	}

	protected void fillBlocksRotated(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11)
	{
		for (int var12 = var4; var12 <= var7; ++var12)
		{
			for (int var13 = var3; var13 <= var6; ++var13)
			{
				for (int var14 = var5; var14 <= var8; ++var14)
				{
					this.placeBlockRotated(var1, var9, var10, var13, var12, var14, var11, var2);
				}
			}
		}
	}

	protected void fillAirRotated(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9)
	{
		this.fillBlocksRotated(var1, var2, var3, var4, var5, var6, var7, var8, 0, 0, var9);
	}

	protected int getStairMeta(int var1)
	{
		switch ((this.getCoordBaseMode() + var1) % 4)
		{
		case 0:
			return 0;

		case 1:
			return 2;

		case 2:
			return 1;

		case 3:
			return 3;

		default:
			return -1;
		}
	}

	protected int getLadderMeta(int var1)
	{
		switch ((this.getCoordBaseMode() + var1) % 4)
		{
		case 0:
			return 4;

		case 1:
			return 2;

		case 2:
			return 5;

		case 3:
			return 3;

		default:
			return -1;
		}
	}

	protected int getLadderMeta(int var1, int var2)
	{
		return this.getLadderMeta(var1 + var2);
	}

	public void nullifySkyLightForBoundingBox(World var1)
	{
		this.nullifySkyLight(var1, this.boundingBox.minX - 1, this.boundingBox.minY - 1, this.boundingBox.minZ - 1, this.boundingBox.maxX + 1, this.boundingBox.maxY + 1, this.boundingBox.maxZ + 1);
	}

	public void nullifySkyLightAtCurrentPosition(World var1, int var2, int var3, int var4, int var5, int var6, int var7)
	{
		this.nullifySkyLight(var1, this.getXWithOffset(var2, var4), this.getYWithOffset(var3), this.getZWithOffset(var2, var4), this.getXWithOffset(var5, var7), this.getYWithOffset(var6), this.getZWithOffset(var5, var7));
	}

	public void nullifySkyLight(World var1, int var2, int var3, int var4, int var5, int var6, int var7)
	{
		for (int var8 = var2; var8 <= var5; ++var8)
		{
			for (int var9 = var4; var9 <= var7; ++var9)
			{
				for (int var10 = var3; var10 <= var6; ++var10)
				{
					var1.setLightValue(EnumSkyBlock.Sky, var8, var10, var9, 0);
				}
			}
		}
	}
}
