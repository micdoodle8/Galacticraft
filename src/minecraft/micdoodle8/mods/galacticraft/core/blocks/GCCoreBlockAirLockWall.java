package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLock;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockAirLockWall extends BlockBreakable
{
    public GCCoreBlockAirLockWall(int par1, int par2) 
    {
        super(par1, par2, Material.portal, false);
        this.setTickRandomly(true);
	}

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        float var5;
        float var6;

        if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) != this.blockID && par1IBlockAccess.getBlockId(par2 + 1, par3, par4) != this.blockID)
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
            par1World.setBlockWithNotify(par2, par3, par4, 0);
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
                boolean var10 = par1World.getBlockId(par2 - 1, par3, par4) == this.blockID || par1World.getBlockId(par2 + 1, par3, par4) == this.blockID;
                boolean var11 = par1World.getBlockId(par2, par3, par4 - 1) == this.blockID || par1World.getBlockId(par2, par3, par4 + 1) == this.blockID;

                TileEntity te = par1World.getBlockTileEntity(par2, var8 + var9, par4);
                
        		if (te instanceof GCCoreTileEntityAirLock && ((GCCoreTileEntityAirLock) te).otherAirLockBlocks.size() > 8)
        		{
        			
        		}
        		else
        		{
                    if (var10 && var11)
                    {
                        par1World.setBlockWithNotify(par2, par3, par4, 0);
                    }
                    else
                    {
                        if ((par1World.getBlockId(par2 + var6, par3, par4 + var7) != GCCoreBlocks.airLockFrame.blockID || par1World.getBlockId(par2 - var6, par3, par4 - var7) != this.blockID) && (par1World.getBlockId(par2 - var6, par3, par4 - var7) != GCCoreBlocks.airLockFrame.blockID || par1World.getBlockId(par2 + var6, par3, par4 + var7) != this.blockID))
                        {
                            par1World.setBlockWithNotify(par2, par3, par4, 0);
                        }
                    }
        		}
            }
            else
            {
                par1World.setBlock(par2, par3, par4, 0);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par1IBlockAccess.getBlockId(par2, par3, par4) == this.blockID)
        {
            return false;
        }
        else
        {
            boolean var6 = par1IBlockAccess.getBlockId(par2 - 1, par3, par4) == this.blockID && par1IBlockAccess.getBlockId(par2 - 2, par3, par4) != this.blockID;
            boolean var7 = par1IBlockAccess.getBlockId(par2 + 1, par3, par4) == this.blockID && par1IBlockAccess.getBlockId(par2 + 2, par3, par4) != this.blockID;
            boolean var8 = par1IBlockAccess.getBlockId(par2, par3, par4 - 1) == this.blockID && par1IBlockAccess.getBlockId(par2, par3, par4 - 2) != this.blockID;
            boolean var9 = par1IBlockAccess.getBlockId(par2, par3, par4 + 1) == this.blockID && par1IBlockAccess.getBlockId(par2, par3, par4 + 2) != this.blockID;
            boolean var10 = var6 || var7;
            boolean var11 = var8 || var9;
            return var10 && par5 == 4 ? true : (var10 && par5 == 5 ? true : (var11 && par5 == 2 ? true : var11 && par5 == 3));
        }
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
    
    @Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
	}
}
