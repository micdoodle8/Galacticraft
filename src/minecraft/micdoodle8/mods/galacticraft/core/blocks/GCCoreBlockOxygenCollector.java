package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockOxygenCollector extends BlockContainer
{
	public GCCoreBlockOxygenCollector(int par1, int par2)
	{
		super(par1, par2, Material.rock);
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
		return GalacticraftCore.proxy.getGCOxygenCollectorRenderID();
   	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new GCCoreTileEntityOxygenCollector();
	}

	@Override
    @SideOnly(Side.CLIENT)
    public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
		boolean active = false;
		
		if (par1IBlockAccess.getBlockTileEntity(par2, par3, par4) instanceof GCCoreTileEntityOxygenCollector)
		{
			if (((GCCoreTileEntityOxygenCollector)par1IBlockAccess.getBlockTileEntity(par2, par3, par4)).currentPower > 1)
			{
				active = true;
			}
		}
		
        if (par5 == 1)
        {
            return this.blockIndexInTexture;
        }
        else if (par5 == 0)
        {
            return this.blockIndexInTexture + 1;
        }
        else
        {
            final int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
            return par5 != var6 && par5 != (var6 == 5 || var6 == 3 ? var6 - 1 : var6 + 1) ? this.blockIndexInTexture + 2 : active ? this.blockIndexInTexture - 1 : this.blockIndexInTexture - 2;
        }
    }

	@Override
    public int getBlockTextureFromSide(int par1)
    {
        return par1 == 1 ? this.blockIndexInTexture : par1 == 0 ? this.blockIndexInTexture + 2 : par1 == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture + 2;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
		if (par1World.getBlockTileEntity(par2, par3, par4) instanceof GCCoreTileEntityOxygenCollector)
		{
			if (((GCCoreTileEntityOxygenCollector)par1World.getBlockTileEntity(par2, par3, par4)).currentPower > 1)
			{
				for (int var6 = 0; var6 < 10; ++var6)
		        {
		            double var7 = par2 + par5Random.nextFloat();
		            final double var9 = par3 + par5Random.nextFloat();
		            double var11 = par4 + par5Random.nextFloat();
		            double var13 = 0.0D;
		            double var15 = 0.0D;
		            double var17 = 0.0D;
		            final int var19 = par5Random.nextInt(2) * 2 - 1;
		            var13 = (par5Random.nextFloat() - 0.5D) * 0.5D;
		            var15 = (par5Random.nextFloat() - 0.5D) * 0.5D;
		            var17 = (par5Random.nextFloat() - 0.5D) * 0.5D;

		            final int var2 = par1World.getBlockMetadata(par2, par3, par4);

		            if (var2 == 3 || var2 == 2)
		            {
		                var7 = par2 + 0.5D + 0.25D * var19;
		                var13 = par5Random.nextFloat() * 2.0F * var19;
		            }
		            else
		            {
		                var11 = par4 + 0.5D + 0.25D * var19;
		                var17 = par5Random.nextFloat() * 2.0F * var19;
		            }

		           	GalacticraftCore.proxy.spawnParticle("oxygen", var7, var9, var11, var13, var15, var17, 0.7D, 0.7D, 1.0D, false);
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
