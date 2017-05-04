package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.fluid.OxygenPressureProtocol;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBrightBreathableAir extends BlockAir
{
    public static final PropertyBool THERMAL = PropertyBool.create("thermal");
    
    public BlockBrightBreathableAir(String assetName)
    {
        this.setResistance(1000.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(THERMAL, false));
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
        final Block block = blockAccess.getBlockState(pos).getBlock();
        if (block == this || block == GCBlocks.breatheableAir)
        {
            return false;
        }
        else
        {
            return block instanceof BlockAir;
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (Blocks.AIR == blockIn)
        //Do no check if replacing breatheableAir with a solid block, although that could be dividing a sealed space
        {
            OxygenPressureProtocol.onEdgeBlockUpdated((World) worldIn, pos);
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, THERMAL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(THERMAL, meta % 2 == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(THERMAL) ? 1 : 0);
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
