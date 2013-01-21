package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlock extends Block
{
	/**
	 *  0: COPPER ORE
	 *  1: ALUMINUM ORE
	 *  2: TITANIUM ORE
	 *  3: COPPER BLOCK
	 *  4: ALUMINUM BLOCK
	 *  5: TITANIUM BLOCK
	 *  6: BREATHABLE AIR
	 *  7: LANDING PAD
	 *  8: OXYGEN PIPE
	 *  9: ROCKET BENCH
	 */
	protected GCCoreBlock(int i, int j) 
	{
		super(i, j, Material.rock);
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
        this.setRequiresSelfNotify();
	}

	@Override
    public int damageDropped(int meta)
    {
		switch (meta)
		{
		default:
			return meta;
		}
    }
	
//	@Override
//    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
//    {
//		switch (world.getBlockMetadata(x, y, z))
//		{
//    	case 0:
//    		
//    	case 1:
//    		
//    	case 2:
//
//    	case 3:
//
//    	case 4:
//
//    	case 5:
//
//    	case 6:
//
//    	case 7:
//
//    	case 8:
//
//    	case 9:
//		}
//    }

    @SideOnly(Side.CLIENT)
	@Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 1; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) 
	{
		switch (meta) 
		{
		case 0:
			return 1;
		case 1:
			return 0;
		case 2:
			return 2;
    	case 3:

    	case 4:

    	case 5:

    	case 6:

    	case 7:

    	case 8:

    	case 9:
    		
		default:
			return -1;
		}
	}
	
    @Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
    {
    	final int meta = par1World.getBlockMetadata(par2, par3, par4);
    	
    	switch (meta)
    	{
    	case 0:
    		
    	case 1:
    		
    	case 2:

    	case 3:

    	case 4:

    	case 5:

    	case 6:

    	case 7:

    	case 8:

    	case 9:

    	case 10:

    	case 11:

    	case 12:

    	case 13:

    	case 14:

    	case 15:

    	case 16:
    		
    	}
    	
        return this.blockHardness;
    }
	
	@Override
    public int getRenderType()
    {
////		if (this.blockID == GCCoreBlocks.fallenMeteor.blockID)
//		{
//	        return GalacticraftCore.proxy.getGCMeteorRenderID();
//		}
//		else 
		{
			return super.getRenderType();
		}
    }

	@Override
    public boolean isOpaqueCube()
    {
//		if (this.blockID == GCCoreBlocks.fallenMeteor.blockID)
//		{
//			return false;
//		}
//		else
		{
			return super.isOpaqueCube();
		}
    }
	
	@Override
    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
//		if (this.blockID == GCCoreBlocks.fallenMeteor.blockID)
//		{
//			return par2Random.nextInt(5) == 0 ? 1 : 0;
//		}
//		else
		{
			return super.quantityDroppedWithBonus(par1, par2Random);
		}
    }

	@Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
//		if (this.blockID == GCCoreBlocks.fallenMeteor.blockID)
//		{
//	        return GCMoonItems.meteoricIronRaw.itemID;
//		}
//		else
		{
			return super.idDropped(par1, par2Random, par3);
		}
    }

	@Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
//		if (this.blockID == GCCoreBlocks.fallenMeteor.blockID)
//		{
//			if (par5Entity instanceof EntityLiving)
//			{
//				EntityLiving livingEntity = (EntityLiving) par5Entity;
//				
//		        par1World.playSoundEffect(par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);
//
//		        for (int var5 = 0; var5 < 8; ++var5)
//		        {
//		            par1World.spawnParticle("largesmoke", par2 + Math.random(), par3 + 0.2D + Math.random(), par4 + Math.random(), 0.0D, 0.0D, 0.0D);
//		        }
//		        
//		        if (!livingEntity.isBurning())
//		        {
//			        livingEntity.setFire(2);
//		        }
//		        
//		        double var9 = livingEntity.posX - par2;
//	            double var7;
//
//	            for (var7 = livingEntity.posZ - par4; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D)
//	            {
//	                var9 = (Math.random() - Math.random()) * 0.01D;
//	            }
//
//	            livingEntity.knockBack(livingEntity, 1, var9 / 2, var7 / 2);
//			}
//		}
//		else
		{
			super.onEntityCollidedWithBlock(par1World, par2, par3, par4, par5Entity);
		}
    }

	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
//		if (this.blockID == GCCoreBlocks.fallenMeteor.blockID)
//		{
//	        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
//		}
//		else
		{
			super.onBlockAdded(par1World, par2, par3, par4);
		}
    }

	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
//		if (this.blockID == GCCoreBlocks.fallenMeteor.blockID)
//		{
//	        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
//		}
//		else
		{
			super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
		}
    }

	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
//		if (this.blockID == GCCoreBlocks.fallenMeteor.blockID)
//		{
//	        if (!par1World.isRemote)
//	        {
//	            this.tryToFall(par1World, par2, par3, par4);
//	        }
//		}
//		else
		{
			super.updateTick(par1World, par2, par3, par4, par5Random);
		}
    }
	
    private void tryToFall(World par1World, int par2, int par3, int par4)
    {
        if (canFallBelow(par1World, par2, par3 - 1, par4) && par3 >= 0)
        {
            final byte var8 = 32;
            
            par1World.setBlockWithNotify(par2, par3, par4, 0);

            while (canFallBelow(par1World, par2, par3 - 1, par4) && par3 > 0)
            {
                --par3;
            }

            if (par3 > 0)
            {
                par1World.setBlockWithNotify(par2, par3, par4, this.blockID);
            }
        }
    }

    public static boolean canFallBelow(World par0World, int par1, int par2, int par3)
    {
        final int var4 = par0World.getBlockId(par1, par2, par3);

        if (var4 == 0)
        {
            return true;
        }
        else if (var4 == Block.fire.blockID)
        {
            return true;
        }
        else
        {
            final Material var5 = Block.blocksList[var4].blockMaterial;
            return var5 == Material.water ? true : var5 == Material.lava;
        }
    }

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }
}
