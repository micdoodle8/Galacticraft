package micdoodle8.mods.galacticraft;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlockGrass extends Block
{
	public GCBlockGrass(int par1, int par2) 
	{
		super(par1, par2, Material.grass);
		this.setTickRandomly(true);
		this.setStepSound(Block.soundGrassFootstep);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public int getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side) 
	{
		return side == 1 ? 4 : (side == 0 ? 5 : 3);
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) 
	{
		if (!par1World.isRemote) 
		{
			if (par1World.getBlockLightValue(par2, par3 + 1, par4) < 4 && Block.lightOpacity[par1World.getBlockId(par2, par3 + 1, par4)] > 2)
			{
				par1World.setBlockWithNotify(par2, par3, par4, GCBlocks.marsDirt.blockID);
			} 
			else if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9) 
			{
				for (int var6 = 0; var6 < 4; ++var6) 
				{
					int var7 = par2 + par5Random.nextInt(3) - 1;
					int var8 = par3 + par5Random.nextInt(5) - 3;
					int var9 = par4 + par5Random.nextInt(3) - 1;
					
					int var10 = par1World.getBlockId(var7, var8 + 1, var9);

					if (par1World.getBlockId(var7, var8, var9) == GCBlocks.marsDirt.blockID	&& par1World.getBlockLightValue(var7, var8 + 1, var9) >= 4 && Block.lightOpacity[var10] <= 2) 
					{
						par1World.setBlockWithNotify(var7, var8, var9, this.blockID);
					}
				}
			}
		}
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) 
	{
		return GCBlocks.marsDirt.blockID;
	}

	@Override
    public String getTextureFile()
    {
		return "/micdoodle8/mods/galacticraft/client/blocks/mars.png";
    }
}
