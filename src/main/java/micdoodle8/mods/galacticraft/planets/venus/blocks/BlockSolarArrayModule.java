package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvanced;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntitySolarArrayModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockSolarArrayModule extends BlockAdvanced implements IShiftDescription, IPartialSealableBlock, ISortable
{
    protected static final VoxelShape AABB = VoxelShapes.create(0.0, 0.375, 0.0, 1.0, 0.625, 1.0);

    public BlockSolarArrayModule(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return AABB;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);

        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof INetworkConnection)
        {
            ((INetworkConnection) tile).refresh();
        }
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySolarArrayModule();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving);

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

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, Direction direction)
    {
        return direction.getAxis() == Direction.Axis.Y;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
}
