//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityRefinery;
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
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.Nullable;
//import java.util.Random;
//
//public class BlockRefinery extends BlockAdvancedTile implements IShiftDescription, ISortableBlock
//{
//    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
//
//    public BlockRefinery(Properties builder)
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
//    @SideOnly(Side.CLIENT)
//    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
//    {
//        final TileEntity te = worldIn.getTileEntity(pos);
//
//        if (te instanceof TileEntityRefinery)
//        {
//            final TileEntityRefinery refinery = (TileEntityRefinery) te;
//
//            if (refinery.processTicks > 0)
//            {
//                final float var7 = pos.getX() + 0.5F;
//                final float var8 = pos.getY() + 1.1F;
//                final float var9 = pos.getZ() + 0.5F;
//                final float var10 = 0.0F;
//                final float var11 = 0.0F;
//
//                for (int i = -1; i <= 1; i++)
//                {
//                    for (int j = -1; j <= 1; j++)
//                    {
//                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7 + var11 + i * 0.2, var8, var9 + var10 + j * 0.2, 0.0D, 0.01D, 0.0D);
//                        worldIn.spawnParticle(EnumParticleTypes.FLAME, var7 + var11 + i * 0.1, var8 - 0.2, var9 + var10 + j * 0.1, 0.0D, 0.0001D, 0.0D);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
//        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
//        return true;
//    }
//
//    @Nullable
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityRefinery();
//    }
//
//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        worldIn.setBlockState(pos, getStateFromMeta(Direction.getHorizontal(angle).getOpposite().getHorizontalIndex()), 3);
//    }
//
//    @Override
//    public String getShiftDescription(int meta)
//    {
//        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
//    }
//
//    @Override
//    public boolean showDescription(int meta)
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
