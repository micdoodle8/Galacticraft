package micdoodle8.mods.galacticraft.mars;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

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
            par1World.playSound((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "creepernest.singledrip", 0.5F, par5Random.nextFloat() * 0.4F + 0.8F);
        }
    }
}
