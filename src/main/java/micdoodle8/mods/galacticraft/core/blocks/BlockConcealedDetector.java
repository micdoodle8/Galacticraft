package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlayerDetector;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockConcealedDetector extends Block implements ISortableBlock, ITileEntityProvider
{
    public static final IntegerProperty VARIANT = IntegerProperty.create("var", 0, 1);
    public static final IntegerProperty FACING = IntegerProperty.create("facing", 0, 3);
    public static final BooleanProperty DETECTED = BooleanProperty.create("det");

    public BlockConcealedDetector(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(FACING, 0).with(VARIANT, 0).with(DETECTED, false));
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
    public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int damage, LivingEntity placer)
    {
        return this.getDefaultState().with(FACING, placer.getHorizontalFacing().getHorizontalIndex());
    }

    @Override
    public BlockState getStateFromMeta(int meta)
    {
        int facing = meta & 3;
        int var = (meta >> 2) & 1;
        return this.getDefaultState().with(FACING, Integer.valueOf(facing)).with(VARIANT, Integer.valueOf(var)).with(DETECTED, Boolean.valueOf(meta >= 8));
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        return state.get(FACING).intValue() + state.get(VARIANT).intValue() * 4 + (state.get(DETECTED) ? 8 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, VARIANT, DETECTED});
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
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityPlayerDetector();
    }
    
    @Override
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }

    @Override
    public int getWeakPower(BlockState state, IBlockAccess worldIn, BlockPos pos, Direction side)
    {
        if (worldIn instanceof World && RedstoneUtil.isBlockReceivingDirectRedstone((World) worldIn, pos))
            return 0;
            
        return worldIn.getBlockState(pos).getValue(DETECTED) ? 0 : 15;
    }

    public void updateState(World worldObj, BlockPos pos, boolean result)
    {
        BlockState bs = worldObj.getBlockState(pos);
        if (result != (boolean) bs.getValue(DETECTED))
        {
            worldObj.setBlockState(pos, bs.with(DETECTED, result), 3);
        }
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return false;
    }
}
