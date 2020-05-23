package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockSolar extends BlockTileGC implements IShiftDescription, IPartialSealableBlock, ISortableBlock
{
    public static final int BASIC_METADATA = 0;
    public static final int ADVANCED_METADATA = 4;

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final EnumProperty<EnumSolarType> TYPE = EnumProperty.create("type", EnumSolarType.class);

    public enum EnumSolarType implements IStringSerializable
    {
        BASIC_SOLAR(0, "basic_solar"),
        ADVANCED_SOLAR(1, "advanced_solar"); // 3 for backwards compatibility

        private final int meta;
        private final String name;

        EnumSolarType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumSolarType[] values = values();
        public static EnumSolarType byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockSolar(Properties builder)
    {
        super(builder);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, BlockSolar.BASIC_METADATA));
        list.add(new ItemStack(this, 1, BlockSolar.ADVANCED_METADATA));
    }

    @Override
    public ItemGroup getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
    {
        for (int y = 1; y <= 2; y++)
        {
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    BlockPos posAt = pos.add(y == 2 ? x : 0, y, y == 2 ? z : 0);
                    BlockState bs = worldIn.getBlockState(posAt);

                    if (bs.getMaterial() != Material.AIR && !bs.getBlock().isReplaceable(worldIn, posAt))
                    {
                        return false;
                    }
                }
            }
        }

        for (int x = -2; x <= 2; x++)
        {
            for (int z = -2; z <= 2; z++)
            {
                BlockPos posAt = pos.add(x, 0, z);
                Block block = worldIn.getBlockState(posAt).getBlock();

                if (block == this)
                {
                    return false;
                }
            }
        }

        return true;
        // return new BlockVec3(x1, y1, z1).newVecSide(side ^ 1).getBlock(world) != GCBlocks.fakeBlock; TODO
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = Direction.getHorizontal(angle).getOpposite().getHorizontalIndex();

        if (stack.getItemDamage() >= ADVANCED_METADATA)
        {
            change += ADVANCED_METADATA;
        }
        else if (stack.getItemDamage() >= BASIC_METADATA)
        {
            change += BASIC_METADATA;
        }

        worldIn.setBlockState(pos, getStateFromMeta(change), 3);

        BlockMulti.onPlacement(worldIn, pos, placer, this);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (var9 instanceof TileEntitySolar)
        {
            ((TileEntitySolar) var9).onDestroy(var9);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public int damageDropped(BlockState state)
    {
        if (getMetaFromState(state) >= BlockSolar.ADVANCED_METADATA)
        {
            return BlockSolar.ADVANCED_METADATA;
        }
        else
        {
            return BlockSolar.BASIC_METADATA;
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        if (getMetaFromState(state) >= BlockSolar.ADVANCED_METADATA)
        {
            return new TileEntitySolar(2);
        }
        else
        {
            return new TileEntitySolar(1);
        }
    }

    @Override
    public boolean isFullCube(BlockState state)
    {
        return false;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case BASIC_METADATA:
            return GCCoreUtil.translate("tile.solar_basic.description");
        case ADVANCED_METADATA:
            return GCCoreUtil.translate("tile.solar_adv.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube(BlockState state)
    {
        return false;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, Direction direction)
    {
        return true;
    }

    @Override
    public BlockState getStateFromMeta(int meta)
    {
        Direction enumfacing = Direction.getHorizontal(meta % 4);
        EnumSolarType type = EnumSolarType.byMetadata((int) Math.floor(meta / 4.0));
        return this.getDefaultState().with(FACING, enumfacing).with(TYPE, type);
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        return state.get(FACING).getHorizontalIndex() + state.get(TYPE).getMeta() * 4;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
