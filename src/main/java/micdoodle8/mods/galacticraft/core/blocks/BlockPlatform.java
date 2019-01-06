package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
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
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlatform extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumCorner> CORNER = PropertyEnum.create("type", EnumCorner.class);
    public static final float HEIGHT = 0.875F;
    protected static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0D, 6 / 16.0D, 0.0D, 1.0D, HEIGHT, 1.0D);
    protected static final AxisAlignedBB BOUNDING_BOX_ZEROG = new AxisAlignedBB(0.0D, 6 / 16.0D, 0.0D, 1.0D, 1.0D, 1.0D);;
    public static boolean ignoreCollisionTests;

    public enum EnumCorner implements IStringSerializable
    {
        NONE(0, "none"),
        NW(1, "sw"),
        SW(2, "nw"),
        NE(3, "se"),
        SE(4, "ne");
        // Yes these labels are the wrong way round, n should be s!  But the BlockState model is hard-coded to work with this as it is.

        private final int meta;
        private final String name;

        EnumCorner(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumCorner[] values = values();
        public static EnumCorner byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockPlatform(String assetName)
    {
        super(Material.IRON);
        this.setHardness(1.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CORNER, EnumCorner.NONE));
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    private boolean checkAxis(World worldIn, BlockPos pos, Block block, EnumFacing facing)
    {
        int sameCount = 0;
        for (int i = 1; i <= 2; i++)
        {
            IBlockState bs = worldIn.getBlockState(pos.offset(facing, i)); 
            if (bs.getBlock() == block && bs.getValue(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.NONE)
            {
                sameCount++;
            }
        }

        return sameCount > 1;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        final Block id = GCBlocks.platform;

        if (checkAxis(worldIn, pos, id, EnumFacing.EAST) ||
                checkAxis(worldIn, pos, id, EnumFacing.WEST) ||
                checkAxis(worldIn, pos, id, EnumFacing.NORTH) ||
                checkAxis(worldIn, pos, id, EnumFacing.SOUTH))
        {
            return false;
        }

        if (worldIn.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() == GCBlocks.platform && side == EnumFacing.UP)
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
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityPlatform(meta);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (var9 instanceof TileEntityPlatform)
        {
            ((TileEntityPlatform) var9).onDestroy(var9);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return direction == EnumFacing.UP;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(CORNER, EnumCorner.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumCorner) state.getValue(CORNER)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, CORNER);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.DECORATION;
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity entityIn, boolean p_185477_7_)
    {
        if (ignoreCollisionTests) return;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityPlatform)
        {
            if (((TileEntityPlatform) te).noCollide()) return;
        }
        AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(state, worldIn, pos).offset(pos);

        if (axisalignedbb != null && mask.intersects(axisalignedbb))
        {
            list.add(axisalignedbb);
        }
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (world instanceof World && ((World) world).provider instanceof IZeroGDimension)
            return BOUNDING_BOX_ZEROG;
        return BOUNDING_BOX;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState bs, World worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityPlatform)
        {
            if (((TileEntityPlatform) te).noCollide())
            {
                if (bs.getBlock() == this && bs.getValue(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.SE)
                    return new AxisAlignedBB((double)pos.getX() + 9/16D, (double)pos.getY(), (double)pos.getZ() + 9/16D, (double)pos.getX() + 1.0D, (double)pos.getY() + HEIGHT, (double)pos.getZ() + 1.0D);
                else
                    return new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX() + 7/16D, (double)pos.getY() + HEIGHT, (double)pos.getZ() + 7/16D);
            }
        }
        return super.getSelectedBoundingBox(bs, worldIn, pos);
    }
}
