package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.BlockAir;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBrightAir extends BlockAir
{
    public BlockBrightAir(String assetName)
    {
        this.setResistance(1000.0F);
        this.setHardness(0.0F);
        this.setUnlocalizedName(assetName);
        this.setLightLevel(1.0F);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.AIR);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return false;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return 15;
    }
    
    @Override
    public int getLightOpacity(IBlockState state)
    {
        return 0;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos vec, IBlockState state)
    {
    }
}
