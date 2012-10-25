package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
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
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
		if (par5Entity instanceof EntityLiving)
		{
			EntityLiving livingEntity = (EntityLiving) par5Entity;
			
	        par1World.playSoundEffect((double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

	        for (int var5 = 0; var5 < 8; ++var5)
	        {
	            par1World.spawnParticle("largesmoke", (double)par2 + Math.random(), (double)par3 + 0.2D + Math.random(), (double)par4 + Math.random(), 0.0D, 0.0D, 0.0D);
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
}
