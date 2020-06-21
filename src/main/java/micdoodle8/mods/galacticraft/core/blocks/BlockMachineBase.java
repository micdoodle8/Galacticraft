package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public abstract class BlockMachineBase extends BlockTileGC implements IShiftDescription
{
    public static final int METADATA_MASK = 0x0c; //Used to select the machine type from metadata
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
//        int metadata = getMetaFromState(state);
//
//        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        int change = Direction.byHorizontalIndex(angle).getOpposite().getHorizontalIndex();
//
//        worldIn.setBlockState(pos, this.getStateFromMeta((metadata & BlockMachineBase.METADATA_MASK) + change), 3);
        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 3);
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        if (!world.isRemote)
        {
//            entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ()); TODO
        }

        return true;
    }

    @Override
    public boolean onSneakUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IMachineSides)
        {
            ((IMachineSides)tile).nextSideConfiguration(tile);
            return true;
        }
        return false;
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
        return GCCoreUtil.translate(getTranslationKey());
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

//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.MACHINE;
//    }

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
