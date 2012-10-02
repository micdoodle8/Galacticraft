package micdoodle8.mods.galacticraft;

import java.util.Random;

import net.minecraft.src.BlockDragonEgg;
import net.minecraft.src.BlockSand;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlockCreeperEgg extends BlockDragonEgg
{
    public GCBlockCreeperEgg(int par1, int par2)
    {
        super(par1, par2);
    }
    
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
    }

    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        this.fallIfPossible(par1World, par2, par3, par4);
    }

    private void fallIfPossible(World par1World, int par2, int par3, int par4)
    {
        if (BlockSand.canFallBelow(par1World, par2, par3 - 1, par4) && par3 >= 0)
        {
            byte var5 = 32;

            if (!BlockSand.fallInstantly && par1World.checkChunksExist(par2 - var5, par3 - var5, par4 - var5, par2 + var5, par3 + var5, par4 + var5))
            {
                EntityFallingSand var6 = new EntityFallingSand(par1World, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), this.blockID);
                par1World.spawnEntityInWorld(var6);
            }
            else
            {
                par1World.setBlockWithNotify(par2, par3, par4, 0);

                while (BlockSand.canFallBelow(par1World, par2, par3 - 1, par4) && par3 > 0)
                {
                    --par3;
                }

                if (par3 > 0)
                {
                    par1World.setBlockWithNotify(par2, par3, par4, this.blockID);
                }
            }
        }
    }

    public int tickRate()
    {
        return 3;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 27;
    }

    @SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return 0;
    }
    
    public String getTextureFile()
    {
		return "/micdoodle8/mods/galacticraft/client/blocks/mars.png";
    }
}
