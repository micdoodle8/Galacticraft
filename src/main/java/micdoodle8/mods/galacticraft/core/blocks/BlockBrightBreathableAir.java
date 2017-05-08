package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.fluid.OxygenPressureProtocol;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
        this.setStepSound(new SoundType("sand", 0.0F, 1.0F));
        this.setLightLevel(1.0F);
    }

    @Override
    public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public int getMobilityFlag()
    {
        return 1;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.air);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        final Block block = worldIn.getBlockState(pos).getBlock();
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
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (Blocks.air == neighborBlock)
        //Do no check if replacing breatheableAir with a solid block, although that could be dividing a sealed space
        {
            OxygenPressureProtocol.onEdgeBlockUpdated(worldIn, pos);
        }
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, THERMAL);
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
    public int getLightValue(IBlockAccess world, BlockPos pos)
    {
        return 15;
    }
    
    @Override
    public int getLightOpacity()
    {
        return 0;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos vec, IBlockState state)
    {
    }
}
