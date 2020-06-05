//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.Nullable;
//
//public class BlockOxygenDistributor extends BlockAdvancedTile implements IShiftDescription, ISortableBlock
//{
//    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
//
//    public BlockOxygenDistributor(Properties builder)
//    {
//        super(builder);
//        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
//        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
//        return true;
//    }
//
//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        worldIn.setBlockState(pos, getStateFromMeta(Direction.getHorizontal(angle).getOpposite().getHorizontalIndex()), 3);
//    }
//
//    @Nullable
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityOxygenDistributor();
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return true;
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.getHorizontal(meta);
//        return this.getDefaultState().with(FACING, enumfacing);
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(FACING);
//    }
//
//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.MACHINE;
//    }
//}
