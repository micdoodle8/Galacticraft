package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsBlockCreeperDungeonWall extends GCMarsBlock
{
	public GCMarsBlockCreeperDungeonWall(int i, int j, Material material) 
	{
		super(i, j, material);
        this.setTickRandomly(true);
	}

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	if (par5Random.nextInt(70) == 0)
    	{
    		GalacticraftMars.proxy.spawnParticle("sludgeDrip", par2 + 0.5F, par3, par4 + 0.5F, 0, 0, 0, false);
    	}
    	
        if (par5Random.nextInt(2000) == 0)
        {
            par1World.playSoundEffect(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "creepernest.singledrip", 0.5F, par5Random.nextFloat() * 0.4F + 0.8F);
        }
    }
}
