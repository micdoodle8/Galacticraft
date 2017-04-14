package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IOxygenReliantBlock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockUnlitTorch extends Block implements IOxygenReliantBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != EnumFacing.DOWN);

    public boolean lit;
    public Block litVersion;
    public Block unlitVersion;
    public Block fallback;

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
        GalacticraftCore.handler.registerTorchType(littorch, vanillatorch);
    }

    public Block changeState()
    {
        if (this.lit)
        {
            return this.litVersion;
        }
        else
        {
            return this.unlitVersion;
        }
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

    private boolean canPlaceTorchOn(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock().isSideSolid(state, world, pos, EnumFacing.UP))
        {
            return true;
        }
        else
        {
            return world.getBlockState(pos).getBlock().canPlaceTorchOnTop(state, world, pos);
        }
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

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
    {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        boolean flag = facing.getAxis().isHorizontal();
        return flag && worldIn.isSideSolid(blockpos, facing, true) || facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos);
    }

    private boolean canPlaceOn(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getBlock().isSideSolid(state, worldIn, pos, EnumFacing.UP))
        {
            return true;
        }
        else
        {
            Block block = worldIn.getBlockState(pos).getBlock();
            return block.canPlaceTorchOnTop(state, worldIn, pos);
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        if (this.canPlaceAt(world, pos, facing))
        {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        else
        {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (world.isSideSolid(pos.offset(enumfacing.getOpposite()), enumfacing, true))
                {
                    return this.getDefaultState().withProperty(FACING, enumfacing);
                }
            }

            return this.getDefaultState();
        }
    }

    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, (EnumFacing) state.getValue(FACING)))
        {
            return true;
        }
        else
        {
            if (worldIn.getBlockState(pos).getBlock() == this)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
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
            this.checkOxygen(worldIn, pos);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.checkForDrop(worldIn, pos, state))
        {
            this.checkOxygen(worldIn, pos);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
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
                this.checkOxygen(worldIn, pos);
            }
        }
    }

    private void checkOxygen(World world, BlockPos pos)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            if (OxygenUtil.checkTorchHasOxygen(world, pos))
            {
                this.onOxygenAdded(world, pos);
            }
            else
            {
                this.onOxygenRemoved(world, pos);
            }
        }
        else
        {
            EnumFacing enumfacing = (EnumFacing) world.getBlockState(pos).getValue(FACING);
            world.setBlockState(pos, this.fallback.getDefaultState().withProperty(FACING, enumfacing), 2);
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector
     * returning a ray trace hit. Args: world, x, y, z, startVec, endVec
     */
//    @Override
//    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
//    {
//        EnumFacing enumfacing = worldIn.getBlockState(pos).getValue(FACING);
//        float f = 0.15F;
//
//        if (enumfacing == EnumFacing.EAST)
//        {
//            this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
//        }
//        else if (enumfacing == EnumFacing.WEST)
//        {
//            this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
//        }
//        else if (enumfacing == EnumFacing.SOUTH)
//        {
//            this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
//        }
//        else if (enumfacing == EnumFacing.NORTH)
//        {
//            this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
//        }
//        else
//        {
//            f = 0.1F;
//            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
//        }
//
//        return super.collisionRayTrace(worldIn, pos, start, end);
//    }


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
    public void onOxygenRemoved(World world, BlockPos pos)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            EnumFacing enumfacing = (EnumFacing) world.getBlockState(pos).getValue(FACING);
            world.setBlockState(pos, this.unlitVersion.getDefaultState().withProperty(FACING, enumfacing), 2);
        }
    }

    @Override
    public void onOxygenAdded(World world, BlockPos pos)
    {
        if (world.provider instanceof IGalacticraftWorldProvider)
        {
            EnumFacing enumfacing = (EnumFacing) world.getBlockState(pos).getValue(FACING);
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
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta)
        {
        case 1:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST);
            break;
        case 2:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.WEST);
            break;
        case 3:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.SOUTH);
            break;
        case 4:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.NORTH);
            break;
        case 5:
        default:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.UP);
        }

        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        switch ((EnumFacing) state.getValue(FACING))
        {
        case EAST:
            i = i | 1;
            break;
        case WEST:
            i = i | 2;
            break;
        case SOUTH:
            i = i | 3;
            break;
        case NORTH:
            i = i | 4;
            break;
        case DOWN:
        case UP:
        default:
            i = i | 5;
        }

        return i;
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
