package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConcealedRedstone extends Block implements ISortableBlock
{
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private boolean canProvidePower = true;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.<BlockPos>newHashSet();

    public BlockConcealedRedstone(String assetName)
    {
        super(Material.iron);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.blockResistance = 15F;
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)));
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
         return EnumSortCategoryBlock.DECORATION;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue();
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {POWER});
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state)
    {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (BlockPos blockpos : list)
        {
            worldIn.notifyNeighborsOfStateChange(blockpos, this);
        }

        return state;
    }

    private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state)
    {
        IBlockState iblockstate = state;
        int currentPower = ((Integer)state.getValue(POWER)).intValue();
        int maxPower = 0;
        maxPower = this.getMaxCurrentStrength(worldIn, pos2, maxPower);
        this.canProvidePower = false;
        int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
        this.canProvidePower = true;

        if (k > 0 && k > maxPower - 1)
        {
            maxPower = k;
        }

        int l = 0;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos = pos1.offset(enumfacing);
            boolean flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();

            if (flag)
            {
                l = this.getMaxCurrentStrength(worldIn, blockpos, l);
            }

            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube() && !worldIn.getBlockState(pos1.up()).getBlock().isNormalCube())
            {
                if (flag && pos1.getY() >= pos2.getY())
                {
                    l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l);
                }
            }
            else if (!worldIn.getBlockState(blockpos).getBlock().isNormalCube() && flag && pos1.getY() <= pos2.getY())
            {
                l = this.getMaxCurrentStrength(worldIn, blockpos.down(), l);
            }
        }

        if (l > maxPower)
        {
            maxPower = l - 1;
        }
        else if (maxPower > 0)
        {
            maxPower--;
        }
        else
        {
            maxPower = 0;
        }

        if (k > maxPower - 1)
        {
            maxPower = k;
        }

        if (currentPower != maxPower)
        {
            state = state.withProperty(POWER, Integer.valueOf(maxPower));

            if (worldIn.getBlockState(pos1) == iblockstate)
            {
                worldIn.setBlockState(pos1, state, 2);
            }

            this.blocksNeedingUpdate.add(pos1);

            for (EnumFacing enumfacing1 : EnumFacing.VALUES)
            {
                this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
            }
        }

        return state;
    }

    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
    {
        Block b = worldIn.getBlockState(pos).getBlock(); 
        if (b == this || b == Blocks.redstone_wire)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);

            for (EnumFacing enumfacing : EnumFacing.VALUES)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            this.updateSurroundingRedstone(worldIn, pos, state);

            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            this.notifyNeighbors(worldIn, pos);
        }
    }

    private void notifyNeighbors(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos = pos.offset(enumfacing2);

            if (worldIn.getBlockState(blockpos).getBlock().isNormalCube())
            {
                this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
            }
            else
            {
                this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote)
        {
            for (EnumFacing enumfacing : EnumFacing.VALUES)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }

            this.updateSurroundingRedstone(worldIn, pos, state);

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            this.notifyNeighbors(worldIn, pos);
        }
    }

    private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength)
    {
        if (worldIn.getBlockState(pos).getBlock() != this)
        {
            return strength;
        }
        else
        {
            int i = (Integer) worldIn.getBlockState(pos).getValue(POWER);
            return i > strength ? i : strength;
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            this.updateSurroundingRedstone(worldIn, pos, state);
        }
    }

    @Override
    public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return !this.canProvidePower ? 0 : this.getWeakPower(worldIn, pos, state, side);
    }

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        if (!this.canProvidePower)
        {
            return 0;
        }
        else
        {
            return ((Integer)state.getValue(POWER)).intValue();
        }
    }

    protected static boolean canRedstoneConnect(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.redstone_wire || state.getBlock() == GCBlocks.concealedRedstone)
        {
            return true;
        }
        else if (Blocks.unpowered_repeater.isAssociated(state.getBlock()))
        {
            EnumFacing direction = (EnumFacing)state.getValue(BlockRedstoneRepeater.FACING);
            return direction == side || direction.getOpposite() == side;
        }
        else
        {
            return state.getBlock().canConnectRedstone(world, pos, side);
        }
    }

    @Override
    public boolean canProvidePower()
    {
        return this.canProvidePower;
    }
    
    @Override
    public int getLightOpacity()
    {
        return 0;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }

    @Override
    public boolean isFullCube()
    {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }
}
