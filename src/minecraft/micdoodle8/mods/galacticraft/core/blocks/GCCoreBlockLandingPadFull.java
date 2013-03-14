package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.block.BlockAdvanced;
import universalelectricity.prefab.multiblock.TileEntityMulti;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreBlockLandingPadFull extends BlockAdvanced
{
	public GCCoreBlockLandingPadFull(int i)
	{
		super(i, Material.rock);
        this.setBlockBounds(-1.0F, 0.0F, -1.0F, 2.0F, 0.2F, 2.0F);
	}

	@Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getAABBPool().getAABB((double)par2 - 1.0, (double)par3 + 0.0, (double)par4 - 1.0, (double)par2 + 2.0, (double)par3 + 0.21, (double)par4 + 2.0);
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) 
    {
        this.setBlockBounds(-1.0F, 0.0F, -1.0F, 2.0F, 0.2F, 2.0F);
    }

	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock)
	{
		worldObj.setBlockAndMetadataWithNotify(position.intX(), position.intY(), position.intZ(), this.blockID, 0, 3);
		((TileEntityMulti) worldObj.getBlockTileEntity(position.intX(), position.intY(), position.intZ())).setMainBlock(mainBlock);
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getGCFullLandingPadRenderID();
	}

    @Override
    public void func_94332_a(IconRegister par1IconRegister)
    {
    	this.field_94336_cN = par1IconRegister.func_94245_a("galacticraftcore:launch_pad");
    }
    
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		TileEntity tile = (world.getBlockTileEntity(x, y, z));
		
		if (tile != null)
		{
			Vector3 tileVec = new Vector3(tile);
			
			if (tile instanceof GCCoreTileEntityLandingPad)
			{
				FMLLog.info("" + tileVec);
				GCCoreTileEntityLandingPad pad = (GCCoreTileEntityLandingPad) tile;
			}
		}
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
	public TileEntity createTileEntity(World world, int meta)
	{
		return new GCCoreTileEntityLandingPad();
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
