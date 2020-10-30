package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenSealer;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockParaChest extends Block implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    protected static final VoxelShape NOT_CONNECTED_AABB = VoxelShapes.create(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

    public BlockParaChest(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return NOT_CONNECTED_AABB;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

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
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
//    {
//        super.onBlockAdded(worldIn, pos, state);
//    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        if (worldIn.isRemote)
        {
            return ActionResultType.SUCCESS;
        }
        else
        {
            IInventory iinventory = this.getInventory(worldIn, pos);

            if (iinventory != null && playerIn instanceof ServerPlayerEntity)
            {
                NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
                return ActionResultType.SUCCESS;
            }

            return ActionResultType.PASS;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        TileEntityParaChest tileentitychest = (TileEntityParaChest) worldIn.getTileEntity(pos);

        if (tileentitychest != null)
        {
            tileentitychest.updateContainingBlockInfo();
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntityParaChest tileentitychest = (TileEntityParaChest) worldIn.getTileEntity(pos);

        if (tileentitychest != null)
        {
            Random syncRandom = GCCoreUtil.getRandom(pos);
            for (int j1 = 0; j1 < tileentitychest.getSizeInventory(); ++j1)
            {
                ItemStack itemstack = tileentitychest.getStackInSlot(j1);

                if (itemstack != null)
                {

                    float f = syncRandom.nextFloat() * 0.8F + 0.1F;
                    float f1 = syncRandom.nextFloat() * 0.8F + 0.1F;
                    ItemEntity entityitem;

                    for (float f2 = syncRandom.nextFloat() * 0.8F + 0.1F; !itemstack.isEmpty(); worldIn.addEntity(entityitem))
                    {
                        entityitem = new ItemEntity(worldIn, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, itemstack.split(syncRandom.nextInt(21) + 10));
                        float f3 = 0.05F;
//                        entityitem.motionX = (float) syncRandom.nextGaussian() * f3;
//                        entityitem.motionY = (float) syncRandom.nextGaussian() * f3 + 0.2F;
//                        entityitem.motionZ = (float) syncRandom.nextGaussian() * f3;
                        entityitem.setMotion(syncRandom.nextGaussian() * f3, syncRandom.nextGaussian() * f3 + 0.2F, syncRandom.nextGaussian() * f3);
                    }
                }
            }

            worldIn.updateComparatorOutputLevel(pos, null);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    public IInventory getInventory(World par1World, BlockPos pos)
    {
        Object object = par1World.getTileEntity(pos);

        if (object == null)
        {
            return null;
        }
        else if (par1World.getBlockState(pos.up()).isNormalCube(par1World, pos.up()))
        {
            return null;
        }
        else if (BlockParaChest.isCatSittingAbove(par1World, pos))
        {
            return null;
        }
        else
        {
            return (IInventory) object;
        }
    }

    public static boolean isCatSittingAbove(World worldIn, BlockPos pos)
    {
        List<CatEntity> list = worldIn.getEntitiesWithinAABB(CatEntity.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1));
        if (!list.isEmpty())
        {
            for (CatEntity catentity : list)
            {
                if (catentity.isSitting())
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityParaChest();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
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

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byIndex(meta);
//
//        if (enumfacing.getAxis() == Direction.Axis.Y)
//        {
//            enumfacing = Direction.NORTH;
//        }
//
//        return this.getDefaultState().with(FACING, enumfacing);
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(COLOR, FACING);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//        if (!(tile instanceof TileEntityParaChest))
//        {
//            return state;
//        }
//        TileEntityParaChest chest = (TileEntityParaChest) tile;
//        return state.with(COLOR, chest.color);
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
    }
}
