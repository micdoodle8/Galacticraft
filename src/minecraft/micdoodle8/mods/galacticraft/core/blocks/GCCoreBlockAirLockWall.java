package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.IPartialSealedBlock;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLock;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockAirLockWall extends BlockBreakable implements IPartialSealedBlock
{
    public GCCoreBlockAirLockWall(int par1)
    {
        super(par1, "galacticraftcore:oxygentile_3", Material.portal, false);
        this.setTickRandomly(true);
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
    	this.blockIcon = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_4");
    }

    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        float var5;
        float var6;

        if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) != GCCoreBlocks.airLockFrame.blockID && par1IBlockAccess.getBlockId(par2 + 1, par3, par4) != GCCoreBlocks.airLockFrame.blockID && par1IBlockAccess.getBlockId(par2 - 1, par3, par4) != GCCoreBlocks.airLockSeal.blockID && par1IBlockAccess.getBlockId(par2 + 1, par3, par4) != GCCoreBlocks.airLockSeal.blockID)
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

	@Override
	public boolean isSealed(World world, int x, int y, int z)
	{
		return true;
	}
}
