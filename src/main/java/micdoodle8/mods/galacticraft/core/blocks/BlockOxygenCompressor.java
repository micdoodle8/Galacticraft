package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockOxygenCompressor extends BlockAdvancedTile implements IShiftDescription
{
    public static final int OXYGEN_COMPRESSOR_METADATA = 0;
    public static final int OXYGEN_DECOMPRESSOR_METADATA = 4;

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
//    public static final EnumProperty<EnumCompressorType> TYPE = EnumProperty.create("type", EnumCompressorType.class);

//    public enum EnumCompressorType implements IStringSerializable
//    {
//        COMPRESSOR(0, "compressor"),
//        DECOMPRESSOR(1, "decompressor");
//
//        private final int meta;
//        private final String name;
//
//        EnumCompressorType(int meta, String name)
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
//        private final static EnumCompressorType[] values = values();
//        public static EnumCompressorType byMetadata(int meta)
//        {
//            return values[meta % values.length];
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }

    public BlockOxygenCompressor(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
//        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ()); TODO guis
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return null;
//        int metadata = getMetaFromState(state);
//        if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
//        {
//            return new TileEntityOxygenDecompressor();
//        }
//        else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
//        {
//            return new TileEntityOxygenCompressor();
//        }
//        else
//        {
//            return null;
//        }
    }

//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        int change = Direction.byHorizontalIndex(angle).getOpposite().getHorizontalIndex();
//
//        if (stack.getItemDamage() >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
//        {
//            change += BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
//        }
//        else if (stack.getItemDamage() >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
//        {
//            change += BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
//        }
//
//        worldIn.setBlockState(pos, getStateFromMeta(change), 3);
//    }

//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        list.add(new ItemStack(this, 1, BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA));
//        list.add(new ItemStack(this, 1, BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA));
//    }
//
//    @Override
//    public int damageDropped(BlockState state)
//    {
//        int metadata = getMetaFromState(state);
//        if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
//        {
//            return BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
//        }
//        else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
//        {
//            return BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
//        }
//        else
//        {
//            return 0;
//        }
//    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return stack.getItem() == Item.getItemFromBlock(GCBlocks.oxygenCompressor) ? GCCoreUtil.translate("tile.oxygen_compressor.description") : GCCoreUtil.translate("tile.oxygen_decompressor.description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(meta % 4);
//        EnumCompressorType type = EnumCompressorType.byMetadata((int) Math.floor(meta / 4.0));
//        return this.getDefaultState().with(FACING, enumfacing).with(TYPE, type);
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.MACHINE;
//    }
}
