package micdoodle8.mods.galacticraft.core;

import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import micdoodle8.mods.galacticraft.core.client.GCCoreEntityOxygenFX;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySpellParticleFX;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class GCCoreBlockOxygenCollector extends BlockContainer
{
	public GCCoreBlockOxygenCollector(int par1, int par2) 
	{
		super(par1, par2, Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
		return new GCCoreTileEntityOxygenCollector();
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

	@Override
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

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {

//        for (int var6 = 0; var6 < 4; ++var6)
//        {
////            double var7 = (double)((float)par2 + par5Random.nextFloat());
////            double var9 = (double)((float)par3 + par5Random.nextFloat());
////            double var11 = (double)((float)par4 + par5Random.nextFloat());
////            double var13 = 0.0D;
////            double var15 = 0.0D;
////            double var17 = 0.0D;
////            int var19 = par5Random.nextInt(2) * 2 - 1;
////            var13 = ((double)par5Random.nextFloat() - 0.5D) * 0.5D;
////            var15 = ((double)par5Random.nextFloat() - 0.5D) * 0.5D;
////            var17 = ((double)par5Random.nextFloat() - 0.5D) * 0.5D;
////
////            if (par1World.getBlockId(par2 - 1, par3, par4) != this.blockID && par1World.getBlockId(par2 + 1, par3, par4) != this.blockID)
////            {
////                var7 = (double)par2 + 0.5D + 0.25D * (double)var19;
////                var13 = (double)(par5Random.nextFloat() * 2.0F * (float)var19);
////            }
////            else
////            {
////                var11 = (double)par4 + 0.5D + 0.25D * (double)var19;
////                var17 = (double)(par5Random.nextFloat() * 2.0F * (float)var19);
////            }
//            
//            GalacticraftCore.proxy.spawnParticle("oxygen", par2 + 0.5D, par3 + 0.7D, par4 + 0.5D, var6 - par2 + par5Random.nextFloat() - 0.5D, par3 - par5Random.nextFloat() - 1.0F, var7 - par4 + par5Random.nextFloat() - 0.5D, 1D, 1D, 1D, false);
//        }

        for (int var6 = 0; var6 < 10; ++var6)
        {
            double var7 = (double)((float)par2 + par5Random.nextFloat());
            double var9 = (double)((float)par3 + par5Random.nextFloat());
            double var11 = (double)((float)par4 + par5Random.nextFloat());
            double var13 = 0.0D;
            double var15 = 0.0D;
            double var17 = 0.0D;
            int var19 = par5Random.nextInt(2) * 2 - 1;
            var13 = ((double)par5Random.nextFloat() - 0.5D) * 0.5D;
            var15 = ((double)par5Random.nextFloat() - 0.5D) * 0.5D;
            var17 = ((double)par5Random.nextFloat() - 0.5D) * 0.5D;

            if (par1World.getBlockId(par2 - 1, par3, par4) != this.blockID && par1World.getBlockId(par2 + 1, par3, par4) != this.blockID)
            {
                var7 = (double)par2 + 0.5D + 0.25D * (double)var19;
                var13 = (double)(par5Random.nextFloat() * 2.0F * (float)var19);
            }
            else
            {
                var11 = (double)par4 + 0.5D + 0.25D * (double)var19;
                var17 = (double)(par5Random.nextFloat() * 2.0F * (float)var19);
            }

           	GalacticraftCore.proxy.spawnParticle("oxygen", var7, var9, var11, var13, var15, var17, 0.7D, 0.7D, 1.0D, false);
        }
	}
    
	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
