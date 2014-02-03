package micdoodle8.mods.galacticraft.moon.world.gen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

/**
 * GCMoonComponentVillageField.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMoonComponentVillageField extends GCMoonComponentVillage
{
	private int averageGroundLevel = -1;

	public GCMoonComponentVillageField()
	{
	}

	public GCMoonComponentVillageField(GCMoonComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
	{
		super(par1ComponentVillageStartPiece, par2);
		this.coordBaseMode = par5;
		this.boundingBox = par4StructureBoundingBox;
	}

	@Override
	protected void func_143012_a(NBTTagCompound nbt)
	{
		super.func_143012_a(nbt);

		nbt.setInteger("AvgGroundLevel", this.averageGroundLevel);
	}

	@Override
	protected void func_143011_b(NBTTagCompound nbt)
	{
		super.func_143011_b(nbt);

		this.averageGroundLevel = nbt.getInteger("AvgGroundLevel");
	}

	@SuppressWarnings("rawtypes")
	public static GCMoonComponentVillageField func_74900_a(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
	{
		final StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 4, 9, par6);
		return StructureComponent.findIntersecting(par1List, var8) == null ? new GCMoonComponentVillageField(par0ComponentVillageStartPiece, par7, par2Random, var8, par6) : null;
	}

	@Override
	public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
	{
		if (this.averageGroundLevel < 0)
		{
			this.averageGroundLevel = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);

			if (this.averageGroundLevel < 0)
			{
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 7 - 1, 0);
		}

		this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 12, 4, 8, 0, 0, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, Block.dirt.blockID, Block.dirt.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, Block.dirt.blockID, Block.dirt.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 7, 0, 1, 8, 0, 7, Block.dirt.blockID, Block.dirt.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 10, 0, 1, 11, 0, 7, Block.dirt.blockID, Block.dirt.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 12, 0, 0, 12, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 11, 0, 0, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 11, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
		this.fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 1, 9, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
		int var4;

		for (var4 = 1; var4 <= 7; ++var4)
		{
			for (int i = 1; i < 12; i++)
			{
				if (i % 3 == 0)
				{
					;
				}
				else
				{
					if (par2Random.nextInt(3) == 0)
					{
						this.placeBlockAtCurrentPosition(par1World, Block.sapling.blockID, MathHelper.getRandomIntegerInRange(par2Random, 0, 2), i, 1, var4, par3StructureBoundingBox);
					}
				}
			}
		}

		for (var4 = 0; var4 < 9; ++var4)
		{
			for (int var5 = 0; var5 < 13; ++var5)
			{
				this.clearCurrentPositionBlocksUpwards(par1World, var5, 4, var4, par3StructureBoundingBox);
				this.fillCurrentPositionBlocksDownwards(par1World, Block.dirt.blockID, 0, var5, -1, var4, par3StructureBoundingBox);
			}
		}

		return true;
	}
}
