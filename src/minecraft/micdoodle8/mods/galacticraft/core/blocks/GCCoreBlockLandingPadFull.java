package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.multiblock.IMultiBlock;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreBlockLandingPadFull extends BlockAdvanced
{
	private Icon[] icons = new Icon[3];
	
	public GCCoreBlockLandingPadFull(int i)
	{
		super(i, Material.rock);
	}

	@Override
    public int quantityDropped(Random par1Random)
    {
        return 9;
    }

	@Override
    public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6)
    {
        final TileEntity var9 = var1.getBlockTileEntity(var2, var3, var4);

        if (var9 instanceof IMultiBlock)
        {
            ((IMultiBlock)var9).onDestroy(var9);
        }

        super.breakBlock(var1, var2, var3, var4, var5, var6);
    }

	@Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return GCCoreBlocks.landingPad.blockID;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
    	switch (world.getBlockMetadata(x, y, z))
    	{
    	case 0:
            return AxisAlignedBB.getAABBPool().getAABB((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
    	default:
            return AxisAlignedBB.getAABBPool().getAABB((double)x + 0.0D, (double)y + 0.0D, (double)z + 0.0D, (double)x + 1.0D, (double)y + 0.2D, (double)z + 1.0D);
    	}
    }
    
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
    	switch (world.getBlockMetadata(x, y, z))
    	{
    	case 0:
            return AxisAlignedBB.getAABBPool().getAABB((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
    	default:
            return AxisAlignedBB.getAABBPool().getAABB((double)x + 0.0D, (double)y + 0.0D, (double)z + 0.0D, (double)x + 1.0D, (double)y + 0.2D, (double)z + 1.0D);
    	}
    }

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getGCFullLandingPadRenderID();
	}

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
    	icons[0] = par1IconRegister.registerIcon("galacticraftcore:launch_pad");
    	icons[1] = par1IconRegister.registerIcon("galacticraftcore:buggy_fueler");
    	icons[2] = par1IconRegister.registerIcon("galacticraftcore:buggy_fueler_blank");
    	this.blockIcon = par1IconRegister.registerIcon("galacticraftcore:launch_pad");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
    	switch (par2)
    	{
    	case 0:
    		return this.icons[0];
    	case 1:
    		return this.icons[1];
    	case 2:
    		return this.icons[2];
    	}
    	
        return this.blockIcon;
    }

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		for (int x2 = -1; x2 < 2; ++x2)
		{
			for (int z2 = -1; z2 < 2; ++z2)
			{
				if (!super.canPlaceBlockAt(world, x + x2, y, z + z2))
				{
					return false;
				}
			}

		}

		return true;
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		switch (metadata)
		{
		case 0:
			return new GCCoreTileEntityLandingPad();
		default:
			return new GCCoreTileEntityBuggyFueler();
		}
	}

	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
		par1World.markBlockForUpdate(par2, par3, par4);
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
}
