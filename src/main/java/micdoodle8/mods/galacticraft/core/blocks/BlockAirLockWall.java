package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockAirLockWall extends BlockBreakable implements IPartialSealableBlock, ISortableBlock
{
    public static final PropertyEnum<EnumAirLockSealConnection> CONNECTION_TYPE = PropertyEnum.create("connection", EnumAirLockSealConnection.class);
    protected static final AxisAlignedBB AABB_X = new AxisAlignedBB(0.25, 0.0, 0.0, 0.75, 1.0, 1.0);
    protected static final AxisAlignedBB AABB_Z = new AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 1.0, 0.75);
    protected static final AxisAlignedBB AABB_FLAT = new AxisAlignedBB(0.0, 0.25, 0.0, 1.0, 0.75, 1.0);

    public enum EnumAirLockSealConnection implements IStringSerializable
    {
        X("x"),
        Z("z"),
        FLAT("flat");

        private final String name;

        EnumAirLockSealConnection(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockAirLockWall(String assetName)
    {
        super(Material.IRON, false);
        this.setTickRandomly(true);
        this.setHardness(1000.0F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (getConnection(source, pos))
        {
        case X:
            return AABB_X;
        case Z:
            return AABB_Z;
        default:
        case FLAT:
            return AABB_FLAT;
        }
    }

//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getSelectedBoundingBox(worldIn, pos);
//    }
//
//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
//    {
//        switch (getConnection(worldIn, pos))
//        {
//        case FLAT:
//            this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 0.75F, 1.0F);
//            break;
//        case X:
//            this.setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
//            break;
//        case Z:
//            this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 0.75F);
//            break;
//        }
//    }


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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return true;
    }

//    @Override
//    public Item getItem(World world, BlockPos pos)
//    {
//        return null;
//    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, CONNECTION_TYPE);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(CONNECTION_TYPE, getConnection(worldIn, pos));
    }

    private EnumAirLockSealConnection getConnection(IBlockAccess worldIn, BlockPos pos)
    {
        EnumAirLockSealConnection connection;

        Block frameID = GCBlocks.airLockFrame;
        Block sealID = GCBlocks.airLockSeal;

        Block idXMin = worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock();
        Block idXMax = worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock();

        if (idXMin != frameID && idXMax != frameID && idXMin != sealID && idXMax != sealID)
        {
            connection = EnumAirLockSealConnection.X;
        }
        else
        {
            int adjacentCount = 0;

            for (EnumFacing dir : EnumFacing.HORIZONTALS)
            {
                Block blockID = worldIn.getBlockState(pos.offset(dir)).getBlock();

                if (blockID == GCBlocks.airLockFrame || blockID == GCBlocks.airLockSeal)
                {
                    adjacentCount++;
                }
            }

            if (adjacentCount == 4)
            {
                connection = EnumAirLockSealConnection.FLAT;
            }
            else
            {
                connection = EnumAirLockSealConnection.Z;
            }
        }

        return connection;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }
}
