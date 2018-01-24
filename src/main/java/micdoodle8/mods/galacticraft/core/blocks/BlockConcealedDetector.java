package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlayerDetector;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConcealedDetector extends Block implements ISortableBlock, ITileEntityProvider
{
    public static final PropertyInteger VARIANT = PropertyInteger.create("var", 0, 1);
    public static final PropertyInteger FACING = PropertyInteger.create("facing", 0, 3);
    public static final PropertyBool DETECTED = PropertyBool.create("det");

    public BlockConcealedDetector(String assetName)
    {
        super(Material.IRON);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.METAL);
        this.blockResistance = 15F;
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, Integer.valueOf(0)).withProperty(VARIANT, Integer.valueOf(0)).withProperty(DETECTED, false));
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
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int damage, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getHorizontalIndex());
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        int facing = meta & 3;
        int var = (meta >> 2) & 1;
        return this.getDefaultState().withProperty(FACING, Integer.valueOf(facing)).withProperty(VARIANT, Integer.valueOf(var)).withProperty(DETECTED, Boolean.valueOf(meta >= 8));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).intValue() + state.getValue(VARIANT).intValue() * 4 + (state.getValue(DETECTED) ? 8 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, VARIANT, DETECTED});
    }

    @Override
    public int getLightOpacity(IBlockState state)
    {
        return 0;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityPlayerDetector();
    }
    
    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        if (worldIn instanceof World && RedstoneUtil.isBlockReceivingDirectRedstone((World) worldIn, pos))
            return 0;
            
        return worldIn.getBlockState(pos).getValue(DETECTED) ? 0 : 15;
    }

    public void updateState(World worldObj, BlockPos pos, boolean result)
    {
        IBlockState bs = worldObj.getBlockState(pos);
        if (result != (boolean) bs.getValue(DETECTED))
        {
            worldObj.setBlockState(pos, bs.withProperty(DETECTED, result), 3);
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
