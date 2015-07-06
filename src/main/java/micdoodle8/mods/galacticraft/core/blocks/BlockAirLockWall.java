package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockAirLockWall extends BlockBreakable implements IPartialSealableBlock
{
    public BlockAirLockWall(String assetName)
    {
        super(/*GalacticraftCore.TEXTURE_PREFIX + "oxygentile_3", */Material.iron, false);
        this.setTickRandomly(true);
        this.setHardness(1000.0F);
        this.setStepSound(Block.soundTypeMetal);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "deco_aluminium_4");
    }*/

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        float var5;
        float var6;

        Block frameID = GCBlocks.airLockFrame;
        Block sealID = GCBlocks.airLockSeal;

        Block idXMin = worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock();
        Block idXMax = worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock();

        if (idXMin != frameID && idXMax != frameID && idXMin != sealID && idXMax != sealID)
        {
            var5 = 0.25F;
            var6 = 0.5F;
            this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
        }
        else
        {
            int adjacentCount = 0;

            for (EnumFacing dir : EnumFacing.values())
            {
                if (dir != EnumFacing.UP && dir != EnumFacing.DOWN)
                {
                    Block blockID = worldIn.getBlockState(pos.offset(dir)).getBlock();

                    if (blockID == GCBlocks.airLockFrame || blockID == GCBlocks.airLockSeal)
                    {
                        adjacentCount++;
                    }
                }
            }

            if (adjacentCount == 4)
            {
                this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 0.75F, 1.0F);
            }
            else
            {
                var5 = 0.5F;
                var6 = 0.25F;
                this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
            }
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return true;
    }
    
    @Override
    public Item getItem(World world, BlockPos pos)
    {
		return null;
    }
}
