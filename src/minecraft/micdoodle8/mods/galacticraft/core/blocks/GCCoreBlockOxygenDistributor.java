package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreBlockOxygenDistributor extends BlockContainer
{
    private final Random distributorRand = new Random();

    private static boolean keepDistributorInventory = false;

	public GCCoreBlockOxygenDistributor(int par1, boolean isActive)
	{
		super(par1, 22, Material.rock);
		this.setRequiresSelfNotify();
	}

    @Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
//    	for (int i = 0; i < ForgeDirection.values().length - 1; i++)
//    	{
//    		final TileEntity tile = world.getBlockTileEntity(x + ForgeDirection.getOrientation(i).offsetX, y + ForgeDirection.getOrientation(i).offsetY, z + ForgeDirection.getOrientation(i).offsetZ);
//    		final GCCoreTileEntityOxygenDistributor thisDistributor = (GCCoreTileEntityOxygenDistributor)world.getBlockTileEntity(x, y, z);
//
//    		if (tile != null && thisDistributor != null && tile instanceof GCCoreTileEntityBreathableAir)
//    		{
//    			final GCCoreTileEntityBreathableAir air = (GCCoreTileEntityBreathableAir)tile;
//
//				air.removeDistributor(thisDistributor);
//
//				for (final GCCoreTileEntityBreathableAir airTile : air.connectedAir)
//				{
//					airTile.removeDistributor(thisDistributor);
//				}
//    		}
//    	}
//
//    	for (int i = 0; i < ForgeDirection.values().length - 1; i++)
//    	{
//    		if (world.getBlockTileEntity(x, y, z) instanceof GCCoreTileEntityOxygenPipe)
//    		{
//        		final TileEntity tile = world.getBlockTileEntity(x + ForgeDirection.getOrientation(i).offsetX, y + ForgeDirection.getOrientation(i).offsetY, z + ForgeDirection.getOrientation(i).offsetZ);
//        		final GCCoreTileEntityOxygenPipe thisPipe = (GCCoreTileEntityOxygenPipe)world.getBlockTileEntity(x, y, z);
//
//        		if (tile != null && thisPipe != null && tile instanceof GCCoreTileEntityOxygenPipe)
//        		{
//        			final GCCoreTileEntityOxygenPipe pipe = (GCCoreTileEntityOxygenPipe)tile;
//
//    				pipe.setOxygenInPipe(0D);
//    				pipe.setZeroOxygen();
//        		}
//    		}
//    	}

    	super.breakBlock(world, x, y, z, par5, par6);
    }

	@Override
	public boolean renderAsNormalBlock()
   	{
       	return GCCoreConfigManager.disableFancyTileEntities ? true : false;
   	}

	@Override
    public boolean isOpaqueCube()
    {
        return GCCoreConfigManager.disableFancyTileEntities ? true : false;
    }

	@Override
	public int getRenderType()
	{
		return GCCoreConfigManager.disableFancyTileEntities ? 0 : GalacticraftCore.proxy.getGCOxygenDistributorRenderID();
   	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new GCCoreTileEntityOxygenDistributor();
	}

    private void setDefaultDirection(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            final int var5 = par1World.getBlockId(par2, par3, par4 - 1);
            final int var6 = par1World.getBlockId(par2, par3, par4 + 1);
            final int var7 = par1World.getBlockId(par2 - 1, par3, par4);
            final int var8 = par1World.getBlockId(par2 + 1, par3, par4);
            byte var9 = 3;

            if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6])
            {
                var9 = 3;
            }

            if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5])
            {
                var9 = 2;
            }

            if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8])
            {
                var9 = 5;
            }

            if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7])
            {
                var9 = 4;
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, var9);
        }
    }

//    @Override
//	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
//    {
//        par5EntityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiAirDistributor, par1World, par2, par3, par4);
//        return true;
//    }

    @Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        final int var6 = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (var6 == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2);
        }

        if (var6 == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5);
        }

        if (var6 == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3);
        }

        if (var6 == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4);
        }
    }

    public void removeAirBlocks(World world, int x, int y, int z)
    {
    	for (int j = -8; j <= 8; j++)
		{
			for (int i = -8; i <= 8; i++)
			{
				for (int k = -8; k <= 8; k++)
				{
					if (world.getBlockId(x + i, y + j, z + k) == GCCoreBlocks.unlitTorchLit.blockID)
					{
						final int meta = world.getBlockMetadata(x + i, y + j, z + k);
						world.setBlockAndMetadataWithNotify(x + i, y + j, z + k, GCCoreBlocks.unlitTorch.blockID, meta);
					}
				}
			}
		}
    }

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }

//	@Override
//	public boolean isConnectableOnSide(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side)
//	{
//		if (side != ForgeDirection.UP && side != ForgeDirection.DOWN)
//		{
//			return true;
//		}
//
//		return false;
//	}
}
