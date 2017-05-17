package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public class BlockStairsGC extends BlockStairs implements ISortableBlock
{
    public BlockStairsGC(String name, IBlockState state)
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

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.STAIRS;
    }
    
    //Override the interaction with StructuredComponent to do nothing here, otherwise messes up our Abandoned Base generation code merged from 1.8.9
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state;
    }

    //Override the interaction with StructuredComponent to do nothing here, otherwise messes up our Abandoned Base generation code merged from 1.8.9
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state;
    }
}