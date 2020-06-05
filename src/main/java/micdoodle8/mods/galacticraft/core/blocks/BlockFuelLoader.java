//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
//import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.core.util.WorldUtil;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.properties.PropertyEnum;
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
//
//import javax.annotation.Nullable;
//
//public class BlockFuelLoader extends BlockAdvancedTile implements IShiftDescription, ISortableBlock
//{
//    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
//    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
//    public static final PropertyEnum SIDES = MACHINESIDES_RENDERTYPE.asProperty;
//
//    public BlockFuelLoader(Properties builder)
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
//    @Nullable
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityFuelLoader();
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
//        worldIn.setBlockState(pos, state.with(FACING, Direction.getHorizontal(angle).getOpposite()), 3);
//        WorldUtil.markAdjacentPadForUpdate(worldIn, pos);
//    }
//
//    @Override
//    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, BlockState state)
//    {
//        super.onBlockDestroyedByPlayer(worldIn, pos, state);
//        WorldUtil.markAdjacentPadForUpdate(worldIn, pos);
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
//        builder.add(FACING, SIDES);
//    }
//
//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//
//        return IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);
//    }
//
//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.MACHINE;
//    }
//
//    @Override
//    public boolean onSneakUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
//        TileEntity tile = world.getTileEntity(pos);
//        if (tile instanceof IMachineSides)
//        {
//            ((IMachineSides)tile).nextSideConfiguration(tile);
//            return true;
//        }
//        return false;
//    }
//}
