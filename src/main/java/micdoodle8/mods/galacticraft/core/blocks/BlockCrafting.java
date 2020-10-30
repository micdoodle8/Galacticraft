package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.inventory.ContainerCrafting;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockCrafting extends BlockAdvancedTile implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockCrafting(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if (this.useWrench(worldIn, pos, playerIn, hand, heldItem, hit) == ActionResultType.SUCCESS)
        {
            return ActionResultType.SUCCESS;
        }

        if (playerIn.isSneaking())
        {
            if (this.onSneakMachineActivated(worldIn, pos, playerIn, hand, heldItem, hit) == ActionResultType.SUCCESS)
            {
                return ActionResultType.SUCCESS;
            }
        }

        if (!worldIn.isRemote)
        {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
        }

        return ActionResultType.PASS;
    }

    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider)tileentity : null;
    }

    @Override
    public ActionResultType onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        this.rotate6Ways(world, pos);
        return ActionResultType.SUCCESS;
    }

    private void rotate6Ways(World world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        Direction facing = state.get(FACING);
        if (facing == Direction.DOWN)
        {
            facing = Direction.UP;
        }
        else if (facing == Direction.UP)
        {
            facing = Direction.NORTH;
        }
        else if (facing == Direction.WEST)
        {
            facing = Direction.DOWN;
        }
        else
        {
            facing = facing.rotateY();
        }
//        int metadata = this.getMetaFromState(world.getBlockState(pos));
//        int metaDir = ((metadata & 7) + 1) % 6;
//        //DOWN->UP->NORTH->*EAST*->*SOUTH*->WEST
//        //0->1 1->2 2->5 3->4 4->0 5->3
//        if (metaDir == 3) //after north
//        {
//            metaDir = 5;
//        }
//        else if (metaDir == 0)
//        {
//            metaDir = 3;
//        }
//        else if (metaDir == 5)
//        {
//            metaDir = 0;
//        }

        world.setBlockState(pos, state.with(FACING, facing), 3);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityCrafting();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, getFacingFromEntity(context.getWorld(), context.getPos(), context.getPlayer()));
    }

    public static Direction getFacingFromEntity(World worldIn, BlockPos clickedBlock, LivingEntity entityIn)
    {
        if (MathHelper.abs((float) entityIn.getPosX() - (float) clickedBlock.getX()) < 3.0F && MathHelper.abs((float) entityIn.getPosZ() - (float) clickedBlock.getZ()) < 3.0F)
        {
            double d0 = entityIn.getPosY() + (double) entityIn.getEyeHeight();

            if (d0 - (double) clickedBlock.getY() > 2.0D)
            {
                return Direction.UP;
            }

            if ((double) clickedBlock.getY() - d0 > 1.0D)
            {
                return Direction.DOWN;
            }
        }

        return entityIn.getHorizontalFacing().getOpposite();
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
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
//        return this.getDefaultState().with(FACING, Direction.byIndex(meta));
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

//    @Override
//    public void dropEntireInventory(World worldIn, BlockPos pos, BlockState state)
//    {
//        super.dropEntireInventory(worldIn, pos, state);
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//        if (tileEntity instanceof TileEntityCrafting)
//        {
//            ((TileEntityCrafting)tileEntity).dropHiddenOutputBuffer(worldIn, pos);
//        }
//    } TODO
}
