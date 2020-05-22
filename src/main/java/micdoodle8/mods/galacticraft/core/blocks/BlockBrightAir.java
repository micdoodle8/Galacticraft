package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBrightAir extends AirBlock
{
    public BlockBrightAir(Properties builder)
    {
        super(builder);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public PushReaction getMobilityFlag(BlockState state)
    {
        return PushReaction.DESTROY;
    }

    @Override
    public Item getItemDropped(BlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.AIR);
    }

    @Override
    public boolean shouldSideBeRendered(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side)
    {
        return false;
    }

    @Override
    public int getLightValue(BlockState state, IBlockAccess world, BlockPos pos)
    {
        return 15;
    }
    
    @Override
    public int getLightOpacity(BlockState state)
    {
        return 0;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos vec, BlockState state)
    {
    }
}
