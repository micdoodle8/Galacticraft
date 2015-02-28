package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

import java.util.Arrays;
import java.util.List;

public class BlockStairsGC extends BlockStairs
{
	private IIcon[] tinSideIcon;

	public static enum StairsCategoryGC
	{
		TIN1 ("stone"),
		TIN2 ("stone"),
		MOON_STONE ("stone"),
		MOON_BRICKS ("stone"),
		MARS_COBBLESTONE ("stone"),
		MARS_BRICKS ("stone");

		private final List<String> values;
		private String type;

		private StairsCategoryGC(String type)
		{
			this.type = type;
			this.values = Arrays.asList(type);
		}
	}

	private final StairsCategoryGC category;

	public BlockStairsGC(String name, Block model, StairsCategoryGC cat)
	{
		super(model, 0);
		this.category = cat;
		this.setBlockName(name);
		this.useNeighborBrightness = true;
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		if (this.category == StairsCategoryGC.TIN1 || this.category == StairsCategoryGC.TIN2 || this.category == StairsCategoryGC.MOON_STONE || this.category == StairsCategoryGC.MARS_COBBLESTONE || this.category == StairsCategoryGC.MOON_BRICKS || this.category == StairsCategoryGC.MARS_BRICKS)
		{
			return GalacticraftCore.galacticraftBlocksTab;
		}
		return null;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		if (this.category == StairsCategoryGC.TIN1)//Tin Decoration
		{
			this.blockIcon = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_4");
		}
		else if (this.category == StairsCategoryGC.TIN2)//Tin Decoration
		{
			this.blockIcon = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_2");
		}
		else if (this.category == StairsCategoryGC.MOON_STONE)//Moon Stone
		{
			this.blockIcon = par1IconRegister.registerIcon("galacticraftmoon:bottom");
		}
		else if (this.category == StairsCategoryGC.MOON_BRICKS)//Moon Dungeon Bricks
		{
			this.blockIcon = par1IconRegister.registerIcon("galacticraftmoon:brick");
		}

		if (GalacticraftCore.isPlanetsLoaded)
		{
			if (this.category == StairsCategoryGC.MARS_COBBLESTONE)//Mars Cobblestone
			{
				this.blockIcon = par1IconRegister.registerIcon("galacticraftmars:cobblestone");
			}
			else if (this.category == StairsCategoryGC.MARS_BRICKS)//Mars Dungeon Bricks
			{
				this.blockIcon = par1IconRegister.registerIcon("galacticraftmars:brick");
			}
		}

		this.tinSideIcon = new IIcon[2];
		this.tinSideIcon[0] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_1");
		this.tinSideIcon[1] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_4");
	}

	public boolean isWoodCategory(String block)
	{
		String type = StairsCategoryGC.valueOf(block).type;

		if (type == "wood")
		{
			return true;
		}
		return false;
	}

	public boolean isStoneCategory(String block)
	{
		String type = StairsCategoryGC.valueOf(block).type;

		if (type == "stone")
		{
			return true;
		}
		return false;
	}

	public static int getWoodCategoryAmount()
	{
		int woodCatNo = 0;

		for (StairsCategoryGC cat : StairsCategoryGC.values())
		{
			if (cat.values.contains("wood"))
			{
				++woodCatNo;
			}
		}
		return woodCatNo;
	}

	public static int getStoneCategoryAmount()
	{
		int woodCatNo = 0;

		for (StairsCategoryGC cat : StairsCategoryGC.values())
		{
			if (cat.values.contains("stone"))
			{
				++woodCatNo;
			}
		}
		return woodCatNo;
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (this.category == StairsCategoryGC.TIN2)//Tin Decoration
		{
			if (meta == 0 || meta == 8)
			{
				switch (side)
				{
				case 0:
					return this.tinSideIcon[1]; //BOTTOM
				case 1:
					return this.blockIcon; //TOP
				case 2:
					return this.tinSideIcon[0]; //Z-
				case 3:
					return this.tinSideIcon[0]; //Z+
				case 4:
					return this.blockIcon; //X-
				case 5:
					return this.tinSideIcon[0]; //X+
				default:
					return this.blockIcon;
				}
			}
			if (meta == 1 || meta == 9)
			{
				switch (side)
				{
				case 0:
					return this.tinSideIcon[1]; //BOTTOM
				case 1:
					return this.blockIcon; //TOP
				case 2:
					return this.tinSideIcon[0]; //Z-
				case 3:
					return this.tinSideIcon[0]; //Z+
				case 4:
					return this.tinSideIcon[0]; //X-
				case 5:
					return this.blockIcon; //X+
				default:
					return this.blockIcon;
				}
			}
			if (meta == 2 || meta == 10)
			{
				switch (side)
				{
				case 0:
					return this.tinSideIcon[1]; //BOTTOM
				case 1:
					return this.blockIcon; //TOP
				case 2:
					return this.blockIcon; //Z-
				case 3:
					return this.tinSideIcon[0]; //Z+
				case 4:
					return this.tinSideIcon[0]; //X-
				case 5:
					return this.tinSideIcon[0]; //X+
				default:
					return this.blockIcon;
				}
			}
			if (meta == 3 || meta == 11)
			{
				switch (side)
				{
				case 0:
					return this.tinSideIcon[1]; //BOTTOM
				case 1:
					return this.blockIcon; //TOP
				case 2:
					return this.tinSideIcon[0]; //Z-
				case 3:
					return this.blockIcon; //Z+
				case 4:
					return this.tinSideIcon[0]; //X-
				case 5:
					return this.tinSideIcon[0]; //X+
				default:
					return this.blockIcon;
				}
			}
			if (meta == 4 || meta == 12)
			{
				switch (side)
				{
				case 0:
					return this.blockIcon; //BOTTOM
				case 1:
					return this.tinSideIcon[1]; //TOP
				case 2:
					return this.tinSideIcon[0]; //Z-
				case 3:
					return this.tinSideIcon[0]; //Z+
				case 4:
					return this.blockIcon; //X-
				case 5:
					return this.tinSideIcon[0]; //X+
				default:
					return this.blockIcon;
				}
			}
			if (meta == 5 || meta == 13)
			{
				switch (side)
				{
				case 0:
					return this.blockIcon; //BOTTOM
				case 1:
					return this.tinSideIcon[1]; //TOP
				case 2:
					return this.tinSideIcon[0]; //Z-
				case 3:
					return this.tinSideIcon[0]; //Z+
				case 4:
					return this.tinSideIcon[0]; //X-
				case 5:
					return this.blockIcon; //X+
				default:
					return this.blockIcon;
				}
			}
			if (meta == 6 || meta == 14)
			{
				switch (side)
				{
				case 0:
					return this.blockIcon; //BOTTOM
				case 1:
					return this.tinSideIcon[1]; //TOP
				case 2:
					return this.blockIcon; //Z-
				case 3:
					return this.tinSideIcon[0]; //Z+
				case 4:
					return this.tinSideIcon[0]; //X-
				case 5:
					return this.tinSideIcon[0]; //X+
				default:
					return this.blockIcon;
				}
			}
			if (meta == 7 || meta == 15)
			{
				switch (side)
				{
				case 0:
					return this.blockIcon; //BOTTOM
				case 1:
					return this.tinSideIcon[1]; //TOP
				case 2:
					return this.tinSideIcon[0]; //Z-
				case 3:
					return this.blockIcon; //Z+
				case 4:
					return this.tinSideIcon[0]; //X-
				case 5:
					return this.tinSideIcon[0]; //X+
				default:
					return this.blockIcon;
				}
			}
		}
		return this.blockIcon;
	}
}