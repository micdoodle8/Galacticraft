package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSolar extends BlockTileGC implements IShiftDescription, IPartialSealableBlock, ISortableBlock
{
    public static final int BASIC_METADATA = 0;
    public static final int ADVANCED_METADATA = 4;

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<EnumSolarType> TYPE = PropertyEnum.create("type", EnumSolarType.class);

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

    public BlockSolar(String assetName)
    {
        super(Material.IRON);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, BlockSolar.BASIC_METADATA));
        list.add(new ItemStack(this, 1, BlockSolar.ADVANCED_METADATA));
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        for (int y = 1; y <= 2; y++)
        {
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    BlockPos posAt = pos.add(y == 2 ? x : 0, y, y == 2 ? z : 0);
                    IBlockState bs = worldIn.getBlockState(posAt); 

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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();

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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (var9 instanceof TileEntitySolar)
        {
            ((TileEntitySolar) var9).onDestroy(var9);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public int damageDropped(IBlockState state)
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

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
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
    public boolean isFullCube(IBlockState state)
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
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumSolarType type = EnumSolarType.byMetadata((int) Math.floor(meta / 4.0));
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex() + state.getValue(TYPE).getMeta() * 4;
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
