package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLock;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockAirLockWall extends BlockBreakable
{
    public GCCoreBlockAirLockWall(int par1)
    {
        super(par1, "galacticraftcore:oxygentile_3", Material.portal, false);
        this.setTickRandomly(true);
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
    	this.field_94336_cN = par1IconRegister.func_94245_a("galacticraftcore:deco_aluminium_4");
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        float var5;
        float var6;

        if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) != GCCoreBlocks.airLockFrame.blockID && par1IBlockAccess.getBlockId(par2 + 1, par3, par4) != GCCoreBlocks.airLockFrame.blockID)
        {
            var5 = 0.325F;
            var6 = 0.5F;
            this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
        }
        else
        {
            var5 = 0.5F;
            var6 = 0.325F;
            this.setBlockBounds(0.5F - var5, 0.0F, 0.5F - var6, 0.5F + var5, 1.0F, 0.5F + var6);
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        byte var6 = 0;
        byte var7 = 1;

        if (par1World.getBlockId(par2 - 1, par3, par4) == this.blockID || par1World.getBlockId(par2 + 1, par3, par4) == this.blockID)
        {
            var6 = 1;
            var7 = 0;
        }

        int var8;

        for (var8 = par3; par1World.getBlockId(par2, var8 - 1, par4) == this.blockID; --var8)
        {
            ;
        }

        if (par1World.getBlockId(par2, var8 - 1, par4) != GCCoreBlocks.airLockFrame.blockID)
        {
            par1World.setBlockAndMetadataWithNotify(par2, par3, par4, 0, 0, 3);
        }
        else
        {
            int var9;

            for (var9 = 1; var9 < 4 && par1World.getBlockId(par2, var8 + var9, par4) == this.blockID; ++var9)
            {
                ;
            }

            if (var9 == 2 && par1World.getBlockId(par2, var8 + var9, par4) == GCCoreBlocks.airLockFrame.blockID)
            {
                final boolean var10 = par1World.getBlockId(par2 - 1, par3, par4) == this.blockID || par1World.getBlockId(par2 + 1, par3, par4) == this.blockID;
                final boolean var11 = par1World.getBlockId(par2, par3, par4 - 1) == this.blockID || par1World.getBlockId(par2, par3, par4 + 1) == this.blockID;

                final TileEntity te = par1World.getBlockTileEntity(par2, var8 + var9, par4);

        		if (te instanceof GCCoreTileEntityAirLock && ((GCCoreTileEntityAirLock) te).otherAirLockBlocks.size() > 8)
        		{

        		}
        		else
        		{
                    if (var10 && var11)
                    {
                        par1World.setBlockAndMetadataWithNotify(par2, par3, par4, 0, 0, 3);
                    }
                    else
                    {
                        if ((par1World.getBlockId(par2 + var6, par3, par4 + var7) != GCCoreBlocks.airLockFrame.blockID || par1World.getBlockId(par2 - var6, par3, par4 - var7) != this.blockID) && (par1World.getBlockId(par2 - var6, par3, par4 - var7) != GCCoreBlocks.airLockFrame.blockID || par1World.getBlockId(par2 + var6, par3, par4 + var7) != this.blockID))
                        {
                            par1World.setBlockAndMetadataWithNotify(par2, par3, par4, 0, 0, 3);
                        }
                    }
        		}
            }
            else
            {
                par1World.setBlockAndMetadataWithNotify(par2, par3, par4, 0, 0, 3);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
    	return true;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}
