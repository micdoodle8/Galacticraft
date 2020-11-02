package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public abstract class BlockMachineBase extends BlockTileGC implements IShiftDescription, ISortable
{
    public static final int METADATA_MASK = 0x0c; //Used to select the machine type from metadata
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
//    protected EnumMachineBase[] types;
//    protected EnumMachineBase typeBase;

    public BlockMachineBase(Properties builder)
    {
        super(builder);
//        this.initialiseTypes();
    }

//    protected abstract void initialiseTypes();

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

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

    @Override
    public ActionResultType onSneakUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IMachineSides)
        {
            ((IMachineSides) tile).nextSideConfiguration(tile);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

//    @Nullable
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        int meta = getMetaFromState(state);
//        EnumMachineBase type = typeBase.fromMetadata(meta);
//        return type.tileConstructor();
//    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return getMetaFromState(state) & BlockMachineBase.METADATA_MASK;
//    }

//    public String getTranslationKey(int meta)
//    {
//        EnumMachineBase type = typeBase.fromMetadata(meta);
//        return type.getTranslationKey();
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        EnumMachineBase type = typeBase.fromMetadata(meta);
//        return GCCoreUtil.translate(type.getShiftDescriptionKey());
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

    public static Direction getFront(BlockState state)
    {
        if (state.getBlock() instanceof BlockMachineBase)
        {
            return (state.get(BlockMachineBase.FACING));
        }
        return Direction.NORTH;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }

//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (EnumMachineBase type : types)
//            list.add(new ItemStack(this, 1, type.getMetadata()));
//    }

    public interface EnumMachineBase<T extends Enum<T> & IStringSerializable>
    {
        int getMetadata();

        EnumMachineBase fromMetadata(int meta);

        String getShiftDescriptionKey();

        String getTranslationKey();

        TileEntity tileConstructor();
    }
}
