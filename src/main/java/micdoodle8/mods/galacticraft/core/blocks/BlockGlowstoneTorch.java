package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Random;

public class BlockGlowstoneTorch extends Block implements ItemBlockDesc.IBlockShiftDesc
{
    protected BlockGlowstoneTorch(String assetName)
    {
        super(Material.circuits);
        this.setTickRandomly(true);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
        this.setLightLevel(0.85F);
        this.setStepSound(Block.soundTypeWood);
    }

    private static boolean isBlockSolidOnSide(World world, BlockPos pos, EnumFacing direction, boolean nope)
    {
        return world.getBlockState(pos).getBlock().isSideSolid(world, pos, direction);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
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
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    private boolean canPlaceTorchOn(World worldIn, BlockPos pos)
    {
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos))
        {
            return true;
        }
        else
        {
            Block l = worldIn.getBlockState(pos).getBlock();
            return l.canPlaceTorchOnTop(worldIn, pos);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return BlockGlowstoneTorch.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.WEST), EnumFacing.EAST, true)
                || BlockGlowstoneTorch.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.EAST), EnumFacing.WEST, true)
                || BlockGlowstoneTorch.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.NORTH), EnumFacing.SOUTH, true)
                || BlockGlowstoneTorch.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.SOUTH), EnumFacing.NORTH, true)
                || this.canPlaceTorchOn(worldIn, pos.offset(EnumFacing.DOWN));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        /*int j1 = par9;

        if (par5 == 1 && this.canPlaceTorchOn(par1World, par2, par3 - 1, par4))
        {
            j1 = 5;
        }

        if (par5 == 2 && BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2, par3, par4 + 1, NORTH, true))
        {
            j1 = 4;
        }

        if (par5 == 3 && BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2, par3, par4 - 1, SOUTH, true))
        {
            j1 = 3;
        }

        if (par5 == 4 && BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2 + 1, par3, par4, WEST, true))
        {
            j1 = 2;
        }

        if (par5 == 5 && BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2 - 1, par3, par4, EAST, true))
        {
            j1 = 1;
        }

        return j1;*/
        return getDefaultState();
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (getMetaFromState(state) == 0)
        {
            this.onBlockAdded(worldIn, pos, state);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        /*if (worldIn.getBlockMetadata(par2, par3, par4) == 0)
        {
            if (BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2 - 1, par3, par4, EAST, true))
            {
                worldIn.setBlockMetadataWithNotify(par2, par3, par4, 1, 2);
            }
            else if (BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2 + 1, par3, par4, WEST, true))
            {
                worldIn.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
            }
            else if (BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2, par3, par4 - 1, SOUTH, true))
            {
                worldIn.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
            }
            else if (BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2, par3, par4 + 1, NORTH, true))
            {
                worldIn.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
            }
            else if (this.canPlaceTorchOn(par1World, par2, par3 - 1, par4))
            {
                worldIn.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
            }
        }

        this.dropTorchIfCantStay(par1World, par2, par3, par4);*/
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        /*if (this.dropTorchIfCantStay(par1World, par2, par3, par4))
        {
            int i1 = par1World.getBlockMetadata(par2, par3, par4);
            boolean flag = false;

            if (!BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2 - 1, par3, par4, EAST, true) && i1 == 1)
            {
                flag = true;
            }

            if (!BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2 + 1, par3, par4, WEST, true) && i1 == 2)
            {
                flag = true;
            }

            if (!BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2, par3, par4 - 1, SOUTH, true) && i1 == 3)
            {
                flag = true;
            }

            if (!BlockGlowstoneTorch.isBlockSolidOnSide(par1World, par2, par3, par4 + 1, NORTH, true) && i1 == 4)
            {
                flag = true;
            }

            if (!this.canPlaceTorchOn(par1World, par2, par3 - 1, par4) && i1 == 5)
            {
                flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockToAir(par2, par3, par4);
            }
        }*/
    }

    protected boolean dropTorchIfCantStay(World worldIn, BlockPos pos)
    {
        if (!this.canPlaceBlockAt(worldIn, pos))
        {
            if (worldIn.getBlockState(pos).getBlock() == this)
            {
                this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        int l = getMetaFromState(worldIn.getBlockState(pos)) & 7;
        float f = 0.15F;

        if (l == 1)
        {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        }
        else if (l == 2)
        {
            this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        }
        else if (l == 3)
        {
            this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        }
        else if (l == 4)
        {
            this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
        else
        {
            f = 0.1F;
            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        }

        return super.collisionRayTrace(worldIn, pos, start, end);
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
}
