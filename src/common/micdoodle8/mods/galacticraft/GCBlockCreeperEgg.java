package micdoodle8.mods.galacticraft;

import java.util.Random;

import net.minecraft.src.BlockDragonEgg;
import net.minecraft.src.BlockSand;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityPlayer;
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
    
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	
    }
    
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        return false;
    }
    
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
    	
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
