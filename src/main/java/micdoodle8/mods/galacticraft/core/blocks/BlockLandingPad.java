package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFuelerSingle;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPadSingle;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLandingPad extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumLandingPadType> PAD_TYPE = PropertyEnum.create("type", EnumLandingPadType.class);
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0);

    public enum EnumLandingPadType implements IStringSerializable
    {
        ROCKET_PAD(0, "rocket"),
        BUGGY_PAD(1, "buggy");

        private final int meta;
        private final String name;

        EnumLandingPadType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumLandingPadType[] values = values();
        public static EnumLandingPadType byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockLandingPad(String assetName)
    {
        super(Material.IRON);
        this.setHardness(1.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < 2; i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    private boolean checkAxis(World worldIn, BlockPos pos, Block block, EnumFacing facing)
    {
        int sameCount = 0;
        for (int i = 1; i <= 3; i++)
        {
            if (worldIn.getBlockState(pos.offset(facing, i)).getBlock() == block)
            {
                sameCount++;
            }
        }

        return sameCount < 3;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        final Block id = GCBlocks.landingPad;

        if (!checkAxis(worldIn, pos, id, EnumFacing.EAST) ||
                !checkAxis(worldIn, pos, id, EnumFacing.WEST) ||
                !checkAxis(worldIn, pos, id, EnumFacing.NORTH) ||
                !checkAxis(worldIn, pos, id, EnumFacing.SOUTH))
        {
            return false;
        }

        if (worldIn.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() == GCBlocks.landingPad && side == EnumFacing.UP)
        {
            return false;
        }
        else
        {
            return this.canPlaceBlockAt(worldIn, pos);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        switch (meta)
        {
        case 0:
            return new TileEntityLandingPadSingle();
        case 1:
            return new TileEntityBuggyFuelerSingle();
        default:
            return null;
        }
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return direction == EnumFacing.UP;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public String getShiftDescription(int meta)
    {
        if (meta == 0)
        {
            return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
        }
        return GCCoreUtil.translate("tile.buggy_pad.description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(PAD_TYPE, EnumLandingPadType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumLandingPadType) state.getValue(PAD_TYPE)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, PAD_TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.PAD;
    }
}
