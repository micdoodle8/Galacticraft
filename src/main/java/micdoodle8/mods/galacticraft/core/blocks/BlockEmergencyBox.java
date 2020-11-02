package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEmergencyBox;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockEmergencyBox extends BlockAdvancedTile implements IShiftDescription, IPartialSealableBlock, ISortable
{
    public BlockEmergencyBox(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(KIT, meta % 2 == 1);
//    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        list.add(new ItemStack(this, 1, 0));
//        list.add(new ItemStack(this, 1, 1));
//    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return getMetaFromState(state);
//    }

//    @Override
//    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
//    {
//        return new ItemStack(this, 1, this.getMetaFromState(state));
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }


//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return (face == Direction.UP || face == Direction.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
//    } TODO

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityEmergencyBox();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public ActionResultType onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityEmergencyBox)
        {
            if (!world.isRemote)
            {
                ((TileEntityEmergencyBox) tile).click(player, hit.getFace(), state.getBlock() instanceof BlockEmergencyBoxKit);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

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
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, Direction direction)
    {
        return direction.ordinal() < 2;
    }

//    @Override
//    public boolean isSideSolid(BlockState state, IBlockReader world, BlockPos pos, Direction direction)
//    {
//        return direction.ordinal() < 2;
//    }
}
