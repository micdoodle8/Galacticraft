package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class GCCoreBlockFallenMeteor extends GCCoreBlock
{
	public GCCoreBlockFallenMeteor(int i) 
	{
		super(i, 0, Material.rock);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
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
		        livingEntity.setFire(5);
	        }
		}
    }
}
