package micdoodle8.mods.galacticraft.core.blocks;

import com.google.common.base.Predicate;
import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.Random;

public class BlockUnlitTorch extends Block implements IOxygenReliantBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate()
    {
        public boolean apply(EnumFacing facing)
        {
            return facing != EnumFacing.DOWN;
        }
        public boolean apply(Object p_apply_1_)
        {
            return this.apply((EnumFacing)p_apply_1_);
        }
    });

    protected BlockUnlitTorch(boolean lit, String assetName)
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setUnlocalizedName(assetName);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    private boolean canPlaceOn(World worldIn, BlockPos pos)
    {
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos))
        {
            return true;
        }
        else
        {
            Block block = worldIn.getBlockState(pos).getBlock();
            return block.canPlaceTorchOnTop(worldIn, pos);
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        Iterator iterator = FACING.getAllowedValues().iterator();
        EnumFacing enumfacing;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            enumfacing = (EnumFacing)iterator.next();
        }
        while (!this.canPlaceAt(worldIn, pos, enumfacing));

        return true;
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
    {
        BlockPos blockpos1 = pos.offset(facing.getOpposite());
        boolean flag = facing.getAxis().isHorizontal();
        return flag && worldIn.isSideSolid(blockpos1, facing, true) || facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos1);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (this.canPlaceAt(worldIn, pos, facing))
        {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        else
        {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing enumfacing1;

            do
            {
                if (!iterator.hasNext())
                {
                    return this.getDefaultState();
                }

                enumfacing1 = (EnumFacing)iterator.next();
            }
            while (!worldIn.isSideSolid(pos.offset(enumfacing1.getOpposite()), enumfacing1, true));

            return this.getDefaultState().withProperty(FACING, enumfacing1);
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.checkForDrop(worldIn, pos, state);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        this.onNeighborChangeInternal(worldIn, pos, state);
    }

    protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.checkForDrop(worldIn, pos, state))
        {
            return true;
        }
        else
        {
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
            EnumFacing.Axis axis = enumfacing.getAxis();
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            boolean flag = false;

            if (axis.isHorizontal() && !worldIn.isSideSolid(pos.offset(enumfacing1), enumfacing1, true))
            {
                flag = true;
            }
            else if (axis.isVertical() && !this.canPlaceOn(worldIn, pos.offset(enumfacing1)))
            {
                flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, (EnumFacing)state.getValue(FACING)))
        {
            return true;
        }
        else
        {
            if (worldIn.getBlockState(pos).getBlock() == this)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
        float f = 0.15F;

        if (enumfacing == EnumFacing.EAST)
        {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        }
        else if (enumfacing == EnumFacing.WEST)
        {
            this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        }
        else if (enumfacing == EnumFacing.SOUTH)
        {
            this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        }
        else if (enumfacing == EnumFacing.NORTH)
        {
            this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
        else
        {
            f = 0.1F;
            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        }

        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta)
        {
            case 1:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST);
                break;
            case 2:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.WEST);
                break;
            case 3:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.SOUTH);
                break;
            case 4:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.NORTH);
                break;
            case 5:
            default:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.UP);
        }

        return iblockstate;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        double d0 = (double)pos.getX() + 0.5D;
        double d1 = (double)pos.getY() + 0.7D;
        double d2 = (double)pos.getZ() + 0.5D;
        double d3 = 0.22D;
        double d4 = 0.27D;

        if (enumfacing.getAxis().isHorizontal())
        {
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4 * (double)enumfacing1.getFrontOffsetX(), d1 + d3, d2 + d4 * (double)enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4 * (double)enumfacing1.getFrontOffsetX(), d1 + d3, d2 + d4 * (double)enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
        }
        else
        {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    public int getMetaFromState(IBlockState state)
    {
        byte b0 = 0;
        int i;

        switch (BlockUnlitTorch.SwitchEnumFacing.FACING_LOOKUP[((EnumFacing)state.getValue(FACING)).ordinal()])
        {
            case 1:
                i = b0 | 1;
                break;
            case 2:
                i = b0 | 2;
                break;
            case 3:
                i = b0 | 3;
                break;
            case 4:
                i = b0 | 4;
                break;
            case 5:
            case 6:
            default:
                i = b0 | 5;
        }

        return i;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING});
    }

    @Override
    public void onOxygenRemoved(World world, BlockPos pos)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            world.setBlockState(pos, GCBlocks.unlitTorch.getStateFromMeta(this.getMetaFromState(world.getBlockState(pos))), 2);
        }
        else
        {
            world.setBlockState(pos, Blocks.torch.getStateFromMeta(this.getMetaFromState(world.getBlockState(pos))), 2);
        }
    }

    @Override
    public void onOxygenAdded(World world, BlockPos pos)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            world.setBlockState(pos, GCBlocks.unlitTorch.getStateFromMeta(this.getMetaFromState(world.getBlockState(pos))), 2);
        }
        else
        {
            world.setBlockState(pos, Blocks.torch.getStateFromMeta(this.getMetaFromState(world.getBlockState(pos))), 2);
        }
    }

    static final class SwitchEnumFacing
    {
        static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];

        static
        {
            try
            {
                FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                FACING_LOOKUP[EnumFacing.UP.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
