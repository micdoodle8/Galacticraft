package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlockOxygenDistributor extends BlockContainer
{
    private final Random distributorRand = new Random();
    
	protected boolean isActive;
    
    private static boolean keepDistributorInventory = false;
	
	public GCCoreBlockOxygenDistributor(int par1, boolean isActive) 
	{
		super(par1, Material.rock);
		this.blockIndexInTexture = 22;
		this.isActive = isActive;
		this.setRequiresSelfNotify();
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
    
    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (this.isActive)
        {
            final int var6 = par1World.getBlockMetadata(par2, par3, par4);
            final float var7 = par2 + 0.5F;
            final float var8 = par3 + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
            final float var9 = par4 + 0.5F;
            final float var10 = 0.52F;
            final float var11 = par5Random.nextFloat() * 0.6F - 0.3F;

            if (var6 == 4)
            {
                par1World.spawnParticle("smoke", var7 - var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", var7 - var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 5)
            {
                par1World.spawnParticle("smoke", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 2)
            {
                par1World.spawnParticle("smoke", var7 + var11, var8, var9 - var10, 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", var7 + var11, var8, var9 - var10, 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 3)
            {
                par1World.spawnParticle("smoke", var7 + var11, var8, var9 + var10, 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", var7 + var11, var8, var9 + var10, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        par5EntityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiAirDistributor, par1World, par2, par3, par4);
        return true;
    }

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

    @Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	if (!keepDistributorInventory)
    	{
    		final GCCoreTileEntityOxygenDistributor var7 = (GCCoreTileEntityOxygenDistributor)par1World.getBlockTileEntity(par2, par3, par4);

            if (var7 != null)
            {
                for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
                {
                    final ItemStack var9 = var7.getStackInSlot(var8);

                    if (var9 != null)
                    {
                        final float var10 = this.distributorRand.nextFloat() * 0.8F + 0.1F;
                        final float var11 = this.distributorRand.nextFloat() * 0.8F + 0.1F;
                        final float var12 = this.distributorRand.nextFloat() * 0.8F + 0.1F;

                        while (var9.stackSize > 0)
                        {
                            int var13 = this.distributorRand.nextInt(21) + 10;

                            if (var13 > var9.stackSize)
                            {
                                var13 = var9.stackSize;
                            }

                            var9.stackSize -= var13;
                            final EntityItem var14 = new EntityItem(par1World, par2 + var10, par3 + var11, par4 + var12, new ItemStack(var9.itemID, var13, var9.getItemDamage()));

                            if (var9.hasTagCompound())
                            {
                                var14.func_92014_d().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                            }

                            final float var15 = 0.05F;
                            var14.motionX = (float)this.distributorRand.nextGaussian() * var15;
                            var14.motionY = (float)this.distributorRand.nextGaussian() * var15 + 0.2F;
                            var14.motionZ = (float)this.distributorRand.nextGaussian() * var15;
                            par1World.spawnEntityInWorld(var14);
                        }
                    }
                }
            }
    	}
    	
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
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

    public static void updateDistributorState(boolean activate, World par1World, int x, int y, int z)
    {
    	if (!par1World.isRemote)
    	{
    		final int var5 = par1World.getBlockMetadata(x, y, z);
            final TileEntity var6 = par1World.getBlockTileEntity(x, y, z);
            keepDistributorInventory = true;

            if (activate)
            {
                par1World.setBlockWithNotify(x, y, z, GCCoreBlocks.airDistributorActive.blockID);
            }
            else
            {
                par1World.setBlockWithNotify(x, y, z, GCCoreBlocks.airDistributor.blockID);
                GCCoreBlocks.airDistributor.removeAirBlocks(par1World, x, y, z);
            }

            keepDistributorInventory = false;
            par1World.setBlockMetadataWithNotify(x, y, z, var5);

            if (var6 != null)
            {
                var6.validate();
                par1World.setBlockTileEntity(x, y, z, var6);
            }
    	}
    }
    
	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
