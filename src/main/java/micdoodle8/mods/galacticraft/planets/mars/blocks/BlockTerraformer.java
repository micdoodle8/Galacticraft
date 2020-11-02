package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockTerraformer extends BlockTileGC implements IShiftDescription, ISortable
{
    //    public static final int TERRAFORMER_METADATA = 0;
//    public static final int CRYOGENIC_CHAMBER_METADATA = 4;
//    public static final int LAUNCH_CONTROLLER_METADATA = 8;
//
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
//    public static final EnumProperty<EnumMachineType> TYPE = EnumProperty.create("type", EnumMachineType.class);
//
//    public enum EnumMachineType implements IStringSerializable
//    {
//        TERRAFORMER(0, "terraformer"),
//        CRYOGENIC_CHAMBER(1, "cryogenic_chamber"),
//        LAUNCH_CONTROLLER(2, "launch_controller");
//
//        private final int meta;
//        private final String name;
//
//        EnumMachineType(int meta, String name)
//        {
//            this.meta = meta;
//            this.name = name;
//        }
//
//        public int getMeta()
//        {
//            return this.meta;
//        }
//
//        public static EnumMachineType byMetadata(int meta)
//        {
//            return values()[meta % 3];
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }

    public BlockTerraformer(Properties builder)
    {
        super(builder);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (var9 instanceof IMultiBlock)
        {
            ((IMultiBlock) var9).onDestroy(var9);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public ActionResultType onMachineActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
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

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        EnumMachineType type = state.get(TYPE);
//        return type == EnumMachineType.CRYOGENIC_CHAMBER ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
//    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTerraformer();
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

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public boolean shouldSideBeRendered(BlockState blockState, IWorldReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }

//    @Override
//    public boolean isSealed(World world, BlockPos pos, Direction direction)
//    {
//	    return world.getBlockState(pos).get(TYPE) != EnumMachineType.CRYOGENIC_CHAMBER;
//    }
}
