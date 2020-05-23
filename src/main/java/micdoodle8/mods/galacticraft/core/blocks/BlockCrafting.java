package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockCrafting extends BlockAdvancedTile implements ITileEntityProvider, ISortableBlock, IShiftDescription
{
    public static final DirectionProperty FACING = DirectionProperty.create("facing");

    public BlockCrafting(Properties builder)
    {
        super(builder);
    }

    @Override
    public ItemGroup getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if (this.useWrench(worldIn, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ))
        {
            return true;
        }

        if (playerIn.isSneaking())
        {
            if (this.onSneakMachineActivated(worldIn, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ))
            {
                return true;
            }
        }

        if (!worldIn.isRemote)
        {
            playerIn.openGui(GalacticraftCore.instance, -1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        this.rotate6Ways(world, pos);
        return true;
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
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCrafting();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.with(FACING, getFacingFromEntity(worldIn, pos, placer)), 2);
    }

    public static Direction getFacingFromEntity(World worldIn, BlockPos clickedBlock, LivingEntity entityIn)
    {
        if (MathHelper.abs((float)entityIn.posX - (float)clickedBlock.getX()) < 3.0F && MathHelper.abs((float)entityIn.posZ - (float)clickedBlock.getZ()) < 3.0F)
        {
            double d0 = entityIn.posY + (double)entityIn.getEyeHeight();

            if (d0 - (double)clickedBlock.getY() > 2.0D)
            {
                return Direction.UP;
            }

            if ((double)clickedBlock.getY() - d0 > 1.0D)
            {
                return Direction.DOWN;
            }
        }

        return entityIn.getHorizontalFacing().getOpposite();
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
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
    public BlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().with(FACING, Direction.getFront(meta));
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        return (state.get(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }
    
    @Override
    public void dropEntireInventory(World worldIn, BlockPos pos, BlockState state)
    {
        super.dropEntireInventory(worldIn, pos, state);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCrafting)
        {
            ((TileEntityCrafting)tileEntity).dropHiddenOutputBuffer(worldIn, pos);
        }
    }

}
