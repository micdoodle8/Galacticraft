package micdoodle8.mods.galacticraft.core;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.GCBlockBreathableAir;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlockOxygenDistributor extends BlockContainer
{
    private Random distributorRand = new Random();
    
	protected boolean isActive;
    
    private static boolean keepDistributorInventory = false;
	
	public GCCoreBlockOxygenDistributor(int par1, boolean isActive) 
	{
		super(par1, Material.rock);
		this.blockIndexInTexture = 8;
		this.isActive = isActive;
		this.setRequiresSelfNotify();
	}

	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
		return new GCCoreTileEntityOxygenDistributor();
	}

	@Override
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        int var3 = par2 & 12;
        int var4 = par2 & 3;
        return var3 == 0 && (par1 == 1 || par1 == 0) ? 21 : (var3 == 4 && (par1 == 5 || par1 == 4) ? 21 : (var3 == 8 && (par1 == 2 || par1 == 3) ? 21 : (var4 == 1 ? 116 : (var4 == 2 ? 117 : (var4 == 3 ? 153 : 20)))));
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        this.setDefaultDirection(par1World, par2, par3, par4);
    }

    private void setDefaultDirection(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            int var5 = par1World.getBlockId(par2, par3, par4 - 1);
            int var6 = par1World.getBlockId(par2, par3, par4 + 1);
            int var7 = par1World.getBlockId(par2 - 1, par3, par4);
            int var8 = par1World.getBlockId(par2 + 1, par3, par4);
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
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (this.isActive)
        {
            int var6 = par1World.getBlockMetadata(par2, par3, par4);
            float var7 = (float)par2 + 0.5F;
            float var8 = (float)par3 + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
            float var9 = (float)par4 + 0.5F;
            float var10 = 0.52F;
            float var11 = par5Random.nextFloat() * 0.6F - 0.3F;

            if (var6 == 4)
            {
                par1World.spawnParticle("smoke", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 5)
            {
                par1World.spawnParticle("smoke", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 2)
            {
                par1World.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 3)
            {
                par1World.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        par5EntityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiAirDistributor, par1World, par2, par3, par4);
        return true;
    }

    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        int var6 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

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

    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	removeAirBlocks(par1World, par2, par3, par4);
    	
    	if (!keepDistributorInventory)
    	{
    		GCCoreTileEntityOxygenDistributor var7 = (GCCoreTileEntityOxygenDistributor)par1World.getBlockTileEntity(par2, par3, par4);

            if (var7 != null)
            {
                for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
                {
                    ItemStack var9 = var7.getStackInSlot(var8);

                    if (var9 != null)
                    {
                        float var10 = this.distributorRand.nextFloat() * 0.8F + 0.1F;
                        float var11 = this.distributorRand.nextFloat() * 0.8F + 0.1F;
                        float var12 = this.distributorRand.nextFloat() * 0.8F + 0.1F;

                        while (var9.stackSize > 0)
                        {
                            int var13 = this.distributorRand.nextInt(21) + 10;

                            if (var13 > var9.stackSize)
                            {
                                var13 = var9.stackSize;
                            }

                            var9.stackSize -= var13;
                            EntityItem var14 = new EntityItem(par1World, (double)((float)par2 + var10), (double)((float)par3 + var11), (double)((float)par4 + var12), new ItemStack(var9.itemID, var13, var9.getItemDamage()));

                            if (var9.hasTagCompound())
                            {
                                var14.item.setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                            }

                            float var15 = 0.05F;
                            var14.motionX = (double)((float)this.distributorRand.nextGaussian() * var15);
                            var14.motionY = (double)((float)this.distributorRand.nextGaussian() * var15 + 0.2F);
                            var14.motionZ = (double)((float)this.distributorRand.nextGaussian() * var15);
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
					if (world.getBlockId(x + i, y + j, z + k) == GCCoreBlocks.breatheableAir.blockID)
					{
//						if (!GCBlockBreathableAir.activeDistributorNearby(world, x + i, y + j, z + k, true)) TODO
						{
//							world.setBlockWithNotify(x + i, y + j, z + k, 0);
						}
					}
					else if (world.getBlockId(x + i, y + j, z + k) == GCCoreBlocks.unlitTorchLit.blockID)
					{
						int meta = world.getBlockMetadata(x + i, y + j, z + k);
						world.setBlockAndMetadataWithNotify(x + i, y + j, z + k, GCCoreBlocks.unlitTorch.blockID, meta);
					}
				}
			}
		}
    }

    public static void updateDistributorState(boolean activate, World par1World, int x, int y, int z)
    {
        int var5 = par1World.getBlockMetadata(x, y, z);
        TileEntity var6 = par1World.getBlockTileEntity(x, y, z);
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
    
	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
