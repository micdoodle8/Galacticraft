package micdoodle8.mods.galacticraft.core.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class BlockConcealedRedstone extends Block implements ISortableBlock
{
    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);
    private boolean canProvidePower = true;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.<BlockPos>newHashSet();

    public BlockConcealedRedstone(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(POWER, Integer.valueOf(0)));
    }

    @Override
    public ItemGroup getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
         return EnumSortCategoryBlock.DECORATION;
    }

    @Override
    public BlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().with(POWER, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        return ((Integer)state.get(POWER)).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {POWER});
    }

    private BlockState updateSurroundingRedstone(World worldIn, BlockPos pos, BlockState state)
    {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (BlockPos blockpos : list)
        {
            worldIn.notifyNeighborsOfStateChange(blockpos, this, false);
        }

        return state;
    }

    private BlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, BlockState state)
    {
        BlockState iblockstate = state;
        int currentPower = ((Integer)state.get(POWER)).intValue();
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

        for (Direction enumfacing : Direction.Plane.HORIZONTAL)
        {
            BlockPos blockpos = pos1.offset(enumfacing);
            boolean flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();

            if (flag)
            {
                l = this.getMaxCurrentStrength(worldIn, blockpos, l);
            }

            BlockState bs = worldIn.getBlockState(blockpos);
            BlockState bsUp = worldIn.getBlockState(pos1.up());
            if (bs.getBlock().isNormalCube(bs) && !bsUp.getBlock().isNormalCube(bsUp))
            {
                if (flag && pos1.getY() >= pos2.getY())
                {
                    l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l);
                }
            }
            else if (!bs.getBlock().isNormalCube(bs) && flag && pos1.getY() <= pos2.getY())
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
            state = state.with(POWER, Integer.valueOf(maxPower));

            if (worldIn.getBlockState(pos1) == iblockstate)
            {
                worldIn.setBlockState(pos1, state, 2);
            }

            this.blocksNeedingUpdate.add(pos1);

            for (Direction enumfacing1 : Direction.VALUES)
            {
                this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
            }
        }

        return state;
    }

    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
    {
        Block b = worldIn.getBlockState(pos).getBlock(); 
        if (b == this || b == Blocks.REDSTONE_WIRE)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);

            for (Direction enumfacing : Direction.VALUES)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state)
    {
        if (!worldIn.isRemote)
        {
            this.updateSurroundingRedstone(worldIn, pos, state);

            for (Direction enumfacing : Direction.Plane.VERTICAL)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            for (Direction enumfacing1 : Direction.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            this.notifyNeighbors(worldIn, pos);
        }
    }

    private void notifyNeighbors(World worldIn, BlockPos pos)
    {
        for (Direction enumfacing2 : Direction.Plane.HORIZONTAL)
        {
            BlockPos blockpos = pos.offset(enumfacing2);

            BlockState bs = worldIn.getBlockState(blockpos);
            if (bs.getBlock().isNormalCube(bs))
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
    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote)
        {
            for (Direction enumfacing : Direction.VALUES)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            this.updateSurroundingRedstone(worldIn, pos, state);

            for (Direction enumfacing1 : Direction.Plane.HORIZONTAL)
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
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            this.updateSurroundingRedstone(worldIn, pos, state);
        }
    }

    @Override
    public int getStrongPower(BlockState state, IBlockAccess worldIn, BlockPos pos, Direction side)
    {
        return !this.canProvidePower ? 0 : this.getWeakPower(state, worldIn, pos, side);
    }

    @Override
    public int getWeakPower(BlockState state, IBlockAccess blockAccess, BlockPos pos, Direction side)
    {
        if (!this.canProvidePower)
        {
            return 0;
        }
        else
        {
            return ((Integer)state.get(POWER)).intValue();
        }
    }

    protected static boolean canRedstoneConnect(IBlockAccess world, BlockPos pos, Direction side)
    {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.REDSTONE_WIRE || state.getBlock() == GCBlocks.concealedRedstone)
        {
            return true;
        }
        else if (Blocks.UNPOWERED_REPEATER.isAssociatedBlock(state.getBlock()))
        {
            Direction direction = (Direction)state.get(RepeaterBlock.FACING);
            return direction == side || direction.getOpposite() == side;
        }
        else
        {
            return state.getBlock().canConnectRedstone(state, world, pos, side);
        }
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return this.canProvidePower;
    }
    
    @Override
    public int getLightOpacity(BlockState state)
    {
        return 0;
    }
    
    @Override
    public boolean isOpaqueCube(BlockState state)
    {
        return true;
    }

    @Override
    public boolean isFullCube(BlockState state)
    {
        return true;
    }

    @Override
    public boolean isSideSolid(BlockState state, IBlockAccess world, BlockPos pos, Direction side)
    {
        return true;
    }
}
