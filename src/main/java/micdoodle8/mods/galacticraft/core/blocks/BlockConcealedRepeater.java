package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockConcealedRepeater extends RepeaterBlock implements ISortableBlock
{
    protected static final AxisAlignedBB CUBE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockConcealedRepeater(String assetName, boolean powered)
    {
        super(powered);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.METAL);
        this.blockResistance = 15F;
        this.setUnlocalizedName(assetName);
        this.setCreativeTab(powered ? null : GalacticraftCore.galacticraftBlocksTab);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
    {
        return CUBE_AABB;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
         return EnumSortCategoryBlock.DECORATION;
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
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(BlockState state, IBlockAccess worldIn, BlockPos pos, Direction side)
    {
        return true;
    }
  
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return true;
    }
    
    @Override
    protected BlockState getPoweredState(BlockState unpoweredState)
    {
        Integer integer = (Integer)unpoweredState.getValue(DELAY);
        Boolean obool = (Boolean)unpoweredState.getValue(LOCKED);
        Direction enumfacing = (Direction)unpoweredState.getValue(FACING);
        return GCBlocks.concealedRepeater_Powered.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }

    @Override
    protected BlockState getUnpoweredState(BlockState poweredState)
    {
        Integer integer = (Integer)poweredState.getValue(DELAY);
        Boolean obool = (Boolean)poweredState.getValue(LOCKED);
        Direction enumfacing = (Direction)poweredState.getValue(FACING);
        return GCBlocks.concealedRepeater_Unpowered.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }
    
    @Override
    public Item getItemDropped(BlockState state, Random rand, int fortune)
    {
        return BlockItem.getItemFromBlock(GCBlocks.concealedRepeater_Unpowered);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state)
    {
        return new ItemStack(GCBlocks.concealedRepeater_Unpowered);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    }
}
