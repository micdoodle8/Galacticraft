package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Arrays;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockStairsGC extends BlockStairs
{
	public static enum StairsCategoryGC
	{
		TIN1 ("stone"),
		TIN2 ("stone"),
		MOON_STONE ("stone"),
		MOON_BRICKS ("stone"),
		MARS_COBBLESTONE ("stone"),
		MARS_BRICKS ("stone");

		private List<String> values;
		private StairsCategoryGC(String type)
		{
			this.values = Arrays.asList(type);
		}
	}

	public BlockStairsGC(String name, IBlockState state, StairsCategoryGC cat)
	{
		super(state);
		this.setUnlocalizedName(name);
		this.useNeighborBrightness = true;
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}
}
