//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityDish;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockRenderType;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.material.Material;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.World;
//
//public class BlockDish extends BlockTileGC implements IShiftDescription, IPartialSealableBlock, ISortable
//{
//    public BlockDish(Properties builder)
//    {
//        super(builder);
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public boolean canPlaceBlockOnSide(World world, BlockPos pos, Direction side)
//    {
//        for (int y = 1; y <= 2; y++)
//        {
//            for (int x = -1; x <= 1; x++)
//            {
//                for (int z = -1; z <= 1; z++)
//                {
//                    BlockPos pos1 = pos.add((y == 2 ? x : 0), y, (y == 2 ? z : 0));
//                    Block block = world.getBlockState(pos1).getBlock();
//
//                    if (block.getMaterial(world.getBlockState(pos)) != Material.AIR && !block.isReplaceable(world, pos1))
//                    {
//                        return false;
//                    }
//                }
//            }
//        }
//
//        Direction facing = Direction.byIndex(LogicalSide.getIndex() ^ 1);
//        return world.getBlockState(pos.add(facing.getXOffset(), facing.getYOffset(), facing.getZOffset())).getBlock() != GCBlocks.fakeBlock;
//    }
//
//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        int metadata = state.getBlock().getMetaFromState(state);
//
//        int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        int change = 0;
//
//        switch (angle)
//        {
//        case 0:
//            change = 1;
//            break;
//        case 1:
//            change = 2;
//            break;
//        case 2:
//            change = 0;
//            break;
//        case 3:
//            change = 3;
//            break;
//        }
//
//        worldIn.setBlockState(pos, state.getBlock().getStateFromMeta(change), 3);
//
//        BlockMulti.onPlacement(worldIn, pos, placer, this);
//    }
//
//    @Override
//    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
//    {
//        final TileEntity tile = worldIn.getTileEntity(pos);
//
//        if (tile instanceof TileEntityDish)
//        {
//            ((TileEntityDish) tile).onDestroy(tile);
//        }
//
//        super.onReplaced(state, worldIn, pos, newState, isMoving);
//    }
//
//    @Override
//    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
////        IBlockState state = world.getBlockState(pos);
////        int original = state.getBlock().getMetaFromState(state);
////        int change = world.getBlockState(pos).getValue(FACING).rotateY().getHorizontalIndex();
//
////        TileEntity te = world.getTileEntity(pos);
////        if (te instanceof TileBaseUniversalElectrical)
////        {
////            ((TileBaseUniversalElectrical) te).updateFacing();
////        }
////
////        world.setBlockState(pos, state.getBlock().getStateFromMeta(change), 3);
//        return true;
//    }
//
//    @Override
//    public boolean onSneakMachineActivated(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
//        //entityPlayer.openGui(GalacticraftCore.instance, -1, world, x, y, z);
//        return true;
//    }
//
//    @Override
//    public TileEntity createTileEntity(World world, BlockState metadata)
//    {
//        return new TileEntityDish();
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        return GCCoreUtil.translate("tile.radio_telescope.description");
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isSealed(World world, BlockPos pos, Direction direction)
//    {
//        return true;
//    }
//
//    @Override
//    public EnumSortCategory getCategory()
//    {
//        return EnumSortCategory.GENERAL;
//    }
//
//    @Override
//    public BlockRenderType getRenderType(BlockState state)
//    {
//        return BlockRenderType.MODEL;
//    }
//}
