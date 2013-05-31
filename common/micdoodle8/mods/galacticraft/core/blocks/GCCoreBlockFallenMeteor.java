package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class GCCoreBlockFallenMeteor extends Block
{
    public GCCoreBlockFallenMeteor(int i)
    {
        super(i, Material.rock);
        this.setBlockBounds(0.2F, 0.2F, 0.2F, 0.8F, 0.8F, 0.8F);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("galacticraftcore:fallen_meteor");
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
        return 1;
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return GCMoonItems.meteoricIronRaw.itemID;
    }

    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (par5Entity instanceof EntityLiving)
        {
            final EntityLiving livingEntity = (EntityLiving) par5Entity;

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
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
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
        if (GCCoreBlockFallenMeteor.canFallBelow(par1World, par2, par3 - 1, par4) && par3 >= 0)
        {
            par1World.setBlock(par2, par3, par4, 0, 0, 3);

            while (GCCoreBlockFallenMeteor.canFallBelow(par1World, par2, par3 - 1, par4) && par3 > 0)
            {
                --par3;
            }

            if (par3 > 0)
            {
                par1World.setBlock(par2, par3, par4, this.blockID, 0, 3);
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
}
