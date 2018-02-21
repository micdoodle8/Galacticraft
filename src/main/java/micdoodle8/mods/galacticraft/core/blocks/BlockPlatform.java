package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlatform extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumCorner> CORNER = PropertyEnum.create("type", EnumCorner.class);
    public static final float HEIGHT = 0.875F;
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

        public static EnumCorner byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockPlatform(String assetName)
    {
        super(Material.iron);
        this.setBlockBounds(0.0F, 6 / 16.0F, 0.0F, 1.0F, HEIGHT, 1.0F);
        this.setHardness(1.0F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundTypeMetal);
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
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
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
    protected BlockState createBlockState()
    {
        return new BlockState(this, CORNER);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.DECORATION;
    }
    
    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
    {
        if (ignoreCollisionTests) return;
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityPlatform)
        {
            if (((TileEntityPlatform) te).noCollide()) return;
        }
        AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(worldIn, pos, state);

        if (axisalignedbb != null && mask.intersectsWith(axisalignedbb))
        {
            list.add(axisalignedbb);
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        double top = (double)pos.getY() + (worldIn.provider instanceof IZeroGDimension ? 1.0D : this.maxY); 
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, top, (double)pos.getZ() + this.maxZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityPlatform)
        {
            if (((TileEntityPlatform) te).noCollide())
            {
                IBlockState bs = worldIn.getBlockState(pos);
                if (bs.getBlock() == this && bs.getValue(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.SE)
                    return new AxisAlignedBB((double)pos.getX() + 9/16D, (double)pos.getY() + this.minY, (double)pos.getZ() + 9/16D, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
                else
                    return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + 7/16D, (double)pos.getY() + this.maxY, (double)pos.getZ() + 7/16D);
            }
        }
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    }
}
