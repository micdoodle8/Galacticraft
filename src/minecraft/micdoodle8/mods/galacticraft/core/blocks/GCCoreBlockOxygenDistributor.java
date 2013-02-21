package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.IConnectableToPipe;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlockOxygenDistributor extends BlockContainer implements IConnectableToPipe
{
    private final Random distributorRand = new Random();
    
    private static boolean keepDistributorInventory = false;
	
	public GCCoreBlockOxygenDistributor(int par1, boolean isActive)
	{
		super(par1, Material.rock);
		this.blockIndexInTexture = 22;
		this.setRequiresSelfNotify();
	}

	@Override
	public boolean renderAsNormalBlock()
   	{
       	return false;
   	}

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getGCOxygenDistributorRenderID();
   	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new GCCoreTileEntityOxygenDistributor();
	}

    @Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        this.setDefaultDirection(par1World, par2, par3, par4);
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
}
