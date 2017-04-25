package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityThruster;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSpinThruster extends BlockAdvanced implements IShiftDescription, ITileEntityProvider, ISortableBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockSpinThruster(String assetName)
    {
        super(Material.circuits);
        this.setHardness(0.1F);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return world.isSideSolid(pos.west(), EnumFacing.EAST, true) || world.isSideSolid(pos.east(), EnumFacing.WEST, true) || world.isSideSolid(pos.north(), EnumFacing.SOUTH, true) || world.isSideSolid(pos.south(), EnumFacing.NORTH, true);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (facing.getAxis().isHorizontal() && this.canBlockStay(world, pos, facing))
        {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        else
        {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (this.canBlockStay(world, pos, enumfacing))
                {
                    return this.getDefaultState().withProperty(FACING, enumfacing);
                }
            }
            return this.getDefaultState();
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (this.getMetaFromState(state) == 0)
        {
            this.onBlockAdded(worldIn, pos, state);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        int metadata = this.getMetaFromState(state);

        BlockPos baseBlock;
        switch (metadata)
        {
        case 1:
            baseBlock = pos.offset(EnumFacing.WEST);
            break;
        case 2:
            baseBlock = pos.offset(EnumFacing.EAST);
            break;
        case 3:
            baseBlock = pos.offset(EnumFacing.NORTH);
            break;
        case 4:
            baseBlock = pos.offset(EnumFacing.SOUTH);
            break;
        default:
            return;
        }

        if (!worldIn.isRemote)
        {
            if (worldIn.provider instanceof WorldProviderSpaceStation)
            {
                ((WorldProviderSpaceStation) worldIn.provider).getSpinManager().refresh(baseBlock, true);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        EnumFacing enumfacing = state.getValue(FACING);

        if (!this.canBlockStay(world, pos, enumfacing))
        {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
        if (!world.isRemote)
        {
            if (world.provider instanceof WorldProviderSpaceStation)
            {
                ((WorldProviderSpaceStation) world.provider).getSpinManager().refresh(pos, true);
            }
        }
    }

    protected boolean canBlockStay(World world, BlockPos pos, EnumFacing facing)
    {
        return world.isSideSolid(pos.offset(facing.getOpposite()), facing, true);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        float var8 = 0.3F;

        EnumFacing facing = worldIn.getBlockState(pos).getValue(BlockMachine.FACING);

        switch (facing)
        {
        case NORTH:
            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
            break;
        case EAST:
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
            break;
        case SOUTH:
            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
            break;
        case WEST:
            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
            break;
        default:
            break;
        }

        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        //TODO this is torch code as a placeholder, still need to adjust positioning and particle type
        //Also make small thrust sounds
        if (worldIn.provider instanceof WorldProviderSpaceStation)
        {
            if (((WorldProviderSpaceStation) worldIn.provider).getSpinManager().thrustersFiring || rand.nextInt(80) == 0)
            {
                final int var6 = this.getMetaFromState(state) & 7;
                final double var7 = pos.getX() + 0.5F;
                final double var9 = pos.getY() + 0.7F;
                final double var11 = pos.getZ() + 0.5F;
                final double var13 = 0.2199999988079071D;
                final double var15 = 0.27000001072883606D;

                if (var6 == 1)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 2)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 3)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 4)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        EnumFacing currentFacing = world.getBlockState(pos).getValue(FACING);
        for (EnumFacing nextFacing = currentFacing.rotateY(); ; nextFacing = nextFacing.rotateY())
        {
            if (this.canBlockStay(world, pos.offset(nextFacing.getOpposite()), nextFacing))
            {
                world.setBlockState(pos, this.getStateFromMeta(nextFacing.getHorizontalIndex()), 2);
                break;
            }

            if (nextFacing == currentFacing)
            {
                break;
            }
        }

        if (world.provider instanceof WorldProviderSpaceStation && !world.isRemote)
        {
            WorldProviderSpaceStation worldOrbital = (WorldProviderSpaceStation) world.provider;
            worldOrbital.getSpinManager().refresh(pos, true);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityThruster();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            final int facing = this.getMetaFromState(state) & 8;
            if (worldIn.provider instanceof WorldProviderSpaceStation)
            {
                WorldProviderSpaceStation worldOrbital = (WorldProviderSpaceStation) worldIn.provider;
                worldOrbital.getSpinManager().removeThruster(pos, facing == 0);
                worldOrbital.getSpinManager().updateSpinSpeed();
            }
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, FACING);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
