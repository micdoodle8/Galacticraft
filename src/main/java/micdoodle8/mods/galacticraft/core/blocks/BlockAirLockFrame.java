package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAirLockFrame extends BlockAdvancedTile implements IShiftDescription, ISortableBlock
{
    private enum EnumAirLockType implements IStringSerializable
    {
        AIR_LOCK_FRAME(0, "air_lock_frame"),
        AIR_LOCK_CONTROLLER(1, "air_lock_controller");

        private final int meta;
        private final String name;

        EnumAirLockType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumAirLockType[] values = values();
        public static EnumAirLockType byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public static final PropertyEnum<EnumAirLockType> AIR_LOCK_TYPE = PropertyEnum.create("airlocktype", EnumAirLockType.class);

    public BlockAirLockFrame(String assetName)
    {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AIR_LOCK_TYPE, EnumAirLockType.AIR_LOCK_FRAME));
        this.setHardness(1.0F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, EnumAirLockType.AIR_LOCK_FRAME.getMeta()));
        list.add(new ItemStack(this, 1, EnumAirLockType.AIR_LOCK_CONTROLLER.getMeta()));
    }

    @Override
    public ItemGroup getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityAirLockController && placer instanceof PlayerEntity)
        {
            ((TileEntityAirLockController) tile).ownerName = PlayerUtil.getName(((PlayerEntity) placer));
        }
    }

    @Override
    public TileEntity createTileEntity(World world, BlockState state)
    {
        if (((EnumAirLockType) state.getValue(AIR_LOCK_TYPE)).getMeta() == EnumAirLockType.AIR_LOCK_FRAME.getMeta())
        {
            return new TileEntityAirLock();
        }
        else
        {
            return new TileEntityAirLockController();
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockAccess world, BlockPos pos, Direction side)
    {
        return true;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, Direction side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (((EnumAirLockType) state.getValue(AIR_LOCK_TYPE)).getMeta() == EnumAirLockType.AIR_LOCK_CONTROLLER.getMeta() && tile instanceof TileEntityAirLockController)
        {
            entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityAirLockController)
        {
            ((TileEntityAirLockController) tile).unsealAirLock();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int damageDropped(BlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public BlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AIR_LOCK_TYPE, EnumAirLockType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        return ((EnumAirLockType) state.getValue(AIR_LOCK_TYPE)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AIR_LOCK_TYPE);
    }

    @Override
    public String getShiftDescription(int itemDamage)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int itemDamage)
    {
        return true;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
