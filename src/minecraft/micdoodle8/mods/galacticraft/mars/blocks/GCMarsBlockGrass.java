package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.API.IPlantableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsBlockGrass extends Block implements IPlantableBlock
{
	public GCMarsBlockGrass(int par1, int par2) 
	{
		super(par1, par2, Material.grass);
		this.setTickRandomly(true);
		this.setStepSound(Block.soundGrassFootstep);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

	@Override
    public int getBlockTextureFromSideAndMetadata(int side, int par2)
    {
		return side == 1 ? 2 : side == 0 ? 3 : 1;
    }
    
	@Override
	public int getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side) 
	{
		return side == 1 ? 2 : side == 0 ? 3 : 1;
	}

	@Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
    {
        final int plantID = plant.getPlantID(world, x, y + 1, z);
        
        if (plant instanceof BlockFlower)
        {
            return true;
        }
        
        return false;
    }

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) 
	{
		if (!par1World.isRemote) 
		{
			if (par1World.getBlockLightValue(par2, par3 + 1, par4) < 4 && Block.lightOpacity[par1World.getBlockId(par2, par3 + 1, par4)] > 2)
			{
				par1World.setBlockWithNotify(par2, par3, par4, GCMarsBlocks.marsDirt.blockID);
			} 
			else if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9) 
			{
				for (int var6 = 0; var6 < 4; ++var6) 
				{
					final int var7 = par2 + par5Random.nextInt(3) - 1;
					final int var8 = par3 + par5Random.nextInt(5) - 3;
					final int var9 = par4 + par5Random.nextInt(3) - 1;
					
					final int var10 = par1World.getBlockId(var7, var8 + 1, var9);

					if (par1World.getBlockId(var7, var8, var9) == GCMarsBlocks.marsDirt.blockID	&& par1World.getBlockLightValue(var7, var8 + 1, var9) >= 4 && Block.lightOpacity[var10] <= 2) 
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
		return GCMarsBlocks.marsDirt.blockID;
	}

	@Override
    public String getTextureFile()
    {
		return "/micdoodle8/mods/galacticraft/mars/client/blocks/mars.png";
    }

	@Override
	public int requiredLiquidBlocksNearby() 
	{
		return 4;
	}
}
