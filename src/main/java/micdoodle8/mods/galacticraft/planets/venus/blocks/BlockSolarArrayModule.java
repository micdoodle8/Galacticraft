package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvanced;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntitySolarArrayModule;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSolarArrayModule extends BlockAdvanced implements IShiftDescription, IPartialSealableBlock, ISortableBlock, ITileEntityProvider
{
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.375, 0.0, 1.0, 0.625, 1.0);

    public BlockSolarArrayModule(String assetName)
    {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof INetworkConnection)
        {
            ((INetworkConnection) tile).refresh();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySolarArrayModule();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);

        if (!worldIn.isRemote)
        {
//            boolean added = false;
//            for (EnumFacing facing : EnumFacing.HORIZONTALS)
//            {
//                TileEntity tile = worldIn.getTileEntity(pos.offset(facing));
//                if (tile instanceof TileEntitySolarArrayController)
//                {
//                    ((TileEntitySolarArrayController) tile).addArrayModule(pos);
//                    added = true;
//                    break;
//                }
//            }
//            if (!added)
//            {
//
//            }
//            List<TileEntitySolarArrayController> controllers = Lists.newArrayList();
//            for (TileEntity tile : worldIn.loadedTileEntityList)
//            {
//                if (tile instanceof TileEntitySolarArrayController)
//                {
//                    BlockPos diff = tile.getPos().subtract(pos);
//                    if (Math.abs(diff.getX()) <= 16 && Math.abs(diff.getY()) <= 16 && Math.abs(diff.getZ()) <= 16)
//                    {
//                        controllers.add((TileEntitySolarArrayController) tile);
//                    }
//                }
//            }
//            for (TileEntitySolarArrayController controller : controllers)
//            {
//                controller.updateConnected(pos, controllers);
//            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
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

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return direction.getAxis() == EnumFacing.Axis.Y;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
