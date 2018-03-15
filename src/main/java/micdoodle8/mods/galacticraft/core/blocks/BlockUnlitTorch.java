package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BlockUnlitTorch extends BlockTorchBase implements IOxygenReliantBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != EnumFacing.DOWN);

    public boolean lit;
    public Block litVersion;
    public Block unlitVersion;
    public Block fallback;
    public static HashMap<Block, Block> registeredTorches = new HashMap<>();

    public BlockUnlitTorch(boolean lit, String assetName)
    {
        super(Material.CIRCUITS);
        this.setTickRandomly(true);
        this.lit = lit;
        this.setLightLevel(lit ? 0.9375F : 0.2F);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
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

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing : FACING.getAllowedValues())
        {
            if (this.canPlaceAt(worldIn, pos, enumfacing))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (state.getBlock().getMetaFromState(state) == 0)
        {
            this.onBlockAdded(worldIn, pos, state);
        }
        else
        {
            this.checkOxygen(worldIn, pos, state);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.checkForDrop(worldIn, pos, state))
        {
            this.checkOxygen(worldIn, pos, state);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (this.checkForDrop(worldIn, pos, state))
        {
            EnumFacing enumfacing = state.getValue(FACING);
            EnumFacing.Axis enumfacingAxis = enumfacing.getAxis();
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            boolean flag = false;

            if (enumfacingAxis.isHorizontal() && !worldIn.isSideSolid(pos.offset(enumfacing1), enumfacing, true))
            {
                flag = true;
            }
            else if (enumfacingAxis.isVertical() && !this.canPlaceOn(worldIn, pos.offset(enumfacing1)))
            {
                flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            else
            {
                this.checkOxygen(worldIn, pos, state);
            }
        }
    }

    private void checkOxygen(World world, BlockPos pos, IBlockState state)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
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
            EnumFacing enumfacing = state.getValue(FACING);
            world.setBlockState(pos, this.fallback.getDefaultState().withProperty(FACING, enumfacing), 2);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        EnumFacing enumfacing = stateIn.getValue(FACING);
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;
        double d3 = 0.22D;
        double d4 = 0.27D;

        if (enumfacing.getAxis().isHorizontal())
        {
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4 * (double) enumfacing1.getFrontOffsetX(), d1 + d3, d2 + d4 * (double) enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
            if (this == GCBlocks.unlitTorchLit)
            {
                worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4 * (double) enumfacing1.getFrontOffsetX(), d1 + d3, d2 + d4 * (double) enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
        else
        {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
            if (this == GCBlocks.unlitTorchLit)
            {
                worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }

    @Override
    public void onOxygenRemoved(World world, BlockPos pos, IBlockState state)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            EnumFacing enumfacing = state.getValue(FACING);
            world.setBlockState(pos, this.unlitVersion.getDefaultState().withProperty(FACING, enumfacing), 2);
        }
    }

    @Override
    public void onOxygenAdded(World world, BlockPos pos, IBlockState state)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            EnumFacing enumfacing = state.getValue(FACING);
            world.setBlockState(pos, this.litVersion.getDefaultState().withProperty(FACING, enumfacing), 2);
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this.litVersion));
        return ret;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
