package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BlockUnlitTorch extends BlockTorchBase implements IOxygenReliantBlock
{
//    public static final DirectionProperty HORIZONTAL_FACING = HorizontalBlock.HORIZONTAL_FACING;

    public boolean lit;
    public Block litVersion;
    public Block unlitVersion;
    public Block fallback;
    public static HashMap<Block, Block> registeredTorches = new HashMap<>();

    public BlockUnlitTorch(boolean lit, Properties builder)
    {
        super(builder);
        this.lit = lit;
        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.UP));
    }

    public static void register(BlockUnlitTorch unlittorch, BlockUnlitTorch littorch, Block vanillatorch)
    {
        littorch.litVersion = littorch;
        littorch.unlitVersion = unlittorch;
        littorch.fallback = vanillatorch;
        unlittorch.litVersion = littorch;
        unlittorch.unlitVersion = unlittorch;
        unlittorch.fallback = vanillatorch;
        registeredTorches.put(littorch, vanillatorch);
        GCBlocks.itemChanges.put(unlittorch, littorch);
    }

//    @Override
//    public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
//    {
//        if (state.getBlock().getMetaFromState(state) == 0)
//        {
//            this.onBlockAdded(worldIn, pos, state);
//        }
//        else
//        {
//            this.checkOxygen(worldIn, pos, state);
//        }
//    }

//    @Override
//    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
//    {
//        if (this.checkForDrop(worldIn, pos, state))
//        {
//            this.checkOxygen(worldIn, pos, state);
//        }
//    }

//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        if (this.checkForDrop(worldIn, pos, state))
//        {
//            Direction enumfacing = state.get(FACING);
//            Direction.Axis enumfacingAxis = enumfacing.getAxis();
//            Direction enumfacing1 = enumfacing.getOpposite();
//            boolean flag = false;
//
//            if (enumfacingAxis.isHorizontal() && !worldIn.isSideSolid(pos.offset(enumfacing1), enumfacing, true))
//            {
//                flag = true;
//            }
//            else if (enumfacingAxis.isVertical() && !this.canPlaceOn(worldIn, pos.offset(enumfacing1)))
//            {
//                flag = true;
//            }
//
//            if (flag)
//            {
//                this.dropBlockAsItem(worldIn, pos, state, 0);
//                worldIn.removeBlock(pos, false);
//            }
//            else
//            {
//                this.checkOxygen(worldIn, pos, state);
//            }
//        }
//    }

    private void checkOxygen(World world, BlockPos pos, BlockState state)
    {
        if (world.getDimension() instanceof IGalacticraftDimension)
        {
            if (OxygenUtil.checkTorchHasOxygen(world, pos))
            {
                this.onOxygenAdded(world, pos, state);
            }
            else
            {
                this.onOxygenRemoved(world, pos, state);
            }
        }
        else
        {
            Direction enumfacing = state.get(FACING);
            world.setBlockState(pos, this.fallback.getDefaultState().with(FACING, enumfacing), 2);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        Direction enumfacing = stateIn.get(FACING);
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;
        double d3 = 0.22D;
        double d4 = 0.27D;

        if (enumfacing.getAxis().isHorizontal())
        {
            Direction enumfacing1 = enumfacing.getOpposite();
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d4 * (double) enumfacing1.getXOffset(), d1 + d3, d2 + d4 * (double) enumfacing1.getZOffset(), 0.0D, 0.0D, 0.0D);
            if (this == GCBlocks.unlitTorchLit)
            {
                worldIn.addParticle(ParticleTypes.FLAME, d0 + d4 * (double) enumfacing1.getXOffset(), d1 + d3, d2 + d4 * (double) enumfacing1.getZOffset(), 0.0D, 0.0D, 0.0D);
            }
        }
        else
        {
            worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            if (this == GCBlocks.unlitTorchLit)
            {
                worldIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onOxygenRemoved(World world, BlockPos pos, BlockState state)
    {
        if (world.getDimension() instanceof IGalacticraftDimension)
        {
            Direction enumfacing = state.get(FACING);
            world.setBlockState(pos, this.unlitVersion.getDefaultState().with(FACING, enumfacing), 2);
        }
    }

    @Override
    public void onOxygenAdded(World world, BlockPos pos, BlockState state)
    {
        if (world.getDimension() instanceof IGalacticraftDimension)
        {
            Direction enumfacing = state.get(FACING);
            world.setBlockState(pos, this.litVersion.getDefaultState().with(FACING, enumfacing), 2);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        return this.litVersion.getDrops(state, builder);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
