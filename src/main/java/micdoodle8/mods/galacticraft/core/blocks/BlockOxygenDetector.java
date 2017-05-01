package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDetector;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOxygenDetector extends BlockContainer implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    
    public BlockOxygenDetector(String assetName)
    {
        super(Material.iron);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityOxygenDetector();
    }

    public void updateOxygenState(World worldIn, BlockPos pos, boolean valid)
    {
        if (valid)
        {
            worldIn.setBlockState(pos, getStateFromMeta(1), 3);
        }
        else
        {
            worldIn.setBlockState(pos, getStateFromMeta(0), 3);
        }
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return getMetaFromState(worldIn.getBlockState(pos)) == 1 ? 15 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ACTIVE, meta > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] { ACTIVE });
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    //Solid block: can places torches on it (like a Redstone Block)
    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
