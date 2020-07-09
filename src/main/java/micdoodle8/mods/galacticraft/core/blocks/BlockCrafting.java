package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCrafting;
import micdoodle8.mods.galacticraft.core.inventory.GCContainerNames;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockCrafting extends BlockAdvancedTile implements IShiftDescription
{
    public static final DirectionProperty FACING = DirectionProperty.create("facing");

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
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if (this.useWrench(worldIn, pos, playerIn, hand, heldItem, hit))
        {
            return true;
        }

        if (playerIn.isSneaking())
        {
            if (this.onSneakMachineActivated(worldIn, pos, playerIn, hand, heldItem, hit))
            {
                return true;
            }
        }

        if (!worldIn.isRemote)
        {
            INamedContainerProvider container = new SimpleNamedContainerProvider((w, p, pl) -> new ContainerCrafting(w, p, (TileEntityCrafting) worldIn.getTileEntity(pos)), new TranslationTextComponent("container.magneticcrafting.name"));
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, container);
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
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
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
        if (MathHelper.abs((float) entityIn.posX - (float) clickedBlock.getX()) < 3.0F && MathHelper.abs((float) entityIn.posZ - (float) clickedBlock.getZ()) < 3.0F)
        {
            double d0 = entityIn.posY + (double) entityIn.getEyeHeight();

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

//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.GENERAL;
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
