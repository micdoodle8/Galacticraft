package micdoodle8.mods.galacticraft.core;

import java.util.Random;

import micdoodle8.mods.galacticraft.moon.GCMoonItems;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class GCCoreBlockFallenMeteor extends GCCoreBlock
{
	public GCCoreBlockFallenMeteor(int i) 
	{
		super(i, 21, Material.rock);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
		this.setBlockBounds(0.2F, 0.2F, 0.2F, 0.8F, 0.8F, 0.8F);
	}
	
	@Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCMeteorRenderID();
    }

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
    public int quantityDroppedWithBonus(int par1, Random par2Random)
    {
        return par2Random.nextInt(5) == 0 ? 1 : 0;
    }

	@Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return GCMoonItems.meteoricIronRaw.shiftedIndex;
    }

	@Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
		if (par5Entity instanceof EntityLiving)
		{
			EntityLiving livingEntity = (EntityLiving) par5Entity;
			
	        par1World.playSoundEffect(par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

	        for (int var5 = 0; var5 < 8; ++var5)
	        {
	            par1World.spawnParticle("largesmoke", par2 + Math.random(), par3 + 0.2D + Math.random(), par4 + Math.random(), 0.0D, 0.0D, 0.0D);
	        }
	        
	        if (!livingEntity.isBurning())
	        {
		        livingEntity.setFire(2);
	        }
	        
	        double var9 = livingEntity.posX - par2;
            double var7;

            for (var7 = livingEntity.posZ - par4; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D)
            {
                var9 = (Math.random() - Math.random()) * 0.01D;
            }

            livingEntity.knockBack(livingEntity, 1, var9 / 2, var7 / 2);
		}
    }

	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
    }

	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
    }

	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            this.tryToFall(par1World, par2, par3, par4);
        }
    }
	
    private void tryToFall(World par1World, int par2, int par3, int par4)
    {
        if (canFallBelow(par1World, par2, par3 - 1, par4) && par3 >= 0)
        {
            byte var8 = 32;
            
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
        int var4 = par0World.getBlockId(par1, par2, par3);

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
            Material var5 = Block.blocksList[var4].blockMaterial;
            return var5 == Material.water ? true : var5 == Material.lava;
        }
    }
}
